package com.market.application.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.market.application.SharePreferenceHelper;
import com.market.application.javaclass.Cart;
import com.market.application.javaclass.DetailCart;
import com.market.application.javaclass.Order;
import com.market.application.javaclass.Product;
import com.market.application.javaclass.QualityProduct;
import com.market.application.javaclass.Stock;
import com.market.application.javaclass.Type;

import java.util.ArrayList;

/**
 * Created by Administrator on 22/3/2560.
 */
public class DatabaseManagement {
    public static final String TAG = "DATABASEHELPER";

    Context context;
    SQLiteDatabase sqLiteDatabase;
    SqliteHelper sqliteHelper;

    public DatabaseManagement(Context context) {
        this.context = context;
        sqliteHelper = new SqliteHelper(context);
    }

    public long insertProduct(Product product) {
        sqLiteDatabase = sqliteHelper.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(SqliteHelper.PRODUCT_NAME, product.getName());
        contentValues.put(SqliteHelper.PRODUCT_PICTURE, product.getPathimage());
        contentValues.put(SqliteHelper.ID_TYPE, product.getType().getId());
        contentValues.put(SqliteHelper.ID_DISABLE, 1);

        long i = sqLiteDatabase.insert(SqliteHelper.PRODUCT_TB, null, contentValues);

        return i;
    }

    public long updateProduct(Product product) {
        sqLiteDatabase = sqliteHelper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(SqliteHelper.PRODUCT_NAME, product.getName());
        contentValues.put(SqliteHelper.PRODUCT_PICTURE, product.getPathimage());
        contentValues.put(SqliteHelper.ID_TYPE, product.getType().getId());

        long i = sqLiteDatabase.update(SqliteHelper.PRODUCT_TB, contentValues, SqliteHelper.PRODUCT_ID + "=?"
                , new String[]{String.valueOf(product.getId())});

        return i;
    }

    public int disableProduct(int id) {
        sqLiteDatabase = sqliteHelper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(SqliteHelper.ID_DISABLE, 2);
        int i = sqLiteDatabase.update(SqliteHelper.PRODUCT_TB, contentValues, SqliteHelper.PRODUCT_ID + "=?"
                , new String[]{String.valueOf(id)});

        return i;
    }



    public long insertOrder(Order order) {
        sqLiteDatabase = sqliteHelper.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(SqliteHelper.ORDER_DATE, order.getOrder_date());
        contentValues.put(SqliteHelper.ID_STATUS, order.getStatus().getStatus_id());

        long i = sqLiteDatabase.insert(SqliteHelper.ORDER_TB, null, contentValues);


        return i;
    }


    public long updateStatusOrder(int status) {
        SharePreferenceHelper sp = new SharePreferenceHelper(context);
        int id = sp.getOrderSharePreference();

        sqLiteDatabase = sqliteHelper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(SqliteHelper.ID_STATUS, status);


        long i = sqLiteDatabase.update(SqliteHelper.ORDER_TB, contentValues, SqliteHelper.ORDER_ID + "=?"
                , new String[]{String.valueOf(id)});

        return i;
    }

    public long updateTotalOnOrder(double total, double discount) {
        SharePreferenceHelper sp = new SharePreferenceHelper(context);
        int id = sp.getOrderSharePreference();

        sqLiteDatabase = sqliteHelper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(SqliteHelper.ORDER_DISCOUNT, discount);
        contentValues.put(SqliteHelper.ORDER_TOTAL, total+discount);

        long i = sqLiteDatabase.update(SqliteHelper.ORDER_TB, contentValues, SqliteHelper.ORDER_ID + "=?"
                , new String[]{String.valueOf(id)});

        return i;
    }

