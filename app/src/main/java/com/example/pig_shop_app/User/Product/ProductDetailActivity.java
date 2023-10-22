package com.example.pig_shop_app.User.Product;

import android.app.AlertDialog;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.pig_shop_app.Cart;
import com.example.pig_shop_app.R;
import com.example.pig_shop_app.User.Product.Product;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class ProductDetailActivity extends AppCompatActivity {

    private ImageView productImageView;
    private TextView productNameTextView;
    private TextView productPriceTextView;
    private TextView productDescriptionTextView;
    private Button addToCartButton;
    private String product_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.item_activity_product_detail);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        // Ánh xạ các thành phần trong layout
        productImageView = findViewById(R.id.product_detail_image);
        productNameTextView = findViewById(R.id.product_detail_name);
        productPriceTextView = findViewById(R.id.product_detail_price);
        productDescriptionTextView = findViewById(R.id.product_detail_description);
        addToCartButton = findViewById(R.id.add_cart);
       findViewById(R.id.back_button).setOnClickListener(view -> {
               finish();
       });
        String productId = getIntent().getStringExtra("product_id");
        product_id = productId;

        FirebaseDatabase.getInstance().getReference("pigs").child(productId)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            Product product = dataSnapshot.getValue(Product.class);

                            // Hiển thị thông tin sản phẩm trong layout
                            productNameTextView.setText(product.getTitle());
                            productPriceTextView.setText("Price: $" + product.getPrice());
                            productDescriptionTextView.setText(product.getDescription());
                            Picasso.get().load(product.getAvatar()).into(productImageView);
                            addToCartButton.setOnClickListener(view -> {
                                showAddProductDialog();
                            });
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        // Xử lý lỗi
                    }
                });
    }
    public void showAddProductDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.add_cart, null);
        builder.setView(dialogView);

        // Reference to Firebase Database
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("carts");
        builder.setTitle("Đặt hàng");

        builder.setPositiveButton("Thêm", (dialog, which) -> {
            EditText fullName = dialogView.findViewById(R.id.full_name);
            EditText phoneNumber = dialogView.findViewById(R.id.phone_number);
            EditText address = dialogView.findViewById(R.id.address);

            String fullNameValue = fullName.getText().toString();
            String phoneNumberValue = phoneNumber.getText().toString();
            String addressValue = address.getText().toString();

            // Tạo một đối tượng Product và thêm nó vào Firebase Database
            String key = databaseReference.push().getKey();
            Cart cart = new Cart(product_id,fullNameValue, phoneNumberValue,addressValue,key);
            databaseReference.child(key).setValue(cart);

            Toast.makeText(this, "Bạn đã đặt hàng thành công !", Toast.LENGTH_SHORT).show();
        });

        builder.setNegativeButton("Hủy", (dialog, which) -> {
            // Đóng dialog nếu người dùng chọn hủy
            dialog.cancel();
        });

        builder.show();
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