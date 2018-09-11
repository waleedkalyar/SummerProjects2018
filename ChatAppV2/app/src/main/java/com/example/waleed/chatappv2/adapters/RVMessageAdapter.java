package com.example.waleed.chatappv2.adapters;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.waleed.chatappv2.ConversationActivity;
import com.example.waleed.chatappv2.R;
import com.example.waleed.chatappv2.convsUtils.FriendlyMessage;

import java.util.List;

public class RVMessageAdapter extends RecyclerView.Adapter<RVMessageAdapter.MessageViewHolder> {
private List<FriendlyMessage> objects;
private Context context;
    private String UserName;
    private static boolean sendMessageNodeState;
private     int LayoutIdForListItem;
    public RVMessageAdapter( List<FriendlyMessage> objects, String UserName) {
        this.objects=objects;
        this.context=context;
        this.UserName=UserName;
    }

    @Override
    public int getItemViewType(int position) {
        FriendlyMessage message=objects.get(position);
        if (message.name.equals(UserName)){
         return    LayoutIdForListItem=R.layout.item_message_user;
        } else if (!message.name.equals(UserName)){
          return   LayoutIdForListItem=R.layout.item_message;
        }
        return LayoutIdForListItem=R.layout.item_message;
    }

    @Override
    public MessageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context=parent.getContext();
       // int LayoutIdForListItem=R.layout.item_message;
        LayoutInflater inflater=LayoutInflater.from(context);
        boolean shouldAttachToParentImediately=false;

        View view=inflater.inflate(LayoutIdForListItem,parent,shouldAttachToParentImediately);
        MessageViewHolder messageViewHolder=new MessageViewHolder(view);
        return messageViewHolder;
    }

    @Override
    public void onBindViewHolder(MessageViewHolder holder, int position) {
  FriendlyMessage   friendlyMessage=this.objects.get(position);

    holder.bindMessages(friendlyMessage);
    }

    @Override
    public int getItemCount() {
        return objects.size();
    }


    public class MessageViewHolder extends RecyclerView.ViewHolder{
        ImageView photoImageView;
        TextView messageTextView;
        TextView authorTextView;
        TextView timeInstanceMessage;
        public MessageViewHolder(View itemView) {
            super(itemView);

             photoImageView = (ImageView) itemView.findViewById(R.id.photoImageView);
             messageTextView = (TextView) itemView.findViewById(R.id.messageTextView);
             authorTextView = (TextView) itemView.findViewById(R.id.nameTextView);
             timeInstanceMessage=(TextView) itemView.findViewById(R.id.send_time);

        }

        void bindMessages(FriendlyMessage message){
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
            timeInstanceMessage.setText(message.getTimeStamp());



        }
    }

    public static void getMessageSendState(boolean sendMessageNodeState){
        RVMessageAdapter.sendMessageNodeState =sendMessageNodeState;
    }
}
