package com.market.application.fragment;


import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.market.application.R;
import com.market.application.adapter.ProductAdapter;
import com.market.application.javaclass.Stock;

import java.util.Collections;

/**
 * A simple {@link Fragment} subclass.
 */
public class ProductNameFragment extends BaseFragment {
    public static final String PRODUCTNAME = "PRODUCTNAME";
    private String mParam1;
    private RecyclerView recyclerView;
    private LinearLayout emptyLayout;


    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if(recyclerView !=null){
            setRecyclerView(recyclerView,emptyLayout);
        }
    }

    public ProductNameFragment() {

    }

    public static ProductNameFragment newInstance(String key) {
        ProductNameFragment fragment = new ProductNameFragment();
        Bundle bundle = new Bundle();
        bundle.putString(PRODUCTNAME, key);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(PRODUCTNAME);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_product_name, container, false);
        recyclerView = (RecyclerView) v.findViewById(R.id.productview);
        emptyLayout = (LinearLayout) v.findViewById(R.id.empty_product);
        return v;
    }

    @Override
    public void onResume() {
        super.onResume();
        setRecyclerView(recyclerView,emptyLayout);
        Collections.sort(getStocks(),Stock.sortAscStockName);
        getAdapter().notifyDataSetChanged();
    }
}
