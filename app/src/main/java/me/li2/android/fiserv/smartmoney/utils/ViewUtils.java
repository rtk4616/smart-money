/*
 *    Copyright (C) 2015 Haruki Hasegawa
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package me.li2.android.fiserv.smartmoney.utils;

import android.content.Context;
import android.os.Build;
import android.support.v4.view.ViewCompat;
import android.view.View;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import me.li2.android.fiserv.smartmoney.R;

public class ViewUtils {
    public static boolean hitTest(View v, int x, int y) {
        final int tx = (int) (ViewCompat.getTranslationX(v) + 0.5f);
        final int ty = (int) (ViewCompat.getTranslationY(v) + 0.5f);
        final int left = v.getLeft() + tx;
        final int right = v.getRight() + tx;
        final int top = v.getTop() + ty;
        final int bottom = v.getBottom() + ty;

        return (x >= left) && (x <= right) && (y >= top) && (y <= bottom);
    }

    public static boolean supportsViewElevation() {
        return (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP);
    }

    public static String moneyAmountFormat(Context context, double value) {
        return String.format(context.getString(R.string.account_balance_format), value);
    }


    private static final String DEFAULT_DATE_FORMATTER = "MM/dd/yyyy";
    public static final String DATE_FORMAT_hhmma = "hh:mma";

    public static String dateToString(Date date) {
        return dateToString(date, DEFAULT_DATE_FORMATTER);
    }

    /**
     *  Converts date to string values.
     *
     * @param date
     * @param format
     * @return
     */
    public static String dateToString(Date date, String format) {
        // DONOT use Locale.getDefault(), just display, for example, 02:02a or 02:02p,
        // instead of multiple language
        SimpleDateFormat sdf = new SimpleDateFormat(format, Locale.US);
        String result = sdf.format(date);
        if (format.equals(DATE_FORMAT_hhmma)) {
            if (result != null && result.length() > 0) {
                result = result.subSequence(0, result.length()-1).toString();
            }
        }
        return result;
    }
}
