package com.example.waleed.chatappv2.adapters;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.waleed.chatappv2.ConversationActivity;
import com.example.waleed.chatappv2.R;
import com.example.waleed.chatappv2.convsUtils.FriendlyMessage;
import com.example.waleed.chatappv2.models.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.List;

public class MessageAdapter extends ArrayAdapter<FriendlyMessage> {
public FirebaseUser  firebaseUser=FirebaseAuth.getInstance().getCurrentUser();
public String UserName;
    int resourceLayoutId;

    public void SetCurrentUserName(String UserName){
        this.UserName=UserName;
    }

    public MessageAdapter(Context context, int resource, List<FriendlyMessage> objects) {
        super(context, resource, objects);


    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        FriendlyMessage message1 = getItem(position);
        if (message1.name.equals(UserName) && convertView==null){
            //Toast.makeText(getContext(), message.name+" **Match** "+UserName, Toast.LENGTH_SHORT).show();
             resourceLayoutId=R.layout.item_message_user;
        } else {resourceLayoutId=R.layout.item_message;
            //Toast.makeText(getContext(), message.name+" --NotMatch-- "+UserName, Toast.LENGTH_SHORT).show();
             }
        if (convertView == null) {
            convertView = ((Activity) getContext()).getLayoutInflater().inflate(resourceLayoutId, parent, false);
        }
        ImageView photoImageView = (ImageView) convertView.findViewById(R.id.photoImageView);
        TextView messageTextView = (TextView) convertView.findViewById(R.id.messageTextView);
        TextView authorTextView = (TextView) convertView.findViewById(R.id.nameTextView);

        FriendlyMessage message=getItem(position);
        boolean isPhoto = message.getPhotoUrl() != null;
        if (isPhoto) {
            messageTextView.setVisibility(View.GONE);
            photoImageView.setVisibility(View.VISIBLE);
            Glide.with(photoImageView.getContext())
                    .load(message.getPhotoUrl())
                    .into(photoImageView);
        } else {
            messageTextView.setVisibility(View.VISIBLE);
            photoImageView.setVisibility(View.GONE);
            messageTextView.setText(message.getText());
        }
        authorTextView.setText(message.getName());

        return convertView;
    }
}
