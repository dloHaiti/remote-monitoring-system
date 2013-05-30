package com.dlohaiti.dlokiosk;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.TextView;
import com.dlohaiti.dlokiosk.domain.Parameter;

import java.util.List;

public class ParameterAdapter extends ArrayAdapter<Parameter> {

    private final Context context;
    private final List<Parameter> parameters;

    public ParameterAdapter(Context context, int textViewResourceId, List<Parameter> parameters) {
        super(context, textViewResourceId, parameters);
        this.context = context;
        this.parameters = parameters;
    }

    @Override public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if(view == null) {
            LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.parameter_row, null);
        }
        Parameter parameter = parameters.get(position);
        if(parameter != null) {
            TextView label = (TextView)view.findViewById(R.id.parameter_label);
            EditText value = (EditText)view.findViewById(R.id.parameter_input);
            TextView units = (TextView)view.findViewById(R.id.parameter_units);
            TextView range = (TextView)view.findViewById(R.id.parameter_range);

            label.setText(parameter.getName());
            units.setText(parameter.getUnitOfMeasure());
            if(parameter.hasRange()) {
                range.setText(parameter.getRange());
            }
        }
        return view;
    }
}
