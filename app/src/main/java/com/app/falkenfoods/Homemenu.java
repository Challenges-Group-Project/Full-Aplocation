package com.app.falkenfoods;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.Toolbar;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import com.app.falkenfoods.ui.cart.CartFragment;
import com.app.falkenfoods.ui.home.HomeFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.synnapps.carouselview.CarouselView;
import com.synnapps.carouselview.ImageClickListener;
import com.synnapps.carouselview.ImageListener;

public class Homemenu extends AppCompatActivity {

    /*private int[] sampleImages = new int[]{
            R.drawable.pizzvec, R.drawable.ricevec, R.drawable.noodlesvec
    };
    private String[]imageTitle = new String[]{
        "Pizzz","Rice","Noodles"
    };*/
    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homemenu);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        BottomNavigationView navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        /*AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_notifications)
                .build();*/
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        //.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(navView, navController);


        /*CarouselView  carouselView = findViewById(R.id.carouselView);
        carouselView.setPageCount(sampleImages.length);
        carouselView.setImageListener(new ImageListener() {
            @Override
            public void setImageForPosition(int position, ImageView imageView) {
                imageView.setImageResource(sampleImages[position]);

            }
        });

        carouselView.setImageClickListener(new ImageClickListener() {

            @Override
            public void onClick(int position) {
                Toast.makeText(Homemenu.this,imageTitle[position],Toast.LENGTH_SHORT).show();

            }
        });
    }*/
        BottomNavigationView navigationView = findViewById(R.id.nav_view);

        navigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                int i = item.getItemId();

                if (i == R.id.Home) {
                    HomeFragment fragment = new HomeFragment();
                    FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                    fragmentTransaction.replace(R.id.flayout, fragment);
                    fragmentTransaction.commit();

                }
                if (i == R.id.navigation_cart) {
                    CartFragment fragment = new CartFragment();
                    FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                    fragmentTransaction.replace(R.id.fcartlayout, fragment);
                    Intent intent = new Intent(Homemenu.this, cartList.class);
                    startActivity(intent);

                    return false;
                }
                if (i == R.id.navigation_dashboard) {
                    CartFragment fragment = new CartFragment();
                    FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                    fragmentTransaction.replace(R.id.fcartlayout, fragment);
                    Intent intent = new Intent(Homemenu.this, foodMenu.class);
                    startActivity(intent);

                    return false;
                }
                return false;
            }

        });

    }
}