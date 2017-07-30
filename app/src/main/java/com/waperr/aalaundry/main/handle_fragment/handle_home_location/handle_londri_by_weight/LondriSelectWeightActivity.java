package com.waperr.aalaundry.main.handle_fragment.handle_home_location.handle_londri_by_weight;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.waperr.aalaundry.R;
import com.waperr.aalaundry.main.handle_fragment.handle_home_location.LondriScheduleActivity;

public class LondriSelectWeightActivity extends AppCompatActivity {

    private String idMitra, namaMitra, alamatMitra, teleponMitra,  namaUser,
            namaAlamatUser, detilDeskripsiUser, teleponUser, alamatUser, latitudeUser,
            longitudeUser, hargaPilih, jenisPilih, layananPilih, londri;
    private int berat=0, harga = 0, totalHarga, totalBerat;
    private double latitudeMitra, longitudeMitra;

    private Button buttonSelanjutnya, buttonKurang, buttonTambah;
    private TextView tvBerat, tvHarga;
    private EditText etCatatan;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_londri_select_weight);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        etCatatan = (EditText) findViewById(R.id.editTextCatatanWeight);

        Intent intent = getIntent();
        idMitra = intent.getStringExtra("idMitra");
        namaMitra= intent.getStringExtra("namaMitra");
        alamatMitra= intent.getStringExtra("alamatMitra");
        teleponMitra= intent.getStringExtra("teleponMitra");
        namaUser= intent.getStringExtra("namaUser");
        detilDeskripsiUser = intent.getStringExtra("detilDeskripsiUser");
        teleponUser = intent.getStringExtra("teleponUser");
        alamatUser = intent.getStringExtra("alamatUser");
        latitudeUser = intent.getStringExtra("latitudeUser");
        longitudeUser = intent.getStringExtra("longitudeUser");
        hargaPilih = intent.getStringExtra("hargaPilih");
        jenisPilih = intent.getStringExtra("jenisPilih");
        layananPilih = intent.getStringExtra("layananPilih");
        latitudeMitra = intent.getDoubleExtra("latitudeMitra",0);
        longitudeMitra = intent.getDoubleExtra("longitudeMitra",0);
        londri = intent.getStringExtra("londri");
        Log.d("DETIL",""+detilDeskripsiUser);

        tvBerat = (TextView) findViewById(R.id.textViewBerat);
        buttonSelanjutnya = (Button) findViewById(R.id.buttonLondriSelectWeightNext);
        buttonTambah = (Button) findViewById(R.id.buttonTambahBerat);
        buttonKurang = (Button) findViewById(R.id.buttonKurangBerat);
        tvHarga = (TextView) findViewById(R.id.textViewHarga);

        tvHarga.setText("IDR 0");

        buttonTambah.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                berat = berat + 1;
                if(layananPilih.equals("1")){
                    harga = berat * Integer.parseInt(hargaPilih);
                }else if(layananPilih.equals("2")){
                    harga = (berat * Integer.parseInt(hargaPilih)) * 2;
                }else if(layananPilih.equals("3")){
                    harga = (berat * Integer.parseInt(hargaPilih)) * 4;
                }
                tvBerat.setText(berat + " Kg");
                tvHarga.setText("IDR "+harga);
            }
        });

        buttonKurang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                berat = berat - 1;
                if(berat<=0){
                    berat=0;
                    harga=0;
                    tvBerat.setText(berat+" Kg");
                    tvHarga.setText("IDR 0");
                }else{
                    if(layananPilih.equals("1")){
                        harga = berat * Integer.parseInt(hargaPilih);
                    }else if(layananPilih.equals("2")){
                        harga = (berat * Integer.parseInt(hargaPilih)) * 2;
                    }else if(layananPilih.equals("3")){
                        harga = (berat * Integer.parseInt(hargaPilih)) * 4;
                    }
                    harga = berat * Integer.parseInt(hargaPilih);
                    tvBerat.setText(berat + " Kg");
                    tvHarga.setText("IDR "+harga);
                }
            }
        });

        buttonSelanjutnya.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (berat == 0){
                    Toast.makeText(LondriSelectWeightActivity.this, "Silahkan masukan berat londri Anda!", Toast.LENGTH_SHORT).show();
                }else{
                    totalBerat = berat;
                    totalHarga = Integer.parseInt(hargaPilih) * totalBerat;
                    String catatan;
                    if(etCatatan.getText().toString().isEmpty()){
                        catatan = "Tidak ada catatan tambahan";
                    }else{
                        catatan = etCatatan.getText().toString();
                    }
                    Intent intent = new Intent(LondriSelectWeightActivity.this, LondriScheduleActivity.class);
                    intent.putExtra("idMitra",idMitra);
                    intent.putExtra("namaMitra",namaMitra);
                    intent.putExtra("alamatMitra",alamatMitra);
                    intent.putExtra("teleponMitra",teleponMitra);
                    intent.putExtra("namaUser",namaUser);
                    intent.putExtra("alamatUser",alamatUser);
                    intent.putExtra("detilDeskripsiUser",detilDeskripsiUser);
                    intent.putExtra("teleponUser",teleponUser);
                    intent.putExtra("latitudeUser",latitudeUser);
                    intent.putExtra("longitudeUser",longitudeUser);
                    intent.putExtra("latitudeMitra",latitudeMitra);
                    intent.putExtra("longitudeMitra",longitudeMitra);
                    intent.putExtra("jenisPilih",jenisPilih);
                    intent.putExtra("hargaPilih",hargaPilih);
                    intent.putExtra("layananPilih",layananPilih);
                    intent.putExtra("totalBerat",""+totalBerat);
                    intent.putExtra("totalHarga",""+totalHarga);
                    intent.putExtra("catatan",catatan);
                    intent.putExtra("londri",londri);
                    startActivity(intent);
                    Log.d("Berat",""+totalBerat);
                }
            }
        });
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
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
