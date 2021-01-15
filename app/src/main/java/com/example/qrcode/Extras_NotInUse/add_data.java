package com.example.qrcode.Extras_NotInUse;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.qrcode.Databases.DbTicketInfo;
import com.example.qrcode.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class add_data extends AppCompatActivity {
    DatabaseReference databaseDta;
    EditText name, email, phone, work, occu, lname;
    Button add;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_data);

        name = findViewById(R.id.add_data_Fname);
        lname = findViewById(R.id.add_data_Lname);
        email = findViewById(R.id.add_data_Email);
        phone = findViewById(R.id.add_data_Phone);
        occu = findViewById(R.id.add_data_Occu);
        work = findViewById(R.id.add_data_Work);


    }

    public void add_data(View view) {

        String gfname = name.getText().toString().trim();
        String glname = lname.getText().toString().trim();
        String gemail = email.getText().toString().trim().toLowerCase();
        String gphone = "+60" + phone.getText().toString().trim();
        String gwork = work.getText().toString().trim();
        String goccu = occu.getText().toString().trim();

        if (TextUtils.isEmpty(gfname)) {

            name.setError("* required");
            name.requestFocus();
        } else if (TextUtils.isEmpty(glname)) {

            lname.setError("* required");
            lname.requestFocus();

        } else if (gphone.length() <= 3) {

            phone.setError("* required");
            phone.requestFocus();

        } else if (gphone.length() < 13 || gphone.length() > 13) {

            phone.setError("Check your number again");
            phone.requestFocus();

        } else if (TextUtils.isEmpty(goccu)) {

            occu.setError("* required");
            occu.requestFocus();

        } else if (TextUtils.isEmpty(gwork)) {

            work.setError("* required");
            work.requestFocus();

        } else {


            databaseDta = FirebaseDatabase.getInstance().getReference("DbTicketInfo");
            String id = databaseDta.push().getKey();
            DbTicketInfo data = new DbTicketInfo(gfname,gemail,gemail,gemail,gemail,gemail,gphone);
            databaseDta.child(id).setValue(data);
            Message.message(getApplicationContext(), "Account Created");

            name.setText("");
            lname.setText("");
            email.setText("");
            phone.setText("");
            work.setText("");
            occu.setText("");


        }


    }
}
