package com.waperr.aalaundry.main.handle_fragment.handle_home_location;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.MarkerOptions;
import com.squareup.picasso.Picasso;
import com.waperr.aalaundry.R;
import com.waperr.aalaundry.database.DatabaseHandler;
import com.waperr.aalaundry.pojo.Alamat;
import com.waperr.aalaundry.pojo.Mitra;

public class AddAlamatActivity extends AppCompatActivity {

    private Button btSimpan;
    private EditText etNamaAlamat, etAlamat, etDeskripsiDetil, etKodePos, etNamaPenerima, etTeleponPenerima;

    private String namaAlamatUser, alamatUser, deskripsiDetilUser, kodePosAlamat, namaUser, telpUser, latUser, lngUser,dataLondri;

    private DatabaseHandler dataSource;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_alamat);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        btSimpan = (Button) findViewById(R.id.buttonAlamatSimpanAlamat);
        etNamaAlamat = (EditText) findViewById(R.id.editTextNamaAlamat);
        etAlamat = (EditText) findViewById(R.id.editTextAlamatAlamat);
        etDeskripsiDetil = (EditText) findViewById(R.id.editTextDeskripsiAlamat);
        etNamaPenerima = (EditText) findViewById(R.id.editTextNamaPenerimaAlamat);
        etTeleponPenerima = (EditText) findViewById(R.id.editTextTeleponPenerimaAlamat);

        dataSource = new DatabaseHandler(this);
        Intent intent = getIntent();
        dataLondri = intent.getStringExtra("dataLondri");

        etAlamat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                search(9);
            }
        });

        btSimpan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(etNamaAlamat.getText().toString().length()==0||
                        etAlamat.getText().toString().length()==0||
                            etDeskripsiDetil.getText().toString().length()==0||
                                etNamaPenerima.getText().toString().length()==0||
                                    etTeleponPenerima.getText().toString().length()==0){
                    Toast.makeText(AddAlamatActivity.this, "Silahkan isi dengan benar!", Toast.LENGTH_LONG).show();
                }else{
                    namaAlamatUser = etNamaAlamat.getText().toString();
                    alamatUser = etAlamat.getText().toString();
                    deskripsiDetilUser = etDeskripsiDetil.getText().toString();
                    namaUser = etNamaPenerima.getText().toString();
                    telpUser = etTeleponPenerima.getText().toString();
                    Log.d("LOKASI",latUser+" "+lngUser);
                    dataSource.tambahAlamat(new Alamat(namaAlamatUser, alamatUser, deskripsiDetilUser,"kode pos",namaUser,telpUser,latUser,lngUser));
                    Intent intentKill = new Intent("finish_activity");
                    sendBroadcast(intentKill);
                    Intent intent = new Intent(AddAlamatActivity.this, LocationActivity.class);
                    intent.putExtra("dataLondri",dataLondri);
                    startActivity(intent);
                    finish();
                }
            }
        });
    }

    private void search(int kode) {
        try {
            //Intent intent = new PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_OVERLAY).build(this);
            PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();
            startActivityForResult(builder.build(this), kode);
        } catch (GooglePlayServicesRepairableException e) {
            GoogleApiAvailability.getInstance().getErrorDialog(this, e.getConnectionStatusCode(), 0).show();
        } catch (GooglePlayServicesNotAvailableException e) {
            String message = "Google Play Services is not available: " +
                    GoogleApiAvailability.getInstance().getErrorString(e.errorCode);
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
        } catch (Exception e){
            Toast.makeText(this, "Koneksi kurang stabil, periksa koneksi!", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == 9) {
            Place place = PlacePicker.getPlace(data, this);
            try{
                Log.d("lokasi",""+place.getLatLng());

                etAlamat.setText(place.getAddress().toString());
                alamatUser = place.getAddress().toString();
                latUser = String.valueOf(place.getLatLng().latitude);
                lngUser = String.valueOf(place.getLatLng().longitude);

                Log.e("nama",place.getName().toString());
                Log.e("alamat",place.getAddress().toString());
                Log.e("latitude", String.valueOf(place.getLatLng().latitude));
                Log.e("longitude", String.valueOf(place.getLatLng().longitude));
            }catch(Exception e){
                Toast.makeText(this, "Koneksi tidak stabil, periksa koneksi atau pilih dengan perlahan!", Toast.LENGTH_LONG).show();
            }
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
