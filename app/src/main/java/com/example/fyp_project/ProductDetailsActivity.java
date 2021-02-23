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

    private String saveCurrentDate;
    private String saveCurrentTime;

    private DatabaseReference orderReference;

    //Get productID of item user clicked
    private String productID = "";
    private String status = "Normal";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_details);

        nameDetails = findViewById(R.id.details_name);
        descriptionDetails = findViewById(R.id.details_description);
        priceDetails = findViewById(R.id.details_price);
        productImageDetails = findViewById(R.id.details_image);

        elegantNumberButton = findViewById(R.id.elegant_number_button);
        addProductCartButton = findViewById(R.id.add_to_cart_button);

        productID = getIntent().getStringExtra("productID");

        orderReference = FirebaseDatabase.getInstance().getReference()
                .child("Orders")
                .child(Common.currentUser.getPhone());

        getProductDetails(productID);

        addProductCartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(status.equals("Order Placed") || status.equals("Order Shipped")){
                    Toast.makeText(ProductDetailsActivity.this, "You can place more orders once your order has been shipped or confirmed", Toast.LENGTH_LONG).show();
                } else {
                    AddToCartList();
                }
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        CheckOrderState();
    }

    private void AddToCartList() {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat currentDate = new SimpleDateFormat("dd MMM, yyyy");
        saveCurrentDate = currentDate.format(calendar.getTime());

        SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss a");
        saveCurrentTime = currentTime.format(calendar.getTime());

        //Store in firebase
        DatabaseReference cartListReference = FirebaseDatabase.getInstance().getReference().child("Cart List");

        final HashMap<String, Object> cartMap = new HashMap<>();
        cartMap.put("productID", productID);
        cartMap.put("date", saveCurrentDate);
        cartMap.put("time", saveCurrentTime);
        cartMap.put("name", nameDetails.getText().toString());
        cartMap.put("price", priceDetails.getText().toString());
        cartMap.put("quantity", elegantNumberButton.getNumber());

        cartListReference.child("User View").child(Common.currentUser.getPhone())
                .child("Products").child(productID)
                .updateChildren(cartMap)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            cartListReference.child("Admin View").child(Common.currentUser.getPhone())
                                    .child("Products").child(productID)
                                    .updateChildren(cartMap)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            Toast.makeText(ProductDetailsActivity.this, "Added to cart.", Toast.LENGTH_LONG).show();
                                            Intent intent = new Intent(ProductDetailsActivity.this, MainActivity.class);
                                            startActivity(intent);
                                        }
                                    });
                        }
                    }
                });

    }


    private void getProductDetails(String productID){
        DatabaseReference productReference = FirebaseDatabase.getInstance().getReference().child("Products");
        productReference.child(productID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
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

    private void CheckOrderState(){
        orderReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    String shippingStatus = snapshot.child("status").getValue().toString();

                    if(shippingStatus.equals("Shipped")){
                        status = "Order Shipped";
                    } else if (shippingStatus.equals("Order Not Shipped")){
                        status = "Order Placed";
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}