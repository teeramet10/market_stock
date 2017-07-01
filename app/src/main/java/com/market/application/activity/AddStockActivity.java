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
import android.widget.TextView;
import android.widget.Toast;

import com.market.application.R;
import com.market.application.SharePreferenceHelper;
import com.market.application.TextDecimalFilter;
import com.market.application.database.DatabaseManagement;
import com.market.application.database.SqliteHelper;
import com.market.application.javaclass.Product;
import com.market.application.javaclass.Stock;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by barbie on 5/2/2017.
 */

public class AddStockActivity extends BaseActivity {

    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
    Context context = this;
    Activity activity = AddStockActivity.this;

    Toolbar toolbar;
    FloatingActionButton fabSave;
    ImageButton imgAddpic;
    TextView tvName;
    EditText edtPrice;
    EditText edtQuality;
    EditText edtCost;
    Button btnDate;
    TextView tvType;

    int productid;
    Date date = new Date();



    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_stock);

        btnDate = (Button) findViewById(R.id.edtdate);
        edtCost = (EditText) findViewById(R.id.edtcost);
        edtQuality = (EditText) findViewById(R.id.edtquality);
        edtPrice = (EditText) findViewById(R.id.edtprice);
        tvName = (TextView) findViewById(R.id.tvname);
        tvType = (TextView) findViewById(R.id.tvtype);
        imgAddpic = (ImageButton) findViewById(R.id.addpic);
        fabSave = (FloatingActionButton) findViewById(R.id.fabedit);
        toolbar = (Toolbar) findViewById(R.id.toolbar);

        btnDate.setText(simpleDateFormat.format(System.currentTimeMillis()));

        setSupportActionBar(toolbar);
        SharePreferenceHelper sp = new SharePreferenceHelper(context);

        boolean type = sp.getTypeSharePreference();
        if (!type) {
            Intent intent = new Intent(context, ManageTypeActivity.class);
            startActivity(intent);
            finish();
        }

        Intent intent = getIntent();
        String name = intent.getStringExtra(SqliteHelper.PRODUCT_NAME);
        String types = intent.getStringExtra(SqliteHelper.TYPE_NAME);
        String pic = intent.getStringExtra(SqliteHelper.PRODUCT_PICTURE);
        productid =intent.getIntExtra(SqliteHelper.ID_PRODUCT, 0);

        tvName.setText(name);
        tvType.setText(types);

        if (pic != null) {
            File file = new File(pic);
            if (file.exists()) {
                Picasso.with(context).load(file).fit().error(R.drawable.pack).into(imgAddpic);
            }
        } else {
            Picasso.with(context).load(R.drawable.pack).fit().into(imgAddpic);
        }

        edtCost.setFilters(new InputFilter[]{new TextDecimalFilter()});
        edtPrice.setFilters(new InputFilter[]{new TextDecimalFilter()});

        btnDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                datePicker();
            }
        });

        fabSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!edtQuality.getText().toString().equals("") && !edtPrice.getText().toString().equals("")
                        && !edtCost.getText().toString().equals("")) {

                    int quality = Integer.parseInt(edtQuality.getText().toString());
                    double cost = Double.valueOf(edtCost.getText().toString());
                    double price = Double.valueOf(edtPrice.getText().toString());


                    DatabaseManagement databaseManagement = new DatabaseManagement(context);

                    Product product=new Product(productid);
                    Stock stock = new Stock();
                    stock.setProduct(product);
                    stock.setProduct_cost(cost);
                    stock.setProduct_quality(quality);
                    stock.setProduct_price(price);
                    stock.setDate_fist_in(date.getTime());

                    long value = databaseManagement.addStock(stock);
                    databaseManagement.closeDatabase();

                    if (value !=   -1) {
                        finish();
                    } else {
                        Toast.makeText(context,getResources().getString(R.string.failed), Toast.LENGTH_SHORT).show();
                    }

                } else {
                    Toast.makeText(context,getResources().getString(R.string.fillinform), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }



    public void datePicker() {
        Calendar cal=Calendar.getInstance();
        int setdate =cal.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog datePicker = new DatePickerDialog(context, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar calendar = Calendar.getInstance();
                calendar.set(year, monthOfYear, dayOfMonth);
                date = calendar.getTime();
                btnDate.setText(simpleDateFormat.format(date));
            }
        }, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH),setdate);

        datePicker.show();

    }
}
