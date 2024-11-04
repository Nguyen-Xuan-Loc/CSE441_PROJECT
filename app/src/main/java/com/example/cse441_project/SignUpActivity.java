package com.example.cse441_project;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.DataSnapshot;

import java.util.HashMap;
import java.util.Map;

public class SignUpActivity extends AppCompatActivity {
    private EditText edtTenDangKy, edtMatKhau, edtXacNhanMatKhau;
    private Button btnDangKy;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        edtTenDangKy = (EditText) findViewById(R.id.edt_ten_dang_ky);
        edtMatKhau = (EditText) findViewById(R.id.edt_mat_Khau_dang_ky);
        edtXacNhanMatKhau = (EditText) findViewById(R.id.edt_xac_nhan_mat_khau);
        btnDangKy = (Button) findViewById(R.id.btn_dang_ky);

        btnDangKy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerUser();
            }
        });
    }

    private void registerUser() {
        String email = edtTenDangKy.getText().toString().trim();
        String password = edtMatKhau.getText().toString().trim();
        String confirmPassword = edtXacNhanMatKhau.getText().toString().trim();

        // Kiểm tra thông tin đầu vào
        if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password) || TextUtils.isEmpty(confirmPassword)) {
            Toast.makeText(this, "Hãy điền đầy đủ thông tin", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!password.equals(confirmPassword)) {
            Toast.makeText(this, "Mật khẩu xác nhận không chính xác", Toast.LENGTH_SHORT).show();
            return;
        }

        // Lấy kích thước hiện tại của dữ liệu Users
        DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference("Users");
        usersRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                int userCount = (int) dataSnapshot.getChildrenCount();
                String userId = "" + (userCount);

                saveUserData(userId, email, password);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(SignUpActivity.this, "Lấy thông tin đăng nhập thất bại", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void saveUserData(String userId, String email, String password) {
        DatabaseReference databaseRef = FirebaseDatabase.getInstance().getReference("Users").child(userId);

        // Tạo dữ liệu người dùng
        Map<String, Object> userData = new HashMap<>();
        userData.put("username", email);
        userData.put("password", password);
        userData.put("role", "customer");
        userData.put("userId", userId);

        databaseRef.setValue(userData).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Toast.makeText(this, "Đăng ký thành công", Toast.LENGTH_SHORT).show();
                finish();
            } else {
                Toast.makeText(this, "Đăng ký thất bại", Toast.LENGTH_SHORT).show();
            }
        });
    }
}