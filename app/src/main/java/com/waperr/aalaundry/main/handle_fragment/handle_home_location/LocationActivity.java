package com.waperr.aalaundry.main.handle_fragment.handle_home_location;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.waperr.aalaundry.R;
import com.waperr.aalaundry.adapter.LocationAlamatAdapter;
import com.waperr.aalaundry.adapter.LocationMitraAdapter;
import com.waperr.aalaundry.customlibs.NonScrollListView;
import com.waperr.aalaundry.database.DatabaseHandler;
import com.waperr.aalaundry.pojo.Alamat;
import com.waperr.aalaundry.pojo.Mitra;

import java.util.ArrayList;

public class LocationActivity extends AppCompatActivity {

    private NonScrollListView listViewMitra, listViewAlamat;
    private Button btAddAlamat, btAddMitra, btSelanjutnya;
    private TextView tvAlamat, tvMitra;

    private DatabaseHandler dataSource;
    private ArrayList<Alamat> valueAlamat;
    private ArrayList<Mitra> valueMitra;

    private LocationAlamatAdapter alamatAdapter;
    private LocationMitraAdapter mitraAdapter;

    private BroadcastReceiver broadcastReceiver;

    private String idMitra, namaMitra, alamatMitra, teleponMitra, namaUser, namaAlamatUser, detilDeskripsiUser, teleponUser, alamatUser, latitudeUser,
            longitudeUser, dataLondri;

    private double latitudeMitra,longitudeMitra;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        listViewAlamat = (NonScrollListView) findViewById(R.id.listViewAlamat);
        listViewMitra = (NonScrollListView) findViewById(R.id.listViewMitra);
        btAddAlamat = (Button) findViewById(R.id.buttonAddAlamat);
        btAddMitra = (Button) findViewById(R.id.buttonAddMitra);
        btSelanjutnya = (Button) findViewById(R.id.buttonLocationNext);
        tvMitra = (TextView) findViewById(R.id.textViewLocationMitra);
        tvAlamat = (TextView) findViewById(R.id.textViewLocationAlamat);

        dataSource = new DatabaseHandler(this);
        valueAlamat = (ArrayList<Alamat>) dataSource.getAllAlamats();
        valueMitra = (ArrayList<Mitra>) dataSource.getAllMitras();

        Intent intent = getIntent();
        dataLondri = intent.getStringExtra("dataLondri");

        alamatAdapter = new LocationAlamatAdapter(this, valueAlamat);
        listViewAlamat.setAdapter(alamatAdapter);
        alamatAdapter.notifyDataSetChanged();

        mitraAdapter = new LocationMitraAdapter(this, valueMitra);
        listViewMitra.setAdapter(mitraAdapter);
        mitraAdapter.notifyDataSetChanged();

        listViewMitra.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                idMitra = valueMitra.get(position).getId_mitra();
                namaMitra = valueMitra.get(position).getNama_mitra();
                alamatMitra = valueMitra.get(position).getAlamat_mitra();
                teleponMitra = valueMitra.get(position).getDeskripsi_mitra();
                latitudeMitra = valueMitra.get(position).getLat();
                longitudeMitra = valueMitra.get(position).getLng();
                tvMitra.setText(namaMitra);
                Toast.makeText(LocationActivity.this, "Anda Memilih Outlet "+valueMitra.get(position).getNama_mitra(), Toast.LENGTH_SHORT).show();
            }
        });

        listViewAlamat.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                namaAlamatUser = valueAlamat.get(position).getNamaAlamat();
                alamatUser = valueAlamat.get(position).getAlamatAlamat();
                detilDeskripsiUser = valueAlamat.get(position).getDeskripsiAlamat();
                namaUser = valueAlamat.get(position).getNamaUser();
                teleponUser = valueAlamat.get(position).getTeleponUser();
                latitudeUser = valueAlamat.get(position).getLatUser();
                longitudeUser = valueAlamat.get(position).getLngUser();
                tvAlamat.setText(namaAlamatUser);
                Toast.makeText(LocationActivity.this, "Anda Memilih Alamat "+valueAlamat.get(position).getNamaAlamat(), Toast.LENGTH_SHORT).show();
            }
        });

        btAddMitra.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LocationActivity.this, AddMitraActivity.class);
                intent.putExtra("dataLondri",dataLondri);
                startActivity(intent);
            }
        });

        btAddAlamat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LocationActivity.this, AddAlamatActivity.class);
                intent.putExtra("dataLondri",dataLondri);
                startActivity(intent);
            }
        });

        btSelanjutnya.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try{
                    if(namaMitra.equals(null)||namaUser.equals(null)){
                        Toast.makeText(LocationActivity.this, "Silahkan pilih Outlet dan Alamat Tujuan!", Toast.LENGTH_SHORT).show();
                    }else{
                        if(dataLondri.equals("ByWeight")){
                            Intent intent = new Intent(LocationActivity.this, LondriTypeActivity.class);
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
                            intent.putExtra("londri","ByWeight");
                            startActivity(intent);
                        }else if(dataLondri.equals("ByItem")){
                            Intent intent = new Intent(LocationActivity.this, LondriTypeActivity.class);
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
                            intent.putExtra("londri","ByItem");
                            startActivity(intent);
                        }
                    }
                }catch (Exception e){
                    Log.d("ERROR LOCATION","Hasil : "+e);
                    Toast.makeText(LocationActivity.this, "Silahkan pilih Outlet dan Alamat Tujuan!", Toast.LENGTH_SHORT).show();
                }

            }
        });

        destroyActivity();
    }

    public void destroyActivity(){
        broadcastReceiver = new BroadcastReceiver() {

            @Override
            public void onReceive(Context arg0, Intent intent) {
                String action = intent.getAction();
                if (action.equals("finish_activity")) {
                    finish();
                }
                arg0.unregisterReceiver(broadcastReceiver);
            }
        };
        registerReceiver(broadcastReceiver, new IntentFilter("finish_activity"));
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
            this.unregisterReceiver(broadcastReceiver);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        finish();
        this.unregisterReceiver(broadcastReceiver);
    }
}
