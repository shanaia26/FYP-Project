package com.example.fyp_project.Admin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.fyp_project.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.HashMap;

public class AdminMaintainProductsActivity extends AppCompatActivity {
    private EditText maintainName;
    private EditText maintainPrice;
    private EditText maintainDescription;

    private ImageView maintainImage;
    private Button applyChangesButton;
    private Button removeProductButton;

    //Get productID of item user clicked
    private String productID = "";

    private DatabaseReference productsReference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_maintain_products);

        productID = getIntent().getStringExtra("productID");
        productsReference = FirebaseDatabase.getInstance().getReference()
                .child("Products")
                .child(productID);

        maintainName = findViewById(R.id.maintain_product_name);
        maintainPrice = findViewById(R.id.maintain_product_price);
        maintainDescription = findViewById(R.id.maintain_product_description);
        maintainImage = findViewById(R.id.maintain_product_image);
        applyChangesButton = findViewById(R.id.apply_changes_button);
        removeProductButton = findViewById(R.id.remove_product_button);

        displaySpecificProductInfo();

        applyChangesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                applyChanges();
            }
        });

        removeProductButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                removeThisProduct();
            }
        });
    }

    private void removeThisProduct() {
        productsReference.removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    Intent intent = new Intent(AdminMaintainProductsActivity.this, AdminMainActivity.class);
                    startActivity(intent);
                    finish();

                    Toast.makeText(AdminMaintainProductsActivity.this, "The product has been removed successfully", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void applyChanges() {
        String name = maintainName.getText().toString();
        String price = maintainPrice.getText().toString();
        String description = maintainDescription.getText().toString();

        //Tell admin if anything is empty
        if(name.equals("") || price.equals("") || description.equals("")){
            Toast.makeText(AdminMaintainProductsActivity.this, "Empty Fields", Toast.LENGTH_LONG).show();
        } else {
            HashMap<String, Object> productMap = new HashMap<>();
            productMap.put("productID", productID);
            productMap.put("name", name);
            productMap.put("description", description);
            productMap.put("price", price);

            productsReference.updateChildren(productMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                   if(task.isSuccessful()){
                       Toast.makeText(AdminMaintainProductsActivity.this, "Changes applied successfully.", Toast.LENGTH_LONG).show();

                       Intent intent = new Intent(AdminMaintainProductsActivity.this, AdminMainActivity.class);
                       startActivity(intent);
                       finish();
                   }
                }
            });
        }
    }

    private void displaySpecificProductInfo() {
        productsReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    String name = snapshot.child("name").getValue().toString();
                    String price = snapshot.child("price").getValue().toString();
                    String description = snapshot.child("description").getValue().toString();
                    String image = snapshot.child("image").getValue().toString();

                    maintainName.setText(name);
                    maintainPrice.setText(price);
                    maintainDescription.setText(description);
                    Picasso.get().load(image).into(maintainImage);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}