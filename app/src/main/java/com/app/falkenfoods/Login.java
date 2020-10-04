package com.app.falkenfoods;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.app.falkenfoods.Required.User;
import com.app.falkenfoods.UserCommon.UserCommon;
import com.app.falkenfoods.admin.adminDashboard;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;


public class Login extends AppCompatActivity {
    EditText addPhoneNumber, addPassword;
    Button LoginBtn, SignUpBtn;
    ProgressDialog progressDialog;
    TextView AdminLogin, UserLogin;
    StorageReference storageReferance;
    String dbName = "users";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //Set Full screen
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_login);

        LoginBtn = findViewById(R.id.loginbtn);
        addPhoneNumber = findViewById(R.id.loginPhoneNumber);
        addPassword = findViewById(R.id.loginPassword);
        AdminLogin = findViewById(R.id.Admin_lgin);
        UserLogin = findViewById(R.id.userLogin);
        SignUpBtn = findViewById(R.id.signup);

        progressDialog = new ProgressDialog(this);

        //Open register activity
        SignUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                openregister();
            }
        });

        LoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
                LoginUser();
            }
        });


        //Admin Session Login
        AdminLogin.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               LoginBtn.setText("Login Admin");
               AdminLogin.setVisibility(View.INVISIBLE);
               UserLogin.setVisibility(View.VISIBLE);
               dbName = "Admin";
           }
       });

        //User Session Login
        UserLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LoginBtn.setText("Login User");
                AdminLogin.setVisibility(View.VISIBLE);
                UserLogin.setVisibility(View.INVISIBLE);
                dbName = "User";
            }
        });
    }

    private  void LoginUser() {
        String phoneNumber= addPhoneNumber.getText().toString();
        String password = addPassword.getText().toString();

        if (TextUtils.isEmpty(phoneNumber)) {
            Toast.makeText(this, "Please Enter your Phone Number...!", Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(password)) {
            Toast.makeText(this, "Please Enter your Password...!",Toast.LENGTH_SHORT).show();

        }
        else{
            progressDialog.setTitle("Grant Access User Account");
            progressDialog.setMessage("Wait moment...! We are checking your Account data in Database");
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.show();

            grantAccesstoAccount(phoneNumber,password);
        }
    }

    private void grantAccesstoAccount(final String phoneNumber, final String password) {
        final DatabaseReference databaseReference;
        databaseReference = FirebaseDatabase.getInstance().getReference();

        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if (snapshot.child(dbName).child(phoneNumber).exists()) {
                    User userData = snapshot.child(dbName).child(phoneNumber).getValue(User.class);

                   
                    if (userData.getPhoneNumber().equals(phoneNumber)) {

                        if (userData.getPassword().equals(password)) {

                            if (dbName.equals("Admin")) {
                                Toast.makeText(Login.this, "You Grant Access to Admin Panel", Toast.LENGTH_SHORT).show();
                                progressDialog.dismiss();

                                Intent intent = new Intent(Login.this, adminDashboard.class);
                                startActivity(intent);
                            }
                            else if (dbName.equals("User")) {
                                Toast.makeText(Login.this, "Access Successfully", Toast.LENGTH_SHORT).show();
                                progressDialog.dismiss();

                                Intent intent = new Intent(Login.this, Homemenu.class);
                                UserCommon.ActiveUser = userData;
                                startActivity(intent);
                            }
                        }
                        else {
                            progressDialog.dismiss();
                            Toast.makeText(Login.this, "Access Denied..! Password is incorrect", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
                else {
                    Toast.makeText(Login.this, "this " + phoneNumber + "Phone Number not Available Database...!", Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
    private void openregister() {
        Intent intent = new Intent(this, register.class);
        startActivity(intent);
    }


}