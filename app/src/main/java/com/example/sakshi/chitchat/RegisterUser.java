package com.example.sakshi.chitchat;

import android.app.ProgressDialog;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.content.Intent;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;


public class RegisterUser extends AppCompatActivity implements View.OnClickListener{

    private Button buttonRegister;
    private EditText editTextEmail, editTextPassword, editTextName;
    private ProgressDialog progressDialog;
    private FirebaseAuth firebaseAuth;
    private DatabaseReference RootRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        firebaseAuth = FirebaseAuth.getInstance();

        progressDialog = new ProgressDialog(this);

        buttonRegister = (Button) findViewById(R.id.buttonRegister);

        editTextEmail = (EditText) findViewById(R.id.editTextEmail);
        editTextPassword = (EditText) findViewById(R.id.editTextPassword);
        editTextName = (EditText) findViewById(R.id.editTextName);

        RootRef = FirebaseDatabase.getInstance().getReference();
        buttonRegister.setOnClickListener(this);

    }

    private void registerUser()
    {
        String name = editTextName.getText().toString().trim();
        String email = editTextEmail.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();

        if(TextUtils.isEmpty(name))
        {
            Toast.makeText(this,"Please enter name", Toast.LENGTH_SHORT).show();
            return;
        }


        if(TextUtils.isEmpty(email))
        {
            Toast.makeText(this,"Please enter email", Toast.LENGTH_SHORT).show();
            return;
        }

        if(TextUtils.isEmpty(password))
        {
            Toast.makeText(this,"Please enter password", Toast.LENGTH_SHORT).show();
            return;
        }

        progressDialog.setMessage("Registering user...");
        progressDialog.show();

        firebaseAuth.createUserWithEmailAndPassword(editTextEmail.getText().toString(),editTextPassword.getText().toString())
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if(task.isSuccessful())
                                {
                                    firebaseAuth.getCurrentUser().sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task)
                                        {

                                            if(task.isSuccessful())
                                            {
                                                String curUserId = firebaseAuth.getCurrentUser().getUid();
                                                String userName = editTextName.getText().toString();

                                                HashMap<String, String> profileMap = new HashMap<>();
                                                profileMap.put("uid", curUserId);
                                                profileMap.put("name", userName);

                                                RootRef.child("Users").child(curUserId).setValue(profileMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task)
                                                    {
                                                        if(task.isSuccessful())
                                                        {
                                                            Toast.makeText(RegisterUser.this, "Registered successfully! Please check your email for verification.", Toast.LENGTH_LONG).show();
                                                            editTextEmail.setText("");
                                                            editTextName.setText("");
                                                            editTextPassword.setText("");
                                                            Intent myIntent = new Intent(RegisterUser.this,
                                                                    Login.class);
                                                            startActivity(myIntent);
                                                        }
                                                        else
                                                        {
                                                            String msg = task.getException().toString();
                                                            Toast.makeText(RegisterUser.this, "Error " + msg, Toast.LENGTH_SHORT);
                                                        }
                                                    }
                                                });


                                            }
                                            else
                                            {
                                                Toast.makeText(RegisterUser.this, task.getException().getMessage(),Toast.LENGTH_LONG).show();
                                            }

                                        }
                                    });

                                }

                                else
                                {
                                    Toast.makeText(RegisterUser.this, "Failed to register. Try again.", Toast.LENGTH_SHORT).show();
                                    progressDialog.cancel();
                                }
                            }
                        }
                );


    }

    @Override
    public void onClick(View view)
    {
        if(view == buttonRegister)
            registerUser();

    }
}
