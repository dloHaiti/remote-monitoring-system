package com.dlohaiti.dlokiosk.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import com.dlohaiti.dlokiosk.R;
import com.dlohaiti.dlokiosk.ShoppingCartActivity;
import com.dlohaiti.dlokiosk.domain.Promotion;
import com.dlohaiti.dlokiosk.domain.Promotions;

public class PromotionGridAdapter extends BaseAdapter {
    private final ShoppingCartActivity activity;
    private final Promotions promotions;

    public PromotionGridAdapter(ShoppingCartActivity activity, Promotions promotions) {
        this.activity = activity;
        this.promotions = promotions;
    }

    public int getCount() {
        return promotions.size();
    }

    public Object getItem(int position) {
        return promotions.get(position);
    }

    public long getItemId(int position) {
        return promotions.get(position).getId();
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        PromotionGridViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(activity).inflate(R.layout.layout_removable_grid_item, parent, false);
            holder = new PromotionGridViewHolder();
            holder.promotionIcon = (ImageView) convertView.findViewById(R.id.icon);
            holder.removePromotionButton = (ImageButton) convertView.findViewById(R.id.remove_item_button);
            holder.quantity = (TextView) convertView.findViewById(R.id.quantity);
            convertView.setTag(holder);
        } else {
            holder = (PromotionGridViewHolder) convertView.getTag();
        }

        final Promotion promotion = (Promotion) getItem(position);
        holder.quantity.setVisibility(View.GONE);
        holder.promotionIcon.setImageBitmap(promotion.getImageResource());
        holder.removePromotionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                activity.onPromotionRemoveButtonClick(promotion);
            }
        });

        return convertView;
    }

    class PromotionGridViewHolder {
        ImageView promotionIcon;
        ImageButton removePromotionButton;
        TextView quantity;
    }
}
