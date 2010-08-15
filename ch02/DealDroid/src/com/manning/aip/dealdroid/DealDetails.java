package com.manning.aip.dealdroid;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

public class DealDetails extends Activity {

   private static final int NONE = 0;
   private static final int MAIL = 1;
   private static final int BROWSE = 2;
   private static final int SHARE = 3;

   private DealDroidApp app;

   @Override
   public void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      setContentView(R.layout.dealdetails);

      app = (DealDroidApp) getApplication();

      /*
      Bitmap bitmap = app.getIconCache().get(item.itemId);
      ImageView icon = (ImageView) findViewById(R.id.details_icon);
      icon.setImageBitmap(bitmap);      
      */
      
      TextView title = (TextView) findViewById(R.id.details_title);
      title.setText(app.getCurrentItem().title);
      
      CharSequence pricePrefix = getText(R.string.deal_details_price_prefix);
      TextView price = (TextView) findViewById(R.id.details_price);
      price.setText(pricePrefix + app.getCurrentItem().convertedCurrentPrice);

   }

   @Override
   public boolean onCreateOptionsMenu(Menu menu) {
      menu.add(NONE, MAIL, NONE, R.string.deal_details_mail_menu);
      menu.add(NONE, BROWSE, NONE, R.string.deal_details_browser_menu);
      menu.add(NONE, SHARE, NONE, R.string.deal_details_share_menu);
      return true;

   }

   @Override
   public boolean onOptionsItemSelected(MenuItem item) {
      switch (item.getItemId()) {
         case MAIL:
            mailDeal();
            break;
         case BROWSE:
            openDealInBrowser();
            break;
         case SHARE:
            shareDeal();
            break;
      }
      return false;
   }

   /*
   
   @Override
   public void onResume(){                                                  
     super.onResume();
     if (app.getFeed() == null){                                            
       progressDialog = ProgressDialog.show(this,                           
         getString(R.string.progress_dialog_title),                        
         getString(R.string.progress_dialog_message),                       
         true, false);
         Runnable task = new Runnable(){                                    
        @Override
        public void run() {
          try {
            feed = DealsFeedParser.parseFeed();
            app.setFeed(feed);
            app.setCurrentSection(feed);
            handler.sendEmptyMessage(RESULT_OK);
          } catch (Exception e) {
            Log.e("ShowDeals","Exception parsing Deals feed", e);
          }
        }   
         };
         Thread thread = new Thread(task);
        thread.start(); 
     }
     */

   private void mailDeal() {
      Intent i = new Intent(Intent.ACTION_SEND);
      i.setType("text/html");
      i.putExtra(Intent.EXTRA_SUBJECT, "Subject:");
      i.putExtra(Intent.EXTRA_TEXT, "Mail text for deal");
      try {
         startActivity(Intent.createChooser(i, "Send mail..."));
      } catch (android.content.ActivityNotFoundException ex) {
         Toast.makeText(DealDetails.this, "There are no email clients installed.", Toast.LENGTH_SHORT).show();
      }
   }

   private void openDealInBrowser() {
      Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse(app.getCurrentItem().dealUrl));
      startActivity(i);
   }

   private void shareDeal() {
      Intent i = new Intent(Intent.ACTION_SEND);
      i.setType("text/*");
      i.putExtra(Intent.EXTRA_SUBJECT, "Subject:");
      i.putExtra(Intent.EXTRA_TEXT, "Mail text for deal");
      startActivity(Intent.createChooser(i, "Share options"));
   }

}
