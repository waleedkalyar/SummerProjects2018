package com.example.waleed.chatappv3.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.waleed.chatappv3.R;
import com.example.waleed.chatappv3.models.User;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class UserProfilesMenuAdapter extends RecyclerView.Adapter<UserProfilesMenuAdapter.UserProfilesViewHolder> {
    private List<User> usersList;

    public UserProfilesMenuAdapter(List<User> usersList){
        this.usersList=usersList;
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


    public class UserProfilesViewHolder extends RecyclerView.ViewHolder{
    CircleImageView imageProfile;
    TextView userNameProfile;
    TextView userConnectionStatus;
        public UserProfilesViewHolder(View itemView) {
            super(itemView);
            imageProfile=(CircleImageView) itemView.findViewById(R.id.userPhotoProfile);
            userNameProfile=(TextView) itemView.findViewById(R.id.userFirstNameProfile);
            userConnectionStatus=(TextView) itemView.findViewById(R.id.connectionStatus);
        }

        void bindUsers(User user){

            Glide.with(imageProfile.getContext())
                    .load(user.getProfileImageUrl())
                    .into(imageProfile);
            userNameProfile.setText(user.getUserName());
            userConnectionStatus.setText(user.getconnectionState());
        }
    }
}
