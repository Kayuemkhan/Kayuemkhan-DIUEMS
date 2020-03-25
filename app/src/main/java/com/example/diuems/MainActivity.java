package com.example.diuems;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private EditText signInEmailEditText, signInPasswordEditText;
    private TextView signUpTextView;
    private Button signInButton;
    ProgressBar progressBar;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();
        signInEmailEditText=findViewById(R.id.emailEditTextID);
        signInPasswordEditText=findViewById(R.id.passwordEditTextID);
        signUpTextView=findViewById(R.id.SignUPTextId);
        signInButton=findViewById(R.id.signInButtonId);
        progressBar=findViewById(R.id.progressBarID);
        signInButton.setOnClickListener(this);
        signUpTextView.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.signInButtonId:
                userLogin();
                break;
            case R.id.SignUPTextId:
                Intent intent=new Intent(this,SignUpActivity.class);
                startActivity(intent);

                break;
        }
    }

    private void userLogin() {
        String email = signInEmailEditText.getText().toString().trim();//trim for ignoring the spaces
        String password=signInPasswordEditText.getText().toString().trim();
        if(email.isEmpty()){
            signInEmailEditText.setError("Enter an email address");
            signInEmailEditText.requestFocus();// cursor will go to there
            return;
        }
        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            signInEmailEditText.setError("Enter a valid email address");
            signInEmailEditText.requestFocus();
            return;
        }
        if(password.isEmpty()){
            signInPasswordEditText.setError("Enter an password");
            signInPasswordEditText.requestFocus();
            return;
        }
        if(password.length()<6){
            signInPasswordEditText.setError("Minimum length of a password should be 6");
            signInPasswordEditText.requestFocus();
            return;
        }
        if(email.equals("admin")&& password.equals("adminpass")){
            Intent intent = new Intent(MainActivity.this,AddMenuActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        }
        progressBar.setVisibility(View.VISIBLE);
        mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    finish();
                    Intent intent = new Intent(MainActivity.this,HomeActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    progressBar.setVisibility(View.INVISIBLE);
                }
                else {
                    Toast.makeText(getApplicationContext(),"User is already Registered",Toast.LENGTH_SHORT).show();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getApplicationContext(),""+e.getMessage().toString(),Toast.LENGTH_SHORT).show();
            }
        });
    }
}
