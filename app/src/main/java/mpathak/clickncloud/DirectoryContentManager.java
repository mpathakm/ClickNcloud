package mpathak.clickncloud;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

import javax.inject.Inject;

/**
 * Created by mpathak on 4/1/2018.
 */

public class DirectoryContentManager
{
    public ArrayList<File> readImagesFromLocation(File root)
    {
        ArrayList<File> returnImages = new ArrayList<File>();
        File[] images = root.listFiles();

        for (int i = 0; i<images.length; i++)
        {
            if(images[i].isDirectory())
            {
                returnImages.addAll(readImagesFromLocation(images[i]));
            }
            else
            {
                if (images[i].getName().endsWith(".jpg") || images[i].getName().endsWith(".JPG") || images[i].getName().endsWith(".JPEG") || images[i].getName().endsWith(".jpeg"))
                {
                    returnImages.add(images[i]);
                }
            }
        }

        return returnImages;
    }

    public Bitmap converImageToThumbNail(File image)
    {
        return ImageCompressor.convertImageToThumbnail(image.getAbsolutePath());
    }

    public void converImagesToThumbnails(ArrayList<File> images)
    {
        for(int i=0; i<images.size(); i++) {
            Bitmap bitmapImage = ImageCompressor.convertImageToThumbnail(images.get(i).getAbsolutePath());

            String filename = images.get(i).getAbsolutePath().substring(images.get(i).getAbsolutePath().lastIndexOf("/") + 1);
            ImageCompressor.saveThumbnailInThumbnailDirectory(bitmapImage, filename);
        }
    }

    public boolean deleteThumbFromDirectory(String thumbName)
    {
        boolean delete = false;
        File thumbnailFile = new File(Environment.getExternalStorageDirectory(), Constants.Thumbnails + "/" + thumbName);

        if(thumbnailFile.exists())
        {
            delete = thumbnailFile.delete();
        }

        return delete;
    }

    public void CreateApplicationDirectories()
    {
        File applicationRootDirectory = new File(Environment.getExternalStorageDirectory(), Constants.Application_Root_Directory);

        if(!applicationRootDirectory.exists())
        {
            applicationRootDirectory.mkdir();
        }

        File thumbnailsDirectory = new File(Environment.getExternalStorageDirectory(), Constants.Thumbnails);

        if(!thumbnailsDirectory.exists())
        {
            thumbnailsDirectory.mkdir();
        }

    }

    @Deprecated
    private void copyFile(String inputImageFilePath, String outputFilePath) {

        InputStream in = null;
        OutputStream out = null;
        try
        {

            //create output directory if it doesn't exist
            File dir = new File (outputFilePath);
            if (!dir.exists())
            {
                dir.mkdirs();
            }

            String inputFileName = inputImageFilePath.substring(inputImageFilePath.lastIndexOf("/")+1);
            in = new FileInputStream(inputImageFilePath);
            out = new FileOutputStream(outputFilePath + "/"+ inputFileName);

            byte[] buffer = new byte[1024];
            int read;
            while ((read = in.read(buffer)) != -1) {
                out.write(buffer, 0, read);
            }
            in.close();
            in = null;

            // write the output file (You have now copied the file)
            out.flush();
            out.close();
            out = null;

        }
        catch (FileNotFoundException fileNotFoundException)
        {
            Log.e("DrctryCntntMngr:cpyFile", fileNotFoundException.getMessage());
        }
        catch (Exception e)
        {
            Log.e("tag", e.getMessage());
        }

    }
}
