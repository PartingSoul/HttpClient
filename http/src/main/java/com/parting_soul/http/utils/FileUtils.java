package com.parting_soul.http.utils;

import java.io.Closeable;
import java.io.IOException;

/**
 * @author parting_soul
 * @date 2018/1/27
 * IO操作工具类
 */
public class FileUtils {

    public static void closeQuietly(Closeable... closeables) {
        if (closeables != null) {
            Closeable closeable = null;
            for (int i = 0; i < closeables.length; i++) {
                closeable = closeables[i];
                if (closeable != null) {
                    try {
                        closeable.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    } finally {
                        closeables[i] = null;
                    }
                }
            }
        }
    }


}
