package com.example.pig_shop_app.User.Product;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.view.MenuItem;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.pig_shop_app.R;
import com.example.pig_shop_app.User.Product.Product;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class ProductDetailActivity extends AppCompatActivity {

    private ImageView productImageView;
    private TextView productNameTextView;
    private TextView productPriceTextView;
    private TextView productDescriptionTextView;
    private Button addToCartButton;

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
        addToCartButton = findViewById(R.id.add_to_cart_button);
       findViewById(R.id.back_button).setOnClickListener(view -> {
               finish();
       });
        String productId = getIntent().getStringExtra("product_id");

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
                                // Thực hiện logic thêm sản phẩm vào giỏ hàng ở đây
                            });
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        // Xử lý lỗi
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