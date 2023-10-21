package com.example.pig_shop_app.User.Blogs;

import android.support.annotation.NonNull;
import android.widget.ArrayAdapter;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class DatabaseHelper  {

    private DatabaseReference databaseReference;

    public DatabaseHelper() {
        databaseReference = FirebaseDatabase.getInstance().getReference().child("Posts");
    }

    public void addPost(String title, String content, String date, String image) {
        String postId = databaseReference.push().getKey();
        Post post = new Post(postId, title, content, date, image);
        databaseReference.child(postId).setValue(post);
    }

    public void updatePost(String postId, String title, String content, String date, String image) {
        Post post = new Post(postId, title, content, date, image);
        databaseReference.child(postId).setValue(post);
    }

    public void deletePost(String postId) {
        databaseReference.child(postId).removeValue();
    }

    public DatabaseReference getDatabaseReference() {
        return databaseReference;
    }

    public ValueEventListener readPosts(final ArrayAdapter<String> adapter) {
        return databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                adapter.clear();
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    Post post = postSnapshot.getValue(Post.class);
                    if (post != null) {
                        adapter.add(post.getTitle());
                    }
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle errors if needed
            }
        });
    }
}