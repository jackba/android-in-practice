package com.manning.aip;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Handler.Callback;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

public class MyMovies extends ListActivity implements Callback {

    private MovieAdapter adapter;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.main);

        ListView listView = getListView();

        Button backToTop = (Button) getLayoutInflater().inflate(R.layout.list_footer, null);
        backToTop.setCompoundDrawablesWithIntrinsicBounds(getResources().getDrawable(
                android.R.drawable.ic_menu_upload), null, null, null);
        listView.addFooterView(backToTop, null, true);

        this.adapter = new MovieAdapter(this);
        listView.setAdapter(this.adapter);
        listView.setItemsCanFocus(false);

        new UpdateNoticeTask(new Handler(this)).execute();
    }

    public void backToTop(View view) {
        getListView().setSelection(0);
    }

    protected void onListItemClick(ListView l, View v, int position, long id) {
        this.adapter.toggleMovie(position);
        this.adapter.notifyDataSetInvalidated();
    }

    public boolean handleMessage(Message msg) {
        String updateNotice = msg.getData().getString("text");
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle("What's new");
        dialog.setMessage(updateNotice);
        dialog.setIcon(android.R.drawable.ic_dialog_info);
        dialog.setPositiveButton(getString(android.R.string.ok), new OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        dialog.show();
        return false;
    }
}