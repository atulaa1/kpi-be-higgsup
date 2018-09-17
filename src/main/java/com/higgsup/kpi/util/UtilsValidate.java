package com.higgsup.kpi.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UtilsValidate {

    public static Boolean containRegex(String str) {
        Pattern pattern = Pattern.compile("^((?!.*\\..*\\.)(?!.*_.*_)[A-Za-z0-9_.])*$");
        Matcher matcher = pattern.matcher(str);
        return !matcher.matches();
    }

    public static Boolean isValidPhoneNumber(String str) {
        Pattern pattern = Pattern.compile("^[0-9]{0,11}$");
        Matcher matcher = pattern.matcher(str);
        return matcher.matches();
    }

    public static Boolean isValidEmail(String str) {
        Pattern pattern = Pattern.compile("^[\\w-\\+]+(\\.[\\w]+)*@[\\w-]+(\\.[\\w]+)*(\\.[a-z]{2,})$");
        Matcher matcher = pattern.matcher(str);
        return matcher.matches();
    }

    public static Boolean isValidPoint(String str) {
        Pattern pattern = Pattern.compile("^([0-9]{0,2}[.])?[0-9]{0,2}");
        Matcher matcher = pattern.matcher(str);
        return matcher.matches();
    }

    public static Boolean isValidLateTimeNumber(String str){
        Pattern pattern = Pattern.compile("\\d+[.][0]{1}");
        Matcher matcher = pattern.matcher(str);
        return matcher.matches();
    }
}
