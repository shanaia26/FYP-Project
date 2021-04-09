package com.example.fyp_project;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.fyp_project.Common.Common;
import com.example.fyp_project.Model.UserOrders;
import com.example.fyp_project.ViewHolder.OrderHistoryViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class OrderHistoryActivity extends AppCompatActivity {

    private RecyclerView recyclerOrderHistory;
    private DatabaseReference orderHistoryReference;

    private Button historyProductsButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_history);

        historyProductsButton = findViewById(R.id.history_products_button);

        orderHistoryReference = FirebaseDatabase.getInstance().getReference()
                .child("Orders")
                .child(Common.currentUser.getPhone());

        recyclerOrderHistory = findViewById(R.id.recycler_order_history);
        recyclerOrderHistory.setLayoutManager(new LinearLayoutManager(this));

        historyProductsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Show All Products User has ordered
                Intent intent = new Intent(OrderHistoryActivity.this, ProductOrderHistoryActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseRecyclerOptions<UserOrders> options =
                new FirebaseRecyclerOptions.Builder<UserOrders>()
                        .setQuery(orderHistoryReference, UserOrders.class)
                        .build();

        FirebaseRecyclerAdapter<UserOrders, OrderHistoryViewHolder> adapter
                = new FirebaseRecyclerAdapter<UserOrders, OrderHistoryViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull OrderHistoryViewHolder holder, int position, @NonNull UserOrders model) {
                holder.historyOrderID.setText("Order ID: " + model.getOrderID());
                holder.historyTotalPrice.setText("Name: " + model.getShipmentName());
                holder.historyOrderDate.setText("Date Ordered: " + model.getDate());
                holder.historyShipmentStatus.setText("Shipment Status: " + model.getShipmentStatus());

            }

            @NonNull
            @Override
            public OrderHistoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.order_history_layout, parent, false);
                return new OrderHistoryViewHolder(view);
            }
        };

        recyclerOrderHistory.setAdapter(adapter);
        adapter.startListening();
    }

}
