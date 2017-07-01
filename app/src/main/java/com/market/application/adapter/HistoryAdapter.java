package com.market.application.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.market.application.R;
import com.market.application.javaclass.Order;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

/**
 * Created by barbie on 5/1/2017.
 */

public class HistoryAdapter extends BaseAdapter {
    SimpleDateFormat dateFormat=new SimpleDateFormat("MMM d, yyyy");
    Context context;
    ArrayList<Order> orders;
    LayoutInflater layoutInflater;
    MyHolder myHolder;

    public HistoryAdapter(Context context, ArrayList<Order> orders) {
        this.context = context;
        this.orders = orders;
        this.layoutInflater=LayoutInflater.from(context);
        this.myHolder = myHolder;
    }

    public void setOrders(ArrayList<Order> orders) {
        this.orders = orders;
    }

    @Override
    public int getCount() {
        return orders.size();
    }

    @Override
    public Object getItem(int i) {
        return orders.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if(view==null) {
            myHolder=new MyHolder();
            view = layoutInflater.inflate(R.layout.item_view_order, null,false);
            myHolder.tvOrderId= (TextView) view.findViewById(R.id.orderid);
            myHolder.tvOrderDate = (TextView) view.findViewById(R.id.orderdate);
            myHolder.tvOrderTotal = (TextView) view.findViewById(R.id.ordertotal);
            view.setTag(myHolder);
        }else {
            myHolder = (MyHolder) view.getTag();
        }

        Order order=orders.get(i);
        if(order!=null){
            myHolder.tvOrderId.setText("ID: "+String.valueOf(order.getOrder_id()));
            myHolder.tvOrderDate.setText(dateFormat.format(order.getOrder_date()));
            myHolder.tvOrderTotal.setText(String.valueOf((int)order.getOrder_total()));

        }
        return view;
    }

    private class MyHolder{
        TextView tvOrderId;
        TextView tvOrderDate;
        TextView tvOrderTotal;
    }
}
