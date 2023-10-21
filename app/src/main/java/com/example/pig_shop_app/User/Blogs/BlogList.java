package com.example.pig_shop_app.User.Blogs;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
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
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
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