    public long deleteOrder(int id) {

        sqLiteDatabase = sqliteHelper.getReadableDatabase();
        String sql = "select " + SqliteHelper.ID_CART_DETAIL + " from " + SqliteHelper.CART_TB
                + " where " + SqliteHelper.ID_ORDER_F + "=" + id;

        Cursor cursor = sqLiteDatabase.rawQuery(sql, null);

        ArrayList<Integer> arrayid = new ArrayList<>();
        while (cursor.moveToNext()) {
            arrayid.add(cursor.getInt(cursor.getColumnIndex(SqliteHelper.ID_CART_DETAIL)));

        }
        cursor.close();

        sqLiteDatabase = sqliteHelper.getWritableDatabase();
        long i = sqLiteDatabase.delete(SqliteHelper.ORDER_TB, SqliteHelper.ORDER_ID + "=?"
                , new String[]{String.valueOf(id)});

        sqLiteDatabase.delete(SqliteHelper.CART_TB, SqliteHelper.ID_ORDER_F + "=?", new String[]{String.valueOf(id)});
        if (arrayid.size() > 0) {
            for (int j = 0; j < arrayid.size(); j++) {
                sqLiteDatabase.delete(SqliteHelper.DETAIL_CART_TB, SqliteHelper.DETAIL_CART_ID + "=" + arrayid.get(j), null);
            }
        }
        return i;
    }


    public long insertCart(Cart cart) {
        sqLiteDatabase = sqliteHelper.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(SqliteHelper.ID_ORDER_F, cart.getOrder().getOrder_id());
        contentValues.put(SqliteHelper.ID_CART_DETAIL, String.valueOf(cart.getDetailCart().getDetail_cart_id()));

        long i = sqLiteDatabase.insert(SqliteHelper.CART_TB, null, contentValues);

        return i;
    }

    public long updateCart(Cart cart) {
        sqLiteDatabase = sqliteHelper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(SqliteHelper.CART_ID, cart.getCart_id());
        contentValues.put(SqliteHelper.ID_ORDER_F, cart.getOrder().getOrder_id());
        contentValues.put(SqliteHelper.ID_CART_DETAIL, String.valueOf(cart.getDetailCart()));


        long i = sqLiteDatabase.update(SqliteHelper.CART_TB, contentValues, SqliteHelper.CART_ID + "=?"
                , new String[]{String.valueOf(cart.getCart_id())});

        return i;
    }

    public long deleteCart(int id) {
        sqLiteDatabase = sqliteHelper.getWritableDatabase();
        long i = sqLiteDatabase.delete(SqliteHelper.CART_TB, SqliteHelper.CART_ID + "=?"
                , new String[]{String.valueOf(id)});

        return i;
    }

    public long deleteDetailCartInCart(int id) {
        sqLiteDatabase = sqliteHelper.getWritableDatabase();
        long i = sqLiteDatabase.delete(SqliteHelper.CART_TB, SqliteHelper.ID_CART_DETAIL + "=?"
                , new String[]{String.valueOf(id)});

        return i;
    }

    public long insertDetailCart(DetailCart detailCart) {
        sqLiteDatabase = sqliteHelper.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(SqliteHelper.ID_PRODUCT_STOCK, detailCart.getStock().getStockid());
        contentValues.put(SqliteHelper.DETAIL_CART_QUALITY, detailCart.getDetail_cart_quality());
        contentValues.put(SqliteHelper.DETAIL_CART_PRICE, detailCart.getDetail_cart_price());
        contentValues.put(SqliteHelper.DETAIL_CART_TOTAL, detailCart.getDetail_cart_total());
        contentValues.put(SqliteHelper.DETAIL_CART_COST, detailCart.getDetail_cart_cost());

        long i = sqLiteDatabase.insert(SqliteHelper.DETAIL_CART_TB, null, contentValues);

        return i;
    }

    public long updateDetailCartOnCashier(DetailCart detailCart) {
        sqLiteDatabase = sqliteHelper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(SqliteHelper.ID_PRODUCT_STOCK, detailCart.getStock().getStockid());
        contentValues.put(SqliteHelper.DETAIL_CART_QUALITY, detailCart.getDetail_cart_quality());
        contentValues.put(SqliteHelper.DETAIL_CART_PRICE, detailCart.getDetail_cart_price());
        contentValues.put(SqliteHelper.DETAIL_CART_COST, detailCart.getDetail_cart_cost());
        contentValues.put(SqliteHelper.DETAIL_CART_TOTAL, detailCart.getDetail_cart_total());


        long i = sqLiteDatabase.update(SqliteHelper.DETAIL_CART_TB, contentValues, SqliteHelper.DETAIL_CART_ID + "=?"
                , new String[]{String.valueOf(detailCart.getDetail_cart_id())});

        return i;
    }

