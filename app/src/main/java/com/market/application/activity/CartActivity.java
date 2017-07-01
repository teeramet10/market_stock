package com.market.application.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.market.application.R;
import com.market.application.SharePreferenceHelper;
import com.market.application.adapter.CartAdapter;
import com.market.application.database.DatabaseManagement;
import com.market.application.database.SqliteHelper;
import com.market.application.javaclass.DetailCart;
import com.market.application.javaclass.QualityProduct;

import java.util.ArrayList;

public class CartActivity extends BaseActivity {
    public static final int BACK = 101;
    public static final int NEXT = 102;
    ListView listViewCart;
    Context context = this;
    ArrayList<DetailCart> detailCarts;
    Button btnSale;
    CartAdapter cartAdapter;
    Toolbar toolbar;

    ArrayList<QualityProduct> qualityProducts;
    int press;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);
        listViewCart = (ListView) findViewById(R.id.lvcart);
        btnSale = (Button) findViewById(R.id.btnsale);
        toolbar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);


        DatabaseManagement databaseManagement = new DatabaseManagement(context);
        SharePreferenceHelper sp = new SharePreferenceHelper(context);
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

        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.empty_cart, null);
        ViewGroup viewGroup = (ViewGroup) listViewCart.getParent();
        viewGroup.addView(view);
        listViewCart.setEmptyView(view);

        detailCarts = databaseManagement.selectDetailCartOnCart(sql, arg);

        databaseManagement.closeDatabase();


        for (int j = 0; j < detailCarts.size(); j++) {
            int qual = 0;
            int id = detailCarts.get(j).getStock().getProduct().getId();
            String sql1 = "select * from " + SqliteHelper.STOCK_TB
                    + " where " + SqliteHelper.PRODUCT_QUALITY + ">0 and " + SqliteHelper.ID_PRODUCT + "=" + id;
            qualityProducts = databaseManagement.selectQualInStock(sql1, null);
            for (int i = 0; i < qualityProducts.size(); i++) {
                qual += qualityProducts.get(i).getQual();
            }
            detailCarts.get(j).getStock().setProduct_quality(qual);
            databaseManagement.closeDatabase();
        }


        cartAdapter = new CartAdapter(context, detailCarts);
        listViewCart.setAdapter(cartAdapter);

        btnSale.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                press = NEXT;
                new RunBackground().execute();
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        press = BACK;
        new RunBackground().execute();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.cart_activity_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (detailCarts.size() != 0) {
            if (id == R.id.delorder) {
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
                alertDialog.setMessage(context.getResources().getString(R.string.del_order));
                alertDialog.setIcon(android.R.drawable.ic_delete);
                alertDialog.setPositiveButton(context.getResources().getString(R.string.ok), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        delOrder();
                    }
                });
                alertDialog.setNegativeButton(context.getResources().getString(R.string.cancel), null);
                alertDialog.setCancelable(false);
                alertDialog.create().show();

            }
        }
        return super.onOptionsItemSelected(item);
    }

    private void delOrder() {
        SharePreferenceHelper sp = new SharePreferenceHelper(context);
        int idorder = sp.getOrderSharePreference();
        Log.i("ORDER", "ID_ORDER:" + idorder);
        DatabaseManagement db = new DatabaseManagement(context);
        long iddel = db.deleteOrder(idorder);
        db.closeDatabase();

        Log.i("ORDER", "DELID:" + iddel);

        if (iddel != 0) {
            detailCarts.clear();
            cartAdapter.notifyDataSetChanged();
        }
        sp.delOrderSharePreference();

        Log.i("ORDER", "DELID:" + sp.getOrderSharePreference());
    }


    private class RunBackground extends AsyncTask<Void, Integer, Void> {
        DatabaseManagement db;
        ProgressDialog progressDialog;

        @Override
        protected Void doInBackground(Void... voids) {

            for (int i = 0; i < cartAdapter.getCount(); i++) {
                DetailCart detailCart = (DetailCart) cartAdapter.getItem(i);
                db.updateDetailCart(detailCart);

            }

            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            db = new DatabaseManagement(context);
            progressDialog = new ProgressDialog(context);
            progressDialog.setMessage(getResources().getString(R.string.waiting));
            progressDialog.show();
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            progressDialog.dismiss();
            db.closeDatabase();

            if(press==NEXT){
                if (detailCarts.size() > 0) {
                    Intent intent = new Intent(context, CashierActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if(cartAdapter.getToast()!=null) {
            cartAdapter.getToast().cancel();
        }
    }
}
