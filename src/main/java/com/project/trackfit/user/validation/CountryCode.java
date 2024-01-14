package com.project.trackfit.user.validation;

public enum CountryCode {
    GREECE("0030"),
    UNITED_KINGDOM("0044"),
    FRANCE("0033");


    private final String code;

    CountryCode(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }

    public static boolean isValidCode(String code) {
        for (CountryCode countryCode : CountryCode.values()) {
            if (countryCode.getCode().equals(code)) {
                return true;
            }
        }
        return false;
    }
}
