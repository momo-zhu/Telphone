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
import cn.zicoo.ir2teledemo.common.ViewFindUtils;
import cn.zicoo.ir2teledemo.model.FoodMenu;

public class ProductAdapter extends RecyclerView.Adapter{
    protected List<FoodMenu> items;
    protected Context cxt;
    protected LayoutInflater inflater;
    public ProductAdapter(Context context, List<FoodMenu> objects) {
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
        View itemView = inflater.inflate(R.layout.item_food, viewGroup, false);
        return new ProductAdapter.ProductAdapterViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {
        FoodMenu item = items.get(position);
        ProductAdapterViewHolder holder = (ProductAdapterViewHolder) viewHolder;
        holder.name.setText(item.Name);
        holder.code.setText( item.Code);
        if (item.hasSkus())
            holder.price.setText(String.format(cxt.getString(R.string.price_low), item.getMinPrice()));
            else
        holder.price.setText(String.format("%.2f", item.Price));
    }

    @Override
    public int getItemCount() {
        return items.size();
    }
    class ProductAdapterViewHolder extends RecyclerView.ViewHolder
    {
        public TextView name;//
        public TextView price;//
        public TextView code;//
        public ProductAdapterViewHolder(View itemView)
        {
            super(itemView);
            name = ViewFindUtils.hold(itemView, R.id.name);
            price = ViewFindUtils.hold(itemView, R.id.price);
            code = ViewFindUtils.hold(itemView, R.id.code);
            itemView.setOnClickListener(v -> listener.click(v, getAdapterPosition()));
            itemView.setOnLongClickListener(v -> {
                listener.longClick(v, getAdapterPosition());
                return true;
            });

        }
    }


}
