package com.example.fyp_project.ViewHolder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.fyp_project.Interface.ItemClickListener;
import com.example.fyp_project.R;

public class ProductViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    public TextView productName;
    public TextView productDescription;
    public TextView productPrice;
    public ImageView productImage;

    public ItemClickListener listener;

    public ProductViewHolder(View itemView){
        super(itemView);

        productName = itemView.findViewById(R.id.product_name);
        productDescription = itemView.findViewById(R.id.product_description);
        productPrice = itemView.findViewById(R.id.product_price);
        productImage = itemView.findViewById(R.id.product_image);
    }

    public void setItemClickListener(ItemClickListener listener){
        this.listener = listener;
    }

    @Override
    public void onClick(View view) {
        listener.onClick(view, getAdapterPosition(), false);
    }
}
