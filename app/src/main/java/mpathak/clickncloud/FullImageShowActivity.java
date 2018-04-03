package mpathak.clickncloud;

import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;

import java.io.File;

public class FullImageShowActivity extends AppCompatActivity
{
    ImageView fullImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_image_show);

        String imageUri = getIntent().getStringExtra("img");
        fullImageView = findViewById(R.id.fullimageView);
        fullImageView.setImageURI(Uri.parse(imageUri));
    }
}
