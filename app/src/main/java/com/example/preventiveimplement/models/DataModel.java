package com.example.preventiveimplement.models;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

public class DataModel implements Parcelable {

    private String dataFrom;
    private String hari;
    private String jenis_kerusakan;
    private String kalender;
    private String kalender_filter;
    private String keterangan;
    private String pic;
    private String asset_tag;
    private String tindakan;
    private String waktu;
    private String jumlah;
    private String thermal_suhu;
    private String software;
    private String kebersihan_printer;
    private String test_printer;
    private String Signature;

    public DataModel() {
    }

    public String getAsset_tag() {
        return asset_tag;
    }

    public void setAsset_tag(String asset_tag) {
        this.asset_tag = asset_tag;
    }

    public String getDataFrom() {
        return dataFrom;
    }

    public void setDataFrom(String dataFrom) {
        this.dataFrom = dataFrom;
    }

    public String getHari() {
        return hari;
    }

    public void setHari(String hari) {
        this.hari = hari;
    }

    public String getJenis_kerusakan() {
        return jenis_kerusakan;
    }

    public void setJenis_kerusakan(String jenis_kerusakan) {
        this.jenis_kerusakan = jenis_kerusakan;
    }

    public String getKalender() {
        return kalender;
    }

    public void setKalender(String kalender) {
        this.kalender = kalender;
    }

    public String getKalender_filter() {
        return kalender_filter;
    }

    public void setKalender_filter(String kalender_filter) {
        this.kalender_filter = kalender_filter;
    }

    public String getKeterangan() {
        return keterangan;
    }

    public void setKeterangan(String keterangan) {
        this.keterangan = keterangan;
    }

    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }

    public String getTindakan() {
        return tindakan;
    }

    public void setTindakan(String tindakan) {
        this.tindakan = tindakan;
    }

    public String getWaktu() {
        return waktu;
    }

    public String getJumlah() {
        return jumlah;
    }

    public void setJumlah(String jumlah) {
        this.jumlah = jumlah;
    }

    public void setWaktu(String waktu) {
        this.waktu = waktu;
    }

    public String getThermal_suhu() {
        return thermal_suhu;
    }

    public void setThermal_suhu(String thermal_suhu) {
        this.thermal_suhu = thermal_suhu;
    }

    public String getSoftware() {
        return software;
    }

    public void setSoftware(String software) {
        this.software = software;
    }

    public String getKebersihan_printer() {
        return kebersihan_printer;
    }

    public void setKebersihan_printer(String kebersihan_printer) {
        this.kebersihan_printer = kebersihan_printer;
    }

    public String getTest_printer() {
        return test_printer;
    }

    public void setTest_printer(String test_printer) {
        this.test_printer = test_printer;
    }

    public String getSignature() {
        return Signature;
    }

    public void setSignature(String signature) {
        Signature = signature;
    }

    protected DataModel(Parcel in) {
        dataFrom = in.readString();
        hari = in.readString();
        jenis_kerusakan = in.readString();
        kalender = in.readString();
        kalender_filter = in.readString();
        keterangan = in.readString();
        pic = in.readString();
        asset_tag = in.readString();
        tindakan = in.readString();
        waktu = in.readString();
        jumlah = in.readString();
        thermal_suhu = in.readString();
        software = in.readString();
        kebersihan_printer = in.readString();
        test_printer = in.readString();
        Signature = in.readString();
    }

    public static final Creator<DataModel> CREATOR = new Creator<DataModel>() {
        @Override
        public DataModel createFromParcel(Parcel in) {
            return new DataModel(in);
        }

        @Override
        public DataModel[] newArray(int size) {
            return new DataModel[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel parcel, int flags) {
        parcel.writeString(dataFrom);
        parcel.writeString(hari);
        parcel.writeString(jenis_kerusakan);
        parcel.writeString(kalender);
        parcel.writeString(kalender_filter);
        parcel.writeString(keterangan);
        parcel.writeString(pic);
        parcel.writeString(asset_tag);
        parcel.writeString(tindakan);
        parcel.writeString(waktu);
        parcel.writeString(jumlah);
        parcel.writeString(thermal_suhu);
        parcel.writeString(software);
        parcel.writeString(kebersihan_printer);
        parcel.writeString(test_printer);
        parcel.writeString(Signature);
    }
}
