package com.example.cse441_project;

import com.example.cse441_project.Adapter.ProductAdapter;
import com.example.cse441_project.Model.Product1;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ProductAdapter productAdapter;
    private List<Product1> productList1 = new ArrayList<>();
    private List<Product1> filteredProductList = new ArrayList<>();
    private DrawerLayout drawerLayout;
    private ImageView menuIcon;
    private NavigationView navigationView;
    private EditText searchField;
    private ImageView searchIcon;
    private TextView noProductsMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        drawerLayout = findViewById(R.id.main);
        menuIcon = findViewById(R.id.menu_icon);
        navigationView = findViewById(R.id.navigation_view);


        //Cau truc recyclerView va hien thi san pham
        recyclerView = findViewById(R.id.product_list);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2);
        recyclerView.setLayoutManager(gridLayoutManager);

        loadJsonData();

        productAdapter = new ProductAdapter(this, productList1, filteredProductList);
        recyclerView.setAdapter(productAdapter);

        noProductsMessage = findViewById(R.id.no_products_message);

        //Bat su kien cho Navigation
        menuIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Kiểm tra nếu NavigationView đã mở thì đóng, nếu chưa thì mở
                if (drawerLayout.isDrawerOpen(findViewById(R.id.navigation_view))) {
                    drawerLayout.closeDrawer(findViewById(R.id.navigation_view));
                } else {
                    drawerLayout.openDrawer(findViewById(R.id.navigation_view));
                }
            }
        });


        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @SuppressLint("NonConstantResourceId")
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                int itemId = item.getItemId();

                if (itemId == R.id.nav_phone) {
                    startActivity(new Intent(MainActivity.this, ProductListActivity.class));
                }else if(itemId == R.id.nav_contact){
                    startActivity(new Intent(MainActivity.this, ContactActivity.class));
                }
                drawerLayout.closeDrawer(navigationView);
                return true;
            }
        });

        searchField = findViewById(R.id.search_field);
        searchIcon = findViewById(R.id.search_icon);

        searchIcon.setOnClickListener(v -> {
            String searchQuery = searchField.getText().toString().trim();
            searchProducts(searchQuery);
        });

    }

    private void loadJsonData() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference productsRef = database.getReference("Products");
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference();

        productsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                productList1.clear();
                List<Product1> tempProductList = new ArrayList<>(); // Danh sách tạm thời để lưu sản phẩm

                for (DataSnapshot productSnapshot : snapshot.getChildren()) {
                    String name = productSnapshot.child("name").getValue(String.class);
                    Double price = productSnapshot.child("price").getValue(Double.class);
                    String imagename = productSnapshot.child("imageurl").getValue(String.class);

                    if (name != null && price != null && imagename != null) {
                        // Log dữ liệu để kiểm tra
                        Log.d("ImageName", "Image name: " + imagename);

                        // Lấy URL từ Firebase Storage
                        storageRef.child("images/" + imagename).getDownloadUrl().addOnSuccessListener(uri -> {
                            String imageUrl = uri.toString();
                            tempProductList.add(new Product1(name, price, imageUrl));

                            // Kiểm tra nếu đã tải đủ số sản phẩm
                            if (tempProductList.size() == snapshot.getChildrenCount()) {
                                productList1.addAll(tempProductList);
                                productAdapter.notifyDataSetChanged(); // Chỉ gọi một lần
                            }
                        }).addOnFailureListener(exception -> {
                            Log.e("FirebaseStorage", "Error getting image URL", exception);
                        });
                    } else {
                        Log.e("FirebaseData", "Missing data for product.");
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("FirebaseDatabase", "Error loading products", error.toException());
            }
        });
    }

    private void searchProducts(String query) {
        filteredProductList.clear();

        if (query.isEmpty()) {
            filteredProductList.addAll(productList1); // Hiển thị tất cả sản phẩm nếu không có từ khóa
            noProductsMessage.setVisibility(View.GONE);
        } else {
            for (Product1 product : productList1) {
                if (product.getName().equalsIgnoreCase(query)) {
                    filteredProductList.add(product); // Thêm sản phẩm khớp vào danh sách lọc
                }
            }

            if (filteredProductList.isEmpty()) {
                noProductsMessage.setVisibility(View.VISIBLE); // Hiển thị thông báo không có sản phẩm
            } else {
                noProductsMessage.setVisibility(View.GONE); // Ẩn thông báo nếu có sản phẩm khớp
            }
        }

        productAdapter.notifyDataSetChanged(); // Cập nhật lại RecyclerView
    }

}