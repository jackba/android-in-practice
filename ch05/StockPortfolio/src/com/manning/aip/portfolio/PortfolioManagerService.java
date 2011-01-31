package com.manning.aip.portfolio;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;
import android.widget.RemoteViews;

import com.manning.aip.portfolio.service.IStockService;

/**
 * Background <code>Service</code> used for managing the list of stocks in a
 * user's portfolio, periodically updating stock price information on those
 * stocks, and publishing the user if stock prices go too high or too low.
 * 
 * @author Michael Galpin
 *
 */
public class PortfolioManagerService extends Service {	
	
	private static final String TAG = "PortfolioManagerService";

	// This is a data access object used for persisting stock information.
	private StocksDb db;
	
	// Timestamp of last time stock data was downloaded from the Itnernet
	private long timestamp = 0L;
	
	// How old downloaded stock data can be and still be used
	private static final int MAX_CACHE_AGE = 15*60*1000; // 15 minutes
	
	// Types of Notifications
	private static final int HIGH_PRICE_NOTIFICATION = 1;
	private static final int LOW_PRICE_NOTIFICATION = 0;
	
	@Override
	public void onCreate() {
		super.onCreate();
		db = new StocksDb(this);
	}
	
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		if (db == null){
			db = new StocksDb(this);
		}
		try {
			updateStockData();
		} catch (IOException e) {
			Log.e(TAG, "Exception updating stock data", e);
		}
		return Service.START_STICKY;
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		db.close();
	}

	@Override
	public IBinder onBind(Intent intent) {
		if (db == null){
			db = new StocksDb(this);
		}
		// implement the IStockService interface defined in AIDL 
		return new IStockService.Stub() {
			public Stock addToPortfolio(Stock stock) throws RemoteException {
				Log.d(TAG, "Adding stock="+stock);
				Stock s = db.addStock(stock);
				Log.d(TAG, "Stock added to db");
				try {
					updateStockData();
					for (Stock x:db.getStocks()){
						if (x.getSymbol().equalsIgnoreCase(stock.getSymbol())){
							s = x;
						}
					}
					Log.d(TAG, "Stock data updated");
				} catch (IOException e) {
					Log.e(TAG, "Exception updating stock data", e);
					throw new RemoteException();
				}
				return s;
			}

			public List<Stock> getPortfolio() throws RemoteException {
				Log.d(TAG, "Getting portfolio");
				ArrayList<Stock> stocks = db.getStocks();
				long currTime = System.currentTimeMillis();
				if (currTime - timestamp <= MAX_CACHE_AGE){
					Log.d(TAG, "Fresh cache, returning it");
					return stocks;
				}
				// else cache is stale, refresh it
				Stock[] currStocks = new Stock[stocks.size()];
				stocks.toArray(currStocks);
				try {
					Log.d(TAG, "Stale cache, refreshing it");
					ArrayList<Stock> newStocks = fetchStockData(currStocks);
					Log.d(TAG, "Got new stock data, updating cache");
					updateCachedStocks(newStocks);
					return newStocks;
				} catch (Exception e) {
					Log.e(TAG, "Exception getting stock data",e);
					throw new RemoteException();
				}
			}
		};
	}
	
	private void updateStockData() throws IOException{
		ArrayList<Stock> stocks = db.getStocks();
		Stock[] currStocks = new Stock[stocks.size()];
		currStocks = stocks.toArray(currStocks);
		stocks = fetchStockData(currStocks);
		updateCachedStocks(stocks);
	}
	
	private void updateCachedStocks(ArrayList<Stock> stocks){
		Log.d(TAG, "Got new stock data to update cache with");
		timestamp = System.currentTimeMillis();
		Stock[] currStocks = new Stock[stocks.size()];
		currStocks = stocks.toArray(currStocks);
		for (Stock stock : currStocks){
			Log.d(TAG, "Updating cache with stock=" + stock.toString());
			db.updateStockPrice(stock);
		}
		Log.d(TAG, "Cache updated, checking for alerts");
		checkForAlerts(stocks);
	}
	
	private ArrayList<Stock> fetchStockData(Stock[] stocks) throws IOException{
		Log.d(TAG, "Fetching stock data from Yahoo");
		ArrayList<Stock> newStocks = new ArrayList<Stock>(stocks.length);
		if (stocks.length > 0){
			StringBuilder sb = new StringBuilder();
			for (Stock stock : stocks){
				sb.append(stock.getSymbol());
				sb.append('+');
			}
			sb.deleteCharAt(sb.length() - 1);
			String urlStr = 
				"http://finance.yahoo.com/d/quotes.csv?f=sb2n&s=" + 
					sb.toString();
			HttpClient client = new DefaultHttpClient();
			HttpGet request = new HttpGet(urlStr.toString());
			HttpResponse response = client.execute(request);
			BufferedReader reader = new BufferedReader(
					new InputStreamReader(response.getEntity().getContent()));
			String line = reader.readLine();
			int i = 0;
			Log.d(TAG, "Parsing stock data from Yahoo");
			while (line != null){
				Log.d(TAG, "Parsing: " + line);
				String[] values = line.split(",");
				Stock stock = new Stock(stocks[i], stocks[i].getId());
				stock.setCurrentPrice(Double.parseDouble(values[1]));
				stock.setName(values[2]);
				Log.d(TAG, "Parsed Stock: " + stock);
				newStocks.add(stock);
				line = reader.readLine();
				i++;
			}
		}
		return newStocks;
	}
	
	private void checkForAlerts(Iterable<Stock> stocks){
		try{
			for (Stock stock : stocks){
				double current = stock.getCurrentPrice();
				if (current > stock.getMaxPrice()){
					createHighPriceNotification(stock);
					continue;
				}
				if (current < stock.getMinPrice()){
					createLowPriceNotification(stock);
				}
			}
		} finally {
			AlarmReceiver.releaseLock();
			stopSelf();
		}
	}
	
	private void createHighPriceNotification(Stock stock) {
		NotificationManager mgr = (NotificationManager) 
			getSystemService(Context.NOTIFICATION_SERVICE);
		int dollarBill = R.drawable.dollar_icon;
		String shortMsg = "High price alert: " + stock.getSymbol();
		long time = System.currentTimeMillis();
		Notification n = new Notification(dollarBill, shortMsg, time);
		
		String title = stock.getName();
		String msg = "Current price $" + stock.getCurrentPrice() + 
			" is high";
		Intent i = new Intent(this, NotificationDetails.class);
		i.putExtra("stock", stock);
		PendingIntent pi = PendingIntent.getActivity(this, 0, i, 0);
	
		n.setLatestEventInfo(this, title, msg, pi);
		n.defaults |= Notification.DEFAULT_SOUND;
		long[] steps = {0, 500, 100, 200, 100, 200};
		n.vibrate = steps;
		n.ledARGB = 0x80009500;
		n.ledOnMS = 250;
		n.ledOffMS = 500;
		n.flags |= Notification.FLAG_SHOW_LIGHTS;
		mgr.notify(HIGH_PRICE_NOTIFICATION, n);
	}
	
	private void createLowPriceNotification(Stock stock){
		NotificationManager mgr = (NotificationManager) 
			getSystemService(Context.NOTIFICATION_SERVICE);		
		int dollarBill = R.drawable.dollar_icon;
		String shortMsg = "Low price alert: " + stock.getSymbol();
		long time = System.currentTimeMillis();
		Notification n = new Notification(dollarBill, shortMsg, time);
		
		String pkg = getPackageName();
		RemoteViews view = new RemoteViews(pkg, R.layout.notification_layout);
		String msg = "Current price $" + stock.getCurrentPrice() + 
		" is low";
		view.setTextViewText(R.id.notification_message, msg);
		n.contentView = view;
		Intent i = new Intent(this, NotificationDetails.class);
		i.putExtra("stock", stock);
		PendingIntent pi = PendingIntent.getActivity(this, 0, i, 0);
		n.contentIntent = pi;
		
		n.defaults |= Notification.DEFAULT_SOUND;
		long[] steps = {0, 500, 100, 500, 100, 500, 100, 500};
		n.vibrate = steps;
		n.ledARGB = 0x80A80000;
		n.ledOnMS = 1;
		n.ledOffMS = 0;
		n.flags |= Notification.FLAG_SHOW_LIGHTS;
		mgr.notify(LOW_PRICE_NOTIFICATION, n);			
	}
}
