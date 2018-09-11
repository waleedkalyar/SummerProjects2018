package com.example.waleed.chatappv2;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.waleed.chatappv2.models.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class RegisterActivity extends AppCompatActivity {
    private EditText inputEmail,inputPassword,inputName;
    Button btnSignup,btnLogin,btnResetPass;
    private ProgressBar progressBar;
    FirebaseAuth auth;
    private DatabaseReference mDatabaseReference;

    @Override
    protected void onResume() {
        super.onResume();
        progressBar.setVisibility(View.GONE);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        auth=FirebaseAuth.getInstance();
        mDatabaseReference= FirebaseDatabase.getInstance().getReference();

        btnSignup=(Button) findViewById(R.id.sign_up_button);
        btnLogin=(Button) findViewById(R.id.sign_in_button);
        btnResetPass=(Button) findViewById(R.id.btn_reset_password);
        inputEmail=(EditText) findViewById(R.id.input_email);
        inputPassword=(EditText) findViewById(R.id.input_password);
        inputName=(EditText) findViewById(R.id.input_name);
        progressBar=(ProgressBar) findViewById(R.id.progressBar);

        btnResetPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(RegisterActivity.this,ResetPasswordActivity.class));
            }
        });

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(RegisterActivity.this,LoginActivity.class));
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
                    auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(RegisterActivity.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                           // Toast.makeText(RegisterActivity.this, "Create user with email:on complete", Toast.LENGTH_SHORT).show();
                            progressBar.setVisibility(View.GONE);

                            if (!task.isSuccessful()) {
                                Toast.makeText(RegisterActivity.this, "Registration failed, Check your Network Connection!", Toast.LENGTH_SHORT).show();
                            } else {
                                onAuthSuccess(task.getResult().getUser());
                                //Toast.makeText(RegisterActivity.this, "we will make pass of login here", Toast.LENGTH_SHORT).show();
                                //finish();
                            }
                        }

                    });



                }
            }
        });



    }

    private void onAuthSuccess(FirebaseUser firebaseUser){
        String userName=usernameFromEmail(firebaseUser.getEmail());

        writeNewUser(firebaseUser.getUid(),inputName.getText().toString(),firebaseUser.getEmail());
       // Toast.makeText(this, "User Name also Added", Toast.LENGTH_SHORT).show();
        startActivity(new Intent(RegisterActivity.this,ConversationActivity.class));
        finish();

    }

    private String usernameFromEmail(String email) {
        if (email.contains("@")) {
            return email.split("@")[0];
        } else {
            return email;
        }
    }

    private void writeNewUser(String userId, String name, String email) {
        User user = new User(name, email);

        mDatabaseReference.child("users").child(userId).setValue(user);


    }
}
