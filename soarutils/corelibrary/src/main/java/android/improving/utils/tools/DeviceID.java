package android.improving.utils.tools;

import android.content.Context;
import android.provider.Settings;
import android.telephony.TelephonyManager;

import java.util.UUID;

/**
 * Auth：yujunyao
 * Since: 2016/3/15 15:21
 * Email：yujunyao@yonglibao.com
 */
public class DeviceID {
    private final static String TAG = "DeviceID";

    /**获取设备唯一标识*/
    public static String getDeviceId(Context context) {
        String deviceUniqueId = "";

        //step1 获取IMEI
        TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        String tmDevice = tm.getDeviceId() == null ? "" : tm.getDeviceId();
        String tmSerial = tm.getSimSerialNumber() == null ? "" : tm.getSimSerialNumber();

        //step2 ANDROID_ID
        String tempAndroidId = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
        String androidId = tempAndroidId == null ? "" : tempAndroidId;

        //step3
        UUID deviceUUID = new UUID(androidId.hashCode(), ((long)tmDevice.hashCode()<< 32) | tmSerial.hashCode());

        deviceUniqueId = deviceUUID.toString();

        LogPrint.iLogPrint(TAG, deviceUniqueId);

        return MD5.MD5Encrypt(deviceUniqueId);
    }

}
