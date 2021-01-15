package com.example.qrcode.Extra_Features;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.qrcode.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class updateoccu extends AppCompatActivity {
    Button updateoccup;
    EditText occu;
    DatabaseReference databaseDta;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.updateoccu);

        occu = findViewById(R.id.updateoccu_Occupation);

        updateoccup = findViewById(R.id.updateoccu_Update);
        updateoccup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                databaseDta = FirebaseDatabase.getInstance().getReference("userinfo");
                String occupation = occu.getText().toString().trim() ;

                if(TextUtils.isEmpty(occupation)){

                    occu.setError("* required");
                    occu.requestFocus();
                }
                else {

                    Intent i = getIntent();
                    String pid = i.getExtras().getString("pid");

                    Map<String, Object> updatedvalues = new HashMap<>();

                    updatedvalues.put("/" + pid + "/occupation", occupation);

                    databaseDta.updateChildren(updatedvalues);
                    Toast.makeText(getApplicationContext(), "Updated", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(updateoccu.this, myaccount.class);
                    intent.putExtra("pid", pid);
                    finish();
                }

            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent i = getIntent();
        String pid= i.getExtras().getString("pid");
        Intent intent = new Intent(updateoccu.this, myaccount.class);
        intent.putExtra("pid", pid);
        startActivity(intent);
    }
}
