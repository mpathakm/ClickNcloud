package mpathak.clickncloud;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ThumbnailUtils;
import android.os.Environment;

import java.io.File;
import java.io.FileOutputStream;

/**
 * Created by mpathak on 4/1/2018.
 */

public class ImageCompressor
{
    public static Bitmap convertImageToThumbnail(String imagePath)
    {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = 10;

        Bitmap bitmap = BitmapFactory.decodeFile(imagePath, options);

        Bitmap extractedThumbnailBitmap = ThumbnailUtils.extractThumbnail(bitmap, 500,500);

        return extractedThumbnailBitmap;
    }

    public static void saveThumbnailInThumbnailDirectory(Bitmap finalBitmap, String imageName)
    {
        File thumbnailsDirectory = new File(Environment.getExternalStorageDirectory(), Constants.Thumbnails);

        if(!thumbnailsDirectory.exists())
        {
            throw new Resources.NotFoundException("The thumbnail directory could not be found, please create one");
        }

        File file = new File (thumbnailsDirectory, imageName);
        if (file.exists ())
        {
            file.delete ();
        }
        try {
            FileOutputStream out = new FileOutputStream(file);
            finalBitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
            out.flush();
            out.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
