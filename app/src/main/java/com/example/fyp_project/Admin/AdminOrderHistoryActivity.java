package com.example.fyp_project.Admin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.fyp_project.Common.Common;
import com.example.fyp_project.Model.AdminOrderHistory;
import com.example.fyp_project.Model.UserOrders;
import com.example.fyp_project.R;
import com.example.fyp_project.ViewHolder.AdminOrderHistoryViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AdminOrderHistoryActivity extends AppCompatActivity {

    private RecyclerView recyclerOrders;
    private DatabaseReference ordersReference;

    private String userID = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_order_history);

        userID = getIntent().getStringExtra("userID");

        ordersReference = FirebaseDatabase.getInstance().getReference()
                .child("Admin Orders")
                .child(userID);

        recyclerOrders = findViewById(R.id.recycler_orders);
        recyclerOrders.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseRecyclerOptions<AdminOrderHistory> options =
                new FirebaseRecyclerOptions.Builder<AdminOrderHistory>()
                        .setQuery(ordersReference, AdminOrderHistory.class)
                        .build();

        FirebaseRecyclerAdapter<AdminOrderHistory, AdminOrderHistoryViewHolder> adapter
                = new FirebaseRecyclerAdapter<AdminOrderHistory, AdminOrderHistoryViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull AdminOrderHistoryViewHolder holder, int position, @NonNull AdminOrderHistory model) {
                holder.userName.setText("Name: " + model.getShipmentName());
                holder.userPhone.setText("Phone Number: " + model.getPhone());
                holder.userOrderID.setText("Order ID: " + model.getOrderID());
                holder.userPaymentStatus.setText("Payment Status: " + model.getShipmentStatus());
                holder.userTotalPrice.setText("Total Amount: €" + model.getOverallTotal());
                holder.userShipmentStatus.setText("Shipment Status: " + model.getShipmentStatus());
                holder.userAddress.setText("Shipping Address: " + model.getAddress());

                holder.showProductsButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String userID = model.getPhone();
                        Intent intent = new Intent(AdminOrderHistoryActivity.this, AdminProductHistoryActivity.class);
                        intent.putExtra("userID", userID);
                        startActivity(intent);
                    }
                });

                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        CharSequence options[] = new CharSequence[]{
                                "Yes",
                                "No"
                        };

                        AlertDialog.Builder builder = new AlertDialog.Builder(AdminOrderHistoryActivity.this);
                        builder.setTitle("Has this order been shipped?");

                        builder.setItems(options, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int i) {
                                if(i == 0){
                                    //String userID = getRef(position).getKey();
                                    //Let user know their order is Shipped
                                    ordersReference.child("shipmentStatus").setValue("Shipped");

                                } else{
                                    finish();
                                }
                            }
                        });
                        builder.show();
                    }
                });
            }

            @NonNull
            @Override
            public AdminOrderHistoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.admin_order_history_layout, parent, false);
                return new AdminOrderHistoryViewHolder(view);
            }
        };

        recyclerOrders.setAdapter(adapter);
        adapter.startListening();
    }
}