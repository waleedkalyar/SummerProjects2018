package com.example.waleed.chatappv3.authentication;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.waleed.chatappv3.MainActivity;
import com.example.waleed.chatappv3.R;
import com.example.waleed.chatappv3.models.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import de.hdodenhof.circleimageview.CircleImageView;

public class RegisterActivity extends AppCompatActivity {
    private EditText inputEmail,inputPassword,inputName;
    Button btnSignup,btnLogin,btnResetPass;
    CircleImageView profileImageView;
    private ProgressBar progressBar;
    FirebaseAuth auth;
    private DatabaseReference mDatabaseReference;
    private FirebaseStorage mFirebaseStorage;
    private StorageReference mProfilePicStorageReference;
    private static final int RC_PHOTO_PICKER=1;
    private Uri selectedProfileImageUri;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        auth=FirebaseAuth.getInstance();
        mDatabaseReference= FirebaseDatabase.getInstance().getReference().child("Users");
        mProfilePicStorageReference=FirebaseStorage.getInstance().getReference().child("ProfilePictures");

        btnSignup=(Button) findViewById(R.id.sign_up_button);
        btnLogin=(Button) findViewById(R.id.sign_in_button);
        btnResetPass=(Button) findViewById(R.id.btn_reset_password);
        inputEmail=(EditText) findViewById(R.id.input_email);
        inputPassword=(EditText) findViewById(R.id.input_password);
        inputName=(EditText) findViewById(R.id.input_name);
        profileImageView=(CircleImageView)findViewById(R.id.user_profile_picture);
        progressBar=(ProgressBar) findViewById(R.id.progress_bar_register);

        btnResetPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(RegisterActivity.this,PasswordResetActivity.class));
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

    private void onAuthSuccess(final FirebaseUser firebaseUser){
        String userName=usernameFromEmail(firebaseUser.getEmail());
        StorageReference profilePicRef=mProfilePicStorageReference.child(selectedProfileImageUri.getLastPathSegment());
        profilePicRef.putFile(selectedProfileImageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Uri downloadUri=taskSnapshot.getDownloadUrl();
                writeNewUser(firebaseUser.getUid(),inputName.getText().toString(),firebaseUser.getEmail(),downloadUri.toString());
            }
        });
        // Toast.makeText(this, "User Name also Added", Toast.LENGTH_SHORT).show();
        startActivity(new Intent(RegisterActivity.this,MainActivity.class));
        finish();

    }

    private String usernameFromEmail(String email) {
        if (email.contains("@")) {
            return email.split("@")[0];
        } else {
            return email;
        }
    }

    private void writeNewUser(String userId, String name, String email, String profilePicUrl) {
        User user = new User(FirebaseAuth.getInstance().getCurrentUser().getUid(),name, profilePicUrl,email,null,null);

        mDatabaseReference.child(userId).setValue(user);


    }

    public void getProfilePicture(View view) {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        intent.putExtra(Intent.EXTRA_LOCAL_ONLY,true);
        startActivityForResult(Intent.createChooser(intent,"Complete action using"),RC_PHOTO_PICKER);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode== Activity.RESULT_OK) {
            Uri selectedImageUri = data.getData();
            selectedProfileImageUri = selectedImageUri;
            profileImageView.setImageURI(selectedImageUri);
        }

    }
}
