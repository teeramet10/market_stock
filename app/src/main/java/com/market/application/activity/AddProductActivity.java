package com.market.application.activity;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
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
import com.market.application.SharePreferenceHelper;
import com.market.application.TextDecimalFilter;
import com.market.application.adapter.TypeAdapter;
import com.market.application.database.DatabaseManagement;
import com.market.application.database.SqliteHelper;
import com.market.application.javaclass.Product;
import com.market.application.javaclass.Stock;
import com.market.application.javaclass.Type;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;


public class AddProductActivity extends BaseActivity {
    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
    Context context = this;
    Activity activity = AddProductActivity.this;

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

    int setdate ;

    ImageManage mImageManage;
    Date date = new Date();

    Intent imageIntent;
    String path;
    TypeAdapter typeAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_product);
        btnDate = (Button) findViewById(R.id.edtdate);
        edtCost = (EditText) findViewById(R.id.edtcost);
        edtQuality = (EditText) findViewById(R.id.edtquality);
        edtPrice = (EditText) findViewById(R.id.edtprice);
        edtName = (EditText) findViewById(R.id.edtname);
        spnType = (Spinner) findViewById(R.id.spinnertype);
        imgAddpic = (ImageButton) findViewById(R.id.addpic);
        fabSave = (FloatingActionButton) findViewById(R.id.fabedit);
        toolbar = (Toolbar) findViewById(R.id.toolbar);

        Calendar calendar =Calendar.getInstance();
        setdate =calendar.get(Calendar.DAY_OF_MONTH);
        btnDate.setText(simpleDateFormat.format(System.currentTimeMillis()));
        typeAdapter();

        setSupportActionBar(toolbar);
        SharePreferenceHelper sp = new SharePreferenceHelper(context);
        boolean type = sp.getTypeSharePreference();
        if (!type) {
            Intent intent = new Intent(context, ManageTypeActivity.class);
            startActivity(intent);
            finish();
        }

        edtCost.setFilters(new InputFilter[]{new TextDecimalFilter()});
        edtPrice.setFilters(new InputFilter[]{new TextDecimalFilter()});

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
            public void onClick(View v) {
                datePicker();
            }
        });

        fabSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!edtName.getText().toString().equals("") && !edtQuality.getText().toString().equals("")
                        && !edtPrice.getText().toString().equals("") && !edtCost.getText().toString().equals("")) {

                    if(Double.valueOf(edtCost.getText().toString())<=Double.valueOf(edtPrice.getText().toString())) {
                        String name = edtName.getText().toString().trim();

                        String sql="select * from "+SqliteHelper.PRODUCT_TB
                                +" where "+SqliteHelper.PRODUCT_NAME+"=? and "+SqliteHelper.ID_DISABLE+"=?";
                        DatabaseManagement databaseManagement = new DatabaseManagement(context);
                        ArrayList<Product> products=databaseManagement.selectNameProduct(sql,new String[]{name,String.valueOf(1)});

                        int quality = Integer.parseInt(edtQuality.getText().toString().trim());
                        double cost = Double.valueOf(edtCost.getText().toString().trim());
                        double price = Double.valueOf(edtPrice.getText().toString().trim());
                        int position = spnType.getSelectedItemPosition();
                        Type type = typeAdapter.getItem(position);

                        if(products.size()==0) {
                            Product product = new Product();
                            product.setName(name);
                            product.setType(type);
                            if (imageIntent != null) {
                                mImageManage = new ImageManage(context, activity);
                                String path = mImageManage.Gallery(imageIntent);
                                product.setPathimage(path);
                            }


                            long idproduct = databaseManagement.insertProduct(product);
                            Product productId = new Product((int) idproduct);

                            Stock stock = new Stock();
                            stock.setProduct(productId);
                            stock.setProduct_cost(cost);
                            stock.setProduct_quality(quality);
                            stock.setProduct_price(price);
                            stock.setDate_fist_in(date.getTime());

                            long idstock = databaseManagement.insertStock(stock);
                            databaseManagement.closeDatabase();

                            if (idstock != -1) {
                                finish();
                            } else {
                                Toast.makeText(context,getResources().getString(R.string.failed), Toast.LENGTH_SHORT).show();
                            }
                        }else{
                            Toast.makeText(context,getResources().getString(R.string.repeatname), Toast.LENGTH_SHORT).show();
                        }
                    }else {
                        Toast.makeText(context, getResources().getString(R.string.failed), Toast.LENGTH_SHORT).show();
                    }

                } else {
                    Toast.makeText(context, getResources().getString(R.string.fillinform), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void typeAdapter() {
        DatabaseManagement db = new DatabaseManagement(context);
        String sql = "select * from " + SqliteHelper.TYPE_TB+" where "+SqliteHelper.ID_DISABLE+"=?";
        typeArrayList = db.selectType(sql, new String[]{String.valueOf(1)});

        typeAdapter = new TypeAdapter(context, typeArrayList);
        spnType.setAdapter(typeAdapter);
        db.closeDatabase();

    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
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

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    public void datePicker() {
        Calendar cal=Calendar.getInstance();
        DatePickerDialog datePicker = new DatePickerDialog(context, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar calendar = Calendar.getInstance();
                calendar.set(year, monthOfYear, dayOfMonth);
                date = calendar.getTime();
                btnDate.setText(simpleDateFormat.format(date));

                setdate =dayOfMonth;
            }
        }, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), setdate);

        datePicker.show();

    }



}
