package com.market.application.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.market.application.R;

/**
 * Created by barbie on 4/9/2017.
 */

public class BaseActivity extends AppCompatActivity {
    public static final String[] menu = new String[]{"",""};
    Context context=BaseActivity.this;
    private boolean foreground;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String seedeatail=getResources().getString(R.string.seedetail);
        String addstock =getResources().getString(R.string.addproduct);
        menu[0]=seedeatail;
        menu[1]=addstock;
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnback:
                onBackPressed();
                break;
        }

    }


}
