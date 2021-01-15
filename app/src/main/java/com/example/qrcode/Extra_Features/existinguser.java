package com.example.qrcode.Extra_Features;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.qrcode.MainActivity;
import com.example.qrcode.R;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class existinguser extends AppCompatActivity {



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.existinguser);
    }

    public void info(View view){

        Intent intent = getIntent();
        String r = intent.getExtras().getString("pid");

        Intent i = new Intent(existinguser.this, showuserinfo.class);
        i.putExtra("pid",r);
        startActivity(i);

    }

    public void updateinfo(View view){

        Intent intent = getIntent();
        String r = intent.getExtras().getString("pid");

        Intent i = new Intent(existinguser.this, myaccount.class);
        i.putExtra("pid",r);
        startActivity(i);

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(existinguser.this, MainActivity.class);
        startActivity(intent);
    }
}
