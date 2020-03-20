package com.system.operaciones.adapters;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.system.operaciones.R;
import com.system.operaciones.activities.FichaMantenimientoActivity;
import com.system.operaciones.activities.FichaUrgenciaActivity;
import com.system.operaciones.activities.MantenimientosActivity;
import com.system.operaciones.response.RespuestaResponse;
import com.system.operaciones.utils.Credentials;
import com.system.operaciones.utils.Utils;
import com.toptoche.searchablespinnerlibrary.SearchableSpinner;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

import jrizani.jrspinner.JRSpinner;

public class MantenimientoAdapter extends RecyclerView.Adapter<MantenimientoAdapter.ViewHolder>{

    private Credentials cred;
    private Context ctx;
    private List<JSONObject> l = new ArrayList<>();
    private EditText dialog_hora,dialog_fecha,observaciones;
    private ImageView icon_calendar,icon_clock;
    private Button btn_cancelar,btn_update;
    private AlertDialog alertDialog;
    private String str_fecha,str_hora;
    private String spinner_id;
    private int tipo_proveedor=1;

    SearchableSpinner spinner_equipos;
    String[] equipos_ids;
    String equipo_id="0";

    SearchableSpinner spinner_motivos;
    ArrayAdapter<String> motivo_adapter;
    String[] motivos_ids;
    String motivo_id="0";

    private String[] personal_ids;
    private SearchableSpinner spinner_personal;
    private String personal_id="0";

    String mantenimiento_id = "0";

    RadioButton radioUezu;
    RadioButton radioContratistas;
    TextView label_personal;

    public MantenimientoAdapter(List<JSONObject> l) {
        this.l = l;
    }

