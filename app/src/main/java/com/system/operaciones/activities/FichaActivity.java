package com.system.operaciones.activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

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

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import jrizani.jrspinner.JRSpinner;

public class FichaActivity extends AppCompatActivity {

    Context ctx;
    Credentials cred;
    //ANTES
    private ImageView icon_camera_falla,icon_camera_presion_baja,icon_camera_presion_alta,icon_camera_temp_l1,icon_camera_temp_l2,icon_camera_temp_l3,icon_camera_amp_l1,icon_camera_amp_l2,icon_camera_amp_l3,icon_camera_volt_l1,icon_camera_volt_l2,icon_camera_volt_l3;
    private ImageView icon_gallery_falla,icon_gallery_presion_baja,icon_gallery_presion_alta,icon_gallery_temp_l1,icon_gallery_temp_l2,icon_gallery_temp_l3,icon_gallery_amp_l1,icon_gallery_amp_l2,icon_gallery_amp_l3,icon_gallery_volt_l1,icon_gallery_volt_l2,icon_gallery_volt_l3;
    String image_falla="",image_presion_baja="",image_presion_alta="",image_temp_l1="",image_temp_l2="",image_temp_l3="",image_amp_l1="",image_amp_l2="",image_amp_l3="",image_volt_l1="",image_volt_l2="",image_volt_l3="",image_signature_tecnico="",image_signature_cliente="";
    private EditText presion_baja,presion_alta,temp_l1,temp_l2,temp_l3,amp_l1,amp_l2,amp_l3,volt_l1,volt_l2,volt_l3;
    //DESPUES
    private ImageView despues_icon_camera_falla,despues_icon_camera_presion_baja,despues_icon_camera_presion_alta,despues_icon_camera_temp_l1,despues_icon_camera_temp_l2,despues_icon_camera_temp_l3,despues_icon_camera_amp_l1,despues_icon_camera_amp_l2,despues_icon_camera_amp_l3,despues_icon_camera_volt_l1,despues_icon_camera_volt_l2,despues_icon_camera_volt_l3;
    private ImageView despues_icon_gallery_falla,despues_icon_gallery_presion_baja,despues_icon_gallery_presion_alta,despues_icon_gallery_temp_l1,despues_icon_gallery_temp_l2,despues_icon_gallery_temp_l3,despues_icon_gallery_amp_l1,despues_icon_gallery_amp_l2,despues_icon_gallery_amp_l3,despues_icon_gallery_volt_l1,despues_icon_gallery_volt_l2,despues_icon_gallery_volt_l3;
    String despues_image_falla="",despues_image_presion_baja="",despues_image_presion_alta="",despues_image_temp_l1="",despues_image_temp_l2="",despues_image_temp_l3="",despues_image_amp_l1="",despues_image_amp_l2="",despues_image_amp_l3="",despues_image_volt_l1="",despues_image_volt_l2="",despues_image_volt_l3="",despues_image_signature_tecnico="",despues_image_signature_cliente="";
    private EditText despues_presion_baja,despues_presion_alta,despues_temp_l1,despues_temp_l2,despues_temp_l3,despues_amp_l1,despues_amp_l2,despues_amp_l3,despues_volt_l1,despues_volt_l2,despues_volt_l3;

    private Button btn_lectura_cancelar;
    private Button btn_lectura_guardar;
    private String id,tienda_id;

    AlertDialog alertDialog;
    JRSpinner spinner_motivos;
    String[] motivos_id;

    JRSpinner spinner_diagnosticos;
    String[] diagnosticos_id;

    String solucion;
    TextView tv_solucion;
    EditText et_diagnostico;

    String motivo_id;
    String diagnostico_id;

    private String name_tecnico,dni_tecnico,cargo_tecnico,name_cliente,dni_cliente,cargo_cliente;

    int equipo_count;
    int tipo_nro_serie = 1;
    private static final int CODIGO_PERMISOS_CAMARA = 1, CODIGO_INTENT = 2;
    private boolean permisoCamaraConcedido = false, permisoSolicitadoDesdeBoton = false;

    ViewDialog viewDialog;

