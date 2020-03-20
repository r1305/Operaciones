package com.system.operaciones.activities;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
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

import androidx.appcompat.app.AlertDialog;
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

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import jrizani.jrspinner.JRSpinner;

public class FichaUrgenciaActivity extends AppCompatActivity {

    Context ctx;
    Credentials cred;
    //ANTES
    private ImageView icon_camera_falla,icon_camera_presion_baja,icon_camera_presion_alta,icon_camera_temp_l1,icon_camera_temp_l2,icon_camera_temp_l3,icon_camera_amp_l1,icon_camera_amp_l2,icon_camera_amp_l3,icon_camera_volt_l1,icon_camera_volt_l2,icon_camera_volt_l3;
    private ImageView icon_gallery_falla,icon_gallery_presion_baja,icon_gallery_presion_alta,icon_gallery_temp_l1,icon_gallery_temp_l2,icon_gallery_temp_l3,icon_gallery_amp_l1,icon_gallery_amp_l2,icon_gallery_amp_l3,icon_gallery_volt_l1,icon_gallery_volt_l2,icon_gallery_volt_l3;
    String image_falla="",image_presion_baja="",image_presion_alta="",image_temp_l1="",image_temp_l2="",image_temp_l3="",image_amp_l1="",image_amp_l2="",image_amp_l3="",image_volt_l1="",image_volt_l2="",image_volt_l3="",image_signature_tecnico="",image_signature_cliente="";
    String et_temp_l1="0",et_temp_l2="0",et_temp_l3="0",et_presion_baja="0",et_presion_alta="0",et_amp_l1="0",et_amp_l2="0",et_amp_l3="0",et_volt_l1="0",et_volt_l2="0",et_volt_l3="0";
    private EditText presion_baja,presion_alta,temp_l1,temp_l2,temp_l3,amp_l1,amp_l2,amp_l3,volt_l1,volt_l2,volt_l3;
    //DESPUES
    private ImageView despues_icon_camera_falla,despues_icon_camera_presion_baja,despues_icon_camera_presion_alta,despues_icon_camera_temp_l1,despues_icon_camera_temp_l2,despues_icon_camera_temp_l3,despues_icon_camera_amp_l1,despues_icon_camera_amp_l2,despues_icon_camera_amp_l3,despues_icon_camera_volt_l1,despues_icon_camera_volt_l2,despues_icon_camera_volt_l3;
    private ImageView despues_icon_gallery_falla,despues_icon_gallery_presion_baja,despues_icon_gallery_presion_alta,despues_icon_gallery_temp_l1,despues_icon_gallery_temp_l2,despues_icon_gallery_temp_l3,despues_icon_gallery_amp_l1,despues_icon_gallery_amp_l2,despues_icon_gallery_amp_l3,despues_icon_gallery_volt_l1,despues_icon_gallery_volt_l2,despues_icon_gallery_volt_l3;
    String despues_image_falla="",despues_image_presion_baja="",despues_image_presion_alta="",despues_image_temp_l1="",despues_image_temp_l2="",despues_image_temp_l3="",despues_image_amp_l1="",despues_image_amp_l2="",despues_image_amp_l3="",despues_image_volt_l1="",despues_image_volt_l2="",despues_image_volt_l3="";
    String et_despues_temp_l1="0",et_despues_temp_l2="0",et_despues_temp_l3="0",et_despues_presion_baja="0",et_despues_presion_alta="0",et_despues_amp_l1="0",et_despues_amp_l2="0",et_despues_amp_l3="0",et_despues_volt_l1="0",et_despues_volt_l2="0",et_despues_volt_l3="0";
    private EditText despues_presion_baja,despues_presion_alta,despues_temp_l1,despues_temp_l2,despues_temp_l3,despues_amp_l1,despues_amp_l2,despues_amp_l3,despues_volt_l1,despues_volt_l2,despues_volt_l3;

    private Button btn_lectura_cancelar;
    private Button btn_lectura_guardar;
    private String id,tienda_id;

    AlertDialog alertDialog;
    SearchableSpinner spinner_motivos;
    String[] motivos_id;

    SearchableSpinner spinner_diagnosticos;
    String[] diagnosticos_id;

    String solucion;
    TextView tv_solucion;
    EditText et_diagnostico;

    String motivo_id;
    String diagnostico_id;

    private String name_tecnico,dni_tecnico,cargo_tecnico,name_cliente,dni_cliente,cargo_cliente;

    int equipo_count;
    int tipo_nro_serie = 1;
    private static final int  CODIGO_INTENT = 2;

    ViewDialog viewDialog;

