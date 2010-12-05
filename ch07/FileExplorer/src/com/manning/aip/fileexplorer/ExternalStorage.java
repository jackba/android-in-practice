package com.manning.aip.fileexplorer;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.manning.aip.fileexplorer.util.FileUtil;

import java.io.File;

public class ExternalStorage extends Activity {

   // wrap some operations that are likely to be needed in more than one place in FileUtil
   
   private EditText input;
   private TextView output;
   private Button write;
   private Button read;

   @Override
   public void onCreate(final Bundle icicle) {
      super.onCreate(icicle);
      this.setContentView(R.layout.external_storage);

      this.input = (EditText) findViewById(R.id.external_storage_input);
      this.output = (TextView) findViewById(R.id.external_storage_output);

      this.write = (Button) findViewById(R.id.external_storage_write_button);
      this.write.setOnClickListener(new OnClickListener() {
         public void onClick(final View v) {
            write();
         }
      });

      this.read = (Button) findViewById(R.id.external_storage_read_button);
      this.read.setOnClickListener(new OnClickListener() {
         public void onClick(final View v) {
            read();
         }
      });
   }

   private void write() {
      if (FileUtil.isExternalStorageWritable()) {
         File dir = FileUtil.getExternalFilesDirAllApiLevels(this.getPackageName());
         File file = new File(dir, "test.txt");
         FileUtil.writeStringAsFile(input.getText().toString(), file);
         Toast.makeText(this, "File written", Toast.LENGTH_SHORT).show();
         input.setText("");
      } else {
         Toast.makeText(this, "External storage not writable", Toast.LENGTH_SHORT).show();
      }
   }

   private void read() {
      if (FileUtil.isExternalStorageReadable()) {
         File dir = FileUtil.getExternalFilesDirAllApiLevels(this.getPackageName());
         File file = new File(dir, "test.txt");
         if (file.exists() && file.canRead()) {
            output.setText(FileUtil.readFileAsString(file));  
            Toast.makeText(this, "File read", Toast.LENGTH_SHORT).show();
         } else {
            Toast.makeText(this, "Unable to read file: " + file.getAbsolutePath(), Toast.LENGTH_SHORT).show();  
         }
      } else {
         Toast.makeText(this, "External storage not readable", Toast.LENGTH_SHORT).show();
      }
   }
}
