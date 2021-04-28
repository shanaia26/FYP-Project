package com.example.fyp_project.ViewHolder;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fyp_project.Interface.ItemClickListener;
import com.example.fyp_project.R;

public class AdminCustomerEnquiriesViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
    public TextView enquiryStatus;
    public TextView enquiryProductId;
    public TextView enquiryDescription;
    public TextView enquirySize;
    public TextView enquiryComment;
    public Button seeEnquiryButton;

    private ItemClickListener itemClickListener;

    public AdminCustomerEnquiriesViewHolder(@NonNull View itemView) {
        super(itemView);

        enquiryStatus = itemView.findViewById(R.id.admin_enquiries_status);
        enquiryProductId = itemView.findViewById(R.id.admin_enquiries_product_id);
        enquiryDescription = itemView.findViewById(R.id.admin_enquiries_description);
        enquirySize = itemView.findViewById(R.id.admin_enquiries_size);
        enquiryComment = itemView.findViewById(R.id.admin_enquiries_comment);
        seeEnquiryButton = itemView.findViewById(R.id.admin_enquiries_see_enquiry_button);

    }

    @Override
    public void onClick(View view){
        itemClickListener.onClick(view, getAdapterPosition(), false);
    }

    public void setItemClickListener(ItemClickListener itemClickListener){
        this.itemClickListener = itemClickListener;
    }
}

