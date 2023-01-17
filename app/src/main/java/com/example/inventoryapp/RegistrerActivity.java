package com.example.inventoryapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

public class RegistrerActivity extends AppCompatActivity {

    private Button RegistreerButton;
    private EditText editTextNaamReg, editTextPasswordReg, editTextEmailReg;
    private ProgressBar progressBar;
    private ImageButton backButton;

    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrer);
        progressBar = findViewById(R.id.progressBar);
        editTextEmailReg = findViewById(R.id.editTextEmailReg);
        editTextNaamReg = findViewById(R.id.editTextNaamReg);
        editTextPasswordReg = findViewById(R.id.editTextPasswordReg);
        mAuth = FirebaseAuth.getInstance();
        backButton = findViewById(R.id.imageButtonReg);
        RegistreerButton = findViewById(R.id.RegistrerButton);

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RegistrerActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });
        RegistreerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               registreerUser();
            }
        });

    }

    private void registreerUser() {
        String email = editTextEmailReg.getText().toString().trim();
        String naam = editTextNaamReg.getText().toString().trim();
        String wachtwoord = editTextPasswordReg.getText().toString().trim();

        if(naam.isEmpty()){
            editTextNaamReg.setError("Naam is niet ingevuld!");
            editTextNaamReg.requestFocus();
            return;
        }
        if(email.isEmpty()){
            editTextEmailReg.setError("Email is niet ingevuld!");
            editTextEmailReg.requestFocus();
            return;
        }
        if(wachtwoord.length() < 6 ){
            editTextPasswordReg.setError("Wachtwoord is te kort!");
            editTextPasswordReg.requestFocus();
            return;
        }
        if(wachtwoord.isEmpty()){
            editTextPasswordReg.setError("Wachtwoord is niet ingevuld!");
            editTextPasswordReg.requestFocus();
            return;
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){ // gaat controleren of het een geldige email is
            editTextEmailReg.setError("Email is niet geldig!");
            editTextEmailReg.requestFocus();
            return;
        }
        progressBar.setVisibility(View.VISIBLE);
        mAuth.createUserWithEmailAndPassword(email,wachtwoord)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){ // dit betekent wanneer de gebruiker is gerigsteerd krijg je een succes
                            User user = new User(naam, email);

                            FirebaseDatabase.getInstance().getReference("Users")
                                    .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                    .setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()){
                                                Toast.makeText(RegistrerActivity.this, "Registratie is succesvol!", Toast.LENGTH_LONG).show();
                                                progressBar.setVisibility(View.GONE);
                                                Intent intent = new Intent(RegistrerActivity.this, MainActivity.class);
                                                startActivity(intent);
                                            }
                                            else {
                                                Toast.makeText(RegistrerActivity.this, "Registratie is niet gelukt!", Toast.LENGTH_LONG).show();
                                                progressBar.setVisibility(View.GONE);
                                            }
                                        }
                                    });

                        }else {
                            Toast.makeText(RegistrerActivity.this, "Registratie is niet gelukt!", Toast.LENGTH_LONG).show();
                            progressBar.setVisibility(View.GONE);

                        }
                    }

                });

    }
}
