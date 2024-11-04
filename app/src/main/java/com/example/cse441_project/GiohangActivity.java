package com.example.cse441_project;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent; // Import Intent để chuyển activity
import android.widget.Toast;

import com.example.cse441_project.Adapter.GiohangAdapter;
import com.example.cse441_project.Model.Giohang;
import com.example.cse441_project.Model.Product2;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class GiohangActivity extends AppCompatActivity {
    private ListView lvGiohang;
    private TextView txtTongtien;
    private ArrayList<Giohang> giohangList;
    private ArrayList<Product2> productList; // Danh sách sản phẩm
    private GiohangAdapter adapter;
    private DatabaseReference databaseReference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_giohang);

        lvGiohang = findViewById(R.id.lv_giohang);
        txtTongtien = findViewById(R.id.txt_tongtien);
        giohangList = new ArrayList<>();
        productList = new ArrayList<>(); // Khởi tạo danh sách sản phẩm
        adapter = new GiohangAdapter(this, giohangList, productList); // Truyền danh sách sản phẩm
        lvGiohang.setAdapter(adapter);
        databaseReference = FirebaseDatabase.getInstance().getReference("ShoppingCarts/0/items");

        loadShoppingCartData();
        loadProductData(); // Tải dữ liệu sản phẩm




    // Trong onCreate() thêm mã sau
        Button btnThanhtoan = findViewById(R.id.btn_thanhtoan);
        btnThanhtoan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (giohangList.isEmpty()) {
                    // Hiển thị thông báo nếu giỏ hàng rỗng
                    Toast.makeText(GiohangActivity.this, "Giỏ hàng hiện đang trống", Toast.LENGTH_SHORT).show();
                } else {
                    // Chuyển sang ThanhToanActivity nếu có sản phẩm trong giỏ hàng
                    Intent intent = new Intent(GiohangActivity.this, ThanhtoanActivity.class);
                    startActivity(intent);
                }
            }
        });


        // Xử lý sự kiện cho nút thoát
        Button btnThoat = findViewById(R.id.btn_thoat);
        btnThoat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish(); // Đóng activity
            }
        });

        // Xử lý sự kiện cho nút tiếp tục mua hàng
        Button btnTieptucmuahang = findViewById(R.id.bt_tieptucmuahang);
        btnTieptucmuahang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish(); // Đóng activity
            }
        });
    }

    private void loadShoppingCartData() {
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                giohangList.clear(); // Xóa danh sách hiện tại

                for (DataSnapshot itemSnapshot : dataSnapshot.getChildren()) {
                    double price = itemSnapshot.child("price").getValue(Double.class);
                    long productId = itemSnapshot.child("productId").getValue(long.class);
                    int quantity = itemSnapshot.child("quantity").getValue(Integer.class);

                    // Thêm sản phẩm vào giỏ hàng
                    giohangList.add(new Giohang(productId, price, quantity));
                }

                adapter.notifyDataSetChanged(); // Cập nhật danh sách
                updateTotalPrice();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Xử lý lỗi
            }
        });
    }

    private void loadProductData() {
        // Tải danh sách sản phẩm từ Firebase
        DatabaseReference productRef = FirebaseDatabase.getInstance().getReference("Products");
        productRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                productList.clear(); // Xóa danh sách sản phẩm hiện tại

                for (DataSnapshot productSnapshot : dataSnapshot.getChildren()) {
                    long productId = productSnapshot.child("productId").getValue(long.class);
                    String name = productSnapshot.child("name").getValue(String.class);
                    double price = productSnapshot.child("price").getValue(Double.class);
                    String imageUrl = productSnapshot.child("imageurl").getValue(String.class);
                    int stockQuantity = productSnapshot.child("stock_quantity").getValue(Integer.class);

                    // Thêm sản phẩm vào danh sách
                    productList.add(new Product2(productId, name, price, imageUrl, stockQuantity));
                }

                adapter.notifyDataSetChanged(); // Cập nhật adapter khi danh sách sản phẩm đã tải xong
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Xử lý lỗi
            }
        });
    }

    public void updateTotalPrice() {
        double totalPrice = 0.0;
        for (Giohang item : giohangList) {
            totalPrice += item.getPrice() * item.getQuantity();
        }
        txtTongtien.setText(String.format("%.2f VNĐ", totalPrice));
    }
}
