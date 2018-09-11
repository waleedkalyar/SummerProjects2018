package com.example.waleed.recyclerviewpract;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.zip.Inflater;

public class GreenAdapter extends RecyclerView.Adapter<GreenAdapter.NumberViewHolder> {
  int mNumberItems;
  private static int viewHolderCount;
  private final ListItemClickListner mOnClickListner;

  public interface ListItemClickListner{
      void onListItemClick(int clickedItemIndex);
  }

  public GreenAdapter(int numberOfItems, ListItemClickListner listner ){
      mNumberItems=numberOfItems;
      mOnClickListner=listner;
      viewHolderCount=0;
  }

    @Override
    public NumberViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context=parent.getContext();
        int listLayoutIdOfItem=R.layout.number_list_item;
        LayoutInflater inflater=LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately=false;
        View view=inflater.inflate(listLayoutIdOfItem,parent,shouldAttachToParentImmediately);
        NumberViewHolder viewHolder=new NumberViewHolder(view);

        viewHolder.viewHolderIndex.setText("ViewHolder Index:"+viewHolderCount);
        int backgroundColorForItem=ColorUtils.getViewHolderBackgroundColorFromInstance(context,viewHolderCount);
        viewHolder.itemView.setBackgroundColor(backgroundColorForItem);
        viewHolderCount++;

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(NumberViewHolder holder, int position) {
      holder.bind(position);
    }

    @Override
    public int getItemCount() {
        return mNumberItems;
    }


    class NumberViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
TextView listNumberItemView;
TextView viewHolderIndex;
        public NumberViewHolder(View itemView) {
            super(itemView);
            listNumberItemView=(TextView)itemView.findViewById(R.id.tv_item_number);
            viewHolderIndex=(TextView)itemView.findViewById(R.id.tv_view_holder_index);

            itemView.setOnClickListener(this);

        }

        void bind(int indexItem){
            listNumberItemView.setText(String.valueOf(indexItem));
        }

        @Override
        public void onClick(View v) {
            int clickedItemPosition=getAdapterPosition();
            mOnClickListner.onListItemClick(clickedItemPosition);
        }
    }
}
