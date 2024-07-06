package com.example.preventiveimplement;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

public class HomeActivity extends AppCompatActivity {
    private ImageButton repair, maintenance, inventory, report, logout;
    private TextView txtRepair, txtMaintenance, txtReport, txtInventory;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_home);

        repair = findViewById(R.id.menuRepair);
        maintenance = findViewById(R.id.menuMaintenance);
        inventory = findViewById(R.id.menuInventory);
        report = findViewById(R.id.menuReport);
        logout = findViewById(R.id.iconLogout);

        txtInventory = findViewById(R.id.btnMenuInventory);
        txtRepair = findViewById(R.id.btnMenuRepair);
        txtMaintenance = findViewById(R.id.btnMenuMaintenance);
        txtReport = findViewById(R.id.btnMenuReport);

        Intent intent = getIntent();
        String role = intent.getStringExtra("role");

        if ("user".equals(role)){
            inventory.setVisibility(View.GONE);
            report.setVisibility(View.GONE);
            txtInventory.setVisibility(View.GONE);
            txtReport.setVisibility(View.GONE);
        }

        repair.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent gotoRepair = new Intent(HomeActivity.this, RepairActivity.class);
                gotoRepair.putExtra("role", role);
                startActivity(gotoRepair);
            }
        });

        maintenance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent gotoMaintenance = new Intent(HomeActivity.this, MaintenanceActivity.class);
                gotoMaintenance.putExtra("role", role);
                startActivity(gotoMaintenance);
            }
        });

        inventory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent gotoInventory = new Intent(HomeActivity.this, InventoryActivity.class);
                gotoInventory.putExtra("role", role);
                startActivity(gotoInventory);
            }
        });

        report.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent gotoReport = new Intent(HomeActivity.this, ReportActivity.class);
                gotoReport.putExtra("role", role);
                startActivity(gotoReport);
            }
        });

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPopup();
            }
        });
    }

    @Override
    public void onBackPressed(){
        showLogoutConfirmation();
    }

    private void showPopup() {
        PopupMenu popupMenu = new PopupMenu(this , logout);
        popupMenu.getMenuInflater().inflate(R.menu.popup_profile, popupMenu.getMenu());

        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if (item.getItemId() == R.id.menu_about) {
                    showCustomMessage("PrevenIT - since 2024 \ntanya@prevenit.id");
                    return true;
                }else if (item.getItemId() == R.id.menu_logout){
                    showLogoutConfirmation();
                    return  true;
                }
                return false;
            }
        });
        popupMenu.show();
    }

    private void showLogoutConfirmation() {
        AlertDialog.Builder alerBuilder = new AlertDialog.Builder(this);
        alerBuilder.setTitle("Logout");
        alerBuilder.setMessage("Apakah kamu yakin ingin logout?");

        alerBuilder.setPositiveButton("Logout", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                FirebaseAuth.getInstance().signOut();
                dialog.dismiss();
                toLogin();
            }
        });

        alerBuilder.setNegativeButton("Batal", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        AlertDialog dialog = alerBuilder.create();
        dialog.show();
    }

    private void toLogin() {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }

    private void showCustomMessage(String message) {
        String copyEmail = "tanya@prevenit.id";
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("About");
        builder.setMessage(message);
        builder.setPositiveButton("Salin Email", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                ClipboardManager copy =(ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData textCopy = ClipData.newPlainText("label", copyEmail);
                copy.setPrimaryClip(textCopy);
                Toast.makeText(HomeActivity.this, "Email Telah Disalin", Toast.LENGTH_SHORT).show();
            }
        });

        builder.setNegativeButton("Tutup", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }
}