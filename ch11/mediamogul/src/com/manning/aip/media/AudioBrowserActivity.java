package com.manning.aip.media;

import static android.provider.BaseColumns._ID;
import static android.provider.MediaStore.Audio.AudioColumns.ARTIST;
import static android.provider.MediaStore.Audio.AudioColumns.IS_MUSIC;
import static android.provider.MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
import static android.provider.MediaStore.MediaColumns.DATA;
import static android.provider.MediaStore.MediaColumns.TITLE;

import java.util.HashSet;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcel;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.TextView;

public class AudioBrowserActivity extends Activity {

	private Song selectedSong;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.audio_browser);
		AudioListAdapter adapter = new AudioListAdapter();
		ListView songList = (ListView) findViewById(R.id.list);
		songList.setAdapter(adapter);
		Button next = (Button) findViewById(R.id.nxtBtn2);
		next.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(AudioBrowserActivity.this, 
						VideoChooserActivity.class);
				intent.putExtras(getIntent());
				intent.putExtra("selectedSong", selectedSong);
				startActivity(intent);
			}
			
		});
	}

	private class AudioListAdapter extends BaseAdapter{
		private Cursor cursor;
		private Activity activity = AudioBrowserActivity.this;
		private RadioButton selectedRow;
		private final HashSet<Long> playingSongs = new HashSet<Long>();
		
		public AudioListAdapter(){
			super();
			String[] columns = {TITLE,ARTIST,_ID, DATA};
			String whereClause = IS_MUSIC + " = ?";
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
			song.uri = new Uri.Builder().appendPath(
					cursor.getString(cursor.getColumnIndex(DATA))).build();
			return song;
		}

		@Override
		public long getItemId(int position) {
			return ((Song) getItem(position)).id;
		}

		@Override
		public View getView(int position, View row, ViewGroup parent) {
			if (row == null){
				LayoutInflater airPump = activity.getLayoutInflater();
				row = airPump.inflate(R.layout.audio_list_item, parent, false);
			}
			RowViewHolder holder = (RowViewHolder) row.getTag();
			if (holder == null){
				holder = new RowViewHolder(row);
				row.setTag(holder);
			}
			final Song song = (Song) getItem(position);
			TextView songLabel = holder.songLabel;
			songLabel.setText(song.toString());
			final Button playBtn = holder.playBtn;
			if (playingSongs.contains(song.id)){
				playBtn.setText(R.string.pause);
			} else {
				playBtn.setText(R.string.play);
			}
			playBtn.setOnClickListener(new OnClickListener(){
				private Handler handler = new Handler();
				MediaPlayer player = null;
				long maxTime = 15L*1000; // 15 seconds
				long timeLeft = maxTime;
				Runnable autoStop;
				@Override
				public void onClick(View arg0) {
					if (player == null){
						player = MediaPlayer.create(activity, song.uri);
					}
					if (!playingSongs.contains(song.id)){
						player.start();
						playingSongs.add(song.id);
						autoStop = new Runnable(){
							@Override
							public void run() {
								player.pause();
								player.seekTo(0);
								playingSongs.remove(song.id);
								playBtn.setText(R.string.play);
								timeLeft = maxTime;
							}
						};
						handler.postDelayed(autoStop, timeLeft);					
						playBtn.setText(R.string.pause);
					} else {
						player.pause();
						playingSongs.remove(song.id);
						timeLeft = maxTime - player.getCurrentPosition();
						playBtn.setText(R.string.play);
						handler.removeCallbacks(autoStop);
					}
				}
				
			});
			final RadioButton radio = holder.radio;
			radio.setOnClickListener(new OnClickListener(){

				@Override
				public void onClick(View v) {
					if (selectedRow != null){
						selectedRow.setChecked(false);
					}
					selectedRow = radio;
					selectedSong = song;
				}
				
			});
			if (selectedSong != null && song.id == selectedSong.id){
				radio.setChecked(true);
			} else {
				radio.setChecked(false);
			}
			return row;
		}		
	}
	
	private static class RowViewHolder{
		final TextView songLabel;
		final Button playBtn;
		final RadioButton radio;
		RowViewHolder(View row){
			songLabel = (TextView) row.findViewById(R.id.song);
			playBtn = (Button) row.findViewById(R.id.playBtn);
			radio = (RadioButton) row.findViewById(R.id.rbtn);
		}
	}
	
	public static class Song implements Parcelable{
		String title;
		String artist;
		long id;
		Uri uri;
		
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

		@Override
		public int describeContents() {
			return 0;
		}

		@Override
		public void writeToParcel(Parcel parcel, int flags) {
			parcel.writeString(title);
			parcel.writeString(artist);
			parcel.writeLong(id);
			Uri.writeToParcel(parcel, uri);
		}
		
		public static final Parcelable.Creator<Song> CREATOR = 
			new Parcelable.Creator<Song>() {

			@Override
			public Song createFromParcel(Parcel source) {
				Song song = new Song();
				song.title = source.readString();
				song.artist = source.readString();
				song.id = source.readLong();
				song.uri = Uri.CREATOR.createFromParcel(source);
				return song;
			}

			@Override
			public Song[] newArray(int size) {
				return new Song[size];
			}
			
		};
	}
}
