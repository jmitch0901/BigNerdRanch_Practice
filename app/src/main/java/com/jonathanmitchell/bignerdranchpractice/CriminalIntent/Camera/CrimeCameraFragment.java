package com.jonathanmitchell.bignerdranchpractice.CriminalIntent.Camera;


import android.annotation.TargetApi;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.Image;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;

import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import android.hardware.Camera;

import com.jonathanmitchell.bignerdranchpractice.*;
import com.jonathanmitchell.bignerdranchpractice.CriminalIntent.CrimeFragment;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

import android.hardware.Camera.*;
import android.widget.ImageButton;

/**
 * Created by jonathanmitchell on 1/6/15.
 */
public class CrimeCameraFragment extends Fragment {

    public static final String EXTRA_PHOTO_FILENAME =
            "com.jonathanmitchell.bignerdranchpractice.CriminalIntent.photo_filename";

    private static final String TAG = "CrimeCameraFragment";


    private SurfaceView mSurfaceView;
    private Camera mCamera; //Depricated

    private View mProgressContainer;


    private ShutterCallback mShutterCallback = new ShutterCallback(){

        @Override
        public void onShutter() {
            mProgressContainer.setVisibility(View.VISIBLE);
        }
    };

    private PictureCallback mJpegCallback = new PictureCallback() {
        @Override
        public void onPictureTaken(byte[] data, Camera camera) {

            //Create a filename
            String filename = UUID.randomUUID().toString()+".jpg";

            //Save the jpeg data to disk
            FileOutputStream os = null;
            boolean success = true;

            try{
                os = getActivity().openFileOutput(filename, Context.MODE_PRIVATE);
                os.write(data);

            } catch (Exception e) {
                Log.e(TAG,"Error writing to file "+filename, e);
                success = false;
            } finally {
                if(os!=null){
                    try{
                        os.close();
                    } catch (Exception e){
                        Log.e(TAG, "Error closing file "+filename, e);
                        success = false;
                    }

                }
            }

            if(success){
                //Log.i(TAG, "JPEG saved at "+filename);
                Intent i = new Intent();
                i.putExtra(EXTRA_PHOTO_FILENAME,filename);
                getActivity().setResult(Activity.RESULT_OK,i);

            } else {
                getActivity().setResult(Activity.RESULT_CANCELED);
            }

            getActivity().finish();

        }
    };


    @Override
    @SuppressWarnings("deprecation")
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_crime_camera,container, false);

        final Button takePictureButton = (Button)v.findViewById(R.id.crime_camera_takePictureButton);
        takePictureButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            if(mCamera != null){
                mCamera.takePicture(mShutterCallback, null, mJpegCallback);
            }


            }
        });

        mSurfaceView = (SurfaceView)v.findViewById(R.id.crime_camera_surfaceView);
        SurfaceHolder holder = mSurfaceView.getHolder();
        //setType() and SURFACE_TYPE_PUSH_BUFFERS are both deprecated,
        //but are required for Camera Preview to work on pre-3.0 devices.
        holder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);

        holder.addCallback(new SurfaceHolder.Callback() {


            @Override
            public void surfaceCreated(SurfaceHolder holder) {

                //Tell the camera to use this surface as its preview area
                try{
                    if(mCamera != null){
                        mCamera.setPreviewDisplay(holder);
                    }
                } catch (IOException e) {
                    Log.e(TAG,"Error setting up preview display",e);
                }
            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
               //The surface has changed size, so update the camera PREVIEW SIZE
                if(mCamera == null) return;

                Parameters parameters = mCamera.getParameters();
                Size s = getBestSupportedSize(parameters.getSupportedPreviewSizes(),width,height);
                parameters.setPreviewSize(s.width, s.height);

                s = getBestSupportedSize(parameters.getSupportedPictureSizes(), width, height);
                parameters.setPictureSize(s.width, s.height);

                mCamera.setParameters(parameters);

                try{
                    mCamera.startPreview();
                } catch (Exception e) {
                    Log.e(TAG,"Could not start preview",e);
                    mCamera.release();
                    mCamera = null;
                }
            }

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {

                //We can no longer display on this surface, so stop the preview.
                if(mCamera!=null){
                    mCamera.stopPreview();
                }
            }
        });


        mProgressContainer = v.findViewById(R.id.crime_camera_progressContainer);
        mProgressContainer.setVisibility(View.INVISIBLE);



        return v;
    }


/*
    The camera method used is deprecated and should be eventually changed
 */
    @TargetApi(9)
    @Override
    public void onResume() {
        super.onResume();

        //Need API 21 Camera2 call FIRST Eventually
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD){
            mCamera = Camera.open(0);
        } else {
            mCamera = Camera.open();
        }
    }

    @Override
    public void onPause() {
        super.onPause();

        if(mCamera!=null){
            mCamera.release();
            mCamera=null;
        }
    }

    private Size getBestSupportedSize(List<Size> sizes, int width, int height){
        Size bestSize = sizes.get(0);
        int largestArea = bestSize.width * bestSize.height;

        for(Size s : sizes){

            int area = s.width * s.height;
            if(area > largestArea){
                largestArea = area;
            }

        }

        return bestSize;
    }



    /*
            Will use Camera2 at a later time
     */

    /*

    @TargetApi(21)
    private void useCamera2API(){

        final CameraManager camMan = (CameraManager)getActivity().getSystemService(Context.CAMERA_SERVICE);


        try {
            final String[] ids = camMan.getCameraIdList();

            camMan.openCamera(ids[0],new CameraDevice.StateCallback(){

                @Override
                public void onOpened(CameraDevice camera) {
                    Log.d(TAG,"Camera Opened");

                    try {


                        CameraCharacteristics characteristics = camMan.getCameraCharacteristics(ids[0]);
                        StreamConfigurationMap configs = characteristics.get(CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP);
                        Size[] sizes = configs.getOutputSizes(ImageFormat.JPEG);
                        ImageReader reader = ImageReader.newInstance(sizes[0].getWidth(),
                                sizes[0].getHeight(),ImageFormat.JPEG,2);

                        Surface jpegCaptureSurface = reader.getSurface();



                        surfaces = new ArrayList<Surface>(0);
                        surfaces.add(jpegCaptureSurface);

                        CrimeCameraFragment.this.camera = camera;

                        previewRequest = camera.createCaptureRequest(CameraDevice.TEMPLATE_PREVIEW);

                        previewRequest.addTarget(mSurfaceView.getHolder().getSurface());
                        previewRequest.build();




                        camera.createCaptureSession(surfaces, new CameraCaptureSession.StateCallback() {
                            @Override
                            public void onConfigured(CameraCaptureSession session) {

                            }

                            @Override
                            public void onConfigureFailed(CameraCaptureSession session) {

                            }
                        }, null);

                    } catch (CameraAccessException e) {
                        Log.e(TAG,"Error @onOpened method");
                        e.printStackTrace();
                    }


                }

                @Override
                public void onDisconnected(CameraDevice camera) {
                    camera.close();
                    Log.d(TAG,"Camera Disconnected");
                }

                @Override
                public void onError(CameraDevice camera, int error) {
                    camera.close();
                    Log.e(TAG,"Camera cloased with following error: "+error);
                }

            },null);
        } catch (CameraAccessException e) {
            Log.e(TAG,"Error Getting Camera ID List");
            e.printStackTrace();
        }


    }*/
}
