package comp5216.sydney.edu.au.takepic;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

public class imgPreview extends Activity {
    ImageView imgView;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.img_preview);
        imgView = (ImageView)findViewById(R.id.photopreview);
        String img = getIntent().getStringExtra("pic");
        Bitmap show = BitmapFactory.decodeFile(img);

        imgView.setImageBitmap(show);
        imgView.setVisibility(View.VISIBLE);
    }
}
