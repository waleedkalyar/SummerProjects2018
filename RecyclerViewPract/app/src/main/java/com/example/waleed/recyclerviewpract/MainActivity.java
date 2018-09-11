package com.example.waleed.recyclerviewpract;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements GreenAdapter.ListItemClickListner {
    private static final int NUM_LIST_ITEMS=100;
RecyclerView mNumberItems;
GreenAdapter mAdapter;
Toast mToast;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId=item.getItemId();
        switch (itemId){
            case R.id.action_refresh:
                mAdapter=new GreenAdapter(NUM_LIST_ITEMS,this);
                mNumberItems.setAdapter(mAdapter);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mNumberItems=(RecyclerView)findViewById(R.id.rv_numbers);
        LinearLayoutManager layoutManager=new LinearLayoutManager(this);
        mNumberItems.setLayoutManager(layoutManager);
        mNumberItems.setHasFixedSize(true);
        mAdapter=new GreenAdapter(NUM_LIST_ITEMS,this);
        mNumberItems.setAdapter(mAdapter);


    }

    @Override
    public void onListItemClick(int clickedItemIndex) {
        String toastMessage="Item # "+clickedItemIndex+" is clicked";
        mToast=Toast.makeText(this,toastMessage,Toast.LENGTH_LONG);

        mToast.show();
    }
}
