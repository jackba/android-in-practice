package com.manning.aip.brewmap;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

public class QueryResults extends Activity {
   
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.query_results);
        
        TextView results = (TextView) findViewById(R.id.results);
        String resultString = getIntent().getStringExtra("RESULTS");
        results.setText(resultString);
    }
}