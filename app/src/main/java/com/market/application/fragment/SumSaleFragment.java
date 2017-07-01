package com.market.application.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.market.application.R;
import com.market.application.activity.DetailSummarySaleActivity;
import com.market.application.activity.SumActivity;
import com.market.application.adapter.SumAdapter;
import com.market.application.database.DatabaseManagement;
import com.market.application.database.SqliteHelper;
import com.market.application.javaclass.Cart;
import com.market.application.javaclass.Order;
import com.market.application.javaclass.SummarySale;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class SumSaleFragment extends Fragment {
    private DecimalFormat decimalFormat = new DecimalFormat("#,###.00");
    Context context;
    Activity activity;
    ArrayList<SummarySale> summarySales;
    ArrayList<Order> orders;
    ListView listView;
    SumAdapter sumAdapter;

    TextView tvCost;
    TextView tvTotal;
    TextView tvProfit;
    TextView tvDiscount;

    private static final String ARG_PARAM1 = "param1";


    public static SumSaleFragment newInstance(int i) {
        SumSaleFragment fragment = new SumSaleFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_PARAM1, i);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sum_sale, container, false);

        listView = (ListView) view.findViewById(R.id.sumlist);
        tvCost = (TextView) view.findViewById(R.id.tvcost);
        tvTotal = (TextView) view.findViewById(R.id.total);
        tvProfit = (TextView) view.findViewById(R.id.tvprofit);
        tvDiscount = (TextView) view.findViewById(R.id.discount);

        if (activity instanceof SumActivity) {
            String date = ((SumActivity) activity).datesFormat.format(System.currentTimeMillis());
            readData(date);
            sumAdapter = new SumAdapter(summarySales, context);
            listView.setAdapter(sumAdapter);


            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    Intent intent = new Intent(context, DetailSummarySaleActivity.class);
                    intent.putExtra("ID", summarySales.get(i).getId());
                    intent.putExtra("DATE",((SumActivity) activity).date);
                    startActivity(intent);
                }
            });

        }

        return view;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        activity = (Activity) context;
        this.context = context;

    }

    public void readData(String date) {
        ArrayList<Cart> carts = null;
        String sql = "select * from " + SqliteHelper.CART_TB
                + " inner join " + SqliteHelper.DETAIL_CART_TB
                + " on " + SqliteHelper.CART_TB + "." + SqliteHelper.ID_CART_DETAIL + "="
                + SqliteHelper.DETAIL_CART_TB + "." + SqliteHelper.DETAIL_CART_ID
                + " inner join " + SqliteHelper.STOCK_TB
                + " on " + SqliteHelper.STOCK_TB + "." + SqliteHelper.STOCK_ID + "="
                + SqliteHelper.DETAIL_CART_TB + "." + SqliteHelper.ID_PRODUCT_STOCK
                + " inner join " + SqliteHelper.PRODUCT_TB
                + " on " + SqliteHelper.PRODUCT_TB + "." + SqliteHelper.PRODUCT_ID + "="
                + SqliteHelper.STOCK_TB + "." + SqliteHelper.ID_PRODUCT
                + " inner join " + SqliteHelper.ORDER_TB
                + " on " + SqliteHelper.CART_TB + "." + SqliteHelper.ID_ORDER_F + "="
                + SqliteHelper.ORDER_TB + "." + SqliteHelper.ORDER_ID
                + " where date(" + SqliteHelper.ORDER_TB + "." + SqliteHelper.ORDER_DATE + "/1000,'unixepoch','localtime') ='" + date
                + "' and " + SqliteHelper.ORDER_TB + "." + SqliteHelper.ID_STATUS + "=2";

        String sqlorder = "select * from " + SqliteHelper.ORDER_TB
                + " where date(" + SqliteHelper.ORDER_DATE + "/1000,'unixepoch','localtime') ='" + date
                + "' and " + SqliteHelper.ID_STATUS + "=2";

        DatabaseManagement db = new DatabaseManagement(context);
        carts = db.selectAllCart(sql, null);
        ArrayList<Integer> products = db.selectIdProduct();
        db.closeDatabase();
        orders = db.selectOrder(sqlorder, null);

        if (summarySales == null) {
            setSummarySales(carts, products);
        } else {
            summarySales.clear();
            setSummarySales(carts, products);
            sumAdapter.setSummarySales(summarySales);
            sumAdapter.notifyDataSetChanged();
        }

        setTextData();

    }

    public void setTextData() {
        double cost = 0;
        double total = 0;
        double profit = 0;
        double discount = 0;

        for (int i = 0; i < summarySales.size(); i++) {
            cost += summarySales.get(i).getCost();
            total += summarySales.get(i).getTotal();
        }

        for (int i = 0; i < orders.size(); i++) {
            discount += orders.get(i).getDiscount();
        }

        profit = total - cost - discount;
        if (total > 0) {
            tvDiscount.setText(decimalFormat.format(discount));
            tvCost.setText(decimalFormat.format(cost));
            tvTotal.setText(decimalFormat.format(total));
            tvProfit.setText(decimalFormat.format(profit));
        }else{
            tvDiscount.setText(String.valueOf((int)discount));
            tvCost.setText(String.valueOf((int)cost));
            tvTotal.setText(String.valueOf((int)total));
            tvProfit.setText(String.valueOf((int)profit));
        }
    }
    private void setSummarySales(ArrayList<Cart> carts, ArrayList<Integer> products) {
        summarySales = new ArrayList<>();
        SummarySale summarySale;
        if (carts.size() != 0) {
            for (int i = 0; i < products.size(); i++) {
                summarySale = new SummarySale();
                int id = products.get(i);

                int qual = 0;
                int quality =0;
                double total = 0;
                double cost = 0;
                double profit;

                for (int j = 0; j < carts.size(); j++) {
                    int productid = carts.get(j).getDetailCart().getStock().getProduct().getId();

                    if (productid == id) {
                        if (summarySale.getName() == null && summarySale.getPath() == null) {
                            summarySale.setName(carts.get(j).getDetailCart().getStock().getProduct().getName());
                            summarySale.setPath(carts.get(j).getDetailCart().getStock().getProduct().getPathimage());
                            summarySale.setId(carts.get(j).getDetailCart().getStock().getProduct().getId());
                        }
                        quality+= carts.get(j).getDetailCart().getDetail_cart_quality();
                        qual = carts.get(j).getDetailCart().getDetail_cart_quality();
                        total += (carts.get(j).getDetailCart().getDetail_cart_price() * qual);
                        cost += (carts.get(j).getDetailCart().getDetail_cart_cost() * qual);


                    }

                }

                profit = total - cost;
                summarySale.setValue(quality);
                summarySale.setCost(cost);
                summarySale.setTotal(total);
                summarySale.setProfit(profit);
                if (qual > 0) {
                    summarySales.add(summarySale);

                }
            }
        }
    }

}
