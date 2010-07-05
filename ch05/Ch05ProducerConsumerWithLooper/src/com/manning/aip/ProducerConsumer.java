package com.manning.aip;

import java.util.Random;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

public class ProducerConsumer extends Activity {

   private Handler handler;

   private class Consumer extends Thread {

      @Override
      public void run() {

         Looper.prepare();

         handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
               int number = msg.what;
               if (number % 2 == 0) {
                  Log.d("Consumer", number + " is divisible by 2");
               } else {
                  Log.d("Consumer", number + " is not divisible by 2");
               }
            }
         };

         Looper.loop();
      }
   }

   private class Producer extends Thread {

      public Producer(String name) {
         super(name);
      }

      @Override
      public void run() {
         Random random = new Random();
         while (true) {
            int number = random.nextInt(100);
            Log.d("Producer " + getName(), Integer.toString(number));
            handler.sendEmptyMessage(number);
            try {
               Thread.sleep(500);
            } catch (InterruptedException e) {
            }
         }
      }
   }

   @Override
   public void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);

      new Consumer().start();
      new Producer("A").start();
      new Producer("B").start();
   }
}