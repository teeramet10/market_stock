package com.market.application.activity;

import android.app.DatePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.DatePicker;
import android.widget.ImageButton;

import com.market.application.R;
import com.market.application.adapter.SumPagerAdapter;
import com.market.application.fragment.SumSaleFragment;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class SumActivity extends BaseActivity {
    public SimpleDateFormat datesFormat = new SimpleDateFormat("yyyy-MM-dd");
    Context context = this;
    ViewPager viewPager;
    SumPagerAdapter sumPagerAdapter;
    TabLayout tabLayout;
    ImageButton imbCalendar;
    int dates;
    public String date;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sum);
        viewPager = (ViewPager) findViewById(R.id.sumpager);
        tabLayout = (TabLayout) findViewById(R.id.tabs);
        imbCalendar = (ImageButton) findViewById(R.id.btncalendar);
        sumPagerAdapter = new SumPagerAdapter(getSupportFragmentManager(),context);
        viewPager.setAdapter(sumPagerAdapter);
        tabLayout.setupWithViewPager(viewPager);

        Calendar cal=Calendar.getInstance();
        dates=cal.get(Calendar.DAY_OF_MONTH);
        date = datesFormat.format(System.currentTimeMillis());

        imbCalendar.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Calendar cal =Calendar.getInstance();
                        DatePickerDialog datePickerDialog = new DatePickerDialog(context, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                        Calendar calendar=Calendar.getInstance();
                        calendar.set(i,i1,i2);

                        date = datesFormat.format(calendar.getTime());
                        SumSaleFragment saleFragment = (SumSaleFragment) getSupportFragmentManager().getFragments().get(0);

                        dates=i2;

                        if(saleFragment instanceof  SumSaleFragment){
                            saleFragment.readData(date);
                        }
                    }
                }, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH),dates );

                datePickerDialog.show();
            }
        });

    }




}
