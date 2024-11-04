package com.example.cse441_project.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.cse441_project.GiohangActivity;
import com.example.cse441_project.Model.Giohang;
import com.example.cse441_project.R;
import com.example.cse441_project.Model.Product2;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class GiohangAdapter extends ArrayAdapter<Giohang> {
    private Context context;
    private ArrayList<Giohang> giohangList;
    private ArrayList<Product2> productList; // Danh sách sản phẩm

    public GiohangAdapter(Context context, ArrayList<Giohang> giohangList, ArrayList<Product2> productList) {
        super(context, 0, giohangList);
        this.context = context;
        this.giohangList = giohangList;
        this.productList = productList; // Nhận danh sách sản phẩm
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.dong_giohang, parent, false);
        }

        Giohang currentItem = giohangList.get(position);

        ImageView imgProduct = convertView.findViewById(R.id.img_product);
        TextView txtProductName = convertView.findViewById(R.id.txt_product_name);
        TextView txtQuantity = convertView.findViewById(R.id.txt_quantity);
        TextView txtPrice = convertView.findViewById(R.id.txt_price);
        Button btnDecrease = convertView.findViewById(R.id.btn_decrease);
        Button btnIncrease = convertView.findViewById(R.id.btn_increase);

        // Lấy thông tin sản phẩm từ danh sách sản phẩm
        Product2 product = getProductById(currentItem.getProductId());

        if (product != null) {
            txtProductName.setText(product.getName());
            txtPrice.setText(String.format("Giá: %.2f VNĐ", product.getPrice())); // Hiển thị giá từ thông tin sản phẩm
            txtQuantity.setText(String.valueOf(currentItem.getQuantity()));
            Picasso.get().load(product.getImageUrl()).into(imgProduct);
        }

        // Các sự kiện cho nút tăng giảm
        btnDecrease.setOnClickListener(v -> {
            int quantity = currentItem.getQuantity();
            if (quantity > 1) {
                quantity--;
                currentItem.setQuantity(quantity);
                txtQuantity.setText(String.valueOf(quantity));
            } else {
                giohangList.remove(position);
                notifyDataSetChanged();
            }
            notifyDataSetChanged();
            ((GiohangActivity) context).updateTotalPrice();
        });

        btnIncrease.setOnClickListener(v -> {
            currentItem.setQuantity(currentItem.getQuantity() + 1);
            txtQuantity.setText(String.valueOf(currentItem.getQuantity()));
            notifyDataSetChanged();
            ((GiohangActivity) context).updateTotalPrice();
        });

        return convertView;
    }

    private Product2 getProductById(long productId) {
        for (Product2 product : productList) {
            if (product.getProductId() == productId) { // Sử dụng toán tử == để so sánh
                return product;
            }
        }
        return null; // Nếu không tìm thấy sản phẩm
    }


}
