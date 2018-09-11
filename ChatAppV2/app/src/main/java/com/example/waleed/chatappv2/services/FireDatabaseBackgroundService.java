package com.example.waleed.chatappv2.services;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.example.waleed.chatappv2.convsUtils.FriendlyMessage;
import com.example.waleed.chatappv2.models.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class FireDatabaseBackgroundService extends Service {
    private static FirebaseDatabase firebaseDatabase;
    private static DatabaseReference mMessageDatabaseReference;
    private static DatabaseReference mUserDatabaseReference;
    private static ChildEventListener mChildEventListener;

    private static String userName;
    private static FriendlyMessage friendlyMessage;
    private static String  senderEmail;
    private static String  senderText;
    private List<FriendlyMessage> friendlyMessages;



    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        firebaseDatabase=FirebaseDatabase.getInstance();
        mMessageDatabaseReference=firebaseDatabase.getReference().child("Messages");
        mUserDatabaseReference=firebaseDatabase.getReference().child("users");
        mMessageDatabaseReference.keepSynced(true);
        mUserDatabaseReference.keepSynced(true);


        friendlyMessages=new ArrayList<>();


        mChildEventListener=new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                friendlyMessage=dataSnapshot.getValue(FriendlyMessage.class);
                // friendlyMessages.add((FriendlyMessage) friendlyMessages);
                senderText=friendlyMessage.getText();
                senderEmail=friendlyMessage.getEmail();
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
        mMessageDatabaseReference.addChildEventListener(mChildEventListener);

        FirebaseUser user= FirebaseAuth.getInstance().getCurrentUser();

        mUserDatabaseReference.child("users").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                User myUser=dataSnapshot.getValue(User.class);
                String mUserEmail=myUser.getEmail();
                while (myUser.getEmail().equals(senderEmail)){
                    Toast.makeText(FireDatabaseBackgroundService.this, mUserEmail, Toast.LENGTH_SHORT).show();
                     myUser=dataSnapshot.getValue(User.class);
                userName=dataSnapshot.child("username").getValue().toString();
                }
                Toast.makeText(FireDatabaseBackgroundService.this, userName, Toast.LENGTH_SHORT).show();
                NotificationsUtils.getNewMesssageNotification(getApplicationContext(),userName,senderText);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }


}
