package com.market.application.adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.market.application.R;
import com.market.application.fragment.LowInStockFragment;
import com.market.application.fragment.LowSaleFragment;
import com.market.application.fragment.ProductPriceFragment;
import com.market.application.fragment.StockFragment;

/**
 * Created by barbie on 5/12/2017.
 */

public class SelectPageStockAdapter extends FragmentStatePagerAdapter {

    private Context context;
    public SelectPageStockAdapter(FragmentManager fm, Context context) {
        super(fm);
        this.context=context;
    }

    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case 0:
                return StockFragment.newInstance("0");
            case 1:
                return LowInStockFragment.newInstance("1");
            case 2:
                return LowSaleFragment.newInstance("2");
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return 3;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return context.getResources().getString(R.string.allproduct);
            case 1:
                return context.getResources().getString(R.string.lowinstock);
            case 2:
                return  context.getString(R.string.badsale);
            default:
                return null;
        }
    }
}
