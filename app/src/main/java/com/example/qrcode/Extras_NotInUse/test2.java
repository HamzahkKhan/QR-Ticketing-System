package com.example.qrcode.Extras_NotInUse;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.qrcode.Databases.userinfo;
import com.example.qrcode.Extras_NotInUse.Message;
import com.example.qrcode.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;


public class test2 extends AppCompatActivity {

EditText fname,lname,email;
Button add;
DatabaseReference databaseDta;
List<userinfo> userinfoList;
TextView textf, textg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.test2);

        databaseDta = FirebaseDatabase.getInstance().getReference("userinfo");


        userinfoList = new ArrayList<>();

        fname = findViewById(R.id.fFname);
        lname = findViewById(R.id.fLname);
        email= findViewById(R.id.fkey);
        add = findViewById(R.id.fbutton);
        textf = findViewById(R.id.viewfdata);
        textg= findViewById(R.id.dbname);

        /*Query query = FirebaseDatabase.getInstance().getReference("userinfo")
              .orderByChild("ffkey")
              .equalTo("123ABC"); */

        /*
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {

                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        userinfo dbb = snapshot.getValue(userinfo.class);
                        QrScanner ss = new QrScanner();
                        Intent i = getIntent();
                        String r = i.getExtras().getString("Code");
                        textf.setText(r);
                        //textf.setText(dbb.getPid());
                        //textg.setText(dbb.getFirname());

                    }

                }
                else {
                    textf.setText("null");
                    textg.setText("null");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        }); */

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                adddata();
                fname.setText("");
                lname.setText("");
                email.setText("");
            }
        });

    }

    private void adddata(){
        String firstname = fname.getText().toString().trim();
        String lasttname = lname.getText().toString().trim();
        String Email = email.getText().toString().trim();

        if(!(TextUtils.isEmpty(firstname) || TextUtils.isEmpty(lasttname) || TextUtils.isEmpty(Email))){

           String id = databaseDta.push().getKey();
            userinfo data = new userinfo(id, Email, firstname, lasttname, "", "", "");
           databaseDta.child(id).setValue(data);
           Message.message(getApplicationContext(),"Added");

        } else {
            Message.message(getApplicationContext(),"Fill all");

        }
    }
}








