package cn.zicoo.ir2teledemo;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.PowerManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.SparseArray;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.beardedhen.androidbootstrap.BootstrapButton;
import com.beardedhen.androidbootstrap.api.defaults.ButtonMode;
import com.beardedhen.androidbootstrap.api.defaults.DefaultBootstrapBrand;
import com.bigkoo.alertview.AlertView;
import com.bigkoo.alertview.OnItemClickListener;
import com.flyco.tablayout.SlidingTabLayout;
import com.rengwuxian.materialedittext.MaterialEditText;
import com.rengwuxian.materialedittext.validation.METValidator;

import org.apache.commons.lang3.StringUtils;
import org.apmem.tools.layouts.FlowLayout;
import org.kymjs.kjframe.KJActivity;
import org.kymjs.kjframe.KJDB;
import org.kymjs.kjframe.database.utils.TableInfo;
import org.kymjs.kjframe.ui.BindView;
import org.kymjs.kjframe.ui.ViewInject;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cn.pedant.SweetAlert.SweetAlertDialog;
import cn.zicoo.ir2teledemo.adapter.MyFragmentPagerAdapter;
import cn.zicoo.ir2teledemo.adapter.ProductSelectAdapter;
import cn.zicoo.ir2teledemo.adapter.TableFragmentPagerAdapter;
import cn.zicoo.ir2teledemo.base.AdapterListener;
import cn.zicoo.ir2teledemo.common.Config;
import cn.zicoo.ir2teledemo.common.DateUtils;
import cn.zicoo.ir2teledemo.common.Global;
import cn.zicoo.ir2teledemo.common.PrintClient;
import cn.zicoo.ir2teledemo.common.Util;
import cn.zicoo.ir2teledemo.fragment.FoodFragment;
import cn.zicoo.ir2teledemo.fragment.TableManagerFragment;
import cn.zicoo.ir2teledemo.fragment.TimePickerFragment;
import cn.zicoo.ir2teledemo.model.AppSetting;
import cn.zicoo.ir2teledemo.model.Customer;
import cn.zicoo.ir2teledemo.model.Order;
import cn.zicoo.ir2teledemo.model.OrderItem;
import cn.zicoo.ir2teledemo.model.FoodMenu;
import cn.zicoo.ir2teledemo.model.Category;
import cn.zicoo.ir2teledemo.model.FoodMenuProperty;
import cn.zicoo.ir2teledemo.model.FoodMenuItem;
import cn.zicoo.ir2teledemo.model.Place;
import cn.zicoo.ir2teledemo.model.Table;
import cn.zicoo.tele.Tele;
import cn.zicoo.tele.TeleManager;
import cn.zicoo.tele.TeleSettings;
import cn.zicoo.tele.TeleStateListener;

public class MainActivity extends KJActivity {
    public static final String TAG = "cn.zicoo.ir2teledemo.MainActivity";
    public static final int REQUEST_CUSTOMER = 100;
    public static final int REQUEST_CALL_RECORD = 101;
    public static final int REQUEST_PRODUCT_MANAGER = 102;
    public static final int REQUEST_TABLE = 103;

    private List<Table> mDatas;
    private String table_state;
    private int table_id;
    private int table_number;
    private int contains_number;

    //public TextView phoneNumber;
    //private TextView customer;
    @BindView(id = R.id.tableNo, click = true)
    public Button tableNo;
    @BindView(id = R.id.searchEditText)
    public MaterialEditText searchEditText;
    @BindView(id = R.id.customerName)
    public MaterialEditText customerName;
    @BindView(id = R.id.categoryTab)
    private SlidingTabLayout categoryTab;
    @BindView(id = R.id.categoryTab_table)
    private SlidingTabLayout categoryTab_table;
    //    @BindView(id = R.id.searchView)
//    private android.support.v7.widget.SearchView searchView;
    @BindView(id = R.id.viewPager)
    private ViewPager viewPager;
    @BindView(id = R.id.viewPager_table)
    private ViewPager viewPager_table;
    @BindView(id = R.id.foodRecycleView)
    private RecyclerView foodRecycleView;
    @BindView(id = R.id.totalPrice)
    private TextView totalPrice;
    //public ImageView answer;
    //public ImageView refuse;
    //private EditText description;
    @BindView(id = R.id.submit, click = true)
    private BootstrapButton submitButton;
    @BindView(id = R.id.dinnerTime, click = true)
    private BootstrapButton dinnerTimeButton;
    @BindView(id = R.id.reset, click = true)
    private BootstrapButton resetButton;
    @BindView(id = R.id.offhook, click = true)
    private BootstrapButton offhookButton;
    private SoundPool sp;
    private int music;

    //private Button addFood;
    @BindView(id = R.id.remark, click = true)
    private BootstrapButton remark;

    //private List<ProductCategory> categories = new List<ProductCategory>();
    private List<OrderItem> orderItems = new ArrayList<>();
    private List<Fragment> fragments = new ArrayList<>();
    private List<Fragment> fragments_table = new ArrayList<>();
    private MyFragmentPagerAdapter foodPagerAdapter;
    private TableFragmentPagerAdapter tablePagerAdapter;
    private ProductSelectAdapter foodSelectAdapter;

    private double totalAmount;

    //private BroadcastReceiver hookReceiver;

    private String currentPhone;
    private String description;

    private Date dinnerTime;

    private Customer currentCustomer;

    public android.app.Dialog phoneDialog;

    private Handler handler;
    private PowerManager pm;
    private PowerManager.WakeLock wl;
    private boolean isRinging;
    private AlertView alertView;
    private TextView phoneLabel;
    private int flag = 60;

    public void setCurrentPhone(String value) {
        if (Objects.equals(currentPhone, value))
            return;
        currentPhone = value;
        if (StringUtils.isBlank(value)) {
            currentCustomer = null;
            getSupportActionBar().setTitle(R.string.app_name);
            customerName.setVisibility(View.GONE);
            phoneLabel.setText("");
        } else {
            List<Customer> customers = Global.db.findAllByWhere(Customer.class, "Telephone='" + value + "'");
            if (!customers.isEmpty())
                currentCustomer = customers.get(0);
            else
                currentCustomer = null;
            if (currentCustomer != null) {
                getSupportActionBar().setTitle(currentCustomer.Name + "(" + currentCustomer.Telephone + ")");
                phoneLabel.setText(currentCustomer.Name + "\n" + currentCustomer.Telephone);
                customerName.setVisibility(View.GONE);
            } else {
                customerName.setVisibility(View.VISIBLE);
                getSupportActionBar().setTitle(value);
                phoneLabel.setText(value);

            }
        }
    }

    public void setTotalAmount(double value) {
        this.totalAmount = value;
        totalPrice.setText(getString(R.string.heji) + String.format("%.2f", totalAmount));
    }

