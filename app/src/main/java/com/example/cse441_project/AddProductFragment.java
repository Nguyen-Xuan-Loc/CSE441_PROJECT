package com.example.cse441_project;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.OpenableColumns;
import android.text.Editable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import com.example.cse441_project.model.Product;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.text.BreakIterator;
import java.util.HashMap;

public class AddProductFragment extends Fragment {
    private Button btnXacNhan;
    private Button btnTroVe;
    private Button btnChooseImage;
    private EditText etProductName, etProductBrand, etPrice, etQuantity, etDescription;
    private ImageView imageView;
    private Uri imageUri;

    private DatabaseReference databaseReference;
    private StorageReference storageReference;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_product, container, false);

        Toolbar toolbar = view.findViewById(R.id.toolbar);
        if (getActivity() != null) {
            ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
            ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("MOBILSHOP");
        }

        // Khởi tạo Firebase references
        databaseReference = FirebaseDatabase.getInstance().getReference("Products");
        storageReference = FirebaseStorage.getInstance().getReference("images");

        // Khởi tạo các thành phần giao diện
        btnTroVe = view.findViewById(R.id.btn_back);
        btnXacNhan = view.findViewById(R.id.btn_confirm);
        btnChooseImage = view.findViewById(R.id.btn_choose_image);
        etProductName = view.findViewById(R.id.et_product_name);
        etProductBrand = view.findViewById(R.id.et_product_type);
        etPrice = view.findViewById(R.id.et_price);
        etQuantity = view.findViewById(R.id.et_quantity);
        etDescription = view.findViewById(R.id.et_description);
        imageView = view.findViewById(R.id.img_product);

        // Xử lý nút "Trở về"
        btnTroVe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requireActivity().getSupportFragmentManager().popBackStack();
            }
        });

        // Xử lý nút "Xác nhận"
        btnXacNhan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addProductToFirebase();
            }
        });

        // Xử lý nút chọn ảnh
        btnChooseImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFileChooser();
            }
        });

        return view;
    }

    private void openFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Cần chọn ảnh"), 1);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == getActivity().RESULT_OK && data != null && data.getData() != null) {
            imageUri = data.getData();
            imageView.setImageURI(imageUri);
        }
    }

    private void addProductToFirebase() {
        if (imageUri != null) {
            String fileName = imageUri.getLastPathSegment();
            databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    int productId = (int) snapshot.getChildrenCount();

                    // Lưu ảnh vào Firebase Storage
                    StorageReference fileReference = storageReference.child(fileName + ".png");

                    fileReference.putFile(imageUri).addOnSuccessListener(taskSnapshot -> {
                        fileReference.getDownloadUrl().addOnSuccessListener(uri -> {
                            String name = etProductName.getText().toString().trim();
                            String brand = etProductBrand.getText().toString().trim();
                            String price = etPrice.getText().toString().trim();
                            String quantity = etQuantity.getText().toString().trim();
                            String description = etDescription.getText().toString().trim();
                            double priceProduct = 0.0;
                            int stockQuantity = 0;

                            // Kiểm tra và ép kiểu giá
                            if (!price.isEmpty()) {
                                priceProduct = Double.parseDouble(price);
                            }

                            // Kiểm tra và ép kiểu số lượng
                            if (!quantity.isEmpty()) {
                                stockQuantity = Integer.parseInt(quantity);
                            }
                            // Tạo dữ liệu sản phẩm
                            HashMap<String, Object> productData = new HashMap<>();
                            productData.put("productId", productId);
                            productData.put("name", name);
                            productData.put("brand", brand);
                            productData.put("price", priceProduct);
                            productData.put("stock_quantity", stockQuantity);
                            productData.put("description", description);
                            productData.put("imageurl", fileName+".png");

                            // Lưu vào Firebase Database
                            databaseReference.child(String.valueOf(productId)).setValue(productData)
                                    .addOnCompleteListener(task -> {
                                        if (task.isSuccessful()) {
                                            Toast.makeText(getActivity(), "Thêm sản phẩm thành công", Toast.LENGTH_SHORT).show();
                                            getActivity().onBackPressed();
                                        } else {
                                            Toast.makeText(getActivity(), "Thêm sản phẩm thất bại", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                        });
                    }).addOnFailureListener(e -> {
                        Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
                    });
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Toast.makeText(getActivity(), "Lỗi khi lấy ID sản phẩm", Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            Toast.makeText(getActivity(), "Hãy chọn 1 ảnh", Toast.LENGTH_SHORT).show();
        }
    }
}

