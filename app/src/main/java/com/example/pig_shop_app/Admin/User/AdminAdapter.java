package com.example.pig_shop_app.Admin.User;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pig_shop_app.Admin.Product.CreateProductActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.ViewHolder;
import com.example.pig_shop_app.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AdminAdapter extends RecyclerView.Adapter<AdminAdapter.MyViewHolder> {

    private final List<AdminItems> items;
    private final Context context;
    private DatabaseReference databaseReference;
    private String userRole;
    private String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.com";
    DatabaseReference databaseReference1 = FirebaseDatabase.getInstance().getReferenceFromUrl("https://pig-shop-default-rtdb.asia-southeast1.firebasedatabase.app/");

    public AdminAdapter(List<AdminItems> items, Context context) {
        this.items = items;
        this.context = context;
        this.databaseReference = FirebaseDatabase.getInstance().getReference().child("admin");
        SharedPreferences preferences = context.getSharedPreferences("user_info", Context.MODE_PRIVATE);
        userRole = preferences.getString("userRole", "");
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_admin_adapter_layout, null));
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, final int position) {
        final AdminItems myItems = items.get(position);

        holder.name.setText(myItems.getName());
        holder.date.setText(myItems.getDate());
        holder.email.setText(myItems.getEmail());
        holder.phone.setText(myItems.getPhone());

        if (userRole.equals("1")) {
            // Ẩn nút btn_edit và btn_del nếu userRole = "1"
            holder.btnedit.setVisibility(View.GONE);
            holder.btndel.setVisibility(View.GONE);
        } else {
            holder.btnedit.setVisibility(View.VISIBLE);
            holder.btndel.setVisibility(View.VISIBLE);
            if (myItems.getRole().equals("0")) {
                holder.btndel.setVisibility(View.GONE);
            } else {
                holder.btndel.setEnabled(true);
            }
        }

        final int itemPosition = holder.getAdapterPosition();

        holder.btndel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Xóa tài khoản");
                builder.setMessage(" Bạn có chắc chắn muốn xóa không?");

                builder.setPositiveButton("Có", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        // Xác nhận xóa
                        deleteAdminItem(itemPosition);
                    }
                });
                builder.setNegativeButton("Không", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });

                builder.show();
            }
        });

        holder.btnedit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final DialogPlus dialogPlus = DialogPlus.newDialog(context)
                        .setContentHolder(new ViewHolder(R.layout.activity_edit))
                        .setExpanded(true, 1750)
                        .create();

                View myview = dialogPlus.getHolderView();
                EditText editname = myview.findViewById(R.id.edt_editname);
                EditText editdate = myview.findViewById(R.id.edt_editdate);
                EditText editphone = myview.findViewById(R.id.edt_editphone);
                EditText editemail = myview.findViewById(R.id.edt_editemail);
                EditText editpass = myview.findViewById(R.id.edt_editpass);
                Button save = myview.findViewById(R.id.btn_save);

                editname.setText(myItems.getName());
                editdate.setText(myItems.getDate());
                editphone.setText(myItems.getPhone());
                editemail.setText(myItems.getEmail());
                editpass.setText(myItems.getPass());

                editdate.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        showDatePickerDialog(editdate);
                    }
                });

                dialogPlus.show();

                final String adminIdToUpdate = myItems.getAdminId();

                save.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        // Lấy dữ liệu từ các trường nhập liệu
                        String editName = editname.getText().toString();
                        String editDate = editdate.getText().toString();
                        String editPhone = editphone.getText().toString();
                        String editEmail = editemail.getText().toString();
                        String editPass = editpass.getText().toString();

                        if (editName.isEmpty() || editDate.isEmpty() || editPhone.isEmpty() || editEmail.isEmpty()) {
                            Toast.makeText(context, "Điền đầy đủ thông tin", Toast.LENGTH_SHORT).show();
                        } else if (editPass.isEmpty() || editPass.length() < 6 || editPass.length() > 16) {
                            editpass.setError("Mật khẩu tối thiểu 6 ký tự, tối đa 16 ký tự");
                        } else if (!editEmail.matches(emailPattern)) {
                            editemail.setError("Nhập đúng định dạng email!");
                        } else if (editPhone.isEmpty() || editPhone.length() != 10) {
                            editphone.setError("Số điện thoại phải gồm 10 chữ số");
                        } else {
                            // Thực hiện kiểm tra trùng lặp
                            databaseReference1.child("admin").addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    boolean phoneExists = false;
                                    boolean emailExists = false;

                                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                        String dbPhone = dataSnapshot.child("phone").getValue(String.class);
                                        String dbEmail = dataSnapshot.child("email").getValue(String.class);

                                        // Kiểm tra trùng lặp số điện thoại
                                        if (dbPhone != null && dbPhone.equals(editPhone) && !dataSnapshot.getKey().equals(myItems.getAdminId())) {
                                            phoneExists = true;
                                            break;
                                        }

                                        // Kiểm tra trùng lặp địa chỉ email
                                        if (dbEmail != null && dbEmail.equals(editEmail) && !dataSnapshot.getKey().equals(myItems.getAdminId())) {
                                            emailExists = true;
                                            break;
                                        }
                                    }

                                    if (phoneExists) {
                                        editphone.setError("Số điện thoại đã tồn tại");
                                    } else if (emailExists) {
                                        editemail.setError("Email đã tồn tại");
                                    } else {
                                        // Tiến hành cập nhật thông tin vào Firebase
                                        Map<String, Object> map = new HashMap<>();
                                        map.put("name", editName);
                                        map.put("date", editDate);
                                        map.put("phone", editPhone);
                                        map.put("email", editEmail);
                                        map.put("pass", editPass);

                                        // Lưu các thông tin đã cập nhật vào Firebase
                                        FirebaseDatabase.getInstance().getReference()
                                                .child("admin")
                                                .child(myItems.getAdminId())
                                                .updateChildren(map)
                                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                    @Override
                                                    public void onSuccess(Void unused) {
                                                        dialogPlus.dismiss();
                                                        Toast.makeText(context, "Cập nhật thành công", Toast.LENGTH_SHORT).show();
                                                    }
                                                })
                                                .addOnFailureListener(new OnFailureListener() {
                                                    @Override
                                                    public void onFailure(@NonNull Exception e) {
                                                        dialogPlus.dismiss();
                                                        Toast.makeText(context, "Cập nhật thất bại", Toast.LENGTH_SHORT).show();
                                                    }
                                                });
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {
                                    // Xử lý lỗi nếu cần
                                }
                            });
                        }
                    }
                });

            }
        });
    }
    private void showDatePickerDialog(final EditText dateEditText) {
        //khi click vào sẽ hiên ra ngay hien tai
        final Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        // Sử dụng một biến final để truy cập AdminAdapter.this
        final AdminAdapter adminAdapter = this;

        DatePickerDialog datePickerDialog = new DatePickerDialog(context, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int i, int i1, int i2) { //truyền vào 3 kiểu năm, tháng, ngày
                // Lưu ngày sinh vào EditText
                calendar.set(i, i1, i2); //set giá trị cho edittext theo ngày mà ta chon
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy"); //Đinh dang ngaỳ/thang/năm
                dateEditText.setText(simpleDateFormat.format(calendar.getTime())); //truyen du  lieu
            }
        }, year, month, day);

        datePickerDialog.show();
    }

    private void deleteAdminItem(final int position) {
        // Lấy ra ID của mục cần xóa trong Firebase
        final String adminId = items.get(position).getAdminId();

        // Thực hiện xóa trên Firebase
        DatabaseReference adminRef = FirebaseDatabase.getInstance().getReferenceFromUrl("https://pig-shop-default-rtdb.asia-southeast1.firebasedatabase.app/")
                .child("admin").child(adminId);

        adminRef.removeValue()
            .addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    Toast.makeText(context, "Xoá sản phẩm thành công", Toast.LENGTH_SHORT).show();
                }
            })
                    .addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(context,"Xoá sản phẩm thất bại", Toast.LENGTH_SHORT).show();
                }
            });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    static class MyViewHolder extends RecyclerView.ViewHolder {

        private final TextView name, date, phone, email;
        private final Button btndel, btnedit;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            name = itemView.findViewById(R.id.tv_name);
            date = itemView.findViewById(R.id.tv_date);
            phone = itemView.findViewById(R.id.tv_phone);
            email = itemView.findViewById(R.id.tv_email);
            btndel = itemView.findViewById(R.id.btn_del);
            btnedit = itemView.findViewById(R.id.btn_edit);
        }
    }
}
