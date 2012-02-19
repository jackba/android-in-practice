package com.manning.aip.media;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

import android.app.Activity;
import android.graphics.Bitmap;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.VideoView;

import com.manning.aip.media.AudioBrowserActivity.Song;

/**
 * Show the slideshow created from the pics, song, and video the user selected
 * @author Michael Galpin
 *
 */
public class SlideshowActivity extends Activity {

	private static final String LOG_TAG = "SlideshowActivity";
	
	private ImageView leftSlide;
	private ImageView rightSlide;
	private Handler handler = new Handler();
	private static final int TIME_PER_SLIDE = 3*1000;
	private Song song;
	private MediaPlayer player;
	private MediaController videoPlayer;
	private VideoView video;
	private boolean playingSlides = true;
	private int maxEdge = 0;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.slideshow);
		
		// determine screen size
		DisplayMetrics metrics = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(metrics);
		maxEdge = Math.max(metrics.widthPixels, metrics.heightPixels);
		Log.d(LOG_TAG, "Max edge for this device = " + maxEdge);
		
		// create the UI
		// this is two images in a frame being swapped in and out
		leftSlide = (ImageView) findViewById(R.id.slide0);
		rightSlide = (ImageView) findViewById(R.id.slide1);
		// play the song the user picked
		song = getIntent().getParcelableExtra("selectedSong");
		player = MediaPlayer.create(this, song.uri);
		// when the song finishes, clear the UI and play the video
		player.setOnCompletionListener(new OnCompletionListener(){
			@Override
			public void onCompletion(MediaPlayer mp) {
				FrameLayout frame = (FrameLayout) findViewById(R.id.frame);
				frame.removeAllViews();
				playingSlides = false;
				video = new VideoView(SlideshowActivity.this);
				video.setLayoutParams(new LayoutParams(
								LayoutParams.FILL_PARENT, 
								LayoutParams.FILL_PARENT));
				frame.addView(video);
				video.setVideoURI(
						(Uri) getIntent().getExtras().get("videoUri")); 
				videoPlayer = new MediaController(SlideshowActivity.this);
				videoPlayer.setMediaPlayer(video);
				video.setMediaController(videoPlayer);
				video.requestFocus();
				video.start();
			}
		});
	}
	@Override
	public void onPause(){
		super.onPause();
		if (player != null && player.isPlaying()){
			player.pause();
		}
		if (video != null && video.isPlaying()){
			video.pause();
		}
	}
	@Override
	protected void onDestroy() {
		super.onDestroy();
		if (player != null && player.isPlaying()){
			player.stop();
		}
		if (video != null && video.isPlaying()){
			video.stopPlayback();
		}
		player.release();
	}

	@Override
	public void onResume() {
		super.onResume();
		final DissolveTransition animation = new DissolveTransition();
		handler.postDelayed(new Runnable(){
			@Override
			public void run() {
				animation.nextSlide();
			}
		}, 100);
		player.start();
	}

	/**
	 * Simple dissolve animation used to transition between slides
	 */
	private class DissolveTransition{
		private ArrayList<String> images;
		private int count = 0;
		private Bitmap currentImage = null;
		int current = -1;
		private Bitmap nextImage = null;
		private Random rnd = new Random(System.currentTimeMillis());
		public DissolveTransition() {
			images = getIntent().getStringArrayListExtra("imageFileNames");
			currentImage = getNextImage();			
			leftSlide.setImageBitmap(currentImage);
			nextImage = getNextImage();
			rightSlide.setImageBitmap(nextImage);
			count = 1;
		}
		private Bitmap getImage(int index){
			File file = new File(images.get(index));
			Bitmap bmp = null;
			try{
				bmp = BitmapUtil.decodeDownsizedBitmapStream(file, maxEdge, SlideshowActivity.this);
			} catch (IOException e) {
				Log.e(LOG_TAG, "Exception loading image",e);
			}
			return bmp;
		}
		private void nextSlide() {
			AlphaAnimation animation = new AlphaAnimation(0.0f, 1.0f);
			if ((count % 2) == 0) {
				animation = new AlphaAnimation(1.0f, 0.0f);
			}
			animation.setStartOffset(TIME_PER_SLIDE);
			animation.setDuration(TIME_PER_SLIDE);
			animation.setFillAfter(true);
			animation.setAnimationListener(new Animation.AnimationListener() {			
				@Override
				public void onAnimationStart(Animation animation) {}
				@Override
				public void onAnimationRepeat(Animation animation) {}
				@Override
				public void onAnimationEnd(Animation animation) {
					if (playingSlides){
						nextImage = getNextImage();
						ImageView backgroundImage = 
							(count % 2 == 0) ? rightSlide : leftSlide;
						backgroundImage.setImageBitmap(nextImage);
						count++;
						nextSlide();
					}
				}
			});
			rightSlide.startAnimation(animation);
			currentImage = nextImage;
		}
		public Bitmap getNextImage(){
			int index = rnd.nextInt(images.size());
			if (current < 0) current = index;
			while (index == current){
				index = rnd.nextInt(images.size());
			}
			current = index;
			return getImage(index);
		}
	}
}
