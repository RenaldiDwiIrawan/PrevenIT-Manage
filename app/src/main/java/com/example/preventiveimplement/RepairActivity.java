package com.example.preventiveimplement;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class RepairActivity extends AppCompatActivity {
    private ImageView btn_back;
    private CardView submit, btnDashboard, reset_ttd, btnBackReport;

    private Spinner pic, assetTag;
    private TextView tKalender, txtRepair;
    private EditText jKerusakan, tindakan, keterangan, namaPerangkat;
    private int tahun, bulan, tanggal;
    private String waktu, hari;
    private SignaturePad ttd;


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_repair);

        btn_back = findViewById(R.id.back_repair);
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        btnBackReport = findViewById(R.id.cardBackToReportRepair);
        tKalender = findViewById(R.id.txt_kalender_repair);
        submit = findViewById(R.id.cardSubmit_repair);
        btnDashboard = findViewById(R.id.cardDashboard_repair);

        jKerusakan = findViewById(R.id.eJenis_kerusakan);
        tindakan = findViewById(R.id.eTindakan);
        keterangan = findViewById(R.id.eKeterangan);
        txtRepair = findViewById(R.id.txt_repair);
        namaPerangkat = findViewById(R.id.eNamaPerangkat);

        pic = findViewById(R.id.spinner_pic_repair);
        assetTag = findViewById(R.id.spinner_asset_tag_repair);

        btnBackReport.setVisibility(View.GONE);

        Intent intent = getIntent();
        DataModel dataModel = intent.getParcelableExtra("dataPrevenIT");

        String fromReport = intent.getStringExtra("REPAIR");

        if ("REPAIR".equals(fromReport)) {
            submit.setVisibility(View.GONE);
            btn_back.setVisibility(View.GONE);
            btnBackReport.setVisibility(View.VISIBLE);
        }

        List<String> assetTagList = new ArrayList<>();
        ArrayAdapter<String> adapter = new ArrayAdapter<>(RepairActivity.this,
                android.R.layout.simple_spinner_item, assetTagList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        assetTag.setAdapter(adapter);
        assetTag.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedAssetTag = (String) parent.getItemAtPosition(position);
                DatabaseReference databaseReff = FirebaseDatabase.getInstance().getReference().child("dataInventory");
                Query query = databaseReff.orderByChild("asset_tag").equalTo(selectedAssetTag);

                query.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()) {
                            for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                String namaPerangkatStr = dataSnapshot.child("nama_aset").getValue(String.class);

                                namaPerangkat.setText(namaPerangkatStr);
                                namaPerangkat.setEnabled(false);
                            }
                        }
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

        if (dataModel == null) {

            DatabaseReference dataReff = FirebaseDatabase.getInstance().getReference().child("dataInventory");
            dataReff.addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                    String asset_tagStr = snapshot.child("asset_tag").getValue(String.class);

                    if (asset_tagStr != null && !asset_tagStr.isEmpty()) {
                        ArrayAdapter<String> adapter = (ArrayAdapter<String>) assetTag.getAdapter();
                        if (adapter != null && adapter.getPosition(asset_tagStr) == -1) {
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

        btnDashboard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                backToDashboard();
            }
        });

        btnBackReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                backToReport();
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
                dialog = new DatePickerDialog(RepairActivity.this, new DatePickerDialog.OnDateSetListener() {
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

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseDatabase db = FirebaseDatabase.getInstance();
                DatabaseReference reff = db.getReference("dataPrevenIT");
                String newKey = reff.push().getKey();

                Date date = new Date(tahun, bulan, tanggal);
                SimpleDateFormat dateFormat = new SimpleDateFormat("MMMM", new Locale("id"));
                String namaBulan = dateFormat.format(date);

                String jenisKerusakanStr = jKerusakan.getText().toString();
                String tindakanStr = tindakan.getText().toString();
                String keteranganStr = keterangan.getText().toString();

                String ePicStr = pic.getSelectedItem().toString();
                String assetTagStr = assetTag.getSelectedItem().toString();

                String txtRepairStr = txtRepair.getText().toString();
                String formattedDay = String.format("%02d", tanggal);
                String formattedMonth = String.format("%02d", (bulan + 1));

                if (jenisKerusakanStr.isEmpty() || namaBulan.isEmpty() || tindakanStr.isEmpty()
                        || keteranganStr.isEmpty() || ePicStr.isEmpty() || assetTagStr.isEmpty()) {
                    Toast.makeText(RepairActivity.this, "Data tidak boleh kosong", Toast.LENGTH_SHORT).show();
                    return;
                } else {
                    Map<String, Object> dataRepairMap = new HashMap<>();
                    dataRepairMap.put("kalender", formattedDay + "-" + namaBulan + "-" + tahun);
                    dataRepairMap.put("kalender_filter", formattedMonth + "-" + tahun);
                    dataRepairMap.put("waktu", waktu);
                    dataRepairMap.put("hari", hari);
                    dataRepairMap.put("pic", ePicStr);
                    dataRepairMap.put("asset_tag", assetTagStr);
                    dataRepairMap.put("jenis_kerusakan", jenisKerusakanStr);
                    dataRepairMap.put("tindakan", tindakanStr);
                    dataRepairMap.put("keterangan", keteranganStr);
                    dataRepairMap.put("dataFrom", txtRepairStr);

                    reff.child(newKey).setValue(dataRepairMap).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            Toast.makeText(RepairActivity.this, "Data berhasil disimpan", Toast.LENGTH_SHORT).show();

                            tKalender.setText("");
                            pic.setSelection(0);
                            assetTag.setSelection(0);
                            jKerusakan.setText("");
                            tindakan.setText("");
                            keterangan.setText("");
                        }

                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(RepairActivity.this, "Data gagal disimpan", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });


        if (intent != null) {
            if (dataModel != null) {
                List<String> picListIntent = new ArrayList<>();
                ArrayAdapter<String> adapterIntentPic = new ArrayAdapter<>(RepairActivity.this,
                        android.R.layout.simple_spinner_item, picListIntent);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                String picStr = dataModel.getPic();
                picListIntent.add(picStr);
                adapter.notifyDataSetChanged();

                List<String> assetTagListIntent = new ArrayList<>();
                ArrayAdapter<String> adapterIntentAsset_tag = new ArrayAdapter<>(RepairActivity.this,
                        android.R.layout.simple_spinner_item, assetTagListIntent);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                String asset_tagStr = dataModel.getAsset_tag();
                assetTagListIntent.add(asset_tagStr);
                adapter.notifyDataSetChanged();

                submit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        submit.setEnabled(false);
                        Toast.makeText(RepairActivity.this, "Button disabled. silahkan klik Dashboard.", Toast.LENGTH_SHORT).show();
                    }
                });

                btn_back.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        btn_back.setEnabled(false);
                        Toast.makeText(RepairActivity.this, "Button disabled. silahkan klik Dashboard.", Toast.LENGTH_SHORT).show();
                    }
                });

                pic.setAdapter(adapterIntentPic);
                assetTag.setAdapter(adapterIntentAsset_tag);
                tKalender.setText(dataModel.getKalender());
                jKerusakan.setText(dataModel.getJenis_kerusakan());
                tindakan.setText(dataModel.getTindakan());
                keterangan.setText(dataModel.getKeterangan());

                pic.setEnabled(false);
                assetTag.setEnabled(false);
                tKalender.setEnabled(false);
                jKerusakan.setEnabled(false);
                tindakan.setEnabled(false);
                keterangan.setEnabled(false);
//                ttd.setEnabled(false);
            }
        }
    }

    public void backToDashboard() {
        Intent dashboard = new Intent(RepairActivity.this, HomeActivity.class);
        startActivity(dashboard);
    }

    public void backToReport() {
        Intent report = new Intent(RepairActivity.this, ReportActivity.class);
        startActivity(report);
    }
}