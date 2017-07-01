package com.market.application.adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.market.application.R;
import com.market.application.fragment.ChartFragment;
import com.market.application.fragment.SumSaleFragment;

/**
 * Created by barbie on 5/5/2017.
 */

public class SumPagerAdapter  extends FragmentStatePagerAdapter{

    Context context;

    public SumPagerAdapter(FragmentManager fm, Context context) {
        super(fm);
        this.context=context;
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return SumSaleFragment.newInstance(0);
            case 1:
                return ChartFragment.newInstance(1);

            default:
                return null;
        }
    }


    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return context.getString(R.string.sumprice);
            case 1:
                return context.getString(R.string.graph);

            default:
                return null;
        }


    }
}
