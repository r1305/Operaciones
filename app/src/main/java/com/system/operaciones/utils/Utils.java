package com.system.operaciones.utils;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.system.operaciones.response.RespuestaResponse;

import org.json.simple.parser.JSONParser;

import java.util.HashMap;
import java.util.Map;

public class Utils {
    public static void openPdf(Context ctx, String pdf)
    {
        Uri uri2 = Uri.parse(pdf);
        Intent pdfOpenintent = new Intent(Intent.ACTION_VIEW);
        pdfOpenintent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        pdfOpenintent.setData(uri2);
        ctx.startActivity(pdfOpenintent);
    }

    public void sendMail(final String tienda_id,final Context ctx)
    {
        final Credentials cred = new Credentials(ctx);
        String url = "http://161.132.108.154:8083/WebService/SendNewUrgencia";
        RequestQueue queue = Volley.newRequestQueue(ctx);

        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        System.out.println("sendMail_response: " + response);
                        try {
                            RespuestaResponse cliente = new Gson().fromJson(response, RespuestaResponse.class);
                            JSONParser parser = new JSONParser();
//                        JSONArray respuesta = (JSONArray)parser.parse((String)cliente.getRespuesta());
//                        System.out.println("sendMail_response: "+respuesta);
                            if (cliente.getIde_error() == 0) {
                                Toast.makeText(ctx, cliente.getDes_error(), Toast.LENGTH_LONG).show();
                            } else {
                            }
                        } catch (Exception e) {
                            Toast.makeText(ctx, e.getMessage(), Toast.LENGTH_LONG).show();
                            System.out.println("sendMail_error1: " + e.getMessage());
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println("sendMail_error2: " + error.getMessage());

            }
        }){
            @Override
            protected Map<String, String> getParams()
            {
                Map<String, String> params = new HashMap<>();
                params.put("tienda_id",tienda_id);
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

    public void sendFicha(final String tienda_id,final String urgencia_id,final Context ctx)
    {
        final Credentials cred = new Credentials(ctx);
        String url = "http://161.132.108.154:8083/WebService/SendFichaUrgencia";
        RequestQueue queue = Volley.newRequestQueue(ctx);

        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        System.out.println("sendMail_response: " + response);
                        try {
                            RespuestaResponse cliente = new Gson().fromJson(response, RespuestaResponse.class);
                            JSONParser parser = new JSONParser();
//                        JSONArray respuesta = (JSONArray)parser.parse((String)cliente.getRespuesta());
//                        System.out.println("sendMail_response: "+respuesta);
                            if (cliente.getIde_error() == 0) {
                                Toast.makeText(ctx, cliente.getDes_error(), Toast.LENGTH_LONG).show();
                            } else {
                            }
                        } catch (Exception e) {
                            Toast.makeText(ctx, e.getMessage(), Toast.LENGTH_LONG).show();
                            System.out.println("sendMail_error1: " + e.getMessage());
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println("sendMail_error2: " + error.getMessage());

            }
        }){
            @Override
            protected Map<String, String> getParams()
            {
                Map<String, String> params = new HashMap<>();
                params.put("tienda_id",tienda_id);
                params.put("urgencia_id",urgencia_id);
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
}
