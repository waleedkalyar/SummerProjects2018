package com.example.waleed.chatappv3;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.waleed.chatappv3.adapters.ConversationAdapter;
import com.example.waleed.chatappv3.models.Message;
import com.example.waleed.chatappv3.models.User;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnPausedListener;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class ConversationActivity extends AppCompatActivity implements ConversationAdapter.MessageListItemClickListner {
    public static final int DEFAULT_MSG_LENGTH_LIMIT = 1000;
    private static final int RC_PHOTO_PICKER =  2;
    final int CONTEXT_MENU_DELETE_FOR_ME=22;
    final int CONTEXT_MENU_DELETE_FOR_EVERYONE=33;

    private int adapterPosition;

    private RecyclerView mMessageRecyclerView;
    private ConversationAdapter mMessageAdapter;
    private ProgressBar mProgressBar;
    private ImageButton mPhotoPickerButton;
    private EditText mMessageEditText;
    private Button mSendButton;

    User user;
    Message messageClicked;
    CircleImageView imageViewActionBar;
    List<Message> myMessages;

private ChildEventListener mChildEventListener;
private StorageReference mConversationPicsStorageRef;
private DatabaseReference mCurrentUserDatabaseRef;
private ValueEventListener mMessageValueSent;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conversation);
        Intent intent=getIntent();
         user= (User) intent.getSerializableExtra("ConversationUser");
        // This is for Action Bar
        setActionBar();
        //here register all Xmls...
        mProgressBar = (ProgressBar) findViewById(R.id.progressBar);
        mMessageRecyclerView = (RecyclerView) findViewById(R.id.messageListView);
        mPhotoPickerButton = (ImageButton) findViewById(R.id.photoPickerButton);
        mMessageEditText = (EditText) findViewById(R.id.messageEditText);
        mSendButton = (Button) findViewById(R.id.sendButton);

        myMessages=new ArrayList<>();
       // Message message=new Message("tt","yy","WTF","lol",null,"sent");
        //myMessages.add(message);
        LinearLayoutManager layoutManager=new LinearLayoutManager(this);
        layoutManager.setStackFromEnd(true);
        mMessageRecyclerView.setLayoutManager(layoutManager);
        mMessageRecyclerView.setHasFixedSize(true);
        //mMessageAdapter=new ConversationAdapter(myMessages,user);
        //mMessageRecyclerView.setAdapter(mMessageAdapter);


        final String  mCurrentUserUID=FirebaseAuth.getInstance().getCurrentUser().getUid().toString();
        mCurrentUserDatabaseRef= FirebaseDatabase.getInstance().getReference().child("conversations");
        mConversationPicsStorageRef= FirebaseStorage.getInstance().getReference().child("ConversationPictures");

        mProgressBar.setVisibility(ProgressBar.INVISIBLE);
        mPhotoPickerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                intent.putExtra(Intent.EXTRA_LOCAL_ONLY,true);
                startActivityForResult(Intent.createChooser(intent,"Complete action using"),RC_PHOTO_PICKER);
            }
        });

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

        mSendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String currentUserUid=FirebaseAuth.getInstance().getCurrentUser().getUid();
                Message mMessage=new Message(currentUserUid,user.getUserUID(),mMessageEditText.getText().toString(),java.text.DateFormat.getDateTimeInstance().format(new Date()),null,"sent");
                mCurrentUserDatabaseRef.push().setValue(mMessage);

                mMessageEditText.setText("");
            }
        });

        mChildEventListener=new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Message mMessage=dataSnapshot.getValue(Message.class);
              String  selectedUserUid=user.getUserUID();
              if ((mMessage.getSendBy().equals(mCurrentUserUID) && mMessage.getSendTo().equals(selectedUserUid))|| (mMessage.getSendBy().equals(selectedUserUid) && mMessage.getSendTo().equals(mCurrentUserUID))){
                myMessages.add(mMessage);}
                mMessageAdapter=new ConversationAdapter(myMessages,user,ConversationActivity.this);
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
        mCurrentUserDatabaseRef.addChildEventListener(mChildEventListener);





    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Uri selectedImageUri=data.getData();
        StorageReference photoRef=mConversationPicsStorageRef.child(selectedImageUri.getLastPathSegment());
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
                String currentUserUid=FirebaseAuth.getInstance().getCurrentUser().getUid();
                Message mMessage=new Message(currentUserUid,user.getUserUID(),null,java.text.DateFormat.getDateTimeInstance().format(new Date()),downloadUrl.toString(),"sent");
                mCurrentUserDatabaseRef.push().setValue(mMessage);
            }
        });
    }




    public void setActionBar(){
        ActionBar mActionBar = getSupportActionBar();
        mActionBar.setDisplayShowHomeEnabled(false);
        mActionBar.setDisplayShowTitleEnabled(false);
        LayoutInflater mInflater = LayoutInflater.from(this);
        View mCustomView=mInflater.inflate(R.layout.custom_action_bar,null);
        TextView    mNameActionBar=mCustomView.findViewById(R.id.title_text_Action_bar);
        imageViewActionBar=mCustomView.findViewById(R.id.circularimageView_action_bar);
        TextView   mSubStatusCon=mCustomView.findViewById(R.id.statusUser_Actionbar);
        mNameActionBar.setText(user.getUserName());
        mSubStatusCon.setText(user.getconnectionState());
        Picasso.get().load(Uri.parse(user.getProfileImageUrl())).into(imageViewActionBar);
        mActionBar.setDisplayUseLogoEnabled(true);
        mActionBar.setCustomView(mCustomView);
        mActionBar.setDisplayShowCustomEnabled(true);
    }

    @Override
    public void onMessageItemClick(Message messageClicked, int position) {
        // click action
        this.messageClicked=messageClicked;
        adapterPosition=position;
        registerForContextMenu(mMessageRecyclerView);
        openContextMenu(mMessageRecyclerView);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        menu.add(Menu.NONE,CONTEXT_MENU_DELETE_FOR_ME,Menu.NONE,"Delete for Me");
        if (messageClicked.getSendBy().equals(FirebaseAuth.getInstance().getCurrentUser().getUid().toString())){
        menu.add(Menu.NONE,CONTEXT_MENU_DELETE_FOR_EVERYONE,Menu.NONE,"Delete for Everyone");
        }


    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        final Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content) , "Message is deleted!",Snackbar.LENGTH_LONG).setAction("close", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });
        View view=snackbar.getView();
        TextView tvSnack=view.findViewById(android.support.design.R.id.snackbar_text);
        tvSnack.setTextColor(Color.YELLOW);
        snackbar.setDuration(5000);
        switch (item.getItemId()){
            case CONTEXT_MENU_DELETE_FOR_EVERYONE:
                DatabaseReference delRef=FirebaseDatabase.getInstance().getReference().child("conversations");
                delRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (DataSnapshot messageSnap :dataSnapshot.getChildren()){
                            Message message=messageSnap.getValue(Message.class);
                            FirebaseUser user=FirebaseAuth.getInstance().getCurrentUser();
                            if (message.getSendBy().equals(user.getUid().toString())){
                                if (message.getTimeStamp().equals(messageClicked.getTimeStamp())){
                                    messageSnap.getRef().removeValue();
                                }
                            }
                        }
                        myMessages.remove(adapterPosition);
                        mMessageAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
               // Toast.makeText(this, messageClicked.getTextMessage()+"\n Deleted for Everyone ", Toast.LENGTH_SHORT).show();
                snackbar.setText("Message is deleted for Everyone!");
                snackbar.show();
                break;
            case CONTEXT_MENU_DELETE_FOR_ME:
                myMessages.remove(adapterPosition);
                synchronized (myMessages){
                    myMessages.notify();
                    mMessageAdapter.notifyItemRangeChanged(adapterPosition,myMessages.size());
                }
                mMessageAdapter.notifyDataSetChanged();
                //Toast.makeText(this, messageClicked.getTextMessage()+"\n is Delete for you!", Toast.LENGTH_SHORT).show();
                snackbar.setText("Message is deleted for You!");
                snackbar.show();
                break;
        }
        return super.onContextItemSelected(item);
    }
}
