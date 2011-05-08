package com.manning.aip.proguard;

import org.apache.commons.lang.StringUtils;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

public class MainActivity extends Activity {

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        String toast = StringUtils.repeat("Hello ProGuard! ", 3);
        Toast.makeText(this, toast, Toast.LENGTH_SHORT).show();

        new Bomb().explode();
    }

    public void myClickHandler(View customView) {
        Toast.makeText(this, "clicked", Toast.LENGTH_SHORT).show();
    }

    public void unusedMethod() {
        System.out.println("I'm not used anywhere!");
    }
}