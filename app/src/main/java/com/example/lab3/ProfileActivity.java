package com.example.lab3;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;

public class ProfileActivity extends AppCompatActivity {

    static final int REQUEST_IMAGE_CAPTURE = 1;
    ImageButton mImageButton;
    public static final String ACTIVITY_NAME = "PROFILE_ACTIVITY";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.e(ACTIVITY_NAME, "In function: onCreate");

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        //This gets you the object nextPage from FirstActivity.java
        Intent intent = getIntent();

        EditText emailText = findViewById(R.id.emailInput);
        emailText.setText(intent.getStringExtra("EMAIL"));

        mImageButton = findViewById(R.id.imgButton);
        mImageButton.setOnClickListener( click -> {
            dispatchTakePictureIntent();
        });

    }

    @Override
    protected void onStart() {
        Log.e(ACTIVITY_NAME, "In function: onStart");
        super.onStart();
    }

    @Override
    protected void onResume() {
        Log.e(ACTIVITY_NAME, "In function: onResume");
        super.onResume();
    }

    @Override
    protected void onPause() {
        Log.e(ACTIVITY_NAME, "In function: onPause");
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        Log.e(ACTIVITY_NAME, "In function: onDestroy");
        super.onDestroy();
    }

    //sending data back to previous page
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.e(ACTIVITY_NAME, "In function: onActivityResult");
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            mImageButton.setImageBitmap(imageBitmap);
        }
    }

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }



}