package com.manning.aip.mymoviesdatabase;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.AdapterView.OnItemClickListener;

import com.manning.aip.mymoviesdatabase.data.DataManager;
import com.manning.aip.mymoviesdatabase.model.Category;

import java.util.List;

public class CategoryManager extends Activity {

   private static final int EDIT = 0;
   private static final int DELETE = 1;
   
   private MyMoviesApp application;
   private DataManager dataManager;

   private List<Category> categories;
   private ArrayAdapter<Category> adapter;

   private ListView listView;
   private EditText categoryNew;
   private Button submit;

   @Override
   protected void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      setContentView(R.layout.category_manager);

      application = (MyMoviesApp) getApplication();

      dataManager = application.getDataManager();
      categories = dataManager.getCategoryDao().getAll();
      listView = (ListView) this.findViewById(R.id.category_manager_list);
      listView.setEmptyView(findViewById(R.id.category_manager_empty));
      adapter = new ArrayAdapter<Category>(this, android.R.layout.simple_list_item_1, categories);
      listView.setAdapter(adapter);
      listView.setOnItemClickListener(new OnItemClickListener() {
         public void onItemClick(final AdapterView<?> parent, final View v, final int index, final long id) {
            Toast.makeText(CategoryManager.this, "TODO, long press for actions", Toast.LENGTH_SHORT).show();
         }
      });      
      registerForContextMenu(listView);

      categoryNew = (EditText) findViewById(R.id.category_new);
      submit = (Button) findViewById(R.id.category_new_submit);
      this.submit.setOnClickListener(new OnClickListener() {
         public void onClick(View v) {
            if (!isTextViewEmpty(categoryNew)) {
               Category category = new Category(categoryNew.getText().toString());
               dataManager.getCategoryDao().save(category);
               // example of ADD instead of editing backing collection and notify
               adapter.add(category);
               Toast.makeText(CategoryManager.this, "Added category " + category.getName(), Toast.LENGTH_SHORT);
            }
         }
      });
   }  

   @Override
   public void onCreateContextMenu(final ContextMenu menu, final View v, final ContextMenuInfo menuInfo) {
      super.onCreateContextMenu(menu, v, menuInfo);
      menu.add(0, EDIT, 0, "Edit Category");
      menu.add(0, DELETE, 1, "Delete Category");
      menu.setHeaderTitle("Action");
   }

   @Override
   public boolean onContextItemSelected(final MenuItem item) {
      AdapterContextMenuInfo info = (AdapterContextMenuInfo) item.getMenuInfo();      
      final Category category = categories.get(info.position);
      switch (item.getItemId()) {
         case EDIT:
            Toast.makeText(CategoryManager.this, "TODO EDIT " + category.getName(), Toast.LENGTH_SHORT);
            return true;
         case DELETE:
            new AlertDialog.Builder(CategoryManager.this).setTitle("Delete Category?").setMessage(category.getName())
                     .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        public void onClick(final DialogInterface d, final int i) {
                           dataManager.getCategoryDao().delete(category);
                           adapter.remove(category);
                        }
                     }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                        public void onClick(final DialogInterface d, final int i) {
                        }
                     }).show();
            return true;
         default:
            return super.onContextItemSelected(item);
      }
   }

   private boolean isTextViewEmpty(final TextView textView) {
      return !(textView != null && textView.getText() != null && textView.getText().toString() != null && !textView
               .getText().toString().equals(""));
   }
}
