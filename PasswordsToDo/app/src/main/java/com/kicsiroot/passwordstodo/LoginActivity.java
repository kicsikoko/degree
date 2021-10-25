package com.kicsiroot.passwordstodo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.core.Constants;

public class LoginActivity extends AppCompatActivity {

    private EditText logEmail;
    private EditText logPwd;
    private Button logBtn;

    private ImageView imgView;
    private ImageView imgView1;

    private FirebaseAuth auth;
    private TextView textView;
    private ProgressBar logProgBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        imgView = (ImageView)findViewById(R.id.locked);
        imgView1 = (ImageView)findViewById(R.id.unlocked);
        logEmail = findViewById(R.id.etxtEmail);
        logPwd = findViewById(R.id.etxtPwd);
        logBtn = findViewById(R.id.login_btn);
        textView = findViewById(R.id.emailDisplay);
        logProgBar = findViewById(R.id.login_pb);
        logProgBar.setVisibility(View.INVISIBLE);

        auth = FirebaseAuth.getInstance();

        String eamil_in =  getIntent().getStringExtra("email");
        logEmail.setText(eamil_in);

        logBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String txt_email = logEmail.getText().toString();
                String txt_pwd = logPwd.getText().toString();
                if (TextUtils.isEmpty(txt_email) && TextUtils.isEmpty(txt_pwd)){
                    Toast.makeText(LoginActivity.this, "Empty fields!", Toast.LENGTH_SHORT).show();
                    logBtn.setVisibility(View.VISIBLE);
                    logProgBar.setVisibility(View.INVISIBLE);
                } else {
                    logBtn.setVisibility(View.INVISIBLE);
                    logProgBar.setVisibility(View.VISIBLE);
                    loginUser(txt_email, txt_pwd);
                }
            }
        });

        final TextView textView = findViewById(R.id.tvSignup);
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                String emailtoReg = logEmail.getText().toString().trim();
                if(TextUtils.isEmpty(emailtoReg)) {
                    startActivity(intent);
                    logPwd.setText("");
                } else {
                    intent.putExtra("toReg", emailtoReg);
                    startActivity(intent);
                }
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser user = auth.getCurrentUser();
        if (user != null) {
            //user is already connected so we need to redirect to the Home page.
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        }
    }

    private void loginUser(final String email, String pwd) {
        final String regMail = logEmail.getText().toString().trim();
        final String regPwd = logPwd.getText().toString().trim();
        auth.signInWithEmailAndPassword(email, pwd).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    User user = new User(regMail, regPwd);
                    Toast.makeText(LoginActivity.this, "Login Successfull!", Toast.LENGTH_SHORT).show();
                    imgView.setVisibility(View.INVISIBLE);
                    imgView1.setVisibility(View.VISIBLE);
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    logBtn.setVisibility(View.VISIBLE);
                    logProgBar.setVisibility(View.INVISIBLE);
                    Toast.makeText(LoginActivity.this, "Wrong password!", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
}