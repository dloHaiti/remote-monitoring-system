package com.dlohaiti.dlokiosknew.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import com.dlohaiti.dlokiosknew.R;
import com.dlohaiti.dlokiosknew.domain.SalesChannel;
import com.dlohaiti.dlokiosknew.domain.SalesChannels;
import com.dlohaiti.dlokiosknew.view_holder.LeftPaneListViewHolder;

import java.util.List;

public class SalesChannelArrayAdapter extends ArrayAdapter<SalesChannel> {
    private final Context context;
    private final List<SalesChannel> listItems;

    public SalesChannelArrayAdapter(Context context, SalesChannels listItems) {
        super(context, R.layout.layout_left_pane_list_item, listItems);
        this.context = context;
        this.listItems = listItems;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        LeftPaneListViewHolder holder;
        if (view == null) {
            holder = new LeftPaneListViewHolder();
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.layout_left_pane_list_item, parent, false);
            holder.listItem = (TextView) view.findViewById(R.id.list_item);
            view.setTag(holder);
        } else {
            holder = (LeftPaneListViewHolder) view.getTag();
        }

        SalesChannel salesChannel = listItems.get(position);
        holder.listItem.setText(salesChannel.name());
        holder.id = salesChannel.getId();
        if (salesChannel.isSelected()) {
            holder.listItem.setTextAppearance(getContext(), R.style.selected_left_pane_list_item);
            holder.listItem.setBackgroundColor(context.getResources().getColor(R.color.selected_list_item_background));
        } else {
            holder.listItem.setTextAppearance(getContext(), R.style.list_item);
            holder.listItem.setBackgroundColor(Color.TRANSPARENT);
        }
        return view;
    }

}