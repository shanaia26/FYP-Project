package com.example.fyp_project;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.fyp_project.Common.Common;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class ProfileActivity extends AppCompatActivity {
    private TextView profileName;
    private TextView profileEmail;
    private TextView profilePhone;

    private String name;
    private String email;
    private String phone;

    private Button updateProfileButton;
    private Button securityQuestionsButton;
    private TextView removeButton;

    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        profileName = findViewById(R.id.profile_name);
        profileEmail = findViewById(R.id.profile_email);
        profilePhone = findViewById(R.id.profile_phone);

        updateProfileButton = findViewById(R.id.update_profile_button);
        securityQuestionsButton = findViewById(R.id.security_questions_button);
        removeButton = findViewById(R.id.profile_remove);

        progressDialog = new ProgressDialog(this);


        Intent intent = getIntent();
        name = intent.getStringExtra("name");
        email = intent.getStringExtra("email");
        phone = intent.getStringExtra("phone");


        profileName.setText(name);
        profileEmail.setText(email);
        profilePhone.setText(phone);

        // Remove user profile from the database
        removeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users").child(Common.currentUser.getPhone());
                reference.removeValue();
                Toast.makeText(ProfileActivity.this, "Account Deleted", Toast.LENGTH_SHORT).show();

                startActivity(new Intent(ProfileActivity.this, StartActivity.class));
            }
        });

        securityQuestionsButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProfileActivity.this, ResetPasswordActivity.class);
                intent.putExtra("check", "profile");
                startActivity(intent);
            }
        });

        //Updates the changes made by the user in the database
        updateProfileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                progressDialog.setTitle("Update Profile");
                progressDialog.setMessage("Please wait...");
                progressDialog.setCanceledOnTouchOutside(false);
                progressDialog.show();

                //Store it for that specific user
                DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Users");

                //Get all the values inputted by the user
                HashMap<String, Object> userMap = new HashMap<>();
                userMap.put("name", profileName.getText().toString());
                userMap.put("email", profileEmail.getText().toString());
                userMap.put("phoneOrder", profilePhone.getText().toString());

                //Update Info
                reference.child(Common.currentUser.getPhone()).updateChildren(userMap);
                progressDialog.dismiss();

                startActivity(new Intent(ProfileActivity.this, MainActivity.class));
                Toast.makeText(ProfileActivity.this, "Profile info updated.", Toast.LENGTH_LONG).show();
                finish();
            }
        });
    }
}
