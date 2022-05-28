package cn.zicoo.ir2teledemo.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.beardedhen.androidbootstrap.BootstrapButton;

import org.apache.commons.lang3.StringUtils;

import cn.zicoo.ir2teledemo.R;

import java.util.Date;
import java.util.List;

import cn.zicoo.ir2teledemo.base.AdapterListener;
import cn.zicoo.ir2teledemo.common.DateUtils;
import cn.zicoo.ir2teledemo.common.Global;
import cn.zicoo.ir2teledemo.common.Util;
import cn.zicoo.ir2teledemo.common.ViewFindUtils;
import cn.zicoo.ir2teledemo.model.CallRecord;

public class CallRecordAdapter extends RecyclerView.Adapter {
    protected List<CallRecord> items;
    protected Context cxt;
    protected LayoutInflater inflater;
    private AdapterListener listener;

    public void setListener(AdapterListener listener) {
        this.listener = listener;
    }

    public CallRecordAdapter(Context context, List<CallRecord> objects) {
        this.cxt = context;
        this.items = objects;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = inflater.inflate(R.layout.item_call, viewGroup, false);
        return new CallRecordAdapterViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {
        CallRecord item = items.get(position);
        CallRecordAdapterViewHolder holder = (CallRecordAdapterViewHolder) viewHolder;

        holder.time.setText(Util.getDateDiff(item.Date));
        holder.name.setText(Global.customerPhoneNameDict.containsKey(item.Number) ? Global.customerPhoneNameDict.get(item.Number) + "(" + item.Number + ")" : item.Number);
        holder.type.setText(DateUtils.sdfMS.format(new Date(item.Duration * 1000)));
        switch (item.Type) {
            case 1:
                holder.icon.setImageResource(R.drawable.icon_incoming);
                break;
            case 2:
                holder.icon.setImageResource(R.drawable.icon_outcoming);
                break;
            case 3:
                holder.icon.setImageResource(R.drawable.icon_missing);
                break;
            case 5:
            case 6:
                holder.icon.setImageResource(R.drawable.icon_reject);
                break;
            default:
                holder.icon.setImageResource(R.drawable.icon_incoming);
                break;


        }
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    class CallRecordAdapterViewHolder extends RecyclerView.ViewHolder {
        ImageView icon;//
        TextView name;//
        TextView time;//
        TextView type;//
        BootstrapButton order;//
        BootstrapButton call;//

        public CallRecordAdapterViewHolder(View itemView) {
            super(itemView);

            name = ViewFindUtils.hold(itemView, R.id.name);
            time = ViewFindUtils.hold(itemView, R.id.time);
            icon = ViewFindUtils.hold(itemView, R.id.icon);
            type = ViewFindUtils.hold(itemView, R.id.type);
            order = ViewFindUtils.hold(itemView, R.id.order);

            call = ViewFindUtils.hold(itemView, R.id.call);

            order.setOnClickListener(v -> listener.click1(v, getAdapterPosition()));
            call.setOnClickListener(v -> {
                listener.click2(v, getAdapterPosition());
            });
        }
    }

}
