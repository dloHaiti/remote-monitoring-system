package com.dlohaiti.dlokiosk;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.NumberPicker;
import com.dlohaiti.dlokiosk.domain.Product;

public class NumberPickerDialog extends DialogFragment {
    private final Product product;

    public NumberPickerDialog(Product product) {
        super();
        this.product = product;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        LayoutInflater inflater = getActivity().getLayoutInflater();
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        final View view = inflater.inflate(R.layout.number_picker_dialog, null);
        final NumberPicker numberPicker = (NumberPicker) view.findViewById(R.id.quantity_picker);
        numberPicker.setMinValue(product.getMinimumQuantity());
        numberPicker.setValue(product.getMinimumQuantity());
        numberPicker.setMaxValue(product.getMaximumQuantity());
        numberPicker.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);
        builder.setView(view);
        builder.setPositiveButton(R.string.add_to_cart, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                int quantity = numberPicker.getValue();
                ((EnterSaleActivity) getActivity()).addToShoppingCart(product.withQuantity(quantity));
            }
        });
        builder.setNegativeButton(R.string.dialog_cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        return builder.create();
    }
}