    public void setDinnerTime(Date value) {
        this.dinnerTime = value;
        if (value == null)
            dinnerTimeButton.setText(R.string.dinner_time);
        else
            dinnerTimeButton.setText(DateUtils.formatDate(value, "HH:mm"));
    }

    @Override
    public void initData() {
        super.initData();
        if(Util.checkPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.CALL_PHONE, Manifest.permission.READ_PHONE_STATE, Manifest.permission.READ_CALL_LOG, Manifest.permission.RECEIVE_BOOT_COMPLETED)){
            initdb();
        }
        handler = new Handler(Looper.getMainLooper());

        sp = new SoundPool(10, AudioManager.STREAM_SYSTEM, 5);

        music = sp.load(this, R.raw.beep, 1);


        foodSelectAdapter = new ProductSelectAdapter(this, orderItems);
        foodSelectAdapter.setListener(new AdapterListener() {
            @Override
            public void click1(View view, int position) {
                OrderItem item = orderItems.get(position);
                setTotalAmount(totalAmount + item.Price);
                item.Quantity += 1;
                item.SubTotal = item.Price * item.Quantity;
                foodSelectAdapter.notifyItemChanged(position);
            }

            @Override
            public void click2(View view, int position) {
                if (orderItems.size() > position && position >= 0) {
                    OrderItem item = orderItems.get(position);
                    setTotalAmount(totalAmount - item.Price);

                    item.Quantity -= 1;
                    if (item.Quantity <= 0) {
                        orderItems.remove(position);
                        foodSelectAdapter.notifyItemRemoved(position);
                    } else {
                        item.SubTotal = item.Price * item.Quantity;
                        foodSelectAdapter.notifyItemChanged(position);
                    }
                }
            }
        });

        foodRecycleView.setAdapter(foodSelectAdapter);
        foodRecycleView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        foodRecycleView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
//        SearchView.SearchAutoComplete editText = searchView.findViewById(R.id.search_src_text);
//        editText.setMaxEms(3);
//        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
//            @Override
//            public boolean onQueryTextSubmit(String s) {
//                return false;
//            }
//
//            @Override
//            public boolean onQueryTextChange(String s) {
//                if (StringUtils.isNotBlank(s) && s.trim().length() == 3) {
//                    List<FoodMenu> foods = Global.db.findAllByWhere(FoodMenu.class, "Code = '" + s + "'");
//                    if (!foods.isEmpty()) {
//                        ViewInject.toast(getString(R.string.product_ordered) + foods.get(0).Name);
//                        orderProduct(foods.get(0));
//                        searchView.setQuery("", false);
//                        //searchView.ClearFocus(); // 不获取焦点
//                    }
//                }
//                return true;
//            }
//        });
        searchEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (StringUtils.isNotBlank(s.toString()) && s.toString().trim().length() == 3) {
                    List<FoodMenu> foods = Global.db.findAllByWhere(FoodMenu.class, "Code = '" + s + "'");
                    if (!foods.isEmpty()) {
                        ViewInject.toast(getString(R.string.product_ordered) + foods.get(0).Name);
                        orderProduct(foods.get(0));
                        searchEditText.setText("");
                        //searchView.ClearFocus(); // 不获取焦点
                    }
                }
            }
        });
        searchEditText.setOnEditorActionListener((v, actionId, event) -> {
            /*判断是否是“GO”键*/
            if (actionId == EditorInfo.IME_ACTION_SEARCH && searchEditText.getText() != null) {
                /*隐藏软键盘*/

                if (StringUtils.isNotBlank(searchEditText.getText().toString()) && searchEditText.getText().toString().trim().length() == 3) {
                    List<FoodMenu> foods = Global.db.findAllByWhere(FoodMenu.class, "Code = '" + searchEditText.getText().toString() + "'");
                    if (!foods.isEmpty()) {
                        ViewInject.toast(getString(R.string.product_ordered) + foods.get(0).Name);
                        orderProduct(foods.get(0));
                        searchEditText.setText("");
                        //searchView.ClearFocus(); // 不获取焦点
                    }
                }
                return true;
            }
            return false;
        });
        InitPhoneView();

        pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
        wl = pm.newWakeLock(PowerManager.SCREEN_BRIGHT_WAKE_LOCK | PowerManager.ACQUIRE_CAUSES_WAKEUP, "Ordercustomer:warklock");
        wl.acquire();
        wl.release();

        //屏幕解锁
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.flags |= WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED;
        lp.flags |= WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD;
        getWindow().setAttributes(lp);





        alertView = new AlertView(null, null, getString(R.string.cancel),
                new String[]{getString(R.string.product_manager), getString(R.string.default_sku), getString(R.string.default_property), getString(R.string.shop_config), getString(R.string.printer_config), getString(R.string.email_config), getString(R.string.table_config)},
                null, this, AlertView.Style.ActionSheet, new OnItemClickListener() {
            @Override
            public void onItemClick(Object o, int position) {
                switch (position) {
                    case 0:
                        Intent intent = new Intent(aty, ProductManagerActivity.class);
                        startActivityForResult(intent, REQUEST_PRODUCT_MANAGER);
                        break;
                    case 1:
                        SetDefaultSpecs();
                        break;
                    case 2:
                        SetDefaultProperties();
                        break;
                    case 3:
                        SetShopConfig();
                        break;
                    case 4:
                        SetPrinterConfig();
                        break;
                    case 5:
                        SetEmailAccount();
                        break;
                    case 6:
                        SetTableAccount();
                        break;
                }
            }
        }).setCancelable(true);
