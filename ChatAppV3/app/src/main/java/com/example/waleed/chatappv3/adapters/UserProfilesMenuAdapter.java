package com.example.waleed.chatappv3.adapters;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.waleed.chatappv3.R;
import com.example.waleed.chatappv3.models.User;
import com.example.waleed.chatappv3.utils.CreateAnimationView;
import com.google.firebase.database.ChildEventListener;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class UserProfilesMenuAdapter extends RecyclerView.Adapter<UserProfilesMenuAdapter.UserProfilesViewHolder> {
    private List<User> usersList;
    final private ListItemClickListner mOnClickListner;

    public interface ListItemClickListner{
        void onListItemClick(User clickedUser);
    }


    public UserProfilesMenuAdapter(List<User> usersList, ListItemClickListner listner){
        this.usersList=usersList;
        mOnClickListner=listner;
    }

    @Override
    public UserProfilesViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context=parent.getContext();
        int LayoutInflatorId=R.layout.user_profile;
        LayoutInflater inflater=LayoutInflater.from(context);
        boolean shouldAttachToParentImediately=false;
        View view=inflater.inflate(LayoutInflatorId,parent,shouldAttachToParentImediately);
        UserProfilesViewHolder profilesViewHolder=new UserProfilesViewHolder(view);
        return profilesViewHolder;
    }

    @Override
    public void onBindViewHolder(UserProfilesViewHolder holder, int position) {
        User user = this.usersList.get(position);
        holder.bindUsers(user);
    }

    @Override
    public int getItemCount() {
        return usersList.size();
    }


    public class UserProfilesViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
    CircleImageView imageProfile;
    TextView userNameProfile;
    TextView userConnectionStatus;
    TextView userLastMessage;
        public UserProfilesViewHolder(View itemView) {
            super(itemView);
            imageProfile=(CircleImageView) itemView.findViewById(R.id.userPhotoProfile);
            userNameProfile=(TextView) itemView.findViewById(R.id.userFirstNameProfile);
            userConnectionStatus=(TextView) itemView.findViewById(R.id.connectionStatus);
            userLastMessage=(TextView) itemView.findViewById(R.id.userLastMessage);
            itemView.setOnClickListener(this);
        }

        void bindUsers(User user){

            Glide.with(imageProfile.getContext())
                    .load(user.getProfileImageUrl())
                    .into(imageProfile);
            imageProfile.setImageResource(R.drawable.headshot_7);

            userNameProfile.setText(user.getUserName());
            if (user.getconnectionState().equals("online")){
                userConnectionStatus.setTextColor(Color.GREEN);
            } else if (user.getconnectionState().equals("offline")){
                userConnectionStatus.setTextColor(Color.RED);
            }
            userConnectionStatus.setText(user.getconnectionState());
        }

        @Override
        public void onClick(View v) {
            int adapterPosition=getAdapterPosition();
            User user=usersList.get(adapterPosition);
            mOnClickListner.onListItemClick(user);
        }
    }

    public void refill(User users) {

        // Add each user and notify recyclerView about change
        usersList.add(users);
        notifyDataSetChanged();
    }
}
