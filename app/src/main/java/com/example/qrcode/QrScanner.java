package com.example.qrcode;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.hardware.Camera;
import android.os.Bundle;
import android.os.Vibrator;
import android.util.SparseArray;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.IOException;

import androidx.appcompat.app.AppCompatActivity;

public class QrScanner extends AppCompatActivity {

    SurfaceView surfaceView;
    CameraSource cameraSource;
    TextView textView, qrcode;
    BarcodeDetector barcodeDetector;
    DatabaseReference databaseDta;
    Camera camera;


    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE); // hide the title
        getSupportActionBar().hide(); // hide the title bar
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN); //full screen
        setContentView(R.layout.scanner);

        databaseDta = FirebaseDatabase.getInstance().getReference("userinfo");
        surfaceView = findViewById(R.id.camerapreview);
        textView = findViewById(R.id.textview);


        barcodeDetector = new BarcodeDetector.Builder(this)
                .setBarcodeFormats(Barcode.QR_CODE).build();

        int [] s = getBackCameraResolution();


        cameraSource = new CameraSource.Builder(this, barcodeDetector)

                .setRequestedPreviewSize(s[0],s[1])
                .setRequestedFps(60)
                .setAutoFocusEnabled(true)
                .build();


        // Toast.makeText(this,"Height " + s[1] + "Width " + s[0] ,Toast.LENGTH_SHORT).show();


        scandata();


    }

    public void scandata() {

        surfaceView.getHolder().addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                try {
                    cameraSource.start(holder);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

            }

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {

                cameraSource.stop();

            }
        });

        barcodeDetector.setProcessor(new Detector.Processor<Barcode>() {
            @Override
            public void release() {

            }

            @Override
            public void receiveDetections(Detector.Detections<Barcode> detections) {


                final SparseArray<Barcode> qrCodes = detections.getDetectedItems();

                if (qrCodes.size() != 0) {
                    Vibrator vibrator = (Vibrator) getApplicationContext().getSystemService(Context.VIBRATOR_SERVICE);
                    vibrator.vibrate(200);



                    Intent intent = new Intent(getApplicationContext(), EntryResult.class);
                    intent.putExtra("Code", qrCodes.valueAt(0).displayValue);
                    startActivity(intent);
                    setContentView(R.layout.placeholderforvibrate);

                }

            }

        });

    }

    public int[] getBackCameraResolution()
    {
        int noOfCameras = Camera.getNumberOfCameras();
        int maxWidth;
        long pixelWidth = -1;
        int maxHeight;
        long pixelHeight = -1;
        int [] hw = new int[2];
        for (int i = 0;i < noOfCameras;i++)
        {
            Camera.CameraInfo cameraInfo = new Camera.CameraInfo();
            Camera.getCameraInfo(i, cameraInfo);

            if (cameraInfo.facing == Camera.CameraInfo.CAMERA_FACING_BACK)
            {
                Camera camera = Camera.open(i);;
                Camera.Parameters cameraParams = camera.getParameters();
                for (int j = 0;j < cameraParams.getSupportedPictureSizes().size();j++)
                {
                    long pixelCountWidth = cameraParams.getSupportedPictureSizes().get(j).width;
                    long pixelCountHeight = cameraParams.getSupportedPictureSizes().get(j).height;
                    if (pixelCountWidth > pixelWidth && pixelCountWidth > pixelHeight)
                    {
                        pixelWidth = pixelCountWidth;
                        maxWidth = ((int)pixelCountWidth);

                        pixelHeight = pixelCountHeight;
                        maxHeight= ((int)pixelCountHeight);

                        hw[0]= maxWidth;
                        hw[1]= maxHeight;
                    }
                }

                camera.release();
            }
        }

        return hw;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(QrScanner.this, MainActivity.class);
        startActivity(intent);

    }
}







