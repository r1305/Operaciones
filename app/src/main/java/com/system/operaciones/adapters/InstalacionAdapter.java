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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
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
import com.system.operaciones.activities.FichaInstalacionActivity;
import com.system.operaciones.activities.FichaInstalacionActivity;
import com.system.operaciones.activities.InstalacionesActivity;
import com.system.operaciones.response.RespuestaResponse;
import com.system.operaciones.utils.Credentials;
import com.system.operaciones.utils.Utils;
import com.toptoche.searchablespinnerlibrary.SearchableSpinner;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.TimeZone;

public class InstalacionAdapter extends RecyclerView.Adapter<InstalacionAdapter.ViewHolder>{

    Credentials cred;
    Context ctx;
    List<JSONObject> l = new ArrayList<>();
    private int tipo_proveedor=1;

    private AlertDialog alertDialog;
    private Button btn_cancelar,btn_update;
    private String str_fecha,str_hora;

    private EditText dialog_hora,dialog_fecha,observaciones;
    private ImageView icon_calendar,icon_clock;

    private String[] personal_ids;
    private SearchableSpinner spinner_personal;
    private String personal_id;

    String urgencia_id = "0";
    String tienda_id = "0";
    TextView label_personal;

    RadioButton radioUezu,radioContratistas;

    public InstalacionAdapter(List<JSONObject> l) {
        this.l = l;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ctx = parent.getContext();
        cred = new Credentials(ctx);
        return new InstalacionAdapter.ViewHolder(LayoutInflater.from(ctx).inflate(R.layout.item_instalacion, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        JSONObject ob = l.get(position);
        System.out.println("json_adapter: "+ob);
        urgencia_id = (String)ob.get("id");
        tienda_id = (String)ob.get("tienda_id");
        final String status = (String)ob.get("status");
        System.out.println("holder_proveedor: "+ (String)ob.get("proveedor"));

        if(status.equals("0")){
            holder.icon_file.setImageDrawable(ctx.getResources().getDrawable(R.drawable.icon_write,null));
            holder.icon_pencil.setVisibility(View.VISIBLE);
            holder.icon_file.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ctx.startActivity(new Intent((ctx), FichaInstalacionActivity.class)
                            .putExtra("id",(String)l.get(position).get("id"))
                            .putExtra("tienda_id",((InstalacionesActivity)ctx).getTienda_id()));
                }
            });
        }else{
            holder.icon_file.setImageDrawable(ctx.getResources().getDrawable(R.drawable.icon_pdf,null));
            holder.icon_pencil.setVisibility(View.GONE);
            holder.icon_file.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    urgencia_id = (String)l.get(position).get("id");
                    String pdf_url = ctx.getResources().getString(R.string.pdf_url_instalacion)+urgencia_id+".pdf";
                    System.out.println("pdf_url: "+pdf_url);
                    Utils.openPdf(ctx,pdf_url);
                }
            });
        }

        /*holder.number.setText((String)ob.get("urgencia"));
        holder.registro.setText((String)ob.get("registro"));
        holder.fecha_hora_atencion.setText((String)ob.get("atencion"));
        holder.contratista.setText((String)ob.get("proveedor"));
        holder.cierre.setText((String)ob.get("cierre"));
        holder.nro_equipo.setText("Equipo N° "+(String)ob.get("nro_equipo"));
        if(status.equals("0")){

            holder.icon_file.setImageDrawable(ctx.getResources().getDrawable(R.drawable.icon_write,null));
            holder.icon_pencil.setVisibility(View.VISIBLE);
            holder.icon_file.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ctx.startActivity(new Intent(ctx, FichaInstalacionActivity.class)
                            .putExtra("id",(String)l.get(position).get("id"))
                            .putExtra("tienda_id",((InstalacionesActivity)ctx).getTienda_id()));
                }
            });
        }else{
            holder.icon_file.setImageDrawable(ctx.getResources().getDrawable(R.drawable.icon_pdf,null));
            holder.icon_pencil.setVisibility(View.GONE);
            holder.icon_file.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    urgencia_id = (String)l.get(position).get("id");
                    String pdf_url = ctx.getResources().getString(R.string.pdf_url_urgencia)+urgencia_id+".pdf";
                    System.out.println("pdf_url: "+pdf_url);
                    Utils.openPdf(ctx,pdf_url);
                }
            });
        }

        if(cred.getData("key_user_type").equals("2"))
        {
            if(status.equals("0")){
                holder.icon_pencil.setVisibility(View.GONE);
                holder.icon_status.setVisibility(View.GONE);
                holder.icon_file.setVisibility(View.GONE);
            }
            holder.linear_contratista.setVisibility(View.GONE);
        }else if(cred.getData("key_user_type").equals("3"))
        {
            holder.icon_pencil.setVisibility(View.GONE);
        }

        holder.icon_pencil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                JSONObject json = l.get(holder.getAdapterPosition());
                urgencia_id = (String)json.get("id");
                System.out.println("holder_id: "+position);
                final AlertDialog.Builder builder = new AlertDialog.Builder(ctx);
                Rect displayRectangle = new Rect();
                Window window = ((InstalacionesActivity)ctx).getWindow();
                window.getDecorView().getWindowVisibleDisplayFrame(displayRectangle);
                LayoutInflater inflater = ((InstalacionesActivity)ctx).getLayoutInflater();
                View dialogView = inflater.inflate(R.layout.dialog_update_urgencia, null);
                dialogView.setMinimumWidth((int)(displayRectangle.width() * 0.7f));
                dialogView.setMinimumHeight((int)(displayRectangle.height() * 0.7f));
                builder.setView(dialogView);
                radioUezu = dialogView.findViewById(R.id.radio_uezu);
                radioContratistas = dialogView.findViewById(R.id.radio_contratistas);
                label_personal = dialogView.findViewById(R.id.label_personal);
//                getUrgencia(urgencia_id);

                radioUezu.setChecked(true);
                tipo_proveedor = 1;
                radioUezu.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        tipo_proveedor=1;
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

                spinner_personal = dialogView.findViewById(R.id.spinner_contratista);
                btn_cancelar = dialogView.findViewById(R.id.dialog_edit_btn_cancelar);
                btn_update = dialogView.findViewById(R.id.dialog_btn_actualizar);
                dialog_fecha = dialogView.findViewById(R.id.dialog_edit_urgencia_fecha);
                dialog_hora = dialogView.findViewById(R.id.dialog_edit_urgencia_hora);
                icon_calendar = dialogView.findViewById(R.id.dialog_edit_icon_calendar);
                icon_clock = dialogView.findViewById(R.id.dialog_edit_icon_clock);
                dialog_hora = dialogView.findViewById(R.id.dialog_edit_urgencia_hora);
                observaciones = dialogView.findViewById(R.id.dialog_edit_observaciones);

                alertDialog = builder.create();
                alertDialog.show();
                btn_cancelar.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        alertDialog.dismiss();
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

//                        updateUrgencia(observaciones.getText().toString(),equipo_id, str_fecha,str_hora, personal_id, tipo_proveedor,motivo_id,urgencia_id);
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
                dialog_fecha.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                    @Override
                    public void onFocusChange(View v, boolean hasFocus) {
                        if(hasFocus)
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
                dialog_hora.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                    @Override
                    public void onFocusChange(View v, boolean hasFocus) {
                        if(hasFocus)
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
        }*/
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
        TextView registro,fecha_hora_atencion,contratista,cierre,number,nro_equipo;
        ImageView icon_status,icon_file,icon_pencil;
        LinearLayout linear_full,linear_contratista;
        private ViewHolder(View itemView) {
            super(itemView);
            card = itemView.findViewById(R.id.item_card_instalacion);
            registro = itemView.findViewById(R.id.item_urgencia_registro);
            fecha_hora_atencion = itemView.findViewById(R.id.item_urgencia_atencion);
            contratista = itemView.findViewById(R.id.item_urgencia_contratista);
            cierre = itemView.findViewById(R.id.item_urgencia_cierre);
            icon_file = itemView.findViewById(R.id.icon_file);
            icon_status = itemView.findViewById(R.id.icon_status);
            icon_pencil = itemView.findViewById(R.id.icon_pencil);
        }
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
                                System.out.println("contratistas_data_ids: "+data_id.length);
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
}
