package com.dlohaiti.dlokiosk.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.dlohaiti.dlokiosk.R;
import com.dlohaiti.dlokiosk.domain.Product;
import com.dlohaiti.dlokiosk.domain.ProductCategories;
import com.tonicartos.widget.stickygridheaders.StickyGridHeadersSimpleAdapter;

import java.util.List;

public class ProductGridAdapter extends BaseAdapter implements StickyGridHeadersSimpleAdapter {

    private final List<Product> products;
    private final ProductCategories productCategories;
    private final int headerResId;
    private final int itemResId;
    private final LayoutInflater inflater;

    public ProductGridAdapter(Context context, List<Product> products, ProductCategories productCategories, int headerResId, int itemResId) {
        this.products = products;
        this.productCategories = productCategories;
        this.headerResId = headerResId;
        this.itemResId = itemResId;
        inflater = LayoutInflater.from(context);

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
        ViewHolder holder;
        if (convertView == null) {
            convertView = inflater.inflate(itemResId, parent, false);
            holder = new ViewHolder();
            holder.productIcon = (ImageView) convertView.findViewById(R.id.product_icon);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        Product product = (Product) getItem(position);
        holder.productIcon.setImageBitmap(product.getImageResource());

        return convertView;
    }

    protected class HeaderViewHolder {
        public TextView textView;
    }

    protected class ViewHolder {
        public ImageView productIcon;
    }
}