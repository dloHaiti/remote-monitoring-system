package com.dlohaiti.dlokiosk.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import com.dlohaiti.dlokiosk.R;
import com.dlohaiti.dlokiosk.domain.Promotion;

import java.util.List;

public class PromotionGridAdapter extends BaseAdapter {
    private final Context context;
    private final List<Promotion> items;

    public PromotionGridAdapter(Context c, List<Promotion> promotions) {
        this.context = c;
        this.items = promotions;
    }

    public int getCount() {
        return items.size();
    }

    public Object getItem(int position) {
        return items.get(position);
    }

    public long getItemId(int position) {
        return items.get(position).getId();
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        PromotionGridViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.layout_promotion_grid_item, parent, false);
            holder = new PromotionGridViewHolder();
            holder.promotionIcon = (ImageView) convertView.findViewById(R.id.promotion_icon);
            convertView.setTag(holder);
        } else {
            holder = (PromotionGridViewHolder) convertView.getTag();
        }

        Promotion promotion = (Promotion) getItem(position);
        holder.promotionIcon.setImageBitmap(promotion.getImageResource());

        return convertView;
    }

    class PromotionGridViewHolder {
        ImageView promotionIcon;
    }
}