    public long updateDetailCart(DetailCart detailCart) {
        sqLiteDatabase = sqliteHelper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(SqliteHelper.DETAIL_CART_QUALITY, detailCart.getDetail_cart_quality());
        contentValues.put(SqliteHelper.DETAIL_CART_PRICE, detailCart.getDetail_cart_price());
        contentValues.put(SqliteHelper.DETAIL_CART_COST, detailCart.getDetail_cart_cost());
        contentValues.put(SqliteHelper.DETAIL_CART_TOTAL, detailCart.getDetail_cart_total());


        long i = sqLiteDatabase.update(SqliteHelper.DETAIL_CART_TB, contentValues, SqliteHelper.DETAIL_CART_ID + "=?"
                , new String[]{String.valueOf(detailCart.getDetail_cart_id())});

        return i;
    }


    public long deleteDetailCart(int id) {
        sqLiteDatabase = sqliteHelper.getWritableDatabase();
        long i = sqLiteDatabase.delete(SqliteHelper.DETAIL_CART_TB, SqliteHelper.DETAIL_CART_ID + "=?"
                , new String[]{String.valueOf(id)});

        return i;
    }

    public ArrayList<Integer> deleteStockInDetailCart(int id) {
        sqLiteDatabase = sqliteHelper.getWritableDatabase();
        String sql = "select " + SqliteHelper.DETAIL_CART_ID + " from " + SqliteHelper.DETAIL_CART_TB
                + " where " + SqliteHelper.ID_PRODUCT_STOCK + "=" + id;

        Cursor cursor = sqLiteDatabase.rawQuery(sql, null);

        ArrayList<Integer> arrayid = new ArrayList<>();
        while (cursor.moveToNext()) {
            arrayid.add(cursor.getInt(cursor.getColumnIndex(SqliteHelper.DETAIL_CART_ID)));

        }
        cursor.close();
        sqLiteDatabase.delete(SqliteHelper.DETAIL_CART_TB, SqliteHelper.ID_PRODUCT_STOCK + "=?"
                , new String[]{String.valueOf(id)});

        return arrayid;
    }


    public long insertStock(Stock stock) {
        sqLiteDatabase = sqliteHelper.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(SqliteHelper.ID_PRODUCT, stock.getProduct().getId());
        contentValues.put(SqliteHelper.PRODUCT_COST, stock.getProduct_cost());
        contentValues.put(SqliteHelper.PRODUCT_PRICE, stock.getProduct_price());
        contentValues.put(SqliteHelper.PRODUCT_QUALITY, stock.getProduct_quality());
        contentValues.put(SqliteHelper.DATE_FIRST_IN, stock.getDate_fist_in());

        long i = sqLiteDatabase.insert(SqliteHelper.STOCK_TB, null, contentValues);
        return i;
    }

    public long addStock(Stock stock) {

        sqLiteDatabase = sqliteHelper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(SqliteHelper.ID_PRODUCT, stock.getProduct().getId());
        contentValues.put(SqliteHelper.PRODUCT_COST, stock.getProduct_cost());
        contentValues.put(SqliteHelper.PRODUCT_PRICE, stock.getProduct_price());
        contentValues.put(SqliteHelper.PRODUCT_QUALITY, stock.getProduct_quality());
        contentValues.put(SqliteHelper.DATE_FIRST_IN, stock.getDate_fist_in());


        long i = sqLiteDatabase.insert(SqliteHelper.STOCK_TB, null, contentValues);
        if (i != 0) {
            ContentValues cv = new ContentValues();
            cv.put(SqliteHelper.PRODUCT_PRICE, stock.getProduct_price());

            sqLiteDatabase.update(SqliteHelper.STOCK_TB, cv, SqliteHelper.ID_PRODUCT + "=?"
                    , new String[]{String.valueOf(stock.getProduct().getId())});
        }
        return i;
    }

    public int updateStock(Stock stock) {
        sqLiteDatabase = sqliteHelper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(SqliteHelper.PRODUCT_COST, stock.getProduct_cost());
        contentValues.put(SqliteHelper.PRODUCT_PRICE, stock.getProduct_price());
        contentValues.put(SqliteHelper.PRODUCT_QUALITY, stock.getProduct_quality());
        contentValues.put(SqliteHelper.DATE_FIRST_IN, stock.getDate_fist_in());


        int i = sqLiteDatabase.update(SqliteHelper.STOCK_TB, contentValues, SqliteHelper.STOCK_ID + "=?"
                , new String[]{String.valueOf(stock.getStockid())});

        return i;
    }

