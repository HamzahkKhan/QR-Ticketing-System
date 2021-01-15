package com.example.qrcode;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Point;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.view.Display;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.qrcode.Databases.DbTicketInfo;
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
import androidx.constraintlayout.widget.ConstraintLayout;

public class MassGenrate extends AppCompatActivity {

    TextView name, abs, hname, delegate, qr, diet, schedule;
    ImageView genratedqr;
    Bitmap bitmap;
    int total = 0;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ticket);

        query();

    }

    public void query() {

        name = findViewById(R.id.ticket_NameFilled);
        delegate = findViewById(R.id.ticket_CatFilled);
        qr = findViewById(R.id.ticket_QrCode_shown);
        abs = findViewById(R.id.ticket_AbstractFilled);
        diet = findViewById(R.id.ticket_dietfilled);
        schedule = findViewById(R.id.ticket_ScheduleFilled);


        hname = findViewById(R.id.ticket_Name);


        Query query = FirebaseDatabase.getInstance().getReference("DbTicketInfo");

        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (dataSnapshot.exists()) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        DbTicketInfo dbb = snapshot.getValue(DbTicketInfo.class);


                        name.setText(dbb.getName().toUpperCase());

                        delegate.setText(dbb.getDelegate());
                        qr.setText(dbb.getSno());

                        if (dbb.getDiet().isEmpty() || dbb.getDiet() == "" || dbb.getDiet() == null) {
                            diet.setText("Nil");
                        } else {
                            diet.setText(dbb.getDiet());
                        }
                        if (dbb.getAbs().isEmpty() || dbb.getAbs() == "" || dbb.getAbs() == null) {
                            abs.setText("Nil");
                        } else {
                            abs.setText(dbb.getAbs());
                        }
                        if (dbb.getSchedule().isEmpty() || dbb.getSchedule() == "" || dbb.getSchedule() == null) {
                            schedule.setText("Nil");
                        } else {
                            schedule.setText(dbb.getSchedule());
                        }

                        qrGenrator(dbb.getSno());
                        total++;
                        screen(dbb.getSno());


                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }

    public void qrGenrator(String value) {

        genratedqr = findViewById(R.id.ticket_QRcode);
        String key = value.trim();
        if (key.length() > 0) {
            WindowManager manager = (WindowManager) getSystemService(WINDOW_SERVICE);
            Display display = manager.getDefaultDisplay();

            Point point = new Point();
            display.getSize(point);

            try {

                Map<EncodeHintType, Object> hintMap = new HashMap<EncodeHintType, Object>();
                hintMap.put(EncodeHintType.MARGIN, new Integer(0));
                BitMatrix matrix = new MultiFormatWriter().encode(
                        new String(key.getBytes()),
                        BarcodeFormat.QR_CODE, 200, 200, hintMap);

                bitmap = Bitmap.createBitmap(200, 200, Bitmap.Config.ARGB_8888);
                for (int i = 0; i < 200; i++) {
                    for (int j = 0; j < 200; j++) {
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
    public void screen(String name) {
        try {
            ConstraintLayout content = findViewById(R.id.ticket_Ticket);
            content.setDrawingCacheEnabled(true);
            Bitmap bitmap = content.getDrawingCache();

            // SimpleDateFormat formatter = new SimpleDateFormat("yyyy_MM_dd_hh_mm_ss");
            // Date now = new Date();
            // String fileName = formatter.format(now);

            File file, f;

            if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
                file = new File(Environment.getExternalStorageDirectory(), "Tickets");
                if (!file.exists()) {
                    file.mkdirs();
                }
                f = new File(file.getAbsolutePath() + file.separator + name +
                        ".png");

                FileOutputStream ostream = new FileOutputStream(f);
                bitmap.compress(Bitmap.CompressFormat.PNG, 10, ostream);
                Toast.makeText(this, "Tickets Genrated: " + total, Toast.LENGTH_SHORT)
                        .show();
                ostream.close();
            }


        } catch (Exception e) {
            Toast.makeText(this, "Some thing went wrong", Toast.LENGTH_SHORT)
                    .show();
        }


    }
}
