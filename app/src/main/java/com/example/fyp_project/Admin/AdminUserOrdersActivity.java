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

import com.example.fyp_project.Model.UserOrders;
import com.example.fyp_project.Model.Users;
import com.example.fyp_project.R;
import com.example.fyp_project.ViewHolder.AdminOrderHistoryViewHolder;
import com.example.fyp_project.ViewHolder.AdminUserOrdersViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AdminUserOrdersActivity extends AppCompatActivity {

    private RecyclerView recyclerUsers;
    private DatabaseReference usersReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_user_orders);

        usersReference = FirebaseDatabase.getInstance().getReference()
                .child("Users");

        recyclerUsers = findViewById(R.id.recycler_users);
        recyclerUsers.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseRecyclerOptions<Users> options =
                new FirebaseRecyclerOptions.Builder<Users>()
                .setQuery(usersReference, Users.class)
                .build();

        FirebaseRecyclerAdapter<Users, AdminUserOrdersViewHolder> adapter
                = new FirebaseRecyclerAdapter<Users, AdminUserOrdersViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull AdminUserOrdersViewHolder holder, int position, @NonNull Users model) {
                holder.userOrderName.setText("Name: " + model.getName());
                holder.userOrderPhone.setText("Phone Number: " + model.getPhone());
                holder.userOrderEmail.setText("Email: " + model.getEmail());

                holder.showAllOrdersButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String userID = model.getPhone();
                        Intent intent = new Intent(AdminUserOrdersActivity.this, AdminOrderHistoryActivity.class);
                        intent.putExtra("userID",userID);
                        startActivity(intent);
                    }
                });

                holder.showEnquiriesButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String userID = model.getPhone();
                        Intent intent = new Intent(AdminUserOrdersActivity.this, AdminCustomerEnquiries.class);
                        intent.putExtra("userID",userID);
                        startActivity(intent);
                    }
                });
            }

            @NonNull
            @Override
            public AdminUserOrdersViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.admin_user_orders_layout, parent, false);
                return new AdminUserOrdersViewHolder(view);
            }
        };

        recyclerUsers.setAdapter(adapter);
        adapter.startListening();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(AdminUserOrdersActivity.this, AdminMainActivity.class);
        startActivity(intent);
        finish();
    }

}