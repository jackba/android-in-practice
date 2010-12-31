package com.manning.aip.media;

import static android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI;

import java.io.FileNotFoundException;
import java.io.InputStream;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;

public class TitlePageActivity extends Activity{
	private static final String TITLE_PAGE_PHOTO_URI = "titlePagePhotoUri";
	private Uri photoUri;

	private final static int TAKE_PHOTO = 1;
	private final static String PHOTO_URI = "photoUri";
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.title_page);
		Button takePhotoBtn = (Button) findViewById(R.id.takePhotoBtn);
		takePhotoBtn.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View button) {
				Intent intent = 
					new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
				photoUri = getContentResolver().insert(
						EXTERNAL_CONTENT_URI, new ContentValues());
				intent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
				startActivityForResult(intent,TAKE_PHOTO);
			}
		});
		Button nxtBtn = (Button) findViewById(R.id.nxtBtn0);
		nxtBtn.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(TitlePageActivity.this, 
						ImageBrowserActivity.class);
				intent.putExtra(TITLE_PAGE_PHOTO_URI, photoUri);
				startActivity(intent);
			}
		});
		if (savedInstanceState != null){
			photoUri = (Uri) savedInstanceState.get(PHOTO_URI);
		}
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putParcelable(PHOTO_URI, photoUri);
	}
	@Override
	protected void onActivityResult(int requestCode, int resultCode, 
			Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode != Activity.RESULT_OK || requestCode != TAKE_PHOTO){
			return;
		}
		ImageView img = (ImageView) findViewById(R.id.photoThumb);
		try {
			InputStream stream = 
				getContentResolver().openInputStream(photoUri);
			Bitmap bmp = BitmapFactory.decodeStream(stream);
			img.setImageBitmap(bmp);
			Log.d("TitlePageActivity", "Set image using URI");
		} catch (FileNotFoundException e) {
			Log.e("TitlePageActivity", "FileNotFound",e);
		}
	}
	

}