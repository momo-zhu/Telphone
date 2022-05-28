package cn.zicoo.ir2teledemo.base;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;

import cn.zicoo.ir2teledemo.common.OnItemClickListener;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

/**
 * Created by mummyding on 15-8-14.
 */
public abstract class BaseListenerAdapter<T, V extends RecyclerView.ViewHolder> extends RecyclerView.Adapter<V> {
    protected final List<T> items;
    protected final Context cxt;
    protected LayoutInflater inflater;
    protected SimpleDateFormat format = new SimpleDateFormat("MM-dd HH:mm", Locale.getDefault());
    protected OnItemClickListener listener;
    protected OnItemClickListener longClickListener;
    protected OnItemClickListener otherClickListener;

    public BaseListenerAdapter(Context context, List<T> objects) {
        this.cxt = context;
        this.items = objects;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }

    public void remove(int position) {
        items.remove(position);
        notifyItemRemoved(position);
    }

    public void remove(T item) {
        int index = items.indexOf(item);
        if (index != -1) remove(index);
    }

    public void insert(int position, T item) {
        items.add(position, item);
        if (items.size() == 1)
            notifyDataSetChanged();
        else
            notifyItemInserted(position);
    }

    public T getItem(int position) {
        return items.get(position);
    }

    public void setListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public void setLongClickListener(OnItemClickListener longClickListener) {
        this.longClickListener = longClickListener;
    }

    public void setOtherClickListener(OnItemClickListener otherClickListener) {
        this.otherClickListener = otherClickListener;
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }
}

