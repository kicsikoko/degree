package com.kicsiroot.passwordstodo;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NavUtils;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.Html;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.view.MenuItem;
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
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.snackbar.SnackbarContentLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import org.w3c.dom.Text;

import static android.util.Patterns.EMAIL_ADDRESS;

public class RegisterActivity extends AppCompatActivity {

    private FirebaseAuth auth;
    private FirebaseUser user;
    private DatabaseReference db;
    public EditText regEmail, regPwd, regPwdcheck;
    public Button regBtn;
    ImageView regIV;
    static int FReqcode = 1;
    static int REQUESTCODE = 1;
    Uri pickedImgUri;
    ProgressBar progressBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        regEmail = (EditText) findViewById(R.id.etxtRegmail);
        regPwd = (EditText) findViewById(R.id.etxtRegpwd);
        regPwdcheck = (EditText) findViewById(R.id.etxtRegpwdcheck);
        regBtn = (Button) findViewById(R.id.btnSignup);
        regIV = findViewById(R.id.regImage);
        progressBar = findViewById(R.id.regProgressbar);
        progressBar.setVisibility(View.INVISIBLE);

        Intent myintent = getIntent();
        String email_value = myintent.getStringExtra("toReg");
        regEmail.setText(email_value);

        auth = FirebaseAuth.getInstance();
        db = FirebaseDatabase.getInstance().getReference().child("RegisteredUsers");

        regIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Build.VERSION.SDK_INT >= 23) {
                    checkAndRequestPermission();
                }
                else {
                    openGallery();
                }
            }
        });


        //final View view =  findViewById(R.id.reg_layout);


        regBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String txt_email = regEmail.getText().toString();
                String txt_pwd = regPwd.getText().toString();
                String txt_pwdCheck = regPwdcheck.getText().toString();

                boolean temp = true;

                regBtn.setVisibility(View.INVISIBLE);
                progressBar.setVisibility(View.VISIBLE);

                if (TextUtils.isEmpty(txt_email) || TextUtils.isEmpty(txt_pwd)){
                    Toast.makeText(RegisterActivity.this, "Empty Fields!", Toast.LENGTH_SHORT).show();
                    regBtn.setVisibility(View.VISIBLE);
                    progressBar.setVisibility(View.INVISIBLE);
                } else if (!EMAIL_ADDRESS.matcher(txt_email).matches()) {
                    Toast.makeText(RegisterActivity.this, "Invalid Email Address!", Toast.LENGTH_SHORT).show();

                }else if (txt_pwd.length() < 8){
                    Toast.makeText(RegisterActivity.this, "Password must be at least 8 character!", Toast.LENGTH_SHORT).show();
                    regBtn.setVisibility(View.VISIBLE);
                    progressBar.setVisibility(View.INVISIBLE);
                } else if (!txt_pwd.equals(txt_pwdCheck)){
                    Toast.makeText(RegisterActivity.this, "Passwords not match!", Toast.LENGTH_SHORT).show();
                    temp = false;
                    regBtn.setVisibility(View.VISIBLE);
                    progressBar.setVisibility(View.INVISIBLE);
                }
                else {
                    registerUser(txt_email, txt_pwd);
                }
            }
        });


        TextView support = (TextView) findViewById(R.id.reg_tvSupport);
        support.setText(Html.fromHtml("<a href=\"mailto:kicsiroot@gmail.com\">Support</a>"));
        support.setMovementMethod(LinkMovementMethod.getInstance());

        TextView about = (TextView) findViewById(R.id.reg_tvAbout);
        about.setText(Html.fromHtml("<a href=\"https://youtube.com/channel/UCv5Ddk0aS6BR-4ZZtfHBrZA\">About</a>"));
        about.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                linkClick("https://youtube.com/channel/UCv5Ddk0aS6BR-4ZZtfHBrZA");
            }
        });

    }


    private void registerUser(final String txt_email, final String txt_pwd) {
        auth.createUserWithEmailAndPassword(txt_email, txt_pwd).addOnCompleteListener(RegisterActivity.this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    user = auth.getCurrentUser();
                    String id = user.getUid();
                    User u = new User(txt_email, txt_pwd);
                    updateUserInfo(txt_email, pickedImgUri, auth.getCurrentUser());
                    db.child(id).setValue(u).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(RegisterActivity.this, "Sign up succssesfull!", Toast.LENGTH_SHORT).show();
                                String message = regEmail.getText().toString().trim();
                                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                                intent.putExtra("email", message);
                                startActivity(intent);
                                finish();
                            } else {
                                Toast.makeText(RegisterActivity.this, "Sign up failed!", Toast.LENGTH_SHORT).show();
                                regBtn.setVisibility(View.VISIBLE);
                                progressBar.setVisibility(View.INVISIBLE);
                            }
                        }
                    });
                }
            }
        });
    }

    private void updateUserInfo(final String txt_email, Uri pickedImgUri, final FirebaseUser currentUser) {
        StorageReference mStorage = FirebaseStorage.getInstance().getReference().child("users_photos");
        final StorageReference imageFilePath = mStorage.child(pickedImgUri.getPath());
        imageFilePath.putFile(pickedImgUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(final UploadTask.TaskSnapshot taskSnapshot) {
                //image uploaded successfully
                //now we can get  our image uri

                imageFilePath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        UserProfileChangeRequest profileUpdate = new UserProfileChangeRequest.Builder()
                                .setDisplayName(txt_email)
                                .setPhotoUri(uri)
                                .build();

                        currentUser.updateProfile(profileUpdate).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()){
                                    //user info updated successfully
                                    Toast.makeText(RegisterActivity.this, "Updated!", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }
                });
            }
        });
    }

    private void openGallery() {
        //TODO: open gallery intent and wait for user to pick an image !

        Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
        galleryIntent.setType("image/*");
        startActivityForResult(galleryIntent, REQUESTCODE);
    }

    private void checkAndRequestPermission() {
        if (ContextCompat.checkSelfPermission(RegisterActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(RegisterActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
                Toast.makeText(RegisterActivity.this, "Please accept for required permission", Toast.LENGTH_SHORT).show();
            }
            else {
                ActivityCompat.requestPermissions(RegisterActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, FReqcode);
            }
        } else {
            openGallery();
        }
    };

    public void linkClick(String url){
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(url));
        startActivity(intent);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == REQUESTCODE && data != null){
            //the user has successfully  picked an image, we need to save its reference to a Uri variable
            pickedImgUri = data.getData();
            regIV.setImageURI(pickedImgUri);
        }
    }

}