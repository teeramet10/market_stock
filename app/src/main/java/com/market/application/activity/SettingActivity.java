package com.market.application.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.market.application.R;
import com.market.application.SharePreferenceHelper;

public class SettingActivity extends BaseActivity {
    LinearLayout changePass;
    LinearLayout lyminValue;
    TextView tvMinValue;
    Switch switchUsePass;
    Context context = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);



        changePass = (LinearLayout) findViewById(R.id.changepasscode);
        switchUsePass = (Switch) findViewById(R.id.switchpass);
        lyminValue = (LinearLayout) findViewById(R.id.valueproduct);
        tvMinValue = (TextView) findViewById(R.id.value);


        SharePreferenceHelper sp=new SharePreferenceHelper(context);
        int value =sp.getValueSharePreference();
        tvMinValue.setText(String.valueOf(value));

        switchUsePass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!switchUsePass.isChecked()) {
                    AlertDialog.Builder alert = new AlertDialog.Builder(context);
                    alert.setTitle(getResources().getString(R.string.wantdelpass));
                    alert.setPositiveButton(getResources().getString(R.string.ok), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            SharePreferenceHelper sp = new SharePreferenceHelper(context);
                            sp.delPassSharePreference();
                            sp.delStatusPassSharePreference();
                            switchUsePass.setChecked(false);
                        }
                    });

                    alert.setNegativeButton(getResources().getString(R.string.cancel), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            SharePreferenceHelper sp = new SharePreferenceHelper(context);
                            boolean statuspass = sp.getStatusPassSharePreference();
                            if (statuspass) {
                                switchUsePass.setChecked(true);
                            } else {
                                switchUsePass.setChecked(false);
                            }
                        }
                    });
                    alert.setCancelable(false);
                    alert.create().show();

                } else {
                    switchUsePass.setChecked(true);
                    Intent intent = new Intent(context, PassCodeActivity.class);
                    intent.putExtra(PassCodeActivity.STATUS, PassCodeActivity.USEPASS);
                    startActivity(intent);
                }
            }
        });


        changePass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharePreferenceHelper sp = new SharePreferenceHelper(context);
                boolean statuspass = sp.getStatusPassSharePreference();

                if (statuspass) {
                    Intent intent = new Intent(context, PassCodeActivity.class);
                    intent.putExtra(PassCodeActivity.STATUS, PassCodeActivity.CHANGEPASS);
                    startActivity(intent);
                } else {
                    Toast.makeText(context, getResources().getString(R.string.notusepass), Toast.LENGTH_SHORT).show();
                }

            }
        });

        lyminValue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final AlertDialog.Builder alert = new AlertDialog.Builder(context);
                View viewdialog = getLayoutInflater().inflate(R.layout.dialog_setvalue, null);
                final EditText edtMinValue = (EditText) viewdialog.findViewById(R.id.edtminvalue);
                alert.setCancelable(false);
                alert.setView(viewdialog);
                alert.setPositiveButton(context.getResources().getString(R.string.ok), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        if(!edtMinValue.getText().toString().equals("")) {
                            SharePreferenceHelper sp = new SharePreferenceHelper(context);
                            sp.putValuePassSharePreference(Integer.parseInt(edtMinValue.getText().toString()));
                            tvMinValue.setText(String.valueOf(sp.getValueSharePreference()));
                        }else {
                            Toast.makeText(context, getResources().getString(R.string.fillinform), Toast.LENGTH_SHORT).show();
                        }
                    }
                });

                alert.setNegativeButton(context.getResources().getString(R.string.cancel), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        alert.create().dismiss();
                    }
                });

                alert.create().show();
            }
        });

    }


    @Override
    protected void onResume() {
        super.onResume();

        SharePreferenceHelper sp = new SharePreferenceHelper(context);
        boolean statuspass = sp.getStatusPassSharePreference();

        if (statuspass) {
            switchUsePass.setChecked(true);
        } else {
            switchUsePass.setChecked(false);
        }
    }

}
