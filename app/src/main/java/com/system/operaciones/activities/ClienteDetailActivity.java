package com.system.operaciones.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.system.operaciones.R;
import com.system.operaciones.response.RespuestaResponse;
import com.system.operaciones.utils.Credentials;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class ClienteDetailActivity extends AppCompatActivity implements View.OnClickListener {
    Context ctx;
    Credentials cred;
    LinearLayout datos,equipos,servicios;
    LinearLayout tab_datos,tab_equipos,tab_servicios;

    TextView tv_admin_cel,tv_admin,tv_tienda_tlf,tv_tienda,tv_email,tv_distrito,tv_direccion;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cliente_detail);
        ctx = this;
        cred = new Credentials(ctx);

        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("");
        datos = findViewById(R.id.linear_datos);
        tab_datos = findViewById(R.id.tab_datos);
        equipos = findViewById(R.id.linear_equipos);
        tab_equipos = findViewById(R.id.tab_equipos);
        servicios = findViewById(R.id.linear_servicios);
        tab_servicios = findViewById(R.id.tab_servicios);

        tv_admin_cel = findViewById(R.id.admin_cel);
        tv_admin = findViewById(R.id.admin);
        tv_tienda = findViewById(R.id.tienda);
        tv_tienda_tlf = findViewById(R.id.tienda_tlf);
        tv_email = findViewById(R.id.email);
        tv_distrito = findViewById(R.id.distrito);
        tv_direccion = findViewById(R.id.direccion);

        Intent intent = getIntent();
        String tienda_id = intent.getStringExtra("tienda_id");

        getTienda(tienda_id);

        datos.setVisibility(View.VISIBLE);
        tab_datos.setBackgroundColor(getResources().getColor(R.color.celesteClaro));

        tab_datos.setOnClickListener(this);
        tab_equipos.setOnClickListener(this);
        tab_servicios.setOnClickListener(this);

    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();

        switch (id){
            case R.id.tab_datos:
                hideTabs();
                datos.setVisibility(View.VISIBLE);
                tab_datos.setBackgroundColor(getResources().getColor(R.color.celesteClaro));
            break;
            case R.id.tab_equipos:
                hideTabs();
                equipos.setVisibility(View.VISIBLE);
                tab_equipos.setBackgroundColor(getResources().getColor(R.color.celesteClaro));
            break;
            case R.id.tab_servicios:
                hideTabs();
                servicios.setVisibility(View.VISIBLE);
                tab_servicios.setBackgroundColor(getResources().getColor(R.color.celesteClaro));
            break;
        }
    }

    public void hideTabs()
    {
        datos.setVisibility(View.INVISIBLE);
        equipos.setVisibility(View.INVISIBLE);
        servicios.setVisibility(View.INVISIBLE);
        tab_datos.setBackgroundColor(getResources().getColor(R.color.white));
        tab_equipos.setBackgroundColor(getResources().getColor(R.color.white));
        tab_servicios.setBackgroundColor(getResources().getColor(R.color.white));
    }

    public void getTienda(final String tienda_id)
    {
        String url=ctx.getApplicationContext().getString(R.string.base_url)+ctx.getApplicationContext().getString(R.string.getTienda_url);
        Log.i("getTienda_url",url);
        RequestQueue queue = Volley.newRequestQueue(ctx);

        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        System.out.println("getTienda_response: " + response);
                        try {
                            RespuestaResponse cliente = new Gson().fromJson(response, RespuestaResponse.class);
                            JSONParser parser = new JSONParser();
                            JSONArray respuesta = (JSONArray) parser.parse((String) cliente.getRespuesta());

                            if (cliente.getIde_error() == 0) {
                                Toast.makeText(ctx, cliente.getDes_error(), Toast.LENGTH_LONG).show();
                            } else {
                                for(Object o: respuesta){
                                    JSONObject ob = (JSONObject)o;
                                    String tienda = (String)ob.get("tienda");
                                    String tienda_tlf = (String)ob.get("tienda_cel");
                                    String admin = (String)ob.get("administrador");
                                    String admin_cel = (String)ob.get("admin_cel");
                                    String direccion = (String)ob.get("direccion");
                                    String distrito = (String)ob.get("distrito");

                                    tv_tienda.setText(tienda);
                                    tv_tienda_tlf.setText(tienda_tlf);
                                    tv_admin.setText(admin);
                                    tv_admin_cel.setText(admin_cel);
                                    tv_direccion.setText(direccion);
                                    tv_distrito.setText(distrito);
                                }
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println("getTienda_error: " + error.getMessage());
            }
        }){
            @Override
            protected Map<String, String> getParams()
            {
                Map<String, String> params = new HashMap<>();
                params.put("tienda_id", tienda_id);
                return params;
            }
        };

        // Add the request to the RequestQueue.
        queue.add(stringRequest);
    }
}
