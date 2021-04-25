package com.example.fyp_project;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.fyp_project.Admin.AdminMainActivity;
import com.example.fyp_project.Common.Common;
import com.example.fyp_project.Model.Users;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import io.paperdb.Paper;

public class LoginActivity extends AppCompatActivity {
    private EditText phone;
    private EditText password;
    private Button loginButton;

    private TextView forgotPasswordLink;
    private TextView register;
    private TextView adminLink;
    private TextView notAdminLink;

    private ProgressDialog progressDialog;

    private String parentDBName = "Users";
    private CheckBox checkBoxRememberMe;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        phone = findViewById(R.id.phone);
        password = findViewById(R.id.password);
        loginButton = findViewById(R.id.login_button);
        register = findViewById(R.id.register_user);

        forgotPasswordLink = findViewById(R.id.forgot_password_link);
        adminLink = findViewById(R.id.admin_panel_link);
        notAdminLink = findViewById(R.id.not_admin_panel_link);

        progressDialog = new ProgressDialog(this);

        checkBoxRememberMe = findViewById(R.id.remember_me_checkBox);
        Paper.init(this);

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
            }
        });

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoginUser();
            }
        });

        forgotPasswordLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, ResetPasswordActivity.class);
                intent.putExtra("check", "login");
                startActivity(intent);
            }
        });

        adminLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginButton.setText("Login Admin");
                adminLink.setVisibility(View.INVISIBLE);
                notAdminLink.setVisibility(View.VISIBLE);
                parentDBName = "Admin";
            }
        });

        notAdminLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginButton.setText("Login");
                adminLink.setVisibility(View.VISIBLE);
                notAdminLink.setVisibility(View.INVISIBLE);
                parentDBName = "Users";
            }
        });


    }

    private void LoginUser() {
        String uPhone = phone.getText().toString().trim();
        String uPassword = password.getText().toString().trim();

        if (TextUtils.isEmpty(uPhone) || TextUtils.isEmpty(uPassword)) {
            Toast.makeText(LoginActivity.this, Common.EmptyCredentialsKey, Toast.LENGTH_SHORT).show();
        } else if (password.length() < 6) {
            Toast.makeText(LoginActivity.this, Common.PasswordFailKey, Toast.LENGTH_SHORT).show();
        } else {
            //Allow user to log in
            progressDialog.setTitle("Login Account");
            progressDialog.setMessage("Please wait...");
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.show();

            AllowAccessToAccount(uPhone, uPassword);
        }
    }

    private void AllowAccessToAccount(String phone, String password) {
        if(checkBoxRememberMe.isChecked()){
            //Remember user info
            Paper.book().write(Common.UserPhoneKey, phone);
            Paper.book().write(Common.UserPasswordKey, password);
        }

        final DatabaseReference rootReference;
        rootReference = FirebaseDatabase.getInstance().getReference();

        rootReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.child(parentDBName).child(phone).exists()) {
                    Users userData = snapshot.child(parentDBName).child(phone).getValue(Users.class);

                    if (userData.getPhone().equals(phone)) {
                        if (userData.getPassword().equals(password)) {
                            if(parentDBName.equals("Admin")){
                                Toast.makeText(LoginActivity.this, Common.LoginSuccessKey, Toast.LENGTH_SHORT).show();
                                progressDialog.dismiss();

                                Intent intent = new Intent(LoginActivity.this, AdminMainActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(intent);
                                finish();
                            } else if(parentDBName.equals("Users")) {
                                Toast.makeText(LoginActivity.this, Common.LoginSuccessKey, Toast.LENGTH_SHORT).show();
                                progressDialog.dismiss();

                                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                Common.currentUser = userData;
                                startActivity(intent);
                                finish();
                            }
                        }
                    }
                } else {
                    Toast.makeText(LoginActivity.this, Common.LoginFailKey, Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}