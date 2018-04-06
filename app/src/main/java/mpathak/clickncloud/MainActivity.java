package mpathak.clickncloud;

import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.channels.FileChannel;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity
{

GridView mainGrigView;
ArrayList<File> images;
    ArrayList<File> thumbImages;
    ImageEntity ImageEntity = new ImageEntity();
    DatabaseHelper dbHelper;

    ArrayList<ImageEntity> imageEntitiesToStoreInDb = new ArrayList<ImageEntity> ();

private DirectoryContentManager _directoryContentManager = new DirectoryContentManager();

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        dbHelper = new DatabaseHelper(this);//Creating the database

        //ToDo: delete the following line of deleting all entries
        dbHelper.deleteAllEntries();
        images = _directoryContentManager.readImagesFromLocation(new File(Environment.getExternalStorageDirectory(),getString(R.string.camera_directory)));

        for(int i = 0; i<images.size(); i++)
        {
            ImageEntity imageEntity = new ImageEntity();
            imageEntity.Name = images.get(i).getName();
            imageEntity.AbsolutePath = images.get(i).getAbsolutePath();
            imageEntity.IsUploaded = 0;
            imageEntity.QueuedToDelete = 0;
            imageEntity.CloudProvider = "Gmail";
            imageEntity.OriginalSize = images.get(i).length();

            imageEntitiesToStoreInDb.add(imageEntity);
        }

        _directoryContentManager.converImagesToThumbnails(images, this);
        thumbImages = _directoryContentManager.readImagesFromLocation(new File(Environment.getExternalStorageDirectory(),getString(R.string.thumbnails)));

        for(int i = 0; i < thumbImages.size(); i++)
        {
            for(int j = 0; j < imageEntitiesToStoreInDb.size(); j++)
            {
                if(imageEntitiesToStoreInDb.get(j).Name.equals(thumbImages.get(i).getName()))
                {
                    imageEntitiesToStoreInDb.get(j).ReducedSize = thumbImages.get(i).length();
                }
            }
        }

        dbHelper.insertImageEntities(imageEntitiesToStoreInDb);

        exportDatabse("clickncloud.db");

        mainGrigView = findViewById(R.id.maingridview);
        mainGrigView.setAdapter(new MainGridAdapter());
        mainGrigView.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                startActivity(new Intent(getApplicationContext(), FullImageShowActivity.class).putExtra("img", thumbImages.get(position).toString()));
            }
        });
    }

    public void exportDatabse(String databaseName)
    {
        try {
            File sd = new File(Environment.getExternalStorageDirectory(),getString(R.string.root_application_directory));//Environment.getExternalStorageDirectory();
            File data = Environment.getDataDirectory();

            if (sd.canWrite()) {
                String currentDBPath = "//data//"+getPackageName()+"//databases//"+databaseName+"";
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

    class MainGridAdapter extends BaseAdapter
    {
        @Override
        public int getCount() {
        return thumbImages.size();
    }

        @Override
        public Object getItem(int position) {
        return thumbImages.get(position);
    }

        @Override
        public long getItemId(int position) {//When using database
        return 0;
    }

        @Override
        public View getView(int position, View convertView, ViewGroup parent)
        {
            convertView = getLayoutInflater().inflate((R.layout.main_gridelement), parent,false);
            ImageView iv = convertView.findViewById(R.id.mainSingleImage);
            iv.setImageURI(Uri.parse(getItem(position).toString()));
            return convertView;
        }
    }
}
