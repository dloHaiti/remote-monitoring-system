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
import com.dlohaiti.dlokiosk.domain.Product;
import com.dlohaiti.dlokiosk.domain.ProductCategories;
import com.tonicartos.widget.stickygridheaders.StickyGridHeadersSimpleAdapter;

import java.util.List;

public class ProductGridAdapter extends BaseAdapter implements StickyGridHeadersSimpleAdapter {

    private ShoppingCartActivity activity;
    private final List<Product> products;
    private final ProductCategories productCategories;
    private final int headerResId;
    private final int itemResId;
    private final LayoutInflater inflater;

    public ProductGridAdapter(ShoppingCartActivity activity, List<Product> products, ProductCategories productCategories,
                              int headerResId, int itemResId) {
        this.activity = activity;
        this.products = products;
        this.productCategories = productCategories;
        this.headerResId = headerResId;
        this.itemResId = itemResId;
        inflater = LayoutInflater.from(activity);

    }

    @Override
    public long getHeaderId(int position) {
        return ((Product) getItem(position)).getCategoryId();
    }

    @Override
    public View getHeaderView(int position, View convertView, ViewGroup parent) {
        HeaderViewHolder holder;
        if (convertView == null) {
            convertView = inflater.inflate(headerResId, parent, false);
            holder = new HeaderViewHolder();
            holder.textView = (TextView) convertView.findViewById(android.R.id.text1);
            convertView.setTag(holder);
        } else {
            holder = (HeaderViewHolder) convertView.getTag();
        }

        Product item = (Product) getItem(position);
        // set header text as first char in string

        holder.textView.setText(productCategories.findProductCategoryById(item.getCategoryId()).name());

        return convertView;
    }

    @Override
    public int getCount() {
        return products.size();
    }

    @Override
    public Object getItem(int i) {
        return products.get(i);
    }

    @Override
    public long getItemId(int i) {
        return products.get(i).getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        if (convertView == null) {
            convertView = inflater.inflate(itemResId, parent, false);
            holder = new ViewHolder();
            holder.productIcon = (ImageView) convertView.findViewById(R.id.icon);
            holder.removeProductButton = (ImageButton) convertView.findViewById(R.id.remove_item_button);
            holder.quantity = (TextView) convertView.findViewById(R.id.quantity);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        final Product product = (Product) getItem(position);
        holder.productIcon.setImageBitmap(product.getImageResource());
        holder.quantity.setText(String.valueOf(product.getQuantity()));
        holder.removeProductButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                activity.onProductRemoveButtonClick(product);
            }
        });
        return convertView;
    }

    protected class HeaderViewHolder {
        public TextView textView;
    }

    protected class ViewHolder {
        public ImageView productIcon;
        public ImageButton removeProductButton;
        public TextView quantity;
    }
}