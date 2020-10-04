package com.app.falkenfoods;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.app.falkenfoods.Required.Foods;
import com.app.falkenfoods.ViewHolder.foodViewHolder;
import com.app.falkenfoods.admin.adminMaintainFoods;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;


public class foodListView extends AppCompatActivity {

    private DatabaseReference foodRef;
    private RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    private String CategoryID = "";
    private String type = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_list_view);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        if(bundle!=null){
            type = getIntent().getExtras().get("Admin").toString();
        }
        CategoryID = getIntent().getStringExtra("fcategory");

        foodRef = FirebaseDatabase.getInstance().getReference().child("Foods");
        recyclerView = findViewById(R.id.recyclerViewFoodList);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseRecyclerOptions<Foods> options = new FirebaseRecyclerOptions.Builder<Foods>().setQuery(foodRef, Foods.class).build();

        FirebaseRecyclerAdapter<Foods, foodViewHolder> adapter = new FirebaseRecyclerAdapter<Foods, foodViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull foodViewHolder foodViewHolder, int i, @NonNull final Foods foods) {
                foodViewHolder.FoodNameTxt.setText(foods.getFname());
                foodViewHolder.FoodDescriptionTxt.setText(foods.getFdescriptiopn());
                foodViewHolder.FoodPriceTxt.setText("Price = Rs." + foods.getFprice());
                Picasso.get().load(foods.getFimage()).into(foodViewHolder.foodImageView);

                foodViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if(type.equals("Admin")) {
                            Intent intent = new Intent(foodListView.this, adminMaintainFoods.class);
                            intent.putExtra("fdid", foods.getFdid());
                            startActivity(intent);
                        }
                        else{
                            Intent intent = new Intent (foodListView.this, foodListDetailView.class );
                            intent.putExtra("fdid", foods.getFdid());
                            startActivity(intent);
                        }
                    }
                });
            }


            @NonNull
            @Override
            public foodViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_food_view,parent,false);
                foodViewHolder foodViewHolder = new foodViewHolder(view);
                return foodViewHolder;
            }
        };
        recyclerView.setAdapter(adapter);
        adapter.startListening();
    }
}