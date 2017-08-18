package com.waperr.aalaundry.main.handle_fragment.handle_message;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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
import com.waperr.aalaundry.adapter.ChatAdapter;
import com.waperr.aalaundry.config.APIConfig;
import com.waperr.aalaundry.config.AppController;
import com.waperr.aalaundry.database.DatabaseHandler;
import com.waperr.aalaundry.pojo.Chats;
import com.waperr.aalaundry.pojo.Profil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MessageActivity extends AppCompatActivity {

    private Button btn_send_msg;
    private EditText input_msg;
    private DatabaseHandler dataSource;
    private ArrayList<Profil> valuesProfil;
    private ArrayList<Chats> valuesChat = new ArrayList<>();
    private ListView listView;
    private ChatAdapter adapter;
    private String token, idOrder,idMitra,idUser;

    private ProgressDialog loading;
    private boolean pesanKirim = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);

        listView = (ListView) findViewById(R.id.listViewChat);
        btn_send_msg = (Button)findViewById(R.id.button);
        input_msg = (EditText)findViewById(R.id.editText);

        for (Profil profil : valuesProfil) {
            token = profil.getToken();
            idUser = String.valueOf(profil.getUserID());
        }

        Intent intent = getIntent();
        idOrder = intent.getStringExtra("idOrder");
        idMitra = intent.getStringExtra("idMitra");
        String invoice = intent.getStringExtra("invoice");
        setTitle(invoice);

        requestChat(idOrder);

        adapter = new ChatAdapter(MessageActivity.this, valuesChat);
        listView.setAdapter(adapter);
        listView.setDividerHeight(0);
        adapter.notifyDataSetChanged();

        btn_send_msg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String chat = input_msg.getText().toString().trim();
                if(chat.isEmpty()){
                    Toast.makeText(MessageActivity.this, "Silahkan isi dengan benar!", Toast.LENGTH_SHORT).show();
                }else {
                    pesanKirim = false;
                    sendChat(idOrder,idUser,idMitra,chat);
                }
            }
        });

    }

    public void requestChat(final String order_id){
        String tag_string_req = "req_login";

        StringRequest strReq = new StringRequest(Request.Method.POST, APIConfig.API_CHAT_READ, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {

                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");
                    if(!error){
                        valuesChat.clear();
                        JSONArray jArray = jObj.getJSONArray("data");
                        for (int i=0; i<jArray.length();i++){
                            JSONObject data = jArray.getJSONObject(i);
                            String id = data.getString("id");
                            String sender_id = data.getString("sender_id");
                            String receiver_id = data.getString("receiver_id");
                            String sender_name = data.getString("sender_name");
                            String receiver_name = data.getString("receiver_name");
                            String content = data.getString("content");
                            valuesChat.add(new Chats(id,sender_id,receiver_id,sender_name,receiver_name,content));
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), "JSON Error : " + e.getMessage(), Toast.LENGTH_LONG).show();
                }
                adapter.notifyDataSetChanged();
                new Handler().postDelayed(new Thread() {
                    @Override
                    public void run() {
                        if(pesanKirim){
                            requestChat(order_id);
                        }
                    }
                }, 800);
            }
        }, new Response.ErrorListener() {
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), "Gagal Terhubung ke Server", Toast.LENGTH_LONG).show();
                finish();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("order_id",order_id);
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
        int socketTimeout = 5000; // 40 seconds. You can change it
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        strReq.setRetryPolicy(policy);
        AppController.getmInstance().addToRequestQueue(strReq, tag_string_req);
    }

    public void sendChat(final String order_id, final String user_id, final String mitra_id, final String content){
        String tag_string_req = "req_login";


        loading = ProgressDialog.show(MessageActivity.this,"Mohon Tunggu","Sedang mengirim...",false,false);

        StringRequest strReq = new StringRequest(Request.Method.POST, APIConfig.API_CHAT_SEND, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {

                try {
                    pesanKirim = true;
                    input_msg.setText("");
                    JSONObject jObj = new JSONObject(response);
                    hideDialog();

                } catch (JSONException e) {
                    hideDialog();
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), "JSON Error : " + e.getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        }, new Response.ErrorListener() {
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), "Gagal Terhubung ke Server", Toast.LENGTH_LONG).show();
                finish();
                hideDialog();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("order_id",order_id);
                params.put("receiver_id",mitra_id);
                params.put("content",content);
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
        int socketTimeout = 10000; // 40 seconds. You can change it
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
    public void onBackPressed() {
        finish();
    }
}
