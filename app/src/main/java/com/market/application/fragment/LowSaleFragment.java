package com.market.application.fragment;


import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.market.application.R;
import com.market.application.adapter.ProductAdapter;
import com.market.application.database.DatabaseManagement;
import com.market.application.database.SqliteHelper;
import com.market.application.javaclass.Product;
import com.market.application.javaclass.QualityProduct;
import com.market.application.javaclass.Stock;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;


public class LowSaleFragment extends BaseFragment {
    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    private static final String ARG_PARAM1 = "param1";

    private String mParam1;
    private RecyclerView recyclerView;
    private LinearLayout emptyLayout;
    ArrayList<Stock> stocks;
    private ProductAdapter productAdapter;

    public LowSaleFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static LowSaleFragment newInstance(String param1) {
        LowSaleFragment fragment = new LowSaleFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_low_sale, container, false);
        recyclerView = (RecyclerView) v.findViewById(R.id.productlowsale);
        emptyLayout = (LinearLayout) v.findViewById(R.id.empty_product);

        stocks = selectLowSale();

        if (stocks.size() == 0) {
            if (emptyLayout.getVisibility() == View.INVISIBLE) {
                emptyLayout.setVisibility(View.VISIBLE);
            }
        } else {
            if (emptyLayout.getVisibility() == View.VISIBLE) {
                emptyLayout.setVisibility(View.INVISIBLE);
            }
        }

        productAdapter = new ProductAdapter(context, stocks);
        recyclerView.setLayoutManager(new GridLayoutManager(context, 2));
        recyclerView.setAdapter(productAdapter);

        return v;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if(stocks!=null){
            stocks =selectLowSale();
            productAdapter.setStockArrayList(stocks);
        }
    }

    private ArrayList<Stock> selectLowSale() {
        Date date = new Date();
        long current = date.getTime();
        long twoweek = 1209600033;

        long week = current - twoweek;
        date.setTime(week);
        String strdate = dateFormat.format(date);
        String selectstock = "select * from " + SqliteHelper.STOCK_TB
                +" inner join "+SqliteHelper.PRODUCT_TB+" on "+SqliteHelper.PRODUCT_ID+"="+SqliteHelper.ID_PRODUCT
                + " where date("+ SqliteHelper.DATE_FIRST_IN + "/1000,'unixepoch','localtime')<='" + strdate
                + "' and " + SqliteHelper.PRODUCT_QUALITY + ">0 and "+SqliteHelper.ID_DISABLE+"=1";

        String selectcart = "select * from " + SqliteHelper.DETAIL_CART_TB
                + " inner join " + SqliteHelper.STOCK_TB + " on "
                + SqliteHelper.STOCK_TB + "." + SqliteHelper.STOCK_ID + "="
                + SqliteHelper.DETAIL_CART_TB + "." + SqliteHelper.ID_PRODUCT_STOCK
                + " inner join " + SqliteHelper.PRODUCT_TB + " on "
                + SqliteHelper.STOCK_TB + "." + SqliteHelper.ID_PRODUCT + "="
                + SqliteHelper.PRODUCT_TB + "." + SqliteHelper.PRODUCT_ID + " order by "
                + SqliteHelper.PRODUCT_TB + "." + SqliteHelper.PRODUCT_ID + " asc ";

        DatabaseManagement db = new DatabaseManagement(context);
        ArrayList<QualityProduct> qualInStock = db.selectQualInStock(selectstock, null);
        ArrayList<QualityProduct> qualInOrder = db.selectQualInOrder(selectcart, null);
        ArrayList<Integer> idproduct = new ArrayList<>();
        db.closeDatabase();
        if (qualInStock.size() != 0) {

            for (int i = 0; i < qualInStock.size(); i++) {
                int idinstock = qualInStock.get(i).getIdproduct();
                int sumqual = qualInStock.get(i).getQual();

                if (qualInOrder.size() != 0) {
                    for (int j = 0; j < qualInOrder.size(); j++) {
                        int idinorder = qualInOrder.get(j).getIdproduct();
                        if (idinorder == idinstock) {
                            sumqual += qualInOrder.get(j).getQual();
                        }
                    }

                    int qualinorder = sumqual - qualInStock.get(i).getQual();
                    double ratioinstock = 100 - (((double) qualinorder / sumqual) * 100);

                    if (ratioinstock > 50) {
                        idproduct.add(qualInStock.get(i).getIdproduct());
                    }
                } else {
                    idproduct.add(qualInStock.get(i).getIdproduct());
                }
            }
            ArrayList<Stock> stocks = new ArrayList<>();

            for (int i = 0; i < idproduct.size(); i++) {
                String stocklowsale = "select * from " + SqliteHelper.STOCK_TB
                        + " inner join " + SqliteHelper.PRODUCT_TB
                        + " on " + SqliteHelper.PRODUCT_TB + "." + SqliteHelper.PRODUCT_ID
                        + "=" + SqliteHelper.STOCK_TB + "." + SqliteHelper.ID_PRODUCT
                        + " inner join " + SqliteHelper.TYPE_TB
                        + " on " + SqliteHelper.TYPE_TB + "." + SqliteHelper.TYPE_ID
                        + "=" + SqliteHelper.PRODUCT_TB + "." + SqliteHelper.ID_TYPE
                        + " where " + SqliteHelper.PRODUCT_TB + "." + SqliteHelper.ID_DISABLE + "=1"
                        + " and " + SqliteHelper.STOCK_TB + "." + SqliteHelper.ID_PRODUCT + "=" + idproduct.get(i)
                        + " order by " + SqliteHelper.STOCK_TB + "." + SqliteHelper.ID_PRODUCT + " ASC,"
                        + SqliteHelper.STOCK_TB + "." + SqliteHelper.DATE_FIRST_IN + " ASC";
                ArrayList<Stock> products = db.selectStock(stocklowsale, null);
                Stock stock = new Stock();
                int qual = 0;
                for (int j = 0; j < products.size(); j++) {
                    if (j == 0) {
                        Product product = products.get(j).getProduct();
                        stock.setStockid(products.get(j).getStockid());
                        stock.setProduct(product);
                        stock.setProduct_price(products.get(j).getProduct_price());
                    }
                    qual += products.get(j).getProduct_quality();
                }
                stock.setProduct_quality(qual);

                int count =0;
                for (int k = 0; k < stocks.size(); k++) {
                    if (stocks.get(k).getProduct().getId()==stock.getStockid()) {
                        count++;
                    }
                }
                if(count==0) {
                    stocks.add(stock);
                }

            }
            return stocks;
        } else {
            return new ArrayList<>();
        }


    }

}
