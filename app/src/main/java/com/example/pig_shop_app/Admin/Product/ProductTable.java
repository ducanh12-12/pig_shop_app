package com.example.pig_shop_app.Admin.Product;

import android.app.Activity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MenuItem;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.pig_shop_app.R;
import com.example.pig_shop_app.User.Product.Product;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ProductTable extends AppCompatActivity {
    private ArrayList<Product> productList;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        productList = new ArrayList<Product>();
        setContentView(R.layout.activity_table_product);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        FirebaseDatabase.getInstance().getReference("pigs")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        productList.clear();
                        for (DataSnapshot productSnapshot : dataSnapshot.getChildren()) {
                            Product product = productSnapshot.getValue(Product.class);
                            product.setKey(productSnapshot.getKey());
                            productList.add(product);
                        }
                        createTable();
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
    private void createTable() {
        TableLayout tableLayout = findViewById(R.id.table_product);
        TableRow.LayoutParams rowParams = new TableRow.LayoutParams(
                TableRow.LayoutParams.MATCH_PARENT,
                TableRow.LayoutParams.WRAP_CONTENT
        );
        TableRow headerRow = new TableRow(this);
        headerRow.setLayoutParams(rowParams);

        TextView headerName = new TextView(this);
        headerName.setText("Tên sản phẩm");
        headerName.setPadding(8, 8, 8, 8);
        headerName.setGravity(Gravity.CENTER);

        TextView headerPrice = new TextView(this);
        headerPrice.setText("Giá sản phẩm");
        headerPrice.setPadding(8, 8, 8, 8);
        headerPrice.setGravity(Gravity.CENTER);
        TextView headerSize = new TextView(this);
        headerSize.setText("Kích thước sản phẩm");
        headerPrice.setPadding(8, 8, 8, 8);
        headerPrice.setGravity(Gravity.CENTER);
        headerRow.addView(headerName);
        headerRow.addView(headerPrice);
        headerRow.addView(headerSize);

        tableLayout.addView(headerRow);

        for (Product product : productList) {
            TableRow dataRow = new TableRow(this);
            dataRow.setLayoutParams(rowParams);

            TextView productName = new TextView(this);
            productName.setText(product.getTitle());
            productName.setPadding(8, 8, 8, 8);
            productName.setGravity(Gravity.CENTER);

            TextView productPrice = new TextView(this);
            productPrice.setText(product.getPrice());
            productPrice.setPadding(8, 8, 8, 8);
            productPrice.setGravity(Gravity.CENTER);
            TextView productSize = new TextView(this);
            productSize.setText(product.getPrice());
            productSize.setPadding(8, 8, 8, 8);
            productSize.setGravity(Gravity.CENTER);
            dataRow.addView(productName);
            dataRow.addView(productPrice);
            dataRow.addView(productSize);

            tableLayout.addView(dataRow);
        }
    }
}
