package com.waperr.aalaundry.main.handle_fragment.handle_history_fragment;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.waperr.aalaundry.R;
import com.waperr.aalaundry.main.LondriOrderSuccessActivity;
import com.waperr.aalaundry.main.MainActivity;

public class HistoryDetailActivity extends AppCompatActivity {

    private String idOrder, idUser, idKurir, idMitra, start_lat, start_lon,
            end_lat, end_lon, status, nama_alias, phone_alias, invoice_number, berat,
            total_harga, is_ekspress, is_byitem, tanggal_mulai, tanggal_akhir, waktu_mulai, waktu_akhir,
            alamat, detail_lokasi, catatan, statusDetail, alasan;

    private TextView tvInvoice, tvTanggalOrder, tvWaktuJemput, tvTipe, tvJenis, tvLayanan,
            tvBiaya,tvJumlah,tvTotalBiaya, tvAlamat, tvStatus, tvAlasan;
    private Button btAksi;

    private CardView cardViewAlasan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        tvInvoice = (TextView) findViewById(R.id.textViewHistoryDetailInvoice);
        tvStatus = (TextView) findViewById(R.id.textViewHistoryDetailStatus);
        tvTanggalOrder = (TextView) findViewById(R.id.textViewHistoryDetailTanggalOrder);
        tvWaktuJemput = (TextView) findViewById(R.id.textViewHistoryDetailWaktuJemput);
        tvTipe = (TextView) findViewById(R.id.textViewHistoryDetailTipeLondri);
        tvJenis = (TextView) findViewById(R.id.textViewHistoryDetailJenisLondri);
        tvLayanan = (TextView) findViewById(R.id.textViewHistoryDetailLayananLondri);
        tvBiaya = (TextView) findViewById(R.id.textViewHistoryDetailBiaya);
        tvJumlah = (TextView) findViewById(R.id.textViewHistoryDetailJumlah);
        tvTotalBiaya = (TextView) findViewById(R.id.textViewHistoryDetailTotal);
        tvAlamat = (TextView) findViewById(R.id.textViewHistoryDetailAlamat);
        btAksi = (Button) findViewById(R.id.buttonHistoryDetailAksi);
        tvAlasan = (TextView) findViewById(R.id.textViewHistoryDetailAlasan);
        cardViewAlasan = (CardView) findViewById(R.id.cardViewAlasan);

        cardViewAlasan.setVisibility(View.GONE);

        Intent intent = getIntent();
        idOrder = intent.getStringExtra("idOrder");
        idUser = intent.getStringExtra("idUser");
        idKurir = intent.getStringExtra("idKurir");
        idMitra = intent.getStringExtra("idMitra");
        start_lat = intent.getStringExtra("start_lat");
        start_lon = intent.getStringExtra("start_lon");
        end_lat = intent.getStringExtra("end_lat");
        end_lon = intent.getStringExtra("end_lon");
        status = intent.getStringExtra("status");
        nama_alias = intent.getStringExtra("nama_alias");
        phone_alias = intent.getStringExtra("phone_alias");
        invoice_number = intent.getStringExtra("invoice_number");
        berat = intent.getStringExtra("berat");
        total_harga = intent.getStringExtra("total_harga");
        is_ekspress = intent.getStringExtra("is_ekspress");
        is_byitem = intent.getStringExtra("is_byitem");
        tanggal_mulai = intent.getStringExtra("tanggal_mulai");
        tanggal_akhir = intent.getStringExtra("tanggal_akhir");
        waktu_mulai = intent.getStringExtra("waktu_mulai");
        waktu_akhir = intent.getStringExtra("waktu_akhir");
        alamat = intent.getStringExtra("alamat");
        detail_lokasi = intent.getStringExtra("detail_lokasi");
        catatan = intent.getStringExtra("catatan");
        statusDetail = intent.getStringExtra("statusDetail");
        alasan = intent.getStringExtra("alasan");

        Log.d("IDMITRA",""+idMitra);

        tvWaktuJemput.setText(waktu_mulai+" WIB");
        tvInvoice.setText(invoice_number+"");
        tvTanggalOrder.setText(tanggal_mulai+"");

        btAksi.setVisibility(View.GONE);

        tvAlamat.setText(nama_alias+"\n"+alamat+"\n"+detail_lokasi+"\nTelepon : "+phone_alias);
        if(is_byitem.equals("0")){
            float hargaSatuan = Integer.parseInt(total_harga)/Float.valueOf(berat);
            tvBiaya.setText("IDR "+hargaSatuan);
            tvTotalBiaya.setText("IDR "+total_harga);
            tvJumlah.setText(berat+" Kg");
            tvTipe.setText("Londri Kiloan");
        }else if(is_byitem.equals("1")) {
            tvBiaya.setText("IDR " + total_harga);
            tvTotalBiaya.setText("IDR " + total_harga);
            tvJumlah.setText("xxx Piece");
            tvTipe.setText("Londri Satuan");
        }

        if (is_ekspress.equals("1")){
            tvLayanan.setText("Reguler");
        }else if(is_ekspress.equals("2")){
            tvLayanan.setText("Ekspress");
        }else if(is_ekspress.equals("3")){
            tvLayanan.setText("Kilat");
        }

        if (statusDetail.equals("0")&&!status.equals("1")){
            tvStatus.setText("Order Belum Ditangani");
        }else if(statusDetail.equals("1")){
            tvStatus.setText("Order Sedang Dijemput");
        }else if(statusDetail.equals("2")){
            tvStatus.setText("Order Sedang Dicuci");
        }else if(statusDetail.equals("3")){
            tvStatus.setText("Order Sedang Diantar");
        }else if(statusDetail.equals("4")){
            btAksi.setVisibility(View.VISIBLE);
            tvStatus.setText("Order Selesai");
            btAksi.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(HistoryDetailActivity.this, FeedbackActivity.class);
                    intent.putExtra("idMitra",idMitra);
                    startActivity(intent);
                }
            });
        }else if(statusDetail.equals("0")&&status.equals("1")){
            tvStatus.setText("Order Dibatalkan");
            cardViewAlasan.setVisibility(View.VISIBLE);
            tvAlasan.setText("Karena "+alasan);
        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if(id==android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        finish();
    }

}
