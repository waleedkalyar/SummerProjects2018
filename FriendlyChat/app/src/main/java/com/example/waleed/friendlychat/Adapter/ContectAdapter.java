package com.example.waleed.friendlychat.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.waleed.friendlychat.R;

public class ContectAdapter extends RecyclerView.Adapter<ContectAdapter.ContectViewHolder>{
private String[] mContectsList;
private final ContectItemClickeHandler mContectClickHandler;

public interface ContectItemClickeHandler{
    void onClick(String ContectOfThisPerson);
}

public ContectAdapter( ContectItemClickeHandler clickeHandler ){
mContectClickHandler=clickeHandler;
}
    @Override
    public ContectViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context=parent.getContext();
        int layoutIdForListItem=R.layout.contect_item;
        LayoutInflater inflater=LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately=false;
        View view=inflater.inflate(layoutIdForListItem,parent,shouldAttachToParentImmediately);
        ContectViewHolder viewHolder=new ContectViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ContectViewHolder holder, int position) {
String contectOfThisPerson=mContectsList[position];
// the lines were implement in future and all things also set in set text
//int ProfileImg=contectOfThisPerson.getImage();
//String name=contectOfThisPerson.getName();
//String message=contectOfThisPerson.getMessage();
holder.personName.setText(contectOfThisPerson);
holder.personMessage.setText(contectOfThisPerson);
holder.imgProfile.setImageResource(R.mipmap.ic_launcher_round);
    }

    @Override
    public int getItemCount() {
        return 0;
    }

    class ContectViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
ImageView imgProfile;
TextView personName;
TextView personMessage;
        public ContectViewHolder(View itemView) {
            super(itemView);
            imgProfile=itemView.findViewById(R.id.img_profile);
            personName=itemView.findViewById(R.id.tv_person_name);
            personMessage=itemView.findViewById(R.id.tv_person_message);

            itemView.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
            int clickedPosition=getAdapterPosition();
            String contectOfThisPerson=mContectsList[clickedPosition];
            mContectClickHandler.onClick(contectOfThisPerson);
        }
    }
}
