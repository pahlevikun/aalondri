package com.waperr.aalaundry.main.handle_fragment.handle_history_fragment;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.waperr.aalaundry.R;
import com.waperr.aalaundry.adapter.HistoryAdapter;
import com.waperr.aalaundry.config.APIConfig;
import com.waperr.aalaundry.config.AppController;
import com.waperr.aalaundry.database.DatabaseHandler;
import com.waperr.aalaundry.main.MainActivity;
import com.waperr.aalaundry.pojo.History;
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
public class CompletedHistoryFragment extends Fragment {

    private ListView lv;
    private DatabaseHandler dataSource;
    public ArrayList<Profil> valuesProfil;

    private ProgressDialog loading;

    private String invoice, layanan, cek, token, selected;
    private LinearLayout linRiwayat;
    private HistoryAdapter adapter;

    private List<History> dataList = new ArrayList<History>();


    public CompletedHistoryFragment() {
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
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menuRefresh:
                makeJsonObjectRequest();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_requested_history, container, false);

        lv = (ListView) rootView.findViewById(R.id.listViewHistory);
        linRiwayat = (LinearLayout) rootView.findViewById(R.id.linLayBelumRiwayat);
        dataSource = new DatabaseHandler(getActivity());
        valuesProfil = (ArrayList<Profil>) dataSource.getAllProfils();

        adapter = new HistoryAdapter(getActivity(), dataList);
        dataList.clear();
        lv.setAdapter(adapter);

        makeJsonObjectRequest();

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                Intent intent = new Intent(getActivity(), HistoryDetailActivity.class);
                intent.putExtra("idOrder",dataList.get(position).getIdOrder());
                intent.putExtra("idUser",dataList.get(position).getIdUser());
                intent.putExtra("idKurir",dataList.get(position).getIdKurir());
                intent.putExtra("idMitra",dataList.get(position).getIdMitra());
                intent.putExtra("start_lat",dataList.get(position).getStart_lat());
                intent.putExtra("start_lon",dataList.get(position).getStart_lon());
                intent.putExtra("end_lat",dataList.get(position).getEnd_lat());
                intent.putExtra("end_lon",dataList.get(position).getEnd_lon());
                intent.putExtra("status",dataList.get(position).getStatus());
                intent.putExtra("nama_alias",dataList.get(position).getNama_alias());
                intent.putExtra("phone_alias",dataList.get(position).getPhone_alias());
                intent.putExtra("invoice_number",dataList.get(position).getInvoice_number());
                intent.putExtra("berat",dataList.get(position).getBerat());
                intent.putExtra("total_harga",dataList.get(position).getTotal_harga());
                intent.putExtra("is_ekspress",dataList.get(position).getIs_ekspress());
                intent.putExtra("is_byitem",dataList.get(position).getIs_byitem());
                intent.putExtra("tanggal_mulai",dataList.get(position).getTanggal_mulai());
                intent.putExtra("tanggal_akhir",dataList.get(position).getTanggal_akhir());
                intent.putExtra("waktu_mulai",dataList.get(position).getWaktu_mulai());
                intent.putExtra("waktu_akhir",dataList.get(position).getWaktu_akhir());
                intent.putExtra("alamat",dataList.get(position).getAlamat());
                intent.putExtra("detail_lokasi",dataList.get(position).getDetail_lokasi());
                intent.putExtra("catatan",dataList.get(position).getCatatan());
                intent.putExtra("statusDetail",dataList.get(position).getStatusDetail());
                intent.putExtra("alasan",dataList.get(position).getAlasan());

