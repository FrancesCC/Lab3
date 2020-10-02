package com.example.lab3;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {

    SharedPreferences prefs = null;  //obj

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        prefs = getSharedPreferences("FileName", Context.MODE_PRIVATE);
        String savedString = prefs.getString("emailAdd", "Default Value");
        EditText typeField = findViewById(R.id.emailID);
        typeField.setText(savedString);

        Button saveButton = findViewById(R.id.logButton);

        saveButton.setOnClickListener(bt ->
                {
                    // creates a transition to load profileActivity.java
                    Intent goToProfile  = new Intent(MainActivity.this,ProfileActivity.class);
                    goToProfile.putExtra("EMAIL", savedString);
                    saveSharedPrefs(typeField.getText().toString());

                    startActivity(goToProfile);    //go to SecondActivity.java
                });
    }


    @Override
    protected void onPause() {
        super.onPause();

//        prefs = getSharedPreferences("FileName", Context.MODE_PRIVATE);
//        String savedString = prefs.getString("emailAdd", "Default Value");
//        EditText typeField = findViewById(R.id.emailID);
//        typeField.setText(savedString);
//
//        Button saveButton = findViewById(R.id.logButton);
//        saveButton.setOnClickListener(bt ->
//                {
//                    // creates a transition to load profileActivity.java
//                    Intent goToProfile  = new Intent(MainActivity.this,ProfileActivity.class);
//                    //goToProfile.putExtra("EMAIL",typeField.getText().toString());
//                    saveSharedPrefs(typeField.getText().toString());
//
//                    startActivity(goToProfile);    //go to SecondActivity.java
//                });
    }


    private void saveSharedPrefs(String stringToSave) {
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("emailAdd", stringToSave);
        editor.commit();
    }


}