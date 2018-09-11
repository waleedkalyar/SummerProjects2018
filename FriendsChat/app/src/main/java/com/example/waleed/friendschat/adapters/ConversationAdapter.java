package com.example.waleed.friendschat.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.waleed.friendschat.R;

public class ConversationAdapter extends RecyclerView.Adapter<ConversationAdapter.ConversationViewHolder>{
private String message;
private String name;
    public ConversationAdapter(String name, String message){
    this.name=name;
    this.message=message;
    }

    @Override
    public ConversationViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        Context context=parent.getContext();
        int layoutIdForListItem=R.layout.item_message;
        LayoutInflater layoutInflater=LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately=false;
        View view=layoutInflater.inflate(layoutIdForListItem,parent,shouldAttachToParentImmediately);
        return new ConversationViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ConversationViewHolder holder, int position) {
    holder.mMessageTextView.setText(message);
    holder.mNameTextView.setText(name);
    }

    @Override
    public int getItemCount() {
        return 0;
    }


    public class ConversationViewHolder extends RecyclerView.ViewHolder{
        public  ImageView mConversationImage;
        public TextView mMessageTextView;
        public TextView mNameTextView;
        public ConversationViewHolder(View itemView) {
            super(itemView);
            mConversationImage=(ImageView) itemView.findViewById(R.id.Conversation_photo_image_view);
            mMessageTextView=(TextView) itemView.findViewById(R.id.Conversation_messageTextView);
            mNameTextView=(TextView) itemView.findViewById(R.id.Conversation_nameTextView);
        }
    }

    public void setConversationData( String message, String name){
        this.message=message;
        this.name=name;
    }
}
