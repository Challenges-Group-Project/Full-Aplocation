package com.app.falkenfoods;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.app.falkenfoods.UserCommon.UserCommon;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class order extends AppCompatActivity {
    private EditText orderName,orderPhone,OrderAddress,orderCity;
    private Button OrderPlcebtn,buyBtn;

    private String amount;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);

        amount=getIntent().getStringExtra("fprice");
        Toast.makeText(this,"Amount = Rs " + amount,Toast.LENGTH_SHORT).show();

        OrderPlcebtn=(Button) findViewById(R.id.place_order_btn);
        buyBtn=(Button) findViewById(R.id.buy_order);
        orderName=(EditText) findViewById(R.id.ordernametxt);
        orderPhone=(EditText) findViewById(R.id.order_mobile);
        OrderAddress=(EditText) findViewById(R.id.order_street);
        orderCity=(EditText) findViewById(R.id.order_city);

        OrderPlcebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                validate();

            }
        });
        buyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent=new Intent(order.this,OrderSuccessfullActivity.class);

                startActivity(intent);

            }
        });
    }

    private void validate() {
        if(TextUtils.isEmpty(orderName.getText().toString()))
        {
            Toast.makeText(this,"Please provide your full name.",Toast.LENGTH_SHORT).show();

        }
        else if(TextUtils.isEmpty(orderPhone.getText().toString()))
        {
            Toast.makeText(this,"Please provide your phone number.",Toast.LENGTH_SHORT).show();


        }

        else if(TextUtils.isEmpty(OrderAddress.getText().toString()))
        {
            Toast.makeText(this,"Please provide your address.",Toast.LENGTH_SHORT).show();

        }
        else if(TextUtils.isEmpty(orderCity.getText().toString()))
        {
            Toast.makeText(this,"Please provide your city.",Toast.LENGTH_SHORT).show();

        }
        else
        {

            validateOrder();
        }

    }

    private void validateOrder() {

        final String storeSvDate,storeSvTime;

        Calendar Date=Calendar.getInstance();
        SimpleDateFormat CuDate=new SimpleDateFormat("MMM dd, yyyy");
        storeSvDate=CuDate.format(Date.getTime());


        SimpleDateFormat currentTime=new SimpleDateFormat("HH:mm:ss");
        storeSvTime=CuDate.format(Date.getTime());

        final DatabaseReference ordersRef= FirebaseDatabase.getInstance().getReference()
                .child("Orders")
                .child(UserCommon.ActiveUser.getPhoneNumber());


        HashMap<String,Object> ordersMap=new HashMap<>();
        ordersMap.put("orderAmount",amount);
        ordersMap.put("orderName",orderName.getText().toString());
        ordersMap.put("phoneNumber",orderPhone.getText().toString());
        ordersMap.put("orderAddress",OrderAddress.getText().toString());
        ordersMap.put("homeTown",orderCity.getText().toString());
        ordersMap.put("date",storeSvDate);
        ordersMap.put("time",storeSvTime);
        ordersMap.put("state","Not Recived");

        ordersRef.updateChildren(ordersMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

                if(task.isSuccessful())
                {
                    FirebaseDatabase.getInstance().getReference()
                            .child("Cart List")
                            .child("User View")
                            .child(UserCommon.ActiveUser.getPhoneNumber())
                            .removeValue()
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {

                                    if(task.isSuccessful())
                                    {
                                        Toast.makeText(order.this,"Your final order has been placed successfully.",Toast.LENGTH_SHORT).show();

                                        Intent intent=new Intent(order.this,orderSuccessfully.class);
                                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                        startActivity(intent);
                                        finish();
                                    }

                                }
                            });


                }

            }
        });
    }
}