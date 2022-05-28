package cn.zicoo.ir2teledemo.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import cn.zicoo.ir2teledemo.R;

import org.apache.commons.lang3.StringUtils;

import java.util.List;

import cn.zicoo.ir2teledemo.common.ViewFindUtils;
import cn.zicoo.ir2teledemo.model.OrderItemModel;

public class OrderItemAdapter extends ArrayAdapter<OrderItemModel> {
    public OrderItemAdapter(@NonNull Context context, int resource, @NonNull List<OrderItemModel> objects) {
        super(context, resource, objects);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if(convertView == null)
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_order_item, parent,false);
        TextView name = ViewFindUtils.hold(convertView, R.id.name);
        TextView totalPrice = ViewFindUtils.hold(convertView, R.id.totalPrice);
        OrderItemModel item = getItem(position);
        SpannableStringBuilder sb = new SpannableStringBuilder(item.name);
        sb.setSpan(new ForegroundColorSpan(Color.BLACK), 0, sb.length() - 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        sb.setSpan(new AbsoluteSizeSpan(15, true), 0, sb.length() - 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        int start = item.name.length();
        if (StringUtils.isNoneBlank(item.subtitle))
        {
            sb.append("\n").append(item.subtitle);
            sb.setSpan(new ForegroundColorSpan(Color.GRAY), start, sb.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            sb.setSpan(new AbsoluteSizeSpan(13, true), start, sb.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
        name.setText(sb);
        totalPrice.setText(String.format("%.2f", item.price));

        return convertView;
    }
}
