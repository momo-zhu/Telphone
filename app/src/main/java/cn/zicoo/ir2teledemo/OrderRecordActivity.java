package cn.zicoo.ir2teledemo;

import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshRecyclerView;

import org.kymjs.kjframe.KJActivity;
import org.kymjs.kjframe.ui.BindView;

import java.util.ArrayList;
import java.util.List;

import cn.zicoo.ir2teledemo.adapter.OrderItemAdapter;
import cn.zicoo.ir2teledemo.adapter.OrderRecordAdapter;
import cn.zicoo.ir2teledemo.base.AdapterListener;
import cn.zicoo.ir2teledemo.common.Global;
import cn.zicoo.ir2teledemo.common.PrintClient;
import cn.zicoo.ir2teledemo.common.Util;
import cn.zicoo.ir2teledemo.model.Customer;
import cn.zicoo.ir2teledemo.model.Order;
import cn.zicoo.ir2teledemo.model.OrderItem;
import cn.zicoo.ir2teledemo.model.OrderItemModel;

public class OrderRecordActivity extends KJActivity {
    @BindView(id = R.id.recycleView)
    private PullToRefreshRecyclerView recyclerView;
    private OrderRecordAdapter adapter;
    protected List<Order> mList;
    private Customer customer;

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

        int customerId = getIntent().getIntExtra("customerId",0);
        if(customerId != 0)
            customer = Global.db.findById(customerId,Customer.class);
        else
            customer = Global.guest;
        mList = new ArrayList<>();

        adapter = new OrderRecordAdapter(this, mList);

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
                Load(mList.size() / 20 + 1);

            }
        });

        Load(1);
        adapter.setListener(new AdapterListener() {
            @Override
            public void click(View view1, int position) {
                Order order = mList.get(position);
                View v = getLayoutInflater().inflate(R.layout.dialog_order_detail, null);
                ListView listView = v.findViewById(R.id.listView);
                List<OrderItem> orderItems = Global.db.findAllByWhere(OrderItem.class, "OrderId = " + order.OrderId);
                List<OrderItemModel> objects = new ArrayList<>();
                for (OrderItem orderItem : orderItems)
                    objects.add(new OrderItemModel(orderItem.Name, orderItem.Properties, orderItem.SubTotal));
                OrderItemAdapter adapter = new OrderItemAdapter(aty, R.layout.item_order_item, objects);
                listView.setAdapter(adapter);
                listView.post(() ->
                {
                    Util.setListViewHeightBasedOnChildren(listView);
                });
                new AlertDialog.Builder(aty).setTitle(R.string.detail).setView(v)
                        .setPositiveButton(R.string.print, (dialog, which) -> PrintClient.Print(order, customer,aty))
                        .setNegativeButton(R.string.cancel, null)
                        .show();
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }


    private void Load(int page) {
        if (page == 1)
            mList.clear();
        String where = "";
        if (customer != null)
            where = "CustomerId = '" + customer.CustomerId +"'";
        List<Order> items = Global.db.findAllByWhere(Order.class, where, "CreateDate DESC LIMIT 20 OFFSET " + (page - 1) * 20);
        mList.addAll(items);
        if (items.size() >= 20)
            recyclerView.setMode(PullToRefreshBase.PtrMode.BOTH);
        else
            recyclerView.setMode(PullToRefreshBase.PtrMode.PULL_FROM_START);

        adapter.notifyDataSetChanged();
        recyclerView.onRefreshComplete();

    }

}
