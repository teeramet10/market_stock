package com.market.application.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.market.application.R;
import com.market.application.SharePreferenceHelper;
import com.market.application.adapter.SelectProductPagerAdapter;
import com.market.application.database.DatabaseManagement;
import com.market.application.database.SqliteHelper;
import com.market.application.fragment.ProductNameFragment;
import com.market.application.fragment.ProductPriceFragment;
import com.market.application.fragment.ProductQualFragment;
import com.market.application.javaclass.Cart;
import com.market.application.javaclass.DetailCart;
import com.market.application.javaclass.Order;
import com.market.application.javaclass.Status;
import com.market.application.javaclass.Stock;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;

public class ProductActivity extends BaseActivity {
    ViewPager productPager;
    TabLayout tabLayout;
    SelectProductPagerAdapter selectProductPagerAdapter;
    TextView tvTitle;

    Context context = this;
    public int idType;
    String title;

    int countname = 0;
    int countprice = 0;
    int countqual = 0;
    SearchView searchView;
    Toast toast;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product);
        productPager = (ViewPager) findViewById(R.id.productpager);
        tabLayout = (TabLayout) findViewById(R.id.tabbar);
        tvTitle = (TextView) findViewById(R.id.tvtitle);
        searchView = (SearchView) findViewById(R.id.search);


        Intent intent = getIntent();
        idType = intent.getIntExtra("ID_TYPE", 0);
        title = intent.getStringExtra("NAME");

        tvTitle.setText(title);

        selectProductPagerAdapter = new SelectProductPagerAdapter(getSupportFragmentManager(), this);
        productPager.setAdapter(selectProductPagerAdapter);
        tabLayout.setupWithViewPager(productPager);

        setSearchView();

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                int i = tab.getPosition();
                if (i == 0) {
                    ProductNameFragment fragment = (ProductNameFragment) selectProductPagerAdapter.getProductNameFragment();
                    if(countname%2==0) {
                        Collections.sort(fragment.getStocks(), Stock.sortDescStockName);
                    }else {
                        Collections.sort(fragment.getStocks(), Stock.sortAscStockName);
                    }
                    fragment.getAdapter().notifyDataSetChanged();
                    countname++;
                } else if (i == 1) {
                    ProductPriceFragment fragment = (ProductPriceFragment) selectProductPagerAdapter.getProductPriceFragment();
                    if(countprice%2==0) {
                        Collections.sort(fragment.getStocks(), Stock.sortAscStockPrice);
                    }else {
                        Collections.sort(fragment.getStocks(), Stock.sortDescStockPrice);
                    }
                    fragment.getAdapter().notifyDataSetChanged();
                    countprice++;
                } else if (i == 2) {
                    ProductQualFragment fragment = (ProductQualFragment) selectProductPagerAdapter.getProductQualFragment();
                    if(countqual%2==0) {
                        Collections.sort(fragment.getStocks(), Stock.sortAscStockQuality);

                    }else{
                        Collections.sort(fragment.getStocks(), Stock.sortDescStockQuality);
                    }
                    fragment.getAdapter().notifyDataSetChanged();
                    countqual++;
                }
            }
        });

    }

    public void createOrder(Stock stock) {
        SharePreferenceHelper sp = new SharePreferenceHelper(context);
        int orderid;
        orderid = sp.getOrderSharePreference();
        DatabaseManagement databaseManagement = new DatabaseManagement(context);
        Order order = new Order();
        order.setOrder_id(orderid);
        if (orderid == -1) {
            Date date = new Date();
            Status status = new Status(Status.statusid[0]);
            order.setOrder_date(date.getTime());
            order.setStatus(status);
            orderid = (int) databaseManagement.insertOrder(order);
            order.setOrder_id(orderid);
            sp.putOrderSharePreference(orderid);
            Log.i("ORDER", String.valueOf(orderid));
        }
        String sql = "select * from " + SqliteHelper.DETAIL_CART_TB + " inner join " + SqliteHelper.CART_TB
                + " on " + SqliteHelper.DETAIL_CART_TB + "." + SqliteHelper.DETAIL_CART_ID
                + "=" + SqliteHelper.CART_TB + "." + SqliteHelper.ID_CART_DETAIL
                + " where " + SqliteHelper.DETAIL_CART_TB + "." + SqliteHelper.ID_PRODUCT_STOCK + "=" + stock.getStockid()
                + " and " + SqliteHelper.CART_TB + "." + SqliteHelper.ID_ORDER_F + "=" + orderid;
        ArrayList<DetailCart> detailCarts = databaseManagement.selectDetailCart(sql, new String[]{});

        DetailCart detailCart = new DetailCart();
        if (detailCarts.size() == 0) {

            detailCart.setDetail_cart_quality(1);
            detailCart.setDetail_cart_price(stock.getProduct_price());
            detailCart.setDetail_cart_total(stock.getProduct_price());
            detailCart.setDetail_cart_cost(0);
            detailCart.setStock(stock);
            long iddetailcart = databaseManagement.insertDetailCart(detailCart);

            if(iddetailcart!=-1) {
                detailCart.setDetail_cart_id((int) iddetailcart);
                Cart cart = new Cart();
                cart.setOrder(order);
                cart.setDetailCart(detailCart);
                long idcart = databaseManagement.insertCart(cart);

                String message = detailCart.getStock().getProduct().getName() + "=" + detailCart.getDetail_cart_quality();
                Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
            }else {
                toast=Toast.makeText(context,getResources().getString(R.string.notaddcart), Toast.LENGTH_SHORT);
                toast.show();
            }

        } else {

            int quality = detailCarts.get(0).getDetail_cart_quality() + 1;

            if (quality <= stock.getProduct_quality()) {
                detailCart.setDetail_cart_id(detailCarts.get(0).getDetail_cart_id());
                detailCart.setDetail_cart_quality(quality);
                detailCart.setDetail_cart_price(stock.getProduct_price());
                detailCart.setDetail_cart_total(stock.getProduct_price() * quality);
                databaseManagement.updateDetailCart(detailCart);

                String message = stock.getProduct().getName() + "=" + detailCart.getDetail_cart_quality();
                toast=Toast.makeText(context, message, Toast.LENGTH_SHORT);
                toast.show();
            } else {
                toast=Toast.makeText(context, getResources().getString(R.string.productnenough), Toast.LENGTH_SHORT);
                toast.show();
            }
        }
        databaseManagement.closeDatabase();
    }


    private void setSearchView() {
        searchView.setOnSearchClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if (tvTitle.getVisibility() == View.VISIBLE) {
                    tvTitle.setVisibility(View.INVISIBLE);
                    searchView.setFitsSystemWindows(true);
                }

            }
        });
        searchView.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                if (tvTitle.getVisibility() == View.INVISIBLE) {
                    tvTitle.setVisibility(View.VISIBLE);
                    searchView.setFitsSystemWindows(true);
                }
                return false;
            }
        });

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                Fragment fragment1 = selectProductPagerAdapter.getProductNameFragment();
                Fragment fragment2 = selectProductPagerAdapter.getProductPriceFragment();
                Fragment fragment3 = selectProductPagerAdapter.getProductQualFragment();


                if (fragment1 instanceof ProductNameFragment) {
                    ((ProductNameFragment) fragment1).getAdapter().filterList(newText);
                }
                if (fragment2 instanceof ProductPriceFragment) {
                    ((ProductPriceFragment) fragment2).getAdapter().filterList(newText);
                }
                if (fragment3 instanceof ProductQualFragment) {
                    ((ProductQualFragment) fragment3).getAdapter().filterList(newText);
                }
                return true;
            }

        });
    }

    @Override
    protected void onStop() {
        super.onStop();
        if(toast!=null) {
            toast.cancel();
        }
    }
}
