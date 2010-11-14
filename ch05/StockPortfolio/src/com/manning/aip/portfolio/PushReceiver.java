package com.manning.aip.portfolio;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * <code>BroadcastReceiver</code> used to receive events from the
 * Cloud to Device Messaging (C2DM) service. 
 * 
 * @author Michael Galpin
 *
 */
public class PushReceiver extends BroadcastReceiver {
	
	@Override
	public void onReceive(Context context, Intent intent) {
		AlarmReceiver.acquireLock(context);
	    if (intent.getAction().equals(
	    		"com.google.android.c2dm.intent.REGISTRATION")) {
	        onRegistration(context, intent);
	    } else if (intent.getAction().equals(
	    		"com.google.android.c2dm.intent.RECEIVE")) {
	        onMessage(context, intent);
	     }
	 }
	
	/**
	 * This method is used to handle the registration event being sent from
	 * C2DM.
	 * 
	 * @param 	context			The <code>Context</code> for the event
	 * @param 	intent			The <code>Intent</code> received from C2DM
	 */
	private void onRegistration(Context context, Intent intent) {
		String regId = intent.getStringExtra("registration_id"); 
		if (regId != null) {
			Intent i = new Intent(context, SendC2dmRegistrationService.class);
			i.putExtra("regId", regId);
			context.startService(i);
		}
	}
	
	/**
	 * This method is used to handle events sent from your servers, via the
	 * C2DM service.
	 * 
	 * @param 	context			The <code>Context</code> for the event
	 * @param 	intent			The <code>Intent</code> received from C2DM
	 */
	private void onMessage(Context context, Intent intent){
		Intent stockService = 
			new Intent(context, PortfolioManagerService.class);
		// copy any data sent from your server
		stockService.putExtras(intent);
		context.startService(stockService);
	}
}
