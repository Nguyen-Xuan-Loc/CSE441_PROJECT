package com.example.cse441_project.Adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cse441_project.Model.Product1;
import com.example.cse441_project.R;

import java.io.IOException;
import java.net.URL;
import java.util.List;

public class Product2Adapter extends RecyclerView.Adapter<Product2Adapter.ProductViewHolder> {
    private Context context;
    private List<Product1> productList1;
    private List<Product1> filteredProductList ;

    public Product2Adapter(Context context, List<Product1> productList1,List<Product1> filteredProductList) {
        this.context = context;
        this.productList1 = productList1;
        this.filteredProductList = filteredProductList;
    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.product_item, parent, false);
        return new ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {
        Product1 product = filteredProductList.isEmpty() ? productList1.get(position) : filteredProductList.get(position);
        holder.textViewName.setText(product.getName());
        holder.textViewPrice.setText(String.valueOf(product.getPrice()));
        loadImageFromUrl(holder.imageView, product.getImageUrl());
    }

    @Override
    public int getItemCount() {
        return filteredProductList.isEmpty() ? productList1.size() : filteredProductList.size();
    }

    public static class ProductViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView textViewName, textViewPrice, textViewDescription;

        public ProductViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.prdDetail_image);
            textViewName = itemView.findViewById(R.id.product_name);
            textViewPrice = itemView.findViewById(R.id.product_price);
        }
    }

    private void loadImageFromUrl(ImageView imageView, String url) {
        Log.d("ProductAdapter", "Loading image from URL: " + url);
        new Thread(() -> {
            try {
                URL imageUrl = new URL(url);
                Bitmap bitmap = BitmapFactory.decodeStream(imageUrl.openConnection().getInputStream());
                imageView.post(() -> imageView.setImageBitmap(bitmap));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();
    }

}
