package com.example.qrcode.Extra_Features;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.qrcode.R;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class myaccount extends AppCompatActivity {
    Button bsignout;



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.updateinfo);

    }

    public void updateName(View view){

        Intent intent = getIntent();
        String r = intent.getExtras().getString("pid");

        Intent i = new Intent(myaccount.this, updatename.class);
        i.putExtra("pid",r);
        startActivity(i);

    }

    public void updatePhone(View view){

        Intent intent = getIntent();
        String r = intent.getExtras().getString("pid");

        Intent i = new Intent(myaccount.this, updatephone.class);
        i.putExtra("pid",r);
        startActivity(i);

    }

    public void updateOccu(View view){

        Intent intent = getIntent();
        String r = intent.getExtras().getString("pid");

        Intent i = new Intent(myaccount.this, updateoccu.class);
        i.putExtra("pid",r);
        startActivity(i);

    }

    public void updateWork(View view){

        Intent intent = getIntent();
        String r = intent.getExtras().getString("pid");

        Intent i = new Intent(myaccount.this, updatework.class);
        i.putExtra("pid",r);
        startActivity(i);

    }

    public void deleteaccount(){

    }

    public void signOutfunction(){

        GoogleSignInClient mGoogleSignInClient ;
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(getBaseContext(), gso);
        mGoogleSignInClient.signOut().addOnCompleteListener(myaccount.this,
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
        Intent intent = new Intent(myaccount.this, existinguser.class);
        startActivity(intent);
    }
}
