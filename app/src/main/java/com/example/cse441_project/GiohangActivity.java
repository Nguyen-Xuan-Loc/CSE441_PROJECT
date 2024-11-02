package com.example.cse441_project;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.cse441_project.adapter.GiohangAdapter;
import com.example.cse441_project.model.Giohang;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class GiohangActivity extends AppCompatActivity {
    private ListView listViewGiohang;
    private TextView txtTongtien, txtThongBao;
    private GiohangAdapter giohangAdapter;
    private ArrayList<Giohang> giohangArrayList;
    private Button btnThanhToanHang, btnTiepTucMuaHang, btnThoat;
    private DatabaseReference shoppingCartsRef, productsRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_giohang);

        listViewGiohang = findViewById(R.id.lv_giohang);
        txtTongtien = findViewById(R.id.txt_tongtien);
        txtThongBao = findViewById(R.id.txt_thongbao);
        btnThanhToanHang = findViewById(R.id.bt_thanhtoanhang);
        btnTiepTucMuaHang = findViewById(R.id.bt_tieptucmuahang);
        btnThoat = findViewById(R.id.btn_thoat);

        giohangArrayList = new ArrayList<>();
        giohangAdapter = new GiohangAdapter(this, giohangArrayList);
        listViewGiohang.setAdapter(giohangAdapter);

        shoppingCartsRef = FirebaseDatabase.getInstance().getReference("ShoppingCarts").child("0"); // ID giỏ hàng
        productsRef = FirebaseDatabase.getInstance().getReference("Products");

        loadCartData();

        btnThanhToanHang.setOnClickListener(v -> {
            if (giohangArrayList.isEmpty()) {
                txtThongBao.setVisibility(View.VISIBLE);
                txtThongBao.setText("Giỏ hàng của bạn đang trống. Vui lòng thêm sản phẩm trước khi thanh toán!");
            } else {
                Intent intent = new Intent(GiohangActivity.this, ThanhToanActivity.class);
                startActivity(intent);
            }
        });

        btnTiepTucMuaHang.setOnClickListener(v -> finish());
        btnThoat.setOnClickListener(v -> finish());
    }

    private void loadCartData() {
        shoppingCartsRef.child("items").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                giohangArrayList.clear();
                for (DataSnapshot itemSnapshot : snapshot.getChildren()) {
                    String productId = itemSnapshot.child("productId").getValue(String.class);
                    int quantity = itemSnapshot.child("quantity").getValue(Integer.class);
                    double price = itemSnapshot.child("price").getValue(Double.class);

                    // Log sản phẩm nhận được
                    Log.d("GiohangActivity", "ProductId: " + productId + ", Quantity: " + quantity + ", Price: " + price);

                    // Fetch product details from Products table based on productId
                    productsRef.child(productId).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot productSnapshot) {
                            if (productSnapshot.exists()) {
                                String tensp = productSnapshot.child("name").getValue(String.class);
                                String hinhsp = productSnapshot.child("imageurl").getValue(String.class);
                                int giasp = (int) price; // Giá mỗi sản phẩm

                                // Log chi tiết sản phẩm
                                Log.d("GiohangActivity", "Name: " + tensp + ", ImageURL: " + hinhsp + ", Price: " + giasp);

                                // Add the item to giohangArrayList
                                giohangArrayList.add(new Giohang(productId, tensp, giasp, hinhsp, quantity));
                                giohangAdapter.notifyDataSetChanged();
                                updateTotalPrice();
                            } else {
                                Log.e("GiohangActivity", "Product not found for ID: " + productId);
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            // Handle potential error
                            Log.e("GiohangActivity", "Error fetching product details: " + error.getMessage());
                        }
                    });
                }

                if (giohangArrayList.isEmpty()) {
                    listViewGiohang.setVisibility(View.GONE);
                    txtThongBao.setVisibility(View.VISIBLE);
                } else {
                    listViewGiohang.setVisibility(View.VISIBLE);
                    txtThongBao.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle potential error
                Log.e("GiohangActivity", "Error fetching cart data: " + error.getMessage());
            }
        });
    }

    private void updateTotalPrice() {
        long totalPrice = 0L;
        for (Giohang giohang : giohangArrayList) {
            totalPrice += giohang.getGiasp() * giohang.getSoluongsp();
        }
        txtTongtien.setText(totalPrice + " Đ  ");
    }

    public void updateItemCount(int position, int newCount) {
        if (newCount >= 0) {
            giohangArrayList.get(position).setSoluongsp(newCount);
            giohangAdapter.notifyDataSetChanged();
            updateTotalPrice();
        }
    }
}
