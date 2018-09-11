package com.example.waleed.friendlychat;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class SignupActivity extends AppCompatActivity {
    EditText edEmail,edPass;
    Button btnSignup;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        edEmail=(EditText)findViewById(R.id.sign_email);
        edPass=(EditText)findViewById(R.id.sign_pass);
        btnSignup=(Button)findViewById(R.id.btn_signup);

        mAuth=FirebaseAuth.getInstance();




        btnSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String  email=edEmail.getText().toString();
                String password=edPass.getText().toString();
                if (email.isEmpty()){
                    Toast.makeText(SignupActivity.this, "Email is empty"+email, Toast.LENGTH_SHORT).show();
                } else if (password.isEmpty()){
                    Toast.makeText(SignupActivity.this, "password is empty"+password, Toast.LENGTH_SHORT).show();
                }else
                {

                    FirebaseAuth.getInstance().createUserWithEmailAndPassword(email,password)
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    Log.d("TAG", "createUserWithEmail:onComplete:" + task.isSuccessful());

                                    // If sign in fails, display a message to the user. If sign in succeeds
                                    // the auth state listener will be notified and logic to handle the
                                    // signed in user can be handled in the listener.
                                    if (!task.isSuccessful()) {
                                        Toast.makeText(SignupActivity.this, R.string.auth_failed,
                                                Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                }}


        });


    }

    public void goToLogin(View view)
    {
        startActivity(new Intent(SignupActivity.this,AuthUIActivity.class));
    }
}
