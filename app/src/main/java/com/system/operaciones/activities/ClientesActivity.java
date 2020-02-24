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

public class ClientesActivity extends AppCompatActivity {
    Context ctx;
    Credentials cred;
    RecyclerView recycler;
    List<JSONObject> l=new ArrayList<>();
    ClienteAdapter adapter;
    ViewDialog viewDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clientes);

        ctx = this;
        cred = new Credentials(ctx);
        viewDialog = new ViewDialog(this);

//        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Clientes");

        recycler= findViewById(R.id.recycler_view_clientes);
        recycler.setLayoutManager(new LinearLayoutManager(ctx));
        adapter=new ClienteAdapter(l);
        recycler.setAdapter(adapter);
        l.clear();

        String act = cred.getData("key_act");
        Log.e("act",act);
        viewDialog.showDialog();
        getClientes("");
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    public void getClientes(final String filtro)
    {
        String url=ctx.getApplicationContext().getString(R.string.base_url)+ctx.getApplicationContext().getString(R.string.getClientes_url);
        Log.i("login_url",url);
        RequestQueue queue = Volley.newRequestQueue(ctx);

        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        System.out.println("getClientes_response: " + response);
                        try {
                            RespuestaResponse cliente = new Gson().fromJson(response, RespuestaResponse.class);
                            JSONParser parser = new JSONParser();
                            JSONArray respuesta = (JSONArray) parser.parse((String) cliente.getRespuesta());

                            if (cliente.getIde_error() == 0) {
                                viewDialog.hideDialog(2);
                                Toast.makeText(ctx, cliente.getDes_error(), Toast.LENGTH_LONG).show();
                            } else {
                                for (Object o : respuesta) {
                                    JSONObject ob = (JSONObject) o;
                                    System.out.println(ob);
                                    l.add(ob);
                                }
                                adapter.notifyDataSetChanged();
                                viewDialog.hideDialog(2);
                            }
                        } catch (Exception e) {
                            viewDialog.hideDialog(2);
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                viewDialog.hideDialog(2);
                System.out.println("login_error: " + error.getMessage());
            }
        }){
            @Override
            protected Map<String, String> getParams()
            {
                Map<String, String> params = new HashMap<>();
                params.put("filtro", filtro);
                params.put("company_id", cred.getData("key_empresa"));
                return params;
            }
        };

        // Add the request to the RequestQueue.
        queue.add(stringRequest);
    }
}
