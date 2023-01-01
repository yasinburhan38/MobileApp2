package com.example.inventoryapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.nio.BufferUnderflowException;
import java.util.ArrayList;

public class ProductActivity extends AppCompatActivity {

    private FirebaseUser user;
    private DatabaseReference ref;
    private ImageButton Logout;
    private ListView listView;


    private String UserId; // we gebruiken userId omdat iedereen die zich registreerd krijgt een UserId waarmee we kunnen vinden wie er heeft ingolgd

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product);
        user = FirebaseAuth.getInstance().getCurrentUser();
        ref = FirebaseDatabase.getInstance().getReference("Users");
        UserId = user.getUid();
        final TextView welkomTextview = findViewById(R.id.textViewUserName);
        Logout = findViewById(R.id.imageButton);
        listView= findViewById(R.id.ListViewProduct);

        ref.child(UserId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User user = snapshot.getValue(User.class);

                if( user!=null ){
                    String Naam = user.naam;

                    welkomTextview.setText("Welkom, "+Naam +"!");
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(ProductActivity.this, "Er is iets four gegaan!", Toast.LENGTH_LONG).show();
            }
        });


        Logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AlertDialog.Builder builder = new AlertDialog.Builder(ProductActivity.this);
                builder.setMessage("Bent u zeker dat u wilt uitloggen?");
                builder.setCancelable(true);
                builder.setNegativeButton("Ja", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        FirebaseAuth.getInstance().signOut();


                        Intent intent = new Intent(ProductActivity.this, MainActivity.class);
                        startActivity(intent);
                    }
                });
                builder.setPositiveButton("Nee", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        return;
                    }
                });
                AlertDialog alertDialog = builder.create();
                alertDialog.show();


            }
        });

        final ArrayList list = new ArrayList<>();
        final ArrayList list1 = new ArrayList<>();
        final ArrayAdapter adapter = new ArrayAdapter<>(this, R.layout.list_items, list1);
        listView.setAdapter(adapter);

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Producten")
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid());

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                list1.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    Product product = dataSnapshot.getValue(Product.class);
                    list.add(product);

                    String productnaam = product.getNaam();
                    String productCode = product.getProductcode();
                    String productAantal = product.getAantal();
                    String productPrijs = product.getPrijs();
                    String txt = "Naam: "+productnaam + ", Code:"+ productCode +"\n"+ "Aantal: "+productAantal+", ProductPrijs: "+ productPrijs;

                    list1.add(txt);
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Product product = (Product) list.get(position);
                String naam = product.getNaam();

                Bundle bundle = new Bundle();
                bundle.putSerializable("PRODUCT",product);
                Intent intent = new Intent(ProductActivity.this, ProductEditActivity.class);
                intent.putExtras(bundle);
                startActivity(intent);
                String info = naam;
                Toast.makeText(ProductActivity.this, info,Toast.LENGTH_LONG).show();

            }
        });



        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ProductActivity.this, AddProductActivity.class);
                startActivity(intent);
                String welcome = "Product Toevoegen";
                Toast.makeText(getApplicationContext(), welcome, Toast.LENGTH_LONG).show();
            }
        });
    }
}