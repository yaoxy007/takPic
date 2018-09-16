package comp5216.sydney.edu.au.takepic;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;


public class imgEdit extends Activity {
    ImageView imgView;
    Button crop_Btn;
    Button save_Btn;
    Button undo_Btn;
    Button back_Btn;
    String file;
    Bitmap cropped_pic;
    File newFile;
    public static final int PHOTO_RESULT = 202;
    public static final int CROP_CODE = 203;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.img_edit);
        imgView = (ImageView) findViewById(R.id.photoEdit);

        if (getIntent().getStringExtra("img_edit") == null) {

        } else {
            file = getIntent().getStringExtra("img_edit");
            Bitmap show = BitmapFactory.decodeFile(file);
            imgView.setImageBitmap(show);
            imgView.setVisibility(View.VISIBLE);
        }

        crop_Btn = (Button) findViewById(R.id.cropBtn);
        crop_Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Crop();
            }
        });

        undo_Btn = (Button) findViewById(R.id.undoBtn);
        undo_Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                newFile.delete();
                Bitmap show = BitmapFactory.decodeFile(file);
                imgView.setImageBitmap(show);
                imgView.setVisibility(View.VISIBLE);
            }
        });

        back_Btn = (Button) findViewById(R.id.backBtn);
        back_Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (newFile.exists()) {
                    try {
                        MediaStore.Images.Media.insertImage(getContentResolver(),newFile.getAbsolutePath(),"","");
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                    Intent backIntent = new Intent(imgEdit.this, MainActivity.class);
                    startActivity(backIntent);
                } else {
                    finish();
                }
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PHOTO_RESULT) {
            cropped_pic = data.getParcelableExtra("data");
            imgView.setImageBitmap(cropped_pic);
            imgView.setVisibility(View.VISIBLE);
        }
    }

    private void Crop() {
        Intent cropIntent = new Intent("com.android.camera.action.CROP");
        File file = new File(getIntent().getStringExtra("img_edit"));
        Uri fileuri = FileProvider.getUriForFile(this, "au.edu.sydney.comp5216.TakePic.fileProvider",
                file);
        cropIntent.setDataAndType(fileuri, "image/*");
        cropIntent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
        cropIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        cropIntent.putExtra("crop", "true");
        cropIntent.putExtra("scale", true);
        cropIntent.putExtra("aspectX", 1);
        cropIntent.putExtra("aspectY", 1);
        cropIntent.putExtra("outputX", 200);
        cropIntent.putExtra("outputY", 200);
        cropIntent.putExtra("return-data", true);
        cropIntent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());

        cropIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(saveCroppedPic()));
        startActivityForResult(cropIntent, PHOTO_RESULT);
    }

    private File saveCroppedPic() {
        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES), "Images");
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        newFile = new File(mediaStorageDir.getPath() + File.separator + "IMG" + timeStamp + ".jpg");
        return newFile;
    }
}
