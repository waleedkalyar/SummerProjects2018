package com.example.waleed.friendschat;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.waleed.friendschat.adapters.ConversationAdapter;
import com.firebase.ui.auth.AuthUI;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class ConversationActivity extends AppCompatActivity {
    private static final int RC_SIGN_IN =1 ;
    private RecyclerView conversationRecyclerView;
    private ConversationAdapter mAdapter;
    private EditText mMessageEditText;
    private ImageButton mUploadImageButton;
    private Button mSendMessageButton;
    private ProgressBar mLoadingIndicator;

    private String mUserName;
    private String Message;

    private FirebaseAuth.AuthStateListener mAuthStateListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conversation);
        mUserName="Anonymous";
        conversationRecyclerView = findViewById(R.id.conversation_recycler_view);
        mUploadImageButton = findViewById(R.id.conversation_photo_picker);
        mMessageEditText = findViewById(R.id.EditText_conversation_message);
        mSendMessageButton = findViewById(R.id.btn_send_message);
        mLoadingIndicator = findViewById(R.id.Conversation_Loading_indicator);
        Message=mMessageEditText.getText().toString();
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        conversationRecyclerView.setLayoutManager(layoutManager);
        conversationRecyclerView.setHasFixedSize(true);
        mAdapter = new ConversationAdapter(mUserName,Message);
        conversationRecyclerView.setAdapter(mAdapter);

        mAuthStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    onSigninInitialize(user.getDisplayName());
                } else {
                    onSignoutCleanup();

                    startActivityForResult(AuthUI.getInstance()
                    .createSignInIntentBuilder()
                    .setIsSmartLockEnabled(false)
                    .setProviders(
                            AuthUI.EMAIL_PROVIDER,
                            AuthUI.GOOGLE_PROVIDER)
                            .setLogo(R.drawable.tw__composer_logo_blue)
                            .build(),
                            RC_SIGN_IN);
                }
            }
        };

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==RC_SIGN_IN){
            if (requestCode==RESULT_OK){
                Toast.makeText(this, "Sign In", Toast.LENGTH_SHORT).show();
            } else if (requestCode==RESULT_CANCELED){
                Toast.makeText(this, "Sign In Cancel", Toast.LENGTH_SHORT).show();
                finish();
            }
        }
    }

    private void onSigninInitialize(String displayName) {
        mUserName = displayName;
        //attachDatabaseReadListener();

    }

    private void onSignoutCleanup() {
        mUserName = "Anonymous";
        // mAdapter.clear();
        // detachDatabaseReadLitnener();
    }
}
