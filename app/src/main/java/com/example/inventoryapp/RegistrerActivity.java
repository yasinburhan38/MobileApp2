package com.example.inventoryapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class RegistrerActivity extends AppCompatActivity {

    Button RegistreerButton ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrer);

        RegistreerButton = findViewById(R.id.RegistrerButton);
        RegistreerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RegistrerActivity.this, ProductActivity.class);
                startActivity(intent);
                String welcome = "Welcome In uw Inventory";
                Toast.makeText(getApplicationContext(), welcome, Toast.LENGTH_LONG).show();

            }
        });

    }
}
