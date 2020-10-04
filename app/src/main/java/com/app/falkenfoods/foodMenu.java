package com.app.falkenfoods;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class foodMenu extends AppCompatActivity {

    TextView foodmenu;
    ImageView Pizza, BugerBun, Noodles,Rice;
    TextView Pizzatxt, BugerBuntxt, Noodlestxt, Ricetxt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_menu);

        foodmenu = findViewById(R.id.foodMenu);
        foodmenu.setText("FoodMenu");

        Pizzatxt = findViewById(R.id.pizzaTxt);
        BugerBuntxt = findViewById(R.id.bugerBunTxt);
        Noodlestxt = findViewById(R.id.noodlessTxt);
        Ricetxt = findViewById(R.id.riceTxt);

        Pizza = findViewById(R.id.pizza);
        BugerBun = findViewById(R.id.buger);
        Noodles = findViewById(R.id.noodless);
        Rice = findViewById(R.id.rice);

        Pizza.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(foodMenu.this, foodListView.class);
                startActivity(intent);
            }
        });

        BugerBun.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(foodMenu.this, foodListView.class);
                startActivity(intent);
            }
        });

        Noodles.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(foodMenu.this, foodListView.class);
                startActivity(intent);
            }
        });

        Rice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(foodMenu.this, foodListView.class);
                startActivity(intent);
            }
        });

    }
    public void foodDetailView (View view){
        Intent intent1 = new Intent(foodMenu.this, foodListView.class);
        startActivity(intent1);
     }
}