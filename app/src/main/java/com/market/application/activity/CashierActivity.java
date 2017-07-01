package com.market.application.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.market.application.R;
import com.market.application.SharePreferenceHelper;
import com.market.application.adapter.CashierProductAdapter;
import com.market.application.database.DatabaseManagement;
import com.market.application.database.SqliteHelper;
import com.market.application.javaclass.Cart;
import com.market.application.javaclass.DetailCart;
import com.market.application.javaclass.Order;
import com.market.application.javaclass.Stock;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class CashierActivity extends BaseActivity {
    DecimalFormat decimalFormat = new DecimalFormat("#,###");

    private ListView listViewProduct;
    private Button btnFinish;
    private TextView tvDiscount;
    private TextView tvTotal;
    private EditText edtSum;
    public Context context = this;

    double discount = 0;
    double total;
    double profit;
    double costs = 0;

    public ArrayList<DetailCart> detailCarts;
    DatabaseManagement databaseManagement;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cashier);
        listViewProduct = (ListView) findViewById(R.id.listproduct);
        tvDiscount = (TextView) findViewById(R.id.discount);
        tvTotal = (TextView) findViewById(R.id.total);
        edtSum = (EditText) findViewById(R.id.edtsum);
        btnFinish = (Button) findViewById(R.id.btnfinish);


        SharePreferenceHelper sp = new SharePreferenceHelper(context);
        databaseManagement = new DatabaseManagement(context);
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
                + " where " + SqliteHelper.CART_TB + "." + SqliteHelper.ID_ORDER_F + "=" + sp.getOrderSharePreference();

        String[] arg = new String[]{};
        detailCarts = databaseManagement.selectDetailCartOnCart(sql, arg);
        databaseManagement.closeDatabase();

        CashierProductAdapter cashierProductAdapter = new CashierProductAdapter(context, detailCarts);
        listViewProduct.setAdapter(cashierProductAdapter);


        for (int i = 0; i < detailCarts.size(); i++) {
            total += detailCarts.get(i).getDetail_cart_total();
        }


        profit = total - discount;

        tvTotal.setText(decimalFormat.format(total));
        tvDiscount.setText(decimalFormat.format((int)discount));
        edtSum.setText(decimalFormat.format(profit));

        edtSum.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                if (!charSequence.toString().equals("")) {
                    String edt = charSequence.toString();

                    double newSum = Double.parseDouble(edt.replace(",", ""));

                    tvDiscount.setText(String.valueOf((int)newSum - (int)total));
                    double dcount = Double.valueOf(tvDiscount.getText().toString().replace(",", ""));
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        btnFinish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!edtSum.getText().toString().equals("")) {

                    if (Double.valueOf(edtSum.getText().toString().replace(",", "")) <=
                            Double.valueOf(tvTotal.getText().toString().replace(",", ""))) {

                        new RunBackground().execute();

                    } else {
                        Toast.makeText(context, "กรุณากรอกข้อมูลให้ถูกต้อง", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

    private void updateTotalAndDiscount() {

        DatabaseManagement db = new DatabaseManagement(context);
        double total =Double.valueOf(edtSum.getText().toString().replace(",", ""));
        double discount =Math.abs(Double.valueOf(tvDiscount.getText().toString().replace(",", "")));
        db.updateTotalOnOrder(total,discount);
        db.closeDatabase();

        SharePreferenceHelper sp = new SharePreferenceHelper(context);
        sp.delOrderSharePreference();

    }

    private ArrayList<DetailCart> decreaseStock() {
        DatabaseManagement db = new DatabaseManagement(context);
        ArrayList<DetailCart> detailCartlist = new ArrayList<>();
        for (int i = 0; i < detailCarts.size(); i++) {
            int id = detailCarts.get(i).getStock().getProduct().getId();
            ArrayList<Stock> stocks;
            String sql = "select * from " + SqliteHelper.STOCK_TB
                    + " inner join " + SqliteHelper.PRODUCT_TB
                    + " on " + SqliteHelper.PRODUCT_TB + "." + SqliteHelper.PRODUCT_ID
                    + " = " + SqliteHelper.STOCK_TB + "." + SqliteHelper.ID_PRODUCT
                    + " inner join " + SqliteHelper.TYPE_TB
                    + " on " + SqliteHelper.PRODUCT_TB + "." + SqliteHelper.ID_TYPE
                    + " = " + SqliteHelper.TYPE_TB + "." + SqliteHelper.TYPE_ID
                    + " where " + SqliteHelper.STOCK_TB + "." + SqliteHelper.ID_PRODUCT + "=" + id
                    + " and " + SqliteHelper.STOCK_TB + "." + SqliteHelper.PRODUCT_QUALITY + ">0"
                    + " order by " + SqliteHelper.STOCK_TB + "." + SqliteHelper.DATE_FIRST_IN + " asc";
            stocks = db.selectStock(sql, null);
            db.closeDatabase();
            if (stocks.size() != 0) {
                int qualincart = detailCarts.get(i).getDetail_cart_quality();
                while (qualincart != 0) {
                    for (int j = 0; j < stocks.size(); j++) {
                        int qualinstock = stocks.get(j).getProduct_quality();
                        if (qualincart > 0) {
                            if (qualincart > qualinstock) {
                                //แสดงว่าสินค้าในตระกร้า เยอะกว่าหรือเท่ากับสินค้าใน stock ต้องหักสต๊อกนั้นทิ้งหมด

                                DetailCart detailCart = new DetailCart();
                                detailCart.setStock(stocks.get(j));
                                detailCart.setDetail_cart_quality(qualinstock);
                                detailCart.setDetail_cart_cost(stocks.get(j).getProduct_cost());
                                detailCart.setDetail_cart_price(stocks.get(j).getProduct_price());
                                detailCart.setDetail_cart_total(stocks.get(j).getProduct_price() * qualinstock);
                                detailCartlist.add(detailCart);

                                db.deleteQualInStock(0, detailCart.getStock().getStockid());
                                db.closeDatabase();
                                qualincart -= qualinstock;
                            } else {
                                //แสดงว่าในสินค้าใน stock เยอะกว่าในตระกร้า ต้องเอาสินค้าในตระกร้าไปหัก

                                DetailCart detailCart = new DetailCart();
                                detailCart.setStock(stocks.get(j));
                                detailCart.setDetail_cart_id(detailCarts.get(i).getDetail_cart_id());
                                detailCart.setDetail_cart_quality(qualincart);
                                detailCart.setDetail_cart_cost(stocks.get(j).getProduct_cost());
                                detailCart.setDetail_cart_price(stocks.get(j).getProduct_price());
                                detailCart.setDetail_cart_total(stocks.get(j).getProduct_price() * qualincart);
                                detailCartlist.add(detailCart);

                                qualinstock -= qualincart;
                                db.deleteQualInStock(qualinstock, detailCart.getStock().getStockid());
                                db.closeDatabase();

                                qualincart = 0;
                            }
                        } else {
                            break;
                        }

                    }

                }
            }

        }

        return detailCartlist;
    }

    private class RunBackground extends AsyncTask<Void, Integer, Void> {
        SharePreferenceHelper sp;
        DatabaseManagement db;
        ProgressDialog pd;


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            sp = new SharePreferenceHelper(context);
            db = new DatabaseManagement(context);
            pd = new ProgressDialog(context);
            pd.setMessage("กรุณารอซักครู่...");
            pd.setCancelable(false);
            pd.show();
        }

        @Override
        protected Void doInBackground(Void... params) {
            detailCarts = decreaseStock();

            if (detailCarts.size() != 0) {
                for (int i = 0; i < detailCarts.size(); i++) {
                    Cart cart = new Cart();
                    Order order = new Order(sp.getOrderSharePreference());
                    if (detailCarts.get(i).getDetail_cart_id() == 0) {
                        long id = db.insertDetailCart(detailCarts.get(i));
                        db.closeDatabase();

                        detailCarts.get(i).setDetail_cart_id((int) id);
                        cart.setDetailCart(detailCarts.get(i));
                        cart.setOrder(order);

                        db.insertCart(cart);
                        db.closeDatabase();
                    } else {
                        cart.setDetailCart(detailCarts.get(i));
                        cart.setOrder(order);
                        db.updateDetailCartOnCashier(detailCarts.get(i));
                        db.closeDatabase();
                    }
                }

                db.updateStatusOrder(com.market.application.javaclass.Status.statusid[1]);
                updateTotalAndDiscount();
                db.closeDatabase();

            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            pd.cancel();
            if (!pd.isShowing()) {
                Toast.makeText(context, getResources().getString(R.string.finish), Toast.LENGTH_SHORT).show();
                finish();
            }
        }
    }
}





