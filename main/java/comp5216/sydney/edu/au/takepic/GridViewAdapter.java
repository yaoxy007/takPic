package comp5216.sydney.edu.au.takepic;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import java.io.File;
import java.util.ArrayList;

public class GridViewAdapter extends BaseAdapter{

    private Context context;
    private ArrayList<String> pathlist;

    public GridViewAdapter(Context context, ArrayList<String> list){
        this.context=context;
        this.pathlist=list;
    }

    @Override
    public int getCount() {
        return this.pathlist.size();
    }

    @Override
    public Object getItem(int position) {
        return pathlist.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView imageView;
        if(convertView == null){
            imageView = new ImageView(this.context);
            imageView.setLayoutParams(new GridView.LayoutParams(500,500));
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        }else{
            imageView = (ImageView)convertView;
        }
        BitmapFactory.Options op = new BitmapFactory.Options();
        op.inSampleSize = 4;

        Bitmap bmimg = BitmapFactory.decodeFile(this.pathlist.get(position),op);

        imageView.setImageBitmap(bmimg);
        return imageView;
    }
}