    public void showCustomLoadingDialog(View view) {

        //..show gif
        viewDialog.showDialog();

        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                //...here i'm waiting 5 seconds before hiding the custom dialog
                //...you can do whenever you want or whenever your work is done
                viewDialog.hideDialog(5);
            }
        }, 5000);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ficha);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Ficha Técnica");

        ctx = this;
        cred = new Credentials(ctx);
        id = getIntent().getStringExtra("urgencia");
        tienda_id = getIntent().getStringExtra("tienda_id");
        getEquipos(tienda_id);
        viewDialog = new ViewDialog(this);
        viewDialog.showDialog();

        verificarYPedirPermisosDeCamara();
        cred.save_data("image_type","0");

        spinner_motivos = findViewById(R.id.spinner_motivo);
        spinner_diagnosticos = findViewById(R.id.spinner_diagnostico);
        et_diagnostico = findViewById(R.id.et_diagnostico);
        getMotivos();
        spinner_motivos.setOnItemClickListener(new JRSpinner.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                motivo_id = motivos_id[position];
                if(motivo_id.equals("6")){
                    et_diagnostico.setVisibility(View.VISIBLE);
                }else{
                    et_diagnostico.setVisibility(View.GONE);
                }
                getDiagnostico(motivo_id);
            }
        });

        spinner_diagnosticos.setOnItemClickListener(new JRSpinner.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                diagnostico_id = diagnosticos_id[position];
                getSolucion(diagnostico_id);
            }
        });
        tv_solucion = findViewById(R.id.tv_solucion);
        Button btn_antes = findViewById(R.id.btn_antes);
        Button btn_despues = findViewById(R.id.btn_despues);
        btn_antes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(ctx);
                LayoutInflater inflater = getLayoutInflater();
                View dialogView = inflater.inflate(R.layout.dialog_lecturas, null);
                builder.setView(dialogView);
                builder.setCancelable(false);
                icon_camera_falla = dialogView.findViewById(R.id.icon_camera_falla);

                icon_camera_presion_baja = dialogView.findViewById(R.id.icon_camera_presion_baja);
                icon_camera_presion_alta = dialogView.findViewById(R.id.icon_camera_presion_alta);

                icon_camera_temp_l1 = dialogView.findViewById(R.id.icon_cam_temp_l1);
                icon_camera_temp_l2 = dialogView.findViewById(R.id.icon_cam_temp_l2);
                icon_camera_temp_l3 = dialogView.findViewById(R.id.icon_cam_temp_l3);

                icon_camera_amp_l1 = dialogView.findViewById(R.id.icon_cam_amp_l1);
                icon_camera_amp_l2 = dialogView.findViewById(R.id.icon_cam_amp_l2);
                icon_camera_amp_l3 = dialogView.findViewById(R.id.icon_cam_amp_l3);

                icon_camera_volt_l1 = dialogView.findViewById(R.id.icon_cam_volt_l1);
                icon_camera_volt_l2 = dialogView.findViewById(R.id.icon_cam_volt_l2);
                icon_camera_volt_l3 = dialogView.findViewById(R.id.icon_cam_volt_l3);

                icon_gallery_falla = dialogView.findViewById(R.id.icon_gallery_falla);

                icon_gallery_presion_baja = dialogView.findViewById(R.id.icon_gallery_presion_baja);
                icon_gallery_presion_alta = dialogView.findViewById(R.id.icon_gallery_presion_alta);

                icon_gallery_temp_l1 = dialogView.findViewById(R.id.icon_gallery_temp_l1);
                icon_gallery_temp_l2 = dialogView.findViewById(R.id.icon_gallery_temp_l2);
                icon_gallery_temp_l3 = dialogView.findViewById(R.id.icon_gallery_temp_l3);

                icon_gallery_amp_l1 = dialogView.findViewById(R.id.icon_gallery_amp_l1);
                icon_gallery_amp_l2 = dialogView.findViewById(R.id.icon_gallery_amp_l2);
                icon_gallery_amp_l3 = dialogView.findViewById(R.id.icon_gallery_amp_l3);

                icon_gallery_volt_l1 = dialogView.findViewById(R.id.icon_gallery_volt_l1);
                icon_gallery_volt_l2 = dialogView.findViewById(R.id.icon_gallery_volt_l2);
                icon_gallery_volt_l3 = dialogView.findViewById(R.id.icon_gallery_volt_l3);

                presion_baja = dialogView.findViewById(R.id.et_presion_baja);
                presion_alta = dialogView.findViewById(R.id.et_presion_alta);

                temp_l1 = dialogView.findViewById(R.id.et_temperatura_l1);
                temp_l2 = dialogView.findViewById(R.id.et_temperatura_l2);
                temp_l3 = dialogView.findViewById(R.id.et_temperatura_l3);

                amp_l1 = dialogView.findViewById(R.id.et_amperaje_l1);
                amp_l2 = dialogView.findViewById(R.id.et_amperaje_l2);
                amp_l3 = dialogView.findViewById(R.id.et_amperaje_l3);

                volt_l1 = dialogView.findViewById(R.id.et_voltaje_l1);
                volt_l2 = dialogView.findViewById(R.id.et_voltaje_l2);
                volt_l3 = dialogView.findViewById(R.id.et_voltaje_l3);

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

                icon_camera_falla.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Log.e("falla","23");
                        cred.save_data("image_type","23");
                        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        startActivityForResult(intent, 1);
                    }
                });

                icon_gallery_falla.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showPreview(image_falla);
                    }
                });

                icon_camera_presion_baja.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Log.e("presion_baja","1");
                        cred.save_data("image_type","1");
                        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        startActivityForResult(intent, 1);
                    }
                });

                icon_gallery_presion_baja.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showPreview(image_presion_baja);
                    }
                });

                icon_camera_presion_alta.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        cred.save_data("image_type","2");
                        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        startActivityForResult(intent, 1);
                    }
                });

                icon_gallery_presion_alta.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showPreview(image_presion_alta);
                    }
                });

                icon_camera_amp_l1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        cred.save_data("image_type","3");
                        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        startActivityForResult(intent, 1);
                    }
                });

                icon_gallery_amp_l1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showPreview(image_amp_l1);
                    }
                });

                icon_camera_amp_l2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        cred.save_data("image_type","4");
                        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        startActivityForResult(intent, 1);
                    }
                });

                icon_gallery_amp_l2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showPreview(image_amp_l2);
                    }
                });

                icon_camera_amp_l3.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        cred.save_data("image_type","5");
                        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        startActivityForResult(intent, 1);
                    }
                });

                icon_gallery_amp_l3.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showPreview(image_amp_l3);
                    }
                });

                icon_camera_volt_l1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        cred.save_data("image_type","6");
                        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        startActivityForResult(intent, 1);
                    }
                });

                icon_gallery_volt_l1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showPreview(image_volt_l1);
                    }
                });

                icon_camera_volt_l2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        cred.save_data("image_type","7");
                        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        startActivityForResult(intent, 1);
                    }
                });

                icon_gallery_volt_l2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showPreview(image_volt_l2);
                    }
                });

                icon_camera_volt_l3.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        cred.save_data("image_type","8");
                        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        startActivityForResult(intent, 1);
                    }
                });

                icon_gallery_volt_l3.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showPreview(image_volt_l3);
                    }
                });

                icon_camera_temp_l1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        cred.save_data("image_type","9");
                        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        startActivityForResult(intent, 1);
                    }
                });

                icon_gallery_temp_l1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showPreview(image_temp_l1);
                    }
                });

                icon_camera_temp_l2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        cred.save_data("image_type","10");
                        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        startActivityForResult(intent, 1);
                    }
                });

                icon_gallery_temp_l2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showPreview(image_temp_l2);
                    }
                });

                icon_camera_temp_l3.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        cred.save_data("image_type","11");
                        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        startActivityForResult(intent, 1);
                    }
                });

                icon_gallery_temp_l3.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showPreview(image_temp_l3);
                    }
                });

                //Temperatura
                if(!image_temp_l1.isEmpty()){
                    icon_camera_temp_l1.setVisibility(View.GONE);
                    icon_gallery_temp_l1.setVisibility(View.VISIBLE);
                }else{
                    icon_camera_temp_l1.setVisibility(View.VISIBLE);
                    icon_gallery_temp_l1.setVisibility(View.GONE);
                }

                if(!image_temp_l2.isEmpty()){
                    icon_camera_temp_l2.setVisibility(View.GONE);
                    icon_gallery_temp_l2.setVisibility(View.VISIBLE);
                }else{
                    icon_camera_temp_l2.setVisibility(View.VISIBLE);
                    icon_gallery_temp_l2.setVisibility(View.GONE);
                }

                if(!image_temp_l3.isEmpty()){
                    icon_camera_temp_l3.setVisibility(View.GONE);
                    icon_gallery_temp_l3.setVisibility(View.VISIBLE);
                }else{
                    icon_camera_temp_l3.setVisibility(View.VISIBLE);
                    icon_gallery_temp_l3.setVisibility(View.GONE);
                }

                //Presión

                if(!image_presion_baja.isEmpty()){
                    icon_camera_presion_baja.setVisibility(View.GONE);
                    icon_gallery_presion_baja.setVisibility(View.VISIBLE);
                }else{
                    icon_camera_presion_baja.setVisibility(View.VISIBLE);
                    icon_gallery_presion_baja.setVisibility(View.GONE);
                }

                if(!image_presion_alta.isEmpty()){
                    icon_camera_presion_alta.setVisibility(View.GONE);
                    icon_gallery_presion_alta.setVisibility(View.VISIBLE);
                }else{
                    icon_camera_presion_alta.setVisibility(View.VISIBLE);
                    icon_gallery_presion_alta.setVisibility(View.GONE);
                }

                //Amperaje
                if(!image_amp_l1.isEmpty()){
                    icon_camera_amp_l1.setVisibility(View.GONE);
                    icon_gallery_amp_l1.setVisibility(View.VISIBLE);
                }else{
                    icon_camera_amp_l1.setVisibility(View.VISIBLE);
                    icon_gallery_amp_l1.setVisibility(View.GONE);
                }

                if(!image_amp_l2.isEmpty()){
                    icon_camera_amp_l2.setVisibility(View.GONE);
                    icon_gallery_amp_l2.setVisibility(View.VISIBLE);
                }else{
                    icon_camera_amp_l2.setVisibility(View.VISIBLE);
                    icon_gallery_amp_l2.setVisibility(View.GONE);
                }

                if(!image_amp_l3.isEmpty()){
                    icon_camera_amp_l3.setVisibility(View.GONE);
                    icon_gallery_amp_l3.setVisibility(View.VISIBLE);
                }else{
                    icon_camera_amp_l3.setVisibility(View.VISIBLE);
                    icon_gallery_amp_l3.setVisibility(View.GONE);
                }

                //Voltaje
                if(!image_volt_l1.isEmpty()){
                    icon_camera_volt_l1.setVisibility(View.GONE);
                    icon_gallery_volt_l1.setVisibility(View.VISIBLE);
                }else{
                    icon_camera_volt_l1.setVisibility(View.VISIBLE);
                    icon_gallery_volt_l1.setVisibility(View.GONE);
                }

                if(!image_volt_l2.isEmpty()){
                    icon_camera_volt_l2.setVisibility(View.GONE);
                    icon_gallery_volt_l2.setVisibility(View.VISIBLE);
                }else{
                    icon_camera_volt_l2.setVisibility(View.VISIBLE);
                    icon_gallery_volt_l2.setVisibility(View.GONE);
                }

                if(!image_volt_l3.isEmpty()){
                    icon_camera_volt_l3.setVisibility(View.GONE);
                    icon_gallery_volt_l3.setVisibility(View.VISIBLE);
                }else{
                    icon_camera_volt_l3.setVisibility(View.VISIBLE);
                    icon_gallery_volt_l3.setVisibility(View.GONE);
                }
            }
        });

        //BTN_DESPUES
        btn_despues.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(ctx);
                LayoutInflater inflater = getLayoutInflater();
                View dialogView = inflater.inflate(R.layout.dialog_lecturas, null);
                builder.setView(dialogView);
                builder.setCancelable(false);

                despues_icon_camera_falla = dialogView.findViewById(R.id.icon_camera_falla);

                despues_icon_camera_presion_baja = dialogView.findViewById(R.id.icon_camera_presion_baja);
                despues_icon_camera_presion_alta = dialogView.findViewById(R.id.icon_camera_presion_alta);

                despues_icon_camera_temp_l1 = dialogView.findViewById(R.id.icon_cam_temp_l1);
                despues_icon_camera_temp_l2 = dialogView.findViewById(R.id.icon_cam_temp_l2);
                despues_icon_camera_temp_l3 = dialogView.findViewById(R.id.icon_cam_temp_l3);

                despues_icon_camera_amp_l1 = dialogView.findViewById(R.id.icon_cam_amp_l1);
                despues_icon_camera_amp_l2 = dialogView.findViewById(R.id.icon_cam_amp_l2);
                despues_icon_camera_amp_l3 = dialogView.findViewById(R.id.icon_cam_amp_l3);

                despues_icon_camera_volt_l1 = dialogView.findViewById(R.id.icon_cam_volt_l1);
                despues_icon_camera_volt_l2 = dialogView.findViewById(R.id.icon_cam_volt_l2);
                despues_icon_camera_volt_l3 = dialogView.findViewById(R.id.icon_cam_volt_l3);

                despues_icon_gallery_falla = dialogView.findViewById(R.id.icon_gallery_falla);

                despues_icon_gallery_presion_baja = dialogView.findViewById(R.id.icon_gallery_presion_baja);
                despues_icon_gallery_presion_alta = dialogView.findViewById(R.id.icon_gallery_presion_alta);

                despues_icon_gallery_temp_l1 = dialogView.findViewById(R.id.icon_gallery_temp_l1);
                despues_icon_gallery_temp_l2 = dialogView.findViewById(R.id.icon_gallery_temp_l2);
                despues_icon_gallery_temp_l3 = dialogView.findViewById(R.id.icon_gallery_temp_l3);

                despues_icon_gallery_amp_l1 = dialogView.findViewById(R.id.icon_gallery_amp_l1);
                despues_icon_gallery_amp_l2 = dialogView.findViewById(R.id.icon_gallery_amp_l2);
                despues_icon_gallery_amp_l3 = dialogView.findViewById(R.id.icon_gallery_amp_l3);

                despues_icon_gallery_volt_l1 = dialogView.findViewById(R.id.icon_gallery_volt_l1);
                despues_icon_gallery_volt_l2 = dialogView.findViewById(R.id.icon_gallery_volt_l2);
                despues_icon_gallery_volt_l3 = dialogView.findViewById(R.id.icon_gallery_volt_l3);

                despues_presion_baja = dialogView.findViewById(R.id.et_presion_baja);
                despues_presion_alta = dialogView.findViewById(R.id.et_presion_alta);

                despues_temp_l1 = dialogView.findViewById(R.id.et_temperatura_l1);
                despues_temp_l2 = dialogView.findViewById(R.id.et_temperatura_l2);
                despues_temp_l3 = dialogView.findViewById(R.id.et_temperatura_l3);

                despues_amp_l1 = dialogView.findViewById(R.id.et_amperaje_l1);
                despues_amp_l2 = dialogView.findViewById(R.id.et_amperaje_l2);
                despues_amp_l3 = dialogView.findViewById(R.id.et_amperaje_l3);

                despues_volt_l1 = dialogView.findViewById(R.id.et_voltaje_l1);
                despues_volt_l2 = dialogView.findViewById(R.id.et_voltaje_l2);
                despues_volt_l3 = dialogView.findViewById(R.id.et_voltaje_l3);

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

                despues_icon_camera_falla.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Log.e("falla","24");
                        cred.save_data("image_type","24");
                        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        startActivityForResult(intent, 1);
                    }
                });

                despues_icon_gallery_falla.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showPreview(despues_image_falla);
                    }
                });

                despues_icon_camera_presion_baja.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Log.e("presion_baja","12");
                        cred.save_data("image_type","12");
                        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        startActivityForResult(intent, 1);
                    }
                });

                despues_icon_gallery_presion_baja.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showPreview(despues_image_presion_baja);
                    }
                });

                despues_icon_camera_presion_alta.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        cred.save_data("image_type","13");
                        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        startActivityForResult(intent, 1);
                    }
                });

                despues_icon_gallery_presion_alta.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showPreview(despues_image_presion_alta);
                    }
                });

                despues_icon_camera_amp_l1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        cred.save_data("image_type","14");
                        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        startActivityForResult(intent, 1);
                    }
                });

                despues_icon_gallery_amp_l1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showPreview(despues_image_amp_l1);
                    }
                });

                despues_icon_camera_amp_l2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        cred.save_data("image_type","15");
                        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        startActivityForResult(intent, 1);
                    }
                });

                despues_icon_gallery_amp_l2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showPreview(despues_image_amp_l2);
                    }
                });

                despues_icon_camera_amp_l3.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        cred.save_data("image_type","16");
                        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        startActivityForResult(intent, 1);
                    }
                });

                despues_icon_gallery_amp_l3.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showPreview(despues_image_amp_l3);
                    }
                });

                despues_icon_camera_volt_l1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        cred.save_data("image_type","17");
                        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        startActivityForResult(intent, 1);
                    }
                });

                despues_icon_gallery_volt_l1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showPreview(despues_image_volt_l1);
                    }
                });

                despues_icon_camera_volt_l2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        cred.save_data("image_type","18");
                        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        startActivityForResult(intent, 1);
                    }
                });

                despues_icon_gallery_volt_l2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showPreview(despues_image_volt_l2);
                    }
                });

                despues_icon_camera_volt_l3.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        cred.save_data("image_type","19");
                        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        startActivityForResult(intent, 1);
                    }
                });

                despues_icon_gallery_volt_l3.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showPreview(despues_image_volt_l3);
                    }
                });

                despues_icon_camera_temp_l1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        cred.save_data("image_type","20");
                        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        startActivityForResult(intent, 1);
                    }
                });

                despues_icon_gallery_temp_l1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showPreview(despues_image_temp_l1);
                    }
                });

                despues_icon_camera_temp_l2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        cred.save_data("image_type","21");
                        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        startActivityForResult(intent, 1);
                    }
                });

                despues_icon_gallery_temp_l2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showPreview(despues_image_temp_l2);
                    }
                });

                despues_icon_camera_temp_l3.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        cred.save_data("image_type","22");
                        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        startActivityForResult(intent, 1);
                    }
                });

                despues_icon_gallery_temp_l3.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showPreview(despues_image_temp_l3);
                    }
                });

                //Temperatura
                if(!despues_image_temp_l1.isEmpty()){
                    despues_icon_camera_temp_l1.setVisibility(View.GONE);
                    despues_icon_gallery_temp_l1.setVisibility(View.VISIBLE);
                }else{
                    despues_icon_camera_temp_l1.setVisibility(View.VISIBLE);
                    despues_icon_gallery_temp_l1.setVisibility(View.GONE);
                }

                if(!despues_image_temp_l2.isEmpty()){
                    despues_icon_camera_temp_l2.setVisibility(View.GONE);
                    despues_icon_gallery_temp_l2.setVisibility(View.VISIBLE);
                }else{
                    despues_icon_camera_temp_l2.setVisibility(View.VISIBLE);
                    despues_icon_gallery_temp_l2.setVisibility(View.GONE);
                }

                if(!despues_image_temp_l3.isEmpty()){
                    despues_icon_camera_temp_l3.setVisibility(View.GONE);
                    despues_icon_gallery_temp_l3.setVisibility(View.VISIBLE);
                }else{
                    despues_icon_camera_temp_l3.setVisibility(View.VISIBLE);
                    despues_icon_gallery_temp_l3.setVisibility(View.GONE);
                }

                //Presión

                if(!despues_image_presion_baja.isEmpty()){
                    despues_icon_camera_presion_baja.setVisibility(View.GONE);
                    despues_icon_gallery_presion_baja.setVisibility(View.VISIBLE);
                }else{
                    despues_icon_camera_presion_baja.setVisibility(View.VISIBLE);
                    despues_icon_gallery_presion_baja.setVisibility(View.GONE);
                }

                if(!despues_image_presion_alta.isEmpty()){
                    despues_icon_camera_presion_alta.setVisibility(View.GONE);
                    despues_icon_gallery_presion_alta.setVisibility(View.VISIBLE);
                }else{
                    despues_icon_camera_presion_alta.setVisibility(View.VISIBLE);
                    despues_icon_gallery_presion_alta.setVisibility(View.GONE);
                }

                //Amperaje
                if(!despues_image_amp_l1.isEmpty()){
                    despues_icon_camera_amp_l1.setVisibility(View.GONE);
                    despues_icon_gallery_amp_l1.setVisibility(View.VISIBLE);
                }else{
                    despues_icon_camera_amp_l1.setVisibility(View.VISIBLE);
                    despues_icon_gallery_amp_l1.setVisibility(View.GONE);
                }

                if(!despues_image_amp_l2.isEmpty()){
                    despues_icon_camera_amp_l2.setVisibility(View.GONE);
                    despues_icon_gallery_amp_l2.setVisibility(View.VISIBLE);
                }else{
                    despues_icon_camera_amp_l2.setVisibility(View.VISIBLE);
                    despues_icon_gallery_amp_l2.setVisibility(View.GONE);
                }

                if(!despues_image_amp_l3.isEmpty()){
                    despues_icon_camera_amp_l3.setVisibility(View.GONE);
                    despues_icon_gallery_amp_l3.setVisibility(View.VISIBLE);
                }else{
                    despues_icon_camera_amp_l3.setVisibility(View.VISIBLE);
                    despues_icon_gallery_amp_l3.setVisibility(View.GONE);
                }

                //Voltaje
                if(!despues_image_volt_l1.isEmpty()){
                    despues_icon_camera_volt_l1.setVisibility(View.GONE);
                    despues_icon_gallery_volt_l1.setVisibility(View.VISIBLE);
                }else{
                    despues_icon_camera_volt_l1.setVisibility(View.VISIBLE);
                    despues_icon_gallery_volt_l1.setVisibility(View.GONE);
                }

                if(!despues_image_volt_l2.isEmpty()){
                    despues_icon_camera_volt_l2.setVisibility(View.GONE);
                    despues_icon_gallery_volt_l2.setVisibility(View.VISIBLE);
                }else{
                    despues_icon_camera_volt_l2.setVisibility(View.VISIBLE);
                    despues_icon_gallery_volt_l2.setVisibility(View.GONE);
                }

                if(!despues_image_volt_l3.isEmpty()){
                    despues_icon_camera_volt_l3.setVisibility(View.GONE);
                    despues_icon_gallery_volt_l3.setVisibility(View.VISIBLE);
                }else{
                    despues_icon_camera_volt_l3.setVisibility(View.VISIBLE);
                    despues_icon_gallery_volt_l3.setVisibility(View.GONE);
                }
            }
        });

        Button btn_registrar = findViewById(R.id.btn_registrar);
        Button btn_cerrar = findViewById(R.id.btn_cerrar);
        btn_cerrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((FichaActivity)ctx).finish();
            }
        });
        Button btn_firma_tecnico = findViewById(R.id.btn_firma_tecnico);
        Button btn_firma_cliente = findViewById(R.id.btn_firma_cliente);

        btn_registrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(image_temp_l1.isEmpty()){
                    Toast.makeText(ctx,"Debe cargar foto antes de la reparación de temperatura T1",Toast.LENGTH_SHORT).show();
                    return;
                }
                if(image_temp_l2.isEmpty()){
                    Toast.makeText(ctx,"Debe cargar foto antes de la reparación de temperatura T2",Toast.LENGTH_SHORT).show();
                    return;
                }
                if(image_temp_l3.isEmpty()){
                    Toast.makeText(ctx,"Debe cargar foto antes de la reparación de temperatura T3",Toast.LENGTH_SHORT).show();
                    return;
                }
                if(image_presion_baja.isEmpty())
                {
                    Toast.makeText(ctx,"Debe cargar foto antes de la reparación de presión baja",Toast.LENGTH_SHORT).show();
                    return;
                }
                if(image_presion_alta.isEmpty())
                {
                    Toast.makeText(ctx,"Debe cargar foto antes de la reparación de presión alta",Toast.LENGTH_SHORT).show();
                    return;
                }
                if(image_amp_l1.isEmpty())
                {
                    Toast.makeText(ctx,"Debe cargar foto antes de la reparación de amperaje en la Línea 1",Toast.LENGTH_SHORT).show();
                    return;
                }
                if(image_amp_l2.isEmpty())
                {
                    Toast.makeText(ctx,"Debe cargar foto antes de la reparación de amperaje en la Línea 2",Toast.LENGTH_SHORT).show();
                    return;
                }
                if(image_amp_l3.isEmpty())
                {
                    Toast.makeText(ctx,"Debe cargar foto antes de la reparación de amperaje en la Línea 3",Toast.LENGTH_SHORT).show();
                    return;
                }

                if(despues_image_temp_l1.isEmpty()){
                    Toast.makeText(ctx,"Debe cargar foto después de la repación de temperatura T1",Toast.LENGTH_SHORT).show();
                    return;
                }
                if(despues_image_temp_l2.isEmpty()){
                    Toast.makeText(ctx,"Debe cargar foto después de la repación de temperatura T2",Toast.LENGTH_SHORT).show();
                    return;
                }
                if(despues_image_temp_l3.isEmpty()){
                    Toast.makeText(ctx,"Debe cargar foto después de la repación de temperatura T3",Toast.LENGTH_SHORT).show();
                    return;
                }
                if(despues_image_presion_baja.isEmpty())
                {
                    Toast.makeText(ctx,"Debe cargar foto después de la repación de presión baja",Toast.LENGTH_SHORT).show();
                    return;
                }
                if(despues_image_presion_alta.isEmpty())
                {
                    Toast.makeText(ctx,"Debe cargar foto después de la repación de presión alta",Toast.LENGTH_SHORT).show();
                    return;
                }
                if(despues_image_amp_l1.isEmpty())
                {
                    Toast.makeText(ctx,"Debe cargar foto después de la repación de amperaje en la Línea 1",Toast.LENGTH_SHORT).show();
                    return;
                }
                if(despues_image_amp_l2.isEmpty())
                {
                    Toast.makeText(ctx,"Debe cargar foto después de la repación de amperaje en la Línea 2",Toast.LENGTH_SHORT).show();
                    return;
                }
                if(despues_image_amp_l3.isEmpty())
                {
                    Toast.makeText(ctx,"Debe cargar foto después de la repación de amperaje en la Línea 3",Toast.LENGTH_SHORT).show();
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
                final EditText name = dialogView.findViewById(R.id.dialog_signature_et_name);
                final EditText dni = dialogView.findViewById(R.id.dialog_signature_et_dni);
                final EditText cargo = dialogView.findViewById(R.id.dialog_signature_et_cargo);
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
                        }
                        if(name.getText().toString().trim().isEmpty()){
                            Toast.makeText(ctx,"Ingrese Nombre y Apellido",Toast.LENGTH_SHORT).show();
                            name.setError("Completar campo");
                        }else{
                            name.setError(null);
                            name_cliente = name.getText().toString().trim();
                        }
                        if(dni.getText().toString().trim().isEmpty() || dni.getText().toString().length()<8 || dni.getText().toString().length()>8){
                            dni.setError("Completar campo");
                            Toast.makeText(ctx,"Ingrese DNI válido",Toast.LENGTH_SHORT).show();
                        }else{
                            dni.setError(null);
                            dni_cliente = dni.getText().toString().trim();
                        }
                        if(cargo.getText().toString().trim().isEmpty()){
                            cargo.setError("Completar campo");
                            Toast.makeText(ctx,"Ingrese Cargo válido",Toast.LENGTH_SHORT).show();
                        }else{
                            cargo.setError(null);
                            cargo_cliente = cargo.getText().toString().trim();
                        }
                        if(!image_signature_cliente.isEmpty() && !name_cliente.isEmpty() && !dni_cliente.isEmpty() && !cargo_cliente.isEmpty())
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
                final EditText name = dialogView.findViewById(R.id.dialog_signature_et_name);
                final EditText dni = dialogView.findViewById(R.id.dialog_signature_et_dni);
                final EditText cargo = dialogView.findViewById(R.id.dialog_signature_et_cargo);
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
    public void getEquipos(final String tienda_id)
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
                                Toast.makeText(ctx, cliente.getDes_error(), Toast.LENGTH_LONG).show();
                            } else {
                                equipos.clear();
                                for(Object o: respuesta){
                                    JSONObject ob = (JSONObject)o;
                                    equipos.add((JSONObject)o);
                                    if(!ob.get("id").equals("0"))
                                        equipo_count+=1;
                                }
                                getEquipo();

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
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                50000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        // Add the request to the RequestQueue.
        queue.add(stringRequest);
    }

    private void showPreview(String b64)
    {
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
        AlertDialog alert_preview = builder.create();
        alert_preview.show();
    }

    @Override
    public boolean onSupportNavigateUp()
    {
        onBackPressed();
        return true;
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        System.out.println("requestCode: "+requestCode);
        System.out.println("resultCode: "+resultCode);
        if (resultCode == RESULT_CANCELED) {
            return;
        }
        if (resultCode  == RESULT_OK) {
            String image_type = cred.getData("image_type");
            String b64 = "";
            if(!image_type.equals("0")){
                Bitmap image = (Bitmap) data.getExtras().get("data");
                 b64= Image.convert(image);
            }
            switch (image_type){
                case "0":
                    String codigo = data.getStringExtra("codigo");
                    if(tipo_nro_serie==1)
                        et_nro_serie.setText(codigo);
                    else
                        et_cond_nro_serie.setText(codigo);
                    break;
                case "1":
                    image_presion_baja = b64;
                    Log.e("image_presion_baja","1");
                    icon_camera_presion_baja.setVisibility(View.GONE);
                    icon_gallery_presion_baja.setVisibility(View.VISIBLE);
                    if(image_presion_baja.isEmpty())
                        System.out.println("image_presion_baja NULL");
                    break;
                case "2":
                    image_presion_alta = b64;
                    Log.e("image_presion_alta","2");
                    icon_camera_presion_alta.setVisibility(View.GONE);
                    icon_gallery_presion_alta.setVisibility(View.VISIBLE);
                    if(image_presion_alta.isEmpty())
                        System.out.println("image_presion_alta NULL");
                    break;
                case "3":
                    image_amp_l1 = b64;
                    Log.e("image_amp_l1","3");
                    icon_camera_amp_l1.setVisibility(View.GONE);
                    icon_gallery_amp_l1.setVisibility(View.VISIBLE);
                    if(image_amp_l1.isEmpty())
                        System.out.println("image_amp_l1 NULL");
                    break;
                case "4":
                    image_amp_l2 = b64;
                    Log.e("image_amp_l2","4");
                    icon_camera_amp_l2.setVisibility(View.GONE);
                    icon_gallery_amp_l2.setVisibility(View.VISIBLE);
                    if(image_amp_l2.isEmpty())
                        System.out.println("image_amp_l2 NULL");
                    break;
                case "5":
                    image_amp_l3 = b64;
                    Log.e("image_amp_l3","5");
                    icon_camera_amp_l3.setVisibility(View.GONE);
                    icon_gallery_amp_l3.setVisibility(View.VISIBLE);
                    if(image_amp_l3.isEmpty())
                        System.out.println("image_amp_l3 NULL");
                    break;
                case "6":
                    image_volt_l1 = b64;
                    Log.e("image_volt_l1","6");
                    icon_camera_volt_l1.setVisibility(View.GONE);
                    icon_gallery_volt_l1.setVisibility(View.VISIBLE);
                    if(image_volt_l1.isEmpty())
                        System.out.println("image_volt_l1 NULL");
                    break;
                case "7":
                    image_volt_l2 = b64;
                    Log.e("image_volt_l2","7");
                    icon_camera_volt_l2.setVisibility(View.GONE);
                    icon_gallery_volt_l2.setVisibility(View.VISIBLE);
                    if(image_volt_l2.isEmpty())
                        System.out.println("image_volt_l2 NULL");
                    break;
                case "8":
                    image_volt_l3 = b64;
                    Log.e("image_volt_l3","8");
                    icon_camera_volt_l3.setVisibility(View.GONE);
                    icon_gallery_volt_l3.setVisibility(View.VISIBLE);
                    if(image_volt_l3.isEmpty())
                        System.out.println("image_volt_l3 NULL");
                    break;
                case "9":
                    image_temp_l1 = b64;
                    Log.e("image_temp_l1","9");
                    icon_camera_temp_l1.setVisibility(View.GONE);
                    icon_gallery_temp_l1.setVisibility(View.VISIBLE);
                    if(image_temp_l1.isEmpty())
                        System.out.println("image_temp_l1 NULL");
                    break;
                case "10":
                    image_temp_l2 = b64;
                    Log.e("image_temp_l2","10");
                    icon_camera_temp_l2.setVisibility(View.GONE);
                    icon_gallery_temp_l2.setVisibility(View.VISIBLE);
                    if(image_temp_l2.isEmpty())
                        System.out.println("image_temp_l2 NULL");
                    break;
                case "11":
                    image_temp_l3 = b64;
                    Log.e("image_temp_l3","11");
                    icon_camera_temp_l3.setVisibility(View.GONE);
                    icon_gallery_temp_l3.setVisibility(View.VISIBLE);
                    if(image_temp_l3.isEmpty())
                        System.out.println("image_temp_l3 NULL");
                    break;
                    //despues
                case "12":
                    despues_image_presion_baja = b64;
                    Log.e("image_presion_baja","12");
                    despues_icon_camera_presion_baja.setVisibility(View.GONE);
                    despues_icon_gallery_presion_baja.setVisibility(View.VISIBLE);
                    if(despues_image_presion_baja.isEmpty())
                        System.out.println("despues_image_presion_baja NULL");
                    break;
                case "13":
                    despues_image_presion_alta = b64;
                    Log.e("despues_image_presion_alta","13");
                    despues_icon_camera_presion_alta.setVisibility(View.GONE);
                    despues_icon_gallery_presion_alta.setVisibility(View.VISIBLE);
                    if(despues_image_presion_alta.isEmpty())
                        System.out.println("despues_image_presion_alta NULL");
                    break;
                case "14":
                    despues_image_amp_l1 = b64;
                    Log.e("image_amp_l1","14");
                    despues_icon_camera_amp_l1.setVisibility(View.GONE);
                    despues_icon_gallery_amp_l1.setVisibility(View.VISIBLE);
                    if(despues_image_amp_l1.isEmpty())
                        System.out.println("image_amp_l1 NULL");
                    break;
                case "15":
                    despues_image_amp_l2 = b64;
                    Log.e("despues_image_amp_l2","15");
                    despues_icon_camera_amp_l2.setVisibility(View.GONE);
                    despues_icon_gallery_amp_l2.setVisibility(View.VISIBLE);
                    if(despues_image_amp_l2.isEmpty())
                        System.out.println("despues_image_amp_l2 NULL");
                    break;
                case "16":
                    despues_image_amp_l3 = b64;
                    Log.e("despues_image_amp_l3","16");
                    despues_icon_camera_amp_l3.setVisibility(View.GONE);
                    despues_icon_gallery_amp_l3.setVisibility(View.VISIBLE);
                    if(despues_image_amp_l3.isEmpty())
                        System.out.println("despues_image_amp_l3 NULL");
                    break;
                case "17":
                    despues_image_volt_l1 = b64;
                    Log.e("despues_image_volt_l1","17");
                    despues_icon_camera_volt_l1.setVisibility(View.GONE);
                    despues_icon_gallery_volt_l1.setVisibility(View.VISIBLE);
                    if(despues_image_volt_l1.isEmpty())
                        System.out.println("despues_image_volt_l1 NULL");
                    break;
                case "18":
                    despues_image_volt_l2 = b64;
                    Log.e("despues_image_volt_l2","18");
                    despues_icon_camera_volt_l2.setVisibility(View.GONE);
                    despues_icon_gallery_volt_l2.setVisibility(View.VISIBLE);
                    if(despues_image_volt_l2.isEmpty())
                        System.out.println("despues_image_volt_l2 NULL");
                    break;
                case "19":
                    despues_image_volt_l3 = b64;
                    Log.e("despues_image_volt_l3","19");
                    despues_icon_camera_volt_l3.setVisibility(View.GONE);
                    despues_icon_gallery_volt_l3.setVisibility(View.VISIBLE);
                    if(despues_image_volt_l3.isEmpty())
                        System.out.println("despues_image_volt_l3 NULL");
                    break;
                case "20":
                    despues_image_temp_l1 = b64;
                    Log.e("despues_image_temp_l1","20");
                    despues_icon_camera_temp_l1.setVisibility(View.GONE);
                    despues_icon_gallery_temp_l1.setVisibility(View.VISIBLE);
                    if(despues_image_temp_l1.isEmpty())
                        System.out.println("despues_image_temp_l1 NULL");
                    break;
                case "21":
                    despues_image_temp_l2 = b64;
                    Log.e("despues_image_temp_l2","21");
                    despues_icon_camera_temp_l2.setVisibility(View.GONE);
                    despues_icon_gallery_temp_l2.setVisibility(View.VISIBLE);
                    if(despues_image_temp_l2.isEmpty())
                        System.out.println("despues_image_temp_l2 NULL");
                    break;
                case "22":
                    despues_image_temp_l3 = b64;
                    Log.e("despues_image_temp_l3","22");
                    despues_icon_camera_temp_l3.setVisibility(View.GONE);
                    despues_icon_gallery_temp_l3.setVisibility(View.VISIBLE);
                    if(despues_image_temp_l3.isEmpty())
                        System.out.println("despues_image_temp_l3 NULL");
                    break;
                case "23":
                    image_falla = b64;
                    Log.e("image_falla","23");
                    icon_camera_falla.setVisibility(View.GONE);
                    icon_gallery_falla.setVisibility(View.VISIBLE);
                    if(image_falla.isEmpty())
                        System.out.println("image_falla NULL");
                    break;
                case "24":
                    despues_image_falla = b64;
                    Log.e("despues_image_falla","24");
                    despues_icon_camera_falla.setVisibility(View.GONE);
                    despues_icon_gallery_falla.setVisibility(View.VISIBLE);
                    if(despues_image_falla.isEmpty())
                        System.out.println("despues_image_falla NULL");
                    break;
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults)
    {
        if (requestCode == CODIGO_PERMISOS_CAMARA) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Escanear directamten solo si fue pedido desde el botón
                if (permisoSolicitadoDesdeBoton) {
                    escanear();
                }
                permisoCamaraConcedido = true;
            } else {
                permisoDeCamaraDenegado();
            }
        }
    }

    private void registrarFicha(final String urgencia_id)
    {
        viewDialog.showDialog();
        String url=ctx.getApplicationContext().getString(R.string.base_url)+ctx.getApplicationContext().getString(R.string.register_ficha_urgencia_url);
        RequestQueue queue = Volley.newRequestQueue(ctx);

        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        System.out.println("register_ficha_urgencia_response: " + response);
                        try {
                            RespuestaResponse cliente = new Gson().fromJson(response, RespuestaResponse.class);
                            JSONParser parser = new JSONParser();
                            JSONArray respuesta = (JSONArray) parser.parse((String) cliente.getRespuesta());

                            if (cliente.getIde_error() == 0) {
                                Toast.makeText(ctx, cliente.getDes_error(), Toast.LENGTH_LONG).show();
                            } else {
                                Toast.makeText(ctx,"Registro correcto",Toast.LENGTH_SHORT).show();
                                createFicha(urgencia_id);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                System.out.println("register_ficha_urgencia_error: " + error);
            }
        }){
            @Override
            protected Map<String, String> getParams()
            {
                Map<String, String> params = new HashMap<>();
                params.put("urgencia_id", urgencia_id);
                params.put("tienda_id", tienda_id);

                //MOTIVO DIAGNOSTICO SOLUCION
                params.put("motivo",motivo_id);
                params.put("diagnostico",diagnostico_id);

                //ANTES
                params.put("falla", image_falla);
                params.put("presion_baja", presion_baja.getText().toString());
                params.put("img_presion_baja", image_presion_baja);
                params.put("presion_alta", presion_alta.getText().toString());
                params.put("img_presion_alta", image_presion_alta);

                params.put("amperaje_l1", amp_l1.getText().toString());
                params.put("img_amperaje_l1", image_amp_l1);
                params.put("amperaje_l2", amp_l2.getText().toString());
                params.put("img_amperaje_l2", image_amp_l2);
                params.put("amperaje_l3", amp_l3.getText().toString());
                params.put("img_amperaje_l3", image_amp_l3);

                params.put("voltaje_l1", volt_l1.getText().toString());
                params.put("img_voltaje_l1", image_volt_l1);
                params.put("voltaje_l2", volt_l2.getText().toString());
                params.put("img_voltaje_l2", image_volt_l2);
                params.put("voltaje_l3", volt_l3.getText().toString());
                params.put("img_voltaje_l3", image_volt_l3);

                params.put("temperatura_l1", temp_l1.getText().toString());
                params.put("img_temperatura_l1", image_temp_l1);
                params.put("temperatura_l2", temp_l2.getText().toString());
                params.put("img_temperatura_l2", image_temp_l2);
                params.put("temperatura_l3", temp_l3.getText().toString());
                params.put("img_temperatura_l3", image_temp_l3);
                //DESPUES
                params.put("despues_falla", despues_image_falla);
                params.put("despues_presion_baja", despues_presion_baja.getText().toString());
                params.put("despues_img_presion_baja", despues_image_presion_baja);
                params.put("despues_presion_alta", despues_presion_alta.getText().toString());
                params.put("despues_img_presion_alta", despues_image_presion_alta);

                params.put("despues_amperaje_l1", despues_amp_l1.getText().toString());
                params.put("despues_img_amperaje_l1", despues_image_amp_l1);
                params.put("despues_amperaje_l2", despues_amp_l2.getText().toString());
                params.put("despues_img_amperaje_l2", despues_image_amp_l2);
                params.put("despues_amperaje_l3", despues_amp_l3.getText().toString());
                params.put("despues_img_amperaje_l3", despues_image_amp_l3);

                params.put("despues_voltaje_l1", despues_volt_l1.getText().toString());
                params.put("despues_img_voltaje_l1", despues_image_volt_l1);
                params.put("despues_voltaje_l2", despues_volt_l2.getText().toString());
                params.put("despues_img_voltaje_l2", despues_image_volt_l2);
                params.put("despues_voltaje_l3", despues_volt_l3.getText().toString());
                params.put("despues_img_voltaje_l3", despues_image_volt_l3);

                params.put("despues_temperatura_l1", despues_temp_l1.getText().toString());
                params.put("despues_img_temperatura_l1", despues_image_temp_l1);
                params.put("despues_temperatura_l2", despues_temp_l2.getText().toString());
                params.put("despues_img_temperatura_l2", despues_image_temp_l2);
                params.put("despues_temperatura_l3", despues_temp_l3.getText().toString());
                params.put("despues_img_temperatura_l3", despues_image_temp_l3);

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
        String url=ctx.getApplicationContext().getString(R.string.base_url)+ctx.getApplicationContext().getString(R.string.crear_ficha_url);
        RequestQueue queue = Volley.newRequestQueue(ctx);

        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        System.out.println("create_ficha_urgencia_response: " + response);
                        try {
                            RespuestaResponse cliente = new Gson().fromJson(response, RespuestaResponse.class);
                            JSONParser parser = new JSONParser();
                            JSONArray respuesta = (JSONArray) parser.parse((String) cliente.getRespuesta());

                            if (cliente.getIde_error() == 0) {
                                Toast.makeText(ctx, cliente.getDes_error(), Toast.LENGTH_LONG).show();
                            } else {
                                Toast.makeText(ctx,"Ficha creada correctamente",Toast.LENGTH_SHORT).show();
                                viewDialog.hideDialog(5);
                                new Handler().postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        new Utils().sendFicha(tienda_id,urgencia_id,ctx);
                                        ((FichaActivity)ctx).finish();
                                    }
                                }, 5000);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                System.out.println("create_ficha_urgencia_error: " + error);
            }
        }){
            @Override
            protected Map<String, String> getParams()
            {
                Map<String, String> params = new HashMap<>();
                params.put("urgencia_id", urgencia_id);
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

    private void getMotivos()
    {
        String url=ctx.getApplicationContext().getString(R.string.base_url)+ctx.getApplicationContext().getString(R.string.getMotivos_url);
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
                                String[] motivos = new String[respuesta.size()];
                                motivos_id = new String[respuesta.size()];
                                int i=0;
                                for (Object o : respuesta)
                                {
                                    JSONObject ob = (JSONObject)o;
                                    motivos[i] = (String)ob.get("motivo");
                                    motivos_id[i] = (String)ob.get("id");
                                    i++;
                                }
                                spinner_motivos.setItems(motivos);
                                spinner_motivos.clear();
                                spinner_diagnosticos.clear();
                                tv_solucion.setText("");
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                System.out.println("getMotivos_error: " + error);
            }
        });

        // Add the request to the RequestQueue.
        queue.add(stringRequest);
    }

    private void getDiagnostico(final String motivo_id)
    {
        System.out.println("motivo_id: "+motivo_id);
        String url=ctx.getApplicationContext().getString(R.string.base_url)+ctx.getApplicationContext().getString(R.string.getDiagnosticos_url);
        RequestQueue queue = Volley.newRequestQueue(ctx);

        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        System.out.println("getDiagnosticos_response: " + response);
                        try {
                            RespuestaResponse cliente = new Gson().fromJson(response, RespuestaResponse.class);
                            JSONParser parser = new JSONParser();
                            JSONArray respuesta = (JSONArray) parser.parse((String) cliente.getRespuesta());

                            if (cliente.getIde_error() == 0) {
                                Toast.makeText(ctx, cliente.getDes_error(), Toast.LENGTH_LONG).show();
                            } else {
                                String[] diagnosticos = new String[respuesta.size()];
                                diagnosticos_id = new String[respuesta.size()];
                                int i = 0;
                                for(Object o:respuesta)
                                {
                                    JSONObject ob = (JSONObject)o;
                                    diagnosticos[i] = (String)ob.get("diagnostico");
                                    diagnosticos_id[i] = (String)ob.get("id");
                                    i++;
                                }
                                spinner_diagnosticos.setItems(diagnosticos);
                                spinner_diagnosticos.clear();
                                tv_solucion.setText("");
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                System.out.println("getDiagnosticos_error: " + error);
            }
        }){
            @Override
            protected Map<String, String> getParams()
            {
                Map<String, String> params = new HashMap<>();
                params.put("motivo_id", motivo_id);
                return params;
            }
        };

        // Add the request to the RequestQueue.
        queue.add(stringRequest);
    }

    private void getSolucion(final String diagnostico_id)
    {
        String url=ctx.getApplicationContext().getString(R.string.base_url)+ctx.getApplicationContext().getString(R.string.getSolucion_url);
        RequestQueue queue = Volley.newRequestQueue(ctx);

        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        System.out.println("getDiagnosticos_response: " + response);
                        try {
                            RespuestaResponse cliente = new Gson().fromJson(response, RespuestaResponse.class);
                            JSONParser parser = new JSONParser();
                            JSONArray respuesta = (JSONArray) parser.parse((String) cliente.getRespuesta());

                            if (cliente.getIde_error() == 0) {
                                Toast.makeText(ctx, cliente.getDes_error(), Toast.LENGTH_LONG).show();
                            } else {
                                for(Object o : respuesta)
                                {
                                    JSONObject ob = (JSONObject)o;
                                    solucion = (String)ob.get("solucion");
                                }
                                tv_solucion.setText(solucion);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                System.out.println("getDiagnosticos_error: " + error);
            }
        }){
            @Override
            protected Map<String, String> getParams()
            {
                Map<String, String> params = new HashMap<>();
                params.put("diagnostico_id", diagnostico_id);
                return params;
            }
        };

        // Add the request to the RequestQueue.
        queue.add(stringRequest);
    }

    //Dialog New Equipo
    Button btn_cancelar,btn_actualizar,btn_registrar,btn_siguiente,btn_atras;
    LinearLayout linear_evaporadora,linear_condensadora;

    Spinner spinner_marcas;
    ArrayAdapter<String> marcas_adapter;
    String[] marcas_id;

    Spinner spinner_selection_equipos;
    ArrayAdapter<String> selection_equipos_adapter;
    String[] selection_equipos_id;

    Spinner spinner_cond_marcas;
    ArrayAdapter<String> marcas_cond_adapter;
    String[] marcas_cond_id;

    JRSpinner spinner_modelos;
    String[] modelos_ids;

    JRSpinner spinner_cond_modelos;
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

    TextView tv_marca,tv_modelo,tv_btu,tv_nro_serie,tv_tipo,tv_voltaje,tv_refrigerante;
    String equipo_id;
    private void getEquipo()
    {
        String url=ctx.getApplicationContext().getString(R.string.base_url)+ctx.getApplicationContext().getString(R.string.getEquipo_url);
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
                                equipo_id="";
                                for (Object o:respuesta)
                                {
                                    JSONObject ob = (JSONObject)o;
                                    equipo_id= (String)ob.get("equipo_id");
                                }
                                System.out.println("getEquipo_equipo_contador: "+equipo_count);
                                viewDialog.hideDialog(5);
                                if(equipo_count==0){
                                    showModalRegisterEquipo();
                                }else{
                                    System.out.println("equipo_id: "+equipo_id);
                                    if(equipo_id.equals("0")){
                                        showModalEquipoSelection();
                                    }else{
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

                                            tv_marca = findViewById(R.id.ficha_marca);
                                            tv_btu = findViewById(R.id.ficha_btu);
                                            tv_tipo = findViewById(R.id.ficha_tipo);
                                            tv_modelo = findViewById(R.id.ficha_modelo);
                                            tv_nro_serie = findViewById(R.id.ficha_nro_serie);
                                            tv_voltaje = findViewById(R.id.ficha_voltaje);
                                            tv_refrigerante = findViewById(R.id.ficha_refrigerante);

                                            tv_marca.setText(marca);
                                            tv_btu.setText(btu);
                                            tv_tipo.setText(tipo);
                                            tv_modelo.setText(modelo);
                                            tv_nro_serie.setText(nro_serie);
                                            tv_voltaje.setText(voltaje);
                                            tv_refrigerante.setText(refrigerante);
                                        }
                                    }
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
                params.put("urgencia_id", id);
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
                                marca_id = marcas_id[0];

                                marcas_cond_adapter = new ArrayAdapter<>(ctx,R.layout.dropdown_style,data);
                                marcas_cond_id = data_id;
                                spinner_cond_marcas.setAdapter(marcas_cond_adapter);
                                marcas_cond_adapter.notifyDataSetChanged();
                                cond_marca_id = marcas_cond_id[0];
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
                                spinner_modelos.setItems(data);
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
                                spinner_cond_modelos.setItems(data);
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
                                btu_id = btus_id[0];

                                btu_cond_adapter = new ArrayAdapter<>(ctx,R.layout.dropdown_style,data);
                                btus_cond_id = data_id;
                                spinner_cond_btus.setAdapter(btu_cond_adapter);
                                btu_cond_adapter.notifyDataSetChanged();
                                cond_btu_id = btus_cond_id[0];
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
                                    String btu = (String)ob.get("tipo");
                                    String id = (String)ob.get("id");
                                    data[i] = btu;
                                    data_id[i] = id;
                                    i++;
                                }
                                tipo_adapter = new ArrayAdapter<>(ctx,R.layout.dropdown_style,data);
                                tipos_id = data_id;
                                spinner_tipos.setAdapter(tipo_adapter);
                                tipo_adapter.notifyDataSetChanged();
                                tipo_id = tipos_id[0];
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
                                cond_tipo_id = tipos_cond_id[0];
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
                                voltaje_id = voltajes_id[0];

                                voltaje_cond_adapter = new ArrayAdapter<>(ctx,R.layout.dropdown_style,data);
                                voltajes_cond_id = data_id;
                                spinner_cond_voltajes.setAdapter(voltaje_adapter);
                                voltaje_cond_adapter.notifyDataSetChanged();
                                cond_voltaje_id = voltajes_cond_id[0];
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
                                fase_id = fases_id[0];

                                fase_cond_adapter = new ArrayAdapter<>(ctx,R.layout.dropdown_style,data);
                                fases_cond_id = data_id;
                                spinner_cond_fases.setAdapter(fase_cond_adapter);
                                fase_cond_adapter.notifyDataSetChanged();
                                cond_fase_id = fases_cond_id[0];
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
                                refrigerante_id = refrigerantes_id[0];

                                refrigerante_cond_adapter = new ArrayAdapter<>(ctx,R.layout.dropdown_style,data);
                                refrigerantes_cond_id = data_id;
                                spinner_cond_refrigerantes.setAdapter(refrigerante_cond_adapter);
                                refrigerante_cond_adapter.notifyDataSetChanged();
                                cond_refrigerante_id = refrigerantes_cond_id[0];
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

    private void registrarEquipo()
    {
        viewDialog.showDialog();
        nro_serie = et_nro_serie.getText().toString();
        cond_nro_serie = et_cond_nro_serie.getText().toString();
        if(modelo_id.isEmpty()){
            spinner_modelos.setError("Debe ingresar un modelo");
            return;
        }else{
            spinner_modelos.setError(null);
        }
        if(cond_modelo_id.isEmpty()){
            spinner_modelos.setError("Debe ingresar un modelo");
            return;
        }else{
            spinner_cond_modelos.setError(null);
        }
        if(nro_serie.isEmpty()){
            et_nro_serie.setError("Debe ingresar un numero de serie");
            return;
        }
        if(cond_nro_serie.isEmpty()){
            et_cond_nro_serie.setError("Debe ingresar un numero de serie");
            return;
        }

        String url=ctx.getApplicationContext().getString(R.string.base_url)+ctx.getApplicationContext().getString(R.string.register_equipo_url);
        RequestQueue queue = Volley.newRequestQueue(ctx);

        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        System.out.println("registerEquipo_response: " + response);
                        try {
                            RespuestaResponse cliente = new Gson().fromJson(response, RespuestaResponse.class);
                            JSONParser parser = new JSONParser();
                            JSONArray respuesta = (JSONArray) parser.parse((String) cliente.getRespuesta());

                            if (cliente.getIde_error() == 0) {
                                Toast.makeText(ctx, cliente.getDes_error(), Toast.LENGTH_LONG).show();
                            } else {
                                viewDialog.hideDialog(3);
                                getEquipos(tienda_id);
                                System.out.println("equipos_count: "+((UrgenciasActivity)ctx).getEquipo_count());
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                System.out.println("registerEquipo_error: " + error);
            }
        }){
            @Override
            protected Map<String, String> getParams()
            {
                Map<String, String> params = new HashMap<>();
                params.put("tienda_id", tienda_id);
                //Evaporadora
                params.put("marca_id", marca_id);
                params.put("modelo_id", modelo_id);
                params.put("btu_id", btu_id);
                params.put("tipo_id", tipo_id);
                params.put("voltaje_id", voltaje_id);
                params.put("fase_id", fase_id);
                params.put("refrigerante_id", refrigerante_id);
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
                return params;
            }
        };

        // Add the request to the RequestQueue.
        queue.add(stringRequest);
    }

    private void showModalRegisterEquipo()
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(ctx);
        LayoutInflater inflater = getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.dialog_register_equipo, null);
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
        icon_evap_scan = dialogView.findViewById(R.id.icon_evap_scan);
        icon_cond_scan = dialogView.findViewById(R.id.icon_cond_scan);

        et_nro_serie = dialogView.findViewById(R.id.nro_serie);
        icon_evap_scan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tipo_nro_serie=1;
                if (!permisoCamaraConcedido) {
                    Toast.makeText(ctx, "Por favor permite que la app acceda a la cámara", Toast.LENGTH_SHORT).show();
                    permisoSolicitadoDesdeBoton = true;
                    verificarYPedirPermisosDeCamara();
                    return;
                }
                escanear();
            }
        });
        et_cond_nro_serie = dialogView.findViewById(R.id.cond_nro_serie);
        icon_cond_scan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tipo_nro_serie=2;
                if (!permisoCamaraConcedido) {
                    Toast.makeText(ctx, "Por favor permite que la app acceda a la cámara", Toast.LENGTH_SHORT).show();
                    permisoSolicitadoDesdeBoton = true;
                    verificarYPedirPermisosDeCamara();
                    return;
                }
                escanear();
            }
        });

