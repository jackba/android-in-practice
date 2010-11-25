package com.manning.aip.mymoviesdatabase;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

import com.manning.aip.mymoviesdatabase.data.DataManager;
import com.manning.aip.mymoviesdatabase.model.Category;

import java.util.List;

public class CategoryManager extends Activity {

   private MyMoviesApp application;

   private List<Category> categories;
   private ArrayAdapter<Category> adapter;

   private ListView listView;

   @Override
   protected void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      setContentView(R.layout.category_manager);

      application = (MyMoviesApp) getApplication();

      /*
      this.submit = (Button) this.findViewById(R.id.submit);
      this.submit.setOnClickListener(new OnClickListener() {
         public void onClick(View v) {

         }
      });
      */

      DataManager dataManager = application.getDataManager();
      categories = dataManager.getCategoryDao().getAll();
      listView = (ListView) this.findViewById(R.id.category_manager_list);
      listView.setEmptyView(findViewById(R.id.category_manager_empty));
      adapter = new ArrayAdapter<Category>(this, android.R.layout.simple_list_item_1, categories);
      listView.setAdapter(adapter);
      listView.setOnItemClickListener(new OnItemClickListener() {
         public void onItemClick(final AdapterView<?> parent, final View v, final int index, final long id) {
            Toast.makeText(CategoryManager.this, "selected Category " + categories.get(index).getName(),
                     Toast.LENGTH_SHORT).show();
         }
      });

   }

   private boolean isTextViewEmpty(final TextView textView) {
      return !(textView != null && textView.getText() != null && textView.getText().toString() != null && !textView
               .getText().toString().equals(""));
   }
}
