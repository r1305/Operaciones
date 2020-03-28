package com.system.operaciones.activities;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
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
import android.view.View;
import android.view.Window;
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
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
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

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import jrizani.jrspinner.JRSpinner;

public class FichaMantenimientoActivity extends AppCompatActivity {

    Context ctx;
    Credentials cred;
    //ANTES
    private ImageView icon_camera_serpentin,icon_camera_bomba,icon_camera_cortina;
    private ImageView icon_gallery_serpentin,icon_gallery_bomba,icon_gallery_cortina;
    private ImageView icon_camera_cond_serpentin,icon_camera_ventilador,icon_camera_carcaza;
    private ImageView icon_gallery_cond_serpentin,icon_gallery_ventilador,icon_gallery_carcaza;
    String image_serpentin="",image_bomba="",image_cortina="";
    String image_cond_serpentin="",image_ventilador="",image_carcaza="";

    //DESPUES
    private ImageView despues_icon_camera_serpentin,despues_icon_camera_bomba,despues_icon_camera_cortina;
    private ImageView despues_icon_gallery_serpentin,despues_icon_gallery_bomba,despues_icon_gallery_cortina;
    private ImageView despues_icon_camera_cond_serpentin,despues_icon_camera_ventilador,despues_icon_camera_carcaza;
    private ImageView despues_icon_gallery_cond_serpentin,despues_icon_gallery_ventilador,despues_icon_gallery_carcaza;
    String despues_image_serpentin="",despues_image_bomba="",despues_image_cortina="";
    String despues_image_cond_serpentin="",despues_image_ventilador="",despues_image_carcaza="";

    //Firmas
    String image_signature_cliente="",image_signature_tecnico="";

    private Button btn_lectura_cancelar;
    private Button btn_lectura_guardar;
    private String id,tienda_id;

    AlertDialog alertDialog;

    private String name_tecnico="",dni_tecnico="",cargo_tecnico="",name_cliente="",dni_cliente="",cargo_cliente="";

    int equipo_count=0;
    int cortina_count=0;
    int tipo_nro_serie = 1;
    private static final int  CODIGO_INTENT = 2;

    ViewDialog viewDialog;

    int modo_prueba = 0;
    String fase = "0";

