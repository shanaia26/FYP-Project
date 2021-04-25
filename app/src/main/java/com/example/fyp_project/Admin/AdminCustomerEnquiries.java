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
import android.widget.Toast;

import com.example.fyp_project.Common.Common;
import com.example.fyp_project.Model.CustomerEnquiries;
import com.example.fyp_project.Model.Users;
import com.example.fyp_project.ProductDetailsActivity;
import com.example.fyp_project.R;
import com.example.fyp_project.ViewHolder.AdminCustomerEnquiriesViewHolder;
import com.example.fyp_project.ViewHolder.AdminUserOrdersViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

public class AdminCustomerEnquiries extends AppCompatActivity {
    private RecyclerView recyclerEnquiries;
    private DatabaseReference enquiriesReference;

    private String userID = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_customer_enquiries);

        userID = getIntent().getStringExtra("userID");

        enquiriesReference = FirebaseDatabase.getInstance().getReference()
                .child("Customer Enquiries")
                .child(userID);
        ;

        recyclerEnquiries = findViewById(R.id.recycler_enquiries);
        recyclerEnquiries.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseRecyclerOptions<CustomerEnquiries> options =
                new FirebaseRecyclerOptions.Builder<CustomerEnquiries>()
                        .setQuery(enquiriesReference, CustomerEnquiries.class)
                        .build();

        FirebaseRecyclerAdapter<CustomerEnquiries, AdminCustomerEnquiriesViewHolder> adapter
                = new FirebaseRecyclerAdapter<CustomerEnquiries, AdminCustomerEnquiriesViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull AdminCustomerEnquiriesViewHolder holder, int position, @NonNull CustomerEnquiries model) {
                holder.enquiryStatus.setText("Status: " + model.getEnquiryStatus());
                holder.enquiryProductId.setText("Product ID: " + model.getProductID());
                holder.enquiryDescription.setText("Description: " + model.getDescription());
                holder.enquirySize.setText("UK Size: " + model.getSize());
                holder.enquiryComment.setText("Comment: " + model.getComment());

                holder.seeDetailsButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String productID = model.getProductID();
                        Intent intent = new Intent(AdminCustomerEnquiries.this, AdminCustomerEnquiriesDetailsActivity.class);
                        intent.putExtra("userID", userID);
                        intent.putExtra("productID", productID);
                        startActivity(intent);

                    }
                });
            }

            @NonNull
            @Override
            public AdminCustomerEnquiriesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.admin_customer_enquiries_layout, parent, false);
                return new AdminCustomerEnquiriesViewHolder(view);
            }
        };
        recyclerEnquiries.setAdapter(adapter);
        adapter.startListening();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(AdminCustomerEnquiries.this, AdminUserOrdersViewHolder.class);
        startActivity(intent);
        finish();
    }

}