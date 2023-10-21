package com.example.pig_shop_app.Admin.Product;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pig_shop_app.R;
import com.squareup.picasso.Picasso;
import android.content.Intent;

import java.util.ArrayList;
import java.util.List;

public class AdminProductAdapter extends RecyclerView.Adapter<AdminProductAdapter.ProductViewHolder> {
    public interface OnProductClickListener {
        void onProductClick(int position);
    }
    private ArrayList<Product> productList;
    private Context context;
    private OnProductClickListener clickListener;

    public AdminProductAdapter(ArrayList<Product> productList, Context context) {
        this.productList = productList;
        this.context = context;
    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_pig, parent, false);
        return new ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {
        Product product = productList.get(position);
        holder.productNameTextView.setText(product.getTitle());
        holder.productPriceTextView.setText("Price: $" + product.getPrice());
        Picasso.get().load(product.getAvatar()).into(holder.productImageView);
        holder.productImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, CreateProductActivity.class);
                intent.putExtra("product_id", product.getKey());
                context.startActivity(intent);
            }
        });
    }
    private  void  showToast(String message) {
        Toast.makeText(context,message,Toast.LENGTH_SHORT).show();
    }
    @Override
    public int getItemCount() {
        return productList.size();
    }

    public static class ProductViewHolder extends RecyclerView.ViewHolder {
        public ImageView productImageView;
        public TextView productNameTextView;
        public TextView productPriceTextView;

        public ProductViewHolder(@NonNull View itemView) {
            super(itemView);
            productImageView = itemView.findViewById(R.id.product_image);
            productNameTextView = itemView.findViewById(R.id.product_name);
            productPriceTextView = itemView.findViewById(R.id.product_price);
        };

    }
}