//        btn_cancelar = dialogView.findViewById(R.id.btn_cancelar);
        btn_registrar = dialogView.findViewById(R.id.btn_registrar);
        btn_siguiente = dialogView.findViewById(R.id.btn_siguiente);
        btn_atras = dialogView.findViewById(R.id.btn_atras);

        linear_evaporadora = dialogView.findViewById(R.id.linear_evaporadora);
        linear_condensadora = dialogView.findViewById(R.id.linear_condensadora);

        getMarcas();
        getModelos();
        getBtus();
        getTipos();
        getVoltajes();
        getFases();
        getRefrigerantes();

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
        spinner_modelos.setOnItemClickListener(new JRSpinner.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                modelo_id = modelos_ids[position];
                System.out.println("modelo_id: "+modelo_id);
                spinner_modelos.setError(null);
            }
        });
        spinner_cond_modelos.setOnItemClickListener(new JRSpinner.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                cond_modelo_id = modelos_cond_ids[position];
                System.out.println("cond_modelo_id: "+cond_modelo_id);
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
        alertDialog.show();
        btn_registrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                registrarEquipo();
            }
        });
        btn_siguiente.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                linear_evaporadora.setVisibility(View.GONE);
                linear_condensadora.setVisibility(View.VISIBLE);
                btn_siguiente.setVisibility(View.GONE);
                btn_registrar.setVisibility(View.VISIBLE);
