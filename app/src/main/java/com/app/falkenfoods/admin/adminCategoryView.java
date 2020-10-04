package com.app.falkenfoods.admin;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.app.falkenfoods.R;

public class adminCategoryView extends AppCompatActivity {

    private ImageView pizza, bugerBun, noodles, rice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_category_view);

        pizza = findViewById(R.id.pizza);
        bugerBun = findViewById(R.id.buger);
        noodles = findViewById(R.id.noodless);
        rice = findViewById(R.id.rice);

        pizza.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(adminCategoryView.this, adminAddFood.class);
                intent.putExtra("Category", "Pizza");
                startActivity(intent);
            }
        });

        bugerBun.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            Intent intent = new Intent (adminCategoryView.this, adminAddFood.class);
            intent.putExtra("Category", "Buger Bun");
            startActivity(intent);
            }
        });

        noodles.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent (adminCategoryView.this, adminAddFood.class);
                intent.putExtra("Category", "Noodles");
                startActivity(intent);
            }
        });

        rice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(adminCategoryView.this, adminAddFood.class);
                intent.putExtra("Category", "Rice");
                startActivity(intent);
            }
        });

    }
}