package com.dlohaiti.dlokiosk;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import com.dlohaiti.dlokiosk.domain.CustomerAccount;

public class BalanceAmountDialog extends DialogFragment {
    private final CustomerAccount account;

    public BalanceAmountDialog(CustomerAccount account) {
        super();
        this.account=account;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        LayoutInflater inflater = getActivity().getLayoutInflater();
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        final View view = inflater.inflate(R.layout.layout_balance_amount_dialog, null);
        final EditText balanceAmount = (EditText) view.findViewById(R.id.balance_amount);
        balanceAmount.setText( String.format( "%.2f",account.getDueAmount()));
        balanceAmount.requestFocus();
        builder.setTitle(R.string.balance_amount_label);
        builder.setView(view);

        builder.setPositiveButton(R.string.make_payment, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                Double amount = Double.valueOf(balanceAmount.getText().toString());
                if(amount>account.getDueAmount()){
                    balanceAmount.setError("Enter valid amount");
                }
                ((CustomerAccountsActivity) getActivity()).addBalanceAmount(account,amount);
            }
        });
        builder.setNegativeButton(R.string.dialog_cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        final AlertDialog dialog = builder.create();
        balanceAmount.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                String text = balanceAmount.getText().toString();
                if (TextUtils.isEmpty(text) || Double.valueOf(text) == 0 || Double.valueOf(text) > account.getDueAmount() ) {
                    ((AlertDialog)dialog ).getButton(
                            AlertDialog.BUTTON_POSITIVE).setEnabled(false);
                } else {
                    ((AlertDialog) dialog).getButton(
                            AlertDialog.BUTTON_POSITIVE).setEnabled(true);
                }

            }
        });
        return dialog;
    }
}