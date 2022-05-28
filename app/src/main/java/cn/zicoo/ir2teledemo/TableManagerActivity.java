package cn.zicoo.ir2teledemo;

import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.os.Bundle;
import android.support.v7.widget.PopupMenu;
import android.view.Menu;
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
import java.util.Date;
import java.util.List;

import cn.zicoo.ir2teledemo.adapter.MyFragmentPagerAdapter;
import cn.zicoo.ir2teledemo.adapter.TableFragmentPagerAdapter;
import cn.zicoo.ir2teledemo.adapter.TableManagerAdapter;
import cn.zicoo.ir2teledemo.common.Global;
import cn.zicoo.ir2teledemo.fragment.FoodManagerFragment;
import cn.zicoo.ir2teledemo.fragment.TableManagerFragment;
import cn.zicoo.ir2teledemo.model.Category;
import cn.zicoo.ir2teledemo.model.FoodMenu;
import cn.zicoo.ir2teledemo.model.Place;
import cn.zicoo.ir2teledemo.model.Table;

public class TableManagerActivity extends KJActivity {

    @BindView(id = R.id.categoryTab_table)
    private SlidingTabLayout categoryTab_table;
//    @BindView(id = R.id.placeLv)
//    private SlidingTabLayout placeLv;

    @BindView(id = R.id.addCategory_table)
    private ImageButton addCategory_table;
    @BindView(id = R.id.viewPager_table)
    private ViewPager viewPager_table;


    private List<Place> Places_Table;
    private TableManagerAdapter adapter_table;
    //protected List<Table> tList;
    private List<Fragment> fragments_table = new ArrayList<>();


    private TableFragmentPagerAdapter tablePagerAdapter;

    @Override
    public void setRootView() {
        setContentView(R.layout.activity_table_manager);
    }

    @Override
    public void initData() {
        super.initData();
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

//        tList = new ArrayList<>();
//        adapter_table = new TableManagerAdapter(this, tList);

        addCategory_table.setOnClickListener(v -> {
            View addView = getLayoutInflater().inflate(R.layout.dialog_add_table_adress, null);
            MaterialEditText name_table = addView.findViewById(R.id.name_table);
            name_table.addValidator(new METValidator(getString(R.string.please_input_name)) {
                @Override
                public boolean isValid(@NonNull CharSequence text, boolean isEmpty) {
                    return !isEmpty;
                }
            });
            AlertDialog dialog = new AlertDialog.Builder(aty).setTitle(R.string.add_table_adress).setView(addView)
                    .setPositiveButton(R.string.save_and_continue, null)
                    .setNegativeButton(R.string.cancel, (d, which) -> d.dismiss())
                    .setCancelable(false).create();
            dialog.show();//必须加这句，不然后面会报空指针错误
            dialog.getButton((int) AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (name_table.validate()) {
                        savePlace(name_table.getText().toString());
                        name_table.setText("");
                    }
                }
            });
        });
        Places_Table = Global.places_table;
        InitPlaceAndTable();

    }

    private void savePlace(String place) {
        Place place_table = new Place();
        place_table.PlaceTable = place;
        if (Places_Table.size() > 0)
            place_table.Sort = Places_Table.get(Places_Table.size() - 1).Sort + 1;
        Global.db.saveBindId(place_table);
        Places_Table.add(place_table);
        TableManagerFragment tableFragment = new TableManagerFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("placeId", place_table.PlaceId);
        bundle.putBoolean("hasPlus", true);
        tableFragment.setArguments(bundle);
        fragments_table.add(tableFragment);
        tablePagerAdapter.notifyDataSetChanged();
        categoryTab_table.notifyDataSetChanged();
    }

        private void InitPlaceAndTable() {
        //categories = Global.db.Select(Global.db.From<ProductCategory>().OrderBy(c => c.Sort));
        for (Place place : Places_Table) {
            TableManagerFragment tableFragment = new TableManagerFragment();
            Bundle bundle = new Bundle();
            bundle.putInt("placeId", place.PlaceId);
            tableFragment.setArguments(bundle);
            fragments_table.add(tableFragment);
        }
        tablePagerAdapter = new TableFragmentPagerAdapter(getSupportFragmentManager(), fragments_table, Places_Table);
        viewPager_table.setAdapter(tablePagerAdapter);
        categoryTab_table.setViewPager(viewPager_table);
        categoryTab_table.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelect(int position) {

            }

            @Override
            public void onTabReselect(int position) {
                PopupMenu popup = new PopupMenu(aty, categoryTab_table.getTitleView(position));
                //将R.menu.popup_menu菜单资源加载到popup菜单中
                popup.getMenuInflater().inflate(R.menu.menu_product, popup.getMenu());
                popup.setOnMenuItemClickListener(menuItem -> {
                    switch (menuItem.getItemId()) {
                        case R.id.modify:
                            ModifyPlace(position);
                            break;
                        case R.id.delete:
                            DeletePlace(position);

                            break;
                    }
                    return true;
                });
                popup.show();
            }
        });
    }

    private void ModifyPlace(int position) {
        Place place = Places_Table.get(position);
        View addView = getLayoutInflater().inflate(R.layout.dialog_add_table_adress, null);
        MaterialEditText name_table = addView.findViewById(R.id.name_table);
        name_table.addValidator(new METValidator(getString(R.string.please_input_name)) {
            @Override
            public boolean isValid(@NonNull CharSequence text, boolean isEmpty) {
                return !isEmpty;
            }
        });
        AlertDialog dialog = new AlertDialog.Builder(aty).setTitle(R.string.add_table_adress).setView(addView)
                .setPositiveButton(R.string.save_and_continue, null)
                .setNegativeButton(R.string.cancel, (d, which) -> d.dismiss())
                .setCancelable(false).create();
        dialog.show();//必须加这句，不然后面会报空指针错误
        dialog.getButton((int) AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (name_table.validate()) {
                    place.PlaceTable = name_table.getText().toString().trim();
                    Global.db.update(place);
                    categoryTab_table.notifyDataSetChanged();
                    dialog.dismiss();
                }
            }
        });
    }

    private void DeletePlace(int position) {
        new AlertDialog.Builder(this).setTitle(R.string.confirm_delete_category)
                .setPositiveButton(R.string.confirm, (dialog, which) -> {
                    Global.db.deleteByWhere(FoodMenu.class, "CategoryId = '" + Places_Table.get(position).PlaceId+"'");
                    Global.db.delete(Places_Table.get(position));
                    Places_Table.remove(position);
                    fragments_table.remove(position);
                    tablePagerAdapter.notifyDataSetChanged();
                    categoryTab_table.notifyDataSetChanged();
                }).setNegativeButton(R.string.cancel, null).show();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }


}
