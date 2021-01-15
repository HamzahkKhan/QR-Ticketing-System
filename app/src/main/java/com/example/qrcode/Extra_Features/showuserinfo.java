package com.example.qrcode.Extra_Features;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Point;
import android.icu.text.SimpleDateFormat;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.qrcode.Databases.userinfo;
import com.example.qrcode.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

public class showuserinfo extends AppCompatActivity {

    TextView name,
            work,
            occu,
            hname, hoccu, hwork, creating;
    String key;
    ImageView genratedqr;
    ProgressBar progressBar;
    Bitmap bitmap;
    Button b;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.userinfo);

        query();

    }

    public void query() {

        Intent i = getIntent();
        key = i.getExtras().getString("pid");
        name = findViewById(R.id.showuserinfoName);
        //email=findViewById(R.id.showuserinfoEmail);
        work = findViewById(R.id.showuserinfoWork);
        occu = findViewById(R.id.showuserinfoOccu);
        //num=findViewById(R.id.showuserinfoNumber);

        hname = findViewById(R.id.Uheadname);
        hoccu = findViewById(R.id.Uheadoccu);
        hwork = findViewById(R.id.UheadWork);
        creating = findViewById(R.id.creating);

        progressBar = findViewById(R.id.userinfoPbar);


        Query query = FirebaseDatabase.getInstance().getReference("userinfo")
                .orderByChild("pid")
                .equalTo(key);

        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {

                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        userinfo dbb = snapshot.getValue(userinfo.class);


                        name.setText(dbb.getFirstname() + " " + dbb.getLastname());
                        work.setText(dbb.getWorkplace());
                        occu.setText(dbb.getOccupation());


                        hname.setVisibility(View.VISIBLE);
                        hoccu.setVisibility(View.VISIBLE);
                        hwork.setVisibility(View.VISIBLE);

                        qrGenrator(key);


                        progressBar.setVisibility(View.INVISIBLE);
                        creating.setText(" ");


                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }

    public void qrGenrator(String value) {

        genratedqr = findViewById(R.id.genratedqr);
        String key = value.trim();
        if (key.length() > 0) {
            WindowManager manager = (WindowManager) getSystemService(WINDOW_SERVICE);
            Display display = manager.getDefaultDisplay();

            Point point = new Point();
            display.getSize(point);

            try {

                Map<EncodeHintType, Object> hintMap = new HashMap<EncodeHintType, Object>();
                hintMap.put(EncodeHintType.MARGIN, new Integer(1));
                BitMatrix matrix = new MultiFormatWriter().encode(
                        new String(key.getBytes()),
                        BarcodeFormat.QR_CODE, 150, 150, hintMap);

                bitmap = Bitmap.createBitmap(150, 150, Bitmap.Config.ARGB_8888);
                for (int i = 0; i < 150; i++) {
                    for (int j = 0; j < 150; j++) {
                        bitmap.setPixel(i, j, matrix.get(i, j) ? Color.BLACK
                                : Color.WHITE);
                    }
                    genratedqr.setImageBitmap(bitmap);
                }


            } catch (WriterException e) {

                Toast.makeText(getApplicationContext(), "Something went HORRIBLY Wrong", Toast.LENGTH_SHORT).show();

            }


        }


    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void screen(View view) {
        try {
            CardView content = findViewById(R.id.cardView2);
            content.setDrawingCacheEnabled(true);
            Bitmap bitmap = content.getDrawingCache();
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy_MM_dd_hh_mm_ss");
            Date now = new Date();
            String fileName = formatter.format(now);

            File file, f;

            if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
                file = new File(Environment.getExternalStorageDirectory(), "DCIM");
                if (!file.exists()) {
                    file.mkdirs();
                }
                f = new File(file.getAbsolutePath()+file.separator+ "image_"+fileName+".png");

                FileOutputStream ostream = new FileOutputStream(f);
                bitmap.compress(Bitmap.CompressFormat.PNG, 10, ostream);
                Toast.makeText(this,"done", Toast.LENGTH_SHORT).show();
                ostream.close();
            }


        } catch (Exception e) {
            e.printStackTrace();
        }


    }
}
