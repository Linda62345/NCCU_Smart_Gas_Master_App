package com.glotech.smartgasmasterapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import glotech.smartgasmasterapp.R;

public class OrderListAdapter extends ArrayAdapter<OrderListItem> {
    int mresource;
    private Context mContext;
    public OrderListAdapter (Context context, int resource, ArrayList<OrderListItem> objects) {
        super(context,resource,objects);
        mresource = resource;
        mContext = context;
    }
    public View getView(int position, View convertView, ViewGroup parent){
        String address = getItem(position).getAddress();
        String expectTime = getItem(position).getExpectTime();

        LayoutInflater inflater = LayoutInflater.from(mContext);
        convertView = inflater.inflate(mresource,parent,false);

        TextView addressTV = convertView.findViewById(R.id.addressTV);
        TextView expectTimeTV = convertView.findViewById(R.id.expectTimeTV);

        if(address!=null){
            addressTV.setText(address);
        }
        else{
            addressTV.setText("地址錯誤");
        }
        if(expectTime!=null){
            expectTimeTV.setText("預期時間: "+expectTime);
        }
        else{
            expectTimeTV.setText("預期時間錯誤");
        }

        return convertView;
    }
}
