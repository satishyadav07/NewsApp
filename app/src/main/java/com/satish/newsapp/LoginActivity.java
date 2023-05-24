package com.satish.newsapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GetTokenResult;
import com.google.firebase.auth.GoogleAuthProvider;


public class LoginActivity extends AppCompatActivity {
    private String client_id ="700833497335-9qauujqbtfasopgvo0o5kmpmbkkbebkd.apps.googleusercontent.com";
    private FirebaseAuth firebaseAuth;
    GoogleSignInOptions googleSignInOptions;
    private static final int RC_SIGN_IN = 3;
    private static final String TAG = "LoginActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        SignInButton signInButton = findViewById(R.id.sign_in_btn);
        firebaseAuth = FirebaseAuth.getInstance();
        buildGoogleSignInOption();
        GoogleSignInClient googleSignInClient = GoogleSignIn.getClient(this, googleSignInOptions);
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);
        if (account != null) {
            updateUI(account);
        }
        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: ");
                Intent signInClient = googleSignInClient.getSignInIntent();
                startActivityForResult(signInClient, RC_SIGN_IN);
            }
        });



    }

    private void buildGoogleSignInOption() {
        Log.d(TAG, "onCreate: 2");
        googleSignInOptions = new GoogleSignInOptions
                .Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(client_id)
                .requestEmail()
                .build();

    }
    private void updateUI(GoogleSignInAccount account) {
        startActivity(new Intent(LoginActivity.this, MainActivity.class));

    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            Log.d(TAG, "onActivityResult: ");
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }

    }

    private void handleSignInResult(Task<GoogleSignInAccount> task) {
        try {
            Log.d(TAG, "handleSignInResult: ");
            GoogleSignInAccount account = task.getResult(ApiException.class);
            // Signed in successfully, authenticate with Firebase
            firebaseAuthWithGoogle(account.getIdToken());
        } catch (ApiException e) {
            // Sign-in failed, handle the error
            Log.w(TAG, "signInResult:failed code=" + e.getStatusCode());
        }
    }

    private void firebaseAuthWithGoogle(String idToken) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        firebaseAuth.signInWithCredential(credential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isCanceled()) {
                    Log.d(TAG, "isCanceled: ");
                    return;
                } else if (task.isSuccessful()) {
                    Log.d(TAG, "isSuccessful: ");
                    FirebaseUser user = firebaseAuth.getCurrentUser();
                    if (user != null) {
                        user.getIdToken(true).addOnSuccessListener(new OnSuccessListener<GetTokenResult>() {
                            @Override
                            public void onSuccess(GetTokenResult getTokenResult) {
                                if (user.isEmailVerified()) {
                                    Log.d(TAG, "onComplete: " + user.getEmail());
                                    Log.d(TAG, "onComplete: " + user.getDisplayName());
                                    Intent i = new Intent(LoginActivity.this, MainActivity.class);
                                    startActivity(i);
                                }
                            }
                        });
                    }
                }

            }
        });
    }


}