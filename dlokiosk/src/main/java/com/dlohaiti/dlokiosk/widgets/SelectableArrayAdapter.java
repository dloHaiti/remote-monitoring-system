package com.dlohaiti.dlokiosk.widgets;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import com.dlohaiti.dlokiosk.R;

public class SelectableArrayAdapter extends ArrayAdapter<SelectableListItem> {
    private final Context context;
    private final SelectableListItem[] listItems;

    public SelectableArrayAdapter(Context context, SelectableListItem[] listItems) {
        super(context, R.layout.layout_selectable_list_item, listItems);
        this.context = context;
        this.listItems = listItems;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View rowView = inflater.inflate(R.layout.layout_selectable_list_item, parent, false);
        TextView textView = (TextView) rowView.findViewById(R.id.list_item);
        textView.setText(listItems[position].name());
        if (listItems[position].isSelected()) {
            rowView.setBackgroundColor(context.getResources().getColor(android.R.color.holo_blue_light));
        } else {
            rowView.setBackgroundColor(Color.TRANSPARENT);
        }
        return rowView;
    }
}

