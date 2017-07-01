package com.market.application.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;

import com.market.application.R;
import com.market.application.SharePreferenceHelper;
import com.market.application.adapter.TypeAdapter;
import com.market.application.database.DatabaseManagement;
import com.market.application.database.SqliteHelper;
import com.market.application.javaclass.Type;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    final private int REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS = 124;

    FloatingActionButton fab;
    SearchView searchView;
    TextView tvTitle;
    Context context = MainActivity.this;

    GridView typeview;
    TypeAdapter typeAdapter;
    ArrayList<Type> typeArrayList;
    LayoutInflater inflater;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Bundle bundle=getIntent().getExtras();
        boolean status =false;
        if(bundle!=null) {
            status = bundle.getBoolean("statuspass");
        }
        if(status!=true){
            SharePreferenceHelper sp = new SharePreferenceHelper(context);
            boolean statuspass = sp.getStatusPassSharePreference();
            if (statuspass) {
                Intent intent = new Intent(context, PassCodeActivity.class);
                intent.putExtra(PassCodeActivity.STATUS, PassCodeActivity.KEYPASS);
                startActivity(intent);
                finish();

            }
        }


        exportDatabaseFile(context,SqliteHelper.DB_NAME);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        typeview= (GridView) findViewById(R.id.catview);
        searchView = (SearchView) findViewById(R.id.search);
        tvTitle = (TextView) findViewById(R.id.title);

        setSupportActionBar(toolbar);


        typeview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent=new Intent(context,ProductActivity.class);
                intent.putExtra("ID_TYPE",typeArrayList.get(i).getId());
                intent.putExtra("NAME",typeArrayList.get(i).getName());
                startActivity(intent);
            }
        });

        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, AddProductActivity.class);
                startActivity(intent);

            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    private void setGridView(){
        DatabaseManagement db = new DatabaseManagement(context);
        String sql = "select * from " + SqliteHelper.TYPE_TB + " where " + SqliteHelper.ID_DISABLE + "=?";
        typeArrayList = db.selectType(sql, new String[]{String.valueOf(1)});
        db.closeDatabase();
        typeAdapter = new TypeAdapter(context, typeArrayList);
        typeview.setAdapter(typeAdapter);

        inflater = LayoutInflater.from(context);


        ViewGroup viewGroup = (ViewGroup) typeview.getParent();
        View view = inflater.inflate(R.layout.empty_product,null,false);
        viewGroup.addView(view);
        typeview.setEmptyView(view);

    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_HOME);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.menu_cart) {
            Intent intent = new Intent(MainActivity.this, CartActivity.class);
            startActivity(intent);
        } else if (id == R.id.menu_stock) {
            Intent intent = new Intent(MainActivity.this, StockActivity.class);
            startActivity(intent);
        } else if (id == R.id.menu_dashboard) {
            Intent intent = new Intent(MainActivity.this, SumActivity.class);
            startActivity(intent);
        } else if (id == R.id.menu_history) {
            Intent intent = new Intent(MainActivity.this, HistoryActivity.class);
            startActivity(intent);

        } else if (id == R.id.menu_catalogue) {
            Intent intent = new Intent(MainActivity.this, ManageTypeActivity.class);
            startActivity(intent);

        } else if (id == R.id.menu_setting) {
            SharePreferenceHelper sp = new SharePreferenceHelper(context);
            boolean statuspass = sp.getStatusPassSharePreference();
            if (statuspass) {
                Intent intent = new Intent(MainActivity.this, PassCodeActivity.class);
                intent.putExtra(PassCodeActivity.STATUS, PassCodeActivity.GOTOSETPASS);
                startActivity(intent);
            } else {
                Intent intent = new Intent(MainActivity.this, SettingActivity.class);
                startActivity(intent);
            }
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        setGridView();

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        Log.i("KEYS", String.valueOf(keyCode));
        if (keyCode == KeyEvent.KEYCODE_H) {
            onBackPressed();
            return true;

        } else if (keyCode == KeyEvent.KEYCODE_BACK) {
            onBackPressed();
        }

        return true;

    }


    public boolean exportDatabaseFile(Context context, String dbName) {
        try {
            File dbFile = context.getDatabasePath(dbName);
            File exportFile = new File(Environment.getExternalStorageDirectory() + "/" + dbName);
            FileInputStream fileInputStream = new FileInputStream(dbFile);
            FileOutputStream fileOutputStream = new FileOutputStream(exportFile);
            FileChannel fileInputChannel = fileInputStream.getChannel();
            FileChannel fileOutputChannel = fileOutputStream.getChannel();
            fileInputChannel.transferTo(0, fileInputChannel.size(), fileOutputChannel);
            fileInputStream.close();
            fileOutputStream.close();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

}
