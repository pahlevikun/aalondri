package com.waperr.aalaundry.main;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import com.waperr.aalaundry.pojo.Profil;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class LondriTrackingOrderActivity extends AppCompatActivity {

    private String nama_alias, invoice_number, berat, harga, tanggal, layanan, status, mitra_id, token;
    private ImageView ivTracking;
    private EditText editFeedback;
    private Button btSimpan;
    private LinearLayout linLayFeedback;

    public ArrayList<Profil> valuesProfil;
    public DatabaseHandler dataSource;

    private ProgressDialog loading;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_londri_tracking_order);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        dataSource = new DatabaseHandler(this);
        valuesProfil = (ArrayList<Profil>) dataSource.getAllProfils();


        ivTracking = (ImageView) findViewById(R.id.imageViewTracking);
        linLayFeedback = (LinearLayout) findViewById(R.id.linLayFeedback);
        editFeedback = (EditText) findViewById(R.id.etFeedback);
        btSimpan = (Button) findViewById(R.id.buttonFeedback);

        Intent ambil = getIntent();
        nama_alias = ambil.getStringExtra("nama_alias");
        invoice_number = ambil.getStringExtra("invoice_number");
        berat = ambil.getStringExtra("berat");
        harga = ambil.getStringExtra("harga");
        tanggal = ambil.getStringExtra("tanggal");
        layanan = ambil.getStringExtra("layanan");
        status = ambil.getStringExtra("status");
        mitra_id = ambil.getStringExtra("mitra_id");

        linLayFeedback.setVisibility(View.GONE);

        if(status.equals("Order Belum Ditangani")){
            ivTracking.setImageResource(R.drawable.tracking1);
            linLayFeedback.setVisibility(View.INVISIBLE);
        }else if(status.equals("Order Telah Dijemput Kurir")){
            ivTracking.setImageResource(R.drawable.tracking2);
            linLayFeedback.setVisibility(View.INVISIBLE);
        }else if(status.equals("Order Sedang Diproses Pihak Laundry")||status.equals("Order Sedang Diantar Kembali Oleh Kurir")){
            ivTracking.setImageResource(R.drawable.tracking3);
            linLayFeedback.setVisibility(View.INVISIBLE);
        }else{
            ivTracking.setImageResource(R.drawable.tracking4);
            linLayFeedback.setVisibility(View.VISIBLE);
        }
        btSimpan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                testimoni();
            }
        });

    }

    public void testimoni(){
        String tag_string_req = "req_login";

        for (Profil profil : valuesProfil){
            token = profil.getToken();
        }

        final String testi = editFeedback.getText().toString().trim();

        loading = ProgressDialog.show(this,"Mohon Tunggu","Sedang memesan...",false,false);

        //Buat memulai Request, AppConfig.API_LOGIN itu url untuk api login
        StringRequest strReq = new StringRequest(Request.Method.POST, APIConfig.API_TESTIMONI, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                hideDialog();

                //Mulai parsing json dari response API
                try {
                    JSONObject jObj = new JSONObject(response);
                    String error = jObj.getString("error");
                    //Jika tidak ada error pada response API
                    if (error.equals("false")) {
                        //Jika login gagal, ditandai dengan adanya pesan error "incorrect" pada respon API
                        Toast.makeText(LondriTrackingOrderActivity.this, "Terima Kasih Telah Memberikan Testimoni", Toast.LENGTH_LONG).show();
                        editFeedback.setText("");
                        linLayFeedback.setVisibility(View.GONE);
                    } else {
                        Toast.makeText(getApplicationContext(), "Gagal memberikan testimoni", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), "Error : " + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), "Gagal Terhubung ke Server", Toast.LENGTH_SHORT).show();
                hideDialog();
            }
        }) {
            //Untuk post data menggunakan volley
            //Data yang dikirim adalah email dan password
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("mitra_id", mitra_id);
                params.put("komentar", testi);

                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                String bearer = "Bearer " + token;
                Map<String, String> headersSys = super.getHeaders();
                Map<String, String> headers = new HashMap<String, String>();
                headersSys.remove("Authorization");
                headers.put("Authorization", bearer);
                headers.putAll(headersSys);
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


