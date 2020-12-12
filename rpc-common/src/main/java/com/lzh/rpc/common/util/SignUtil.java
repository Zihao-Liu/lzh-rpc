package com.lzh.rpc.common.util;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.Map;
import java.util.TreeMap;

/**
 * @author Liuzihao
 * Created on 2020-11-06
 */
public class SignUtil {
    private SignUtil() {
    }

    public static <T> String getSignMd5(Long time, Integer appId, String appToken, T param) {
        String paramsJson = JsonUtil.toJson(param);
        Map<String, Object> treeMap = new TreeMap<>();
        treeMap.put("timestamp", time);
        treeMap.put("appId", appId);
        treeMap.put("appToken", appToken);
        treeMap.put("param", paramsJson);
        return MD5(JsonUtil.toJson(treeMap)).toUpperCase();

    }

    private static String MD5(String s) {
        char[] hexDigits = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};

        try {
            // 按照相应编码格式获取byte[]
            byte[] btInput = s.getBytes(StandardCharsets.UTF_8);
            // 获得MD5摘要算法的 MessageDigest 对象
            MessageDigest mdInst = MessageDigest.getInstance("MD5");
            // 使用指定的字节更新摘要
            mdInst.update(btInput);
            // 获得密文
            byte[] md = mdInst.digest();
            // 把密文转换成十六进制的字符串形式

            int j = md.length;
            char[] str = new char[j * 2];
            int k = 0;

            for (byte byte0 : md) {
                str[k++] = hexDigits[byte0 >>> 4 & 0xf];
                str[k++] = hexDigits[byte0 & 0xf];
            }
            return new String(str);
        } catch (Exception e) {
            return "-1";
        }
    }

}
