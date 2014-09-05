package com.dlohaiti.dlokiosk.adapter;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import com.dlohaiti.dlokiosk.AddProductsToSaleActivity;
import com.dlohaiti.dlokiosk.R;
import com.dlohaiti.dlokiosk.domain.Product;
import com.dlohaiti.dlokiosk.domain.Products;

import java.util.List;

public class ProductArrayAdapter extends ArrayAdapter<Product> {
    private final AddProductsToSaleActivity context;
    private final List<Product> products;

    public ProductArrayAdapter(AddProductsToSaleActivity context, Products products) {
        super(context, R.layout.layout_left_pane_list_item, products);
        this.context = context;
        this.products = products;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        final ProductViewHolder holder;
        if (view == null) {
            holder = new ProductViewHolder();
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.layout_product_list_item, parent, false);
            holder.icon = (ImageView) view.findViewById(R.id.icon);
            holder.description = (TextView) view.findViewById(R.id.description);
            holder.quantityView = (EditText) view.findViewById(R.id.quantity);
            holder.addProduct = (Button) view.findViewById(R.id.add_product);
            view.setTag(holder);
        } else {
            holder = (ProductViewHolder) view.getTag();
        }

        Product product = products.get(position);
        holder.product = product;
        holder.icon.setImageBitmap(product.getImageResource());
        holder.description.setText(product.getDescription());
        initialiseAddProductButton(holder);
        initialiseQuantityView(holder, position);

        return view;
    }

    private void initialiseQuantityView(final ProductViewHolder holder, int position) {
//        holder.quantityView.setId(position);
//        holder.quantityView.setOnFocusChangeListener(new View.OnFocusChangeListener() {
//            @Override
//            public void onFocusChange(View view, boolean hasFocus) {
//                if (!hasFocus) {
//                    final EditText quantityView = (EditText) view;
//                    String newQuantity = quantityView.getText().toString();
//                    holder.quantity = tryParse(newQuantity, 0);
//                }
//            }
//        });
        holder.quantityView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                int newQuantity = tryParse(holder.quantityView.getText().toString(), 0);
                holder.addProduct.setEnabled(newQuantity > 0 && newQuantity != holder.quantity);
            }
        });
    }

    private void initialiseAddProductButton(final ProductViewHolder holder) {
        holder.addProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                holder.quantity = tryParse(holder.quantityView.getText().toString(), 0);
                holder.addProduct.setEnabled(false);
                context.onAddProduct(holder.product, holder.quantity);
            }
        });
    }

    private int tryParse(String text, Integer defaultValue) {
        try {
            return Integer.parseInt(text);
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }
}