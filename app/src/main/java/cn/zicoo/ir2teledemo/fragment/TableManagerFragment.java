package cn.zicoo.ir2teledemo.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.beardedhen.androidbootstrap.BootstrapButton;
import com.rengwuxian.materialedittext.MaterialEditText;
import com.rengwuxian.materialedittext.validation.METValidator;

import org.apache.commons.lang3.StringUtils;
import org.kymjs.kjframe.ui.BindView;
import org.kymjs.kjframe.ui.SupportFragment;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import cn.zicoo.ir2teledemo.ProductManagerActivity;
import cn.zicoo.ir2teledemo.R;
import cn.zicoo.ir2teledemo.TableManagerActivity;
import cn.zicoo.ir2teledemo.adapter.ProductManagerAdapter;
import cn.zicoo.ir2teledemo.adapter.SimpleItemTouchHelperCallback;
import cn.zicoo.ir2teledemo.adapter.TableManagerAdapter;
import cn.zicoo.ir2teledemo.application.CustomApplication;
import cn.zicoo.ir2teledemo.base.AdapterListener;
import cn.zicoo.ir2teledemo.common.Global;
import cn.zicoo.ir2teledemo.model.FoodMenu;
import cn.zicoo.ir2teledemo.model.FoodMenuItem;
import cn.zicoo.ir2teledemo.model.FoodMenuProperty;
import cn.zicoo.ir2teledemo.model.Table;


/**
 * Created by Tech07 on 2016/1/26.
 */
public class TableManagerFragment extends SupportFragment {
    @BindView(id = R.id.recycleView)
    protected RecyclerView recycleView;
    protected List<Table> mList;
    protected TableManagerAdapter adapter;
    protected TableManagerActivity aty;
    protected CustomApplication app;
    private int placeId;

    @Override
    protected View inflaterView(LayoutInflater inflater, ViewGroup container, Bundle bundle) {
        return inflater.inflate(R.layout.recycle_view, container, false);
    }

    @Override
    protected void initData() {
        aty = (TableManagerActivity) getActivity();
        app = ((CustomApplication) aty.getApplication());
        placeId = getArguments().getInt("placeId");
        if (Global.tables.get(placeId) == null)
            Global.tables.put(placeId, new ArrayList<>());
        mList = Global.tables.get(placeId);

    }

    @Override
    protected void initWidget(View parentView) {
        adapter = new TableManagerAdapter(aty, mList);
        recycleView.setAdapter(adapter);
        adapter.setListener(new AdapterListener() {
            @Override
            public void click(View view, int position) {
                TableMenu(view, position);
            }

            @Override
            public void click1(View view, int position) {
                TableMenuAdd();
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

    private void TableMenuAdd() {
        View addView = getLayoutInflater().inflate(R.layout.dialog_add_table, null);
        MaterialEditText table_number = addView.findViewById(R.id.table_number);
        MaterialEditText contains_number = addView.findViewById(R.id.contains_number);
        table_number.addValidator(new METValidator(getString(R.string.input_table_number)) {
            @Override
            public boolean isValid(@NonNull CharSequence text, boolean isEmpty) {
                return !isEmpty;
            }
        });
        contains_number.addValidator(new METValidator(getString(R.string.input_table_contains)) {
            @Override
            public boolean isValid(@NonNull CharSequence text, boolean isEmpty) {
                return !isEmpty;
            }
        });

        final Table[] table = {new Table()};
        AlertDialog dialog = new AlertDialog.Builder(aty).setTitle(R.string.add_table_adress).setView(addView)
                .setPositiveButton(R.string.save_and_continue, null)
                .setNegativeButton(R.string.cancel, (d, which) -> d.dismiss())
                .setCancelable(false).create();
        dialog.show();//必须加这句，不然后面会报空指针错误
        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (table_number.validate() && contains_number.validate() ) {
                    table[0].table_number =  Integer.parseInt(table_number.getText().toString());
                    table[0].PlaceId = placeId;
                    table[0].Sort = getLastSort();
                    table[0].contains_number = Integer.parseInt(contains_number.getText().toString());
                    SaveTable(table[0]);
                    table[0] = new Table();
                    table_number.setText("");
                    contains_number.setText("");
                }
            }
        });
    }

    private void TableMenu(View view, int position) {
        PopupMenu popup = new PopupMenu(aty, view);
        //将R.menu.popup_menu菜单资源加载到popup菜单中
        popup.getMenuInflater().inflate(R.menu.menu_product, popup.getMenu());
        popup.setOnMenuItemClickListener(menuItem -> {
            switch (menuItem.getItemId()) {
                case R.id.modify:
                    ModifyTable(position);
                    break;
                case R.id.delete:
                    Table place = mList.get(position);
                    mList.remove(position);
                    adapter.notifyItemRemoved(position + 1);
                    Global.db.delete(place);
                    break;
            }
            return true;
        });
        popup.show();
    }

    private void ModifyTable(int position) {
        Table table = mList.get(position);

        View addView = getLayoutInflater().inflate(R.layout.dialog_add_table, null);
        MaterialEditText table_number = addView.findViewById(R.id.table_number);
        MaterialEditText contains_number = addView.findViewById(R.id.contains_number);
        table_number.addValidator(new METValidator(getString(R.string.please_input_name)) {
            @Override
            public boolean isValid(@NonNull CharSequence text, boolean isEmpty) {
                return !isEmpty;
            }
        });

        contains_number.addValidator(new METValidator(getString(R.string.please_input_name)) {
            @Override
            public boolean isValid(@NonNull CharSequence text, boolean isEmpty) {
                return !isEmpty;
            }
        });

        table_number.setText(table.table_number+"");
        contains_number.setText(table.contains_number+"");
        AlertDialog dialog = new AlertDialog.Builder(aty).setTitle(R.string.modify_product).setView(addView)
                .setPositiveButton(R.string.save, null)
                .setNegativeButton(R.string.cancel, (d, which) -> d.dismiss())
                .setCancelable(false).create();

        dialog.show();
        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (table_number.validate() &&contains_number.validate() ) {
                    table.table_number =  Integer.parseInt(table_number.getText().toString());
                    table.contains_number =  Integer.parseInt(contains_number.getText().toString());
                    adapter.notifyItemChanged(position + 1);
                    Global.db.update(table);
                    dialog.dismiss();
                }
            }
        });
    }


    public void SaveTable(Table table) {
        mList.add(table);
        adapter.notifyItemInserted(mList.size());
        Global.db.saveBindId(table);
    }




}
