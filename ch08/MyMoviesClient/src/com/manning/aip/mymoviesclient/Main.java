package com.manning.aip.mymoviesclient;

import android.app.Activity;
import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.widget.TextView;


public class Main extends Activity {
   
   // this is defined in the provider (see MyMoviesContentProvider)
   public static final String AUTHORITY = "com.manning.aip.mymoviesdatabase";
   public static final Uri AUTHORITY_URI = Uri.parse("content://" + AUTHORITY);
   public static final Uri CONTENT_URI = Uri.withAppendedPath(AUTHORITY_URI, "movies");
   
   private TextView output;
   
   // TODO add buttons and a simple form, allow query all movies, by id
   // also add/delete/update movies
   // also discuss/deal with content provider listeners   
   
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);        
        
        output = (TextView) findViewById(R.id.content_out);       
        
        ContentResolver client = this.getContentResolver();
        Cursor c = client.query(CONTENT_URI, null, null, null, null);
        System.out.println("query with content uri " + CONTENT_URI);
        
        int nameColumn = c.getColumnIndex("movie_name"); 
        int urlColumn = c.getColumnIndex("url");
        int yearColumn = c.getColumnIndex("year");
        
        StringBuilder sb = new StringBuilder();
        if (c.moveToFirst()) {
           do {
              sb.append("\nMOVIE: " +  c.getString(nameColumn));
              sb.append(" " + c.getString(urlColumn));              
           } while (c.moveToNext());
        }
        if (!c.isClosed()) {
           c.close();
        }
        
        output.setText(sb.toString());
    }
}