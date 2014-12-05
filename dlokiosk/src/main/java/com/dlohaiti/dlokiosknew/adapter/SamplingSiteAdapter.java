package com.dlohaiti.dlokiosknew.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.dlohaiti.dlokiosknew.R;
import com.dlohaiti.dlokiosknew.domain.SamplingSite;
import com.dlohaiti.dlokiosknew.domain.SamplingSites;
import com.dlohaiti.dlokiosknew.view_holder.LeftPaneListViewHolder;

import java.util.List;

public class SamplingSiteAdapter extends ArrayAdapter<SamplingSite> {
    private final Context context;
    private final List<SamplingSite> listItems;

    public SamplingSiteAdapter(Context context, SamplingSites samplingSites) {
        super(context, R.layout.layout_left_pane_list_item,samplingSites);
        this.context = context;
        this.listItems = samplingSites;
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
        SamplingSite samplingSite=listItems.get(position);
        holder.listItem.setText(samplingSite.getName());
        holder.id = samplingSite.getId();
        if (samplingSite.isSelected()) {
            holder.listItem.setTextAppearance(getContext(), R.style.selected_left_pane_list_item);
            holder.listItem.setBackgroundColor(context.getResources().getColor(R.color.selected_list_item_background));
        } else {
            holder.listItem.setTextAppearance(getContext(), R.style.list_item);
            holder.listItem.setBackgroundColor(Color.TRANSPARENT);
        }
        return view;
    }
}
