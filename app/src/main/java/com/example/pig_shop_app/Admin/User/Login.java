package com.example.pig_shop_app.Admin.User;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.pig_shop_app.Admin.AdminActivity;
import com.example.pig_shop_app.R;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Login extends AppCompatActivity {

    //tạo kết nối đến Realtime Database qua đường dẫn
    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://pig-shop-default-rtdb.asia-southeast1.firebasedatabase.app/");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        // lấy dữ liệu từ EditText gán vào các biến
        final EditText email = findViewById(R.id.edt_email);
        final EditText pass = findViewById(R.id.edt_pass);
        final Button btn_login = findViewById(R.id.btn_login);

        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String emailtxt = email.getText().toString();
                final String passtxt = pass.getText().toString();

                // Kiểm tra báo rỗng
                if (emailtxt.isEmpty() || passtxt.isEmpty()) {
                    Toast.makeText(Login.this, "Điền đầy đủ email và mật khẩu!", Toast.LENGTH_SHORT).show();
                } else {
                    databaseReference.child("admin").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            // Kiểm tra email có tồn tại trong database không
                            boolean emailFound = false;
                            String userRole = "";

                            for (DataSnapshot userSnapshot : snapshot.getChildren()) {
                                String userEmail = userSnapshot.child("email").getValue(String.class);

                                if (userEmail != null && userEmail.equals(emailtxt)) {
                                    emailFound = true;

                                    // Lấy mật khẩu từ Firebase và so sánh nó với mật khẩu người dùng nhập
                                    String getpass = userSnapshot.child("pass").getValue(String.class);
                                    userRole = userSnapshot.child("role").getValue(String.class);

                                    if (getpass != null && getpass.equals(passtxt)) {
                                        Toast.makeText(Login.this, "Đăng nhập thành công!", Toast.LENGTH_SHORT).show();
                                            startActivity(new Intent(Login.this, AdminActivity.class));
                                            saveUserRole(userRole); // Lưu userRole vào SharedPreferences
                                        finish();
                                    } else {
                                        Toast.makeText(Login.this, "Sai mật khẩu", Toast.LENGTH_SHORT).show();
                                    }

                                    break; // Dừng vòng lặp nếu tìm thấy email
                                }
                            }

                            if (!emailFound) {
                                Toast.makeText(Login.this, "Sai Email!", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }
            }
        });
    }
    // Lưu userRole vào SharedPreferences
    private void saveUserRole(String userRole) {
        SharedPreferences preferences = getSharedPreferences("user_info", MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("userRole", userRole);
        editor.apply();
    }
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            // Handle the back button click event here.
            onBackPressed(); // This will simulate the Back button press.
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
//    // Mở hoạt động tùy thuộc vào userRole
//    private void openRelevantActivity(Context context, String userRole) {
//
//        startActivity(new Intent(context, AdminMain.class));
//        finish();
//    }
}
