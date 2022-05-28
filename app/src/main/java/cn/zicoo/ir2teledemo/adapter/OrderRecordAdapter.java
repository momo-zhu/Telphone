package cn.zicoo.ir2teledemo.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import cn.zicoo.ir2teledemo.R;

import java.util.List;

import cn.zicoo.ir2teledemo.base.AdapterListener;
import cn.zicoo.ir2teledemo.common.DateUtils;
import cn.zicoo.ir2teledemo.common.Global;
import cn.zicoo.ir2teledemo.common.ViewFindUtils;
import cn.zicoo.ir2teledemo.model.Order;

public class OrderRecordAdapter extends RecyclerView.Adapter{
    protected List<Order> items;
    protected Context cxt;
    protected LayoutInflater inflater;
    public OrderRecordAdapter(Context context, List<Order> objects) {
        this.cxt = context;
        this.items = objects;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }
    private AdapterListener listener;

    public void setListener(AdapterListener listener) {
        this.listener = listener;
    }
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = inflater.inflate(R.layout.item_order_record, viewGroup, false);
        return new OrderRecordAdapter.OrderRecordAdapterViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {
        Order item = items.get(position);
        OrderRecordAdapterViewHolder holder = (OrderRecordAdapterViewHolder) viewHolder;
        holder.time.setText( cxt.getString(R.string.order_time) + DateUtils.formatDate(item.CreateDate,"MM-dd HH:mm"));
        holder.name.setText(Global.customerIdNameDict.containsKey(item.CustomerId) ? Global.customerIdNameDict.get(item.CustomerId) :null);

        holder.totalPrice.setText(cxt.getString(R.string.order_amount)+String.format("%.2f",item.SubTotal));
    }

    @Override
    public int getItemCount() {
        return items.size();
    }
    class OrderRecordAdapterViewHolder extends RecyclerView.ViewHolder
    {
        public TextView time;//
        public TextView name;//
        public TextView totalPrice;//
        public OrderRecordAdapterViewHolder(View itemView)
        {
            super(itemView);
            name = ViewFindUtils.hold(itemView, R.id.name);
            time = ViewFindUtils.hold(itemView, R.id.time);
            totalPrice = ViewFindUtils.hold(itemView, R.id.totalPrice);
            itemView.setOnClickListener(v -> listener.click(v, getAdapterPosition()));
            itemView.setOnLongClickListener(v -> {
                listener.longClick(v, getAdapterPosition());
                return true;
            });

        }
    }


}
