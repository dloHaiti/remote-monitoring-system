package com.dlohaiti.dlokiosk.adapter;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.TextView;

import com.dlohaiti.dlokiosk.R;
import com.dlohaiti.dlokiosk.domain.FlowMeterReading;
import com.dlohaiti.dlokiosk.domain.FlowMeterReadings;
import com.dlohaiti.dlokiosk.view_holder.FlowMeterViewHolder;

import java.util.List;

public class FlowMeterAdapter extends ArrayAdapter<FlowMeterReading> {

    private final Context context;
    private final List<FlowMeterReading> listItems;
    private boolean isDisplayError;

    public FlowMeterAdapter(Context context, FlowMeterReadings listItems) {
        super(context, R.layout.parameter_row, listItems);
        this.context = context;
        this.listItems = listItems;
        this.isDisplayError = false;
    }

    public void setDisplayError(boolean isDisplayError) {
        this.isDisplayError = isDisplayError;
    }

    public void cleanQuantity() {
        for (FlowMeterReading r : listItems) {
            r.setQuantity("");
        }
        notifyDataSetChanged();
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final FlowMeterViewHolder holder;
        final FlowMeterReading flowMeterReading = listItems.get(position);

        if (convertView == null) {
            holder = new FlowMeterViewHolder();
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.flowmeter_row, parent, false);

            holder.listItem = (EditText) convertView.findViewById(R.id.flow_gallons);
//            editText.setHint("Enter Gallons");
            convertView.setTag(holder);
        } else {
            holder = (FlowMeterViewHolder) convertView.getTag();
        }
        if(holder.listener!=null) {
            holder.listItem.removeTextChangedListener(holder.listener);
        }
        holder.listItem.setId(position);
        holder.id = flowMeterReading.getId();
        holder.listItem.setText(flowMeterReading.getQuantity());

        TextView label = (TextView) convertView.findViewById(R.id.flow_channel);
        if (isDisplayError && String.valueOf(holder.listItem.getText()).isEmpty()) {
            holder.listItem.setError("Can't be blank");
        } else {
            holder.listItem.setError(null);
        }
        holder.listener=new CustomTextWatcher(holder.listItem);
        holder.listItem.addTextChangedListener(holder.listener);

        label.setText(flowMeterReading.getSamplingName());
        return convertView;
    }

    class CustomTextWatcher implements TextWatcher {

        private final EditText view;

        public CustomTextWatcher(EditText view) {
            this.view = view;
        }

        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            String val = view.getText().toString();
            listItems.get(view.getId()).setQuantity(val);
        }

        @Override
        public void afterTextChanged(Editable editable) {

        }
    }
}

