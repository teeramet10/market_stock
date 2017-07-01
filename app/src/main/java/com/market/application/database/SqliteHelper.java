package com.market.application.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Administrator on 8/3/2560.
 */
public class SqliteHelper extends SQLiteOpenHelper {
    public static final String DB_NAME = "db_name";
    public static final int db_version = 1;

    public static final String DISABLE_TB = "disable_tb";
    public static final String DISABLE_ID = "disable_id";
    public static final String DISABLE_NAME = "disable_name";

    private static final String CREATE_TABLE_DISABLE = "CREATE TABLE " + DISABLE_TB + "("
            + DISABLE_ID + " INTEGER(1) PRIMARY KEY AUTOINCREMENT,"
            + DISABLE_NAME + " VARCHAR(10) NOT NULL);";

    public static final String TYPE_TB = "type_tb";
    public static final String TYPE_ID = "type_id";
    public static final String TYPE_NAME = "type_name";
    public static final String ID_DISABLE = "id_disable";

    private static final String CREATE_TABLE_TYPE = "CREATE TABLE " + TYPE_TB + "("
            + TYPE_ID + " INTEGER(4) PRIMARY KEY AUTOINCREMENT,"
            + TYPE_NAME + " VARCHAR(40) NOT NULL,"
            + ID_DISABLE + " INTEGER(1) NOT NULL,"
            +" FOREIGN KEY ("+ID_DISABLE+") REFERENCES "+DISABLE_TB+"("+DISABLE_ID+")"
            +");";

    public static final String STATUS_TB = "status_tb";
    public static final String STATUS_ID = "status_id";
    public static final String STATUS_NAME = "status_name";

    private static final String CREATE_TABLE_STATUS = "CREATE TABLE " + STATUS_TB + "("
            + STATUS_ID + " INTEGER(1) PRIMARY KEY AUTOINCREMENT,"
            + STATUS_NAME + " VARCHAR(15) NOT NULL);";


    public static final String ORDER_TB = "order_tb";
    public static final String ORDER_ID = "order_id";
    public static final String ORDER_DATE = "order_date";
    public static final String ORDER_DISCOUNT = "order_discount";
    public static final String ORDER_TOTAL = "order_total";
    public static final String ID_STATUS = "id_status";

    private static final String CREATE_TABLE_ORDER = "CREATE TABLE " + ORDER_TB + "("
            + ORDER_ID + " INTEGER(4) PRIMARY KEY AUTOINCREMENT,"
            + ORDER_DATE + " LONG(13) NOT NULL,"
            + ORDER_DISCOUNT + " DOUBLE(4,2),"
            + ORDER_TOTAL + " DOUBLE(5,2),"
            + ID_STATUS + " INTEGER(1),"
            +" FOREIGN KEY ("+ID_STATUS+") REFERENCES "+STATUS_TB+"("+STATUS_ID+")"
            +");";

    public static final String PRODUCT_TB = "product_tb";
    public static final String PRODUCT_ID = "product_id";
    public static final String PRODUCT_NAME = "product_name";
    public static final String PRODUCT_PICTURE = "product_picture";
    public static final String ID_TYPE = "id_type";



    private static final String CREATE_TABLE_PRODUCT = "CREATE TABLE " + PRODUCT_TB + "("
            + PRODUCT_ID + " INTEGER(4) PRIMARY KEY AUTOINCREMENT,"
            + PRODUCT_NAME + " VARCHAR(40) NOT NULL,"
            + PRODUCT_PICTURE + " TEXT,"
            + ID_DISABLE + " INTEGER(1) NOT NULL,"
            + ID_TYPE + " INTEGER(4) NOT NULL,"
            +" FOREIGN KEY ("+ID_DISABLE+") REFERENCES "+DISABLE_TB+"("+DISABLE_ID+"),"
            +" FOREIGN KEY ("+ID_TYPE+") REFERENCES "+TYPE_TB+"("+TYPE_ID+")"
            +");";

    public static final String STOCK_TB = "stock_tb";
    public static final String STOCK_ID = "stock_id";
    public static final String ID_PRODUCT = "id_product";
    public static final String PRODUCT_COST = "product_cost";
    public static final String PRODUCT_PRICE = "product_price";
    public static final String PRODUCT_QUALITY = "product_quality";
    public static final String DATE_FIRST_IN = "date_first_in";


