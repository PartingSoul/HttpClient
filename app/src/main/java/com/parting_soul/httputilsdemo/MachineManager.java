package com.parting_soul.httputilsdemo;

import android.Manifest;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.telephony.TelephonyManager;
import android.text.TextUtils;

import java.util.UUID;

/**
 * @author : ciba
 * @date : 2018/7/9
 * @description : 获取安卓设备唯一标识码，会使用外部存储，调用前请自行申请权限
 */

public class MachineManager {
    private static final String DEFAULT_ANDROID_ID = "9774d56d682e549c";
    private static final String MACHINE_ID = "MACHINE_ID";
    private static MachineManager instance;
    private String machine;

    public MachineManager() {
    }

    public static MachineManager instance() {
        if (instance == null) {
            synchronized (MachineManager.class) {
                if (instance == null) {
                    instance = new MachineManager();
                }
            }
        }
        return instance;
    }

    /**
     * 获取Machine
     */
    public String getMachine(@NonNull Context context) {
        // 如果内存中的值不为空，则获取内存中的值
        if (!TextUtils.isEmpty(machine)) {
            return machine;
        }
        // 如果内存中的值为空，则先获取SP持久化的值
        machine = getSpMachine(context);
        if (!TextUtils.isEmpty(machine)) {
            return machine;
        }
        // 如果本地持久化都为空，则获取新的值并本地持久化
        machine = getNewMachine(context);
        putSpMachine(context, machine);
        return machine;
    }

    private SharedPreferences getSP(Context context) {
        return context.getSharedPreferences(context.getPackageName(), Context.MODE_PRIVATE);
    }

    /**
     * 键值对类型持久化字符串
     *
     * @param value ：值
     */
    public void putSpMachine(Context context, String value) {
        try {
            getSP(context).edit().putString(MACHINE_ID, value).apply();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 根据键获取持久化的值
     *
     * @return ：值
     */
    public String getSpMachine(Context context) {
        try {
            return getSP(context).getString(MACHINE_ID, null);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 获取新的Machine值
     *
     * @param context
     * @return
     */
    public String getNewMachine(@NonNull Context context) {
        Context applicationContext = context.getApplicationContext();

        String imei = getIMEI(applicationContext);
        String mac = getMAC(applicationContext);
        String deviceId = getAndroidId(applicationContext);

        String id;
        if (mac != null) {
            if (deviceId != null) {
                id = mac + deviceId;
            } else {
                id = mac;
            }
        } else if (imei != null) {
            if (deviceId != null) {
                id = imei + deviceId;
            } else {
                id = imei;
            }
        } else {
            id = getUUID();
        }
        id = MD5Util.md5(id);
        return id;
    }

    public String getUUID() {
        return UUID.randomUUID().toString();
    }

    public String getIMEI(@NonNull Context context) {
        try {
            TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            if (ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
                return null;
            }
            return tm.getDeviceId();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public String getMAC(@NonNull Context context) {
        try {
            WifiManager wifi = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
            WifiInfo info = wifi.getConnectionInfo();
            return info.getMacAddress();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public String getAndroidId(@NonNull Context context) {
        try {
            String androidId = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
            if (!DEFAULT_ANDROID_ID.equals(androidId)) {
                return androidId;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}