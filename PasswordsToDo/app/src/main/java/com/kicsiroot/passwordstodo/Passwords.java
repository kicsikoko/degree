package com.kicsiroot.passwordstodo;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Layout;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Adapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicMarkableReference;

public class Passwords extends AppCompatActivity {


    RecyclerView.LayoutManager rvManager;
    RecyclerView postRecycleV;
    PostAdapter postAdapter;
    private RecyclerView.Adapter rvAdapter;
    FloatingActionButton fb_pwd;
    Passwords activity;
    //TextView popUrl, popEmail, popUsername, popPassword;
    Toolbar pwdToolbar;
    Dialog popAddPwd;
    EditText popUrl, popEmail, popUsername, popPassword;
    Button popButt;
    FirebaseUser currentUser;
    FirebaseAuth auth;
    FirebaseDatabase fDb;
    DatabaseReference dRef;
    DatabaseReference mData;
    //String postKey;
    List<Post>  postList;
    String TAG;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_passwords);

        pwdToolbar = findViewById(R.id.pwd_toolbar);
        setSupportActionBar(pwdToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        auth = FirebaseAuth.getInstance();
        currentUser = auth.getCurrentUser();


        //View v = new View(activity);
        //activity.setContentView(v);
        postRecycleV = findViewById(R.id.rv_pwds);
        rvManager = new LinearLayoutManager(this);
        postRecycleV.setLayoutManager(rvManager);
        //postRecycleV.setAdapter(new PostAdapter(activity, postList));
        fDb = FirebaseDatabase.getInstance();
        dRef= fDb.getReference("Post");

        iniPopup();

        fb_pwd = findViewById(R.id.fab_pwd);
        fb_pwd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popAddPwd.show();

            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();

        dRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                postList = new ArrayList<>();
                for (DataSnapshot postsnap: snapshot.getChildren()) {
                    Post post = postsnap.getValue(Post.class);
                    postList.add(post);
                }
                postAdapter = new PostAdapter(getBaseContext(), postList);
                postRecycleV.setAdapter(postAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(Passwords.this, error.toString(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home){
            finish();
            return true;
        } else
            return super.onOptionsItemSelected(item);
    }

    private void iniPopup() {
        popAddPwd = new Dialog(this);
        popAddPwd.setContentView(R.layout.popup_add_pwd);
        popAddPwd.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        popAddPwd.getWindow().setLayout(Toolbar.LayoutParams.MATCH_PARENT, Toolbar.LayoutParams.WRAP_CONTENT);
        popAddPwd.getWindow().getAttributes().gravity = Gravity.CENTER;

        //ini popup widgets
        popUrl = popAddPwd.findViewById(R.id.addURL);
        popEmail = popAddPwd.findViewById(R.id.addEmail);
        popUsername = popAddPwd.findViewById(R.id.addUsername);
        popPassword = popAddPwd.findViewById(R.id.addPwd);
        popButt = popAddPwd.findViewById(R.id.addButton);

        //Add click listener
        popButt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!popUrl.toString().isEmpty() && !popEmail.toString().isEmpty() && !popUsername.toString().isEmpty() && !popPassword.toString().isEmpty() ){
                    //if everything is okay
                    String pwd = null;
                    try {
                        pwd = Crypt.encrypt(popPassword.getText().toString());
                    } catch (Exception e){
                        e.printStackTrace();
                    }
                    Post post = new Post(popUrl.getText().toString(),
                            popEmail.getText().toString(),
                            popUsername.getText().toString(),
                            pwd,
                            currentUser.getUid(),
                            currentUser.getPhotoUrl());

                    //Add post to Firebase db
                    addPost(post);
                    clearText();
                } else {
                    Toast.makeText(Passwords.this,
                            "Something went wrong!",
                            Toast.LENGTH_SHORT).show();

                }
            }
        });


    }

    private void addPost(Post post) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("Post").push();
        //get post unique ID and update post key
        String key = myRef.getKey();
        post.setPostKey(key);
        //add post data to firebase database
        myRef.setValue(post).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(Passwords.this, "Successfully Added", Toast.LENGTH_SHORT).show();
                popAddPwd.dismiss();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(Passwords.this, "Something went wrong! :(", Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void clearText(){
        popEmail.getText().clear();
        popUrl.getText().clear();
        popPassword.getText().clear();
        popUsername.getText().clear();
    }
}