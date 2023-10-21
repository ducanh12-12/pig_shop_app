package com.example.pig_shop_app.Admin.Product;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MenuItem;
import android.widget.Adapter;
import android.widget.GridLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pig_shop_app.R;
import com.example.pig_shop_app.Admin.Product.Product;
import com.example.pig_shop_app.User.Product.ProductAdapter;
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
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        RecyclerView recyclerView = findViewById(R.id.recycler_view);

        AdminProductAdapter adapter = new AdminProductAdapter(productList, this);
        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(this, 2);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setAdapter(adapter);
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
                        adapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        // Xử lý lỗi
                    }
                });
        findViewById(R.id.btnAdd).setOnClickListener(view -> {
            Intent intent = new Intent(this, CreateProductActivity.class);
            this.startActivity(intent);
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
