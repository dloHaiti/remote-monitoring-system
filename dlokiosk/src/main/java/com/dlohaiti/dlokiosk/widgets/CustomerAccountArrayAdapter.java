package com.dlohaiti.dlokiosk.widgets;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import com.dlohaiti.dlokiosk.R;
import com.dlohaiti.dlokiosk.domain.CustomerAccount;

import java.util.List;

public class CustomerAccountArrayAdapter extends ArrayAdapter<CustomerAccount> {
    private final Context context;
    private final List<CustomerAccount> accounts;

    public CustomerAccountArrayAdapter(Context context, List<CustomerAccount> accounts) {
        super(context, R.layout.layout_selectable_list_item, accounts);
        this.context = context;
        this.accounts = accounts;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        CustomerAccountViewHolder holder;
        if (view == null) {
            holder = new CustomerAccountViewHolder();
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.layout_selectable_list_item, parent, false);
            holder.listItem = (TextView) view.findViewById(R.id.list_item);
            view.setTag(holder);
        } else {
            holder = (CustomerAccountViewHolder) view.getTag();
        }

        holder.listItem.setText(accounts.get(position).name());
        if (accounts.get(position).isSelected()) {
            holder.listItem.setBackgroundColor(context.getResources().getColor(android.R.color.holo_blue_light));
        } else {
            holder.listItem.setBackgroundColor(Color.TRANSPARENT);
        }
        return view;
    }

    class CustomerAccountViewHolder {
        TextView listItem;
    }

}