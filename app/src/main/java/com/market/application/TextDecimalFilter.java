package com.market.application;

import android.text.InputFilter;
import android.text.Spanned;
import android.text.method.DigitsKeyListener;

import java.util.regex.Pattern;

/**
 * Created by Barbie-Pc on 5/31/2017.
 */

public class TextDecimalFilter implements InputFilter {

    int beforeDecimal = 4, afterDecimal = 2;


    @Override
    public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
        StringBuilder builder = new StringBuilder(dest);
        builder.replace(dstart, dend, source
                .subSequence(start, end).toString());
        if (!builder.toString().matches(
                "(([1-9]{1})([0-9]{0,"+(beforeDecimal-1)+"})?)?(\\.[0-9]{0,"+afterDecimal+"})?"

        )) {
            if(source.length()==0)
                return dest.subSequence(dstart, dend);
            return "";
        }

        return null;

    }
}
