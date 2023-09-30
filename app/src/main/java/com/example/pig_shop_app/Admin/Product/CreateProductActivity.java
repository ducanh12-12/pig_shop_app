package com.example.pig_shop_app.Admin.Product;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.example.pig_shop_app.User.Product.Product;
import com.example.pig_shop_app.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class CreateProductActivity extends AppCompatActivity {

    private EditText productNameEditText;
    private EditText productPriceEditText;
    private EditText productDescriptionEditText;
    private EditText productSizeEditText;
    private EditText productImageEditText;
    private Button createProductButton;
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
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("pigs");

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
                String productId = databaseReference.push().getKey();
                if (productId != null) {
                    databaseReference.child(productId).setValue(product);
                    finish(); // Đóng màn hình sau khi tạo sản phẩm
                }
            }
        });

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