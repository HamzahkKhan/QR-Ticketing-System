package com.example.qrcode.Extra_Features;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.qrcode.Databases.userinfo;
import com.example.qrcode.Extras_NotInUse.Message;
import com.example.qrcode.R;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class signin extends AppCompatActivity {
    SignInButton signInButton;
    FirebaseAuth firebaseAuth;
    GoogleSignInClient googleSignInClient;
    DatabaseReference databaseDta;
    ProgressBar progressBar;
    TextView SignIn;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signin);


        firebaseAuth = FirebaseAuth.getInstance();

        // Configure Google Sign In
        GoogleSignInOptions googleSignInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        googleSignInClient = GoogleSignIn.getClient(this, googleSignInOptions);
        signInButton = findViewById(R.id.googleSignIn);

        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent signInIntent = googleSignInClient.getSignInIntent();
                startActivityForResult(signInIntent, 101);

            }
        });

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == 101) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account);
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately

                // ...
            }
        }
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount account) {

        AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
        firebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {

                            FirebaseUser user = firebaseAuth.getCurrentUser();
                            String email = user.getEmail();
                            checkeruser(email);


                        } else {
                            // If sign in fails, display a message to the user.


                            Toast.makeText(getApplicationContext(),"Cannot sign in",Toast.LENGTH_LONG).show();

                        }

                        // ...
                    }
                });

    }

    public void checkeruser(final String uemail){

        final String useremail= uemail;
        progressBar = findViewById(R.id.signIn);
        SignIn = findViewById(R.id.signingIn);
        progressBar.setVisibility(View.VISIBLE);
        SignIn.setVisibility(View.VISIBLE);

        Query query = FirebaseDatabase.getInstance().getReference("userinfo")
                .orderByChild("email")
                .equalTo(useremail);

        query.addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        userinfo dbb = snapshot.getValue(userinfo.class);
                        String fname= dbb.getFirstname();

                        if (fname.equals("")){
                            Intent i = new Intent(getApplicationContext(), newuser.class);
                            i.putExtra("pid",dbb.getPid());
                            startActivity(i);
                        } else {

                            Intent i = new Intent(getApplicationContext(), existinguser.class);
                            i.putExtra("pid",dbb.getPid());
                            startActivity(i);
                        }

                    }



                }
                else {

                    addemail(useremail);


                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }

    private void addemail(String addemail){

        databaseDta = FirebaseDatabase.getInstance().getReference("userinfo");

        String id = databaseDta.push().getKey();
        userinfo data = new userinfo(id, addemail, "", "", "", "", "");
        databaseDta.child(id).setValue(data);
        Message.message(getApplicationContext(),"Account Created");
        Intent i = new Intent(getApplicationContext(),newuser.class);
        i.putExtra("pid", id);
        startActivity(i);

    }

}
