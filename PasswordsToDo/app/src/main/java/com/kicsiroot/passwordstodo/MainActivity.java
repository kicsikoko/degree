package com.kicsiroot.passwordstodo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;
import de.hdodenhof.circleimageview.CircleImageView;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity  implements NavigationView.OnNavigationItemSelectedListener {

    FirebaseAuth auth;
    FirebaseUser currentUser;
    TextView textView;
    TextView emailet;
    NavigationView navigationView;
    View headerView;
    FloatingActionButton fb_pwd;
    Passwords activity;
    ArrayList list;
    //TextView popUrl, popEmail, popUsername, popPassword;
    LinearLayout layoutT;
    Dialog popAddPwd;
    EditText popUrl, popEmail, popUsername, popPassword;
    Button popButt;
    String postKey;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        auth = FirebaseAuth.getInstance();
        currentUser = auth.getCurrentUser();

        final DrawerLayout drawerLayout = findViewById(R.id.drawerLayout);

        navigationView = findViewById(R.id.navigationView);
        headerView = navigationView.getHeaderView(0);
        emailet = headerView.findViewById(R.id.emailDisplay);
        emailet.setText(currentUser.getEmail());

        updateNavHeader();

        findViewById(R.id.imageMenu).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawerLayout.openDrawer(GravityCompat.START);
            }
        });




        navigationView.setItemIconTintList(null);

        NavController navController = Navigation.findNavController(this, R.id.navHostFragment);
        NavigationUI.setupWithNavController(navigationView, navController);

        navigationView.getMenu().findItem(R.id.menuProfile).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                getSupportFragmentManager().beginTransaction().replace(R.id.navHostFragment, new profileFragment()).commit();
                drawerLayout.closeDrawer(GravityCompat.START);
                return true;
            }
        });
        navigationView.getMenu().findItem(R.id.menuNotifications).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                Intent intent = new Intent(MainActivity.this, NotificationActivity.class);
                startActivity(intent);
                return true;
            }
        });
        navigationView.getMenu().findItem(R.id.menuTodo).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                Intent intent = new Intent(MainActivity.this, TodoActivity.class);
                startActivity(intent);
                return true;
            }
        });

        navigationView.getMenu().findItem(R.id.menuPasswords).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                //getSupportFragmentManager().beginTransaction().replace(R.id.navHostFragment, new passwordsFragment()).commit();
                Intent intent = new Intent(MainActivity.this, Passwords.class);
                startActivity(intent);
                return true;
            }
        });

        navigationView.getMenu().findItem(R.id.menuSettings).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                Intent intent = new Intent(MainActivity.this, Settings.class);
                startActivity(intent);
                return true;
            }
        });

        navigationView.getMenu().findItem(R.id.menuAboutus).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {

                return true;
            }
        });

        navigationView.getMenu().findItem(R.id.menuLogout).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
                return true;
            }
        });

        navigationView.getMenu().findItem(R.id.menuCards).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                Intent intent = new Intent(MainActivity.this, Cards.class);
                startActivity(intent);
                return true;
            }
        });



    }

    public void updateNavHeader(){
        NavigationView navigationView = (NavigationView) findViewById(R.id.navigationView);
        View headerView = navigationView.getHeaderView(0);
        TextView navEmail = headerView.findViewById(R.id.emailDisplay);
        CircleImageView navUserPhoto = headerView.findViewById(R.id.imageProfile);


        Glide.with(this).load(currentUser.getPhotoUrl()).into(navUserPhoto);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        return true;
    }
}