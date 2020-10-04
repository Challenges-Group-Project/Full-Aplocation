package com.app.falkenfoods.admin;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.app.falkenfoods.Login;
import com.app.falkenfoods.R;
import com.app.falkenfoods.foodListView;

public class adminDashboard extends AppCompatActivity {
    Button addFoodBtn, foodMaintainBtn, logoutBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_dashboard);

        addFoodBtn = findViewById(R.id.addNewBtn);
        foodMaintainBtn = findViewById(R.id.maintainFood);
        logoutBtn = findViewById(R.id.logoutBtn);

        foodMaintainBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(adminDashboard.this, foodListView.class);
                intent.putExtra("Admin", "Admin");
                startActivity(intent);

            }
        });

        logoutBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(adminDashboard.this, Login.class);
                startActivity(intent);
            }
        });

        addFoodBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(adminDashboard.this,adminCategoryView.class);
                startActivity(intent);
            }
        });

    }
}


