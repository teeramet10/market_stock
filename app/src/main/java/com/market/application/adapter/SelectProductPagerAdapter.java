package com.market.application.adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.market.application.R;
import com.market.application.fragment.ProductNameFragment;
import com.market.application.fragment.ProductPriceFragment;
import com.market.application.fragment.ProductQualFragment;

/**
 * Created by barbie on 5/12/2017.
 */

public class SelectProductPagerAdapter extends FragmentStatePagerAdapter {
    private Context context;
    private ProductNameFragment nameFragment;
    private ProductPriceFragment priceFragment;
    private ProductQualFragment qualFragment;


    public SelectProductPagerAdapter(FragmentManager fm, Context context) {
        super(fm);
        this.context = context;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                nameFragment = ProductNameFragment.newInstance("0");
                return nameFragment;
            case 1:
                priceFragment = ProductPriceFragment.newInstance("1");
                return priceFragment;
            case 2:
                qualFragment = ProductQualFragment.newInstance("2");
                return qualFragment;

            default:
                return null;
        }
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return context.getResources().getString(R.string.sortname);
            case 1:
                return context.getResources().getString(R.string.sortprice);
            case 2:
                return context.getResources().getString(R.string.sortqual);
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return 3;
    }


    public Fragment getProductNameFragment(){
        return nameFragment;
    }

    public Fragment getProductPriceFragment(){
        return priceFragment;
    }

    public Fragment getProductQualFragment(){
        return qualFragment;
    }
}
