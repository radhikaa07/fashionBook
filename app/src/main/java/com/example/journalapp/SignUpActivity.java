package com.example.journalapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class SignUpActivity extends AppCompatActivity {

    EditText password_create, username_create, email_create;
    Button createBTN;

    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener authStateListener;
    private FirebaseUser currentUser;




    private FirebaseFirestore db= FirebaseFirestore.getInstance();
    private CollectionReference collectionReference= db.collection("Users");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        createBTN= findViewById(R.id.acc_signUp_btn);
        password_create= findViewById(R.id.password_create);
        username_create= findViewById(R.id.username_create_ET);
        email_create= findViewById(R.id.email_create);


        firebaseAuth= FirebaseAuth.getInstance();

        authStateListener= new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                currentUser= firebaseAuth.getCurrentUser();

                if(currentUser!=null){

                }
                else{

                }
            }
        };

        createBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!TextUtils.isEmpty(email_create.getText().toString())
                && !TextUtils.isEmpty(username_create.getText().toString())
                && !TextUtils.isEmpty(password_create.getText().toString())){

                    String email= email_create.getText().toString().trim();
                    String pass= password_create.getText().toString().trim();
                    String username = username_create.getText().toString().trim();

                    CreateUserEmailAccount(email,pass,username);
                }
                else{
                    Toast.makeText(SignUpActivity.this, "Account is created successfully", Toast.LENGTH_SHORT).show();
                }
            }
        });



    }

    private void CreateUserEmailAccount(String email, String pass, String usename){
         if(!TextUtils.isEmpty(email) && !TextUtils.isEmpty(pass) && !TextUtils.isEmpty(usename)){
             firebaseAuth.createUserWithEmailAndPassword(email
                     ,pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                 @Override
                 public void onComplete(@NonNull Task<AuthResult> task) {
                     if(task.isSuccessful()){
                         Toast.makeText(SignUpActivity.this, "Account is created successfully", Toast.LENGTH_SHORT).show();
                     }
                 }
             });
         }
    }










}