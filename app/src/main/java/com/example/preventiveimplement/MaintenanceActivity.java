package com.example.preventiveimplement;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.preventiveimplement.models.DataModel;
import com.github.gcacace.signaturepad.views.SignaturePad;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class MaintenanceActivity extends AppCompatActivity {
    private ImageView btn_back;
    private TextView tKalender, txtMaintenance;
    private CardView submit, reset_ttd, dashboard;
    private EditText thermal, software,  tPrinter;
    private EditText kPrinter;
    private Spinner pic, assetTag;
    private int tahun, bulan, tanggal;
    private String waktu, hari;
    private SignaturePad ttd;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_maintenance);

        btn_back = findViewById(R.id.back_maintenance);

        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        tKalender = findViewById(R.id.txt_kalender_maintenance);
        ttd = findViewById(R.id.signaturePad);
        submit = findViewById(R.id.card_submit_maintenance);
        reset_ttd = findViewById(R.id.reset_signature);
        dashboard = findViewById(R.id.card_dashboard_inventory);

        pic = findViewById(R.id.spinner_pic_maintenance);
        assetTag = findViewById(R.id.spinner_asset_tag_maintenance);

        thermal = findViewById(R.id.thermal_edit);
        software = findViewById(R.id.software_edit);
        kPrinter = findViewById(R.id.printer_edit);
        tPrinter = findViewById(R.id.testPrint_edit);
        txtMaintenance = findViewById(R.id.txt_maintenance);

        Intent intent = getIntent();
        DataModel dataModel = intent.getParcelableExtra("dataPreventIT");

        List<String> assetTagList = new ArrayList<>();
        ArrayAdapter<String> adapter = new ArrayAdapter<>(MaintenanceActivity.this,
                android.R.layout.simple_spinner_item, assetTagList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        assetTag.setAdapter(adapter);
        assetTag.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedAssetTag = (String) parent.getItemAtPosition(position);

                DatabaseReference dataReff = FirebaseDatabase.getInstance().getReference().child("dataInventory");
                Query query = dataReff.orderByChild("asset_tag").equalTo(selectedAssetTag);

                query.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        if (dataModel == null){
            DatabaseReference dataReff = FirebaseDatabase.getInstance().getReference().child("dataInventory");
            dataReff.addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                    String asset_tagStr = snapshot.child("asset_tag").getValue(String.class);

                    if (asset_tagStr != null && !asset_tagStr.isEmpty()) {
                        ArrayAdapter<String> adapter = (ArrayAdapter<String>) assetTag.getAdapter();
                        if ((adapter != null && adapter.getPosition(asset_tagStr) == -1)) {
                            assetTagList.add(asset_tagStr);
                            adapter.notifyDataSetChanged();
                        }
                    }
                    if (snapshot.exists()) {
                        assetTag.setAdapter(adapter);
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

                }
            });
        }


        dashboard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toDashboard();
            }
        });

        tKalender.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd - MMMM - yyyy", new Locale("id", "ID"));
                SimpleDateFormat formatWaktu = new SimpleDateFormat("HH:mm:ss", new Locale("id", "ID"));
                SimpleDateFormat formatHari = new SimpleDateFormat("EEEE", new Locale("id", "ID"));

                Calendar calendar = Calendar.getInstance();
                tahun = calendar.get(Calendar.YEAR);
                bulan = calendar.get(Calendar.MONTH);
                tanggal = calendar.get(Calendar.DATE);

                waktu = formatWaktu.format(calendar.getTime());
                hari = formatHari.format(calendar.getTime());

                DatePickerDialog dialog;
                dialog = new DatePickerDialog(MaintenanceActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        tahun = year;
                        bulan = month;
                        tanggal = dayOfMonth;

                        calendar.set(Calendar.YEAR, year);
                        calendar.set(Calendar.MONTH, month);
                        calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                        tKalender.setText(simpleDateFormat.format(calendar.getTime()));
                    }
                }, tahun, bulan, tanggal);
                dialog.show();
            }
        });

        reset_ttd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ttd.clear();
            }
        });

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseDatabase db = FirebaseDatabase.getInstance();
                DatabaseReference reff = db.getReference("dataPrevenIT");
                String newKey = reff.push().getKey();


                Date date = new Date(tahun, bulan, tanggal);
                SimpleDateFormat dateFormat = new SimpleDateFormat("MMMM", new Locale("id"));
                String namaBulan = dateFormat.format(date);

                String picStr = pic.getSelectedItem().toString();
                String assetTagStr = assetTag.getSelectedItem().toString();

                String thermalStr = thermal.getText().toString();
                String softwareStr = software.getText().toString();
                String kPrinterStr = kPrinter.getText().toString();
                String tPrinterStr = tPrinter.getText().toString();
                String txtMaintenanceStr = txtMaintenance.getText().toString();
                String formattedDay = String.format("%02d", tanggal);
                String formattedMonth = String.format("%02d", (bulan + 1));
                String ttdBase64 = convertSignatureToBase64();

                Map<String, Object> dataMaintenanceMap = new HashMap<>();
                dataMaintenanceMap.put("kalender", formattedDay + "-" + namaBulan + "-" + tahun);
                dataMaintenanceMap.put("kalender_filter", formattedMonth + "-" + tahun);
                dataMaintenanceMap.put("hari", hari);
                dataMaintenanceMap.put("waktu", waktu);

                dataMaintenanceMap.put("pic", picStr);
                dataMaintenanceMap.put("asset_tag", assetTagStr);

                dataMaintenanceMap.put("thermal_suhu", thermalStr);
                dataMaintenanceMap.put("software", softwareStr);
                dataMaintenanceMap.put("kebersihan_printer", kPrinterStr);
                dataMaintenanceMap.put("test_printer", tPrinterStr);
                dataMaintenanceMap.put("dataFrom", txtMaintenanceStr);
                dataMaintenanceMap.put("signature", ttdBase64);


                reff.child(newKey).setValue(dataMaintenanceMap).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(MaintenanceActivity.this, "Data berhasil disimpan", Toast.LENGTH_SHORT).show();

                        pic.setSelection(0);
                        assetTag.setSelection(0);

                        thermal.setText("");
                        software.setText("");
                        kPrinter.setText("");
                        tPrinter.setText("");
                        ttd.clear();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(MaintenanceActivity.this, "Data gagal disimpan", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        if (intent != null){
            if (dataModel !=null){
                List<String> picListIntent = new ArrayList<>();
                ArrayAdapter<String> adapterIntentPic = new ArrayAdapter<>(MaintenanceActivity.this,
                        android.R.layout.simple_spinner_item, picListIntent);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                String picStr = dataModel.getPic();
                picListIntent.add(picStr);
                adapter.notifyDataSetChanged();

                List<String> assetTagListIntent = new ArrayList<>();
                ArrayAdapter<String> adapterIntentAsset_tag = new ArrayAdapter<>(MaintenanceActivity.this,
                        android.R.layout.simple_spinner_item, assetTagListIntent);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                String asset_tagStr = dataModel.getAsset_tag();
                assetTagListIntent.add(asset_tagStr);
                adapter.notifyDataSetChanged();
                submit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        submit.setEnabled(false);
                        Toast.makeText(MaintenanceActivity.this, "Button disabled. silahkan klik Dashboard.", Toast.LENGTH_SHORT).show();
                    }
                });

                reset_ttd.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        reset_ttd.setEnabled(false);
                        Toast.makeText(MaintenanceActivity.this, "Button disabled. silahkan klik Dashboard.", Toast.LENGTH_SHORT).show();
                    }
                });

                tKalender.setText(dataModel.getKalender());
                pic.setAdapter(adapterIntentPic);
                assetTag.setAdapter(adapterIntentAsset_tag);
                thermal.setText(dataModel.getThermal_suhu());
                software.setText(dataModel.getSoftware());
                kPrinter.setText(dataModel.getKebersihan_printer());
                tPrinter.setText(dataModel.getTest_printer());
                setSignatureFromBase64(dataModel.getSignature());

                tKalender.setEnabled(false);
                pic.setEnabled(false);
                assetTag.setEnabled(false);
                thermal.setEnabled(false);
                software.setEnabled(false);
                kPrinter.setEnabled(false);
                tPrinter.setEnabled(false);
                ttd.setEnabled(false);

            }
        }
    }
    private void toDashboard () {
        Intent dashboard = new Intent(MaintenanceActivity.this, HomeActivity.class);
        startActivity(dashboard);
    }

    private String convertSignatureToBase64() {
        Bitmap signatureBitmap = ttd.getSignatureBitmap();

        if (signatureBitmap != null) {
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            signatureBitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
            byte[] byteArray = byteArrayOutputStream.toByteArray();
            return Base64.encodeToString(byteArray, Base64.DEFAULT);
        } else {
            return null;
        }
    }
    private void setSignatureFromBase64(String base64String) {
        if (base64String != null && !base64String.isEmpty()) {
            byte[] decodedBytes = Base64.decode(base64String, Base64.DEFAULT);
            Bitmap signatureBitmap = BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.length);

            if (signatureBitmap != null) {
                ttd.setSignatureBitmap(signatureBitmap);
            } else {
                Toast.makeText(this, "Gagal memuat tanda tangan.", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "Tidak ada tanda tangan yang tersimpan.", Toast.LENGTH_SHORT).show();
        }
    }

}