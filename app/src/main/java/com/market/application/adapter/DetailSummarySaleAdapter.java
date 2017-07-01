package com.market.application.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.market.application.R;
import com.market.application.javaclass.DetailCart;

import java.text.DecimalFormat;
import java.util.ArrayList;

/**
 * Created by barbie on 5/29/2017.
 */

public class DetailSummarySaleAdapter extends BaseAdapter {
    private DecimalFormat decimalFormat = new DecimalFormat("#,###.00");
    private ArrayList<DetailCart> detailCarts;
    Context context;
    private LayoutInflater layoutInflater;
    private ViewHolder viewHolder;

    public DetailSummarySaleAdapter(ArrayList<DetailCart> detailCarts, Context context) {
        this.detailCarts = detailCarts;
        this.context = context;
        this.layoutInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return detailCarts.size();
    }

    @Override
    public Object getItem(int i) {
        return detailCarts.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View convertView, ViewGroup viewGroup) {
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = layoutInflater.inflate(R.layout.item_detail_summary_sale, viewGroup, false);

            viewHolder.tvQual = (TextView) convertView.findViewById(R.id.tvquality);
            viewHolder.tvCost = (TextView) convertView.findViewById(R.id.tvcost);
            convertView.setTag(viewHolder);

        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        if (detailCarts.size() != 0) {
            DetailCart detailCart = detailCarts.get(i);
            String qual = String.valueOf(detailCart.getDetail_cart_quality());
            viewHolder.tvQual.setText(qual);
            viewHolder.tvCost.setText(decimalFormat.format(detailCart.getDetail_cart_cost()) + " "
                    + context.getResources().getString(R.string.productunit));

        }
        return convertView;
    }

    private class ViewHolder {
        TextView tvQual;
        TextView tvCost;
    }
}
