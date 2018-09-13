package com.higgsup.kpi.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class UtilsConvert {
    public static Integer convertDateToYearMonthInt(Date date) {
        DateFormat dateFormat = new SimpleDateFormat("yyyyMM");
        String strYearMonth = dateFormat.format(date);
        return Integer.valueOf(strYearMonth);
    }

    public static Date convertYearMonthIntToDate(Integer dateInt) {
        DateFormat dateFormat = new SimpleDateFormat("yyyyMM");
        Date date = null;
        try {
            date = dateFormat.parse(dateInt.toString());
        } catch (ParseException ignored) {

        }
        return date;
    }
}
