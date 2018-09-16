package comp5216.sydney.edu.au.takepic;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    GridView imageGrid;
    GridViewAdapter gvAdapter;
    File file;
    ArrayList<String> path;
    public final static int EDITED_IMG=101;

    MarshmallowPermission marshmallowPermissions = new MarshmallowPermission(this);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        showPic();
        onGridViewListener();
    }

    private void showPic(){
        if(!marshmallowPermissions.checkPermissionForReadfiles()){
            marshmallowPermissions.requestPermissionForReadfiles();
        }else{
            Cursor c = MediaStore.Images.Media.query(getContentResolver(), MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                    null,null,MediaStore.Images.Media._ID+" DESC");
            int count = c.getCount();

            path = new ArrayList<String>();

            for (int i = 0; i < count; i++) {
                c.moveToPosition(i);
                String p = c.getString(c.getColumnIndex(MediaStore.Images.Media.DATA));
                path.add(p);

            }
            c.close();
            gvAdapter= new GridViewAdapter(this,path);

            imageGrid = (GridView) findViewById(R.id.gallery);
            imageGrid.setAdapter(gvAdapter);
        }
    }

    public void onTakePhotoClick(View v) {

        if (!marshmallowPermissions.checkPermissionForCamera() ||
                !marshmallowPermissions.checkPermissionForExternalStorage()) {
            marshmallowPermissions.requestPermissionForCamera();
        } else {
            Intent intent = new Intent(MainActivity.this, useCamera.class);
            startActivity(intent);
        }
    }

    public void onGridViewListener(){
        imageGrid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String img_path =(String)gvAdapter.getItem(position);
                Intent editPicIntent = new Intent(MainActivity.this,imgEdit.class);
                editPicIntent.putExtra("img_edit", img_path);
                startActivityForResult(editPicIntent,EDITED_IMG);
            }
        });
    }

}
