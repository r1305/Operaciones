package com.system.operaciones.activities;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
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
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.system.operaciones.R;
import com.system.operaciones.adapters.EquipoAdapter;
import com.system.operaciones.adapters.UrgenciaAdapter;
import com.system.operaciones.response.RespuestaResponse;
import com.system.operaciones.utils.Credentials;
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

import jrizani.jrspinner.JRSpinner;

public class UrgenciasActivity extends AppCompatActivity implements View.OnClickListener {

    Context ctx;
    Credentials cred;
    LinearLayout datos, equipos, servicios;
    LinearLayout tab_datos, tab_equipos, tab_servicios;
    ImageView btn_new_urgencia, icon_tuerca, icon_split, icon_check, btn_new_equipo;
    Spinner motivos_spin;
    TextView tv_admin_cel, tv_admin, tv_tienda_tlf, tv_tienda, tv_email, tv_direccion;
    TextView txt_settings, txt_equipos, txt_datos, observaciones;
    Button dialog_btn_cancelar, dialog_btn_registrar;
    AlertDialog alertDialog;
    String tienda_id;
    ArrayAdapter<String> motivo_adapter;
    String[] motivo_id_adapter;
    private EditText dialog_hora, dialog_fecha;
    private ImageView icon_calendar, icon_clock;
    private String str_fecha, str_hora;
    private int tipo_proveedor = 1;

    private String[] personal_ids;
    private SearchableSpinner personal_spinner;
    private String personal_id;

    RecyclerView recycler;
    List<JSONObject> l = new ArrayList<>();
    UrgenciaAdapter adapter;
    SearchableSpinner spinner_equipos;
    RecyclerView equipos_recycler;
    List<JSONObject> equipos_l = new ArrayList<>();
    EquipoAdapter equipos_adapter;

    String[] equipos_ids;
    String equipo_id;

    int equipo_count = 0;
    int tipo_nro_serie = 1;
    private static final int CODIGO_PERMISOS_CAMARA = 1, CODIGO_INTENT = 2;
    private boolean permisoCamaraConcedido = false, permisoSolicitadoDesdeBoton = false;