    public long deleteStock(int id) {
        sqLiteDatabase = sqliteHelper.getWritableDatabase();
        long i = sqLiteDatabase.delete(SqliteHelper.STOCK_TB, SqliteHelper.STOCK_ID + "=?"
                , new String[]{String.valueOf(id)});

        return i;
    }

    public int deleteQualInStock(int quality, int idstock) {
        sqLiteDatabase = sqliteHelper.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(SqliteHelper.PRODUCT_QUALITY, quality);
        int i = sqLiteDatabase.update(SqliteHelper.STOCK_TB, contentValues, SqliteHelper.STOCK_ID + "=?"
                , new String[]{String.valueOf(idstock)});

        return i;
    }


    public long insertType(Type type) {
        sqLiteDatabase = sqliteHelper.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(SqliteHelper.TYPE_NAME, type.getName());
        contentValues.put(SqliteHelper.ID_DISABLE, 1);


        long i = sqLiteDatabase.insert(SqliteHelper.TYPE_TB, null, contentValues);

        return i;
    }

    public int updateType(Type type) {
        sqLiteDatabase = sqliteHelper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(SqliteHelper.TYPE_NAME, type.getName());


        int i = sqLiteDatabase.update(SqliteHelper.TYPE_TB, contentValues, SqliteHelper.TYPE_ID + "=?"
                , new String[]{String.valueOf(type.getId())});

        return i;
    }

    public int disableType(int id) {
        sqLiteDatabase = sqliteHelper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(SqliteHelper.ID_DISABLE, 2);
        int i = sqLiteDatabase.update(SqliteHelper.TYPE_TB, contentValues, SqliteHelper.TYPE_ID + "=?"
                , new String[]{String.valueOf(id)});
        sqLiteDatabase.close();
        return i;
    }



    public ArrayList<Product> selectProduct(String sql, String[] arg) {
        ArrayList<Product> products = new ArrayList<>();
        sqLiteDatabase = sqliteHelper.getReadableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery(sql, arg);
        while (cursor.moveToNext()) {
            Product product = new Product();
            Type type = new Type();
            product.setId(cursor.getInt(cursor.getColumnIndex(SqliteHelper.PRODUCT_ID)));
            product.setName(cursor.getString(cursor.getColumnIndex(SqliteHelper.PRODUCT_NAME)));
            product.setPathimage(cursor.getString(cursor.getColumnIndex(SqliteHelper.PRODUCT_PICTURE)));
            type.setId(cursor.getInt(cursor.getColumnIndex(SqliteHelper.TYPE_ID)));
            type.setName(cursor.getString(cursor.getColumnIndex(SqliteHelper.TYPE_NAME)));
            product.setType(type);
            products.add(product);
        }
        cursor.close();
        return products;

    }

    public ArrayList<Product> selectProductType(String sql, String[] arg) {
        ArrayList<Product> products = new ArrayList<>();
        sqLiteDatabase = sqliteHelper.getReadableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery(sql, arg);
        while (cursor.moveToNext()) {
            Product product = new Product();
            product.setId(cursor.getInt(cursor.getColumnIndex(SqliteHelper.PRODUCT_ID)));
            products.add(product);
        }
        cursor.close();
        return products;

    }


    public ArrayList<Order> selectOrder(String sql, String[] arg) {
        ArrayList<Order> orders = new ArrayList<>();
        sqLiteDatabase = sqliteHelper.getReadableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery(sql, arg);
        while (cursor.moveToNext()) {
            Order order = new Order();
            order.setOrder_id(cursor.getInt(cursor.getColumnIndex(SqliteHelper.ORDER_ID)));
            order.setDiscount(cursor.getDouble(cursor.getColumnIndex(SqliteHelper.ORDER_DISCOUNT)));
            order.setOrder_date(cursor.getLong(cursor.getColumnIndex(SqliteHelper.ORDER_DATE)));
            order.setOrder_total(cursor.getDouble(cursor.getColumnIndex(SqliteHelper.ORDER_TOTAL)));

            orders.add(order);
        }
        cursor.close();
        return orders;

    }

