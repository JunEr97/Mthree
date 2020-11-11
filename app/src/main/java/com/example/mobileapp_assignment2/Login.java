package com.example.mobileapp_assignment2;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class Login extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private DrawerLayout drawer;

    Button signup, resetpass, login;
    TextView reset;
    EditText email, password;
    FirebaseAuth fAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        signup = findViewById(R.id.btn_gosignup);
        login = findViewById(R.id.btn_login);
        resetpass = findViewById(R.id.btn_forgotpass);
        email = findViewById(R.id.etloguser);;
        password = findViewById(R.id.etpass);

        fAuth = FirebaseAuth.getInstance();

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(getApplicationContext(), Register.class));
            }
        });


        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String Email = email.getText().toString().trim();
                String Password = password.getText().toString().trim();

                if (TextUtils.isEmpty(Email)) {
                    email.setError("Email is required.");
                    return;
                }

                if (TextUtils.isEmpty(Password)) {
                    password.setError("Password is required.");
                    return;
                }

                if (password.length() < 6) {
                    password.setError("Password must be more than 6 characters.");
                    return;
                }

                //authenticate user
                fAuth.signInWithEmailAndPassword(Email, Password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(Login.this,"Logged in successfully.", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(getApplicationContext(), Dashboard.class));
                        } else{
                            Toast.makeText(Login.this, "Error! "+task.getException().getMessage(), Toast.LENGTH_SHORT).show();

                        }
                    }
                });

            }

        });

        resetpass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), ResetPassword.class));
            }
        });

        drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view1);
        navigationView.setNavigationItemSelectedListener(this);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.nav_project:
                startActivity(new Intent(getApplicationContext(), Dashboard.class));
                break;
            case R.id.nav_location:
                //startActivity(new Intent(getApplicationContext(),Location.class));
                break;
            case R.id.nav_edit_profile:
                startActivity(new Intent(getApplicationContext(),EditProfile.class));
                break;
            case R.id.nav_logout:
                Toast.makeText(this, "Logged Out.", Toast.LENGTH_SHORT).show();
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(getApplicationContext(),Login.class));
                break;
        }
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
