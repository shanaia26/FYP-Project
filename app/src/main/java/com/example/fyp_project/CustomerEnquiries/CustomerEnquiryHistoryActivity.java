package com.example.fyp_project.CustomerEnquiries;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.fyp_project.Admin.AdminOrderHistoryActivity;
import com.example.fyp_project.Admin.AdminProductHistoryActivity;
import com.example.fyp_project.CartActivity;
import com.example.fyp_project.Common.Common;
import com.example.fyp_project.MainActivity;
import com.example.fyp_project.Model.CustomerEnquiries;
import com.example.fyp_project.Model.UserOrders;
import com.example.fyp_project.OrderHistoryActivity;
import com.example.fyp_project.ProductDetailsActivity;
import com.example.fyp_project.ProductOrderHistoryActivity;
import com.example.fyp_project.R;
import com.example.fyp_project.ViewHolder.CustomerEnquiriesHistoryViewHolder;
import com.example.fyp_project.ViewHolder.OrderHistoryViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class CustomerEnquiryHistoryActivity extends AppCompatActivity {
    private RecyclerView recyclerEnquiriesHistory;
    private DatabaseReference customerEnquiriesReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_enquiry_history);

        customerEnquiriesReference = FirebaseDatabase.getInstance().getReference()
                .child("Customer Enquiries")
                .child(Common.currentUser.getPhone());

        recyclerEnquiriesHistory = findViewById(R.id.recycler_enquiries_history);
        recyclerEnquiriesHistory.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseRecyclerOptions<CustomerEnquiries> options =
                new FirebaseRecyclerOptions.Builder<CustomerEnquiries>()
                        .setQuery(customerEnquiriesReference, CustomerEnquiries.class)
                        .build();

        FirebaseRecyclerAdapter<CustomerEnquiries, CustomerEnquiriesHistoryViewHolder> adapter
                = new FirebaseRecyclerAdapter<CustomerEnquiries, CustomerEnquiriesHistoryViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull CustomerEnquiriesHistoryViewHolder holder, int position, @NonNull CustomerEnquiries model) {
                String productID = model.getProductID();

                holder.enquiriesStatus.setText("Status: " + model.getEnquiryStatus());
                holder.enquiriesProductID.setText("Product ID: " + model.getProductID());
                holder.enquiriesName.setText("Name: " + model.getName());
                holder.enquiriesDescription.setText("Description: " + model.getDescription());
                holder.enquiriesSize.setText("Size: " + model.getDate());
                holder.enquiriesComment.setText("Comment: " + model.getComment());
                holder.enquiriesPrice.setText("Price: € " + model.getPrice());

                if (model.getPrice().equals("N/A") || model.getEnquiryStatus().equals("Ordered")) {
                    holder.enquiriesOrderButton.setEnabled(false);
                    holder.enquiriesCancelButton.setEnabled(false);
                } else {
                    holder.enquiriesCancelButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            //If user decided to cancel their order - remove product from DB? or set status as cancelled
                            customerEnquiriesReference
                                    .child(productID)
                                    .child("enquiryStatus")
                                    .setValue("Cancelled");
                            holder.enquiriesOrderButton.setEnabled(false);
                            holder.enquiriesCancelButton.setEnabled(false);
                        }
                    });

                    holder.enquiriesOrderButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
//                            String productID = model.getProductID();
                            //String category = model.getCategory();
                            //Add to Cart
                            customerEnquiriesReference
                                    .child(productID)
                                    .child("enquiryStatus")
                                    .setValue("Ordered");
                            holder.enquiriesCancelButton.setEnabled(false);

                            //Add to Cart List
                            //Store in firebase
                            final DatabaseReference cartListReference =
                                    FirebaseDatabase.getInstance().getReference()
                                            .child("Cart List");

                            final HashMap<String, Object> cartMap = new HashMap<>();
                            cartMap.put("orderID", "Not Available");
                            cartMap.put("productID", productID);
                            cartMap.put("productName", model.getName());
                            cartMap.put("price", model.getPrice());
                            cartMap.put("quantity", model.getQuantity());
                            cartMap.put("size", model.getSize());
                            cartMap.put("shipmentStatus", "Order Not Shipped");

                            cartListReference
                                    .child("User View")
                                    .child(Common.currentUser.getPhone())
                                    .child("Products")
                                    .child(productID)
                                    .updateChildren(cartMap)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                //Add to Admin View DB - for Admin to see
                                                cartListReference
                                                        .child("Admin View")
                                                        .child(Common.currentUser.getPhone())
                                                        .child("Products")
                                                        .child(productID)
                                                        .updateChildren(cartMap)
                                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                            @Override
                                                            public void onComplete(@NonNull Task<Void> task) {
                                                                Toast.makeText(CustomerEnquiryHistoryActivity.this, "Added to cart.", Toast.LENGTH_LONG).show();
                                                                //Go to Cart
                                                                Intent intent = new Intent(CustomerEnquiryHistoryActivity.this, CartActivity.class);
                                                                intent.putExtra("productID", productID);
                                                                //intent.putExtra("category", category);
                                                                startActivity(intent);
                                                            }
                                                        });
                                            }
                                        }
                                    });

                        }


                    });
                }
            }

            @NonNull
            @Override
            public CustomerEnquiriesHistoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent,
                                                                         int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.customer_enquiries_history_layout, parent, false);
                return new CustomerEnquiriesHistoryViewHolder(view);
            }
        };

        recyclerEnquiriesHistory.setAdapter(adapter);
        adapter.startListening();
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(CustomerEnquiryHistoryActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}