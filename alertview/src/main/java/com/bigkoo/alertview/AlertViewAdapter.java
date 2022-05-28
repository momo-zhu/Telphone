package com.bigkoo.alertview;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Sai on 15/8/9.
 */
public class AlertViewAdapter extends BaseAdapter {
    private List<String> others;
    private List<String> mDestructive;

    public AlertViewAdapter(List<String> others, List<String> destructive) {
        this.others = others;
        this.mDestructive = destructive;
    }

    @Override
    public int getCount() {
        return others.size() + mDestructive.size();
    }

    @Override
    public Object getItem(int position) {
        if (position < others.size())
            return others.get(position);
        else return mDestructive.get(position - others.size());
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        String data = (String) getItem(position);
        Holder holder;
        View view = convertView;
        if (view == null) {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            view = inflater.inflate(R.layout.item_alertbutton, null);
            holder = creatHolder(view);
            view.setTag(holder);
        } else {
            holder = (Holder) view.getTag();
        }
        holder.UpdateUI(parent.getContext(), data, position);
        return view;
    }

    public Holder creatHolder(View view) {
        return new Holder(view);
    }

    class Holder {
        private TextView tvAlert;

        public Holder(View view) {
            tvAlert = (TextView) view.findViewById(R.id.tvAlert);
        }

        public void UpdateUI(Context context, String data, int position) {
            tvAlert.setText(data);
            if (position >= others.size()) {
                tvAlert.setTextColor(context.getResources().getColor(R.color.textColor_alert_button_destructive));
            } else {
                tvAlert.setTextColor(context.getResources().getColor(R.color.textColor_alert_button_others));
            }
        }
    }
}