    EditText cargo,dni,name;
    String fase = "0";
    int modo_prueba = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ficha_urgencia);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Ficha Urgencia");

        ctx = this;
        cred = new Credentials(ctx);
        id = getIntent().getStringExtra("id");
        tienda_id = getIntent().getStringExtra("tienda_id");
        getEquipos(tienda_id);
        viewDialog = new ViewDialog(this);
        viewDialog.showDialog();
        cred.save_data("image_type","0");

        spinner_motivos = findViewById(R.id.spinner_motivo);
        spinner_diagnosticos = findViewById(R.id.spinner_diagnostico);
        et_diagnostico = findViewById(R.id.et_diagnostico);
        getMotivos();
        spinner_motivos.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                motivo_id = motivos_id[i];
                if(motivo_id.equals("6")){
                    et_diagnostico.setVisibility(View.VISIBLE);
                }else{
                    et_diagnostico.setVisibility(View.GONE);
                }
                getDiagnostico(motivo_id);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        spinner_diagnosticos.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                diagnostico_id = diagnosticos_id[i];
                getSolucion(diagnostico_id);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

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

                LinearLayout linear_amp_l3 = dialogView.findViewById(R.id.linear_amp_l3);
                LinearLayout linear_volt_l3 = dialogView.findViewById(R.id.linear_volt_l3);

                if(fase.equals("1")){
                    linear_amp_l3.setVisibility(View.GONE);
                    linear_volt_l3.setVisibility(View.GONE);
                }else{
                    linear_amp_l3.setVisibility(View.VISIBLE);
                    linear_volt_l3.setVisibility(View.VISIBLE);
                }

                validateUpload();

                temp_l1.setText(et_temp_l1);
                temp_l2.setText(et_temp_l2);
                temp_l3.setText(et_temp_l3);

                presion_alta.setText(et_presion_alta);
                presion_baja.setText(et_presion_baja);

                amp_l1.setText(et_amp_l1);
                amp_l2.setText(et_amp_l2);
                amp_l3.setText(et_amp_l3);

                volt_l1.setText(et_volt_l1);
                volt_l2.setText(et_volt_l2);
                volt_l3.setText(et_volt_l3);

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
                        et_temp_l1=temp_l1.getText().toString();
                        et_temp_l2=temp_l2.getText().toString();
                        et_temp_l3=temp_l3.getText().toString();
                        et_presion_baja=presion_baja.getText().toString();
                        et_presion_alta=presion_alta.getText().toString();
                        et_amp_l1=amp_l1.getText().toString();
                        et_amp_l2=amp_l2.getText().toString();
                        et_amp_l3=amp_l3.getText().toString();
                        et_volt_l1=volt_l1.getText().toString();
                        et_volt_l2=volt_l2.getText().toString();
                        et_volt_l3=volt_l3.getText().toString();
                        alertDialog.dismiss();
                        Toast.makeText(ctx,"Guardado",Toast.LENGTH_SHORT).show();
                    }
                });

                alertDialog = builder.create();
                alertDialog.show();

                icon_camera_falla.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Log.e("falla","1");
                        cred.save_data("image_type","1");
                        openChooser();
                    }
                });

                icon_gallery_falla.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showPreview(image_falla,1);
                    }
                });

                icon_camera_temp_l1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        cred.save_data("image_type","2");
                        openChooser();
                    }
                });

                icon_gallery_temp_l1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showPreview(image_temp_l1,2);
                    }
                });

                icon_camera_temp_l2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        cred.save_data("image_type","3");
                        openChooser();
                    }
                });

                icon_gallery_temp_l2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showPreview(image_temp_l2,3);
                    }
                });

                icon_camera_temp_l3.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        cred.save_data("image_type","4");
                        openChooser();
                    }
                });

                icon_gallery_temp_l3.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showPreview(image_temp_l3,4);
                    }
                });

                icon_camera_presion_baja.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Log.e("presion_baja","1");
                        cred.save_data("image_type","5");
                        openChooser();
                    }
                });

                icon_gallery_presion_baja.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showPreview(image_presion_baja,5);
                    }
                });

                icon_camera_presion_alta.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        cred.save_data("image_type","6");
                        openChooser();
                    }
                });

                icon_gallery_presion_alta.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showPreview(image_presion_alta,6);
                    }
                });

                icon_camera_amp_l1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        cred.save_data("image_type","7");
                        openChooser();
                    }
                });

                icon_gallery_amp_l1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showPreview(image_amp_l1,7);
                    }
                });

                icon_camera_amp_l2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        cred.save_data("image_type","8");
                        openChooser();
                    }
                });

                icon_gallery_amp_l2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showPreview(image_amp_l2,8);
                    }
                });

                icon_camera_amp_l3.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        cred.save_data("image_type","9");
                        openChooser();
                    }
                });

                icon_gallery_amp_l3.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showPreview(image_amp_l3,9);
                    }
                });

                icon_camera_volt_l1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        cred.save_data("image_type","10");
                        openChooser();
                    }
                });

                icon_gallery_volt_l1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showPreview(image_volt_l1,10);
                    }
                });

                icon_camera_volt_l2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        cred.save_data("image_type","11");
                        openChooser();
                    }
                });

                icon_gallery_volt_l2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showPreview(image_volt_l2,11);
                    }
                });

                icon_camera_volt_l3.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        cred.save_data("image_type","12");
                        openChooser();
                    }
                });

                icon_gallery_volt_l3.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showPreview(image_volt_l3,12);
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

                LinearLayout linear_amp_l3 = dialogView.findViewById(R.id.linear_amp_l3);
                LinearLayout linear_volt_l3 = dialogView.findViewById(R.id.linear_volt_l3);

                if(fase.equals("1")){
                    linear_amp_l3.setVisibility(View.GONE);
                    linear_volt_l3.setVisibility(View.GONE);
                }else{
                    linear_amp_l3.setVisibility(View.VISIBLE);
                    linear_volt_l3.setVisibility(View.VISIBLE);
                }

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
                        et_despues_temp_l1=temp_l1.getText().toString();
                        et_despues_temp_l2=temp_l2.getText().toString();
                        et_despues_temp_l3=temp_l3.getText().toString();
                        et_despues_presion_baja=presion_baja.getText().toString();
                        et_despues_presion_alta=presion_alta.getText().toString();
                        et_despues_amp_l1=amp_l1.getText().toString();
                        et_despues_amp_l2=amp_l2.getText().toString();
                        et_despues_amp_l3=amp_l3.getText().toString();
                        et_despues_volt_l1=volt_l1.getText().toString();
                        et_despues_volt_l2=volt_l2.getText().toString();
                        et_despues_volt_l3=volt_l3.getText().toString();
                        alertDialog.dismiss();
                        Toast.makeText(ctx,"Guardado",Toast.LENGTH_SHORT).show();
                    }
                });

                alertDialog = builder.create();
                alertDialog.show();

                despues_icon_camera_falla.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Log.e("despues_falla","13");
                        cred.save_data("image_type","13");
                        openChooser();
                    }
                });

                despues_icon_gallery_falla.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showPreview(despues_image_falla,13);
                    }
                });

                despues_icon_camera_temp_l1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        cred.save_data("image_type","14");
                        openChooser();
                    }
                });

                despues_icon_gallery_temp_l1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showPreview(despues_image_temp_l1,14);
                    }
                });

                despues_icon_camera_temp_l2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        cred.save_data("image_type","15");
                        openChooser();
                    }
                });

                despues_icon_gallery_temp_l2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showPreview(despues_image_temp_l2,15);
                    }
                });

                despues_icon_camera_temp_l3.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        cred.save_data("image_type","16");
                        openChooser();
                    }
                });

                despues_icon_gallery_temp_l3.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showPreview(despues_image_temp_l3,16);
                    }
                });

                despues_icon_camera_presion_baja.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Log.e("despues_presion_baja","17");
                        cred.save_data("image_type","17");
                        openChooser();
                    }
                });

                despues_icon_gallery_presion_baja.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showPreview(despues_image_presion_baja,17);
                    }
                });

                despues_icon_camera_presion_alta.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        cred.save_data("image_type","18");
                        openChooser();
                    }
                });

                despues_icon_gallery_presion_alta.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showPreview(despues_image_presion_alta,18);
                    }
                });

                despues_icon_camera_amp_l1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        cred.save_data("image_type","19");
                        openChooser();
                    }
                });

                despues_icon_gallery_amp_l1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showPreview(despues_image_amp_l1,19);
                    }
                });

                despues_icon_camera_amp_l2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        cred.save_data("image_type","20");
                        openChooser();
                    }
                });

                despues_icon_gallery_amp_l2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showPreview(despues_image_amp_l2,20);
                    }
                });

                despues_icon_camera_amp_l3.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        cred.save_data("image_type","21");
                        openChooser();
                    }
                });

                despues_icon_gallery_amp_l3.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showPreview(despues_image_amp_l3,21);
                    }
                });

                despues_icon_camera_volt_l1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        cred.save_data("image_type","22");
                        openChooser();
                    }
                });

                despues_icon_gallery_volt_l1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showPreview(despues_image_volt_l1,22);
                    }
                });

                despues_icon_camera_volt_l2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        cred.save_data("image_type","23");
                        openChooser();
                    }
                });

                despues_icon_gallery_volt_l2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showPreview(despues_image_volt_l2,23);
                    }
                });

                despues_icon_camera_volt_l3.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        cred.save_data("image_type","24");
                        openChooser();
                    }
                });

                despues_icon_gallery_volt_l3.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showPreview(despues_image_volt_l3,24);
                    }
                });

            }
        });

        Button btn_registrar = findViewById(R.id.btn_registrar);
        Button btn_cerrar = findViewById(R.id.btn_cerrar);
        btn_cerrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((FichaUrgenciaActivity)ctx).finish();
            }
        });
        Button btn_firma_tecnico = findViewById(R.id.btn_firma_tecnico);
        Button btn_firma_cliente = findViewById(R.id.btn_firma_cliente);

        btn_registrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(image_falla.isEmpty()){
                    Toast.makeText(ctx,"Debe cargar foto de la falla",Toast.LENGTH_SHORT).show();
                    return;
                }
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

                if(despues_image_falla.isEmpty()){
                    Toast.makeText(ctx,"Debe cargar foto después de la reparación de la falla",Toast.LENGTH_SHORT).show();
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
                        if(image_signature_cliente!=null && !image_signature_cliente.isEmpty() && !name_cliente.isEmpty() && !dni_cliente.isEmpty() && !cargo_cliente.isEmpty())
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
                name = dialogView.findViewById(R.id.dialog_signature_et_name);
                dni = dialogView.findViewById(R.id.dialog_signature_et_dni);
                cargo = dialogView.findViewById(R.id.dialog_signature_et_cargo);
                final SignaturePad signaturePad = dialogView.findViewById(R.id.dialog_signature);
                final AlertDialog alertDialog = builder.create();
                Log.e("tipo_proveedor",tipo_proveedor);
                if(tipo_proveedor.equals("1")){
                    getDatosTecnico(proveedor_id);
                }
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
                                viewDialog.hideDialog(1);
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
                            viewDialog.hideDialog(1);
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
        builder.setPositiveButton("Eliminar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                cleanPhoto(element);
            }
        });


        AlertDialog alert_preview = builder.create();
        alert_preview.show();
    }

    void cleanPhoto(int element)
    {
        switch (element){
            case 1:
                image_falla="";
                icon_gallery_falla.setVisibility(View.GONE);
                icon_camera_falla.setVisibility(View.VISIBLE);
            break;
            case 2:
                image_temp_l1="";
                icon_gallery_temp_l1.setVisibility(View.GONE);
                icon_camera_temp_l1.setVisibility(View.VISIBLE);
                break;
            case 3:
                image_temp_l2="";
                icon_gallery_temp_l2.setVisibility(View.GONE);
                icon_camera_temp_l2.setVisibility(View.VISIBLE);
                break;
            case 4:
                image_temp_l3="";
                icon_gallery_temp_l3.setVisibility(View.GONE);
                icon_camera_temp_l3.setVisibility(View.VISIBLE);
                break;
            case 5:
                image_presion_baja="";
                icon_gallery_presion_baja.setVisibility(View.GONE);
                icon_camera_presion_baja.setVisibility(View.VISIBLE);
                break;
            case 6:
                image_presion_alta="";
                icon_gallery_presion_alta.setVisibility(View.GONE);
                icon_camera_presion_alta.setVisibility(View.VISIBLE);
                break;
            case 7:
                image_amp_l1="";
                icon_gallery_amp_l1.setVisibility(View.GONE);
                icon_camera_amp_l1.setVisibility(View.VISIBLE);
                break;
            case 8:
                image_amp_l2="";
                icon_gallery_amp_l2.setVisibility(View.GONE);
                icon_camera_amp_l2.setVisibility(View.VISIBLE);
                break;
            case 9:
                image_amp_l3="";
                icon_gallery_amp_l3.setVisibility(View.GONE);
                icon_camera_amp_l3.setVisibility(View.VISIBLE);
                break;
            case 10:
                image_volt_l1="";
                icon_gallery_volt_l1.setVisibility(View.GONE);
                icon_camera_volt_l1.setVisibility(View.VISIBLE);
                break;
            case 11:
                image_volt_l2="";
                icon_gallery_volt_l2.setVisibility(View.GONE);
                icon_camera_volt_l2.setVisibility(View.VISIBLE);
                break;
            case 12:
                image_volt_l3="";
                icon_gallery_volt_l3.setVisibility(View.GONE);
                icon_camera_volt_l3.setVisibility(View.VISIBLE);
                break;
            case 13:
                despues_image_falla="";
                despues_icon_gallery_falla.setVisibility(View.GONE);
                despues_icon_camera_falla.setVisibility(View.VISIBLE);
                break;
            case 14:
                despues_image_temp_l1="";
                despues_icon_gallery_temp_l1.setVisibility(View.GONE);
                despues_icon_camera_temp_l1.setVisibility(View.VISIBLE);
                break;
            case 15:
                despues_image_temp_l2="";
                despues_icon_gallery_temp_l2.setVisibility(View.GONE);
                despues_icon_camera_temp_l2.setVisibility(View.VISIBLE);
                break;
            case 16:
                despues_image_temp_l3="";
                despues_icon_gallery_temp_l3.setVisibility(View.GONE);
                despues_icon_camera_temp_l3.setVisibility(View.VISIBLE);
                break;
            case 17:
                despues_image_amp_l1="";
                despues_icon_gallery_amp_l1.setVisibility(View.GONE);
                despues_icon_camera_amp_l1.setVisibility(View.VISIBLE);
                break;
            case 18:
                despues_image_amp_l2="";
                despues_icon_gallery_amp_l2.setVisibility(View.GONE);
                despues_icon_camera_amp_l2.setVisibility(View.VISIBLE);
                break;
            case 19:
                despues_image_amp_l3="";
                despues_icon_gallery_amp_l3.setVisibility(View.GONE);
                despues_icon_camera_amp_l3.setVisibility(View.VISIBLE);
                break;
            case 20:
                despues_image_volt_l1="";
                despues_icon_gallery_volt_l1.setVisibility(View.GONE);
                despues_icon_camera_volt_l1.setVisibility(View.VISIBLE);
                break;
            case 21:
                despues_image_volt_l2="";
                despues_icon_gallery_volt_l2.setVisibility(View.GONE);
                despues_icon_camera_volt_l2.setVisibility(View.VISIBLE);
                break;
            case 22:
                despues_image_volt_l3="";
                despues_icon_gallery_volt_l3.setVisibility(View.GONE);
                despues_icon_camera_volt_l3.setVisibility(View.VISIBLE);
                break;
        }
    }

    @Override
    public boolean onSupportNavigateUp()
    {
        onBackPressed();
        return true;
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
                                viewDialog.hideDialog(1);
                                Toast.makeText(ctx, cliente.getDes_error(), Toast.LENGTH_LONG).show();
                            } else {
                                Toast.makeText(ctx,"Registro correcto",Toast.LENGTH_SHORT).show();
                                createFicha(urgencia_id);
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

                params.put("temperatura_l1", temp_l1.getText().toString());
                params.put("img_temperatura_l1", image_temp_l1);
                params.put("temperatura_l2", temp_l2.getText().toString());
                params.put("img_temperatura_l2", image_temp_l2);
                params.put("temperatura_l3", temp_l3.getText().toString());
                params.put("img_temperatura_l3", image_temp_l3);

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

                //DESPUES
                params.put("despues_falla", despues_image_falla);

                params.put("despues_temperatura_l1", despues_temp_l1.getText().toString());
                params.put("despues_img_temperatura_l1", despues_image_temp_l1);
                params.put("despues_temperatura_l2", despues_temp_l2.getText().toString());
                params.put("despues_img_temperatura_l2", despues_image_temp_l2);
                params.put("despues_temperatura_l3", despues_temp_l3.getText().toString());
                params.put("despues_img_temperatura_l3", despues_image_temp_l3);

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

                //FIRMAS
                params.put("img_signature_tecnico",image_signature_tecnico);
                params.put("name_tecnico",name_tecnico);
                params.put("dni_tecnico",dni_tecnico);
                params.put("cargo_tecnico",cargo_tecnico);

                params.put("img_signature_cliente",image_signature_cliente);
                params.put("name_cliente",name_cliente);
                params.put("dni_cliente",dni_cliente);
                params.put("cargo_cliente",cargo_cliente);

                params.put("txt_diagnostico",et_diagnostico.getText().toString());
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
                                viewDialog.hideDialog(1);
                                Toast.makeText(ctx, cliente.getDes_error(), Toast.LENGTH_LONG).show();
                            } else {
                                Toast.makeText(ctx,"Ficha creada correctamente",Toast.LENGTH_SHORT).show();
                                viewDialog.hideDialog(1);
                                new Handler().postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        new Utils().sendFichaUrgencia(tienda_id,urgencia_id,ctx);
                                        ((FichaUrgenciaActivity)ctx).finish();
                                    }
                                }, 5000);
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
    ArrayAdapter<String> motivos_adapter;
    ArrayAdapter<String> diagnostico_adapter;
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
                                viewDialog.hideDialog(1);
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
                                motivos_adapter = new ArrayAdapter<>(ctx,R.layout.dropdown_style,motivos);
                                spinner_motivos.setAdapter(motivos_adapter);
                                tv_solucion.setText("");
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
                                viewDialog.hideDialog(1);
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
                                diagnostico_adapter = new ArrayAdapter<>(ctx,R.layout.dropdown_style,diagnosticos);
                                spinner_diagnosticos.setAdapter(diagnostico_adapter);
                                tv_solucion.setText("");
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
                                viewDialog.hideDialog(1);
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
                            viewDialog.hideDialog(1);
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

    TextView tv_marca,tv_modelo,tv_btu,tv_nro_serie,tv_tipo,tv_voltaje,tv_refrigerante,tv_motivo,tv_nro_equipo;
    String equipo_id;
    String tipo_proveedor="0",proveedor_id="0";

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
                                            String nro_equipo = (String)ob.get("nro_equipo");
                                            String motivo = (String)ob.get("motivo");
                                            fase = (String)ob.get("cond_fase_id");

                                            tv_marca = findViewById(R.id.ficha_marca);
                                            tv_btu = findViewById(R.id.ficha_btu);
                                            tv_tipo = findViewById(R.id.ficha_tipo);
                                            tv_modelo = findViewById(R.id.ficha_modelo);
                                            tv_nro_serie = findViewById(R.id.ficha_nro_serie);
                                            tv_voltaje = findViewById(R.id.ficha_voltaje);
                                            tv_refrigerante = findViewById(R.id.ficha_refrigerante);
                                            tv_motivo = findViewById(R.id.ficha_tv_motivo);
                                            tv_nro_equipo = findViewById(R.id.ficha_tv_nro_equipo);

                                            tv_marca.setText(marca);
                                            tv_btu.setText(btu);
                                            tv_tipo.setText(tipo);
                                            tv_modelo.setText(modelo);
                                            tv_nro_serie.setText(nro_serie);
                                            tv_voltaje.setText(voltaje);
                                            tv_refrigerante.setText(refrigerante);
                                            tv_motivo.setText(motivo);
                                            tv_nro_equipo.setText("Equipo N° "+nro_equipo);

                                            tipo_proveedor = (String)ob.get("tipo_proveedor");
                                            if(tipo_proveedor.equals("1")){
                                                proveedor_id = (String)ob.get("proveedor_id");
                                            }
                                        }
                                    }
                                }
                            }
                        } catch (Exception e) {
                            viewDialog.hideDialog(1);
                            e.printStackTrace();
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
                                viewDialog.hideDialog(1);
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
                            viewDialog.hideDialog(1);
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                viewDialog.hideDialog(1);
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
                                viewDialog.hideDialog(1);
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
                            viewDialog.hideDialog(1);
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                viewDialog.hideDialog(1);
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
                                viewDialog.hideDialog(1);
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
                            viewDialog.hideDialog(1);
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                viewDialog.hideDialog(1);
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
                                viewDialog.hideDialog(1);
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
                            viewDialog.hideDialog(1);
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                viewDialog.hideDialog(1);
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
                                viewDialog.hideDialog(1);
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
                            viewDialog.hideDialog(1);
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                viewDialog.hideDialog(1);
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
                                viewDialog.hideDialog(1);
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
                            viewDialog.hideDialog(1);
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                viewDialog.hideDialog(1);
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
                                viewDialog.hideDialog(1);
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
                            viewDialog.hideDialog(1);
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                viewDialog.hideDialog(1);
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
                                viewDialog.hideDialog(1);
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
                            viewDialog.hideDialog(1);
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                viewDialog.hideDialog(1);
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
                                viewDialog.hideDialog(1);
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
                            viewDialog.hideDialog(1);
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                viewDialog.hideDialog(1);
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
                                viewDialog.hideDialog(1);
                                Toast.makeText(ctx, cliente.getDes_error(), Toast.LENGTH_LONG).show();
                            } else {
                                viewDialog.hideDialog(3);
                                getEquipos(tienda_id);
                                System.out.println("equipos_count: "+((UrgenciasActivity)ctx).getEquipo_count());
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
        final View dialogView = inflater.inflate(R.layout.dialog_new_equipo, null);
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
                escanear();
            }
        });
        et_cond_nro_serie = dialogView.findViewById(R.id.cond_nro_serie);
        icon_cond_scan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tipo_nro_serie=2;
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
                                viewDialog.hideDialog(1);
                                Toast.makeText(ctx, cliente.getDes_error(), Toast.LENGTH_LONG).show();
                            } else {
                                getEquipo();
                                Toast.makeText(ctx,"Equipo actualizado",Toast.LENGTH_SHORT).show();
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

    void validateUploadDespues()
    {
        despues_temp_l1.setText(et_despues_temp_l1);
        despues_temp_l2.setText(et_despues_temp_l2);
        despues_temp_l3.setText(et_despues_temp_l3);

        despues_presion_alta.setText(et_despues_presion_alta);
        despues_presion_baja.setText(et_despues_presion_baja);

        despues_amp_l1.setText(et_despues_amp_l1);
        despues_amp_l2.setText(et_despues_amp_l2);
        despues_amp_l3.setText(et_despues_amp_l3);

        despues_volt_l1.setText(et_despues_volt_l1);
        despues_volt_l2.setText(et_despues_volt_l2);
        despues_volt_l3.setText(et_despues_volt_l3);

        //Falla
        if(!despues_image_falla.isEmpty()){
            despues_icon_camera_falla.setVisibility(View.GONE);
            despues_icon_gallery_falla.setVisibility(View.VISIBLE);
        }else{
            despues_icon_gallery_falla.setVisibility(View.GONE);
            despues_icon_camera_falla.setVisibility(View.VISIBLE);
        }

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

    void validateUpload()
    {
        temp_l1.setText(et_temp_l1);
        temp_l2.setText(et_temp_l2);
        temp_l3.setText(et_temp_l3);

        presion_alta.setText(et_presion_alta);
        presion_baja.setText(et_presion_baja);

        amp_l1.setText(et_amp_l1);
        amp_l2.setText(et_amp_l2);
        amp_l3.setText(et_amp_l3);

        volt_l1.setText(et_volt_l1);
        volt_l2.setText(et_volt_l2);
        volt_l3.setText(et_volt_l3);

        //Falla
        if(!image_falla.isEmpty()){
            icon_camera_falla.setVisibility(View.GONE);
            icon_gallery_falla.setVisibility(View.VISIBLE);
        }else{
            icon_gallery_falla.setVisibility(View.GONE);
            icon_camera_falla.setVisibility(View.VISIBLE);
        }

        if(!image_falla.isEmpty()){
            icon_camera_falla.setVisibility(View.GONE);
            icon_gallery_falla.setVisibility(View.VISIBLE);
        }else{
            icon_gallery_falla.setVisibility(View.GONE);
            icon_camera_falla.setVisibility(View.VISIBLE);
        }

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
        if(!fase.equals("1")){

            if(!image_amp_l3.isEmpty()){
                icon_camera_amp_l3.setVisibility(View.GONE);
                icon_gallery_amp_l3.setVisibility(View.VISIBLE);
            }else{
                icon_camera_amp_l3.setVisibility(View.VISIBLE);
                icon_gallery_amp_l3.setVisibility(View.GONE);
            }
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

        if(!fase.equals("1")){

            if(!image_volt_l3.isEmpty()){
                icon_camera_volt_l3.setVisibility(View.GONE);
                icon_gallery_volt_l3.setVisibility(View.VISIBLE);
            }else{
                icon_camera_volt_l3.setVisibility(View.VISIBLE);
                icon_gallery_volt_l3.setVisibility(View.GONE);
            }
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
                case "1":
                    image_falla = Image.convert(BitmapFactory.decodeFile(b64,options));
                    Log.e("image_falla","1");
                    icon_camera_falla.setVisibility(View.GONE);
                    icon_gallery_falla.setVisibility(View.VISIBLE);
                    if(image_falla.isEmpty())
                        System.out.println("image_falla NULL");

                    if(modo_prueba==1){
                        setAllValues();
                    }
                    break;
                case "2":
                    image_temp_l1 = Image.convert(BitmapFactory.decodeFile(b64,options));
                    Log.e("image_temp_l1","2");
                    icon_camera_temp_l1.setVisibility(View.GONE);
                    icon_gallery_temp_l1.setVisibility(View.VISIBLE);
                    if(image_temp_l1.isEmpty())
                        System.out.println("image_temp_l1 NULL");
                    break;
                case "3":
                    image_temp_l2 = Image.convert(BitmapFactory.decodeFile(b64,options));
                    Log.e("image_temp_l2","3");
                    icon_camera_temp_l2.setVisibility(View.GONE);
                    icon_gallery_temp_l2.setVisibility(View.VISIBLE);
                    if(image_temp_l2.isEmpty())
                        System.out.println("image_temp_l2 NULL");
                    break;
                case "4":
                    image_temp_l3 = Image.convert(BitmapFactory.decodeFile(b64,options));
                    Log.e("image_temp_l3","4");
                    icon_camera_temp_l3.setVisibility(View.GONE);
                    icon_gallery_temp_l3.setVisibility(View.VISIBLE);
                    if(image_temp_l3.isEmpty())
                        System.out.println("image_temp_l3 NULL");
                    break;
                case "5":
                    image_presion_baja = Image.convert(BitmapFactory.decodeFile(b64,options));
                    Log.e("image_presion_baja","5");
                    icon_camera_presion_baja.setVisibility(View.GONE);
                    icon_gallery_presion_baja.setVisibility(View.VISIBLE);
                    if(image_presion_baja.isEmpty())
                        System.out.println("image_presion_baja NULL");
                    break;
                case "6":
                    image_presion_alta = Image.convert(BitmapFactory.decodeFile(b64,options));
                    Log.e("image_presion_alta","6");
                    icon_camera_presion_alta.setVisibility(View.GONE);
                    icon_gallery_presion_alta.setVisibility(View.VISIBLE);
                    if(image_presion_alta.isEmpty())
                        System.out.println("image_presion_alta NULL");
                    break;
                case "7":
                    image_amp_l1 = Image.convert(BitmapFactory.decodeFile(b64,options));
                    Log.e("image_amp_l1","7");
                    icon_camera_amp_l1.setVisibility(View.GONE);
                    icon_gallery_amp_l1.setVisibility(View.VISIBLE);
                    if(image_amp_l1.isEmpty())
                        System.out.println("image_amp_l1 NULL");
                    break;
                case "8":
                    image_amp_l2 = Image.convert(BitmapFactory.decodeFile(b64,options));
                    Log.e("image_amp_l2","8");
                    icon_camera_amp_l2.setVisibility(View.GONE);
                    icon_gallery_amp_l2.setVisibility(View.VISIBLE);
                    if(image_amp_l2.isEmpty())
                        System.out.println("image_amp_l2 NULL");
                    break;
                case "9":
                    image_amp_l3 = Image.convert(BitmapFactory.decodeFile(b64,options));
                    Log.e("image_amp_l3","9");
                    icon_camera_amp_l3.setVisibility(View.GONE);
                    icon_gallery_amp_l3.setVisibility(View.VISIBLE);
                    if(image_amp_l3.isEmpty())
                        System.out.println("image_amp_l3 NULL");
                    break;
                case "10":
                    image_volt_l1 = Image.convert(BitmapFactory.decodeFile(b64,options));
                    Log.e("image_volt_l1","10");
                    icon_camera_volt_l1.setVisibility(View.GONE);
                    icon_gallery_volt_l1.setVisibility(View.VISIBLE);
                    if(image_volt_l1.isEmpty())
                        System.out.println("image_volt_l1 NULL");
                    break;
                case "11":
                    image_volt_l2 = Image.convert(BitmapFactory.decodeFile(b64,options));
                    Log.e("image_volt_l2","11");
                    icon_camera_volt_l2.setVisibility(View.GONE);
                    icon_gallery_volt_l2.setVisibility(View.VISIBLE);
                    if(image_volt_l2.isEmpty())
                        System.out.println("image_volt_l2 NULL");
                    break;
                case "12":
                    image_volt_l3 = Image.convert(BitmapFactory.decodeFile(b64,options));
                    Log.e("image_volt_l3","12");
                    icon_camera_volt_l3.setVisibility(View.GONE);
                    icon_gallery_volt_l3.setVisibility(View.VISIBLE);
                    if(image_volt_l3.isEmpty())
                        System.out.println("image_volt_l3 NULL");
                    break;

                //despues
                case "13":
                    despues_image_falla = Image.convert(BitmapFactory.decodeFile(b64,options));
                    Log.e("despues_image_falla","13");
                    despues_icon_camera_falla.setVisibility(View.GONE);
                    despues_icon_gallery_falla.setVisibility(View.VISIBLE);
                    if(despues_image_falla.isEmpty())
                        System.out.println("despues_image_falla NULL");

                    if(modo_prueba==1){
                        setAllValuesDespues();
                    }
                    break;
                case "14":
                    despues_image_temp_l1 = Image.convert(BitmapFactory.decodeFile(b64,options));
                    Log.e("despues_image_temp_l1","14");
                    despues_icon_camera_temp_l1.setVisibility(View.GONE);
                    despues_icon_gallery_temp_l1.setVisibility(View.VISIBLE);
                    if(despues_image_temp_l1.isEmpty())
                        System.out.println("despues_image_temp_l1 NULL");
                    break;
                case "15":
                    despues_image_temp_l2 = Image.convert(BitmapFactory.decodeFile(b64,options));
                    Log.e("despues_image_temp_l2","15");
                    despues_icon_camera_temp_l2.setVisibility(View.GONE);
                    despues_icon_gallery_temp_l2.setVisibility(View.VISIBLE);
                    if(despues_image_temp_l2.isEmpty())
                        System.out.println("despues_image_temp_l2 NULL");
                    break;
                case "16":
                    despues_image_temp_l3 = Image.convert(BitmapFactory.decodeFile(b64,options));
                    Log.e("despues_image_temp_l3","16");
                    despues_icon_camera_temp_l3.setVisibility(View.GONE);
                    despues_icon_gallery_temp_l3.setVisibility(View.VISIBLE);
                    if(despues_image_temp_l3.isEmpty())
                        System.out.println("despues_image_temp_l3 NULL");
                    break;
                case "17":
                    despues_image_presion_baja = Image.convert(BitmapFactory.decodeFile(b64,options));
                    Log.e("image_presion_baja","17");
                    despues_icon_camera_presion_baja.setVisibility(View.GONE);
                    despues_icon_gallery_presion_baja.setVisibility(View.VISIBLE);
                    if(despues_image_presion_baja.isEmpty())
                        System.out.println("despues_image_presion_baja NULL");
                    break;
                case "18":
                    despues_image_presion_alta = Image.convert(BitmapFactory.decodeFile(b64,options));
                    Log.e("despues_image_presion_alta","18");
                    despues_icon_camera_presion_alta.setVisibility(View.GONE);
                    despues_icon_gallery_presion_alta.setVisibility(View.VISIBLE);
                    if(despues_image_presion_alta.isEmpty())
                        System.out.println("despues_image_presion_alta NULL");
                    break;
                case "19":
                    despues_image_amp_l1 = Image.convert(BitmapFactory.decodeFile(b64,options));
                    Log.e("image_amp_l1","19");
                    despues_icon_camera_amp_l1.setVisibility(View.GONE);
                    despues_icon_gallery_amp_l1.setVisibility(View.VISIBLE);
                    if(despues_image_amp_l1.isEmpty())
                        System.out.println("image_amp_l1 NULL");
                    break;
                case "20":
                    despues_image_amp_l2 = Image.convert(BitmapFactory.decodeFile(b64,options));
                    Log.e("despues_image_amp_l2","20");
                    despues_icon_camera_amp_l2.setVisibility(View.GONE);
                    despues_icon_gallery_amp_l2.setVisibility(View.VISIBLE);
                    if(despues_image_amp_l2.isEmpty())
                        System.out.println("despues_image_amp_l2 NULL");
                    break;
                case "21":
                    despues_image_amp_l3 = Image.convert(BitmapFactory.decodeFile(b64,options));
                    Log.e("despues_image_amp_l3","21");
                    despues_icon_camera_amp_l3.setVisibility(View.GONE);
                    despues_icon_gallery_amp_l3.setVisibility(View.VISIBLE);
                    if(despues_image_amp_l3.isEmpty())
                        System.out.println("despues_image_amp_l3 NULL");
                    break;
                case "22":
                    despues_image_volt_l1 = Image.convert(BitmapFactory.decodeFile(b64,options));
                    Log.e("despues_image_volt_l1","22");
                    despues_icon_camera_volt_l1.setVisibility(View.GONE);
                    despues_icon_gallery_volt_l1.setVisibility(View.VISIBLE);
                    if(despues_image_volt_l1.isEmpty())
                        System.out.println("despues_image_volt_l1 NULL");
                    break;
                case "23":
                    despues_image_volt_l2 = Image.convert(BitmapFactory.decodeFile(b64,options));
                    Log.e("despues_image_volt_l2","23");
                    despues_icon_camera_volt_l2.setVisibility(View.GONE);
                    despues_icon_gallery_volt_l2.setVisibility(View.VISIBLE);
                    if(despues_image_volt_l2.isEmpty())
                        System.out.println("despues_image_volt_l2 NULL");
                    break;
                case "24":
                    despues_image_volt_l3 = Image.convert(BitmapFactory.decodeFile(b64,options));
                    Log.e("despues_image_volt_l3","24");
                    despues_icon_camera_volt_l3.setVisibility(View.GONE);
                    despues_icon_gallery_volt_l3.setVisibility(View.VISIBLE);
                    if(despues_image_volt_l3.isEmpty())
                        System.out.println("despues_image_volt_l3 NULL");
                    break;
            }
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
                if(cred.getData("image_type").equals("0")){
                    String codigo = data.getStringExtra("codigo");
                    if(tipo_nro_serie==1)
                        et_nro_serie.setText(codigo);
                    else
                        et_cond_nro_serie.setText(codigo);
                }else{
                    galleryAddPic();
                }
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
                        case "1":
                            image_falla = Image.convert(bitmap);
                            Log.e("image_falla","1");
                            icon_camera_falla.setVisibility(View.GONE);
                            icon_gallery_falla.setVisibility(View.VISIBLE);
                            if(image_falla.isEmpty())
                                System.out.println("image_falla NULL");
                            break;
                        case "2":
                            image_temp_l1 = Image.convert(bitmap);
                            Log.e("image_temp_l1","2");
                            icon_camera_temp_l1.setVisibility(View.GONE);
                            icon_gallery_temp_l1.setVisibility(View.VISIBLE);
                            if(image_temp_l1.isEmpty())
                                System.out.println("image_temp_l1 NULL");
                            break;
                        case "3":
                            image_temp_l2 = Image.convert(bitmap);
                            Log.e("image_temp_l2","3");
                            icon_camera_temp_l2.setVisibility(View.GONE);
                            icon_gallery_temp_l2.setVisibility(View.VISIBLE);
                            if(image_temp_l2.isEmpty())
                                System.out.println("image_temp_l2 NULL");
                            break;
                        case "4":
                            image_temp_l3 = Image.convert(bitmap);
                            Log.e("image_temp_l3","4");
                            icon_camera_temp_l3.setVisibility(View.GONE);
                            icon_gallery_temp_l3.setVisibility(View.VISIBLE);
                            if(image_temp_l3.isEmpty())
                                System.out.println("image_temp_l3 NULL");
                            break;
                        case "5":
                            image_presion_baja = Image.convert(bitmap);
                            Log.e("image_presion_baja","5");
                            icon_camera_presion_baja.setVisibility(View.GONE);
                            icon_gallery_presion_baja.setVisibility(View.VISIBLE);
                            if(image_presion_baja.isEmpty())
                                System.out.println("image_presion_baja NULL");
                            break;
                        case "6":
                            image_presion_alta = Image.convert(bitmap);
                            Log.e("image_presion_alta","6");
                            icon_camera_presion_alta.setVisibility(View.GONE);
                            icon_gallery_presion_alta.setVisibility(View.VISIBLE);
                            if(image_presion_alta.isEmpty())
                                System.out.println("image_presion_alta NULL");
                            break;
                        case "7":
                            image_amp_l1 = Image.convert(bitmap);
                            Log.e("image_amp_l1","7");
                            icon_camera_amp_l1.setVisibility(View.GONE);
                            icon_gallery_amp_l1.setVisibility(View.VISIBLE);
                            if(image_amp_l1.isEmpty())
                                System.out.println("image_amp_l1 NULL");
                            break;
                        case "8":
                            image_amp_l2 = Image.convert(bitmap);
                            Log.e("image_amp_l2","8");
                            icon_camera_amp_l2.setVisibility(View.GONE);
                            icon_gallery_amp_l2.setVisibility(View.VISIBLE);
                            if(image_amp_l2.isEmpty())
                                System.out.println("image_amp_l2 NULL");
                            break;
                        case "9":
                            image_amp_l3 = Image.convert(bitmap);
                            Log.e("image_amp_l3","9");
                            icon_camera_amp_l3.setVisibility(View.GONE);
                            icon_gallery_amp_l3.setVisibility(View.VISIBLE);
                            if(image_amp_l3.isEmpty())
                                System.out.println("image_amp_l3 NULL");
                            break;
                        case "10":
                            image_volt_l1 = Image.convert(bitmap);
                            Log.e("image_volt_l1","10");
                            icon_camera_volt_l1.setVisibility(View.GONE);
                            icon_gallery_volt_l1.setVisibility(View.VISIBLE);
                            if(image_volt_l1.isEmpty())
                                System.out.println("image_volt_l1 NULL");
                            break;
                        case "11":
                            image_volt_l2 = Image.convert(bitmap);
                            Log.e("image_volt_l2","11");
                            icon_camera_volt_l2.setVisibility(View.GONE);
                            icon_gallery_volt_l2.setVisibility(View.VISIBLE);
                            if(image_volt_l2.isEmpty())
                                System.out.println("image_volt_l2 NULL");
                            break;
                        case "12":
                            image_volt_l3 = Image.convert(bitmap);
                            Log.e("image_volt_l3","12");
                            icon_camera_volt_l3.setVisibility(View.GONE);
                            icon_gallery_volt_l3.setVisibility(View.VISIBLE);
                            if(image_volt_l3.isEmpty())
                                System.out.println("image_volt_l3 NULL");
                            break;

                        //despues
                        case "13":
                            despues_image_falla = Image.convert(bitmap);
                            Log.e("despues_image_falla","13");
                            despues_icon_camera_falla.setVisibility(View.GONE);
                            despues_icon_gallery_falla.setVisibility(View.VISIBLE);
                            if(despues_image_falla.isEmpty())
                                System.out.println("despues_image_falla NULL");
                            break;
                        case "14":
                            despues_image_temp_l1 = Image.convert(bitmap);
                            Log.e("despues_image_temp_l1","14");
                            despues_icon_camera_temp_l1.setVisibility(View.GONE);
                            despues_icon_gallery_temp_l1.setVisibility(View.VISIBLE);
                            if(despues_image_temp_l1.isEmpty())
                                System.out.println("despues_image_temp_l1 NULL");
                            break;
                        case "15":
                            despues_image_temp_l2 = Image.convert(bitmap);
                            Log.e("despues_image_temp_l2","15");
                            despues_icon_camera_temp_l2.setVisibility(View.GONE);
                            despues_icon_gallery_temp_l2.setVisibility(View.VISIBLE);
                            if(despues_image_temp_l2.isEmpty())
                                System.out.println("despues_image_temp_l2 NULL");
                            break;
                        case "16":
                            despues_image_temp_l3 = Image.convert(bitmap);
                            Log.e("despues_image_temp_l3","16");
                            despues_icon_camera_temp_l3.setVisibility(View.GONE);
                            despues_icon_gallery_temp_l3.setVisibility(View.VISIBLE);
                            if(despues_image_temp_l3.isEmpty())
                                System.out.println("despues_image_temp_l3 NULL");
                            break;
                        case "17":
                            despues_image_presion_baja = Image.convert(bitmap);
                            Log.e("image_presion_baja","17");
                            despues_icon_camera_presion_baja.setVisibility(View.GONE);
                            despues_icon_gallery_presion_baja.setVisibility(View.VISIBLE);
                            if(despues_image_presion_baja.isEmpty())
                                System.out.println("despues_image_presion_baja NULL");
                            break;
                        case "18":
                            despues_image_presion_alta = Image.convert(bitmap);
                            Log.e("despues_image_presion_alta","18");
                            despues_icon_camera_presion_alta.setVisibility(View.GONE);
                            despues_icon_gallery_presion_alta.setVisibility(View.VISIBLE);
                            if(despues_image_presion_alta.isEmpty())
                                System.out.println("despues_image_presion_alta NULL");
                            break;
                        case "19":
                            despues_image_amp_l1 = Image.convert(bitmap);
                            Log.e("image_amp_l1","19");
                            despues_icon_camera_amp_l1.setVisibility(View.GONE);
                            despues_icon_gallery_amp_l1.setVisibility(View.VISIBLE);
                            if(despues_image_amp_l1.isEmpty())
                                System.out.println("image_amp_l1 NULL");
                            break;
                        case "20":
                            despues_image_amp_l2 = Image.convert(bitmap);
                            Log.e("despues_image_amp_l2","20");
                            despues_icon_camera_amp_l2.setVisibility(View.GONE);
                            despues_icon_gallery_amp_l2.setVisibility(View.VISIBLE);
                            if(despues_image_amp_l2.isEmpty())
                                System.out.println("despues_image_amp_l2 NULL");
                            break;
                        case "21":
                            despues_image_amp_l3 = Image.convert(bitmap);
                            Log.e("despues_image_amp_l3","21");
                            despues_icon_camera_amp_l3.setVisibility(View.GONE);
                            despues_icon_gallery_amp_l3.setVisibility(View.VISIBLE);
                            if(despues_image_amp_l3.isEmpty())
                                System.out.println("despues_image_amp_l3 NULL");
                            break;
                        case "22":
                            despues_image_volt_l1 = Image.convert(bitmap);
                            Log.e("despues_image_volt_l1","22");
                            despues_icon_camera_volt_l1.setVisibility(View.GONE);
                            despues_icon_gallery_volt_l1.setVisibility(View.VISIBLE);
                            if(despues_image_volt_l1.isEmpty())
                                System.out.println("despues_image_volt_l1 NULL");
                            break;
                        case "23":
                            despues_image_volt_l2 = Image.convert(bitmap);
                            Log.e("despues_image_volt_l2","23");
                            despues_icon_camera_volt_l2.setVisibility(View.GONE);
                            despues_icon_gallery_volt_l2.setVisibility(View.VISIBLE);
                            if(despues_image_volt_l2.isEmpty())
                                System.out.println("despues_image_volt_l2 NULL");
                            break;
                        case "24":
                            despues_image_volt_l3 = Image.convert(bitmap);
                            Log.e("despues_image_volt_l3","24");
                            despues_icon_camera_volt_l3.setVisibility(View.GONE);
                            despues_icon_gallery_volt_l3.setVisibility(View.VISIBLE);
                            if(despues_image_volt_l3.isEmpty())
                                System.out.println("despues_image_volt_l3 NULL");
                            break;
                    }
                } catch (Exception e) {
                    viewDialog.hideDialog(1);
                    e.printStackTrace();
                }
                break;
        }
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

    void setAllValues()
    {
        image_temp_l1 =image_falla;
        image_temp_l2 =image_falla;
        image_temp_l3 =image_falla;
        image_presion_alta =image_falla;
        image_presion_baja =image_falla;
        image_volt_l1 =image_falla;
        image_volt_l2 =image_falla;
        image_volt_l3 =image_falla;
        image_amp_l1 =image_falla;
        image_amp_l2 =image_falla;
        image_amp_l3 =image_falla;

        icon_camera_temp_l1.setVisibility(View.GONE);
        icon_gallery_temp_l1.setVisibility(View.VISIBLE);
        icon_camera_temp_l2.setVisibility(View.GONE);
        icon_gallery_temp_l2.setVisibility(View.VISIBLE);
        icon_camera_temp_l3.setVisibility(View.GONE);
        icon_gallery_temp_l3.setVisibility(View.VISIBLE);
        icon_camera_presion_baja.setVisibility(View.GONE);
        icon_gallery_presion_baja.setVisibility(View.VISIBLE);
        icon_camera_presion_alta.setVisibility(View.GONE);
        icon_gallery_presion_alta.setVisibility(View.VISIBLE);
        icon_camera_amp_l1.setVisibility(View.GONE);
        icon_gallery_amp_l1.setVisibility(View.VISIBLE);
        icon_camera_amp_l2.setVisibility(View.GONE);
        icon_gallery_amp_l2.setVisibility(View.VISIBLE);
        icon_camera_amp_l3.setVisibility(View.GONE);
        icon_gallery_amp_l3.setVisibility(View.VISIBLE);
        icon_camera_volt_l1.setVisibility(View.GONE);
        icon_gallery_volt_l1.setVisibility(View.VISIBLE);
        icon_camera_volt_l2.setVisibility(View.GONE);
        icon_gallery_volt_l2.setVisibility(View.VISIBLE);
        icon_camera_volt_l3.setVisibility(View.GONE);
        icon_gallery_volt_l3.setVisibility(View.VISIBLE);
    }

    void setAllValuesDespues()
    {
        despues_image_temp_l1 = despues_image_falla;
        despues_image_temp_l2 = despues_image_falla;
        despues_image_temp_l3 = despues_image_falla;
        despues_image_presion_alta = despues_image_falla;
        despues_image_presion_baja = despues_image_falla;
        despues_image_volt_l1 = despues_image_falla;
        despues_image_volt_l2 = despues_image_falla;
        despues_image_volt_l3 = despues_image_falla;
        despues_image_amp_l1 = despues_image_falla;
        despues_image_amp_l2 = despues_image_falla;
        despues_image_amp_l3 = despues_image_falla;

        despues_icon_camera_temp_l1.setVisibility(View.GONE);
        despues_icon_gallery_temp_l1.setVisibility(View.VISIBLE);
        despues_icon_camera_temp_l2.setVisibility(View.GONE);
        despues_icon_gallery_temp_l2.setVisibility(View.VISIBLE);
        despues_icon_camera_temp_l3.setVisibility(View.GONE);
        despues_icon_gallery_temp_l3.setVisibility(View.VISIBLE);
        despues_icon_camera_presion_baja.setVisibility(View.GONE);
        despues_icon_gallery_presion_baja.setVisibility(View.VISIBLE);
        despues_icon_camera_presion_alta.setVisibility(View.GONE);
        despues_icon_gallery_presion_alta.setVisibility(View.VISIBLE);
        despues_icon_camera_amp_l1.setVisibility(View.GONE);
        despues_icon_gallery_amp_l1.setVisibility(View.VISIBLE);
        despues_icon_camera_amp_l2.setVisibility(View.GONE);
        despues_icon_gallery_amp_l2.setVisibility(View.VISIBLE);
        despues_icon_camera_amp_l3.setVisibility(View.GONE);
        icon_gallery_amp_l3.setVisibility(View.VISIBLE);
        despues_icon_camera_volt_l1.setVisibility(View.GONE);
        despues_icon_gallery_volt_l1.setVisibility(View.VISIBLE);
        despues_icon_camera_volt_l2.setVisibility(View.GONE);
        despues_icon_gallery_volt_l2.setVisibility(View.VISIBLE);
        despues_icon_camera_volt_l3.setVisibility(View.GONE);
        despues_icon_gallery_volt_l3.setVisibility(View.VISIBLE);
    }
}
