package com.market.application.activity;

import android.content.Context;
import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.widget.TextView;

import com.market.application.R;
import com.market.application.adapter.SelectProductPagerAdapter;
import com.market.application.fragment.BaseFragment;
import com.market.application.fragment.ProductNameFragment;
import com.market.application.fragment.ProductPriceFragment;
import com.market.application.fragment.ProductQualFragment;
import com.market.application.javaclass.Stock;

import java.util.Collections;

public class ProductStockActivity extends BaseActivity {
    public static final String[] menu = new String[]{"",""};

    ViewPager productPager;
    TabLayout tabLayout;
    SelectProductPagerAdapter selectProductPagerAdapter;
    TextView tvTitle;

    Context context=this;
    public int idType;
    String title;
    int countname = 0;
    int countprice = 0;
    int countqual = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_stock);

        productPager = (ViewPager) findViewById(R.id.productpager);
        tabLayout = (TabLayout) findViewById(R.id.tabbar);
        tvTitle = (TextView) findViewById(R.id.tvtitle);

        Intent intent = getIntent();
        idType =intent.getIntExtra("ID_TYPE",0);
        title =intent.getStringExtra("NAME");

        tvTitle.setText(title);

        selectProductPagerAdapter = new SelectProductPagerAdapter(getSupportFragmentManager(), this);
        productPager.setAdapter(selectProductPagerAdapter);
        tabLayout.setupWithViewPager(productPager);

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                int i = tab.getPosition();
                if (i == 0) {
                    ProductNameFragment fragment = (ProductNameFragment) selectProductPagerAdapter.getProductNameFragment();
                    if(countname%2==0) {
                        Collections.sort(fragment.getStocks(), Stock.sortDescStockName);
                    }else {
                        Collections.sort(fragment.getStocks(), Stock.sortAscStockName);
                    }
                    fragment.getAdapter().notifyDataSetChanged();
                    countname++;
                } else if (i == 1) {
                    ProductPriceFragment fragment = (ProductPriceFragment) selectProductPagerAdapter.getProductPriceFragment();
                    if(countprice%2==0) {
                        Collections.sort(fragment.getStocks(), Stock.sortAscStockPrice);
                    }else {
                        Collections.sort(fragment.getStocks(), Stock.sortDescStockPrice);
                    }
                    fragment.getAdapter().notifyDataSetChanged();
                    countprice++;
                } else if (i == 2) {
                    ProductQualFragment fragment = (ProductQualFragment) selectProductPagerAdapter.getProductQualFragment();
                    if(countqual%2==0) {
                        Collections.sort(fragment.getStocks(), Stock.sortAscStockQuality);

                    }else{
                        Collections.sort(fragment.getStocks(), Stock.sortDescStockQuality);
                    }
                    fragment.getAdapter().notifyDataSetChanged();
                    countqual++;
                }
            }
        });
    }
}
