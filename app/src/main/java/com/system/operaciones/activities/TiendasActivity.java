package com.system.operaciones.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.system.operaciones.R;
import com.system.operaciones.adapters.ClienteAdapter;
import com.system.operaciones.adapters.TiendaAdapter;
import com.system.operaciones.response.RespuestaResponse;
import com.system.operaciones.utils.Credentials;
import com.system.operaciones.utils.ViewDialog;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class TiendasActivity extends AppCompatActivity {
    Context ctx;
    Credentials cred;
    RecyclerView recycler;
    List<JSONObject> l=new ArrayList<>();
    TiendaAdapter adapter;
    ViewDialog viewDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tiendas);

        ctx = this;
        cred = new Credentials(ctx);
        viewDialog = new ViewDialog(this);

        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Tiendas");

        recycler= findViewById(R.id.recycler_view_tiendas);
        recycler.setLayoutManager(new LinearLayoutManager(ctx));
        adapter=new TiendaAdapter(l);
        recycler.setAdapter(adapter);
        l.clear();

        String cliente = cred.getData("key_cliente");
        System.out.println(cliente);
        viewDialog.showDialog();
        getTiendas(cliente,"");
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    public void getTiendas(final String cliente_id,final String filtro)
    {
        String url=ctx.getApplicationContext().getString(R.string.base_url)+ctx.getApplicationContext().getString(R.string.getTiendas_url);
        Log.i("getTiendas_url",url);
        RequestQueue queue = Volley.newRequestQueue(ctx);

        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        System.out.println("getTiendas_response: " + response);
                        try {
                            RespuestaResponse cliente = new Gson().fromJson(response, RespuestaResponse.class);
                            JSONParser parser = new JSONParser();
                            JSONArray respuesta = (JSONArray) parser.parse((String) cliente.getRespuesta());

                            if (cliente.getIde_error() == 0) {
                                viewDialog.hideDialog(5);
                                Toast.makeText(ctx, cliente.getDes_error(), Toast.LENGTH_LONG).show();
                            } else {
                                for (Object o : respuesta) {
                                    JSONObject ob = (JSONObject) o;
                                    System.out.println(ob);
                                    l.add(ob);
                                }
                                adapter.notifyDataSetChanged();
                                viewDialog.hideDialog(5);
                            }
                        } catch (Exception e) {
                            viewDialog.hideDialog(5);
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println("login_error: " + error.getMessage());
                viewDialog.hideDialog(5);
            }
        }){
            @Override
            protected Map<String, String> getParams()
            {
                Map<String, String> params = new HashMap<>();
                params.put("filtro", filtro);
                params.put("cliente_id", cliente_id);
                return params;
            }
        };

        // Add the request to the RequestQueue.
        queue.add(stringRequest);
    }
}
