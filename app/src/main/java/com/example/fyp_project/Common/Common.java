package com.example.fyp_project.Common;

import android.graphics.Color;
import android.view.MotionEvent;

import com.example.fyp_project.Model.AdminOrderHistory;
import com.example.fyp_project.Model.Users;

public class Common {
    public static Users currentUser;
    public static int COLOUR_SELECTED = Color.RED;
    public static MotionEvent EVENT_USED;

    public static final String UserPhoneKey = "UserPhone";
    public static final String UserPasswordKey = "UserPassword";

    public static final String EmptyCredentialsKey = "Please provide full details.";
    public static final String PasswordFailKey = "Password failed, please try again.";
    public static final String RegisterSuccessKey = "Congratulations! Your account has been made successfully.";
    public static final String RegisterFailKey = "Something went wrong, Please try again.";
    public static final String PhoneExistsKey = "Account with this phone number already exists.";
    public static final String TryAgainPhoneKey = "Please try again using another phone number.";
    public static final String LoginSuccessKey = "Logged in successfully.";
    public static final String LoginFailKey = "Account does not exist.";
    public static final String AccountDeletedKey = "Account Deleted.";
    public static final String AccountUpdatedKey = "Account Updated.";
    public static final String SecurityQuestionsSuccessKey = "Security questions answered successfully.";
    public static final String SecurityQuestionsFailKey = "Incorrect answer, please try again.";
    public static final String IncorrectAnswerKey = "Incorrect answer, please try again.";
    public static final String PasswordChangedSuccessKey = "Password changed successfully.";

}
