package com.example.fyp_project;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.example.fyp_project.Common.Common;
import com.example.fyp_project.Model.Cart;
import com.example.fyp_project.Model.Orders;
import com.example.fyp_project.Model.UserOrders;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class ShipmentActivity extends AppCompatActivity {

    private EditText shipmentName;
    private EditText shipmentPhone;
    private EditText shipmentAddress;

    private CheckBox standardCheckbox;
    private CheckBox expressCheckbox;
    private CheckBox internationalCheckbox;

    private Button confirmOrderButton;

    private String saveCurrentDate;
    private String saveCurrentTime;
    private String orderID;

    private String totalAmount;
    private String deliveryOption;

    private String productID;
    //private DatabaseReference orderHistoryReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shipment);

        shipmentName = findViewById(R.id.shipment_name);
        shipmentPhone = findViewById(R.id.shipment_phone);
        shipmentAddress = findViewById(R.id.shipment_address);
        standardCheckbox = findViewById(R.id.standard_checkBox);
        expressCheckbox = findViewById(R.id.express_checkBox);
        internationalCheckbox = findViewById(R.id.international_checkBox);
        confirmOrderButton = findViewById(R.id.confirm_button);

//        orderHistoryReference = FirebaseDatabase.getInstance().getReference()
//                .child("Orders")
//                .child(Common.currentUser.getPhone())
//                .child("Products");

        productID = getIntent().getStringExtra("productID");

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
        if (TextUtils.isEmpty(shipmentName.getText().toString()) || TextUtils.isEmpty(shipmentPhone.getText().toString()) ||
                TextUtils.isEmpty(shipmentAddress.getText().toString())) {
            Toast.makeText(ShipmentActivity.this, "Please provide full details.", Toast.LENGTH_LONG).show();
        } else {
            ConfirmOrder();
        }
    }

    private void ConfirmOrder() {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat currentDate = new SimpleDateFormat("dd MMM, yyyy");
        saveCurrentDate = currentDate.format(calendar.getTime());

        SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss a");
        saveCurrentTime = currentTime.format(calendar.getTime());

        //Creates a unique key
        orderID = saveCurrentDate + saveCurrentTime;

        final DatabaseReference orderHistoryReference = FirebaseDatabase.getInstance().getReference()
                .child("Orders")
                .child(Common.currentUser.getPhone());

        final DatabaseReference adminReference = FirebaseDatabase.getInstance().getReference()
                .child("Cart List")
                .child("Admin View")
                .child(Common.currentUser.getPhone())
                .child("Products");

        //Add Delivery Option to firebase
        if (standardCheckbox.isChecked()) {
            String standardDelivery = "10";
            orderHistoryReference.child(orderID).child("deliveryOption").setValue(standardDelivery);
            deliveryOption = standardDelivery;
        } else if (expressCheckbox.isChecked()) {
            String expressDelivery = "15";
            orderHistoryReference.child(orderID).child("deliveryOption").setValue(expressDelivery);
            deliveryOption = expressDelivery;
        } else if (internationalCheckbox.isChecked()) {
            String internationalDelivery = "20";
            orderHistoryReference.child(orderID).child("deliveryOption").setValue(internationalDelivery);
            deliveryOption = internationalDelivery;
        }

        String shipmentStatus = "Order Not Shipped";
        String paymentStatus = "Payment Pending";

        HashMap<String, Object> orderMap = new HashMap<>();
        orderMap.put("orderID", orderID);
        orderMap.put("shipmentName", shipmentName.getText().toString());
        orderMap.put("phone", shipmentPhone.getText().toString());
        orderMap.put("address", shipmentAddress.getText().toString());
        orderMap.put("date", saveCurrentDate);
        orderMap.put("totalAmount", totalAmount);
        orderMap.put("shipmentStatus", shipmentStatus);
        orderMap.put("paymentStatus", paymentStatus);

        orderHistoryReference
                .child(orderID)
                .updateChildren(orderMap)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            HashMap<String, Object> updateOrderID = new HashMap<String, Object>();
                            String newOrderID = orderID;
                            String oldOrderID = "Not Available";

                            //Update Admin View Map too - Add Order ID to products ordered
                            adminReference
                                    .orderByChild("orderID")
                                    .equalTo(oldOrderID)
                                    .addValueEventListener(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                                            if (snapshot.exists()) {
                                                for (DataSnapshot childSnapshot : snapshot.getChildren()) {
                                                    updateOrderID.put(childSnapshot.getKey() + "/orderID", newOrderID);
                                                }

                                                adminReference.updateChildren(updateOrderID);
                                            }
                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError error) {

                                        }
                                    });

                            Intent intent = new Intent(ShipmentActivity.this, CheckoutActivity.class);
                            intent.putExtra("orderID", orderID);
                            intent.putExtra("productID", productID);
                            intent.putExtra("totalAmount", totalAmount);
                            intent.putExtra("deliveryOption", deliveryOption);

                            startActivity(intent);
                        }
                    }
                });
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(ShipmentActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}