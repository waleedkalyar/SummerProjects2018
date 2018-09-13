package com.example.waleed.chatappv3;

import android.app.ActivityOptions;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Parcelable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.transition.Explode;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.TextView;
import android.widget.Toast;

import com.example.waleed.chatappv3.adapters.UserProfilesMenuAdapter;
import com.example.waleed.chatappv3.authentication.LoginActivity;
import com.example.waleed.chatappv3.models.User;
import com.example.waleed.chatappv3.services.FireaseChatBackgroundService;
import com.firebase.client.Firebase;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements UserProfilesMenuAdapter.ListItemClickListner {
    static {
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
    }
   private RecyclerView usersProfilesRecView;
   private List<User> userList;
   private FirebaseDatabase mFirebaseDatabase;
   private DatabaseReference mUsersDatabaseReference;
   private UserProfilesMenuAdapter menuAdapter;
   private ChildEventListener mChildEventListner;
            ValueEventListener mConnectionListner;

  //private  Firebase mFirebaseChatRef;
            DatabaseReference myConnectionStateRef;
  // private Firebase mChatAppUserRef;

  private String mCurrentUserUID;

    @Override
    protected void onStart() {

        super.onStart();

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.LOLLIPOP){
            getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);
            getWindow().setExitTransition(new Explode());

        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mFirebaseDatabase=FirebaseDatabase.getInstance();







        usersProfilesRecView = (RecyclerView) findViewById(R.id.usersFireChatRecyclerView);
        mFirebaseDatabase.getReference().keepSynced(true);
        mUsersDatabaseReference=mFirebaseDatabase.getReference().child("Users");
        mCurrentUserUID=FirebaseAuth.getInstance().getCurrentUser().getUid().toString();
        userList = new ArrayList<>();
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        usersProfilesRecView.setLayoutManager(layoutManager);
        usersProfilesRecView.setHasFixedSize(true);
        menuAdapter = new UserProfilesMenuAdapter(userList,MainActivity.this);
        usersProfilesRecView.setAdapter(menuAdapter);


        myConnectionStateRef=mUsersDatabaseReference.child(mCurrentUserUID).child("connectionState");
        mConnectionListner=mUsersDatabaseReference.getRoot().child(".info/connected").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                boolean connected=(Boolean)dataSnapshot.getValue();
                if (connected){
                    myConnectionStateRef.setValue("online");

                    myConnectionStateRef.onDisconnect().setValue("offline");

                    // snackbar code
                    final Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content), "You are Connected!",Snackbar.LENGTH_LONG).setAction("close", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                        }
                    });
                    View view=snackbar.getView();
                    TextView tvSnack=view.findViewById(android.support.design.R.id.snackbar_text);
                    tvSnack.setTextColor(Color.GREEN);
                    snackbar.setDuration(10000);
                    snackbar.show();
                }
                else {
                    final Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content) , "You are Disconnected!",Snackbar.LENGTH_LONG).setAction("close", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                        }
                    });
                    View view=snackbar.getView();
                    TextView tvSnack=view.findViewById(android.support.design.R.id.snackbar_text);
                    tvSnack.setTextColor(Color.RED);
                    snackbar.setDuration(10000);
                    snackbar.show();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        mChildEventListner=new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                if (dataSnapshot.exists()){
                    String userUid=dataSnapshot.getKey();

                    if (!userUid.equals(mCurrentUserUID)){
                        User user=dataSnapshot.getValue(User.class);
                        userList.add(user);
                        menuAdapter = new UserProfilesMenuAdapter(userList,MainActivity.this);
                        usersProfilesRecView.setAdapter(menuAdapter);

                    }
                }





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
        mUsersDatabaseReference.addChildEventListener(mChildEventListner);


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        switch (itemId) {
            case R.id.logout:
                myConnectionStateRef.setValue("offline");
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(MainActivity.this, LoginActivity.class));
                finish();
                break;


        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onListItemClick(User clickedUser) {
        Intent intent=new Intent(MainActivity.this,ConversationActivity.class);
        intent.putExtra("ConversationUser", clickedUser);
       // Toast.makeText(this, clickedUser.getUserName()+" is clicked", Toast.LENGTH_SHORT).show();
        if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.LOLLIPOP){
        startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(this).toBundle());}
        else {startActivity(intent);}
    }
}
