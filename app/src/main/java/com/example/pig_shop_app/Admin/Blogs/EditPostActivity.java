package com.example.pig_shop_app.Admin.Blogs;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import android.content.DialogInterface;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.example.pig_shop_app.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import com.google.firebase.database.FirebaseDatabase;

public class EditPostActivity extends AppCompatActivity {

    public EditText titleEditText;
    public EditText contentEditText;
    public DatabaseReference databaseReference;
    public EditText datetEditText;
    public ImageView postImageView;
    public Button captureImageButton;
    public String action;

    public String selectedImagePath;
    private String imagePath;
    private String title,content,date,id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_post);
        titleEditText = findViewById(R.id.title);
        contentEditText = findViewById(R.id.content);
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("blogs");
        datetEditText= findViewById(R.id.date);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        captureImageButton = findViewById(R.id.button_select_image);
        postImageView = findViewById(R.id.image_view_post);
        Intent intent = getIntent();
        action = intent.getStringExtra("action");

        if (action.equals("edit")) {
            Button editButton = findViewById(R.id.edit_button);
            editButton.setText("Lưu bài viết");
             title = intent.getStringExtra("title");
             id = intent.getStringExtra("blog_id");
             content = intent.getStringExtra("content");
             date = intent.getStringExtra("date");
            selectedImagePath = intent.getStringExtra("imagePath");
            titleEditText.setText(title);
            contentEditText.setText(content);
            datetEditText.setText(date);
        } else {
            findViewById(R.id.delete_button).setVisibility(View.INVISIBLE);
            Button editButton = findViewById(R.id.edit_button);
            editButton.setText("Thêm bài viết");
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
            String currentDate = dateFormat.format(Calendar.getInstance().getTime());
            datetEditText.setText(currentDate);
        }
            if (selectedImagePath != null) {
                // Hiển thị hình ảnh đã chọn
                postImageView.setImageURI(Uri.parse(selectedImagePath));
            }
        findViewById(R.id.edit_button).setOnClickListener(view -> {
            if (titleEditText.getText().toString().isEmpty() || contentEditText.getText().toString().isEmpty() || datetEditText.getText().toString().isEmpty()) {
                Toast.makeText(EditPostActivity.this, "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show();
            } else {
                if (action.equals("edit")) {
                    title =titleEditText.getText().toString();
                    content =contentEditText.getText().toString();
                    date = datetEditText.getText().toString();
                    updatePost(id,title,content,date,"", "update");
                } else  {
                    title =titleEditText.getText().toString();
                    content =contentEditText.getText().toString();
                    date = datetEditText.getText().toString();
                    updatePost(id,title,content,date,"", "add");
                }
            }
        });
            findViewById(R.id.delete_button).setOnClickListener(view -> {
                showDeleteConfirmationDialog(this, id);
            });
        }



    public void updatePost(String postId, String updatedTitle, String updatedContent, String updatedDate, String updatedImage, String action) {
        // Tạo một HashMap chứa thông tin đã cập nhật của bài viết
        if (action == "add") {
            postId = databaseReference.push().getKey();
        }
        Post post = new Post(postId,updatedTitle,updatedContent,updatedDate,updatedImage);

        // Cập nhật thông tin bài viết trong FirebaseDatabase
        databaseReference.child(postId).setValue(post)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(EditPostActivity.this, action == "update" ? "Cập nhật bài viết thành công" : "Tạo mới bài viết thành công", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(EditPostActivity.this,action == "update" ? "Cập nhật bài viết thất bại" : "Tạo mới bài viết thất bại", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                });
    }
    public void showDeleteConfirmationDialog(final Context context, final String blogId) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
        alertDialogBuilder.setTitle("Xác nhận xoá");
        alertDialogBuilder.setMessage("Bạn có chắc chắn muốn xoá bài viết này?");
        alertDialogBuilder.setPositiveButton("Xoá", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                deleteBlog(blogId);
            }
        });
        alertDialogBuilder.setNegativeButton("Hủy", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        alertDialogBuilder.create().show();
    }

    public void deleteBlog(String blogId) {
        if (blogId != null && !blogId.isEmpty()) {
            databaseReference.child(blogId).removeValue()
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toast.makeText(EditPostActivity.this, "Xoá bài viết thành công", Toast.LENGTH_SHORT).show();
                            finish();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(EditPostActivity.this,"Xoá bài viết thất bại", Toast.LENGTH_SHORT).show();
                            finish();
                        }
                    });
        }
    }  FirebaseDatabase.getInstance().getReference("blogs")
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            // Nhận hình ảnh đã chụp
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");

            // Lưu hình ảnh trong điện thoại và lấy đường dẫn
            selectedImagePath = saveImageToGallery(imageBitmap);

            // Hiển thị hình ảnh đã chọn
            postImageView.setImageBitmap(imageBitmap);
        }
    }

    private String saveImageToGallery(Bitmap bitmap) {
        // Lưu hình ảnh vào bộ nhớ và trả về đường dẫn của hình ảnh đã lưu
        // Hãy xem thêm tài liệu về lưu hình ảnh vào bộ nhớ trong Android

        return imagePath;
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
