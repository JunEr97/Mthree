package com.example.mobileapp_assignment2;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.example.mobileapp_assignment2.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.util.HashMap;
import java.util.Map;

public class EditProfile extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private DrawerLayout drawer;
    private static final String TAG = "MainActivity";
    EditText name,department,email,phone,employeeid;
    Button save,back;
    FirebaseAuth fAuth;
    FirebaseFirestore fStore;
    String userID;
    FirebaseUser user;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        final FirebaseAuth fAuth;
        final FirebaseFirestore fStore;
        final String userID;


        name =findViewById(R.id.editname);
        employeeid=findViewById(R.id.editid);
        email=findViewById(R.id.editemail);
        phone=findViewById(R.id.editphone);
        department=findViewById(R.id.editdepartment);
        save=findViewById(R.id.btnpsave);
        back=findViewById(R.id.btneditback);

        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();
        user =fAuth.getCurrentUser();

        userID = fAuth.getCurrentUser().getUid();
        final DocumentReference documenentReference = fStore.collection("users").document(userID);
        documenentReference.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                name.setText(value.getString("fname"));
                employeeid.setText(value.getString("employeeid"));
                email.setText(value.getString("email"));
                phone.setText(value.getString("phone"));
                department.setText(value.getString("department"));

            }
        });

//
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(email.getText().toString().isEmpty() || name.getText().toString().isEmpty() || phone.getText().toString().isEmpty() || employeeid.getText().toString().isEmpty()
                        ||department.getText().toString().isEmpty()) {
                    Toast.makeText(EditProfile.this, "One or amny fields are empty.", Toast.LENGTH_SHORT).show();
                    return;
                }


                String Email = email.getText().toString().trim();
                String Name = name.getText().toString().trim();;
                String Department= department.getText().toString().trim();;
                String ID=employeeid.getText().toString().trim();;
                String Phoneno=phone.getText().toString().trim();;

                user.updateEmail(Email).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(EditProfile.this, "Email updated succesfully.", Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(EditProfile.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
                DocumentReference defref = fStore.collection("users").document(userID);
                Map<String, Object> edited = new HashMap<>();
                edited.put("fname",Name);
                edited.put("employeeid",ID);
                edited.put("email",Email);
                edited.put("phone",Phoneno);
                edited.put("department",Department);
                defref.set(edited).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG,"onSuccess: user Profile is created for "+ userID);
                    }
                });
                Toast.makeText(EditProfile.this, "Profile Updated!", Toast.LENGTH_SHORT).show();
//
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(getApplicationContext(), Profile.class));
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