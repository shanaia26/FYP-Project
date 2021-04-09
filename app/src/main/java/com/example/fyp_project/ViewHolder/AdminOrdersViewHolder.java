package com.example.fyp_project.ViewHolder;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fyp_project.Interface.ItemClickListener;
import com.example.fyp_project.R;

public class AdminOrdersViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
    public TextView userName;
    public TextView userPhone;
    public TextView userTotalPrice;
    public TextView userAddress;
    public TextView userOrderID;
    public TextView userStatus;
    public Button showProductsButton;

    private ItemClickListener itemClickListener;

    public AdminOrdersViewHolder(@NonNull View itemView) {
        super(itemView);

        userName = itemView.findViewById(R.id.order_user_name);
        userPhone = itemView.findViewById(R.id.order_phone_number);
        userTotalPrice = itemView.findViewById(R.id.order_total_price);
        userAddress = itemView.findViewById(R.id.order_address_city);
        userOrderID = itemView.findViewById(R.id.order_order_id);
        userStatus = itemView.findViewById(R.id.order_status);
        showProductsButton = itemView.findViewById(R.id.show_products_button);

    }

    @Override
    public void onClick(View view){
        itemClickListener.onClick(view, getAdapterPosition(), false);
    }

    public void setItemClickListener(ItemClickListener itemClickListener){
        this.itemClickListener = itemClickListener;
    }
}
