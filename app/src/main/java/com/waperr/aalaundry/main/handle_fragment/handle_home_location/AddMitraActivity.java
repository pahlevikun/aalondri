package com.waperr.aalaundry.main.handle_fragment.handle_home_location;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.waperr.aalaundry.R;
import com.waperr.aalaundry.database.DatabaseHandler;
import com.waperr.aalaundry.pojo.Mitra;

public class AddMitraActivity extends AppCompatActivity {

    private Button btCari, btSimpan;
    private EditText etNama, etAlamat, etDeskripsi;

    private String idMitra, namaMitra, alamatMitra, deskripsiMitra, teleponMitra, dataLondri;
    private double lat, lng;

    private DatabaseHandler dataSource;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_mitra);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        btCari = (Button) findViewById(R.id.buttonMitraCariOutlet);
        btSimpan = (Button) findViewById(R.id.buttonMitraSimpanOutlet);
        etNama = (EditText) findViewById(R.id.editTextNamaOutlet);
        etAlamat = (EditText) findViewById(R.id.editTextAlamatOutlet);
        etDeskripsi = (EditText) findViewById(R.id.editTextDeskripsiOutlet);

        dataSource = new DatabaseHandler(this);
        Intent intent = getIntent();
        dataLondri = intent.getStringExtra("dataLondri");

        btCari.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AddMitraActivity.this, PickMitraActivity.class);
                startActivityForResult(intent, 13);
            }
        });

        btSimpan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(etNama.getText().toString().length()==0||etAlamat.getText().toString().length()==0){
                    Toast.makeText(AddMitraActivity.this, "Silahkan cari outlet terlebih dahulu!", Toast.LENGTH_LONG).show();
                }else{
                    dataSource.tambahMitra(new Mitra(idMitra,namaMitra,alamatMitra,deskripsiMitra,lat,lng));
                    Intent intentKill = new Intent("finish_activity");
                    sendBroadcast(intentKill);
                    Intent intent = new Intent(AddMitraActivity.this, LocationActivity.class);
                    intent.putExtra("dataLondri",dataLondri);
                    startActivity(intent);
                    finish();

                }
            }
        });

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(/*requestCode == 13 &&*/ resultCode == RESULT_OK){
            idMitra = data.getStringExtra("idMitra");
            alamatMitra = data.getStringExtra("alamatMitra");
            namaMitra = data.getStringExtra("namaMitra");
            deskripsiMitra = data.getStringExtra("teleponMitra");
            lat = data.getDoubleExtra("latlondri",0);
            lng = data.getDoubleExtra("lnglondri",0);

            etNama.setText(namaMitra);
            etAlamat.setText(alamatMitra);
            etDeskripsi.setText("Telp : "+deskripsiMitra);
        }
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
