package com.system.operaciones.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
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
import com.system.operaciones.adapters.InstalacionAdapter;
import com.system.operaciones.adapters.UrgenciaAdapter;
import com.system.operaciones.response.RespuestaResponse;
import com.system.operaciones.utils.Credentials;
import com.system.operaciones.utils.Image;
import com.system.operaciones.utils.Utils;
import com.system.operaciones.utils.ViewDialog;
import com.toptoche.searchablespinnerlibrary.SearchableSpinner;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.TimeZone;

public class InstalacionesActivity extends AppCompatActivity implements View.OnClickListener{
    Context ctx;
    Credentials cred;
    LinearLayout datos, equipos, servicios;
    LinearLayout tab_datos, tab_equipos, tab_servicios;
    ImageView btn_new_instalacion, icon_tuerca, icon_split, icon_check,pdf_error;
    TextView tv_admin_cel, tv_admin, tv_tienda_tlf, tv_tienda, tv_email, tv_direccion;
    TextView txt_settings, txt_equipos, txt_datos, observaciones;
    Button dialog_btn_cancelar, dialog_btn_registrar;
    AlertDialog alertDialog;
    String tienda_id;
    String user_type;

    private EditText dialog_hora, dialog_fecha;
    private ImageView icon_calendar, icon_clock;
    private String str_fecha, str_hora;
    private int tipo_proveedor = 1;

    private String[] personal_ids;
    private SearchableSpinner spinner_personal;
    private String personal_id;

    RecyclerView recycler;
    List<JSONObject> l = new ArrayList<>();
    InstalacionAdapter adapter;
    RecyclerView equipos_recycler;
    List<JSONObject> equipos_l = new ArrayList<>();
    EquipoAdapter equipos_adapter;


    int equipo_count = 0;
    int cortina_count = 0;
    int tipo_nro_serie = 1;
    private static final int CODIGO_INTENT = 2;

    ViewDialog viewDialog;

    String lat,lng;

    ImageView img_adjunto;
    ImageView img_adjunto_view;
    String adjunto="";

    String[] equipos_ids;

    int cant_equipos=0;
    int cant_cortinas=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_instalaciones);
        ctx = this;
        cred = new Credentials(ctx);
        viewDialog = new ViewDialog(this);

        user_type = cred.getData("key_user_type");
        System.out.println("user_type"+user_type);

        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Instalaciones");

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
        tv_direccion = findViewById(R.id.direccion);
        tv_direccion.setOnClickListener(this);
        pdf_error = findViewById(R.id.img_error_code);
        pdf_error.setOnClickListener(this);

        btn_new_instalacion = findViewById(R.id.btn_new_instalacion);
        icon_tuerca = findViewById(R.id.icon_tuerca);
        icon_split = findViewById(R.id.icon_split);
        icon_check = findViewById(R.id.icon_check);

        txt_datos = findViewById(R.id.txt_datos);
        txt_equipos = findViewById(R.id.txt_equipos);
        txt_settings = findViewById(R.id.txt_settings);

        recycler = findViewById(R.id.recycler_view_instalaciones);
        recycler.setLayoutManager(new LinearLayoutManager(ctx));
        adapter = new InstalacionAdapter(l);
        recycler.setAdapter(adapter);
        l.clear();

        equipos_recycler = findViewById(R.id.recycler_view_instalaciones_equipos);
        equipos_recycler.setLayoutManager(new LinearLayoutManager(ctx));
        equipos_adapter = new EquipoAdapter(equipos_l);
        equipos_recycler.setAdapter(equipos_adapter);
        equipos_l.clear();

        btn_new_instalacion.setOnClickListener(this);

        tienda_id = cred.getData("key_tienda_id");
        setTienda_id(tienda_id);
        getTienda();
        if(!user_type.equals("3"))
            getInstalaciones();
        else
            getInstalacionesByUsuario(cred.getData("user_id"));