    public ArrayList<Cart> selectAllCart(String sql, String[] arg) {
        ArrayList<Cart> carts = new ArrayList<>();

        sqLiteDatabase = sqliteHelper.getReadableDatabase();

        Cursor cursor = sqLiteDatabase.rawQuery(sql, arg);
        while (cursor.moveToNext()) {
            Cart cart = new Cart();
            DetailCart detailCart = new DetailCart();
            Stock stock = new Stock();
            Product product = new Product();
            Order order = new Order();

            product.setName(cursor.getString(cursor.getColumnIndex(SqliteHelper.PRODUCT_NAME)));
            product.setPathimage(cursor.getString(cursor.getColumnIndex(SqliteHelper.PRODUCT_PICTURE)));
            product.setId(cursor.getInt(cursor.getColumnIndex(SqliteHelper.PRODUCT_ID)));

            stock.setProduct(product);
            stock.setStockid(cursor.getInt(cursor.getColumnIndex(SqliteHelper.STOCK_ID)));
            stock.setProduct_cost(cursor.getDouble(cursor.getColumnIndex(SqliteHelper.PRODUCT_COST)));

            detailCart.setStock(stock);
            detailCart.setDetail_cart_id(cursor.getInt(cursor.getColumnIndex(SqliteHelper.ID_CART_DETAIL)));
            detailCart.setDetail_cart_quality(cursor.getInt(cursor.getColumnIndex(SqliteHelper.DETAIL_CART_QUALITY)));
            detailCart.setDetail_cart_price(cursor.getDouble(cursor.getColumnIndex(SqliteHelper.DETAIL_CART_PRICE)));
            detailCart.setDetail_cart_cost(cursor.getDouble(cursor.getColumnIndex(SqliteHelper.DETAIL_CART_COST)));

            order.setOrder_id(cursor.getInt(cursor.getColumnIndex(SqliteHelper.ID_ORDER_F)));

            cart.setCart_id(cursor.getInt(cursor.getColumnIndex(SqliteHelper.CART_ID)));
            cart.setDetailCart(detailCart);
            cart.setOrder(order);

            carts.add(cart);
        }
        cursor.close();
        return carts;

    }


    public ArrayList<Cart> selectCart(String sql, String[] arg) {
        ArrayList<Cart> carts = new ArrayList<>();
        sqLiteDatabase = sqliteHelper.getReadableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery(sql, arg);
        while (cursor.moveToNext()) {
            Cart cart = new Cart();
            Order order = new Order();
            DetailCart detailCart = new DetailCart();

            cart.setCart_id(cursor.getInt(cursor.getColumnIndex(SqliteHelper.CART_ID)));
            order.setOrder_id(cursor.getInt(cursor.getColumnIndex(SqliteHelper.ID_ORDER_F)));
            detailCart.setDetail_cart_id(cursor.getInt(cursor.getColumnIndex(SqliteHelper.ID_CART_DETAIL)));
            cart.setDetailCart(detailCart);
            cart.setOrder(order);
            carts.add(cart);
        }
        cursor.close();
        return carts;

    }


    public ArrayList<DetailCart> selectDetailCart(String sql, String[] arg) {
        ArrayList<DetailCart> detailCarts = new ArrayList<>();
        sqLiteDatabase = sqliteHelper.getReadableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery(sql, arg);
        while (cursor.moveToNext()) {
            DetailCart detailCart = new DetailCart();
            Stock stock = new Stock();

            detailCart.setDetail_cart_id(cursor.getInt(cursor.getColumnIndex(SqliteHelper.DETAIL_CART_ID)));
            stock.setStockid(cursor.getInt(cursor.getColumnIndex(SqliteHelper.ID_PRODUCT_STOCK)));
            detailCart.setDetail_cart_quality(cursor.getInt(cursor.getColumnIndex(SqliteHelper.DETAIL_CART_QUALITY)));
            detailCart.setDetail_cart_price(cursor.getDouble(cursor.getColumnIndex(SqliteHelper.DETAIL_CART_PRICE)));
            detailCart.setDetail_cart_cost(cursor.getDouble(cursor.getColumnIndex(SqliteHelper.DETAIL_CART_COST)));
            detailCart.setDetail_cart_total(cursor.getDouble(cursor.getColumnIndex(SqliteHelper.DETAIL_CART_TOTAL)));


            detailCart.setStock(stock);
            detailCarts.add(detailCart);
        }
        cursor.close();

        return detailCarts;

    }

