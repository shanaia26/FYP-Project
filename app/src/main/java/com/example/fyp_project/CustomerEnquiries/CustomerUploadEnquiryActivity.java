package com.example.fyp_project.CustomerEnquiries;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.fyp_project.Common.Common;
import com.example.fyp_project.MainActivity;
import com.example.fyp_project.R;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class CustomerUploadEnquiryActivity extends AppCompatActivity {

    private EditText customerProductDescription;
    private EditText customerProductSize;
    private EditText customerProductComment;
    private ImageView customerProductImage;
    private Button customerUploadButton;

    private String productID;
    private String downloadImageURL;

    private String aCustomerDescription;
    private String aCustomerSize;
    private String aCustomerComment;

    private String saveCurrentDate;
    private String saveCurrentTime;

    private static final int GalleryPick = 1;
    private Uri imageURI;

    private StorageReference customerImagesReference;
    private DatabaseReference customerDesignReference;

    private ProgressDialog progressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_upload_enquiry);

        customerProductDescription = findViewById(R.id.customer_product_description);
        customerProductSize = findViewById(R.id.customer_product_size);
        customerProductComment = findViewById(R.id.customer_product_comment);
        customerProductImage = findViewById(R.id.customer_product_image);
        customerUploadButton = findViewById(R.id.customer_upload_button);

        progressDialog = new ProgressDialog(this);

        customerImagesReference = FirebaseStorage.getInstance().getReference()
                .child("Customer Product Images");

        customerDesignReference = FirebaseDatabase.getInstance().getReference()
                .child("Customer Enquiries");

        customerProductImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OpenGallery();
            }
        });

        customerUploadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ValidateProductData();
            }
        });

    }

    private void OpenGallery(){
        Intent galleryIntent = new Intent();
        galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
        galleryIntent.setType("image/*");
        startActivityForResult(galleryIntent, GalleryPick);
    }

    //Getting the image user added and adding to database
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode,data);

        if(requestCode==GalleryPick && resultCode==RESULT_OK && data!=null){
            imageURI = data.getData();
            //Display Image
            customerProductImage.setImageURI(imageURI);
        }
    }

    private void ValidateProductData(){
        aCustomerDescription = customerProductDescription.getText().toString().trim();
        aCustomerSize = customerProductSize.getText().toString().trim();
        aCustomerComment = customerProductComment.getText().toString().trim();

        //Check if user added an image
        if(imageURI == null){
            Toast.makeText(this, "Image Required.", Toast.LENGTH_LONG).show();
        } else if (TextUtils.isEmpty(aCustomerDescription) || TextUtils.isEmpty(aCustomerSize)){
            Toast.makeText(this, "Please provide more information.", Toast.LENGTH_LONG).show();
        } else{
            if(TextUtils.isEmpty(aCustomerComment)){
                //Set comment as Not Available
                aCustomerComment = "N/A";
            }
            StoreProductInformation();
        }
    }

    private void StoreProductInformation(){
        progressDialog.setTitle("Uploading Your Design");
        progressDialog.setMessage("Please wait...");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();

        Calendar calendar = Calendar.getInstance();
        //Time & Date user added new product
        SimpleDateFormat currentDate = new SimpleDateFormat("dd MMM, yyyy");
        saveCurrentDate = currentDate.format(calendar.getTime());

        SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss a");
        saveCurrentTime = currentTime.format(calendar.getTime());

        //Creates a unique key
        productID = saveCurrentDate + saveCurrentTime;

        //Unique Key for storing image
        StorageReference filePath = customerImagesReference.child(imageURI.getLastPathSegment() + productID + ".jpg");

        final UploadTask uploadTask = filePath.putFile(imageURI);

        uploadTask.addOnFailureListener(new OnFailureListener() {
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(CustomerUploadEnquiryActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Toast.makeText(CustomerUploadEnquiryActivity.this, "Image uploaded successfully.", Toast.LENGTH_SHORT).show();

                Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                    @Override
                    public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                        if(!task.isSuccessful()){
                            throw task.getException();
                        }

                        //Retrieving the image URI
                        downloadImageURL = filePath.getDownloadUrl().toString();
                        return filePath.getDownloadUrl();
                    }
                }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {
                        if(task.isSuccessful()){
                            //Getting image link
                            downloadImageURL = task.getResult().toString();
                            SaveProductInfoToDatabase();
                        }
                    }
                });
            }
        });
    }

    private void SaveProductInfoToDatabase(){
        String category = "Customer Enquiry";
        String price = "N/A";
        String enquiryStatus = "Pending";
        String name = "Enquiry Product";
        String quantity = "1";

        HashMap<String, Object> productMap = new HashMap<>();
        productMap.put("productID", productID);
        productMap.put("date", saveCurrentDate);
        productMap.put("time", saveCurrentTime);
        productMap.put("name", name);
        productMap.put("description", aCustomerDescription);
        productMap.put("size", aCustomerSize);
        productMap.put("comment", aCustomerComment);
        productMap.put("image", downloadImageURL);
        productMap.put("category", category);
        productMap.put("price", price);
        productMap.put("enquiryStatus", enquiryStatus);
        productMap.put("quantity", quantity);

        customerDesignReference
                .child(Common.currentUser.getPhone())
                .child(productID)
                .updateChildren(productMap)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    Intent intent = new Intent(CustomerUploadEnquiryActivity.this, CustomerSendEnquiryActivity.class);
                    intent.putExtra("productID", productID);
                    startActivity(intent);

                    progressDialog.dismiss();
                    Toast.makeText(CustomerUploadEnquiryActivity.this, Common.EnquiryUploadedSuccessKey, Toast.LENGTH_SHORT).show();
                } else {
                    progressDialog.dismiss();
                    Toast.makeText(CustomerUploadEnquiryActivity.this, Common.FailKey, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(CustomerUploadEnquiryActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}
