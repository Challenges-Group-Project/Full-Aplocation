package com.app.falkenfoods;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.app.falkenfoods.Required.Foods;
import com.app.falkenfoods.UserCommon.UserCommon;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class foodListDetailView extends AppCompatActivity {

    private ImageView foodImage;
    private TextView foodPrice, foodName, foodDes;
    private Button addTOCartBtn, messageBtn, cart;
    private String foodId = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_list_detail_view);

        foodId = getIntent().getStringExtra("fdid");
        addTOCartBtn = findViewById(R.id.addToCartBtn);
        messageBtn = findViewById(R.id.messageBtn);
        //cart = findViewById(R.id.cartBtn);
        foodImage = findViewById(R.id.food_image_view);
        foodPrice = findViewById(R.id.food_price);
        foodName = findViewById(R.id.food_name);
        foodDes = findViewById(R.id.food_des);

        accessFoodInformation(foodId);

        addTOCartBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addtoCartArray();
                //Intent intent = new Intent(foodListDetailView.this, CartActivity.class);
                //startActivity(intent);
            }

        });

        messageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(foodListDetailView.this, ChatHome.class);
                startActivity(intent);
            }

        });

        cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(foodListDetailView.this, cartList.class);
                startActivity(intent);
            }

        });

    }

    public void OnStart() {
        super.onStart();
    }


    private void addtoCartArray(){
        String saveTime, saveDate;

        Calendar calDate = Calendar.getInstance();
        SimpleDateFormat Date = new SimpleDateFormat("MM, dd, yyyy");
        saveDate = Date.format(calDate.getTime());

        SimpleDateFormat Time = new SimpleDateFormat("HH:mm:ss");
        saveTime = Date.format(calDate.getTime());


        final DatabaseReference cartRef = FirebaseDatabase.getInstance().getReference().child("Cart Array");

        final HashMap<String, Object> cartMap = new HashMap<>();
        cartMap.put("fdid", foodId);
        cartMap.put("fname", foodName.getText().toString());
        cartMap.put("fprice", foodPrice.getText().toString());
        cartMap.put("date", saveDate);
        cartMap.put("time", saveTime);
        //cartMap.put("quantity",qty);
        //cartMap.put("discount","");

        cartRef.child("UserSee").child(UserCommon.ActiveUser.getPhoneNumber())
                .child("Foods").child(foodId)
                .updateChildren(cartMap)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            cartRef.child("AdminSee").child(UserCommon.ActiveUser.getPhoneNumber())
                                    .child("Foods").child(foodId)
                                    .updateChildren(cartMap)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                Toast.makeText(foodListDetailView.this,"Processed Completed, Add to Cart Operation Done", Toast.LENGTH_SHORT).show();

                                                Intent intent = new Intent(foodListDetailView.this, foodListView.class);
                                                startActivity(intent);

                                            }
                                        }
                                    });
                        }
                    }
                });
    }

    private void accessFoodInformation(final String foodId){
        DatabaseReference foodRef = FirebaseDatabase.getInstance().getReference().child("Foods");

        foodRef.child(foodId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    Foods foods = snapshot.getValue(Foods.class);

                    foodName.setText("Name : " + foods.getFname());
                    foodPrice.setText("Price : Rs." + foods.getFprice());
                    foodDes.setText("Description: " + foods.getFdescriptiopn());
                    Picasso.get().load(foods.getFimage()).into(foodImage);
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {

            }
        });
    }

}