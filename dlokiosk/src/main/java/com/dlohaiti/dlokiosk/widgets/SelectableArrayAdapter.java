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
        ViewHolder holder;
        if (view == null) {
            holder = new ViewHolder();
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.layout_selectable_list_item, parent, false);
            holder.listItem = (TextView) view.findViewById(R.id.list_item);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }

        holder.listItem.setText(listItems[position].name());
        if (listItems[position].isSelected()) {
            holder
                    .listItem
                    .setBackgroundColor(Color.
                            context.getResources().getColor(android.R.color.holo_blue_light));
        } else {
            holder
                    .listItem
                    .setBackgroundColor(Color.TRANSPARENT);
        }
        return view;
    }

    class ViewHolder {
        TextView listItem;
    }
}

