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

import com.example.cse441_project.R;
import com.example.cse441_project.Model.ProductList;

import java.io.IOException;
import java.net.URL;
import java.util.List;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductViewHolder> {
    private List<ProductList> productList;
    private Context context;

    public ProductAdapter(Context context, List<ProductList> productList) {
        this.context = context;
        this.productList = productList;
    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_product, parent, false);
        return new ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {
        ProductList productList = this.productList.get(position);
        holder.textViewName.setText(productList.getName());
        holder.textViewPrice.setText(String.valueOf(productList.getPrice()));
        holder.textViewDescription.setText(productList.getDescription());
        loadImageFromUrl(holder.imageView, productList.getImageUrl());
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    public static class ProductViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView textViewName, textViewPrice, textViewDescription;

        public ProductViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.img_phone);
            textViewName = itemView.findViewById(R.id.txt_product_name);
            textViewPrice = itemView.findViewById(R.id.txt_product_price);
            textViewDescription = itemView.findViewById(R.id.txt_product_description);
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




