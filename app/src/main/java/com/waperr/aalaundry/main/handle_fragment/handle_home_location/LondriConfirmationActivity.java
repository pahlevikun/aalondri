package com.waperr.aalaundry.main.handle_fragment.handle_home_location;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.waperr.aalaundry.R;
import com.waperr.aalaundry.config.APIConfig;
import com.waperr.aalaundry.config.AppController;
import com.waperr.aalaundry.database.DatabaseHandler;
import com.waperr.aalaundry.main.LondriOrderSuccessActivity;
import com.waperr.aalaundry.pojo.Cart;
import com.waperr.aalaundry.pojo.Profil;
import com.waperr.aalaundry.pojo.Riwayat;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class LondriConfirmationActivity extends AppCompatActivity {

    private Button buttonSetuju;
    private TextView textViewNama, textViewAlamat, textViewService, textViewPickUp, textViewDelivery, textViewTotal, textViewTambahan;

    private String token, layanan, idMitra, namaMitra, alamatMitra, teleponMitra,  namaUser,
            namaAlamatUser, detilDeskripsiUser, teleponUser, alamatUser, latitudeUser, tanggalAntar, catatan,
            longitudeUser, hargaPilih, jenisPilih, layananPilih, londri, waktuJemput, tanggalJemput,totalBerat,totalHarga;
    private double latitudeMitra, longitudeMitra;
    private StringBuffer stringBuffer;

    public ArrayList<Profil> valuesProfil;
    public DatabaseHandler dataSource;
    private ArrayList<Cart> foodCart = new ArrayList<Cart>();

    private ProgressDialog loading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_londri_confirmation);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);


        final AlertDialog.Builder alert = new AlertDialog.Builder(this);
        dataSource = new DatabaseHandler(this);
        valuesProfil = (ArrayList<Profil>) dataSource.getAllProfils();
        for (Profil profil : valuesProfil){
            token = profil.getToken();
        }

        buttonSetuju = (Button) findViewById(R.id.buttonSetujuWeightConfirmation);
        textViewNama = (TextView) findViewById(R.id.textViewWeightNamaKonfirmasi);
        textViewAlamat = (TextView) findViewById(R.id.textViewWeightAlamatKonfirmasi);
        textViewService = (TextView) findViewById(R.id.textViewWeightServiceKonfirmasi);
        textViewPickUp = (TextView) findViewById(R.id.textViewWeightPickupKonfirmasi);
        textViewDelivery = (TextView) findViewById(R.id.textVieWeightDeliveryKonfirmasi);
        textViewTotal = (TextView) findViewById(R.id.textViewWeightTotalKonfirmasi);
        textViewTambahan = (TextView) findViewById(R.id.textViewWeightAdditionKonfirmasi);


        /*Intent ambil = getIntent();
        //nama_alias = ambil.getStringExtra("namaUser");
        alamat = ambil.getStringExtra("alamatUser");
        phone_alias = ambil.getStringExtra("teleponUser");
        detil_lokasi = ambil.getStringExtra("detilUser");
        catatan_tambahan = ambil.getStringExtra("catatanUser");
        latitude = ambil.getStringExtra("latUser");
        longitude = ambil.getStringExtra("lngUser");
        berat = ambil.getStringExtra("beratUser");
        harga = ambil.getStringExtra("hargaUser");
        ekspress = ambil.getStringExtra("expressUser");
        id_mitra = ambil.getStringExtra("id_mitra");
        outputTanggalSekarang = ambil.getStringExtra("tanggalJemputUser");
        outputTanggalPrediksi = ambil.getStringExtra("tanggalAntarUser");
        outputWaktu = ambil.getStringExtra("waktuUser");*/


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
        latitudeMitra = intent.getDoubleExtra("latitudeMitra",0);
        longitudeMitra = intent.getDoubleExtra("longitudeMitra",0);
        hargaPilih = intent.getStringExtra("hargaPilih");
        jenisPilih = intent.getStringExtra("jenisPilih");
        layananPilih = intent.getStringExtra("layananPilih");
        totalBerat = intent.getStringExtra("totalBerat");
        totalHarga = intent.getStringExtra("totalHarga");
        catatan = intent.getStringExtra("catatan");
        waktuJemput = intent.getStringExtra("waktuJemput");
        tanggalJemput = intent.getStringExtra("tanggalJemput");
        tanggalAntar = intent.getStringExtra("tanggalAntar");
        londri = intent.getStringExtra("londri");
        foodCart = (ArrayList<Cart>)getIntent().getExtras().getSerializable("order");
        Log.d("DETIL",""+detilDeskripsiUser);

        if(layananPilih.equals("3")){
            layanan = "Layanan AALondri Ekspress";
        }else if(layananPilih.equals("2")){
            layanan = "Layanan AALondri Kilat";
        }else if(layananPilih.equals("1")){
            layanan = "Layanan AALondri Reguler";
        }

        if(londri.equals("ByWeight")){
            textViewNama.setText(namaUser);
            textViewAlamat.setText(alamatUser+"\n"+detilDeskripsiUser+"\n"+teleponUser);
            textViewService.setText("Londri by Weight "+ totalBerat +" Kg");
            textViewPickUp.setText(tanggalJemput+" : "+waktuJemput+" WIB");
            textViewDelivery.setText(tanggalAntar+" : "+waktuJemput+" WIB");
            textViewTotal.setText("IDR "+totalHarga);
            textViewTambahan.setText(layanan+"\n"+catatan);
        }else if(londri.equals("ByItem")){
            int harga = 0;
            int item = 0;
            stringBuffer = new StringBuffer();
            for (int i = 0; i <foodCart.size(); i++){
                item = item + Integer.parseInt(foodCart.get(i).getJumlah());
                harga = harga + Integer.parseInt(foodCart.get(i).getTotal_harga());
                stringBuffer.append("\n"+foodCart.get(i).getJumlah()+" "+foodCart.get(i).getMenu());
            }
            if(layananPilih.equals("1")){
                totalHarga = String.valueOf(harga);
            }else if(layananPilih.equals("2")){
                totalHarga = String.valueOf(harga * 2);
            }else if(layananPilih.equals("3")){
                totalHarga = String.valueOf(harga * 4);
            }
            totalHarga = String.valueOf(harga);
            textViewNama.setText(namaUser);
            textViewAlamat.setText(alamatUser+"\n"+detilDeskripsiUser+"\n"+teleponUser);
            textViewService.setText("Londri by Item ("+item+" item)"+stringBuffer);
            textViewPickUp.setText(tanggalJemput+" : "+waktuJemput+" WIB");
            textViewDelivery.setText(tanggalAntar+" : "+waktuJemput+" WIB");
            textViewTotal.setText("IDR "+totalHarga);
            textViewTambahan.setText(layanan+"\n"+catatan);
        }


        buttonSetuju.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alert.setTitle("Peringatan");
                alert.setMessage("Pesan sekarang?");
                alert.setPositiveButton("Ya",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // TODO Auto-generated method stub
                                requestPesanan(latitudeUser,longitudeUser,latitudeMitra+"",longitudeMitra+"",
                                        layananPilih,idMitra,alamatUser,namaUser,teleponUser,totalBerat,detilDeskripsiUser,totalHarga,
                                        catatan,latitudeUser,longitudeUser,alamatUser,tanggalJemput,tanggalAntar,waktuJemput,waktuJemput);
                            }
                        });
                alert.setNegativeButton("Tidak",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // TODO Auto-generated method stub
                            }
                        });
                alert.show();
            }
        });

    }

    public void requestPesanan(final String start_lat, final String start_lon, final String end_lat, final String end_lon, final String type, final String mitra_id,
                               final String nama_jalan, final String nama_alias, final String phone_alias,final String berat, final String detail_lokasi, final String total_harga,
                               final String catatan, final String latitude, final String longitude, final String alamat,
                               final String tanggal_mulai, final String tanggal_akhir, final String waktu_mulai, final String waktu_akhir){
        String tag_string_req = "req_login";
        loading = ProgressDialog.show(this,"Mohon Tunggu","Sedang memesan...",false,false);

        Log.d("HASIL ","\n"+start_lat+", "+start_lon
                        +"\n"+end_lat+", "+end_lat
                        +"\n"+type+", "+mitra_id
                        +"\n"+nama_jalan+", "+nama_alias
                        +"\n"+phone_alias+", "+berat
                        +"\n"+detail_lokasi+", "+total_harga
                        +"\n"+catatan+", "+latitude+", "+longitude
                        +"\n"+alamat
                        +"\n"+tanggal_mulai+", "+tanggal_akhir
                        +"\n"+waktu_mulai+", "+waktu_akhir);

        StringRequest strReq = new StringRequest(Request.Method.POST, APIConfig.API_ORDER_V2, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                hideDialog();

                Log.d("RESPON","hasil "+response);

                try {
                    JSONObject jObj = new JSONObject(response);
                    String error = jObj.getString("error");
                    if (error.equals("false")) {
                        String terima = jObj.getString("message");
                        Intent intent = new Intent(LondriConfirmationActivity.this, LondriOrderSuccessActivity.class);
                        intent.putExtra("message",terima);
                        intent.putExtra("pickUp",tanggal_akhir+", "+waktu_akhir+" WIB");
                        intent.putExtra("berat",berat);
                        intent.putExtra("total", total_harga);
                        intent.putExtra("layanan", type);
                        if(londri.equals("ByItem")){
                            intent.putExtra("jenis", "Londri Satun");
                            dataSource.tambahRiwayat(new Riwayat(terima, total_harga, layanan, tanggal_akhir+", "+waktu_akhir+" WIB"));
                        }else if(londri.equals("ByWeight")){
                            intent.putExtra("jenis", "Londri Kiloan");
                            dataSource.tambahRiwayat(new Riwayat(terima, total_harga, layanan+" "+berat+" Kg",tanggal_akhir+", "+waktu_akhir+" WIB"));
                        }
                        Toast.makeText(LondriConfirmationActivity.this, "Berhasil memesan!", Toast.LENGTH_SHORT).show();
                        startActivity(intent);
                        finish();
                    } else {
                        String gagal = jObj.getString("message");
                        Toast.makeText(getApplicationContext(), gagal, Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), "JSON Error : " + e.getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        }, new Response.ErrorListener() {
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), "Gagal Terhubung ke Server", Toast.LENGTH_LONG).show();
                hideDialog();
                Log.d("RESPON","hasil "+error);
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                if(londri.equals("ByItem")){
                    params.put("start_lat", start_lat);
                    params.put("start_lon", start_lon);
                    params.put("end_lat", end_lat);
                    params.put("end_lon", end_lon);
                    for (int i=0;i>foodCart.size();i++){
                        params.put("item_id[" +i+ "]", foodCart.get(i).getIdMenu()+","+foodCart.get(i).getJumlah()+","+foodCart.get(i).getHarga()+","+foodCart.get(i).getTotal_harga());
                    }
                    params.put("type", "0");
                    params.put("is_ekspress", type);
                    params.put("mitra_id", mitra_id);
                    params.put("nama_jalan", nama_jalan);
                    params.put("nama_alias", nama_alias);
                    params.put("phone_alias", phone_alias);
                    params.put("berat", "0");
                    params.put("total_harga", total_harga);
                    params.put("detail_lokasi", detail_lokasi);
                    params.put("catatan", catatan);
                    params.put("latitude", latitude);
                    params.put("longitude", longitude);
                    params.put("alamat", alamat);
                    params.put("tanggal_mulai", tanggal_mulai);
                    params.put("tanggal_akhir", tanggal_akhir);
                    params.put("waktu_mulai", waktu_mulai);
                    params.put("waktu_akhir", waktu_akhir);
                    params.put("mitra_id", mitra_id);
                    params.put("is_byitem", "1");
                }else if(londri.equals("ByWeight")){
                    params.put("start_lat", start_lat);
                    params.put("start_lon", start_lon);
                    params.put("end_lat", end_lat);
                    params.put("end_lon", end_lon);
                    params.put("type", "0");
                    params.put("is_ekspress", type);
                    params.put("mitra_id", mitra_id);
                    params.put("nama_jalan", nama_jalan);
                    params.put("nama_alias", nama_alias);
                    params.put("phone_alias", phone_alias);
                    params.put("berat", berat);
                    params.put("total_harga", total_harga);
                    params.put("detail_lokasi", detail_lokasi);
                    params.put("catatan", catatan);
                    params.put("latitude", latitude);
                    params.put("longitude", longitude);
                    params.put("alamat", alamat);
                    params.put("tanggal_mulai", tanggal_mulai);
                    params.put("tanggal_akhir", tanggal_akhir);
                    params.put("waktu_mulai", waktu_mulai);
                    params.put("waktu_akhir", waktu_akhir);
                    params.put("mitra_id", mitra_id);
                    params.put("is_byitem", "0");
                }


                return params;
            }

                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                String bearer = "Bearer " + token;
                Map<String, String> headers = new HashMap<String, String>();
                headers.put("Authorization", bearer);
                return headers;
            }
        };
        int socketTimeout = 40000; // 40 seconds. You can change it
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        strReq.setRetryPolicy(policy);
        AppController.getmInstance().addToRequestQueue(strReq, tag_string_req);
    }

    private void hideDialog() {
        if (loading.isShowing())
            loading.dismiss();
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
