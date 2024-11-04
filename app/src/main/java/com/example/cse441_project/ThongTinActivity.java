package com.example.cse441_project;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

public class ThongTinActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thongtin); // Kết nối với layout thongtin.xml

        // Xử lý sự kiện cho nút thoát
        Button btnThoat = findViewById(R.id.btn_thoat);
        btnThoat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish(); // Đóng activity khi nhấn nút thoát
            }
        });
    }
}
