package com.example.fyp_project;

import android.os.Bundle;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RegisterActivity extends AppCompatActivity {
    EditText firstname;
    EditText lastname;
    EditText address;
    EditText email;
    EditText password;
    Button register;

    DatabaseReference customerDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE); //will hide the title
        getSupportActionBar().hide(); // hide the title bar
        setContentView(R.layout.activity_main);

        firstname = (EditText) findViewById(R.id.first_name);
        lastname = (EditText) findViewById(R.id.last_name);
        address = (EditText) findViewById(R.id.address);
        email = (EditText) findViewById(R.id.email_address);
        password =  (EditText) findViewById(R.id.password);
        register = (Button) findViewById(R.id.register_button);
        customerDB = FirebaseDatabase.getInstance().getReference("Customer");

    }
}
