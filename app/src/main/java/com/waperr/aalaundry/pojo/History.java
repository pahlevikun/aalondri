package com.waperr.aalaundry.pojo;

/**
 * Created by farhan on 6/17/17.
 */

public class History {

    private String id, idOrder, idUser, idKurir, idMitra, start_lat, start_lon,
            end_lat, end_lon, status, nama_alias, phone_alias, invoice_number, berat,
            total_harga, is_ekspress, is_byitem, tanggal_mulai, tanggal_akhir, waktu_mulai, waktu_akhir,
            alamat, detail_lokasi, catatan, statusDetail, alasan;

    public History() {
    }

    public History(String id, String idOrder, String idUser, String idKurir, String idMitra, String start_lat,
                   String start_lon, String end_lat, String end_lon, String status, String nama_alias, String phone_alias,
                   String invoice_number, String berat, String total_harga, String is_ekspress, String is_byitem, String tanggal_mulai,
                   String tanggal_akhir, String waktu_mulai, String waktu_akhir, String alamat, String detail_lokasi, String catatan,
                   String statusDetail, String alasan) {
        this.id = id;
        this.idOrder = idOrder;
        this.idUser = idUser;
        this.idKurir = idKurir;
        this.idMitra = idMitra;
        this.start_lat = start_lat;
        this.start_lon = start_lon;
        this.end_lat = end_lat;
        this.end_lon = end_lon;
        this.status = status;
        this.nama_alias = nama_alias;
        this.phone_alias = phone_alias;
        this.invoice_number = invoice_number;
        this.berat = berat;
        this.total_harga = total_harga;
        this.is_ekspress = is_ekspress;
        this.is_byitem = is_byitem;
        this.tanggal_mulai = tanggal_mulai;
        this.tanggal_akhir = tanggal_akhir;
        this.waktu_mulai = waktu_mulai;
        this.waktu_akhir = waktu_akhir;
        this.alamat = alamat;
        this.detail_lokasi = detail_lokasi;
        this.catatan = catatan;
        this.statusDetail = statusDetail;
        this.alasan = alasan;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIdOrder() {
        return idOrder;
    }

    public void setIdOrder(String idOrder) {
        this.idOrder = idOrder;
    }

    public String getIdUser() {
        return idUser;
    }

    public void setIdUser(String idUser) {
        this.idUser = idUser;
    }

    public String getIdKurir() {
        return idKurir;
    }

    public void setIdKurir(String idKurir) {
        this.idKurir = idKurir;
    }

    public String getIdMitra() {
        return idMitra;
    }

    public void setIdMitra(String idMitra) {
        this.idMitra = idMitra;
    }

    public String getStart_lat() {
        return start_lat;
    }

    public void setStart_lat(String start_lat) {
        this.start_lat = start_lat;
    }

    public String getStart_lon() {
        return start_lon;
    }

    public void setStart_lon(String start_lon) {
        this.start_lon = start_lon;
    }

    public String getEnd_lat() {
        return end_lat;
    }

    public void setEnd_lat(String end_lat) {
        this.end_lat = end_lat;
    }

    public String getEnd_lon() {
        return end_lon;
    }

    public void setEnd_lon(String end_lon) {
        this.end_lon = end_lon;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getNama_alias() {
        return nama_alias;
    }

    public void setNama_alias(String nama_alias) {
        this.nama_alias = nama_alias;
    }

    public String getPhone_alias() {
        return phone_alias;
    }

    public void setPhone_alias(String phone_alias) {
        this.phone_alias = phone_alias;
    }

    public String getInvoice_number() {
        return invoice_number;
    }

    public void setInvoice_number(String invoice_number) {
        this.invoice_number = invoice_number;
    }

    public String getBerat() {
        return berat;
    }

    public void setBerat(String berat) {
        this.berat = berat;
    }

    public String getTotal_harga() {
        return total_harga;
    }

    public void setTotal_harga(String total_harga) {
        this.total_harga = total_harga;
    }

    public String getIs_ekspress() {
        return is_ekspress;
    }

    public void setIs_ekspress(String is_ekspress) {
        this.is_ekspress = is_ekspress;
    }

    public String getIs_byitem() {
        return is_byitem;
    }

    public void setIs_byitem(String is_byitem) {
        this.is_byitem = is_byitem;
    }

    public String getTanggal_mulai() {
        return tanggal_mulai;
    }

    public void setTanggal_mulai(String tanggal_mulai) {
        this.tanggal_mulai = tanggal_mulai;
    }

    public String getTanggal_akhir() {
        return tanggal_akhir;
    }

    public void setTanggal_akhir(String tanggal_akhir) {
        this.tanggal_akhir = tanggal_akhir;
    }

    public String getWaktu_mulai() {
        return waktu_mulai;
    }

    public void setWaktu_mulai(String waktu_mulai) {
        this.waktu_mulai = waktu_mulai;
    }

    public String getWaktu_akhir() {
        return waktu_akhir;
    }

    public void setWaktu_akhir(String waktu_akhir) {
        this.waktu_akhir = waktu_akhir;
    }

    public String getAlamat() {
        return alamat;
    }

    public void setAlamat(String alamat) {
        this.alamat = alamat;
    }

    public String getDetail_lokasi() {
        return detail_lokasi;
    }

    public void setDetail_lokasi(String detail_lokasi) {
        this.detail_lokasi = detail_lokasi;
    }

    public String getCatatan() {
        return catatan;
    }

    public void setCatatan(String catatan) {
        this.catatan = catatan;
    }

    public String getStatusDetail() {
        return statusDetail;
    }

    public void setStatusDetail(String statusDetail) {
        this.statusDetail = statusDetail;
    }

    public String getAlasan() {
        return alasan;
    }

    public void setAlasan(String alasan) {
        this.alasan = alasan;
    }
}
