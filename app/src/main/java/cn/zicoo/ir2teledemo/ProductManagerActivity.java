package cn.zicoo.ir2teledemo;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.PopupMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;

import com.flyco.tablayout.SlidingTabLayout;
import com.flyco.tablayout.listener.OnTabSelectListener;
import com.rengwuxian.materialedittext.MaterialEditText;
import com.rengwuxian.materialedittext.validation.METValidator;

import org.kymjs.kjframe.KJActivity;
import org.kymjs.kjframe.ui.BindView;

import java.util.ArrayList;
import java.util.List;

import cn.zicoo.ir2teledemo.adapter.MyFragmentPagerAdapter;
import cn.zicoo.ir2teledemo.common.Global;
import cn.zicoo.ir2teledemo.fragment.FoodManagerFragment;
import cn.zicoo.ir2teledemo.model.FoodMenu;
import cn.zicoo.ir2teledemo.model.Category;

public class ProductManagerActivity extends KJActivity {
    @BindView(id = R.id.categoryTab)
    private SlidingTabLayout categoryTab;
    @BindView(id = R.id.addCategory)

    private ImageButton addCategory;
    @BindView(id = R.id.viewPager)

    private ViewPager viewPager;

    private List<Category> categories;
    private List<Fragment> fragments = new ArrayList<>();
    private MyFragmentPagerAdapter foodPagerAdapter;

    @Override
    public void setRootView() {
        setContentView(R.layout.activity_product_manager);
    }

    @Override
    public void initData() {
        super.initData();
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        addCategory.setOnClickListener(v -> {
            View addView = getLayoutInflater().inflate(R.layout.dialog_add_category, null);
            MaterialEditText name = addView.findViewById(R.id.name);
            name.addValidator(new METValidator(getString(R.string.please_input_name)) {
                @Override
                public boolean isValid(@NonNull CharSequence text, boolean isEmpty) {
                    return !isEmpty;
                }
            });
            AlertDialog dialog = new AlertDialog.Builder(aty).setTitle(R.string.add_category).setView(addView)
                    .setPositiveButton(R.string.save_and_continue, null)
                    .setNegativeButton(R.string.cancel, (d, which) -> d.dismiss())
                    .setCancelable(false).create();
            dialog.show();//必须加这句，不然后面会报空指针错误
            dialog.getButton((int) AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (name.validate()) {
                        saveCategory(name.getText().toString());
                        name.setText("");
                    }
                }
            });
        });
        categories = Global.categories;
        InitCategoryAndFood();
    }

    private void InitCategoryAndFood() {
        //categories = Global.db.Select(Global.db.From<ProductCategory>().OrderBy(c => c.Sort));
        for (Category category : categories) {
            FoodManagerFragment foodFragment = new FoodManagerFragment();
            Bundle bundle = new Bundle();
            bundle.putInt("categoryId", category.CategoryId);
            foodFragment.setArguments(bundle);
            fragments.add(foodFragment);
        }
        foodPagerAdapter = new MyFragmentPagerAdapter(getSupportFragmentManager(), fragments, categories);
        viewPager.setAdapter(foodPagerAdapter);
        categoryTab.setViewPager(viewPager);
        categoryTab.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelect(int position) {

            }

            @Override
            public void onTabReselect(int position) {
                PopupMenu popup = new PopupMenu(aty, categoryTab.getTitleView(position));
                //将R.menu.popup_menu菜单资源加载到popup菜单中
                popup.getMenuInflater().inflate(R.menu.menu_product, popup.getMenu());
                popup.setOnMenuItemClickListener(menuItem -> {
                    switch (menuItem.getItemId()) {
                        case R.id.modify:
                            ModifyCategory(position);
                            break;
                        case R.id.delete:
                            DeleteCategory(position);

                            break;
                    }
                    return true;
                });
                popup.show();
            }
        });
    }

    private void ModifyCategory(int position) {
        Category category = categories.get(position);
        View addView = getLayoutInflater().inflate(R.layout.dialog_add_category, null);
        MaterialEditText name = addView.findViewById(R.id.name);
        name.setText(category.Name);
        name.addValidator(new METValidator(getString(R.string.please_input_name)) {
            @Override
            public boolean isValid(@NonNull CharSequence text, boolean isEmpty) {
                return !isEmpty;
            }
        });
        AlertDialog dialog = new AlertDialog.Builder(this).setTitle(R.string.modify_category).setView(addView)
                .setPositiveButton(R.string.save, null)
                .setNegativeButton(R.string.cancel, (dialog1, which) -> dialog1.dismiss())
                .setCancelable(false).create();
        dialog.show();//必须加这句，不然后面会报空指针错误
        dialog.getButton((int) AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (name.validate()) {
                    category.Name = name.getText().toString().trim();
                    Global.db.update(category);
                    categoryTab.notifyDataSetChanged();
                    dialog.dismiss();
                }
            }
        });
    }

    private void DeleteCategory(int position) {
        new AlertDialog.Builder(this).setTitle(R.string.confirm_delete_category)
                .setPositiveButton(R.string.confirm, (dialog, which) -> {
                    Global.db.deleteByWhere(FoodMenu.class, "CategoryId = '" + categories.get(position).CategoryId+"'");
                    Global.db.delete(categories.get(position));
                    categories.remove(position);
                    fragments.remove(position);
                    foodPagerAdapter.notifyDataSetChanged();
                    categoryTab.notifyDataSetChanged();
                }).setNegativeButton(R.string.cancel, null).show();


    }

    private void saveCategory(String name) {
        Category category = new Category();
        category.Name = name;
        if (categories.size() > 0)
            category.Sort = categories.get(categories.size() - 1).Sort + 1;
        Global.db.saveBindId(category);
        categories.add(category);
        FoodManagerFragment foodFragment = new FoodManagerFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("categoryId", category.CategoryId);
        bundle.putBoolean("hasPlus", true);
        foodFragment.setArguments(bundle);
        fragments.add(foodFragment);
        foodPagerAdapter.notifyDataSetChanged();
        categoryTab.notifyDataSetChanged();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
