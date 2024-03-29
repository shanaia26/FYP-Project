package com.example.fyp_project.CustomerEnquiries;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.fyp_project.Common.Common;
import com.example.fyp_project.MainActivity;
import com.example.fyp_project.Model.CustomerEnquiries;
import com.example.fyp_project.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class CustomerSendEnquiryActivity extends AppCompatActivity {
    private TextView customerDescription;
    private TextView customerSize;
    private TextView customerComment;
    private ImageView customerImage;
    private Button customerCancelButton;
    private Button customerSendButton;

    private DatabaseReference customerEnquiryReference;

    //Get productID of item user clicked
    private String productID = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_send_enquiry);

        customerDescription = findViewById(R.id.customer_description);
        customerSize = findViewById(R.id.customer_size);
        customerComment = findViewById(R.id.customer_comment);
        customerImage = findViewById(R.id.customer_image);
        customerCancelButton = findViewById(R.id.customer_cancel_button);
        customerSendButton = findViewById(R.id.customer_send_button);

        productID = getIntent().getStringExtra("productID");

        customerEnquiryReference = FirebaseDatabase.getInstance().getReference()
                .child("Customer Enquiries")
                .child(Common.currentUser.getPhone());

        GetProductDetails(productID);

        customerCancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Remove enquiry from DB
                customerEnquiryReference.removeValue();
            }
        });

        customerSendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CustomerSendEnquiryActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }

    private void GetProductDetails(String customerProductID) {
        customerEnquiryReference
                .child(customerProductID)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()) {
                            CustomerEnquiries customerEnquiries = snapshot.getValue(CustomerEnquiries.class);

                            customerDescription.setText("Description: " + customerEnquiries.getDescription());
                            customerSize.setText("Shoe Size: " + customerEnquiries.getSize());
                            customerComment.setText("Comment: " + customerEnquiries.getComment());
                            //Getting customer product image
                            Picasso.get().load(customerEnquiries.getImage()).into(customerImage);
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
        Intent intent = new Intent(CustomerSendEnquiryActivity.this, CustomerEnquiryHistoryActivity.class);
        startActivity(intent);
        finish();
    }
}
