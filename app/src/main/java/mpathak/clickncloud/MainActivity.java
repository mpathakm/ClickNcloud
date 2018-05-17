package mpathak.clickncloud;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Environment;
import android.preference.PreferenceManager;
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
    private GridView mainGrigView;
    private ArrayList<File> thumbImages = new ArrayList<>();

    private String sharedPreferencesKey = "IsThisFIrstTime";
    private FirstTimeSettings _firstTimeSettings;
    private DirectoryContentManager _directoryContentManager;

    public MainActivity() {

        _directoryContentManager = new DirectoryContentManager();
        _firstTimeSettings = new FirstTimeSettings(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);

        if (!preferences.contains(sharedPreferencesKey)) {
            SharedPreferences.Editor editor = preferences.edit();
            editor.putBoolean(sharedPreferencesKey, false);
            editor.apply();

            try {
                startTheServices();
            }
            catch (Exception ex) {
            }

            _firstTimeSettings.initialize();
        }

        setContentView(R.layout.activity_main);

        thumbImages = _directoryContentManager.readImagesFromLocation(new File(Environment.getExternalStorageDirectory(), Constants.Thumbnails));
        mainGrigView = findViewById(R.id.maingridview);
        mainGrigView.setAdapter(new MainGridAdapter());
        mainGrigView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                startActivity(new Intent(getApplicationContext(), FullImageShowActivity.class).putExtra("img", thumbImages.get(position).toString()));
            }
        });
     }

    private void startTheServices()
    {
        Intent intent = new Intent(this, BackgroundServices.class);
        startService(intent);
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
