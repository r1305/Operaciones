package com.system.operaciones.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
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
import com.system.operaciones.adapters.EquipoAdapter;
import com.system.operaciones.adapters.TiendaAdapter;
import com.system.operaciones.adapters.UrgenciaAdapter;
import com.system.operaciones.response.RespuestaResponse;
import com.system.operaciones.utils.Credentials;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class UrgenciasActivity extends AppCompatActivity implements View.OnClickListener{

    Context ctx;
    Credentials cred;
    LinearLayout datos,equipos,servicios;
    LinearLayout tab_datos,tab_equipos,tab_servicios;
    ImageView btn_new_urgencia,icon_tuerca,icon_split,icon_check;
    Spinner equipos_spin,motivos_spin;
    TextView tv_admin_cel,tv_admin,tv_tienda_tlf,tv_tienda,tv_email,tv_distrito,tv_direccion;
    TextView txt_settings,txt_equipos,txt_datos,observaciones;
    Button dialog_btn_cancelar,dialog_btn_registrar;
    AlertDialog alertDialog;
    String tienda_id;
    ArrayAdapter<String> motivo_adapter,equipo_adapter;
    String[] motivo_id_adapter,equipo_id_adapter;

    RecyclerView recycler;
    List<JSONObject> l=new ArrayList<>();
    UrgenciaAdapter adapter;

    RecyclerView equipos_recycler;
    List<JSONObject> equipos_l=new ArrayList<>();
    EquipoAdapter equipos_adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_urgencias);
        ctx = this;
        cred = new Credentials(ctx);

        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Urgencias");
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

        btn_new_urgencia = findViewById(R.id.btn_new_urgencia);
        icon_tuerca = findViewById(R.id.icon_tuerca);
        icon_split = findViewById(R.id.icon_split);
        icon_check = findViewById(R.id.icon_check);

        txt_datos = findViewById(R.id.txt_datos);
        txt_equipos = findViewById(R.id.txt_equipos);
        txt_settings = findViewById(R.id.txt_settings);

        recycler= findViewById(R.id.recycler_view_urgencias);
        recycler.setLayoutManager(new LinearLayoutManager(ctx));
        adapter=new UrgenciaAdapter(l);
        recycler.setAdapter(adapter);
        l.clear();

        equipos_recycler= findViewById(R.id.recycler_view_urgencias_equipos);
        equipos_recycler.setLayoutManager(new LinearLayoutManager(ctx));
        equipos_adapter=new EquipoAdapter(equipos_l);
        equipos_recycler.setAdapter(equipos_adapter);
        equipos_l.clear();

        btn_new_urgencia.setOnClickListener(this);

        Intent intent = getIntent();
        tienda_id = intent.getStringExtra("tienda_id");
        setTienda_id(tienda_id);
        getTienda(tienda_id);
        getUrgencias(tienda_id);

        datos.setVisibility(View.VISIBLE);
        tab_datos.setBackgroundColor(getResources().getColor(R.color.verdePastel));

        tab_datos.setOnClickListener(this);
        tab_equipos.setOnClickListener(this);
        tab_servicios.setOnClickListener(this);
        txt_datos.setTextColor(ctx.getResources().getColor(R.color.blanco,null));
        icon_check.setImageResource(R.drawable.icon_check_white);

        equipos_l.add(new JSONObject());
        equipos_l.add(new JSONObject());
        equipos_adapter.notifyDataSetChanged();
    }

    public String getTienda_id() {
        return tienda_id;
    }

    public void setTienda_id(String tienda_id) {
        this.tienda_id = tienda_id;
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
                txt_datos.setTextColor(ctx.getResources().getColor(R.color.blanco));
                icon_check.setImageResource(R.drawable.icon_check_white);
                tab_datos.setBackgroundColor(getResources().getColor(R.color.verdePastel));
                break;
            case R.id.tab_equipos:
                hideTabs();
                equipos.setVisibility(View.VISIBLE);
                txt_equipos.setTextColor(ctx.getResources().getColor(R.color.blanco));
                icon_split.setImageResource(R.drawable.icon_split_white);
                tab_equipos.setBackgroundColor(getResources().getColor(R.color.verdePastel));
                break;
            case R.id.tab_servicios:
                hideTabs();
                icon_tuerca.setImageResource(R.drawable.icon_settings);
                txt_settings.setTextColor(ctx.getResources().getColor(R.color.blanco));
                servicios.setVisibility(View.VISIBLE);
                tab_servicios.setBackgroundColor(getResources().getColor(R.color.verdePastel));
                break;
            case R.id.btn_new_urgencia:
                final AlertDialog.Builder builder = new AlertDialog.Builder(ctx);
//                LayoutInflater inflater = getLayoutInflater();
//                View dialogView = inflater.inflate(R.layout.dialog_new_urgencia, null);
                // retrieve display dimensions
                Rect displayRectangle = new Rect();
                Window window = getWindow();
                window.getDecorView().getWindowVisibleDisplayFrame(displayRectangle);
                // inflate and adjust layout
                LayoutInflater inflater = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View dialogView = inflater.inflate(R.layout.dialog_new_urgencia, null);
                dialogView.setMinimumWidth((int)(displayRectangle.width() * 0.7f));
                dialogView.setMinimumHeight((int)(displayRectangle.height() * 0.7f));

                builder.setView(dialogView);

                equipos_spin = dialogView.findViewById(R.id.urgencia_spinner_equipos);
                equipos_spin.setDropDownWidth(500);
                motivos_spin = dialogView.findViewById(R.id.urgencia_spinner_motivos);
                dialog_btn_cancelar = dialogView.findViewById(R.id.dialog_btn_cancelar);
                dialog_btn_registrar = dialogView.findViewById(R.id.dialog_btn_registrar);
                observaciones = dialogView.findViewById(R.id.dialog_observaciones);
                //getEquipos(tienda_id);
                String[] data= {"Equipo 1","Equipo 2"};
                String[] data_id= {"1","2"};
                equipo_adapter = new ArrayAdapter<>(ctx,R.layout.dropdown_style,data);
                equipo_id_adapter = new String[2];
                equipo_id_adapter = data_id;

                equipos_spin.setAdapter(equipo_adapter);
                equipo_adapter.notifyDataSetChanged();

                getMotivos();
                dialog_btn_cancelar.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        alertDialog.dismiss();
                    }
                });
                dialog_btn_registrar.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        alertDialog.dismiss();
                        motivos_spin.getSelectedItem();
                        long motivo_id = motivo_adapter.getItemId(motivos_spin.getSelectedItemPosition());
                        long equipo_id =equipo_adapter.getItemId(equipos_spin.getSelectedItemPosition());
                        String item_motivo = motivo_id_adapter[Integer.parseInt(motivo_id+"")];
                        String item_equipo = motivo_id_adapter[Integer.parseInt(equipo_id+"")];
                        Log.e("item_motivo",item_motivo);
                        Log.e("item_equipo",item_equipo);
                        registerUrgencia(item_motivo,observaciones.getText().toString(),item_equipo);
                    }
                });
                alertDialog = builder.create();
                alertDialog.show();
                break;
        }
    }

    public void hideTabs()
    {
        datos.setVisibility(View.GONE);
        equipos.setVisibility(View.GONE);
        servicios.setVisibility(View.GONE);
        tab_datos.setBackgroundColor(getResources().getColor(R.color.plomoBackground));
        tab_equipos.setBackgroundColor(getResources().getColor(R.color.plomoBackground));
        tab_servicios.setBackgroundColor(getResources().getColor(R.color.plomoBackground));

        icon_tuerca.setImageResource(R.drawable.icon_settings_black);
        icon_split.setImageResource(R.drawable.icon_split);
        icon_check.setImageResource(R.drawable.icon_check);

        txt_datos.setTextColor(ctx.getResources().getColor(R.color.negro));
        txt_equipos.setTextColor(ctx.getResources().getColor(R.color.negro));
        txt_settings.setTextColor(ctx.getResources().getColor(R.color.negro));
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
                                    tv_direccion.setText(direccion+" - "+distrito);
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

    public void getUrgencias(final String tienda_id)
    {
        String url=ctx.getApplicationContext().getString(R.string.base_url)+ctx.getApplicationContext().getString(R.string.getUrgencias_url);
        Log.i("getUrgencia_url",url);
        RequestQueue queue = Volley.newRequestQueue(ctx);

        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        System.out.println("getUrgencia_response: " + response);
                        try {
                            RespuestaResponse cliente = new Gson().fromJson(response, RespuestaResponse.class);
                            JSONParser parser = new JSONParser();
                            JSONArray respuesta = (JSONArray) parser.parse((String) cliente.getRespuesta());

                            if (cliente.getIde_error() == 0) {
                                Toast.makeText(ctx, cliente.getDes_error(), Toast.LENGTH_LONG).show();
                            } else {
                                l.clear();
                                for(Object o: respuesta){
                                    JSONObject ob = (JSONObject)o;
                                    l.add(ob);
                                }
                                adapter.notifyDataSetChanged();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println("getUrgencia_error: " + error.getMessage());
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

    public void getEquipos(final String tienda_id)
    {
        String url=ctx.getApplicationContext().getString(R.string.base_url)+ctx.getApplicationContext().getString(R.string.getEquipos_url);
        Log.i("getEquipos_url",url);
        RequestQueue queue = Volley.newRequestQueue(ctx);

        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        System.out.println("getEquipos_response: " + response);
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
                System.out.println("getEquipos_error: " + error.getMessage());
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

    public void getMotivos()
    {
        String url=ctx.getApplicationContext().getString(R.string.base_url)+ctx.getApplicationContext().getString(R.string.getMotivos_url);
        Log.i("getMotivos_url",url);
        RequestQueue queue = Volley.newRequestQueue(ctx);

        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        System.out.println("getMotivos_response: " + response);
                        try {
                            RespuestaResponse cliente = new Gson().fromJson(response, RespuestaResponse.class);
                            JSONParser parser = new JSONParser();
                            JSONArray respuesta = (JSONArray) parser.parse((String) cliente.getRespuesta());

                            if (cliente.getIde_error() == 0) {
                                Toast.makeText(ctx, cliente.getDes_error(), Toast.LENGTH_LONG).show();
                            } else {
                                String[] data = new String[respuesta.size()];
                                String[] data_id = new String[respuesta.size()];
                                int i = 0;
                                for(Object o: respuesta){
                                    JSONObject ob = (JSONObject)o;
                                    String motivo = (String)ob.get("motivo");
                                    String id = (String)ob.get("id");
                                    data[i] = motivo;
                                    data_id[i] = id;
                                    i++;
                                }

                                motivo_adapter = new ArrayAdapter<String>(ctx,R.layout.dropdown_style,data);
                                motivo_id_adapter = data_id;
                                motivos_spin.setAdapter(motivo_adapter);
                                motivo_adapter.notifyDataSetChanged();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println("getMotivos_error: " + error.getMessage());
            }
        });

        // Add the request to the RequestQueue.
        queue.add(stringRequest);
    }

    public void registerUrgencia(final String motivo_id,final String observaciones,final String equipo_id)
    {
        String url=ctx.getApplicationContext().getString(R.string.base_url)+ctx.getApplicationContext().getString(R.string.crear_urgencia_url);
        Log.i("create_urgencia_url",url);
        RequestQueue queue = Volley.newRequestQueue(ctx);

        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        System.out.println("create_urgencia_response: " + response);
                        try {
                            RespuestaResponse cliente = new Gson().fromJson(response, RespuestaResponse.class);
                            JSONParser parser = new JSONParser();
                            JSONArray respuesta = (JSONArray) parser.parse((String) cliente.getRespuesta());

                            if (cliente.getIde_error() == 0) {
                                Toast.makeText(ctx, cliente.getDes_error(), Toast.LENGTH_LONG).show();
                            } else {
                                String[] data = new String[respuesta.size()];

                                int i = 0;
                                for(Object o: respuesta){
                                    JSONObject ob = (JSONObject)o;
                                    String motivo = (String)ob.get("motivo");
                                    data[i] = motivo;
                                    i++;
                                }

                                ArrayAdapter<String> adapter = new ArrayAdapter<String>(ctx,R.layout.dropdown_style,data);
                                motivos_spin.setAdapter(adapter);

                                adapter.notifyDataSetChanged();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println("create_urgencia_error: " + error.getMessage());
            }
        }){
            @Override
            protected Map<String, String> getParams()
            {
                Map<String, String> params = new HashMap<>();
                params.put("tienda_id", tienda_id);
                params.put("usuario_id", cred.getData("user_id"));
                params.put("motivo_id", motivo_id);
                params.put("equipo_id", equipo_id);
                params.put("observaciones", observaciones);
                return params;
            }
        };

        // Add the request to the RequestQueue.
        queue.add(stringRequest);
    }
}
