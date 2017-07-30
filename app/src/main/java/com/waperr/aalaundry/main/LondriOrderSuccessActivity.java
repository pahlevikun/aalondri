package com.waperr.aalaundry.main;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.waperr.aalaundry.R;
import com.waperr.aalaundry.database.DatabaseHandler;
import com.waperr.aalaundry.pojo.Riwayat;

public class LondriOrderSuccessActivity extends AppCompatActivity {

    private TextView tvHasil, tvPickup, tvLayanan;
    private Button btSimpan;
    private DatabaseHandler dataSource;
    private String berat, hasil, pickup, harga, jenis;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_londri_order_success);

        tvHasil = (TextView) findViewById(R.id.tvOrderSuccess);
        tvLayanan = (TextView) findViewById(R.id.tvJenisLayanan);
        tvPickup = (TextView) findViewById(R.id.tvPickUpSuccess);
        btSimpan = (Button) findViewById(R.id.buttonSetujuOrderSuccess);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        dataSource = new DatabaseHandler(this);

        Intent ambil = getIntent();
        berat = ambil.getStringExtra("berat");
        hasil = ambil.getStringExtra("message");
        pickup = ambil.getStringExtra("pickUp") ;
        harga = ambil.getStringExtra("total");
        jenis = ambil.getStringExtra("jenis");
        tvHasil.setText(hasil);
        if(jenis.equals("Londri Kiloan")){
            tvLayanan.setText(jenis+" "+berat+" Kg");
        }else{
            tvLayanan.setText(jenis);
        }
        tvPickup.setText("Pickup : "+pickup+"\nTotal : IDR "+harga);

        btSimpan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*if(jenis.equals("Londri Kiloan")){
                    dataSource.tambahRiwayat(new Riwayat(hasil, harga, jenis+" "+berat+" Kg",pickup));
                }else{
                    dataSource.tambahRiwayat(new Riwayat(hasil, harga, jenis,pickup));
                }*/
                Intent intent = new Intent(LondriOrderSuccessActivity.this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
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
            Intent intent = new Intent(LondriOrderSuccessActivity.this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(LondriOrderSuccessActivity.this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }
}
