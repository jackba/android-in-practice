package com.manning.aip.media;

import java.io.File;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.media.ThumbnailUtils;
import android.media.MediaPlayer.OnCompletionListener;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import static android.os.Environment.*;


public class ImageBrowserActivity extends Activity {
	
	private static final String LOG_TAG = "ImageBrowserActivity";
	private GridView grid;
	private ArrayList<File> selectedFiles = new ArrayList<File>();
	private MediaPlayer player;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.image_browser);
        playThemeMusic();
        grid = (GridView) findViewById(R.id.grid);
        final GridAdapter adapter = new GridAdapter();
        grid.setAdapter(adapter);
        Button nxtBtn = (Button) findViewById(R.id.nxtBtn1);
        if (grid.getCount() == 0){
        	LinearLayout container = (LinearLayout) findViewById(R.id.container);
        	container.removeAllViews();
        	TextView t = new TextView(this);
        	t.setText("This application requires you to have images in your public images directory");
        	container.addView(t);
        	Button btn = new Button(this);
        	btn.setText("Exit");
        	btn.setOnClickListener(new OnClickListener(){

				@Override
				public void onClick(View button) {
					ImageBrowserActivity.this.finish();
				}
        		
        	});
        	container.addView(btn);
        }    
        nxtBtn.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View view) {
				Intent i = 
					new Intent(ImageBrowserActivity.this, 
							AudioBrowserActivity.class);
				ArrayList<String> fileNamesList = 
					new ArrayList<String>(selectedFiles.size());
				for (File f : selectedFiles){
					try {
						fileNamesList.add(f.getCanonicalPath());
					} catch (IOException e) {
						Log.e(LOG_TAG, "Exception putting image file names",e);
					}
				}
				i.putStringArrayListExtra("imageFileNames", fileNamesList);
				startActivity(i);
			}
		});   
    }

    private void playThemeMusic() {
    	player = new MediaPlayer();
		AssetManager mgr = getResources().getAssets();
		String audioDir = "audio";
		try {
			final LinkedList<FileDescriptor> queue = 
				new LinkedList<FileDescriptor>();
			for (String song : mgr.list("audio")){
				queue.add(mgr.openFd(audioDir + "/" + song).getFileDescriptor());
			}
			playNextSong(queue);
			player.setOnCompletionListener(new OnCompletionListener(){
				@Override
				public void onCompletion(MediaPlayer mp) {
					try {
						playNextSong(queue);
					} catch (IOException e) {
						Log.e(LOG_TAG, "Exception loading theme music",e);
					}
				}
			});
		} catch (IOException e) {
			Log.e(LOG_TAG, "Exception loading theme music",e);
		}
	}

	private void playNextSong(LinkedList<FileDescriptor> queue) throws IOException {
		if (!queue.isEmpty()){
			FileDescriptor song = queue.poll();
			player.setDataSource(song);
			player.prepare();
			player.start();
		}
	}
	
	private void playThemeSong(){
		player = MediaPlayer.create(this, R.raw.constancy);
		player.start();
	}
	
	@Override
	public void onPause(){
		super.onPause();
		if (player != null && player.isPlaying()){
			player.pause();
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		if (player != null && player.isPlaying()){
			player.stop();
		}
		player.release();
	}

	@Override
	public void onResume() {
		super.onResume();
		if (player != null){
			player.start();
		}
	}

	private class GridAdapter extends BaseAdapter{
		private List<File> imageFiles;
		private List<Bitmap> thumbs;
		private static final int MAX_DIMENSION = 200;
		private Activity activity = ImageBrowserActivity.this;
		
		public GridAdapter(){
			File picturesDir = 
	        	getExternalStoragePublicDirectory(DIRECTORY_PICTURES);
			if (picturesDir == null || picturesDir.list() == null || picturesDir.list().length == 0){
				Toast.makeText(activity, "Sorry but you have no pictures " +
						"in your public pictures folder", Toast.LENGTH_LONG);
				imageFiles = new ArrayList<File>(0);
				thumbs = new ArrayList<Bitmap>(0);
				return;
			}
			int maxNumFiles;
			String[] nameArray = picturesDir.list();
			if (nameArray == null){
				maxNumFiles = 0; 
			} else {
				maxNumFiles = nameArray.length;
			}
			ArrayList<File> theFiles = new ArrayList<File>(maxNumFiles);
			if (maxNumFiles == 0) return;
			for (String fileName : nameArray){
				File file = new File(picturesDir, fileName);
				if (file.isFile()){
					theFiles.add(file);
				}
			}
			imageFiles = Collections.unmodifiableList(theFiles);
			ArrayList<Bitmap> tempThumbs = 
				new ArrayList<Bitmap>(imageFiles.size());
			for (int i=0;i<imageFiles.size();i++){
				tempThumbs.add(makeThumb(i));
			}
			thumbs = Collections.unmodifiableList(tempThumbs);
		}
    	
		@Override
		public int getCount() {
			return imageFiles.size();
		}

		@Override
		public Object getItem(int position) {
			if (position > getCount()){
				return null;
			}
			return thumbs.get(position);
		}
		
		private Bitmap makeThumb(int position){
			try{
				File imgFile = getImageFile(position);
				if (imgFile != null){
					InputStream stream = new FileInputStream(imgFile);
					Bitmap bm = BitmapFactory.decodeStream(stream);
		        	int imgWidth = bm.getWidth();
		        	int imgHeight = bm.getHeight();
		        	int max = Math.max(imgWidth, imgHeight);
		        	double scale = ((double) MAX_DIMENSION) / ((double) max);
		        	int width = (int) (scale * imgWidth);
		        	int height = (int) (scale * imgHeight);
		        	Log.d(LOG_TAG, "Scaled width=" + width);
		        	Log.d(LOG_TAG, "Scaled height=" + height);
		        	Bitmap thumb = 
		        		ThumbnailUtils.extractThumbnail(bm, width, height);
		        	return thumb;
				}
			} catch (Exception e) {
				Log.e(LOG_TAG, "Exception getting thumbnail image", e);
			}
			return null;
		}

		private File getImageFile(int position) {
			if (position > getCount()){
				return null;
			}
			return imageFiles.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(final int position, View cell, ViewGroup parent) {
			if (cell == null){
				LayoutInflater airPump = activity.getLayoutInflater();
				cell = airPump.inflate(R.layout.grid_item, parent, false);
			}
			ViewHolder holder = (ViewHolder) cell.getTag();
			if (holder == null){
				holder = new ViewHolder(cell);
				cell.setTag(holder);
			}
			ImageView img = holder.img;
			Bitmap thumb = (Bitmap) getItem(position);
			img.setLayoutParams(
					new LinearLayout.LayoutParams(thumb.getWidth(), 
							thumb.getHeight()));
			img.setImageBitmap(thumb);
			final File file = getImageFile(position);
			final CheckBox cbox = holder.cbox;
			if (selectedFiles.contains(file)){
				cbox.setChecked(true);
			} else {
				cbox.setChecked(false);
			}
			cbox.setOnClickListener(new OnClickListener(){
				@Override
				public void onClick(View view) {
					
					if (selectedFiles.remove(file)){
						Log.d(LOG_TAG, "Removed file:" + file.getName());
						cbox.setSelected(false);
					} else {
						selectedFiles.add(file);
						cbox.setSelected(true);
						Log.d(LOG_TAG, "Adding file:" + file.getName());
					}
				}
				
			});
			return cell;
		}
    	
    }
    
   static class ViewHolder {
    	final ImageView img;
    	final CheckBox cbox;
    	ViewHolder(View cell){
    		img = (ImageView) cell.findViewById(R.id.thumb);
    		cbox = (CheckBox) cell.findViewById(R.id.cbox);
    	}
    }
}