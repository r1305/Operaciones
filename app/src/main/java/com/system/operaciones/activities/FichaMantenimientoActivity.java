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
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
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

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class FichaMantenimientoActivity extends AppCompatActivity {

    Context ctx;
    Credentials cred;
    /************** MANTENIMIENTO ************************/
    //ANTES
    private ImageView icon_camera_serpentin,icon_camera_bomba,icon_camera_cortina;
    private ImageView icon_gallery_serpentin,icon_gallery_bomba,icon_gallery_cortina;
    private ImageView icon_camera_cond_serpentin,icon_camera_ventilador,icon_camera_carcaza;
    private ImageView icon_gallery_cond_serpentin,icon_gallery_ventilador,icon_gallery_carcaza;
    private String image_serpentin="",image_bomba="",image_cortina="";
    private String image_cond_serpentin="",image_ventilador="",image_carcaza="";

    //DESPUES
    private ImageView despues_icon_camera_serpentin,despues_icon_camera_bomba,despues_icon_camera_cortina;
    private ImageView despues_icon_gallery_serpentin,despues_icon_gallery_bomba,despues_icon_gallery_cortina;
    private ImageView despues_icon_camera_cond_serpentin,despues_icon_camera_ventilador,despues_icon_camera_carcaza;
    private ImageView despues_icon_gallery_cond_serpentin,despues_icon_gallery_ventilador,despues_icon_gallery_carcaza;
    private String despues_image_serpentin="",despues_image_bomba="",despues_image_cortina="";
    private String despues_image_cond_serpentin="",despues_image_ventilador="",despues_image_carcaza="";

    private Button btn_mto_cancelar;
    private Button btn_mto_guardar;

    /************************** LECTURA ***************************/
    //ANTES
    private ImageView icon_camera_temp_serpentin,icon_camera_temp_ambiente,icon_camera_presion_alta,icon_camera_presion_baja,icon_camera_amp_l1,icon_camera_amp_l2,icon_camera_amp_l3,icon_camera_volt_l1,icon_camera_volt_l2,icon_camera_volt_l3;
    private ImageView icon_gallery_temp_serpentin,icon_gallery_temp_ambiente,icon_gallery_presion_alta,icon_gallery_presion_baja,icon_gallery_amp_l1,icon_gallery_amp_l2,icon_gallery_amp_l3,icon_gallery_volt_l1,icon_gallery_volt_l2,icon_gallery_volt_l3;
    private String temp_serpentin="",temp_ambiente="",presion_alta="",presion_baja="",amp_l1="",amp_l2="",amp_l3="",volt_l1="",volt_l2="",volt_l3="";
    private String txt_temp_serpentin="0",txt_temp_ambiente="0",txt_presion_alta="0",txt_presion_baja="0",txt_amp_l1="0",txt_amp_l2="0",txt_amp_l3="0",txt_volt_l1="0",txt_volt_l2="0",txt_volt_l3="0";
    private EditText et_temp_serpentin,et_temp_ambiente,et_presion_alta,et_presion_baja,et_amp_l1,et_amp_l2,et_amp_l3,et_volt_l1,et_volt_l2,et_volt_l3;

    //DESPUES
    private ImageView despues_icon_camera_temp_serpentin,despues_icon_camera_temp_ambiente,despues_icon_camera_presion_alta,despues_icon_camera_presion_baja,despues_icon_camera_amp_l1,despues_icon_camera_amp_l2,despues_icon_camera_amp_l3,despues_icon_camera_volt_l1,despues_icon_camera_volt_l2,despues_icon_camera_volt_l3;
    private ImageView despues_icon_gallery_temp_serpentin,despues_icon_gallery_temp_ambiente,despues_icon_gallery_presion_alta,despues_icon_gallery_presion_baja,despues_icon_gallery_amp_l1,despues_icon_gallery_amp_l2,despues_icon_gallery_amp_l3,despues_icon_gallery_volt_l1,despues_icon_gallery_volt_l2,despues_icon_gallery_volt_l3;
    private String despues_temp_serpentin="",despues_temp_ambiente="",despues_presion_alta="",despues_presion_baja="",despues_amp_l1="",despues_amp_l2="",despues_amp_l3="",despues_volt_l1="",despues_volt_l2="",despues_volt_l3="";
    private String despues_txt_temp_serpentin="0",despues_txt_temp_ambiente="0",despues_txt_presion_alta="0",despues_txt_presion_baja="0",despues_txt_amp_l1="0",despues_txt_amp_l2="0",despues_txt_amp_l3="0",despues_txt_volt_l1="0",despues_txt_volt_l2="0",despues_txt_volt_l3="0";
    private EditText despues_et_temp_serpentin,despues_et_temp_ambiente,despues_et_presion_alta,despues_et_presion_baja,despues_et_amp_l1,despues_et_amp_l2,despues_et_amp_l3,despues_et_volt_l1,despues_et_volt_l2,despues_et_volt_l3;

    private Button btn_lectura_cancelar;
    private Button btn_lectura_guardar;
    /************************ FIRMAS ***************************/

    //Firmas
    String image_signature_cliente="",image_signature_tecnico="";

    private String id,tienda_id;

    AlertDialog alertDialog;
    private String name_tecnico="",dni_tecnico="",cargo_tecnico="",name_cliente="",dni_cliente="",cargo_cliente="";

    int equipo_count=0;
    int cortina_count=0;
    private static final int  CODIGO_INTENT = 2;

    ViewDialog viewDialog;

    int modo_prueba = 0;
    String fase = "0";

    EditText cargo,dni,name;
    ImageView adjunto_preview;
    String adjunto_b64="";
    EditText et_otros;
    Spinner spinner_observaciones;

    JSONObject datos = new JSONObject();
    String datos_guardados = "";
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ficha_mantenimiento);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(false);
        getSupportActionBar().setTitle("Ficha Mantenimiento");

        ctx = this;
        cred = new Credentials(ctx);
        id = getIntent().getStringExtra("id");
        datos_guardados = cred.getData("2-"+id);
        System.out.println("datos_guardados: "+datos_guardados);
        if(!datos_guardados.isEmpty()){
            try {
                datos = (JSONObject)new JSONParser().parse(datos_guardados);
            } catch (ParseException e) {
                e.printStackTrace();
                System.out.println("parse: "+e);
            }
        }else{
            datos.put("modulo",2);
            datos.put("ficha_id",id);
        }

        cred.save_data("2-"+id,datos.toString());
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

        Button btn_lectura_antes = findViewById(R.id.btn_antes);
        Button btn_mto_antes = findViewById(R.id.btn_mto_antes);

        Button btn_lectura_despues = findViewById(R.id.btn_despues);
        Button btn_mto_despues = findViewById(R.id.btn_mto_despues);

        // BTN_ANTES MANTENIMIENTO
        btn_mto_antes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(ctx);
                LayoutInflater inflater = getLayoutInflater();
                View dialogView = inflater.inflate(R.layout.dialog_evidencia_mantenimiento, null);
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

                btn_lectura_cancelar = dialogView.findViewById(R.id.dialog_mto_btn_cancelar);
                btn_lectura_guardar = dialogView.findViewById(R.id.dialog_mto_btn_guardar);
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
                        cred.save_data("2-"+id,datos.toString());
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

        //BTN_DESPUES MANTENIMIENTO
        btn_mto_despues.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(ctx);
                LayoutInflater inflater = getLayoutInflater();
                View dialogView = inflater.inflate(R.layout.dialog_evidencia_mantenimiento, null);
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

                btn_lectura_cancelar = dialogView.findViewById(R.id.dialog_mto_btn_cancelar);
                btn_lectura_guardar = dialogView.findViewById(R.id.dialog_mto_btn_guardar);
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
                        cred.save_data("2-"+id,datos.toString());
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

        //BTN_ANTES LECTURA
        btn_lectura_antes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(ctx);
                LayoutInflater inflater = getLayoutInflater();
                View dialogView = inflater.inflate(R.layout.dialog_lecturas_mantenimiento, null);
                builder.setView(dialogView);
                builder.setCancelable(false);
                icon_camera_temp_serpentin = dialogView.findViewById(R.id.icon_cam_temp_serpentin);
                icon_camera_temp_ambiente = dialogView.findViewById(R.id.icon_cam_temp_ambiente);
                icon_camera_presion_baja = dialogView.findViewById(R.id.icon_cam_presion_baja);
                icon_camera_presion_alta = dialogView.findViewById(R.id.icon_cam_presion_alta);
                icon_camera_amp_l1 = dialogView.findViewById(R.id.icon_cam_amp_l1);
                icon_camera_amp_l2 = dialogView.findViewById(R.id.icon_cam_amp_l2);
                icon_camera_amp_l3 = dialogView.findViewById(R.id.icon_cam_amp_l3);
                icon_camera_volt_l1 = dialogView.findViewById(R.id.icon_cam_volt_l1);
                icon_camera_volt_l2 = dialogView.findViewById(R.id.icon_cam_volt_l2);
                icon_camera_volt_l3 = dialogView.findViewById(R.id.icon_cam_volt_l3);

                et_temp_serpentin = dialogView.findViewById(R.id.et_temp_serpentin);
                et_temp_ambiente = dialogView.findViewById(R.id.et_temp_ambiente);
                et_presion_baja = dialogView.findViewById(R.id.et_presion_baja);
                et_presion_alta = dialogView.findViewById(R.id.et_presion_alta);
                et_amp_l1 = dialogView.findViewById(R.id.et_amp_l1);
                et_amp_l2 = dialogView.findViewById(R.id.et_amp_l2);
                et_amp_l3 = dialogView.findViewById(R.id.et_amp_l3);
                et_volt_l1 = dialogView.findViewById(R.id.et_volt_l1);
                et_volt_l2 = dialogView.findViewById(R.id.et_volt_l2);
                et_volt_l3 = dialogView.findViewById(R.id.et_volt_l3);

                icon_gallery_temp_serpentin = dialogView.findViewById(R.id.icon_gallery_temp_serpentin);
                icon_gallery_temp_ambiente = dialogView.findViewById(R.id.icon_gallery_temp_ambiente);
                icon_gallery_presion_baja = dialogView.findViewById(R.id.icon_gallery_presion_baja);
                icon_gallery_presion_alta = dialogView.findViewById(R.id.icon_gallery_presion_alta);
                icon_gallery_amp_l1 = dialogView.findViewById(R.id.icon_gallery_amp_l1);
                icon_gallery_amp_l2 = dialogView.findViewById(R.id.icon_gallery_amp_l2);
                icon_gallery_amp_l3 = dialogView.findViewById(R.id.icon_gallery_amp_l3);
                icon_gallery_volt_l1 = dialogView.findViewById(R.id.icon_gallery_volt_l1);
                icon_gallery_volt_l2 = dialogView.findViewById(R.id.icon_gallery_volt_l2);
                icon_gallery_volt_l3 = dialogView.findViewById(R.id.icon_gallery_volt_l3);

                btn_lectura_cancelar = dialogView.findViewById(R.id.dialog_lectura_btn_cancelar);
                btn_lectura_guardar = dialogView.findViewById(R.id.dialog_lectura_btn_guardar);

                validateLecturaUpload();

                alertDialog = builder.create();
                alertDialog.show();

                btn_lectura_cancelar.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        alertDialog.dismiss();
                    }
                });
                btn_lectura_guardar.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        txt_temp_serpentin = et_temp_serpentin.getText().toString();
                        txt_temp_ambiente = et_temp_ambiente.getText().toString();
                        txt_presion_baja = et_presion_baja.getText().toString();
                        txt_presion_alta = et_presion_alta.getText().toString();
                        txt_amp_l1 = et_amp_l1.getText().toString();
                        txt_amp_l2 = et_amp_l2.getText().toString();
                        txt_amp_l3 = et_amp_l3.getText().toString();
                        txt_volt_l1 = et_volt_l1.getText().toString();
                        txt_volt_l2 = et_volt_l2.getText().toString();
                        txt_volt_l3 = et_volt_l3.getText().toString();

                        datos.put("txt_temp_serpentin",txt_temp_serpentin);
                        datos.put("txt_temp_ambiente",txt_temp_ambiente);
                        datos.put("txt_presion_baja",txt_presion_baja);
                        datos.put("txt_presion_alta",txt_presion_alta);
                        datos.put("txt_amp_l1",txt_amp_l1);
                        datos.put("txt_amp_l2",txt_amp_l2);
                        datos.put("txt_amp_l3",txt_amp_l3);
                        datos.put("txt_volt_l1",txt_volt_l1);
                        datos.put("txt_volt_l2",txt_volt_l2);
                        datos.put("txt_volt_l3",txt_volt_l3);
                        cred.save_data("2-"+id,datos.toString());
                        alertDialog.dismiss();
                        Toast.makeText(ctx,"Guardado",Toast.LENGTH_SHORT).show();
                    }
                });

                icon_camera_temp_serpentin.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Log.e("temp_serpentin","13");
                        cred.save_data("image_type","13");
                        openChooser();
                    }
                });

                icon_gallery_temp_serpentin.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showPreview(temp_serpentin,13);
                    }
                });

                icon_camera_temp_ambiente.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Log.e("temp_ambiente","14");
                        cred.save_data("image_type","14");
                        openChooser();
                    }
                });

                icon_gallery_temp_ambiente.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showPreview(temp_ambiente,14);
                    }
                });

                icon_camera_presion_baja.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Log.e("presion_baja","15");
                        cred.save_data("image_type","15");
                        openChooser();
                    }
                });

                icon_gallery_presion_baja.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showPreview(presion_baja,15);
                    }
                });

                icon_camera_presion_alta.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Log.e("presion_alta","16");
                        cred.save_data("image_type","16");
                        openChooser();
                    }
                });

                icon_gallery_presion_alta.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showPreview(presion_alta,16);
                    }
                });

                icon_camera_amp_l1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Log.e("amp_l1","17");
                        cred.save_data("image_type","17");
                        openChooser();
                    }
                });

                icon_gallery_amp_l1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showPreview(amp_l1,17);
                    }
                });

                icon_camera_amp_l2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Log.e("amp_l2","18");
                        cred.save_data("image_type","18");
                        openChooser();
                    }
                });

                icon_gallery_amp_l2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showPreview(amp_l2,18);
                    }
                });

                icon_camera_amp_l3.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Log.e("amp_l3","18");
                        cred.save_data("image_type","19");
                        openChooser();
                    }
                });

                icon_gallery_amp_l3.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showPreview(amp_l3,19);
                    }
                });

                //Voltaje
                icon_camera_volt_l1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Log.e("volt_l1","20");
                        cred.save_data("image_type","20");
                        openChooser();
                    }
                });

                icon_gallery_volt_l1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showPreview(volt_l1,20);
                    }
                });

                icon_camera_volt_l2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Log.e("volt_l2","21");
                        cred.save_data("image_type","21");
                        openChooser();
                    }
                });

                icon_gallery_volt_l2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showPreview(volt_l2,21);
                    }
                });

                icon_camera_volt_l3.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Log.e("volt_l3","22");
                        cred.save_data("image_type","22");
                        openChooser();
                    }
                });

                icon_gallery_volt_l3.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showPreview(volt_l3,22);
                    }
                });

                validateLecturaUpload();

            }
        });

        //BTN_DESPUES LECTURA
        btn_lectura_despues.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(ctx);
                LayoutInflater inflater = getLayoutInflater();
                View dialogView = inflater.inflate(R.layout.dialog_lecturas_mantenimiento, null);
                builder.setView(dialogView);
                builder.setCancelable(false);
                despues_icon_camera_temp_serpentin = dialogView.findViewById(R.id.icon_cam_temp_serpentin);
                despues_icon_camera_temp_ambiente = dialogView.findViewById(R.id.icon_cam_temp_ambiente);
                despues_icon_camera_presion_baja = dialogView.findViewById(R.id.icon_cam_presion_baja);
                despues_icon_camera_presion_alta = dialogView.findViewById(R.id.icon_cam_presion_alta);
                despues_icon_camera_amp_l1 = dialogView.findViewById(R.id.icon_cam_amp_l1);
                despues_icon_camera_amp_l2 = dialogView.findViewById(R.id.icon_cam_amp_l2);
                despues_icon_camera_amp_l3 = dialogView.findViewById(R.id.icon_cam_amp_l3);
                despues_icon_camera_volt_l1 = dialogView.findViewById(R.id.icon_cam_volt_l1);
                despues_icon_camera_volt_l2 = dialogView.findViewById(R.id.icon_cam_volt_l2);
                despues_icon_camera_volt_l3 = dialogView.findViewById(R.id.icon_cam_volt_l3);

                despues_et_temp_serpentin = dialogView.findViewById(R.id.et_temp_serpentin);
                despues_et_temp_ambiente = dialogView.findViewById(R.id.et_temp_ambiente);
                despues_et_presion_baja = dialogView.findViewById(R.id.et_presion_baja);
                despues_et_presion_alta = dialogView.findViewById(R.id.et_presion_alta);
                despues_et_amp_l1 = dialogView.findViewById(R.id.et_amp_l1);
                despues_et_amp_l2 = dialogView.findViewById(R.id.et_amp_l2);
                despues_et_amp_l3 = dialogView.findViewById(R.id.et_amp_l3);
                despues_et_volt_l1 = dialogView.findViewById(R.id.et_volt_l1);
                despues_et_volt_l2 = dialogView.findViewById(R.id.et_volt_l2);
                despues_et_volt_l3 = dialogView.findViewById(R.id.et_volt_l3);

                despues_icon_gallery_temp_serpentin = dialogView.findViewById(R.id.icon_gallery_temp_serpentin);
                despues_icon_gallery_temp_ambiente = dialogView.findViewById(R.id.icon_gallery_temp_ambiente);
                despues_icon_gallery_presion_baja = dialogView.findViewById(R.id.icon_gallery_presion_baja);
                despues_icon_gallery_presion_alta = dialogView.findViewById(R.id.icon_gallery_presion_alta);
                despues_icon_gallery_amp_l1 = dialogView.findViewById(R.id.icon_gallery_amp_l1);
                despues_icon_gallery_amp_l2 = dialogView.findViewById(R.id.icon_gallery_amp_l2);
                despues_icon_gallery_amp_l3 = dialogView.findViewById(R.id.icon_gallery_amp_l3);
                despues_icon_gallery_volt_l1 = dialogView.findViewById(R.id.icon_gallery_volt_l1);
                despues_icon_gallery_volt_l2 = dialogView.findViewById(R.id.icon_gallery_volt_l2);
                despues_icon_gallery_volt_l3 = dialogView.findViewById(R.id.icon_gallery_volt_l3);

                btn_lectura_cancelar = dialogView.findViewById(R.id.dialog_lectura_btn_cancelar);
                btn_lectura_guardar = dialogView.findViewById(R.id.dialog_lectura_btn_guardar);

                validateLecturaDespuesUpload();

                alertDialog = builder.create();
                alertDialog.show();

                btn_lectura_cancelar.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        alertDialog.dismiss();
                    }
                });
                btn_lectura_guardar.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        despues_txt_temp_serpentin = despues_et_temp_serpentin.getText().toString();
                        despues_txt_temp_ambiente = despues_et_temp_ambiente.getText().toString();
                        despues_txt_presion_baja = despues_et_presion_baja.getText().toString();
                        despues_txt_presion_alta = despues_et_presion_alta.getText().toString();
                        despues_txt_amp_l1 = despues_et_amp_l1.getText().toString();
                        despues_txt_amp_l2 = despues_et_amp_l2.getText().toString();
                        despues_txt_amp_l3 = despues_et_amp_l3.getText().toString();
                        despues_txt_volt_l1 = despues_et_volt_l1.getText().toString();
                        despues_txt_volt_l2 = despues_et_volt_l2.getText().toString();
                        despues_txt_volt_l3 = despues_et_volt_l3.getText().toString();
                        cred.save_data("2-"+id,datos.toString());
                        alertDialog.dismiss();
                        Toast.makeText(ctx,"Guardado",Toast.LENGTH_SHORT).show();
                    }
                });

                despues_icon_camera_temp_serpentin.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Log.e("despues_temp_serpentin","23");
                        cred.save_data("image_type","23");
                        openChooser();
                    }
                });

                despues_icon_gallery_temp_serpentin.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showPreview(despues_temp_serpentin,23);
                    }
                });

                despues_icon_camera_temp_ambiente.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Log.e("despues_temp_ambiente","24");
                        cred.save_data("image_type","24");
                        openChooser();
                    }
                });

                despues_icon_gallery_temp_ambiente.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showPreview(despues_temp_ambiente,24);
                    }
                });

                despues_icon_camera_presion_baja.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Log.e("despues_presion_baja","25");
                        cred.save_data("image_type","25");
                        openChooser();
                    }
                });

                despues_icon_gallery_presion_baja.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showPreview(despues_presion_baja,25);
                    }
                });

                despues_icon_camera_presion_alta.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Log.e("despues_presion_alta","26");
                        cred.save_data("image_type","26");
                        openChooser();
                    }
                });

                despues_icon_gallery_presion_alta.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showPreview(despues_presion_alta,26);
                    }
                });

                despues_icon_camera_amp_l1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Log.e("despues_amp_l1","27");
                        cred.save_data("image_type","27");
                        openChooser();
                    }
                });

                despues_icon_gallery_amp_l1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showPreview(despues_amp_l1,27);
                    }
                });

                despues_icon_camera_amp_l2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Log.e("despues_amp_l2","28");
                        cred.save_data("image_type","28");
                        openChooser();
                    }
                });

                despues_icon_gallery_amp_l2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showPreview(despues_amp_l2,28);
                    }
                });

                despues_icon_camera_amp_l3.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Log.e("despues_amp_l3","29");
                        cred.save_data("image_type","29");
                        openChooser();
                    }
                });

                despues_icon_gallery_amp_l3.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showPreview(despues_amp_l3,29);
                    }
                });

                //Voltaje
                despues_icon_camera_volt_l1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Log.e("despues_volt_l1","30");
                        cred.save_data("image_type","30");
                        openChooser();
                    }
                });

                despues_icon_gallery_volt_l1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showPreview(despues_volt_l1,30);
                    }
                });

                despues_icon_camera_volt_l2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Log.e("despues_volt_l2","31");
                        cred.save_data("image_type","31");
                        openChooser();
                    }
                });

                despues_icon_gallery_volt_l2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showPreview(despues_volt_l2,31);
                    }
                });

                despues_icon_camera_volt_l3.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Log.e("despues_volt_l3","32");
                        cred.save_data("image_type","32");
                        openChooser();
                    }
                });

                despues_icon_gallery_volt_l3.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showPreview(despues_volt_l3,32);
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
                //ANTES LECTURAS
                if(temp_serpentin.isEmpty()){
                    Toast.makeText(ctx,"Debe cargar foto antes del mantenimiento de la temperatura del serpentín",Toast.LENGTH_SHORT).show();
                    return;
                }
                if(temp_ambiente.isEmpty()){
                    Toast.makeText(ctx,"Debe cargar foto antes del mantenimiento de la temperatura del ambiente",Toast.LENGTH_SHORT).show();
                    return;
                }
                if(presion_baja.isEmpty()){
                    Toast.makeText(ctx,"Debe cargar foto antes del mantenimiento de la presión baja",Toast.LENGTH_SHORT).show();
                    return;
                }
                if(presion_alta.isEmpty()){
                    Toast.makeText(ctx,"Debe cargar foto antes del mantenimiento de la presión alta",Toast.LENGTH_SHORT).show();
                    return;
                }
                if(amp_l1.isEmpty()){
                    Toast.makeText(ctx,"Debe cargar foto antes del mantenimiento del amperaje de la Línea 1",Toast.LENGTH_SHORT).show();
                    return;
                }
                if(amp_l2.isEmpty()){
                    Toast.makeText(ctx,"Debe cargar foto antes del mantenimiento del amperaje de la Línea 2",Toast.LENGTH_SHORT).show();
                    return;
                }
                if(fase!=null && !fase.equals("1")){
                    if(amp_l3.isEmpty()){
                        Toast.makeText(ctx,"Debe cargar foto antes del mantenimiento del voltaje de la Línea 3",Toast.LENGTH_SHORT).show();
                        return;
                    }
                }
                if(volt_l1.isEmpty()){
                    Toast.makeText(ctx,"Debe cargar foto antes del mantenimiento del voltaje de la Línea 1",Toast.LENGTH_SHORT).show();
                    return;
                }
                if(volt_l2.isEmpty()){
                    Toast.makeText(ctx,"Debe cargar foto antes del mantenimiento del voltaje de la Línea 2",Toast.LENGTH_SHORT).show();
                    return;
                }
                if(fase!=null && !fase.equals("1")){
                    if(volt_l3.isEmpty()){
                        Toast.makeText(ctx,"Debe cargar foto antes del mantenimiento del amperaje de la Línea 3",Toast.LENGTH_SHORT).show();
                        return;
                    }
                }
                //DESPUES LECTURAS
                if(despues_temp_serpentin.isEmpty()){
                    Toast.makeText(ctx,"Debe cargar foto después del mantenimiento de la temperatura del serpentín",Toast.LENGTH_SHORT).show();
                    return;
                }
                if(despues_temp_ambiente.isEmpty()){
                    Toast.makeText(ctx,"Debe cargar foto después del mantenimiento de la temperatura del ambiente",Toast.LENGTH_SHORT).show();
                    return;
                }
                if(despues_presion_baja.isEmpty()){
                    Toast.makeText(ctx,"Debe cargar foto después del mantenimiento de la presión baja",Toast.LENGTH_SHORT).show();
                    return;
                }
                if(despues_presion_alta.isEmpty()){
                    Toast.makeText(ctx,"Debe cargar foto después del mantenimiento de la presión alta",Toast.LENGTH_SHORT).show();
                    return;
                }
                if(despues_amp_l1.isEmpty()){
                    Toast.makeText(ctx,"Debe cargar foto después del mantenimiento del amperaje de la Línea 1",Toast.LENGTH_SHORT).show();
                    return;
                }
                if(despues_amp_l2.isEmpty()){
                    Toast.makeText(ctx,"Debe cargar foto después del mantenimiento del amperaje de la Línea 2",Toast.LENGTH_SHORT).show();
                    return;
                }
                if(fase!=null && !fase.equals("1")){
                    if(despues_amp_l3.isEmpty()){
                        Toast.makeText(ctx,"Debe cargar foto después del mantenimiento del voltaje de la Línea 3",Toast.LENGTH_SHORT).show();
                        return;
                    }
                }
                if(despues_volt_l1.isEmpty()){
                    Toast.makeText(ctx,"Debe cargar foto después del mantenimiento del voltaje de la Línea 1",Toast.LENGTH_SHORT).show();
                    return;
                }
                if(despues_volt_l2.isEmpty()){
                    Toast.makeText(ctx,"Debe cargar foto después del mantenimiento del voltaje de la Línea 2",Toast.LENGTH_SHORT).show();
                    return;
                }
                if(fase!=null && !fase.equals("1")){
                    if(despues_volt_l3.isEmpty()){
                        Toast.makeText(ctx,"Debe cargar foto después del mantenimiento del voltaje de la Línea 3",Toast.LENGTH_SHORT).show();
                        return;
                    }
                }
                //ANTES MANTENIMIENTO
                if(image_serpentin.isEmpty()){
                    Toast.makeText(ctx,"Debe cargar foto antes del mantenimiento del serpentín",Toast.LENGTH_SHORT).show();
                    return;
                }
                if(image_bomba.isEmpty()){
                    Toast.makeText(ctx,"Debe cargar foto antes del mantenimiento de la bomba",Toast.LENGTH_SHORT).show();
                    return;
                }
                if(image_cortina.isEmpty()){
                    Toast.makeText(ctx,"Debe cargar foto antes del mantenimiento de la cortina de aire",Toast.LENGTH_SHORT).show();
                    return;
                }

                if(image_cond_serpentin.isEmpty())
                {
                    Toast.makeText(ctx,"Debe cargar foto antes del mantenimiento del serpentín de la condensadora",Toast.LENGTH_SHORT).show();
                    return;
                }
                if(image_ventilador.isEmpty())
                {
                    Toast.makeText(ctx,"Debe cargar foto antes del mantenimiento del ventilador",Toast.LENGTH_SHORT).show();
                    return;
                }
                if(image_carcaza.isEmpty())
                {
                    Toast.makeText(ctx,"Debe cargar foto antes del mantenimiento de la carcaza",Toast.LENGTH_SHORT).show();
                    return;
                }

                if(despues_image_serpentin.isEmpty()){
                    Toast.makeText(ctx,"Debe cargar foto después del mantenimiento del serpentín",Toast.LENGTH_SHORT).show();
                    return;
                }
                if(despues_image_bomba.isEmpty()){
                    Toast.makeText(ctx,"Debe cargar foto después del mantenimiento de la bomba",Toast.LENGTH_SHORT).show();
                    return;
                }
                if(despues_image_cortina.isEmpty()){
                    Toast.makeText(ctx,"Debe cargar foto después del mantenimiento de la cortina de aire",Toast.LENGTH_SHORT).show();
                    return;
                }

                if(despues_image_cond_serpentin.isEmpty())
                {
                    Toast.makeText(ctx,"Debe cargar foto después del mantenimiento del serpentín de la condensadora",Toast.LENGTH_SHORT).show();
                    return;
                }
                if(despues_image_ventilador.isEmpty())
                {
                    Toast.makeText(ctx,"Debe cargar foto después del mantenimiento del ventilador",Toast.LENGTH_SHORT).show();
                    return;
                }
                if(despues_image_carcaza.isEmpty())
                {
                    Toast.makeText(ctx,"Debe cargar foto después del mantenimiento de la carcaza",Toast.LENGTH_SHORT).show();
                    return;
                }

                // FIRMAS CLIENTE Y TECNICO
                if(image_signature_cliente.isEmpty()){
                    Toast.makeText(ctx,"Debe hacer firmar al cliente",Toast.LENGTH_SHORT).show();
                    return;
                }
                if(name_cliente.isEmpty()){
                    Toast.makeText(ctx,"Debe ingresar NOMBRE del cliente",Toast.LENGTH_SHORT).show();
                    return;
                }
                if(dni_cliente.isEmpty()){
                    Toast.makeText(ctx,"Debe ingresar DNI del cliente",Toast.LENGTH_SHORT).show();
                    return;
                }
                if(cargo_cliente.isEmpty()){
                    Toast.makeText(ctx,"Debe ingresar CARGO del cliente",Toast.LENGTH_SHORT).show();
                    return;
                }

                if(image_signature_tecnico.isEmpty()){
                    Toast.makeText(ctx,"Debes firmar antes de finalizar",Toast.LENGTH_SHORT).show();
                    return;
                }
                if(name_tecnico.isEmpty()){
                    Toast.makeText(ctx,"Debe ingresar NOMBRE del tecnico",Toast.LENGTH_SHORT).show();
                    return;
                }
                if(dni_tecnico.isEmpty()){
                    Toast.makeText(ctx,"Debe ingresar DNI del tecnico",Toast.LENGTH_SHORT).show();
                    return;
                }
                if(cargo_tecnico.isEmpty()){
                    Toast.makeText(ctx,"Debe ingresar CARGO del tecnico",Toast.LENGTH_SHORT).show();
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
                Button limpiar = dialogView.findViewById(R.id.dialog_signature_btn_limpiar);
                name = dialogView.findViewById(R.id.dialog_signature_et_name);
                dni = dialogView.findViewById(R.id.dialog_signature_et_dni);
                cargo = dialogView.findViewById(R.id.dialog_signature_et_cargo);
                Log.e("tipo_proveedor",tipo_proveedor);
                if(tipo_proveedor.equals("1")){
                    getDatosTecnico(tecnico_id);
                }
                final SignaturePad signaturePad = dialogView.findViewById(R.id.dialog_signature);
                if(!datos_guardados.isEmpty()){
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
                    dialog.dismiss();
                }
            });
        }


        AlertDialog alert_preview = builder.create();
        alert_preview.show();
    }

    void cleanPhoto(int element)
    {
        switch (element){
            //MANTENIMIENTO Antes
            case 1:
                image_serpentin="";
                icon_camera_serpentin.setVisibility(View.VISIBLE);
                icon_gallery_serpentin.setVisibility(View.GONE);
                datos.put("image_serpentin",image_serpentin);
                cred.save_data("2-"+id,datos.toString());
                break;
            case 2:
                image_bomba="";
                icon_gallery_bomba.setVisibility(View.GONE);
                icon_camera_bomba.setVisibility(View.VISIBLE);
                datos.put("image_bomba",image_bomba);
                cred.save_data("2-"+id,datos.toString());
                break;
            case 3:
                image_cortina="";
                icon_gallery_cortina.setVisibility(View.GONE);
                icon_camera_cortina.setVisibility(View.VISIBLE);
                datos.put("image_cortina",image_cortina);
                cred.save_data("2-"+id,datos.toString());
                break;
            case 4:
                image_cond_serpentin="";
                icon_gallery_cond_serpentin.setVisibility(View.GONE);
                icon_camera_cond_serpentin.setVisibility(View.VISIBLE);
                datos.put("image_cond_serpentin",image_cond_serpentin);
                cred.save_data("2-"+id,datos.toString());
                break;
            case 5:
                image_ventilador="";
                icon_gallery_ventilador.setVisibility(View.GONE);
                icon_camera_ventilador.setVisibility(View.VISIBLE);
                datos.put("image_ventilador",image_ventilador);
                cred.save_data("2-"+id,datos.toString());
                break;
            case 6:
                image_carcaza="";
                icon_gallery_carcaza.setVisibility(View.GONE);
                icon_camera_carcaza.setVisibility(View.VISIBLE);
                datos.put("image_carcaza",image_carcaza);
                cred.save_data("2-"+id,datos.toString());
                break;
            //MANTENIMIENTO Despues
            case 7:
                despues_image_serpentin="";
                despues_icon_gallery_serpentin.setVisibility(View.GONE);
                despues_icon_camera_serpentin.setVisibility(View.VISIBLE);
                datos.put("despues_image_serpentin",despues_image_serpentin);
                cred.save_data("2-"+id,datos.toString());
                break;
            case 8:
                despues_image_bomba="";
                despues_icon_gallery_bomba.setVisibility(View.GONE);
                despues_icon_camera_bomba.setVisibility(View.VISIBLE);
                datos.put("despues_image_bomba",despues_image_bomba);
                cred.save_data("2-"+id,datos.toString());
                break;
            case 9:
                despues_image_cortina="";
                despues_icon_gallery_cortina.setVisibility(View.GONE);
                despues_icon_camera_cortina.setVisibility(View.VISIBLE);
                datos.put("despues_image_cortina",despues_image_cortina);
                cred.save_data("2-"+id,datos.toString());
                break;
            case 10:
                despues_image_cond_serpentin="";
                despues_icon_gallery_cond_serpentin.setVisibility(View.GONE);
                despues_icon_camera_cond_serpentin.setVisibility(View.VISIBLE);
                datos.put("despues_image_cond_serpentin",despues_image_cond_serpentin);
                cred.save_data("2-"+id,datos.toString());
                break;
            case 11:
                despues_image_ventilador="";
                despues_icon_gallery_ventilador.setVisibility(View.GONE);
                despues_icon_camera_ventilador.setVisibility(View.VISIBLE);
                datos.put("despues_image_ventilador",despues_image_ventilador);
                cred.save_data("2-"+id,datos.toString());
                break;
            case 12:
                despues_image_carcaza="";
                despues_icon_gallery_carcaza.setVisibility(View.GONE);
                despues_icon_camera_carcaza.setVisibility(View.VISIBLE);
                datos.put("despues_image_carcaza",despues_image_carcaza);
                cred.save_data("2-"+id,datos.toString());
                break;
            //LECTURAS ANTES
            case 13:
                temp_serpentin="";
                icon_camera_temp_serpentin.setVisibility(View.GONE);
                icon_gallery_temp_serpentin.setVisibility(View.VISIBLE);
                datos.put("temp_serpentin",temp_serpentin);
                cred.save_data("2-"+id,datos.toString());
                break;
            case 14:
                temp_ambiente="";
                icon_camera_temp_ambiente.setVisibility(View.GONE);
                icon_gallery_temp_ambiente.setVisibility(View.VISIBLE);
                datos.put("temp_ambiente",temp_ambiente);
                cred.save_data("2-"+id,datos.toString());
                break;
            case 15:
                presion_baja="";
                icon_camera_presion_baja.setVisibility(View.GONE);
                icon_gallery_presion_baja.setVisibility(View.VISIBLE);
                datos.put("presion_baja",presion_baja);
                cred.save_data("2-"+id,datos.toString());
                break;
            case 16:
                presion_alta="";
                icon_camera_presion_alta.setVisibility(View.GONE);
                icon_gallery_presion_alta.setVisibility(View.VISIBLE);
                datos.put("presion_alta",presion_alta);
                cred.save_data("2-"+id,datos.toString());
                break;
            case 17:
                amp_l1="";
                icon_camera_amp_l1.setVisibility(View.GONE);
                icon_gallery_amp_l1.setVisibility(View.VISIBLE);
                datos.put("amp_l1",amp_l1);
                cred.save_data("2-"+id,datos.toString());
                break;
            case 18:
                amp_l2="";
                icon_camera_amp_l2.setVisibility(View.GONE);
                icon_gallery_amp_l2.setVisibility(View.VISIBLE);
                datos.put("amp_l2",amp_l2);
                cred.save_data("2-"+id,datos.toString());
                break;
            case 19:
                amp_l3="";
                icon_camera_amp_l3.setVisibility(View.GONE);
                icon_gallery_amp_l3.setVisibility(View.VISIBLE);
                datos.put("amp_l3",amp_l3);
                cred.save_data("2-"+id,datos.toString());
                break;
            case 20:
                amp_l1="";
                icon_camera_volt_l1.setVisibility(View.GONE);
                icon_gallery_volt_l1.setVisibility(View.VISIBLE);
                datos.put("amp_l1",amp_l1);
                cred.save_data("2-"+id,datos.toString());
                break;
            case 21:
                volt_l2="";
                icon_camera_volt_l2.setVisibility(View.GONE);
                icon_gallery_volt_l2.setVisibility(View.VISIBLE);
                datos.put("volt_l2",volt_l2);
                cred.save_data("2-"+id,datos.toString());
                break;
            case 22:
                volt_l3="";
                icon_camera_volt_l3.setVisibility(View.GONE);
                icon_gallery_volt_l3.setVisibility(View.VISIBLE);
                datos.put("volt_l3",volt_l3);
                cred.save_data("2-"+id,datos.toString());
                break;
            //LECTURAS DESPUES
            case 23:
                despues_temp_serpentin="";
                despues_icon_camera_temp_serpentin.setVisibility(View.GONE);
                despues_icon_gallery_temp_serpentin.setVisibility(View.VISIBLE);
                datos.put("despues_temp_serpentin",despues_temp_serpentin);
                cred.save_data("2-"+id,datos.toString());
                break;
            case 24:
                despues_temp_ambiente="";
                despues_icon_camera_temp_ambiente.setVisibility(View.GONE);
                despues_icon_gallery_temp_ambiente.setVisibility(View.VISIBLE);
                datos.put("despues_temp_ambiente",despues_temp_ambiente);
                cred.save_data("2-"+id,datos.toString());
                break;
            case 25:
                despues_presion_baja="";
                despues_icon_camera_presion_baja.setVisibility(View.GONE);
                despues_icon_gallery_presion_baja.setVisibility(View.VISIBLE);
                datos.put("despues_presion_baja",despues_presion_baja);
                cred.save_data("2-"+id,datos.toString());
                break;
            case 26:
                despues_presion_alta="";
                icon_camera_presion_alta.setVisibility(View.GONE);
                despues_icon_gallery_presion_alta.setVisibility(View.VISIBLE);
                datos.put("despues_presion_alta",despues_presion_alta);
                cred.save_data("2-"+id,datos.toString());
                break;
            case 27:
                despues_amp_l1="";
                despues_icon_camera_amp_l1.setVisibility(View.GONE);
                despues_icon_gallery_amp_l1.setVisibility(View.VISIBLE);
                datos.put("despues_amp_l1",despues_amp_l1);
                cred.save_data("2-"+id,datos.toString());
                break;
            case 28:
                despues_amp_l2="";
                despues_icon_camera_amp_l2.setVisibility(View.GONE);
                despues_icon_gallery_amp_l2.setVisibility(View.VISIBLE);
                datos.put("despues_amp_l2",despues_amp_l2);
                cred.save_data("2-"+id,datos.toString());
                break;
            case 29:
                despues_amp_l3="";
                despues_icon_camera_amp_l3.setVisibility(View.GONE);
                despues_icon_gallery_amp_l3.setVisibility(View.VISIBLE);
                datos.put("despues_amp_l3",despues_amp_l3);
                cred.save_data("2-"+id,datos.toString());
                break;
            case 30:
                despues_amp_l1="";
                despues_icon_camera_volt_l1.setVisibility(View.GONE);
                despues_icon_gallery_volt_l1.setVisibility(View.VISIBLE);
                datos.put("despues_amp_l1",despues_amp_l1);
                cred.save_data("2-"+id,datos.toString());
                break;
            case 31:
                despues_volt_l2="";
                despues_icon_camera_volt_l2.setVisibility(View.GONE);
                despues_icon_gallery_volt_l2.setVisibility(View.VISIBLE);
                datos.put("despues_volt_l2",despues_volt_l2);
                cred.save_data("2-"+id,datos.toString());
                break;
            case 32:
                despues_volt_l3="";
                despues_icon_camera_volt_l3.setVisibility(View.GONE);
                despues_icon_gallery_volt_l3.setVisibility(View.VISIBLE);
                datos.put("despues_volt_l3",despues_volt_l3);
                cred.save_data("2-"+id,datos.toString());
                break;
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        this.finishAffinity();
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

                //MANTENIMIENTO ANTES
                params.put("img_serpentin", image_serpentin);
                params.put("img_bomba", image_bomba);
                params.put("img_cortina", image_cortina);
                params.put("img_cond_serpentin", image_cond_serpentin);
                params.put("img_ventilador", image_ventilador);
                params.put("img_carcaza", image_carcaza);

                //LECTURAS ANTES
                params.put("temp_serpentin", temp_serpentin);
                params.put("temp_ambiente", temp_ambiente);
                params.put("presion_baja", presion_baja);
                params.put("presion_alta", presion_alta);
                params.put("amp_l1", amp_l1);
                params.put("amp_l2", amp_l2);
                params.put("amp_l3", amp_l3);
                params.put("volt_l1", volt_l1);
                params.put("volt_l2", volt_l2);
                params.put("volt_l3", volt_l3);

                params.put("txt_temp_serpentin", txt_temp_serpentin);
                params.put("txt_temp_ambiente", txt_temp_ambiente);
                params.put("txt_presion_baja", txt_presion_baja);
                params.put("txt_presion_alta", txt_presion_alta);
                params.put("txt_amp_l1", txt_amp_l1);
                params.put("txt_amp_l2", txt_amp_l2);
                params.put("txt_amp_l3", txt_amp_l3);
                params.put("txt_volt_l1", txt_volt_l1);
                params.put("txt_volt_l2", txt_volt_l2);
                params.put("txt_volt_l3", txt_volt_l3);

                //MANTENIMIENTO DESPUES
                params.put("despues_img_serpentin", despues_image_serpentin);
                params.put("despues_img_bomba", despues_image_bomba);
                params.put("despues_img_cortina", despues_image_cortina);
                params.put("despues_img_cond_serpentin", despues_image_cond_serpentin);
                params.put("despues_img_ventilador", despues_image_ventilador);
                params.put("despues_img_carcaza", despues_image_carcaza);

                //LECTURAS DESPUES
                params.put("despues_temp_serpentin", despues_temp_serpentin);
                params.put("despues_temp_ambiente", despues_temp_ambiente);
                params.put("despues_presion_baja", despues_presion_baja);
                params.put("despues_presion_alta", despues_presion_alta);
                params.put("despues_amp_l1", despues_amp_l1);
                params.put("despues_amp_l2", despues_amp_l2);
                params.put("despues_amp_l3", despues_amp_l3);
                params.put("despues_volt_l1", despues_volt_l1);
                params.put("despues_volt_l2", despues_volt_l2);
                params.put("despues_volt_l3", despues_volt_l3);

                params.put("despues_txt_temp_serpentin", despues_txt_temp_serpentin);
                params.put("despues_txt_temp_ambiente", despues_txt_temp_ambiente);
                params.put("despues_txt_presion_baja", despues_txt_presion_baja);
                params.put("despues_txt_presion_alta", despues_txt_presion_alta);
                params.put("despues_txt_amp_l1", despues_txt_amp_l1);
                params.put("despues_txt_amp_l2", despues_txt_amp_l2);
                params.put("despues_txt_amp_l3", despues_txt_amp_l3);
                params.put("despues_txt_volt_l1", despues_txt_volt_l1);
                params.put("despues_txt_volt_l2", despues_txt_volt_l2);
                params.put("despues_txt_volt_l3", despues_txt_volt_l3);

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

    private void createFicha(final String mantenimiento_id)
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
                                        new Utils().sendFichaMantenimiento(tienda_id,mantenimiento_id,ctx);
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
                params.put("mantenimiento_id", mantenimiento_id);
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

    void validateUploadDespues()
    {
        if(!datos_guardados.isEmpty()){
            despues_image_serpentin = (String)datos.get("despues_image_serpentin")==null?"":(String)datos.get("despues_image_serpentin");
            despues_image_bomba = (String)datos.get("despues_image_bomba")==null?"":(String)datos.get("despues_image_bomba");
            despues_image_cortina = (String)datos.get("despues_image_cortina")==null?"":(String)datos.get("despues_image_cortina");
            despues_image_cond_serpentin = (String)datos.get("despues_image_cond_serpentin")==null?"":(String)datos.get("despues_image_cond_serpentin");
            despues_image_ventilador = (String)datos.get("despues_image_ventilador")==null?"":(String)datos.get("despues_image_ventilador");
            despues_image_carcaza = (String)datos.get("despues_image_carcaza")==null?"":(String)datos.get("despues_image_carcaza");
        }
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
        if(!datos_guardados.isEmpty()){
            image_serpentin = (String)datos.get("image_serpentin")==null?"":(String)datos.get("image_serpentin");
            image_bomba = (String)datos.get("image_bomba")==null?"":(String)datos.get("image_bomba");
            image_cortina = (String)datos.get("image_cortina")==null?"":(String)datos.get("image_cortina");
            image_cond_serpentin = (String)datos.get("image_cond_serpentin")==null?"":(String)datos.get("image_cond_serpentin");
            image_ventilador = (String)datos.get("image_ventilador")==null?"":(String)datos.get("image_ventilador");
            image_carcaza = (String)datos.get("image_carcaza")==null?"":(String)datos.get("image_carcaza");
        }
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

    void validateLecturaUpload()
    {
        if(!datos_guardados.isEmpty()){
            txt_temp_serpentin = (String)datos.get("txt_temp_serpentin")==null?"0":(String)datos.get("txt_temp_serpentin");
            txt_temp_ambiente = (String)datos.get("txt_temp_ambiente")==null?"0":(String)datos.get("txt_temp_ambiente");
            txt_presion_baja = (String)datos.get("txt_presion_baja")==null?"0":(String)datos.get("txt_presion_baja");
            txt_presion_alta = (String)datos.get("txt_presion_alta")==null?"0":(String)datos.get("txt_presion_alta");
            txt_amp_l1 = (String)datos.get("txt_amp_l1")==null?"0":(String)datos.get("txt_amp_l1");
            txt_amp_l2 = (String)datos.get("txt_amp_l2")==null?"0":(String)datos.get("txt_amp_l2");
            txt_amp_l3 = (String)datos.get("txt_amp_l3")==null?"0":(String)datos.get("txt_amp_l3");
            txt_volt_l1 = (String)datos.get("txt_volt_l1")==null?"0":(String)datos.get("txt_volt_l1");
            txt_volt_l2 = (String)datos.get("txt_volt_l2")==null?"0":(String)datos.get("txt_volt_l2");
            txt_volt_l3 = (String)datos.get("txt_volt_l3")==null?"0":(String)datos.get("txt_volt_l3");

            temp_serpentin = (String)datos.get("temp_serpentin")==null?"":(String)datos.get("temp_serpentin");
            temp_ambiente = (String)datos.get("temp_ambiente")==null?"":(String)datos.get("temp_ambiente");
            presion_baja = (String)datos.get("presion_baja")==null?"":(String)datos.get("presion_baja");
            presion_alta = (String)datos.get("presion_alta")==null?"":(String)datos.get("presion_alta");
            amp_l1 = (String)datos.get("amp_l1")==null?"":(String)datos.get("amp_l1");
            amp_l2 = (String)datos.get("amp_l2")==null?"":(String)datos.get("amp_l2");
            amp_l3 = (String)datos.get("amp_l3")==null?"":(String)datos.get("amp_l3");
            volt_l1 = (String)datos.get("volt_l1")==null?"":(String)datos.get("volt_l1");
            volt_l2 = (String)datos.get("volt_l2")==null?"":(String)datos.get("volt_l2");
            volt_l3 = (String)datos.get("volt_l3")==null?"":(String)datos.get("volt_l3");
        }
        et_temp_serpentin.setText(txt_temp_serpentin);
        et_temp_ambiente.setText(txt_temp_ambiente);
        et_presion_baja.setText(txt_presion_baja);
        et_presion_alta.setText(txt_presion_alta);
        et_amp_l1.setText(txt_amp_l1);
        et_amp_l2.setText(txt_amp_l2);
        et_amp_l3.setText(txt_amp_l3);
        et_volt_l1.setText(txt_volt_l1);
        et_volt_l2.setText(txt_volt_l2);
        et_volt_l3.setText(txt_volt_l3);

        if(!temp_serpentin.isEmpty()){
            icon_camera_temp_serpentin.setVisibility(View.GONE);
            icon_gallery_temp_serpentin.setVisibility(View.VISIBLE);
        }else{
            icon_gallery_temp_serpentin.setVisibility(View.GONE);
            icon_camera_temp_serpentin.setVisibility(View.VISIBLE);
        }

        if(!temp_ambiente.isEmpty()){
            icon_camera_temp_ambiente.setVisibility(View.GONE);
            icon_gallery_temp_ambiente.setVisibility(View.VISIBLE);
        }else{
            icon_camera_temp_ambiente.setVisibility(View.VISIBLE);
            icon_gallery_temp_ambiente.setVisibility(View.GONE);
        }

        if(!presion_baja.isEmpty()){
            icon_camera_presion_baja.setVisibility(View.GONE);
            icon_gallery_presion_baja.setVisibility(View.VISIBLE);
        }else{
            icon_camera_presion_baja.setVisibility(View.VISIBLE);
            icon_gallery_presion_baja.setVisibility(View.GONE);
        }

        if(!presion_alta.isEmpty()){
            icon_camera_presion_alta.setVisibility(View.GONE);
            icon_gallery_presion_alta.setVisibility(View.VISIBLE);
        }else{
            icon_camera_presion_alta.setVisibility(View.VISIBLE);
            icon_gallery_presion_alta.setVisibility(View.GONE);
        }

        if(!amp_l1.isEmpty()){
            icon_camera_amp_l1.setVisibility(View.GONE);
            icon_gallery_amp_l1.setVisibility(View.VISIBLE);
        }else{
            icon_camera_amp_l1.setVisibility(View.VISIBLE);
            icon_gallery_amp_l1.setVisibility(View.GONE);
        }

        if(!amp_l2.isEmpty()){
            icon_camera_amp_l2.setVisibility(View.GONE);
            icon_gallery_amp_l2.setVisibility(View.VISIBLE);
        }else{
            icon_camera_amp_l2.setVisibility(View.VISIBLE);
            icon_gallery_amp_l2.setVisibility(View.GONE);
        }

        if(!amp_l3.isEmpty()){
            icon_camera_amp_l3.setVisibility(View.GONE);
            icon_gallery_amp_l3.setVisibility(View.VISIBLE);
        }else{
            icon_camera_amp_l3.setVisibility(View.VISIBLE);
            icon_gallery_amp_l3.setVisibility(View.GONE);
        }

        if(!volt_l1.isEmpty()){
            icon_camera_volt_l1.setVisibility(View.GONE);
            icon_gallery_volt_l1.setVisibility(View.VISIBLE);
        }else{
            icon_camera_volt_l1.setVisibility(View.VISIBLE);
            icon_gallery_volt_l1.setVisibility(View.GONE);
        }

        if(!volt_l2.isEmpty()){
            icon_camera_volt_l2.setVisibility(View.GONE);
            icon_gallery_volt_l2.setVisibility(View.VISIBLE);
        }else{
            icon_camera_volt_l2.setVisibility(View.VISIBLE);
            icon_gallery_volt_l2.setVisibility(View.GONE);
        }

        if(!volt_l3.isEmpty()){
            icon_camera_volt_l3.setVisibility(View.GONE);
            icon_gallery_volt_l3.setVisibility(View.VISIBLE);
        }else{
            icon_camera_volt_l3.setVisibility(View.VISIBLE);
            icon_gallery_volt_l3.setVisibility(View.GONE);
        }
    }

    void validateLecturaDespuesUpload()
    {
        if(!datos_guardados.isEmpty()){
            despues_txt_temp_serpentin = (String)datos.get("despues_txt_temp_serpentin")==null?"0":(String)datos.get("despues_txt_temp_serpentin");
            despues_txt_temp_ambiente = (String)datos.get("despues_txt_temp_ambiente")==null?"0":(String)datos.get("despues_txt_temp_ambiente");
            despues_txt_presion_baja = (String)datos.get("despues_txt_presion_baja")==null?"0":(String)datos.get("despues_txt_presion_baja");
            despues_txt_presion_alta = (String)datos.get("despues_txt_presion_alta")==null?"0":(String)datos.get("despues_txt_presion_alta");
            despues_txt_amp_l1 = (String)datos.get("despues_txt_amp_l1")==null?"0":(String)datos.get("despues_txt_amp_l1");
            despues_txt_amp_l2 = (String)datos.get("despues_txt_amp_l2")==null?"0":(String)datos.get("despues_txt_amp_l2");
            despues_txt_amp_l3 = (String)datos.get("despues_txt_amp_l3")==null?"0":(String)datos.get("despues_txt_amp_l3");
            despues_txt_volt_l1 = (String)datos.get("despues_txt_volt_l1")==null?"0":(String)datos.get("despues_txt_volt_l1");
            despues_txt_volt_l2 = (String)datos.get("despues_txt_volt_l2")==null?"0":(String)datos.get("despues_txt_volt_l2");
            despues_txt_volt_l3 = (String)datos.get("despues_txt_volt_l3")==null?"0":(String)datos.get("despues_txt_volt_l3");

            despues_temp_serpentin = (String)datos.get("despues_temp_serpentin")==null?"":(String)datos.get("despues_temp_serpentin");
            despues_temp_ambiente = (String)datos.get("despues_temp_ambiente")==null?"":(String)datos.get("despues_temp_ambiente");
            despues_presion_baja = (String)datos.get("despues_presion_baja")==null?"":(String)datos.get("despues_presion_baja");
            despues_presion_alta = (String)datos.get("despues_presion_alta")==null?"":(String)datos.get("despues_presion_alta");
            despues_amp_l1 = (String)datos.get("despues_amp_l1")==null?"":(String)datos.get("despues_amp_l1");
            despues_amp_l2 = (String)datos.get("despues_amp_l2")==null?"":(String)datos.get("despues_amp_l2");
            despues_amp_l3 = (String)datos.get("despues_amp_l3")==null?"":(String)datos.get("despues_amp_l3");
            despues_volt_l1 = (String)datos.get("despues_volt_l1")==null?"":(String)datos.get("despues_volt_l1");
            despues_volt_l2 = (String)datos.get("despues_volt_l2")==null?"":(String)datos.get("despues_volt_l2");
            despues_volt_l3 = (String)datos.get("despues_volt_l3")==null?"":(String)datos.get("despues_volt_l3");
        }
         despues_et_temp_serpentin.setText(despues_txt_temp_serpentin);
         despues_et_temp_ambiente.setText(despues_txt_temp_ambiente);
         despues_et_presion_baja.setText(despues_txt_presion_baja);
         despues_et_presion_alta.setText(despues_txt_presion_alta);
         despues_et_amp_l1.setText(despues_txt_amp_l1);
         despues_et_amp_l2.setText(despues_txt_amp_l2);
         despues_et_amp_l3.setText(despues_txt_amp_l3);
         despues_et_volt_l1.setText(despues_txt_volt_l1);
         despues_et_volt_l2.setText(despues_txt_volt_l2);
         despues_et_volt_l3.setText(despues_txt_volt_l3);

        if(!despues_temp_serpentin.isEmpty()){
            despues_icon_camera_temp_serpentin.setVisibility(View.GONE);
            despues_icon_gallery_temp_serpentin.setVisibility(View.VISIBLE);
        }else{
            despues_icon_gallery_temp_serpentin.setVisibility(View.GONE);
            despues_icon_camera_temp_serpentin.setVisibility(View.VISIBLE);
        }

        if(!despues_temp_ambiente.isEmpty()){
            despues_icon_camera_temp_ambiente.setVisibility(View.GONE);
            despues_icon_gallery_temp_ambiente.setVisibility(View.VISIBLE);
        }else{
            despues_icon_camera_temp_ambiente.setVisibility(View.VISIBLE);
            despues_icon_gallery_temp_ambiente.setVisibility(View.GONE);
        }

        if(!despues_presion_baja.isEmpty()){
            despues_icon_camera_presion_baja.setVisibility(View.GONE);
            despues_icon_gallery_presion_baja.setVisibility(View.VISIBLE);
        }else{
            despues_icon_camera_presion_baja.setVisibility(View.VISIBLE);
            despues_icon_gallery_presion_baja.setVisibility(View.GONE);
        }

        if(!despues_presion_alta.isEmpty()){
            despues_icon_camera_presion_alta.setVisibility(View.GONE);
            despues_icon_gallery_presion_alta.setVisibility(View.VISIBLE);
        }else{
            despues_icon_camera_presion_alta.setVisibility(View.VISIBLE);
            despues_icon_gallery_presion_alta.setVisibility(View.GONE);
        }

        if(!despues_amp_l1.isEmpty()){
            despues_icon_camera_amp_l1.setVisibility(View.GONE);
            despues_icon_gallery_amp_l1.setVisibility(View.VISIBLE);
        }else{
            despues_icon_camera_amp_l1.setVisibility(View.VISIBLE);
            despues_icon_gallery_amp_l1.setVisibility(View.GONE);
        }

        if(!despues_amp_l2.isEmpty()){
            despues_icon_camera_amp_l2.setVisibility(View.GONE);
            despues_icon_gallery_amp_l2.setVisibility(View.VISIBLE);
        }else{
            despues_icon_camera_amp_l2.setVisibility(View.VISIBLE);
            despues_icon_gallery_amp_l2.setVisibility(View.GONE);
        }

        if(!despues_amp_l3.isEmpty()){
            despues_icon_camera_amp_l3.setVisibility(View.GONE);
            despues_icon_gallery_amp_l3.setVisibility(View.VISIBLE);
        }else{
            despues_icon_camera_amp_l3.setVisibility(View.VISIBLE);
            despues_icon_gallery_amp_l3.setVisibility(View.GONE);
        }

        if(!despues_volt_l1.isEmpty()){
            despues_icon_camera_volt_l1.setVisibility(View.GONE);
            despues_icon_gallery_volt_l1.setVisibility(View.VISIBLE);
        }else{
            despues_icon_camera_volt_l1.setVisibility(View.VISIBLE);
            despues_icon_gallery_volt_l1.setVisibility(View.GONE);
        }

        if(!despues_volt_l2.isEmpty()){
            despues_icon_camera_volt_l2.setVisibility(View.GONE);
            despues_icon_gallery_volt_l2.setVisibility(View.VISIBLE);
        }else{
            despues_icon_camera_volt_l2.setVisibility(View.VISIBLE);
            despues_icon_gallery_volt_l2.setVisibility(View.GONE);
        }

        if(!despues_volt_l3.isEmpty()){
            despues_icon_camera_volt_l3.setVisibility(View.GONE);
            despues_icon_gallery_volt_l3.setVisibility(View.VISIBLE);
        }else{
            despues_icon_camera_volt_l3.setVisibility(View.VISIBLE);
            despues_icon_gallery_volt_l3.setVisibility(View.GONE);
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
                //Mantenimiento Antes
                case "1":
                    image_serpentin = Image.convert(BitmapFactory.decodeFile(b64,options));
                    datos.put("image_serpentin",image_serpentin);
                    cred.save_data("2-"+id,datos.toString());
                    Log.e("image_serpentin","1");
                    icon_camera_serpentin.setVisibility(View.GONE);
                    icon_gallery_serpentin.setVisibility(View.VISIBLE);
                    if(image_serpentin.isEmpty())
                        System.out.println("image_serpentin NULL");

                    if(modo_prueba==1){
                        setAllValues();
                    }
                    break;
                case "2":
                    image_bomba = Image.convert(BitmapFactory.decodeFile(b64,options));
                    datos.put("image_bomba",image_bomba);
                    cred.save_data("2-"+id,datos.toString());
                    Log.e("image_bomba","2");
                    icon_camera_bomba.setVisibility(View.GONE);
                    icon_gallery_bomba.setVisibility(View.VISIBLE);
                    if(image_bomba.isEmpty())
                        System.out.println("image_bomba NULL");
                    break;
                case "3":
                    image_cortina = Image.convert(BitmapFactory.decodeFile(b64,options));
                    datos.put("image_cortina",image_cortina);
                    cred.save_data("2-"+id,datos.toString());
                    Log.e("image_cortina","3");
                    icon_camera_cortina.setVisibility(View.GONE);
                    icon_gallery_cortina.setVisibility(View.VISIBLE);
                    if(image_cortina.isEmpty())
                        System.out.println("image_cortina NULL");
                    break;
                case "4":
                    image_cond_serpentin = Image.convert(BitmapFactory.decodeFile(b64,options));
                    datos.put("image_cond_serpentin",image_cond_serpentin);
                    cred.save_data("2-"+id,datos.toString());
                    Log.e("image_cond_serpentin","5");
                    icon_camera_cond_serpentin.setVisibility(View.GONE);
                    icon_gallery_cond_serpentin.setVisibility(View.VISIBLE);
                    if(image_cond_serpentin.isEmpty())
                        System.out.println("image_cond_serpentin NULL");
                    break;
                case "5":
                    image_ventilador = Image.convert(BitmapFactory.decodeFile(b64,options));
                    datos.put("image_ventilador",image_ventilador);
                    cred.save_data("2-"+id,datos.toString());
                    Log.e("image_ventilador","6");
                    icon_camera_ventilador.setVisibility(View.GONE);
                    icon_gallery_ventilador.setVisibility(View.VISIBLE);
                    if(image_ventilador.isEmpty())
                        System.out.println("image_ventilador NULL");
                    break;
                case "6":
                    image_carcaza = Image.convert(BitmapFactory.decodeFile(b64,options));
                    datos.put("image_carcaza",image_carcaza);
                    cred.save_data("2-"+id,datos.toString());
                    Log.e("image_carcaza","7");
                    icon_camera_carcaza.setVisibility(View.GONE);
                    icon_gallery_carcaza.setVisibility(View.VISIBLE);
                    if(image_carcaza.isEmpty())
                        System.out.println("image_carcaza NULL");
                    break;
                //Mantenimiento Después
                case "7":
                    despues_image_serpentin = Image.convert(BitmapFactory.decodeFile(b64,options));
                    datos.put("despues_image_serpentin",despues_image_serpentin);
                    cred.save_data("2-"+id,datos.toString());
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
                    datos.put("despues_image_bomba",despues_image_bomba);
                    cred.save_data("2-"+id,datos.toString());
                    Log.e("despues_image_bomba","2");
                    despues_icon_camera_bomba.setVisibility(View.GONE);
                    despues_icon_gallery_bomba.setVisibility(View.VISIBLE);
                    if(despues_image_bomba.isEmpty())
                        System.out.println("despues_image_bomba NULL");
                    break;
                case "9":
                    despues_image_cortina = Image.convert(BitmapFactory.decodeFile(b64,options));
                    datos.put("despues_image_cortina",despues_image_cortina);
                    cred.save_data("2-"+id,datos.toString());
                    Log.e("despues_image_cortina","3");
                    despues_icon_camera_cortina.setVisibility(View.GONE);
                    despues_icon_gallery_cortina.setVisibility(View.VISIBLE);
                    if(despues_image_cortina.isEmpty())
                        System.out.println("despues_image_cortina NULL");
                    break;
                case "10":
                    despues_image_cond_serpentin = Image.convert(BitmapFactory.decodeFile(b64,options));
                    datos.put("despues_image_cond_serpentin",despues_image_cond_serpentin);
                    cred.save_data("2-"+id,datos.toString());
                    Log.e("despues_image_cond_serpentin","5");
                    despues_icon_camera_cond_serpentin.setVisibility(View.GONE);
                    despues_icon_gallery_cond_serpentin.setVisibility(View.VISIBLE);
                    if(despues_image_cond_serpentin.isEmpty())
                        System.out.println("despues_image_cond_serpentin NULL");
                    break;
                case "11":
                    despues_image_ventilador = Image.convert(BitmapFactory.decodeFile(b64,options));
                    datos.put("despues_image_ventilador",despues_image_ventilador);
                    cred.save_data("2-"+id,datos.toString());
                    Log.e("despues_image_ventilador","6");
                    despues_icon_camera_ventilador.setVisibility(View.GONE);
                    despues_icon_gallery_ventilador.setVisibility(View.VISIBLE);
                    if(despues_image_ventilador.isEmpty())
                        System.out.println("despues_image_ventilador NULL");
                    break;
                case "12":
                    despues_image_carcaza = Image.convert(BitmapFactory.decodeFile(b64,options));
                    datos.put("despues_image_carcaza",despues_image_carcaza);
                    cred.save_data("2-"+id,datos.toString());
                    Log.e("despues_image_carcaza","7");
                    despues_icon_camera_carcaza.setVisibility(View.GONE);
                    despues_icon_gallery_carcaza.setVisibility(View.VISIBLE);
                    if(despues_image_carcaza.isEmpty())
                        System.out.println("despues_image_carcaza NULL");
                    break;
                //LECTURA ANTES
                case "13":
                    temp_serpentin = Image.convert(BitmapFactory.decodeFile(b64,options));
                    datos.put("temp_serpentin",temp_serpentin);
                    cred.save_data("2-"+id,datos.toString());
                    Log.e("temp_serpentin","13");
                    icon_camera_temp_serpentin.setVisibility(View.GONE);
                    icon_gallery_temp_serpentin.setVisibility(View.VISIBLE);
                    if(temp_serpentin.isEmpty())
                        System.out.println("temp_serpentin NULL");
                    break;
                case "14":
                    temp_ambiente = Image.convert(BitmapFactory.decodeFile(b64,options));
                    datos.put("temp_ambiente",temp_ambiente);
                    cred.save_data("2-"+id,datos.toString());
                    Log.e("temp_ambiente","14");
                    icon_camera_temp_ambiente.setVisibility(View.GONE);
                    icon_gallery_temp_ambiente.setVisibility(View.VISIBLE);
                    if(temp_ambiente.isEmpty())
                        System.out.println("temp_ambiente NULL");
                    break;
                case "15":
                    presion_baja = Image.convert(BitmapFactory.decodeFile(b64,options));
                    datos.put("presion_baja",presion_baja);
                    cred.save_data("2-"+id,datos.toString());
                    Log.e("presion_baja","3");
                    icon_camera_presion_baja.setVisibility(View.GONE);
                    icon_gallery_presion_baja.setVisibility(View.VISIBLE);
                    if(presion_baja.isEmpty())
                        System.out.println("presion_baja NULL");
                    break;
                case "16":
                    presion_alta = Image.convert(BitmapFactory.decodeFile(b64,options));
                    datos.put("presion_alta",presion_alta);
                    cred.save_data("2-"+id,datos.toString());
                    Log.e("presion_alta","16");
                    icon_camera_presion_alta.setVisibility(View.GONE);
                    icon_gallery_presion_alta.setVisibility(View.VISIBLE);
                    if(presion_alta.isEmpty())
                        System.out.println("presion_alta NULL");
                    break;
                case "17":
                    amp_l1 = Image.convert(BitmapFactory.decodeFile(b64,options));
                    datos.put("amp_l1",amp_l1);
                    cred.save_data("2-"+id,datos.toString());
                    Log.e("amp_l1","17");
                    icon_camera_amp_l1.setVisibility(View.GONE);
                    icon_gallery_amp_l1.setVisibility(View.VISIBLE);
                    if(amp_l1.isEmpty())
                        System.out.println("amp_l1 NULL");
                    break;
                case "18":
                    amp_l2 = Image.convert(BitmapFactory.decodeFile(b64,options));
                    datos.put("amp_l1",amp_l2);
                    cred.save_data("2-"+id,datos.toString());
                    Log.e("amp_l2","18");
                    icon_camera_amp_l2.setVisibility(View.GONE);
                    icon_gallery_amp_l2.setVisibility(View.VISIBLE);
                    if(amp_l2.isEmpty())
                        System.out.println("amp_l2 NULL");
                    break;
                case "19":
                    amp_l3 = Image.convert(BitmapFactory.decodeFile(b64,options));
                    datos.put("amp_l3",amp_l3);
                    cred.save_data("2-"+id,datos.toString());
                    Log.e("amp_l3","19");
                    icon_camera_amp_l3.setVisibility(View.GONE);
                    icon_gallery_amp_l3.setVisibility(View.VISIBLE);
                    if(amp_l3.isEmpty())
                        System.out.println("amp_l3 NULL");
                    break;
                case "20":
                    volt_l1 = Image.convert(BitmapFactory.decodeFile(b64,options));
                    datos.put("volt_l1",volt_l1);
                    cred.save_data("2-"+id,datos.toString());
                    Log.e("volt_l1","17");
                    icon_camera_volt_l1.setVisibility(View.GONE);
                    icon_gallery_volt_l1.setVisibility(View.VISIBLE);
                    if(volt_l1.isEmpty())
                        System.out.println("volt_l1 NULL");
                    break;
                case "21":
                    volt_l2 = Image.convert(BitmapFactory.decodeFile(b64,options));
                    datos.put("volt_l2",volt_l2);
                    cred.save_data("2-"+id,datos.toString());
                    Log.e("volt_l2","18");
                    icon_camera_volt_l2.setVisibility(View.GONE);
                    icon_gallery_volt_l2.setVisibility(View.VISIBLE);
                    if(volt_l2.isEmpty())
                        System.out.println("volt_l2 NULL");
                    break;
                case "22":
                    volt_l3 = Image.convert(BitmapFactory.decodeFile(b64,options));
                    datos.put("volt_l3",volt_l3);
                    cred.save_data("2-"+id,datos.toString());
                    Log.e("volt_l3","19");
                    icon_camera_volt_l3.setVisibility(View.GONE);
                    icon_gallery_volt_l3.setVisibility(View.VISIBLE);
                    if(volt_l3.isEmpty())
                        System.out.println("volt_l3 NULL");
                    break;
                //LECTURA DESPUES
                case "23":
                    despues_temp_serpentin = Image.convert(BitmapFactory.decodeFile(b64,options));
                    datos.put("despues_temp_serpentin",despues_temp_serpentin);
                    cred.save_data("2-"+id,datos.toString());
                    Log.e("despues_temp_serpentin","20");
                    despues_icon_camera_temp_serpentin.setVisibility(View.GONE);
                    despues_icon_gallery_temp_serpentin.setVisibility(View.VISIBLE);
                    if(despues_temp_serpentin.isEmpty())
                        System.out.println("despues_temp_serpentin NULL");
                    break;
                case "24":
                    despues_temp_ambiente = Image.convert(BitmapFactory.decodeFile(b64,options));
                    datos.put("despues_temp_ambiente",despues_temp_ambiente);
                    cred.save_data("2-"+id,datos.toString());
                    Log.e("despues_temp_ambiente","21");
                    despues_icon_camera_temp_ambiente.setVisibility(View.GONE);
                    despues_icon_gallery_temp_ambiente.setVisibility(View.VISIBLE);
                    if(despues_temp_ambiente.isEmpty())
                        System.out.println("despues_temp_ambiente NULL");
                    break;
                case "25":
                    despues_presion_baja = Image.convert(BitmapFactory.decodeFile(b64,options));
                    datos.put("despues_presion_baja",despues_presion_baja);
                    cred.save_data("2-"+id,datos.toString());
                    Log.e("despues_presion_baja","22");
                    despues_icon_camera_presion_baja.setVisibility(View.GONE);
                    despues_icon_gallery_presion_baja.setVisibility(View.VISIBLE);
                    if(despues_presion_baja.isEmpty())
                        System.out.println("despues_presion_baja NULL");
                    break;
                case "26":
                    despues_presion_alta = Image.convert(BitmapFactory.decodeFile(b64,options));
                    datos.put("despues_presion_alta",despues_presion_alta);
                    cred.save_data("2-"+id,datos.toString());
                    Log.e("despues_presion_alta","23");
                    despues_icon_camera_presion_alta.setVisibility(View.GONE);
                    despues_icon_gallery_presion_alta.setVisibility(View.VISIBLE);
                    if(presion_alta.isEmpty())
                        System.out.println("despues_presion_alta NULL");
                    break;
                case "27":
                    despues_amp_l1 = Image.convert(BitmapFactory.decodeFile(b64,options));
                    datos.put("despues_amp_l1",despues_amp_l1);
                    cred.save_data("2-"+id,datos.toString());
                    Log.e("despues_amp_l1","24");
                    despues_icon_camera_amp_l1.setVisibility(View.GONE);
                    despues_icon_gallery_amp_l1.setVisibility(View.VISIBLE);
                    if(despues_amp_l1.isEmpty())
                        System.out.println("despues_amp_l1 NULL");
                    break;
                case "28":
                    despues_amp_l2 = Image.convert(BitmapFactory.decodeFile(b64,options));
                    datos.put("despues_amp_l2",despues_amp_l2);
                    cred.save_data("2-"+id,datos.toString());
                    Log.e("despues_amp_l2","25");
                    despues_icon_camera_amp_l2.setVisibility(View.GONE);
                    despues_icon_gallery_amp_l2.setVisibility(View.VISIBLE);
                    if(despues_amp_l2.isEmpty())
                        System.out.println("despues_amp_l2 NULL");
                    break;
                case "29":
                    despues_amp_l3 = Image.convert(BitmapFactory.decodeFile(b64,options));
                    datos.put("despues_amp_l3",despues_amp_l3);
                    cred.save_data("2-"+id,datos.toString());
                    Log.e("despues_amp_l3","26");
                    despues_icon_camera_amp_l3.setVisibility(View.GONE);
                    despues_icon_gallery_amp_l3.setVisibility(View.VISIBLE);
                    if(despues_amp_l3.isEmpty())
                        System.out.println("despues_amp_l3 NULL");
                    break;
                case "30":
                    despues_volt_l1 = Image.convert(BitmapFactory.decodeFile(b64,options));
                    datos.put("despues_volt_l1",despues_amp_l3);
                    cred.save_data("2-"+id,datos.toString());
                    Log.e("despues_volt_l1","24");
                    despues_icon_camera_volt_l1.setVisibility(View.GONE);
                    despues_icon_gallery_volt_l1.setVisibility(View.VISIBLE);
                    if(despues_volt_l1.isEmpty())
                        System.out.println("despues_volt_l1 NULL");
                    break;
                case "31":
                    despues_volt_l2 = Image.convert(BitmapFactory.decodeFile(b64,options));
                    datos.put("despues_volt_l2",despues_amp_l3);
                    cred.save_data("2-"+id,datos.toString());
                    Log.e("despues_volt_l2","25");
                    despues_icon_camera_volt_l2.setVisibility(View.GONE);
                    despues_icon_gallery_volt_l2.setVisibility(View.VISIBLE);
                    if(despues_volt_l2.isEmpty())
                        System.out.println("despues_volt_l2 NULL");
                    break;
                case "32":
                    despues_volt_l3 = Image.convert(BitmapFactory.decodeFile(b64,options));
                    datos.put("despues_volt_l3",despues_volt_l3);
                    cred.save_data("2-"+id,datos.toString());
                    Log.e("despues_volt_l3","26");
                    despues_icon_camera_volt_l3.setVisibility(View.GONE);
                    despues_icon_gallery_volt_l3.setVisibility(View.VISIBLE);
                    if(despues_volt_l3.isEmpty())
                        System.out.println("despues_volt_l3 NULL");
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
                if(data!=null){
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
                            //Mantenimiento Antes
                            case "1":
                                image_serpentin = Image.convert(bitmap);
                                datos.put("image_serpentin",image_serpentin);
                                Log.e("image_falla","1");
                                icon_camera_serpentin.setVisibility(View.GONE);
                                icon_gallery_serpentin.setVisibility(View.VISIBLE);
                                if(image_serpentin.isEmpty())
                                    System.out.println("image_serpentin NULL");
                                break;
                            case "2":
                                image_bomba = Image.convert(bitmap);
                                datos.put("image_bomba",image_bomba);
                                Log.e("image_bomba","2");
                                icon_camera_bomba.setVisibility(View.GONE);
                                icon_gallery_bomba.setVisibility(View.VISIBLE);
                                if(image_bomba.isEmpty())
                                    System.out.println("image_tbomba NULL");
                                break;
                            case "3":
                                image_cortina = Image.convert(bitmap);
                                datos.put("image_cortina",image_cortina);
                                Log.e("image_cortina","3");
                                icon_camera_cortina.setVisibility(View.GONE);
                                icon_gallery_cortina.setVisibility(View.VISIBLE);
                                if(image_cortina.isEmpty())
                                    System.out.println("image_cortina NULL");
                                break;
                            case "4":
                                image_cond_serpentin = Image.convert(bitmap);
                                datos.put("image_cond_serpentin",image_cond_serpentin);
                                Log.e("image_cond_serpentin","5");
                                icon_camera_cond_serpentin.setVisibility(View.GONE);
                                icon_gallery_cond_serpentin.setVisibility(View.VISIBLE);
                                if(image_cond_serpentin.isEmpty())
                                    System.out.println("image_cond_serpentin NULL");
                                break;
                            case "5":
                                image_ventilador = Image.convert(bitmap);
                                datos.put("image_ventilador",image_ventilador);
                                Log.e("image_ventilador","6");
                                icon_camera_ventilador.setVisibility(View.GONE);
                                icon_gallery_ventilador.setVisibility(View.VISIBLE);
                                if(image_ventilador.isEmpty())
                                    System.out.println("image_ventilador NULL");
                                break;
                            case "6":
                                image_carcaza = Image.convert(bitmap);
                                datos.put("image_carcaza",image_carcaza);
                                Log.e("image_carcaza","7");
                                icon_camera_carcaza.setVisibility(View.GONE);
                                icon_gallery_carcaza.setVisibility(View.VISIBLE);
                                if(image_carcaza.isEmpty())
                                    System.out.println("image_carcaza NULL");
                                break;
                            //Mantenimiento Depues
                            case "7":
                                despues_image_serpentin = Image.convert(bitmap);
                                datos.put("despues_image_serpentin",despues_image_serpentin);
                                Log.e("despues_image_falla","1");
                                despues_icon_camera_serpentin.setVisibility(View.GONE);
                                despues_icon_gallery_serpentin.setVisibility(View.VISIBLE);
                                if(despues_image_serpentin.isEmpty())
                                    System.out.println("despues_image_serpentin NULL");
                                break;
                            case "8":
                                despues_image_bomba = Image.convert(bitmap);
                                datos.put("despues_image_bomba",despues_image_bomba);
                                Log.e("despues_image_bomba","2");
                                despues_icon_camera_bomba.setVisibility(View.GONE);
                                despues_icon_gallery_bomba.setVisibility(View.VISIBLE);
                                if(despues_image_bomba.isEmpty())
                                    System.out.println("despues_image_tbomba NULL");
                                break;
                            case "9":
                                despues_image_cortina = Image.convert(bitmap);
                                datos.put("despues_image_cortina",despues_image_cortina);
                                Log.e("despues_image_cortina","3");
                                despues_icon_camera_cortina.setVisibility(View.GONE);
                                despues_icon_gallery_cortina.setVisibility(View.VISIBLE);
                                if(despues_image_cortina.isEmpty())
                                    System.out.println("image_cortina NULL");
                                break;
                            case "10":
                                despues_image_cond_serpentin = Image.convert(bitmap);
                                datos.put("despues_image_cond_serpentin",despues_image_cond_serpentin);
                                Log.e("despues_image_cond_serpentin","5");
                                despues_icon_camera_cond_serpentin.setVisibility(View.GONE);
                                despues_icon_gallery_cond_serpentin.setVisibility(View.VISIBLE);
                                if(image_cond_serpentin.isEmpty())
                                    System.out.println("despues_image_cond_serpentin NULL");
                                break;
                            case "11":
                                despues_image_ventilador = Image.convert(bitmap);
                                datos.put("despues_image_ventilador",despues_image_ventilador);
                                Log.e("despues_image_ventilador","6");
                                despues_icon_camera_ventilador.setVisibility(View.GONE);
                                despues_icon_gallery_ventilador.setVisibility(View.VISIBLE);
                                if(despues_image_ventilador.isEmpty())
                                    System.out.println("despues_image_ventilador NULL");
                                break;
                            case "12":
                                despues_image_carcaza = Image.convert(bitmap);
                                datos.put("despues_image_carcaza",despues_image_carcaza);
                                Log.e("despues_image_carcaza","7");
                                despues_icon_camera_carcaza.setVisibility(View.GONE);
                                despues_icon_gallery_carcaza.setVisibility(View.VISIBLE);
                                if(despues_image_carcaza.isEmpty())
                                    System.out.println("despues_image_carcaza NULL");
                                break;
                            //LECTURA ANTES
                            case "13":
                                temp_serpentin = Image.convert(bitmap);
                                datos.put("temp_serpentin",temp_serpentin);
                                Log.e("temp_serpentin","13");
                                icon_camera_temp_serpentin.setVisibility(View.GONE);
                                icon_gallery_temp_serpentin.setVisibility(View.VISIBLE);
                                if(temp_serpentin.isEmpty())
                                    System.out.println("temp_serpentin NULL");
                                break;
                            case "14":
                                temp_ambiente = Image.convert(bitmap);
                                datos.put("temp_ambiente",temp_ambiente);
                                Log.e("temp_ambiente","14");
                                icon_camera_temp_ambiente.setVisibility(View.GONE);
                                icon_gallery_temp_ambiente.setVisibility(View.VISIBLE);
                                if(temp_ambiente.isEmpty())
                                    System.out.println("temp_ambiente NULL");
                                break;
                            case "15":
                                presion_baja = Image.convert(bitmap);
                                datos.put("presion_baja",presion_baja);
                                Log.e("presion_baja","3");
                                icon_camera_presion_baja.setVisibility(View.GONE);
                                icon_gallery_presion_baja.setVisibility(View.VISIBLE);
                                if(presion_baja.isEmpty())
                                    System.out.println("presion_baja NULL");
                                break;
                            case "16":
                                presion_alta = Image.convert(bitmap);
                                datos.put("presion_alta",presion_alta);
                                Log.e("presion_alta","16");
                                icon_camera_presion_alta.setVisibility(View.GONE);
                                icon_gallery_presion_alta.setVisibility(View.VISIBLE);
                                if(presion_alta.isEmpty())
                                    System.out.println("presion_alta NULL");
                                break;
                            case "17":
                                amp_l1 = Image.convert(bitmap);
                                datos.put("amp_l1",amp_l1);
                                Log.e("amp_l1","17");
                                icon_camera_amp_l1.setVisibility(View.GONE);
                                icon_gallery_amp_l1.setVisibility(View.VISIBLE);
                                if(amp_l1.isEmpty())
                                    System.out.println("amp_l1 NULL");
                                break;
                            case "18":
                                amp_l2 = Image.convert(bitmap);
                                datos.put("amp_l2",amp_l2);
                                Log.e("amp_l2","18");
                                icon_camera_amp_l2.setVisibility(View.GONE);
                                icon_gallery_amp_l2.setVisibility(View.VISIBLE);
                                if(amp_l2.isEmpty())
                                    System.out.println("amp_l2 NULL");
                                break;
                            case "19":
                                amp_l3 = Image.convert(bitmap);
                                datos.put("amp_l3",amp_l3);
                                Log.e("amp_l3","19");
                                icon_camera_amp_l3.setVisibility(View.GONE);
                                icon_gallery_amp_l3.setVisibility(View.VISIBLE);
                                if(amp_l3.isEmpty())
                                    System.out.println("amp_l3 NULL");
                                break;
                            case "20":
                                volt_l1 = Image.convert(bitmap);
                                datos.put("volt_l1",volt_l1);
                                Log.e("volt_l1","17");
                                icon_camera_volt_l1.setVisibility(View.GONE);
                                icon_gallery_volt_l1.setVisibility(View.VISIBLE);
                                if(volt_l1.isEmpty())
                                    System.out.println("volt_l1 NULL");
                                break;
                            case "21":
                                volt_l2 = Image.convert(bitmap);
                                datos.put("volt_l2",volt_l2);
                                Log.e("volt_l2","18");
                                icon_camera_volt_l2.setVisibility(View.GONE);
                                icon_gallery_volt_l2.setVisibility(View.VISIBLE);
                                if(volt_l2.isEmpty())
                                    System.out.println("volt_l2 NULL");
                                break;
                            case "22":
                                volt_l3 = Image.convert(bitmap);
                                datos.put("volt_l3",volt_l3);
                                Log.e("volt_l3","19");
                                icon_camera_volt_l3.setVisibility(View.GONE);
                                icon_gallery_volt_l3.setVisibility(View.VISIBLE);
                                if(volt_l3.isEmpty())
                                    System.out.println("volt_l3 NULL");
                                break;
                            //LECTURA DESPUES
                            case "23":
                                despues_temp_serpentin = Image.convert(bitmap);
                                datos.put("despues_temp_serpentin",despues_temp_serpentin);
                                Log.e("despues_temp_serpentin","20");
                                despues_icon_camera_temp_serpentin.setVisibility(View.GONE);
                                despues_icon_gallery_temp_serpentin.setVisibility(View.VISIBLE);
                                if(despues_temp_serpentin.isEmpty())
                                    System.out.println("despues_temp_serpentin NULL");
                                break;
                            case "24":
                                despues_temp_ambiente = Image.convert(bitmap);
                                datos.put("despues_temp_ambiente",despues_temp_ambiente);
                                Log.e("despues_temp_ambiente","21");
                                despues_icon_camera_temp_ambiente.setVisibility(View.GONE);
                                despues_icon_gallery_temp_ambiente.setVisibility(View.VISIBLE);
                                if(despues_temp_ambiente.isEmpty())
                                    System.out.println("despues_temp_ambiente NULL");
                                break;
                            case "25":
                                despues_presion_baja = Image.convert(bitmap);
                                datos.put("despues_presion_baja",despues_presion_baja);
                                Log.e("despues_presion_baja","22");
                                despues_icon_camera_presion_baja.setVisibility(View.GONE);
                                despues_icon_gallery_presion_baja.setVisibility(View.VISIBLE);
                                if(despues_presion_baja.isEmpty())
                                    System.out.println("despues_presion_baja NULL");
                                break;
                            case "26":
                                despues_presion_alta = Image.convert(bitmap);
                                datos.put("despues_presion_alta",despues_presion_alta);
                                Log.e("despues_presion_alta","23");
                                despues_icon_camera_presion_alta.setVisibility(View.GONE);
                                despues_icon_gallery_presion_alta.setVisibility(View.VISIBLE);
                                if(presion_alta.isEmpty())
                                    System.out.println("despues_presion_alta NULL");
                                break;
                            case "27":
                                despues_amp_l1 = Image.convert(bitmap);
                                datos.put("despues_amp_l1",despues_amp_l1);
                                Log.e("despues_amp_l1","24");
                                despues_icon_camera_amp_l1.setVisibility(View.GONE);
                                despues_icon_gallery_amp_l1.setVisibility(View.VISIBLE);
                                if(despues_amp_l1.isEmpty())
                                    System.out.println("despues_amp_l1 NULL");
                                break;
                            case "28":
                                despues_amp_l2 = Image.convert(bitmap);
                                datos.put("despues_amp_l2",despues_amp_l2);
                                Log.e("despues_amp_l2","25");
                                despues_icon_camera_amp_l2.setVisibility(View.GONE);
                                despues_icon_gallery_amp_l2.setVisibility(View.VISIBLE);
                                if(despues_amp_l2.isEmpty())
                                    System.out.println("despues_amp_l2 NULL");
                                break;
                            case "29":
                                despues_amp_l3 = Image.convert(bitmap);
                                datos.put("despues_amp_l3",despues_amp_l3);
                                Log.e("despues_amp_l3","26");
                                despues_icon_camera_amp_l3.setVisibility(View.GONE);
                                despues_icon_gallery_amp_l3.setVisibility(View.VISIBLE);
                                if(despues_amp_l3.isEmpty())
                                    System.out.println("despues_amp_l3 NULL");
                                break;
                            case "30":
                                despues_volt_l1 = Image.convert(bitmap);
                                datos.put("despues_volt_l1",despues_volt_l1);
                                Log.e("despues_volt_l1","24");
                                despues_icon_camera_volt_l1.setVisibility(View.GONE);
                                despues_icon_gallery_volt_l1.setVisibility(View.VISIBLE);
                                if(despues_volt_l1.isEmpty())
                                    System.out.println("despues_volt_l1 NULL");
                                break;
                            case "31":
                                despues_volt_l2 = Image.convert(bitmap);
                                datos.put("despues_volt_l2",despues_volt_l2);
                                Log.e("despues_volt_l2","25");
                                despues_icon_camera_volt_l2.setVisibility(View.GONE);
                                despues_icon_gallery_volt_l2.setVisibility(View.VISIBLE);
                                if(despues_volt_l2.isEmpty())
                                    System.out.println("despues_volt_l2 NULL");
                                break;
                            case "32":
                                despues_volt_l3 = Image.convert(bitmap);
                                datos.put("despues_volt_l3",despues_volt_l3);
                                Log.e("despues_volt_l3","26");
                                despues_icon_camera_volt_l3.setVisibility(View.GONE);
                                despues_icon_gallery_volt_l3.setVisibility(View.VISIBLE);
                                if(despues_volt_l3.isEmpty())
                                    System.out.println("despues_volt_l3 NULL");
                                break;
                        }
                    } catch (Exception e) {
                        viewDialog.hideDialog(1);
                        e.printStackTrace();
                        System.out.println("getFases_error: " + e);
                    }
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.activity_menu_principal_drawer,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
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
        startActivity(new Intent(ctx, TiendasActivity.class));
        return true;
    }
}
