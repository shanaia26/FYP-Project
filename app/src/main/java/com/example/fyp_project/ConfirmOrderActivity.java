package com.example.fyp_project;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.fyp_project.Common.Common;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class ConfirmOrderActivity extends AppCompatActivity {

    private EditText shipmentName;
    private EditText shipmentPhone;
    private EditText shipmentAddress;
    private EditText shipmentCity;

    private Button confirmOrderButton;

    private String totalAmount;
    private String orderID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_order);

        shipmentName = findViewById(R.id.shipment_name);
        shipmentPhone = findViewById(R.id.shipment_phone);
        shipmentAddress = findViewById(R.id.shipment_address);
        shipmentCity = findViewById(R.id.shipment_city);
        confirmOrderButton = findViewById(R.id.confirm_button);

        totalAmount = getIntent().getStringExtra("Total Price");
        Toast.makeText(this, "Total Price: €" + totalAmount, Toast.LENGTH_SHORT).show();

        confirmOrderButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Check();
            }
        });
    }

    private void Check() {
        if(TextUtils.isEmpty(shipmentName.getText().toString()) || TextUtils.isEmpty(shipmentPhone.getText().toString()) ||
                TextUtils.isEmpty(shipmentAddress.getText().toString()) || TextUtils.isEmpty(shipmentCity.getText().toString())){
            Toast.makeText(ConfirmOrderActivity.this, "Please provide full details.", Toast.LENGTH_LONG).show();
        } else {
            ConfirmOrder();
        }
    }

    private void ConfirmOrder() {
        final String saveCurrentDate;
        final String saveCurrentTime;

        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat currentDate = new SimpleDateFormat("dd MMM, yyyy");
        saveCurrentDate = currentDate.format(calendar.getTime());

        SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss a");
        saveCurrentTime = currentTime.format(calendar.getTime());

        //Creates a unique key
        orderID = saveCurrentDate + saveCurrentTime;

        final DatabaseReference ordersReference = FirebaseDatabase.getInstance().getReference()
                .child("Orders")
                .child(Common.currentUser.getPhone());

        HashMap<String, Object> orderMap = new HashMap<>();
        orderMap.put("orderID", orderID);
        orderMap.put("totalAmount", totalAmount);
        orderMap.put("name", shipmentName.getText().toString());
        orderMap.put("phone", shipmentPhone.getText().toString());
        orderMap.put("address", shipmentAddress.getText().toString());
        orderMap.put("city", shipmentCity.getText().toString());
        orderMap.put("status", "Order Not Shipped");

        ordersReference
                .updateChildren(orderMap)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    //Empties the cart when user has ordered
                    FirebaseDatabase.getInstance().getReference()
                            .child("Cart List")
                            .child("User View")
                            .child(Common.currentUser.getPhone())
                            .removeValue()
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful()){
                                        Toast.makeText(ConfirmOrderActivity.this, "Your order has been placed successfully.", Toast.LENGTH_LONG).show();
                                        Intent intent = new Intent(ConfirmOrderActivity.this, MainActivity.class);
                                        intent.putExtra(orderID, "orderID");
                                        startActivity(intent);
                                    }
                                }
                            });

                }
            }
        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent= new Intent(ConfirmOrderActivity.this,MainActivity.class);
        startActivity(intent);
        finish();
    }
}