                Log.d("IDMITRA",""+dataList.get(position).getIdMitra());
                startActivity(intent);
            }
        });

        return rootView;
    }

    private void makeJsonObjectRequest() {


        for (Profil profil : valuesProfil) {
            token = profil.getToken();
        }
        loading = ProgressDialog.show(getActivity(),"Mohon Tunggu","Sedang memuat...",false,false);

        StringRequest jsonObjReq = new StringRequest(Request.Method.GET, APIConfig.API_HISTORY_V2, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {

                hideDialog();
                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");
                    if (!error) {
                        JSONArray dataArray = jObj.getJSONArray("data");
                        try {
                            for (int i = 0; i < dataArray.length(); i++) {
                                JSONObject isi = dataArray.getJSONObject(i);
                                String id = isi.getString("id");
                                String user_id = isi.getString("user_id");
                                String kurir_id = isi.getString("kurir_id");
                                String mitra_id = isi.getString("mitra_id");
                                String start_lat = isi.getString("start_lat");
                                String start_lon = isi.getString("start_lon");
                                String end_lat = isi.getString("end_lat");
                                String end_lon = isi.getString("end_lon");
                                String status = isi.getString("status");
                                try {
                                    JSONObject detail = isi.getJSONObject("detail");
                                    String nama_alias = detail.getString("nama_alias");
                                    String phone_alias = detail.getString("phone_alias");
                                    String invoice_number = detail.getString("invoice_number");
                                    String berat = detail.getString("berat");
                                    String total_harga = detail.getString("total_harga");
                                    String alamat = detail.getString("alamat");
                                    String detail_lokasi = detail.getString("detail_lokasi");
                                    String catatan = detail.getString("catatan");
                                    String statusDetail = detail.getString("status");
                                    String alasan = detail.getString("alasan");
                                    String is_ekspress = detail.getString("is_ekspress");
                                    String is_byitem = detail.getString("is_byitem");
                                    String tanggal_mulai = detail.getString("tanggal_mulai");
                                    String tanggal_akhir = detail.getString("tanggal_akhir");
                                    String waktu_mulai = detail.getString("waktu_mulai");
                                    String waktu_akhir = detail.getString("waktu_akhir");
                                    Log.d("LISTVIEW","TAMBAH LIST STATUS "+statusDetail);
                                    if(statusDetail.equals("4")) {
                                        dataList.add(new History(i+"", id, user_id, kurir_id, mitra_id, start_lat, start_lon, end_lat, end_lon,
                                                status, nama_alias, phone_alias, invoice_number, berat, total_harga, is_ekspress, is_byitem,
                                                tanggal_mulai, tanggal_akhir, waktu_mulai, waktu_akhir, alamat, detail_lokasi, catatan,
                                                statusDetail, alasan));
                                    }else if(status.equals("1")&&statusDetail.equals("0")) {
                                        dataList.add(new History(i+"", id, user_id, kurir_id, mitra_id, start_lat, start_lon, end_lat, end_lon,
                                                status, nama_alias, phone_alias, invoice_number, berat, total_harga, is_ekspress, is_byitem,
                                                tanggal_mulai, tanggal_akhir, waktu_mulai, waktu_akhir, alamat, detail_lokasi, catatan,
                                                statusDetail, alasan));
                                    }
                                }catch (Exception e){

                                }
                            }
                            if (dataList.size()==0){
                                lv.setVisibility(View.GONE);
                                linRiwayat.setVisibility(View.VISIBLE);
                            }else{
                                lv.setVisibility(View.VISIBLE);
                                linRiwayat.setVisibility(View.GONE);
                            }
                        } catch (JSONException e) {
                            Toast.makeText(getActivity(), e.toString(), Toast.LENGTH_LONG).show();
                            Log.d("ERROR 1",""+e);
                        }
                    }
                } catch (JSONException e) {
                    Toast.makeText(getActivity(), "" + e, Toast.LENGTH_SHORT).show();
                    Log.d("ERROR 2",""+e);
                }
                adapter.notifyDataSetChanged();
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {

                hideDialog();
                Toast.makeText(getActivity(), "" + error, Toast.LENGTH_SHORT).show();
            }
        }) {
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
        jsonObjReq.setRetryPolicy(policy);
        // Adding request to request queue
        AppController.getmInstance().addToRequestQueue(jsonObjReq);
    }

    private void hideDialog() {
        if (loading.isShowing())
            loading.dismiss();
    }
}
