package com.example.waleed.chatappv3;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.example.waleed.chatappv3.adapters.UserProfilesMenuAdapter;
import com.example.waleed.chatappv3.models.User;

import java.util.List;

public class MainActivity extends AppCompatActivity {
RecyclerView usersProfilesRecView;
List<User> userList;
UserProfilesMenuAdapter menuAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        usersProfilesRecView=(RecyclerView)findViewById(R.id.usersFireChatRecyclerView);
        User user =new User("Waleed",null,"Wali@hotmail.com","online");
        User user1 =new User("Abrahum",null,"abra@hotmail.com","offline");
        userList.add(user);
        userList.add(user1);
        menuAdapter=new UserProfilesMenuAdapter(userList);
        LinearLayoutManager layoutManager=new LinearLayoutManager(this);
        layoutManager.setStackFromEnd(true);
        usersProfilesRecView.setLayoutManager(layoutManager);
        usersProfilesRecView.setHasFixedSize(true);
        usersProfilesRecView.setAdapter(menuAdapter);

    }
}
