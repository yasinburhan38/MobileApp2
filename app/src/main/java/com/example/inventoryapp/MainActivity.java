package com.example.inventoryapp;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.util.Patterns;
import android.view.View;

import androidx.navigation.ui.AppBarConfiguration;

import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.inventoryapp.databinding.ActivityMainBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration appBarConfiguration;
    private ActivityMainBinding binding;
    private FirebaseAuth mAuth;
    private EditText editTextEmail, editTextPassword;
    private Button LoginButton  ;
    private Button RegistreerButton;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        mAuth = FirebaseAuth.getInstance() ;
        LoginButton = findViewById(R.id.LoginButton);
        RegistreerButton = findViewById(R.id.AddButton);
        editTextEmail = findViewById(R.id.editTextEmailLogin);
        editTextPassword= findViewById(R.id.editTextPasswordLogin);
        progressBar = findViewById(R.id.progressBar);



        RegistreerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, RegistrerActivity.class);
                startActivity(intent);
                String welcome = "Registratie";
                Toast.makeText(getApplicationContext(), welcome, Toast.LENGTH_LONG).show();

            }
        });
        LoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = editTextEmail.getText().toString().trim();
                String wachtwoord = editTextPassword.getText().toString().trim();

                if (email.isEmpty()){
                    editTextEmail.setError("Email is niet ingevuld!");
                    editTextEmail.requestFocus();
                    return;
                }
                if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
                    editTextEmail.setError("Email is niet correct!");
                    editTextEmail.requestFocus();
                    return;
                }
                if(wachtwoord.isEmpty()){
                    editTextPassword.setError("Wachtwoord is niet ingevuld!");
                    editTextPassword.requestFocus();
                    return;
                }
                if (wachtwoord.length()< 6){
                    editTextPassword.setError("Wachtwoord is te kort!");
                    editTextPassword.requestFocus();
                    return;
                }
                progressBar.setVisibility(View.VISIBLE);
                mAuth.signInWithEmailAndPassword(email,wachtwoord).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            Intent intent = new Intent(MainActivity.this, ProductActivity.class);
                            startActivity(intent);
                            progressBar.setVisibility(View.GONE);
                            Toast.makeText(MainActivity.this, "Login succesvol!", Toast.LENGTH_LONG).show();

                        }else{
                            progressBar.setVisibility(View.GONE);
                            Toast.makeText(MainActivity.this, "Login mislukt, email of wachtwoord is incorrect!", Toast.LENGTH_LONG).show();
                        }

                    }
                });


            }
        });




    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


}