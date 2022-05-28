package cn.zicoo.ir2teledemo.fragment;

import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import cn.zicoo.ir2teledemo.R;

import cn.zicoo.ir2teledemo.MainActivity;
import cn.zicoo.ir2teledemo.adapter.ProductAdapter;
import cn.zicoo.ir2teledemo.application.CustomApplication;
import cn.zicoo.ir2teledemo.base.AdapterListener;

import org.kymjs.kjframe.ui.BindView;
import org.kymjs.kjframe.ui.SupportFragment;

import java.util.ArrayList;
import java.util.List;

import cn.zicoo.ir2teledemo.common.Global;
import cn.zicoo.ir2teledemo.model.FoodMenu;
import mehdi.sakout.dynamicbox.DynamicBox;


/**
 * Created by Tech07 on 2016/1/26.
 */
public class FoodFragment extends SupportFragment {
    @BindView(id = R.id.recycleView)
    protected RecyclerView recycleView;
    protected List<FoodMenu> mList;
    protected ProductAdapter adapter;
    protected MainActivity aty;
    protected CustomApplication app;
    private int categoryId;
    private DynamicBox box;

    @Override
    protected View inflaterView(LayoutInflater inflater, ViewGroup container, Bundle bundle) {
        return inflater.inflate(R.layout.recycle_view, container, false);
    }

    @Override
    protected void initData() {
        aty = (MainActivity) getActivity();
        app = ((CustomApplication) aty.getApplication());
//        user = app.getUser();
        categoryId = getArguments().getInt("categoryId");
        if (Global.products.get(categoryId) == null)
            Global.products.put(categoryId, new ArrayList<>());
        mList = Global.products.get(categoryId);

    }

    @Override
    protected void initWidget(View parentView) {
        box = new DynamicBox(aty, recycleView);
        View emptyCollectionView = aty.getLayoutInflater().inflate(R.layout.empty_data, null, false);
        box.addCustomView(emptyCollectionView, "empty");
        adapter = new ProductAdapter(aty, mList);
        recycleView.setAdapter(adapter);
        adapter.setListener(new AdapterListener() {
            @Override
            public void click(View view, int position) {
                aty.orderProduct(mList.get(position));
            }
        });
        recycleView.setLayoutManager(new GridLayoutManager(aty, 4, GridLayoutManager.VERTICAL, false));

    }


    public int getLastSort() {
        if (!mList.isEmpty())
            return mList.get(mList.size() - 1).Sort + 10000;
        return 0;
    }
}
