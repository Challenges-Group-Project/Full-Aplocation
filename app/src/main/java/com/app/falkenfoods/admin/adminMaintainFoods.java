package com.app.falkenfoods.admin;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.app.falkenfoods.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.HashMap;

public class adminMaintainFoods extends AppCompatActivity {

    private Button applyBtn, deleteBtn;
    private ImageView imageView;
    private String foodId = "";
    private EditText name, price, description;

    private DatabaseReference foodRef;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_maintain_foods);

        foodId = getIntent().getStringExtra("fdid");
        foodRef = FirebaseDatabase.getInstance().getReference().child("Foods").child(foodId);

        applyBtn = findViewById(R.id.apply_btn);
        deleteBtn = findViewById(R.id.delete_btn);
        name = findViewById(R.id.food_maintain_name);
        description = findViewById(R.id.food_maintain_des);
        price = findViewById(R.id.food_maintain_price);
        imageView = findViewById(R.id.food_image_maintain);
        deleteBtn = findViewById(R.id.delete_btn);

        displayfoodInfo();

        applyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                apply();
            }
        });
        deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                delete();
            }
        });
    }

    private void delete() {
        foodRef.removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                Intent intent = new Intent(adminMaintainFoods.this, adminDashboard.class);
                startActivity(intent);
                finish();

                Toast.makeText(adminMaintainFoods.this,"Delete Process Successfully finished",Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void apply() {
        String fName = name.getText().toString();
        String fDescription = description.getText().toString();
        String fPrice = price.getText().toString();

        if (fName.equals("")){
            Toast.makeText(this,"Food Name Required..!", Toast.LENGTH_SHORT).show();
        }
        else if (fDescription.equals("")) {
            Toast.makeText(this, "Food Description Required..!",Toast.LENGTH_SHORT).show();
        }
        else if (fPrice.equals("")) {
            Toast.makeText(this, "Food Price Required..!",Toast.LENGTH_SHORT).show();
        }
        else{
            HashMap<String, Object>foodHashMap = new HashMap<>();
            foodHashMap.put("fdid", foodId);
            foodHashMap.put("fdescriptiopn", fDescription);
            foodHashMap.put("fprice",fPrice);
            foodHashMap.put("fname",fName);

            foodRef.updateChildren(foodHashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if(task.isSuccessful()){
                        Toast.makeText(adminMaintainFoods.this, "Your Changes Successfully Saved..! ", Toast.LENGTH_SHORT).show();

                        Intent intent = new Intent(adminMaintainFoods.this,adminDashboard.class);
                        startActivity(intent);
                        finish();
                    }
                }
            });
        }
    }

    private void displayfoodInfo() {
        foodRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    String fName = snapshot.child("fname").getValue().toString();
                    String fPrice = snapshot.child("fprice").getValue().toString();
                    String fDescription = snapshot.child("fdescriptiopn").getValue().toString();
                    String fImage = snapshot.child("fimage").getValue().toString();

                    name.setText(fName);
                    description.setText(fDescription);
                    price.setText(fPrice);
                    Picasso.get().load(fImage).into(imageView);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}