package com.example.fyp_project.Admin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.fyp_project.Model.AdminOrders;
import com.example.fyp_project.R;
import com.example.fyp_project.ViewHolder.AdminOrdersViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AdminUserOrdersActivity extends AppCompatActivity {

    private RecyclerView recyclerOrders;

    private DatabaseReference ordersReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_new_orders);

        ordersReference = FirebaseDatabase.getInstance().getReference().child("Orders");

        recyclerOrders = findViewById(R.id.recycler_orders);
        recyclerOrders.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseRecyclerOptions<AdminOrders> options =
                new FirebaseRecyclerOptions.Builder<AdminOrders>()
                .setQuery(ordersReference, AdminOrders.class)
                .build();

        FirebaseRecyclerAdapter<AdminOrders, AdminOrdersViewHolder> adapter
                = new FirebaseRecyclerAdapter<AdminOrders, AdminOrdersViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull AdminOrdersViewHolder holder, int position, @NonNull AdminOrders model) {
                holder.userName.setText("Name: " + model.getName());
                holder.userPhone.setText("Phone Number: " + model.getPhone());
                holder.userOrderID.setText("Order ID: " + model.getOrderID());
                holder.userStatus.setText("Shipment Status: " + model.getShipmentStatus());
                holder.userTotalPrice.setText("Total Price: €" + model.getTotalAmount());
                holder.userAddress.setText("Shipping Address: " + model.getAddress());

                holder.showProductsButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String userID = getRef(position).getKey();

                        Intent intent = new Intent(AdminUserOrdersActivity.this, AdminUserProductsActivity.class);
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

                        AlertDialog.Builder builder = new AlertDialog.Builder(AdminUserOrdersActivity.this);
                        builder.setTitle("Has this order been shipped?");

                        builder.setItems(options, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int i) {
                                if(i == 0){
                                    String userID = getRef(position).getKey();

                                    //ChangeOrderStatus(userID);

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
            public AdminOrdersViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.admin_orders_layout, parent, false);
                return new AdminOrdersViewHolder(view);
            }
        };

        recyclerOrders.setAdapter(adapter);
        adapter.startListening();
    }

    //Remove order when Admin ships order
    private void ChangeOrderStatus(final String status) {
        //ordersReference.child(userID).removeValue();
        ordersReference.child(status).setValue("Shipped");
        //Let user know their order is Shipped
    }
}