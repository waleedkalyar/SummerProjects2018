package com.example.waleed.chatappv2;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.text.format.DateFormat;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.waleed.chatappv2.adapters.MessageAdapter;
import com.example.waleed.chatappv2.adapters.RVMessageAdapter;
import com.example.waleed.chatappv2.convsUtils.FriendlyMessage;
import com.example.waleed.chatappv2.models.User;
import com.example.waleed.chatappv2.services.FireDatabaseBackgroundService;
import com.example.waleed.chatappv2.services.FirebaseDatabaseIntentService;
import com.example.waleed.chatappv2.services.NotificationsUtils;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreSettings;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnPausedListener;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ConversationActivity extends AppCompatActivity {
    FirebaseUser firebaseUser;

    private static final String TAG = "ConversationActivity";

    public static final String ANONYMOUS = "anonymous";
    public static final int DEFAULT_MSG_LENGTH_LIMIT = 1000;
    private static final int RC_PHOTO_PICKER =  2;
    private static boolean connectionState;
    private static boolean sendMessageNodeState;

    private RecyclerView mMessageRecyclerView;
    private RVMessageAdapter mMessageAdapter;
    private ProgressBar mProgressBar;
    private ImageButton mPhotoPickerButton;
    private EditText mMessageEditText;
    private Button mSendButton;

    private String mUsername;
    private FirebaseAuth mFirebaseAuth;
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mMessageDatabaseReference;
    private DatabaseReference mUserDatabaseReference;
    private ChildEventListener mChidEventLisetner;
    private FirebaseStorage mFirebaseStorage;
    private StorageReference mChatPhotoStorageReference;
   public User user1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conversation);
        // Data presistance code must first
        try {
            FirebaseDatabase.getInstance().setPersistenceEnabled(true);
        } catch (Exception e){
          // Toast.makeText(this, e.getMessage()+" Must Be Start", Toast.LENGTH_SHORT).show();
        }



        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setIcon(R.mipmap.chat_logo);

        firebaseUser=FirebaseAuth.getInstance().getCurrentUser();
        mFirebaseAuth=FirebaseAuth.getInstance();
        mFirebaseDatabase=FirebaseDatabase.getInstance();
        mFirebaseStorage=FirebaseStorage.getInstance();

        mMessageDatabaseReference=mFirebaseDatabase.getReference().child("Messages");
        mChatPhotoStorageReference=mFirebaseStorage.getReference().child("chat_photos");
        mUserDatabaseReference=FirebaseDatabase.getInstance().getReference();
        mUserDatabaseReference.keepSynced(true);


       // mUsername = ANONYMOUS;
        mUsername=firebaseUser.getEmail();

        // Initialize references to views
        mProgressBar = (ProgressBar) findViewById(R.id.progressBar);
        mMessageRecyclerView = (RecyclerView) findViewById(R.id.messageListView);
        mPhotoPickerButton = (ImageButton) findViewById(R.id.photoPickerButton);
        mMessageEditText = (EditText) findViewById(R.id.messageEditText);
        mSendButton = (Button) findViewById(R.id.sendButton);
firebaseUser=mFirebaseAuth.getCurrentUser();
        // Initialize message ListView and its adapter
        final List<FriendlyMessage> friendlyMessages = new ArrayList<>();
