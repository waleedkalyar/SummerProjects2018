package com.example.waleed.projectchat;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class SignupActivity extends AppCompatActivity {
    private EditText inputEmail,inputPassword;
    Button btnSignup,btnLogin,btnResetPass;
    private ProgressBar progressBar;
    FirebaseAuth auth;

    @Override
    protected void onResume() {
        super.onResume();
        progressBar.setVisibility(View.GONE);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        auth=FirebaseAuth.getInstance();

        btnSignup=(Button) findViewById(R.id.sign_up_button);
        btnLogin=(Button) findViewById(R.id.sign_in_button);
        btnResetPass=(Button) findViewById(R.id.btn_reset_password);
        inputEmail=(EditText) findViewById(R.id.input_email);
        inputPassword=(EditText) findViewById(R.id.input_password);
        progressBar=(ProgressBar) findViewById(R.id.progressBar);


        btnResetPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(SignupActivity.this,ResetPasswordActivity.class));
            }
        });

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(SignupActivity.this,LoginActivity.class));
                finish();
            }
        });



        btnSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email=inputEmail.getText().toString();
                String password=inputPassword.getText().toString();
                if(email.isEmpty()){
                    inputEmail.setError("Please Enter Email");
                }
                else if(password.isEmpty()){
                    inputPassword.setError("Please Enter Password");
                }
                else if(password.length()<6){
                    inputPassword.setError("Password to short, enter minimum 6 characters");
                }
                else {
                    progressBar.setVisibility(View.VISIBLE);
                    // create user
                    auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(SignupActivity.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            Toast.makeText(SignupActivity.this, "Create user with email:on complete", Toast.LENGTH_SHORT).show();
                            progressBar.setVisibility(View.GONE);

                            if (!task.isSuccessful()) {
                                Toast.makeText(SignupActivity.this, "Authentication failed", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(SignupActivity.this, "we will make pass of login here", Toast.LENGTH_SHORT).show();
                                //finish();
                            }
                        }

                    });
                }
            }
        });

    }
}
