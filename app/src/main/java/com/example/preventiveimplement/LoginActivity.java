package com.example.preventiveimplement;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;


public class LoginActivity extends AppCompatActivity {

    private TextInputLayout tb_username,tb_password;
    private Button btn_login, btn_regis;
    private TextView tLogin, tRegis;
    private CardView cLogin, cRegis;
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
        btn_regis = findViewById(R.id.regis);

        tLogin = findViewById(R.id.textView_login);
        tRegis = findViewById(R.id.textView_regis);
        cLogin = findViewById(R.id.cLogin);
        cRegis = findViewById(R.id.cRegis);

        Intent intent = getIntent();
        String loginOrRegis = intent.getStringExtra("loginOrRegis");
        String roleUser = intent.getStringExtra("roleUser");

        databaseReference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://prevenit-72936-default-rtdb.asia-southeast1.firebasedatabase.app/");

        if ("login".equals(loginOrRegis)){
            btn_regis.setVisibility(View.GONE);
            tRegis.setVisibility(View.GONE);
            cRegis.setVisibility(View.GONE);
        } else if ("register".equals(loginOrRegis)){
            btn_login.setVisibility(View.GONE);
            tLogin.setVisibility(View.GONE);
            cLogin.setVisibility(View.GONE);
        } else if ("admin".equals(roleUser)){
            btn_login.setVisibility(View.GONE);
            tLogin.setVisibility(View.GONE);
            cLogin.setVisibility(View.GONE);
        }

        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = tb_username.getEditText().getText().toString();
                String password = tb_password.getEditText().getText().toString();

                databaseReference = FirebaseDatabase.getInstance().getReference("userLogin/");
                if (username.isEmpty() || password.isEmpty()){
                    Toast.makeText(getApplicationContext(), "Username atau Password tidak boleh kosong", Toast.LENGTH_SHORT).show();
                } else {
                    databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (snapshot.child(username).exists()){
                                String role = snapshot.child(username).child("role").getValue(String.class);
                                if (snapshot.child(username).child("password").getValue(String.class).equals(password)){
                                    Toast.makeText(getApplicationContext(), "Welcome in PrevenIT", Toast.LENGTH_SHORT).show();

                                    Intent masuk = new Intent(getApplicationContext(), HomeActivity.class);
                                    masuk.putExtra("role", role);
                                    masuk.putExtra("roleUser", role);
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

        btn_regis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = tb_username.getEditText().getText().toString();
                String password = tb_password.getEditText().getText().toString();

                databaseReference = FirebaseDatabase.getInstance().getReference("userLogin/");
                databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.child(username).exists()){
                            Toast.makeText(LoginActivity.this, "Akun Sudah Ada, Coba Lagi", Toast.LENGTH_SHORT).show();
                            finish();
                        } else if (username.isEmpty() || password.isEmpty()){
                            Toast.makeText(getApplicationContext(), "Username atau Password tidak boleh kosong", Toast.LENGTH_SHORT).show();
                        } else {
                            Map<String, String> account = new HashMap<>();
                            account.put("username", username);
                            account.put("role", roleUser);
                            account.put("password", password);

                            databaseReference.child(username).setValue(account).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    Toast.makeText(LoginActivity.this, "Account Berhasil dibuat, Silahkan Login", Toast.LENGTH_SHORT).show();

                                    Intent intent1 = new Intent(getApplicationContext(), RegisterActivity.class);
                                    startActivity(intent1);
                                }
                            });
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
//                if (username.isEmpty() || password.isEmpty()){
//                    Toast.makeText(getApplicationContext(), "Username atau Password tidak boleh kosong", Toast.LENGTH_SHORT).show();
//                } else {
//                    Map<String, String> account = new HashMap<>();
//                    account.put("username", username);
//                    account.put("role", roleUser);
//                    account.put("password", password);
//
//                    databaseReference.child(username).setValue(account).addOnSuccessListener(new OnSuccessListener<Void>() {
//                        @Override
//                        public void onSuccess(Void unused) {
//                            Toast.makeText(LoginActivity.this, "Account Berhasil dibuat, Silahkan Login", Toast.LENGTH_SHORT).show();
//
//                            Intent intent1 = new Intent(getApplicationContext(), RegisterActivity.class);
//                            startActivity(intent1);
//                        }
//                    });
//                }
            }
        });
    }
}