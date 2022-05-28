package cn.zicoo.ir2teledemo;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.text.format.DateUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshRecyclerView;
import com.rengwuxian.materialedittext.MaterialEditText;
import com.rengwuxian.materialedittext.validation.METValidator;

import org.apache.commons.lang3.StringUtils;
import org.kymjs.kjframe.KJActivity;
import org.kymjs.kjframe.ui.BindView;
import org.kymjs.kjframe.ui.ViewInject;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import cn.zicoo.ir2teledemo.adapter.CustomerAdapter;
import cn.zicoo.ir2teledemo.base.AdapterListener;
import cn.zicoo.ir2teledemo.common.Global;
import cn.zicoo.ir2teledemo.model.Customer;
import cn.zicoo.tele.TeleManager;

public class CustomerActivity extends KJActivity {
    @BindView(id = R.id.recycleView)
    private PullToRefreshRecyclerView recyclerView;
    private CustomerAdapter adapter;
    protected List<Customer> mList;
    @BindView(id = R.id.searchView)
    private android.support.v7.widget.SearchView searchView;
    private String query;

    @Override
    public void setRootView() {
        setContentView(R.layout.pull_to_refresh_recycle);
    }

    @Override
    public void initData() {
        super.initData();
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mList = new ArrayList<>();

        adapter = new CustomerAdapter(this, mList);

        recyclerView.getRefreshableView().setAdapter(adapter);
        recyclerView.getRefreshableView().setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        recyclerView.getRefreshableView().addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        recyclerView.setMode(PullToRefreshBase.PtrMode.PULL_FROM_START);
        recyclerView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<RecyclerView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<RecyclerView> refreshView) {
                String label = DateUtils.formatDateTime(aty, System.currentTimeMillis(),
                        DateUtils.FORMAT_SHOW_TIME | DateUtils.FORMAT_ABBREV_ALL);
                // Update the LastUpdatedLabel
                recyclerView.getLoadingLayoutProxy().setLastUpdatedLabel(label);
                // Do work to refresh the msgList here.
                Load(1);
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<RecyclerView> refreshView) {
                Load((mList.size() - 1)/ 20 + 1);

            }
        });

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                query = s;
                searchView.clearFocus(); // 不获取焦点
                recyclerView.setRefreshing();
                return true;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                return false;
            }
        });
        searchView.setOnCloseListener(() -> {
            query = null;
            searchView.clearFocus(); // 不获取焦点
            recyclerView.setRefreshing();
            return true;
        });

        Load(1);
        adapter.setListener(new AdapterListener() {
            @Override
            public void click(View view, int position) {
                if(mList.get(position).CustomerId > 0) {
                    PopupMenu popup = new PopupMenu(aty, view);
                    //将R.menu.popup_menu菜单资源加载到popup菜单中
                    popup.getMenuInflater().inflate(R.menu.menu_product, popup.getMenu());
                    popup.setOnMenuItemClickListener(menuItem -> {
                        switch (menuItem.getItemId()) {
                            case R.id.modify:
                                ModifyCustomer(position);
                                break;
                            case R.id.delete:
                                DeleteCustomer(position);
                                break;
                        }
                        return true;
                    });
                    popup.show();
                }
            }

            @Override
            public void click1(View view, int position) {
                Intent intent = new Intent();
                intent.putExtra("customerId", mList.get(position).CustomerId);
                setResult(RESULT_OK, intent);
                finish();
            }

            @Override
            public void click2(View view, int position) {
                if(TeleManager.getInstance().getTeleState() == 0){
                    TeleManager.getInstance().dialNumber(mList.get(position).Telephone);
                }
                else{
                    ViewInject.toast(R.string.unable_make_phone_call);
                }
            }

            @Override
            public void click3(View view, int position) {
                Intent intent = new Intent(aty, OrderRecordActivity.class);
                intent.putExtra("customerId", mList.get(position).CustomerId);
                startActivity(intent);
            }
        });
    }

    private void ModifyCustomer(int position) {
        Customer customer = mList.get(position);
        View addView = getLayoutInflater().inflate(R.layout.dialog_add_customer, null);
        MaterialEditText name = addView.findViewById(R.id.name);
        MaterialEditText phone = addView.findViewById(R.id.phone);
        MaterialEditText email = addView.findViewById(R.id.email);
        name.addValidator(new METValidator(getString(R.string.input_customer_name)) {
            @Override
            public boolean isValid(@NonNull CharSequence text, boolean isEmpty) {
                return !isEmpty;
            }
        });
        phone.addValidator(new METValidator(getString(R.string.input_customer_phone)) {
            @Override
            public boolean isValid(@NonNull CharSequence text, boolean isEmpty) {
                return !isEmpty;
            }
        });
        email.addValidator(new METValidator(getString(R.string.input_emailaddress)) {
            @Override
            public boolean isValid(@NonNull CharSequence text, boolean isEmpty) {
                return !isEmpty;
            }
        });
        name.setText(customer.Name);
        phone.setText(customer.Telephone);
        email.setText(customer.EmailAddress);
        AlertDialog dialog = new AlertDialog.Builder(this).setTitle(R.string.modify_customer).setView(addView)
                .setPositiveButton(R.string.confirm, null)
                .setNegativeButton(R.string.cancel, (d, which) -> d.dismiss())
                .setCancelable(false).create();
        dialog.show();
        dialog.getButton((int) AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (name.validate() && phone.validate() && email.validate()) {
                    customer.Name = name.getText().toString();
                    customer.Telephone = phone.getText().toString();
                    customer.EmailAddress = email.getText().toString();
                    customer.UpdateDate = new Date();
                    adapter.notifyItemChanged(position);
                    Global.db.update(customer);
                    dialog.dismiss();

                }
            }
        });
    }

    private void DeleteCustomer(int position) {
        Customer customer = mList.get(position);
        new AlertDialog.Builder(this).setTitle(R.string.confirm_delete_customer)
                .setPositiveButton(R.string.confirm, (dialog, which) -> {
                            mList.remove(position);
                            adapter.notifyItemRemoved(position);
                            Global.db.delete(customer);
                            Global.customerPhoneNameDict.remove(customer.Telephone);
                            Global.customerIdNameDict.remove(customer.CustomerId);

                            //Global.db.ExecuteSql("DELETE t1,t2 FROM OrderItem t1 INNER JOIN Order t2 ON t1.OrderId = t2.OrderId WHERE t1.MemberKey = ?", new[] { customer.CardNO });
                        }
                )
                .setNegativeButton(R.string.cancel, null)
                .show();//必须加这句，不然后面会报空指针错误
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_customer, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        } else if (item.getItemId()  == R.id.addCustomer) {
            View addView = getLayoutInflater().inflate(R.layout.dialog_add_customer, null);
            MaterialEditText name = addView.findViewById(R.id.name);
            MaterialEditText phone = addView.findViewById(R.id.phone);
            MaterialEditText email = addView.findViewById(R.id.email);
            name.addValidator(new METValidator(getString(R.string.input_customer_name)) {
                @Override
                public boolean isValid(@NonNull CharSequence text, boolean isEmpty) {
                    return !isEmpty;
                }
            });
            phone.addValidator(new METValidator(getString(R.string.input_customer_phone)) {
                @Override
                public boolean isValid(@NonNull CharSequence text, boolean isEmpty) {
                    return !isEmpty;
                }
            });
            email.addValidator(new METValidator(getString(R.string.input_emailaddress)) {
                @Override
                public boolean isValid(@NonNull CharSequence text, boolean isEmpty) {
                    return !isEmpty;
                }
            });
            AlertDialog dialog = new AlertDialog.Builder(this).setTitle(R.string.add_customer).setView(addView)
                    .setPositiveButton(R.string.save, null)
                    .setNegativeButton(R.string.cancel, (d, which) -> d.dismiss())
                    .setCancelable(false).create();
            dialog.show();
            dialog.getButton((int) AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (name.validate() && phone.validate() && email.validate()) {
                        Customer customer = new Customer();
                        {
                            customer.Name = name.getText().toString();
                            customer.Telephone = phone.getText().toString();
                            customer.EmailAddress = email.getText().toString();
                            customer.CardNO = UUID.randomUUID().toString().replaceAll("-","");
                        } ;
                        Global.db.saveBindId(customer);
                        Global.customerPhoneNameDict.put(customer.Telephone, customer.Name);
                        Global.customerIdNameDict.put(customer.CustomerId, customer.Name);
                        mList.add(1, customer);
                        adapter.notifyItemInserted(1);
                        dialog.dismiss();
                    }

                }
            });
        }
        return super.onOptionsItemSelected(item);
    }


    private void Load(int page) {
        if (page == 1) {
            mList.clear();
            mList.add(Global.guest);
        }
        String where  = "";
        if (StringUtils.isNotBlank(query))
            where = "Name LIKE '%"+query+"%' OR Telephone LIKE '%" +query+"%'";
        List<Customer> items = Global.db.findAllByWhere(Customer.class, where, "UpdateDate DESC LIMIT 20 OFFSET " + (page - 1) * 20);
        mList.addAll(items);
        if (items.size() >= 20)
            recyclerView.setMode(PullToRefreshBase.PtrMode.BOTH);
        else
            recyclerView.setMode(PullToRefreshBase.PtrMode.PULL_FROM_START);

        adapter.notifyDataSetChanged();
        recyclerView.onRefreshComplete();

    }

}
