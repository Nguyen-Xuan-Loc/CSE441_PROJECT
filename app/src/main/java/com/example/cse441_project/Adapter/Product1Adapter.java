package com.example.cse441_project.Adapter;

import android.content.Context;
import android.content.Intent;
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

import com.example.cse441_project.Activity.ProductDetailActivity;
import com.example.cse441_project.Model.Product1;
import com.example.cse441_project.R;

import java.io.IOException;
import java.net.URL;
import java.util.List;

public class Product1Adapter extends RecyclerView.Adapter<Product1Adapter.ProductViewHolder> {
    private Context context;
    private List<Product1> productList1;

    public Product1Adapter(Context context, List<Product1> productList1) {
        this.context = context;
        this.productList1 = productList1;
    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.product_item_2, parent, false);
        return new ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {
        Product1 productList = this.productList1.get(position);
        holder.textViewName.setText(productList.getName());
        holder.textViewPrice.setText(String.valueOf(productList.getPrice()));
        loadImageFromUrl(holder.imageView, productList.getImageUrl());

        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, ProductDetailActivity.class);
            intent.putExtra("productName", productList.getName());
            intent.putExtra("productPrice", productList.getPrice());
            intent.putExtra("productImageUrl", productList.getImageUrl());
            intent.putExtra("productDescribe", productList.getDescription());
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return productList1.size();
    }

    public static class ProductViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView textViewName, textViewPrice, textViewDescription;

        public ProductViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.prdDetail_image);
            textViewName = itemView.findViewById(R.id.productName);
            textViewPrice = itemView.findViewById(R.id.productPrice);
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
