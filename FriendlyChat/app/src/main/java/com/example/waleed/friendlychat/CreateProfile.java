package com.example.waleed.friendlychat;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.google.firebase.database.FirebaseDatabase;

import java.io.IOException;

public class CreateProfile extends AppCompatActivity {
ImageView imgProf;
EditText edName;
Button btnGetImg,btnUploadData;

private int PICK_IMG_REQUEST=1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_profile);

        imgProf=(ImageView) findViewById(R.id.img_upload);
        edName=(EditText) findViewById(R.id.ed_name);
        btnGetImg=(Button) findViewById(R.id.btn_get_img);
        btnUploadData=(Button)findViewById(R.id.btn_upload_data);

        btnGetImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent,"Select Picture"),PICK_IMG_REQUEST);
            }
        });
       btnUploadData.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               String userName=edName.getText().toString();
               if (userName.isEmpty()){
                   edName.setError("Please fill Name Field!");
               }
               else {
                   // here will code to upload data
                   FirebaseDatabase database=FirebaseDatabase.getInstance();
               }
           }
       });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode==PICK_IMG_REQUEST && resultCode==RESULT_OK && data!=null && data.getData()!=null){
            Uri uri=data.getData();
        try {
            Bitmap bitmap= MediaStore.Images.Media.getBitmap(getContentResolver(),uri);
            imgProf.setImageBitmap(bitmap);
        }
        catch (Exception e){
            e.printStackTrace();
        }
        }
    }
}