    public ArrayList<DetailCart> selectDetailCartWithProduct(String sql, String[] arg) {
        ArrayList<DetailCart> detailCarts = new ArrayList<>();
        sqLiteDatabase = sqliteHelper.getReadableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery(sql, arg);
        while (cursor.moveToNext()) {
            DetailCart detailCart = new DetailCart();
            Stock stock = new Stock();
            Product product=new Product();
            product.setName(cursor.getString(cursor.getColumnIndex(SqliteHelper.PRODUCT_NAME)));
            product.setPathimage(cursor.getString(cursor.getColumnIndex(SqliteHelper.PRODUCT_PICTURE)));
            stock.setStockid(cursor.getInt(cursor.getColumnIndex(SqliteHelper.ID_PRODUCT_STOCK)));
            stock.setProduct(product);

            detailCart.setDetail_cart_id(cursor.getInt(cursor.getColumnIndex(SqliteHelper.DETAIL_CART_ID)));
            detailCart.setDetail_cart_quality(cursor.getInt(cursor.getColumnIndex(SqliteHelper.DETAIL_CART_QUALITY)));
            detailCart.setDetail_cart_price(cursor.getDouble(cursor.getColumnIndex(SqliteHelper.DETAIL_CART_PRICE)));
            detailCart.setDetail_cart_cost(cursor.getDouble(cursor.getColumnIndex(SqliteHelper.DETAIL_CART_COST)));
            detailCart.setDetail_cart_total(cursor.getDouble(cursor.getColumnIndex(SqliteHelper.DETAIL_CART_TOTAL)));


            detailCart.setStock(stock);
            detailCarts.add(detailCart);
        }
        cursor.close();

        return detailCarts;

    }

    public ArrayList<DetailCart> selectDetailCartOnCart(String sql, String[] arg) {
        ArrayList<DetailCart> detailCarts = new ArrayList<>();
        sqLiteDatabase = sqliteHelper.getReadableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery(sql, arg);

        while (cursor.moveToNext()) {
            DetailCart detailCart = new DetailCart();
            Stock stock = new Stock();
            Product product = new Product();

            product.setId(cursor.getInt(cursor.getColumnIndex(SqliteHelper.PRODUCT_ID)));
            product.setName(cursor.getString(cursor.getColumnIndex(SqliteHelper.PRODUCT_NAME)));
            product.setPathimage(cursor.getString(cursor.getColumnIndex(SqliteHelper.PRODUCT_PICTURE)));
            product.setType(new Type(cursor.getInt(cursor.getColumnIndex(SqliteHelper.ID_TYPE))));

            stock.setStockid(cursor.getInt(cursor.getColumnIndex(SqliteHelper.ID_PRODUCT_STOCK)));
            stock.setProduct_price(cursor.getDouble(cursor.getColumnIndex(SqliteHelper.PRODUCT_PRICE)));
            stock.setProduct_quality(cursor.getInt(cursor.getColumnIndex(SqliteHelper.PRODUCT_QUALITY)));
            detailCart.setDetail_cart_id(cursor.getInt(cursor.getColumnIndex(SqliteHelper.DETAIL_CART_ID)));
            detailCart.setDetail_cart_quality(cursor.getInt(cursor.getColumnIndex(SqliteHelper.DETAIL_CART_QUALITY)));
            detailCart.setDetail_cart_price(cursor.getDouble(cursor.getColumnIndex(SqliteHelper.DETAIL_CART_PRICE)));
            detailCart.setDetail_cart_total(cursor.getDouble(cursor.getColumnIndex(SqliteHelper.DETAIL_CART_TOTAL)));

            stock.setProduct(product);
            detailCart.setStock(stock);
            detailCarts.add(detailCart);
        }
        cursor.close();

        return detailCarts;

    }

