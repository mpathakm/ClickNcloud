package mpathak.clickncloud;

import android.net.Uri;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

/**
 * Created by mpathak on 4/1/2018.
 */

public class MainGridAdapter  //extends BaseAdapter
{
    /*@Override
    public int getCount() {
        return images.size();
    }

    @Override
    public Object getItem(int position) {
        return images.get(position);
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
    }*/
}