    EditText cargo,dni,name;
    ImageView adjunto_preview;
    String adjunto_b64="";
    EditText et_otros;
    Spinner spinner_observaciones;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ficha_mantenimiento);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Ficha Mantenimiento");

        ctx = this;
        cred = new Credentials(ctx);
        id = getIntent().getStringExtra("id");
        tienda_id = getIntent().getStringExtra("tienda_id");
        getEquipos();
        viewDialog = new ViewDialog(this);
        viewDialog.showDialog();
        adjunto_preview = findViewById(R.id.preview_adjunto);
        if(adjunto_b64!=null && !adjunto_b64.equals("")){

            adjunto_preview.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    showPreview(adjunto_b64,0);
                }
            });
        }

        et_otros = findViewById(R.id.et_otros);
        spinner_observaciones = findViewById(R.id.spinner_observaciones);
        spinner_observaciones.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if(adapterView.getItemAtPosition(i).equals("Otros"))
                {
                    et_otros.setVisibility(View.VISIBLE);
                }else{
                    et_otros.setVisibility(View.GONE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        cred.save_data("image_type","0");

        Button btn_antes = findViewById(R.id.btn_antes);
        Button btn_despues = findViewById(R.id.btn_despues);

        btn_antes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(ctx);
                LayoutInflater inflater = getLayoutInflater();
                View dialogView = inflater.inflate(R.layout.dialog_lecturas_mantenimiento, null);
                builder.setView(dialogView);
                builder.setCancelable(false);
                icon_camera_serpentin = dialogView.findViewById(R.id.icon_cam_serpentin);
                icon_camera_bomba = dialogView.findViewById(R.id.icon_cam_bomba);
                icon_camera_cortina = dialogView.findViewById(R.id.icon_camera_cortina);
                icon_camera_cond_serpentin = dialogView.findViewById(R.id.icon_cam_cond_serpentin);
                icon_camera_ventilador = dialogView.findViewById(R.id.icon_cam_ventilador);
                icon_camera_carcaza = dialogView.findViewById(R.id.icon_cam_carcaza);

                icon_gallery_serpentin = dialogView.findViewById(R.id.icon_gallery_serpentin);
                icon_gallery_bomba = dialogView.findViewById(R.id.icon_gallery_bomba);
                icon_gallery_cortina = dialogView.findViewById(R.id.icon_gallery_cortina);
                icon_gallery_cond_serpentin = dialogView.findViewById(R.id.icon_gallery_cond_serpentin);
                icon_gallery_ventilador = dialogView.findViewById(R.id.icon_gallery_ventilador);
                icon_gallery_carcaza = dialogView.findViewById(R.id.icon_gallery_carcaza);

                btn_lectura_cancelar = dialogView.findViewById(R.id.dialog_lectura_btn_cancelar);
                btn_lectura_guardar = dialogView.findViewById(R.id.dialog_lectura_btn_guardar);
                btn_lectura_cancelar.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        alertDialog.dismiss();
                    }
                });
                btn_lectura_guardar.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        alertDialog.dismiss();
                        Toast.makeText(ctx,"Guardado",Toast.LENGTH_SHORT).show();
                    }
                });

                alertDialog = builder.create();
                alertDialog.show();

                icon_camera_serpentin.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Log.e("falla","1");
                        cred.save_data("image_type","1");
                        openChooser();
                    }
                });

                icon_gallery_serpentin.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showPreview(image_serpentin,1);
                    }
                });

                icon_camera_bomba.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        cred.save_data("image_type","2");
                        openChooser();
                    }
                });

                icon_gallery_bomba.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showPreview(image_bomba,2);
                    }
                });

                icon_camera_cortina.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        cred.save_data("image_type","3");
                        openChooser();
                    }
                });

                icon_gallery_cortina.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showPreview(image_cortina,3);
                    }
                });

                icon_camera_cond_serpentin.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Log.e("cond_serpentin","4");
                        cred.save_data("image_type","4");
                        openChooser();
                    }
                });

                icon_gallery_cond_serpentin.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showPreview(image_cortina,4);
                    }
                });

                icon_camera_ventilador.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Log.e("cond_ventilador","5");
                        cred.save_data("image_type","5");
                        openChooser();
                    }
                });

                icon_gallery_ventilador.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showPreview(image_ventilador,5);
                    }
                });

                icon_camera_carcaza.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        cred.save_data("image_type","6");
                        openChooser();
                    }
                });

                icon_gallery_carcaza.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showPreview(image_carcaza,6);
                    }
                });

                validateUpload();
            }
        });

        //BTN_DESPUES
        btn_despues.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(ctx);
                LayoutInflater inflater = getLayoutInflater();
                View dialogView = inflater.inflate(R.layout.dialog_lecturas_mantenimiento, null);
                builder.setView(dialogView);
                builder.setCancelable(false);

                despues_icon_camera_serpentin = dialogView.findViewById(R.id.icon_cam_serpentin);
                despues_icon_camera_bomba = dialogView.findViewById(R.id.icon_cam_bomba);
                despues_icon_camera_cortina = dialogView.findViewById(R.id.icon_camera_cortina);
                despues_icon_camera_cond_serpentin = dialogView.findViewById(R.id.icon_cam_cond_serpentin);
                despues_icon_camera_ventilador = dialogView.findViewById(R.id.icon_cam_ventilador);
                despues_icon_camera_carcaza = dialogView.findViewById(R.id.icon_cam_carcaza);

                despues_icon_gallery_serpentin = dialogView.findViewById(R.id.icon_gallery_serpentin);
                despues_icon_gallery_bomba = dialogView.findViewById(R.id.icon_gallery_bomba);
                despues_icon_gallery_cortina = dialogView.findViewById(R.id.icon_gallery_cortina);
                despues_icon_gallery_cond_serpentin = dialogView.findViewById(R.id.icon_gallery_cond_serpentin);
                despues_icon_gallery_ventilador = dialogView.findViewById(R.id.icon_gallery_ventilador);
                despues_icon_gallery_carcaza = dialogView.findViewById(R.id.icon_gallery_carcaza);

                validateUploadDespues();

                btn_lectura_cancelar = dialogView.findViewById(R.id.dialog_lectura_btn_cancelar);
                btn_lectura_guardar = dialogView.findViewById(R.id.dialog_lectura_btn_guardar);
                btn_lectura_cancelar.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        alertDialog.dismiss();
                    }
                });
                btn_lectura_guardar.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        alertDialog.dismiss();
                        Toast.makeText(ctx,"Guardado",Toast.LENGTH_SHORT).show();
                    }
                });

                alertDialog = builder.create();
                alertDialog.show();

                despues_icon_camera_serpentin.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Log.e("falla","1");
                        cred.save_data("image_type","7");
                        openChooser();
                    }
                });

                despues_icon_gallery_serpentin.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showPreview(image_serpentin,7);
                    }
                });

                despues_icon_camera_bomba.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        cred.save_data("image_type","8");
                        openChooser();
                    }
                });

                despues_icon_gallery_bomba.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showPreview(image_bomba,8);
                    }
                });

                despues_icon_camera_cortina.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        cred.save_data("image_type","9");
                        openChooser();
                    }
                });

                despues_icon_gallery_cortina.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showPreview(image_cortina,9);
                    }
                });

                despues_icon_camera_cond_serpentin.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Log.e("despues_cond_serpentin","10");
                        cred.save_data("image_type","10");
                        openChooser();
                    }
                });

                despues_icon_gallery_cond_serpentin.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showPreview(image_cortina,10);
                    }
                });

                despues_icon_camera_ventilador.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Log.e("cond_ventilador","11");
                        cred.save_data("image_type","11");
                        openChooser();
                    }
                });

                despues_icon_gallery_ventilador.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showPreview(image_ventilador,11);
                    }
                });

                despues_icon_camera_carcaza.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        cred.save_data("image_type","12");
                        openChooser();
                    }
                });

                despues_icon_gallery_carcaza.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showPreview(image_carcaza,12);
                    }
                });

            }
        });

        Button btn_registrar = findViewById(R.id.btn_registrar);
        Button btn_cerrar = findViewById(R.id.btn_cerrar);
        btn_cerrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((FichaMantenimientoActivity)ctx).finish();
            }
        });
        Button btn_firma_tecnico = findViewById(R.id.btn_firma_tecnico);
        Button btn_firma_cliente = findViewById(R.id.btn_firma_cliente);

        btn_registrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(image_serpentin.isEmpty()){
                    Toast.makeText(ctx,"Debe cargar foto de la falla",Toast.LENGTH_SHORT).show();
                    return;
                }
                if(image_bomba.isEmpty()){
                    Toast.makeText(ctx,"Debe cargar foto antes de la reparación de temperatura T1",Toast.LENGTH_SHORT).show();
                    return;
                }
                if(image_cortina.isEmpty()){
                    Toast.makeText(ctx,"Debe cargar foto antes de la reparación de temperatura T2",Toast.LENGTH_SHORT).show();
                    return;
                }

                if(image_cond_serpentin.isEmpty())
                {
                    Toast.makeText(ctx,"Debe cargar foto antes de la reparación de presión baja",Toast.LENGTH_SHORT).show();
                    return;
                }
                if(image_ventilador.isEmpty())
                {
                    Toast.makeText(ctx,"Debe cargar foto antes de la reparación de presión alta",Toast.LENGTH_SHORT).show();
                    return;
                }
                if(image_carcaza.isEmpty())
                {
                    Toast.makeText(ctx,"Debe cargar foto antes de la reparación de amperaje en la Línea 1",Toast.LENGTH_SHORT).show();
                    return;
                }

                registrarFicha(id);
            }
        });

        btn_firma_cliente.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(ctx);
                LayoutInflater inflater = getLayoutInflater();
                View dialogView = inflater.inflate(R.layout.dialog_ficha_registro_atencion, null);
                builder.setView(dialogView);
                builder.setCancelable(false);
                Button guardar = dialogView.findViewById(R.id.dialog_signature_btn_guardar);
                Button cancelar = dialogView.findViewById(R.id.dialog_signature_btn_cancelar);
                name = dialogView.findViewById(R.id.dialog_signature_et_name);
                dni = dialogView.findViewById(R.id.dialog_signature_et_dni);
                cargo = dialogView.findViewById(R.id.dialog_signature_et_cargo);
                final SignaturePad signaturePad = dialogView.findViewById(R.id.dialog_signature);
                final AlertDialog alertDialog = builder.create();
                alertDialog.show();
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
        });

        btn_firma_tecnico.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(ctx);
                LayoutInflater inflater = getLayoutInflater();
                View dialogView = inflater.inflate(R.layout.dialog_ficha_registro_atencion, null);
                builder.setView(dialogView);
                builder.setCancelable(false);
                Button guardar = dialogView.findViewById(R.id.dialog_signature_btn_guardar);
                Button cancelar = dialogView.findViewById(R.id.dialog_signature_btn_cancelar);
                name = dialogView.findViewById(R.id.dialog_signature_et_name);
                dni = dialogView.findViewById(R.id.dialog_signature_et_dni);
                cargo = dialogView.findViewById(R.id.dialog_signature_et_cargo);
                Log.e("tipo_proveedor",tipo_proveedor);
                if(tipo_proveedor.equals("1")){
                    getDatosTecnico(tecnico_id);
                }
                final SignaturePad signaturePad = dialogView.findViewById(R.id.dialog_signature);
                final AlertDialog alertDialog = builder.create();
                alertDialog.show();
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
                    }
                });
                guardar.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(image_signature_tecnico.isEmpty()){
                            Toast.makeText(ctx,"Ingrese firma de técnico",Toast.LENGTH_SHORT).show();
                            return;
                        }
                        if(name.getText().toString().trim().isEmpty()){
                            Toast.makeText(ctx,"Ingrese Nombre y Apellido",Toast.LENGTH_SHORT).show();
                            return;
                        }else{
                            name_tecnico = name.getText().toString().trim();
                        }
                        if(dni.getText().toString().trim().isEmpty() || dni.getText().toString().length()<8 || dni.getText().toString().length()>8){
                            Toast.makeText(ctx,"Ingrese DNI válido",Toast.LENGTH_SHORT).show();
                            return;
                        }else{
                            dni_tecnico = dni.getText().toString().trim();
                        }
                        if(cargo.getText().toString().trim().isEmpty()){
                            Toast.makeText(ctx,"Ingrese Cargo válido",Toast.LENGTH_SHORT).show();
                            return;
                        }else{
                            cargo_tecnico = cargo.getText().toString().trim();
                        }
                        if(!image_signature_tecnico.isEmpty() && !name_tecnico.isEmpty() && !dni_tecnico.isEmpty() && !cargo_tecnico.isEmpty())
                        {
                            Toast.makeText(ctx,"Guardado",Toast.LENGTH_SHORT).show();
                            alertDialog.dismiss();
                        }else{
                            Toast.makeText(ctx,"Debe completar la información",Toast.LENGTH_SHORT).show();
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
                    signaturePad.setSignatureBitmap(Image.convert(image_signature_tecnico));
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
        });
    }

    List<JSONObject> equipos = new ArrayList<>();
    public void getEquipos()
    {
        System.out.println("tienda_id: "+tienda_id);
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
                                equipos.clear();
                                for(Object o: respuesta){
                                    JSONObject ob = (JSONObject)o;
                                    equipos.add((JSONObject)o);
                                    if(!ob.get("id").equals("0") && ob.get("tipo").equals("1"))
                                        equipo_count+=1;
                                    else if(!ob.get("id").equals("0") && ob.get("tipo").equals("2"))
                                        cortina_count+=1;

                                }
                                getEquipo();

                            }
                        } catch (Exception e) {
                            viewDialog.hideDialog(1);
                            e.printStackTrace();
                            System.out.println("getFases_error: " + e);
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
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                50000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        // Add the request to the RequestQueue.
        queue.add(stringRequest);
    }

    private void showPreview(String b64,final int element)
    {
        System.out.println("preview_url: "+b64);
        AlertDialog.Builder builder = new AlertDialog.Builder(ctx);
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
                    cleanPhoto(element);
                }
            });
        }


        AlertDialog alert_preview = builder.create();
        alert_preview.show();
    }

    void cleanPhoto(int element)
    {
        switch (element){
            //Antes
            case 1:
                image_serpentin="";
                icon_gallery_serpentin.setVisibility(View.GONE);
                icon_camera_serpentin.setVisibility(View.VISIBLE);
                break;
            case 2:
                image_bomba="";
                icon_gallery_bomba.setVisibility(View.GONE);
                icon_camera_bomba.setVisibility(View.VISIBLE);
                break;
            case 3:
                image_cortina="";
                icon_gallery_cortina.setVisibility(View.GONE);
                icon_camera_cortina.setVisibility(View.VISIBLE);
                break;
            case 4:
                image_cond_serpentin="";
                icon_gallery_cond_serpentin.setVisibility(View.GONE);
                icon_camera_cond_serpentin.setVisibility(View.VISIBLE);
                break;
            case 5:
                image_ventilador="";
                icon_gallery_ventilador.setVisibility(View.GONE);
                icon_camera_ventilador.setVisibility(View.VISIBLE);
                break;
            case 6:
                image_carcaza="";
                icon_gallery_carcaza.setVisibility(View.GONE);
                icon_camera_carcaza.setVisibility(View.VISIBLE);
                break;
            //Despues
            case 7:
                despues_image_serpentin="";
                despues_icon_gallery_serpentin.setVisibility(View.GONE);
                despues_icon_camera_serpentin.setVisibility(View.VISIBLE);
                break;
            case 8:
                despues_image_bomba="";
                despues_icon_gallery_bomba.setVisibility(View.GONE);
                despues_icon_camera_bomba.setVisibility(View.VISIBLE);
                break;
            case 9:
                despues_image_cortina="";
                despues_icon_gallery_cortina.setVisibility(View.GONE);
                despues_icon_camera_cortina.setVisibility(View.VISIBLE);
                break;
            case 10:
                despues_image_cond_serpentin="";
                despues_icon_gallery_cond_serpentin.setVisibility(View.GONE);
                despues_icon_camera_cond_serpentin.setVisibility(View.VISIBLE);
                break;
            case 11:
                despues_image_ventilador="";
                despues_icon_gallery_ventilador.setVisibility(View.GONE);
                despues_icon_camera_ventilador.setVisibility(View.VISIBLE);
                break;
            case 12:
                despues_image_carcaza="";
                despues_icon_gallery_carcaza.setVisibility(View.GONE);
                despues_icon_camera_carcaza.setVisibility(View.VISIBLE);
                break;
        }
    }

    @Override
    public boolean onSupportNavigateUp()
    {
        onBackPressed();
        return true;
    }

    private void registrarFicha(final String mantenimiento_id)
    {
        viewDialog.showDialog();
        String url=ctx.getApplicationContext().getString(R.string.base_url)+ctx.getApplicationContext().getString(R.string.register_ficha_mantenimiento_url);
        RequestQueue queue = Volley.newRequestQueue(ctx);

        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        System.out.println("register_ficha_mantenimiento_response: " + response);
                        try {
                            RespuestaResponse cliente = new Gson().fromJson(response, RespuestaResponse.class);
                            JSONParser parser = new JSONParser();
                            JSONArray respuesta = (JSONArray) parser.parse((String) cliente.getRespuesta());

                            if (cliente.getIde_error() == 0) {
                                viewDialog.hideDialog(1);
                                Toast.makeText(ctx, cliente.getDes_error(), Toast.LENGTH_LONG).show();
                            } else {
                                Toast.makeText(ctx,"Registro correcto",Toast.LENGTH_SHORT).show();
                                createFicha(mantenimiento_id);
                            }
                        } catch (Exception e) {
                            viewDialog.hideDialog(1);
                            e.printStackTrace();
                            System.out.println("register_ficha_mantenimiento_error: " + e);
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                viewDialog.hideDialog(1);
                error.printStackTrace();
                System.out.println("register_ficha_mantenimiento_error: " + error);
            }
        }){
            @Override
            protected Map<String, String> getParams()
            {
                Map<String, String> params = new HashMap<>();
                params.put("mantenimiento_id", mantenimiento_id);
                params.put("tienda_id", tienda_id);
                if(spinner_observaciones.getSelectedItem().toString().equals("Otros")){
                    params.put("observaciones",et_otros.getText().toString());
                }else{
                    params.put("observaciones",spinner_observaciones.getSelectedItem().toString());
                }

                //ANTES
                params.put("img_serpentin", image_serpentin);
                params.put("img_bomba", image_bomba);
                params.put("img_cortina", image_cortina);
                params.put("img_cond_serpentin", image_cond_serpentin);
                params.put("img_ventilador", image_ventilador);
                params.put("img_carcaza", image_carcaza);

                //DESPUES
                params.put("despues_img_serpentin", despues_image_serpentin);
                params.put("despues_img_bomba", despues_image_bomba);
                params.put("despues_img_cortina", despues_image_cortina);
                params.put("despues_img_cond_serpentin", despues_image_cond_serpentin);
                params.put("despues_img_ventilador", despues_image_ventilador);
                params.put("despues_img_carcaza", despues_image_carcaza);

                //FIRMAS
                params.put("img_signature_tecnico",image_signature_tecnico);
                params.put("name_tecnico",name_tecnico);
                params.put("dni_tecnico",dni_tecnico);
                params.put("cargo_tecnico",cargo_tecnico);

                params.put("img_signature_cliente",image_signature_cliente);
                params.put("name_cliente",name_cliente);
                params.put("dni_cliente",dni_cliente);
                params.put("cargo_cliente",cargo_cliente);
                return params;
            }
        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                50000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        // Add the request to the RequestQueue.
        queue.add(stringRequest);
    }

    private void createFicha(final String urgencia_id)
    {
        String url=ctx.getApplicationContext().getString(R.string.base_url)+ctx.getApplicationContext().getString(R.string.crear_ficha_mantenimiento_url);
        RequestQueue queue = Volley.newRequestQueue(ctx);

        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        System.out.println("create_ficha_mantenimiento_response: " + response);
                        try {
                            RespuestaResponse cliente = new Gson().fromJson(response, RespuestaResponse.class);
                            JSONParser parser = new JSONParser();
                            JSONArray respuesta = (JSONArray) parser.parse((String) cliente.getRespuesta());

                            if (cliente.getIde_error() == 0) {
                                viewDialog.hideDialog(1);
                                viewDialog.hideDialog(1);
                                Toast.makeText(ctx, cliente.getDes_error(), Toast.LENGTH_LONG).show();
                            } else {
                                Toast.makeText(ctx,"Ficha creada correctamente",Toast.LENGTH_SHORT).show();
                                new Handler().postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        new Utils().sendFichaMantenimiento(tienda_id,urgencia_id,ctx);
                                        viewDialog.hideDialog(0);
                                        ((FichaMantenimientoActivity)ctx).finish();
                                    }
                                }, 5000);
                            }
                        } catch (Exception e) {
                            viewDialog.hideDialog(1);
                            e.printStackTrace();
                            System.out.println("create_ficha_mantenimiento_error: " + e);
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                viewDialog.hideDialog(1);
                error.printStackTrace();
                System.out.println("create_ficha_mantenimiento_error: " + error);
            }
        }){
            @Override
            protected Map<String, String> getParams()
            {
                Map<String, String> params = new HashMap<>();
                params.put("mantenimiento_id", urgencia_id);
                return params;
            }
        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                50000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        // Add the request to the RequestQueue.
        queue.add(stringRequest);
    }


    EditText et_nro_serie,et_cond_nro_serie;

    TextView tv_marca,tv_modelo,tv_btu,tv_nro_serie,tv_tipo,tv_voltaje,tv_refrigerante,tv_nro_equipo;
    String equipo_id;

    String tipo_proveedor="0";
    String tecnico_id = "0";
    private void getEquipo()
    {
        String url=ctx.getApplicationContext().getString(R.string.base_url)+ctx.getApplicationContext().getString(R.string.getEquipoMantenimiento_url);
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
                            System.out.println("json_equipo"+respuesta);
                            if (cliente.getIde_error() == 0) {
                                viewDialog.hideDialog(1);
                                Toast.makeText(ctx, cliente.getDes_error(), Toast.LENGTH_LONG).show();
                            } else {
                                equipo_id="";
                                for (Object o:respuesta)
                                {
                                    JSONObject ob = (JSONObject)o;
                                    equipo_id= (String)ob.get("equipo_id");
                                }
                                System.out.println("getEquipo_equipo_contador: "+equipo_count);
                                viewDialog.hideDialog(1);
                                for (Object o:respuesta)
                                {
                                    JSONObject ob = (JSONObject)o;
                                    String marca = (String)ob.get("marca");
                                    String btu = (String)ob.get("btu");
                                    String tipo = (String)ob.get("tipo");
                                    String modelo = (String)ob.get("modelo");
                                    String nro_serie = (String)ob.get("nro_serie");
                                    String voltaje = (String)ob.get("voltaje");
                                    String refrigerante = (String)ob.get("refrigerante");
                                    fase = (String)ob.get("cond_fase_id");
                                    String nro_equipo = (String)ob.get("nro_equipo");
                                    adjunto_b64 = (String)ob.get("adjunto");
                                    if(adjunto_b64!=null && !adjunto_b64.equals("")){
                                        adjunto_preview.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View view) {
                                                showPreview(adjunto_b64,0);
                                            }
                                        });
                                    }else{
                                        Toast.makeText(ctx,"Archivo adjunto no disponible",Toast.LENGTH_SHORT).show();
                                    }
                                    tv_marca = findViewById(R.id.ficha_marca);
                                    tv_btu = findViewById(R.id.ficha_btu);
                                    tv_tipo = findViewById(R.id.ficha_tipo);
                                    tv_modelo = findViewById(R.id.ficha_modelo);
                                    tv_nro_serie = findViewById(R.id.ficha_nro_serie);
                                    tv_voltaje = findViewById(R.id.ficha_voltaje);
                                    tv_refrigerante = findViewById(R.id.ficha_refrigerante);
                                    tv_nro_equipo = findViewById(R.id.ficha_tv_nro_equipo);

                                    tv_marca.setText(marca);
                                    tv_btu.setText(btu);
                                    tv_tipo.setText(tipo);
                                    tv_modelo.setText(modelo);
                                    tv_nro_serie.setText(nro_serie);
                                    tv_voltaje.setText(voltaje);
                                    tv_refrigerante.setText(refrigerante);
                                    tv_nro_equipo.setText("Equipo N° "+nro_equipo);


                                    tipo_proveedor = (String)ob.get("tipo_proveedor");
                                    if(tipo_proveedor.equals("1")){
                                        tecnico_id = (String)ob.get("proveedor_id");
                                    }
                                }
                            }
                        } catch (Exception e) {
                            viewDialog.hideDialog(1);
                            e.printStackTrace();
                            System.out.println("getFases_error: " + e);
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                viewDialog.hideDialog(1);
                error.printStackTrace();
                System.out.println("getEquipo_error: " + error);
            }
        }){
            @Override
            protected Map<String, String> getParams()
            {
                Map<String, String> params = new HashMap<>();
                params.put("mantenimiento_id", id);
                return params;
            }
        };

        // Add the request to the RequestQueue.
        queue.add(stringRequest);
    }

    private void getDatosTecnico(final String tecnico_id)
    {
        String url=ctx.getApplicationContext().getString(R.string.base_url)+ctx.getApplicationContext().getString(R.string.getDatosTecnico_url);
        Log.i("getDatosTecnico_url",url);
        RequestQueue queue = Volley.newRequestQueue(ctx);

        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        System.out.println("getDatosTecnico_response: " + response);
                        try {
                            RespuestaResponse cliente = new Gson().fromJson(response, RespuestaResponse.class);
                            JSONParser parser = new JSONParser();
                            JSONArray respuesta = (JSONArray) parser.parse((String) cliente.getRespuesta());

                            if (cliente.getIde_error() == 0) {
                                Toast.makeText(ctx, cliente.getDes_error(), Toast.LENGTH_LONG).show();
                            } else {
                                for (Object ob : respuesta){
                                    JSONObject o = (JSONObject)ob;
                                    cargo.setText((String)o.get("cargo"));
                                    name.setText((String)o.get("tecnico"));
                                    dni.setText((String)o.get("dni"));
                                }
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            System.out.println("getDatosTecnico_error: "+e.getMessage());
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println("getDatosTecnico_error: " + error.getMessage());
            }
        }){
            @Override
            protected Map<String, String> getParams()
            {
                Map<String, String> params = new HashMap<>();
                params.put("tecnico_id", tecnico_id);
                return params;
            }
        };

        // Add the request to the RequestQueue.
        queue.add(stringRequest);
    }

    String marca_id="",cond_marca_id = "";
    String modelo_id="",cond_modelo_id = "";
    String btu_id="",cond_btu_id="";
    String tipo_id="",cond_tipo_id="";
    String voltaje_id="",cond_voltaje_id="";
    String fase_id="", cond_fase_id="";
    String refrigerante_id="",cond_refrigerante_id="";
    String nro_serie="",cond_nro_serie="";

    void validateUploadDespues()
    {
        if(!despues_image_serpentin.isEmpty()){
            despues_icon_camera_serpentin.setVisibility(View.GONE);
            despues_icon_gallery_serpentin.setVisibility(View.VISIBLE);
        }else{
            despues_icon_gallery_serpentin.setVisibility(View.GONE);
            despues_icon_camera_serpentin.setVisibility(View.VISIBLE);
        }

        if(!despues_image_bomba.isEmpty()){
            despues_icon_camera_bomba.setVisibility(View.GONE);
            despues_icon_gallery_bomba.setVisibility(View.VISIBLE);
        }else{
            despues_icon_camera_bomba.setVisibility(View.VISIBLE);
            despues_icon_gallery_bomba.setVisibility(View.GONE);
        }

        if(!despues_image_cortina.isEmpty()){
            despues_icon_camera_cortina.setVisibility(View.GONE);
            despues_icon_gallery_cortina.setVisibility(View.VISIBLE);
        }else{
            despues_icon_camera_cortina.setVisibility(View.VISIBLE);
            despues_icon_gallery_cortina.setVisibility(View.GONE);
        }

        if(!despues_image_cond_serpentin.isEmpty()){
            despues_icon_camera_cond_serpentin.setVisibility(View.GONE);
            despues_icon_gallery_cond_serpentin.setVisibility(View.VISIBLE);
        }else{
            despues_icon_camera_cond_serpentin.setVisibility(View.VISIBLE);
            despues_icon_gallery_cond_serpentin.setVisibility(View.GONE);
        }

        if(!despues_image_ventilador.isEmpty()){
            despues_icon_camera_ventilador.setVisibility(View.GONE);
            despues_icon_gallery_ventilador.setVisibility(View.VISIBLE);
        }else{
            despues_icon_camera_ventilador.setVisibility(View.VISIBLE);
            despues_icon_gallery_ventilador.setVisibility(View.GONE);
        }

        if(!despues_image_carcaza.isEmpty()){
            despues_icon_camera_carcaza.setVisibility(View.GONE);
            despues_icon_gallery_carcaza.setVisibility(View.VISIBLE);
        }else{
            despues_icon_camera_carcaza.setVisibility(View.VISIBLE);
            despues_icon_gallery_carcaza.setVisibility(View.GONE);
        }
    }

    void validateUpload()
    {
        if(!image_serpentin.isEmpty()){
            icon_camera_serpentin.setVisibility(View.GONE);
            icon_gallery_serpentin.setVisibility(View.VISIBLE);
        }else{
            icon_gallery_serpentin.setVisibility(View.GONE);
            icon_camera_serpentin.setVisibility(View.VISIBLE);
        }

        if(!image_bomba.isEmpty()){
            icon_camera_bomba.setVisibility(View.GONE);
            icon_gallery_bomba.setVisibility(View.VISIBLE);
        }else{
            icon_camera_bomba.setVisibility(View.VISIBLE);
            icon_gallery_bomba.setVisibility(View.GONE);
        }

        if(!image_cortina.isEmpty()){
            icon_camera_cortina.setVisibility(View.GONE);
            icon_gallery_cortina.setVisibility(View.VISIBLE);
        }else{
            icon_camera_cortina.setVisibility(View.VISIBLE);
            icon_gallery_cortina.setVisibility(View.GONE);
        }

        if(!image_cond_serpentin.isEmpty()){
            icon_camera_cond_serpentin.setVisibility(View.GONE);
            icon_gallery_cond_serpentin.setVisibility(View.VISIBLE);
        }else{
            icon_camera_cond_serpentin.setVisibility(View.VISIBLE);
            icon_gallery_cond_serpentin.setVisibility(View.GONE);
        }

        if(!image_ventilador.isEmpty()){
            icon_camera_ventilador.setVisibility(View.GONE);
            icon_gallery_ventilador.setVisibility(View.VISIBLE);
        }else{
            icon_camera_ventilador.setVisibility(View.VISIBLE);
            icon_gallery_ventilador.setVisibility(View.GONE);
        }

        if(!image_carcaza.isEmpty()){
            icon_camera_carcaza.setVisibility(View.GONE);
            icon_gallery_carcaza.setVisibility(View.VISIBLE);
        }else{
            icon_camera_carcaza.setVisibility(View.VISIBLE);
            icon_gallery_carcaza.setVisibility(View.GONE);
        }
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
            String b64 = currentPhotoPath;
            switch (image_type){
                //Antes
                case "1":
                    image_serpentin = Image.convert(BitmapFactory.decodeFile(b64,options));
                    Log.e("image_falla","1");
                    icon_camera_serpentin.setVisibility(View.GONE);
                    icon_gallery_serpentin.setVisibility(View.VISIBLE);
                    if(image_serpentin.isEmpty())
                        System.out.println("image_falla NULL");

                    if(modo_prueba==1){
                        setAllValues();
                    }
                    break;
                case "2":
                    image_bomba = Image.convert(BitmapFactory.decodeFile(b64,options));
                    Log.e("image_bomba","2");
                    icon_camera_bomba.setVisibility(View.GONE);
                    icon_gallery_bomba.setVisibility(View.VISIBLE);
                    if(image_bomba.isEmpty())
                        System.out.println("image_bomba NULL");
                    break;
                case "3":
                    image_cortina = Image.convert(BitmapFactory.decodeFile(b64,options));
                    Log.e("image_cortina","3");
                    icon_camera_cortina.setVisibility(View.GONE);
                    icon_gallery_cortina.setVisibility(View.VISIBLE);
                    if(image_cortina.isEmpty())
                        System.out.println("image_cortina NULL");
                    break;
                case "4":
                    image_cond_serpentin = Image.convert(BitmapFactory.decodeFile(b64,options));
                    Log.e("image_cond_serpentin","5");
                    icon_camera_cond_serpentin.setVisibility(View.GONE);
                    icon_gallery_cond_serpentin.setVisibility(View.VISIBLE);
                    if(image_cond_serpentin.isEmpty())
                        System.out.println("image_cond_serpentin NULL");
                    break;
                case "5":
                    image_ventilador = Image.convert(BitmapFactory.decodeFile(b64,options));
                    Log.e("image_ventilador","6");
                    icon_camera_ventilador.setVisibility(View.GONE);
                    icon_gallery_ventilador.setVisibility(View.VISIBLE);
                    if(image_ventilador.isEmpty())
                        System.out.println("image_ventilador NULL");
                    break;
                case "6":
                    image_carcaza = Image.convert(BitmapFactory.decodeFile(b64,options));
                    Log.e("image_carcaza","7");
                    icon_camera_carcaza.setVisibility(View.GONE);
                    icon_gallery_carcaza.setVisibility(View.VISIBLE);
                    if(image_carcaza.isEmpty())
                        System.out.println("image_carcaza NULL");
                    break;
                //Después
                case "7":
                    despues_image_serpentin = Image.convert(BitmapFactory.decodeFile(b64,options));
                    Log.e("despues_image_serpentin","1");
                    despues_icon_camera_serpentin.setVisibility(View.GONE);
                    despues_icon_gallery_serpentin.setVisibility(View.VISIBLE);
                    if(despues_image_serpentin.isEmpty())
                        System.out.println("despues_image_serpentin NULL");

                    if(modo_prueba==1){
                        setAllValuesDespues();
                    }
                    break;
                case "8":
                    despues_image_bomba = Image.convert(BitmapFactory.decodeFile(b64,options));
                    Log.e("despues_image_bomba","2");
                    despues_icon_camera_bomba.setVisibility(View.GONE);
                    despues_icon_gallery_bomba.setVisibility(View.VISIBLE);
                    if(despues_image_bomba.isEmpty())
                        System.out.println("despues_image_bomba NULL");
                    break;
                case "9":
                    despues_image_cortina = Image.convert(BitmapFactory.decodeFile(b64,options));
                    Log.e("despues_image_cortina","3");
                    despues_icon_camera_cortina.setVisibility(View.GONE);
                    despues_icon_gallery_cortina.setVisibility(View.VISIBLE);
                    if(despues_image_cortina.isEmpty())
                        System.out.println("despues_image_cortina NULL");
                    break;
                case "10":
                    despues_image_cond_serpentin = Image.convert(BitmapFactory.decodeFile(b64,options));
                    Log.e("despues_image_cond_serpentin","5");
                    despues_icon_camera_cond_serpentin.setVisibility(View.GONE);
                    despues_icon_gallery_cond_serpentin.setVisibility(View.VISIBLE);
                    if(despues_image_cond_serpentin.isEmpty())
                        System.out.println("despues_image_cond_serpentin NULL");
                    break;
                case "11":
                    despues_image_ventilador = Image.convert(BitmapFactory.decodeFile(b64,options));
                    Log.e("despues_image_ventilador","6");
                    despues_icon_camera_ventilador.setVisibility(View.GONE);
                    despues_icon_gallery_ventilador.setVisibility(View.VISIBLE);
                    if(despues_image_ventilador.isEmpty())
                        System.out.println("despues_image_ventilador NULL");
                    break;
                case "12":
                    despues_image_carcaza = Image.convert(BitmapFactory.decodeFile(b64,options));
                    Log.e("despues_image_carcaza","7");
                    despues_icon_camera_carcaza.setVisibility(View.GONE);
                    despues_icon_gallery_carcaza.setVisibility(View.VISIBLE);
                    if(despues_image_carcaza.isEmpty())
                        System.out.println("despues_image_carcaza NULL");
                    break;
            }
        } catch (Exception e) {
            viewDialog.hideDialog(1);
            e.printStackTrace();
            System.out.println("galleryPic_error: " + e);
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

    private void pickFromGallery(){
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
                galleryAddPic();
                break;
            case 2://choose from gallery
                //data.getData return the content URI for the selected Image
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
                    String b64 = selectedImage.toString();
                    switch (image_type){
                        //Antes
                        case "1":
                            image_serpentin = Image.convert(bitmap);
                            Log.e("image_falla","1");
                            icon_camera_serpentin.setVisibility(View.GONE);
                            icon_gallery_serpentin.setVisibility(View.VISIBLE);
                            if(image_serpentin.isEmpty())
                                System.out.println("image_serpentin NULL");
                            break;
                        case "2":
                            image_bomba = Image.convert(bitmap);
                            Log.e("image_bomba","2");
                            icon_camera_bomba.setVisibility(View.GONE);
                            icon_gallery_bomba.setVisibility(View.VISIBLE);
                            if(image_bomba.isEmpty())
                                System.out.println("image_tbomba NULL");
                            break;
                        case "3":
                            image_cortina = Image.convert(bitmap);
                            Log.e("image_cortina","3");
                            icon_camera_cortina.setVisibility(View.GONE);
                            icon_gallery_cortina.setVisibility(View.VISIBLE);
                            if(image_cortina.isEmpty())
                                System.out.println("image_cortina NULL");
                            break;
                        case "4":
                            image_cond_serpentin = Image.convert(bitmap);
                            Log.e("image_cond_serpentin","5");
                            icon_camera_cond_serpentin.setVisibility(View.GONE);
                            icon_gallery_cond_serpentin.setVisibility(View.VISIBLE);
                            if(image_cond_serpentin.isEmpty())
                                System.out.println("image_cond_serpentin NULL");
                            break;
                        case "5":
                            image_ventilador = Image.convert(bitmap);
                            Log.e("image_ventilador","6");
                            icon_camera_ventilador.setVisibility(View.GONE);
                            icon_gallery_ventilador.setVisibility(View.VISIBLE);
                            if(image_ventilador.isEmpty())
                                System.out.println("image_ventilador NULL");
                            break;
                        case "6":
                            image_carcaza = Image.convert(bitmap);
                            Log.e("image_carcaza","7");
                            icon_camera_carcaza.setVisibility(View.GONE);
                            icon_gallery_carcaza.setVisibility(View.VISIBLE);
                            if(image_carcaza.isEmpty())
                                System.out.println("image_carcaza NULL");
                            break;
                        //Depues
                        case "7":
                            despues_image_serpentin = Image.convert(bitmap);
                            Log.e("despues_image_falla","1");
                            despues_icon_camera_serpentin.setVisibility(View.GONE);
                            despues_icon_gallery_serpentin.setVisibility(View.VISIBLE);
                            if(despues_image_serpentin.isEmpty())
                                System.out.println("despues_image_serpentin NULL");
                            break;
                        case "8":
                            despues_image_bomba = Image.convert(bitmap);
                            Log.e("despues_image_bomba","2");
                            despues_icon_camera_bomba.setVisibility(View.GONE);
                            despues_icon_gallery_bomba.setVisibility(View.VISIBLE);
                            if(despues_image_bomba.isEmpty())
                                System.out.println("despues_image_tbomba NULL");
                            break;
                        case "9":
                            despues_image_cortina = Image.convert(bitmap);
                            Log.e("despues_image_cortina","3");
                            despues_icon_camera_cortina.setVisibility(View.GONE);
                            despues_icon_gallery_cortina.setVisibility(View.VISIBLE);
                            if(despues_image_cortina.isEmpty())
                                System.out.println("image_cortina NULL");
                            break;
                        case "10":
                            despues_image_cond_serpentin = Image.convert(bitmap);
                            Log.e("despues_image_cond_serpentin","5");
                            despues_icon_camera_cond_serpentin.setVisibility(View.GONE);
                            despues_icon_gallery_cond_serpentin.setVisibility(View.VISIBLE);
                            if(image_cond_serpentin.isEmpty())
                                System.out.println("despues_image_cond_serpentin NULL");
                            break;
                        case "11":
                            despues_image_ventilador = Image.convert(bitmap);
                            Log.e("despues_image_ventilador","6");
                            despues_icon_camera_ventilador.setVisibility(View.GONE);
                            despues_icon_gallery_ventilador.setVisibility(View.VISIBLE);
                            if(despues_image_ventilador.isEmpty())
                                System.out.println("despues_image_ventilador NULL");
                            break;
                        case "12":
                            despues_image_carcaza = Image.convert(bitmap);
                            Log.e("despues_image_carcaza","7");
                            despues_icon_camera_carcaza.setVisibility(View.GONE);
                            despues_icon_gallery_carcaza.setVisibility(View.VISIBLE);
                            if(despues_image_carcaza.isEmpty())
                                System.out.println("despues_image_carcaza NULL");
                            break;
                    }
                } catch (Exception e) {
                    viewDialog.hideDialog(1);
                    e.printStackTrace();
                    System.out.println("getFases_error: " + e);
                }
                break;
        }


    }

    void setAllValues()
    {
        image_bomba =image_serpentin;
        image_cortina =image_serpentin;
        image_cond_serpentin =image_serpentin;
        image_ventilador =image_serpentin;
        image_carcaza =image_serpentin;

        icon_camera_bomba.setVisibility(View.GONE);
        icon_camera_cortina.setVisibility(View.GONE);
        icon_camera_cond_serpentin.setVisibility(View.GONE);
        icon_camera_ventilador.setVisibility(View.GONE);
        icon_camera_carcaza.setVisibility(View.GONE);

        icon_gallery_bomba.setVisibility(View.VISIBLE);
        icon_gallery_cortina.setVisibility(View.VISIBLE);
        icon_gallery_cond_serpentin.setVisibility(View.VISIBLE);
        icon_gallery_ventilador.setVisibility(View.VISIBLE);
        icon_gallery_carcaza.setVisibility(View.VISIBLE);
    }

    void setAllValuesDespues()
    {
        despues_image_bomba = despues_image_serpentin;
        despues_image_cortina = despues_image_serpentin;
        despues_image_cond_serpentin = despues_image_serpentin;
        despues_image_ventilador = despues_image_serpentin;
        despues_image_carcaza = despues_image_serpentin;

        despues_icon_camera_bomba.setVisibility(View.GONE);
        despues_icon_camera_cortina.setVisibility(View.GONE);
        despues_icon_camera_cond_serpentin.setVisibility(View.GONE);
        despues_icon_camera_ventilador.setVisibility(View.GONE);
        despues_icon_camera_carcaza.setVisibility(View.GONE);

        despues_icon_gallery_bomba.setVisibility(View.VISIBLE);
        despues_icon_gallery_cortina.setVisibility(View.VISIBLE);
        despues_icon_gallery_cond_serpentin.setVisibility(View.VISIBLE);
        despues_icon_gallery_ventilador.setVisibility(View.VISIBLE);
        despues_icon_gallery_carcaza.setVisibility(View.VISIBLE);
    }
}
