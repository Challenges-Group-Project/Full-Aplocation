package com.app.falkenfoods.admin;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.app.falkenfoods.R;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class adminAddFood extends AppCompatActivity {
    private String categoryname, description, price, fName, currentTime, currentDate;
    private Button addfoodbtn;
    private ImageView foodimage;
    private EditText foodname, fooddescription, foodprice;
    private static final int GalleryPick = 1;
    private Uri ImageUri;
    private String foodrandomid, downloadUrl;
    private DatabaseReference databaseReference;
    private StorageReference foodimageref;
    private ProgressDialog loadingBar;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adminaddfood);

        categoryname = getIntent().getExtras().get("Category").toString();
        foodimageref = FirebaseStorage.getInstance().getReference().child("Food Images");
        databaseReference = FirebaseDatabase.getInstance().getReference().child("Foods");

        addfoodbtn = findViewById(R.id.addFoodBtn);
        foodname = findViewById(R.id.foodName);
        fooddescription = findViewById(R.id.foodDescription);
        foodimage = findViewById(R.id.addfoodImage);
        foodprice = findViewById(R.id.price);
        loadingBar = new ProgressDialog(this);

        foodimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OpenGallery();
            }
        });

        addfoodbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validateFoodData();
            }
        });

    }

    private void OpenGallery() {
        Intent gallery = new Intent();
        gallery.setAction(Intent.ACTION_GET_CONTENT);
        gallery.setType("image/*");
        startActivityForResult(gallery, GalleryPick);

    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==GalleryPick && resultCode==RESULT_OK && data!=null){
            ImageUri = data.getData();
            foodimage.setImageURI(ImageUri);
        }
    }

    private void validateFoodData() {
        description = fooddescription.getText().toString();
        price = foodprice.getText().toString();
        fName = foodname.getText().toString();

        if(ImageUri == null){
            Toast.makeText(this, "Food Image is can't be Empty", Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(description)){
            Toast.makeText(this, "Please Enter Food Description", Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(price)){
            Toast.makeText(this, "Please Enter Food Price", Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(fName)){
            Toast.makeText(this, "Please Enter Food Name", Toast.LENGTH_SHORT).show();
        }
        else{
            SaveImageDetails();
        }
    }

    private void SaveImageDetails() {
        loadingBar.setTitle("Processing");
        loadingBar.setMessage("Please Wait moment while we are storing food Details");
        loadingBar.setCanceledOnTouchOutside(false);
        loadingBar.show();

        Calendar calendar = Calendar.getInstance();

        SimpleDateFormat date = new SimpleDateFormat("MM, DD,YYYY");
        currentDate = date.format(calendar.getTime());

        SimpleDateFormat time = new SimpleDateFormat("HH:MM::SS");
        currentTime = time.format(calendar.getTime());

        foodrandomid = currentDate + currentTime;

        final StorageReference path = foodimageref.child(ImageUri.getLastPathSegment() + foodrandomid + "jpg");

        final UploadTask uploadTask = path.putFile(ImageUri);

        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                String message = e.toString();
                Toast.makeText(adminAddFood.this, "Error : " + message, Toast.LENGTH_SHORT).show();
                loadingBar.dismiss();
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Toast.makeText(adminAddFood.this, "Process Done Successfully", Toast.LENGTH_SHORT).show();

                Task<Uri> urlprocess = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                    @Override
                    public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                        if(!task.isSuccessful()){
                            throw task.getException();
                        }
                        downloadUrl = path.getDownloadUrl().toString();
                        return path.getDownloadUrl();
                    }
                }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {
                        if(task.isSuccessful()){
                            downloadUrl = task.getResult().toString();
                            Toast.makeText(adminAddFood.this,"Food image URL Captured successfully.",Toast.LENGTH_SHORT).show();
                            saveFoodInformationDB();
                        }
                    }
                });
            }
        });

    }
    private void saveFoodInformationDB() {
        HashMap<String, Object> foodMap = new HashMap<>();
        foodMap.put("fdid", foodrandomid);
        foodMap.put("date", currentDate);
        foodMap.put("time", currentTime);
        foodMap.put("fdescriptiopn",description);
        foodMap.put("fimage",downloadUrl);
        foodMap.put("fcategory",categoryname);
        foodMap.put("fprice",price);
        foodMap.put("fname",fName);


        databaseReference.child(foodrandomid).updateChildren(foodMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    Intent intent = new Intent(adminAddFood.this,adminCategoryView.class);
                    startActivity(intent);

                    loadingBar.dismiss();
                    Toast.makeText(adminAddFood.this, "Insert Process Done Successfully", Toast.LENGTH_SHORT).show();
                }
                else{
                    loadingBar.dismiss();
                    String message = task.getException().toString();
                    Toast.makeText(adminAddFood.this, "Error : " + message, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}