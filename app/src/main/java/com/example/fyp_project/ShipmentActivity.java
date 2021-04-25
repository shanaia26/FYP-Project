package com.example.fyp_project;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
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
    private Button confirmOrderButton;
    private CheckBox standardCheckbox;
    private CheckBox expressCheckbox;
    private CheckBox internationalCheckbox;

   private String aShipmentName;
    private String aShipmentPhone;
   private String aShipmentAddress;

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

        //Set Phone as user's phone number
        shipmentPhone.setText(Common.currentUser.getPhone());
        shipmentPhone.setEnabled(false);

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
        aShipmentName = shipmentName.getText().toString().trim();
        aShipmentPhone = shipmentPhone.getText().toString().trim();
        aShipmentAddress = shipmentAddress.getText().toString().trim();

        if (TextUtils.isEmpty(aShipmentName) || TextUtils.isEmpty(aShipmentAddress)) {
            Toast.makeText(ShipmentActivity.this, "Please provide full details.", Toast.LENGTH_LONG).show();
        } else if (standardCheckbox.isChecked() == false && expressCheckbox.isChecked() == false && internationalCheckbox.isChecked() == false) {
            Toast.makeText(ShipmentActivity.this, "Please pick a delivery option.", Toast.LENGTH_LONG).show();
            //CheckBoxValidation();
        } else {
            ConfirmOrder(aShipmentName, aShipmentPhone, aShipmentAddress);
        }
    }

//    private void CheckBoxValidation() {
//        //Disable checkbox once user picked one option
//        if (standardCheckbox.isChecked() == true) {
//            //Disable other checkboxes
//            expressCheckbox.setClickable(false);
//            internationalCheckbox.setClickable(false);
//        } else if (expressCheckbox.isChecked() == true) {
//            //Disable other checkboxes
//            standardCheckbox.setClickable(false);
//            internationalCheckbox.setClickable(false);
//        } else if (internationalCheckbox.isChecked() == true) {
//            //Disable other checkboxes
//            standardCheckbox.setClickable(false);
//            expressCheckbox.setClickable(false);
//        }
//    }

    private void ConfirmOrder(String aShipmentName, String aShipmentPhone, String aShipmentAddress) {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat currentDate = new SimpleDateFormat("dd MMM, yyyy");
        saveCurrentDate = currentDate.format(calendar.getTime());

        SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss a");
        saveCurrentTime = currentTime.format(calendar.getTime());

        //Creates a unique key
        orderID = saveCurrentDate + saveCurrentTime;

        final DatabaseReference orderHistoryReference = FirebaseDatabase.getInstance().getReference()
                .child("Order History")
                .child(Common.currentUser.getPhone())
                .child(orderID);

        final DatabaseReference adminOrderReference = FirebaseDatabase.getInstance().getReference()
                .child("Admin Orders")
                .child(Common.currentUser.getPhone());

        final DatabaseReference adminReference = FirebaseDatabase.getInstance().getReference()
                .child("Cart List")
                .child("Admin View")
                .child(Common.currentUser.getPhone())
                .child("Products");

        //Add Delivery Option to firebase
        if (standardCheckbox.isChecked()) {
            String standardDelivery = "10";
            orderHistoryReference.child("deliveryOption").setValue(standardDelivery);
            deliveryOption = standardDelivery;
        } else if (expressCheckbox.isChecked()) {
            String expressDelivery = "15";
            orderHistoryReference.child("deliveryOption").setValue(expressDelivery);
            deliveryOption = expressDelivery;
        } else if (internationalCheckbox.isChecked()) {
            String internationalDelivery = "20";
            orderHistoryReference.child("deliveryOption").setValue(internationalDelivery);
            deliveryOption = internationalDelivery;
        }

        String shipmentStatus = "Order Not Shipped";
        String paymentStatus = "Payment Pending";

        HashMap<String, Object> orderMap = new HashMap<>();
        orderMap.put("orderID", orderID);
        orderMap.put("shipmentName", aShipmentName);
        orderMap.put("phone", aShipmentPhone);
        orderMap.put("address", aShipmentAddress);
        orderMap.put("date", saveCurrentDate);
        orderMap.put("totalAmount", totalAmount);
        orderMap.put("shipmentStatus", shipmentStatus);
        orderMap.put("paymentStatus", paymentStatus);

        orderHistoryReference
                .updateChildren(orderMap)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        adminOrderReference
                                .child(orderID)
                                .updateChildren(orderMap)
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            //Add details for Admin DB
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
                });
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(ShipmentActivity.this, CartActivity.class);
        startActivity(intent);
        finish();
    }
}