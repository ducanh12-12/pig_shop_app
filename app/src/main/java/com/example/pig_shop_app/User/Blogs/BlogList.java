package com.example.pig_shop_app.User.Blogs;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.pig_shop_app.MainActivity;
import com.example.pig_shop_app.R;
import com.example.pig_shop_app.User.Product.Product;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BlogList extends AppCompatActivity {
    public DatabaseReference databaseReference;
    public ListView postListView;
    public ArrayList<Post> postList;
    public BlogAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_blog);
        databaseReference = FirebaseDatabase.getInstance().getReference("blogs");
        postList = new ArrayList<Post>();
        postListView = findViewById(R.id.post_list);
        adapter = new BlogAdapter(this, postList);
        postListView.setAdapter(adapter);

        findViewById(R.id.add_button).setOnClickListener(view -> {
            Intent intent = new Intent(this, EditPostActivity.class);
            intent.putExtra("action", "add");
            startActivity(intent);
        });
        FirebaseDatabase.getInstance().getReference("blogs")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        postList.clear();
                        for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                            Post post = postSnapshot.getValue(Post.class);
                            post.setId(postSnapshot.getKey());
                            postList.add(post);
                        }
                        adapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        // Xử lý lỗi
                    }
                });
        postListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Post selectedPost = postList.get(position);
                Intent intent = new Intent(BlogList.this, PostDetail.class);
                intent.putExtra("postId", selectedPost.getId());
                startActivity(intent);
            }

        });
        postListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                // Xử lý khi người dùng nhấn giữ lâu trên một bài viết
                Post selectedPost = postList.get(position);
                showEditDialog(selectedPost);
                return true;
            }
        });
    }
    // Phương thức để hiển thị hộp thoại sửa bài viết
    public void showEditDialog(final Post post) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        View dialogView = getLayoutInflater().inflate(R.layout.activity_edit_post, null);
        dialogBuilder.setView(dialogView);

        final EditText editTextTitle = dialogView.findViewById(R.id.title);
        final EditText editTextContent = dialogView.findViewById(R.id.content);
        final EditText editTextDate = dialogView.findViewById(R.id.date);
        final EditText editTextImage = dialogView.findViewById(R.id.image_view_post);
        Button buttonUpdate = dialogView.findViewById(R.id.edit_button);
        Button buttonDelete = dialogView.findViewById(R.id.delete_button);

        editTextTitle.setText(post.getTitle());
        editTextContent.setText(post.getContent());
        editTextDate.setText(post.getDate());
        editTextImage.setText(post.getImage());

        final AlertDialog alertDialog = dialogBuilder.create();
        alertDialog.show();

        // Xử lý khi người dùng nhấn nút Cập nhật
        buttonUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String updatedTitle = editTextTitle.getText().toString();
                String updatedContent = editTextContent.getText().toString();
                String updatedDate = editTextDate.getText().toString();
                String updatedImage = editTextImage.getText().toString();

                updatePost(post.getId(), updatedTitle, updatedContent, updatedDate, updatedImage);
                alertDialog.dismiss();
            }
        });

        // Xử lý khi người dùng nhấn nút Xóa
        buttonDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deletePost(post.getId());
                alertDialog.dismiss();
            }
        });
    }

    //...

    public void updatePost(String postId, String updatedTitle, String updatedContent, String updatedDate, String updatedImage) {
        // Tạo một HashMap chứa thông tin đã cập nhật của bài viết
        HashMap<String, Object> updatedValues = new HashMap<>();
        updatedValues.put("title", updatedTitle);
        updatedValues.put("content", updatedContent);
        updatedValues.put("date", updatedDate);
        updatedValues.put("image", updatedImage);
        // Cập nhật thông tin bài viết trong FirebaseDatabase
        databaseReference.child("blogs").child(postId).setValue(updatedValues)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(BlogList.this, "Cập nhật bài viết thành công", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(BlogList.this, "Cập nhật bài viết thất bại", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    public void deletePost(String postId) {
        // Xóa bài viết khỏi FirebaseDatabase
        databaseReference.child("blogs").child(postId).removeValue()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(BlogList.this, "Xóa bài viết thành công", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(BlogList.this, "Xóa bài viết thất bại", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}




