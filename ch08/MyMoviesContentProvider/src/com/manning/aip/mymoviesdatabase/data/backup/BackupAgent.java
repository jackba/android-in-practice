package com.manning.aip.mymoviesdatabase.data.backup;

import android.app.backup.BackupAgentHelper;
import android.app.backup.BackupDataInput;
import android.app.backup.BackupDataOutput;
import android.app.backup.SharedPreferencesBackupHelper;
import android.os.ParcelFileDescriptor;

import com.manning.aip.mymoviesdatabase.util.FileUtil;

import java.io.IOException;

public class BackupAgent extends BackupAgentHelper {

   private static final String DEFAULT_SHARED_PREFS_KEY = "com.manning.aip.mymoviesdatabase_preferences";

   private static final String PREFS_BACKUP_KEY = "defaultprefs";

   @Override
   public void onCreate() {
      SharedPreferencesBackupHelper prefsHelper =
               new SharedPreferencesBackupHelper(this, BackupAgent.DEFAULT_SHARED_PREFS_KEY);
      addHelper(BackupAgent.PREFS_BACKUP_KEY, prefsHelper);

      ///FileBackupHelper csvFileHelper = new FileBackupHelper(this, DataConstants.EXPORT_FILENAME);
      ///addHelper(CSV_FILE_BACKUP_KEY, csvFileHelper);
   }

   @Override
   public void onBackup(ParcelFileDescriptor oldState, BackupDataOutput data, ParcelFileDescriptor newState)
      throws IOException {
      // hold the lock while the FileBackupHelper performs backup
      synchronized (FileUtil.DATA_LOCK) {
         super.onBackup(oldState, data, newState);
      }
   }

   @Override
   public void onRestore(BackupDataInput data, int appVersionCode, ParcelFileDescriptor newState) throws IOException {
      // hold the lock while the FileBackupHelper restores the file
      synchronized (FileUtil.DATA_LOCK) {
         super.onRestore(data, appVersionCode, newState);
      }
   }
}
