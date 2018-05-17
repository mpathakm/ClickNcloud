package mpathak.clickncloud;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Environment;
import android.os.FileObserver;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.channels.FileChannel;

/**
 * Created by mpathak on 4/11/2018.
 */

public class DirectoryObserver extends FileObserver {
    private static final String TAG = "DIRECTORY_OBERSVER";
    private String directoryPath;
    private DatabaseHelper _databaseHelper;
    private DirectoryContentManager _directoryContentManager;
    private Context _context;

    public DirectoryObserver(String path, Context context) {
        super(path);

        Log.i(TAG, "Something Happening " + path);
        _context = context;
        directoryPath = path;
        _directoryContentManager = new DirectoryContentManager();
        _databaseHelper = new DatabaseHelper(context);
    }

    @Override
    public void onEvent(int event, @Nullable String fileName) {
        if (fileName == null) {
            return;
        }
        //a new file or subdirectory was created under the monitored directory
        if ((FileObserver.CREATE & event)!=0) {
            Log.i(TAG, "A file is added to the path " + fileName);

            File newlyAddedFile = new File(directoryPath + "/"+ fileName);

            ImageEntity imageEntity = new ImageEntity();
            imageEntity.Name = fileName;
            imageEntity.AbsolutePath = newlyAddedFile.getAbsolutePath();
            imageEntity.QueuedToDelete = 0;
            imageEntity.IsUploaded = 0;
            imageEntity.OriginalSize = newlyAddedFile.length();
            imageEntity.CloudProvider = "Gmail";

            _databaseHelper.insertImageData(imageEntity);
            Bitmap thumb = _directoryContentManager.converImageToThumbNail(newlyAddedFile);
            String filename = newlyAddedFile.getAbsolutePath().substring(newlyAddedFile.getAbsolutePath().lastIndexOf("/") + 1);
            ImageCompressor.saveThumbnailInThumbnailDirectory(thumb, filename);

            exportDatabse("clickncloud.db");
        }

        //[todo: consider combine this one with one below]
        //a file was deleted from the monitored directory
        if ((FileObserver.DELETE & event)!=0) {
            Log.i(TAG, "A file is deleted to the path " + fileName);
            _databaseHelper.deleteEntryByname(fileName);

            boolean result = _directoryContentManager.deleteThumbFromDirectory(fileName);

            if(result)
            {
                Log.i(TAG, "Delete Successful");
            }
            else
            {
                Log.e(TAG, "Can not find the thumbnail which needs to be deleted");
            }

            exportDatabse("clickncloud.db");
        }

        //Below this are useless for now
        //a file or directory was opened
        /*if ((FileObserver.OPEN & event)!=0) {
            Log.i(TAG, "A file is opened to the path " + path);
        }
        //data was read from a file
        if ((FileObserver.ACCESS & event)!=0) {
            Log.i(TAG, "A file is accessed to the path " + path);
        }
        //data was written to a file
        if ((FileObserver.MODIFY & event)!=0) {
            Log.i(TAG, "A file is modified to the path " + path);
        }
        //someone has a file or directory open read-only, and closed it
        if ((FileObserver.CLOSE_NOWRITE & event)!=0) {
            Log.i(TAG, "A file is closed to the path " + path);
        }
        //someone has a file or directory open for writing, and closed it
        if ((FileObserver.CLOSE_WRITE & event)!=0) {
            Log.i(TAG, "A file is open closed to the path " + path);
        }

        //the monitored file or directory was deleted, monitoring effectively stops
        if ((FileObserver.DELETE_SELF & event)!=0) {
            Log.i(TAG, "A file is deletd self to the path " + path);
        }
        //a file or subdirectory was moved from the monitored directory
        if ((FileObserver.MOVED_FROM & event)!=0) {
            Log.i(TAG, "A file is MOVED_FROM to the path " + path);
        }
        //a file or subdirectory was moved to the monitored directory
        if ((FileObserver.MOVED_TO & event)!=0) {
            Log.i(TAG, "A file is MOVED_TO to the path " + path);
        }
        //the monitored file or directory was moved; monitoring continues
        if ((FileObserver.MOVE_SELF & event)!=0) {
            Log.i(TAG, "A file is MOVE_SELF to the path " + path);
        }
        //Metadata (permissions, owner, timestamp) was changed explicitly
        if ((FileObserver.ATTRIB & event)!=0) {
            Log.i(TAG, "A file is ATTRIB to the path " + path);
        }*/

    }


    public void exportDatabse(String databaseName)
    {
        try {
            File sd = new File(Environment.getExternalStorageDirectory(),Constants.Application_Root_Directory);
            File data = Environment.getDataDirectory();

            if (sd.canWrite()) {
                String currentDBPath = "//data//"+"mpathak.clickncloud"+"//databases//"+databaseName+"";
                String backupDBPath = "backup_clickncloud.db";
                File currentDB = new File(data, currentDBPath);
                File backupDB = new File(sd, backupDBPath);

                if (currentDB.exists()) {
                    FileChannel src = new FileInputStream(currentDB).getChannel();
                    FileChannel dst = new FileOutputStream(backupDB).getChannel();
                    dst.transferFrom(src, 0, src.size());
                    src.close();
                    dst.close();
                }
            }
        } catch (Exception e) {

        }
    }
}