//        getEquipos();
        getEquiposTienda();

        System.out.println("usuario_id: "+cred.getData("user_id"));

        datos.setVisibility(View.VISIBLE);
        tab_datos.setBackgroundColor(getResources().getColor(R.color.verdePastel));

        tab_datos.setOnClickListener(this);
        tab_equipos.setOnClickListener(this);
        tab_servicios.setOnClickListener(this);
        txt_datos.setTextColor(ctx.getResources().getColor(R.color.blanco, null));
        icon_check.setImageResource(R.drawable.icon_check_white);

        equipos_adapter.notifyDataSetChanged();
    }

    public int getEquipo_count() {
        return equipo_count;
    }

    public void setEquipo_count(int equipo_count) {
        this.equipo_count = equipo_count;
    }

    public int getCortina_count() {
        return cortina_count;
    }

    public void setCortina_count(int cortina_count) {
        this.cortina_count = cortina_count;
    }

    public int getCant_equipos() {
        return cant_equipos;
    }

    public void setCant_equipos(int cant_equipos) {
        this.cant_equipos = cant_equipos;
    }

    public int getCant_cortinas() {
        return cant_cortinas;
    }

    public void setCant_cortinas(int cant_cortinas) {
        this.cant_cortinas = cant_cortinas;
    }

    @Override
    protected void onStart()
    {
        super.onStart();
        getInstalaciones();
    }

    @Override
    protected void onPostResume()
    {
        super.onPostResume();
        getInstalaciones();
    }

    public String getTienda_id()
    {
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

    private void escanear() {
        Intent i = new Intent(ctx, LectorActivity.class);
        startActivityForResult(i, CODIGO_INTENT);
    }

    @Override
    public void onClick(View v)
    {
        int id = v.getId();

        switch (id) {
            case R.id.direccion:
                System.out.println("lat: "+lat+", lng: "+lng);
                if(!lat.equals("0") && !lng.equals("0")){
                    String strUri = "http://maps.google.com/maps?q=loc:" + lat + "," + lng + " (" + "Label which you want" + ")";
                    Intent intent = new Intent(android.content.Intent.ACTION_VIEW, Uri.parse(strUri));

                    intent.setClassName("com.google.android.apps.maps", "com.google.android.maps.MapsActivity");

                    startActivity(intent);
                }else{
                    Toast.makeText(ctx,"Información aún no disponible",Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.img_error_code:
                String pdf_url = ctx.getResources().getString(R.string.pdf_url_urgencia)+"codigo_errores.pdf";
                Utils.openPdf(ctx,pdf_url);
                break;
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
                icon_tuerca.setImageResource(R.drawable.icon_instalacion_white);
                txt_settings.setTextColor(ctx.getResources().getColor(R.color.blanco));
                servicios.setVisibility(View.VISIBLE);
                tab_servicios.setBackgroundColor(getResources().getColor(R.color.verdePastel));
                break;
            case R.id.btn_new_instalacion:
                final AlertDialog.Builder builder = new AlertDialog.Builder(ctx);
                // retrieve display dimensions
                Rect displayRectangle = new Rect();
                Window window = getWindow();
                window.getDecorView().getWindowVisibleDisplayFrame(displayRectangle);
                // inflate and adjust layout
                LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View dialogView = inflater.inflate(R.layout.dialog_new_instalacion, null);
                dialogView.setMinimumWidth((int) (displayRectangle.width() * 0.5f));
                dialogView.setMinimumHeight((int) (displayRectangle.height() * 0.7f));

                img_adjunto = dialogView.findViewById(R.id.img_adjunto);
                img_adjunto_view = dialogView.findViewById(R.id.img_adjunto_view);

                img_adjunto.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        pickFromGallery();
                    }
                });
                img_adjunto_view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        showPreview(adjunto);
                    }
                });
                spinner_personal = dialogView.findViewById(R.id.dialog_spinner_contratistas);
                spinner_personal.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        personal_id = personal_ids[position];
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });

                builder.setView(dialogView);

                dialog_fecha = dialogView.findViewById(R.id.dialog_urgencia_fecha);
                dialog_hora = dialogView.findViewById(R.id.dialog_urgencia_hora);
                icon_calendar = dialogView.findViewById(R.id.dialog_icon_calendar);
                icon_clock = dialogView.findViewById(R.id.dialog_icon_clock);
                dialog_hora = dialogView.findViewById(R.id.dialog_urgencia_hora);
                dialog_btn_cancelar = dialogView.findViewById(R.id.dialog_btn_cancelar);
                dialog_btn_registrar = dialogView.findViewById(R.id.dialog_btn_registrar);
                observaciones = dialogView.findViewById(R.id.dialog_observaciones);
                RadioButton radioUezu = dialogView.findViewById(R.id.radio_uezu);
                RadioButton radioContratistas = dialogView.findViewById(R.id.radio_contratistas);
                final TextView label_personal = dialogView.findViewById(R.id.label_personal);

                tipo_proveedor = 1;
                getEquipos();
                getTecnicos();
                radioUezu.setChecked(true);
                radioUezu.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        label_personal.setText("Técnicos");
                        tipo_proveedor=1;
                        getTecnicos();
                    }
                });
                radioContratistas.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        label_personal.setText("Contratistas");
                        tipo_proveedor=2;
                        getContratistas();
                    }
                });

                spinner_personal.setPositiveButton("Cerrar");

                Calendar cal = Calendar.getInstance(TimeZone.getDefault()); // Get current date

                // Create the DatePickerDialog instance
                final DatePickerDialog datePicker = new DatePickerDialog(ctx, new DatePickerDialog.OnDateSetListener() {

                    // when dialog box is closed, below method will be called.
                    public void onDateSet(DatePicker view, int selectedYear,
                                          int selectedMonth, int selectedDay) {
                        String year1 = String.valueOf(selectedYear);
                        String month1 = ((selectedMonth + 1) < 10 ? "0" : "") + (selectedMonth + 1);
                        String day1 = ((selectedDay < 10) ? "0" : "") + selectedDay;
                        str_fecha = year1 + "-" + month1 + "-" + day1;
                        dialog_fecha.setText(str_fecha);
                        dialog_fecha.setError(null);
                    }
                },
                        cal.get(Calendar.YEAR),
                        cal.get(Calendar.MONTH),
                        cal.get(Calendar.DAY_OF_MONTH));
                datePicker.setCancelable(false);
                datePicker.setTitle("Seleccione la fecha");
                icon_calendar.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        datePicker.show();
                    }
                });
                dialog_fecha.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                    @Override
                    public void onFocusChange(View v, boolean hasFocus) {
                        if(hasFocus)
                            datePicker.show();
                    }
                });
                dialog_fecha.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        datePicker.show();
                    }
                });
                // Create the DatePickerDialog instance
                final TimePickerDialog timePicker = new TimePickerDialog(ctx, new TimePickerDialog.OnTimeSetListener() {

                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        String hour = (hourOfDay < 10 ? "0" : "") + hourOfDay;
                        String min = (minute < 10 ? "0" : "") + minute;
                        str_hora = hour + ":" + min;
                        dialog_hora.setText(str_hora);
                        dialog_hora.setError(null);
                    }
                },
                        cal.get(Calendar.HOUR_OF_DAY),
                        cal.get(Calendar.MINUTE),
                        true);
                datePicker.setCancelable(false);
                datePicker.setTitle("Seleccione la hora");

                icon_clock.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        timePicker.show();
                    }
                });
                dialog_hora.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                    @Override
                    public void onFocusChange(View v, boolean hasFocus) {
                        if(hasFocus)
                            timePicker.show();
                    }
                });
                dialog_hora.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        timePicker.show();
                    }
                });

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
                        registerInstalacion(observaciones.getText().toString(), str_fecha, str_hora, personal_id);
                    }
                });
                LinearLayout linear_fecha = dialogView.findViewById(R.id.linear_fecha);
                LinearLayout linear_personal = dialogView.findViewById(R.id.linear_personal);
                if(cred.getData("key_user_type").equals("2")){
                    linear_fecha.setVisibility(View.GONE);
                    linear_personal.setVisibility(View.GONE);
                }else{
                    linear_fecha.setVisibility(View.VISIBLE);
                    linear_personal.setVisibility(View.VISIBLE);
                }
                alertDialog = builder.create();
                alertDialog.show();
                break;
        }
    }

    public void hideTabs()
    {
        equipos.setVisibility(View.GONE);
        servicios.setVisibility(View.GONE);
        datos.setVisibility(View.GONE);
        tab_datos.setBackgroundColor(getResources().getColor(R.color.plomoBackground));
        tab_equipos.setBackgroundColor(getResources().getColor(R.color.plomoBackground));
        tab_servicios.setBackgroundColor(getResources().getColor(R.color.plomoBackground));

        icon_tuerca.setImageResource(R.drawable.icon_instalacion);
        icon_split.setImageResource(R.drawable.icon_split);
        icon_check.setImageResource(R.drawable.icon_check);

        txt_datos.setTextColor(ctx.getResources().getColor(R.color.negro));
        txt_equipos.setTextColor(ctx.getResources().getColor(R.color.negro));
        txt_settings.setTextColor(ctx.getResources().getColor(R.color.negro));
    }

    private void getContratistas()
    {
        String url = ctx.getApplicationContext().getString(R.string.base_url) + ctx.getApplicationContext().getString(R.string.getContratistas_url);
        RequestQueue queue = Volley.newRequestQueue(ctx);

        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        System.out.println("getContratistas_response: " + response);
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
                                for (Object o : respuesta) {
                                    JSONObject ob = (JSONObject) o;
                                    String motivo = (String) ob.get("proveedor");
                                    String id = (String) ob.get("id");
                                    data[i] = motivo;
                                    data_id[i] = id;
                                    i++;
                                }
                                ArrayAdapter dialog_personal_adapter = new ArrayAdapter<String>(ctx,R.layout.dropdown_style,data);
                                personal_ids = data_id;
                                spinner_personal.setAdapter(dialog_personal_adapter);
                                dialog_personal_adapter.notifyDataSetChanged();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println("getContratistas_error: " + error.getMessage());
            }
        });

        // Add the request to the RequestQueue.
        queue.add(stringRequest);
    }

    private void getTecnicos()
    {
        String url = ctx.getApplicationContext().getString(R.string.base_url) + ctx.getApplicationContext().getString(R.string.getTecnicos_url);
        RequestQueue queue = Volley.newRequestQueue(ctx);

        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        System.out.println("getTecnicos_response: " + response);
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
                                for (Object o : respuesta) {
                                    JSONObject ob = (JSONObject) o;
                                    String motivo = (String) ob.get("tecnico");
                                    String id = (String) ob.get("id");
                                    data[i] = motivo;
                                    data_id[i] = id;
                                    i++;
                                }

                                ArrayAdapter dialog_personal_adapter = new ArrayAdapter<String>(ctx,R.layout.dropdown_style,data);
                                personal_ids = data_id;
                                spinner_personal.setAdapter(dialog_personal_adapter);
                                dialog_personal_adapter.notifyDataSetChanged();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println("getTecnicos_error: " + error.getMessage());
            }
        });
        // Add the request to the RequestQueue.
        queue.add(stringRequest);
    }

    public void getTienda()
    {
        viewDialog.showDialog();
        String url = ctx.getApplicationContext().getString(R.string.base_url) + ctx.getApplicationContext().getString(R.string.getTienda_url);
        Log.i("getTienda_url", url);
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
                                Toast.makeText(ctx, cliente.getDes_error(), Toast.LENGTH_SHORT).show();
                            } else {
                                for (Object o : respuesta) {
                                    JSONObject ob = (JSONObject) o;
                                    String tienda = ob.get("ceco")+" - "+ob.get("tienda");
                                    final String tienda_tlf = (String) ob.get("tienda_cel");
                                    String admin = (String) ob.get("administrador");
                                    final String admin_cel = (String) ob.get("admin_cel");
                                    String direccion = (String) ob.get("direccion");
                                    String distrito = (String) ob.get("distrito");
                                    final String email = (String)ob.get("email");
                                    String equipos = (String)ob.get("cant_equipos");
                                    String cortinas = (String)ob.get("cant_cortinas");
                                    setCant_equipos(Integer.parseInt(equipos));
                                    setCant_cortinas(Integer.parseInt(cortinas));
                                    lat = (String)ob.get("latitud");
                                    lng = (String)ob.get("longitud");

                                    tv_tienda.setText(tienda);
                                    tv_tienda_tlf.setText(tienda_tlf);
                                    tv_email.setText(email);
                                    if(!email.isEmpty() && !user_type.equals("3")){
                                        tv_email.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                Intent intent = new Intent(Intent.ACTION_VIEW);
                                                Uri data = Uri.parse("mailto:"+email+"?subject=" + "" + "&body=" + "");
                                                intent.setData(data);
                                                startActivity(intent);
                                            }
                                        });
                                    }
                                    if (!tienda_tlf.isEmpty() && !user_type.equals("3")) {
                                        tv_tienda_tlf.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                Intent callIntent = new Intent(Intent.ACTION_CALL);
                                                callIntent.setData(Uri.parse("tel:+51" + tienda_tlf));
                                                if (checkSelfPermission(Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                                                    // TODO: Consider calling
                                                    //    Activity#requestPermissions
                                                    // here to request the missing permissions, and then overriding
                                                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                                    //                                          int[] grantResults)
                                                    // to handle the case where the user grants the permission. See the documentation
                                                    // for Activity#requestPermissions for more details.
                                                    return;
                                                }
                                                startActivity(callIntent);
                                            }
                                        });
                                    }
                                    tv_admin.setText(admin);
                                    tv_admin_cel.setText(admin_cel);

                                    if (!admin_cel.isEmpty() && !user_type.equals("3")) {
                                        tv_admin_cel.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                Intent callIntent = new Intent(Intent.ACTION_CALL);
                                                callIntent.setData(Uri.parse("tel:+51" + admin_cel));
                                                if (checkSelfPermission(Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                                                    // TODO: Consider calling
                                                    //    Activity#requestPermissions
                                                    // here to request the missing permissions, and then overriding
                                                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                                    //                                          int[] grantResults)
                                                    // to handle the case where the user grants the permission. See the documentation
                                                    // for Activity#requestPermissions for more details.
                                                    return;
                                                }
                                                startActivity(callIntent);
                                            }
                                        });
                                    }
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

    public void getInstalaciones()
    {
        String url=ctx.getApplicationContext().getString(R.string.base_url)+ctx.getApplicationContext().getString(R.string.getInstalaciones_url);
        Log.i("getInstalaciones_url",url);
        RequestQueue queue = Volley.newRequestQueue(ctx);

        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        System.out.println("getInstalaciones_response: "+response);
                        try {
                            RespuestaResponse cliente = new Gson().fromJson(response, RespuestaResponse.class);
                            JSONParser parser = new JSONParser();
                            JSONArray respuesta = (JSONArray) parser.parse((String) cliente.getRespuesta());

                            if (cliente.getIde_error() == 0) {
                                Toast.makeText(ctx, cliente.getDes_error(), Toast.LENGTH_SHORT).show();
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
                System.out.println("getInstalaciones_error: " + error.getMessage());
                Toast.makeText(ctx, error.getMessage(), Toast.LENGTH_SHORT).show();
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

    public void getInstalacionesByUsuario(final String usuario_id)
    {
        String url=ctx.getApplicationContext().getString(R.string.base_url)+ctx.getApplicationContext().getString(R.string.getInstalacionesByUsuario_url);
        Log.i("getInstalaciones_url",url);
        RequestQueue queue = Volley.newRequestQueue(ctx);

        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            RespuestaResponse cliente = new Gson().fromJson(response, RespuestaResponse.class);
                            JSONParser parser = new JSONParser();
                            JSONArray respuesta = (JSONArray) parser.parse((String) cliente.getRespuesta());

                            if (cliente.getIde_error() == 0) {
                                Toast.makeText(ctx, cliente.getDes_error(), Toast.LENGTH_SHORT).show();
                            } else {
                                l.clear();
                                for(Object o: respuesta){
                                    JSONObject ob = (JSONObject)o;
                                    System.out.println("json_urgencias: "+ob);
                                    l.add(ob);
                                }
                                adapter.notifyDataSetChanged();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            System.out.println("getInstalacionesByUsuario_error1: "+e);
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println("getInstalacionesByUsuario_error2: " + error.getMessage());
                Toast.makeText(ctx, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String, String> getParams()
            {
                Map<String, String> params = new HashMap<>();
                params.put("tienda_id", tienda_id);
                params.put("usuario_id", usuario_id);
                return params;
            }
        };

        // Add the request to the RequestQueue.
        queue.add(stringRequest);
    }

    public void getEquipos()
    {
        Log.e("tienda_id",tienda_id);
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
                                Toast.makeText(ctx, cliente.getDes_error(), Toast.LENGTH_SHORT).show();
                            } else {
                                String[] equipos = new String[respuesta.size()];
                                equipos_ids = new String[respuesta.size()];
                                int i=0;
                                for(Object o: respuesta){
                                    JSONObject ob = (JSONObject)o;
                                    System.out.println(ob);
                                    if(!ob.get("id").equals("0")){
                                        equipos[i] = "Equipo "+ob.get("nro_equipo");
                                    }else{
                                        equipos[i] = (String)ob.get("evap_nro_serie");
                                    }
                                    equipos_ids[i] = (String)ob.get("id");
                                    i++;
                                    setEquipo_count(i+1);
                                }
                                equipos_adapter.notifyDataSetChanged();
                                viewDialog.hideDialog(1);
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
                System.out.println("getEquipos_error: " + error.getMessage());
                Toast.makeText(ctx, error.getMessage(), Toast.LENGTH_LONG).show();
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

    public void getEquiposTienda()
    {
        String url=ctx.getApplicationContext().getString(R.string.base_url)+ctx.getApplicationContext().getString(R.string.getEquiposTienda_url);
        Log.i("getEquiposTienda_url",url);
        RequestQueue queue = Volley.newRequestQueue(ctx);

        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        System.out.println("getEquiposTienda_response: " + response);
                        try {
                            RespuestaResponse cliente = new Gson().fromJson(response, RespuestaResponse.class);
                            JSONParser parser = new JSONParser();
                            JSONArray respuesta = (JSONArray) parser.parse((String) cliente.getRespuesta());

                            if (cliente.getIde_error() == 0) {
                                viewDialog.hideDialog(1);
                                Toast.makeText(ctx, cliente.getDes_error(), Toast.LENGTH_LONG).show();
                            } else {
                                equipos_l.clear();
                                for(Object o: respuesta){
                                    JSONObject ob = (JSONObject)o;
                                    equipos_l.add(ob);
                                }
                                equipos_adapter.notifyDataSetChanged();
                                viewDialog.hideDialog(1);
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
                System.out.println("getEquiposTienda_error: " + error.getMessage());
                Toast.makeText(ctx, error.getMessage(), Toast.LENGTH_LONG).show();
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

    public void registerInstalacion(final String observaciones,final String fecha,final String hora,final String contratista_id)
    {
        String url=ctx.getApplicationContext().getString(R.string.base_url)+ctx.getApplicationContext().getString(R.string.crear_instalacion_url);
        Log.i("create_urgencia_url",url);
        RequestQueue queue = Volley.newRequestQueue(ctx);

        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        System.out.println("create_instalacion_response: " + response);
                        try {
                            RespuestaResponse cliente = new Gson().fromJson(response, RespuestaResponse.class);
                            JSONParser parser = new JSONParser();
                            JSONArray respuesta = (JSONArray) parser.parse((String) cliente.getRespuesta());

                            if (cliente.getIde_error() == 0) {
                                Toast.makeText(ctx, cliente.getDes_error(), Toast.LENGTH_SHORT).show();
                            } else {
                                getInstalaciones();
                                new Utils().sendMailUrgencia(tienda_id,ctx);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            Log.e("create_instalacion_error",e.getMessage());
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println("create_instalacion_error: " + error.getMessage());
            }
        }){
            @Override
            protected Map<String, String> getParams()
            {
                Map<String, String> params = new HashMap<>();
                params.put("tienda_id", tienda_id);
                params.put("usuario_id", cred.getData("user_id"));
                params.put("observaciones", observaciones);
                params.put("fecha", fecha);
                params.put("hora", hora);
                params.put("personal_id", contratista_id);
                params.put("tipo", tipo_proveedor+"");
                params.put("adjunto",adjunto);
                return params;
            }
        };

        // Add the request to the RequestQueue.
        queue.add(stringRequest);
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
        startActivityForResult(intent,1);
    }

    private void showPreview(String b64)
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
                cleanPhoto();
            }
        });

        AlertDialog alert_preview = builder.create();
        alert_preview.show();
    }

    void cleanPhoto()
    {
        img_adjunto.setVisibility(View.VISIBLE);
        img_adjunto_view.setVisibility(View.GONE);
        adjunto="";
    }
}
