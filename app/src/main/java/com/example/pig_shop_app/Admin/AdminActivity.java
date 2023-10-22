package com.example.pig_shop_app.Admin;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.appcompat.app.AppCompatActivity;

import com.example.pig_shop_app.Admin.Cart.CartTable;
import com.example.pig_shop_app.Admin.Product.ProductTable;
import com.example.pig_shop_app.Admin.User.AdminMain;
import com.example.pig_shop_app.R;
import com.example.pig_shop_app.Admin.Blogs.BlogList;

public class AdminActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        findViewById(R.id.pig_button).setOnClickListener(view -> {
            Intent intent = new Intent(this, ProductTable.class);
            startActivity(intent);
        });
        findViewById(R.id.blog_button).setOnClickListener(view -> {
            Intent intent = new Intent(this, BlogList.class);
            startActivity(intent);
        });
        findViewById(R.id.user_button).setOnClickListener(view -> {
            Intent intent = new Intent(this, AdminMain.class);
            startActivity(intent);
        });
        findViewById(R.id.order_button).setOnClickListener(view -> {
            Intent intent = new Intent(this, CartTable.class);
            startActivity(intent);
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