package com.example.waleed.phonebook.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.waleed.phonebook.Models.PhoneBookModel;
import com.example.waleed.phonebook.R;

public class PhoneBookAdapter extends ArrayAdapter {
    Context context;
    int resources;
    PhoneBookModel[] phonebookData;
    LayoutInflater phoneBookInflater;
    public PhoneBookAdapter(@NonNull Context context, int resource, @NonNull PhoneBookModel[] phoneBookData) {
        super(context, resource, phoneBookData);
        this.context=context;
        this.resources=resource;
        this.phonebookData=phoneBookData;
        phoneBookInflater=(LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);

    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        convertView=phoneBookInflater.inflate(resources,null);

        TextView name=convertView.findViewById(R.id.tv_name);
        TextView phoneNo=convertView.findViewById(R.id.tv_phone_no);

        name.setText(phonebookData[position].name);
        phoneNo.setText(phonebookData[position].phoneNo);
        return convertView;

    }

    @Override
    public int getCount() {
        return phonebookData.length;
    }
}
