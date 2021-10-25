package com.kicsiroot.passwordstodo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.bumptech.glide.load.resource.gif.StreamGifDecoder;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class PasswordsDetail extends AppCompatActivity {

    private static final String TAG = "PwdDetail";

    Button saveButton, backfromUpdate, deleteB;
    EditText updateUrl, updateEmail, updateUsername, updatePwd;

    FirebaseUser currentUserPD;
    FirebaseAuth authPD;
    FirebaseDatabase fDb;
    DatabaseReference reference;
    String postKey;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_passwords_detail);
        Log.d(TAG, "onCreate: started");

        updateUrl = findViewById(R.id.update_url);
        updateEmail = findViewById(R.id.update_email);
        updateUsername = findViewById(R.id.update_username);
        updatePwd = findViewById(R.id.update_pwd);
        saveButton = findViewById(R.id.updateBtnPwd);
        backfromUpdate = findViewById(R.id.pwd_back);
        deleteB = findViewById(R.id.pwd_delete);

        authPD = FirebaseAuth.getInstance();
        currentUserPD = authPD.getCurrentUser();
        fDb = FirebaseDatabase.getInstance();


        final String postUrl = getIntent().getStringExtra("url");
        final String postEmail = getIntent().getStringExtra("email");
        final String postUName = getIntent().getStringExtra("username");
        String postPwd = getIntent().getStringExtra("pwd");
        final String postId = getIntent().getStringExtra("key");
        try {
            postPwd = Crypt.decrypt(postPwd);
            updatePwd.setText(postPwd);
        } catch (Exception e){
            e.printStackTrace();
        }

        updateUrl.setText(postUrl);
        updateEmail.setText(postEmail);
        updateUsername.setText(postUName);
        updatePwd.setText(postPwd);

        String updateUrl = updateEmail.toString();
        //String updateName = updateUrl

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final String userId = currentUserPD.getUid();

                reference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        snapshot.getRef().child("addEmail").setValue(postUrl);
                        snapshot.getRef().child("addUrl").setValue(postEmail);
                        snapshot.getRef().child("addUsername").setValue(postUName);
                        snapshot.getRef().child("addPassword").setValue(updatePwd);
                        snapshot.getRef().child("postKey").setValue(postId);
                        Intent intent = new Intent(PasswordsDetail.this, Passwords.class);
                        startActivity(intent);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        });
        backfromUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(PasswordsDetail.this, Passwords.class);
                startActivity(intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
                finish();
            }
        });

        deleteB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                reference.removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()){
                            Intent intent = new Intent(PasswordsDetail.this, Passwords.class);
                            startActivity(intent);
                        } else {
                            Toast.makeText(PasswordsDetail.this, "Error", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });

    }

    private void updatePost(Post post) {
        FirebaseDatabase db = FirebaseDatabase.getInstance();
        DatabaseReference dref = db.getReference("Post").push();
        String ukey = dref.getKey();
        post.setPostKey(ukey);
        dref.setValue(post).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(PasswordsDetail.this, "Successfully Updated!", Toast.LENGTH_SHORT).show();
                finish();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(PasswordsDetail.this, "Unfortunatelly it is failed!", Toast.LENGTH_SHORT).show();
            }
        });
    }
}