package com.market.application.fragment;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.market.application.R;
import com.market.application.database.DatabaseManagement;
import com.market.application.database.SqliteHelper;
import com.market.application.javaclass.DetailCart;
import com.market.application.javaclass.Order;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class ChartFragment extends Fragment {

    private static final String ARG_PARAM2 = "param2";

    SimpleDateFormat dateFormat = new SimpleDateFormat("MM");

    public String[] MONTH = new String[12];

    Context context;
    BarChart barChart;


    public static ChartFragment newInstance(int i) {
        ChartFragment fragment = new ChartFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_PARAM2, i);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_chart, container, false);
        addMonth();

        barChart = (BarChart) view.findViewById(R.id.chart);
        barChart.setBackgroundColor(Color.WHITE);
        barChart.getDescription().setEnabled(false);

        XAxis xAxis = barChart.getXAxis();
        xAxis.setDrawLabels(true);

        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawGridLines(false);
        xAxis.setDrawAxisLine(false);
        xAxis.setTextColor(Color.LTGRAY);
        xAxis.setTextSize(13f);
        barChart.getLegend().setEnabled(true);
        barChart.setPinchZoom(false);

        YAxis left = barChart.getAxisLeft();
        left.setDrawLabels(true);
        left.setSpaceTop(25f);
        left.setDrawTopYLabelEntry(false);
        left.setSpaceBottom(25f);
        left.setDrawAxisLine(true);
        left.setDrawGridLines(true);
        left.setDrawZeroLine(true); // draw a zero line
        left.setZeroLineColor(Color.GRAY);
        left.setZeroLineWidth(0.7f);
        barChart.getAxisRight().setEnabled(false);
        barChart.getLegend().setEnabled(false);

        List<Data> datas = selectTotal();

        xAxis.setValueFormatter(new IAxisValueFormatter() {

            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                return MONTH[(int) value];
            }


        });

        setData(datas);

        return view;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;

    }


    public void addMonth() {
        MONTH[0] = getResources().getString(R.string.jan);
        MONTH[1] = getResources().getString(R.string.feb);
        MONTH[2] = getResources().getString(R.string.march);
        MONTH[3] = getResources().getString(R.string.april);
        MONTH[4] = getResources().getString(R.string.may);
        MONTH[5] = getResources().getString(R.string.june);
        MONTH[6] = getResources().getString(R.string.july);
        MONTH[7] = getResources().getString(R.string.aug);
        MONTH[8] = getResources().getString(R.string.sep);
        MONTH[9] = getResources().getString(R.string.oct);
        MONTH[10] = getResources().getString(R.string.nov);
        MONTH[11] = getResources().getString(R.string.dec);

    }

    private List<Data> selectTotal() {
        List<Data> datas = new ArrayList<>();

        String sql = "select * from " + SqliteHelper.ORDER_TB + " where " + SqliteHelper.ID_STATUS + "=2";

        String[] arg = {};
        DatabaseManagement db = new DatabaseManagement(context);
        ArrayList<Order> orders = db.selectOrder(sql, arg);

        db.closeDatabase();
        //TODO ADDLIST CHART

        for (int i = 0; i < getMonth(); i++) {
            for (int j = 0; j < orders.size(); j++) {
                Date date = new Date(orders.get(j).getOrder_date());
                int valuedate = Integer.valueOf(dateFormat.format(date));

                String sqlcart = "select * from " + SqliteHelper.DETAIL_CART_TB + " inner join " + SqliteHelper.CART_TB
                        + " on " + SqliteHelper.DETAIL_CART_ID + "=" + SqliteHelper.ID_CART_DETAIL
                        + " inner join " + SqliteHelper.ORDER_TB + " on " + SqliteHelper.ORDER_ID + "=" + SqliteHelper.ID_ORDER_F
                        + " where "+SqliteHelper.ORDER_ID+"="+orders.get(j).getOrder_id();


                if ((valuedate - 1) == i) {
                    ArrayList<DetailCart> detailCarts = db.selectDetailCart(sqlcart, null);
                    if (datas.size() <= i) {
                        float total = (float) orders.get(j).getOrder_total();
                        float discount = (float) orders.get(j).getDiscount();
                        float cost = 0;

                        for (int k = 0; k < detailCarts.size(); k++) {
                            int qual =detailCarts.get(k).getDetail_cart_quality();;
                            cost += detailCarts.get(k).getDetail_cart_cost()*qual;
                        }

                        float profit = (total - cost - discount);
                        datas.add(new Data(i, profit, MONTH[i]));
                    } else {
                        float data = datas.get(i).getyValue();
                        float cost = 0;
                        for (int k = 0; k < detailCarts.size(); k++) {
                            int qual =detailCarts.get(k).getDetail_cart_quality();
                            cost += detailCarts.get(k).getDetail_cart_cost()*qual;
                        }

                        data += (orders.get(j).getOrder_total() - cost-orders.get(j).getDiscount());
                        datas.get(i).setyValue(data);
                    }
                }

            }
            if (datas.size() <= i) {
                datas.add(new Data(i, 0, MONTH[i]));
            }
        }


        return datas;
    }

    public int getMonth() {
        int monthofyear = 12;
        return monthofyear;
    }

    private void setData(List<Data> datas) {
        ArrayList<BarEntry> barEntries = new ArrayList<>();
        List<Integer> colors = new ArrayList<Integer>();

        int green = Color.rgb(110, 190, 102);
        int red = Color.rgb(211, 74, 88);

        for (int i = 0; i < datas.size(); i++) {


            Data data = datas.get(i);
            BarEntry entry = new BarEntry(data.xValue, data.yValue);
            barEntries.add(entry);

            if (data.yValue >= 0)
                colors.add(green);
            else
                colors.add(red);


            BarDataSet barDataSet;

            if (barChart.getData() != null && barChart.getData().getDataSetCount() > 0) {
                barDataSet = (BarDataSet) barChart.getData().getDataSetByIndex(0);
                barDataSet.setValues(barEntries);


//                barChart.zoomAndCenterAnimated(8f,0f,3f,0f,YAxis.AxisDependency.LEFT,1000);
                barChart.getBarData().setDrawValues(true);
                barChart.getBarData().setBarWidth(0.8f);
                barChart.getXAxis().setTextColor(R.color.black);
                barChart.getXAxis().setLabelRotationAngle(-75f);
                barChart.setExtraBottomOffset(20f);
                barChart.zoom(1.05f, 1f, 3f, 0f);
                barChart.getData().notifyDataChanged();
                barChart.invalidate();
            } else {
                barDataSet = new BarDataSet(barEntries, getResources().getString(R.string.profits));
                barDataSet.setColors(colors);

                BarData data1 = new BarData(barDataSet);
                data1.setValueTextSize(13f);
                data1.setBarWidth(0.5f);


                barChart.setData(data1);
                barChart.invalidate();
            }
        }
    }

    private class Data {

        private String xAxisValue;
        private float yValue;
        private float xValue;

        public Data(float xValue, float yValue, String xAxisValue) {
            this.xAxisValue = xAxisValue;
            this.yValue = yValue;
            this.xValue = xValue;
        }

        public String getxAxisValue() {
            return xAxisValue;
        }

        public void setxAxisValue(String xAxisValue) {
            this.xAxisValue = xAxisValue;
        }

        public float getyValue() {
            return yValue;
        }

        public void setyValue(float yValue) {
            this.yValue = yValue;
        }

        public float getxValue() {
            return xValue;
        }

        public void setxValue(float xValue) {
            this.xValue = xValue;
        }
    }


}
