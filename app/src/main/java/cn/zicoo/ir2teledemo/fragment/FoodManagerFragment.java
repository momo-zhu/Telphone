package cn.zicoo.ir2teledemo.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.beardedhen.androidbootstrap.BootstrapButton;

import cn.zicoo.ir2teledemo.R;

import com.rengwuxian.materialedittext.MaterialEditText;
import com.rengwuxian.materialedittext.validation.METValidator;

import org.apache.commons.lang3.StringUtils;
import org.kymjs.kjframe.ui.BindView;
import org.kymjs.kjframe.ui.SupportFragment;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import cn.zicoo.ir2teledemo.ProductManagerActivity;
import cn.zicoo.ir2teledemo.adapter.ProductManagerAdapter;
import cn.zicoo.ir2teledemo.adapter.SimpleItemTouchHelperCallback;
import cn.zicoo.ir2teledemo.application.CustomApplication;
import cn.zicoo.ir2teledemo.base.AdapterListener;
import cn.zicoo.ir2teledemo.common.Global;
import cn.zicoo.ir2teledemo.model.FoodMenu;
import cn.zicoo.ir2teledemo.model.FoodMenuProperty;
import cn.zicoo.ir2teledemo.model.FoodMenuItem;


/**
 * Created by Tech07 on 2016/1/26.
 */
public class FoodManagerFragment extends SupportFragment {
    @BindView(id = R.id.recycleView)
    protected RecyclerView recycleView;
    protected List<FoodMenu> mList;
    protected ProductManagerAdapter adapter;
    protected ProductManagerActivity aty;
    protected CustomApplication app;
    private int categoryId;
//    private DynamicBox box;

    @Override
    protected View inflaterView(LayoutInflater inflater, ViewGroup container, Bundle bundle) {
        return inflater.inflate(R.layout.recycle_view, container, false);
    }

    @Override
    protected void initData() {
        aty = (ProductManagerActivity) getActivity();
        app = ((CustomApplication) aty.getApplication());
//        user = app.getUser();
        categoryId = getArguments().getInt("categoryId");
        if (Global.products.get(categoryId) == null)
            Global.products.put(categoryId, new ArrayList<>());
        mList = Global.products.get(categoryId);

    }

    @Override
    protected void initWidget(View parentView) {
//        box = new DynamicBox(aty, recycleView);
//        View emptyCollectionView = getLayoutInflater().inflate(R.layout.empty_data, null, false);
//        box.addCustomView(emptyCollectionView, "empty");
        adapter = new ProductManagerAdapter(aty, mList);
        recycleView.setAdapter(adapter);
        adapter.setListener(new AdapterListener() {
            @Override
            public void click(View view, int position) {
                ProductMenu(view, position);
            }

            @Override
            public void click1(View view, int position) {
                ProductAdd();
            }
        });
        recycleView.setLayoutManager(new GridLayoutManager(aty, 4, GridLayoutManager.VERTICAL, false));

        //用Callback构造ItemtouchHelper
        ItemTouchHelper touchHelper = new ItemTouchHelper(new SimpleItemTouchHelperCallback(adapter));
        //调用ItemTouchHelper的attachToRecyclerView方法建立联系
        touchHelper.attachToRecyclerView(recycleView);
    }


    public int getLastSort() {
        if (!mList.isEmpty())
            return mList.get(mList.size() - 1).Sort + 10000;
        return 0;
    }

