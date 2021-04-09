package com.example.fyp_project;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.fyp_project.Common.Common;
import com.example.fyp_project.Model.Products;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class CheckoutActivity extends AppCompatActivity {
    private TextView subtotalPrice;
    private TextView shipmentPrice;
    private TextView totalPrice;
    private Button stripeButton;

    private String totalAmount ="";
    private String deliveryOption ="";
    private String orderID ="";
    private String productID ="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout);

        subtotalPrice = findViewById(R.id.subtotal_price);
        shipmentPrice = findViewById(R.id.shipment_price);
        totalPrice = findViewById(R.id.total_price);
        stripeButton = findViewById(R.id.stripe_button);

        orderID = getIntent().getStringExtra("orderID");
        productID = getIntent().getStringExtra("productID");

        totalAmount = getIntent().getStringExtra("totalAmount");
        deliveryOption = getIntent().getStringExtra("deliveryOption");
        subtotalPrice.setText(totalAmount);
        shipmentPrice.setText(deliveryOption);

        //Add subtotal and shipping
        int productTotal = Integer.parseInt(totalAmount);
        int shipmentTotal = Integer.parseInt(deliveryOption);
        int overallTotal = productTotal + shipmentTotal;
        totalPrice.setText(String.valueOf(overallTotal));

        stripeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Add complete total (product total + shipping) into firebase
                final DatabaseReference ordersReference = FirebaseDatabase.getInstance().getReference()
                        .child("Orders")
                        .child(Common.currentUser.getPhone())
                        .child(orderID);

                HashMap<String, Object> orderMap = new HashMap<>();
                orderMap.put("overallTotal", String.valueOf(overallTotal));

                ordersReference
                        .updateChildren(orderMap)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    //Go to Stripe Payment
                                    Intent intent = new Intent(CheckoutActivity.this, StripePaymentActivity.class);
                                    intent.putExtra("overallTotal", String.valueOf(overallTotal));
                                    intent.putExtra("orderID", orderID);
                                    intent.putExtra("productID", productID);
                                    startActivity(intent);
                                }
                            }
                        });
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
    }
}