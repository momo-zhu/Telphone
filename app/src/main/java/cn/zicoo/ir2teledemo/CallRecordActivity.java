package cn.zicoo.ir2teledemo;

import android.content.Intent;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateUtils;
import android.view.MenuItem;
import android.view.View;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshRecyclerView;
import cn.zicoo.ir2teledemo.R;

import org.kymjs.kjframe.KJActivity;
import org.kymjs.kjframe.ui.BindView;
import org.kymjs.kjframe.ui.ViewInject;
import org.kymjs.kjframe.utils.StringUtils;

import java.util.ArrayList;
import java.util.List;

import cn.zicoo.ir2teledemo.adapter.CallRecordAdapter;
import cn.zicoo.ir2teledemo.base.AdapterListener;
import cn.zicoo.ir2teledemo.common.Util;
import cn.zicoo.ir2teledemo.model.CallRecord;
import cn.zicoo.tele.TeleManager;

public class CallRecordActivity extends KJActivity {
    @BindView(id = R.id.recycleView)
    private PullToRefreshRecyclerView recyclerView;
    private CallRecordAdapter adapter;
    protected List<CallRecord> mList;

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

        adapter = new CallRecordAdapter(this, mList);

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
            public void click1(View view, int position) {
                Intent intent = new Intent();
                intent.putExtra("phoneNumber", mList.get(position).Number);
                setResult(RESULT_OK, intent);
                finish();
            }

            @Override
            public void click2(View view, int position) {
                if(TeleManager.getInstance().getTeleState() == 0){
                    TeleManager.getInstance().dialNumber(mList.get(position).Number);
                }
                else{
                    ViewInject.toast(R.string.unable_make_phone_call);
                }

            }
        });
    }


    private void Load(int page) {
        if (page == 1)
            mList.clear();
        List<CallRecord> items = Util.callRecords(this, page, 20);
        if (items.size() >= 20)
            recyclerView.setMode(PullToRefreshBase.PtrMode.BOTH);
        else
            recyclerView.setMode(PullToRefreshBase.PtrMode.PULL_FROM_START);

        mList.addAll(items);
        adapter.notifyDataSetChanged();
        recyclerView.onRefreshComplete();

    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
