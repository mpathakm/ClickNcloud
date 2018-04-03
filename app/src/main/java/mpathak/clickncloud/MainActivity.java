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
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity
{

GridView mainGrigView;
ArrayList<File> images;
    ArrayList<File> thumbImages;

private DirectoryContentManager _directoryContentManager = new DirectoryContentManager();

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        images = _directoryContentManager.readImagesFromLocation(new File(Environment.getExternalStorageDirectory(),getString(R.string.camera_directory)));
        _directoryContentManager.copyImagesToWaitingToUploadFolder(images, this);
        thumbImages = _directoryContentManager.readImagesFromLocation(new File(Environment.getExternalStorageDirectory(),getString(R.string.thumbnails)));

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