//        TeleManager.getInstance().listen(new MyTeleStateListener());
        if (getIntent() != null && getIntent().hasExtra("incoming_number")) {
            String state = getIntent().getStringExtra("state");
            String number = getIntent().getStringExtra("incoming_number");
            switch (state) {
                case Tele.RINGING://来电。。
                    if (StringUtils.isNotBlank(number)) {
                        setCurrentPhone(number);
                    }
                    isRinging = true;
                    break;
                case Tele.OFFHOOK://通话中
                    break;
                case "OUTGOING":
                    if (StringUtils.isNotBlank(number)) {
                        setCurrentPhone(number);

                    }
                    break;
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (isRinging) {
            wl.acquire();
            wl.release();
            isRinging = false;
            //answer.Visibility = ViewStates.Visible;
            if (!phoneDialog.isShowing())
                phoneDialog.show();
        }
    }

    @Override
    public void initDataFromThread() {
        super.initDataFromThread();
        while (Global.db == null && flag-- > 0) {
            try {
                Thread.sleep(300);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        if (Global.db != null) {
            Global.categories = Global.db.findAll(Category.class, "Sort");
            Global.places_table = Global.db.findAll(Place.class, "Sort");
            Global.products.clear();
            Global.tables.clear();
            List<FoodMenu> products = Global.db.findAll(FoodMenu.class, "Sort");
            List<FoodMenuItem> productSkus = Global.db.findAll(FoodMenuItem.class, "Sort");
            List<FoodMenuProperty> productProperties = Global.db.findAll(FoodMenuProperty.class);
            SparseArray<List<FoodMenuItem>> skuMap = new SparseArray<>();
            for (FoodMenuItem sku : productSkus) {
                if (skuMap.get(sku.FoodMenuId) == null)
                    skuMap.put(sku.FoodMenuId, new ArrayList<>());
                skuMap.get(sku.FoodMenuId).add(sku);
            }
            SparseArray<List<FoodMenuProperty>> propertyMap = new SparseArray<>();
            for (FoodMenuProperty property : productProperties) {
                if (propertyMap.get(property.FoodMenuId) == null)
                    propertyMap.put(property.FoodMenuId, new ArrayList<>());
                propertyMap.get(property.FoodMenuId).add(property);
            }

            for (FoodMenu p : products) {
                if (skuMap.get(p.FoodMenuId) != null)
                    p.ProductSkus = skuMap.get(p.FoodMenuId);
                if (propertyMap.get(p.FoodMenuId) != null)
                    p.ProductProperties = propertyMap.get(p.FoodMenuId);
                if (Global.products.get(p.CategoryId) == null)
                    Global.products.put(p.CategoryId, new ArrayList<>());
                Global.products.get(p.CategoryId).add(p);
            }
            List<Table> tables = Global.db.findAll(Table.class,"Sort");
            for (Table t : tables) {
                if (Global.tables.get(t.PlaceId) == null)
                    Global.tables.put(t.PlaceId, new ArrayList<>());
                Global.tables.get(t.PlaceId).add(t);

            }

            for (Table table : tables) {
                Global.tableDict.put(table.table_id, table);
            }
            List<Customer> customers = Global.db.findAll(Customer.class);
            for (Customer customer : customers) {
                Global.customerPhoneNameDict.put(customer.Telephone, customer.Name);
                Global.customerIdNameDict.put(customer.CustomerId, customer.Name);
            }
            Global.customerPhoneNameDict.put("Guest", "Guest");
            Global.customerIdNameDict.put(0, "Guest");
        }
    }

    @Override
    protected void threadDataInited() {
        InitCategoryAndFood();
    }

    private void InitCategoryAndFood() {
        fragments.clear();
        for (Category category : Global.categories) {
            FoodFragment foodFragment = new FoodFragment();
            Bundle bundle = new Bundle();
            bundle.putInt("categoryId", category.CategoryId);
            foodFragment.setArguments(bundle);
            fragments.add(foodFragment);
        }
        foodPagerAdapter = new MyFragmentPagerAdapter(getSupportFragmentManager(), fragments, Global.categories);
        viewPager.setAdapter(foodPagerAdapter);
        categoryTab.setViewPager(viewPager);
    }

//    private void InitPlaceAndTable() {
//        //categories = Global.db.Select(Global.db.From<ProductCategory>().OrderBy(c => c.Sort));
//        for (Place place :Global.places_table) {
//            TableManagerFragment tableFragment = new TableManagerFragment();
//            Bundle bundle = new Bundle();
//            bundle.putInt("placeId", place.PlaceId);
//            tableFragment.setArguments(bundle);
//            fragments_table.add(tableFragment);
//        }
//        tablePagerAdapter = new TableFragmentPagerAdapter(getSupportFragmentManager(), fragments_table, Global.places_table);
//        viewPager_table.setAdapter(tablePagerAdapter);
//        categoryTab_table.setViewPager(viewPager_table);
//    }

    public void orderProduct(FoodMenu item) {

        sp.play(music, 1, 1, 0, 0, 1);

        if (!item.hasSkus() && !item.hasSkus()) {
            setTotalAmount(totalAmount + item.Price);
            int index = -1;
            for (int i = 0; i < orderItems.size(); i++)
                if (orderItems.get(i).RelationId == item.FoodMenuId) {
                    index = i;
                    break;
                }
            if (index < 0) {
                OrderItem orderItem = new OrderItem();
                orderItem.Name = item.Name;
                orderItem.RelationId = item.FoodMenuId;
                orderItem.Price = item.Price;
                orderItem.SubTotal = item.Price;
                orderItem.Tax = orderItem.SubTotal * item.TaxRate / 100;
                orderItems.add(orderItem);
                foodSelectAdapter.notifyItemInserted(orderItems.size() - 1);
                foodRecycleView.scrollToPosition(orderItems.size() - 1);
            } else {
                OrderItem orderItem = orderItems.get(index);
                orderItem.Quantity += 1;
                orderItem.SubTotal = orderItem.Price * orderItem.Quantity;
                orderItem.Tax = orderItem.SubTotal * item.TaxRate / 100;
                foodSelectAdapter.notifyItemChanged(index);
            }
        } else {
            OrderItem orderItem = new OrderItem();
            orderItem.Name = item.Name;
            orderItem.RelationId = item.FoodMenuId;
            LinearLayout selectView = (LinearLayout) getLayoutInflater().inflate(R.layout.dialog_select_food, null);
            LinearLayout container = selectView.findViewById(R.id.container);
            TextView totalPrice = selectView.findViewById(R.id.totalPrice);
            TextView subtitle = selectView.findViewById(R.id.subtitle);
            BootstrapButton submit = selectView.findViewById(R.id.submit);


            //规格按钮
            List<BootstrapButton> specButtons = new ArrayList<>();
            //属性按钮
            List<List<BootstrapButton>> propertyButtons = new ArrayList<>();
            String[] propertyArray = new String[(item.hasSkus() ? 1 : 0) + (item.hasProperties() ? item.ProductProperties.size() : 0)];

            if (item.hasSkus()) {
                FlowLayout skuLayout = new FlowLayout(this);
                TextView textView = new TextView(this);
                textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
                textView.setText(R.string.spec);
                ViewGroup.MarginLayoutParams vp = new LinearLayout.MarginLayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                vp.setMargins(10, 2, 10, 2);
                container.addView(textView, vp);
                container.addView(skuLayout, vp);
                AddSkuView(skuLayout, item, orderItem, specButtons);
                for (int i = 0; i < specButtons.size(); i++) {
                    specButtons.get(i).setTag(i);
                    specButtons.get(i).setOnCheckedChangedListener((bootstrapButton, isChecked) -> {
                        for (BootstrapButton sb : specButtons)
                            if (sb != bootstrapButton && sb.isSelected())
                                sb.setSelectedWithoutEvent(false);
                        bootstrapButton.setSelectedWithoutEvent(true);
                        int index = (int) bootstrapButton.getTag();
                        totalPrice.setText(String.format("%.2f", item.ProductSkus.get(index).Price));
                        orderItem.Price = item.ProductSkus.get(index).Price;
                        orderItem.SubTotal = item.ProductSkus.get(index).Price;
                        orderItem.Tax = orderItem.SubTotal * item.TaxRate / 100;
                        propertyArray[0] = item.ProductSkus.get(index).Title;
                        orderItem.Properties = StringUtils.join(propertyArray, ",");
                        subtitle.setText("(" + orderItem.Properties + ")");

                    });
                }

                totalPrice.setText(String.format("%.2f", item.ProductSkus.get(0).Price));
                orderItem.Price = item.ProductSkus.get(0).Price;
                orderItem.SubTotal = item.ProductSkus.get(0).Price;
                orderItem.Tax = orderItem.SubTotal * item.TaxRate / 100;
                propertyArray[0] = item.ProductSkus.get(0).Title;
                subtitle.setText("(" + item.ProductSkus.get(0).Title + ")");
            } else {
                totalPrice.setText(String.format("%.2f", item.Price));
                orderItem.Price = item.Price;
                orderItem.SubTotal = item.Price;
                orderItem.Tax = orderItem.SubTotal * item.TaxRate / 100;

            }
            if (item.hasProperties()) {
                for (int index = 0; index < item.ProductProperties.size(); index++) {

                    TextView textView = new TextView(this);
                    textView.setTextSize(14);
                    textView.setText(item.ProductProperties.get(index).Name);
                    ViewGroup.MarginLayoutParams vp = new LinearLayout.MarginLayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                    vp.setMargins(10, 2, 10, 2);
                    container.addView(textView, vp);

                    String[] values = item.ProductProperties.get(index).ValueStr.split("[,，]");
                    List<BootstrapButton> buttons = new ArrayList<>(values.length);
                    propertyButtons.add(buttons);
                    FlowLayout propertyLayout = new FlowLayout(this);
                    container.addView(propertyLayout, vp);

                    AddPropertyView(propertyLayout, values, orderItem, buttons);
                    for (int i = 0; i < buttons.size(); i++) {
                        buttons.get(i).setTag(index);
                        buttons.get(i).setOnCheckedChangedListener((bootstrapButton, isChecked) -> {
                            for (BootstrapButton sb : buttons)
                                if (sb != bootstrapButton && sb.isSelected())
                                    sb.setSelectedWithoutEvent(false);
                            bootstrapButton.setSelectedWithoutEvent(true);
                            propertyArray[(int) bootstrapButton.getTag() + (item.hasSkus() ? 1 : 0)] = bootstrapButton.getText().toString();
                            orderItem.Properties = StringUtils.join(propertyArray, ",");
                            subtitle.setText("(" + orderItem.Properties + ")");
                        });

                    }
                    propertyArray[index + (item.hasSkus() ? 1 : 0)] = values[0];
                }

            }
            orderItem.Properties = StringUtils.join(propertyArray, ",");
            subtitle.setText("(" + orderItem.Properties + ")");


            AlertDialog dialog = new AlertDialog.Builder(this).setView(selectView).setTitle(item.Name).create();
            submit.setOnClickListener(v -> {
                setTotalAmount(totalAmount + orderItem.Price);
                int index = -1;
                for (int i = 0; i < orderItems.size(); i++)
                    if (orderItems.get(i).RelationId == item.FoodMenuId && Objects.equals(orderItems.get(i).Properties, orderItem.Properties)) {
                        index = i;
                        break;
                    }
                if (index < 0) {
                    orderItems.add(orderItem);
                    foodSelectAdapter.notifyItemInserted(orderItems.size() - 1);
                } else {
                    orderItems.get(index).Quantity += 1;
                    orderItems.get(index).SubTotal = orderItem.Price * orderItem.Quantity;
                    foodSelectAdapter.notifyItemChanged(index);
                }
                dialog.dismiss();
            });
            dialog.show();
        }


    }


    @Override
    public void onClick(View v) {
        if (v == submitButton) {
            confirmOrder();
        } else if (v == remark) {
            sp.play(music, 1, 1, 0, 0, 1);

            View addView = getLayoutInflater().inflate(R.layout.dialog_remark, null);
            EditText editText = addView.findViewById(R.id.description);
            editText.setText(description);
            new AlertDialog.Builder(this).setTitle(R.string.add_remark).setView(addView)
                    .setPositiveButton(getString(R.string.save), (dialog, which) -> description = editText.getText().toString())
                    .setNegativeButton(getString(R.string.cancel), null)
                    .show();//必须加这句，不然后面会报空指针错误
        } else if (v == resetButton) {
            Reset();
        } else if (v == offhookButton) {
            sp.play(music, 1, 1, 0, 0, 1);
            TeleManager.getInstance().endCall();
        } else if (v == dinnerTimeButton) {
            TimePickerFragment.newInstance(dinnerTime, date -> {
                dinnerTime = date;
                dinnerTimeButton.setText(DateUtils.formatDate(date, "HH:mm"));
            }).show(getSupportFragmentManager(), "TimePickerFragment");

        } else if (v == tableNo) {
            String[] tables = new String[Global.tableDict.size()];
            for (int i = 0; i < tables.length; i++) {
                tables[i] = Global.tableDict.valueAt(i).table_number + "(The Largest Number Of People:" + Global.tableDict.valueAt(i).contains_number + ")";
            }
            PopupMenu popup = new PopupMenu(aty, tableNo);
            for(int i=0;i<tables.length;i++) {
                popup.getMenu().add(0, i, i, "NO." + tables[i]);
            }
            popup.setOnMenuItemClickListener(menuItem -> {
                tableNo.setText(String.valueOf(Global.tableDict.valueAt(Integer.valueOf(menuItem.getItemId())).table_number));
                return true;
            });
            popup.show();
        }
    }

    private void confirmOrder() {
        if (StringUtils.isEmpty(currentPhone)) {
            currentCustomer = Global.guest;
            currentPhone = currentCustomer.Telephone;
//            ViewInject.toast(R.string.no_customer_info);
//            return;
        }
        if (orderItems.isEmpty()) {
            ViewInject.toast(R.string.order_first);
            return;
        }
        Pattern pattern = Pattern.compile("[0-9]*");
        Matcher isNum = pattern.matcher(tableNo.getText());
        if (!isNum.matches()) {
            ViewInject.toast(R.string.table_first);
            return;
        }
        sp.play(music, 1, 1, 0, 0, 1);
        //保存客户
        if (currentCustomer == null) {
            currentCustomer = new Customer();
            currentCustomer.CardNO = UUID.randomUUID().toString().replaceAll("-", "");
            currentCustomer.Name = StringUtils.isEmpty(customerName.getText()) ? "anonymous" : customerName.getText().toString();
            currentCustomer.Telephone = currentPhone;
            currentCustomer.OrderCount = 1;
            Global.db.saveBindId(currentCustomer);
            Global.customerPhoneNameDict.put(currentCustomer.Telephone, currentCustomer.Name);
            Global.customerIdNameDict.put(currentCustomer.CustomerId, currentCustomer.Name);

        } else {
            currentCustomer.OrderCount += 1;
            Global.setting.GuestOrderCount = currentCustomer.OrderCount;
            currentCustomer.UpdateDate = new Date();
            if (currentCustomer.CustomerId > 0)
                Global.db.update(currentCustomer, null, new String[]{"OrderCount", "UpdateDate"});
            else {
                Global.db.update(Global.setting, null, "GuestOrderCount");
            }
        }
        Order order = new Order();
//        order.UUId = UUID.randomUUID().toString().replaceAll("-", "");
        order.SubTotal = totalAmount;
        order.CustomerId = currentCustomer.CustomerId;
//        order.MemberName = currentCustomer.Name;
        order.Remark = description;
        order.Items = orderItems;
        Pattern pattern1 = Pattern.compile("[0-9]*");
        Matcher isNum1 = pattern1.matcher(tableNo.getText());

        if (!isNum1.matches()) {
            order.TableId = Integer.parseInt(tableNo.getText().toString());
        }
        order.DeliveryDate = dinnerTime == null ? new Date() : dinnerTime;
        order.TipsTotal = Global.setting.TipsRate * totalAmount / 100;

        double taxTotal = 0;
        for (OrderItem t : orderItems)
            taxTotal += t.Tax;
        order.TaxTotal = taxTotal;

        Global.db.saveBindId(order);
        for (OrderItem item : order.Items)
            item.OrderId = order.OrderId;
        Global.db.save(order.Items);
        ViewInject.toast(R.string.order_success);
        //打印
        PrintClient.Print(order, currentCustomer, this);

        Reset();
    }


    private void InitPhoneView() {
        phoneDialog = new android.app.Dialog(this, R.style.phone_dialog);

        View phoneView = getLayoutInflater().inflate(R.layout.dialog_phone, null);

        phoneLabel = phoneView.findViewById(R.id.phoneLabel);
        ImageView refuse = phoneView.findViewById(R.id.refuse);
        ImageView answer = phoneView.findViewById(R.id.answer);
        answer.setOnClickListener(v -> {
            sp.play(music, 1, 1, 0, 0, 1);
            TeleManager.getInstance().answerRingingCall();
            if (phoneDialog.isShowing())
                phoneDialog.dismiss();
        });
        refuse.setOnClickListener(v -> {
            sp.play(music, 1, 1, 0, 0, 1);

            TeleManager.getInstance().endCall();
            if (phoneDialog.isShowing())
                phoneDialog.dismiss();
        });
        phoneDialog.setCancelable(false);
        //设置dialog的宽高为屏幕的宽高
        ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(getResources().getDisplayMetrics().widthPixels, getResources().getDisplayMetrics().heightPixels);
        phoneDialog.setContentView(phoneView, layoutParams);
    }


    @Override
    public void onBackPressed() {
        moveTaskToBack(false);
    }

    //添加规格
    private void AddSkuView(FlowLayout vp, FoodMenu item, OrderItem orderItem, List<BootstrapButton> specButtons) {
        for (FoodMenuItem sku : item.ProductSkus) {
            BootstrapButton button = new BootstrapButton(this);
            button.setText(sku.Title);
            button.setButtonMode(ButtonMode.TOGGLE);
            button.setShowOutline(true);
            button.setRounded(true);
            button.setBootstrapBrand(DefaultBootstrapBrand.WARNING);
            FlowLayout.LayoutParams lp = new FlowLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            lp.setMargins(3, 2, 3, 2);
            vp.addView(button, lp);
            specButtons.add(button);
        }
        specButtons.get(0).setSelectedWithoutEvent(true);
        //var skuCheckBox = itemView.findViewById<CheckBox>(R.id.useSku);
    }

    //添加属性
    private void AddPropertyView(FlowLayout vp, String[] values, OrderItem orderItem, List<BootstrapButton> buttons) {
        for (String v : values) {

            BootstrapButton button = new BootstrapButton(this);
            button.setText(v);
            button.setButtonMode(ButtonMode.TOGGLE);
            button.setShowOutline(true);
            button.setRounded(true);
            button.setBootstrapBrand(DefaultBootstrapBrand.WARNING);
            FlowLayout.LayoutParams lp = new FlowLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            lp.setMargins(3, 2, 3, 2);
            vp.addView(button, lp);
            button.setOnCheckedChangedListener(new BootstrapButton.OnCheckedChangedListener() {
                @Override
                public void OnCheckedChanged(BootstrapButton bootstrapButton, boolean isChecked) {
                    for (BootstrapButton sb : buttons)
                        if (sb != bootstrapButton && sb.isSelected())
                            sb.setSelectedWithoutEvent(false);
                    bootstrapButton.setSelectedWithoutEvent(true);
                }
            });
            buttons.add(button);
        }
        buttons.get(0).setSelectedWithoutEvent(true);

        //var skuCheckBox = itemView.findViewById<CheckBox>(R.id.useSku);
    }

    //重置
    public void Reset() {
        orderItems.clear();
        foodSelectAdapter.notifyDataSetChanged();
        setTotalAmount(0);
        setCurrentPhone("");
        description = null;
        customerName.setVisibility(View.GONE);
        customerName.setText(null);
        tableNo.setText(R.string.table_no_label);
        setDinnerTime(null);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent e) {
        if (e.getAction() == MotionEvent.ACTION_DOWN) {
            //清除输入框焦点
            View focus = getCurrentFocus();
            if (focus != null) {
                boolean hideInputResult = isTouchInView(focus, (int) e.getRawX(), (int) e.getRawY());
                if (!hideInputResult) {
                    focus.clearFocus();
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    if (imm.isActive()) {
                        imm.hideSoftInputFromWindow(focus.getApplicationWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                    }
                }
            }
        }
        return super.dispatchTouchEvent(e);
    }


    private boolean isTouchInView(View view, int x, int y) {
        int[] location = new int[2];
        view.getLocationOnScreen(location);
        int left = location[0];
        int top = location[1];
        int right = left + view.getMeasuredWidth();
        int bottom = top + view.getMeasuredHeight();
        //view.isClickable() &&
        return y >= top && y <= bottom && x >= left
                && x <= right;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            alertView.show();
            return true;
        } else if (id == R.id.customer) {
            Intent intent = new Intent(getApplicationContext(), CustomerActivity.class);
            startActivityForResult(intent, REQUEST_CUSTOMER);
        } else if (id == R.id.callRecord) {
            Intent intent = new Intent(getApplicationContext(), CallRecordActivity.class);
            startActivityForResult(intent, REQUEST_CALL_RECORD);

        }
        return super.onOptionsItemSelected(item);
    }


    private void SetDefaultSpecs() {
        View addView = getLayoutInflater().inflate(R.layout.dialog_edit_text, null);
        MaterialEditText editText = addView.findViewById(R.id.editText);
        editText.setText(Global.setting.DefaultSkus);
        new AlertDialog.Builder(this).setTitle(R.string.default_sku).setView(addView)
                .setPositiveButton(getString(R.string.save), (dialog, which) -> {
                    InputMethodManager inputMethodManager = (InputMethodManager) aty.getSystemService(Context.INPUT_METHOD_SERVICE);
                    inputMethodManager.hideSoftInputFromWindow(editText.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                    if (StringUtils.isNotBlank(editText.getText().toString())) {
                        Global.setting.DefaultSkus = editText.getText().toString().trim().replace('，', ',');
                        Global.defaultSkus = Global.setting.DefaultSkus.split("[，,]");
                    } else {
                        Global.setting.DefaultSkus = null;
                        Global.defaultSkus = new String[0];
                    }
                    Global.db.update(Global.setting, null, "DefaultSkus");
                })
                .setNegativeButton(getString(R.string.cancel), (dialog, which) -> dialog.dismiss())
                .show();//必须加这句，不然后面会报空指针错误
    }

    @Override
    protected void onNewIntent(Intent intent) {
        if (intent.hasExtra("state")) {
            String state = intent.getStringExtra("state");
            String number = intent.getStringExtra("incoming_number");

            switch (state) {
                case Tele.IDLE://通话结束
                    if (phoneDialog.isShowing())
                        phoneDialog.dismiss();
                    break;
                case Tele.RINGING://来电。。
                    wl.acquire();
                    wl.release();
                    if (StringUtils.isNotBlank(number))
                        setCurrentPhone(number);
                    //answer.Visibility = ViewStates.Visible;
                    if (!phoneDialog.isShowing())
                        phoneDialog.show();
                    break;
                case Tele.OFFHOOK://通话中
                    if (phoneDialog.isShowing())
                        phoneDialog.dismiss();
                    break;
                case "OUTGOING":
                    if (StringUtils.isNotBlank(number))
                        setCurrentPhone(number);
                    //answer.Visibility = ViewStates.Gone;
                    //if (!phoneDialog.IsShowing)
                    //    phoneDialog.Show();
                    break;
            }
        }
        super.onNewIntent(intent);
    }

    //来电

    private void SetDefaultProperties() {
        List<String[]> properties = Global.setting.ParseProperties();
        LinearLayout linearLayout = (LinearLayout) getLayoutInflater().inflate(R.layout.dialog_add_sku, null);

        List<MaterialEditText> nameEditList = new ArrayList<>();
        List<MaterialEditText> valueEditList = new ArrayList<>();
        for (String[] item : properties) {
            AddDefaultPropertyView(linearLayout, item, nameEditList, valueEditList);

        }
        AlertDialog dialog = new AlertDialog.Builder(this).setTitle(R.string.default_property).setView(linearLayout)
                .setPositiveButton(getString(R.string.confirm), null)
                .setNeutralButton(getString(R.string.add), null)
                .setNegativeButton(getString(R.string.cancel), (dialog1, which) -> dialog1.dismiss())
                .setCancelable(false).create();
        dialog.show();
        dialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);

        dialog.getButton(AlertDialog.BUTTON_NEUTRAL).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddDefaultPropertyView(linearLayout, null, nameEditList, valueEditList);
            }
        });
        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<String> newProperties = new ArrayList<String>();
                for (int i = 0; i < nameEditList.size(); i++) {
                    if (!nameEditList.get(i).validate() || !valueEditList.get(i).validate())
                        return;
                    String name = nameEditList.get(i).getText().toString();
                    String[] values = valueEditList.get(i).getText().toString().trim().split("[,，]");
                    if (values.length > 0)
                        newProperties.add(name + "," + StringUtils.join(values, ','));
                }
                Global.setting.DefaultProperties = StringUtils.join(newProperties, "|");
                Global.defaultProperties = Global.setting.ParseProperties();
                Global.db.update(Global.setting, null, "DefaultProperties");
                dialog.dismiss();
            }
        });
    }

    private void AddDefaultPropertyView(ViewGroup vp, String[] namevalues, List<MaterialEditText> nameEditList, List<MaterialEditText> valueEditList) {
        LinearLayout itemView = (LinearLayout) getLayoutInflater().inflate(R.layout.item_property, null);
        vp.addView(itemView);
        //var skuCheckBox = itemView.findViewById<CheckBox>(R.id.useSku);
        MaterialEditText propertyName = itemView.findViewById(R.id.propertyName);
        MaterialEditText propertyValue = itemView.findViewById(R.id.propertyValue);
        ImageView delete = itemView.findViewById(R.id.delete);
        CheckBox useSku = itemView.findViewById(R.id.useSku);
        useSku.setVisibility(View.GONE);
        nameEditList.add(propertyName);
        valueEditList.add(propertyValue);
        propertyName.addValidator(new METValidator(getString(R.string.please_input_property)) {
            @Override
            public boolean isValid(@NonNull CharSequence text, boolean isEmpty) {
                return !isEmpty;
            }
        });
        propertyValue.addValidator(new METValidator(getString(R.string.please_input_property_value)) {
            @Override
            public boolean isValid(@NonNull CharSequence text, boolean isEmpty) {
                return !isEmpty;
            }
        });
        useSku.setChecked(true);
        if (namevalues != null) {
            propertyName.setText(namevalues[0]);
            propertyValue.setText(StringUtils.join(namevalues, ',', 1,namevalues.length));
        }
        delete.setOnClickListener(v -> {
            nameEditList.remove(propertyName);
            valueEditList.remove(propertyValue);
            vp.removeView(itemView);
        });
    }

    private void SetEmailAccount() {
        View addView = getLayoutInflater().inflate(R.layout.dialog_email_config, null);
        MaterialEditText emailAddress = addView.findViewById(R.id.emailAddress);
        MaterialEditText fromName = addView.findViewById(R.id.fromName);
        MaterialEditText smtpHostName = addView.findViewById(R.id.smtpHostName);
        MaterialEditText smtpHostPort = addView.findViewById(R.id.smtpHostPort);
        MaterialEditText account = addView.findViewById(R.id.account);
        MaterialEditText password = addView.findViewById(R.id.password);
        CheckBox useSSL = addView.findViewById(R.id.useSSL);
        emailAddress.addValidator(new METValidator(getString(R.string.input_emailaddress)) {
            @Override
            public boolean isValid(@NonNull CharSequence text, boolean isEmpty) {
                return !isEmpty;
            }
        });
        fromName.addValidator(new METValidator(getString(R.string.input_sender)) {
            @Override
            public boolean isValid(@NonNull CharSequence text, boolean isEmpty) {
                return !isEmpty;
            }
        });
        smtpHostName.addValidator(new METValidator(getString(R.string.input_smtp)) {
            @Override
            public boolean isValid(@NonNull CharSequence text, boolean isEmpty) {
                return !isEmpty;
            }
        });
        smtpHostPort.addValidator(new METValidator(getString(R.string.input_smtp_port)) {
            @Override
            public boolean isValid(@NonNull CharSequence text, boolean isEmpty) {
                return !isEmpty;
            }
        });
        account.addValidator(new METValidator(getString(R.string.input_username)) {
            @Override
            public boolean isValid(@NonNull CharSequence text, boolean isEmpty) {
                return !isEmpty;
            }
        });
        password.addValidator(new METValidator(getString(R.string.input_password)) {
            @Override
            public boolean isValid(@NonNull CharSequence text, boolean isEmpty) {
                return !isEmpty;
            }
        });
        emailAddress.setText(Global.setting.EmailAddress);
        fromName.setText(Global.setting.FromName);
        smtpHostName.setText(Global.setting.SmtpHostName);
        smtpHostPort.setText(String.valueOf(Global.setting.SmtpHostPort));
        account.setText(Global.setting.Account);
        password.setText(Global.setting.Password);
        useSSL.setChecked(Global.setting.SmtpEnableSsl);
        new AlertDialog.Builder(this).setTitle(R.string.email_config).setView(addView)
                .setPositiveButton(getString(R.string.save), (dialog, which) -> {
                    if (emailAddress.validate() && fromName.validate() && smtpHostName.validate() && smtpHostPort.validate() && account.validate() && password.validate()) {
                        Global.setting.EmailAddress = emailAddress.getText().toString();
                        Global.setting.FromName = fromName.getText().toString();
                        Global.setting.SmtpHostName = smtpHostName.getText().toString();
                        Global.setting.SmtpHostPort = Integer.parseInt(smtpHostPort.getText().toString());
                        Global.setting.Account = account.getText().toString();
                        Global.setting.Password = password.getText().toString();
                        Global.setting.SmtpEnableSsl = useSSL.isChecked();
                        Global.db.update(Global.setting, null, new String[]{"EmailAddress", "FromName", "SmtpHostName", "SmtpHostPort", "Password", "SmtpEnableSsl", "Account"});
                    }

                })
                .setNegativeButton(getString(R.string.cancel), null)
                .setCancelable(false)
                .show();//必须加这句，不然后面会报空指针错误
    }

    private void SetShopConfig() {
        View addView = getLayoutInflater().inflate(R.layout.dialog_shop_config, null);
        MaterialEditText shopName = addView.findViewById(R.id.shopName);
        MaterialEditText address = addView.findViewById(R.id.address);
        MaterialEditText tipsRate = addView.findViewById(R.id.tipsRate);
        //var minTips = addView.findViewById<MaterialEditText>(R.id.minTips);
//        MaterialEditText tax = addView.findViewById(R.id.tax);
        MaterialEditText telephone = addView.findViewById(R.id.telephone);
        shopName.addValidator(new METValidator(getString(R.string.input_shop_name)) {
            @Override
            public boolean isValid(@NonNull CharSequence text, boolean isEmpty) {
                return !isEmpty;
            }
        });
//        tax.addValidator(new METValidator(getString(R.string.input_tax_rate)) {
//            @Override
//            public boolean isValid(@NonNull CharSequence text, boolean isEmpty) {
//                return !isEmpty;
//            }
//        });
        tipsRate.addValidator(new METValidator(getString(R.string.input_tip_rate)) {
            @Override
            public boolean isValid(@NonNull CharSequence text, boolean isEmpty) {
                return !isEmpty;
            }
        });
        shopName.setText(Global.setting.ShopName);
        address.setText(Global.setting.ShopAddress);
        tipsRate.setText(String.format("%.2f", Global.setting.TipsRate));
//        tax.setText(String.format("%.2f", Global.setting.Tax));
        //minTips.setText($"{Global.setting.MinTips:F2}");
        telephone.setText(Global.setting.Telephone);
        new AlertDialog.Builder(this).setTitle(R.string.shop_config).setView(addView)
                .setPositiveButton(getString(R.string.save), (dialog, which) -> {
                    if (shopName.validate() && tipsRate.validate()) {
                        Global.setting.ShopName = shopName.getText().toString();
                        Global.setting.ShopAddress = address.getText().toString();
                        Global.setting.Telephone = telephone.getText().toString();
                        Global.setting.TipsRate = Double.parseDouble(tipsRate.getText().toString());
//                        Global.setting.Tax = Double.parseDouble(tax.getText().toString());
                        //Global.setting.MinTips = Convert.ToDouble(minTips.Text);
                        Global.db.update(Global.setting, null, new String[]{"ShopName", "ShopAddress", "Telephone", "TipsRate"});
                    }
                })
                .setNegativeButton(getString(R.string.cancel), null)
                .setCancelable(false)
                .show();//必须加这句，不然后面会报空指针错误
    }


    private void SetPrinterConfig() {
        String[] charsets = getResources().getStringArray(R.array.charsets);
        View addView = getLayoutInflater().inflate(R.layout.dialog_printer_config, null);
        MaterialEditText ip = addView.findViewById(R.id.ip);
        MaterialEditText port = addView.findViewById(R.id.port);
        Spinner charset = addView.findViewById(R.id.charset);
        ip.setText(Global.setting.PrinterIp);
        port.setText(String.valueOf(Global.setting.PrinterPort));
        ip.addValidator(new METValidator(getString(R.string.input_printer_ip)) {
            @Override
            public boolean isValid(@NonNull CharSequence text, boolean isEmpty) {
                return !isEmpty;
            }
        });
        port.addValidator(new METValidator(getString(R.string.input_printer_port)) {
            @Override
            public boolean isValid(@NonNull CharSequence text, boolean isEmpty) {
                return !isEmpty;
            }
        });
        ArrayAdapter adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, charsets);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        charset.setAdapter(adapter);
        int selection = 0;
        for (int i = 0; i < charsets.length; i++)
            if (Objects.equals(charsets[i], Global.setting.PrinterCharset))
                selection = i;
        charset.setSelection(selection);
        AlertDialog dialog = new AlertDialog.Builder(this).setTitle(R.string.printer_config).setView(addView)
                .setNeutralButton(R.string.save_and_test, (dialog1, which) -> {
                    int[] localIp = Util.wiFiIp(aty);
                    if (localIp == null) {
                        ViewInject.toast(R.string.open_wifi);
                        return;
                    }
                    if (ip.validate() && port.validate() && charset.getSelectedItemPosition() >= 0) {
                        Global.setting.PrinterIp = ip.getText().toString();
                        Global.setting.PrinterCharset = charsets[charset.getSelectedItemPosition()];
                        Global.setting.PrinterPort = Integer.parseInt(port.getText().toString());
                        Global.db.update(Global.setting, null, new String[]{"PrinterIp", "PrinterCharset", "PrinterPort"});
//                            boolean result = PrintClient.ReConnect(Global.setting.PrinterIp, Global.setting.PrinterPort);
//                            ViewInject.toast(result ? "连接打印机成功" : "连接打印机失败");
//                            if (result)
                        PrintClient.Test();
                    }
                })
                .setPositiveButton(R.string.seek_printer, null)
                .setNegativeButton(getString(R.string.cancel), (d, which) -> d.dismiss())
                .setCancelable(false).create();
        dialog.show();//必须加这句，不然后面会报空指针错误
        dialog.getButton((int) AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int[] localIp = Util.wiFiIp(aty);
                if (localIp == null)
                    ViewInject.toast(R.string.open_wifi);
                List<String> printers = new ArrayList<>();
                ExecutorService es = Executors.newFixedThreadPool(8);

                SweetAlertDialog loading = new SweetAlertDialog(aty, SweetAlertDialog.PROGRESS_TYPE)
                        .showCancelButton(true).showTitle(false).showContentText(false)
                        .setCancelClickListener(sweetAlertDialog -> {
                            es.shutdownNow();
                            sweetAlertDialog.dismiss();
                        });
                loading.show();
                new Thread(() -> {
                    PrintClient.CheckPrinter(localIp, Global.setting.PrinterPort, printers, es);
                    runOnUiThread(() -> {
                        loading.hide();
                        if (!printers.isEmpty()) {
                            new AlertDialog.Builder(aty).setTitle(R.string.select_printer).setSingleChoiceItems(printers.toArray(new String[0]), -1, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface d, int which) {
                                    if (which >= 0) {
                                        ip.setText(printers.get(which));
                                    }

                                    d.dismiss();
                                }
                            }).show();
                        } else {
                            ViewInject.toast(R.string.no_printer_found);
                        }
                    });
                }).start();
            }
        });

    }


    private void SetTableAccount() {
        Intent intent = new Intent(aty, TableManagerActivity.class);
        startActivityForResult(intent, REQUEST_TABLE);
    }




    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        Util.dealPermissionRequest(this, requestCode, permissions, grantResults);
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (ContextCompat.checkSelfPermission(aty, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            initdb();
        }
    }


    private void initdb(){
        if (Global.db == null) {
            Global.db = KJDB.create(this, Config.DIRECTORY, Config.DB, false, Config.DB_VERSION, (db, oldVersion, newVersion) -> {
            });
//            Global.db.exeSql("drop table " + TableInfo.get(Order.class).getTableName());
//            Global.db.exeSql("drop table "+ TableInfo.get(OrderItem.class).getTableName());
//            Global.db.exeSql("drop table "+ TableInfo.get(Customer.class).getTableName());
//            Global.db.exeSql("drop table "+ TableInfo.get(Table.class).getTableName());


            Global.setting = Global.db.findById("AppSetting", AppSetting.class);
            if (Global.setting == null) {
                Global.setting = new AppSetting();
                Global.setting.AppSettingId = "AppSetting";
                Global.setting.PrinterCharset = "GBK";
                Global.setting.PrinterPort = 9100;
                Global.db.save(Global.setting);
            }
            Global.guest = new Customer();
            Global.guest.Name = "Guest";
            Global.guest.Telephone = "Guest";
            Global.guest.OrderCount = Global.setting.GuestOrderCount;
            Global.defaultSkus = Global.setting.ParseSkus();
            Global.defaultProperties = Global.setting.ParseProperties();
        }
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        flag = 0;
        //UnregisterReceiver(hookReceiver);
        PrintClient.close();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CUSTOMER && resultCode == RESULT_OK)//选择客户
        {
            int customerId = data.getIntExtra("customerId", 0);
            if (customerId > 0) {
                currentCustomer = Global.db.findById(customerId, Customer.class);
                if (currentCustomer != null) {
                    currentPhone = currentCustomer.Telephone;
                    customerName.setVisibility(View.GONE);
                    getSupportActionBar().setTitle(currentCustomer.Name + "(" + currentCustomer.Telephone + ")");
                }
            } else {
                currentCustomer = Global.guest;
                customerName.setVisibility(View.GONE);
                currentPhone = currentCustomer.Telephone;
                getSupportActionBar().setTitle(currentCustomer.Name);

            }
        } else if (requestCode == REQUEST_CALL_RECORD && resultCode == RESULT_OK) //选择通话记录
        {
            String phoneNumber = data.getStringExtra("phoneNumber");
            if (phoneNumber != null) {
                setCurrentPhone(phoneNumber);
            }
        } else if (requestCode == REQUEST_PRODUCT_MANAGER) {
            InitCategoryAndFood();
        }
