package com.example.fyp_project.ViewHolder;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fyp_project.Interface.ItemClickListener;
import com.example.fyp_project.R;

public class CustomerEnquiriesHistoryViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    public TextView enquiriesStatus;
    public TextView enquiriesProductID;
    public TextView enquiriesName;
    public TextView enquiriesDescription;
    public TextView enquiriesSize;
    public TextView enquiriesComment;
    public TextView enquiriesPrice;
    public Button enquiriesOrderButton;
    public Button enquiriesCancelButton;

    private ItemClickListener itemClickListener;

    public CustomerEnquiriesHistoryViewHolder(@NonNull View itemView) {
        super(itemView);

        enquiriesStatus = itemView.findViewById(R.id.customer_enquiries_status);
        enquiriesProductID = itemView.findViewById(R.id.customer_enquiries_product_id);
        enquiriesName = itemView.findViewById(R.id.customer_enquiries_name);
        enquiriesDescription = itemView.findViewById(R.id.customer_enquiries_description);
        enquiriesSize = itemView.findViewById(R.id.customer_enquiries_size);
        enquiriesComment = itemView.findViewById(R.id.customer_enquiries_comment);
        enquiriesPrice = itemView.findViewById(R.id.customer_enquiries_price);
        enquiriesOrderButton = itemView.findViewById(R.id.customer_enquiries_order_button);
        enquiriesCancelButton = itemView.findViewById(R.id.customer_enquiries_cancel_button);

    }

    @Override
    public void onClick(View view){
        itemClickListener.onClick(view, getAdapterPosition(), false);
    }

    public void setItemClickListener(ItemClickListener itemClickListener){
        this.itemClickListener = itemClickListener;
    }
}
