package com.waperr.aalaundry.main.handle_login;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.firebase.iid.FirebaseInstanceId;
import com.waperr.aalaundry.R;
import com.waperr.aalaundry.config.APIConfig;
import com.waperr.aalaundry.config.AppController;
import com.waperr.aalaundry.database.DatabaseHandler;
import com.waperr.aalaundry.main.MainActivity;
import com.waperr.aalaundry.pojo.FCM;
import com.waperr.aalaundry.pojo.Profil;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {

    private TextView tvForgot;
    private EditText etEmail, etPassword;
    private Button btLogin, btDaftar;
    private String editemail, editpassword;
    private ProgressDialog loading;

    private DatabaseHandler dataSource;
    private ArrayList<FCM> valuesFCM;
    private String token, username, mail, created_at, token_fcm;
    private int user_id;

    private boolean doubleBackToExitPressedOnce = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        boolean pertamaJalan = getSharedPreferences("DATA",MODE_PRIVATE).getBoolean("perdana",true);
        boolean session = getSharedPreferences("DATA",MODE_PRIVATE).getBoolean("session",false);

        if(pertamaJalan==true){
            introAct();
        }

        if(session==true){
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        }
        
        dataSource = new DatabaseHandler(this);

        valuesFCM = (ArrayList<FCM>) dataSource.getAllFCMs();
        for(FCM fcm : valuesFCM){
            token_fcm = fcm.getTokenFCM();
        }

        etEmail = (EditText) findViewById(R.id.editTextEmail);
        etPassword = (EditText) findViewById(R.id.editTextPassword);
        btLogin = (Button) findViewById(R.id.buttonMasuk);
        btDaftar = (Button) findViewById(R.id.buttonDaftar);
        tvForgot = (TextView) findViewById(R.id.textViewLupaPassword);

        tvForgot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, ForgotActivity.class);
                startActivity(intent);
            }
        });


        btDaftar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });

        btLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editemail = etEmail.getText().toString().trim();
                editpassword = etPassword.getText().toString().trim();

                if (editemail.isEmpty() || editpassword.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Email / Password masih kosong!", Toast.LENGTH_LONG).show();
                } else {
                    checkLogin(editemail,editpassword);
                }
            }
        });
        Log.d("TOKEN", "Refreshed token: " + FirebaseInstanceId.getInstance().getToken());
    }

    private void checkLogin(final String email, final String password) {
        String tag_string_req = "req_login";
        loading = ProgressDialog.show(this,"Mohon Tunggu","Sedang login...",false,false);

        //Buat memulai Request, AppConfig.API_LOGIN itu url untuk api login
        StringRequest strReq = new StringRequest(Request.Method.POST, APIConfig.API_LOGIN, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                hideDialog();

                //Mulai parsing json dari response API
                try {
                    JSONObject jObj = new JSONObject(response);
                    String error = jObj.getString("error");
                    //Jika tidak ada error pada response API
                    if (error.equals("false")) {
                        //session.setLogin(true);

                        JSONObject created = jObj.getJSONObject("created");
                        //Menyimpan data token dalam string token berdasarkan respon dari api jika autentikasi berhasil.
                        token = jObj.getString("token");
                        user_id = jObj.getInt("userid");
                        username = jObj.getString("username");
                        mail = jObj.getString("email");
                        created_at = created.getString("date");

                        //Menambahkan user id, username, email, token ke database SQLITE
                        //Kenapa harus disimpan, jadi biar kalau mau melakukan request get / post data
                        //Tinggal load token dari database

                        dataSource.tambahProfil(new Profil(user_id, username,mail,token,"INI TOKEN FCM",created_at));
                        SharedPreferences sharedPreferences =  getSharedPreferences("DATA",MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putBoolean("session",true);
                        editor.commit();

                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        LoginActivity.this.startActivity(intent);
                        LoginActivity.this.finish();
                        //Jika login gagal, ditandai dengan adanya pesan error "incorrect" pada respon API
                    } else if (error.equals("incorect")) {
                        //Menampilkan pesan error berdasarkan respon api
                        String gagal = jObj.getString("message");
                        Toast.makeText(getApplicationContext(), gagal, Toast.LENGTH_LONG).show();
                    } else {
                        String gagal = jObj.getString("message");
                        Toast.makeText(getApplicationContext(), gagal, Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), "Json Error : " + e.getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        }, new Response.ErrorListener() {
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), "Gagal Terhubung ke Server", Toast.LENGTH_LONG).show();
                hideDialog();
            }
        }) {
            //Untuk post data menggunakan volley
            //Data yang dikirim adalah email dan password
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("email", email);
                params.put("password", password);
                params.put("fcm_token", token_fcm);

                return params;
            }
        };


        int socketTimeout = 40000; // 40 seconds. You can change it
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        strReq.setRetryPolicy(policy);
        AppController.getmInstance().addToRequestQueue(strReq, tag_string_req);
    }

    private void introAct() {
        SharedPreferences sharedPreferences =  getSharedPreferences("DATA",MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("perdana",false);
        editor.commit();

        Intent intro = new Intent(LoginActivity.this, IntroActivity.class);
        LoginActivity.this.startActivity(intro);
    }

    private void hideDialog() {
        if (loading.isShowing())
            loading.dismiss();
    }

    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }
        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Tekan lagi untuk keluar!", Toast.LENGTH_SHORT).show();
        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce = false;
            }
        }, 2000);
    }
}