    @NonNull
    @Override
    public MantenimientoAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ctx = parent.getContext();
        cred = new Credentials(ctx);
        return new MantenimientoAdapter.ViewHolder(LayoutInflater.from(ctx).inflate(R.layout.item_mantenimiento, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        JSONObject ob = l.get(position);
        final String status = (String)ob.get("status");
        Log.e("status","id: "+l.get(position).get("id")+"->status: "+status);

        holder.registro.setText((String)ob.get("registro"));
        holder.fecha_hora_atencion.setText((String)ob.get("atencion"));
        holder.contratista.setText((String)ob.get("proveedor"));
        holder.cierre.setText((String)ob.get("cierre"));
        holder.number.setText((String)ob.get("mantenimiento"));
        if(status.equals("0")){
            holder.icon_file.setImageDrawable(ctx.getResources().getDrawable(R.drawable.icon_write,null));
            holder.icon_pencil.setVisibility(View.VISIBLE);
            holder.icon_file.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ctx.startActivity(new Intent(ctx, FichaMantenimientoActivity.class)
                            .putExtra("id",(String)l.get(position).get("id"))
                            .putExtra("tienda_id",((MantenimientosActivity)ctx).getTienda_id()));
                }
            });
        }else{
            holder.icon_file.setImageDrawable(ctx.getResources().getDrawable(R.drawable.icon_pdf,null));
            holder.icon_pencil.setVisibility(View.GONE);
            holder.icon_file.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mantenimiento_id = (String)l.get(position).get("id");
                    String pdf_url = ctx.getResources().getString(R.string.pdf_url_mantenimiento)+mantenimiento_id+".pdf";
                    System.out.println("pdf_url: "+pdf_url);
                    Utils.openPdf(ctx,pdf_url);
                }
            });
        }

        if(!cred.getData("key_user_type").equals("1"))
        {
            holder.icon_pencil.setVisibility(View.GONE);
        }else{
            holder.icon_pencil.setVisibility(View.VISIBLE);
        }

        holder.icon_pencil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openModal(l.get(holder.getAdapterPosition()));
            }
        });
        if(status.equals("1")){
            holder.icon_status.setBackgroundColor(ctx.getResources().getColor(R.color.blanco,null));
            holder.icon_status.setImageResource(R.drawable.icon_check);
            holder.linear_full.setBackground(ctx.getResources().getDrawable(R.drawable.fondo_cardview,null));
        }else{
            holder.icon_status.setBackgroundColor(ctx.getResources().getColor(R.color.verdePastel,null));
            holder.icon_status.setImageResource(R.drawable.icon_file);
            holder.linear_full.setBackground(ctx.getResources().getDrawable(R.drawable.fondo_cardview_naranja,null));
        }

        if(cred.getData("key_user_type").equals("2"))
        {
            if(status.equals("0")){
                holder.icon_pencil.setVisibility(View.GONE);
                holder.icon_status.setVisibility(View.GONE);
                holder.icon_file.setVisibility(View.GONE);
            }
            holder.linear_contratista.setVisibility(View.GONE);
        }
    }

    private void openModal(JSONObject mantenimiento)
    {
        mantenimiento_id = (String)mantenimiento.get("id");
        final AlertDialog.Builder builder = new AlertDialog.Builder(ctx);
        Rect displayRectangle = new Rect();
        Window window = ((MantenimientosActivity)ctx).getWindow();
        window.getDecorView().getWindowVisibleDisplayFrame(displayRectangle);
        LayoutInflater inflater = ((MantenimientosActivity)ctx).getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_update_mantenimiento, null);
        dialogView.setMinimumWidth((int)(displayRectangle.width() * 0.7f));
        dialogView.setMinimumHeight((int)(displayRectangle.height() * 0.7f));
        builder.setView(dialogView);

        spinner_personal = dialogView.findViewById(R.id.dialog_spinner_contratistas);
        spinner_equipos = dialogView.findViewById(R.id.dialog_spinner_equipos);
        spinner_motivos = dialogView.findViewById(R.id.dialog_spinner_motivos);
        btn_cancelar = dialogView.findViewById(R.id.dialog_btn_cancelar);
        btn_update = dialogView.findViewById(R.id.dialog_btn_actualizar);
        dialog_fecha = dialogView.findViewById(R.id.dialog_fecha);
        dialog_hora = dialogView.findViewById(R.id.dialog_hora);
        icon_calendar = dialogView.findViewById(R.id.dialog_icon_calendar);
        icon_clock = dialogView.findViewById(R.id.dialog_icon_clock);
        observaciones = dialogView.findViewById(R.id.dialog_observaciones);
        radioUezu = dialogView.findViewById(R.id.radio_uezu);
        radioContratistas = dialogView.findViewById(R.id.radio_contratistas);
        label_personal = dialogView.findViewById(R.id.label_personal);

        spinner_equipos.setPositiveButton("Cerrar");
        spinner_personal.setPositiveButton("Cerrar");
        spinner_motivos.setPositiveButton("Cerrar");

        radioUezu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tipo_proveedor = 1;
                getTecnicos();
                label_personal.setText("Técnicos");
            }
        });

        radioContratistas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tipo_proveedor=2;
                getContratistas();
                label_personal.setText("Contratistas");
            }
        });

        getMantenimiento(mantenimiento_id);

        alertDialog = builder.create();
        alertDialog.show();
        btn_cancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });
        spinner_motivos.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                motivo_id = motivos_ids[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spinner_equipos.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                equipo_id = equipos_ids[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spinner_personal.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                personal_id = personal_ids[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        btn_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                str_fecha = dialog_fecha.getText().toString();
                str_hora = dialog_hora.getText().toString();
                updateMantenimiento(observaciones.getText().toString(),equipo_id, str_fecha,str_hora, personal_id, tipo_proveedor);
            }
        });

        Calendar cal = Calendar.getInstance(TimeZone.getDefault()); // Get current date

        // Create the DatePickerDialog instance
        final DatePickerDialog datePicker = new DatePickerDialog(ctx, new DatePickerDialog.OnDateSetListener() {

            // when dialog box is closed, below method will be called.
            public void onDateSet(DatePicker view, int selectedYear,
                                  int selectedMonth, int selectedDay) {
                String year1 = String.valueOf(selectedYear);
                String month1 = ((selectedMonth + 1)<10?"0":"")+(selectedMonth + 1);
                String day1 = ((selectedDay<10)?"0":"")+selectedDay;
                str_fecha = year1+"-"+ month1+"-"+ day1;
                dialog_fecha.setText(str_fecha);
                dialog_fecha.setError(null);
            }
        },
                cal.get(Calendar.YEAR),
                cal.get(Calendar.MONTH),
                cal.get(Calendar.DAY_OF_MONTH));
        datePicker.setCancelable(false);
        datePicker.setTitle("Seleccione la fecha");
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
                String hour = (hourOfDay<10?"0":"")+hourOfDay;
                String min = (minute<10?"0":"")+minute;
                str_hora = hour+":"+ min;
                dialog_hora.setText(str_hora);
                dialog_hora.setError(null);
            }
        },
                cal.get(Calendar.HOUR_OF_DAY),
                cal.get(Calendar.MINUTE),
                true);
        datePicker.setCancelable(false);
        datePicker.setTitle("Seleccione la hora");

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
        icon_clock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timePicker.show();
            }
        });
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
        TextView registro,fecha_hora_atencion,contratista,cierre,number;
        LinearLayout linear_full,linear_contratista;
        ImageView icon_status,icon_file,icon_pencil;
        private ViewHolder(View itemView) {
            super(itemView);
            linear_contratista = itemView.findViewById(R.id.linear_contratista);
            card = itemView.findViewById(R.id.item_card_urgencia);
            icon_pencil = itemView.findViewById(R.id.icon_pencil);
            registro = itemView.findViewById(R.id.item_mantenimiento_registro);
            cierre = itemView.findViewById(R.id.item_mantenimiento_cierre);
            fecha_hora_atencion = itemView.findViewById(R.id.item_mantenimiento_atencion);
            contratista = itemView.findViewById(R.id.item_mantenimiento_contratista);
            icon_file = itemView.findViewById(R.id.icon_file);
            icon_status = itemView.findViewById(R.id.icon_status);
            linear_full = itemView.findViewById(R.id.linear_card);
            number = itemView.findViewById(R.id.mantenimiento_number);
        }
    }

    private void getTecnicos()
    {
        tipo_proveedor=1;
        String url=ctx.getApplicationContext().getString(R.string.base_url)+ctx.getApplicationContext().getString(R.string.getTecnicos_url);
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
                                for(Object o: respuesta){
                                    JSONObject ob = (JSONObject)o;
                                    String motivo = (String)ob.get("tecnico");
                                    String id = (String)ob.get("id");
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

    private void getContratistas()
    {
        tipo_proveedor=2;
        String url=ctx.getApplicationContext().getString(R.string.base_url)+ctx.getApplicationContext().getString(R.string.getContratistas_url);
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
                                for(Object o: respuesta){
                                    JSONObject ob = (JSONObject)o;
                                    String motivo = (String)ob.get("proveedor");
                                    String id = (String)ob.get("id");
                                    data[i] = motivo;
                                    data_id[i] = id;
                                    i++;
                                }
                                ArrayAdapter<String> personal_adapter = new ArrayAdapter<>(ctx,R.layout.dropdown_style,data);
                                spinner_personal.setAdapter(personal_adapter);
                                personal_ids = data_id;
                                personal_adapter.notifyDataSetChanged();
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

    private void getEquipos(final String tienda_id)
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
                                Toast.makeText(ctx, cliente.getDes_error(), Toast.LENGTH_LONG).show();
                            } else {
                                String[] equipos = new String[respuesta.size()];
                                equipos_ids = new String[respuesta.size()];
                                int i=0;
                                for(Object o: respuesta){
                                    JSONObject ob = (JSONObject)o;
                                    if(!ob.get("id").equals("0")){
                                        equipos[i] = "Equipo "+ob.get("nro_equipo");
                                    }else{
                                        equipos[i] = (String)ob.get("evap_nro_serie");
                                    }
                                    equipos_ids[i] = (String)ob.get("id");
                                    i++;
                                }
                                if(spinner_equipos!=null)
                                {
                                    ArrayAdapter<String> equipos_adapter = new ArrayAdapter<>(ctx,R.layout.dropdown_style,equipos);
                                    spinner_equipos.setAdapter(equipos_adapter);
                                    equipos_adapter.notifyDataSetChanged();
                                }

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

        // Add the request to the RequestQueue.
        queue.add(stringRequest);
    }

    private void getMantenimiento(final String id)
    {
        String url=ctx.getApplicationContext().getString(R.string.base_url)+ctx.getApplicationContext().getString(R.string.getMantenimiento_url);
        Log.i("getMantenimiento_url",url);
        RequestQueue queue = Volley.newRequestQueue(ctx);

        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        System.out.println("getMantenimiento_response: " + response);
                        try {
                            RespuestaResponse cliente = new Gson().fromJson(response, RespuestaResponse.class);
                            JSONParser parser = new JSONParser();
                            JSONArray respuesta = (JSONArray) parser.parse((String) cliente.getRespuesta());

                            if (cliente.getIde_error() == 0) {
                                Toast.makeText(ctx, cliente.getDes_error(), Toast.LENGTH_LONG).show();
                            } else {
                                getEquipos(((MantenimientosActivity)ctx).getTienda_id());
                                for (Object ob : respuesta){
                                    final JSONObject o = (JSONObject)ob;
                                    dialog_fecha.setText((String)o.get("fecha_atencion"));
                                    dialog_hora.setText((String)o.get("hora_atencion"));
                                    observaciones.setText((String)o.get("observaciones"));
                                    if(o.get("tipo_proveedor").equals("1")){
                                        getTecnicos();
                                        radioUezu.setChecked(true);
                                        tipo_proveedor=1;
                                        label_personal.setText("Técnicos");
                                        new Handler().postDelayed(new Runnable() {
                                            @Override
                                            public void run() {

                                                spinner_personal.setSelection(findPersonalPosition((String)o.get("proveedor_id")));
                                            }
                                        }, 2500);

                                    }else{
                                        getContratistas();
                                        radioContratistas.setChecked(true);
                                        tipo_proveedor=2;
                                        label_personal.setText("Contratistas");
                                        new Handler().postDelayed(new Runnable() {
                                            @Override
                                            public void run() {

                                                spinner_personal.setSelection(findPersonalPosition((String)o.get("proveedor_id")));
                                            }
                                        }, 2500);
                                    }
                                    new Handler().postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            spinner_equipos.setSelection(findEquipoPosition((String)o.get("equipo_id")));
                                        }
                                    }, 2500);

                                }
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            System.out.println("getMantenimiento_error: "+e.getMessage());
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println("getMantenimiento_error: " + error.getMessage());
            }
        }){
            @Override
            protected Map<String, String> getParams()
            {
                Map<String, String> params = new HashMap<>();
                params.put("id", id);
                return params;
            }
        };

        // Add the request to the RequestQueue.
        queue.add(stringRequest);
    }

    private void updateMantenimiento(final String observaciones,final String equipo_id,final String fecha,final String hora,final String contratista_id,final int tipo_proveedor)
    {
        System.out.println("observaciones: "+observaciones);
        System.out.println("equipo_id: "+equipo_id);
        System.out.println("fecha: "+fecha);
        System.out.println("hora: "+hora);
        System.out.println("contratista_id: "+contratista_id);
        System.out.println("tipo_proveedor: "+tipo_proveedor);
        System.out.println("mantenimiento_id: "+mantenimiento_id);
        System.out.println("motivo_id: "+motivo_id);
        String url=ctx.getApplicationContext().getString(R.string.base_url)+ctx.getApplicationContext().getString(R.string.update_mantenimiento_url);
        RequestQueue queue = Volley.newRequestQueue(ctx);

        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        System.out.println("update_mantenimiento_response: " + response);
                        try {
                            RespuestaResponse cliente = new Gson().fromJson(response, RespuestaResponse.class);
                            JSONParser parser = new JSONParser();
                            JSONArray respuesta = (JSONArray) parser.parse((String) cliente.getRespuesta());

                            if (cliente.getIde_error() == 0) {
                                Toast.makeText(ctx, cliente.getDes_error(), Toast.LENGTH_LONG).show();
                            } else {
                                ((MantenimientosActivity)ctx).getMantenimientos();
                                alertDialog.dismiss();
                                new Utils().sendMailUpdateMantenimiento(mantenimiento_id,ctx);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            System.out.println("update_mantenimiento_error: " + e.getMessage());
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println("update_mantenimiento_error: " + error.getMessage());
            }
        }){
            @Override
            protected Map<String, String> getParams()
            {
                Map<String, String> params = new HashMap<>();
                params.put("mantenimiento_id", mantenimiento_id);
                params.put("equipo_id", equipo_id);
                params.put("observaciones", observaciones);
                params.put("fecha", fecha);
                params.put("hora", hora);
                params.put("personal_id", contratista_id);
                params.put("tipo", String.valueOf(tipo_proveedor));
                params.put("motivo_id", motivo_id);
                return params;
            }
        };

        // Add the request to the RequestQueue.
        queue.add(stringRequest);
    }

    int findPersonalPosition(String personal)
    {
        int position = 0;
        for(int i=0;i<personal_ids.length;i++){
            if(personal_ids[i].equals(personal)){
                position=i;
                personal_id = personal_ids[i];
            }
        }
        return position;
    }

    int findEquipoPosition(String equipo)
    {
        int position = 0;
        for(int i=0;i<equipos_ids.length;i++){
            if(equipos_ids[i].equals(equipo)){
                position=i;
                equipo_id = equipos_ids[i];
            }
        }
        return position;
    }

}
