package com.dlohaiti.dlokiosk.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.dlohaiti.dlokiosk.R;
import com.dlohaiti.dlokiosk.domain.CustomerAccount;
import com.dlohaiti.dlokiosk.domain.CustomerAccounts;

import org.apache.commons.lang3.StringUtils;

import java.util.List;

public class CustomerAccountEditAdapter extends ArrayAdapter<CustomerAccount> {
    private final Context context;
    private final List<CustomerAccount> accounts;

    public CustomerAccountEditAdapter(Context context, CustomerAccounts accounts) {
        super(context, R.layout.layout_left_pane_list_item, accounts);
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
            view = inflater.inflate(R.layout.layout_customer_account_edit_item, parent, false);
            holder.listItem = view.findViewById(R.id.customer_list_item);
            holder.customerName = (TextView) view.findViewById(R.id.customer_name);
            holder.contactName = (TextView) view.findViewById(R.id.contact_name);
            view.setTag(holder);
        } else {
            holder = (CustomerAccountViewHolder) view.getTag();
        }

        CustomerAccount customerAccount = accounts.get(position);

        holder.customerName.setText(customerAccount.name());

        if (StringUtils.isNotBlank(customerAccount.contactName())) {
            holder.contactName.setText(customerAccount.contactName());
            holder.contactName.setVisibility(View.VISIBLE);
        } else {
            holder.contactName.setVisibility(View.INVISIBLE);
        }

        TextView phone = (TextView) view.findViewById(R.id.customer_phone);
        phone.setText(customerAccount.phoneNumber());

        TextView address = (TextView) view.findViewById(R.id.customer_address);
        address.setText(customerAccount.address());

        if (customerAccount.isSelected()) {
            holder.customerName.setTextAppearance(getContext(), R.style.selected_customer_account_list_item);
            holder.contactName.setTextAppearance(getContext(), R.style.selected_customer_account_list_item);
            holder.listItem.setBackgroundColor(context.getResources().getColor(R.color.selected_list_item_background));
        } else {
            holder.customerName.setTextAppearance(getContext(), R.style.list_item);
            holder.contactName.setTextAppearance(getContext(), R.style.list_item);

            holder.listItem.setBackgroundColor(Color.TRANSPARENT);
        }
        return view;
    }

    class CustomerAccountViewHolder {
        View listItem;
        TextView customerName;
        TextView contactName;
    }

}
