package cn.zicoo.ir2teledemo.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Collections;
import java.util.List;

import cn.zicoo.ir2teledemo.R;
import cn.zicoo.ir2teledemo.base.AdapterListener;
import cn.zicoo.ir2teledemo.common.Global;
import cn.zicoo.ir2teledemo.common.ViewFindUtils;
import cn.zicoo.ir2teledemo.model.FoodMenu;
import cn.zicoo.ir2teledemo.model.Table;

public class TableManagerAdapter extends RecyclerView.Adapter implements  ItemTouchHelperAdapter{
    protected List<Table> items;
    protected Context cxt;
    protected LayoutInflater inflater;
    public TableManagerAdapter(Context context, List<Table> objects) {
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
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup,int viewType) {
        if (viewType == 0)
        {
            View itemView = inflater.inflate(R.layout.item_table, viewGroup, false);
            return new TableManagerAdapterViewHolder(itemView);
        }
        else
        {
            View itemView = inflater.inflate(R.layout.item_table_add, viewGroup, false);
            return new TableManagerAddAdapterViewHolder(itemView);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewholder, int position) {
        if (getItemViewType(position) == 0)
        {
            Table item = items.get(position - 1);
            TableManagerAdapter.TableManagerAdapterViewHolder holder = (TableManagerAdapter.TableManagerAdapterViewHolder) viewholder;
            holder.table_number.setText( item.table_number+"");
            holder.contain.setText( item.contains_number+"");
        }
    }


    @Override
    public int getItemCount() {
        return items.size()+1;
    }


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
        Table tableItem = items.get(fromPosition - 1);
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
            tableItem.Sort = items.get(1).Sort - 10000;
        }
        else if (toPosition >= items.size())
        {
            tableItem.Sort = items.get(items.size() - 1).Sort + 10000;
        }
        else
            tableItem.Sort = (items.get(toPosition - 2).Sort + items.get(toPosition).Sort) / 2;

        //移动过程中移除view的放大效b果
        OnItemClear(source);
        Global.db.update(tableItem,null,"Sort");

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

    class TableManagerAdapterViewHolder extends RecyclerView.ViewHolder
    {
        public TextView table_number;//
        public TextView contain;//
        public TableManagerAdapterViewHolder(View itemView)
        {
            super(itemView);
            table_number = ViewFindUtils.hold(itemView, R.id.table_number);
            contain = ViewFindUtils.hold(itemView, R.id.contain);
            itemView.setOnClickListener(v -> listener.click(v, getAdapterPosition() - 1));

        }
    }
    class TableManagerAddAdapterViewHolder extends RecyclerView.ViewHolder
    {
        public ImageView add;//
        public TableManagerAddAdapterViewHolder(View itemView)
        {
            super(itemView);
            itemView.setOnClickListener(v -> listener.click1(v,getAdapterPosition()));
            add = ViewFindUtils.hold(itemView, R.id.addTable);
        }
    }


}
