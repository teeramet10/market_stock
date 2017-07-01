package com.market.application.activity;

import android.content.Context;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.os.Bundle;

import com.market.application.R;
import com.market.application.adapter.SelectPageStockAdapter;

public class StockActivity extends BaseActivity {
    Context context = this;
    ViewPager stockPager;
    TabLayout tabLayout;
    SelectPageStockAdapter spAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stock);
        stockPager = (ViewPager) findViewById(R.id.stockpager);
        tabLayout = (TabLayout) findViewById(R.id.tablayout);

        spAdapter=new SelectPageStockAdapter(getSupportFragmentManager(),context);
        stockPager.setAdapter(spAdapter);
        tabLayout.setupWithViewPager(stockPager);
    }

}
