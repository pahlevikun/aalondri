package com.waperr.aalaundry.main.handle_fragment.handle_home_location;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.waperr.aalaundry.R;
import com.waperr.aalaundry.config.APIConfig;
import com.waperr.aalaundry.config.AppController;
import com.waperr.aalaundry.database.DatabaseHandler;
import com.waperr.aalaundry.pojo.Markers;
import com.waperr.aalaundry.pojo.Profil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PickMitraActivity extends AppCompatActivity implements OnMapReadyCallback,
        LocationListener,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener {

    private ProgressDialog loading;

    private GoogleMap mGoogleMap;
    private GoogleApiClient mGoogleApiClient;
    private LocationRequest mLocationRequest;
    private Location mLastLocation;
    private Marker mCurrLocationMarker;

    private List<Markers> markerList = new ArrayList<Markers>();
    public ArrayList<Profil> valuesProfil;
    public DatabaseHandler dataSource;

    private String token, markerNama, markerMitraID, alamatMarker, teleponMarker;
    private Double markerLat, markerLng;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_londri_mitra);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        dataSource = new DatabaseHandler(this);
        valuesProfil = (ArrayList<Profil>) dataSource.getAllProfils();

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map6);
        mapFragment.getMapAsync(PickMitraActivity.this);

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(Places.GEO_DATA_API)
                .addApi(Places.PLACE_DETECTION_API)
                .enableAutoManage(this, this)
                .build();

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        mGoogleMap = googleMap;

        //Initialize Google Play Services
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this,
                    android.Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {
                buildGoogleApiClient();
                mGoogleMap.setMyLocationEnabled(true);
            }
        } else {
            buildGoogleApiClient();
            mGoogleMap.setMyLocationEnabled(true);
        }

        mGoogleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        mGoogleMap.getUiSettings().setZoomControlsEnabled(true);
        mGoogleMap.getUiSettings().setAllGesturesEnabled(true);
        mGoogleMap.getUiSettings().setMyLocationButtonEnabled(true);
        mGoogleMap.getUiSettings().setCompassEnabled(true);
        mGoogleMap.getUiSettings().setMapToolbarEnabled(false);

        makeJsonObjectRequest();
    }

    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        mGoogleApiClient.connect();
    }


    @Override
    public void onConnected(Bundle bundle) {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(1000);
        mLocationRequest.setFastestInterval(1000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
        if (ContextCompat.checkSelfPermission(this,
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
        }
    }

    @Override
    public void onConnectionSuspended(int i) {}

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {}

    @Override
    public void onLocationChanged(Location location) {
        mLastLocation = location;
        if (mCurrLocationMarker != null) {
            mCurrLocationMarker.remove();
        }

        //Place current location marker
        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());

        mGoogleMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        mGoogleMap.moveCamera(CameraUpdateFactory.zoomTo(13));

        if (mGoogleApiClient != null) {
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
        }
    }


    private void makeJsonObjectRequest() {
        mGoogleMap.clear();
        loading = ProgressDialog.show(this,"Mohon Tunggu","Memuat Mitra AA Londri...",false,false);

        for (Profil profil : valuesProfil){
            token = profil.getToken();
        }

        StringRequest jsonObjReq = new StringRequest(Request.Method.POST, APIConfig.API_GETMITRA, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                hideDialog();
                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");
                    if (!error) {
                        JSONArray dataArray = jObj.getJSONArray("data");
                        try {
                            //looping untuk mendapatkan seluruh item yang di order oleh user
                            for(int i = 0; i < dataArray.length();i++) {
                                JSONObject data = dataArray.getJSONObject(i);
                                markerMitraID = data.getString("id");
                                markerNama = data.getString("nama");
                                markerLat = data.getDouble("latitude");
                                markerLng = data.getDouble("longitude");
                                alamatMarker = data.getString("alamat");
                                teleponMarker = data.getString("telepon");

                                markerList.add(new Markers(String.valueOf(i),markerMitraID,markerNama,markerLat,markerLng,alamatMarker,teleponMarker));
                            }
                            for(int i =0;i<markerList.size();i++){
                                LatLng latLng = new LatLng(markerList.get(i).getLat(),markerList.get(i).getLong());
                                mGoogleMap.addMarker(new MarkerOptions()
                                        .position(latLng)
                                        .title(markerList.get(i).getNama_mitra())
                                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.marker)))
                                        .hideInfoWindow();
                            }
                            mGoogleMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                                @Override
                                public boolean onMarkerClick(Marker marker) {
                                    Intent intent = new Intent();
                                    String idmarker = markerList.get(Integer.parseInt(marker.getId().replace("m",""))).getMitra_id();
                                    String namalondri = markerList.get(Integer.parseInt(marker.getId().replace("m",""))).getNama_mitra();
                                    String alamatlondri = markerList.get(Integer.parseInt(marker.getId().replace("m",""))).getAlamat_mitra();
                                    String teleponlondri = markerList.get(Integer.parseInt(marker.getId().replace("m",""))).getTelepon_mitra();
                                    double latlondri = markerList.get(Integer.parseInt(marker.getId().replace("m",""))).getLat();
                                    double lnglondri = markerList.get(Integer.parseInt(marker.getId().replace("m",""))).getLong();
                                    intent.putExtra("idMitra", idmarker);
                                    intent.putExtra("namaMitra",namalondri);
                                    intent.putExtra("alamatMitra",alamatlondri);
                                    intent.putExtra("teleponMitra",teleponlondri);
                                    intent.putExtra("latlondri",latlondri);
                                    intent.putExtra("lnglondri",lnglondri);
                                    setResult(Activity.RESULT_OK,intent);
                                    finish();
                                    return false;
                                }
                            });
                        } catch (JSONException e) {
                            Toast.makeText(PickMitraActivity.this, e.toString(), Toast.LENGTH_LONG).show();
                        }
                    } else {

                    }
                } catch (JSONException e) {
                    Toast.makeText(PickMitraActivity.this, ""+e, Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                hideDialog();
                Toast.makeText(PickMitraActivity.this, ""+error, Toast.LENGTH_SHORT).show();
            }
        }){
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
        jsonObjReq.setRetryPolicy(policy);
        // Adding request to request queue
        AppController.getmInstance().addToRequestQueue(jsonObjReq);
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


    private void hideDialog() {
        if (loading.isShowing())
            loading.dismiss();
    }
}
