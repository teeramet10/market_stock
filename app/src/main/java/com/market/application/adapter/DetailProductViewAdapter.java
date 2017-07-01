package com.market.application.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.market.application.R;
import com.market.application.javaclass.Stock;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

/**
 * Created by barbie on 5/18/2017.
 */

public class DetailProductViewAdapter extends BaseAdapter {
    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    DecimalFormat decimalFormat = new DecimalFormat("#,###.00");
    Context context;
    ArrayList<Stock> stocks;
    LayoutInflater inflater;
    ViewHolder viewHolder;

    public DetailProductViewAdapter(Context context, ArrayList<Stock> stocks) {
        this.context = context;
        this.stocks = stocks;
        inflater = LayoutInflater.from(context);

    }

    @Override
    public int getCount() {
        return stocks.size();
    }

    @Override
    public Object getItem(int i) {
        return stocks.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View convertView, ViewGroup viewGroup) {
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = inflater.inflate(R.layout.item_cost_product, viewGroup, false);

            viewHolder.tvDate = (TextView) convertView.findViewById(R.id.tvdate);
            viewHolder.tvQual = (TextView) convertView.findViewById(R.id.tvquality);
            viewHolder.tvCost = (TextView) convertView.findViewById(R.id.tvcost);
            convertView.setTag(viewHolder);

        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }


        if (stocks.size() != 0) {
            Stock stock = stocks.get(i);

            viewHolder.tvDate.setText(dateFormat.format(stock.getDate_fist_in()));
            viewHolder.tvQual.setText(String.valueOf(stock.getProduct_quality()));
            viewHolder.tvCost.setText(decimalFormat.format(stock.getProduct_cost()));

        }
        return convertView;
    }

    class ViewHolder {
        TextView tvDate;
        TextView tvQual;
        TextView tvCost;
    }
}
