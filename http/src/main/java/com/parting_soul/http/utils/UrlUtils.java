package com.parting_soul.http.utils;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Map;

/**
 * @author parting_soul
 * @date 2019-07-22
 */
public class UrlUtils {
    public static final String UTF_8 = "UTF-8";

    public static String encode(String source) {
        return encode(source, UTF_8);
    }

    public static String encode(String source, String charSet) {
        String result = source;
        try {
            result = URLEncoder.encode(source, charSet);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return result;
    }


    /**
     * 拼接参数
     *
     * @param params
     * @return
     */
    public static String buildParams(Map<String, Object> params) {
        StringBuilder paramSb = new StringBuilder();
        if (params != null && !params.isEmpty()) {
            for (Map.Entry<String, Object> entry : params.entrySet()) {
                String key = encode(entry.getKey());
                Object value = entry.getValue();
                if (value instanceof String) {
                    value = encode(value.toString());
                }
                paramSb.append(key)
                        .append("=")
                        .append(value)
                        .append("&");
            }
        }
        if (paramSb.length() > 0) {
            paramSb.setLength(paramSb.length() - 1);
        }
        return paramSb.toString();
    }


}
