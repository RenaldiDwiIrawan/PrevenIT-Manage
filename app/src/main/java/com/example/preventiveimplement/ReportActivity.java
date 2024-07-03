package com.example.preventiveimplement;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;

import com.example.preventiveimplement.helper.DataReportHolder;
import com.example.preventiveimplement.models.DataModel;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.text.DateFormatSymbols;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class ReportActivity extends AppCompatActivity {
    private ImageView btn_back;
    private RecyclerView recyclerView;
    private DatabaseReference db;
    private FirebaseRecyclerAdapter<DataModel, DataReportHolder> adapter;
    private Button tampilkanData;
    private Spinner sTahun, sBulan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_report);

        btn_back = findViewById(R.id.back_report);
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        sTahun = findViewById(R.id.spinnerTahun);
        sBulan = findViewById(R.id.spinnerBulan);
        tampilkanData = findViewById(R.id.btnTampilkanData);
        recyclerView = findViewById(R.id.recyclerView_report);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        db = FirebaseDatabase.getInstance().getReference().child("dataPrevenIT");
        FirebaseRecyclerOptions<DataModel> options =
                new FirebaseRecyclerOptions.Builder<DataModel>()
                        .setQuery(db, DataModel.class)
                        .build();

        adapter = new FirebaseRecyclerAdapter<DataModel, DataReportHolder>(options) {
            @NonNull
            @Override
            public DataReportHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_report, parent, false);
                return new DataReportHolder(view, new DataReportHolder.OnItemClickListener() {
                    @Override
                    public void onItemClick(int position) {
                        DataModel item = adapter.getItem(position);
                        if (item.getDataFrom().equals("REPAIR")){
                            Intent intent = new Intent(ReportActivity.this, RepairActivity.class);
                            intent.putExtra("dataPrevenIT", item);
                            startActivity(intent);
                        } else if (item.getDataFrom().equals("MAINTENANCE")){
                            Intent intent = new Intent(ReportActivity.this, MaintenanceActivity.class);
                            intent.putExtra("dataPreventITMaintenance", item);
                            startActivity(intent);
                        }
                    }
                });
            }

            @Override
            protected void onBindViewHolder(@NonNull DataReportHolder holder, int position, @NonNull DataModel model) {
                holder.bindData(model);
            }
        };
        recyclerView.setAdapter(adapter);

        spinnerTahun();
        spinnerBulan();

        tampilkanData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String selectedMonth = String.valueOf(sBulan.getSelectedItemPosition() + 1);
                String selectedYear = sTahun.getSelectedItem().toString();

                selectedMonth = String.format("%02d", Integer.parseInt(selectedMonth));


                String startRange = selectedMonth + "-" + selectedYear;
                String endRange = selectedMonth + "-" + selectedYear;

                Query baseQuery = db.orderByChild("kalender_filter");

                Query filteredQuery = baseQuery
                        .startAt(startRange)
                        .endAt(endRange);

                FirebaseRecyclerOptions<DataModel> filteredOptions =
                        new FirebaseRecyclerOptions.Builder<DataModel>()
                                .setQuery(filteredQuery, DataModel.class)
                                .build();

                adapter.updateOptions(filteredOptions);
                recyclerView.setAdapter(adapter);
            }
        });

    }

    private void spinnerBulan() {
        String[] months = new DateFormatSymbols().getMonths();

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, months);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sBulan.setAdapter(adapter);
    }

    private void spinnerTahun() {
        List<String> tahun = new ArrayList<>();
        int currentYear = Calendar.getInstance().get(Calendar.YEAR);
        for (int i = currentYear; i >= 1900; i--) {
            tahun.add(String.valueOf(i));
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, tahun);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sTahun.setAdapter(adapter);
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (adapter != null) {
            adapter.startListening();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (adapter != null) {
            adapter.stopListening();
        }
    }
}