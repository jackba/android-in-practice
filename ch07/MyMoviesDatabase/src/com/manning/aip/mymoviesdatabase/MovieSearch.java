package com.manning.aip.mymoviesdatabase;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

import com.manning.aip.mymoviesdatabase.model.MovieSearchResult;
import com.manning.aip.mymoviesdatabase.xml.MovieFeed;
import com.manning.aip.mymoviesdatabase.xml.TheMovieDBXmlPullFeedParser;

import java.util.ArrayList;
import java.util.List;

public class MovieSearch extends Activity {

   private MovieFeed parser;
   private List<MovieSearchResult> movies;
   private ArrayAdapter<MovieSearchResult> adapter;

   private EditText input;
   private Button search;
   private ListView listView;

   @Override
   protected void onCreate(Bundle savedInstanceState) {
      // TODO form and use parser
      super.onCreate(savedInstanceState);
      setContentView(R.layout.movie_search);

      this.parser = new TheMovieDBXmlPullFeedParser();

      this.input = (EditText) this.findViewById(R.id.input);
      this.search = (Button) this.findViewById(R.id.search);
      this.search.setOnClickListener(new OnClickListener() {
         public void onClick(View v) {
            if (input.getText() != null && input.getText().toString() != null) {
               // TODO parse from AsyncTask
               movies.clear();
               movies.addAll(parser.search(input.getText().toString()));
               Log.d(Constants.LOG_TAG, " movies size after parse: " + movies.size());
               adapter.notifyDataSetChanged();
            } else {
               Toast.makeText(MovieSearch.this, "Search term required", Toast.LENGTH_LONG).show();
            }
         }
      });

      movies = new ArrayList<MovieSearchResult>();
      listView = (ListView) findViewById(R.id.searchresults);
      listView.setEmptyView(findViewById(R.id.empty));
      adapter = new ArrayAdapter<MovieSearchResult>(this, android.R.layout.simple_list_item_1, movies);
      listView.setAdapter(adapter);
      listView.setOnItemClickListener(new OnItemClickListener() {
         public void onItemClick(final AdapterView<?> parent, final View v, final int index, final long id) {
            Toast.makeText(MovieSearch.this, "selected Movie " + movies.get(index).getName(), Toast.LENGTH_SHORT).show();
         }
      });
      //registerForContextMenu(listView);
   }
   
   // long press "context" menu - don't have a use for it now, but need to come up with one to demo it
   /*
   @Override
   public void onCreateContextMenu(final ContextMenu menu, final View v, final ContextMenuInfo menuInfo) {
      super.onCreateContextMenu(menu, v, menuInfo);
      // group, item, order
      menu.add(0, 0, 0, "context A");
      menu.add(0, 1, 1, "context B");
      menu.setHeaderTitle("Action");
   }

   @Override
   public boolean onContextItemSelected(final MenuItem item) {
      AdapterContextMenuInfo info = (AdapterContextMenuInfo) item.getMenuInfo(); 
      Toast.makeText(MovieSearch.this, "item " + info.id, Toast.LENGTH_SHORT).show();
      return true;
   }
   */

}
