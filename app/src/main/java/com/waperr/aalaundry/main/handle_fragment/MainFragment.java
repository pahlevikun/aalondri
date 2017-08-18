package com.waperr.aalaundry.main.handle_fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.daimajia.slider.library.Animations.DescriptionAnimation;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.DefaultSliderView;
import com.waperr.aalaundry.R;
import com.waperr.aalaundry.config.APIConfig;
import com.waperr.aalaundry.config.AppController;
import com.waperr.aalaundry.config.PromoData;
import com.waperr.aalaundry.database.DatabaseHandler;
import com.waperr.aalaundry.main.handle_fragment.handle_home_location.LocationActivity;
import com.waperr.aalaundry.pojo.Profil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * A simple {@link Fragment} subclass.
 */
public class MainFragment extends Fragment {

    public ArrayList<Profil> valuesProfil;
    public DatabaseHandler dataSource;

    private LinearLayout orderByWeight, orderBytItem, orderHistory;
    private CardView cardViewByWeight, cardViewByItem;
    private TextView tvNama;

    private String token,nama;
    private HashMap<String,String> url_maps;

    private SliderLayout sliderShow;

    public MainFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // TODO Add your menu entries here
        inflater.inflate(R.menu.main, menu);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);


            sliderShow = (SliderLayout) rootView.findViewById(R.id.slider);
            orderBytItem = (LinearLayout) rootView.findViewById(R.id.linLayOrderbyItem);
            orderByWeight = (LinearLayout) rootView.findViewById(R.id.linLayOrderbyWeight);
            cardViewByItem = (CardView) rootView.findViewById(R.id.cardViewOrderByItem);
            cardViewByWeight = (CardView) rootView.findViewById(R.id.cardViewOrderByWeight);
            tvNama = (TextView) rootView.findViewById(R.id.textViewMainNama);

            dataSource = new DatabaseHandler(getActivity());
            valuesProfil = (ArrayList<Profil>) dataSource.getAllProfils();
            for (Profil profil : valuesProfil){
                token = profil.getToken();
                nama = profil.getUsername();
            }
            tvNama.setText("Hai "+nama+"!");

            cardViewByItem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intentB = new Intent(getActivity(), LocationActivity.class);
                    intentB.putExtra("dataLondri","ByItem");
                    startActivity(intentB);
                }
            });

            cardViewByWeight.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intentA = new Intent(getActivity(), LocationActivity.class);
                    intentA.putExtra("dataLondri","ByWeight");
                    startActivity(intentA);
                }
            });

            makeJsonObjectRequest();

            return rootView;
        }

    private void makeJsonObjectRequest() {

        StringRequest jsonObjReq = new StringRequest(Request.Method.GET, APIConfig.API_PROMO, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");
                    //Mulai parsing json untuk menampilkan order.
                    if (error == false) {
                        String message = jObj.getString("data");
                        List<PromoData> data = new ArrayList<>();
                        JSONArray jArray = new JSONArray(message);
                        try {
                            //looping untuk mendapatkan seluruh item yang di order oleh user
                            url_maps = new HashMap<String, String>();
                            for(int i = 0; i < jArray.length();i++) {
                                JSONObject promo_data = jArray.getJSONObject(i);
                                PromoData promoData = new PromoData();
                                promoData.judul = promo_data.getString("judul");
                                promoData.konten = promo_data.getString("konten");
                                promoData.gambar = promo_data.getString("gambar");

                                url_maps.put(promoData.judul, promoData.gambar);
                            }
                            for(String name : url_maps.keySet()){
                                DefaultSliderView defaultSliderView = new DefaultSliderView(getActivity());
                                // initialize a SliderLayout
                                defaultSliderView
                                        .description(name)
                                        .image(url_maps.get(name))
                                        .setScaleType(BaseSliderView.ScaleType.Fit);

                                Log.d("URL GAMBAR",""+url_maps.get(name));
                                //add your extra information
                                defaultSliderView.bundle(new Bundle());
                                defaultSliderView.getBundle()
                                        .putString("extra",name);

                                sliderShow.addSlider(defaultSliderView);
                            }
                            sliderShow.setPresetTransformer(SliderLayout.Transformer.Default);
                            sliderShow.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);
                            sliderShow.setCustomAnimation(new DescriptionAnimation());
                            sliderShow.setDuration(6000);
                        } catch (JSONException e) {
                            Toast.makeText(getActivity(), e.toString(), Toast.LENGTH_LONG).show();
                        }
                    } else {

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {

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

        // Adding request to request queue
        AppController.getmInstance().addToRequestQueue(jsonObjReq);
    }


    @Override
    public void onStop() {
        sliderShow.stopAutoCycle();
        super.onStop();
    }


}
