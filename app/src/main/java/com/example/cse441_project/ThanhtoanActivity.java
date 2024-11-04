package com.example.cse441_project;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class ThanhtoanActivity extends AppCompatActivity {

    private EditText edtTen, edtEmail, edtSDT, edtDiaChi;
    private CheckBox checkBox;
    private Button btnTroVe, btnXacNhan;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thanhtoan);

        // Khởi tạo các thành phần giao diện
        edtTen = findViewById(R.id.edt_ten);
        edtEmail = findViewById(R.id.edt_email);
        edtSDT = findViewById(R.id._SDT);
        edtDiaChi = findViewById(R.id.edt_dia_chi);
        checkBox = findViewById(R.id.cb_thanhtoankhinhanhang);
        btnTroVe = findViewById(R.id.btn_tro_ve);
        btnXacNhan = findViewById(R.id.btn_xac_nhan);

        // Xử lý sự kiện cho nút Trở Về
        btnTroVe.setOnClickListener(v -> finish()); // Quay về màn hình trước đó

        // Xử lý sự kiện cho nút Xác Nhận
        btnXacNhan.setOnClickListener(v -> {
            if (validateForm()) {
                // Nếu tất cả thông tin đều hợp lệ, hiển thị thông báo đặt hàng thành công
                Toast.makeText(this, "Mua hàng thành công!", Toast.LENGTH_LONG).show();
                // Thực hiện hành động sau khi xác nhận thành công (ví dụ: chuyển về màn hình chính)
                finish(); // Quay lại màn hình trước đó hoặc xử lý khác tùy bạn
            }
        });
    }

    // Phương thức kiểm tra tính hợp lệ của form
    private boolean validateForm() {
        // Kiểm tra các trường thông tin
        if (TextUtils.isEmpty(edtTen.getText())) {
            edtTen.setError("Vui lòng nhập tên người dùng");
            edtTen.requestFocus();
            return false;
        }
        if (TextUtils.isEmpty(edtEmail.getText())) {
            edtEmail.setError("Vui lòng nhập email");
            edtEmail.requestFocus();
            return false;
        }
        if (TextUtils.isEmpty(edtSDT.getText())) {
            edtSDT.setError("Vui lòng nhập số điện thoại");
            edtSDT.requestFocus();
            return false;
        }
        if (TextUtils.isEmpty(edtDiaChi.getText())) {
            edtDiaChi.setError("Vui lòng nhập địa chỉ");
            edtDiaChi.requestFocus();
            return false;
        }
        if (!checkBox.isChecked()) {
            Toast.makeText(this, "Vui lòng đồng ý thanh toán khi nhận hàng.", Toast.LENGTH_SHORT).show();
            checkBox.requestFocus();
            return false;
        }
        return true;
    }
}
