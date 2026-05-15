package com.huaducdat.storemanagerclient.service;

public class TokenStorageService {

    private static String token;

    // =========================
    // SAVE
    // =========================

    public static void setToken(
            String value
    ) {

        token = value;
    }

    // =========================
    // GET
    // =========================

    public static String getToken() {

        return token;
    }

    // =========================
    // CLEAR
    // =========================

    public static void clear() {

        token = null;
    }
}