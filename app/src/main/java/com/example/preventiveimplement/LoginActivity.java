package com.example.preventiveimplement;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.Firebase;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LoginActivity extends AppCompatActivity {

    private TextInputLayout username,password;
    private Button btn_login;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        username = findViewById(R.id.tb_username);
        password = findViewById(R.id.tb_password);
        btn_login = findViewById(R.id.login);

        databaseReference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://prevenit-72936-default-rtdb.asia-southeast1.firebasedatabase.app/");

        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String usernameCheck = username.getEditText().getText().toString();
                String passwordCheck = password.getEditText().getText().toString();

                databaseReference = FirebaseDatabase.getInstance().getReference("userLogin");
                if (usernameCheck.isEmpty() || passwordCheck.isEmpty()){
                    Toast.makeText(LoginActivity.this, "Username atau Password tidak boleh kosong", Toast.LENGTH_SHORT).show();
                } else {
                    databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (snapshot.child(usernameCheck).exists()){
                                if (snapshot.child(usernameCheck).child("password").getValue(String.class).equals(passwordCheck)){
                                    Toast.makeText(LoginActivity.this, "Welcome in PrevenIT", Toast.LENGTH_SHORT).show();
                                    Intent masuk = new Intent(getApplicationContext(), HomeActivity.class);
                                    startActivity(masuk);
                                } else {
                                    Toast.makeText(LoginActivity.this, "Username dan Password Salah", Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                Toast.makeText(LoginActivity.this, "Akun tidak ada=", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }
            }
        });
    }
}