    ViewDialog viewDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_urgencias);
        ctx = this;
        cred = new Credentials(ctx);
        viewDialog = new ViewDialog(this);

        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Urgencias");
        verificarYPedirPermisosDeCamara();

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

        btn_new_urgencia = findViewById(R.id.btn_new_urgencia);
        btn_new_equipo = findViewById(R.id.btn_new_urgencia_equipo);
        icon_tuerca = findViewById(R.id.icon_tuerca);
        icon_split = findViewById(R.id.icon_split);
        icon_check = findViewById(R.id.icon_check);

        txt_datos = findViewById(R.id.txt_datos);
        txt_equipos = findViewById(R.id.txt_equipos);
        txt_settings = findViewById(R.id.txt_settings);

        recycler = findViewById(R.id.recycler_view_urgencias);
        recycler.setLayoutManager(new LinearLayoutManager(ctx));
        adapter = new UrgenciaAdapter(l);
        recycler.setAdapter(adapter);
        l.clear();

        equipos_recycler = findViewById(R.id.recycler_view_urgencias_equipos);
        equipos_recycler.setLayoutManager(new LinearLayoutManager(ctx));
        equipos_adapter = new EquipoAdapter(equipos_l);
        equipos_recycler.setAdapter(equipos_adapter);
        equipos_l.clear();

        btn_new_urgencia.setOnClickListener(this);
        btn_new_equipo.setOnClickListener(this);

        Intent intent = getIntent();
        tienda_id = intent.getStringExtra("tienda_id");
        setTienda_id(tienda_id);
        getTienda(tienda_id);
        getUrgencias(tienda_id);
        getEquipos();
        getEquiposTienda();

        datos.setVisibility(View.VISIBLE);
        tab_datos.setBackgroundColor(getResources().getColor(R.color.verdePastel));

        tab_datos.setOnClickListener(this);
        tab_equipos.setOnClickListener(this);
        tab_servicios.setOnClickListener(this);
        txt_datos.setTextColor(ctx.getResources().getColor(R.color.blanco, null));
        icon_check.setImageResource(R.drawable.icon_check_white);

        equipos_adapter.notifyDataSetChanged();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CODIGO_INTENT) {
            if (resultCode == Activity.RESULT_OK) {
                if (data != null) {
                    String codigo = data.getStringExtra("codigo");
                    if (tipo_nro_serie == 1)
                        et_nro_serie.setText(codigo);
                    else
                        et_cond_nro_serie.setText(codigo);
                }
            }
        }
    }

    private void verificarYPedirPermisosDeCamara() {
        int estadoDePermiso = ctx.checkSelfPermission(Manifest.permission.CAMERA);
        if (estadoDePermiso == PackageManager.PERMISSION_GRANTED) {
            // En caso de que haya dado permisos ponemos la bandera en true
            // y llamar al método
            permisoCamaraConcedido = true;
        } else {
            // Si no, pedimos permisos. Ahora mira onRequestPermissionsResult
            ActivityCompat.requestPermissions(UrgenciasActivity.this,
                    new String[]{Manifest.permission.CAMERA},
                    CODIGO_PERMISOS_CAMARA);
        }
    }

    private void escanear() {
        Intent i = new Intent(ctx, LectorActivity.class);
        startActivityForResult(i, CODIGO_INTENT);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case CODIGO_PERMISOS_CAMARA:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // Escanear directamten solo si fue pedido desde el botón
                    if (permisoSolicitadoDesdeBoton) {
                        escanear();
                    }
                    permisoCamaraConcedido = true;
                } else {
                    permisoDeCamaraDenegado();
                }
                break;
        }
    }

    private void permisoDeCamaraDenegado() {
        // Esto se llama cuando el usuario hace click en "Denegar" o
        // cuando lo denegó anteriormente
        Toast.makeText(ctx, "No puedes escanear si no das permiso", Toast.LENGTH_SHORT).show();
    }

    public int getEquipo_count() {
        return equipo_count;
    }

    public void setEquipo_count(int equipo_count) {
        this.equipo_count = equipo_count;
    }

    @Override
    protected void onStart()
    {
        super.onStart();
        getUrgencias(tienda_id);
    }

    @Override
    protected void onPostResume()
    {
        super.onPostResume();
        getUrgencias(tienda_id);
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

    @Override
    public void onClick(View v)
    {
        int id = v.getId();

        switch (id) {
            case R.id.tab_datos:
                hideTabs();
//                scroll_datos.setVisibility(View.VISIBLE);
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
                icon_tuerca.setImageResource(R.drawable.icon_urgencia_white);
                txt_settings.setTextColor(ctx.getResources().getColor(R.color.blanco));
                servicios.setVisibility(View.VISIBLE);
                tab_servicios.setBackgroundColor(getResources().getColor(R.color.verdePastel));
                break;
            case R.id.btn_new_urgencia:
                final AlertDialog.Builder builder = new AlertDialog.Builder(ctx);
                // retrieve display dimensions
                Rect displayRectangle = new Rect();
                Window window = getWindow();
                window.getDecorView().getWindowVisibleDisplayFrame(displayRectangle);
                // inflate and adjust layout
                LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View dialogView = inflater.inflate(R.layout.dialog_new_urgencia, null);
                dialogView.setMinimumWidth((int) (displayRectangle.width() * 0.5f));
                dialogView.setMinimumHeight((int) (displayRectangle.height() * 0.7f));
                getEquipos();
                personal_spinner = dialogView.findViewById(R.id.spinner_contratista);
                personal_spinner.setPositiveButton("Cerrar");
                personal_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        personal_id = personal_ids[position];
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });
                getLastMantenimiento(tienda_id);

                builder.setView(dialogView);

                spinner_equipos = dialogView.findViewById(R.id.urgencia_spinner_equipos);
                spinner_equipos.setPositiveButton("Cerrar");
                motivos_spin = dialogView.findViewById(R.id.urgencia_spinner_motivos);
                dialog_fecha = dialogView.findViewById(R.id.dialog_urgencia_fecha);
                dialog_hora = dialogView.findViewById(R.id.dialog_urgencia_hora);
                icon_calendar = dialogView.findViewById(R.id.dialog_icon_calendar);
                icon_clock = dialogView.findViewById(R.id.dialog_icon_clock);
                dialog_hora = dialogView.findViewById(R.id.dialog_urgencia_hora);
                dialog_btn_cancelar = dialogView.findViewById(R.id.dialog_btn_cancelar);
                dialog_btn_registrar = dialogView.findViewById(R.id.dialog_btn_registrar);
                observaciones = dialogView.findViewById(R.id.dialog_observaciones);

                spinner_equipos.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        Log.e("position_equipo",position+"");
                        Log.e("equipo_id",equipos_ids[position]);
                        equipo_id = equipos_ids[position];
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });

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
                        registerUrgencia(observaciones.getText().toString(), equipo_id, str_fecha, str_hora, personal_id);
                    }
                });
                alertDialog = builder.create();
                alertDialog.show();
                break;
            case R.id.btn_new_urgencia_equipo:
                showModalRegisterEquipo();
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

        icon_tuerca.setImageResource(R.drawable.icon_urgencia);
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
                                Toast.makeText(ctx, cliente.getDes_error(), Toast.LENGTH_LONG).show();
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
                                personal_spinner.setAdapter(dialog_personal_adapter);
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
        tipo_proveedor = 2;
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
                                Toast.makeText(ctx, cliente.getDes_error(), Toast.LENGTH_LONG).show();
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
                                personal_spinner.setAdapter(dialog_personal_adapter);
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

    public void getTienda(final String tienda_id)
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
                                Toast.makeText(ctx, cliente.getDes_error(), Toast.LENGTH_LONG).show();
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

                                    tv_tienda.setText(tienda);
                                    tv_tienda_tlf.setText(tienda_tlf);
                                    tv_email.setText(email);
                                    if(!email.isEmpty()){
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
                                    if (!tienda_tlf.isEmpty()) {
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
                                    if (!admin_cel.isEmpty()) {
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

    public void getUrgencias(final String tienda_id)
    {
        String url=ctx.getApplicationContext().getString(R.string.base_url)+ctx.getApplicationContext().getString(R.string.getUrgencias_url);
        Log.i("getUrgencias_url",url);
        RequestQueue queue = Volley.newRequestQueue(ctx);

        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        System.out.println("getUrgencias_response: " + response);
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
                System.out.println("getUrgencias_error: " + error.getMessage());
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
                                        equipos[i] = ob.get("modelo")+" - "+ob.get("evap_nro_serie");
                                    }else{
                                        equipos[i] = (String)ob.get("evap_nro_serie");
                                    }
                                    equipos_ids[i] = (String)ob.get("id");
                                    i++;
                                    setEquipo_count(i+1);
                                }
                                ArrayAdapter dialog_equipos_adapter = new ArrayAdapter<String>(ctx,R.layout.dropdown_style,equipos);
                                motivo_id_adapter = equipos_ids;
                                if(spinner_equipos!=null){
                                    spinner_equipos.setAdapter(dialog_equipos_adapter);
                                    dialog_equipos_adapter.notifyDataSetChanged();
                                }

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
                Toast.makeText(ctx, error.getMessage(), Toast.LENGTH_LONG).show();
            }
        });

        // Add the request to the RequestQueue.
        queue.add(stringRequest);
    }

    public void registerUrgencia(final String observaciones,final String equipo_id,final String fecha,final String hora,final String contratista_id)
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
                                getUrgencias(tienda_id);
                                new Utils().sendMailUrgencia(tienda_id,ctx);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            Log.e("create_urgencia_error",e.getMessage());
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
                params.put("equipo_id", equipo_id);
                params.put("observaciones", observaciones);
                params.put("fecha", fecha);
                params.put("hora", hora);
                params.put("personal_id", contratista_id);
                params.put("tipo", tipo_proveedor+"");
                return params;
            }
        };

        // Add the request to the RequestQueue.
        queue.add(stringRequest);
    }

    public void getLastMantenimiento(final String tienda_id)
    {
        String url=ctx.getApplicationContext().getString(R.string.base_url)+ctx.getApplicationContext().getString(R.string.get_last_mantenimiento_url);
        Log.i("getLastMantenimiento_url",url);
        RequestQueue queue = Volley.newRequestQueue(ctx);

        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        System.out.println("getLastMantenimiento_response: " + response);
                        try {
                            RespuestaResponse cliente = new Gson().fromJson(response, RespuestaResponse.class);
                            JSONParser parser = new JSONParser();
                            JSONArray respuesta = (JSONArray) parser.parse((String) cliente.getRespuesta());

                            if (cliente.getIde_error() == 0) {
//                                Toast.makeText(ctx, cliente.getDes_error(), Toast.LENGTH_LONG).show();
                                getContratistas();
                            } else {
                                for(Object o: respuesta){
                                    JSONObject ob = (JSONObject)o;
                                    int last_mantenimiento = Integer.parseInt((String)ob.get("last_mantenimiento"));
                                    System.out.println("last_mantenimiento:"+last_mantenimiento);
                                    if(last_mantenimiento<=7)
                                    {
                                        getContratistas();
                                    }else{
                                        getTecnicos();
                                    }
                                }
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            Log.e("getLastMantenimiento",e.getMessage());
                            getContratistas();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println("getLastMantenimiento_error: " + error.getMessage());
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

    //Dialog New Equipo
    Button btn_cancelar,btn_registrar,btn_siguiente,btn_atras;
    LinearLayout linear_evaporadora,linear_condensadora;

    Spinner spinner_marcas;
    ArrayAdapter<String> marcas_adapter;
    String[] marcas_id;

    Spinner spinner_cond_marcas;
    ArrayAdapter<String> marcas_cond_adapter;
    String[] marcas_cond_id;

    SearchableSpinner spinner_modelos;
    String[] modelos_ids;

    SearchableSpinner  spinner_cond_modelos;
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

    private void showModalRegisterEquipo()
    {
        viewDialog.showDialog();
        androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(ctx);
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

        spinner_modelos.setPositiveButton("Cerrar");

        et_nro_serie = dialogView.findViewById(R.id.nro_serie);
        icon_evap_scan = dialogView.findViewById(R.id.icon_evap_scan);
        et_cond_nro_serie = dialogView.findViewById(R.id.cond_nro_serie);
        icon_cond_scan = dialogView.findViewById(R.id.icon_cond_scan);

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

        btn_cancelar = dialogView.findViewById(R.id.btn_cancelar);
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
        spinner_modelos.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                modelo_id = modelos_ids[position];
                System.out.println("modelo_id: "+modelo_id);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

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

        final androidx.appcompat.app.AlertDialog alertDialog = builder.create();

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
                btn_cancelar.setVisibility(View.GONE);
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
        viewDialog.hideDialog(1.5);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                alertDialog.show();
            }
        }, 1500);
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
                                ArrayAdapter<String> modelos_adapter = new ArrayAdapter<>(ctx,R.layout.dropdown_style,data);
                                spinner_modelos.setAdapter(modelos_adapter);
                                modelos_adapter.notifyDataSetChanged();
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
                                ArrayAdapter<String> cond_modelos_adapter = new ArrayAdapter<>(ctx,R.layout.dropdown_style,data);
                                spinner_cond_modelos.setAdapter(cond_modelos_adapter);
                                modelos_cond_ids = data_id;
                                cond_modelos_adapter.notifyDataSetChanged();
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

    private void registrarEquipo()
    {
        nro_serie = et_nro_serie.getText().toString();
        cond_nro_serie = et_cond_nro_serie.getText().toString();
        System.out.println(marca_id+"-"+modelo_id+"-"+btu_id+"-"+tipo_id+"-"+voltaje_id+"-"+fase_id+"-"+refrigerante_id+"-"+nro_serie);
        System.out.println(cond_marca_id+"-"+cond_modelo_id+"-"+cond_btu_id+"-"+cond_tipo_id+"-"+cond_voltaje_id+"-"+cond_fase_id+"-"+cond_refrigerante_id+"-"+cond_nro_serie);

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
                                ((UrgenciasActivity)ctx).getEquipos();
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
}
