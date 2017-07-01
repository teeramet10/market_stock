package com.market.application.fragment;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import com.market.application.R;
import com.market.application.activity.ProductStockActivity;
import com.market.application.adapter.TypeAdapter;
import com.market.application.database.DatabaseManagement;
import com.market.application.database.SqliteHelper;
import com.market.application.javaclass.Type;

import java.util.ArrayList;


public class StockFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    GridView typeview;
    TypeAdapter typeAdapter;
    ArrayList<Type> typeArrayList;
    LayoutInflater inflater;
    Context context;

    private String mParam1;

    public StockFragment() {
        // Required empty public constructor
    }

    public static StockFragment newInstance(String param1) {
        StockFragment fragment = new StockFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v=inflater.inflate(R.layout.fragment_stock, container, false);
        typeview= (GridView) v.findViewById(R.id.catview);


        setGridView();

        typeview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent=new Intent(context,ProductStockActivity.class);
                intent.putExtra("ID_TYPE",typeArrayList.get(i).getId());
                intent.putExtra("NAME",typeArrayList.get(i).getName());
                startActivity(intent);
            }
        });


        return v;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context=context;
    }

    private void setGridView(){
        DatabaseManagement db = new DatabaseManagement(context);
        String sql = "select * from " + SqliteHelper.TYPE_TB + " where " + SqliteHelper.ID_DISABLE + "=?";
        typeArrayList = db.selectType(sql, new String[]{String.valueOf(1)});
        db.closeDatabase();
        typeAdapter = new TypeAdapter(context, typeArrayList);
        typeview.setAdapter(typeAdapter);

        inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.empty_product, null);

        ViewGroup viewGroup = (ViewGroup) typeview.getParent();
        viewGroup.addView(view);
        typeview.setEmptyView(view);
    }
}
