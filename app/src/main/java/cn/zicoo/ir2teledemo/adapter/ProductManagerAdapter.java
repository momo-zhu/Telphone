package cn.zicoo.ir2teledemo.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import cn.zicoo.ir2teledemo.R;

import java.util.Collections;
import java.util.List;

import cn.zicoo.ir2teledemo.base.AdapterListener;
import cn.zicoo.ir2teledemo.common.Global;
import cn.zicoo.ir2teledemo.common.ViewFindUtils;
import cn.zicoo.ir2teledemo.model.FoodMenu;

public class ProductManagerAdapter extends RecyclerView.Adapter implements  ItemTouchHelperAdapter{
    protected List<FoodMenu> items;
    protected Context cxt;
    protected LayoutInflater inflater;
    public ProductManagerAdapter(Context context, List<FoodMenu> objects) {
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
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        if (viewType == 0)
        {
            View itemView = inflater.inflate(R.layout.item_food, viewGroup, false);
            return new ProductManagerAdapterViewHolder(itemView);
        }
        else
        {
            View itemView = inflater.inflate(R.layout.item_food_add, viewGroup, false);
            return new ProductManagerAddAdapterViewHolder(itemView);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewholder, int position) {
        if (getItemViewType(position) == 0)
        {
            FoodMenu item = items.get(position - 1);
            ProductManagerAdapter.ProductManagerAdapterViewHolder holder = (ProductManagerAdapter.ProductManagerAdapterViewHolder) viewholder;
            holder.name.setText( item.Name);
            holder.code.setText( item.Code);
            if (item.hasSkus())
                holder.price.setText(String.format(cxt.getString(R.string.price_low), item.getMinPrice()));
            else
                holder.price.setText(String.format("%.2f", item.Price));
        }
    }

    @Override
    public int getItemCount() {
        return items.size() + 1;
    }

    @Override
    public int getItemViewType(int position) {
        return position == 0 ? 1 : 0;
    }

    @Override
    public void OnItemMove(RecyclerView.ViewHolder source, RecyclerView.ViewHolder target) {
        int fromPosition = source.getAdapterPosition();
        int toPosition = target.getAdapterPosition();
        if (fromPosition == 0)
            return;
        if (toPosition == 0)
            toPosition = 1;
        FoodMenu fromItem = items.get(fromPosition - 1);
        if (fromPosition < toPosition)
        {
            for (int i = fromPosition - 1; i < toPosition - 1; i++)
            {
                Collections.swap(items,i,i+1);
            }
        }
        else
        {
            for (int i = fromPosition - 1; i > toPosition - 1; i--)
            {
                Collections.swap(items,i,i-1);
            }
        }
        //交换数据位置

        //刷新位置交换
        notifyItemMoved(fromPosition, toPosition);
        if (toPosition == 1)
        {
            fromItem.Sort = items.get(1).Sort - 10000;
        }
        else if (toPosition >= items.size())
        {
            fromItem.Sort = items.get(items.size() - 1).Sort + 10000;
        }
        else
            fromItem.Sort = (items.get(toPosition - 2).Sort + items.get(toPosition).Sort) / 2;

        //移动过程中移除view的放大效b果
        OnItemClear(source);
        Global.db.update(fromItem,null,"Sort");

    }

    @Override
    public void OnItemDissmiss(RecyclerView.ViewHolder source) {

    }

    @Override
    public void OnItemSelect(RecyclerView.ViewHolder source) {
        source.itemView.setScaleX(1.2f);
        source.itemView.setScaleY(1.2f);
    }

    @Override
    public void OnItemClear(RecyclerView.ViewHolder source) {
        source.itemView.setScaleX(1.0f);
        source.itemView.setScaleY(1.0f);
    }

    class ProductManagerAdapterViewHolder extends RecyclerView.ViewHolder
    {
        public TextView name;//
        public TextView price;//ProductManagerAdapterClickEventArgs
        public TextView code;//
        public ProductManagerAdapterViewHolder(View itemView)
        {
            super(itemView);
            name = ViewFindUtils.hold(itemView, R.id.name);
            price = ViewFindUtils.hold(itemView, R.id.price);
            code = ViewFindUtils.hold(itemView, R.id.code);
            itemView.setOnClickListener(v -> listener.click(v, getAdapterPosition() - 1));

        }
    }
    class ProductManagerAddAdapterViewHolder extends RecyclerView.ViewHolder
    {
        public ImageView add;//
        public ProductManagerAddAdapterViewHolder(View itemView)
        {
            super(itemView);
            itemView.setOnClickListener(v -> listener.click1(v,getAdapterPosition()));
            add = ViewFindUtils.hold(itemView, R.id.addFood);
        }
    }


}
