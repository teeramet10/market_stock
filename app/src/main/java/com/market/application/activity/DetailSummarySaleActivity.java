package com.market.application.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.market.application.R;
import com.market.application.adapter.DetailSummarySaleAdapter;
import com.market.application.database.DatabaseManagement;
import com.market.application.database.SqliteHelper;
import com.market.application.javaclass.DetailCart;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.text.DecimalFormat;
import java.util.ArrayList;

public class DetailSummarySaleActivity extends BaseActivity {
    private DecimalFormat decimalFormat = new DecimalFormat("#,###.00");
    Context context = this;
    ArrayList<DetailCart> detailCarts;

    TextView tvName;
    TextView tvPrice;
    TextView tvTotal;
    TextView tvCost;
    TextView tvProfit;
    TextView tvTitle;
    ImageView imgProduct;
    ListView listView;
    DetailSummarySaleAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_summary_sale);

        tvName = (TextView) findViewById(R.id.tvname);
        tvPrice = (TextView) findViewById(R.id.tvprice);
        tvProfit = (TextView) findViewById(R.id.profit);
        tvCost = (TextView) findViewById(R.id.cost);
        tvTotal = (TextView) findViewById(R.id.total);
        tvTitle = (TextView) findViewById(R.id.tvtitle);
        listView = (ListView) findViewById(R.id.listproduct);
        imgProduct = (ImageView) findViewById(R.id.imgproduct);

        Intent intent = getIntent();
        int id = intent.getIntExtra("ID", 0);
        String date = intent.getStringExtra("DATE");

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
                + "' and " + SqliteHelper.ORDER_TB + "." + SqliteHelper.ID_STATUS + "=2"
                + " and " + SqliteHelper.STOCK_TB + "." + SqliteHelper.ID_PRODUCT + "=" + id;

        DatabaseManagement db = new DatabaseManagement(context);
        detailCarts = db.selectDetailCartWithProduct(sql, null);

        tvName.setText(getString(R.string.name_product)+" : "+detailCarts.get(0).getStock().getProduct().getName());
        tvTitle.setText(detailCarts.get(0).getStock().getProduct().getName());
        tvPrice.setText(getString(R.string.pricesale)+" : "+String.valueOf((int)detailCarts.get(0).getDetail_cart_price()));

        if (detailCarts.get(0).getStock().getProduct().getPathimage() != null) {
            File file = new File(detailCarts.get(0).getStock().getProduct().getPathimage());
            if (file.exists()) {
                Picasso.with(context).load(file).fit().centerCrop().error(R.drawable.pack).into(imgProduct);
            }
        } else {
            Picasso.with(context).load(R.drawable.pack).fit().centerCrop().into(imgProduct);
        }


        double price = 0;
        double cost = 0;

        for (int i = 0; i < detailCarts.size(); i++) {
            double qual=detailCarts.get(i).getDetail_cart_quality();
            price += (detailCarts.get(i).getDetail_cart_price()*qual);
            cost += (detailCarts.get(i).getDetail_cart_cost()*qual);

        }

        tvCost.setText(decimalFormat.format(cost));
        tvTotal.setText(decimalFormat.format(price));
        tvProfit.setText(decimalFormat.format(price - cost));

        adapter = new DetailSummarySaleAdapter(detailCarts, context);
        listView.setAdapter(adapter);
    }
}
