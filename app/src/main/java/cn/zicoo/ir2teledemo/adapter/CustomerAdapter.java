package cn.zicoo.ir2teledemo.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.beardedhen.androidbootstrap.BootstrapButton;

import org.apache.commons.lang3.StringUtils;

import cn.zicoo.ir2teledemo.R;

import java.util.List;

import cn.zicoo.ir2teledemo.base.AdapterListener;
import cn.zicoo.ir2teledemo.common.ViewFindUtils;
import cn.zicoo.ir2teledemo.model.Customer;

public class CustomerAdapter extends RecyclerView.Adapter {
    protected List<Customer> items;
    protected Context cxt;
    protected LayoutInflater inflater;

    public CustomerAdapter(Context context, List<Customer> objects) {
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
        View itemView = inflater.inflate(R.layout.item_contact, viewGroup, false);
        return new CustomerAdapter.CustomerAdapterViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {
        Customer item = items.get(position);
        CustomerAdapterViewHolder holder = (CustomerAdapterViewHolder) viewHolder;
        holder.name.setText(String.format("%s(%s)", item.Name, item.Telephone));
        holder.count.setText(String.format(cxt.getString(R.string.order_count), item.OrderCount));
        holder.call.setVisibility(item.CustomerId == 0 ? View.INVISIBLE : View.VISIBLE);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    class CustomerAdapterViewHolder extends RecyclerView.ViewHolder {
        public TextView name;//
        public BootstrapButton order;//
        public TextView count;//
        public BootstrapButton call;//
        public BootstrapButton record;//

        public CustomerAdapterViewHolder(View itemView) {
            super(itemView);
            name = ViewFindUtils.hold(itemView, R.id.name);
            order = ViewFindUtils.hold(itemView, R.id.order);
            count = ViewFindUtils.hold(itemView, R.id.count);
            call = ViewFindUtils.hold(itemView, R.id.call);
            record = ViewFindUtils.hold(itemView, R.id.record);
            itemView.setOnClickListener(v -> listener.click(v, getAdapterPosition()));

            order.setOnClickListener(v -> listener.click1(v, getAdapterPosition()));
            call.setOnClickListener(v -> {
                listener.click2(v, getAdapterPosition());
            });
            record.setOnClickListener(v -> {
                listener.click3(v, getAdapterPosition());
            });
        }
    }


}
