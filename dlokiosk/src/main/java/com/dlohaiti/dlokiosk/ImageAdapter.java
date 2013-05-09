package com.dlohaiti.dlokiosk;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import com.dlohaiti.dlokiosk.domain.Product;

import java.util.List;

public class ImageAdapter extends BaseAdapter {
    private final Context mContext;
    private final List<Product> products;

    public ImageAdapter(Context c, List<Product> products) {
        this.mContext = c;
        this.products = products;
    }

    public int getCount() {
        return products.size();
    }

    public Object getItem(int position) {
        return products.get(position);
    }

    public long getItemId(int position) {
        return products.get(position).getId();
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView imageView;
        if (convertView == null) {
            imageView = new ImageView(mContext);
            imageView.setLayoutParams(new GridView.LayoutParams(144, 144));
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        } else {
            imageView = (ImageView) convertView;
        }

        imageView.setImageResource(R.drawable.green_checkmark);
        return imageView;
    }
}
