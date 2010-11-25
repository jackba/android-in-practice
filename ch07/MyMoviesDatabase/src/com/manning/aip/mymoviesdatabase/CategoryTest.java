package com.manning.aip.mymoviesdatabase;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.manning.aip.mymoviesdatabase.data.CategoryDao;
import com.manning.aip.mymoviesdatabase.data.DataManager;
import com.manning.aip.mymoviesdatabase.model.Category;

import java.util.List;

public class CategoryTest extends Activity {

   private TextView currentCategories;
   private Button submit;

   @Override
   protected void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      setContentView(R.layout.category_test);

      this.currentCategories = (TextView) this.findViewById(R.id.current_categories);      
      
      this.submit = (Button) this.findViewById(R.id.submit);
      this.submit.setOnClickListener(new OnClickListener() {
         public void onClick(View v) {

         }
      });
      
      DataManager dataManager = new DataManager(this);
      // TODO wrap daos with manager
      CategoryDao categoryDao = new CategoryDao(dataManager.getDb());
      List<Category> cats = categoryDao.getAll();
      StringBuilder sb = new StringBuilder();
      for (Category cat : cats) {
         sb.append(cat.toString() + "\n");
      }
      currentCategories.setText(sb.toString());     
      
   }

   private boolean isTextViewEmpty(final TextView textView) {
      return !(textView != null && textView.getText() != null && textView.getText().toString() != null && !textView
               .getText().toString().equals(""));
   }
}
