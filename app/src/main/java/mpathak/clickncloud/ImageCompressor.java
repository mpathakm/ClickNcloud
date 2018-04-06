package mpathak.clickncloud;

import android.content.Context;
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
    public static void convertImageToThumbnail(String imagePath, Context context)
    {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = 10;

        Bitmap bitmap = BitmapFactory.decodeFile(imagePath, options);

        Bitmap extractedThumbnailBitmap = ThumbnailUtils.extractThumbnail(bitmap, 500,500);

        String filename = imagePath.substring(imagePath.lastIndexOf("/")+1);
        saveThumbnailInThumbnailDirectory(extractedThumbnailBitmap, filename, context);

        //return extractedThumbnailBitmap;
    }

    private static void saveThumbnailInThumbnailDirectory(Bitmap finalBitmap, String imageName, Context context)
    {
        File thumbnailsDirectory = new File(Environment.getExternalStorageDirectory(), context.getResources().getString(R.string.thumbnails));

        if(!thumbnailsDirectory.exists())
        {
            thumbnailsDirectory.mkdir();
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
