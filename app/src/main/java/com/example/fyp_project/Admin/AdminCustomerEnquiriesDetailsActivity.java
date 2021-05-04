package com.example.fyp_project.Admin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.fyp_project.Common.Common;
import com.example.fyp_project.Model.CustomerEnquiries;
import com.example.fyp_project.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class AdminCustomerEnquiriesDetailsActivity extends AppCompatActivity {
    private ImageView enquiriesDetailImage;
    private EditText enquiriesDetailPrice;
    private Button enquiriesDetailSendButton;

    private DatabaseReference enquiriesReference;

    private String userID = "";
    private String productID = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_customer_enquiries_details);

        userID = getIntent().getStringExtra("userID");
        productID = getIntent().getStringExtra("productID");

        enquiriesDetailImage = findViewById(R.id.enquiries_detail_image);
        enquiriesDetailPrice = findViewById(R.id.enquiries_detail_price);
        enquiriesDetailSendButton = findViewById(R.id.enquiries_detail_send_button);

        enquiriesReference = FirebaseDatabase.getInstance().getReference()
                .child("Customer Enquiries")
                .child(userID)
                .child(productID);

        enquiriesDetailSendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String price = enquiriesDetailPrice.getText().toString().trim();
                if (price.equals("")) {
                    Toast.makeText(AdminCustomerEnquiriesDetailsActivity.this, Common.EmptyCredentialsKey, Toast.LENGTH_LONG).show();
                } else {
                    enquiriesReference.child("price").setValue(price);
                    enquiriesReference.child("enquiryStatus").setValue("Awaiting Response...");

                    //Notfication
                    String message = "Price sent back to user.";

                    //Allows Notification to run on any devices
                    if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
                        NotificationChannel notificationChannel = new NotificationChannel
                                ("Price enquiry", "Price Enquiry", NotificationManager.IMPORTANCE_DEFAULT);
                        NotificationManager manager = getSystemService(NotificationManager.class);
                        manager.createNotificationChannel(notificationChannel);
                    }

                    NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder
                            (AdminCustomerEnquiriesDetailsActivity.this,"Enquiry Notification");
                    notificationBuilder.setContentTitle("Price Enquiry");
                    notificationBuilder.setContentText(message);
                    notificationBuilder.setSmallIcon(R.drawable.notification_icon);
                    notificationBuilder.setAutoCancel(true);

                    NotificationManagerCompat notificationManagerCompat =
                            NotificationManagerCompat.from(AdminCustomerEnquiriesDetailsActivity.this);
                    notificationManagerCompat.notify(1, notificationBuilder.build());

                    Toast.makeText(AdminCustomerEnquiriesDetailsActivity.this, message, Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(AdminCustomerEnquiriesDetailsActivity.this, AdminMainActivity.class);
                    startActivity(intent);
                }
            }
        });
    }


    @Override
    protected void onStart() {
        super.onStart();
        enquiriesReference
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()) {
                            CustomerEnquiries customerEnquiries = snapshot.getValue(CustomerEnquiries.class);
                            //Getting product image
                            Picasso.get().load(customerEnquiries.getImage()).into(enquiriesDetailImage);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(AdminCustomerEnquiriesDetailsActivity.this, AdminMainActivity.class);
        startActivity(intent);
        finish();
    }
}