    public ArrayList<DetailCart> selectDetailCartOnHistory(String sql, String[] arg) {
        ArrayList<DetailCart> detailCarts = new ArrayList<>();
        sqLiteDatabase = sqliteHelper.getReadableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery(sql, arg);

        while (cursor.moveToNext()) {
            DetailCart detailCart = new DetailCart();
            Stock stock = new Stock();
            Product product = new Product();

            product.setId(cursor.getInt(cursor.getColumnIndex(SqliteHelper.PRODUCT_ID)));
            product.setName(cursor.getString(cursor.getColumnIndex(SqliteHelper.PRODUCT_NAME)));
            product.setPathimage(cursor.getString(cursor.getColumnIndex(SqliteHelper.PRODUCT_PICTURE)));
            product.setType(new Type(cursor.getInt(cursor.getColumnIndex(SqliteHelper.ID_TYPE))));

            stock.setStockid(cursor.getInt(cursor.getColumnIndex(SqliteHelper.ID_PRODUCT_STOCK)));
            stock.setProduct_price(cursor.getDouble(cursor.getColumnIndex(SqliteHelper.PRODUCT_PRICE)));
            stock.setProduct_quality(cursor.getInt(cursor.getColumnIndex(SqliteHelper.PRODUCT_QUALITY)));
            detailCart.setDetail_cart_id(cursor.getInt(cursor.getColumnIndex(SqliteHelper.DETAIL_CART_ID)));
            detailCart.setDetail_cart_quality(cursor.getInt(cursor.getColumnIndex(SqliteHelper.DETAIL_CART_QUALITY)));
            detailCart.setDetail_cart_price(cursor.getDouble(cursor.getColumnIndex(SqliteHelper.DETAIL_CART_PRICE)));
            detailCart.setDetail_cart_cost(cursor.getDouble(cursor.getColumnIndex(SqliteHelper.DETAIL_CART_COST)));
            detailCart.setDetail_cart_total(cursor.getDouble(cursor.getColumnIndex(SqliteHelper.DETAIL_CART_TOTAL)));

            stock.setProduct(product);
            detailCart.setStock(stock);
            detailCarts.add(detailCart);
        }
        cursor.close();

        return detailCarts;

    }

    public ArrayList<Stock> selectStockId(String sql, String[] arg) {
        ArrayList<Stock> stocks = new ArrayList<>();
        sqLiteDatabase = sqliteHelper.getReadableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery(sql, arg);
        while (cursor.moveToNext()) {
            Stock stock = new Stock();
            stock.setStockid(cursor.getInt(cursor.getColumnIndex(SqliteHelper.STOCK_ID)));
            stocks.add(stock);
        }
        cursor.close();
        return stocks;

    }

    public ArrayList<Integer> selectIdProduct() {
        ArrayList<Integer> idproducts = new ArrayList<>();
        sqLiteDatabase = sqliteHelper.getReadableDatabase();
        String sql = "select distinct " + SqliteHelper.PRODUCT_ID + " from " + SqliteHelper.PRODUCT_TB;
        Cursor cursor = sqLiteDatabase.rawQuery(sql, null);
        while (cursor.moveToNext()) {
            idproducts.add(cursor.getInt(cursor.getColumnIndex(SqliteHelper.PRODUCT_ID)));
        }
        cursor.close();
        return idproducts;
    }


    public ArrayList<Product> selectNameProduct(String sql, String[] arg){
        ArrayList<Product> products =new ArrayList<>();
        sqLiteDatabase =sqliteHelper.getReadableDatabase();
        Cursor cursor= sqLiteDatabase.rawQuery(sql,arg);
        while (cursor.moveToNext()){
            Product product=new Product();
            product.setName(cursor.getString(cursor.getColumnIndex(SqliteHelper.PRODUCT_NAME)));
            products.add(product);
        }

        cursor.close();
        return products;
    }


