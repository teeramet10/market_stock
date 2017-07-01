package com.market.application.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;

import com.market.application.R;
import com.market.application.adapter.HistoryAdapter;
import com.market.application.database.DatabaseManagement;
import com.market.application.database.SqliteHelper;
import com.market.application.javaclass.Order;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

import static com.market.application.R.id.year;

public class HistoryActivity extends BaseActivity {
    SimpleDateFormat yearformat = new SimpleDateFormat("yyyy");
    SimpleDateFormat monthformat = new SimpleDateFormat("MM");
    public String[] MONTH = new String[12];
    ArrayList<Order> orders;
    Context context = this;
    ListView orderView;
    LayoutInflater inflater;
    HistoryAdapter historyAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        orderView = (ListView) findViewById(R.id.historyview);

        String sql="";
        int month = Integer.parseInt(monthformat.format(System.currentTimeMillis()));
        int year = Integer.parseInt(yearformat.format(System.currentTimeMillis()));
        if (month >= 10) {
            sql = "select * from " + SqliteHelper.ORDER_TB + " where " + SqliteHelper.ID_STATUS + "=? and "
                    + "date(" + SqliteHelper.ORDER_DATE + "/1000,'unixepoch','localtime') between '" + year + "-" + month + "-01'"
                    + " and '" + year + "-" + month + "-" + getDateOfMonth(month)+"'";
        } else {
            sql = "select * from " + SqliteHelper.ORDER_TB + " where " + SqliteHelper.ID_STATUS + "=? and "
                    + "date(" + SqliteHelper.ORDER_DATE + "/1000,'unixepoch','localtime') between '" + year + "-0" + month + "-01'"
                    + " and '" + year + "-0" + month + "-" + getDateOfMonth(month)+"'";
        }

        DatabaseManagement db = new DatabaseManagement(context);
        orders = db.selectOrder(sql, new String[]{String.valueOf(2)});

        historyAdapter = new HistoryAdapter(context, orders);
        orderView.setAdapter(historyAdapter);

        inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.empty_history, null);

        ViewGroup viewGroup = (ViewGroup) orderView.getParent();
        viewGroup.addView(view);
        orderView.setEmptyView(view);

        orderView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(context, HistoryDetailActivity.class);
                intent.putExtra(SqliteHelper.ORDER_ID, orders.get(i).getOrder_id());
                intent.putExtra(SqliteHelper.ORDER_DATE, orders.get(i).getOrder_date());
                intent.putExtra(SqliteHelper.ORDER_DISCOUNT, orders.get(i).getDiscount());
                intent.putExtra(SqliteHelper.ORDER_TOTAL, orders.get(i).getOrder_total());
                intent.putExtra(SqliteHelper.ORDER_DATE, orders.get(i).getOrder_date());

                startActivity(intent);
            }
        });
        addMonth();


    }

    public void addMonth() {
        MONTH[0] = getResources().getString(R.string.jan);
        MONTH[1] = getResources().getString(R.string.feb);
        MONTH[2] = getResources().getString(R.string.march);
        MONTH[3] = getResources().getString(R.string.april);
        MONTH[4] = getResources().getString(R.string.may);
        MONTH[5] = getResources().getString(R.string.june);
        MONTH[6] = getResources().getString(R.string.july);
        MONTH[7] = getResources().getString(R.string.aug);
        MONTH[8] = getResources().getString(R.string.sep);
        MONTH[9] = getResources().getString(R.string.oct);
        MONTH[10] = getResources().getString(R.string.nov);
        MONTH[11] = getResources().getString(R.string.dec);

    }

    @Override
    public void onClick(View view) {
        super.onClick(view);

        switch (view.getId()) {
            case R.id.btncalendar:
                final android.support.v7.app.AlertDialog.Builder alert = new android.support.v7.app.AlertDialog.Builder(context);
                LayoutInflater inflater = LayoutInflater.from(context);
                View v = inflater.inflate(R.layout.dialog_date, null);

                final Spinner spnMonth = (Spinner) v.findViewById(R.id.month);
                final Spinner spnYear = (Spinner) v.findViewById(year);
                String[] month = new String[12];
                String[] year = new String[30];
                int yearvalue = Integer.valueOf(yearformat.format(System.currentTimeMillis()));

                for (int i = 0; i < 12; i++) {
                    month[i] = MONTH[i];
                }
                for (int i = 0; i < 30; i++) {
                    year[i] = String.valueOf(yearvalue);
                    yearvalue++;
                }
                final ArrayAdapter<String> yearAdapter = new ArrayAdapter<String>
                        (context, android.R.layout.simple_dropdown_item_1line, year);
                final ArrayAdapter<String> monthAdapter = new ArrayAdapter<String>
                        (context, android.R.layout.simple_dropdown_item_1line, month);
                spnMonth.setAdapter(monthAdapter);
                spnYear.setAdapter(yearAdapter);

//                for (int i = 0; i < 12; i++) {
//
//                }
                alert.setView(v);
                alert.setPositiveButton(getResources().getString(R.string.ok), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        int item = spnMonth.getSelectedItemPosition();
                        int yeari =Integer.valueOf(spnYear.getSelectedItem().toString());
                        item++;
                        setAdapter(item, yeari);

                        spnMonth.setSelection(i);
                        spnYear.setSelection(i);
                    }
                });

                alert.setNegativeButton(getResources().getString(R.string.cancel), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        alert.create().dismiss();
                    }
                });

                alert.create().show();

                break;
        }
    }

    public void setAdapter(int month, int year) {
        String sql = "";
        if (month >= 10) {
            sql = "select * from " + SqliteHelper.ORDER_TB + " where " + SqliteHelper.ID_STATUS + "=? and "
                    + "date(" + SqliteHelper.ORDER_DATE + "/1000,'unixepoch','localtime') between '" + year + "-" + month + "-01'"
                    + " and '" + year + "-" + month + "-" + getDateOfMonth(month)+"'";
        } else {
            sql = "select * from " + SqliteHelper.ORDER_TB + " where " + SqliteHelper.ID_STATUS + "=? and "
                    + "date(" + SqliteHelper.ORDER_DATE + "/1000,'unixepoch','localtime') between '" + year + "-0" + month + "-01'"
                    + " and '" + year + "-0" + month + "-" + getDateOfMonth(month)+"'";
        }
        DatabaseManagement db = new DatabaseManagement(context);
        orders = db.selectOrder(sql, new String[]{String.valueOf(2)});
        historyAdapter.setOrders(orders);
        historyAdapter.notifyDataSetChanged();
        db.closeDatabase();

    }

    private int getDateOfMonth(int month) {
        switch (month) {
            case 1:
                return 31;
            case 2:
                return 28;
            case 3:
                return 31;
            case 4:
                return 30;
            case 5:
                return 31;
            case 6:
                return 30;
            case 7:
                return 31;
            case 8:
                return 31;
            case 9:
                return 30;
            case 10:
                return 31;
            case 11:
                return 30;
            case 12:
                return 31;
            default:
                return 30;
        }

    }

}