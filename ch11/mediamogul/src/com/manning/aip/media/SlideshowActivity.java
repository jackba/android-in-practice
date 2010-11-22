package com.manning.aip.media;

import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.ImageView;

public class SlideshowActivity extends Activity {

	private ImageView slide0;
	private ImageView slide1;
	private Handler handler = new Handler();
	private static final int TIME_PER_SLIDE = 3*1000;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.slideshow);
		slide0 = (ImageView) findViewById(R.id.slide0);
		slide1 = (ImageView) findViewById(R.id.slide1);
	}

	@Override
	public void onResume() {
		super.onResume();
		handler.postDelayed(new AnimationAlphaTimer(), 100);
		//new Timer(false).schedule(new AnimationAlphaTimer(), 100);
	}

	private class AnimationAlphaTimer implements Runnable,
			Animation.AnimationListener {

		private ArrayList<String> images;
		private int count = 0;
		private Bitmap currentImage = null;
		int current = -1;
		private Bitmap nextImage = null;
		private Random rnd = new Random(System.currentTimeMillis());

		public AnimationAlphaTimer() {
			images = getIntent().getStringArrayListExtra("imageFileNames");
			currentImage = getNextImage();
			//slide0.setImageResource(currentImage);
			
			slide0.setImageBitmap(currentImage);
			nextImage = getNextImage();
			slide1.setImageBitmap(nextImage);
			count = 1;
		}

		@Override
		public void run() {
			nextSlide();
		}
		
		private Bitmap getImage(int index){
			return BitmapFactory.decodeFile(images.get(index));
		}

		private void nextSlide() {
			AlphaAnimation animation = new AlphaAnimation(0.0f, 1.0f);
			if ((count % 2) == 0) {
				animation = new AlphaAnimation(1.0f, 0.0f);
			}
			animation.setStartOffset(TIME_PER_SLIDE);
			animation.setDuration(TIME_PER_SLIDE);
			animation.setFillAfter(true);
			animation.setAnimationListener(this);
			slide1.startAnimation(animation);
			currentImage = nextImage;
		}

		@Override
		public void onAnimationEnd(Animation animation) {
			nextImage = getNextImage();
			if ((count % 2) == 0) {
				slide1.setImageBitmap(nextImage);
			} else {
				slide0.setImageBitmap(nextImage);
			}
			count++;
			nextSlide();
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

		public void onAnimationRepeat(Animation animation) {
		}

		public void onAnimationStart(Animation animation) {
		}
	}
}
