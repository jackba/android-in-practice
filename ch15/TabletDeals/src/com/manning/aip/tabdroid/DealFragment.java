package com.manning.aip.tabdroid;

import com.manning.aip.dealdroid.DealsApp;
import com.manning.aip.dealdroid.Util;
import com.manning.aip.dealdroid.model.Item;

import android.app.Fragment;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

public class DealFragment extends Fragment {

	DealsApp app;
	private ProgressBar progressBar;
	
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
    	app = (DealsApp) getActivity().getApplication();
    	
    	View dealView = inflater.inflate(R.layout.dealdetails, container, false);
        progressBar = (ProgressBar) dealView.findViewById(R.id.progress);
        progressBar.setIndeterminate(true);
        Item item = app.currentItem;

        if (item != null) {
           populateDealView(dealView, item);
        }
    	return dealView;
    }

	private void populateDealView(View dealView, Item item) {
		ImageView icon = (ImageView) dealView.findViewById(R.id.details_icon);
           icon.setImageResource(R.drawable.placeholder);
           new RetrieveImageTask(icon).execute(item.pic175Url);

           TextView title = (TextView) dealView.findViewById(R.id.details_title);
           title.setText(item.title);

           CharSequence pricePrefix = getText(R.string.deal_details_price_prefix);
           TextView price = (TextView) dealView.findViewById(R.id.details_price);
           price.setText(pricePrefix + item.convertedCurrentPrice);

           TextView msrp = (TextView) dealView.findViewById(R.id.details_msrp);
           msrp.setText(item.msrp);

           TextView quantity = (TextView) dealView.findViewById(R.id.details_quantity);
           quantity.setText(Integer.toString(item.quantity));

           TextView quantitySold = (TextView) dealView.findViewById(R.id.details_quantity_sold);
           quantitySold.setText(Integer.toString(item.quantitySold));

           TextView location = (TextView) dealView.findViewById(R.id.details_location);
           location.setText(item.location);
	}
    
    public void showCurrentItem(){
    	Item item = app.currentItem;
    	View dealView = getView();
    	populateDealView(dealView, item);
    }
    
    private class RetrieveImageTask extends AsyncTask<String, Void, Bitmap> {
        private ImageView imageView;

        public RetrieveImageTask(final ImageView imageView) {
           this.imageView = imageView;
        }

        @Override
        protected Bitmap doInBackground(final String... args) {
           Bitmap bitmap = Util.retrieveBitmap(args[0]);
           return bitmap;
        }

        @Override
        protected void onPostExecute(final Bitmap bitmap) {
        	progressBar.setVisibility(View.GONE);
           if (bitmap != null) {
              imageView.setImageBitmap(bitmap);
              imageView.setVisibility(View.VISIBLE);
           }
        }
     }
}
