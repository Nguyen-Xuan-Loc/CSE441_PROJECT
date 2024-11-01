package com.example.cse441_project;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cse441_project.adapter.ProductAdapter;
import com.example.cse441_project.model.ProductList;
import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;
import java.util.List;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class AdminActivity extends AppCompatActivity {
    private DrawerLayout drawerLayout;
    private RecyclerView recyclerView;
    private ProductAdapter adapter;
    private List<ProductList> productList;
    private NavController navController;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        // Thiết lập Toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle("MOBILSHOP");

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        productList = new ArrayList<>();
        adapter = new ProductAdapter(this, productList);
        recyclerView.setAdapter(adapter);
        loadProductsFromFirebase();
        drawerLayout = findViewById(R.id.drawerLayout);

        // Tạo nút Toggle cho Drawer
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawerLayout, toolbar,
                R.string.navigation_drawer_open,
                R.string.navigation_drawer_close
        );
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        // Xử lý chọn item trong NavigationView
        NavigationView navigationView = findViewById(R.id.navigationView);
        navigationView.setNavigationItemSelectedListener(item -> {
            int id = item.getItemId();

            if (id == R.id.nav_home) {
                Intent openadmin = new Intent(AdminActivity.this, AdminActivity.class);
                startActivity(openadmin);
            } else if (id == R.id.nav_add) {
                replaceFragment(new AddProductFragment());
            }

            drawerLayout.closeDrawer(GravityCompat.START);
            return true;
        });
    }

    // Đóng NavigationView khi nhấn nút back
    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
    private void loadProductsFromFirebase() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference productsRef = database.getReference("Products");
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference();

        productsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                productList.clear();
                for (DataSnapshot productSnapshot : snapshot.getChildren()) {
                    String name = productSnapshot.child("name").getValue(String.class);
                    Double price = productSnapshot.child("price").getValue(Double.class);
                    String description = productSnapshot.child("description").getValue(String.class);
                    String imageName = productSnapshot.child("imageurl").getValue(String.class);

                    if (name != null && price != null && description != null && imageName != null) {
                        // Log dữ liệu để kiểm tra
                        Log.d("ImageName", "Image name: " + imageName);
                        // Lấy URL từ Firebase Storage
                        storageRef.child("images/" + imageName).getDownloadUrl().addOnSuccessListener(uri -> {
                            String imageUrl = uri.toString();
                            ProductList productList = new ProductList(name, price, description, imageUrl);
                            AdminActivity.this.productList.add(productList);

                            // Cập nhật RecyclerView
                            adapter.notifyDataSetChanged();
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
    // Phương thức để thay thế Fragment
    private void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, fragment);
        fragmentTransaction.addToBackStack(null); // Cho phép quay lại fragment trước đó
        fragmentTransaction.commit();
    }

}

