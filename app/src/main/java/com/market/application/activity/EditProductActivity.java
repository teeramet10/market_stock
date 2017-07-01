package com.market.application.activity;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.Toolbar;
import android.text.InputFilter;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.market.application.ImageManage;
import com.market.application.R;
import com.market.application.TextDecimalFilter;
import com.market.application.adapter.TypeAdapter;
import com.market.application.database.DatabaseManagement;
import com.market.application.database.SqliteHelper;
import com.market.application.javaclass.Product;
import com.market.application.javaclass.Stock;
import com.market.application.javaclass.Type;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class EditProductActivity extends BaseActivity {

    DecimalFormat decimalFormat = new DecimalFormat("#,###.00");

    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
    SimpleDateFormat dateformat = new SimpleDateFormat("dd");
    SimpleDateFormat monthformat = new SimpleDateFormat("MM");
    SimpleDateFormat yearformat = new SimpleDateFormat("yyyy");

    Context context = this;
    Activity activity = EditProductActivity.this;

    Toolbar toolbar;
    FloatingActionButton fabSave;
    ImageButton imgAddpic;
    EditText edtName;
    EditText edtPrice;
    EditText edtQuality;
    EditText edtCost;
    Button btnDate;
    Spinner spnType;
    ArrayList<Type> typeArrayList;

    ImageManage mImageManage;
    Date date = new Date();

    Intent imageIntent;
    String path;
    TypeAdapter typeAdapter;
    ArrayList<Stock> stocks;
    long time;

    int id = 0;
    int productid = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_product);

        btnDate = (Button) findViewById(R.id.edtdate);
        edtCost = (EditText) findViewById(R.id.edtcost);
        edtQuality = (EditText) findViewById(R.id.edtquality);
        edtPrice = (EditText) findViewById(R.id.edtprice);
        edtName = (EditText) findViewById(R.id.edtname);
        spnType = (Spinner) findViewById(R.id.spinnertype);
        imgAddpic = (ImageButton) findViewById(R.id.addpic);
        fabSave = (FloatingActionButton) findViewById(R.id.fabedit);
        toolbar = (Toolbar) findViewById(R.id.toolbar);


        Intent intent = getIntent();
        id = intent.getIntExtra("stockid", 0);
        productid = intent.getIntExtra("productid", 0);


        String sql = "select * from " + SqliteHelper.STOCK_TB
                + " inner join " + SqliteHelper.PRODUCT_TB
                + " on " + SqliteHelper.PRODUCT_TB + "." + SqliteHelper.PRODUCT_ID
                + " = " + SqliteHelper.STOCK_TB + "." + SqliteHelper.ID_PRODUCT
                + " inner join " + SqliteHelper.TYPE_TB
                + " on " + SqliteHelper.TYPE_TB + "." + SqliteHelper.TYPE_ID
                + " = " + SqliteHelper.PRODUCT_TB + "." + SqliteHelper.ID_TYPE
                + " where " + SqliteHelper.STOCK_TB + "." + SqliteHelper.STOCK_ID + "=" + id;
        DatabaseManagement databaseManagement = new DatabaseManagement(context);
        stocks = databaseManagement.selectStock(sql, null);
        databaseManagement.closeDatabase();


        edtName.setText(stocks.get(0).getProduct().getName());
        edtCost.setText(decimalFormat.format(stocks.get(0).getProduct_cost()));
        edtPrice.setText(String.valueOf((int)stocks.get(0).getProduct_price()));
        edtQuality.setText(String.valueOf(stocks.get(0).getProduct_quality()));
        btnDate.setText(simpleDateFormat.format(stocks.get(0).getDate_fist_in()));
        if (stocks.get(0).getProduct().getPathimage() != null) {
            File file = new File(stocks.get(0).getProduct().getPathimage());
            if (file.exists()) {
                Picasso.with(context).load(file).fit().error(R.drawable.pack).into(imgAddpic);
            }
        } else {
            Picasso.with(context).load(R.drawable.pack).fit().into(imgAddpic);
        }



        edtCost.setFilters(new InputFilter[]{new TextDecimalFilter()});
        edtPrice.setFilters(new InputFilter[]{new TextDecimalFilter()});


        time = stocks.get(0).getDate_fist_in();
        typeAdapter();


        fabSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveData();

            }
        });


        imgAddpic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select File"), ImageManage.GALLERY_CODE);

            }
        });

        btnDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                datePicker();
            }
        });

    }

    private void saveData() {

        if (!edtName.getText().toString().equals("") && !edtQuality.getText().toString().equals("")
                && !edtPrice.getText().toString().equals("") && !edtCost.getText().toString().equals("")) {


            if (Double.valueOf(edtCost.getText().toString()) <= Double.valueOf(edtPrice.getText().toString())) {
                String name = edtName.getText().toString();
                int quality = Integer.parseInt(edtQuality.getText().toString().trim());
                double cost = Double.valueOf(edtCost.getText().toString().trim());
                double price = Double.valueOf(edtPrice.getText().toString().trim());
                int position = spnType.getSelectedItemPosition();

                String sql = "select * from " + SqliteHelper.PRODUCT_TB
                        + " where " + SqliteHelper.PRODUCT_NAME + "=? and " + SqliteHelper.ID_DISABLE + "=?";
                DatabaseManagement databaseManagement = new DatabaseManagement(context);
                ArrayList<Product> products = databaseManagement.selectNameProduct(sql, new String[]{name, String.valueOf(1)});
                databaseManagement.closeDatabase();
                if (products.size() == 0) {


                    Type type = typeAdapter.getItem(position);
                    String path = stocks.get(0).getProduct().getPathimage();

                    Product product = new Product(productid);
                    product.setName(name);
                    product.setType(type);
                    product.setPathimage(path);
                    if (imageIntent != null) {
                        mImageManage = new ImageManage(context, activity);
                        path = mImageManage.Gallery(imageIntent);
                        product.setPathimage(path);
                    }


                    databaseManagement.updateProduct(product);


                    Stock stock = new Stock();
                    stock.setStockid(id);
                    stock.setProduct_cost(cost);
                    stock.setProduct_quality(quality);
                    stock.setProduct_price(price);
                    stock.setDate_fist_in(date.getTime());

                    long idstock = databaseManagement.updateStock(stock);
                    databaseManagement.closeDatabase();


                    if (idstock != -1) {
                        finish();


                    } else {
                        Toast.makeText(context, getString(R.string.noteditor), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(context, getResources().getString(R.string.repeatname), Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(context, getString(R.string.noteditor), Toast.LENGTH_SHORT).show();
            }

        } else {
            Toast.makeText(context, getString(R.string.fillinform), Toast.LENGTH_SHORT).show();
        }
    }


    public void typeAdapter() {
        DatabaseManagement db = new DatabaseManagement(context);
        String sql = "select * from " + SqliteHelper.TYPE_TB + " where " + SqliteHelper.ID_DISABLE + "=?";
        typeArrayList = db.selectType(sql, new String[]{String.valueOf(1)});
        db.closeDatabase();
        typeAdapter = new TypeAdapter(context, typeArrayList);
        spnType.setAdapter(typeAdapter);

        for (int i = 0; i < typeArrayList.size(); i++) {
            if (typeArrayList.get(i).getId() == stocks.get(0).getProduct().getType().getId()) {
                spnType.setSelection(i);
            }
        }

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == ImageManage.GALLERY_CODE && resultCode == RESULT_OK && data != null) {

            imageIntent = data;
            imgAddpic.setScaleType(ImageView.ScaleType.CENTER_CROP);
            imgAddpic.setImageURI(data.getData());
        }


    }


    public void datePicker() {

        int year = Integer.valueOf(yearformat.format(time));
        int strmonth = Integer.valueOf(monthformat.format(time));
        int day = Integer.valueOf(dateformat.format(time));

        int month = strmonth - 1;

        DatePickerDialog datePicker = new DatePickerDialog(context, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar calendar = Calendar.getInstance();
                calendar.set(year, monthOfYear, dayOfMonth);
                date = calendar.getTime();
                time = calendar.getTimeInMillis();
                btnDate.setText(simpleDateFormat.format(date));


            }
        }, year, month, day);

        datePicker.show();

    }

}
