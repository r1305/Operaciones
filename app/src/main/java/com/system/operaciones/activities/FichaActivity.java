package com.system.operaciones.activities;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

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

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import jrizani.jrspinner.JRSpinner;

public class FichaActivity extends AppCompatActivity {

    Context ctx;
    Credentials cred;
    //ANTES
    private ImageView icon_camera_presion_baja,icon_camera_presion_alta,icon_camera_temp_l1,icon_camera_temp_l2,icon_camera_temp_l3,icon_camera_amp_l1,icon_camera_amp_l2,icon_camera_amp_l3,icon_camera_volt_l1,icon_camera_volt_l2,icon_camera_volt_l3;
    private ImageView icon_gallery_presion_baja,icon_gallery_presion_alta,icon_gallery_temp_l1,icon_gallery_temp_l2,icon_gallery_temp_l3,icon_gallery_amp_l1,icon_gallery_amp_l2,icon_gallery_amp_l3,icon_gallery_volt_l1,icon_gallery_volt_l2,icon_gallery_volt_l3;
    String image_presion_baja="",image_presion_alta="",image_temp_l1="",image_temp_l2="",image_temp_l3="",image_amp_l1="",image_amp_l2="",image_amp_l3="",image_volt_l1="",image_volt_l2="",image_volt_l3="",image_signature_tecnico="",image_signature_cliente="";
    private EditText presion_baja,presion_alta,temp_l1,temp_l2,temp_l3,amp_l1,amp_l2,amp_l3,volt_l1,volt_l2,volt_l3;
    //DESPUES
    private ImageView despues_icon_camera_presion_baja,despues_icon_camera_presion_alta,despues_icon_camera_temp_l1,despues_icon_camera_temp_l2,despues_icon_camera_temp_l3,despues_icon_camera_amp_l1,despues_icon_camera_amp_l2,despues_icon_camera_amp_l3,despues_icon_camera_volt_l1,despues_icon_camera_volt_l2,despues_icon_camera_volt_l3;
    private ImageView despues_icon_gallery_presion_baja,despues_icon_gallery_presion_alta,despues_icon_gallery_temp_l1,despues_icon_gallery_temp_l2,despues_icon_gallery_temp_l3,despues_icon_gallery_amp_l1,despues_icon_gallery_amp_l2,despues_icon_gallery_amp_l3,despues_icon_gallery_volt_l1,despues_icon_gallery_volt_l2,despues_icon_gallery_volt_l3;
    String despues_image_presion_baja="",despues_image_presion_alta="",despues_image_temp_l1="",despues_image_temp_l2="",despues_image_temp_l3="",despues_image_amp_l1="",despues_image_amp_l2="",despues_image_amp_l3="",despues_image_volt_l1="",despues_image_volt_l2="",despues_image_volt_l3="",despues_image_signature_tecnico="",despues_image_signature_cliente="";
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

    //Dialog New Equipo
    Spinner spinner_marcas;
    ArrayAdapter<String> marcas_adapter;
    String[] marcas_id;

    private String name_tecnico,dni_tecnico,cargo_tecnico,name_cliente,dni_cliente,cargo_cliente;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ficha);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Ficha Técnica");
        
        ctx = this;
        cred = new Credentials(ctx);
        id = getIntent().getStringExtra("urgencia");
        tienda_id = getIntent().getStringExtra("tienda_id");
        getEquipo();

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
            Bitmap image = (Bitmap) data.getExtras().get("data");
            String b64 = Image.convert(image);
            String image_type = cred.getData("image_type");
            switch (image_type){
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
            }
        }
    }

    private void registrarFicha(final String urgencia_id)
    {
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
                //ANTES
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

        // Add the request to the RequestQueue.
        queue.add(stringRequest);
    }

    private void createFicha(final String urgencia_id)
    {
        String url=ctx.getApplicationContext().getString(R.string.base_url)+ctx.getApplicationContext().getString(R.string.crear_ficha_url);
        RequestQueue queue = Volley.newRequestQueue(ctx);

        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url+"?urgencia_id="+urgencia_id,
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
                                ((FichaActivity)ctx).finish();
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
        });

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

    private void getEquipo()
    {
        System.out.println("urgencia_id: "+id);
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
                                String equipo_id="";
                                for (Object o:respuesta)
                                {
                                    JSONObject ob = (JSONObject)o;
                                    equipo_id= (String)ob.get("equipo_id");
                                }
                                if(equipo_id.equals("3")){
                                    AlertDialog.Builder builder = new AlertDialog.Builder(ctx);
                                    LayoutInflater inflater = getLayoutInflater();
                                    View dialogView = inflater.inflate(R.layout.dialog_register_equipo, null);
                                    builder.setView(dialogView);
                                    builder.setCancelable(false);
                                    spinner_marcas = dialogView.findViewById(R.id.spinner_marcas);
                                    getMarcas();
                                    AlertDialog alertDialog = builder.create();
                                    alertDialog.show();
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
                                System.out.println();
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
}
