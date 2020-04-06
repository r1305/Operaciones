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

    private String linea_baja,linea_baja_mtr,separador,presostato_baja,linea_alta,linea_alta_mtr,secador,presostato_alta,cableado,cableado_mtr,interruptor,interruptor_mtr,protector,bomba,tuberia,bandeja;
    private Spinner spinner_linea_baja,spinner_linea_baja_mtr,spinner_separador,spinner_presostato_baja,spinner_linea_alta,spinner_linea_alta_mtr,spinner_secador,spinner_presostato_alta,spinner_cableado,spinner_cableado_mtr,spinner_interruptor,spinner_interruptor_mtr,spinner_protector,spinner_bomba,spinner_tuberia,spinner_bandeja;

    private String image_signature_tecnico="",name_tecnico="",dni_tecnico="",cargo_tecnico="";
    private String image_signature_cliente="",name_cliente="",dni_cliente="",cargo_cliente="";

    //Evaporadora
    private String img_evap_placa="",img_evap_frente="",img_evap_isometrico="";
    private ImageView cam_evap_placa,cam_evap_frente,cam_evap_isometrico;
    private ImageView gallery_evap_placa,gallery_evap_frente,gallery_evap_isometrico;
    //Condensadora
    private String img_cond_placa="",img_cond_frente="",img_cond_isometrico="";
    private ImageView cam_cond_placa,cam_cond_frente,cam_cond_isometrico;
    private ImageView gallery_cond_placa,gallery_cond_frente,gallery_cond_isometrico;
    //Cortina
    private String img_cortina="";
    private ImageView cam_cortina;
    private ImageView gallery_cortina;
    //Bomba
    private String img_bomba="";
    private ImageView cam_bomba;
    private ImageView gallery_bomba;

    int tipo_nro_serie = 1;
    private static final int  CODIGO_INTENT = 2;

    AlertDialog alertDialog;
    ViewDialog viewDialog;

    EditText cargo,dni,name;
    String fase = "0";
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
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    private void registrarFicha(final String instalacion_id)
    {
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

                return params;
            }
        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                100000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        // Add the request to the RequestQueue.
        queue.add(stringRequest);

    }

    private void createFicha(final String urgencia_id)
    {
        String url=ctx.getApplicationContext().getString(R.string.base_url)+ctx.getApplicationContext().getString(R.string.crear_ficha_instalacion_url);
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
                                        ((FichaInstalacionActivity)ctx).finish();
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
                100000,
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
                    Log.e("img_evap_placa","1");
                    cam_evap_placa.setVisibility(View.GONE);
                    gallery_evap_placa.setVisibility(View.VISIBLE);
                    datos.put("img_evap_placa",img_evap_placa);
                    cred.save_data("1-"+id,datos.toString());
                    break;
                case "2":
                    img_evap_frente = b64;
                    Log.e("img_evap_frente","2");
                    cam_evap_frente.setVisibility(View.GONE);
                    gallery_evap_frente.setVisibility(View.VISIBLE);
                    datos.put("img_evap_frente",img_evap_frente);
                    cred.save_data("1-"+id,datos.toString());
                    break;
                case "3":
                    img_evap_isometrico = b64;
                    Log.e("img_evap_isometrico","3");
                    cam_evap_isometrico.setVisibility(View.GONE);
                    gallery_evap_isometrico.setVisibility(View.VISIBLE);
                    datos.put("img_evap_isometrico",img_evap_isometrico);
                    cred.save_data("1-"+id,datos.toString());
                    break;
                //Condensadora
                case "4":
                    img_cond_placa = b64;
                    Log.e("img_cond_placa","1");
                    cam_cond_placa.setVisibility(View.GONE);
                    gallery_cond_placa.setVisibility(View.VISIBLE);
                    datos.put("img_cond_placa",img_cond_placa);
                    cred.save_data("1-"+id,datos.toString());
                    break;
                case "5":
                    img_cond_frente = b64;
                    Log.e("img_cond_frente","2");
                    cam_cond_frente.setVisibility(View.GONE);
                    gallery_cond_frente.setVisibility(View.VISIBLE);
                    datos.put("img_cond_frente",img_cond_frente);
                    cred.save_data("1-"+id,datos.toString());
                    break;
                case "6":
                    img_cond_isometrico = b64;
                    Log.e("img_cond_isometrico","3");
                    cam_cond_isometrico.setVisibility(View.GONE);
                    gallery_cond_isometrico.setVisibility(View.VISIBLE);
                    datos.put("img_cond_isometrico",img_cond_isometrico);
                    cred.save_data("1-"+id,datos.toString());
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
                    if(tipo_nro_serie==1) {
//                        et_nro_serie.setText(codigo);
                    }
                    else{
//                        et_cond_nro_serie.setText(codigo);
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
                                Log.e("img_evap_placa","1");
                                cam_evap_placa.setVisibility(View.GONE);
                                gallery_evap_placa.setVisibility(View.VISIBLE);
                                datos.put("img_evap_placa",img_evap_placa);
                                cred.save_data("1-"+id,datos.toString());
                                break;
                            case "2":
                                img_evap_frente = Image.convert(bitmap);
                                Log.e("img_evap_frente","2");
                                cam_evap_frente.setVisibility(View.GONE);
                                gallery_evap_frente.setVisibility(View.VISIBLE);
                                datos.put("img_evap_frente",img_evap_frente);
                                cred.save_data("1-"+id,datos.toString());
                                break;
                            case "3":
                                img_evap_isometrico = Image.convert(bitmap);
                                Log.e("img_evap_isometrico","3");
                                cam_evap_isometrico.setVisibility(View.GONE);
                                gallery_evap_isometrico.setVisibility(View.VISIBLE);
                                datos.put("img_evap_isometrico",img_evap_isometrico);
                                cred.save_data("1-"+id,datos.toString());
                                break;
                            //Condensadora
                            case "4":
                                img_cond_placa = Image.convert(bitmap);
                                Log.e("img_cond_placa","1");
                                cam_cond_placa.setVisibility(View.GONE);
                                gallery_cond_placa.setVisibility(View.VISIBLE);
                                datos.put("img_cond_placa",img_cond_placa);
                                cred.save_data("1-"+id,datos.toString());
                                break;
                            case "5":
                                img_cond_frente = Image.convert(bitmap);
                                Log.e("img_cond_frente","2");
                                cam_cond_frente.setVisibility(View.GONE);
                                gallery_cond_frente.setVisibility(View.VISIBLE);
                                datos.put("img_cond_frente",img_cond_frente);
                                cred.save_data("1-"+id,datos.toString());
                                break;
                            case "6":
                                img_cond_isometrico = Image.convert(bitmap);
                                Log.e("img_cond_isometrico","3");
                                cam_cond_isometrico.setVisibility(View.GONE);
                                gallery_cond_isometrico.setVisibility(View.VISIBLE);
                                datos.put("img_cond_isometrico",img_cond_isometrico);
                                cred.save_data("1-"+id,datos.toString());
                                break;
                        }
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
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.floating_menu,menu);
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

    @Override
    public void onClick(View v) {
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
            case R.id.icon_cam_evap_frente:
                openChooser();
                cred.save_data("image_type","2");
                break;
            case R.id.icon_cam_evap_isometrico:
                openChooser();
                cred.save_data("image_type","3");
                break;
            case R.id.icon_gallery_evap_placa:
                showPreview(img_evap_placa,1);
                break;
            case R.id.icon_gallery_evap_frente:
                showPreview(img_evap_frente,2);
                break;
            case R.id.icon_gallery_evap_isometrico:
                showPreview(img_evap_isometrico,3);
                break;
            //Condensadora
            case R.id.icon_cam_cond_placa:
                openChooser();
                cred.save_data("image_type","4");
                break;
            case R.id.icon_cam_cond_frente:
                openChooser();
                cred.save_data("image_type","5");
                break;
            case R.id.icon_cam_cond_isometrico:
                openChooser();
                cred.save_data("image_type","6");
                break;
            case R.id.icon_gallery_cond_placa:
                showPreview(img_cond_placa,1);
                break;
            case R.id.icon_gallery_cond_frente:
                showPreview(img_cond_frente,2);
                break;
            case R.id.icon_gallery_cond_isometrico:
                showPreview(img_cond_isometrico,3);
                break;

        }
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

        }
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
//        dialogView.setMinimumWidth((int) (displayRectangle.width() * 0.5f));
//        dialogView.setMinimumHeight((int) (displayRectangle.height() * 0.7f));
        builder.setView(dialogView);
        builder.setNegativeButton("Registrar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.setNeutralButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
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
        builder.setNegativeButton("Registrar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
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
    }

    void showModalFirmaCliente()
    {
        androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(ctx);
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
        final androidx.appcompat.app.AlertDialog alertDialog = builder.create();
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
        AlertDialog.Builder builder = new AlertDialog.Builder(ctx);
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_new_cortina, null);
//        dialogView.setMinimumWidth((int) (displayRectangle.width() * 0.5f));
//        dialogView.setMinimumHeight((int) (displayRectangle.height() * 0.7f));
        builder.setView(dialogView);
        builder.setCancelable(false);
        builder.setNegativeButton("Registrar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
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
    }

    void showModalBomba()
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(ctx);
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_new_bomba, null);
//        dialogView.setMinimumWidth((int) (displayRectangle.width() * 0.5f));
//        dialogView.setMinimumHeight((int) (displayRectangle.height() * 0.7f));
        builder.setView(dialogView);
        builder.setCancelable(false);
        builder.setNegativeButton("Registrar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
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
        btn_cancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });
        alertDialog.show();
    }
}
