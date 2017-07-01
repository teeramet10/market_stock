package com.market.application.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.market.application.R;
import com.market.application.javaclass.DetailCart;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.text.DecimalFormat;
import java.util.ArrayList;

/**
 * Created by Administrator on 30/3/2560.
 */
public class CashierProductAdapter extends BaseAdapter {
    DecimalFormat decimalFormat=new DecimalFormat("#,###");
    Context context;
    ArrayList<DetailCart> cartArrayList;
    LayoutInflater inflater;
    ViewHolder viewHolder;

    public CashierProductAdapter(Context context, ArrayList<DetailCart> cartArrayList) {
        this.context = context;
        this.cartArrayList = cartArrayList;
        inflater =LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return cartArrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return cartArrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView==null){
            viewHolder =new ViewHolder();
            convertView=inflater.inflate(R.layout.item_cashier_product,parent,false);

            viewHolder.imgProduct = (ImageView) convertView.findViewById(R.id.imgproduct);
            viewHolder.tvNameProduct= (TextView) convertView.findViewById(R.id.nameproduct);
            viewHolder.tvPrice = (TextView) convertView.findViewById(R.id.totalunit);
            viewHolder.tvValue = (TextView) convertView.findViewById(R.id.value);
            convertView.setTag(viewHolder);
        }else {
            viewHolder = (ViewHolder) convertView.getTag();
        }


        if(cartArrayList.size()!= 0){
            DetailCart detailCart=cartArrayList.get(position);
            if (detailCart.getStock().getProduct().getPathimage() != null) {
                File file = new File(detailCart.getStock().getProduct().getPathimage());
                if (file.exists()) {
                    Picasso.with(context).load(file).fit().centerCrop().error(R.drawable.pack).into(viewHolder.imgProduct);
                }
            } else {
                Picasso.with(context).load(R.drawable.pack).fit().centerCrop().into(viewHolder.imgProduct);
            }
            viewHolder.tvNameProduct.setText(detailCart.getStock().getProduct().getName());
            viewHolder.tvPrice.setText(decimalFormat.format((int)detailCart.getDetail_cart_total()));
            viewHolder.tvValue.setText(String.valueOf(detailCart.getDetail_cart_quality()));

        }
        return convertView;
    }

    class ViewHolder{
        ImageView imgProduct;
        TextView tvNameProduct;
        TextView tvValue;
        TextView tvPrice;
    }
}
