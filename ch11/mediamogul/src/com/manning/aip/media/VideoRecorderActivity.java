package com.manning.aip.media;

import java.io.File;
import java.io.IOException;
import java.util.List;

import android.app.Activity;
import android.hardware.Camera;
import android.hardware.Camera.Parameters;
import android.hardware.Camera.Size;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;
import android.view.Window;

public class VideoRecorderActivity extends Activity {
	private static final String LOG_TAG = "VideoRecorderActivity";
	private SurfaceHolder holder;
	private Camera camera;
	private MediaRecorder mediaRecorder;
	private File tempFile; 
	private SurfaceView preview;
	private boolean isRecording = false;
	private final int maxDurationInMs = 20000;
	private final long maxFileSizeInBytes = 500000;
	private final int videoFramesPerSecond = 20;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		preview = new SurfaceView(this);
		holder = preview.getHolder();
		holder.addCallback(cameraman);
		holder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
		setContentView(preview);
		tempFile = new File(getCacheDir(), "temp.mov");
		if (tempFile.length() > 0){
			tempFile.delete();
		}
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu){
		MenuInflater inflater = new MenuInflater(this);
		inflater.inflate(R.menu.recorder_menu, menu);
		return true;
	}
	
	@Override 
	public boolean onOptionsItemSelected(MenuItem item){
		if (item.getItemId() == R.id.menu_rec_item){
			startRecording();
		} else if (item.getItemId() == R.id.menu_stop_item){
			stopRecording();
		} 
		return true;
	}
	
	private void startRecording(){
		if (isRecording){
			return;
		}
		isRecording = true;
		camera.unlock();
		mediaRecorder = new MediaRecorder();
		mediaRecorder.setCamera(camera);
		mediaRecorder.setAudioSource(MediaRecorder.AudioSource.CAMCORDER);
		mediaRecorder.setVideoSource(MediaRecorder.VideoSource.CAMERA);
		mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.DEFAULT);
		mediaRecorder.setMaxDuration(maxDurationInMs);
		Log.d(LOG_TAG, "Using tempFile=" + tempFile.getPath());
		mediaRecorder.setOutputFile(tempFile.getPath());
		mediaRecorder.setVideoFrameRate(videoFramesPerSecond);
		mediaRecorder.setVideoSize(preview.getWidth(), preview.getHeight());
		mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.DEFAULT);
		mediaRecorder.setVideoEncoder(MediaRecorder.VideoEncoder.DEFAULT);
		mediaRecorder.setPreviewDisplay(holder.getSurface());
		mediaRecorder.setMaxFileSize(maxFileSizeInBytes);
		try {
			mediaRecorder.prepare();
			mediaRecorder.start();
			Log.d(LOG_TAG, "Recording started");
		} catch (IllegalStateException e) {
			Log.e(LOG_TAG, "State exception during recording", e);
		} catch (IOException e) {
			Log.e(LOG_TAG, "IO exception during recording", e);
		}
	}
	
	private void stopRecording(){
		if (!isRecording){
			return;
		}
		isRecording = false;
		mediaRecorder.stop();
		try {
			camera.reconnect();
		} catch (IOException e) {
			Log.e(LOG_TAG, "Exception reconnecting to camera", e);
		}
		Log.d(LOG_TAG, "Recording finished file size=" + tempFile.length());
		camera.lock();
	}
	
	private Callback cameraman = new Callback(){
		@Override
		public void surfaceCreated(SurfaceHolder holder) {
			camera = Camera.open();
			try {
				camera.setPreviewDisplay(holder);
			} catch (IOException e) {
				camera.release();
				Log.e(LOG_TAG, "Exception setting " +
						"preview display",e);
			}
		}
		@Override
		public void surfaceChanged(SurfaceHolder holder, int format, 
				int width,int height) {
			Parameters params = camera.getParameters();
			List<Size> sizes = params.getSupportedPreviewSizes();
			Size optimalSize = getOptimalPreviewSize(sizes, width, height);
			params.setPreviewSize(optimalSize.width, optimalSize.height);
			camera.setParameters(params);
			camera.startPreview();
		}
		@Override
		public void surfaceDestroyed(SurfaceHolder holder) {
			camera.stopPreview();
			camera.release();
		}			
	};
	
	// Copied from Android SDK sample code
    private static Size getOptimalPreviewSize(List<Size> sizes, int width, 
    		int height) {
        double aspectTolerance = 0.05;
        double targetRatio = (double) width / height;
        if (sizes == null){ 
        	return null;
        }
        Size optimalSize = null;
        double minDiff = Double.MAX_VALUE;
        int targetHeight = height;
        // Try to find an size match aspect ratio and size
        for (Size size : sizes) {
            double ratio = (double) size.width / size.height;
            if (Math.abs(ratio - targetRatio) > aspectTolerance) continue;
            if (Math.abs(size.height - targetHeight) < minDiff) {
                optimalSize = size;
                minDiff = Math.abs(size.height - targetHeight);
            }
        }
        // Cannot find the one match the aspect ratio, ignore the requirement
        if (optimalSize == null) {
            minDiff = Double.MAX_VALUE;
            for (Size size : sizes) {
                if (Math.abs(size.height - targetHeight) < minDiff) {
                    optimalSize = size;
                    minDiff = Math.abs(size.height - targetHeight);
                }
            }
        }
        return optimalSize;
    }
}
