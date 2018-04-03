package mpathak.clickncloud;

import android.content.Context;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

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

    public void copyImagesToWaitingToUploadFolder(ArrayList<File> images, Context context)
    {
        File waitingToUploadDirectory = new File(Environment.getExternalStorageDirectory(), context.getResources().getString(R.string.waiting_to_upload));
        if(!waitingToUploadDirectory.exists())
        {
            waitingToUploadDirectory.mkdir();
        }

        for(int i=0; i<images.size(); i++)
        {
            ImageCompressor.convertImageToThumbnail(images.get(i).getAbsolutePath(), context);
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
