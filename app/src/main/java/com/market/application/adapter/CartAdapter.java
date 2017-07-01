package com.market.application.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.market.application.R;
import com.market.application.activity.CartActivity;
import com.market.application.activity.HistoryDetailActivity;
import com.market.application.database.DatabaseManagement;
import com.market.application.javaclass.DetailCart;
import com.squareup.picasso.Picasso;
import com.travijuu.numberpicker.library.Enums.ActionEnum;
import com.travijuu.numberpicker.library.Interface.ValueChangedListener;
import com.travijuu.numberpicker.library.NumberPicker;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by Administrator on 29/3/2560.
 */
public class CartAdapter extends BaseAdapter {

    Context context;
    ArrayList<DetailCart> detailCartArrayList;

    LayoutInflater inflater;
    ViewHolder viewHolder;
    Activity activity;
    private Toast toast;

    public CartAdapter(Context context, ArrayList<DetailCart> detailCartArrayList) {
        this.context = context;
        this.detailCartArrayList = detailCartArrayList;
        inflater = LayoutInflater.from(context);
        this.activity = (Activity) context;
    }

    @Override
    public int getCount() {
        return detailCartArrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return detailCartArrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            viewHolder = new ViewHolder();
            if (activity instanceof CartActivity) {
                convertView = inflater.inflate(R.layout.item_cart, parent, false);
                viewHolder.imgProduct = (ImageView) convertView.findViewById(R.id.imgproduct);
                viewHolder.tvName = (TextView) convertView.findViewById(R.id.name_product);
                viewHolder.tvPrice = (TextView) convertView.findViewById(R.id.price_product);
                viewHolder.numberPicker = (NumberPicker) convertView.findViewById(R.id.numberpicker);
                viewHolder.imgDelete = (ImageButton) convertView.findViewById(R.id.del_product);
            } else {
                convertView = inflater.inflate(R.layout.item_history_detail, parent, false);
                viewHolder.imgProduct = (ImageView) convertView.findViewById(R.id.imgproduct);
                viewHolder.tvName = (TextView) convertView.findViewById(R.id.name_product);
                viewHolder.tvPrice = (TextView) convertView.findViewById(R.id.price_product);
                viewHolder.tvValue = (TextView) convertView.findViewById(R.id.txtvalue);
            }

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }


        if (detailCartArrayList.size() != 0) {
            final DetailCart detailCart = detailCartArrayList.get(position);

            if (detailCart.getStock().getProduct().getPathimage() != null) {
                File file = new File(detailCart.getStock().getProduct().getPathimage());
                if (file.exists()) {
                    Picasso.with(context).load(file).fit().centerCrop().error(R.drawable.pack).into(viewHolder.imgProduct);
                }
            } else {
                Picasso.with(context).load(R.drawable.pack).fit().centerCrop().into(viewHolder.imgProduct);
            }

            viewHolder.tvName.setText(detailCart.getStock().getProduct().getName());
            viewHolder.tvPrice.setText(String.valueOf((int)detailCart.getDetail_cart_total()));

            if (activity instanceof HistoryDetailActivity) {

                viewHolder.tvValue.setText(String.valueOf(detailCart.getDetail_cart_quality()));

            } else if(activity instanceof CartActivity){
                viewHolder.numberPicker.setValue(detailCart.getDetail_cart_quality());
                viewHolder.numberPicker.setValueChangedListener(new ValueChangedListener() {
                    @Override
                    public void valueChanged(int value, ActionEnum action) {

                        DetailCart detailCart = detailCartArrayList.get(position);
                        if(value<=detailCart.getStock().getProduct_quality()) {
                            double price = detailCart.getStock().getProduct_price() * value;
                            double cost =detailCart.getStock().getProduct_cost() *value;
                            detailCart.setDetail_cart_total(price);
                            detailCart.setDetail_cart_quality(value);
                            detailCart.setDetail_cart_cost(cost);
                            detailCartArrayList.set(position, detailCart);

                            if(value ==0){
                                DatabaseManagement db = new DatabaseManagement(context);
                                long delcart = db.deleteDetailCart(detailCart.getDetail_cart_id());
                                long deldecart = db.deleteCart(detailCart.getDetail_cart_id());
                                if (delcart != 0 && deldecart != 0) {
                                    detailCartArrayList.remove(position);
                                    notifyDataSetChanged();
                                }
                                db.closeDatabase();
                            }
                            notifyDataSetChanged();
                        }else {
                            detailCartArrayList.set(position, detailCart);
                            notifyDataSetChanged();
                            toast=Toast.makeText(context, context.getResources().getString(R.string.productnenough), Toast.LENGTH_SHORT);
                            toast.show();

                        }


                    }
                });

                viewHolder.imgDelete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
                        alertDialog.setMessage(context.getString(R.string.del_product));
                        alertDialog.setIcon(android.R.drawable.ic_delete);
                        alertDialog.setPositiveButton(context.getString(R.string.ok), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                DetailCart detailCart = detailCartArrayList.get(position);
                                DatabaseManagement db = new DatabaseManagement(context);
                                long delcart = db.deleteDetailCart(detailCart.getDetail_cart_id());
                                long deldecart = db.deleteCart(detailCart.getDetail_cart_id());
                                if (delcart != 0 && deldecart != 0) {
                                    detailCartArrayList.remove(position);
                                    notifyDataSetChanged();
                                    Toast.makeText(context,context.getString(R.string.finish), Toast.LENGTH_SHORT).show();
                                }
                                db.closeDatabase();
                            }
                        });
                        alertDialog.setNegativeButton(context.getString(R.string.cancel), null);
                        alertDialog.setCancelable(false);
                        alertDialog.create().show();
                    }
                });
            }

        }


        return convertView;
    }


    public Toast getToast() {
        return toast;
    }

    class ViewHolder {
        ImageView imgProduct;
        TextView tvName;
        TextView tvPrice;
        NumberPicker numberPicker;
        ImageButton imgDelete;
        TextView tvValue;
    }
}
