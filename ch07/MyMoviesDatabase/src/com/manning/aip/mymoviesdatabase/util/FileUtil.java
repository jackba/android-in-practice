package com.manning.aip.mymoviesdatabase.util;

import android.util.Log;

import com.manning.aip.mymoviesdatabase.Constants;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.nio.channels.FileChannel;

/**
 * FileUtil methods. 
 * 
 * @author ccollins
 *
 */
public final class FileUtil {

   // Object for intrinsic lock (per docs 0 length array "lighter" than a normal Object)
   public static final Object[] DATA_LOCK = new Object[0];

   private FileUtil() {
   }

   // nio is there too
   /**
    * Copy file.
    * 
    * @param src
    * @param dst
    * @return
    */
   public static boolean copyFile(final File src, final File dst) {
      boolean result = false;
      FileChannel inChannel = null;
      FileChannel outChannel = null;
      synchronized (FileUtil.DATA_LOCK) {
         try {
            inChannel = new FileInputStream(src).getChannel();
            outChannel = new FileOutputStream(dst).getChannel();
            inChannel.transferTo(0, inChannel.size(), outChannel);
            result = true;
         } catch (IOException e) {

         } finally {
            if (inChannel != null && inChannel.isOpen()) {
               try {
                  inChannel.close();
               } catch (IOException e) {
                  // ignore
               }
            }
            if (outChannel != null && outChannel.isOpen()) {
               try {
                  outChannel.close();
               } catch (IOException e) {
                  // ignore
               }
            }
         }
      }
      return result;
   }

   /**
    * Replace entire File with contents of String.
    * 
    * @param fileContents
    * @param file
    * @return
    */
   public static boolean writeStringAsFile(final String fileContents, final File file) {
      boolean result = false;
      try {
         synchronized (FileUtil.DATA_LOCK) {
            if (file != null) {
               file.createNewFile(); // ok if returns false, overwrite
               // FileWriter will use default encoding
               // (could use FileOutputStream to control encoding and guarantee flush, but don't need that here)
               Writer out = new BufferedWriter(new FileWriter(file), 1024);
               out.write(fileContents);
               out.close(); // close will flush and close (no guarantee of sync though)
               result = true;
            }
         }
      } catch (IOException e) {
         Log.e(Constants.LOG_TAG, "Error writing string data to file " + e.getMessage(), e);
      }
      return result;
   }

   /**
    * Append String to end of File.
    * 
    * @param appendContents
    * @param file
    * @return
    */
   public static boolean appendStringToFile(final String appendContents, final File file) {
      boolean result = false;
      try {
         synchronized (FileUtil.DATA_LOCK) {
            if ((file != null) && file.canWrite()) {
               file.createNewFile(); // ok if returns false, overwrite
               // FileWriter will use default encoding, and easy to append
               Writer out = new BufferedWriter(new FileWriter(file, true), 1024);
               out.write(appendContents);
               out.close();
               result = true;
            }
         }
      } catch (IOException e) {
         Log.e(Constants.LOG_TAG, "Error appending string data to file " + e.getMessage(), e);
      }
      return result;
   }

   /**
    * Call sync on a FileOutputStream to ensure it is written to disk immediately
    * (write, flush, close, etc, don't guarantee physical disk write on buffered file systems).
    * 
    * @param stream
    * @return
    */
   public static boolean syncStream(FileOutputStream fos) {
      try {
         if (fos != null) {
            fos.getFD().sync();
         }
         return true;
      } catch (IOException e) {
         Log.e(Constants.LOG_TAG, "Error syncing stream " + e.getMessage(), e);
      }
      return false;
   }
}
