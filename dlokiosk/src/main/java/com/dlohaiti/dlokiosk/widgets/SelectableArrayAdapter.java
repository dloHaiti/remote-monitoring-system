package com.dlohaiti.dlokiosk.widgets;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import com.dlohaiti.dlokiosk.R;

import java.util.List;

public class SelectableArrayAdapter<T extends SelectableListItem> extends ArrayAdapter<T> {
    private final Context context;
    private final List<T> listItems;

    public SelectableArrayAdapter(Context context, List<T> listItems) {
        super(context, R.layout.layout_sales_channel_list_item, listItems);
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
            view = inflater.inflate(R.layout.layout_sales_channel_list_item, parent, false);
            holder.listItem = (TextView) view.findViewById(R.id.list_item);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }

        holder.listItem.setText(listItems.get(position).name());
        if (listItems.get(position).isSelected()) {
            holder.listItem.setBackgroundColor(context.getResources().getColor(android.R.color.holo_blue_light));
        } else {
            holder.listItem.setBackgroundColor(Color.TRANSPARENT);
        }
        return view;
    }

    class ViewHolder {
        TextView listItem;
    }
}