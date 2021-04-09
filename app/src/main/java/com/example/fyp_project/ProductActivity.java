package com.example.fyp_project;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.fyp_project.Admin.AdminMaintainProductsActivity;
import com.example.fyp_project.Model.Products;
import com.example.fyp_project.ViewHolder.ProductViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

public class ProductActivity extends AppCompatActivity {
    private DatabaseReference productReference;
    private RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;

    private FloatingActionButton floatingCartButton;

    private String type = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_products);

        productReference = FirebaseDatabase.getInstance().getReference().child("Products");

        recyclerView = findViewById(R.id.recycler_menu);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        floatingCartButton = findViewById(R.id.fab_cart);

        floatingCartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!type.equals("Admin")){
                    Intent intent = new Intent(ProductActivity.this, CartActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                }
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseRecyclerOptions<Products> options = new FirebaseRecyclerOptions.Builder<Products>()
                .setQuery(productReference, Products.class)
                .build();

        FirebaseRecyclerAdapter<Products, ProductViewHolder> adapter =
                new FirebaseRecyclerAdapter<Products, ProductViewHolder>(options) {
                    @Override
                    protected void onBindViewHolder(@NonNull ProductViewHolder holder, int position, @NonNull Products model) {
                        //Display data on the recyclerview
                        holder.productName.setText(model.getName());
                        holder.productDescription.setText(model.getDescription());
                        holder.productPrice.setText("Price: €" + model.getPrice());

                        Picasso.get().load(model.getImage()).into(holder.productImage);

                        holder.itemView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if(type.equals("Admin")){
                                    //If it's admin send to maintain products activity
                                    Intent intent = new Intent(ProductActivity.this, AdminMaintainProductsActivity.class);
                                    intent.putExtra("productID", model.getProductID());
                                    startActivity(intent);
                                } else {
                                    //If it's user send to product details activity
                                    Intent intent = new Intent(ProductActivity.this, ProductDetailsActivity.class);
                                    intent.putExtra("productID", model.getProductID());
                                    startActivity(intent);
                                }
                            }
                        });
                    }

                    @NonNull
                    @Override
                    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.product_item_layout, parent, false);
                        ProductViewHolder holder = new ProductViewHolder(view);
                        return holder;
                    }
                };
        recyclerView.setAdapter(adapter);
        adapter.startListening();
    }


    //To avoid closing the application on back press
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(ProductActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}