//                btn_cancelar.setVisibility(View.GONE);
                btn_atras.setVisibility(View.VISIBLE);
                getCondensadoraTipos();
                getCondensadoraModelos();
            }
        });
        btn_atras.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                linear_condensadora.setVisibility(View.GONE);
                linear_evaporadora.setVisibility(View.VISIBLE);
                btn_atras.setVisibility(View.GONE);
//                btn_cancelar.setVisibility(View.VISIBLE);
                btn_registrar.setVisibility(View.GONE);
                btn_siguiente.setVisibility(View.VISIBLE);
            }
        });
    }

    private void permisoDeCamaraDenegado() {
        // Esto se llama cuando el usuario hace click en "Denegar" o
        // cuando lo denegó anteriormente
        Toast.makeText(ctx, "No puedes escanear si no das permiso", Toast.LENGTH_SHORT).show();
    }

    private void verificarYPedirPermisosDeCamara() {
        int estadoDePermiso = ctx.checkSelfPermission(Manifest.permission.CAMERA);
        if (estadoDePermiso == PackageManager.PERMISSION_GRANTED) {
            // En caso de que haya dado permisos ponemos la bandera en true
            // y llamar al método
            permisoCamaraConcedido = true;
        } else {
            // Si no, pedimos permisos. Ahora mira onRequestPermissionsResult
            ActivityCompat.requestPermissions(FichaActivity.this,
                    new String[]{Manifest.permission.CAMERA},
                    CODIGO_PERMISOS_CAMARA);
        }
    }

    private void escanear() {
        Intent i = new Intent(ctx, LectorActivity.class);
        startActivityForResult(i, CODIGO_INTENT);
    }

    String equipo_selection_id;
    private void showModalEquipoSelection()
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(ctx);
        LayoutInflater inflater = getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.dialog_equipo_selection, null);
        builder.setView(dialogView);
        builder.setCancelable(false);
        spinner_selection_equipos = dialogView.findViewById(R.id.spinner_equipo_selection);
        String[] data = new String[equipos.size()];
        String[] data_id = new String[equipos.size()];
        int i = 0;
        for(Object o: equipos){
            JSONObject ob = (JSONObject)o;
            String equipo = (String)ob.get("evap_nro_serie");
            String id = (String)ob.get("id");
            data[i] = equipo;
            data_id[i] = id;
            i++;
        }
        selection_equipos_adapter = new ArrayAdapter<>(ctx,R.layout.dropdown_style,data);
        selection_equipos_id = data_id;
        spinner_selection_equipos.setAdapter(selection_equipos_adapter);
        selection_equipos_adapter.notifyDataSetChanged();

        btn_cancelar = dialogView.findViewById(R.id.btn_cancelar);
        btn_actualizar = dialogView.findViewById(R.id.btn_actualizar);
        btn_registrar = dialogView.findViewById(R.id.btn_registrar);

        final AlertDialog alertDialog = builder.create();
        alertDialog.show();
        equipo_selection_id = "";
        spinner_selection_equipos.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                equipo_selection_id = selection_equipos_id[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });
        btn_actualizar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(ctx,"Actualizando equipo",Toast.LENGTH_LONG).show();
                updateEquipo();
            }
        });

        btn_cancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });
        btn_registrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showModalRegisterEquipo();
            }
        });
    }

    private void updateEquipo()
    {
        String url=ctx.getApplicationContext().getString(R.string.base_url)+ctx.getApplicationContext().getString(R.string.update_equipo_url);
        RequestQueue queue = Volley.newRequestQueue(ctx);

        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        System.out.println("update_equipo_response: " + response);
                        try {
                            RespuestaResponse cliente = new Gson().fromJson(response, RespuestaResponse.class);
                            JSONParser parser = new JSONParser();
                            JSONArray respuesta = (JSONArray) parser.parse((String) cliente.getRespuesta());

                            if (cliente.getIde_error() == 0) {
                                Toast.makeText(ctx, cliente.getDes_error(), Toast.LENGTH_LONG).show();
                            } else {
                                getEquipo();
                                Toast.makeText(ctx,"Equipo actualizado",Toast.LENGTH_SHORT).show();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                System.out.println("update_equipo_error: " + error);
            }
        }){
            @Override
            protected Map<String, String> getParams()
            {
                Map<String, String> params = new HashMap<>();
                params.put("urgencia_id", id);
                params.put("equipo_id", equipo_selection_id);
                return params;
            }
        };

        // Add the request to the RequestQueue.
        queue.add(stringRequest);
    }


}
