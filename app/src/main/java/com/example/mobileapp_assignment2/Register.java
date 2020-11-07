package com.example.mobileapp_assignment2;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;


public class Register extends AppCompatActivity {

    private static final String TAG = "Register";

    EditText name,deparment,email,password,cpassword,phone,employeeid;
    CheckBox agree;
    Button signup;
    TextView goLogin,title,fname,fusername,femail,fpassword,fcpassword,fdepartment;
    Toolbar toolbar;
    FirebaseAuth fAuth;
    FirebaseFirestore fStore;
    String userID;
    FirebaseUser user;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        name = findViewById(R.id.etname);
        employeeid =findViewById(R.id.etemployeeid);
        email =findViewById(R.id.etemail);
        password =findViewById(R.id.etpass);
        cpassword =findViewById(R.id.etcpass);
        phone =findViewById(R.id.etphoneno);
        deparment = findViewById(R.id.etdepartment);
        agree =findViewById(R.id.cb_agree);
        signup = findViewById(R.id.btn_signup);
        goLogin=findViewById(R.id.btn_gologin);


        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();
        user = fAuth.getCurrentUser();



        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String Email = email.getText().toString().trim();
                final String Password = password.getText().toString().trim();
                final String Name = name.getText().toString().trim();;
                final String Department= deparment.getText().toString().trim();;
                final String ID=employeeid.getText().toString().trim();;
                final String Phoneno=phone.getText().toString().trim();;

                if(TextUtils.isEmpty(Email)){
                    email.setError("Email is required.");
                    return;
                }


                if(TextUtils.isEmpty(Password)){
                    password.setError("Password is required.");
                    return;
                }

                if(password.length() < 6){
                    password.setError("Password must be more than 6 characters.");
                    return;
                }

                if(agree.isChecked()==false){
                    password.setError("Please agree to term and conditions.");
                    return;
                }

                //create user account
                fAuth.createUserWithEmailAndPassword(Email,Password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(Register.this,"Account Created.", Toast.LENGTH_SHORT).show();
                            userID = fAuth.getCurrentUser().getUid();
                            DocumentReference documenentReference = fStore.collection("users").document(userID);
                            Map<String, Object> user = new HashMap<>();
                            user.put("fname",Name);
                            user.put("employeeid",ID);
                            user.put("email",Email);
                            user.put("phone",Phoneno);
                            user.put("password",Password);
                            user.put("department",Department);
                            documenentReference.set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Log.d(TAG,"onSuccess: user Profile is created for "+ userID);
                                }
                            });
                            startActivity(new Intent(getApplicationContext(), Login.class));
                        } else{
                            Toast.makeText(Register.this, "Error! "+task.getException().getMessage(), Toast.LENGTH_SHORT).show();

                        }
                    }
                });

            }
        });



        goLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(getApplicationContext(), Login.class));
            }
        });
    }
}