    private void ProductAdd() {
        View addView = getLayoutInflater().inflate(R.layout.dialog_add_food, null);
        MaterialEditText name = addView.findViewById(R.id.name);
        MaterialEditText code = addView.findViewById(R.id.code);
        MaterialEditText price = addView.findViewById(R.id.price);
        MaterialEditText taxRate = addView.findViewById(R.id.taxRate);
        //var measure = addView.findViewById<MaterialEditText>(R.id.measure);
        BootstrapButton skuEdit = addView.findViewById(R.id.skuEdit);
        BootstrapButton propertyEdit = addView.findViewById(R.id.propertyEdit);
        name.addValidator(new METValidator(getString(R.string.please_input_name)) {
            @Override
            public boolean isValid(@NonNull CharSequence text, boolean isEmpty) {
                return !isEmpty;
            }
        });
        price.addValidator(new METValidator(getString(R.string.please_input_price)) {
            @Override
            public boolean isValid(@NonNull CharSequence text, boolean isEmpty) {
                return !isEmpty;
            }
        });
        taxRate.addValidator(new METValidator(getString(R.string.please_input_tax_rate)) {
            @Override
            public boolean isValid(@NonNull CharSequence text, boolean isEmpty) {
                return !isEmpty;
            }
        });
        code.addValidator(new METValidator(getString(R.string.produce_code_repeat)) {
            @Override
            public boolean isValid(@NonNull CharSequence text, boolean isEmpty) {
                return Global.db.findAllByWhere(FoodMenu.class, "Code = '" + text + "'").isEmpty();
            }
        });
        taxRate.setText("0");
        final FoodMenu[] product = {new FoodMenu()};
        skuEdit.setOnClickListener(v -> {
            AlertDialog d = SkuEdit(product[0]);
            d.setOnDismissListener(dialog -> price.setVisibility(product[0].hasSkus() ? View.GONE : View.VISIBLE));
        });
        propertyEdit.setOnClickListener(v -> PropertyEdit(product[0]));
        AlertDialog dialog = new AlertDialog.Builder(aty).setTitle(R.string.add_product).setView(addView)
                .setPositiveButton(R.string.save_and_continue, null)
                .setNegativeButton(R.string.cancel, (d, which) -> d.dismiss())
                .setCancelable(false).create();
        dialog.show();//必须加这句，不然后面会报空指针错误
        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (name.validate() && code.validate() && taxRate.validate() && (price.getVisibility() != View.VISIBLE || price.validate())) {
                    product[0].Name = name.getText().toString();
                    product[0].TaxRate = Double.parseDouble(taxRate.getText().toString());
                    product[0].CategoryId = categoryId;
                    if (price.getVisibility() == View.VISIBLE)
                        product[0].Price = Double.parseDouble(price.getText().toString());
                    product[0].Sort = getLastSort();
                    product[0].Code = code.getText().toString();
                    SaveFood(product[0]);
                    product[0] = new FoodMenu();
                    name.setText("");
                    price.setText("");
                    code.setText("");
                }
            }
        });
    }


    private void ProductMenu(View view, int position) {
        PopupMenu popup = new PopupMenu(aty, view);
        //将R.menu.popup_menu菜单资源加载到popup菜单中
        popup.getMenuInflater().inflate(R.menu.menu_product, popup.getMenu());
        popup.setOnMenuItemClickListener(menuItem -> {
            switch (menuItem.getItemId()) {
                case R.id.modify:
                    ModifyProduct(position);
                    break;
                case R.id.delete:
                    FoodMenu product = mList.get(position);
                    mList.remove(position);
                    adapter.notifyItemRemoved(position + 1);
                    //Global.db.ExecuteSql("DELETE t1, t2, t3 from Product as t1 INNER JOIN ProductSku as t2 on t1.FoodMenuId == t2.FoodMenuId INNER JOIN ProductProperty  as t3 on t1.FoodMenuId == t3.FoodMenuId WHERE t1.FoodMenuId = ? ", new[] { product.FoodMenuId });
                    if (product.hasSkus())
                        Global.db.deleteByWhere(FoodMenuItem.class, "FoodMenuId = '" + product.FoodMenuId+"'");
                    if (product.hasProperties())
                        Global.db.deleteByWhere(FoodMenuProperty.class, "FoodMenuId = '" + product.FoodMenuId+"'");
                    Global.db.delete(product);
                    break;
            }
            return true;
        });
        popup.show();
    }

    private void ModifyProduct(int position) {
        FoodMenu product = mList.get(position);

        View addView = getLayoutInflater().inflate(R.layout.dialog_add_food, null);
        MaterialEditText name = addView.findViewById(R.id.name);
        MaterialEditText code = addView.findViewById(R.id.code);
        MaterialEditText price = addView.findViewById(R.id.price);
        MaterialEditText taxRate = addView.findViewById(R.id.taxRate);
        BootstrapButton skuEdit = addView.findViewById(R.id.skuEdit);
        BootstrapButton propertyEdit = addView.findViewById(R.id.propertyEdit);
        name.addValidator(new METValidator(getString(R.string.please_input_name)) {
            @Override
            public boolean isValid(@NonNull CharSequence text, boolean isEmpty) {
                return !isEmpty;
            }
        });
        taxRate.addValidator(new METValidator(getString(R.string.please_input_tax_rate)) {
            @Override
            public boolean isValid(@NonNull CharSequence text, boolean isEmpty) {
                return !isEmpty;
            }
        });
        code.setVisibility(View.GONE);
        //code.AddValidator(new CustomValidator("请输入菜品编码"));
        price.addValidator(new METValidator(getString(R.string.please_input_price)) {
            @Override
            public boolean isValid(@NonNull CharSequence text, boolean isEmpty) {
                return !isEmpty;
            }
        });
        price.setVisibility(product.hasSkus() ? View.GONE : View.VISIBLE);
        skuEdit.setOnClickListener(v -> {
            AlertDialog d = SkuEdit(mList.get(position));
            d.setOnDismissListener(dialog -> price.setVisibility(product.hasSkus() ? View.GONE : View.VISIBLE));
        });
        propertyEdit.setOnClickListener(v -> PropertyEdit(mList.get(position)));
        name.setText(product.Name);
        code.setText(product.Code);
        taxRate.setText(String.format("%.2f", product.TaxRate));
        //measure.Text = product.Measure;
        price.setText(String.format("%.2f", product.Price));
        AlertDialog dialog = new AlertDialog.Builder(aty).setTitle(R.string.modify_product).setView(addView)
                .setPositiveButton(R.string.save, null)
                .setNegativeButton(R.string.cancel, (d, which) -> d.dismiss())
                .setCancelable(false).create();

        dialog.show();
        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (name.validate() && taxRate.validate() && (price.getVisibility() != View.VISIBLE || price.validate())) {
                    product.Name = name.getText().toString();
                    if (price.getVisibility() == View.VISIBLE)
                        product.Price = Double.parseDouble(price.getText().toString());
                    //product.Code = code.Text;
                    product.TaxRate = Double.parseDouble(taxRate.getText().toString());

                    //product.Measure = string.IsNullOrEmpty(measure.Text) ? "份" : measure.Text;
                    adapter.notifyItemChanged(position + 1);
                    Global.db.deleteByWhere(FoodMenuItem.class, "FoodMenuId = '" + product.FoodMenuId+"'");
                    Global.db.deleteByWhere(FoodMenuProperty.class, "FoodMenuId = '" + product.FoodMenuId+"'");
                    Global.db.update(product);
                    Global.db.save(product.ProductProperties);
                    Global.db.save(product.ProductSkus);
                    dialog.dismiss();
                }
            }
        });
    }


    public void SaveFood(FoodMenu product) {
        mList.add(product);
        adapter.notifyItemInserted(mList.size());
        Global.db.saveBindId(product);
        for (FoodMenuProperty temp : product.ProductProperties)
            temp.FoodMenuId = product.FoodMenuId;
        for (FoodMenuItem temp : product.ProductSkus)
            temp.FoodMenuId = product.FoodMenuId;
        Global.db.save(product.ProductProperties);
        Global.db.save(product.ProductSkus);
    }


    private AlertDialog SkuEdit(FoodMenu product) {
        List<FoodMenuItem> skus = new ArrayList<>(product.ProductSkus);

        LinearLayout linearLayout = (LinearLayout) getLayoutInflater().inflate(R.layout.dialog_add_sku, null);
        List<CheckBox> ckeckBoxList = new ArrayList<CheckBox>();
        List<MaterialEditText> specEditList = new ArrayList<MaterialEditText>();
        List<MaterialEditText> priceEditList = new ArrayList<MaterialEditText>();
        //添加默认规格
        for (String item : Global.defaultSkus) {
            boolean exists = false;
            for (FoodMenuItem temp : skus)
                if (Objects.equals(temp.Title, item)) {
                    exists = true;
                    break;
                }
            if (!exists)
                AddSkuView(linearLayout, new Object[]{item, 0d, false}, ckeckBoxList, specEditList, priceEditList);
        }
        //添加自定义规格
        for (FoodMenuItem sku : skus)
            AddSkuView(linearLayout, new Object[]{sku.Title, sku.Price, true}, ckeckBoxList, specEditList, priceEditList);


        AlertDialog dialog = new AlertDialog.Builder(aty).setTitle(R.string.modify_sku).setView(linearLayout)
                .setPositiveButton(R.string.confirm, null)
                .setNeutralButton(R.string.add, null)
                .setNegativeButton(R.string.cancel, (d, which) -> d.dismiss())
                .setCancelable(false).create();

        dialog.show();
        dialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);

        dialog.getButton(AlertDialog.BUTTON_NEUTRAL).setOnClickListener(v -> {
            AddSkuView(linearLayout, new Object[]{null, 0d, true}, ckeckBoxList, specEditList, priceEditList);
        });
        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(v -> {
            List<FoodMenuItem> newSkus = new ArrayList<FoodMenuItem>();
            for (int i = 0; i < ckeckBoxList.size(); i++) {
                if (ckeckBoxList.get(i).isChecked()) {
                    if (!specEditList.get(i).validate() || !priceEditList.get(i).validate())
                        return;
                    FoodMenuItem sku = new FoodMenuItem();
                    sku.Title = specEditList.get(i).getText().toString();
                    sku.Price = Double.parseDouble(priceEditList.get(i).getText().toString());
                    sku.Sort = i;
                    sku.FoodMenuId = product.FoodMenuId;
                    newSkus.add(sku);
                }
            }
            product.ProductSkus.clear();
            product.ProductSkus.addAll(newSkus);
            dialog.dismiss();
        });
        return dialog;
    }

    private void AddSkuView(ViewGroup vp, Object[] sku, List<CheckBox> ckeckBoxList,
                            List<MaterialEditText> specEditList, List<MaterialEditText> priceEditList) {
        LinearLayout itemView = (LinearLayout) getLayoutInflater().inflate(R.layout.item_sku, null);
        vp.addView(itemView);
        //var skuCheckBox = itemView.findViewById<CheckBox>(R.id.useSku);
        CheckBox useSku = itemView.findViewById(R.id.useSku);
        MaterialEditText spec = itemView.findViewById(R.id.spec);
        MaterialEditText price = itemView.findViewById(R.id.price);
        ImageView delete = itemView.findViewById(R.id.delete);
        ckeckBoxList.add(useSku);
        specEditList.add(spec);
        priceEditList.add(price);
        spec.addValidator(new METValidator(getString(R.string.please_input_spec)) {
            @Override
            public boolean isValid(@NonNull CharSequence text, boolean isEmpty) {
                return !isEmpty;
            }
        });
        price.addValidator(new METValidator(getString(R.string.please_input_price)) {
            @Override
            public boolean isValid(@NonNull CharSequence text, boolean isEmpty) {
                return !isEmpty;
            }
        });
        useSku.setChecked((boolean) sku[2]);
        spec.setText((String) sku[0]);
        if ((double) sku[1] != 0)
            price.setText(String.format("%.2f", (double) sku[1]));
        //skuCheckBox.CheckedChange += (sender, e) =>
        //{
        //    sku.Enable = skuCheckBox.Enabled;
        //};
        delete.setOnClickListener(v -> {
            ckeckBoxList.remove(useSku);
            specEditList.remove(spec);
            priceEditList.remove(price);
            vp.removeView(itemView);
        });
    }


    private void PropertyEdit(FoodMenu product) {
        List<FoodMenuProperty> properties = new ArrayList<FoodMenuProperty>(product.ProductProperties);

        LinearLayout linearLayout = (LinearLayout) getLayoutInflater().inflate(R.layout.dialog_add_sku, null);

        ArrayList<CheckBox> ckeckBoxList = new ArrayList<CheckBox>();
        ArrayList<MaterialEditText> nameEditList = new ArrayList<MaterialEditText>();
        ArrayList<MaterialEditText> valueEditList = new ArrayList<MaterialEditText>();

        for (String[] item : Global.defaultProperties) {
            boolean exists = false;
            for (FoodMenuProperty temp : properties)
                if (Objects.equals(temp.Name, item[0])) {
                    exists = true;
                    break;
                }
            if (!exists)
                AddPropertyView(linearLayout, new Object[]{item[0], StringUtils.join(item, ',', 1,item.length), false}, ckeckBoxList, nameEditList, valueEditList);

        }

        for (FoodMenuProperty item : properties) {
            AddPropertyView(linearLayout, new Object[]{item.Name, item.ValueStr, true}, ckeckBoxList, nameEditList, valueEditList);
        }
        AlertDialog dialog = new AlertDialog.Builder(aty).setTitle(R.string.modify_property).setView(linearLayout)
                .setPositiveButton(R.string.confirm, null)
                .setNeutralButton(R.string.add, null)
                .setNegativeButton(R.string.cancel, (d, which) -> d.dismiss())
                .setCancelable(false).create();
        dialog.show();

        dialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);

        dialog.getButton(AlertDialog.BUTTON_NEUTRAL).setOnClickListener(v -> {
            AddPropertyView(linearLayout, new Object[]{null, null, true}, ckeckBoxList, nameEditList, valueEditList);
            nameEditList.get(nameEditList.size() - 1).requestFocus();
        });
        dialog.getButton((int) AlertDialog.BUTTON_POSITIVE).setOnClickListener(v -> {
            ArrayList<FoodMenuProperty> newProperties = new ArrayList<FoodMenuProperty>();
            for (int i = 0; i < ckeckBoxList.size(); i++) {
                if (ckeckBoxList.get(i).isChecked()) {
                    if (!nameEditList.get(i).validate() || !valueEditList.get(i).validate())
                        return;
                    FoodMenuProperty property = new FoodMenuProperty();
                    property.Name = nameEditList.get(i).getText().toString();
                    property.ValueStr = valueEditList.get(i).getText().toString().trim().replace("，", ",");
                    property.FoodMenuId = product.FoodMenuId;
                    newProperties.add(property);
                }
            }
            product.ProductProperties.clear();
            product.ProductProperties.addAll(newProperties);
            dialog.dismiss();
        });
        if (!nameEditList.isEmpty())
            nameEditList.get(0).requestFocus();
    }

    private void AddPropertyView(ViewGroup vp, Object[] property, List<CheckBox> ckeckBoxList,
                                 List<MaterialEditText> nameEditList, List<MaterialEditText> valueEditList) {
        LinearLayout itemView = (LinearLayout) getLayoutInflater().inflate(R.layout.item_property, null);
        vp.addView(itemView);
        //var skuCheckBox = itemView.findViewById<CheckBox>(R.id.useSku);
        MaterialEditText propertyName = itemView.findViewById(R.id.propertyName);
        MaterialEditText propertyValue = itemView.findViewById(R.id.propertyValue);
        ImageView delete = itemView.findViewById(R.id.delete);
        CheckBox useSku = itemView.findViewById(R.id.useSku);

        ckeckBoxList.add(useSku);
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
        //skuCheckBox.Checked = sku.Enable;
        useSku.setChecked((boolean) property[2]);
        propertyName.setText((String) property[0]);
        propertyValue.setText((String) property[1]);
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ckeckBoxList.remove(useSku);
                nameEditList.remove(propertyName);
                valueEditList.remove(propertyValue);
                vp.removeView(itemView);
            }
        });
    }

}
