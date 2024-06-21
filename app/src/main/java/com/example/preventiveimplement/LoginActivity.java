package com.example.preventiveimplement;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class LoginActivity extends AppCompatActivity {

    private TextInputLayout tb_username,tb_password;
    private Button btn_login;
    private DatabaseReference databaseReference;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_login);

        tb_username = findViewById(R.id.tb_username);
        tb_password = findViewById(R.id.tb_password);
        btn_login = findViewById(R.id.login);

        databaseReference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://prevenit-72936-default-rtdb.asia-southeast1.firebasedatabase.app/");

        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = tb_username.getEditText().getText().toString();
                String password = tb_password.getEditText().getText().toString();

                databaseReference = FirebaseDatabase.getInstance().getReference("userLogin/");
                if (username.isEmpty() || username.isEmpty()){
                    Toast.makeText(getApplicationContext(), "Username atau Password tidak boleh kosong", Toast.LENGTH_SHORT).show();
                } else {
                    databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (snapshot.child(username).exists()){
                                if (snapshot.child(username).child("password").getValue(String.class).equals(password)){
                                    Toast.makeText(getApplicationContext(), "Welcome in PrevenIT", Toast.LENGTH_SHORT).show();
                                    Intent masuk = new Intent(getApplicationContext(), HomeActivity.class);
                                    startActivity(masuk);
                                } else {
                                    Toast.makeText(getApplicationContext(), "Username dan Password Salah", Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                Toast.makeText(getApplicationContext(), "Akun tidak ada", Toast.LENGTH_SHORT).show();
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