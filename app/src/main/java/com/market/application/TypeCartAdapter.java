package com.market.application;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import com.market.application.javaclass.Cart;

import java.util.ArrayList;

/**
 * Created by Administrator on 31/3/2560.
 */
public class TypeCartAdapter  extends RecyclerView.Adapter {


    Context context;
    ArrayList<Cart> carts;

    public TypeCartAdapter(Context context, ArrayList<Cart> carts) {
        this.context = context;
        this.carts = carts;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return carts.size();
    }
}
