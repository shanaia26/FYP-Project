package com.example.fyp_project;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;
import com.example.fyp_project.Common.Common;
import com.example.fyp_project.Model.Products;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class ProductDetailsActivity extends AppCompatActivity {
    private Button addProductCartButton;
    private ImageView productImageDetails;
    private ElegantNumberButton elegantNumberButton;
    private TextView nameDetails;
    private TextView descriptionDetails;
    private TextView priceDetails;
    private TextView sizeDetails;

    private DatabaseReference orderReference;

    //Get productID of item user clicked
    private String productID = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_details);

        nameDetails = findViewById(R.id.details_name);
        descriptionDetails = findViewById(R.id.details_description);
        priceDetails = findViewById(R.id.details_price);
        sizeDetails = findViewById(R.id.details_size);
        productImageDetails = findViewById(R.id.details_image);

        elegantNumberButton = findViewById(R.id.elegant_number_button);
        addProductCartButton = findViewById(R.id.add_to_cart_button);

        productID = getIntent().getStringExtra("productID");

        getProductDetails(productID);

        addProductCartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddToCartList();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

    }

    private void AddToCartList() {
        String shoeSize = sizeDetails.getText().toString().trim();
        if (shoeSize.equals("")) {
            Toast.makeText(ProductDetailsActivity.this, "Please enter your shoe size", Toast.LENGTH_LONG).show();
        } else {
            //Store in firebase
            final DatabaseReference cartListReference =
                    FirebaseDatabase.getInstance().getReference()
                            .child("Cart List");

            final HashMap<String, Object> cartMap = new HashMap<>();
            cartMap.put("orderID", "Not Available");
            cartMap.put("productID", productID);
            cartMap.put("productName", nameDetails.getText().toString());
            cartMap.put("price", priceDetails.getText().toString());
            cartMap.put("quantity", elegantNumberButton.getNumber());
            cartMap.put("size", shoeSize);
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
                                                Toast.makeText(ProductDetailsActivity.this, "Added to cart.", Toast.LENGTH_LONG).show();
                                                //Go to Cart
                                                Intent intent = new Intent(ProductDetailsActivity.this, CartActivity.class);
                                                intent.putExtra("productID", productID);
                                                startActivity(intent);
                                            }
                                        });
                            }
                        }
                    });

        }
    }


    private void getProductDetails(String productID) {
        DatabaseReference productReference = FirebaseDatabase.getInstance().getReference().child("Products");
        productReference.child(productID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    Products products = snapshot.getValue(Products.class);

                    nameDetails.setText(products.getName());
                    descriptionDetails.setText(products.getDescription());
                    priceDetails.setText(products.getPrice());
                    //Getting product image
                    Picasso.get().load(products.getImage()).into(productImageDetails);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

}