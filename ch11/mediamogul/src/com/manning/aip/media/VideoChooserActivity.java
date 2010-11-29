package com.manning.aip.media;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.MediaController;
import android.widget.VideoView;

public class VideoChooserActivity extends Activity {

	private static final int SELECT_VIDEO = 1;
	private Uri videoUri;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.video_chooser);
		Button vidBtn = (Button) findViewById(R.id.vidBtn);
		vidBtn.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View button) {
				Intent videoChooser = new Intent(Intent.ACTION_GET_CONTENT);
				videoChooser.setType("video/*");
				startActivityForResult(videoChooser, SELECT_VIDEO);
			}
		});
		
		Button next = (Button) findViewById(R.id.nxtBtn3);
		next.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				Intent intent = 
					new Intent(VideoChooserActivity.this, 
							SlideshowActivity.class);
				intent.putExtras(getIntent());
				intent.putExtra("videoUri", videoUri);
				startActivity(intent);
			}
		});
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode != SELECT_VIDEO || resultCode != RESULT_OK){
			return;
		}
		VideoView video = (VideoView) findViewById(R.id.video);
		videoUri = data.getData();
		video.setVideoURI(videoUri);
		MediaController controller = new MediaController(this);
		controller.setMediaPlayer(video);
		video.setMediaController(controller);
		video.requestFocus();
		video.start();
	}

	
	
}
