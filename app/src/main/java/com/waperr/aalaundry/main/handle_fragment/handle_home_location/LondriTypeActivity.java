package com.waperr.aalaundry.main.handle_fragment.handle_home_location;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.IdRes;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatRadioButton;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioGroup;
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
import com.waperr.aalaundry.main.handle_fragment.handle_home_location.handle_londri_by_item.LondriItemWebActivity;
import com.waperr.aalaundry.main.handle_fragment.handle_home_location.handle_londri_by_weight.LondriSelectWeightActivity;
import com.waperr.aalaundry.pojo.Profil;
import com.waperr.aalaundry.pojo.TipeLondri;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LondriTypeActivity extends AppCompatActivity {

    private RadioGroup radioGroupJenis, radioGroupLayanan;
    private AppCompatRadioButton radioButton;
    private Button btSelanjutnya;

    private ProgressDialog loading;

    private List<TipeLondri> valueRadioJenis = new ArrayList<TipeLondri>();
    private List<String> valueRadioLayanan = new ArrayList<String>();

    public ArrayList<Profil> valuesProfil;
    public DatabaseHandler dataSource;

    private TextView tvJenis;
    private CardView cvJenis;
    private ImageView ivInfo1, ivInfo2, ivInfo3;

    private String idMitra, namaMitra, alamatMitra, teleponMitra,  namaUser,
            namaAlamatUser, detilDeskripsiUser, teleponUser, alamatUser, latitudeUser,
            longitudeUser, hargaPilih, jenisPilih, layananPilih, londri, token;
    private double latitudeMitra, longitudeMitra;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_londri_type);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        dataSource = new DatabaseHandler(this);
        valuesProfil = (ArrayList<Profil>) dataSource.getAllProfils();

        tvJenis = (TextView) findViewById(R.id.textViewJudulJenis);
        cvJenis = (CardView) findViewById(R.id.cardViewJenis);
        ivInfo1 = (ImageView) findViewById(R.id.imageviewInfo1);
        ivInfo2 = (ImageView) findViewById(R.id.imageviewInfo2);
        ivInfo3 = (ImageView) findViewById(R.id.imageviewInfo3);

        for(Profil profil : valuesProfil){
            token = profil.getToken();
        }
        checkLogin(token);

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
        londri = intent.getStringExtra("londri");

        Log.d("DETIL",""+detilDeskripsiUser);
        final AlertDialog.Builder alert = new AlertDialog.Builder(this);


       /* valueRadioJenis.add(new TipeLondri("Cuci Kering Setrika",8000));
        valueRadioJenis.add(new TipeLondri("Cuci Kering Lipat",7000));
        valueRadioJenis.add(new TipeLondri("Cuci Kering",6000));
        valueRadioJenis.add(new TipeLondri("Setrika",4500));*/

        valueRadioLayanan.add("1");
        valueRadioLayanan.add("2");
        valueRadioLayanan.add("3");

        radioGroupJenis = (RadioGroup) findViewById(R.id.radioGroupJenis);
        radioGroupLayanan = (RadioGroup) findViewById(R.id.radioGroupLayanan);
        btSelanjutnya = (Button) findViewById(R.id.buttonLondriTypeNext);

        if(londri.equals("ByItem")){
            cvJenis.setVisibility(View.GONE);
            tvJenis.setVisibility(View.GONE);
            hargaPilih = "0";
            jenisPilih = " ";
        }

        radioGroupJenis.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, @IdRes int checkedId) {
                if(checkedId == R.id.radioButtonJenis1) {
                    hargaPilih = String.valueOf(valueRadioJenis.get(0).getHarga());
                    jenisPilih = valueRadioJenis.get(0).getNama();
                }else if(checkedId == R.id.radioButtonJenis2) {
                    hargaPilih = String.valueOf(valueRadioJenis.get(1).getHarga());
                    jenisPilih = valueRadioJenis.get(1).getNama();
                }else if(checkedId == R.id.radioButtonJenis3) {
                    hargaPilih = String.valueOf(valueRadioJenis.get(2).getHarga());
                    jenisPilih = valueRadioJenis.get(2).getNama();
                }else if(checkedId == R.id.radioButtonJenis4) {
                    hargaPilih = String.valueOf(valueRadioJenis.get(3).getHarga());
                    jenisPilih = valueRadioJenis.get(3).getNama();
                }
            }
        });

        radioGroupLayanan.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, @IdRes int checkedId) {
                if(checkedId == R.id.radioButtonLayanan1) {
                    layananPilih = valueRadioLayanan.get(0);
                }else if(checkedId == R.id.radioButtonLayanan2) {
                    layananPilih = valueRadioLayanan.get(1);
                }else if(checkedId == R.id.radioButtonLayanan3) {
                    layananPilih = valueRadioLayanan.get(2);
                }
            }
        });

        ivInfo1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alert.setTitle("Informasi");
                alert.setMessage(R.string.aareguler);
                alert.setPositiveButton("Ya",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // TODO Auto-generated method stub
                            }
                        });
                alert.show();
            }
        });

        ivInfo2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alert.setTitle("Informasi");
                alert.setMessage(R.string.aaekspress);
                alert.setPositiveButton("Ya",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // TODO Auto-generated method stub
                            }
                        });
                alert.show();
            }
        });

        ivInfo3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alert.setTitle("Informasi");
                alert.setMessage(R.string.aakilat);
                alert.setPositiveButton("Ya",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // TODO Auto-generated method stub
                            }
                        });
                alert.show();
            }
        });

        btSelanjutnya.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try{
                    if(jenisPilih.equals(null)||layananPilih.equals(null)||hargaPilih.equals(null)){
                        Toast.makeText(LondriTypeActivity.this, "Silahkan pilih Jenis dan Layanan!", Toast.LENGTH_SHORT).show();
                    }else{
                        if(londri.equals("ByWeight")){
                            Intent intent = new Intent(LondriTypeActivity.this, LondriSelectWeightActivity.class);
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
                            intent.putExtra("jenisPilih",jenisPilih);
                            intent.putExtra("layananPilih",layananPilih);
                            intent.putExtra("hargaPilih",hargaPilih);
                            intent.putExtra("latitudeMitra",latitudeMitra);
                            intent.putExtra("longitudeMitra",longitudeMitra);
                            intent.putExtra("londri",londri);
                            startActivity(intent);
                        }else if(londri.equals("ByItem")){
                            Intent intent = new Intent(LondriTypeActivity.this, LondriItemWebActivity.class);
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
                            intent.putExtra("jenisPilih",jenisPilih);
                            intent.putExtra("layananPilih",layananPilih);
                            intent.putExtra("hargaPilih",hargaPilih);
                            intent.putExtra("latitudeMitra",latitudeMitra);
                            intent.putExtra("longitudeMitra",longitudeMitra);
                            intent.putExtra("londri",londri);
                            startActivity(intent);
                        }

                    }
                }catch (Exception e){
                    Toast.makeText(LondriTypeActivity.this, "Silahkan pilih Jenis dan Layanan!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void checkLogin(final String token) {
        String tag_string_req = "req_login";
        loading = ProgressDialog.show(this,"Mohon Tunggu","Sedang memuat...",false,false);

        StringRequest strReq = new StringRequest(Request.Method.GET, APIConfig.API_GET_HARGA_V2, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {

                Log.d("RESPON","hasil "+response);

                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");
                    if (!error) {
                        JSONArray array = jObj.getJSONArray("data");
                        for (int i = 0; i<4; i++){
                            JSONObject data = array.getJSONObject(i);
                            String type_name = data.getString("type_name");
                            int type_price =  data.getInt("type_price");
                            valueRadioJenis.add(new TipeLondri(type_name,type_price));
                            ((AppCompatRadioButton) radioGroupJenis.getChildAt(i)).setText(type_name+"\nRp. "+type_price+",-");
                        }
                    } else  {
                        finish();
                        Toast.makeText(LondriTypeActivity.this, "Gagal memuat!", Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), "Json Error : " + e.getMessage(), Toast.LENGTH_LONG).show();
                }
                hideDialog();
            }
        }, new Response.ErrorListener() {
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), "Can't connect to Server", Toast.LENGTH_LONG).show();
                hideDialog();
                finish();
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<String, String>();
                headers.put("Authorization","Bearer "+token);
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
