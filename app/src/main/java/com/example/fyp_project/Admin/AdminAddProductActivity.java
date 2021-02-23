package com.example.fyp_project.Admin;

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

import com.example.fyp_project.R;
import com.example.fyp_project.StartActivity;
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

import io.paperdb.Paper;

public class AdminAddProductActivity extends AppCompatActivity {

    private String categoryName;
    private String productName;
    private String productDescription;
    private String productPrice;
    private String saveCurrentDate;
    private String saveCurrentTime;

    private String productRandomKey;
    private String downloadImageURL;

    private EditText newProductName;
    private EditText newProductDescription;
    private EditText newProductPrice;

    private ImageView newProductImage;
    private Button addProductButton;

    private static final int GalleryPick = 1;
    private Uri imageURI;

    private StorageReference productImagesReference;
    private DatabaseReference productReference;

    private Button exitButton;
    private ProgressDialog progressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_add_product);

        newProductName = findViewById(R.id.new_product_name);
        newProductDescription = findViewById(R.id.new_product_description);
        newProductPrice = findViewById(R.id.new_product_price);
        newProductImage = findViewById(R.id.new_product_image);
        addProductButton = findViewById(R.id.add_product_button);

        exitButton = findViewById(R.id.exit_button);

        progressDialog = new ProgressDialog(this);

        categoryName = getIntent().getExtras().get("category").toString();
        productImagesReference = FirebaseStorage.getInstance().getReference().child("Product Images");
        productReference = FirebaseDatabase.getInstance().getReference().child("Products");

        exitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Removes all saved user information. User has to log in again
                Paper.book().destroy();

                Intent intent = new Intent(AdminAddProductActivity.this, StartActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
            }
        });

        newProductImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OpenGallery();
            }
        });

        addProductButton.setOnClickListener(new View.OnClickListener() {
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
            newProductImage.setImageURI(imageURI);
        }
    }

    private void ValidateProductData(){
        productName = newProductName.getText().toString().trim();
        productDescription = newProductDescription.getText().toString().trim();
        productPrice = newProductPrice.getText().toString().trim();

        //Check if user added an image
        if(imageURI == null){
            Toast.makeText(this, "Product image required.", Toast.LENGTH_LONG).show();
        } else if (TextUtils.isEmpty(productName) || TextUtils.isEmpty(productDescription) || TextUtils.isEmpty(productPrice)){
            Toast.makeText(this, "Empty Fields.", Toast.LENGTH_LONG).show();
        } else{
            StoreProductInformation();
        }
    }

    private void StoreProductInformation(){
        progressDialog.setTitle("Add New Product");
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
        productRandomKey = saveCurrentDate + saveCurrentTime;

        //Unique Key for storing image
        StorageReference filePath = productImagesReference.child(imageURI.getLastPathSegment() + productRandomKey + ".jpg");

        final UploadTask uploadTask = filePath.putFile(imageURI);

        uploadTask.addOnFailureListener(new OnFailureListener() {
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(AdminAddProductActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Toast.makeText(AdminAddProductActivity.this, "Image uploaded successfully.", Toast.LENGTH_SHORT).show();

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

                            Toast.makeText(AdminAddProductActivity.this, "Got product image URL successfully.", Toast.LENGTH_SHORT).show();

                            SaveProductInfoToDatabase();
                        }
                    }
                });
            }
        });
    }

    private void SaveProductInfoToDatabase(){
        HashMap<String, Object> productMap = new HashMap<>();
        productMap.put("productID", productRandomKey);
        productMap.put("date", saveCurrentDate);
        productMap.put("time", saveCurrentTime);
        productMap.put("name", productName);
        productMap.put("description", productDescription);
        productMap.put("price", productPrice);
        productMap.put("image", downloadImageURL);
        productMap.put("category", categoryName);

        productReference.child(productRandomKey).updateChildren(productMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    Intent intent = new Intent(AdminAddProductActivity.this, AdminCategoryActivity.class);
                    startActivity(intent);

                    progressDialog.dismiss();
                    Toast.makeText(AdminAddProductActivity.this, "Product is added successfully", Toast.LENGTH_SHORT).show();
                } else {
                    progressDialog.dismiss();
                    String message = task.getException().toString();
                    Toast.makeText(AdminAddProductActivity.this, "Error: " + message, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
