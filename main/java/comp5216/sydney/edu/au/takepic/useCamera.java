package comp5216.sydney.edu.au.takepic;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.hardware.Camera;
import android.os.Bundle;
import android.os.Environment;
import android.os.PersistableBundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static android.provider.MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE;
import static android.content.ContentValues.TAG;

public class useCamera extends Activity {
    private Camera mycam;
    private myCamera cameraPreview;
    ImageView imgView;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cameralayout);

        mycam = getCameraInstance();
        FrameLayout preview = (FrameLayout) findViewById(R.id.camera_preview);
        cameraPreview = new myCamera(this, mycam);
        preview.addView(cameraPreview);

        Button capBtn = (Button) findViewById(R.id.button_capture);
        Button backBtn = (Button)findViewById(R.id.button_back);
        capBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mycam.takePicture(null, null, mPicture);
            }
        });
        backBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent back = new Intent(useCamera.this,MainActivity.class);
                startActivity(back);
            }
        });
    }

    public static Camera getCameraInstance() {
        Camera c = null;
        try {
            c = Camera.open(); // attempt to get a Camera instance
        } catch (Exception e) {
            // Camera is not available (in use or does not exist)
        }
        return c; // returns null if camera is unavailable
    }

    private Camera.PictureCallback mPicture = new Camera.PictureCallback() {
        @Override
        public void onPictureTaken(byte[] data, Camera camera) {
            File pictureFile = getOutputMediaFile(MEDIA_TYPE_IMAGE);
            if (pictureFile == null) {
                return;
            }
            try {
                FileOutputStream fos = new FileOutputStream(pictureFile);
                fos.write(data);
                fos.close();
                Intent showpic = new Intent(useCamera.this, imgPreview.class);
                showpic.putExtra("pic",pictureFile.getAbsolutePath().toString());
                startActivity(showpic);
                MediaStore.Images.Media.insertImage(getContentResolver(),
                        pictureFile.getAbsolutePath(),"","");

            } catch (FileNotFoundException e) {
                Log.d(TAG, "File not found" + e.getMessage());
            } catch (IOException e) {
                Log.d(TAG, "error accessing file: " + e.getMessage());
            }
            mycam.startPreview();
        }
    };

    private static File getOutputMediaFile(int type) {

        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES), "Images");
        // This location works best if you want the created images to be shared
        // between applications and persist after your app has been uninstalled.

        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                Log.d("Images", "failed to create directory");
                return null;
            }
        }
        // Create a media file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        File mediaFile;
        if (type == MEDIA_TYPE_IMAGE){
            mediaFile = new File(mediaStorageDir.getPath() + File.separator +
                    "IMG_"+ timeStamp + ".jpg");
        } else {
            return null;
        }

        return mediaFile;
    }

}
