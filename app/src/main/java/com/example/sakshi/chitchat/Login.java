package com.example.sakshi.chitchat;

import android.app.ProgressDialog;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.EditText;
import android.widget.ImageView;
import android.content.Intent;
import android.widget.Button;
import android.view.View;
import android.content.Intent;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;


import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import org.w3c.dom.Text;

public class Login extends AppCompatActivity implements View.OnClickListener{


    private Button buttonLogin;
    private ProgressBar progressBar;
    private TextView Signup;
    private FirebaseAuth firebaseAuth;
    private EditText uid,psd;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Signup = (TextView) findViewById(R.id.signup);
        Signup.setOnClickListener(this);

        uid = (EditText)findViewById(R.id.userId) ;
        psd = (EditText)findViewById(R.id.paswd) ;

        progressBar = new ProgressBar(this);


        buttonLogin = (Button)findViewById(R.id.buttonLogin);
        buttonLogin.setOnClickListener(this);

        firebaseAuth = FirebaseAuth.getInstance();
    }

    private void loginUser()
    {
        progressBar.setVisibility(View.VISIBLE);

        if(TextUtils.isEmpty(uid.getText()))
        {
            Toast.makeText(this,"Please enter email", Toast.LENGTH_SHORT).show();
            return;
        }

        if(TextUtils.isEmpty(psd.getText()))
        {
            Toast.makeText(this,"Please enter password", Toast.LENGTH_SHORT).show();
            return;
        }


        firebaseAuth.signInWithEmailAndPassword(uid.getText().toString() , psd.getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>()
        {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task)
            {
                progressBar.setVisibility(View.GONE);

                if(task.isSuccessful())
                {
                    Intent myIntent = new Intent(Login.this,
                            Chats.class);

                    myIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

                    startActivity(myIntent);
                    finish();
                }
                else
                    Toast.makeText(Login.this, "Login failed. Try again!", Toast.LENGTH_SHORT).show();
                    uid.setText("");
                    psd.setText("");
            }
        });
    }

    public void onClick(View view)
    {
        if(view == Signup) {
            Intent myIntent = new Intent(Login.this,
                    RegisterUser.class);

            startActivity(myIntent);

        }

        if(view == buttonLogin)
        {
           loginUser();
        }
    }
}
