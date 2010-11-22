package com.manning.aip.media;

import java.io.File;
import java.util.List;

import android.app.Activity;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import static android.provider.MediaStore.Audio.AudioColumns.TITLE;
import static android.provider.MediaStore.Audio.AudioColumns.ARTIST;
import static android.provider.MediaStore.Audio.AudioColumns._ID;
import static android.provider.MediaStore.Audio.AudioColumns.DATA;
import static android.provider.MediaStore.Audio.AudioColumns.IS_MUSIC;
import static android.provider.MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;

public class AudioBrowserActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.audio_browser);
		AudioListAdapter adapter = new AudioListAdapter();
		ListView songList = (ListView) findViewById(R.id.list);
		songList.setAdapter(adapter);
		// TODO: Set click handler for 'next' button
	}

	private class AudioListAdapter extends BaseAdapter{
		private Cursor cursor;
		private Activity activity = AudioBrowserActivity.this;
		
		public AudioListAdapter(){
			super();
			String[] columns = {TITLE,ARTIST,_ID, DATA};
			String whereClause = IS_MUSIC + " != ?";
			String[] whereValues = {"1"};
			cursor = managedQuery(EXTERNAL_CONTENT_URI,
				columns,
				whereClause,
				whereValues,
				null
			);
		}
		
		@Override
		public int getCount() {
			return cursor.getCount();
		}

		@Override
		public Object getItem(int position) {
			Song song = new Song();
			cursor.moveToPosition(position);
			song.title = cursor.getString(cursor.getColumnIndex(TITLE));
			song.artist = cursor.getString(cursor.getColumnIndex(ARTIST));
			song.id = cursor.getLong(cursor.getColumnIndex(_ID));
			song.uri = cursor.getString(cursor.getColumnIndex(DATA));
			return song;
		}

		@Override
		public long getItemId(int position) {
			return ((Song) getItem(position)).id;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			if (convertView == null){
				LayoutInflater airPump = 
					(LayoutInflater) activity.getSystemService(
							LAYOUT_INFLATER_SERVICE);
				convertView = airPump.inflate(R.layout.audio_list_item, parent, false);
			}
			Song song = (Song) getItem(position);
			TextView songLabel = (TextView) convertView.findViewById(R.id.song);
			songLabel.setText(song.toString());
			// TODO: Set click handler for button
			return convertView;
		}		
	}
	private static class Song{
		String title;
		String artist;
		long id;
		String uri;
		
		@Override
		public String toString(){
			StringBuilder sb = new StringBuilder();
			sb.append(title).append(" -- ");
			if (artist != null){
				sb.append(artist);
			} else {
				sb.append("Unknwon Artist");
			}
			return sb.toString();
		}
	}
}
