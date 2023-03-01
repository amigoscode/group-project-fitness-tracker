package com.project.trackfit.core.exception;
import java.util.regex.*;

public class MobileNumberValidator {
    //TODO: Use Custom Annotation Instead of This Class.

    public static boolean isValidMobileNo(String str) {

        Pattern pattern = Pattern.compile("(0/91)?[7-9][0-9]{9}");

        Matcher match = pattern.matcher(str);

        return (match.find() && match.group().equals(str));
    }
}
