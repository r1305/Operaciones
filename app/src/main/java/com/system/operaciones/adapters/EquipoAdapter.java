package com.system.operaciones.adapters;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.system.operaciones.R;
import com.system.operaciones.activities.LectorActivity;
import com.system.operaciones.response.RespuestaResponse;
import com.system.operaciones.utils.Credentials;
import com.system.operaciones.utils.ViewDialog;
import com.toptoche.searchablespinnerlibrary.SearchableSpinner;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EquipoAdapter extends RecyclerView.Adapter<EquipoAdapter.ViewHolder> {

    Credentials cred;
    Context ctx;
    List<JSONObject> l = new ArrayList<>();
    String tienda_id;
    JSONObject equipo;
    public EquipoAdapter(List<JSONObject> l) {
        this.l = l;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ctx = parent.getContext();
        cred = new Credentials(ctx);
        tienda_id = cred.getData("key_tienda_id");
        return new EquipoAdapter.ViewHolder(LayoutInflater.from(ctx).inflate(R.layout.item_equipo, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        JSONObject ob = l.get(position);
        Log.e("json",ob.toString());
        final String modelo = (String)ob.get("modelo");
        final String tipo = (String)ob.get("tipo");
        final String nro_serie = (String)ob.get("evap_nro_serie");
        final String logo = (String)ob.get("marca");
        final String voltaje = (String)ob.get("voltaje");
        final String nro_equipo = (String)ob.get("nro_equipo");
        final String tipo_equipo = (String)ob.get("tipo_equipo");

        holder.nro_equipo.setText(nro_equipo);
        holder.voltaje.setText(voltaje);
        System.out.println(ob);
        holder.modelo.setText(modelo);
        holder.tipo.setText(tipo);
        holder.nro_serie.setText(nro_serie);
        Glide.with(ctx)
                .load(logo)
//                .centerCrop()
                .crossFade()
                .into(holder.marca);

        if(cred.getData("key_user_type").equals("2"))
        {
            holder.edit.setVisibility(View.GONE);
        }
        if(tipo_equipo.equals("2")){
            holder.linear_voltaje.setVisibility(View.GONE);
            holder.edit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    JSONObject ob = l.get(position);
                    String cortina_id = (String)ob.get("id");
                    showModalUpdateCortina(cortina_id);
                }
            });
        }else{
            holder.linear_voltaje.setVisibility(View.VISIBLE);
            holder.edit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    JSONObject ob = l.get(position);
                    String equipo_id = (String)ob.get("id");
                    showModalUpdateEquipo(equipo_id);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        if(l==null){
            return 0;
        }else {
            return l.size();
        }
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        CardView card;
        TextView modelo,tipo,nro_serie,voltaje,nro_equipo;
        ImageView marca,edit;
        LinearLayout linear_contratista,linear_voltaje;

        private ViewHolder(View itemView) {
            super(itemView);
            card = itemView.findViewById(R.id.item_card_urgencia);
            modelo = itemView.findViewById(R.id.equipo_modelo);
            tipo = itemView.findViewById(R.id.equipo_tipo);
            nro_serie = itemView.findViewById(R.id.equipo_nro_serie);
            marca = itemView.findViewById(R.id.equipo_logo);
            edit = itemView.findViewById(R.id.edit_equipo);
            voltaje = itemView.findViewById(R.id.equipo_voltaje);
            nro_equipo = itemView.findViewById(R.id.tv_nro_equipo);
            linear_voltaje = itemView.findViewById(R.id.linear_voltaje);
        }
    }

    //Dialog New Equipo
    Button btn_cancelar,btn_registrar,btn_siguiente,btn_atras;
    LinearLayout linear_evaporadora,linear_condensadora;

    Spinner spinner_marcas;
    ArrayAdapter<String> marcas_adapter;
    String[] marcas_id;

    Spinner spinner_cond_marcas;
    ArrayAdapter<String> marcas_cond_adapter;
    String[] marcas_cond_id;
    
    ArrayAdapter<String> modelo_adapter;
    SearchableSpinner spinner_modelos;
    String[] modelos_ids;

    ArrayAdapter<String>modelo_cond_adapter;
    SearchableSpinner spinner_cond_modelos;
    String[] modelos_cond_ids;

    Spinner spinner_btus;
    ArrayAdapter<String> btu_adapter;
    String[] btus_id;

    Spinner spinner_cond_btus;
    ArrayAdapter<String> btu_cond_adapter;
    String[] btus_cond_id;

    Spinner spinner_tipos;
    ArrayAdapter<String> tipo_adapter;
    String[] tipos_id;

    Spinner spinner_cond_tipos;
    ArrayAdapter<String> tipo_cond_adapter;
    String[] tipos_cond_id;

    Spinner spinner_voltajes;
    ArrayAdapter<String> voltaje_adapter;
    String[] voltajes_id;

    Spinner spinner_cond_voltajes;
    ArrayAdapter<String> voltaje_cond_adapter;
    String[] voltajes_cond_id;

    Spinner spinner_fases;
    ArrayAdapter<String> fase_adapter;
    String[] fases_id;

    Spinner spinner_cond_fases;
    ArrayAdapter<String> fase_cond_adapter;
    String[] fases_cond_id;

    Spinner spinner_refrigerantes;
    ArrayAdapter<String> refrigerante_adapter;
    String[] refrigerantes_id;

    Spinner spinner_cond_refrigerantes;
    ArrayAdapter<String> refrigerante_cond_adapter;
    String[] refrigerantes_cond_id;

    EditText et_nro_serie,et_cond_nro_serie;
    ImageView icon_evap_scan,icon_cond_scan;

    SearchableSpinner spinner_nro_equipo;

    int tipo_nro_serie = 1;
    private static final int CODIGO_INTENT = 2;

    ViewDialog viewDialog;

    private void showModalUpdateEquipo(final String equipo_id)
    {
        viewDialog = new ViewDialog((Activity)ctx);
        viewDialog.showDialog();
        AlertDialog.Builder builder = new AlertDialog.Builder(ctx);
        LayoutInflater inflater = ((Activity)ctx).getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.dialog_update_equipo, null);
        builder.setView(dialogView);
        builder.setCancelable(false);
        spinner_marcas = dialogView.findViewById(R.id.spinner_marcas);
        spinner_cond_marcas = dialogView.findViewById(R.id.spinner_cond_marcas);
        spinner_modelos = dialogView.findViewById(R.id.spinner_modelo);
        spinner_cond_modelos = dialogView.findViewById(R.id.spinner_cond_modelo);
        spinner_btus = dialogView.findViewById(R.id.spinner_btus);
        spinner_cond_btus = dialogView.findViewById(R.id.spinner_cond_btus);
        spinner_tipos = dialogView.findViewById(R.id.spinner_tipos);
        spinner_cond_tipos = dialogView.findViewById(R.id.spinner_cond_tipos);
        spinner_voltajes = dialogView.findViewById(R.id.spinner_voltajes);
        spinner_cond_voltajes = dialogView.findViewById(R.id.spinner_cond_voltajes);
        spinner_fases = dialogView.findViewById(R.id.spinner_fases);
        spinner_cond_fases = dialogView.findViewById(R.id.spinner_cond_fases);
        spinner_refrigerantes = dialogView.findViewById(R.id.spinner_refrigerantes);
        spinner_cond_refrigerantes = dialogView.findViewById(R.id.spinner_cond_refrigerantes);
        spinner_nro_equipo = dialogView.findViewById(R.id.spinner_nro_equipo);

        et_nro_serie = dialogView.findViewById(R.id.nro_serie);
        icon_evap_scan = dialogView.findViewById(R.id.icon_evap_scan);
        et_cond_nro_serie = dialogView.findViewById(R.id.cond_nro_serie);
        icon_cond_scan = dialogView.findViewById(R.id.icon_cond_scan);

        icon_evap_scan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tipo_nro_serie=1;
                escanear();
            }
        });
        icon_cond_scan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tipo_nro_serie=2;
                escanear();
            }
        });

        btn_cancelar = dialogView.findViewById(R.id.btn_cancelar);
        btn_registrar = dialogView.findViewById(R.id.btn_registrar);
        btn_siguiente = dialogView.findViewById(R.id.btn_siguiente);
        btn_atras = dialogView.findViewById(R.id.btn_atras);

        linear_evaporadora = dialogView.findViewById(R.id.linear_evaporadora);
        linear_condensadora = dialogView.findViewById(R.id.linear_condensadora);

        getModelos();
        getMarcas();
        getBtus();
        getTipos();
        getVoltajes();
        getFases();
        getRefrigerantes();
        getCondensadoraModelos();
        getCondensadoraTipos();

        spinner_marcas.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                marca_id=marcas_id[position];
                System.out.println("marca_id: "+marca_id);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        spinner_cond_marcas.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                cond_marca_id = marcas_cond_id[position];
                System.out.println("cond_marca_id: "+cond_marca_id);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        spinner_modelos.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                modelo_id = modelos_ids[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                modelo_id = "-1";
            }
        });

        spinner_cond_modelos.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                cond_modelo_id = modelos_cond_ids[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                cond_modelo_id = "-1";
            }
        });

        spinner_btus.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                btu_id = btus_id[position];
                System.out.println("btu_id: "+btu_id);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        spinner_cond_btus.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                cond_btu_id = btus_cond_id[position];
                System.out.println("cond_btu_id: "+cond_btu_id);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        spinner_tipos.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                tipo_id = tipos_id[position];
                System.out.println("tipo_id: "+tipo_id);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        spinner_cond_tipos.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                cond_tipo_id = tipos_cond_id[position];
                System.out.println("cond_tipo_id: "+cond_tipo_id);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        spinner_voltajes.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                voltaje_id = voltajes_id[position];
                System.out.println("voltaje_id: "+voltaje_id);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        spinner_cond_voltajes.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                cond_voltaje_id = voltajes_cond_id[position];
                System.out.println("cond_voltaje_id: "+cond_voltaje_id);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        spinner_fases.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                fase_id = fases_id[position];
                System.out.println("fase_id: "+fase_id);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        spinner_cond_fases.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                cond_fase_id = fases_cond_id[position];
                System.out.println("cond_fase_id: "+cond_fase_id);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        spinner_refrigerantes.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                refrigerante_id = refrigerantes_id[position];
                System.out.println("refrigerante_id: "+refrigerante_id);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        spinner_cond_refrigerantes.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                cond_refrigerante_id = refrigerantes_cond_id[position];
                System.out.println("cond_refrigerante_id: "+cond_refrigerante_id);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        final AlertDialog alertDialog = builder.create();

        btn_registrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateEquipo(alertDialog);
            }
        });
        btn_siguiente.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                linear_evaporadora.setVisibility(View.GONE);
                linear_condensadora.setVisibility(View.VISIBLE);
                btn_siguiente.setVisibility(View.GONE);
                btn_registrar.setVisibility(View.VISIBLE);
                btn_cancelar.setVisibility(View.GONE);
                btn_atras.setVisibility(View.VISIBLE);

                et_cond_nro_serie.setText((String)equipo.get("cond_nro_serie"));
                int cond_btu_position = findBtuCond((String)equipo.get("cond_btu_id"));
                spinner_cond_btus.setSelection(cond_btu_position,true);
            }
        });
        btn_atras.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                linear_condensadora.setVisibility(View.GONE);
                linear_evaporadora.setVisibility(View.VISIBLE);
                btn_atras.setVisibility(View.GONE);
                btn_cancelar.setVisibility(View.VISIBLE);
                btn_registrar.setVisibility(View.GONE);
                btn_siguiente.setVisibility(View.VISIBLE);
            }
        });
        btn_cancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });
        viewDialog.hideDialog(2);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                alertDialog.show();
            }
        }, 2000);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                getEquipo(equipo_id);
            }
        }, 2000);

    }

    /*****************************************/
    EditText et_cortina_marca,et_cortina_modelo,et_cortina_nro_serie;
    ImageView icon_cortina_scan;
    Button btn_cortina_update_cancelar,btn_cortina_update_actualizar;


    private void showModalUpdateCortina(final String cortina_id)
    {
        viewDialog = new ViewDialog((Activity)ctx);
        viewDialog.showDialog();
        AlertDialog.Builder builder = new AlertDialog.Builder(ctx);
        LayoutInflater inflater = ((Activity)ctx).getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.dialog_update_cortina, null);
        builder.setView(dialogView);
        builder.setCancelable(false);
        et_cortina_marca = dialogView.findViewById(R.id.et_cortina_marca);
        et_cortina_modelo = dialogView.findViewById(R.id.et_cortina_modelo);
        et_cortina_nro_serie = dialogView.findViewById(R.id.et_cortina_nro_serie);
        icon_cortina_scan = dialogView.findViewById(R.id.icon_cortina_scan);

        icon_cortina_scan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tipo_nro_serie=3;
                escanear();
            }
        });


        btn_cortina_update_cancelar = dialogView.findViewById(R.id.btn_cortina_update_cancelar);
        btn_cortina_update_actualizar = dialogView.findViewById(R.id.btn_cortina_update_registrar);

        final AlertDialog alertDialog = builder.create();

        btn_cortina_update_actualizar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateCortina(alertDialog,cortina_id);
            }
        });
        btn_cortina_update_cancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });
        alertDialog.show();
        getCortina(cortina_id);
    }

    private void escanear() {
        Intent i = new Intent(ctx, LectorActivity.class);
        ((Activity)ctx).startActivityForResult(i, CODIGO_INTENT);
    }

    private void getMarcas()
    {
        String url=ctx.getApplicationContext().getString(R.string.base_url)+ctx.getApplicationContext().getString(R.string.getMarcas_url);
        RequestQueue queue = Volley.newRequestQueue(ctx);

        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        System.out.println("getMarcas_response: " + response);
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
                                    String motivo = (String)ob.get("marca");
                                    String id = (String)ob.get("id");
                                    data[i] = motivo;
                                    data_id[i] = id;
                                    i++;
                                }
                                marcas_adapter = new ArrayAdapter<>(ctx,R.layout.dropdown_style,data);
                                marcas_id = data_id;
                                spinner_marcas.setAdapter(marcas_adapter);
                                marcas_adapter.notifyDataSetChanged();

                                marcas_cond_adapter = new ArrayAdapter<>(ctx,R.layout.dropdown_style,data);
                                marcas_cond_id = data_id;
                                spinner_cond_marcas.setAdapter(marcas_cond_adapter);
                                marcas_cond_adapter.notifyDataSetChanged();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                System.out.println("getMarcas_error: " + error);
            }
        });

        // Add the request to the RequestQueue.
        queue.add(stringRequest);
    }

    private void getModelos()
    {
        String url=ctx.getApplicationContext().getString(R.string.base_url)+ctx.getApplicationContext().getString(R.string.getModelos_url);
        RequestQueue queue = Volley.newRequestQueue(ctx);

        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        System.out.println("getModelos_response: " + response);
                        try {
                            RespuestaResponse cliente = new Gson().fromJson(response, RespuestaResponse.class);
                            JSONParser parser = new JSONParser();
                            JSONArray respuesta = (JSONArray) parser.parse((String) cliente.getRespuesta());

                            if (cliente.getIde_error() == 0) {
                                Toast.makeText(ctx, cliente.getDes_error(), Toast.LENGTH_LONG).show();
                            } else {
                                String[] data = new String[respuesta.size()];
                                String[] data_id = new String[respuesta.size()];
                                int i =0;
                                for(Object o: respuesta){
                                    JSONObject ob = (JSONObject)o;
                                    String modelo = (String)ob.get("modelo");
                                    String id = (String)ob.get("id");
                                    data[i] = modelo;
                                    data_id[i] = id;
                                    i++;
                                }
                                System.out.println("data_mpdelos_length: "+data.length);
                                modelo_adapter = new ArrayAdapter<>(ctx,R.layout.dropdown_style,data);
                                spinner_modelos.setAdapter(modelo_adapter);
                                modelo_adapter.notifyDataSetChanged();
                                modelos_ids = data_id;
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                System.out.println("getModelos_error: " + error);
            }
        });

        // Add the request to the RequestQueue.
        queue.add(stringRequest);
    }

    private void getCondensadoraModelos()
    {
        String url=ctx.getApplicationContext().getString(R.string.base_url)+ctx.getApplicationContext().getString(R.string.getCondensadoraModelos_url);
        RequestQueue queue = Volley.newRequestQueue(ctx);

        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        System.out.println("getCondensadoraModelos_response: " + response);
                        try {
                            RespuestaResponse cliente = new Gson().fromJson(response, RespuestaResponse.class);
                            JSONParser parser = new JSONParser();
                            JSONArray respuesta = (JSONArray) parser.parse((String) cliente.getRespuesta());

                            if (cliente.getIde_error() == 0) {
                                Toast.makeText(ctx, cliente.getDes_error(), Toast.LENGTH_LONG).show();
                            } else {
                                String[] data = new String[respuesta.size()];
                                String[] data_id = new String[respuesta.size()];
                                int i =0;
                                for(Object o: respuesta){
                                    JSONObject ob = (JSONObject)o;
                                    String modelo = (String)ob.get("modelo");
                                    String id = (String)ob.get("id");
                                    data[i] = modelo;
                                    data_id[i] = id;
                                    i++;
                                }
                                modelo_cond_adapter = new ArrayAdapter<>(ctx,R.layout.dropdown_style,data);
                                spinner_cond_modelos.setAdapter(modelo_cond_adapter);
                                modelo_cond_adapter.notifyDataSetChanged();
                                modelos_cond_ids = data_id;
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                System.out.println("getCondensadoraModelos_error: " + error);
            }
        });

        // Add the request to the RequestQueue.
        queue.add(stringRequest);
    }

    private void getBtus()
    {
        String url=ctx.getApplicationContext().getString(R.string.base_url)+ctx.getApplicationContext().getString(R.string.getBtus_url);
        RequestQueue queue = Volley.newRequestQueue(ctx);

        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        System.out.println("getBtus_response: " + response);
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
                                    String btu = (String)ob.get("btu");
                                    String id = (String)ob.get("id");
                                    data[i] = btu;
                                    data_id[i] = id;
                                    i++;
                                }
                                btu_adapter = new ArrayAdapter<>(ctx,R.layout.dropdown_style,data);
                                btus_id = data_id;
                                spinner_btus.setAdapter(btu_adapter);
                                btu_adapter.notifyDataSetChanged();

                                btu_cond_adapter = new ArrayAdapter<>(ctx,R.layout.dropdown_style,data);
                                btus_cond_id = data_id;
                                spinner_cond_btus.setAdapter(btu_cond_adapter);
                                btu_cond_adapter.notifyDataSetChanged();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                System.out.println("getBtus_error: " + error);
            }
        });

        // Add the request to the RequestQueue.
        queue.add(stringRequest);
    }

    private void getTipos()
    {
        String url=ctx.getApplicationContext().getString(R.string.base_url)+ctx.getApplicationContext().getString(R.string.getTipos_url);
        RequestQueue queue = Volley.newRequestQueue(ctx);

        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        System.out.println("getTipos_response: " + response);
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
                                    String tipo = (String)ob.get("tipo");
                                    String id = (String)ob.get("id");
                                    data[i] = tipo;
                                    data_id[i] = id;
                                    i++;
                                }
                                tipo_adapter = new ArrayAdapter<>(ctx,R.layout.dropdown_style,data);
                                tipos_id = data_id;
                                spinner_tipos.setAdapter(tipo_adapter);
                                tipo_adapter.notifyDataSetChanged();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                System.out.println("getTipos_error: " + error);
            }
        });

        // Add the request to the RequestQueue.
        queue.add(stringRequest);
    }

    private void getCondensadoraTipos()
    {
        String url=ctx.getApplicationContext().getString(R.string.base_url)+ctx.getApplicationContext().getString(R.string.getCondensadoraTipos_url);
        RequestQueue queue = Volley.newRequestQueue(ctx);

        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        System.out.println("getCondensadoraTipos_response: " + response);
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
                                    String btu = (String)ob.get("tipo");
                                    String id = (String)ob.get("id");
                                    data[i] = btu;
                                    data_id[i] = id;
                                    i++;
                                }
                                tipo_cond_adapter = new ArrayAdapter<>(ctx,R.layout.dropdown_style,data);
                                tipos_cond_id = data_id;
                                spinner_cond_tipos.setAdapter(tipo_cond_adapter);
                                tipo_cond_adapter.notifyDataSetChanged();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                System.out.println("getCondensadoraTipos_error: " + error);
            }
        });

        // Add the request to the RequestQueue.
        queue.add(stringRequest);
    }

    private void getVoltajes()
    {
        String url=ctx.getApplicationContext().getString(R.string.base_url)+ctx.getApplicationContext().getString(R.string.getVoltajes_url);
        RequestQueue queue = Volley.newRequestQueue(ctx);

        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        System.out.println("getVoltajes_response: " + response);
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
                                    String btu = (String)ob.get("voltaje");
                                    String id = (String)ob.get("id");
                                    data[i] = btu;
                                    data_id[i] = id;
                                    i++;
                                }
                                voltaje_adapter = new ArrayAdapter<>(ctx,R.layout.dropdown_style,data);
                                voltajes_id = data_id;
                                spinner_voltajes.setAdapter(voltaje_adapter);
                                voltaje_adapter.notifyDataSetChanged();

                                voltaje_cond_adapter = new ArrayAdapter<>(ctx,R.layout.dropdown_style,data);
                                voltajes_cond_id = data_id;
                                spinner_cond_voltajes.setAdapter(voltaje_adapter);
                                voltaje_cond_adapter.notifyDataSetChanged();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                System.out.println("getTipos_error: " + error);
            }
        });

        // Add the request to the RequestQueue.
        queue.add(stringRequest);
    }

    private void getFases()
    {
        String url=ctx.getApplicationContext().getString(R.string.base_url)+ctx.getApplicationContext().getString(R.string.getFases_url);
        RequestQueue queue = Volley.newRequestQueue(ctx);

        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        System.out.println("getFases_response: " + response);
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
                                    String fase = (String)ob.get("fase");
                                    String id = (String)ob.get("id");
                                    data[i] = fase;
                                    data_id[i] = id;
                                    i++;
                                }
                                fase_adapter = new ArrayAdapter<>(ctx,R.layout.dropdown_style,data);
                                fases_id = data_id;
                                spinner_fases.setAdapter(fase_adapter);
                                fase_adapter.notifyDataSetChanged();

                                fase_cond_adapter = new ArrayAdapter<>(ctx,R.layout.dropdown_style,data);
                                fases_cond_id = data_id;
                                spinner_cond_fases.setAdapter(fase_cond_adapter);
                                fase_cond_adapter.notifyDataSetChanged();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                System.out.println("getFases_error: " + error);
            }
        });

        // Add the request to the RequestQueue.
        queue.add(stringRequest);
    }

    private void getRefrigerantes()
    {
        String url=ctx.getApplicationContext().getString(R.string.base_url)+ctx.getApplicationContext().getString(R.string.getRefrigerantes_url);
        RequestQueue queue = Volley.newRequestQueue(ctx);

        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        System.out.println("getRefrigerantes_response: " + response);
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
                                    String refrigerante = (String)ob.get("refrigerante");
                                    String id = (String)ob.get("id");
                                    data[i] = refrigerante;
                                    data_id[i] = id;
                                    i++;
                                }
                                refrigerante_adapter = new ArrayAdapter<>(ctx,R.layout.dropdown_style,data);
                                refrigerantes_id = data_id;
                                spinner_refrigerantes.setAdapter(refrigerante_adapter);
                                refrigerante_adapter.notifyDataSetChanged();

                                refrigerante_cond_adapter = new ArrayAdapter<>(ctx,R.layout.dropdown_style,data);
                                refrigerantes_cond_id = data_id;
                                spinner_cond_refrigerantes.setAdapter(refrigerante_cond_adapter);
                                refrigerante_cond_adapter.notifyDataSetChanged();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                System.out.println("getFases_error: " + error);
            }
        });

        // Add the request to the RequestQueue.
        queue.add(stringRequest);
    }

    String marca_id,cond_marca_id = "";
    String modelo_id,cond_modelo_id = "";
    String btu_id,cond_btu_id="";
    String tipo_id,cond_tipo_id="";
    String voltaje_id,cond_voltaje_id="";
    String fase_id, cond_fase_id="";
    String refrigerante_id,cond_refrigerante_id="";
    String nro_serie,cond_nro_serie="";

    private void updateEquipo(final AlertDialog alertDialog)
    {
        nro_serie = et_nro_serie.getText().toString();
        cond_nro_serie = et_cond_nro_serie.getText().toString();
        Log.e("nro_equipo",spinner_nro_equipo.getSelectedItem().toString());
        String url=ctx.getApplicationContext().getString(R.string.base_url)+ctx.getApplicationContext().getString(R.string.update_datos_equipo_url);
        RequestQueue queue = Volley.newRequestQueue(ctx);

        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        System.out.println("updateDatosEquipo_response: " + response);
                        try {
                            RespuestaResponse cliente = new Gson().fromJson(response, RespuestaResponse.class);
                            JSONParser parser = new JSONParser();
                            JSONArray respuesta = (JSONArray) parser.parse((String) cliente.getRespuesta());

                            if (cliente.getIde_error() == 0) {
                                Toast.makeText(ctx, cliente.getDes_error(), Toast.LENGTH_LONG).show();
                            } else {
                                getEquipos();
                                alertDialog.dismiss();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            System.out.println("getEquipo_error1: "+e);
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                System.out.println("getEquipos_error2: " + error);
            }
        }){
            @Override
            protected Map<String, String> getParams()
            {
                Map<String, String> params = new HashMap<>();
                params.put("equipo_id", (String)equipo.get("id"));
                //Evaporadora
                params.put("marca_id", marca_id);
                params.put("modelo_id", modelo_id);
                params.put("tipo_id", tipo_id);
                params.put("nro_serie", nro_serie);
                //Condensadora
                params.put("cond_marca_id", cond_marca_id);
                params.put("cond_modelo_id", cond_modelo_id);
                params.put("cond_btu_id", cond_btu_id);
                params.put("cond_tipo_id", cond_tipo_id);
                params.put("cond_voltaje_id", cond_voltaje_id);
                params.put("cond_fase_id", cond_fase_id);
                params.put("cond_refrigerante_id", cond_refrigerante_id);
                params.put("cond_nro_serie", cond_nro_serie);
                params.put("nro_equipo", spinner_nro_equipo.getSelectedItem().toString());
                return params;
            }
        };

        // Add the request to the RequestQueue.
        queue.add(stringRequest);
    }

    private void updateCortina(final AlertDialog alertDialog,final String cortina_id)
    {
        String url=ctx.getApplicationContext().getString(R.string.base_url)+ctx.getApplicationContext().getString(R.string.update_datos_cortina_url);
        RequestQueue queue = Volley.newRequestQueue(ctx);

        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        System.out.println("updateDatosCortina_response: " + response);
                        try {
                            RespuestaResponse cliente = new Gson().fromJson(response, RespuestaResponse.class);
                            JSONParser parser = new JSONParser();
                            JSONArray respuesta = (JSONArray) parser.parse((String) cliente.getRespuesta());

                            if (cliente.getIde_error() == 0) {
                                Toast.makeText(ctx, cliente.getDes_error(), Toast.LENGTH_LONG).show();
                            } else {
                                getEquipos();
                                alertDialog.dismiss();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            System.out.println("updateDatosCortina_error1: " + e);
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                System.out.println("updateDatosCortina_error2: " + error);
            }
        }){
            @Override
            protected Map<String, String> getParams()
            {
                Map<String, String> params = new HashMap<>();
                params.put("cortina_id", cortina_id);
                //Evaporadora
                params.put("marca", et_cortina_marca.getText().toString());
                params.put("modelo", et_cortina_modelo.getText().toString());
                params.put("nro_serie", et_cortina_nro_serie.getText().toString());
                return params;
            }
        };

        // Add the request to the RequestQueue.
        queue.add(stringRequest);
    }

    private void getEquipo(final String equipo_id)
    {
        System.out.println("equipo_id: "+equipo_id);
        String url=ctx.getApplicationContext().getString(R.string.base_url)+ctx.getApplicationContext().getString(R.string.getEquipoById_url);
        RequestQueue queue = Volley.newRequestQueue(ctx);

        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        System.out.println("getEquipo_response: " + response);
                        try {
                            RespuestaResponse cliente = new Gson().fromJson(response, RespuestaResponse.class);
                            JSONParser parser = new JSONParser();
                            JSONArray respuesta = (JSONArray) parser.parse((String) cliente.getRespuesta());

                            if (cliente.getIde_error() == 0) {
                                Toast.makeText(ctx, cliente.getDes_error(), Toast.LENGTH_LONG).show();
                            } else {
                                for(Object o : respuesta){
                                    equipo = (JSONObject)o;
                                    int nro_equipo_position = Integer.parseInt((String)equipo.get("nro_equipo"))-1;
                                    spinner_nro_equipo.setSelection(nro_equipo_position);
                                    et_nro_serie.setText((String)equipo.get("evap_nro_serie"));
                                    int evap_modelo_position = findModeloEvap((String)equipo.get("evap_modelo_id"));
                                    spinner_modelos.setSelection(evap_modelo_position);
                                    int evap_marca_position = findMarcaEvap((String)equipo.get("evap_marca_id"));
                                    spinner_marcas.setSelection(evap_marca_position);
                                    int evap_btu_position = findBtuEvap((String)equipo.get("cond_btu_id"));
                                    spinner_btus.setSelection(evap_btu_position);
                                    int evap_tipo_position = findTipoEvap((String)equipo.get("evap_tipo_id"));
                                    spinner_tipos.setSelection(evap_tipo_position);
                                    int evap_voltaje_position = findVoltajeEvap((String)equipo.get("cond_voltaje_id"));
                                    spinner_voltajes.setSelection(evap_voltaje_position);
                                    int evap_fase_position = findFaseEvap((String)equipo.get("cond_fase_id"));
                                    spinner_fases.setSelection(evap_fase_position);
                                    int evap_refrigerante_position = findRefrigeranteEvap((String)equipo.get("evap_refrigerante_id"));
                                    spinner_refrigerantes.setSelection(evap_refrigerante_position);

                                    int cond_modelo_position = findModeloCond((String)equipo.get("cond_modelo_id"));
                                    spinner_cond_modelos.setSelection(cond_modelo_position);
                                    int cond_marca_position = findMarcaCond((String)equipo.get("cond_marca_id"));
                                    spinner_cond_marcas.setSelection(cond_marca_position);
                                    int cond_voltaje_position = findVoltajeCond((String)equipo.get("cond_voltaje_id"));
                                    spinner_cond_voltajes.setSelection(cond_voltaje_position);
                                    int cond_fase_position = findFaseCond((String)equipo.get("cond_fase_id"));
                                    spinner_cond_fases.setSelection(cond_fase_position);
                                    int cond_refrigerante_position = findRefrigeranteCond((String)equipo.get("cond_refrigerante_id"));
                                    spinner_cond_refrigerantes.setSelection(cond_refrigerante_position);
                                }
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                System.out.println("getEquipo_error: " + error);
            }
        }){
            @Override
            protected Map<String, String> getParams()
            {
                Map<String, String> params = new HashMap<>();
                params.put("equipo_id", equipo_id);
                return params;
            }
        };

        // Add the request to the RequestQueue.
        queue.add(stringRequest);
    }

    private void getCortina(final String cortina_id)
    {
        System.out.println("cortina_id: "+cortina_id);
        String url=ctx.getApplicationContext().getString(R.string.base_url)+ctx.getApplicationContext().getString(R.string.getCortina_url);
        RequestQueue queue = Volley.newRequestQueue(ctx);

        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        System.out.println("getEquipo_response: " + response);
                        try {
                            RespuestaResponse cliente = new Gson().fromJson(response, RespuestaResponse.class);
                            JSONParser parser = new JSONParser();
                            JSONArray respuesta = (JSONArray) parser.parse((String) cliente.getRespuesta());

                            if (cliente.getIde_error() == 0) {
                                Toast.makeText(ctx, cliente.getDes_error(), Toast.LENGTH_LONG).show();
                            } else {
                                for(Object o : respuesta){
                                    JSONObject ob = (JSONObject)o;
                                    String marca = (String)ob.get("marca");
                                    String modelo = (String)ob.get("modelo");
                                    String nro_serie  = (String)ob.get("nro_serie");
                                    et_cortina_marca.setText(marca);
                                    et_cortina_modelo.setText(modelo);
                                    et_cortina_nro_serie.setText(nro_serie);
                                }
                                viewDialog.hideDialog(1);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            System.out.println("getCortina_error1: "+e);
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                System.out.println("getCortina_error2: " + error);
            }
        }){
            @Override
            protected Map<String, String> getParams()
            {
                Map<String, String> params = new HashMap<>();
                params.put("cortina_id", cortina_id);
                return params;
            }
        };

        // Add the request to the RequestQueue.
        queue.add(stringRequest);
    }

    public void getEquipos()
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
                                viewDialog.hideDialog(1);
                                Toast.makeText(ctx, cliente.getDes_error(), Toast.LENGTH_LONG).show();
                            } else {
                                l.clear();
                                for(Object o: respuesta){
                                    JSONObject ob = (JSONObject)o;
                                    if(!ob.get("id").equals("0"))
                                        l.add(ob);
                                }
                                notifyDataSetChanged();
                                viewDialog.hideDialog(1);
                            }
                        } catch (Exception e) {
                            viewDialog.hideDialog(1);
                            e.printStackTrace();
                            System.out.println("getEquiposTienda_error: "+e);
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                viewDialog.hideDialog(1);
                System.out.println("getEquiposTienda_error2: " + error.getMessage());
                Toast.makeText(ctx, error.getMessage(), Toast.LENGTH_LONG).show();
            }
        }){
            @Override
            protected Map<String, String> getParams()
            {
                Map<String, String> params = new HashMap<>();
                params.put("tienda_id", cred.getData("key_tienda_id"));
                return params;
            }
        };

        // Add the request to the RequestQueue.
        queue.add(stringRequest);
    }

    int findModeloEvap(String modelo)
    {
        int position=0;
        for(int i=0;i<modelos_ids.length;i++)
        {
            if(modelos_ids[i].equals(modelo)){
                position=i;
                modelo_id = modelo;
                System.out.println("modelo_position_find: "+position);
            }
        }
        return position;
    }
    int findBtuEvap(String btu)
    {
        int position=0;
        for(int i=0;i<btus_id.length;i++)
        {
            if(btus_id[i].equals(btu)){
                 position=i;
                 btu_id = btu;
                System.out.println("btu_position_find: "+position);
            }
        }
        return position;
    }
    int findMarcaEvap(String marca)
    {
        int position=0;
        for(int i=0;i<marcas_id.length;i++)
        {
            if(marcas_id[i].equals(marca)){
                position=i;
                marca_id = marca;
                System.out.println("marca_position_find: "+position);
            }
        }
        return position;
    }
    int findTipoEvap(String tipo)
    {
        int position=0;
        for(int i=0;i<tipos_id.length;i++)
        {
            if(tipos_id[i].equals(tipo)){
                position=i;
                tipo_id = tipo;
                System.out.println("tipo_position_find: "+position);
            }
        }
        return position;
    }
    int findVoltajeEvap(String voltaje)
    {
        int position=0;
        for(int i=0;i<voltajes_id.length;i++)
        {
            if(voltajes_id[i].equals(voltaje)){
                position=i;
                voltaje_id = voltaje;
                System.out.println("voltaje_position_find: "+position);
            }
        }
        return position;
    }
    int findFaseEvap(String fase)
    {
        int position=0;
        for(int i=0;i<fases_id.length;i++)
        {
            if(fases_id[i].equals(fase)){
                position=i;
                fase_id = fase;
                System.out.println("fase_position_find: "+position);
            }
        }
        return position;
    }
    int findRefrigeranteEvap(String refrigerante)
    {
        int position=0;
        for(int i=0;i<refrigerantes_id.length;i++)
        {
            if(refrigerantes_id[i].equals(refrigerante)){
                position=i;
                refrigerante_id = refrigerante;
                System.out.println("refrigerante_position_find: "+position);
            }
        }
        return position;
    }

    int findModeloCond(String modelo)
    {
        int position=0;
        for(int i=0;i<modelos_cond_ids.length;i++)
        {
            if(modelos_cond_ids[i].equals(modelo)){
                position=i;
                cond_modelo_id = modelo;
                System.out.println("modelo_cond_position_find: "+position);
            }
        }
        return position;
    }
    int findBtuCond(String btu_id)
    {
        int position=0;
        for(int i=0;i<btus_cond_id.length;i++)
        {
            if(btus_cond_id[i].equals(btu_id)){
                position=i;
                System.out.println("btu_cond_position_find: "+position);
            }
        }
        return position;
    }
    int findMarcaCond(String marca)
    {
        int position=0;
        for(int i=0;i<marcas_cond_id.length;i++)
        {
            if(marcas_cond_id[i].equals(marca)){
                position=i;
                cond_marca_id = marca;
                System.out.println("marca_position_find: "+position);
            }
        }
        return position;
    }
    int findVoltajeCond(String voltaje)
    {
        int position=0;
        for(int i=0;i<voltajes_id.length;i++)
        {
            if(voltajes_cond_id[i].equals(voltaje)){
                position=i;
                cond_voltaje_id = voltaje;
                System.out.println("voltaje_cond_position_find: "+position);
            }
        }
        return position;
    }
    int findFaseCond(String fase)
    {
        int position=0;
        for(int i=0;i<fases_cond_id.length;i++)
        {
            if(fases_cond_id[i].equals(fase)){
                position=i;
                cond_fase_id = fase;
                System.out.println("fase_cond_position_find: "+position);
            }
        }
        return position;
    }
    int findRefrigeranteCond(String refrigerante)
    {
        int position=0;
        for(int i=0;i<refrigerantes_cond_id.length;i++)
        {
            if(refrigerantes_cond_id[i].equals(refrigerante)){
                position=i;
                cond_refrigerante_id = refrigerante;
                System.out.println("refrigerante_cond_position_find: "+position);
            }
        }
        return position;
    }
}
