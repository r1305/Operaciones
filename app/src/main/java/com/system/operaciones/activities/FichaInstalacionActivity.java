package com.system.operaciones.activities;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.github.gcacace.signaturepad.views.SignaturePad;
import com.google.gson.Gson;
import com.system.operaciones.R;
import com.system.operaciones.response.RespuestaResponse;
import com.system.operaciones.utils.Credentials;
import com.system.operaciones.utils.Image;
import com.system.operaciones.utils.Utils;
import com.system.operaciones.utils.ViewDialog;
import com.toptoche.searchablespinnerlibrary.SearchableSpinner;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class FichaInstalacionActivity extends AppCompatActivity implements View.OnClickListener,AdapterView.OnItemSelectedListener {

    Context ctx;
    Credentials cred;

    private Button btn_evaporadora,btn_condensadora,btn_firma_cliente,btn_firma_tecnico,btn_cortina,btn_bomba,btn_fotos,btn_lecturas;
    private Button btn_cancelar,btn_registrar;
    private String id,tienda_id;

    private String linea_baja="",linea_baja_mtr="",separador="",presostato_baja="",linea_alta="",linea_alta_mtr="",secador="",presostato_alta="",cableado="",cableado_mtr="",interruptor="",interruptor_mtr="",protector="",bomba="",tuberia="",bandeja="",lugar="";
    private Spinner spinner_linea_baja,spinner_linea_baja_mtr,spinner_separador,spinner_presostato_baja,spinner_linea_alta,spinner_linea_alta_mtr,spinner_secador,spinner_presostato_alta,spinner_cableado,spinner_cableado_mtr,spinner_interruptor,spinner_interruptor_mtr,spinner_protector,spinner_bomba,spinner_tuberia,spinner_bandeja,spinner_lugar;

    private String image_signature_tecnico="",name_tecnico="",dni_tecnico="",cargo_tecnico="";
    private String image_signature_cliente="",name_cliente="",dni_cliente="",cargo_cliente="";

    //Evaporadora
    private String img_evap_placa="",img_evap_frente="",img_evap_isometrico="",evap_ubicacion = "",txt_nro_serie="";
    private ImageView cam_evap_placa,cam_evap_frente,cam_evap_isometrico;
    private ImageView gallery_evap_placa,gallery_evap_frente,gallery_evap_isometrico,evap_scan;
    EditText et_nro_serie;

    Spinner spinner_evap_ubicacion;

    SearchableSpinner spinner_modelos;
    String[] modelo_ids;
    ArrayAdapter<String> modelo_adapter;

    Spinner spinner_marcas;
    String[] marca_ids;
    ArrayAdapter<String> marca_adapter;

    SearchableSpinner spinner_tipos;
    String[] tipo_ids;
    ArrayAdapter<String> tipo_adapter;

    String marca_evap_id="0",modelo_evap_id="0",tipo_evap_id="0";

    //Condensadora
    private String img_cond_placa="",img_cond_frente="",img_cond_isometrico="",cond_ubicacion="",txt_cond_nro_serie="";
    private ImageView cam_cond_placa,cam_cond_frente,cam_cond_isometrico,cond_scan;
    private ImageView gallery_cond_placa,gallery_cond_frente,gallery_cond_isometrico;
    EditText et_cond_nro_serie;

    Spinner spinner_cond_ubicacion;
    Spinner spinner_refrigerantes;
    String[] refrigerantes_ids;
    ArrayAdapter<String> refrigerante_adapter;

    Spinner spinner_fases;
    String[] fases_ids;
    ArrayAdapter<String> fase_adapter;

    Spinner spinner_voltajes;
    String[] voltajes_ids;
    ArrayAdapter<String> voltaje_adapter;

    Spinner spinner_btus;
    String[] btus_ids;
    ArrayAdapter<String> btu_adapter;

    SearchableSpinner spinner_cond_modelos;
    String[] modelo_cond_ids;
    ArrayAdapter<String> modelo_cond_adapter;

    Spinner spinner_cond_marcas;
    String[] marca_cond_ids;
    ArrayAdapter<String> marca_cond_adapter;

    SearchableSpinner spinner_cond_tipos;
    String[] tipo_cond_ids;
    ArrayAdapter<String> tipo_cond_adapter;

    String marca_cond_id="0",modelo_cond_id="0",tipo_cond_id="0",btu_id="0",refrigerante_id="0",voltaje_id="0",fase_id="0";

    //Cortina
    String et_marca_cortina="",et_modelo_cortina="",et_nro_serie_cortina="";
    EditText marca_cortina,modelo_cortina,nro_serie_cortina;
    ImageView icon_cortina_scan;
    private String img_cortina="";
    private ImageView cam_cortina;
    private ImageView gallery_cortina;

    //Bomba
    String et_marca_bomba="",et_modelo_bomba="",et_nro_serie_bomba="";
    EditText marca_bomba,modelo_bomba,nro_serie_bomba;
    ImageView icon_bomba_scan;
    private String img_bomba="";
    private ImageView cam_bomba;
    private ImageView gallery_bomba;



    /*********** LECTURAS **************/
    private ImageView cam_serpentin,cam_ambiente,cam_presion_baja,cam_presion_alta,cam_amperaje,cam_voltaje;
    private ImageView gallery_serpentin,gallery_ambiente,gallery_presion_baja,gallery_presion_alta,gallery_amperaje,gallery_voltaje;
    String img_temp_serpentin="",img_temp_ambiente="",img_presion_baja="",img_presion_alta="",img_amperaje="",img_voltaje="";
    String txt_temp_serpentin="",txt_temp_ambiente="",txt_presion_baja="",txt_presion_alta="",txt_amperaje="",txt_voltaje="";

    int tipo_nro_serie = 1;
    private static final int  CODIGO_INTENT = 2;

    ViewDialog viewDialog;

    EditText cargo,dni,name;
    int modo_prueba = 0;

    JSONObject datos = new JSONObject();
    String datos_guardados = "";

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ficha_instalacion);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(false);
        getSupportActionBar().setTitle("Ficha Instalación");

        ctx = this;
        cred = new Credentials(ctx);
        viewDialog = new ViewDialog(this);
        id = getIntent().getStringExtra("id");
        datos_guardados = cred.getData("1-"+id);
        System.out.println("datos_guardados: "+datos_guardados);
        if(!datos_guardados.isEmpty()){
            try {
                datos = (JSONObject)new JSONParser().parse(datos_guardados);
            } catch (ParseException e) {
                e.printStackTrace();
                System.out.println("parse: "+e);
            }
        }else{
            datos.put("modulo",1);
            datos.put("ficha_id",id);
        }

        cred.save_data("1-"+id,datos.toString());
        tienda_id = getIntent().getStringExtra("tienda_id");
        btn_evaporadora = findViewById(R.id.btn_evaporadora);
        btn_condensadora = findViewById(R.id.btn_condensadora);
        btn_cortina = findViewById(R.id.btn_cortina);
        btn_bomba = findViewById(R.id.btn_bomba);
        btn_firma_cliente = findViewById(R.id.btn_firma_cliente);
        btn_firma_tecnico = findViewById(R.id.btn_firma_tecnico);
        btn_fotos = findViewById(R.id.btn_fotos);
        btn_lecturas = findViewById(R.id.btn_lecturas);

        btn_registrar = findViewById(R.id.btn_registrar);
        btn_cancelar = findViewById(R.id.btn_cerrar);

        btn_registrar.setOnClickListener(this);
        btn_cancelar.setOnClickListener(this);

        btn_evaporadora.setOnClickListener(this);
        btn_condensadora.setOnClickListener(this);
        btn_cortina.setOnClickListener(this);
        btn_bomba.setOnClickListener(this);

        spinner_lugar = findViewById(R.id.spinner_lugar);
        spinner_linea_baja = findViewById(R.id.spinner_linea_baja);
        spinner_linea_baja_mtr = findViewById(R.id.spinner_linea_baja_mtr);
        spinner_separador = findViewById(R.id.spinner_separador);
        spinner_presostato_baja = findViewById(R.id.spinner_presostato_baja);
        spinner_linea_alta = findViewById(R.id.spinner_linea_alta);
        spinner_linea_alta_mtr = findViewById(R.id.spinner_linea_alta_mtr);
        spinner_secador = findViewById(R.id.spinner_secador);
        spinner_presostato_alta = findViewById(R.id.spinner_presostato_alta);
        spinner_cableado = findViewById(R.id.spinner_cableado);
        spinner_cableado_mtr = findViewById(R.id.spinner_cableado_mtr);
        spinner_interruptor = findViewById(R.id.spinner_interruptor);
        spinner_interruptor_mtr = findViewById(R.id.spinner_interruptor_mtr);
        spinner_protector = findViewById(R.id.spinner_protector);
        spinner_bomba = findViewById(R.id.spinner_bomba);
        spinner_tuberia = findViewById(R.id.spinner_tuberia);
        spinner_bandeja = findViewById(R.id.spinner_bandeja);

        spinner_lugar.setOnItemSelectedListener(this);
        spinner_linea_baja.setOnItemSelectedListener(this);
        spinner_linea_baja_mtr.setOnItemSelectedListener(this);
        spinner_separador.setOnItemSelectedListener(this);
        spinner_presostato_baja.setOnItemSelectedListener(this);
        spinner_linea_alta.setOnItemSelectedListener(this);
        spinner_linea_alta_mtr.setOnItemSelectedListener(this);
        spinner_secador.setOnItemSelectedListener(this);
        spinner_presostato_alta.setOnItemSelectedListener(this);
        spinner_cableado.setOnItemSelectedListener(this);
        spinner_cableado_mtr.setOnItemSelectedListener(this);
        spinner_interruptor.setOnItemSelectedListener(this);
        spinner_interruptor_mtr.setOnItemSelectedListener(this);
        spinner_protector.setOnItemSelectedListener(this);
        spinner_bomba.setOnItemSelectedListener(this);
        spinner_tuberia.setOnItemSelectedListener(this);
        spinner_bandeja.setOnItemSelectedListener(this);

        btn_firma_cliente.setOnClickListener(this);
        btn_firma_tecnico.setOnClickListener(this);
        btn_fotos.setOnClickListener(this);
        btn_lecturas.setOnClickListener(this);
    }

    @Override
    public boolean onSupportNavigateUp() {
        this.finish();
        return true;
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(ctx,InstalacionesActivity.class));
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        switch (parent.getId()){
            case R.id.spinner_lugar:
                Log.e("spinner_lugar",spinner_lugar.getSelectedItem().toString());
                lugar = spinner_lugar.getSelectedItem().toString();
                break;
            case R.id.spinner_linea_baja:
                Log.e("spinner_linea_baja",spinner_linea_baja.getSelectedItem().toString());
                linea_baja = spinner_linea_baja.getSelectedItem().toString();
                break;
            case R.id.spinner_linea_baja_mtr:
                Log.e("spinner_linea_baja_mtr",spinner_linea_baja_mtr.getSelectedItem().toString());
                linea_baja_mtr = spinner_linea_baja_mtr.getSelectedItem().toString();
                break;
            case R.id.spinner_separador:
                Log.e("spinner_separador",spinner_separador.getSelectedItem().toString());
                separador = spinner_separador.getSelectedItem().toString();
                break;
            case R.id.spinner_presostato_baja:
                Log.e("spinner_presostato_baja",spinner_presostato_baja.getSelectedItem().toString());
                presostato_baja = spinner_presostato_baja.getSelectedItem().toString();
                break;
            case R.id.spinner_linea_alta:
                Log.e("spinner_linea_alta",spinner_linea_alta.getSelectedItem().toString());
                linea_alta = spinner_linea_alta.getSelectedItem().toString();
                break;
            case R.id.spinner_linea_alta_mtr:
                Log.e("spinner_linea_alta_mtr",spinner_linea_alta_mtr.getSelectedItem().toString());
                linea_alta_mtr = spinner_linea_alta_mtr.getSelectedItem().toString();
                break;
            case R.id.spinner_secador:
                Log.e("spinner_secador",spinner_secador.getSelectedItem().toString());
                secador = spinner_secador.getSelectedItem().toString();
                break;
            case R.id.spinner_presostato_alta:
                Log.e("spinner_presostato_alta",spinner_presostato_alta.getSelectedItem().toString());
                presostato_alta = spinner_presostato_alta.getSelectedItem().toString();
                break;
            case R.id.spinner_cableado:
                Log.e("spinner_cableado",spinner_cableado.getSelectedItem().toString());
                cableado = spinner_cableado.getSelectedItem().toString();
                break;
            case R.id.spinner_cableado_mtr:
                Log.e("spinner_cableado_mtr",spinner_cableado_mtr.getSelectedItem().toString());
                cableado_mtr = spinner_cableado_mtr.getSelectedItem().toString();
                break;
            case R.id.spinner_interruptor:
                Log.e("spinner_interruptor",spinner_interruptor.getSelectedItem().toString());
                interruptor = spinner_interruptor.getSelectedItem().toString();
                break;
            case R.id.spinner_interruptor_mtr:
                Log.e("spinner_interruptor_mtr",spinner_interruptor_mtr.getSelectedItem().toString());
                interruptor_mtr = spinner_interruptor_mtr.getSelectedItem().toString();
                break;
            case R.id.spinner_protector:
                Log.e("spinner_protector",spinner_protector.getSelectedItem().toString());
                protector = spinner_protector.getSelectedItem().toString();
                break;
            case R.id.spinner_bomba:
                Log.e("spinner_bomba",spinner_bomba.getSelectedItem().toString());
                bomba = spinner_bomba.getSelectedItem().toString();
                break;
            case R.id.spinner_tuberia:
                Log.e("spinner_tuberia",spinner_tuberia.getSelectedItem().toString());
                tuberia = spinner_tuberia.getSelectedItem().toString();
                break;
            case R.id.spinner_bandeja:
                Log.e("spinner_bandeja",spinner_bandeja.getSelectedItem().toString());
                bandeja = spinner_bandeja.getSelectedItem().toString();
                break;
            //Evaporadora
            case R.id.spinner_ubicacion:
                Log.e("spinner_ubicacion", spinner_evap_ubicacion.getSelectedItem().toString());
                evap_ubicacion = spinner_evap_ubicacion.getSelectedItem().toString();
                break;
            case R.id.spinner_marcas:
                marca_evap_id = marca_ids[position];
                break;
            case R.id.spinner_modelo:
                modelo_evap_id = modelo_ids[position];
                break;
            case R.id.spinner_tipos:
                tipo_evap_id = tipo_ids[position];
                break;
            //Condensadora
            case R.id.spinner_cond_ubicacion:
                Log.e("spinner_cond_ubicacion",spinner_cond_ubicacion.getSelectedItem().toString());
                cond_ubicacion = spinner_cond_ubicacion.getSelectedItem().toString();
                break;
            case R.id.spinner_cond_marcas:
                marca_cond_id = marca_cond_ids[position];
                break;
            case R.id.spinner_cond_modelo:
                modelo_cond_id = modelo_cond_ids[position];
                break;
            case R.id.spinner_cond_tipos:
                tipo_cond_id = tipo_cond_ids[position];
                break;
            case R.id.spinner_cond_btus:
                btu_id = btus_ids[position];
                break;
            case R.id.spinner_cond_refrigerantes:
                refrigerante_id = refrigerantes_ids[position];
                break;
            case R.id.spinner_cond_voltajes:
                voltaje_id = voltajes_ids[position];
                break;
            case R.id.spinner_cond_fases:
                fase_id = fases_ids[position];
                break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent)
    {
        switch (parent.getId())
        {
            //Evaporadora
            case R.id.spinner_marcas:
                marca_evap_id = marca_ids[0];
                break;
            case R.id.spinner_modelo:
                modelo_evap_id = modelo_ids[0];
                break;
            case R.id.spinner_tipos:
                tipo_evap_id = tipo_ids[0];
                break;
            //Condensadora
            case R.id.spinner_cond_marcas:
                marca_cond_id = marca_cond_ids[0];
                break;
            case R.id.spinner_cond_modelo:
                modelo_cond_id = modelo_cond_ids[0];
                break;
            case R.id.spinner_cond_tipos:
                tipo_cond_id = tipo_cond_ids[0];
                break;
            case R.id.spinner_cond_btus:
                btu_id = btus_ids[0];
                break;
            case R.id.spinner_cond_refrigerantes:
                refrigerante_id = refrigerantes_ids[0];
                break;
            case R.id.spinner_cond_voltajes:
                voltaje_id = voltajes_ids[0];
                break;
            case R.id.spinner_cond_fases:
                fase_id = fases_ids[0];
                break;
        }
    }

    private void registrarFicha(final String instalacion_id)
    {
        Log.e("instalacion_id", instalacion_id);
        Log.e("tienda_id", tienda_id);

        //Evaporadora
        Log.e("evap_ubicacion",evap_ubicacion);
        Log.e("evap_marca",marca_evap_id);
        Log.e("evap_modelo",modelo_evap_id);
        Log.e("evap_tipo",tipo_evap_id);
        Log.e("evap_nro_serie",txt_nro_serie);

        //Condensadora
        Log.e("cond_ubicacion",cond_ubicacion);
        Log.e("cond_marca",marca_cond_id);
        Log.e("cond_modelo",modelo_cond_id);
        Log.e("cond_tipo",tipo_cond_id);
        Log.e("cond_btu",btu_id);
        Log.e("cond_refrigerante",refrigerante_id);
        Log.e("cond_voltaje",voltaje_id);
        Log.e("cond_fase",fase_id);
        Log.e("cond_nro_serie",txt_cond_nro_serie);

        //Cortina
        Log.e("cortina_marca",et_marca_cortina);
        Log.e("cortina_modelo",et_modelo_cortina);
        Log.e("cortina_nro_serie",et_nro_serie_cortina);

        //Bomba
        Log.e("bomba_marca",et_marca_bomba);
        Log.e("bomba_modelo",et_modelo_bomba);
        Log.e("bomba_nro_serie",et_nro_serie_bomba);

        //Ficha
        Log.e("lugar",lugar);
        Log.e("linea_baja",linea_baja);
        Log.e("linea_baja_mtr",linea_baja_mtr);
        Log.e("separador",separador);
        Log.e("presostato_baja",presostato_baja);
        Log.e("linea_alta",linea_alta);
        Log.e("linea_alta_mtr",linea_alta_mtr);
        Log.e("secador",secador);
        Log.e("presostato_alta",presostato_alta);
        Log.e("cableado",cableado);
        Log.e("cableado_mtr",cableado_mtr);
        Log.e("interruptor",interruptor);
        Log.e("interruptor_mtr",interruptor_mtr);
        Log.e("protector",protector);
        Log.e("bomba",bomba);
        Log.e("tuberia",tuberia);
        Log.e("bandeja",bandeja);

        //Fotos
        Log.e("img_evap_placa",img_evap_placa);
        Log.e("img_evap_frente",img_evap_frente);
        Log.e("img_evap_isometrico",img_evap_isometrico);
        Log.e("img_cond_placa",img_cond_placa);
        Log.e("img_cond_frente",img_cond_frente);
        Log.e("img_cond_isometrico",img_cond_isometrico);
        Log.e("img_cortina",img_cortina);
        Log.e("img_bomba",img_bomba);

        //Lecturas
        Log.e("img_temp_serpentin",img_temp_serpentin);
        Log.e("img_temp_ambiente",img_temp_ambiente);
        Log.e("img_presion_baja",img_presion_baja);
        Log.e("img_presion_alta",img_presion_alta);
        Log.e("img_amperaje",img_amperaje);
        Log.e("img_voltaje",img_voltaje);
        Log.e("txt_temp_serpentin",txt_temp_serpentin);
        Log.e("txt_temp_ambiente",txt_temp_ambiente);
        Log.e("txt_presion_baja",txt_presion_baja);
        Log.e("txt_presion_alta",txt_presion_alta);
        Log.e("txt_amperaje",txt_amperaje);
        Log.e("txt_voltaje",txt_voltaje);

        //Firma Cliente
        Log.e("firma_cliente",image_signature_cliente);
        Log.e("nombre_cliente",name_cliente);
        Log.e("dni_cliente",dni_cliente);
        Log.e("cargo_cliente",cargo_cliente);

        //Firma Técnico
        Log.e("firma_tecnico",image_signature_tecnico);
        Log.e("nombre_tecnico",name_tecnico);
        Log.e("dni_tecnico",dni_tecnico);
        Log.e("cargo_tecnico",cargo_tecnico);

        if(1==1){
            viewDialog.showDialog();
            String url=ctx.getApplicationContext().getString(R.string.base_url)+ctx.getApplicationContext().getString(R.string.register_ficha_instalacion_url);
            RequestQueue queue = Volley.newRequestQueue(ctx);

            // Request a string response from the provided URL.
            StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            System.out.println("register_ficha_instalacion_response: " + response);
                            try {
                                RespuestaResponse cliente = new Gson().fromJson(response, RespuestaResponse.class);
                                JSONParser parser = new JSONParser();
                                JSONArray respuesta = (JSONArray) parser.parse((String) cliente.getRespuesta());

                                if (cliente.getIde_error() == 0) {
                                    viewDialog.hideDialog(1);
                                    Toast.makeText(ctx, cliente.getDes_error(), Toast.LENGTH_LONG).show();
                                } else {
                                    Toast.makeText(ctx,"Registro correcto",Toast.LENGTH_SHORT).show();
                                    createFicha(instalacion_id);
                                }
                            } catch (Exception e) {
                                viewDialog.hideDialog(1);
                                e.printStackTrace();
                                System.out.println("register_ficha_instalacion_error1: " + e);
                            }
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    viewDialog.hideDialog(1);
                    error.printStackTrace();
                    System.out.println("register_ficha_instalacion_error2: " + error);
                }
            }){
                @Override
                protected Map<String, String> getParams()
                {
                    Map<String, String> params = new HashMap<>();
                    params.put("instalacion_id", instalacion_id);
                    params.put("tienda_id", tienda_id);
                    //Evaporadora
                    params.put("evap_ubicacion",evap_ubicacion);
                    params.put("evap_marca",marca_evap_id);
                    params.put("evap_modelo",modelo_evap_id);
                    params.put("evap_tipo",tipo_evap_id);
                    params.put("evap_nro_serie",txt_nro_serie);

                    //Condensadora
                    params.put("cond_ubicacion",cond_ubicacion);
                    params.put("cond_marca",marca_cond_id);
                    params.put("cond_modelo",modelo_cond_id);
                    params.put("cond_tipo",tipo_cond_id);
                    params.put("cond_btu",btu_id);
                    params.put("cond_refrigerante",refrigerante_id);
                    params.put("cond_voltaje",voltaje_id);
                    params.put("cond_fase",fase_id);
                    params.put("cond_nro_serie",txt_cond_nro_serie);

                    //Cortina
                    params.put("cortina_marca",et_marca_cortina);
                    params.put("cortina_modelo",et_modelo_cortina);
                    params.put("cortina_nro_serie",et_nro_serie_cortina);

                    //Bomba
                    params.put("bomba_marca",et_marca_bomba);
                    params.put("bomba_modelo",et_modelo_bomba);
                    params.put("bomba_nro_serie",et_nro_serie_bomba);

                    //Ficha
                    params.put("lugar",lugar);
                    params.put("linea_baja",linea_baja);
                    params.put("linea_baja_mtr",linea_baja_mtr);
                    params.put("separador",separador);
                    params.put("presostato_baja",presostato_baja);
                    params.put("linea_alta",linea_alta);
                    params.put("linea_alta_mtr",linea_alta_mtr);
                    params.put("secador",secador);
                    params.put("presostato_alta",presostato_alta);
                    params.put("cableado",cableado);
                    params.put("cableado_mtr",cableado_mtr);
                    params.put("interruptor",interruptor);
                    params.put("interruptor_mtr",interruptor_mtr);
                    params.put("protector",protector);
                    params.put("bomba",bomba);
                    params.put("tuberia",tuberia);
                    params.put("bandeja",bandeja);

                    //Fotos
                    params.put("img_evap_placa",img_evap_placa);
                    params.put("img_evap_frente",img_evap_frente);
                    params.put("img_evap_isometrico",img_evap_isometrico);
                    params.put("img_cond_placa",img_cond_placa);
                    params.put("img_cond_frente",img_cond_frente);
                    params.put("img_cond_isometrico",img_cond_isometrico);
                    params.put("img_cortina",img_cortina);
                    params.put("img_bomba",img_bomba);

                    //Lecturas
                    params.put("img_temp_serpentin",img_temp_serpentin);
                    params.put("img_temp_ambiente",img_temp_ambiente);
                    params.put("img_presion_baja",img_presion_baja);
                    params.put("img_presion_alta",img_presion_alta);
                    params.put("img_amperaje",img_amperaje);
                    params.put("img_voltaje",img_voltaje);
                    params.put("txt_temp_serpentin",txt_temp_serpentin);
                    params.put("txt_temp_ambiente",txt_temp_ambiente);
                    params.put("txt_presion_baja",txt_presion_baja);
                    params.put("txt_presion_alta",txt_presion_alta);
                    params.put("txt_amperaje",txt_amperaje);
                    params.put("txt_voltaje",txt_voltaje);


                    //Firma Cliente
                    params.put("firma_cliente",image_signature_cliente);
                    params.put("nombre_cliente",name_cliente);
                    params.put("dni_cliente",dni_cliente);
                    params.put("cargo_cliente",cargo_cliente);

                    //Firma Técnico
                    params.put("firma_tecnico",image_signature_tecnico);
                    params.put("nombre_tecnico",name_tecnico);
                    params.put("dni_tecnico",dni_tecnico);
                    params.put("cargo_tecnico",cargo_tecnico);

                    return params;
                }
            };
            stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                    300000,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

            // Add the request to the RequestQueue.
            queue.add(stringRequest);
        }
    }

    private void createFicha(final String instalacion_id)
    {
        String url=ctx.getApplicationContext().getString(R.string.base_url)+ctx.getApplicationContext().getString(R.string.crear_ficha_instalacion_url);
        RequestQueue queue = Volley.newRequestQueue(ctx);

        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        System.out.println("create_ficha_instalacion_response: " + response);
                        try {
                            RespuestaResponse cliente = new Gson().fromJson(response, RespuestaResponse.class);

                            if (cliente.getIde_error() == 0) {
                                viewDialog.hideDialog(1);
                                Toast.makeText(ctx, cliente.getDes_error(), Toast.LENGTH_LONG).show();
                            } else {
                                Toast.makeText(ctx,"Ficha creada correctamente",Toast.LENGTH_SHORT).show();
                                viewDialog.hideDialog(1);
                                new Handler().postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        new Utils().sendFichaUrgencia(tienda_id,instalacion_id,ctx);
                                        ((FichaInstalacionActivity)ctx).finish();
                                    }
                                }, 1000);
                            }
                        } catch (Exception e) {
                            viewDialog.hideDialog(1);
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                viewDialog.hideDialog(1);
                System.out.println("create_ficha_instalacion_error: " + error);
            }
        }){
            @Override
            protected Map<String, String> getParams()
            {
                Map<String, String> params = new HashMap<>();
                params.put("instalacion_id", instalacion_id);
                return params;
            }
        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                300000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        // Add the request to the RequestQueue.
        queue.add(stringRequest);
    }

    void actionTakePhoto(Intent in)
    {
        if (in.resolveActivity(getPackageManager()) != null) {

            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File
                Toast.makeText(ctx,"Ocurrió  un error al guardar la foto",Toast.LENGTH_SHORT).show();
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(ctx,
                        "com.system.operaciones.fileprovider",
                        photoFile);
                in.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(in, 1);
            }
        }
    }
    String currentPhotoPath;

    private File createImageFile() throws IOException {
        // Create an image file name
        currentPhotoPath = "";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                tienda_id+"-"+cred.getData("image_type")+"_",  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        currentPhotoPath = image.getAbsolutePath();
        return image;
    }

    private void galleryAddPic() {
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        File f = new File(currentPhotoPath);
        System.out.println("currentPhotoPath: "+currentPhotoPath);
        Uri contentUri = Uri.fromFile(f);
        System.out.println("contentUri: "+contentUri);
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = 8;
        try {
            String image_type = cred.getData("image_type");
            String b64 = Image.convert(BitmapFactory.decodeFile(currentPhotoPath,options));
            switch (image_type){
                case "1":
                    img_evap_placa = b64;
                    Log.e("img_evap_placa",image_type);
                    cam_evap_placa.setVisibility(View.GONE);
                    gallery_evap_placa.setVisibility(View.VISIBLE);
                    datos.put("img_evap_placa",img_evap_placa);
                    break;
                case "2":
                    img_evap_frente = b64;
                    Log.e("img_evap_frente",image_type);
                    cam_evap_frente.setVisibility(View.GONE);
                    gallery_evap_frente.setVisibility(View.VISIBLE);
                    datos.put("img_evap_frente",img_evap_frente);
                    break;
                case "3":
                    img_evap_isometrico = b64;
                    Log.e("img_evap_isometrico",image_type);
                    cam_evap_isometrico.setVisibility(View.GONE);
                    gallery_evap_isometrico.setVisibility(View.VISIBLE);
                    datos.put("img_evap_isometrico",img_evap_isometrico);
                    break;
                //Condensadora
                case "4":
                    img_cond_placa = b64;
                    Log.e("img_cond_placa",image_type);
                    cam_cond_placa.setVisibility(View.GONE);
                    gallery_cond_placa.setVisibility(View.VISIBLE);
                    datos.put("img_cond_placa",img_cond_placa);
                    break;
                case "5":
                    img_cond_frente = b64;
                    Log.e("img_cond_frente",image_type);
                    cam_cond_frente.setVisibility(View.GONE);
                    gallery_cond_frente.setVisibility(View.VISIBLE);
                    datos.put("img_cond_frente",img_cond_frente);
                    break;
                case "6":
                    img_cond_isometrico = b64;
                    Log.e("img_cond_isometrico",image_type);
                    cam_cond_isometrico.setVisibility(View.GONE);
                    gallery_cond_isometrico.setVisibility(View.VISIBLE);
                    datos.put("img_cond_isometrico",img_cond_isometrico);
                    break;
                case "7":
                    img_cortina = b64;
                    Log.e("img_cortina",image_type);
                    cam_cortina.setVisibility(View.GONE);
                    gallery_cortina.setVisibility(View.VISIBLE);
                    datos.put("img_cortina",img_cortina);
                    break;
                case "8":
                    img_bomba = b64;
                    Log.e("img_bomba",image_type);
                    cam_bomba.setVisibility(View.GONE);
                    gallery_bomba.setVisibility(View.VISIBLE);
                    datos.put("img_cortina",img_bomba);
                    break;
                case "9":
                    img_temp_serpentin = b64;
                    Log.e("img_temp_serpentin",image_type);
                    cam_serpentin.setVisibility(View.GONE);
                    gallery_serpentin.setVisibility(View.VISIBLE);
                    datos.put("img_temp_serpentin",img_cond_isometrico);
                    break;
                case "10":
                    img_temp_ambiente = b64;
                    Log.e("img_temp_ambiente",image_type);
                    cam_ambiente.setVisibility(View.GONE);
                    gallery_ambiente.setVisibility(View.VISIBLE);
                    datos.put("img_temp_ambiente",img_temp_ambiente);
                    break;
                case "11":
                    img_presion_baja = b64;
                    Log.e("img_presion_baja",image_type);
                    cam_presion_baja.setVisibility(View.GONE);
                    gallery_presion_baja.setVisibility(View.VISIBLE);
                    datos.put("img_presion_baja",img_presion_baja);
                    break;
                case "12":
                    img_presion_alta = b64;
                    Log.e("img_presion_alta",image_type);
                    cam_presion_alta.setVisibility(View.GONE);
                    gallery_presion_alta.setVisibility(View.VISIBLE);
                    datos.put("img_presion_alta",img_presion_alta);
                    break;
                case "13":
                    img_amperaje = b64;
                    Log.e("img_amperaje",image_type);
                    cam_amperaje.setVisibility(View.GONE);
                    gallery_amperaje.setVisibility(View.VISIBLE);
                    datos.put("img_amperaje",img_amperaje);
                    break;
                case "14":
                    img_voltaje = b64;
                    Log.e("img_voltaje",image_type);
                    cam_voltaje.setVisibility(View.GONE);
                    gallery_voltaje.setVisibility(View.VISIBLE);
                    datos.put("img_voltaje",img_voltaje);
                    break;
            }
            cred.save_data("1-"+id,datos.toString());
        } catch (Exception e) {
            viewDialog.hideDialog(1);
            e.printStackTrace();
        }
        mediaScanIntent.setData(contentUri);
        this.sendBroadcast(mediaScanIntent);
    }

    void openChooser()
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(ctx);
        LayoutInflater inflater = getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.dialog_chooser_image, null);
        builder.setView(dialogView);
        builder.setCancelable(false);
        builder.setPositiveButton("Cerrar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        TextView take_photo = dialogView.findViewById(R.id.take_photo);
        TextView choose_photo = dialogView.findViewById(R.id.choose_photo);
        final AlertDialog alertDialogChooser = builder.create();
        take_photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialogChooser.dismiss();
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                actionTakePhoto(intent);
            }
        });
        choose_photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialogChooser.dismiss();
                pickFromGallery();
            }
        });
        alertDialogChooser.show();
    }

    private void pickFromGallery()
    {
        //Create an Intent with action as ACTION_PICK
        Intent intent=new Intent(Intent.ACTION_PICK);
        // Sets the type as image/*. This ensures only components of type image are selected
        intent.setType("image/*");
        //We pass an extra array with the accepted mime types. This will ensure only components with these MIME types as targeted.
        String[] mimeTypes = {"image/jpeg", "image/png"};
        intent.putExtra(Intent.EXTRA_MIME_TYPES,mimeTypes);
        // Launching the Intent
        startActivityForResult(intent,2);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case 1://take photo
                if(cred.getData("image_type").equals("0")){
                    String codigo = data.getStringExtra("codigo");
                    if(tipo_nro_serie==1) {
                        et_nro_serie.setText(codigo);
                        datos.put("et_nro_serie",codigo);
                        cred.save_data("1-"+id,datos.toString());
                    }
                    else if(tipo_nro_serie==2){
                        et_cond_nro_serie.setText(codigo);
                        datos.put("et_cond_nro_serie",codigo);
                        cred.save_data("1-"+id,datos.toString());
                        }
                    else if(tipo_nro_serie==3){
                        nro_serie_cortina.setText(codigo);
                        datos.put("nro_serie_cortina",codigo);
                        cred.save_data("1-"+id,datos.toString());
                    }else{
                        nro_serie_bomba.setText(codigo);
                        datos.put("nro_serie_bomba",codigo);
                        cred.save_data("1-"+id,datos.toString());
                    }
                }else{
                    galleryAddPic();
                }
                break;
            case 2://choose from gallery
                //data.getData return the content URI for the selected Image
                if(data!=null){
                    Uri selectedImage = data.getData();
                    String[] filePathColumn = { MediaStore.Images.Media.DATA };
                    // Get the cursor
                    Cursor cursor = getContentResolver().query(selectedImage, filePathColumn, null, null, null);
                    // Move to first row
                    cursor.moveToFirst();
                    //Get the column index of MediaStore.Images.Media.DATA
                    int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                    //Gets the String value in the column
                    String imgDecodableString = cursor.getString(columnIndex);
                    cursor.close();
                    BitmapFactory.Options options = new BitmapFactory.Options();
                    options.inSampleSize = 2;
                    Bitmap bitmap = BitmapFactory.decodeFile(imgDecodableString,options);
                    try {
                        String image_type = cred.getData("image_type");
                        switch (image_type){
                            case "1":
                                img_evap_placa = Image.convert(bitmap);
                                Log.e("img_evap_placa",image_type);
                                cam_evap_placa.setVisibility(View.GONE);
                                gallery_evap_placa.setVisibility(View.VISIBLE);
                                datos.put("img_evap_placa",img_evap_placa);
                                break;
                            case "2":
                                img_evap_frente = Image.convert(bitmap);
                                Log.e("img_evap_frente",image_type);
                                cam_evap_frente.setVisibility(View.GONE);
                                gallery_evap_frente.setVisibility(View.VISIBLE);
                                datos.put("img_evap_frente",img_evap_frente);
                                break;
                            case "3":
                                img_evap_isometrico = Image.convert(bitmap);
                                Log.e("img_evap_isometrico",image_type);
                                cam_evap_isometrico.setVisibility(View.GONE);
                                gallery_evap_isometrico.setVisibility(View.VISIBLE);
                                datos.put("img_evap_isometrico",img_evap_isometrico);
                                break;
                            //Condensadora
                            case "4":
                                img_cond_placa = Image.convert(bitmap);
                                Log.e("img_cond_placa",image_type);
                                cam_cond_placa.setVisibility(View.GONE);
                                gallery_cond_placa.setVisibility(View.VISIBLE);
                                datos.put("img_cond_placa",img_cond_placa);
                                break;
                            case "5":
                                img_cond_frente = Image.convert(bitmap);
                                Log.e("img_cond_frente",image_type);
                                cam_cond_frente.setVisibility(View.GONE);
                                gallery_cond_frente.setVisibility(View.VISIBLE);
                                datos.put("img_cond_frente",img_cond_frente);
                                break;
                            case "6":
                                img_cond_isometrico = Image.convert(bitmap);
                                Log.e("img_cond_isometrico",image_type);
                                cam_cond_isometrico.setVisibility(View.GONE);
                                gallery_cond_isometrico.setVisibility(View.VISIBLE);
                                datos.put("img_cond_isometrico",img_cond_isometrico);
                                break;
                            //OTROS
                            case "7":
                                img_cortina = Image.convert(bitmap);
                                Log.e("img_cortina",image_type);
                                cam_cortina.setVisibility(View.GONE);
                                gallery_cortina.setVisibility(View.VISIBLE);
                                datos.put("img_cortina",img_cortina);
                                break;
                            case "8":
                                img_bomba = Image.convert(bitmap);
                                Log.e("img_bomba",image_type);
                                cam_bomba.setVisibility(View.GONE);
                                gallery_bomba.setVisibility(View.VISIBLE);
                                datos.put("img_bomba",img_bomba);
                                break;
                            //LECTURAS
                            case "9":
                                img_temp_serpentin = Image.convert(bitmap);
                                Log.e("img_temp_serpentin",image_type);
                                cam_serpentin.setVisibility(View.GONE);
                                gallery_serpentin.setVisibility(View.VISIBLE);
                                datos.put("img_temp_serpentin",img_temp_serpentin);
                                break;
                            case "10":
                                img_temp_ambiente = Image.convert(bitmap);
                                Log.e("img_temp_ambiente",image_type);
                                cam_ambiente.setVisibility(View.GONE);
                                gallery_ambiente.setVisibility(View.VISIBLE);
                                datos.put("img_temp_ambiente",img_temp_ambiente);
                                break;
                            case "11":
                                img_presion_baja = Image.convert(bitmap);
                                Log.e("img_temp_ambiente",image_type);
                                cam_presion_baja.setVisibility(View.GONE);
                                gallery_presion_baja.setVisibility(View.VISIBLE);
                                datos.put("img_presion_baja",img_presion_baja);
                                break;
                            case "12":
                                img_presion_alta = Image.convert(bitmap);
                                Log.e("img_presion_alta",image_type);
                                cam_presion_alta.setVisibility(View.GONE);
                                gallery_presion_alta.setVisibility(View.VISIBLE);
                                datos.put("img_presion_alta",img_presion_alta);
                                break;
                            case "13":
                                img_amperaje = Image.convert(bitmap);
                                Log.e("img_amperaje",image_type);
                                cam_amperaje.setVisibility(View.GONE);
                                gallery_amperaje.setVisibility(View.VISIBLE);
                                datos.put("img_amperaje",img_amperaje);
                                break;
                            case "14":
                                img_voltaje = Image.convert(bitmap);
                                Log.e("img_voltaje",image_type);
                                cam_voltaje.setVisibility(View.GONE);
                                gallery_voltaje.setVisibility(View.VISIBLE);
                                datos.put("img_voltaje",img_voltaje);
                                break;
                        }
                        cred.save_data("1-"+id,datos.toString());
                    } catch (Exception e) {
                        viewDialog.hideDialog(1);
                        e.printStackTrace();
                    }
                }
                break;
        }
    }

    void setAllValues()
    {

    }

    void setAllValuesDespues()
    {

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.floating_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item)
    {
        switch (item.getItemId()){
            case R.id.navInstalaciones:
                cred.save_data("key_act","1");
                break;
            case R.id.navMantenimientos:
                cred.save_data("key_act","2");
                break;
            case R.id.navUrgencias:
                cred.save_data("key_act","3");
                break;
        }
        this.finish();
        startActivity(new Intent(ctx, TiendasActivity.class));
        return true;
    }

    //OnClickEvent
    @Override
    public void onClick(View v)
    {
        switch (v.getId()){
            case R.id.btn_evaporadora:
                showModalEvaporadora();
                break;
            case R.id.btn_condensadora:
                showModalCondensadora();
                break;
            case R.id.btn_firma_tecnico:
                showModalFirmaTecnico();
                break;
            case R.id.btn_firma_cliente:
                showModalFirmaCliente();
                break;
            case R.id.btn_cortina:
                showModalCortina();
                break;
            case R.id.btn_bomba:
                showModalBomba();
                break;
            case R.id.btn_fotos:
                showModalFotos();
                break;
            case R.id.btn_lecturas:
                showModalLecturas();
                break;
            case R.id.btn_registrar:
                registrarFicha(id);
                break;
            case R.id.btn_cancelar:
                startActivity(new Intent(this,InstalacionesActivity.class));
                break;
            //Evaporadora
            case R.id.icon_cam_evap_placa:
                openChooser();
                cred.save_data("image_type","1");
                break;
            case R.id.icon_gallery_evap_placa:
                showPreview(img_evap_placa,1);
                break;
            case R.id.icon_cam_evap_frente:
                openChooser();
                cred.save_data("image_type","2");
                break;
            case R.id.icon_gallery_evap_frente:
                showPreview(img_evap_frente,2);
                break;
            case R.id.icon_cam_evap_isometrico:
                openChooser();
                cred.save_data("image_type","3");
                break;
            case R.id.icon_gallery_evap_isometrico:
                showPreview(img_evap_isometrico,3);
                break;
            //Condensadora
            case R.id.icon_cam_cond_placa:
                openChooser();
                cred.save_data("image_type","4");
                break;
            case R.id.icon_gallery_cond_placa:
                showPreview(img_cond_placa,4);
                break;
            case R.id.icon_cam_cond_frente:
                openChooser();
                cred.save_data("image_type","5");
                break;
            case R.id.icon_gallery_cond_frente:
                showPreview(img_cond_frente,5);
                break;
            case R.id.icon_cam_cond_isometrico:
                openChooser();
                cred.save_data("image_type","6");
                break;
            case R.id.icon_gallery_cond_isometrico:
                showPreview(img_cond_isometrico,6);
                break;
            case R.id.icon_cam_cortina:
                openChooser();
                cred.save_data("image_type","7");
                break;
            case R.id.icon_gallery_cortina:
                showPreview(img_cortina,7);
                break;
            case R.id.icon_cam_bomba:
                openChooser();
                cred.save_data("image_type","8");
                break;
            case R.id.icon_gallery_bomba:
                showPreview(img_bomba,8);
                break;
            //Lecturas
            case R.id.icon_cam_temp_serpentin:
                openChooser();
                cred.save_data("image_type","9");
                break;
            case R.id.icon_gallery_temp_serpentin:
                showPreview(img_temp_serpentin,9);
                break;
            case R.id.icon_cam_temp_ambiente:
                openChooser();
                cred.save_data("image_type","10");
                break;
            case R.id.icon_gallery_temp_ambiente:
                showPreview(img_temp_ambiente,10);
                break;
            case R.id.icon_cam_presion_baja:
                openChooser();
                cred.save_data("image_type","11");
                break;
            case R.id.icon_gallery_presion_baja:
                showPreview(img_temp_ambiente,11);
                break;
            case R.id.icon_cam_presion_alta:
                openChooser();
                cred.save_data("image_type","12");
                break;
            case R.id.icon_gallery_presion_alta:
                showPreview(img_temp_ambiente,12);
                break;
            case R.id.icon_cam_amperaje:
                openChooser();
                cred.save_data("image_type","13");
                break;
            case R.id.icon_gallery_amperaje:
                showPreview(img_temp_ambiente,13);
                break;
            case R.id.icon_cam_voltaje:
                openChooser();
                cred.save_data("image_type","14");
                break;
            case R.id.icon_gallery_voltaje:
                showPreview(img_temp_ambiente,14);
                break;
            case R.id.icon_evap_scan:
                cred.save_data("image_type","0");
                tipo_nro_serie=1;
                escanear();
                break;
            case R.id.icon_cond_scan:
                cred.save_data("image_type","0");
                tipo_nro_serie=2;
                escanear();
                break;
            case R.id.icon_cortina_scan:
                cred.save_data("image_type","0");
                tipo_nro_serie=3;
                escanear();
                break;
            case R.id.icon_bomba_scan:
                cred.save_data("image_type","0");
                tipo_nro_serie=4;
                escanear();
                break;
        }
    }

    void escanear() {
        Intent i = new Intent(ctx, LectorActivity.class);
        startActivityForResult(i, 1);
    }

    void showPreview(String b64,final int element)
    {
        System.out.println("preview_url: "+b64);
        androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(ctx);
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_preview_photo, null);
        ImageView preview = dialogView.findViewById(R.id.dialog_preview_photo);
        preview.setImageBitmap(Image.convert(b64));
        preview.setMaxHeight(100);
        builder.setView(dialogView);
        builder.setCancelable(false);
        builder.setNeutralButton("Cerrar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        if(element!=0){
            builder.setPositiveButton("Eliminar", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    cleanPhoto(element); }
            });
        }

        androidx.appcompat.app.AlertDialog alert_preview = builder.create();
        alert_preview.show();
    }

    void cleanPhoto(int element)
    {
        switch (element){
            case 1:
                img_evap_placa="";
                datos.put("img_evap_placa",img_evap_placa);
                gallery_evap_placa.setVisibility(View.GONE);
                cam_evap_placa.setVisibility(View.VISIBLE);
            break;
            case 2:
                img_evap_frente="";
                datos.put("img_evap_frente",img_evap_frente);
                gallery_evap_frente.setVisibility(View.GONE);
                cam_evap_frente.setVisibility(View.VISIBLE);
                break;
            case 3:
                img_evap_isometrico="";
                datos.put("img_evap_isometrico",img_evap_isometrico);
                gallery_evap_isometrico.setVisibility(View.GONE);
                cam_evap_isometrico.setVisibility(View.VISIBLE);
                break;
            case 4:
                img_cond_placa="";
                datos.put("img_cond_placa",img_cond_placa);
                gallery_cond_placa.setVisibility(View.GONE);
                cam_cond_placa.setVisibility(View.VISIBLE);
                break;
            case 5:
                img_cond_frente="";
                datos.put("img_cond_frente",img_cond_frente);
                gallery_cond_frente.setVisibility(View.GONE);
                cam_cond_frente.setVisibility(View.VISIBLE);
                break;
            case 6:
                img_cond_isometrico="";
                datos.put("img_cond_isometrico",img_cond_isometrico);
                gallery_cond_isometrico.setVisibility(View.GONE);
                cam_cond_isometrico.setVisibility(View.VISIBLE);
                break;
            case 7:
                img_cortina="";
                datos.put("img_cortina",img_cortina);
                gallery_cortina.setVisibility(View.GONE);
                cam_cortina.setVisibility(View.VISIBLE);
                break;
            case 8:
                img_bomba="";
                datos.put("img_bomba",img_bomba);
                gallery_bomba.setVisibility(View.GONE);
                cam_bomba.setVisibility(View.VISIBLE);
                break;
            case 9:
                img_temp_serpentin="";
                datos.put("img_temp_serpentin",img_temp_serpentin);
                gallery_serpentin.setVisibility(View.GONE);
                cam_serpentin.setVisibility(View.VISIBLE);
                break;
            case 10:
                img_temp_ambiente="";
                datos.put("img_temp_ambiente",img_temp_ambiente);
                gallery_ambiente.setVisibility(View.GONE);
                cam_ambiente.setVisibility(View.VISIBLE);
                break;
            case 11:
                img_presion_baja="";
                datos.put("img_presion_baja",img_presion_baja);
                gallery_presion_baja.setVisibility(View.GONE);
                cam_presion_baja.setVisibility(View.VISIBLE);
                break;
            case 12:
                img_presion_alta="";
                datos.put("img_presion_alta",img_presion_alta);
                gallery_presion_alta.setVisibility(View.GONE);
                cam_presion_alta.setVisibility(View.VISIBLE);
                break;
            case 13:
                img_amperaje="";
                datos.put("img_amperaje",img_amperaje);
                gallery_amperaje.setVisibility(View.GONE);
                cam_amperaje.setVisibility(View.VISIBLE);
                break;
            case 14:
                img_voltaje="";
                datos.put("img_voltaje",img_voltaje);
                gallery_voltaje.setVisibility(View.GONE);
                cam_voltaje.setVisibility(View.VISIBLE);
                break;

        }
        cred.save_data("1-"+id,datos.toString());
    }

    void showModalEvaporadora()
    {
        final AlertDialog.Builder builder = new AlertDialog.Builder(ctx);
        // retrieve display dimensions
        Rect displayRectangle = new Rect();
        Window window = getWindow();
        window.getDecorView().getWindowVisibleDisplayFrame(displayRectangle);
        // inflate and adjust layout
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View dialogView = inflater.inflate(R.layout.dialog_evaporadora, null);
        builder.setView(dialogView);
        builder.setNegativeButton("Guardar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                txt_nro_serie = et_nro_serie.getText().toString();
                datos.put("et_nro_serie",txt_nro_serie);
                cred.save_data("1-"+id,datos.toString());
                Toast.makeText(ctx,"Guardado",Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }
        });
        builder.setNeutralButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        spinner_evap_ubicacion = dialogView.findViewById(R.id.spinner_ubicacion);
        spinner_modelos = dialogView.findViewById(R.id.spinner_modelo);
        spinner_tipos = dialogView.findViewById(R.id.spinner_tipos);
        spinner_marcas = dialogView.findViewById(R.id.spinner_marcas);
        evap_scan = dialogView.findViewById(R.id.icon_evap_scan);
        et_nro_serie = dialogView.findViewById(R.id.evap_nro_serie);
        evap_scan.setOnClickListener(this);
        spinner_evap_ubicacion.setOnItemSelectedListener(this);
        spinner_modelos.setOnItemSelectedListener(this);
        spinner_tipos.setOnItemSelectedListener(this);
        spinner_marcas.setOnItemSelectedListener(this);
        spinner_evap_ubicacion.setOnItemSelectedListener(this);
        if(datos_guardados!=null)
        {
            if(datos.get("et_nro_serie")!=null)
            {
                et_nro_serie.setText((String)datos.get("et_nro_serie"));
            }
        }
        getModelos();
        getTipos();
        getMarcas();
        builder.setCancelable(false);
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    void showModalCondensadora()
    {
        final AlertDialog.Builder builder = new android.app.AlertDialog.Builder(ctx);
        // retrieve display dimensions
        Rect displayRectangle = new Rect();
        Window window = getWindow();
        window.getDecorView().getWindowVisibleDisplayFrame(displayRectangle);
        // inflate and adjust layout
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View dialogView = inflater.inflate(R.layout.dialog_condensadora, null);
        builder.setView(dialogView);
        builder.setCancelable(false);
        builder.setNegativeButton("Guardar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                txt_cond_nro_serie = et_cond_nro_serie.getText().toString();
                datos.put("et_cond_nro_serie",txt_cond_nro_serie);
                cred.save_data("1-"+id,datos.toString());
                Toast.makeText(ctx,"Guardado",Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }
        });
        builder.setNeutralButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        et_cond_nro_serie = dialogView.findViewById(R.id.cond_nro_serie);
        spinner_cond_ubicacion = dialogView.findViewById(R.id.spinner_cond_ubicacion);
        spinner_refrigerantes = dialogView.findViewById(R.id.spinner_cond_refrigerantes);
        spinner_cond_modelos = dialogView.findViewById(R.id.spinner_cond_modelo);
        spinner_btus = dialogView.findViewById(R.id.spinner_cond_btus);
        spinner_cond_marcas = dialogView.findViewById(R.id.spinner_cond_marcas);
        spinner_cond_tipos = dialogView.findViewById(R.id.spinner_cond_tipos);
        spinner_voltajes = dialogView.findViewById(R.id.spinner_cond_voltajes);
        spinner_fases = dialogView.findViewById(R.id.spinner_cond_fases);
        cond_scan = dialogView.findViewById(R.id.icon_cond_scan);
        cond_scan.setOnClickListener(this);

        spinner_cond_ubicacion.setOnItemSelectedListener(this);
        spinner_refrigerantes.setOnItemSelectedListener(this);
        spinner_cond_modelos.setOnItemSelectedListener(this);
        spinner_btus.setOnItemSelectedListener(this);
        spinner_cond_marcas.setOnItemSelectedListener(this);
        spinner_cond_tipos.setOnItemSelectedListener(this);
        spinner_voltajes.setOnItemSelectedListener(this);
        spinner_fases.setOnItemSelectedListener(this);

        if(datos_guardados!=null)
        {
            if(datos.get("et_cond_nro_serie")!=null)
            {
                et_cond_nro_serie.setText((String)datos.get("et_cond_nro_serie"));
            }
        }
        getRefrigerantes();
        getCondensadoraModelos();
        getCondensadoraTipos();
        getVoltajes();
        getMarcas();
        getBtus();
        getMarcas();
        getVoltajes();
        getFases();

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    void showModalFirmaCliente()
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(ctx);
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_ficha_registro_atencion, null);
        builder.setView(dialogView);
        builder.setCancelable(false);
        Button guardar = dialogView.findViewById(R.id.dialog_signature_btn_guardar);
        Button cancelar = dialogView.findViewById(R.id.dialog_signature_btn_cancelar);
        Button limpiar = dialogView.findViewById(R.id.dialog_signature_btn_limpiar);
        name = dialogView.findViewById(R.id.dialog_signature_et_name);
        dni = dialogView.findViewById(R.id.dialog_signature_et_dni);
        cargo = dialogView.findViewById(R.id.dialog_signature_et_cargo);
        final SignaturePad signaturePad = dialogView.findViewById(R.id.dialog_signature);
        if(!datos_guardados.isEmpty()){
            if(datos.get("name_cliente")!=null){
                name_cliente = (String)datos.get("name_cliente");
            }
            if(datos.get("dni_cliente")!=null){
                dni_cliente = (String)datos.get("dni_cliente");
            }
            if(datos.get("cargo_cliente")!=null){
                cargo_cliente = (String)datos.get("cargo_cliente");
            }
            if(datos.get("image_signature_cliente")!=null){
                image_signature_cliente = (String)datos.get("image_signature_cliente");
            }
        }
        final AlertDialog alertDialog = builder.create();
        alertDialog.show();
        limpiar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signaturePad.clear();
            }
        });
        signaturePad.setOnSignedListener(new SignaturePad.OnSignedListener() {
            @Override
            public void onStartSigning() {
            }

            @Override
            public void onSigned() {
                image_signature_cliente = Image.convert(signaturePad.getSignatureBitmap());
            }

            @Override
            public void onClear() {
                image_signature_cliente="";
            }
        });
        guardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(image_signature_cliente.isEmpty()){
                    Toast.makeText(ctx,"Ingrese firma de cliente",Toast.LENGTH_SHORT).show();
                    return;
                }
                if(name.getText().toString().trim().isEmpty()){
                    Toast.makeText(ctx,"Ingrese Nombre y Apellido",Toast.LENGTH_SHORT).show();
                    name.setError("Completar campo");
                    return;
                }else{
                    name.setError(null);
                    name_cliente = name.getText().toString().trim();
                }
                if(dni.getText().toString().trim().isEmpty() || dni.getText().toString().length()<8 || dni.getText().toString().length()>8){
                    dni.setError("Completar campo");
                    Toast.makeText(ctx,"Ingrese DNI válido",Toast.LENGTH_SHORT).show();
                    return;
                }else{
                    dni.setError(null);
                    dni_cliente = dni.getText().toString();
                }
                if(cargo.getText().toString().trim().isEmpty()){
                    cargo.setError("Completar campo");
                    Toast.makeText(ctx,"Ingrese Cargo válido",Toast.LENGTH_SHORT).show();
                    return;
                }else{
                    cargo.setError(null);
                    cargo_cliente = cargo.getText().toString().trim();
                }

                System.out.println("image_signature_cliente"+image_signature_cliente);
                System.out.println("name_cliente"+name_cliente);
                System.out.println("dni_cliente"+dni_cliente);
                System.out.println("cargo_cliente"+cargo_cliente);
                if(!image_signature_cliente.isEmpty() && !name_cliente.isEmpty() && !dni_cliente.isEmpty() && !cargo_cliente.isEmpty())
                {
                    Toast.makeText(ctx,"Guardado",Toast.LENGTH_SHORT).show();
                    datos.put("image_signature_tecnico",image_signature_tecnico);
                    datos.put("name_tecnico",name_tecnico);
                    datos.put("dni_tecnico",dni_tecnico);
                    datos.put("cargo_tecnico",cargo_tecnico);
                    alertDialog.dismiss();
                }else{
                    Toast.makeText(ctx,"Debe completar la información",Toast.LENGTH_SHORT).show();
                    return;
                }
            }
        });
        cancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });
        if(!image_signature_cliente.isEmpty()){
            signaturePad.setSignatureBitmap(Image.convert(image_signature_cliente));
        }
        if(name_cliente!=null && !name_cliente.isEmpty()){
            name.setText(name_cliente);
        }
        if(dni_cliente!=null && !dni_cliente.isEmpty()){
            dni.setText(dni_cliente);
        }
        if(cargo_cliente!=null && !cargo_cliente.isEmpty()){
            cargo.setText(cargo_cliente);
        }
    }

    void showModalFirmaTecnico()
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(ctx);
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_ficha_registro_atencion, null);
        builder.setView(dialogView);
        builder.setCancelable(false);
        Button guardar = dialogView.findViewById(R.id.dialog_signature_btn_guardar);
        Button cancelar = dialogView.findViewById(R.id.dialog_signature_btn_cancelar);
        Button limpiar = dialogView.findViewById(R.id.dialog_signature_btn_limpiar);
        name = dialogView.findViewById(R.id.dialog_signature_et_name);
        dni = dialogView.findViewById(R.id.dialog_signature_et_dni);
        cargo = dialogView.findViewById(R.id.dialog_signature_et_cargo);
        final SignaturePad signaturePad = dialogView.findViewById(R.id.dialog_signature);
        if(!datos_guardados.isEmpty()){
            if(datos.get("name_tecnico")!=null){
                name_tecnico = (String)datos.get("name_tecnico");
            }
            if(datos.get("dni_tecnico")!=null){
                dni_tecnico = (String)datos.get("dni_tecnico");
            }
            if(datos.get("cargo_tecnico")!=null){
                cargo_tecnico = (String)datos.get("cargo_tecnico");
            }
            if(datos.get("image_signature_tecnico")!=null){
                image_signature_tecnico = (String)datos.get("image_signature_tecnico");
            }
        }
        final AlertDialog alertDialog = builder.create();
        alertDialog.show();
        limpiar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signaturePad.clear();
            }
        });
        signaturePad.setOnSignedListener(new SignaturePad.OnSignedListener() {
            @Override
            public void onStartSigning() {
            }

            @Override
            public void onSigned() {
                image_signature_tecnico = Image.convert(signaturePad.getSignatureBitmap());
            }

            @Override
            public void onClear() {
                image_signature_tecnico="";
            }
        });
        guardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(image_signature_tecnico.isEmpty()){
                    Toast.makeText(ctx,"Ingrese firma de tecnico",Toast.LENGTH_SHORT).show();
                    return;
                }
                if(name.getText().toString().trim().isEmpty()){
                    Toast.makeText(ctx,"Ingrese Nombre y Apellido",Toast.LENGTH_SHORT).show();
                    name.setError("Completar campo");
                    return;
                }else{
                    name.setError(null);
                    name_tecnico = name.getText().toString().trim();
                }
                if(dni.getText().toString().trim().isEmpty() || dni.getText().toString().length()<8 || dni.getText().toString().length()>8){
                    dni.setError("Completar campo");
                    Toast.makeText(ctx,"Ingrese DNI válido",Toast.LENGTH_SHORT).show();
                    return;
                }else{
                    dni.setError(null);
                    dni_tecnico = dni.getText().toString();
                }
                if(cargo.getText().toString().trim().isEmpty()){
                    cargo.setError("Completar campo");
                    Toast.makeText(ctx,"Ingrese Cargo válido",Toast.LENGTH_SHORT).show();
                    return;
                }else{
                    cargo.setError(null);
                    cargo_tecnico = cargo.getText().toString().trim();
                }

                System.out.println("image_signature_tecnico"+image_signature_tecnico);
                System.out.println("name_tecnico"+name_tecnico);
                System.out.println("dni_tecnico"+dni_tecnico);
                System.out.println("cargo_tecnico"+cargo_tecnico);
                if(!image_signature_tecnico.isEmpty() && !name_tecnico.isEmpty() && !dni_tecnico.isEmpty() && !cargo_tecnico.isEmpty())
                {
                    Toast.makeText(ctx,"Guardado",Toast.LENGTH_SHORT).show();
                    datos.put("image_signature_tecnico",image_signature_tecnico);
                    datos.put("name_tecnico",name_tecnico);
                    datos.put("dni_tecnico",dni_tecnico);
                    datos.put("cargo_tecnico",cargo_tecnico);
                    alertDialog.dismiss();
                }else{
                    Toast.makeText(ctx,"Debe completar la información",Toast.LENGTH_SHORT).show();
                    return;
                }
            }
        });
        cancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });
        if(!image_signature_tecnico.isEmpty()){
            signaturePad.setSignatureBitmap(Image.convert(image_signature_cliente));
        }
        if(name_tecnico!=null && !name_tecnico.isEmpty()){
            name.setText(name_tecnico);
        }
        if(dni_tecnico!=null && !dni_tecnico.isEmpty()){
            dni.setText(dni_tecnico);
        }
        if(cargo_tecnico!=null && !cargo_tecnico.isEmpty()){
            cargo.setText(cargo_tecnico);
        }
    }

    void showModalCortina()
    {
        if(!datos_guardados.isEmpty()){
            if(datos.get("marca_cortina")!=null){
                et_marca_cortina = (String)datos.get("marca_cortina");
            }
            if(datos.get("modelo_cortina")!=null){
                et_modelo_cortina = (String)datos.get("modelo_cortina");
            }
            if(datos.get("nro_serie_cortina")!=null){
                et_nro_serie_cortina = (String)datos.get("nro_serie_cortina");
            }
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(ctx);
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_new_cortina_instalacion, null);
        builder.setView(dialogView);
        builder.setCancelable(false);
        builder.setNegativeButton("Guardar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                et_marca_cortina = marca_cortina.getText().toString();
                et_modelo_cortina = modelo_cortina.getText().toString();
                et_nro_serie_cortina = nro_serie_cortina.getText().toString();
                datos.put("marca_cortina",et_marca_cortina);
                datos.put("modelo_cortina",et_modelo_cortina);
                datos.put("nro_serie_cortina",et_nro_serie_cortina);
                cred.save_data("1-"+id,datos.toString());
                dialog.dismiss();
            }
        });
        builder.setNeutralButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
        marca_cortina = dialogView.findViewById(R.id.et_cortina_marca);
        modelo_cortina = dialogView.findViewById(R.id.et_cortina_modelo);
        nro_serie_cortina = dialogView.findViewById(R.id.et_cortina_nro_serie);
        icon_cortina_scan = dialogView.findViewById(R.id.icon_cortina_scan);
        icon_cortina_scan.setOnClickListener(this);
        marca_cortina.setText(et_marca_cortina);
        modelo_cortina.setText(et_modelo_cortina);
        nro_serie_cortina.setText(et_nro_serie_cortina);
    }

    void showModalBomba()
    {
        if(!datos_guardados.isEmpty()){
            if(datos.get("marca_bomba")!=null){
                et_marca_bomba = (String)datos.get("marca_bomba");
            }
            if(datos.get("modelo_bomba")!=null){
                et_modelo_bomba = (String)datos.get("modelo_bomba");
            }
            if(datos.get("nro_serie_bomba")!=null){
                et_nro_serie_bomba = (String)datos.get("nro_serie_bomba");
            }
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(ctx);
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_new_bomba, null);
        builder.setView(dialogView);
        builder.setCancelable(false);
        builder.setNegativeButton("Guardar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                et_marca_bomba = marca_bomba.getText().toString();
                et_modelo_bomba = modelo_bomba.getText().toString();
                et_nro_serie_bomba = nro_serie_bomba.getText().toString();
                datos.put("marca_bomba",et_marca_bomba);
                datos.put("modelo_bomba",et_modelo_bomba);
                datos.put("nro_serie_bomba",et_nro_serie_bomba);
                cred.save_data("1-"+id,datos.toString());
                dialog.dismiss();
            }
        });
        builder.setNeutralButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
        marca_bomba = dialogView.findViewById(R.id.et_bomba_marca);
        modelo_bomba = dialogView.findViewById(R.id.et_bomba_modelo);
        nro_serie_bomba = dialogView.findViewById(R.id.et_bomba_nro_serie);
        icon_bomba_scan = dialogView.findViewById(R.id.icon_bomba_scan);
        icon_bomba_scan.setOnClickListener(this);
        marca_bomba.setText(et_marca_bomba);
        modelo_bomba.setText(et_modelo_bomba);
        nro_serie_bomba.setText(et_nro_serie_bomba);
    }

    /******************************Modal Fotos**********************************/

    void showModalFotos()
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(ctx);
        LayoutInflater inflater = getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.dialog_fotos_instalacion, null);
        builder.setView(dialogView);
        builder.setCancelable(false);
        final AlertDialog alertDialog = builder.create();

        //Evaporadora
        cam_evap_placa = dialogView.findViewById(R.id.icon_cam_evap_placa);
        cam_evap_frente = dialogView.findViewById(R.id.icon_cam_evap_frente);
        cam_evap_isometrico = dialogView.findViewById(R.id.icon_cam_evap_isometrico);
        gallery_evap_placa = dialogView.findViewById(R.id.icon_gallery_evap_placa);
        gallery_evap_frente = dialogView.findViewById(R.id.icon_gallery_evap_frente);
        gallery_evap_isometrico = dialogView.findViewById(R.id.icon_gallery_evap_isometrico);

        //Condensadora
        cam_cond_placa = dialogView.findViewById(R.id.icon_cam_cond_placa);
        cam_cond_frente = dialogView.findViewById(R.id.icon_cam_cond_frente);
        cam_cond_isometrico = dialogView.findViewById(R.id.icon_cam_cond_isometrico);
        gallery_cond_placa = dialogView.findViewById(R.id.icon_gallery_cond_placa);
        gallery_cond_frente = dialogView.findViewById(R.id.icon_gallery_cond_frente);
        gallery_cond_isometrico = dialogView.findViewById(R.id.icon_gallery_cond_isometrico);

        //Cortina
        cam_cortina = dialogView.findViewById(R.id.icon_cam_cortina);
        gallery_cortina = dialogView.findViewById(R.id.icon_gallery_cortina);

        //Bomba
        cam_bomba = dialogView.findViewById(R.id.icon_cam_bomba);
        gallery_bomba = dialogView.findViewById(R.id.icon_gallery_bomba);

        if(!datos_guardados.isEmpty()){
            if(datos.get("img_evap_placa")!=null){
                img_evap_placa = (String)datos.get("img_evap_placa");
                cam_evap_placa.setVisibility(View.GONE);
                gallery_evap_placa.setVisibility(View.VISIBLE);
            }
            if(datos.get("img_evap_frente")!=null){
                img_evap_frente = (String)datos.get("img_evap_frente");
                cam_evap_frente.setVisibility(View.GONE);
                gallery_evap_frente.setVisibility(View.VISIBLE);
            }
            if(datos.get("img_evap_isometrico")!=null){
                img_evap_isometrico = (String)datos.get("img_evap_isometrico");
                cam_evap_isometrico.setVisibility(View.GONE);
                gallery_evap_isometrico.setVisibility(View.VISIBLE);
            }
            if(datos.get("img_presion_alta")!=null){
                img_presion_alta = (String)datos.get("img_presion_alta");
                cam_presion_alta.setVisibility(View.GONE);
                gallery_presion_alta.setVisibility(View.VISIBLE);
            }
            if(datos.get("img_amperaje")!=null){
                img_amperaje = (String)datos.get("img_amperaje");
                cam_amperaje.setVisibility(View.GONE);
                gallery_amperaje.setVisibility(View.VISIBLE);
            }
            if(datos.get("img_voltaje")!=null){
                img_voltaje = (String)datos.get("img_voltaje");
                cam_voltaje.setVisibility(View.GONE);
                gallery_voltaje.setVisibility(View.VISIBLE);
            }
        }

        //Evaporadora
        cam_evap_placa.setOnClickListener(this);
        cam_evap_frente.setOnClickListener(this);
        cam_evap_isometrico.setOnClickListener(this);
        gallery_evap_placa.setOnClickListener(this);
        gallery_evap_frente.setOnClickListener(this);
        gallery_evap_isometrico.setOnClickListener(this);

        //Condensadora
        cam_cond_placa.setOnClickListener(this);
        cam_cond_frente.setOnClickListener(this);
        cam_cond_isometrico.setOnClickListener(this);
        gallery_cond_placa.setOnClickListener(this);
        gallery_cond_frente.setOnClickListener(this);
        gallery_cond_isometrico.setOnClickListener(this);

        //Cortina
        cam_cortina.setOnClickListener(this);
        gallery_cortina.setOnClickListener(this);

        //Bomba
        cam_bomba.setOnClickListener(this);
        gallery_bomba.setOnClickListener(this);

        Button btn_cancelar = dialogView.findViewById(R.id.dialog_fotos_btn_cancelar);
        Button btn_guardar = dialogView.findViewById(R.id.dialog_fotos_btn_guardar);
        btn_guardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(ctx,"Guardado",Toast.LENGTH_SHORT).show();
                cred.save_data("1-"+id,datos.toString());
                alertDialog.dismiss();
            }
        });
        btn_cancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });
        alertDialog.show();
    }

    /********************************Modal Lecturas******************************/
    void showModalLecturas()
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(ctx);
        LayoutInflater inflater = getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.dialog_lecturas_instalacion, null);
        builder.setView(dialogView);
        builder.setCancelable(false);
        final AlertDialog alertDialog = builder.create();
        Button btn_cancelar = dialogView.findViewById(R.id.dialog_lectura_btn_cancelar);
        Button btn_guardar = dialogView.findViewById(R.id.dialog_lectura_btn_guardar);
        btn_cancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });

        alertDialog.show();

        cam_serpentin = dialogView.findViewById(R.id.icon_cam_temp_serpentin);
        cam_ambiente = dialogView.findViewById(R.id.icon_cam_temp_ambiente);
        cam_presion_baja = dialogView.findViewById(R.id.icon_cam_presion_baja);
        cam_presion_alta = dialogView.findViewById(R.id.icon_cam_presion_alta);
        cam_amperaje = dialogView.findViewById(R.id.icon_cam_amperaje);
        cam_voltaje = dialogView.findViewById(R.id.icon_cam_voltaje);

        cam_serpentin.setOnClickListener(this);
        cam_ambiente.setOnClickListener(this);
        cam_presion_baja.setOnClickListener(this);
        cam_presion_alta.setOnClickListener(this);
        cam_amperaje.setOnClickListener(this);
        cam_voltaje.setOnClickListener(this);

        gallery_serpentin = dialogView.findViewById(R.id.icon_gallery_temp_serpentin);
        gallery_ambiente = dialogView.findViewById(R.id.icon_gallery_temp_ambiente);
        gallery_presion_baja = dialogView.findViewById(R.id.icon_gallery_presion_baja);
        gallery_presion_alta = dialogView.findViewById(R.id.icon_gallery_presion_alta);
        gallery_amperaje = dialogView.findViewById(R.id.icon_gallery_amperaje);
        gallery_voltaje = dialogView.findViewById(R.id.icon_gallery_voltaje);

        gallery_serpentin.setOnClickListener(this);
        gallery_ambiente.setOnClickListener(this);
        gallery_presion_baja.setOnClickListener(this);
        gallery_presion_alta.setOnClickListener(this);
        gallery_amperaje.setOnClickListener(this);
        gallery_voltaje.setOnClickListener(this);

        final EditText et_serpentin = dialogView.findViewById(R.id.et_temp_serpentin);
        final EditText et_ambiente = dialogView.findViewById(R.id.et_temp_ambiente);
        final EditText et_baja = dialogView.findViewById(R.id.et_presion_baja);
        final EditText et_alta = dialogView.findViewById(R.id.et_presion_alta);
        final EditText et_amperaje = dialogView.findViewById(R.id.et_amperaje);
        final EditText et_voltaje = dialogView.findViewById(R.id.et_voltaje);

        if(!datos_guardados.isEmpty()){
            if(datos.get("img_temp_serpentin")!=null){
                img_temp_serpentin = (String)datos.get("img_temp_serpentin");
                cam_serpentin.setVisibility(View.GONE);
                gallery_serpentin.setVisibility(View.VISIBLE);
            }
            if(datos.get("txt_temp_serpentin")!=null){
                txt_temp_serpentin = (String)datos.get("txt_temp_serpentin");
                et_serpentin.setText(txt_temp_serpentin);
            }
            if(datos.get("img_temp_ambiente")!=null){
                img_temp_ambiente = (String)datos.get("img_temp_ambiente");
                cam_ambiente.setVisibility(View.GONE);
                gallery_ambiente.setVisibility(View.VISIBLE);
            }
            if(datos.get("txt_temp_ambiente")!=null){
                txt_temp_ambiente = (String)datos.get("txt_temp_ambiente");
                et_ambiente.setText(txt_temp_ambiente);
            }
            if(datos.get("img_presion_baja")!=null){
                img_presion_baja = (String)datos.get("img_presion_baja");
                cam_presion_baja.setVisibility(View.GONE);
                gallery_presion_baja.setVisibility(View.VISIBLE);
            }
            if(datos.get("txt_presion_baja")!=null){
                txt_presion_baja = (String)datos.get("txt_presion_baja");
                et_baja.setText(txt_presion_baja);
            }
            if(datos.get("img_presion_alta")!=null){
                img_presion_alta = (String)datos.get("img_presion_alta");
                cam_presion_alta.setVisibility(View.GONE);
                gallery_presion_alta.setVisibility(View.VISIBLE);
            }
            if(datos.get("txt_presion_alta")!=null){
                txt_presion_alta = (String)datos.get("txt_presion_alta");
                et_alta.setText(txt_presion_alta);
            }
            if(datos.get("img_amperaje")!=null){
                img_amperaje = (String)datos.get("img_amperaje");
                cam_amperaje.setVisibility(View.GONE);
                gallery_amperaje.setVisibility(View.VISIBLE);
            }
            if(datos.get("txt_amperaje")!=null){
                txt_amperaje = (String)datos.get("txt_amperaje");
                et_amperaje.setText(txt_amperaje);
            }
            if(datos.get("img_voltaje")!=null){
                img_voltaje = (String)datos.get("img_voltaje");
                cam_voltaje.setVisibility(View.GONE);
                gallery_voltaje.setVisibility(View.VISIBLE);
            }
            if(datos.get("txt_voltaje")!=null){
                txt_voltaje = (String)datos.get("txt_voltaje");
                et_voltaje.setText(txt_voltaje);
            }
        }

        btn_guardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                txt_temp_serpentin = et_serpentin.getText().toString();
                txt_temp_ambiente = et_ambiente.getText().toString();
                txt_presion_baja = et_baja.getText().toString();
                txt_presion_alta = et_alta.getText().toString();
                txt_amperaje = et_amperaje.getText().toString();
                txt_voltaje = et_voltaje.getText().toString();

                datos.put("txt_temp_serpentin",txt_temp_serpentin);
                datos.put("txt_temp_ambiente",txt_temp_ambiente);
                datos.put("txt_presion_baja",txt_presion_baja);
                datos.put("txt_presion_alta",txt_presion_alta);
                datos.put("txt_amperaje",txt_amperaje);
                datos.put("txt_voltaje",txt_voltaje);

                cred.save_data("1-"+id,datos.toString());

                Toast.makeText(ctx,"Guardado",Toast.LENGTH_SHORT).show();
                alertDialog.dismiss();
            }
        });
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
                                Toast.makeText(ctx, cliente.getDes_error(), Toast.LENGTH_SHORT).show();
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
                                if(spinner_marcas!=null){
                                    marca_adapter = new ArrayAdapter<>(ctx,R.layout.dropdown_style,data);
                                    marca_ids = data_id;
                                    spinner_marcas.setAdapter(marca_adapter);
                                    marca_adapter.notifyDataSetChanged();
                                }

                                if(spinner_cond_marcas!=null){
                                    marca_cond_adapter = new ArrayAdapter<>(ctx,R.layout.dropdown_style,data);
                                    marca_cond_ids = data_id;
                                    spinner_cond_marcas.setAdapter(marca_cond_adapter);
                                    marca_cond_adapter.notifyDataSetChanged();
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
                                Toast.makeText(ctx, cliente.getDes_error(), Toast.LENGTH_SHORT).show();
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
                                modelo_adapter = new ArrayAdapter<>(ctx,R.layout.dropdown_style,data);
                                spinner_modelos.setAdapter(modelo_adapter);
                                modelo_adapter.notifyDataSetChanged();
                                modelo_ids = data_id;
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
                                Toast.makeText(ctx, cliente.getDes_error(), Toast.LENGTH_SHORT).show();
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
                                modelo_cond_ids = data_id;
                                modelo_cond_adapter.notifyDataSetChanged();
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
                                Toast.makeText(ctx, cliente.getDes_error(), Toast.LENGTH_SHORT).show();
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
                                btus_ids = data_id;
                                spinner_btus.setAdapter(btu_adapter);
                                btu_adapter.notifyDataSetChanged();
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
                                Toast.makeText(ctx, cliente.getDes_error(), Toast.LENGTH_SHORT).show();
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
                                tipo_adapter = new ArrayAdapter<>(ctx,R.layout.dropdown_style,data);
                                tipo_ids = data_id;
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
                                Toast.makeText(ctx, cliente.getDes_error(), Toast.LENGTH_SHORT).show();
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
                                tipo_cond_ids = data_id;
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
                                Toast.makeText(ctx, cliente.getDes_error(), Toast.LENGTH_SHORT).show();
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
                                voltajes_ids = data_id;
                                spinner_voltajes.setAdapter(voltaje_adapter);
                                voltaje_adapter.notifyDataSetChanged();
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
                                Toast.makeText(ctx, cliente.getDes_error(), Toast.LENGTH_SHORT).show();
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
                                fases_ids = data_id;
                                spinner_fases.setAdapter(fase_adapter);
                                fase_adapter.notifyDataSetChanged();
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
                                Toast.makeText(ctx, cliente.getDes_error(), Toast.LENGTH_SHORT).show();
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
                                refrigerantes_ids = data_id;
                                spinner_refrigerantes.setAdapter(refrigerante_adapter);
                                refrigerante_adapter.notifyDataSetChanged();
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
}