    private static final String CREATE_TABLE_STOCK = "CREATE TABLE " + STOCK_TB + "("
            + STOCK_ID + " INTEGER(4) PRIMARY KEY AUTOINCREMENT,"
            + ID_PRODUCT + " INTEGER(4) NOT NULL,"
            + PRODUCT_COST + " DOUBLE(4,2) NOT NULL,"
            + PRODUCT_PRICE + " DOUBLE(4,2) NOT NULL,"
            + PRODUCT_QUALITY + " INTEGER(4) NOT NULL,"
            + DATE_FIRST_IN + " LONG(13) NOT NULL,"
            +" FOREIGN KEY ("+ID_PRODUCT+") REFERENCES "+PRODUCT_TB+"("+PRODUCT_ID+")"
            +");";


    public static final String DETAIL_CART_TB = "detail_cart_tb";
    public static final String DETAIL_CART_ID = "detail_cart_id";
    public static final String ID_PRODUCT_STOCK = "id_product_stock";
    public static final String DETAIL_CART_QUALITY = "detail_cart_quality";
    public static final String DETAIL_CART_PRICE = "detail_cart_price";
    public static final String DETAIL_CART_TOTAL = "detail_cart_total";
    public static final String DETAIL_CART_COST = "detail_cart_cost";


    private static final String CREATE_TABLE_DETAIL_CART = "CREATE TABLE " + DETAIL_CART_TB + "("
            + DETAIL_CART_ID + " INTEGER(4) PRIMARY KEY AUTOINCREMENT,"
            + ID_PRODUCT_STOCK + " INTEGER(4) NOT NULL,"
            + DETAIL_CART_QUALITY + " INTEGER(4) NOT NULL,"
            + DETAIL_CART_PRICE + " DOUBLE(4,2) NOT NULL,"
            + DETAIL_CART_COST + " DOUBLE(4,2) NOT NULL,"
            + DETAIL_CART_TOTAL + " DOUBLE(5,2) NOT NULL,"
            +" FOREIGN KEY ("+ID_PRODUCT_STOCK+") REFERENCES "+STOCK_TB+"("+STOCK_ID+")"
            +");";

    public static final String CART_TB = "cart_tb";
    public static final String CART_ID = "cart_id";
    public static final String ID_ORDER_F = "id_order_f";
    public static final String ID_CART_DETAIL = "id_cart_detail";


    private static final String CREATE_TABLE_CART = "CREATE TABLE " + CART_TB + "("
            + CART_ID + " INTEGER(4) PRIMARY KEY AUTOINCREMENT,"
            + ID_ORDER_F + " INTEGER(4) NOT NULL,"
            + ID_CART_DETAIL + " INTEGER(4) NOT NULL,"
            +" FOREIGN KEY ("+ID_ORDER_F+") REFERENCES "+ORDER_TB+"("+ORDER_ID+"),"
            +" FOREIGN KEY ("+ID_CART_DETAIL+") REFERENCES "+DETAIL_CART_TB+"("+DETAIL_CART_ID+")"
            +");";




    public SqliteHelper(Context context) {
        super(context, DB_NAME, null, db_version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_DISABLE);
        db.execSQL(CREATE_TABLE_TYPE);
        db.execSQL(CREATE_TABLE_STATUS);
        db.execSQL(CREATE_TABLE_ORDER);
        db.execSQL(CREATE_TABLE_PRODUCT);
        db.execSQL(CREATE_TABLE_STOCK);
        db.execSQL(CREATE_TABLE_DETAIL_CART);
        db.execSQL(CREATE_TABLE_CART);



        int[] statusid = new int[]{1, 2};
        String[] status_name = new String[]{"กำลังดำเนินการ", "ชำระเงินแล้ว"};

        for (int i = 0; i < statusid.length; i++) {
            ContentValues cv = new ContentValues();
            cv.put(STATUS_ID, statusid[i]);
            cv.put(STATUS_NAME, status_name[i]);
            db.insert(STATUS_TB, null, cv);
        }


        int[] disableid = new int[]{1, 2};
        String[] disablename = new String[]{"enable", "disable"};
        for (int i = 0; i < disableid.length; i++) {
            ContentValues cv = new ContentValues();
            cv.put(DISABLE_ID, disableid[i]);
            cv.put(DISABLE_NAME, disablename[i]);
            db.insert(DISABLE_TB, null, cv);
        }
//        db.close();


    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + CREATE_TABLE_ORDER);
        db.execSQL("DROP TABLE IF EXISTS " + CREATE_TABLE_CART);
        db.execSQL("DROP TABLE IF EXISTS " + CREATE_TABLE_DETAIL_CART);
        db.execSQL("DROP TABLE IF EXISTS " + CREATE_TABLE_STOCK);
        db.execSQL("DROP TABLE IF EXISTS " + CREATE_TABLE_PRODUCT);
        db.execSQL("DROP TABLE IF EXISTS " + CREATE_TABLE_TYPE);
        db.execSQL("DROP TABLE IF EXISTS " + CREATE_TABLE_STATUS);
        onCreate(db);
    }
}
