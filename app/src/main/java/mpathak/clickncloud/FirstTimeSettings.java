package mpathak.clickncloud;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Environment;
import java.io.File;
import java.util.ArrayList;

/**
 * Created by mpathak on 4/18/2018.
 */

public class FirstTimeSettings {

    private DirectoryContentManager _directoryContentManager;
    private Context _context;
    private DatabaseHelper _dbHelper;

    public FirstTimeSettings(Context context)
    {
        _dbHelper = new DatabaseHelper(context);//Creating the database
        _directoryContentManager = new DirectoryContentManager();
        _context = context;

    }

    public void initialize()
    {
        _directoryContentManager.CreateApplicationDirectories();

        ArrayList<File> images = _directoryContentManager.readImagesFromLocation(new File(Environment.getExternalStorageDirectory(),Constants.Camera_Directory));

        for(int i = 0; i<images.size(); i++)
        {
            ImageEntity imageEntity = new ImageEntity();
            imageEntity.Name = images.get(i).getName();
            imageEntity.AbsolutePath = images.get(i).getAbsolutePath();
            imageEntity.IsUploaded = 0;
            imageEntity.QueuedToDelete = 0;
            imageEntity.CloudProvider = "Gmail";
            imageEntity.OriginalSize = images.get(i).length();

            _dbHelper.insertImageData(imageEntity);
            Bitmap thumb = _directoryContentManager.converImageToThumbNail(images.get(i));
            String filename = images.get(i).getAbsolutePath().substring(images.get(i).getAbsolutePath().lastIndexOf("/") + 1);
            ImageCompressor.saveThumbnailInThumbnailDirectory(thumb, filename);
        }
    }
}
