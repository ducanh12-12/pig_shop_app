package com.example.pig_shop_app.User.Blogs;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.pig_shop_app.R;

public class PostDetail extends AppCompatActivity {
    public TextView titleTextView;
    public TextView contentTextView;
    public ImageView postImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.item_post);
        postImageView=findViewById(R.id.imageView);
        titleTextView = findViewById(R.id.textViewTitle);
        contentTextView = findViewById(R.id.textViewTitle);

        Intent intent = getIntent();
        String title = intent.getStringExtra("title");
        String content = intent.getStringExtra("content");
        String image = intent.getStringExtra("image");

        if (title != null && content != null) {
            titleTextView.setText(title);
            contentTextView.setText(content);

            // Hiển thị hình ảnh (nếu cần)
//             imageView.setImageResource(R.drawable.image_name);
        }
    }
}
