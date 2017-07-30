package com.waperr.aalaundry.main.handle_fragment.handle_history_fragment;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
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
import com.waperr.aalaundry.main.LondriTrackingOrderActivity;
import com.waperr.aalaundry.main.handle_fragment.handle_home_location.LondriScheduleActivity;
import com.waperr.aalaundry.pojo.Profil;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class FeedbackActivity extends AppCompatActivity {

    private String idMitra,token,rating;
    private EditText editFeedback;
    private Button btSimpan;
    private Spinner spinner;

    public ArrayList<Profil> valuesProfil;
    public DatabaseHandler dataSource;

    private ProgressDialog loading;

    private String[] bankDuration ={"Select Rating","★★★★★ 5 Star","★★★★ 4 Star","★★★ 3 Star",
            "★★ 2 Star","★ 1 Star"};
    private String[] bankPosition = {"Select Rating","5","4","3","2","1"};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        spinner = (Spinner) findViewById(R.id.spinnerFeedback);
        editFeedback = (EditText) findViewById(R.id.etFeedback);
        btSimpan = (Button) findViewById(R.id.buttonFeedback);

        Intent intent = getIntent();
        idMitra = intent.getStringExtra("idMitra");

        Log.d("IDMITRA",""+idMitra);

        dataSource = new DatabaseHandler(this);
        valuesProfil = (ArrayList<Profil>) dataSource.getAllProfils();

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.adapter_spinner, bankDuration);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                if(position != 0) {
                    Toast.makeText(FeedbackActivity.this, "Anda Memilih "+bankDuration[position], Toast.LENGTH_SHORT).show();
                    rating = bankPosition[position];
                }else{

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        btSimpan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String komentar = editFeedback.getText().toString();
                if(komentar.length()<5||rating.equals("Select Rating")){
                    Toast.makeText(FeedbackActivity.this, "Silahkan isi dengan benar!", Toast.LENGTH_SHORT).show();
                    Log.d("rating",""+rating);
                }else{
                    testimoni(idMitra,komentar,rating);
                }
            }
        });

    }

    public void testimoni(final String idMitra, final String komentar, final String rating){
        String tag_string_req = "req_login";

        Log.d("PARAM",idMitra+" "+komentar+" "+rating);

        for (Profil profil : valuesProfil){
            token = profil.getToken();
        }

        loading = ProgressDialog.show(this,"Mohon Tunggu","Sedang mengirim...",false,false);

        StringRequest strReq = new StringRequest(Request.Method.POST, APIConfig.API_TESTIMONI, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                hideDialog();

                try {
                    JSONObject jObj = new JSONObject(response);
                    String error = jObj.getString("error");
                    if (error.equals("false")) {
                        Toast.makeText(FeedbackActivity.this, "Terima Kasih Telah Memberikan Testimoni", Toast.LENGTH_LONG).show();
                        finish();
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
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("mitra_id", idMitra);
                params.put("komentar", komentar);
                params.put("rating", rating);
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
