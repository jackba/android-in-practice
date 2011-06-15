package com.manning.aip.share;

import java.io.FileNotFoundException;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;

public class ShareActivity extends Activity {
    Uri photoUri0;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.share);
        Button button = (Button) findViewById(R.id.btn0);
        button.setOnClickListener(new OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent request = new Intent(Intent.ACTION_GET_CONTENT);
                request.setType("image/*");
                startActivityForResult(request, 0);
            }
        });
        Button button1 = (Button) findViewById(R.id.btn1);
        button1.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent request = new Intent("com.manning.aip.mash.ACTION");
                request.addCategory(Intent.CATEGORY_DEFAULT);
                request.putExtra("com.manning.aip.mash.EXTRA_PHOTO", photoUri0);
                startActivityForResult(request, 1);
            }
        });
        Button next = (Button) findViewById(R.id.next);
        next.setOnClickListener(new OnClickListener(){

            @Override
            public void onClick(View arg0) {
                ShareRpcActivity.start(ShareActivity.this);
            }
         });
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 0){
            photoUri0 = (Uri) data.getParcelableExtra(Intent.EXTRA_STREAM);
            if (photoUri0 == null && data.getData() != null){
                photoUri0 = data.getData();
            }
            ImageView imgView0 = (ImageView) findViewById(R.id.pic0);
            imgView0.setImageURI(photoUri0);       
        } else if (requestCode == 1){
            Uri photoUri1 = (Uri) data.getParcelableExtra("com.manning.aip.mash.EXTRA_RESULT");
            if (photoUri1 == null && data.getData() != null){
                photoUri1 = data.getData();
            }
            ImageView imgView1 = (ImageView) findViewById(R.id.pic1);
            imgView1.setImageURI(photoUri1);
        }
    }
}