package com.example.waleed.phonebook;

import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.waleed.phonebook.DBMS.PhoneBookDB;

public class AddActivity extends AppCompatActivity {
EditText edName;
EditText edPhoneNo;
Button btnAdd;
String name,phoneNo;
PhoneBookDB DBMS;

SQLiteDatabase db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);
        DBMS=new PhoneBookDB(this);

        edName=findViewById(R.id.ed_name);
        edPhoneNo=findViewById(R.id.ed_phone_no);
        btnAdd=findViewById(R.id.btn_add);


        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                name=edName.getText().toString();
                phoneNo=edPhoneNo.getText().toString();
                if (name.isEmpty())
                   edName.setError("Please Fill name First");
                 if (phoneNo.isEmpty())
                    edPhoneNo.setError("Please fill Phone First");
                else {
                   db=DBMS.getWritableDatabase();
                   DBMS.insertNewUser(db,name,phoneNo);
                   edName.setText("");
                   edPhoneNo.setText("");
                    Toast.makeText(AddActivity.this, "Data inserted successfully", Toast.LENGTH_SHORT).show();
               }
            }
        });

    }
}
