package com.example.qrcode;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Point;
import android.os.Bundle;
import android.view.Display;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;
import androidx.appcompat.app.AppCompatActivity;

public class QrImageGenrator extends AppCompatActivity {

    ImageView genratedqr;
    Bitmap bitmap;

    public void qrGenrator(@NotNull String value, int view) {

        genratedqr = findViewById(view);
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
}
