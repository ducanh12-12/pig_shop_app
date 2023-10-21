package com.example.pig_shop_app.Admin.User;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class AdminMain extends AppCompatActivity {

    FloatingActionButton float_add;
    private final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();

    private  final List<AdminItems> myItemsList = new ArrayList<>();

    private String userRole;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_main);

        float_add = findViewById(R.id.float_add);

        final RecyclerView recyclerView = findViewById(R.id.recyclerView);

        //set size sao cho recyclerview vừa với tất cả dữ liệu có trong nó
        recyclerView.setHasFixedSize(true);

        recyclerView.setLayoutManager(new LinearLayoutManager(AdminMain.this));

        // Lấy giá trị userRole từ SharedPreferences
        SharedPreferences preferences = getSharedPreferences("user_info", MODE_PRIVATE);
        userRole = preferences.getString("userRole", "");

        // Ẩn nút float_add nếu userRole = "1"
        if (userRole.equals("1")) {
            float_add.setVisibility(View.GONE);
        } else {
            float_add.setVisibility(View.VISIBLE);
        }

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                myItemsList.clear();

                for (DataSnapshot admin : snapshot.child("admin").getChildren()) {

                    if (admin.hasChild("name") && admin.hasChild("date") && admin.hasChild("email") && admin.hasChild("phone") && admin.hasChild("role") && admin.hasChild("pass")) {
                        final String getname = admin.child("name").getValue(String.class);
                        final String getdate = admin.child("date").getValue(String.class);
                        final String getemail = admin.child("email").getValue(String.class);
                        final String getphone = admin.child("phone").getValue(String.class);
                        final String getrole = admin.child("role").getValue(String.class);
                        final String getpass = admin.child("pass").getValue(String.class);
                        final String getAdminId = admin.getKey(); // Lấy ID của mục

                        AdminItems myItems = new AdminItems(getAdminId, getname, getdate, getphone, getemail, getrole, getpass);

                        myItemsList.add(myItems);
                    }
                }
                recyclerView.setAdapter(new AdminAdapter(myItemsList, AdminMain.this));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

        float_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(AdminMain.this, Add.class));
            }
        });
    }
}