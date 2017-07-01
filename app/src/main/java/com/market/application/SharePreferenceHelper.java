package com.market.application;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by barbie on 4/9/2017.
 */

public class SharePreferenceHelper {
    public static final String FILE_SHARE ="SETTING_FILE";
    public static final String TYPE ="TYPE";
    public static final String ORDER="ORDER";
    public static final String STATUSPASSCODE ="statuspass";
    public static final String PASSCODE ="pass";
    public static final String VALUE ="value";


    Context context;

    public SharePreferenceHelper(Context context) {
        this.context = context;
    }

    public void putTypeSharePreference(Boolean booltype){
        SharedPreferences sp=context.getSharedPreferences(FILE_SHARE,Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putBoolean(TYPE,booltype);
        editor.commit();
    }

    public Boolean getTypeSharePreference(){
        SharedPreferences sp =context.getSharedPreferences(FILE_SHARE,Context.MODE_PRIVATE);
        boolean type =sp.getBoolean(TYPE,false);
        return type;
    }

    public void putOrderSharePreference(int idorder){
        SharedPreferences sp=context.getSharedPreferences(FILE_SHARE,Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putInt(ORDER,idorder);
        editor.commit();
    }

    public int getOrderSharePreference(){
        SharedPreferences sp =context.getSharedPreferences(FILE_SHARE,Context.MODE_PRIVATE);
        int order =sp.getInt(ORDER,-1);
        return order;
    }

    public void delOrderSharePreference(){
            SharedPreferences sp=context.getSharedPreferences(FILE_SHARE,Context.MODE_PRIVATE);
            SharedPreferences.Editor editor=sp.edit();
            editor.remove(ORDER);
            editor.apply();
    }

    public void putPassSharePreference(String pass){
        SharedPreferences sp=context.getSharedPreferences(FILE_SHARE,Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(PASSCODE,pass);
        editor.commit();
    }

    public String  getPassSharePreference(){
        SharedPreferences sp =context.getSharedPreferences(FILE_SHARE,Context.MODE_PRIVATE);
        String pass =sp.getString(PASSCODE,"");
        return pass;
    }

    public void delPassSharePreference(){
        SharedPreferences sp=context.getSharedPreferences(FILE_SHARE,Context.MODE_PRIVATE);
        SharedPreferences.Editor editor=sp.edit();
        editor.remove(PASSCODE);
        editor.apply();
    }

    public void putStatusPassSharePreference(boolean status){
        SharedPreferences sp=context.getSharedPreferences(FILE_SHARE,Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putBoolean(STATUSPASSCODE,status);
        editor.commit();
    }

    public boolean getStatusPassSharePreference(){
        SharedPreferences sp =context.getSharedPreferences(FILE_SHARE,Context.MODE_PRIVATE);
        boolean statuspass =sp.getBoolean(STATUSPASSCODE,false);
        return statuspass;
    }

    public void delStatusPassSharePreference(){
        SharedPreferences sp=context.getSharedPreferences(FILE_SHARE,Context.MODE_PRIVATE);
        SharedPreferences.Editor editor=sp.edit();
        editor.remove(STATUSPASSCODE);
        editor.apply();
    }

    public void putValuePassSharePreference(int value){
        SharedPreferences sp=context.getSharedPreferences(FILE_SHARE,Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putInt(VALUE,value);
        editor.commit();
    }

    public int getValueSharePreference(){
        SharedPreferences sp =context.getSharedPreferences(FILE_SHARE,Context.MODE_PRIVATE);
        int value =sp.getInt(VALUE,10);
        return value;
    }

}
