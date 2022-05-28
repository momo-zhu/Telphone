package cn.zicoo.ir2teledemo.common;

import android.util.Base64;

import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.TimeZone;

/**
 * Created by Admin on 2017/7/13.
 */

public class Config {
    public static  final  String PHONE_INCOMING = "android.intent.action.PHONE_STATE2";
    public static  final  String HOOK_CTRL = "zicoo.tele.HOOK_CTRL";

    /*数据相关*/
    public static final String APP_Version = "1.4";
    public static final int DB_VERSION = 1;
    public static final String USER_DB = "OrderCustomer.USER";
    public static final String DB = "OrderCustomer.Data";

    /*权限申请*/
    public static final int REQUEST_PERMISSION = 127;
    public static final int REQUEST_PERMISSION_STORAGE = 128;
    public static final int REQUEST_PERMISSION_CAMERA = 129;
    public static final int REQUEST_PERMISSION_AUDIO = 130;
    public static final int REQUEST_PERMISSION_STATE = 129;
    public static final int REQUEST_PERMISSION_LOCATION = 144;



    /*格式化*/
    public static SimpleDateFormat YEAR_FORMAT = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
    public static SimpleDateFormat DATE_DETAIL = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
    public static SimpleDateFormat UTC_Format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
    public static long fileMaxSize = 500000;

    public static String DIRECTORY;


    static {
        UTC_Format.setTimeZone(TimeZone.getTimeZone("UTC"));
    }

    /*果儿验证*/
    public static String basicGEAuth(String token) {
        try {
            return Base64.encodeToString(token.getBytes("utf-8"), Base64.NO_WRAP);
        } catch (UnsupportedEncodingException e) {
            return "";
        }
    }

    /*基础验证*/
    public static String basicAuth(String userName, String token) {
        try {
            return "Basic " + Base64.encodeToString((userName + ":" + token).getBytes("utf-8"), Base64.NO_WRAP);
        } catch (UnsupportedEncodingException e) {
            return "";
        }
    }

}