//        else if (requestCode == REQUEST_TABLE) {
//            InitPlaceAndTable();
//        }
    }


    @Override
    public void setRootView() {
        setContentView(R.layout.activity_main);
    }

    class MyTeleStateListener extends TeleStateListener {
        @Override
        public void onConnectStateChange(boolean connected) {
            if (connected) {
                //自定义通话界面: null-切换到系统的；DISABLED-不要通话界面；Activity-用自己的通话界面
                TeleSettings.setMyCallUi(0, LauncherActivity.class.getName());//"cn.zicoo.ir2teledemo.CallUiActivity");
                TeleSettings.setMyHookupUi(LauncherActivity.class.getName());
                //CustomizeSettings.setLocalCode("0755");//设置客户本地区号
                //CustomizeSettings.setAutoRecord(true);//设置自动录音
                //CustomizeSettings.setHideNumber(true);//设置隐藏通话记录号码(通话记录号码中间显示成****)
            }
        }

        @Override
        public void onCallStateChange(int state, String incomingNumber, int subId) {
            super.onCallStateChange(state, incomingNumber, subId);
            switch (state) {
                case Tele.CALL_STATE_IDLE://通话结束
                    if (phoneDialog.isShowing())
                        phoneDialog.dismiss();
                    break;
                case Tele.CALL_STATE_RINGING://来电。。
                    if (StringUtils.isNotBlank(incomingNumber))
                        setCurrentPhone(incomingNumber);
                    break;
                case Tele.CALL_STATE_OFFHOOK://通话中
                    if (StringUtils.isNotBlank(incomingNumber))
                        setCurrentPhone(incomingNumber);
                    break;
                case Tele.CALL_STATE_DIALING://拨号中
                    if (StringUtils.isNotBlank(incomingNumber))
                        setCurrentPhone(incomingNumber);
                    break;
            }
        }

        @Override
        public void onHookStateChange(boolean pickup, boolean talking) {
            if (pickup) {
                TeleManager.getInstance().answerRingingCall();
                if (phoneDialog.isShowing())
                    phoneDialog.dismiss();
            } else {
                TeleManager.getInstance().endCall();

            }
        }
    }


}
