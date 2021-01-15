package com.example.qrcode.Extra_Features;

import android.content.Intent;
import android.os.Bundle;
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

public class updatephone extends AppCompatActivity {
    Button updatephone;
    EditText number;
    DatabaseReference databaseDta;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.updatephone);


        number = findViewById(R.id.updateinfoPhone);
        updatephone=findViewById(R.id.updatePhoneButton);


        updatephone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                databaseDta = FirebaseDatabase.getInstance().getReference("userinfo");
                String pnumber = "+60" + number.getText().toString().trim() ;

                if(pnumber.length()<=3){

                    number.setError("* required");
                    number.requestFocus();

                }
                else if(pnumber.length()<13 || pnumber.length() > 13) {

                    number.setError("Check your number again");
                    number.requestFocus();

                }

                else {

                    Intent i = getIntent();
                    String pid = i.getExtras().getString("pid");

                    Map<String, Object> updatedvalues = new HashMap<>();

                    updatedvalues.put("/" + pid + "/mnumber", pnumber);

                    databaseDta.updateChildren(updatedvalues);
                    Toast.makeText(getApplicationContext(), "Updated", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(updatephone.this, myaccount.class);
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
        Intent intent = new Intent(updatephone.this, myaccount.class);
        intent.putExtra("pid", pid);
        startActivity(intent);
    }
}
