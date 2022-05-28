package cn.zicoo.ir2teledemo.base;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import cn.zicoo.ir2teledemo.common.OnItemClickListener;

public class ViewHolder extends RecyclerView.ViewHolder {
    public ViewHolder(View itemView, final OnItemClickListener listener) {
        super(itemView);
        itemView.setOnClickListener(v -> {
            if (listener != null)
                listener.onItemClick(v, getAdapterPosition());
        });
    }
}