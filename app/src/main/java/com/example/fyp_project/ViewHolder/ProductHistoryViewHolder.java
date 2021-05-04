package com.example.fyp_project.ViewHolder;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fyp_project.Interface.ItemClickListener;
import com.example.fyp_project.R;
import com.github.chrisbanes.photoview.PhotoView;

public class ProductHistoryViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

    public TextView productHistoryOrderID;
    public TextView productHistoryProductName;
    public TextView productHistoryPrice;
    public TextView productHistoryShipmentStatus;
    public TextView productHistorySize;
    public TextView productHistoryQuantity;


    private ItemClickListener itemClickListener;

    public ProductHistoryViewHolder(@NonNull View itemView) {
        super(itemView);

        productHistoryOrderID = itemView.findViewById(R.id.product_history_order_id);
        productHistoryProductName = itemView.findViewById(R.id.product_history_product_name);
        productHistoryPrice = itemView.findViewById(R.id.product_history_product_price);
        productHistoryShipmentStatus = itemView.findViewById(R.id.product_history_product_shipment_status);
        productHistorySize = itemView.findViewById(R.id.product_history_product_size);
        productHistoryQuantity = itemView.findViewById(R.id.product_history_quantity);
    }

    @Override
    public void onClick(View view){
        itemClickListener.onClick(view, getAdapterPosition(), false);
    }

    public void setItemClickListener(ItemClickListener itemClickListener){
        this.itemClickListener = itemClickListener;
    }
}