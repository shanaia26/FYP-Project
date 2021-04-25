package com.example.fyp_project.ViewHolder;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fyp_project.Interface.ItemClickListener;
import com.example.fyp_project.R;

public class AdminUserOrdersViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
    public TextView userOrderName;
    public TextView userOrderPhone;
    public TextView userOrderEmail;
    public Button showAllOrdersButton;
    public Button showEnquiriesButton;

    private ItemClickListener itemClickListener;

    public AdminUserOrdersViewHolder(@NonNull View itemView) {
        super(itemView);

        userOrderName = itemView.findViewById(R.id.admin_user_order_name);
        userOrderPhone = itemView.findViewById(R.id.admin_user_order_phone);
        userOrderEmail = itemView.findViewById(R.id.admin_user_order_email);
        showAllOrdersButton = itemView.findViewById(R.id.admin_user_order_show_orders);
        showEnquiriesButton = itemView.findViewById(R.id.admin_user_order_show_enquiries);

    }

    @Override
    public void onClick(View view){
        itemClickListener.onClick(view, getAdapterPosition(), false);
    }

    public void setItemClickListener(ItemClickListener itemClickListener){
        this.itemClickListener = itemClickListener;
    }
}
