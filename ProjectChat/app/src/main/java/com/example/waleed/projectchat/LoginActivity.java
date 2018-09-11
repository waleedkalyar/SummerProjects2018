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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {
    EditText edtEmail,edtPassword;
    Button btnLogin,btnForgotPass,btnGoToRegister;
    ProgressBar progressBar;
    FirebaseAuth auth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //auth=FirebaseAuth.getInstance();
      //  if(auth.getCurrentUser()!=null){
          // startActivity(new Intent(LoginActivity.this,MessagesActivity.class));
       // }

        setContentView(R.layout.activity_login);
        edtEmail=(EditText) findViewById(R.id.email);
        edtPassword=(EditText) findViewById(R.id.password);
        btnLogin=(Button) findViewById(R.id.btn_login);
        btnForgotPass=(Button) findViewById(R.id.btn_reset_password);
        btnGoToRegister=(Button) findViewById(R.id.btn_signup);
        progressBar=(ProgressBar) findViewById(R.id.progressBar_login);
        // get firebase instance

        btnGoToRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this,SignupActivity.class));
            }
        });

        btnForgotPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this,ResetPasswordActivity.class));
            }
        });
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email=edtEmail.getText().toString();
                final String password=edtPassword.getText().toString();
                if(email.isEmpty()){
                    edtEmail.setError("Enter the E-mail first!");
                }
                else if(password.isEmpty()){
                    edtPassword.setError("Enter the Password first!");
                }
                else{
                    progressBar.setVisibility(View.VISIBLE);
                    // authenticate the user
                    auth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {

                            progressBar.setVisibility(View.GONE);
                            if(!task.isSuccessful()){
                                if(password.length()<6){
                                    edtPassword.setError(getString(R.string.minimum_password));
                                }else{
                                    Toast.makeText(LoginActivity.this, getString(R.string.auth_failed), Toast.LENGTH_SHORT).show();
                                }
                            }else{
                                startActivity(new Intent(LoginActivity.this,MessagesActivity.class));
                                // Toast.makeText(LoginActivity.this, "Intent for main is here", Toast.LENGTH_SHORT).show();

                            }
                        }
                    });
                }
            }
        });

    }
}
