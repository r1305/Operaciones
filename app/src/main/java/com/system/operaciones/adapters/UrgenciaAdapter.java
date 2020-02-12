package com.system.operaciones.adapters;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import com.github.gcacace.signaturepad.views.SignaturePad;
import com.google.gson.Gson;
import com.system.operaciones.FichaActivity;
import com.system.operaciones.R;
import com.system.operaciones.activities.UrgenciasActivity;
import com.system.operaciones.response.RespuestaResponse;
import com.system.operaciones.utils.Credentials;
import com.system.operaciones.utils.Image;
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

public class UrgenciaAdapter extends RecyclerView.Adapter<UrgenciaAdapter.ViewHolder> {

    private Credentials cred;
    private Context ctx;
    private List<JSONObject> l = new ArrayList<>();
    private JRSpinner contratista_spinner;
    private EditText dialog_hora,dialog_fecha;
    private Button btn_cancelar,btn_update;
    private String[] contratista_ids;
    private AlertDialog alertDialog;
    private String str_fecha,str_hora;
    private String spinner_id;
    private ImageView icon_calendar,icon_clock;
    private ImageView icon_camera_presion_baja,icon_camera_presion_alta,icon_camera_amp_l1,icon_camera_amp_l2,icon_camera_amp_l3,icon_camera_volt_l1,icon_camera_volt_l2,icon_camera_volt_l3;
    private EditText presion_baja,presion_alta,amp_l1,amp_l2,amp_l3,volt_l1,volt_l2,volt_l3;
    private SignaturePad signature;
    private Button btn_registrar,btn_cerrar;
    private int tipo_proveedor=1;
    public UrgenciaAdapter(List<JSONObject> l) {
        this.l = l;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ctx = parent.getContext();
        cred = new Credentials(ctx);
        return new UrgenciaAdapter.ViewHolder(LayoutInflater.from(ctx).inflate(R.layout.item_urgencia, parent, false));
    }

    public ImageView getIcon_camera_presion_baja() {
        return icon_camera_presion_baja;
    }

    public ImageView getIcon_camera_presion_alta() {
        return icon_camera_presion_alta;
    }

    public ImageView getIcon_camera_amp_l1() {
        return icon_camera_amp_l1;
    }

    public ImageView getIcon_camera_amp_l2() {
        return icon_camera_amp_l2;
    }

    public ImageView getIcon_camera_amp_l3() {
        return icon_camera_amp_l3;
    }

    public ImageView getIcon_camera_volt_l1() {
        return icon_camera_volt_l1;
    }

    public ImageView getIcon_camera_volt_l2() {
        return icon_camera_volt_l2;
    }

    public ImageView getIcon_camera_volt_l3() {
        return icon_camera_volt_l3;
    }

