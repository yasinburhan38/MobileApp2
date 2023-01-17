package com.example.inventoryapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ClipData;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.proto.ProtoOutputStream;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.SearchView;
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

import java.net.PortUnreachableException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class ProductActivity extends AppCompatActivity {

    private FirebaseUser user;
    private DatabaseReference ref;
    private ImageButton Logout;
    private ListView listView;
    private SearchView searchView;
    private List<Product> ProductList;
    private MyAdapter adapter1;
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
        searchView = findViewById(R.id.searchbar);
        listView.setTextFilterEnabled(true);
        ProductList = new ArrayList<>();



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


        ArrayList listnaam = new ArrayList<>();
        ArrayList listPrijs = new ArrayList<>();
        ArrayList listaantal = new ArrayList<>();
        ArrayList listProductcode = new ArrayList<>();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Producten")
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid());
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    Product product = dataSnapshot.getValue(Product.class);
                    ProductList.add(product);
                    listnaam.add(product.naam);
                    listPrijs.add(product.prijs);
                    listaantal.add(product.aantal);
                    listProductcode.add(product.productcode);
                }
                adapter1.notifyDataSetChanged();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });




        adapter1 = new MyAdapter(this, listnaam, listPrijs, listaantal,listProductcode);

        listView.setAdapter(adapter1);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Product product = (Product) ProductList.get(position);
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

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                //adapter1.getFilter().filter(newText);
                filterList(newText);
                return false;
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

    private void filterList(String text) {
        List<Product> filteredList=new ArrayList<>();
        ArrayList listnaam = new ArrayList<>();
        ArrayList listPrijs = new ArrayList<>();
        ArrayList listaantal = new ArrayList<>();
        ArrayList listProductcode = new ArrayList<>();
        for(Product item:ProductList){
            if(item.getNaam().toLowerCase().contains(text.toLowerCase()) || item.getProductcode().toLowerCase().contains(text.toLowerCase())){
                filteredList.add(item);
                listnaam.add(item.getNaam());
                listPrijs.add(item.getPrijs());
                listaantal.add(item.getAantal());
                listProductcode.add(item.getProductcode());
            }
        }
        if(filteredList.isEmpty()){
            Toast.makeText(this,"Zoekterm niet gevonden!",Toast.LENGTH_SHORT).show();
        }
        else{
            adapter1=new MyAdapter(this, listnaam, listPrijs, listaantal,listProductcode);

            listView.setAdapter(adapter1);
        }
    }


}
class MyAdapter extends ArrayAdapter<String>{
    Context context;
    ArrayList<String> naamP;
    ArrayList<String> prijs;
    ArrayList<String> aantal;
    ArrayList<String> code;

    MyAdapter(Context c , ArrayList<String> naam, ArrayList<String> Prijs,ArrayList<String> Aantal,ArrayList<String> Code){
        super(c,R.layout.list_items,naam);
        this.context =c;
        this.naamP = naam;
        this.prijs = Prijs;
        this.aantal = Aantal;
        this.code = Code;
    }
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater layoutInflater = (LayoutInflater) context.getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View row = layoutInflater.inflate(R.layout.list_items,parent,false);
        TextView naam = row.findViewById(R.id.textViewProductList);
        TextView Prijs = row.findViewById(R.id.textViewProductList2);
        TextView Aantal = row.findViewById(R.id.textViewProductList4);
        TextView Code = row.findViewById(R.id.textViewProductList3);
        naam.setText("Naam: "+naamP.get(position));
        Prijs.setText("Prijs: €"+prijs.get(position));
        Aantal.setText("Aantal: "+aantal.get(position));
        Code.setText("Productcode: "+code.get(position));
        return row;
    }
}


