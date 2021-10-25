package com.kicsiroot.passwordstodo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import butterknife.ButterKnife;
import butterknife.OnTextChanged;

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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class Cards extends AppCompatActivity {

    //private static final int cardTotalSymbols = 19; // size of pattern 0000-0000-0000-0000
    //private static final int cardTotalDigits = 16; // max numbers of digits in pattern: 0000 x 4
    //private static final int cardDividerModulo = 5; // means divider position is every 5th symbol beginning with 1
    //private static final int cardDividerPosition = cardDividerModulo - 1; // means divider position is every 4th symbol beginning with 0
    //private static final char cardDividerChar = '-';

    //private static final int cardDateTotalSymbols = 5; // size of pattern MM/YY
    //private static final int cardDateTotalDigits = 4; // max numbers of digits in pattern: MM + YY
    //private static final int cardDateDividerModulo = 3; // means divider position is every 3rd symbol beginning with 1
    //private static final int cardDateDividerPosition = cardDateDividerModulo - 1; // means divider position is every 2nd symbol beginning with 0
    //private static final char cardDateDividerChar = '/';

    //private static final int CARD_CVC_TOTAL_SYMBOLS = 3;

    RecyclerView.LayoutManager rv_layoutM;
    RecyclerView rv_cards;
    FirebaseAuth mAuth;
    FirebaseUser currentUser;
    FirebaseDatabase fdb;
    DatabaseReference databaseReference;
    Dialog popCardPost;
    TextView cardNumber, cardName, cardDate, cardCVC;
    EditText cardnumber, cardname, carddate, cardcvc;
    Toolbar cardsToolbar;
    FloatingActionButton fb_cards;
    Button cardButton;
    List<CardPost> pCardList;
    CardAdapter cAdap;
    String TAG;

    EditText updateNumber, updateName, updateDate, updateCVC;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cards);
        ButterKnife.bind(this);

        cardsToolbar = findViewById(R.id.cards_toolbar);
        setSupportActionBar(cardsToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();

        rv_cards = findViewById(R.id.rv_cards);
        rv_layoutM = new LinearLayoutManager(this);
        rv_cards.setLayoutManager(rv_layoutM);

        fdb = FirebaseDatabase.getInstance();
        databaseReference = fdb.getReference("Cards");

        //popup
        cardPopup();
        createNotificationChannel();

        fb_cards = findViewById(R.id.fab_cards);
        fb_cards.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popCardPost.show();
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                pCardList = new ArrayList<>();
                for (DataSnapshot cardSnap: snapshot.getChildren()){
                    CardPost cPost = cardSnap.getValue(CardPost.class);
                    pCardList.add(cPost);
                }
                cAdap = new CardAdapter(getBaseContext(), pCardList);
                rv_cards.setAdapter(cAdap);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(Cards.this, "Cannot retrieve data from database!", Toast.LENGTH_SHORT).show();
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

    private void createNotificationChannel(){
        if (Build.VERSION.SDK_INT >= 26){
            NotificationChannel nChannel = new NotificationChannel(getString(R.string.notChannelID), getString(R.string.notChannel1), NotificationManager.IMPORTANCE_DEFAULT);
            nChannel.setDescription(getString(R.string.notChannel2));
            nChannel.setShowBadge(true);
            NotificationManager nManager = getSystemService(NotificationManager.class);
            nManager.createNotificationChannel(nChannel);
        }
    }
    private void triggerNotification(){
        Intent intent = new Intent(this, Cards.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pIntent =  PendingIntent.getActivity(this, 0, intent, 0);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, getString(R.string.notChannel1))
                .setSmallIcon(R.mipmap.ic_launcher_round)
                .setLargeIcon(BitmapFactory.decodeResource(getResources(),R.drawable.notification))
                .setContentTitle("New Card Added")
                .setContentText("Nem tudom akkor ez mi a fasz :D")
                .setStyle(new NotificationCompat.BigTextStyle().bigText("You succesfully added a new item to your Cards menu!"))
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(pIntent)
                .setChannelId(getString(R.string.notChannelID))
                .setAutoCancel(true);

        NotificationManagerCompat nMC = NotificationManagerCompat.from(this);
        nMC.notify(getResources().getInteger(R.integer.notificationId), builder.build());
    }

    private void cardPopup() {
        popCardPost = new Dialog(this);
        popCardPost.setContentView(R.layout.popup_cards);
        popCardPost.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        popCardPost.getWindow().setLayout(Toolbar.LayoutParams.MATCH_PARENT, Toolbar.LayoutParams.WRAP_CONTENT);
        popCardPost.getWindow().getAttributes().gravity = Gravity.CENTER;

        //popup widgets
        cardnumber = popCardPost.findViewById(R.id.cardsNumber);
        cardname = popCardPost.findViewById(R.id.cardsName);
        carddate = popCardPost.findViewById(R.id.cardsDate);
        cardcvc = popCardPost.findViewById(R.id.cardsCVC);
        cardButton = popCardPost.findViewById(R.id.cardButt);

        cardButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!cardnumber.toString().isEmpty() && !cardname.toString().isEmpty() && !carddate.toString().isEmpty() && !cardcvc.toString().isEmpty()){
                    CardPost cPost = new CardPost(cardnumber.getText().toString(),
                            cardname.getText().toString(),
                            carddate.getText().toString(),
                            cardcvc.getText().toString(),
                            currentUser.getUid());
                    //Add to database
                    addPost(cPost);
                    clearText();

                } else {
                    Toast.makeText(Cards.this, "Empty fields!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void addPost(CardPost cardPost) {
        FirebaseDatabase db = FirebaseDatabase.getInstance();
        DatabaseReference dataRef = db.getReference("Cards").push();
        //get post unique ID and update post key
        String key = dataRef.getKey();
        cardPost.setPostKey(key);
        //add post data to firebase database
        dataRef.setValue(cardPost).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(Cards.this, "Successfully added!", Toast.LENGTH_SHORT).show();
                triggerNotification();
                popCardPost.dismiss();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(Cards.this, "Something went wrong!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void clearText(){
        cardnumber.getText().clear();
        cardname.getText().clear();
        carddate.getText().clear();
        cardcvc.getText().clear();
    }

}