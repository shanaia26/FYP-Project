package com.example.fyp_project;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.fyp_project.Common.Common;
import com.example.fyp_project.Model.Users;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class ResetPasswordActivity extends AppCompatActivity {

    private String check = "";
    private TextView pageTitle;
    private TextView securityQuestionsTitle;
    private EditText findPhoneNumber;
    private EditText question1;
    private EditText question2;
    private Button verifyButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);

        check = getIntent().getStringExtra("check");

        pageTitle = findViewById(R.id.page_title);
        securityQuestionsTitle = findViewById(R.id.security_questions_title);
        findPhoneNumber = findViewById(R.id.find_phone_number);
        question1 = findViewById(R.id.question_1);
        question2 = findViewById(R.id.question_2);
        verifyButton = findViewById(R.id.verify_button);

    }

    @Override
    protected void onStart() {
        super.onStart();

        findPhoneNumber.setVisibility(View.GONE);


        //Check if user is coming from profile / login activity
        if (check.equals("profile")) {
            pageTitle.setText("Set Questions");
            securityQuestionsTitle.setText("Please set answers for the following security questions");
            verifyButton.setText("Set");

            displayPreviousAnswers();

            verifyButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    setAnswers();
                }
            });
        } else if (check.equals("login")) {
            findPhoneNumber.setVisibility(View.VISIBLE);

            verifyButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    verifyUser();
                }
            });

        }
    }

    private void setAnswers() {
        String answer1 = question1.getText().toString().toLowerCase();
        String answer2 = question2.getText().toString().toLowerCase();

        if (question1.equals("") && question2.equals("")) {
            Toast.makeText(ResetPasswordActivity.this, "Please answer both security questions", Toast.LENGTH_LONG).show();
        } else {
            DatabaseReference questionsReference =
                    FirebaseDatabase.getInstance().getReference()
                            .child("Users")
                            .child(Common.currentUser.getPhone());

            HashMap<String, Object> userDataMap = new HashMap<>();
            userDataMap.put("answer1", answer1);
            userDataMap.put("answer2", answer2);

            questionsReference.child("Security Questions").updateChildren(userDataMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        Toast.makeText(ResetPasswordActivity.this, "Security questions answered successfully", Toast.LENGTH_LONG).show();

                        Intent intent = new Intent(ResetPasswordActivity.this, MainActivity.class);
                        startActivity(intent);
                    }
                }
            });
        }
    }

    private void displayPreviousAnswers() {
        DatabaseReference questionsReference =
                FirebaseDatabase.getInstance().getReference()
                        .child("Users")
                        .child(Common.currentUser.getPhone());

        questionsReference.child("Security Questions").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    String answer1 = snapshot.child("answer1").getValue().toString();
                    String answer2 = snapshot.child("answer2").getValue().toString();

                    question1.setText(answer1);
                    question2.setText(answer2);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void verifyUser() {
        final String phone = findPhoneNumber.getText().toString();
        final String answer1 = question1.getText().toString().toLowerCase();
        final String answer2 = question2.getText().toString().toLowerCase();

        if (!phone.equals("") && !answer1.equals("") && !answer2.equals("")) {
            final DatabaseReference questionsReference =
                    FirebaseDatabase.getInstance().getReference()
                            .child("Users")
                            .child(phone);

            questionsReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()) {
                        String aPhone = snapshot.child("phone").getValue().toString();
                        if (phone.equals(aPhone)) {
                            if (snapshot.hasChild("Security Questions")) {
                                String ans1 = snapshot.child("Security Questions").child("answer1").getValue().toString();
                                String ans2 = snapshot.child("Security Questions").child("answer2").getValue().toString();

                                if (!ans1.equals(answer1)) {
                                    Toast.makeText(ResetPasswordActivity.this, "Your first answer is incorrect.", Toast.LENGTH_LONG).show();
                                } else if (!ans2.equals(answer2)) {
                                    Toast.makeText(ResetPasswordActivity.this, "Your second answer is incorrect.", Toast.LENGTH_LONG).show();
                                } else {
                                    //Allow user to change password
                                    AlertDialog.Builder builder = new AlertDialog.Builder(ResetPasswordActivity.this);
                                    builder.setTitle("New Password");

                                    final EditText newPassword = new EditText(ResetPasswordActivity.this);
                                    newPassword.setHint("Enter new password:");
                                    builder.setView(newPassword);

                                    builder.setPositiveButton("Change", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            if (!newPassword.getText().toString().equals("")) {
                                                //Change password
                                                questionsReference.child("password")
                                                        .setValue(newPassword.getText().toString())
                                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                            @Override
                                                            public void onComplete(@NonNull Task<Void> task) {
                                                                if (task.isSuccessful()) {
                                                                    Toast.makeText(ResetPasswordActivity.this, "Password changed successfully.", Toast.LENGTH_LONG).show();

                                                                    Intent intent = new Intent(ResetPasswordActivity.this, LoginActivity.class);
                                                                    startActivity(intent);
                                                                }
                                                            }
                                                        });

                                            }
                                        }
                                    });
                                    //Cancel Button
                                    builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int i) {
                                            dialog.cancel();
                                        }
                                    });
                                    builder.show();
                                }
                            }
                        } else {
                            Toast.makeText(ResetPasswordActivity.this, "Security questions have not been set.", Toast.LENGTH_LONG).show();
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        } else {
            Toast.makeText(this, "Please complete the form", Toast.LENGTH_LONG).show();
        }
    }
}