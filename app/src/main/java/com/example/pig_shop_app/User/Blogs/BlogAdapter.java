package com.example.pig_shop_app.User.Blogs;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.example.pig_shop_app.R;
import com.example.pig_shop_app.User.Product.Product;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

public class BlogAdapter extends ArrayAdapter<Post> {
    private Context context;
    private ArrayList<Post> dataList;
    public BlogAdapter(Context context, ArrayList<Post> dataList) {
        super(context, R.layout.item_post, dataList);
        this.context = context;
        this.dataList = dataList;
    }
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        ViewHolder viewHolder;

        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(context);
            convertView = inflater.inflate(R.layout.item_post, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.titleText = convertView.findViewById(R.id.textViewTitle);
            viewHolder.contentText = convertView.findViewById(R.id.textViewContent);
            viewHolder.datetext = convertView.findViewById(R.id.tvDate);
            viewHolder.imageView = convertView.findViewById(R.id.imageView);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        Post post = dataList.get(position);
        viewHolder.titleText.setText(post.getTitle());
        viewHolder.contentText.setText(post.getContent());
        viewHolder.datetext.setText(post.getDate());
        viewHolder.imageView.setImageResource(R.drawable.lon);
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Tạo một Intent để chuyển đến activity mới
                Intent intent = new Intent(context, EditPostActivity.class);
                intent.putExtra("blog_id", post.getId());
                intent.putExtra("title", post.getTitle());
                intent.putExtra("content", post.getContent());
                intent.putExtra("date", post.getDate());
                intent.putExtra("imageResource", R.drawable.lon);
                intent.putExtra("action", "edit");
                context.startActivity(intent);
            }
        });
        return convertView;
    }
    private static class ViewHolder {
        TextView titleText;
        TextView contentText;
        TextView datetext;
        ImageView imageView;
    }
}
