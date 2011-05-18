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
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.media.ThumbnailUtils;
import android.media.MediaPlayer.OnCompletionListener;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
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

/**
 * Browse images in the shared pictures directory (/Pictures). If there
 * are no images there, then an error message is displayed. So put some 
 * images there to make this interesting.
 * 
 * @author Michael Galpin
 *
 */
public class ImageBrowserActivity extends Activity {
	
	private static final String LOG_TAG = "ImageBrowserActivity";
	
	// Grid of all of the images
	private GridView grid;
	
	// The selected files
	private ArrayList<File> selectedFiles = new ArrayList<File>();
	
	// MediaPlayer for playing theme music
	private MediaPlayer player;
	private ProgressDialog dialog;
	// all of the pictures
	private List<File> imageFiles;
	// thumbnail cache
	private List<Bitmap> thumbs;
	// max size of thumbnails
	private static final double MAX_DIMENSION = 200.0D;

    protected static final int NO_PICS = 19;
	
	private Handler loadingHandler = new Handler(){
		@Override
		public void handleMessage(Message msg) {
		    if (msg.what == NO_PICS){
		        Toast.makeText(ImageBrowserActivity.this, R.string.empty_pics_msg, 
                        Toast.LENGTH_LONG).show();
                imageFiles = new ArrayList<File>(0);
                thumbs = new ArrayList<Bitmap>(0);
		    }
			grid.setAdapter(new GridAdapter());
			dialog.dismiss();
	        if (grid.getCount() == 0){
	        	LinearLayout container = 
	        		(LinearLayout) findViewById(R.id.container);
	        	container.removeAllViews();
	        	TextView t = new TextView(ImageBrowserActivity.this);
	        	t.setText(R.string.no_pics_err_msg);
	        	container.addView(t);
	        	Button btn = new Button(ImageBrowserActivity.this);
	        	btn.setText("Exit");
	        	btn.setOnClickListener(new OnClickListener(){
					@Override
					public void onClick(View button) {
						ImageBrowserActivity.this.finish();
					}
	        	});
	        	container.addView(btn);
	        }    				
		}
	};
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.image_browser);

        grid = (GridView) findViewById(R.id.grid);

        // button to go to next activity
        Button nxtBtn = (Button) findViewById(R.id.nxtBtn1);
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
				// copy any data passed in to this activity, pass it to the next
				Intent req = getIntent();
				if (req != null && req.getExtras() != null){
					i.putExtras(req);
				}
				startActivity(i);
			}
		});   
        
    }

    // Plays all of the music in the assets/audio directory
    private void playThemeMusic() {
    	player = new MediaPlayer();
		AssetManager mgr = getResources().getAssets();
		String audioDir = "audio";
		try {
			final LinkedList<FileDescriptor> queue = 
				new LinkedList<FileDescriptor>();
			for (String song : mgr.list("audio")){
				queue.add(
						mgr.openFd(audioDir + "/" + song).getFileDescriptor());
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

	private void playNextSong(LinkedList<FileDescriptor> queue) 
	throws IOException {
		if (!queue.isEmpty()){
			FileDescriptor song = queue.poll();
			player.setDataSource(song);
			player.prepare();
			player.start();
		}
	}
	
	// Not used but included as an example
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
        // Setup UI and event listeners
        // could take awhile to load all of the pics, show a progress dialog
        dialog = ProgressDialog.show(this, "", "Loading pics");
        playThemeMusic();
		if (player != null){
			player.start();
		}
        Runnable loader = new Runnable(){
            @Override
            public void run() {
                // get pics from /Pictures
                File picturesDir = 
                    getExternalStoragePublicDirectory(DIRECTORY_PICTURES);
                if (picturesDir == null || picturesDir.list() == null || 
                        picturesDir.list().length == 0){
                    Message none = Message.obtain();
                    none.what = NO_PICS;
                    loadingHandler.sendMessage(none);
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
                    Bitmap thumb = makeThumb(i);
                    if (thumb != null){
                        tempThumbs.add(thumb);
                    }
                }
                thumbs = Collections.unmodifiableList(tempThumbs);
                loadingHandler.sendMessage(Message.obtain());
            }
        };
        Thread loadingThread = new Thread(loader);
        loadingThread.start();
	}
	/**
	 * Creates a thumbnail image for the image at the given postion in the
	 * list of images.
	 * @param 	position		Position of the image in the list
	 * @return	A thumbnail image created by cropping the original image
	 */
	private Bitmap makeThumb(int position){
		Log.d(LOG_TAG, "Creating thumb for postion="+position);
		try{
			File imgFile = getImageFile(position);
			if (imgFile != null){
				Log.d(LOG_TAG, "Got file at position="+position+" file="+
						imgFile.getAbsolutePath());
				InputStream stream = new FileInputStream(imgFile);
				Bitmap bm = BitmapFactory.decodeStream(stream);
	        	int imgWidth = bm.getWidth();
	        	int imgHeight = bm.getHeight();
	        	int max = Math.max(imgWidth, imgHeight);
	        	double scale = MAX_DIMENSION / max;
	        	int width = (int) (scale * imgWidth);
	        	int height = (int) (scale * imgHeight);
	        	Log.d(LOG_TAG, "Scaled width=" + width);
	        	Log.d(LOG_TAG, "Scaled height=" + height);
	        	// crops images around center to create thumbnail
	        	Bitmap thumb = 
	        		ThumbnailUtils.extractThumbnail(bm, width, height);
	        	// to scale image instead use Bitmap.createScaledBitmap
	        	bm = null;
	        	return thumb;
			} else {
				Log.w(LOG_TAG, "File at position="+position + " was null");
			}
		} catch (Exception e) {
			Log.e(LOG_TAG, "Exception getting thumbnail image at " +
					"position="+position, e);
		}
		Log.w(LOG_TAG, "Failed to generate thumbnail for position="+position);
		return null;
	}

	private File getImageFile(int position) {
		if (position > imageFiles.size()){
			return null;
		}
		return imageFiles.get(position);
	}
	// custom adapter for grid of pictures
	private class GridAdapter extends BaseAdapter{
		
		@Override
		public int getCount() {
			return thumbs.size();
		}

		@Override
		public Object getItem(int position) {
			if (position > getCount()){
				return null;
			}
			return thumbs.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(final int position, View cell, ViewGroup parent) {
			Log.d(LOG_TAG, "Getting cell for position="+position);
			if (cell == null){
				LayoutInflater airPump = getLayoutInflater();
				cell = airPump.inflate(R.layout.grid_item, parent, false);
			}
			ViewHolder holder = (ViewHolder) cell.getTag();
			if (holder == null){
				holder = new ViewHolder(cell);
				cell.setTag(holder);
			}
			ImageView img = holder.img;
			Bitmap thumb = (Bitmap) getItem(position);
			if (thumb == null){
				Log.w(LOG_TAG, "Null thumb for position="+position);
			} else {
				img.setLayoutParams(
						new LinearLayout.LayoutParams(thumb.getWidth(), 
								thumb.getHeight()));
				img.setImageBitmap(thumb);
			}
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
    
	/**
	 * Using the ViewHolder pattern, useful for caching references to the views
	 * that we want to set dynamically.
	 */
   static class ViewHolder {
    	final ImageView img;
    	final CheckBox cbox;
    	ViewHolder(View cell){
    		img = (ImageView) cell.findViewById(R.id.thumb);
    		cbox = (CheckBox) cell.findViewById(R.id.cbox);
    	}
    }
}