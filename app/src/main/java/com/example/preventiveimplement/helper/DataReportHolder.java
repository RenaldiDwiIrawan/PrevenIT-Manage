package com.example.preventiveimplement.helper;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.preventiveimplement.R;
import com.example.preventiveimplement.models.DataModel;

public class DataReportHolder extends RecyclerView.ViewHolder {
    private final OnItemClickListener listener;
    private TextView tanggalHolder, sumberHolder, picHolder, tanggalView, hari, waktu;

    public DataReportHolder(@NonNull View itemView, OnItemClickListener listener) {
        super(itemView);
        hari = itemView.findViewById(R.id.hari);
        tanggalView =  itemView.findViewById(R.id.tanggal_report);
        tanggalHolder = itemView.findViewById(R.id.tanggal_data_report);
        sumberHolder =  itemView.findViewById(R.id.sumber_data);
        picHolder = itemView.findViewById(R.id.pic_report);
        waktu = itemView.findViewById(R.id.waktu_report);
        this.listener = listener;

        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION && listener != null){
                    listener.onItemClick(position);
                }
            }
        });
    }

    public interface OnItemClickListener{
        void onItemClick(int position);
    }

    public void bindData(DataModel model){
        hari.setText(model.getHari());
        tanggalView.setText(model.getKalender());
        tanggalHolder.setText(model.getKalender());
        sumberHolder.setText(model.getDataFrom());
        picHolder.setText(model.getPic());
        waktu.setText(model.getWaktu());
    }
}
