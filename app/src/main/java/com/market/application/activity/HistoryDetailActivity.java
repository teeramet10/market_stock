package com.market.application.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.TextView;

import com.market.application.R;
import com.market.application.adapter.CartAdapter;
import com.market.application.database.DatabaseManagement;
import com.market.application.database.SqliteHelper;
import com.market.application.javaclass.DetailCart;
import com.market.application.javaclass.Order;
import com.market.application.javaclass.Product;
import com.market.application.javaclass.Stock;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class HistoryDetailActivity extends BaseActivity {
    SimpleDateFormat dateFormat=new SimpleDateFormat("MMM d, yyyy");
    private DecimalFormat decimalFormat = new DecimalFormat("#,###.00");

    ListView listViewCart;
    Context context = this;
    ArrayList<DetailCart> detailCartsNew;

    ArrayList<Order> orders;
    CartAdapter cartAdapter;

    TextView tvTotal;
    TextView tvCost;
    TextView tvProfit;
    TextView tvTitle;
    TextView tvDiscount;
    int idorder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history_detail);
        listViewCart = (ListView) findViewById(R.id.detailhistoryview);
        tvProfit= (TextView) findViewById(R.id.profit);
        tvCost= (TextView) findViewById(R.id.cost);
        tvTotal= (TextView) findViewById(R.id.total);
        tvTitle= (TextView) findViewById(R.id.tvtitle);
        tvDiscount= (TextView) findViewById(R.id.discount);

        Intent intent=getIntent();
        idorder=intent.getIntExtra(SqliteHelper.ORDER_ID,0);

        DatabaseManagement databaseManagement = new DatabaseManagement(context);
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
                + " where " + SqliteHelper.CART_TB + "." + SqliteHelper.ID_ORDER_F + "=" +idorder;

        String[] arg = new String[]{};

        ArrayList<DetailCart> detailCarts = databaseManagement.selectDetailCartOnHistory(sql, arg);

        String sqlorder ="select * from "+SqliteHelper.ORDER_TB+" where "+SqliteHelper.ORDER_ID+"="+idorder;
        orders =databaseManagement.selectOrder(sqlorder,null);
        databaseManagement.closeDatabase();

        float cost = 0;
        for (int k = 0; k < detailCarts.size(); k++) {
            cost += detailCarts.get(k).getDetail_cart_cost()*detailCarts.get(k).getDetail_cart_quality();
        }

        setItemListView(detailCarts);

        cartAdapter = new CartAdapter(context, detailCartsNew);
        listViewCart.setAdapter(cartAdapter);

        double total=orders.get(0).getOrder_total();
        double discount =orders.get(0).getDiscount();
        double profit=total-cost-discount;

        if (discount > 0) {
            tvDiscount.setText(decimalFormat.format(discount));
        }else {
            tvDiscount.setText(String.valueOf((int) discount));
        }

        tvTotal.setText(decimalFormat.format(total));
        tvCost.setText(decimalFormat.format(cost));

        if(profit>0){
            tvProfit.setTextColor(getResources().getColor(R.color.green));
        }else {
            tvProfit.setTextColor(getResources().getColor(R.color.rad));

        }
        tvProfit.setText(decimalFormat.format(profit));
        tvTitle.setText("Order ID : "+idorder+"  "+dateFormat.format(orders.get(0).getOrder_date()));
    }

    private void setItemListView(ArrayList<DetailCart> detailCarts){
        detailCartsNew =new ArrayList<>();
        DatabaseManagement db=new DatabaseManagement(context);
        ArrayList<Integer> product = db.selectIdProduct();
        db.closeDatabase();

        for (int i = 0; i < product.size(); i++) {
            DetailCart detailCart = new DetailCart();
            int idproduct = product.get(i);

            int qual = 0;
            double total = 0;

            for (int j = 0; j < detailCarts.size(); j++) {

                int id = detailCarts.get(j).getStock().getProduct().getId();
                if (id == idproduct) {

                    if (detailCart.getStock() == null) {
                        Stock stock=new Stock();
                        Product product1 = new Product();
                        product1.setId(id);
                        product1.setName(detailCarts.get(j).getStock().getProduct().getName());
                        product1.setPathimage(detailCarts.get(j).getStock().getProduct().getPathimage());
                        stock.setProduct(product1);
                        detailCart.setStock(stock);
                    }
                    total += detailCarts.get(j).getDetail_cart_total();
                    qual += detailCarts.get(j).getDetail_cart_quality();


                }
            }

            detailCart.setDetail_cart_total(total);
            detailCart.setDetail_cart_quality(qual);

            if (qual > 0) {
                detailCartsNew.add(detailCart);
            }
        }
    }
}
