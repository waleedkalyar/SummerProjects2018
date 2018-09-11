package com.example.waleed.chatappv3.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.waleed.chatappv3.R;
import com.example.waleed.chatappv3.models.Message;
import com.example.waleed.chatappv3.models.User;
import com.example.waleed.chatappv3.utils.CreateAnimationView;
import com.google.firebase.auth.FirebaseAuth;

import java.util.List;

public class ConversationAdapter extends RecyclerView.Adapter<ConversationAdapter.MessageViewHolder> {
private final MessageListItemClickListner mOnClickListner;
    private List<Message> objects;
    private Context context;
    private User User;
    private static boolean sendMessageNodeState;
    private     int LayoutIdForListItem;

    public interface MessageListItemClickListner{
        void onMessageItemClick(Message messageClicked, int position);
    }

    public ConversationAdapter( List<Message> objects, User User,MessageListItemClickListner mOnClickListner) {
        this.objects=objects;
        this.context=context;
        this.mOnClickListner=mOnClickListner;
        this.User=User;
    }

    @Override
    public int getItemViewType(int position) {
        Message message=objects.get(position);
        String currentUserUid=FirebaseAuth.getInstance().getCurrentUser().getUid();
        if (message.getSendBy().equals(currentUserUid)){
            return    LayoutIdForListItem= R.layout.message_item_right;
        } else if (!message.getSendBy().equals(currentUserUid)){
            return   LayoutIdForListItem=R.layout.message_item_left;
        }
        return LayoutIdForListItem=R.layout.message_item_left;
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
        Message   friendlyMessage=this.objects.get(position);

        holder.bindMessages(friendlyMessage);
        CreateAnimationView.createAnimation(holder.itemView);
    }

    @Override
    public int getItemCount() {
        return objects.size();
    }


    public class MessageViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        ImageView photoImageView;
        TextView messageTextView;
        TextView messageStatusTextView;
        TextView timeInstanceMessage;
        public MessageViewHolder(View itemView) {
            super(itemView);

            photoImageView = (ImageView) itemView.findViewById(R.id.photoImageView);
            messageTextView = (TextView) itemView.findViewById(R.id.messageTextView);
            messageStatusTextView = (TextView) itemView.findViewById(R.id.send_message_status);
            timeInstanceMessage=(TextView) itemView.findViewById(R.id.send_Message_time);
            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    mOnClickListner.onMessageItemClick(objects.get(getAdapterPosition()),getAdapterPosition());
                    return true;
                }
            });
            itemView.setOnClickListener(this);
        }

        void bindMessages(Message message){
            boolean isPhoto = message.getMessagePhotoUrl() != null;
            if (isPhoto) {
                messageTextView.setVisibility(View.GONE);
                photoImageView.setVisibility(View.VISIBLE);
                Glide.with(photoImageView.getContext())
                        .load(message.getMessagePhotoUrl())
                        .into(photoImageView);
            } else {
                messageTextView.setVisibility(View.VISIBLE);
                photoImageView.setVisibility(View.GONE);
                messageTextView.setText(message.getTextMessage());
            }
            messageStatusTextView.setText(message.getMessageStatus());
            timeInstanceMessage.setText(message.getTimeStamp());

        }

        @Override
        public void onClick(View v) {
           // int adapterPosition=getAdapterPosition();
         //  Message message=objects.get(adapterPosition);
          //  mOnClickListner.onMessageItemClick(message);

        }
    }

    public static void getMessageSendState(boolean sendMessageNodeState){
        ConversationAdapter.sendMessageNodeState =sendMessageNodeState;
    }
}
