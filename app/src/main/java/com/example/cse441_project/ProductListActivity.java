package com.example.cse441_project;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import com.example.cse441_project.Model.Product1;
import com.example.cse441_project.Adapter.Product1Adapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

public class ProductListActivity extends AppCompatActivity {

    private Spinner sortSpinner;
    private RecyclerView productRecyclerView;
    private Product1Adapter product1Adapter;
    private List<Product1> productList = new ArrayList<>();
    private ImageView back;
    private ImageView cart;


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.product_list); // Đảm bảo tên layout đúng

        back = findViewById(R.id.menu_icon);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ProductListActivity.this, MainActivity.class));
            }
        });


        cart = findViewById(R.id.cart_icon1);
        cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ProductListActivity.this , GiohangActivity.class));
            }
        });
        sortSpinner = findViewById(R.id.sort_spinner);
        setupSpinner();

        sortSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 1) {
                    sortProductListByPriceAscending();
                } else if (position == 2) {
                    sortProductListByPriceDescending();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });
        productRecyclerView = findViewById(R.id.product_recycler_view);
        productRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Đọc dữ liệu JSON

        loadJsonData();


        product1Adapter = new Product1Adapter(this, productList);
        productRecyclerView.setAdapter(product1Adapter);


    }

    private void setupSpinner() {
        // Dữ liệu cho Spinner
        List<String> sortOptions = Arrays.asList("Sắp xếp theo", " Giá thấp đến cao", "Giá cao đến thấp");

        // Tạo ArrayAdapter
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, sortOptions);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // Gán adapter cho Spinner
        sortSpinner.setAdapter(adapter);
    }
    private void loadJsonData() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference productsRef = database.getReference("Products");
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference();

        productsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                productList.clear();
                List<Product1> tempProductList = new ArrayList<>(); // Danh sách tạm thời để lưu sản phẩm

                for (DataSnapshot productSnapshot : snapshot.getChildren()) {
                    String name = productSnapshot.child("name").getValue(String.class);
                    Double price = productSnapshot.child("price").getValue(Double.class);
                    String imagename = productSnapshot.child("imageurl").getValue(String.class);
                    String description = productSnapshot.child("description").getValue(String.class);

                    if (name != null && price != null && imagename != null) {
                        Log.d("ImageName", "Image name: " + imagename);

                        // Lấy URL từ Firebase Storage
                        storageRef.child("images/" + imagename).getDownloadUrl().addOnSuccessListener(uri -> {
                            String imageUrl = uri.toString();
                            tempProductList.add(new Product1(name, price, imageUrl, description)); // Thêm description

                            // Kiểm tra nếu đã tải đủ số sản phẩm
                            if (tempProductList.size() == snapshot.getChildrenCount()) {
                                productList.addAll(tempProductList);
                                product1Adapter.notifyDataSetChanged(); // Chỉ gọi một lần
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


    private void sortProductListByPriceAscending() {
        Collections.sort(productList, new Comparator<Product1>() {
            @Override
            public int compare(Product1 p1, Product1 p2) {
                return Double.compare(p1.getPrice(), p2.getPrice());
            }
        });
        product1Adapter.notifyDataSetChanged();
    }

    private void sortProductListByPriceDescending() {
        Collections.sort(productList, new Comparator<Product1>() {
            @Override
            public int compare(Product1 p1, Product1 p2) {
                return Double.compare(p2.getPrice(), p1.getPrice());
            }
        });
        product1Adapter.notifyDataSetChanged();
    }

}