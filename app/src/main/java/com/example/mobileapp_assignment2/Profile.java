package com.example.mobileapp_assignment2;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

public class Profile extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private DrawerLayout drawer;
    TextView name,department,email,employeeid,phone,password;
    FirebaseAuth fAuth;
    FirebaseFirestore fStore;
    String userID;

    Button edit,back;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        name =findViewById(R.id.tvname);
        employeeid=findViewById(R.id.tvid);
        email=findViewById(R.id.tvemail);
        phone=findViewById(R.id.tvphoneno);
        department=findViewById(R.id.tvdepartment);
        edit=findViewById(R.id.btnpedit);
        back=findViewById(R.id.btnpback);

        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();

        userID = fAuth.getCurrentUser().getUid();
        DocumentReference documenentReference = fStore.collection("users").document(userID);
        documenentReference.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                name.setText(value.getString("fname"));
                employeeid.setText(value.getString("intiid"));
                email.setText(value.getString("email"));
                phone.setText(value.getString("phone"));
                department.setText(value.getString("department"));

            }
        });

        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(getApplicationContext(), EditProfile.class));
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(getApplicationContext(), EditProfile.class));
            }
        });


        drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();


    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
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

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    public void setting(View view) {
        startActivity(new Intent(getApplicationContext(),EditProfile.class));
    }
}