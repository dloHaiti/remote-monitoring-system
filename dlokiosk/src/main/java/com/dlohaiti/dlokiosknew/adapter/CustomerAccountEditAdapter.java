package com.dlohaiti.dlokiosknew.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import com.dlohaiti.dlokiosknew.CustomerAccountsActivity;
import com.dlohaiti.dlokiosknew.BalanceAmountDialog;
import com.dlohaiti.dlokiosknew.CustomerFormActivity;
import com.dlohaiti.dlokiosknew.R;
import com.dlohaiti.dlokiosknew.domain.CustomerAccount;
import com.dlohaiti.dlokiosknew.domain.CustomerAccounts;

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
        if(customerAccount.getDueAmount()==0){
            holder.balanceButton.setVisibility(View.INVISIBLE);
        }else{
            holder.balanceButton.setVisibility(View.VISIBLE);
        }
        holder.balanceButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CustomerAccount account = accounts.get(view.getId());
                BalanceAmountDialog balanceAmountDialog = new BalanceAmountDialog(account);
                balanceAmountDialog.show(((CustomerAccountsActivity)context).getFragmentManager(),"balanceAmountDialog");
            }
        });
    }

    private void handleCustomerEdit(int position, View view, CustomerAccountViewHolder holder) {
//        holder.editButton = (ImageButton) view.findViewById(R.id.imageButton);
//        if (holder.editButton==null) return;
        holder.editButton.setId(position);
        holder.editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CustomerAccount account = accounts.get(view.getId());
                Intent intent = new Intent(context, CustomerFormActivity.class);
                intent.putExtra("account_id",String.valueOf(account.getId()));
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                view.getContext().startActivity(intent);
            }
        });
    }

    private void fillCustomerDetails(View view, CustomerAccountViewHolder holder, CustomerAccount customerAccount) {

        holder.customerName.setText(customerAccount.getContactName());

        TextView phone = (TextView) view.findViewById(R.id.customer_phone);
        phone.setText(customerAccount.getPhoneNumber());

        TextView organization = (TextView) view.findViewById(R.id.customer_organization);
        organization.setText(customerAccount.getName());

        TextView balance = (TextView) view.findViewById(R.id.customer_balance);
        balance.setText( String.format( "%.2f",customerAccount.getDueAmount()));

    }

    class CustomerAccountViewHolder {
        ImageButton editButton;
        View listItem;
        TextView customerName;
        ImageButton balanceButton;

    }

}
