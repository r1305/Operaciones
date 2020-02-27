package com.system.operaciones.adapters;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import com.system.operaciones.activities.FichaActivity;
import com.system.operaciones.activities.InstalacionDetailActivity;
import com.system.operaciones.activities.MantenimientoDetailActivity;
import com.system.operaciones.activities.MantenimientosActivity;
import com.system.operaciones.activities.MantenimientosActivity;
import com.system.operaciones.response.RespuestaResponse;
import com.system.operaciones.utils.Credentials;
import com.system.operaciones.utils.Utils;

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
    private Spinner motivos_spin;

    JRSpinner spinner_equipos;
    String[] equipos_ids;
    String equipo_id;

    ArrayAdapter<String> motivo_adapter;
    String[] motivo_id_adapter;

    private String[] personal_ids;
    private JRSpinner personal_spinner;
    private String personal_id;

    String urgencia_id = "0";

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
        urgencia_id = (String)ob.get("id");
        final String status = (String)ob.get("status");
        Log.e("status","id: "+l.get(position).get("id")+"->status: "+status);

        holder.registro.setText((String)ob.get("registro"));
        holder.fecha_hora_atencion.setText((String)ob.get("atencion"));
        holder.contratista.setText((String)ob.get("proveedor"));
        if(status.equals("0")){

            holder.icon_file.setImageDrawable(ctx.getResources().getDrawable(R.drawable.icon_write,null));
            holder.icon_pencil.setVisibility(View.VISIBLE);
            holder.icon_file.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ctx.startActivity(new Intent(ctx, FichaActivity.class)
                            .putExtra("urgencia",(String)l.get(position).get("id"))
                            .putExtra("tienda_id",((MantenimientosActivity)ctx).getTienda_id()));
                }
            });
        }else{
            holder.icon_file.setImageDrawable(ctx.getResources().getDrawable(R.drawable.icon_pdf,null));
            holder.icon_pencil.setVisibility(View.GONE);
            holder.icon_file.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    urgencia_id = (String)l.get(position).get("id");
                    String pdf_url = ctx.getResources().getString(R.string.pdf_url)+urgencia_id+".pdf";
                    System.out.println("pdf_url: "+pdf_url);
                    Utils.openPdf(ctx,pdf_url);
                }
            });
        }

        holder.icon_pencil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                JSONObject json = l.get(holder.getAdapterPosition());
                urgencia_id = (String)json.get("id");
                System.out.println("holder_id: "+position);
                final AlertDialog.Builder builder = new AlertDialog.Builder(ctx);
                Rect displayRectangle = new Rect();
                Window window = ((MantenimientosActivity)ctx).getWindow();
                window.getDecorView().getWindowVisibleDisplayFrame(displayRectangle);
                LayoutInflater inflater = ((MantenimientosActivity)ctx).getLayoutInflater();
                View dialogView = inflater.inflate(R.layout.dialog_update_mantenimiento, null);
                dialogView.setMinimumWidth((int)(displayRectangle.width() * 0.7f));
                dialogView.setMinimumHeight((int)(displayRectangle.height() * 0.7f));
                builder.setView(dialogView);
                personal_spinner = dialogView.findViewById(R.id.spinner_edit_contratista);
                motivos_spin = dialogView.findViewById(R.id.urgencia_edit_spinner_motivos);
                spinner_equipos = dialogView.findViewById(R.id.urgencia_edit_spinner_equipos);
                btn_cancelar = dialogView.findViewById(R.id.dialog_edit_btn_cancelar);
                btn_update = dialogView.findViewById(R.id.dialog_btn_actualizar);
                dialog_fecha = dialogView.findViewById(R.id.dialog_edit_urgencia_fecha);
                dialog_hora = dialogView.findViewById(R.id.dialog_edit_urgencia_hora);
                icon_calendar = dialogView.findViewById(R.id.dialog_edit_icon_calendar);
                icon_clock = dialogView.findViewById(R.id.dialog_edit_icon_clock);
                dialog_hora = dialogView.findViewById(R.id.dialog_edit_urgencia_hora);
                observaciones = dialogView.findViewById(R.id.dialog_edit_observaciones);

                alertDialog = builder.create();
                getEquipos(((MantenimientosActivity)ctx).getTienda_id());
                getMotivos();
                getLastMantenimiento(((MantenimientosActivity)ctx).getTienda_id());
                alertDialog.show();
                btn_cancelar.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        alertDialog.dismiss();
                    }
                });
                spinner_equipos.setOnItemClickListener(new JRSpinner.OnItemClickListener() {
                    @Override
                    public void onItemClick(int position) {
                        equipo_id = equipos_ids[position];
                    }
                });
                personal_spinner.setOnItemClickListener(new JRSpinner.OnItemClickListener() {
                    @Override
                    public void onItemClick(int position) {
                        personal_id = personal_ids[position];
                    }
                });
                btn_update.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Log.e("contratista_id",spinner_id+"");
                        motivos_spin.getSelectedItem();
                        long motivo_id = motivo_adapter.getItemId(motivos_spin.getSelectedItemPosition());
                        String item_motivo = motivo_id_adapter[Integer.parseInt(motivo_id+"")];
                        str_fecha = dialog_fecha.getText().toString();
                        str_hora = dialog_hora.getText().toString();

                        updateUrgencia(observaciones.getText().toString(),equipo_id, str_fecha,str_hora, personal_id, tipo_proveedor,urgencia_id);
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

                icon_clock.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        timePicker.show();
                    }
                });
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
        TextView registro,fecha_hora_atencion,contratista;
        LinearLayout linear_full;
        ImageView icon_status,icon_file,icon_pencil;
        private ViewHolder(View itemView) {
            super(itemView);
            card = itemView.findViewById(R.id.item_card_urgencia);
            icon_pencil = itemView.findViewById(R.id.icon_pencil);
            registro = itemView.findViewById(R.id.item_urgencia_registro);
            fecha_hora_atencion = itemView.findViewById(R.id.item_urgencia_atencion);
            contratista = itemView.findViewById(R.id.item_urgencia_contratista);
            icon_file = itemView.findViewById(R.id.icon_file);
            icon_status = itemView.findViewById(R.id.icon_status);
            linear_full = itemView.findViewById(R.id.linear_card);
        }
    }

    private void getContratistas()
    {
        tipo_proveedor=1;
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
                                System.out.println("contratistas_data_ids: "+data_id.length);
                                personal_ids = data_id;
                                personal_spinner.setItems(data);
                                getUrgencia(urgencia_id);
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
        tipo_proveedor=2;
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
                                personal_ids = data_id;
                                personal_spinner.setItems(data);
                                personal_spinner.setHint("Tecnico");
                                getUrgencia(urgencia_id);

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

    private void getLastMantenimiento(final String tienda_id)
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
                            getContratistas();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println("getLastMantenimiento_error: " + error.getMessage());
                getContratistas();
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
                                        equipos[i] = ob.get("modelo")+" - "+ob.get("evap_nro_serie");
                                    }else{
                                        equipos[i] = (String)ob.get("evap_nro_serie");
                                    }
                                    equipos_ids[i] = (String)ob.get("id");
                                    i++;
                                }
                                spinner_equipos.setItems(equipos);

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

    private void getMotivos()
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
            }
        });

        // Add the request to the RequestQueue.
        queue.add(stringRequest);
    }

    private void getUrgencia(final String id)
    {
        String url=ctx.getApplicationContext().getString(R.string.base_url)+ctx.getApplicationContext().getString(R.string.getUrgencia_url);
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
                                for (Object ob : respuesta){
                                    JSONObject o = (JSONObject)ob;
                                    dialog_fecha.setText((String)o.get("fecha_atencion"));
                                    dialog_hora.setText((String)o.get("hora_atencion"));
                                    observaciones.setText((String)o.get("observaciones"));
                                }
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            Log.e("getUrgencia_error",e.getMessage());
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println("getMotivos_error: " + error.getMessage());
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

    private void updateUrgencia(final String observaciones,final String equipo_id,final String fecha,final String hora,final String contratista_id,final int tipo_proveedor,final String id)
    {
        System.out.println("observaciones: "+observaciones);
        System.out.println("equipo_id: "+equipo_id);
        System.out.println("fecha: "+fecha);
        System.out.println("hora: "+hora);
        System.out.println("contratista_id: "+contratista_id);
        System.out.println("tipo_proveedor: "+tipo_proveedor);
        System.out.println("id: "+id);
        String url=ctx.getApplicationContext().getString(R.string.base_url)+ctx.getApplicationContext().getString(R.string.update_urgencia_url);
        RequestQueue queue = Volley.newRequestQueue(ctx);

        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        System.out.println("update_urgencia_response: " + response);
                        try {
                            RespuestaResponse cliente = new Gson().fromJson(response, RespuestaResponse.class);
                            JSONParser parser = new JSONParser();
                            JSONArray respuesta = (JSONArray) parser.parse((String) cliente.getRespuesta());

                            if (cliente.getIde_error() == 0) {
                                Toast.makeText(ctx, cliente.getDes_error(), Toast.LENGTH_LONG).show();
                            } else {
                                ((MantenimientosActivity)ctx).getUrgencias(((MantenimientosActivity) ctx).getTienda_id());
                                alertDialog.dismiss();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println("update_urgencia_error: " + error.getMessage());
            }
        }){
            @Override
            protected Map<String, String> getParams()
            {
                Map<String, String> params = new HashMap<>();
                params.put("urgencia_id", id);
                params.put("equipo_id", equipo_id);
                params.put("observaciones", observaciones);
                params.put("fecha", fecha);
                params.put("hora", hora);
                params.put("personal_id", contratista_id);
                params.put("tipo", String.valueOf(tipo_proveedor));
                return params;
            }
        };

        // Add the request to the RequestQueue.
        queue.add(stringRequest);
    }
}
