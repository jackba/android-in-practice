package com.manning.aip.share;

import com.manning.aip.mash.IMashService;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.ImageView;

public class ShareRpcActivity extends Activity {
    Uri photoUri0;
    IMashService mashService;
    Button mashButton;
    int bindCount = 0;
    ServiceConnection conn = new ServiceConnection(){
        @Override
        public void onServiceConnected(ComponentName className, IBinder service) {
            mashService = IMashService.Stub.asInterface(service);
            mashButton.setEnabled(true);
        }
        @Override
        public void onServiceDisconnected(ComponentName className) {
            mashService = null;
            mashButton.setEnabled(false);
        }
    };
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.share_rpc);
        Button button = (Button) findViewById(R.id.btn0);
        button.setOnClickListener(new OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent request = new Intent(Intent.ACTION_GET_CONTENT);
                request.setType("image/*");
                startActivityForResult(request, 0);
            }
        });
        mashButton = (Button) findViewById(R.id.button);
        CheckBox syncBox = (CheckBox) findViewById(R.id.syncBox);
        syncBox.setOnCheckedChangeListener(new OnCheckedChangeListener(){
            @Override
            public void onCheckedChanged(CompoundButton button, boolean checked) {
                if (checked){
                    mashButton.setEnabled(false);
                    bindService(new Intent("com.manning.aip.mash.ACTION"), 
                                conn, 
                                Context.BIND_AUTO_CREATE);
                    bindCount += 1;
                } else {
                    bindCount -= 1;
                    if (bindCount == 0){
                        unbindService(conn);
                    }
                    mashButton.setEnabled(true);
                }
            }
        });
        mashButton.setOnClickListener(new OnClickListener(){

            @Override
            public void onClick(View v) {
                EditText input0 = (EditText) findViewById(R.id.input0);
                float scaleX = Float.parseFloat(input0.getText().toString());
                EditText input1 = (EditText) findViewById(R.id.input1);
                float scaleY = Float.parseFloat(input1.getText().toString());
                EditText input2 = (EditText) findViewById(R.id.input2);
                float angle = Float.parseFloat(input2.getText().toString());
                Uri result;
                if (bindCount > 0){
                    try {
                        result = mashService.mash(photoUri0, scaleX, scaleY, angle);
                        ImageView image = (ImageView) findViewById(R.id.image);
                        image.setImageURI(result);
                    } catch (RemoteException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                } else {
                    Intent request = new Intent("com.manning.aip.mash.ACTION");
                    request.putExtra("com.manning.aip.mash.EXTRA_PHOTO", photoUri0);
                    request.putExtra("com.manning.aip.mash.EXTRA_SCALE_X", scaleX);
                    request.putExtra("com.manning.aip.mash.EXTRA_SCALE_Y", scaleY);
                    request.putExtra("com.manning.aip.mash.EXTRA_ANGLE", angle);
                    startService(request);
                }
            }
        });
        mashButton.setEnabled(true);
        BroadcastReceiver receiver = new BroadcastReceiver(){
            @Override
            public void onReceive(Context context, Intent intent) {
                Uri result = intent.getParcelableExtra("com.manning.aip.mash.EXTRA_RESULT");
                ImageView image = (ImageView) findViewById(R.id.image);
                image.setImageURI(result);
            }
        };
        IntentFilter filter = new IntentFilter();
        filter.addAction("com.manning.aip.mash.ACTION_RESPONSE");
        this.registerReceiver(receiver, filter);
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
        } 
    }
    
    public static void start(Context context){
        Intent i = new Intent(context, ShareRpcActivity.class);
        context.startActivity(i);
    }
}
