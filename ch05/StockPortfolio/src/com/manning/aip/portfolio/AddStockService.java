package com.manning.aip.portfolio;

import android.app.Activity;
import android.app.IntentService;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class AddStockService extends IntentService {

	private static final String NAME = "AddStockService";
	public static final String EXTRA_STOCK = "Stock";
	
	public AddStockService(){
		super(NAME);
	}
	
	@Override
	protected void onHandleIntent(Intent request) {
		Stock stock = request.getParcelableExtra(EXTRA_STOCK);
		StocksDb db = new StocksDb(this);
		db.addStock(stock);
	}
public static class StockActivity extends Activity{
	private Stock stock;
	@Override
	public void onCreate(Bundle savedInstance){
		Button button = (Button) findViewById(R.id.btn);
		button.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				Intent request = 
					new Intent(StockActivity.this, AddStockService.class);
				request.putExtra(EXTRA_STOCK, stock);
				startService(request);
			}
			
		});
	}
}
}

