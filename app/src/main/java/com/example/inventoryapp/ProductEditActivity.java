package com.example.inventoryapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class ProductEditActivity extends AppCompatActivity {

    private TextView naam, code, prijs, aantal;
    private Button bewerken, verwijderen;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_edit);

        naam= findViewById(R.id.textViewNaamEdit);
        code = findViewById(R.id.textViewCodeEdit);
        prijs = findViewById(R.id.editTextPrijsEdit);
        aantal = findViewById(R.id.editTextAantalEdit);
        bewerken = findViewById(R.id.buttonEditItem);
        verwijderen= findViewById(R.id.buttonDeleteItem);

        Bundle bundle = getIntent().getExtras();
        Product product = (Product) bundle.getSerializable("PRODUCT");

        naam.setText(product.getNaam());
        code.setText(product.getProductcode());
        prijs.setText(product.getPrijs());
        aantal.setText(product.getAantal());

        verwijderen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String productnaam= naam.getText().toString();

                 FirebaseDatabase.getInstance().getReference().child("Producten")
                         .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                         .child(productnaam).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()){
                                    Toast.makeText(ProductEditActivity.this, "Product is succesvol verwijderd!",Toast.LENGTH_LONG).show();
                                    Intent intent = new Intent(ProductEditActivity.this, ProductActivity.class);
                                    startActivity(intent);
                                }
                                else{
                                    Toast.makeText(ProductEditActivity.this, "Er is iets mislukt!",Toast.LENGTH_LONG).show();
                                }
                            }
                        });

            }
        });

        bewerken.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String productnaam= naam.getText().toString();
                String aantalProduct= aantal.getText().toString();
                String prijsProduct = prijs.getText().toString();
                Map<String, Object> map=new HashMap<>();
                map.put("aantal", aantalProduct);
                map.put("prijs",prijsProduct);

                FirebaseDatabase.getInstance().getReference().child("Producten")
                        .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                        .child(productnaam).updateChildren(map)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if(task.isSuccessful()){
                                    Toast.makeText(ProductEditActivity.this, "Gegevens zijn begewerkt!",Toast.LENGTH_LONG).show();
                                    Intent intent = new Intent(ProductEditActivity.this, ProductActivity.class);
                                    startActivity(intent);
                                }
                                else{
                                    Toast.makeText(ProductEditActivity.this, "Er is iets misgegaan!",Toast.LENGTH_LONG).show();
                                }
                            }
                        });
            }
        });


    }
}