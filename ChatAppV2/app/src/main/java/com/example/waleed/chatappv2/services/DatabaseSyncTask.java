package com.example.waleed.chatappv2.services;


import android.content.Context;
import android.support.annotation.NonNull;
import android.widget.Toast;

import com.example.waleed.chatappv2.convsUtils.FriendlyMessage;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class DatabaseSyncTask {
private static FirebaseDatabase firebaseDatabase;
private static DatabaseReference mMessageDatabaseReference;
private static DatabaseReference mUserDatabaseReference;
private static ChildEventListener mChildEventListener;

private static String userName;
private static FriendlyMessage friendlyMessage;
private static String  senderEmail;


 synchronized  public static void syncMessages(Context context){
     try {


        firebaseDatabase=FirebaseDatabase.getInstance();
        mMessageDatabaseReference=firebaseDatabase.getReference().child("Messages");
        mUserDatabaseReference=firebaseDatabase.getReference().child("users");
        mMessageDatabaseReference.keepSynced(true);
        mUserDatabaseReference.keepSynced(true);
     } catch (Exception e){e.printStackTrace();}
        
    }

  synchronized   public static FriendlyMessage getSyncMessage(final Context context){


     mChildEventListener=new ChildEventListener() {
         @Override
         public void onChildAdded(DataSnapshot dataSnapshot, String s) {
           friendlyMessage=dataSnapshot.getValue(FriendlyMessage.class);
         senderEmail=friendlyMessage.getEmail();
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
     mMessageDatabaseReference.addChildEventListener(mChildEventListener);

     return friendlyMessage;
    }

    public static String getUserName(final Context context) {
     friendlyMessage=getSyncMessage(context);
        final FirebaseUser user= FirebaseAuth.getInstance().getCurrentUser();
//     String senderEmail=friendlyMessage.getEmail();
        if (senderEmail==null){
            senderEmail=user.getUid();
        }

        firebaseDatabase.getReference().child("users").child(senderEmail).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                userName = dataSnapshot.child("username").getValue().toString();
                Toast.makeText(context, userName, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        return userName;
    }
}
