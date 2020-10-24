package com.example.fyp_project;

import android.os.Bundle;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    EditText email;
    EditText password;
    EditText failedLogin;
    EditText register;
    Button loginButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE); //will hide the title
        getSupportActionBar().hide(); // hide the title bar
        setContentView(R.layout.activity_main);

        email = (EditText) findViewById(R.id.email);
        password = (EditText) findViewById(R.id.password);
        failedLogin = (EditText) findViewById(R.id.error_text);
        register = (EditText) findViewById(R.id.register);
        loginButton =  (Button) findViewById(R.id.login_button);
    }
}