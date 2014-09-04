package com.dlohaiti.dlokiosk.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.dlohaiti.dlokiosk.R;
import com.dlohaiti.dlokiosk.domain.Product;
import com.dlohaiti.dlokiosk.domain.Products;

import java.util.List;

public class ProductArrayAdapter extends ArrayAdapter<Product> {
    private final Context context;
    private final List<Product> products;

    public ProductArrayAdapter(Context context, Products products) {
        super(context, R.layout.layout_left_pane_list_item, products);
        this.context = context;
        this.products = products;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        ProductViewHolder holder;
        if (view == null) {
            holder = new ProductViewHolder();
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.layout_product_list_item, parent, false);
            holder.icon = (ImageView) view.findViewById(R.id.icon);
            holder.description = (TextView) view.findViewById(R.id.description);
            view.setTag(holder);
        } else {
            holder = (ProductViewHolder) view.getTag();
        }

        holder.icon.setImageBitmap(products.get(position).getImageResource());
        holder.description.setText(products.get(position).getDescription());
        return view;
    }

    class ProductViewHolder {
        public ImageView icon;
        public TextView description;
    }
}