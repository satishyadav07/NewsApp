package com.satish.newsapp;

import static android.content.ContentValues.TAG;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;

import android.net.Uri;
import android.os.Bundle;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.bumptech.glide.Glide;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.satish.newsapp.DataFetchListner.OnFetchDataListener;
import com.satish.newsapp.Models.NewsApiResponse;
import com.satish.newsapp.Models.Article;
import com.satish.newsapp.OnSelectListener.SelectListener;
import com.satish.newsapp.RecyclerViewClass.RecyclerViewAdapter;
import com.satish.newsapp.RequestManger.RequestManger;

import java.util.List;

public class MainActivity extends AppCompatActivity implements SelectListener, View.OnClickListener {

    GoogleSignInOptions googleSignInOptions;
    GoogleSignInClient googleSignInClient;
    RecyclerView recyclerView;
    RecyclerViewAdapter recyclerViewAdapter;
    ProgressDialog dialog;
    Button btn1,btn2,btn3,btn4,btn5,btn6,btn7;
    SearchView searchView;
    ImageView user_profile;
    private FirebaseAuth firebaseAuth;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        user_profile=findViewById(R.id.profile_image);
        user_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, SignOutActivity.class));
            }
        });

        searchView=findViewById(R.id.search_view);
        searchBar();

        dialog=new ProgressDialog(this);
        dialog.setTitle("Fetching news article..");
        dialog.show();


        btn1=findViewById(R.id.btn_1);
        btn1.setOnClickListener(this);
        btn2=findViewById(R.id.btn_2);
        btn2.setOnClickListener(this);
        btn3=findViewById(R.id.btn_3);
        btn3.setOnClickListener(this);
        btn4=findViewById(R.id.btn_4);
        btn4.setOnClickListener(this);
        btn5=findViewById(R.id.btn_5);
        btn5.setOnClickListener(this);
        btn6=findViewById(R.id.btn_6);
        btn6.setOnClickListener(this);
        btn7=findViewById(R.id.btn_7);
        btn7.setOnClickListener(this);


        googleSignInOptions=new GoogleSignInOptions
                .Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        googleSignInClient= GoogleSignIn.getClient(this,googleSignInOptions);

       GoogleSignInAccount account=GoogleSignIn.getLastSignedInAccount(this);
        if (account!=null){
            firebaseAuth = FirebaseAuth.getInstance();
            FirebaseUser user = firebaseAuth.getCurrentUser();
            if(user!=null) {
                Log.d(TAG, "onCreate: "+user.getPhotoUrl());
                Glide.with(getApplicationContext())
                        .load(user.getPhotoUrl())
                        .circleCrop()
                        .error(R.drawable.no_image_avilable)
                        .into(user_profile);
            }
        }

        RequestManger manager =new RequestManger(this);
        manager.getNewsHeadlines(listener,"general",null);


    }



    private void searchBar() {
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                dialog.setTitle("Fetching news article of "+query);
                dialog.show();
                RequestManger manager =new RequestManger(MainActivity.this);
                manager.getNewsHeadlines(listener,"general",query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
    }

    private final OnFetchDataListener<NewsApiResponse> listener=new OnFetchDataListener<NewsApiResponse>() {
        @Override
        public void onFetchData(List<Article> list, String message) {
            if (list.isEmpty()){
                Toast.makeText(MainActivity.this, "No data Found!1", Toast.LENGTH_SHORT).show();
            }
            else {
                showNews(list);
                dialog.dismiss();
            }

        }

        @Override
        public void onError(String message) {
            Toast.makeText(MainActivity.this,"An error Occurred!! ",Toast.LENGTH_SHORT).show();
        }
    };

    private void showNews(List<Article> list) {
        recyclerView =findViewById(R.id.recycler_main);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(this,1));
        recyclerViewAdapter = new RecyclerViewAdapter(this,list,this);
        recyclerView.setAdapter(recyclerViewAdapter);
    }

    @Override
    public void OnNewsClicked(Article article) {
        startActivity(new Intent(MainActivity.this, DetailsNewsActivity.class)
                .putExtra("data", article));

    }

    @Override
    public void onClick(View v) {
        Button button=(Button) v;
        String category=button.getText().toString();
        dialog.setTitle("Fetching news Articles " +category);
        dialog.show();

        RequestManger manager =new RequestManger(this);
        manager.getNewsHeadlines(listener,category,null);

    }
}