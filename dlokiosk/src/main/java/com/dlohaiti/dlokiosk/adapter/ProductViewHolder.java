package com.dlohaiti.dlokiosk.adapter;

import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import com.dlohaiti.dlokiosk.domain.Product;

public class ProductViewHolder {
    public ImageView icon;
    public TextView description;
    public EditText quantityView;
    public Button addProduct;
    public int quantity;
    public Product product;
}
