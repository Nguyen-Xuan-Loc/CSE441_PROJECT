package com.example.cse441_project.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.example.cse441_project.R;

public class LoginActivity extends AppCompatActivity {
    private TextView txtDuongDanDangKy;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);
        txtDuongDanDangKy = (TextView) findViewById(R.id.txt_duong_dan_dang_ky);
        txtDuongDanDangKy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent openSignUp = new Intent(LoginActivity.this, SignUpActivity.class);
                startActivity(openSignUp);
            }
        });
    }
}
