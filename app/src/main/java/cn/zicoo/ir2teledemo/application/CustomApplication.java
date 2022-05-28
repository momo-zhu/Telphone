package cn.zicoo.ir2teledemo.application;

import android.app.Application;
import android.os.Environment;

import org.kymjs.kjframe.KJDB;

import java.io.File;

import cn.zicoo.ir2teledemo.common.Config;
import cn.zicoo.ir2teledemo.common.Global;
import cn.zicoo.ir2teledemo.model.AppSetting;
import cn.zicoo.ir2teledemo.model.Customer;
import cn.zicoo.tele.TeleManager;

/**
 * Created by Tech07 on 2016/6/30.
 */
public class CustomApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            Config.DIRECTORY = Environment.getExternalStorageDirectory() + "/OrderCustomer";
            File file = new File(Config.DIRECTORY);
            if (!file.exists())
                file.mkdir();
        } else Config.DIRECTORY = getFilesDir().getPath();
//        iniDb();
//        Global.setting = Global.db.findById("AppSetting", AppSetting.class);
//        if (Global.setting == null) {
//            Global.setting = new AppSetting();
//            Global.setting.AppSettingId = "AppSetting";
//            Global.setting.PrinterCharset = "GBK";
//            Global.setting.PrinterPort = 9100;
//            Global.db.save(Global.setting);
//        }
//        Global.guest = new Customer();
//        Global.guest.Name = "Guest";
//        Global.guest.Telephone = "Guest";
//        Global.guest.OrderCount = Global.setting.GuestOrderCount;
//        Global.defaultSkus = Global.setting.ParseSkus();
//        Global.defaultProperties = Global.setting.ParseProperties();
//        TeleManager.setAuth("10ba2003cc7f817929f4eec1fa5481d1bb4f5d6f216edd01d87b4327df84f852");
        //SDK初始化。。
//        TeleManager.init(getApplicationContext());
    }


//    public User getUser() {
//        return user;
//    }
//
//    public void setUser(User user) {
//        this.user = user;
//    }


    /**/
    public void iniDb() {
//        userDb = KJDB.create(this, Config.USER_DB, false, Config.DB_VERSION, new KJDB.DbUpdateListener() {
//            @Override
//            public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
//            }
//        });
        Global.db = KJDB.create(this, Config.DB, false, Config.DB_VERSION, (db, oldVersion, newVersion) -> {
        });
        /*初始化用户信息*/
//        if (user == null) {
//            List<User> activatedUsers = userDb.findAllByWhere(User.class, null);
//            if (!activatedUsers.isEmpty()) {
//                user = activatedUsers.get(0);
//            }
//        }
//        user = new User();

    }

}
