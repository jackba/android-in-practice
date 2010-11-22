package com.manning.aip.media;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ThumbnailUtils;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

import static android.os.Environment.*;


public class ImageBrowserActivity extends Activity {
	
	GridView grid;
	
	ArrayList<File> selectedFiles = new ArrayList<File>();
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.image_browser);
        grid = (GridView) findViewById(R.id.grid);
        final GridAdapter adapter = new GridAdapter();
        grid.setAdapter(adapter);
        
        Button nxtBtn = (Button) findViewById(R.id.nxtBtn1);
        nxtBtn.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View view) {
				Intent i = new Intent(ImageBrowserActivity.this, AudioBrowserActivity.class);
				ArrayList<String> fileNamesList = new ArrayList<String>(selectedFiles.size());
				for (File f : selectedFiles){
					try {
						fileNamesList.add(f.getCanonicalPath());
					} catch (IOException e) {
						Log.e("ImageBrowserActivity", "Exception putting image file names",e);
					}
				}
				i.putStringArrayListExtra("imageFileNames", fileNamesList);
				startActivity(i);
			}
		});
    }

    private class GridAdapter extends BaseAdapter{
    	private List<File> imageFiles;
    	private List<Bitmap> thumbs;
    	private static final int MAX_DIMENSION = 200;
    	
    	public GridAdapter(){
    		File picturesDir = 
            	getExternalStoragePublicDirectory(DIRECTORY_PICTURES);
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
    			// this doesn't handle directories, make this recursive for that
    			theFiles.add(new File(picturesDir, fileName));
    		}
    		imageFiles = Collections.unmodifiableList(theFiles);
    		ArrayList<Bitmap> tempThumbs = new ArrayList<Bitmap>(imageFiles.size());
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
			return thumbs.get(position);
		}
		
		private Bitmap makeThumb(int position){
			try{
				File imgFile = getImageFile(position);
				InputStream stream = new FileInputStream(imgFile);
				Bitmap bm = BitmapFactory.decodeStream(stream);
	        	int imgWidth = bm.getWidth();
	        	int imgHeight = bm.getHeight();
	        	int max = Math.max(imgWidth, imgHeight);
	        	double scale = ((double) MAX_DIMENSION) / ((double) max);
	        	int width = (int) (scale * imgWidth);
	        	int height = (int) (scale * imgHeight);
	        	Log.d("ImageBrowserActivity", "Scaled width=" + width);
	        	Log.d("ImageBrowserActivity", "Scaled height=" + height);
	        	Bitmap thumb = ThumbnailUtils.extractThumbnail(bm, width, height);
	        	return thumb;
			} catch (Exception e) {
				Log.e("ImageBrowserActivity", "Exception getting thumbnail image", e);
			}
			return null;
		}

		private File getImageFile(int position) {
			return imageFiles.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(final int position, View convertView, ViewGroup parent) {
			if (convertView == null){
				LayoutInflater airPump = 
					(LayoutInflater) ImageBrowserActivity.this.
						getSystemService(LAYOUT_INFLATER_SERVICE);
				convertView = airPump.inflate(R.layout.grid_item, parent, false);
			}
			ImageView img = (ImageView) convertView.findViewById(R.id.thumb);
			Bitmap thumb = (Bitmap) getItem(position);
			img.setLayoutParams(new LinearLayout.LayoutParams(thumb.getWidth(), thumb.getHeight()));
			img.setImageBitmap(thumb);
			final CheckBox cbox = (CheckBox) convertView.findViewById(R.id.cbox);
			cbox.setOnClickListener(new OnClickListener(){
				@Override
				public void onClick(View view) {
					File file = getImageFile(position);
					if (selectedFiles.remove(file)){
						Log.d("ImageBrowserActivity", "Removed file:" + file.getName());
						cbox.setSelected(false);
					} else {
						selectedFiles.add(file);
						cbox.setSelected(true);
						Log.d("ImageBrowserActivity", "Adding file:" + file.getName());
					}
				}
				
			});
			return convertView;
		}
    	
    }
}