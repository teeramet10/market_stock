package com.market.application.fragment;


import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.market.application.R;
import com.market.application.javaclass.Stock;

import java.util.Collections;
import java.util.Comparator;


public class ProductPriceFragment extends BaseFragment {
    public static final String PRODUCTPRICE ="PRODUCTPRICE";
    private String mParam1;
    private RecyclerView recyclerView;
    private LinearLayout emptyLayout;

    public ProductPriceFragment() {

    }
    public static ProductPriceFragment newInstance(String key){
        ProductPriceFragment fragment=new ProductPriceFragment();
        Bundle bundle=new Bundle();
        bundle.putString(PRODUCTPRICE,key);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(PRODUCTPRICE);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v=inflater.inflate(R.layout.fragment_product_price, container, false);
        recyclerView = (RecyclerView) v.findViewById(R.id.productview);
        emptyLayout = (LinearLayout) v.findViewById(R.id.empty_product);

        return v;
    }

    @Override
    public void onResume() {
        super.onResume();
        setRecyclerView(recyclerView,emptyLayout);
        Collections.sort(getStocks(), (Comparator<? super Stock>) Stock.sortDescStockPrice);
        getAdapter().notifyDataSetChanged();
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if(isVisibleToUser) {
            if (recyclerView != null) {
                setRecyclerView(recyclerView, emptyLayout);
            }
        }
    }
}
