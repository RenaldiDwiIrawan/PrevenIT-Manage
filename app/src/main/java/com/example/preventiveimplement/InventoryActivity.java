package com.example.preventiveimplement;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class InventoryActivity extends AppCompatActivity {
    private Spinner assetTag;
    private ImageView btn_back;
    private CardView edit_data;
    private EditText user;
//    private EditText location;
    private EditText departement;
    private EditText aset;
    private EditText jumlah;
//    private EditText company;

    @SuppressLint({"WrongViewCast", "MissingInflatedId"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_inventory);

        btn_back = findViewById(R.id.back_inventory);

        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        edit_data = findViewById(R.id.card_edit_inventory);
        assetTag = findViewById(R.id.spinner_asset_inv);
        user = findViewById(R.id.user_db);
//        location = findViewById(R.id.location_db);
        departement = findViewById(R.id.departement_db);
        aset = findViewById(R.id.aset_db);
        jumlah = findViewById(R.id.jumlah_db);
//        company = findViewById(R.id.company_db);

        List<String> assetTagList = new ArrayList<>();
        ArrayAdapter<String> adapter = new ArrayAdapter<>(InventoryActivity.this,
                android.R.layout.simple_spinner_item, assetTagList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        assetTag.setAdapter(adapter);

        assetTag.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedAssetTag = (String) parent.getItemAtPosition(position);

                DatabaseReference databaseRef = FirebaseDatabase.getInstance().getReference().child("dataInventory");
                Query query = databaseRef.orderByChild("asset_tag").equalTo(selectedAssetTag);

                query.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            for (DataSnapshot childSnapshot : dataSnapshot.getChildren()) {
                                String userStr = childSnapshot.child("user").getValue(String.class);
//                                String locationStr = childSnapshot.child("location").getValue(String.class);
                                String departementStr = childSnapshot.child("departement").getValue(String.class);
                                String asetStr = childSnapshot.child("nama_aset").getValue(String.class);
                                String jumlahStr = childSnapshot.child("jumlah").getValue(String.class);
//                                String companyStr = childSnapshot.child("company").getValue(String.class);

                                user.setText(userStr);
//                                location.setText(locationStr);
                                departement.setText(departementStr);
                                aset.setText(asetStr);
                                jumlah.setText(jumlahStr);
//                                company.setText(companyStr);
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Toast.makeText(InventoryActivity.this, "Error: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        DatabaseReference databaseRef = FirebaseDatabase.getInstance().getReference().child("dataInventory");
        databaseRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                String userStr = snapshot.child("user").getValue(String.class);
//                String locationStr = snapshot.child("location").getValue(String.class);
                String departementStr = snapshot.child("departement").getValue(String.class);
                String asetStr = snapshot.child("aset").getValue(String.class);
                String jumlahStr = snapshot.child("jumlah").getValue(String.class);
//                String companyStr = snapshot.child("company").getValue(String.class);
                String asset_tagStr = snapshot.child("asset_tag").getValue(String.class);

                if (asset_tagStr != null && !asset_tagStr.isEmpty()) {
                    ArrayAdapter<String> adapter = (ArrayAdapter<String>) assetTag.getAdapter();
                    if (adapter != null && adapter.getPosition(asset_tagStr) == -1) {
                        assetTagList.add(asset_tagStr);
                        adapter.notifyDataSetChanged();
                    }
                }

                if (snapshot.exists()) {
                    user.setText(userStr);
//                    location.setText(locationStr);
                    departement.setText(departementStr);
                    aset.setText(asetStr);
                    jumlah.setText(jumlahStr);
//                    company.setText(companyStr);
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(InventoryActivity.this, "yah, gagal cuy", Toast.LENGTH_SHORT).show();
                System.out.println(error);
            }
        });


        edit_data.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(InventoryActivity.this);
                builder.setTitle("Konfirmasi");
                builder.setMessage("Anda yakin ingin mengubah data?");

                builder.setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Ambil asset_tag yang dipilih dari Spinner
                        String selectedAssetTag = (String) assetTag.getSelectedItem();

                        DatabaseReference databaseRef = FirebaseDatabase.getInstance().getReference().child("dataInventory");
                        Query query = databaseRef.orderByChild("asset_tag").equalTo(selectedAssetTag);

                        query.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                if (dataSnapshot.exists()) {
                                    for (DataSnapshot childSnapshot : dataSnapshot.getChildren()) {
                                        // Update data lainnya kecuali asset_tag
                                        databaseRef.child(childSnapshot.getKey()).child("user").setValue(user.getText().toString());
//                                        databaseRef.child(childSnapshot.getKey()).child("location").setValue(location.getText().toString());
                                        databaseRef.child(childSnapshot.getKey()).child("departement").setValue(departement.getText().toString());
                                        databaseRef.child(childSnapshot.getKey()).child("aset").setValue(aset.getText().toString());
                                        databaseRef.child(childSnapshot.getKey()).child("jumlah").setValue(jumlah.getText().toString());
//                                        databaseRef.child(childSnapshot.getKey()).child("company").setValue(company.getText().toString());

                                        // Tampilkan pesan atau lakukan aksi lain jika berhasil
                                        Toast.makeText(InventoryActivity.this, "Data berhasil diubah", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {
                                // Penanganan kesalahan jika ada
                                Toast.makeText(InventoryActivity.this, "Error: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                });

                builder.setNegativeButton("Tidak", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss(); // Batal melakukan perubahan
                    }
                });

                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            }
        });
    }
}
