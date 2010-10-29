package com.manning.aip.portfolio;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.accounts.AccountManagerCallback;
import android.accounts.AccountManagerFuture;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;

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
		final String regId = intent.getStringExtra("registration_id"); 
		if (regId != null) {
			// get the Client Login token from the AccountManager service
			AccountManager mgr = AccountManager.get(context);
			Account googleAccount = mgr.getAccountsByType("com.google")[0];
			mgr.getAuthToken(googleAccount, "ah", true, 
					new AccountManagerCallback<Bundle>(){
				public void run(AccountManagerFuture<Bundle> future) {
					Bundle bundle;
					try {
						bundle = future.getResult();
						String authToken = 
							bundle.get(AccountManager.KEY_AUTHTOKEN)
							.toString();
						sendToServer(regId,authToken);
					} catch (Exception e) {
						Log.e("PushReceiver", "Reg/Auth exeption", e);
					} 
				}
			},null);
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
		context.startService(stockService);
	}
	
	/**
	 * This method sends C2DM registration information to your server, so that
	 * your server can then use this information to send events to C2DM.
	 * 
	 * @param 	regId			The registration ID received from C2DM
	 * @param 	authToken		The Client Login token for the user
	 */
	private void sendToServer(String regId, String authToken){
		new AsyncTask<String, Void, Void>(){
			@Override
			protected void onPostExecute(Void result) {
				AlarmReceiver.releaseLock();
			}
			@Override
			protected Void doInBackground(String... params) {
				// send data to your app server
				return null;
			}
		}.execute(regId, authToken);
	}
	
}
