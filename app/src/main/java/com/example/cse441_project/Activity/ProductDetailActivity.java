package com.example.cse441_project.Activity;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.cse441_project.R;
import com.squareup.picasso.Picasso;

public class ProductDetailActivity extends AppCompatActivity {
    private TextView prdDetailName ;
    private TextView prdDetailPrice ;
    private TextView prdDetailDes;
    private ImageView prdImage;
    private ImageView back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.product_detail); // Đảm bảo layout đúng

        prdDetailName = findViewById(R.id.prdDetail_name);
        prdDetailPrice = findViewById(R.id.prdDetail_price);
        prdImage = findViewById(R.id.prdDetail_image);
        prdDetailDes = findViewById(R.id.prdDetail_describe);

        back = findViewById(R.id.menu_icon);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ProductDetailActivity.this , MainActivity.class));
            }
        });

        // Nhận dữ liệu từ Intent
        Intent intent = getIntent();
        String productName = intent.getStringExtra("productName");
        double productPrice = intent.getDoubleExtra("productPrice", 0);
        String productImageUrl = intent.getStringExtra("productImageUrl");
        String productDescribe = intent.getStringExtra("productDescribe");

        // Hiển thị dữ liệu
        prdDetailName.setText(productName);
        prdDetailPrice.setText(String.valueOf(productPrice));
        prdDetailDes.setText(productDescribe);
        Picasso.get().load(productImageUrl).into(prdImage);
    }
}
