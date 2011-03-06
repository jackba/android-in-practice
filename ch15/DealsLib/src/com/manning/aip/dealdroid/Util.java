package com.manning.aip.dealdroid;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

public abstract class Util {
	public static Bitmap retrieveBitmap(final String urlString) {
		Log.d(Constants.LOG_TAG, "making HTTP trip for image:" + urlString);
		Bitmap bitmap = null;
		InputStream stream = null;
		try {
			URL url = new URL(urlString);
			stream = url.openConnection().getInputStream();
			bitmap = BitmapFactory.decodeStream(stream);
		} catch (MalformedURLException e) {
			Log.e(Constants.LOG_TAG, "Exception loading image, malformed URL",
					e);
		} catch (IOException e) {
			Log.e(Constants.LOG_TAG, "Exception loading image, IO error", e);
		} finally {
			try {
				if (stream != null) {
					stream.close();
				}
			} catch (IOException e) {
				Log.w(Constants.LOG_TAG, "Error closing stream", e);
			}
		}
		return bitmap;
	}
}
