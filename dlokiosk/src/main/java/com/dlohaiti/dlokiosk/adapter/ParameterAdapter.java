package com.dlohaiti.dlokiosk.adapter;

import android.content.Context;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.dlohaiti.dlokiosk.R;
import com.dlohaiti.dlokiosk.domain.FlowMeterReading;
import com.dlohaiti.dlokiosk.domain.Parameter;
import com.dlohaiti.dlokiosk.view_holder.LeftPaneListViewHolder;
import com.dlohaiti.dlokiosk.view_holder.WaterMeterToggleHolder;
import com.dlohaiti.dlokiosk.view_holder.WaterQualityViewHolder;

import java.util.List;


public class ParameterAdapter extends ArrayAdapter<Parameter> {
    private static final int EDIT_FORM = 0;
    private static final int RADIO_FORM = 1;
    private final Context context;
    private final List<Parameter> listItems;
    private boolean isDisplayError=false;

    public ParameterAdapter(Context context, List<Parameter> parameters) {
        super(context, R.layout.parameter_row, parameters);
        this.context = context;
        this.listItems = parameters;
    }

    public void setDisplayError(boolean isDisplayError) {
        this.isDisplayError = isDisplayError;
    }

    @Override
    public int getItemViewType(int position) {
        Parameter parameter = listItems.get(position);
        return parameter.isOkNotOk() ?  RADIO_FORM : EDIT_FORM;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        int viewType = this.getItemViewType(position);
        Parameter parameter = listItems.get(position);
        switch (viewType){
            case EDIT_FORM:
                view = generateViewForEditForm(position, view, parent, parameter);
                return view;
            case RADIO_FORM:;
                view = getViewForToggleForm(position, view, parent, parameter);
                return view;
        }
        return view;
    }

    private View getViewForToggleForm(int position, View view, ViewGroup parent, Parameter parameter) {
        WaterMeterToggleHolder meterToggleHolder;
        if (view == null ||!(view.getTag() instanceof WaterMeterToggleHolder)) {
           meterToggleHolder = new WaterMeterToggleHolder();
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.layout_paramater_row_with_radio, parent, false);
            RadioGroup okGroup = (RadioGroup) view.findViewById(R.id.ok_group);
            meterToggleHolder.radioGroup=okGroup;
            meterToggleHolder.id = position;
            view.setTag(meterToggleHolder);
        } else {
            meterToggleHolder = (WaterMeterToggleHolder) view.getTag();
        }
        setTextValues(view,parameter);
        meterToggleHolder.radioGroup.setId(position);
        if(parameter.getValue().equalsIgnoreCase("1")) {
            meterToggleHolder.radioGroup.check(R.id.radio_ok);
        }else if(parameter.getValue().equalsIgnoreCase("0")){
            meterToggleHolder.radioGroup.check(R.id.radio_not_ok);
        }else{
            meterToggleHolder.radioGroup.clearCheck();
        }
        meterToggleHolder.radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                String value;
                if (R.id.radio_ok == checkedId) {
                    value = "1";
                } else if (R.id.radio_not_ok == checkedId) {
                    value ="0";
                } else {
                    value="";
                }
                final int position = group.getId();
                ((Parameter) listItems.get(position)).setValue(value);
            }
        });
        return view;
    }

    private View generateViewForEditForm(int position, View view, ViewGroup parent, Parameter parameter) {
        WaterQualityViewHolder holder;
        if (view == null || !(view.getTag() instanceof WaterQualityViewHolder)) {
            holder = new WaterQualityViewHolder();
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.parameter_row, parent, false);
            final EditText input = (EditText) view.findViewById(R.id.parameter_input);
            holder.listItem = input;
            holder.id = position;
            view.setTag(holder);
        } else {
            holder = (WaterQualityViewHolder) view.getTag();
        }
        if(holder.listItem!=null){
            holder.listItem.setText(parameter.getValue());
        }
        setTextValues(view,parameter);
        holder.listItem.setId(position);
        if(parameter.considersInvalid(holder.listItem.getText().toString()) && !holder.listItem.getText().toString().isEmpty() ){
                holder.listItem.setError("Please enter valid input");
        }else {
            holder.listItem.setError(null);
        }
        holder.listItem.addTextChangedListener(new CustomTextWatcher(holder.listItem));
        return view;
    }

    private void setTextValues(View view, Parameter parameter) {
        TextView label = (TextView) view.findViewById(R.id.parameter_label);
        TextView units = (TextView) view.findViewById(R.id.parameter_units);
        TextView range = (TextView) view.findViewById(R.id.parameter_range);
        label.setText(parameter.getName());
        units.setText(parameter.getUnitOfMeasure());
        if (parameter.hasRange() && !parameter.isOkNotOk()) {
            range.setText(parameter.getRange());
        }
    }

    class CustomTextWatcher implements TextWatcher {

        private final EditText v;

        public CustomTextWatcher(EditText listItem){
            this.v=listItem;
        }
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            final int position = v.getId();
            final EditText Caption = (EditText) v;
            String val = Caption.getText().toString();
            Parameter parameter = (Parameter) listItems.get(position);
            parameter.setValue(val);
            if(!val.isEmpty() && parameter.considersInvalid(val)) {
                Caption.setError("Please enter valid input");
            }else{
                Caption.setError(null);
            }
        }

        @Override
        public void afterTextChanged(Editable editable) {

        }
    }
}
