package com.example.preventiveimplement;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

public class HomeActivity extends AppCompatActivity {
    private ImageButton repair, maintenance, inventory, report;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        repair = findViewById(R.id.menuRepair);
        maintenance = findViewById(R.id.menuMaintenance);
        inventory = findViewById(R.id.menuInventory);
        report = findViewById(R.id.menuReport);

        repair.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent gotoRepair = new Intent(HomeActivity.this, RepairActivity.class);
                startActivity(gotoRepair);
            }
        });

        maintenance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent gotoMaintenance = new Intent(HomeActivity.this, MaintenanceActivity.class);
                startActivity(gotoMaintenance);
            }
        });

        inventory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent gotoInventory = new Intent(HomeActivity.this, InventoryActivity.class);
                startActivity(gotoInventory);
            }
        });

        report.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent gotoReport = new Intent(HomeActivity.this, ReportActivity.class);
                startActivity(gotoReport);
            }
        });
    }
}