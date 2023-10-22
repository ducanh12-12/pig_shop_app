package com.example.pig_shop_app.Admin.Cart;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pig_shop_app.Cart;
import com.example.pig_shop_app.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class AdminCartAdapter extends RecyclerView.Adapter<AdminCartAdapter.ProductViewHolder> {
    public interface OnProductClickListener {
        void onProductClick(int position);
    }
    private ArrayList<Cart> cartList;
    private Context context;
    private OnProductClickListener clickListener;

    public AdminCartAdapter(ArrayList<Cart> cartList, Context context) {
        this.cartList = cartList;
        this.context = context;
    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_cart, parent, false);
        return new ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {
        Cart cart = cartList.get(position);
        holder.productNameTextView.setText(cart.getProduct_id());
        holder.fullNameTextView.setText(cart.getFullName());
        holder.phoneNumberTextView.setText(cart.getPhoneNumber());
        holder.addressTextView.setText(cart.getAddress());
        holder.deleteButton.setOnClickListener(view -> {
            showDeleteConfirmationDialog(context, cart.getKey());
        });
    }
    private  void  showToast(String message) {
        Toast.makeText(context,message,Toast.LENGTH_SHORT).show();
    }
    @Override
    public int getItemCount() {
        return cartList.size();
    }

    public static class ProductViewHolder extends RecyclerView.ViewHolder {
        public TextView productNameTextView;
        public TextView fullNameTextView;
        public TextView phoneNumberTextView;
        public TextView addressTextView;
        public Button deleteButton;

        public ProductViewHolder(@NonNull View itemView) {
            super(itemView);
            fullNameTextView = itemView.findViewById(R.id.full_name);
            productNameTextView = itemView.findViewById(R.id.product);
            phoneNumberTextView = itemView.findViewById(R.id.phone_number);
            addressTextView = itemView.findViewById(R.id.address);
            deleteButton = itemView.findViewById(R.id.btn_del);
        };

    }
    public void showDeleteConfirmationDialog(final Context context, final String cartId) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
        alertDialogBuilder.setTitle("Xác nhận xoá");
        alertDialogBuilder.setMessage("Bạn có chắc chắn muốn xoá đơn hàng này?");
        alertDialogBuilder.setPositiveButton("Xoá", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                deleteCart(cartId);
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
    public void deleteCart(String cartId) {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("carts");
        if (cartId != null && !cartId.isEmpty()) {
            databaseReference.child(cartId).removeValue()
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toast.makeText(context, "Xoá đơn hàng thành công", Toast.LENGTH_SHORT).show();

                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(context,"Xoá đơn hàng thất bại", Toast.LENGTH_SHORT).show();
                        }
                    });
        }
    }
}