//Add and set LayoutManager for RecyclerView
 LinearLayoutManager layoutManager=new LinearLayoutManager(this);
 layoutManager.setStackFromEnd(true);
 mMessageRecyclerView.setLayoutManager(layoutManager);
 mMessageRecyclerView.setHasFixedSize(true);


        // Initialize progress bar
        mProgressBar.setVisibility(ProgressBar.INVISIBLE);

        // ImagePickerButton shows an image picker to upload a image for a message
        mPhotoPickerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO: Fire an intent to show an image picker
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                intent.putExtra(Intent.EXTRA_LOCAL_ONLY,true);
                startActivityForResult(Intent.createChooser(intent,"Complete action using"),RC_PHOTO_PICKER);
            }
        });

        // Enable Send button when there's text to send
        mMessageEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.toString().trim().length() > 0) {
                    mSendButton.setEnabled(true);
                } else {
                    mSendButton.setEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });
        mMessageEditText.setFilters(new InputFilter[]{new InputFilter.LengthFilter(DEFAULT_MSG_LENGTH_LIMIT)});

        // Add Listener for userName:
        FirebaseUser user=mFirebaseAuth.getCurrentUser();
        mUserDatabaseReference.child("users").child(user.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
             mUsername = dataSnapshot.child("username").getValue().toString();
             String userEmail=dataSnapshot.child("email").getValue().toString();
                Toast.makeText(ConversationActivity.this,"Welcome Mr."+ mUsername+" to ChatApp", Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        // Send button sends a message and clears the EditText
        mSendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO: Send messages on click
                FirebaseUser user=mFirebaseAuth.getCurrentUser();
               // Toast.makeText(ConversationActivity.this, user.getUid(), Toast.LENGTH_SHORT).show();

                final FriendlyMessage friendlyMessage =new FriendlyMessage(mMessageEditText.getText().toString(),mUsername,null,java.text.DateFormat.getDateTimeInstance().format(new Date()),checkConnectionState());
                mMessageDatabaseReference.push().setValue(friendlyMessage);

                // Clear input box
                mMessageEditText.setText("");
            }
        });
        // here i  check the connection of server
       checkConnectionState();



        mChidEventLisetner = new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                FriendlyMessage friendlyMessage=   dataSnapshot.getValue(FriendlyMessage.class);
                friendlyMessages.add(friendlyMessage);

                mMessageAdapter=new RVMessageAdapter(friendlyMessages,mUsername);
                mMessageRecyclerView.setAdapter(mMessageAdapter);

            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        };
        mMessageDatabaseReference.addChildEventListener(mChidEventLisetner);




    }

    @Override
    protected void onDestroy() {
        // here is the code for background Services
        Intent serviceIntent=new Intent(this, FireDatabaseBackgroundService.class);
        //serviceIntent.putExtra("User",mUsername);
        startService(serviceIntent);
        super.onDestroy();
    }


    // code for image load
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
      //  if (requestCode==RC_PHOTO_PICKER && requestCode==RESULT_OK){
            Uri selectedImageUri=data.getData();
            StorageReference photoRef=mChatPhotoStorageReference.child(selectedImageUri.getLastPathSegment());
            photoRef.putFile(selectedImageUri).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                    mProgressBar.setVisibility(View.VISIBLE);
                    double progress=100.0*(taskSnapshot.getBytesTransferred()/taskSnapshot.getTotalByteCount());
                    System.out.println("upload is "+progress+" %done");
                    int currentProgress=(int)progress;
                    mProgressBar.setProgress(currentProgress);
                    if (progress==100.0){
                        mProgressBar.setVisibility(View.INVISIBLE);
                    }

                }
            }).addOnPausedListener(new OnPausedListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onPaused(UploadTask.TaskSnapshot taskSnapshot) {
                    System.out.println("Upload is paused");
                }
            }).addOnSuccessListener(this, new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Uri downloadUrl=taskSnapshot.getDownloadUrl();
                    FriendlyMessage friendlyMessage=new FriendlyMessage(null,mUsername,downloadUrl.toString(),java.text.DateFormat.getDateTimeInstance().format(new Date()),checkConnectionState());
                    mMessageDatabaseReference.push().setValue(friendlyMessage);
                }
            });

       // }else {
           // Toast.makeText(this, "The condition is not working properly", Toast.LENGTH_SHORT).show();
        //}
    }



    // server conection
    public boolean checkConnectionState(){
        DatabaseReference connectedRef = FirebaseDatabase.getInstance().getReference(".info/connected");
        connectedRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                boolean connected = snapshot.getValue(Boolean.class);
                connectionState=connected;
                if (connectionState) {
                    System.out.println("connected");

                   // Toast.makeText(ConversationActivity.this, "**Connected**", Toast.LENGTH_SHORT).show();
                } else {
                    System.out.println("not connected");
                   // Toast.makeText(ConversationActivity.this, "--Not Connected--", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                System.err.println("Listener was cancelled");
            }
        });

        return connectionState;
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId=item.getItemId();
        TextView connectionText=(TextView) findViewById(R.id.connection);
        connectionText.setTextColor(Color.YELLOW);
        if (checkConnectionState()){
            connectionText.setText("Connected");
            connectionText.setTextColor(Color.GREEN);
            connectionText.setTypeface(connectionText.getTypeface(), Typeface.BOLD);
        }
        else {
            connectionText.setText("Not Connected");
            connectionText.setTextColor(Color.RED);
            connectionText.setTypeface(connectionText.getTypeface(), Typeface.BOLD);
        }
        switch (itemId){
            case R.id.logout:
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(ConversationActivity.this,LoginActivity.class));
                finish();
            case R.id.connection:

                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
