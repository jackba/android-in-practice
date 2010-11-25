package com.manning.aip.mymoviesdatabase.data;

import android.os.Environment;

public class DataConstants {

   private static final String APP_PACKAGE_NAME = "com.manning.aip.mymoviesdatabase";

   private static final String EXTERNAL_DATA_DIR_NAME = "mymoviesdata";
   public static final String EXTERNAL_DATA_PATH =
            Environment.getExternalStorageDirectory() + "/" + DataConstants.EXTERNAL_DATA_DIR_NAME;

   public static final String DATABASE_NAME = "mymovies.db";
   public static final String DATABASE_PATH =
            Environment.getDataDirectory() + "/data/" + DataConstants.APP_PACKAGE_NAME + "/databases/"
                     + DataConstants.DATABASE_NAME;

   private DataConstants() {
   }
}
