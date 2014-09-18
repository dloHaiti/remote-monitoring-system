package com.dlohaiti.dlokiosk.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import com.dlohaiti.dlokiosk.AccountsActivity;
import com.dlohaiti.dlokiosk.BalanceAmountDialog;
import com.dlohaiti.dlokiosk.CustomerFormActivity;
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
            holder.editButton = (ImageButton) view.findViewById(R.id.imageButton);
            holder.balanceButton = (ImageButton) view.findViewById(R.id.balanceButton);
            view.setTag(holder);
        } else {
            holder = (CustomerAccountViewHolder) view.getTag();
        }
        CustomerAccount customerAccount = accounts.get(position);
        fillCustomerDetails(view, holder, customerAccount);
        handleCustomerEdit(position, view, holder);
        handleBalanceAmount(position, holder, customerAccount);
        return view;
    }

    private void handleBalanceAmount(int position, CustomerAccountViewHolder holder, CustomerAccount customerAccount) {
        holder.balanceButton.setId(position);
        if(customerAccount.dueAmount()==0){
            holder.balanceButton.setVisibility(View.INVISIBLE);
        }else{
            holder.balanceButton.setVisibility(View.VISIBLE);
        }
        holder.balanceButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CustomerAccount account = accounts.get(view.getId());
                BalanceAmountDialog balanceAmountDialog = new BalanceAmountDialog(account);
                balanceAmountDialog.show(((AccountsActivity)context).getFragmentManager(),"balanceAmountDialog");
            }
        });
    }

    private void handleCustomerEdit(int position, View view, CustomerAccountViewHolder holder) {
        holder.editButton = (ImageButton) view.findViewById(R.id.imageButton);
        holder.editButton.setId(position);
        holder.editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CustomerAccount account = accounts.get(view.getId());
                Intent intent = new Intent(context, CustomerFormActivity.class);
                intent.putExtra("account_id",String.valueOf(account.id()));
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                view.getContext().startActivity(intent);
            }
        });
    }

    private void fillCustomerDetails(View view, CustomerAccountViewHolder holder, CustomerAccount customerAccount) {
        if (StringUtils.isNotBlank(customerAccount.contactName())) {
            holder.customerName.setText(customerAccount.contactName());
        } else {
            holder.customerName.setText(customerAccount.name());
        }

        TextView phone = (TextView) view.findViewById(R.id.customer_phone);
        phone.setText(customerAccount.phoneNumber());

        TextView address = (TextView) view.findViewById(R.id.customer_address);
        address.setText(customerAccount.address());

        TextView balance = (TextView) view.findViewById(R.id.customer_balance);
        balance.setText( String.format( "%.2f",customerAccount.dueAmount()));

    }

    class CustomerAccountViewHolder {
        ImageButton editButton;
        View listItem;
        TextView customerName;
        ImageButton balanceButton;

    }

}