    public ArrayList<Stock> selectStock(String sql, String[] arg) {
        ArrayList<Stock> stocks = new ArrayList<>();
        sqLiteDatabase = sqliteHelper.getReadableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery(sql, arg);
        while (cursor.moveToNext()) {
            Stock stock = new Stock();
            Product product = new Product();
            Type type = new Type();
            type.setId(cursor.getInt(cursor.getColumnIndex(SqliteHelper.ID_TYPE)));
            type.setName(cursor.getString(cursor.getColumnIndex(SqliteHelper.TYPE_NAME)));

            product.setId(cursor.getInt(cursor.getColumnIndex(SqliteHelper.PRODUCT_ID)));
            product.setName(cursor.getString(cursor.getColumnIndex(SqliteHelper.PRODUCT_NAME)));
            product.setPathimage(cursor.getString(cursor.getColumnIndex(SqliteHelper.PRODUCT_PICTURE)));
            product.setType(type);

            stock.setStockid(cursor.getInt(cursor.getColumnIndex(SqliteHelper.STOCK_ID)));
            stock.setProduct_cost(cursor.getDouble(cursor.getColumnIndex(SqliteHelper.PRODUCT_COST)));
            stock.setProduct_price(cursor.getDouble(cursor.getColumnIndex(SqliteHelper.PRODUCT_PRICE)));
            stock.setProduct_quality(cursor.getInt(cursor.getColumnIndex(SqliteHelper.PRODUCT_QUALITY)));
            stock.setDate_fist_in(cursor.getLong(cursor.getColumnIndex(SqliteHelper.DATE_FIRST_IN)));
            stock.setProduct(product);
            stocks.add(stock);
        }
        cursor.close();
        return stocks;

    }

    public ArrayList<QualityProduct> selectQualInStock(String sql, String[] arg) {
        ArrayList<QualityProduct> qualityProducts = new ArrayList<>();
        sqLiteDatabase = sqliteHelper.getReadableDatabase();
        ArrayList<Integer> id =selectIdProduct();
        Cursor cursor = sqLiteDatabase.rawQuery(sql, arg);
        while (cursor.moveToNext()) {
            QualityProduct qualityProduct=
                    new QualityProduct(cursor.getInt(cursor.getColumnIndex(SqliteHelper.PRODUCT_QUALITY)),
                            cursor.getInt(cursor.getColumnIndex(SqliteHelper.ID_PRODUCT)));
            qualityProducts.add(qualityProduct);
        }


        for(int j=0;j<id.size();j++) {
            int qual =0;
            for (int i = 0; i < qualityProducts.size(); i++) {
                if(id.get(j)==qualityProducts.get(i).getIdproduct()){
                    qual+=qualityProducts.get(i).getQual();
                    qualityProducts.get(i).setQul(qual);
                }
            }
        }
        cursor.close();
        return qualityProducts;

    }

    public ArrayList<QualityProduct> selectQualInOrder(String sql, String[] arg) {
        ArrayList<QualityProduct> qualityProducts = new ArrayList<>();
        sqLiteDatabase = sqliteHelper.getReadableDatabase();
        ArrayList<Integer> id =selectIdProduct();
        Cursor cursor = sqLiteDatabase.rawQuery(sql, arg);
        while (cursor.moveToNext()) {
            QualityProduct qualityProduct=
                    new QualityProduct(cursor.getInt(cursor.getColumnIndex(SqliteHelper.DETAIL_CART_QUALITY)),
                            cursor.getInt(cursor.getColumnIndex(SqliteHelper.ID_PRODUCT)));
            qualityProducts.add(qualityProduct);
        }


        for(int j=0;j<id.size();j++) {
            int qual =0;
            for (int i = 0; i < qualityProducts.size(); i++) {
                if(id.get(j)==qualityProducts.get(i).getIdproduct()){
                    qual+=qualityProducts.get(i).getQual();
                    qualityProducts.get(i).setQul(qual);
                }
            }
        }
        cursor.close();
        return qualityProducts;

    }



    public ArrayList<Type> selectType(String sql, String[] arg) {
        ArrayList<Type> types = new ArrayList<>();
        sqLiteDatabase = sqliteHelper.getReadableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery(sql, arg);
        Log.i(TAG, String.valueOf(cursor.getCount()));

        while (cursor.moveToNext()) {
            Type type = new Type();
            type.setId(cursor.getInt(cursor.getColumnIndex(SqliteHelper.TYPE_ID)));
            type.setName(cursor.getString(cursor.getColumnIndex(SqliteHelper.TYPE_NAME)));
            types.add(type);
            Log.i(TAG, type.getName());
        }

        cursor.close();
        return types;

    }


    public void closeDatabase() {
        if(sqLiteDatabase!=null) {
            if (sqLiteDatabase.isOpen()) {
                sqLiteDatabase.close();
            }
        }
    }


}
