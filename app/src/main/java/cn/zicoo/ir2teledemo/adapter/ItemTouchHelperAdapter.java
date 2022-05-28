package cn.zicoo.ir2teledemo.adapter;

import android.support.v7.widget.RecyclerView;

public interface ItemTouchHelperAdapter {
    void OnItemMove(RecyclerView.ViewHolder source, RecyclerView.ViewHolder target);

    //数据删除
    void OnItemDissmiss(RecyclerView.ViewHolder source);

    //drag或者swipe选中
    void OnItemSelect(RecyclerView.ViewHolder source);

    //状态清除
    void OnItemClear(RecyclerView.ViewHolder source);
}
