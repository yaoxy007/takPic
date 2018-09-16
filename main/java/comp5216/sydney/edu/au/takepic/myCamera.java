package comp5216.sydney.edu.au.takepic;

import android.content.Context;
import android.hardware.Camera;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.io.IOException;

import static android.content.ContentValues.TAG;

public class myCamera extends SurfaceView implements SurfaceHolder.Callback{
    private SurfaceHolder mholder;
    private Camera mCamera;

    public myCamera(Context context,Camera camera){
        super(context);
        mCamera = camera;
        mholder = getHolder();
        mholder.addCallback(this);
        mholder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
    }
    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        try{
            mCamera.setPreviewDisplay(holder);
            mCamera.setDisplayOrientation(90);
            mCamera.startPreview();

        }catch(IOException e){
            Log.e(TAG,"error setting camera preview:"+e.getMessage());
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        if(mholder.getSurface() ==null){
            return;
        }
        try{
//            mCamera.stopPreview();
        }catch(Exception e){
            Log.d(TAG,"error starting camera");
        }
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {

    }

    private void clearCarmer(){
        if(mCamera != null){
            mCamera.setPreviewCallback(null);
            mCamera.stopPreview();

            mCamera.release();
            mCamera=null;
        }
    }
}
