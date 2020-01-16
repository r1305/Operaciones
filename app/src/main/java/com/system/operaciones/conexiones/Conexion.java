package com.system.operaciones.conexiones;

import android.content.Context;

import com.android.volley.RequestQueue;
import com.system.operaciones.utils.Credentials;

public class Conexion {

    // Instantiate the RequestQueue.
    private RequestQueue queue;
    private String url ="";
    private Context ctx;
    private Credentials cred;

    public Conexion newInstance(Context ctx){
        this.ctx = ctx;
        this.cred = new Credentials(ctx);
        return this;
    }

    /*
    public JSONObject createCliente(final String doc, final String razon, final String email, final String tlf, final String dir, final int act, final ProgressDialog progress)
    {
        JSONObject obj = new JSONObject();
        final String company_id = cred.getData("key_empresa");//cred.getEmpresa();
        url = ctx.getResources().getString(R.string.base_url)+ctx.getResources().getString(R.string.crear_cliente_url);
        queue = Volley.newRequestQueue(ctx);

        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e("register_cliente", response);
                        try {
                            RespuestaResponse cliente = new Gson().fromJson(response, RespuestaResponse.class);
                            JSONParser parser = new JSONParser();
                            JSONArray respuesta = (JSONArray) parser.parse((String) cliente.getRespuesta());

                           if (cliente.getIde_error() == 0) {
                                progress.dismiss();
                                Toast.makeText(ctx, cliente.getDes_error(), Toast.LENGTH_LONG).show();
                           } else {
                                if (respuesta.size() == 0) {

                                    new Handler().postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            if (act == 0) {
                                                Intent i = new Intent(ctx, MenuPrincipalActivity.class);
                                                progress.dismiss();
                                                Toast.makeText(ctx, "Registro exitoso!", Toast.LENGTH_LONG).show();
                                                ctx.startActivity(i);
                                            } else {
                                                Intent i = new Intent(ctx, BuscarActivity.class);
                                                i.putExtra("act",act);
                                                progress.dismiss();
                                                Toast.makeText(ctx, "Registro exitoso!", Toast.LENGTH_LONG).show();
                                                ctx.startActivity(i);
                                            }
                                        }
                                    }, 2000);
                                }
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            progress.dismiss();
                        }


                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progress.dismiss();
                System.out.println("insert_error: " + error.getMessage());
            }
        }){
            @Override
            protected Map<String, String> getParams()
            {
                Map<String, String> params = new HashMap<>();
                params.put("company_id", company_id);
                params.put("ruc", doc);
                params.put("razon", razon);
                params.put("email", email);
                params.put("tlf", tlf);
                params.put("dir", dir);

                return params;
            }
        };

        // Add the request to the RequestQueue.
        queue.add(stringRequest);
        return obj;
    }

    public void changePassword(final String new_psw)
    {
        url=ctx.getResources().getString(R.string.base_url)+ctx.getResources().getString(R.string.change_psw_url);
        final String username = cred.getData("user_id");
        queue = Volley.newRequestQueue(ctx);

        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        System.out.println("updatePassword_response: " + response);
                        try {
                            RespuestaResponse cliente = new Gson().fromJson(response, RespuestaResponse.class);
                            JSONParser parser = new JSONParser();
                            JSONArray respuesta = (JSONArray) parser.parse((String) cliente.getRespuesta());

                            if (cliente.getIde_error() == 0) {
                                Toast.makeText(ctx, cliente.getDes_error(), Toast.LENGTH_LONG).show();
                            } else {
                                if (respuesta.size() == 0) {
                                    Toast.makeText(ctx, "Contraseña actualizada!", Toast.LENGTH_SHORT).show();
                                    new Handler().postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            cred.logout();
                                        }
                                    }, 1500);
                                }
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println("login_error: " + error.getMessage());
            }
        }){
            @Override
            protected Map<String, String> getParams()
            {
                Map<String, String> params = new HashMap<>();
                params.put("user_id", username);
                params.put("psw", new_psw);

                return params;
            }
        };

        // Add the request to the RequestQueue.
        queue.add(stringRequest);
    }

    public void saveLocation(final String latitud,final String longitud)
    {
        try{
            String url=ctx.getApplicationContext().getString(R.string.base_url)+ctx.getApplicationContext().getString(R.string.saveLocation_url);
            Log.i("geolocation_url",url);
            queue = Volley.newRequestQueue(ctx);

            // Request a string response from the provided URL.
            StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            System.out.println("geolocation_response: " + response);
                            try {
                                RespuestaResponse cliente = new Gson().fromJson(response, RespuestaResponse.class);
                                if (cliente.getIde_error() == 0) {
                                    Toast.makeText(ctx, cliente.getDes_error(), Toast.LENGTH_LONG).show();
                                }

                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    System.out.println("geolocation_error: " + error.getMessage());
                }
            }){
                @Override
                protected Map<String, String> getParams()
                {
                    Map<String, String> params = new HashMap<>();
                    params.put("user_id", cred.getData("user_id"));
                    params.put("latitud", latitud);
                    params.put("longitud", longitud);
                    return params;
                }
            };
            // Add the request to the RequestQueue.
            queue.add(stringRequest);
        }catch(Exception e){
            Log.e("SaveLocation_error",e.getMessage());
        }


    }

    public void updateToken(final String token)
    {
        try{
            String url=ctx.getApplicationContext().getString(R.string.base_url)+ctx.getApplicationContext().getString(R.string.update_user_fcm);
            Log.i("updateToken_url",url);
            queue = Volley.newRequestQueue(ctx);

            // Request a string response from the provided URL.
            StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            System.out.println("updateToken_response: " + response);
                            try {
                                RespuestaResponse cliente = new Gson().fromJson(response, RespuestaResponse.class);
                                if (cliente.getIde_error() == 0) {
                                    Toast.makeText(ctx, cliente.getDes_error(), Toast.LENGTH_LONG).show();
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    System.out.println("updateToken_error: " + error.getMessage());
                }
            }){
                @Override
                protected Map<String, String> getParams()
                {
                    Map<String, String> params = new HashMap<>();
                    params.put("user_id", cred.getData("user_id"));
                    params.put("fcm",token);
                    return params;
                }
            };
            // Add the request to the RequestQueue.
            queue.add(stringRequest);
        }catch(Exception e){
            Log.e("updateToken_error", Objects.requireNonNull(e.getMessage()));
        }


    }
    */

}
