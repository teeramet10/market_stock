package com.market.application.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.market.application.R;
import com.market.application.activity.AddProductActivity;
import com.market.application.activity.EditProductActivity;
import com.market.application.javaclass.Type;

import java.util.ArrayList;

/**
 * Created by barbie on 4/9/2017.
 */

public class TypeAdapter extends BaseAdapter {
    Context context;
    private ArrayList<Type> typeArrayList;
    private LayoutInflater layoutInflater;
    private MyHolder myHolder;
    private Activity activity;

    public TypeAdapter(Context context, ArrayList<Type> typeArrayList) {
        this.context = context;
        this.activity= (Activity) context;
        this.typeArrayList = typeArrayList;
        layoutInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return typeArrayList.size();
    }

    @Override
    public Type getItem(int i) {
        return typeArrayList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        if (view == null) {
            myHolder = new MyHolder();
            if(activity instanceof AddProductActivity || activity instanceof EditProductActivity){
                view = layoutInflater.inflate(R.layout.list_item_type2, null, false);
            }else {
                view = layoutInflater.inflate(R.layout.list_item_type, null, false);
            }
            myHolder.tvName = (TextView) view.findViewById(R.id.tvname);
            view.setTag(myHolder);
        } else {
            myHolder = (MyHolder) view.getTag();
        }

        Type type = typeArrayList.get(i);
        if (type != null) {
            myHolder.tvName.setText(type.getName());
        }
        return view;
    }


    private class MyHolder {
        TextView tvName;
    }
}
