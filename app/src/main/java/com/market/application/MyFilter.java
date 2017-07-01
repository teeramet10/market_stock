package com.market.application;

import android.widget.Filter;

import com.market.application.adapter.ProductAdapter;
import com.market.application.javaclass.Stock;

import java.util.ArrayList;

/**
 * Created by barbie on 4/17/2017.
 */

public class MyFilter extends Filter {

    private ArrayList<Stock> stockList;
    private ArrayList<Stock> filteredStockList;
    private ProductAdapter adapter;

    public MyFilter(ArrayList<Stock> stockList, ProductAdapter adapter) {
        this.adapter = adapter;
        this.stockList = stockList;
        this.filteredStockList = new ArrayList();
    }

    @Override
    protected FilterResults performFiltering(CharSequence constraint) {
        filteredStockList.clear();
        FilterResults results = new FilterResults();

        //here you need to add proper items do filteredStockList
        for (Stock item : stockList) {
            if (item.getProduct().getName().toLowerCase().trim().contains(constraint)) {
                filteredStockList.add(item);
            }
        }

        results.values = filteredStockList;
        results.count = filteredStockList.size();
        return results;
    }

    @Override
    protected void publishResults(CharSequence constraint, FilterResults results) {
        adapter.setList(filteredStockList);
        adapter.notifyDataSetChanged();
    }
}
