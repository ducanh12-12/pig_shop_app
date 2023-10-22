package com.example.pig_shop_app.Admin.Product;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.pig_shop_app.User.Product.Product;
import com.example.pig_shop_app.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class CreateProductActivity extends AppCompatActivity {

    private EditText productNameEditText;
    private EditText productPriceEditText;
    private EditText productDescriptionEditText;
    private EditText productSizeEditText;
    private EditText productImageEditText;
    private Button createProductButton;
    private Button deleteButton;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_product);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        productNameEditText = findViewById(R.id.product_name_edittext);
        productPriceEditText = findViewById(R.id.product_price_edittext);
        productImageEditText = findViewById(R.id.product_image_edittext);
        productSizeEditText = findViewById(R.id.product_size_edittext);
        productDescriptionEditText = findViewById(R.id.product_description_edittext);
        createProductButton = findViewById(R.id.create_product_button);
        deleteButton = findViewById(R.id.delete_button_pd);
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("pigs");
        String productId = getIntent().getStringExtra("product_id");
        if (productId != null) {
            createProductButton.setText("Sửa sản phẩm");
            FirebaseDatabase.getInstance().getReference("pigs").child(productId)
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if (dataSnapshot.exists()) {
                                Product product = dataSnapshot.getValue(Product.class);
                                if (product != null) {
                                    productNameEditText.setText(product.getTitle());
                                    productSizeEditText.setText(product.getSize());
                                    productPriceEditText.setText(product.getPrice());
                                    productImageEditText.setText(product.getAvatar());
                                    productDescriptionEditText.setText(product.getDescription());
                                }
                            }
                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                            // Xử lý lỗi
                        }
            });
        } else {
            deleteButton.setVisibility(View.INVISIBLE);
        }
        createProductButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Lấy thông tin sản phẩm từ các EditText
                String productName = productNameEditText.getText().toString().trim();
                String productSize = productSizeEditText.getText().toString();
                String productPrice = productPriceEditText.getText().toString();
                String productImage = productImageEditText.getText().toString();
                String productDescription = productDescriptionEditText.getText().toString().trim();

                // Tạo một sản phẩm mới
                Product product = new Product(productImage,productDescription,productPrice, productSize, productName);

                // Thêm sản phẩm mới vào Firebase Realtime Database
                if (productId == null) {
                    String productId = databaseReference.push().getKey();
                    databaseReference.child(productId).setValue(product)
                         .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toast.makeText(CreateProductActivity.this, "Thêm mới sản phẩm thành công", Toast.LENGTH_SHORT).show();
                            finish();
                        }
                    })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(CreateProductActivity.this,"Thêm mới thất bại", Toast.LENGTH_SHORT).show();
                                    finish();
                                }
                            });
                } else {
                    databaseReference.child(productId).setValue(product)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Toast.makeText(CreateProductActivity.this, "Cập nhật sản phẩm thành công", Toast.LENGTH_SHORT).show();
                                    finish();
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(CreateProductActivity.this,"Cập nhật thất bại", Toast.LENGTH_SHORT).show();
                                    finish();
                                }
                            });
                }
            }
        });
        deleteButton.setOnClickListener(view -> {
            showDeleteConfirmationDialog(productId);
        });
    }
    public void showDeleteConfirmationDialog(final String productId) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(CreateProductActivity.this);
        alertDialogBuilder.setTitle("Xác nhận xoá");
        alertDialogBuilder.setMessage("Bạn có chắc chắn muốn xoá sản phẩm này?");
        alertDialogBuilder.setPositiveButton("Xoá", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                deleteProduct(productId);
            }
        });
        alertDialogBuilder.setNegativeButton("Hủy", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        alertDialogBuilder.create().show();
    }

    public void deleteProduct(String productId) {
        if (productId != null && !productId.isEmpty()) {
            databaseReference.child(productId).removeValue()
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toast.makeText(CreateProductActivity.this, "Xoá sản phẩm thành công", Toast.LENGTH_SHORT).show();
                            finish();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(CreateProductActivity.this,"Xoá sản phẩm thất bại", Toast.LENGTH_SHORT).show();
                            finish();
                        }
                    });
        }
    }
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            // Handle the back button click event here.
            onBackPressed(); // This will simulate the Back button press.
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}