    public SignaturePad getSignature() {
        return signature;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        JSONObject ob = l.get(position);
        final String id = (String)ob.get("id");

        final String status = (String)ob.get("status");

        holder.registro.setText((String)ob.get("registro"));
        holder.fecha_hora_atencion.setText((String)ob.get("atencion"));
        holder.contratista.setText((String)ob.get("proveedor"));
        if(status.equals("0")){
            holder.icon_file.setImageDrawable(ctx.getResources().getDrawable(R.drawable.icon_subir,null));
            holder.icon_file.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ctx.startActivity(new Intent(ctx, FichaActivity.class)
                            .putExtra("urgencia",id)
                            .putExtra("tienda_id",((UrgenciasActivity)ctx).getTienda_id()));
                }
            });

        }else{
            holder.icon_file.setImageDrawable(ctx.getResources().getDrawable(R.drawable.icon_pdf,null));
            holder.icon_pencil.setVisibility(View.GONE);
            holder.icon_file.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    final AlertDialog.Builder builder = new AlertDialog.Builder(ctx);
//                    LayoutInflater inflater = ((UrgenciasActivity)ctx).getLayoutInflater();
//                    View dialogView = inflater.inflate(R.layout.dialog_ficha_atencion, null);
//                    builder.setView(dialogView);
//                    Rect displayRectangle = new Rect();
//                    Window window = ((UrgenciasActivity)ctx).getWindow();
//                    window.getDecorView().getWindowVisibleDisplayFrame(displayRectangle);
//                    dialogView.setMinimumWidth((int)(displayRectangle.width() * 0.7f));
//                    dialogView.setMinimumHeight((int)(displayRectangle.height() * 0.6f));
//                    alertDialog = builder.create();
//                    alertDialog.show();
                    String pdf_url = ctx.getResources().getString(R.string.pdf_url)+id+".pdf";
                    System.out.println("pdf_url: "+pdf_url);
                    Utils.openPdf(ctx,pdf_url);
                }
            });
        }

        holder.icon_pencil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AlertDialog.Builder builder = new AlertDialog.Builder(ctx);
                LayoutInflater inflater = ((UrgenciasActivity)ctx).getLayoutInflater();
                View dialogView = inflater.inflate(R.layout.dialog_update_urgencia, null);
                builder.setView(dialogView);
                contratista_spinner = dialogView.findViewById(R.id.spinner_contratista);
                btn_cancelar = dialogView.findViewById(R.id.dialog_btn_cancelar);
                btn_update = dialogView.findViewById(R.id.dialog_btn_agendar);
                dialog_fecha = dialogView.findViewById(R.id.dialog_urgencia_fecha);
                dialog_hora = dialogView.findViewById(R.id.dialog_urgencia_hora);
                icon_calendar = dialogView.findViewById(R.id.dialog_icon_calendar);
                icon_clock = dialogView.findViewById(R.id.dialog_icon_clock);
                dialog_hora = dialogView.findViewById(R.id.dialog_urgencia_hora);
                getLastMantenimiento(((UrgenciasActivity)ctx).getTienda_id());

                alertDialog = builder.create();
                alertDialog.show();
                btn_cancelar.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        alertDialog.dismiss();
                    }
                });
                contratista_spinner.setOnItemClickListener(new JRSpinner.OnItemClickListener() {
                    @Override
                    public void onItemClick(int position) {
                        spinner_id = contratista_ids[position];
                    }
                });
                btn_update.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Log.e("contratista_id",spinner_id+"");
                        updateUrgencia(str_fecha,str_hora,spinner_id,id);
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
            holder.icon_status.setBackgroundColor(ctx.getResources().getColor(R.color.plomoBackground,null));
            holder.icon_status.setImageResource(R.drawable.icon_check);
            holder.linear_full.setBackgroundColor(ctx.getResources().getColor(R.color.plomoBackground,null));
        }else{
            holder.icon_status.setBackgroundColor(ctx.getResources().getColor(R.color.verdePastel,null));
            holder.icon_status.setImageResource(R.drawable.icon_file);
            holder.linear_full.setBackgroundColor(ctx.getResources().getColor(R.color.blanco,null));
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
                                contratista_ids = data_id;
                                contratista_spinner.setItems(data);
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
                                contratista_ids = data_id;
                                contratista_spinner.setItems(data);
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

    private void updateUrgencia(final String fecha,final String hora,final String contratista_id,final String urgencia_id)
    {
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
                                ((UrgenciasActivity)ctx).getUrgencias(((UrgenciasActivity) ctx).getTienda_id());
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
                params.put("urgencia_id", urgencia_id);
                params.put("contratista_id", contratista_id);
                params.put("fecha",fecha);
                params.put("hora",hora);
                params.put("tipo_proveedor",tipo_proveedor+"");
                return params;
            }
        };

        // Add the request to the RequestQueue.
        queue.add(stringRequest);
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
                                ((UrgenciasActivity)ctx).getUrgencias(((UrgenciasActivity) ctx).getTienda_id());
                                alertDialog.dismiss();
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
                params.put("tienda_id", ((UrgenciasActivity)ctx).getTienda_id()+"");
                params.put("presion_baja", presion_baja.getText().toString());
                params.put("img_presion_baja", ((UrgenciasActivity)ctx).getImage_presion_baja());
                params.put("presion_alta", presion_alta.getText().toString()+"");
                params.put("img_presion_alta", ((UrgenciasActivity)ctx).getImage_presion_alta());
                params.put("amperaje_l1", amp_l1.getText().toString()+"");
                params.put("img_amperaje_l1", ((UrgenciasActivity)ctx).getImage_amp_l1());
                params.put("amperaje_l2", amp_l2.getText().toString()+"");
                params.put("img_amperaje_l2", ((UrgenciasActivity)ctx).getImage_amp_l2());
                params.put("amperaje_l3", amp_l3.getText().toString()+"");
                params.put("img_amperaje_l3", ((UrgenciasActivity)ctx).getImage_amp_l3());
                params.put("voltaje_l1", volt_l1.getText().toString()+"");
                params.put("img_voltaje_l1", ((UrgenciasActivity)ctx).getImage_volt_l1());
                params.put("voltaje_l2", volt_l2.getText().toString()+"");
                params.put("img_voltaje_l2", ((UrgenciasActivity)ctx).getImage_volt_l2());
                params.put("voltaje_l3", volt_l3.getText().toString()+"");
                params.put("img_voltaje_l3", ((UrgenciasActivity)ctx).getImage_volt_l3());
                params.put("img_signature",((UrgenciasActivity)ctx).getImage_signature());
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
                                Toast.makeText(ctx, cliente.getDes_error(), Toast.LENGTH_LONG).show();
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
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println("getLastMantenimiento_error: " + error.getMessage());
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
}
