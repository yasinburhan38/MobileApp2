package com.example.inventoryapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

public class AddProductActivity extends AppCompatActivity {

    private ImageButton backButton ;
    private Button productButton;
    private ProgressBar progressBar;
    private EditText naamEditText, productCode, aantalEditText, prijsEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_product);

        backButton = findViewById(R.id.imageButtonBack);
        productButton = findViewById(R.id.AddButton);
        naamEditText = findViewById(R.id.editTextNaamProduct);
        productCode = findViewById(R.id.editTextCode);
        aantalEditText = findViewById(R.id.editTextAantal);
        prijsEditText = findViewById(R.id.editTextPrijs);
        progressBar = findViewById(R.id.progressBar2);


        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AddProductActivity.this, ProductActivity.class);
                startActivity(intent);
                Toast.makeText(AddProductActivity.this, "Inventory", Toast.LENGTH_LONG).show();
            }
        });

        productButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                productToevoegen();
            }
        });




    }

    private void productToevoegen() {
        String naam = naamEditText.getText().toString().trim();
        String code = productCode.getText().toString().trim();
        String aantal = aantalEditText.getText().toString().trim();
        String prijs = prijsEditText.getText().toString().trim();

        if(naam.isEmpty()){
            naamEditText.setError("Naam is niet ingevuld!");
            naamEditText.requestFocus();
        }
        if (code.isEmpty()){
            productCode.setError("Productcode is niet ingevuld!");
            productCode.requestFocus();
        }
        if (aantal.isEmpty()){
            aantalEditText.setError("Aantal is niet ingevuld!");
            aantalEditText.requestFocus();
        }
        if (prijs.isEmpty()){
            prijsEditText.setError("Prijs is niet ingevuld!");
            prijsEditText.requestFocus();
        }
        Product product = new Product(naam, code, aantal, prijs);
        progressBar.setVisibility(View.VISIBLE);
        FirebaseDatabase.getInstance().getReference("Producten")
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .child(product.naam)
                .setValue(product).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()){
                            progressBar.setVisibility(View.GONE);
                            Toast.makeText(AddProductActivity.this, "Product is toegevoegd!", Toast.LENGTH_LONG).show();
                            Intent intent = new Intent(AddProductActivity.this, ProductActivity.class);
                            startActivity(intent);


                        }
                        else {
                            progressBar.setVisibility(View.GONE);
                            Toast.makeText(AddProductActivity.this, "Toevoegen is mislukt!", Toast.LENGTH_LONG).show();

                        }
                    }
                });

    }
}