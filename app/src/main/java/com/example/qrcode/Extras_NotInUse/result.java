package com.example.qrcode.Extras_NotInUse;

import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.qrcode.Databases.userinfo;
import com.example.qrcode.MainActivity;
import com.example.qrcode.QrScanner;
import com.example.qrcode.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;


public class result extends AppCompatActivity {


        String detectedBarcode;
        TextView name,load,hname,acc_no,qrcode, email, phone, work,occu, hemail,hphone,hwork,hoccu;
        Button button2, addcontacts;
        ProgressBar progressBar;
        DatabaseReference databaseDta;

        @Override
        protected void onCreate(final Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            requestWindowFeature(Window.FEATURE_NO_TITLE); // hide the title
            getSupportActionBar().hide(); // hide the title bar
            this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_FULLSCREEN); //full screen

            setContentView(R.layout.scannedresult);
            Intent i = getIntent();
            detectedBarcode= i.getExtras().getString("Code");

            name=findViewById(R.id.scannedResultName);
            qrcode=findViewById(R.id.qrcode);
            email=findViewById(R.id.scannnedResultEmail);
            work=findViewById(R.id.scannnedResultWork);
            occu=findViewById(R.id.scannnedResultOccupation);
            phone=findViewById(R.id.scannnedResultPhone);

            button2=findViewById(R.id.showInfosignout);
            addcontacts= findViewById(R.id.addtocontacts);



            hname= findViewById(R.id.headname);
            hemail=findViewById(R.id.headEmail);
            hoccu=findViewById(R.id.headoccu);
            hwork= findViewById(R.id.headWork);
            hphone=findViewById(R.id.headphone);
            acc_no=findViewById(R.id.accept);

            progressBar=findViewById(R.id.scannedResultPbar);
            load=findViewById(R.id.load);

            query2(detectedBarcode);
            qrcode.setText(detectedBarcode);


        }

        public void scanner(View view){
            Intent intent= new Intent(result.this, QrScanner.class);
            startActivity(intent);


        }

        public void query(final String bar){

            Query query = FirebaseDatabase.getInstance().getReference("userinfo")
                    .orderByChild("pid")
                    .equalTo(bar);

            query.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {

                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            userinfo dbb = snapshot.getValue(userinfo.class);
                            while (name == null){
                                progressBar.setVisibility(View.VISIBLE);

                            }

                            String phonenumber = dbb.getMnumber();
                            String phoneFormatted = String.format("%s %s-%s %s", phonenumber.substring(0, 3), phonenumber.substring(3, 5),
                                    phonenumber.substring(5, 9), phonenumber.substring(9));
                            String fullname = dbb.getFirstname() + " " + dbb.getLastname();


                            name.setText(fullname);
                            email.setText(dbb.getEmail());
                            work.setText(dbb.getWorkplace());
                            occu.setText(dbb.getOccupation());
                            phone.setText(phoneFormatted);

                            acc_no.setText(R.string.Information);
                            load.setText(" ");
                            progressBar.setVisibility(View.INVISIBLE);
                            hname.setVisibility(View.VISIBLE);
                            button2.setVisibility(View.VISIBLE);
                            hemail.setVisibility(View.VISIBLE);
                            hwork.setVisibility(View.VISIBLE);
                            hoccu.setVisibility(View.VISIBLE);
                            hphone.setVisibility(View.VISIBLE);
                            addcontacts.setVisibility(View.VISIBLE);
                            contacts(fullname,dbb.getWorkplace(),dbb.getMnumber(),dbb.getEmail());

                            scanneduser(dbb.getPid(), dbb.getEmail(), dbb.getFirstname(), dbb.getLastname(), dbb.getMnumber(), dbb.getOccupation(),
                                    dbb.getWorkplace());




                        }

                    }
                    else {

                        while (acc_no==null){
                            progressBar.setVisibility(View.VISIBLE);
                        }

                        progressBar.setVisibility(View.INVISIBLE);
                        acc_no.setTextColor(getResources().getColor(R.color.Red));
                        acc_no.setText(R.string.nodata);
                        load.setText(" ");
                        button2.setVisibility(View.VISIBLE);


                    }
                }
                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

        }

        public void query2(final String bar){

        final Query query = FirebaseDatabase.getInstance().getReference("DbScannedTickets")
                .orderByChild("pid")
                .equalTo(bar);

        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {

                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        userinfo dbb = snapshot.getValue(userinfo.class);
                        while (acc_no == null){
                            progressBar.setVisibility(View.VISIBLE);

                        }
                        acc_no.setText("Already Entered");
                        load.setText(" ");
                        qrcode.setText(bar);
                        progressBar.setVisibility(View.INVISIBLE);

                    }

                }
                else {

                    query(bar);



                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

        public void contacts (final String cname, final String cwork, final String cnum, final String cemail){

            Button button = findViewById(R.id.addtocontacts);
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final Intent contactIntent = new Intent(ContactsContract.Intents.Insert.ACTION);
                    contactIntent.setType(ContactsContract.RawContacts.CONTENT_TYPE);

                    contactIntent
                            .putExtra(ContactsContract.Intents.Insert.NAME, cname)
                            .putExtra(ContactsContract.Intents.Insert.PHONE, cnum)
                            .putExtra(ContactsContract.Intents.Insert.EMAIL, cemail)
                            .putExtra(ContactsContract.Intents.Insert.COMPANY, cwork);
                    startActivityForResult(contactIntent, 1);
                }
            });



        }

        public void back (View view){
            Intent intent = new Intent(result.this, MainActivity.class);
            startActivity(intent);


        }

        public void scanneduser(String scid, String email, String fname, String lname, String mnumber, String occupation, String workplace){
            databaseDta = FirebaseDatabase.getInstance().getReference("DbScannedTickets");

            String id = databaseDta.push().getKey();
            userinfo data = new userinfo(scid,email , fname, lname, mnumber, occupation, workplace);
            databaseDta.child(id).setValue(data);
            Message.message(getApplicationContext(),"Data added");
        }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(result.this, MainActivity.class);
        startActivity(intent);
    }
}



