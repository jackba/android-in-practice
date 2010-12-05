package com.manning.aip.fileexplorer;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Scanner;

public class InternalStorage extends Activity {

   // no utils here, just basic java.io

   // also be aware of getCacheDir, which writes to an internal
   // directory that the system may clean up
   
   private EditText input;
   private TextView output;
   private Button write;
   private Button read;   
   
   @Override
   public void onCreate(final Bundle icicle) {
      super.onCreate(icicle);
      this.setContentView(R.layout.internal_storage);

      this.input = (EditText) findViewById(R.id.internal_storage_input);
      this.output = (TextView) findViewById(R.id.internal_storage_output);

      this.write = (Button) findViewById(R.id.internal_storage_write_button);
      this.write.setOnClickListener(new OnClickListener() {
         public void onClick(final View v) {
            write();
         }
      });

      this.read = (Button) findViewById(R.id.internal_storage_read_button);
      this.read.setOnClickListener(new OnClickListener() {
         public void onClick(final View v) {
            read();
         }
      });
   }

   private void write() {
      FileOutputStream fos = null;
      try {
         // note that there are many modes you can use
         fos = openFileOutput("test.txt", Context.MODE_PRIVATE);
         fos.write(input.getText().toString().getBytes());
         Toast.makeText(this, "File written", Toast.LENGTH_SHORT).show();
         input.setText("");
      } catch (FileNotFoundException e) {
         Log.e(Constants.LOG_TAG, "File not found", e);
      } catch (IOException e) {
         Log.e(Constants.LOG_TAG, "IO problem", e);
      } finally {
         try {
            fos.close();
         } catch (IOException e) {
            // ignore, and take the verbosity punch from Java ;)
         }
      }
   }

   private void read() {
      FileInputStream fis = null;
      Scanner scanner = null;
      StringBuilder sb = new StringBuilder();
      try {
         fis = openFileInput("test.txt");
         // scanner does mean one more object, but it's easier to work with
         scanner = new Scanner(fis);
         while (scanner.hasNextLine()) {
            sb.append(scanner.nextLine() + System.getProperty("line.separator"));
         }
         Toast.makeText(this, "File read", Toast.LENGTH_SHORT).show();
      } catch (FileNotFoundException e) {
         Log.e(Constants.LOG_TAG, "File not found", e);
      } finally {
         if (fis != null) {
            try {
               fis.close();
            } catch (IOException e) {
               // ignore, and take the verbosity punch from Java ;)
            }
         }
         if (scanner != null) {
            scanner.close();
         }
      }
      output.setText(sb.toString());
   }
}
