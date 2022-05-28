package cn.zicoo.ir2teledemo.common;

import android.util.SparseArray;

import org.kymjs.kjframe.KJDB;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.zicoo.ir2teledemo.model.AppSetting;
import cn.zicoo.ir2teledemo.model.Customer;
import cn.zicoo.ir2teledemo.model.FoodMenu;
import cn.zicoo.ir2teledemo.model.Category;
import cn.zicoo.ir2teledemo.model.Place;
import cn.zicoo.ir2teledemo.model.Table;

public class Global {
    public static final String PHONE_INCOMING = "android.intent.action.PHONE_STATE2";
    public static final String HOOK_CTRL = "zicoo.tele.HOOK_CTRL";
    public static AppSetting setting;

    public static String[] defaultSkus;//默认规格
    public static List<String[]> defaultProperties; //默认属性

    public static List<Category> categories = new ArrayList<>(); //菜品分类
     public static List<Place> places_table = new ArrayList<>(); //桌子地址分类
    public static SparseArray<List<FoodMenu>> products = new SparseArray<>(); //所有菜品
    public static SparseArray<List<Table>> tables = new SparseArray<>(); //所有桌子
    public static SparseArray<Table> tableDict = new SparseArray<>();
    public static Map<String, String> customerPhoneNameDict = new HashMap<String, String>();//客户手机号姓名
    public static Map<Integer, String> customerIdNameDict = new HashMap<>();//客户Key姓名
    public static Customer guest;

    public static KJDB db;
}
