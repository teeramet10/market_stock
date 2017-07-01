package com.market.application.fragment;


import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.market.application.R;
import com.market.application.SharePreferenceHelper;
import com.market.application.adapter.ProductAdapter;
import com.market.application.database.DatabaseManagement;
import com.market.application.database.SqliteHelper;
import com.market.application.javaclass.Stock;

import java.util.ArrayList;

public class LowInStockFragment extends BaseFragment {

    private static final String ARG_PARAM1 = "param1";
    private String mParam1;


    private RecyclerView recyclerView;
    private LinearLayout emptyLayout;
    Context context;

    public static LowInStockFragment newInstance(String param1) {
        LowInStockFragment fragment = new LowInStockFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if(recyclerView !=null){
            setRecyclerView(recyclerView,emptyLayout);
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);

        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context=context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v=inflater.inflate(R.layout.fragment_low_in_stock, container, false);
        recyclerView = (RecyclerView) v.findViewById(R.id.productview);
        emptyLayout = (LinearLayout) v.findViewById(R.id.empty_product);

        setRecyclerView(recyclerView,emptyLayout);


        return v;
    }

}
