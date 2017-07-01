package com.market.application.fragment;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.market.application.SharePreferenceHelper;
import com.market.application.activity.ProductActivity;
import com.market.application.activity.ProductStockActivity;
import com.market.application.adapter.ProductAdapter;
import com.market.application.database.DatabaseManagement;
import com.market.application.database.SqliteHelper;
import com.market.application.javaclass.Stock;

import java.util.ArrayList;

/**
 * Created by barbie on 5/12/2017.
 */

public class BaseFragment extends Fragment {

    private ProductAdapter productAdapter;
    ArrayList<Stock> stocks;
    Context context;
    Activity activity;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = super.onCreateView(inflater, container, savedInstanceState);
        return v;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
        this.activity = (Activity) context;
    }




    public int setRecyclerView(RecyclerView recyclerView, LinearLayout emptyLayout) {
        int id = 1;

        if (activity instanceof ProductActivity) {
            id = ((ProductActivity) activity).idType;
        } else if (activity instanceof ProductStockActivity) {
            id = ((ProductStockActivity) activity).idType;
        }
        stocks = new ArrayList<>();
        ArrayList<Stock> stocklist = new ArrayList<>();
        DatabaseManagement db = new DatabaseManagement(context);
        ArrayList<Integer> idproducts = db.selectIdProduct();
        if (activity instanceof ProductActivity || activity instanceof ProductStockActivity) {

            String sql = "select * from " + SqliteHelper.STOCK_TB
                    + " inner join " + SqliteHelper.PRODUCT_TB
                    + " on( " + SqliteHelper.PRODUCT_TB + "." + SqliteHelper.PRODUCT_ID
                    + "=" + SqliteHelper.STOCK_TB + "." + SqliteHelper.ID_PRODUCT
                    + ") inner join " + SqliteHelper.TYPE_TB
                    + " on (" + SqliteHelper.TYPE_TB + "." + SqliteHelper.TYPE_ID
                    + "=" + SqliteHelper.PRODUCT_TB + "." + SqliteHelper.ID_TYPE
                    + ") where " + SqliteHelper.PRODUCT_TB + "." + SqliteHelper.ID_DISABLE + "=? and "
                    + SqliteHelper.PRODUCT_TB + "." + SqliteHelper.ID_TYPE + "=?"
                    + " order by " + SqliteHelper.STOCK_TB + "." + SqliteHelper.ID_PRODUCT + " ASC,"
                    + SqliteHelper.STOCK_TB + "." + SqliteHelper.DATE_FIRST_IN + " ASC";

            stocklist = db.selectStock(sql, new String[]{String.valueOf(1), String.valueOf(id)});


        } else {
            String sql = "select * from " + SqliteHelper.STOCK_TB
                    + " inner join " + SqliteHelper.PRODUCT_TB
                    + " on " + SqliteHelper.PRODUCT_TB + "." + SqliteHelper.PRODUCT_ID
                    + "=" + SqliteHelper.STOCK_TB + "." + SqliteHelper.ID_PRODUCT
                    + " inner join " + SqliteHelper.TYPE_TB
                    + " on " + SqliteHelper.TYPE_TB + "." + SqliteHelper.TYPE_ID
                    + "=" + SqliteHelper.PRODUCT_TB + "." + SqliteHelper.ID_TYPE
                    + " where " + SqliteHelper.PRODUCT_TB + "." + SqliteHelper.ID_DISABLE + "=?"
                    + " order by " + SqliteHelper.STOCK_TB + "." + SqliteHelper.ID_PRODUCT + " ASC,"
                    + SqliteHelper.STOCK_TB + "." + SqliteHelper.DATE_FIRST_IN + " ASC";

            stocklist = db.selectStock(sql, new String[]{String.valueOf(1)});
        }
        db.closeDatabase();

        for (int i = 0; i < idproducts.size(); i++) {
            Stock stock = new Stock();
            int qual = 0;

            for (int j = 0; j < stocklist.size(); j++) {
                if (idproducts.get(i) == stocklist.get(j).getProduct().getId()) {
                    qual += stocklist.get(j).getProduct_quality();
                    stock.setProduct(stocklist.get(j).getProduct());
                    stock.setProduct_cost(stocklist.get(j).getProduct_cost());
                    stock.setProduct_price(stocklist.get(j).getProduct_price());
                    stock.setStockid(stocklist.get(j).getStockid());
                    stock.setDate_fist_in(stocklist.get(j).getDate_fist_in());
                }
            }
            stock.setProduct_quality(qual);
            if (activity instanceof ProductActivity || activity instanceof ProductStockActivity) {
                if (stock.getProduct() != null) {
                    stocks.add(stock);
                }
            } else {
                SharePreferenceHelper sp = new SharePreferenceHelper(context);
                int value = sp.getValueSharePreference();
                //TODO เลือกเฉพาะสินค้าเหลือน้อย
                if (stock.getProduct() != null && stock.getProduct_quality() <= value) {
                    stocks.add(stock);
                }
            }
        }


        if (stocks.size() == 0) {
            if (emptyLayout.getVisibility() == View.INVISIBLE) {
                emptyLayout.setVisibility(View.VISIBLE);
            }
        } else {
            if (emptyLayout.getVisibility() == View.VISIBLE) {
                emptyLayout.setVisibility(View.INVISIBLE);
            }
        }

        productAdapter = new ProductAdapter(context, stocks);
        recyclerView.setLayoutManager(new GridLayoutManager(context, 2));
        recyclerView.setAdapter(productAdapter);

        return id;
    }

    public ProductAdapter getAdapter() {
        return productAdapter;
    }

    public ArrayList<Stock> getStocks() {
        return stocks;
    }
}
