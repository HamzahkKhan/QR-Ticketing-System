package com.example.qrcode.Extra_Features;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.qrcode.R;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class newuser extends AppCompatActivity {

    TextView userInfoH;
    Button signout, next;
    EditText fname,lname,occupation,workplace,mnumber;
    DatabaseReference databaseDta;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.newuser);



        userInfoH = findViewById(R.id.userinfoheading);
        signout = findViewById(R.id.newuserSignout);
        next=findViewById(R.id.newuserNext);

        fname = findViewById(R.id.newUserFirstName);
        lname = findViewById(R.id.newUserLastName);
        occupation =findViewById(R.id.newUserOccupation);
        workplace = findViewById(R.id.newUserWorkplace);
        mnumber = findViewById(R.id.newUserMobile);



        signout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signOutfunction();
            }
        });

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateData();
            }
        });





    }

    public void updateData(){

        databaseDta = FirebaseDatabase.getInstance().getReference("userinfo");
        String firstname = fname.getText().toString().trim();
        String lastname = lname.getText().toString().trim();
        String mobile = "+60" + mnumber.getText().toString().trim();
        String work = workplace.getText().toString().trim();
        String occup = occupation.getText().toString().trim();



        if(TextUtils.isEmpty(firstname)){

            fname.setError("* required");
            fname.requestFocus();
        }
        else if(TextUtils.isEmpty(lastname)){

            lname.setError("* required");
            lname.requestFocus();

        }
        else if(mobile.length()<=3){

            mnumber.setError("* required");
            mnumber.requestFocus();

        }
        else if(mobile.length()<13 || mobile.length() > 13){

            mnumber.setError("Check your number again");
            mnumber.requestFocus();

        }
        else if(TextUtils.isEmpty(occup)){

            occupation.setError("* required");
            occupation.requestFocus();

        }
        else if(TextUtils.isEmpty(work)){

            workplace.setError("* required");
            workplace.requestFocus();

        }
        else {

            Intent i = getIntent();
            String pid= i.getExtras().getString("pid");

            Map<String, Object> updatedvalues = new HashMap<>();

            updatedvalues.put("/" + pid +"/firstname" ,firstname);
            updatedvalues.put("/" + pid +"/lastname" ,lastname);
            updatedvalues.put("/" + pid +"/mnumber" ,mobile);
            updatedvalues.put("/" + pid +"/occupation" ,occup);
            updatedvalues.put("/" + pid +"/workplace" ,work);

            databaseDta.updateChildren(updatedvalues);

            Toast.makeText(getApplicationContext(),"Welcome " + firstname, Toast.LENGTH_LONG).show();

            Intent v = getIntent();
            String p = v.getExtras().getString("pid");
            Intent intent = new Intent(newuser.this, existinguser.class);
            intent.putExtra("pid",p);
            startActivity(intent);
            finish();

        }




    }

    public void signOutfunction(){

        GoogleSignInClient mGoogleSignInClient ;
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(getBaseContext(), gso);
        mGoogleSignInClient.signOut().addOnCompleteListener(newuser.this,
                new OnCompleteListener<Void>() {  //signout Google
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        FirebaseAuth.getInstance().signOut(); //signout firebase
                        Intent setupIntent = new Intent(getBaseContext(), signin.class);
                        Toast.makeText(getBaseContext(), "Logged Out", Toast.LENGTH_LONG).show(); //if u want to show some text
                        setupIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(setupIntent);
                        finish();
                    }
                });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        signOutfunction();
    }
}
