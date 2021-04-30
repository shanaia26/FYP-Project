package com.example.fyp_project.ViewHolder;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fyp_project.Interface.ItemClickListener;
import com.example.fyp_project.R;

public class OrderHistoryViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
    public TextView historyOrderID;
    public TextView historyOrderDate;
    public TextView historyOrderName;
    public TextView historyOrderPhone;
    public TextView historyShipmentStatus;
    public Button historyProductsButton;

    private ItemClickListener itemClickListener;

    public OrderHistoryViewHolder(@NonNull View itemView) {
        super(itemView);

        historyOrderID = itemView.findViewById(R.id.history_order_id);
        historyOrderDate = itemView.findViewById(R.id.history_order_date);
        historyOrderName = itemView.findViewById(R.id.history_order_name);
        historyOrderPhone = itemView.findViewById(R.id.history_order_phone);
        historyShipmentStatus = itemView.findViewById(R.id.history_order_status);
        historyProductsButton = itemView.findViewById(R.id.history_products_button);
    }

    @Override
    public void onClick(View view){
        itemClickListener.onClick(view, getAdapterPosition(), false);
    }

    public void setItemClickListener(ItemClickListener itemClickListener){
        this.itemClickListener = itemClickListener;
    }
}
