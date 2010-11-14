package com.manning.aip.portfolio;

import android.app.IntentService;
import android.content.Intent;

public class SendC2dmRegistrationService extends IntentService {

	private static final String WORKER_NAME = "SendC2DMReg";
	public SendC2dmRegistrationService() {
		super(WORKER_NAME);
	}

	@Override
	protected void onHandleIntent(Intent intent) {
		try{
			String regId = intent.getStringExtra("regId");
			// TODO: Send the regId to the server
		} finally {
			AlarmReceiver.releaseLock();
		}
	}
}