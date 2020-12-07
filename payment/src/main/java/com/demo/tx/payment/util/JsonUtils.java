package com.demo.tx.payment.util;

import com.google.gson.Gson;

public final class JsonUtils {
    private static Gson gson = new Gson();

    public static String toJson(Object o) {
        return gson.toJson(o);
    }

    public static <T> T fromJson(String json, Class<T> clazz) {
        return gson.fromJson(json, clazz);
    }

}
