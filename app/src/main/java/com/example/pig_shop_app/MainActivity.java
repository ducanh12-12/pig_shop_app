package com.example.pig_shop_app;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.example.pig_shop_app.Admin.AdminActivity;
import com.example.pig_shop_app.User.Product.ProductListActivity;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FirebaseApp.initializeApp(this);
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
        setContentView(R.layout.activity_main);
        findViewById(R.id.category_product).setOnClickListener(view -> {
            Intent intent = new Intent(this, ProductListActivity.class);
            startActivity(intent);
    });
        findViewById(R.id.admin_category).setOnClickListener(view -> {
            Intent intent = new Intent(this, AdminActivity.class);
            startActivity(intent);
        });
    }
}