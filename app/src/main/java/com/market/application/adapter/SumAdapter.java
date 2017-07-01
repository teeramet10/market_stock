package com.market.application.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.market.application.R;
import com.market.application.javaclass.SummarySale;

import java.text.DecimalFormat;
import java.util.ArrayList;

/**
 * Created by barbie on 5/5/2017.
 */

public class SumAdapter extends BaseAdapter {
    private DecimalFormat decimalFormat = new DecimalFormat("#,###.00");
    ArrayList<SummarySale> summarySales;
    Context context;
    ViewHolder viewHolder;
    LayoutInflater inflater;

    public SumAdapter(ArrayList<SummarySale> summarySales, Context context) {
        this.summarySales = summarySales;
        this.context = context;
        inflater = LayoutInflater.from(context);
    }

    public void setSummarySales(ArrayList<SummarySale> summarySales) {
        this.summarySales = summarySales;
    }

    @Override
    public int getCount() {
        return summarySales.size();
    }

    @Override
    public Object getItem(int i) {
        return summarySales.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        if (view == null) {
            viewHolder = new ViewHolder();
            view = inflater.inflate(R.layout.item_sum_sale, null, false);
            viewHolder.tvName = (TextView) view.findViewById(R.id.name_product);
            viewHolder.tvQual = (TextView) view.findViewById(R.id.quality);
            viewHolder.tvTotal = (TextView) view.findViewById(R.id.total);
            viewHolder.tvCost = (TextView) view.findViewById(R.id.cost);
            viewHolder.tvProfit = (TextView) view.findViewById(R.id.profit);
            viewHolder.relativeLayout= (RelativeLayout) view.findViewById(R.id.layout);

            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }

        SummarySale summarySale = summarySales.get(i);
        if (summarySale != null) {


            viewHolder.tvName.setText(summarySale.getName());
            viewHolder.tvQual.setText(String.valueOf(summarySale.getValue()));
            viewHolder.tvTotal.setText(String.valueOf((int)summarySale.getTotal()));
            viewHolder.tvCost.setText(decimalFormat.format(summarySale.getCost()));
            viewHolder.tvProfit.setText(decimalFormat.format(summarySale.getProfit()));


        }
        return view;
    }


    private class ViewHolder {

        TextView tvName;
        TextView tvQual;
        TextView tvTotal;
        TextView tvCost;
        TextView tvProfit;
        RelativeLayout relativeLayout;
    }
}
