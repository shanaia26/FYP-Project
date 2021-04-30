package com.example.fyp_project.Admin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.fyp_project.Model.Cart;
import com.example.fyp_project.R;
import com.example.fyp_project.ViewHolder.CartViewHolder;
import com.example.fyp_project.ViewHolder.ProductHistoryViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AdminProductHistoryActivity extends AppCompatActivity {
    private RecyclerView recyclerProducts;
    private RecyclerView.LayoutManager layoutManager;

    private DatabaseReference cartListReference;

    private String userID = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_product_history);

        userID = getIntent().getStringExtra("userID");

        recyclerProducts = findViewById(R.id.recycler_products);
        recyclerProducts.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerProducts.setLayoutManager(layoutManager);


        cartListReference = FirebaseDatabase.getInstance().getReference()
                .child("Cart List")
                .child("Admin View")
                .child(userID)
                .child("Products");
    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseRecyclerOptions<Cart> options =
                new FirebaseRecyclerOptions.Builder<Cart>()
                .setQuery(cartListReference, Cart.class)
                .build();

        FirebaseRecyclerAdapter<Cart, ProductHistoryViewHolder> adapter =
                new FirebaseRecyclerAdapter<Cart, ProductHistoryViewHolder>(options) {
                    @Override
                    protected void onBindViewHolder(@NonNull ProductHistoryViewHolder holder, int position, @NonNull Cart model) {
                        holder.productHistoryOrderID.setText("Order ID: " + model.getOrderID());
                        holder.productHistoryShipmentStatus.setText("Shipment Status: " + model.getShipmentStatus());
                        holder.productHistoryProductName.setText(model.getProductName());
                        holder.productHistoryPrice.setText("Price: €" + model.getPrice());
                        holder.productHistorySize.setText("Size: " + model.getSize());
                        holder.productHistoryQuantity.setText("Quantity: " + model.getQuantity());
                    }

                    @NonNull
                    @Override
                    public ProductHistoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.product_order_history_layout, parent, false);
                        ProductHistoryViewHolder holder = new ProductHistoryViewHolder(view);
                        return holder;
                    }
                };
        recyclerProducts.setAdapter(adapter);
        adapter.startListening();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(AdminProductHistoryActivity.this, AdminOrderHistoryActivity.class);
        startActivity(intent);
        finish();
    }
}