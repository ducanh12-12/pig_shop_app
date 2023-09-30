package com.example.pig_shop_app.Admin;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.appcompat.app.AppCompatActivity;

import com.example.pig_shop_app.Admin.Product.ProductTable;
import com.example.pig_shop_app.R;

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