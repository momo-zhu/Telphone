package cn.zicoo.ir2teledemo.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import cn.zicoo.ir2teledemo.R;

import org.apache.commons.lang3.StringUtils;

import java.util.List;

import cn.zicoo.ir2teledemo.base.AdapterListener;
import cn.zicoo.ir2teledemo.common.ViewFindUtils;
import cn.zicoo.ir2teledemo.model.OrderItem;

public class ProductSelectAdapter extends RecyclerView.Adapter {
    protected List<OrderItem> items;
    protected Context cxt;
    protected LayoutInflater inflater;

    public ProductSelectAdapter(Context context, List<OrderItem> objects) {
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
        View itemView = inflater.inflate(R.layout.item_select_food, viewGroup, false);
        return new ProductSelectAdapter.ProductSelectAdapterViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewholder, int position) {
        OrderItem item = items.get(position);
        ProductSelectAdapterViewHolder holder = (ProductSelectAdapterViewHolder) viewholder;
        SpannableStringBuilder sb = new SpannableStringBuilder(item.Name);
        sb.setSpan(new ForegroundColorSpan(Color.BLACK), 0, sb.length() - 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        sb.setSpan(new AbsoluteSizeSpan(15, true), 0, sb.length() - 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        int start = item.Name.length();
        if (StringUtils.isNotBlank(item.Properties)) {
            sb.append("\n").append(item.Properties);
            sb.setSpan(new ForegroundColorSpan(Color.GRAY), start, sb.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            sb.setSpan(new AbsoluteSizeSpan(13, true), start, sb.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
        holder.name.setText(sb);

        holder.quantity.setText(String.valueOf(item.Quantity));
        holder.totalPrice.setText(String.format("%.2f", item.Price * item.Quantity));
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    class ProductSelectAdapterViewHolder extends RecyclerView.ViewHolder {
        public TextView name;//
        public TextView totalPrice;//
        public TextView quantity;//
        public Button add;//
        public Button minus;//

        public ProductSelectAdapterViewHolder(View itemView) {
            super(itemView);
            name = ViewFindUtils.hold(itemView, R.id.name);
            totalPrice = ViewFindUtils.hold(itemView, R.id.totalPrice);
            quantity = ViewFindUtils.hold(itemView, R.id.quantity);
            add = ViewFindUtils.hold(itemView, R.id.add);
            minus = ViewFindUtils.hold(itemView, R.id.minus);
            add.setOnClickListener(v -> listener.click1(v, getAdapterPosition()));
            minus.setOnClickListener(v -> listener.click2(v, getAdapterPosition()));

        }
    }


}
