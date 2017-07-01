package com.market.application.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.market.application.R;
import com.market.application.SharePreferenceHelper;

public class PassCodeActivity extends AppCompatActivity implements View.OnClickListener {
    public static final String STATUS = "status";
    public static final String PASSCODE = "passcode";

    public static final String USEPASS = "usepass";
    public static final String CONFIRMPASS = "confirm";
    public static final String KEYPASS = "keypass";
    public static final String CHANGEPASS = "changepass";
    public static final String GOTOSETPASS = "gotosetpass";

    Button btnOne;
    Button btnTwo;
    Button btnThree;
    Button btnFour;
    Button btnFive;
    Button btnSix;
    Button btnSeven;
    Button btnEight;
    Button btnNine;
    Button btnZero;
    Button btnDel;
    Button btnEnter;
    TextView tvTitlePass;
    TextView tvInputpass;

    String status = "";
    String passcode = "";
    String getPasscode = KEYPASS;
    StringBuilder stb = new StringBuilder();


    Context context = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pass_code);
        btnOne = (Button) findViewById(R.id.one);
        btnTwo = (Button) findViewById(R.id.two);
        btnThree = (Button) findViewById(R.id.three);
        btnFour = (Button) findViewById(R.id.four);
        btnFive = (Button) findViewById(R.id.five);
        btnSix = (Button) findViewById(R.id.six);
        btnSeven = (Button) findViewById(R.id.seven);
        btnEight = (Button) findViewById(R.id.eight);
        btnNine = (Button) findViewById(R.id.nine);
        btnZero = (Button) findViewById(R.id.zero);
        btnDel = (Button) findViewById(R.id.del);
        btnEnter = (Button) findViewById(R.id.enter);
        tvTitlePass = (TextView) findViewById(R.id.titlepass);
        tvInputpass = (TextView) findViewById(R.id.inputpass);


        final Intent intent = getIntent();
        status = intent.getStringExtra(STATUS);
        if (status == null) {
            status = KEYPASS;
        }

        if (status.equals(CONFIRMPASS)) {
            getPasscode = intent.getStringExtra(PASSCODE);
        }

        if (status.equals(KEYPASS)) {
            setTextTitlePass(getResources().getString(R.string.keypass));
        } else if (status.equals(USEPASS)) {
            setTextTitlePass(getResources().getString(R.string.usepass));
        } else if (status.equals(CONFIRMPASS)) {
            setTextTitlePass(getResources().getString(R.string.confirmpass));
        } else if (status.equals(CHANGEPASS)) {
            setTextTitlePass(getResources().getString(R.string.changenewpass));
        } else if (status.equals(GOTOSETPASS)) {
            setTextTitlePass(getResources().getString(R.string.keypass));
        }

        setEvent();

        btnDel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (passcode.length() == 4) {
                    passcode = stb.deleteCharAt(3).toString();
                    setTextViewPass(passcode);
                    setEvent();
                } else if (passcode.length() == 3) {
                    passcode = stb.deleteCharAt(2).toString();
                    setTextViewPass(passcode);
                } else if (passcode.length() == 2) {
                    passcode = stb.deleteCharAt(1).toString();
                    setTextViewPass(passcode);
                } else if (passcode.length() == 1) {
                    passcode = stb.deleteCharAt(0).toString();
                    setTextViewPass(passcode);
                } else {
                    Toast.makeText(context, getResources().getString(R.string.notdel), Toast.LENGTH_SHORT).show();
                }
            }
        });


        btnEnter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (passcode.length() == 4) {
                    SharePreferenceHelper sp = new SharePreferenceHelper(context);

                    if (status.equals(USEPASS) || status.equals(CHANGEPASS)) {
                        Intent intent = new Intent(context, PassCodeActivity.class);
                        intent.putExtra(STATUS, CONFIRMPASS);
                        intent.putExtra(PASSCODE, passcode);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        finish();
                    } else if (status.equals(CONFIRMPASS)) {
                        if (getPasscode.equals(passcode)) {
                            sp.putStatusPassSharePreference(true);
                            sp.putPassSharePreference(passcode);
                            finish();
                        } else {
                            Toast.makeText(context,getResources().getString(R.string.wrongpass), Toast.LENGTH_SHORT).show();
                            finish();
                        }
                    } else if (status.equals(KEYPASS)) {
                        String passshare = sp.getPassSharePreference();
                        if (passshare.equals(passcode)) {
                            Intent intent1=new Intent(context,MainActivity.class);
                            intent1.putExtra("statuspass",true);
                            startActivity(intent1);
                            finish();
                        } else {
                            Toast.makeText(context,getResources().getString(R.string.wrongpass), Toast.LENGTH_SHORT).show();
                            setDefault();
                        }
                    } else if (status.equals(GOTOSETPASS)) {
                        String passshare = sp.getPassSharePreference();
                        if (passshare.equals(passcode)) {
                            Intent intent = new Intent(context, SettingActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);
                            finish();
                        } else {
                            Toast.makeText(context,getResources().getString(R.string.wrongpass), Toast.LENGTH_SHORT).show();
                            setDefault();
                        }
                    }


                } else {
                    Toast.makeText(context, getResources().getString(R.string.wrongpass), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void setEventNull() {
        btnOne.setOnClickListener(null);
        btnTwo.setOnClickListener(null);
        btnThree.setOnClickListener(null);
        btnFour.setOnClickListener(null);
        btnFive.setOnClickListener(null);
        btnSix.setOnClickListener(null);
        btnSeven.setOnClickListener(null);
        btnEight.setOnClickListener(null);
        btnNine.setOnClickListener(null);
        btnZero.setOnClickListener(null);
    }

    private void setEvent() {
        btnOne.setOnClickListener(this);
        btnTwo.setOnClickListener(this);
        btnThree.setOnClickListener(this);
        btnFour.setOnClickListener(this);
        btnFive.setOnClickListener(this);
        btnSix.setOnClickListener(this);
        btnSeven.setOnClickListener(this);
        btnEight.setOnClickListener(this);
        btnNine.setOnClickListener(this);
        btnZero.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        String charpass = ((Button) v).getText().toString();

        if (passcode.length() == 0) {
            passcode = stb.append(charpass).toString();
            setTextViewPass(passcode);
        } else if (passcode.length() == 1) {
            passcode = stb.append(charpass).toString();
            setTextViewPass(passcode);
        } else if (passcode.length() == 2) {
            passcode = stb.append(charpass).toString();
            setTextViewPass(passcode);
        } else if (passcode.length() == 3) {
            passcode = stb.append(charpass).toString();
            setTextViewPass(passcode);
            setEventNull();
        }

    }

    public void setDefault() {
        setEvent();
        stb = new StringBuilder();
        passcode = "";
        tvInputpass.setText("");

    }

    public void setTextViewPass(String pass) {
        tvInputpass.setText(pass);
    }

    public void setTextTitlePass(String title) {
        tvTitlePass.setText(title);
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (status.equals(KEYPASS)) {
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_HOME);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
        } else if (status.equals(GOTOSETPASS)) {
            finish();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        setDefault();
    }
}
