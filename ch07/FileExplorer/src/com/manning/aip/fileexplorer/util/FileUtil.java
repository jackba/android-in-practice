package com.manning.aip.fileexplorer.util;

import android.os.Environment;
import android.util.Log;

import com.manning.aip.fileexplorer.Constants;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
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

   // from the Android docs, these are the recommended paths
   private static final String EXT_STORAGE_PATH_PREFIX = "/Android/data/";
   private static final String EXT_STORAGE_FILES_PATH_SUFFIX = "/files/";
   private static final String EXT_STORAGE_CACHE_PATH_SUFFIX = "/cache/";

   // Object for intrinsic lock (per docs 0 length array "lighter" than a normal Object)
   public static final Object[] DATA_LOCK = new Object[0];

   private FileUtil() {
   }

   /**
    * Use Environment to check if external storage is writable.
    * 
    * @return
    */
   public static boolean isExternalStorageWritable() {
      return Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
   }

   /**
    * Use environment to check if external storage is readable.
    * 
    * @return
    */
   public static boolean isExternalStorageReadable() {
      if (isExternalStorageWritable()) {
         return true;
      }
      return Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED_READ_ONLY);
   }

   /**
    * Return the recommended external files directory, whether using API level 8 or lower.
    * (Uses getExternalStorageDirectory and then appends the recommended path.)
    * 
    * @param packageName
    * @return
    */
   public static File getExternalFilesDirAllApiLevels(final String packageName) {
      return FileUtil.getExternalDirAllApiLevels(packageName, EXT_STORAGE_FILES_PATH_SUFFIX);
   }
   
   /**
    * Return the recommended external cache directory, whether using API level 8 or lower.
    * (Uses getExternalStorageDirectory and then appends the recommended path.)
    * 
    * @param packageName
    * @return
    */
   public static File getExternalCacheDirAllApiLevels(final String packageName) {
      return FileUtil.getExternalDirAllApiLevels(packageName, EXT_STORAGE_CACHE_PATH_SUFFIX);
   }

   private static File getExternalDirAllApiLevels(final String packageName, final String suffixType) {
      File dir =
               new File(Environment.getExternalStorageDirectory() + EXT_STORAGE_PATH_PREFIX + packageName + suffixType);
      synchronized (FileUtil.DATA_LOCK) {
         try {
            dir.mkdirs();
            dir.createNewFile();
         } catch (IOException e) {
            Log.e(Constants.LOG_TAG, "Error creating file", e);
         }
      }
      return dir;
   }

   /**
    * Copy file, return true on success, false on failure.
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
    * Replace entire File with contents of String, return true on success, false on failure.
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
               Writer out = new BufferedWriter(new FileWriter(file), 1024);
               out.write(fileContents);
               out.close();
               result = true;
            }
         }
      } catch (IOException e) {
         Log.e(Constants.LOG_TAG, "Error writing string data to file " + e.getMessage(), e);
      }
      return result;
   }

   /**
    * Append String to end of File, return true on success, false on failure.
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
    * Read file as String, return null if file is not present or not readable.
    * 
    * @param file
    * @return
    */
   public static String readFileAsString(final File file) {
      StringBuilder sb = null;
      try {
         synchronized (FileUtil.DATA_LOCK) {
            if ((file != null) && file.canRead()) {
               sb = new StringBuilder();
               String line = null;
               BufferedReader in = new BufferedReader(new FileReader(file), 1024);
               while ((line = in.readLine()) != null) {
                  sb.append(line + System.getProperty("line.separator"));
               }
            }
         }
      } catch (IOException e) {
         Log.e(Constants.LOG_TAG, "Error reading file " + e.getMessage(), e);
      }
      if (sb != null) {
         return sb.toString();
      }
      return null;
   }
}
