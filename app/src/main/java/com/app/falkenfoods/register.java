package com.app.falkenfoods;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;


public class register extends AppCompatActivity {

    Button register;
    EditText addName, addEmail, addPhoneNumber, addPassword, addConFirmPassword;
    ProgressDialog progressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        register = findViewById(R.id.registerBtn);
        addName = findViewById(R.id.name);
        addEmail = findViewById(R.id.email);
        addPhoneNumber = findViewById(R.id.phonenumber);
        addPassword = findViewById(R.id.registerpassword);
        addConFirmPassword = findViewById(R.id.confirmpassword);
        progressDialog = new ProgressDialog(this);

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerAccount();
            }
        });
    }

    private void registerAccount() {
        String name = addName.getText().toString();
        String email = addEmail.getText().toString();
        String phoneNumber = addPhoneNumber.getText().toString();
        String password = addPassword.getText().toString();
        String confirmPassword = addConFirmPassword.getText().toString();

        if (TextUtils.isEmpty(name)) {
            Toast.makeText(this, "Please Enter your Name...!", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(email)) {
            Toast.makeText(this, "PLease Enter your email...!", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(phoneNumber)) {
            Toast.makeText(this, "Please Enter your Phone NUmber...!", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(password)) {
            Toast.makeText(this, "Please Enter your Password", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(confirmPassword)) {
            Toast.makeText(this, "Please Enter your Confirm Password", Toast.LENGTH_SHORT).show();
        } else {
            progressDialog.setTitle("Register");
            progressDialog.setMessage("Please wait moment, We are  Registering your Account Credential");
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.show();

            validateAcc(name,email,phoneNumber, password);

        }
    }

    private void validateAcc(final String name, final String email,  final String phoneNumber, final String password) {
        final DatabaseReference databaseReference;
        databaseReference = FirebaseDatabase.getInstance().getReference();

        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (!(snapshot.child("User").child(phoneNumber).exists())) {
                    HashMap<String, Object> data = new HashMap<>();
                    data.put("Name", name);
                    data.put("Email",email);
                    data.put("PhoneNumber", phoneNumber);
                    data.put("Password", password);


                    databaseReference.child("User").child(phoneNumber).updateChildren(data).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(com.app.falkenfoods.register.this, "Account Creation Done", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(com.app.falkenfoods.register.this, Login.class);
                                startActivity(intent);
                            } else {
                                progressDialog.dismiss();
                                Toast.makeText(com.app.falkenfoods.register.this, "Something Went Wrong...!", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
                else {
                    Toast.makeText(com.app.falkenfoods.register.this, "This" +phoneNumber+ "already exits..!", Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                    Toast.makeText(com.app.falkenfoods.register.this,"Please Use another Phone Number",Toast.LENGTH_SHORT).show();

                    Intent intent = new Intent(com.app.falkenfoods.register.this,Login.class);
                    startActivity(intent);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}