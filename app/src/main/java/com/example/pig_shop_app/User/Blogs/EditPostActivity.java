package com.example.pig_shop_app.User.Blogs;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.example.pig_shop_app.R;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class EditPostActivity extends AppCompatActivity {

    public EditText titleEditText;
    public EditText contentEditText;

    public EditText datetEditText;
    public ImageView postImageView;
    public Button captureImageButton;
    public String action;

    public String selectedImagePath;
    private String imagePath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_post);

        titleEditText = findViewById(R.id.title);
        contentEditText = findViewById(R.id.content);
        datetEditText= findViewById(R.id.date);
        captureImageButton = findViewById(R.id.button_select_image);
        postImageView = findViewById(R.id.image_view_post);
        Intent intent = getIntent();
        action = intent.getStringExtra("action");

        if (action.equals("edit")) {
            String title = intent.getStringExtra("title");
            String content = intent.getStringExtra("content");
            String date = intent.getStringExtra("date");
            selectedImagePath = intent.getStringExtra("imagePath");
            titleEditText.setText(title);
            contentEditText.setText(content);
            datetEditText.setText(date);
        } else {
            // Set the current date as the default value
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
            String currentDate = dateFormat.format(Calendar.getInstance().getTime());
            datetEditText.setText(currentDate);
        }
            if (selectedImagePath != null) {
                // Hiển thị hình ảnh đã chọn
                postImageView.setImageURI(Uri.parse(selectedImagePath));
            }
        }


    public void savePost(View view) {
        String title = titleEditText.getText().toString().trim();
        String content = contentEditText.getText().toString().trim();
        String date = datetEditText.getText().toString().trim();
        Intent intent = new Intent(this, BlogList.class);
        startActivity(intent);
        finish();
    }


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

    public static class Adapter extends ArrayAdapter<Post> {
        private Context context;
        private ArrayList<Post> dataList;
        public Adapter(Context context, ArrayList<Post> dataList) {
            super(context, R.layout.item_post, dataList);
            this.context = context;
            this.dataList = dataList;
        }


    }
}
