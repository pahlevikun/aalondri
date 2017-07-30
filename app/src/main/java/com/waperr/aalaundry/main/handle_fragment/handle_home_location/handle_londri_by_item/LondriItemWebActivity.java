package com.waperr.aalaundry.main.handle_fragment.handle_home_location.handle_londri_by_item;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.waperr.aalaundry.R;
import com.waperr.aalaundry.main.handle_fragment.handle_home_location.LondriScheduleActivity;
import com.waperr.aalaundry.pojo.Cart;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;


public class LondriItemWebActivity extends AppCompatActivity {

    private String url,id,name, idMenu, menu,jumlah,harga,total_harga,alamat;
    private double lat,lng;
    private WebView webView;
    private ArrayList<Cart> foodCart = new ArrayList<Cart>();
    private ProgressDialog loading;

    private String idMitra, namaMitra, alamatMitra, teleponMitra,  namaUser,
            namaAlamatUser, detilDeskripsiUser, teleponUser, alamatUser, latitudeUser,
            longitudeUser, hargaPilih, jenisPilih, layananPilih,londri;
    private double latitudeMitra, longitudeMitra;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_londri_item_web);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar(). setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        webView = (WebView) findViewById(R.id.webViewMakanan);

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

        loading = ProgressDialog.show(this,"Mohon Tunggu","Sedang memuat...",false,false);
        loading.setCancelable(true);
        loading.setOnCancelListener(new DialogInterface.OnCancelListener(){
            @Override
            public void onCancel(DialogInterface dialog){
                hideDialog();
                finish();
            }});

        url = "http://aalondri.com/byitem";
        webView.loadUrl(url);
        webView.clearCache(true);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.addJavascriptInterface(new LoadListener(), "HTMLOUT");
        webView.setWebViewClient(new WebViewClient(){

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                return true;
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
            }

            public void onPageFinished(WebView view, String url) {
                hideDialog();
                if(url.contains("generate")){
                    view.setVisibility(View.INVISIBLE);
                    view.loadUrl("javascript:window.HTMLOUT.processHTML(document.getElementsByTagName('pre')[0].innerHTML);");
                }
            }

            @Override
            public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                hideDialog();
                finish();
            }
        });

        //destroyActivity();

    }

    class LoadListener{
        @JavascriptInterface
        public void processHTML(String html) {
            Log.d("result",html);
            String myJSONString = html;
            try {
                JSONObject jsonObj = new JSONObject(myJSONString);
                boolean error = jsonObj.getBoolean("error");
                if (!error) {
                    JSONArray array = jsonObj.getJSONArray("data");
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject data = array.getJSONObject(i);
                        idMenu = data.getString("id");
                        menu = data.getString("menu");
                        harga = data.getString("harga");
                        jumlah = data.getString("jumlah");
                        total_harga = data.getString("total_harga");
                        foodCart.add(new Cart(String.valueOf(i),idMenu, menu, harga, jumlah, total_harga));
                    }
                    if(foodCart.size()!=0){
                        Intent intent = new Intent(LondriItemWebActivity.this, LondriScheduleActivity.class);
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
                        intent.putExtra("order",foodCart);
                        startActivity(intent);
                        finish();
                    }else{
                        Toast.makeText(LondriItemWebActivity.this, "Pilih jumlah pakaian!", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                }
            }catch (Exception e){
                Toast.makeText(LondriItemWebActivity.this, "Error : "+e, Toast.LENGTH_SHORT).show();
                Log.d("ERROR",""+e);
                finish();
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if(id==android.R.id.home) {
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        hideDialog();
        finish();
    }

    private void hideDialog() {
        if (loading.isShowing())
            loading.dismiss();
    }
}
