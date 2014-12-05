package com.dlohaiti.dlokiosknew.adapter;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import com.dlohaiti.dlokiosknew.AddProductsToSaleActivity;
import com.dlohaiti.dlokiosknew.R;
import com.dlohaiti.dlokiosknew.domain.Product;
import com.dlohaiti.dlokiosknew.domain.Products;
import com.dlohaiti.dlokiosknew.domain.ShoppingCartNew;

public class ProductArrayAdapter extends ArrayAdapter<Product> {
    private final AddProductsToSaleActivity context;
    private final Products products;
    private ShoppingCartNew cart;

    public ProductArrayAdapter(AddProductsToSaleActivity context, Products products, ShoppingCartNew cart) {
        super(context, R.layout.layout_left_pane_list_item, products);
        this.context = context;
        this.products = products;
        this.cart = cart;
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
        Product productInCart = cart.getProducts().findById(product.getId());
        if (productInCart != null) {
            product = productInCart;
        }
        holder.productId = product.getId();
        holder.icon.setImageBitmap(product.getImageResource());
        holder.description.setText(product.getDescription());
        initialiseAddProductButton(holder);
        initialiseQuantityView(holder, product);

        return view;
    }

    private void initialiseQuantityView(final ProductViewHolder holder, final Product product) {
        holder.quantityView.addTextChangedListener(new QuantityChangedListener(holder.quantityView, holder.addProduct));
        holder.quantityView.setId(Integer.valueOf(holder.productId.toString()));
        holder.quantityView.setText(
                product.getQuantity() == null
                        ? null
                        : String.valueOf(product.getQuantity()));
    }

    private void initialiseAddProductButton(final ProductViewHolder holder) {
        holder.addProduct.setEnabled(products.findById(holder.productId).hasQuantityBeenModified);
        holder.addProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Integer quantity = tryParse(holder.quantityView.getText().toString(), 0);
                Product product = products.findById(holder.productId);
                product.hasQuantityBeenModified = false;
                holder.addProduct.setEnabled(false);
                context.onAddProduct(product, quantity, holder.productId);
            }
        });
    }

    private Integer tryParse(String text, Integer defaultValue) {
        try {
            return Integer.parseInt(text);
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }

    class ProductViewHolder {
        public ImageView icon;
        public TextView description;
        public EditText quantityView;
        public Button addProduct;
        public Long productId;
    }

    private class QuantityChangedListener implements TextWatcher {
        private EditText quantityView;
        private Button addProductView;

        public QuantityChangedListener(EditText quantityView, Button addProduct) {
            this.quantityView = quantityView;
            this.addProductView = addProduct;
        }

        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            Integer newQuantity = tryParse(quantityView.getText().toString(), 0);
            Product product = products.findById((long) quantityView.getId());
            if (product == null) {
                return;
            }
            if (!(newQuantity == 0 || newQuantity.equals(product.getQuantity()))) {
                product.hasQuantityBeenModified = true;
                product.setQuantity(newQuantity);
                addProductView.setEnabled(true);
            }
        }

        @Override
        public void afterTextChanged(Editable editable) {

        }
    }
}