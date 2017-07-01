package com.market.application.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.market.application.R;
import com.market.application.adapter.DetailProductViewAdapter;
import com.market.application.database.DatabaseManagement;
import com.market.application.database.SqliteHelper;
import com.market.application.javaclass.Stock;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.ArrayList;

public class DetailProductActivity extends BaseActivity {
    Context context = this;
    Activity activity = DetailProductActivity.this;

    Toolbar toolbar;
    FloatingActionButton fabEdit;
    ImageButton imgAddpic;
    TextView tvName;
    TextView tvPrice;
    TextView tvQuality;
    TextView tvType;
    TextView tvTitle;
    ListView listView;
    int id = 1;

    ArrayList<Stock> stocks;
    DetailProductViewAdapter detailAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_product);


        tvQuality = (TextView) findViewById(R.id.tvquality);
        tvPrice = (TextView) findViewById(R.id.tvprice);
        tvName = (TextView) findViewById(R.id.tvname);
        tvType = (TextView) findViewById(R.id.tvtype);
        tvTitle = (TextView) findViewById(R.id.tvtitle);
        imgAddpic = (ImageButton) findViewById(R.id.addpic);
        fabEdit = (FloatingActionButton) findViewById(R.id.fabedit);
        listView = (ListView) findViewById(R.id.detailproductview);
        toolbar = (Toolbar) findViewById(R.id.toolbar);

        final Intent intent = getIntent();
        id = intent.getIntExtra(SqliteHelper.ID_PRODUCT, 0);

        fabEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(context, EditProductActivity.class);
                intent1.putExtra("stockid", stocks.get(0).getStockid());
                intent1.putExtra("productid", stocks.get(0).getProduct().getId());
                startActivity(intent1);

            }
        });
    }


    private void setComponent() {
        tvTitle.setText(stocks.get(stocks.size() - 1).getProduct().getName());

        tvName.setText(getResources().getString(R.string.name_product) + " : "
                + stocks.get(stocks.size() - 1).getProduct().getName());
        tvPrice.setText(getResources().getString(R.string.pricesale) + " : "
                + String.valueOf((int)stocks.get(stocks.size() - 1).getProduct_price()));
        tvType.setText(getResources().getString(R.string.type) + " : "
                + stocks.get(stocks.size() - 1).getProduct().getType().getName());

        int qual = 0;
        for (int i = 0; i < stocks.size(); i++) {
            qual += stocks.get(i).getProduct_quality();
        }
        tvQuality.setText(getResources().getString(R.string.value) + " : " + String.valueOf(qual));
        String pic = stocks.get(stocks.size() - 1).getProduct().getPathimage();

        if (pic != null) {
            File file = new File(pic);
            if (file.exists()) {
                Picasso.with(context).load(file).fit().centerCrop().error(R.drawable.pack).into(imgAddpic);
            }
        } else {
            Picasso.with(context).load(R.drawable.pack).fit().centerCrop().into(imgAddpic);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        DatabaseManagement db = new DatabaseManagement(context);
        String sql = "select * from " + SqliteHelper.STOCK_TB
                + " inner join " + SqliteHelper.PRODUCT_TB
                + " on " + SqliteHelper.PRODUCT_TB + "." + SqliteHelper.PRODUCT_ID
                + "=" + SqliteHelper.STOCK_TB + "." + SqliteHelper.ID_PRODUCT
                + " inner join " + SqliteHelper.TYPE_TB
                + " on " + SqliteHelper.TYPE_TB + "." + SqliteHelper.TYPE_ID
                + "=" + SqliteHelper.PRODUCT_TB + "." + SqliteHelper.ID_TYPE
                + " where " + SqliteHelper.PRODUCT_TB + "." + SqliteHelper.ID_DISABLE + "=1"
                + " and " + SqliteHelper.STOCK_TB + "." + SqliteHelper.ID_PRODUCT + "=" + id
//                + " and " + SqliteHelper.STOCK_TB + "." + SqliteHelper.PRODUCT_QUALITY + ">0"
                + " order by " + SqliteHelper.STOCK_TB + "." + SqliteHelper.DATE_FIRST_IN + " DESC";
        stocks = db.selectStock(sql, null);

        setComponent();

        detailAdapter = new DetailProductViewAdapter(context, stocks);
        listView.setAdapter(detailAdapter);
        db.closeDatabase();
    }
}
