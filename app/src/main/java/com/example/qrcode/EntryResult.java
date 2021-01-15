package com.example.qrcode;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.qrcode.Databases.DbScannedTickets;
import com.example.qrcode.Databases.DbTicketInfo;
import com.example.qrcode.Extras_NotInUse.Message;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;


public class EntryResult extends AppCompatActivity {


    String detectedBarcode;
    TextView name, hname, acc_no, qrcode, email, hemail, delegate, abs, diet, scd;
    DatabaseReference databaseDta;
    ProgressBar progressBar;



    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE); // hide the title
        getSupportActionBar().hide(); // hide the title bar
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN); //full screen

        setContentView(R.layout.entryresult);
        Intent i = getIntent();
        detectedBarcode = i.getExtras().getString("Code");

        name = findViewById(R.id.entryresult_Name);
        qrcode = findViewById(R.id.entryresult_Qr);
        email = findViewById(R.id.entryresult_Email);
        delegate = findViewById(R.id.entryresult_Delegate);
        abs = findViewById(R.id.entryresult_AbsShown);
        diet = findViewById(R.id.entryresult_DietShown);
        scd = findViewById(R.id.entryresult_scheduleShown);
        progressBar= findViewById(R.id.entryresult_Loading);


        hname = findViewById(R.id.entryresult_hName);
        hemail = findViewById(R.id.entryresult_hEmail);
        acc_no = findViewById(R.id.entryresult_Accepted);



        CheckScannedTicketsDb(detectedBarcode);
        qrcode.setText(detectedBarcode);


    }

    public void scanner(View view) {
        Intent intent = new Intent(EntryResult.this, QrScanner.class);
        startActivity(intent);


    }

    public void CheckScannedTicketsDb(final String bar) {

        final Query query = FirebaseDatabase.getInstance().getReference("DbScannedTickets")
                .orderByChild("ssno")
                .equalTo(bar);

        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {

                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {

                        alreadyEntered(snapshot, bar);

                    }

                } else {

                    CheckTicketInfoDb(bar);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    public void CheckTicketInfoDb(final String bar) {

        Query query = FirebaseDatabase.getInstance().getReference("DbTicketInfo")
                .orderByChild("sno")
                .equalTo(bar);

        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (dataSnapshot.exists()) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        Attendee(snapshot);
                    }


                    Toast.makeText(getApplicationContext(), "Done", Toast.LENGTH_SHORT).show();


                } else {


                    acc_no.setTextColor(getResources().getColor(R.color.Red));
                    acc_no.setText(R.string.nodata);
                    progressBar.setVisibility(View.INVISIBLE);
                    acc_no.setVisibility(View.VISIBLE);



                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    public void ScannedUser(String ssno, String semail, String sdelegate, String sname, String sdiet, String sabs, String sschedule) {


        databaseDta = FirebaseDatabase.getInstance().getReference("DbScannedTickets");
        String id = databaseDta.push().getKey();
        DbScannedTickets data = new DbScannedTickets(ssno, semail, sdelegate, sname, sdiet, sabs, sschedule);
        databaseDta.child(id).setValue(data);
        Message.message(getApplicationContext(), "Data added");
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(EntryResult.this, QrScanner.class);
        startActivity(intent);
    }

    public void alreadyEntered(DataSnapshot snapshot, String bar) {

        DbScannedTickets db = snapshot.getValue(DbScannedTickets.class);
        acc_no.setTextColor(getResources().getColor(R.color.Red));
        acc_no.setText("Already Entered");
        acc_no.setVisibility(View.VISIBLE);
        name.setText(db.getSname());
        email.setText(db.getSemail());
        delegate.setText(db.getSdelegate());


        if (db.getSdiet().isEmpty() || db.getSdiet() == "" || db.getSdiet() == null) {
            diet.setText("Nil");
        } else {
            diet.setText(db.getSdiet());
        }
        if (db.getSabs().isEmpty() || db.getSabs() == "" || db.getSabs() == null) {
            abs.setText("Nil");
        } else {
            abs.setText(db.getSabs());
        }
        if (db.getSschedule().isEmpty() || db.getSschedule() == "" || db.getSschedule() == null) {
            scd.setText("Nil");
        } else {
            scd.setText(db.getSschedule());
        }
        progressBar.setVisibility(View.INVISIBLE);
        setVisible();

        qrcode.setText(bar);


    }

    public void Attendee(DataSnapshot snapshot) {

        DbTicketInfo dbb = snapshot.getValue(DbTicketInfo.class);
        acc_no.setText("ACCEPTED");
        acc_no.setVisibility(View.VISIBLE);
        name.setText(dbb.getName());
        email.setText(dbb.getEmail());
        delegate.setText(dbb.getDelegate());


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
            scd.setText("Nil");
        } else {
            scd.setText(dbb.getSchedule());
        }

        progressBar.setVisibility(View.INVISIBLE);
        setVisible();

        ScannedUser(dbb.getSno(), dbb.getEmail(), dbb.getDelegate(), dbb.getName(), dbb.getDiet(), dbb.getAbs(), dbb.getSchedule());

    }

    public void scanNow(View v) {
        Intent intent = new Intent(EntryResult.this, QrScanner.class);
        startActivity(intent);
    }

    public void back(View view) {
        Intent intent = new Intent(EntryResult.this, MainActivity.class);
        startActivity(intent);
    }

    public void setVisible() {

        delegate.setVisibility(View.VISIBLE);
        email.setVisibility(View.VISIBLE);
        name.setVisibility(View.VISIBLE);
        diet.setVisibility(View.VISIBLE);
        abs.setVisibility(View.VISIBLE);
        scd.setVisibility(View.VISIBLE);

    }
}



