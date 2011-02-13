package com.manning.aip.brewmap;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.manning.aip.brewmap.model.Pub;
import com.manning.aip.brewmap.xml.BeerMappingParser;
import com.manning.aip.brewmap.xml.BeerMappingXmlPullParser;

import java.util.List;

// NOTE -- this is far from complete, just an interim checkin

public class Main extends Activity {

   @Override
   public void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      setContentView(R.layout.main);

      final BeerMappingParser parser = new BeerMappingXmlPullParser();
      
      final EditText input = (EditText) findViewById(R.id.input);
      
      Button city = (Button) findViewById(R.id.button_city);
      city.setOnClickListener(new OnClickListener() {
         @Override
         public void onClick(View v) {           
            List<Pub> pubs = parser.parseCity(input.getText().toString());
            forwardResults(pubs);
         }
      });
      
      Button state = (Button) findViewById(R.id.button_state);
      state.setOnClickListener(new OnClickListener() {
         @Override
         public void onClick(View v) {           
            List<Pub> pubs = parser.parseState(input.getText().toString());
            forwardResults(pubs);
         }
      });
      
      Button piece = (Button) findViewById(R.id.button_piece);
      piece.setOnClickListener(new OnClickListener() {
         @Override
         public void onClick(View v) {           
            List<Pub> pubs = parser.parsePiece(input.getText().toString());
            forwardResults(pubs);
         }
      });
   }
   
   private void forwardResults(List<Pub> pubs) {
      if (pubs != null && !pubs.isEmpty()) {
         StringBuilder sb = new StringBuilder();
         for (Pub p : pubs) {
            sb.append("\n\n" + p.getName());
         }
         Intent i = new Intent(this, QueryResults.class);
         i.putExtra("RESULTS", sb.toString());
         startActivity(i);
      } else {
         Toast.makeText(this, "Pubs empty!", Toast.LENGTH_SHORT).show();
      }
   }
}