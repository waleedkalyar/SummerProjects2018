package com.example.waleed.phonebook;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import com.example.waleed.phonebook.Adapters.PhoneBookAdapter;
import com.example.waleed.phonebook.DBMS.PhoneBookDB;
import com.example.waleed.phonebook.Models.PhoneBookModel;

public class MainActivity extends AppCompatActivity {
ListView phoneBookLV;
FloatingActionButton fabAdd;
PhoneBookModel[] phoneBookModel;
PhoneBookAdapter phoneBookAdapter;
PhoneBookDB DBMS;
SQLiteDatabase db;
    @Override
    protected void onStart() {
        super.onStart();
        db=DBMS.getReadableDatabase();
        Cursor incommingCursor=DBMS.getAllUsers(db);
        if(incommingCursor==null){
            Toast.makeText(MainActivity.this, "No Data In db", Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(MainActivity.this, "Data Is In db", Toast.LENGTH_SHORT).show();
        }
        if (incommingCursor.moveToFirst())
            do{
            String UserName=incommingCursor.getString(0);
            String UserPhone=incommingCursor.getString(1);
            phoneBookModel=new PhoneBookModel[]{new PhoneBookModel(UserName,UserPhone)};
            }
            while (incommingCursor.moveToNext());
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        DBMS=new PhoneBookDB(this);
        fabAdd=findViewById(R.id.fab_add);
        phoneBookLV=findViewById(R.id.lv_phonebook);
        phoneBookModel=new PhoneBookModel[]{
                new PhoneBookModel("Waleed","0302675356"),
                new PhoneBookModel("Arslan","03026753321"),
                new PhoneBookModel("khawar","03026753167"),
                new PhoneBookModel("Usman","03026753788"),
        };

        phoneBookAdapter=new PhoneBookAdapter(MainActivity.this,R.layout.book_item,phoneBookModel);
        phoneBookLV.setAdapter(phoneBookAdapter);

        fabAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            startActivity(new Intent(MainActivity.this,AddActivity.class));
            }
        });


    }


}
