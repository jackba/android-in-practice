package com.manning.aip.brewmap;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.method.LinkMovementMethod;
import android.text.util.Linkify;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;


public abstract class BrewMapActivity extends Activity {

   private static final int MENU_ABOUT = 1;

   private SpannableString aboutString;
   
   protected BrewMapApp app;

   @Override
   protected void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      app = (BrewMapApp) getApplication();
      aboutString = new SpannableString(getString(R.string.aboutString));
      Linkify.addLinks(aboutString, Linkify.ALL);
   }

   @Override
   public boolean onCreateOptionsMenu(Menu menu) {
      menu.add(0, MENU_ABOUT, 0, "About").setIcon(android.R.drawable.ic_menu_info_details);
      return true;
   }

   @Override
   public boolean onOptionsItemSelected(MenuItem item) {
      switch (item.getItemId()) {
         case MENU_ABOUT:
            AlertDialog dialog =
                     new AlertDialog.Builder(BrewMapActivity.this).setTitle("About BrewMap").setMessage(aboutString)
                              .setNeutralButton("Ok", new DialogInterface.OnClickListener() {
                                 public void onClick(final DialogInterface d, final int i) {
                                 }
                              }).create();
            dialog.show();
            // make the Linkify'ed aboutString clickable
            ((TextView) dialog.findViewById(android.R.id.message)).setMovementMethod(LinkMovementMethod.getInstance());
            break;
      }
      return false;
   }
}