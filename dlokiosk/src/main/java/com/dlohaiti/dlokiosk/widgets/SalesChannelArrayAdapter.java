package com.dlohaiti.dlokiosk.widgets;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import com.dlohaiti.dlokiosk.R;
import com.dlohaiti.dlokiosk.domain.SalesChannel;
import com.dlohaiti.dlokiosk.domain.SalesChannels;

import java.util.List;

public class SalesChannelArrayAdapter extends ArrayAdapter<SalesChannel> {
    private final Context context;
    private final List<SalesChannel> listItems;

    public SalesChannelArrayAdapter(Context context, SalesChannels listItems) {
        super(context, R.layout.layout_sales_channel_list_item, listItems);
        this.context = context;
        this.listItems = listItems;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        SalesChannelViewHolder holder;
        if (view == null) {
            holder = new SalesChannelViewHolder();
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.layout_sales_channel_list_item, parent, false);
            holder.listItem = (TextView) view.findViewById(R.id.list_item);
            view.setTag(holder);
        } else {
            holder = (SalesChannelViewHolder) view.getTag();
        }

        SalesChannel salesChannel = listItems.get(position);
        holder.listItem.setText(salesChannel.name());
        holder.id = salesChannel.id();
        if (salesChannel.isSelected()) {
            holder.listItem.setTextAppearance(getContext(), R.style.selected_sales_channel_list_item);
            holder.listItem.setBackgroundColor(context.getResources().getColor(R.color.selected_sales_channel_background));
        } else {
            holder.listItem.setTextAppearance(getContext(), R.style.list_item);
            holder.listItem.setBackgroundColor(Color.TRANSPARENT);
        }
        return view;
    }

}