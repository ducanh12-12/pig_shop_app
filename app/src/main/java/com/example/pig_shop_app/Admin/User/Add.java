package com.example.pig_shop_app.Admin.User;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class Add extends AppCompatActivity {

    Button btn_add, btn_exit;
    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.com";
    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://pig-shop-default-rtdb.asia-southeast1.firebasedatabase.app/");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        final EditText name = findViewById(R.id.edt_name);
        final EditText date = findViewById(R.id.edt_date);
        final EditText phone = findViewById(R.id.edt_phone);
        final EditText email = findViewById(R.id.edt_email);
        final EditText pass = findViewById(R.id.edt_pass);
        final Button add = findViewById(R.id.btn_add);

        btn_exit = findViewById(R.id.btn_exit);

        // Thêm OnClickListener cho EditText ngày sinh
        date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDatePickerDialog(date);
            }
        });

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // lấy dữ liệu từ EditText gán vào các biến
                final String nametxt = name.getText().toString();
                final String phonetxt = phone.getText().toString();
                final String emailtxt = email.getText().toString();
                final String passtxt = pass.getText().toString();
                final String datetxt = date.getText().toString();

                //check điều kiện
                if(nametxt.isEmpty() || phonetxt.isEmpty() || emailtxt.isEmpty() || passtxt.isEmpty() || datetxt.isEmpty()){
                    Toast.makeText(Add.this, "Điền đầy đủ thông tin", Toast.LENGTH_SHORT).show();
                }else if (passtxt.isEmpty() || passtxt.length()<6 || passtxt.length()>16) {
                    pass.setError("Mật khẩu tối thiểu 6 ký tự, tối đa 16 ký tự");
                }else if (!emailtxt.matches(emailPattern))
                {
                    email.setError("Nhập đúng định dạng email!");
                } else if (phonetxt.isEmpty() || phone.length() != 10) {
                    phone.setError("Số điện thoại phải gồm 10 chữ số");
                }
                else{
                    //
                    databaseReference.child("admin").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            boolean phoneExists = false;
                            boolean emailExists = false;

                            for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                String dbPhone = dataSnapshot.child("phone").getValue(String.class);
                                String dbEmail = dataSnapshot.child("email").getValue(String.class);

                                if (dbPhone != null && dbPhone.equals(phonetxt)) {
                                    phoneExists = true;
                                    break;
                                }

                                if (dbEmail != null && dbEmail.equals(emailtxt)) {
                                    emailExists = true;
                                    break;
                                }
                            }

                            if (phoneExists) {
                                phone.setError("Số điện thoại đã tồn tại");
                            } else if (emailExists) {
                                email.setError("Email đã tồn tại");
                            } else {
                                // Thêm thông tin vào Firebase với key tự động tạo ngẫu nhiên
                                DatabaseReference adminRef = databaseReference.child("admin").push();
                                adminRef.child("name").setValue(nametxt);
                                adminRef.child("phone").setValue(phonetxt);
                                adminRef.child("email").setValue(emailtxt);
                                adminRef.child("pass").setValue(passtxt);
                                adminRef.child("date").setValue(datetxt);
                                adminRef.child("role").setValue("1");

                                Toast.makeText(Add.this, "Thêm thành công!", Toast.LENGTH_SHORT).show();
                                finish();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });

                }
            }
        });

        btn_exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Add.this, AdminMain.class));
            }
        });
    }

    // Thêm phương thức để hiển thị DatePickerDialog
    private void showDatePickerDialog(final EditText dateEditText) {
        final Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(Add.this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                // Lưu ngày sinh vào EditText
                calendar.set(i, i1, i2);
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
                dateEditText.setText(simpleDateFormat.format(calendar.getTime()));
            }
        }, year, month, day);

        datePickerDialog.show();
    }
}
