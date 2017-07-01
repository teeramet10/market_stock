package com.market.application.fragment;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.market.application.R;
import com.market.application.javaclass.Product;
import com.market.application.javaclass.Stock;

import java.util.Collections;

public class ProductQualFragment extends BaseFragment {

    public static final String PRODUCTQUAL = "PRODUCTQUAL";
    private String mParam1;

    private RecyclerView recyclerView;
    private LinearLayout emptyLayout;

    public static ProductQualFragment newInstance(String param1) {
        ProductQualFragment fragment = new ProductQualFragment();
        Bundle args = new Bundle();
        args.putString(PRODUCTQUAL, param1);

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(PRODUCTQUAL);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_product_date, container, false);


        recyclerView = (RecyclerView) v.findViewById(R.id.productview);
        emptyLayout = (LinearLayout) v.findViewById(R.id.empty_product);

        return v;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if(recyclerView !=null){
            setRecyclerView(recyclerView,emptyLayout);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        setRecyclerView(recyclerView, emptyLayout);
        Collections.sort(getStocks(), Stock.sortDescStockQuality);
        getAdapter().notifyDataSetChanged();
    }
}
