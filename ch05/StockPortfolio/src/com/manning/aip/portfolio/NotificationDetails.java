package com.manning.aip.portfolio;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

/**
 * An Activity that is started when a user opens a Notification.
 * 
 * @author Michael Galpin
 *
 */
public class NotificationDetails extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.details);
		
		Stock stock = (Stock) this.getIntent().getParcelableExtra("stock");
		TextView nameLabel = (TextView) findViewById(R.id.name);
		if (stock == null){
			nameLabel.setText("No stock passed in to display");
			return;
		}
		
		nameLabel.setText(stock.getName() + "(" + stock.getSymbol() + ")");
		
		double current = stock.getCurrentPrice();
		TextView currentLabel = (TextView) findViewById(R.id.current);
		currentLabel.setText("Current Price: $" + current);
		
		TextView minLabel = (TextView) findViewById(R.id.min);
		if (current < stock.getMinPrice()){
			minLabel.setText("Current price is less than minimum price $" + 
					stock.getMinPrice());
			minLabel.setTextColor(0xFFFF0000);
		} else {
			minLabel.setText("Minimum price: $" + stock.getMinPrice());
		}
		
		TextView maxLabel = (TextView) findViewById(R.id.max);
		if (current > stock.getMaxPrice()){
			maxLabel.setText("Current price is more than maximum price $" + 
					stock.getMaxPrice());
			maxLabel.setTextColor(0xFF00FF00);
		} else {
			maxLabel.setText("Maximum price: $" + stock.getMaxPrice());
		}
	}

}
