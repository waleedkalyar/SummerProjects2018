package com.example.waleed.friendlychat;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.example.waleed.friendlychat.Adapter.ContectAdapter;
import com.example.waleed.friendlychat.model.CotectModel;

public class MainActivity extends AppCompatActivity implements ContectAdapter.ContectItemClickeHandler {
RecyclerView contectsList;
ContectAdapter mAdapter;
CotectModel[] model;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        contectsList=(RecyclerView)findViewById(R.id.rv_contacts);
        LinearLayoutManager layoutManager=new LinearLayoutManager(this);

        model=new CotectModel[]{
                new CotectModel(R.mipmap.ic_launcher_round,"waleed","hello"),
                new CotectModel(R.mipmap.ic_launcher_round,"arslan","hi"),
                new CotectModel(R.mipmap.ic_launcher_round,"BBC","what is that"),
                new CotectModel(R.mipmap.ic_launcher_round,"Netcad","I am new")
        };
        mAdapter=new ContectAdapter(this);

        contectsList.setHasFixedSize(true);
        contectsList.setLayoutManager(layoutManager);
        contectsList.setAdapter(mAdapter);


    }

    @Override
    public void onClick(String ContectOfThisPerson) {
        String msg=ContectOfThisPerson+" Contect is clicked";
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

}
