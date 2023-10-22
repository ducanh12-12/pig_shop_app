package com.example.pig_shop_app.Admin.User;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import com.example.pig_shop_app.R;
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
    //tham chiêú csdl fỉrebase
    private final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();

    // danh sach luu tru thong tin ngươì dùng
    private  final List<AdminItems> myItemsList = new ArrayList<>();

    private String userRole;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_main);

        float_add = findViewById(R.id.float_add);

        final RecyclerView recyclerView = findViewById(R.id.recyclerView);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
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

                //lấy dữ liêụ từ firebase
                for (DataSnapshot admin : snapshot.child("admin").getChildren()) {

                    //Lấy thông tin chi tiêt về người dunfg từ firebase và lưu trữ từng ngươì dùng vào danh sach
                    if (admin.hasChild("name") && admin.hasChild("date") && admin.hasChild("email") && admin.hasChild("phone") && admin.hasChild("role") && admin.hasChild("pass")) {
                        final String getname = admin.child("name").getValue(String.class);
                        final String getdate = admin.child("date").getValue(String.class);
                        final String getemail = admin.child("email").getValue(String.class);
                        final String getphone = admin.child("phone").getValue(String.class);
                        final String getrole = admin.child("role").getValue(String.class);
                        final String getpass = admin.child("pass").getValue(String.class);
                        final String getAdminId = admin.getKey(); // Lấy ID của mục

                        AdminItems myItems = new AdminItems(getAdminId, getname, getdate, getphone, getemail, getrole, getpass);

                        //Thêm dữ liêụ vào danh sach luu tru
                        myItemsList.add(myItems);
                    }
                }
                //user đươc add vào recyclerview
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
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            // Handle the back button click event here.
            onBackPressed(); // This will simulate the Back button press.
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}