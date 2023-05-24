package com.satish.newsapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.accounts.Account;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

public class SignOutActivity extends AppCompatActivity {

    GoogleSignInOptions googleSignInOptions;
    GoogleSignInClient googleSignInClient;

    TextView name,email;
    Button signOutBtn;
    ImageView profile_image;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_out);

        signOutBtn=findViewById(R.id.signOut);
        name = findViewById(R.id.user_name);
        email = findViewById(R.id.email);
        profile_image=findViewById(R.id.profile_image_user);

        fetchUserData();
        signOuBtnClick();



    }

    private void fetchUserData() {
        googleSignInOptions=new GoogleSignInOptions
                .Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        googleSignInClient= GoogleSignIn.getClient(SignOutActivity.this,googleSignInOptions);

        GoogleSignInAccount account=GoogleSignIn.getLastSignedInAccount(SignOutActivity.this);
        if (account!=null){
            String Name=account.getDisplayName();
            String Email=account.getEmail();
            name.setText(Name);
            email.setText(Email);


            Glide.with(getApplicationContext())
                    .load(account.getPhotoUrl())
                    .circleCrop()
                    .error(R.drawable.no_image_avilable)
                    .into(profile_image);
        }
    }

    private void signOuBtnClick() {
        signOutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SignOut();

            }
        });
    }

    private void SignOut() {
        googleSignInClient.signOut().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                finish();
                startActivity(new Intent(SignOutActivity.this,LoginActivity.class));
            }
        });


    }
}