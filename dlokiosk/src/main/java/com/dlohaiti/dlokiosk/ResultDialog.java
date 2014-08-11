package com.dlohaiti.dlokiosk;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;

public class ResultDialog extends DialogFragment {

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        boolean isSuccessful = this.getArguments().getBoolean("isSuccessful");
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        if (isSuccessful) {
            builder.setMessage(R.string.data_save_successful);
        } else {
            builder.setMessage(R.string.data_save_error);
        }
        builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                getActivity().finish();
            }
        });
        return builder.create();
    }
}
