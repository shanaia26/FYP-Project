package com.example.fyp_project.ViewHolder;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fyp_project.Interface.ItemClickListener;
import com.example.fyp_project.R;

public class AdminOrderHistoryViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
    public TextView userName;
    public TextView userPhone;
    public TextView userOrderID;
    public TextView userPaymentStatus;
    public TextView userTotalPrice;
    public TextView userShipmentStatus;
    public TextView userAddress;
    public Button showProductsButton;

    private ItemClickListener itemClickListener;

    public AdminOrderHistoryViewHolder(@NonNull View itemView) {
        super(itemView);

        userName = itemView.findViewById(R.id.admin_order_user_name);
        userPhone = itemView.findViewById(R.id.admin_order_phone_number);
        userOrderID = itemView.findViewById(R.id.admin_order_order_id);
        userPaymentStatus = itemView.findViewById(R.id.admin_order_payment_status);
        userTotalPrice = itemView.findViewById(R.id.admin_order_total_amount);
        userShipmentStatus = itemView.findViewById(R.id.admin_order_shipment_status);
        userAddress = itemView.findViewById(R.id.admin_order_address);
        showProductsButton = itemView.findViewById(R.id.admin_show_products_button);

    }

    @Override
    public void onClick(View view){
        itemClickListener.onClick(view, getAdapterPosition(), false);
    }

    public void setItemClickListener(ItemClickListener itemClickListener){
        this.itemClickListener = itemClickListener;
    }
}
