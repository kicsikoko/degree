package com.kicsiroot.passwordstodo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class CardsDetail extends AppCompatActivity {

    Button saveButton, backfromUpdate, deleteB;
    EditText updateNumber, updateName, updateDate, updateCVC;


    FirebaseUser currentUserCD;
    FirebaseAuth authCD;
    FirebaseDatabase fDb;
    DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cards_detail);


        updateNumber = findViewById(R.id.update_url);
        updateName = findViewById(R.id.update_email);
        updateDate = findViewById(R.id.update_username);
        updateCVC = findViewById(R.id.update_pwd);
        saveButton = findViewById(R.id.card_save);
        backfromUpdate = findViewById(R.id.card_back);
        deleteB = findViewById(R.id.card_delete);

        authCD = FirebaseAuth.getInstance();
        currentUserCD = authCD.getCurrentUser();
        fDb = FirebaseDatabase.getInstance();

        final String postNumber = getIntent().getStringExtra("number");
        final String postName = getIntent().getStringExtra("name");
        final String postDate = getIntent().getStringExtra("date");
        final String postCVC = getIntent().getStringExtra("cvc");
        final String postId = getIntent().getStringExtra("key");

        updateNumber.setText(postNumber);
        updateName.setText(postName);
        updateDate.setText(postDate);
        updateCVC.setText(postCVC);

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                reference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        snapshot.getRef().child("cCVC").setValue(postCVC);
                        snapshot.getRef().child("cDate").setValue(postDate);
                        snapshot.getRef().child("cName").setValue(postName);
                        snapshot.getRef().child("cNumber").setValue(postNumber);
                        snapshot.getRef().child("postKey").setValue(postId);
                        Intent intent = new Intent(CardsDetail.this, Cards.class);
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
                Intent intent = new Intent(CardsDetail.this, Passwords.class);
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
                            Intent intent = new Intent(CardsDetail.this, Cards.class);
                            startActivity(intent);
                        } else {
                            Toast.makeText(CardsDetail.this, "Error", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });

    }
}