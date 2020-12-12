package com.lzh.rpc.common.util;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

/**
 * @author Liuzihao
 * Created on 2020-11-06
 */
public class JsonUtil {
    private JsonUtil() {
    }

    private static final Gson GSON = new Gson();

    public static <T> String toJson(T t) {
        return GSON.toJson(t);
    }

    public static <T> T fromJson(String str, TypeToken<T> typeToken) {
        return GSON.fromJson(str, typeToken.getType());
    }
}
