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
import org.apache.commons.lang3.StringUtils;

import java.util.List;

public class CustomerAccountArrayAdapter extends ArrayAdapter<CustomerAccount> {
    private final Context context;
    private final List<CustomerAccount> accounts;

    public CustomerAccountArrayAdapter(Context context, List<CustomerAccount> accounts) {
        super(context, R.layout.layout_sales_channel_list_item, accounts);
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
            view = inflater.inflate(R.layout.layout_customer_account_list_item, parent, false);
            holder.customerName = (TextView) view.findViewById(R.id.customer_name);
            holder.contactName = (TextView) view.findViewById(R.id.contact_name);
            view.setTag(holder);
        } else {
            holder = (CustomerAccountViewHolder) view.getTag();
        }

        holder.customerName.setText(accounts.get(position).name());

        if (StringUtils.isNotBlank(accounts.get(position).contactName())) {
            holder.contactName.setText(accounts.get(position).contactName());
            holder.contactName.setVisibility(View.VISIBLE);
        } else {
            holder.contactName.setVisibility(View.INVISIBLE);
        }

        holder.customerName.setBackgroundColor(
                accounts.get(position).isSelected()
                        ? context.getResources().getColor(android.R.color.holo_blue_light)
                        : Color.TRANSPARENT);
        return view;
    }

    class CustomerAccountViewHolder {
        TextView customerName;
        TextView contactName;
    }

}