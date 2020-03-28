package com.system.operaciones.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.system.operaciones.R;
import com.system.operaciones.utils.Credentials;
import com.system.operaciones.response.RespuestaResponse;
import com.system.operaciones.utils.ViewDialog;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {
    Context ctx;
    Button btn_login;
    TextView recover_psw;
    EditText usuario,psw;
    ViewDialog viewDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ctx = this;
        viewDialog = new ViewDialog(this);
        usuario = findViewById(R.id.et_login_usuario);
        psw = findViewById(R.id.et_login_psw);
        btn_login = findViewById(R.id.btn_login);
        recover_psw = findViewById(R.id.btn_forgot_psw);
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!usuario.getText().toString().isEmpty() && !psw.getText().toString().isEmpty()){
                    viewDialog.showDialog();
                    login(usuario.getText().toString(),psw.getText().toString(),"","1");
                }else{
                    Toast.makeText(ctx,"Debe completar los datos",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        this.finish();
    }

    public void login(final String user, final String psw, final String mac, final String company_id)
    {
        Log.e("company_id",company_id);
        String url=ctx.getApplicationContext().getString(R.string.base_url)+ctx.getApplicationContext().getString(R.string.login_url);
        Log.i("login_url",url);
        RequestQueue queue = Volley.newRequestQueue(ctx);

        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        System.out.println("login_response: " + response);
                        try {
                            RespuestaResponse cliente = new Gson().fromJson(response, RespuestaResponse.class);
                            JSONParser parser = new JSONParser();
                            JSONArray respuesta = (JSONArray) parser.parse((String) cliente.getRespuesta());

                            if (cliente.getIde_error() == 0) {
                                Toast.makeText(ctx, cliente.getDes_error(), Toast.LENGTH_LONG).show();
                                viewDialog.hideDialog(0);
                            } else {
                                for (Object o : respuesta) {
                                    JSONObject ob = (JSONObject) o;
                                    String codAuth = (String) ob.get("token");
                                    String user_id = (String) ob.get("id");
                                    String is_new_password = (String) ob.get("is_new_password");
                                    String psw1 = (String) ob.get("psw");
                                    String name = (String) ob.get("name");
                                    String empresa_name = (String) ob.get("company_name");
                                    String email = (String) ob.get("email");
                                    String user_type = (String) ob.get("user_type");
                                    System.out.println("login_user_email: " + email);
                                    Credentials cred = new Credentials(ctx);
                                    cred.save_credentials(codAuth, user_id, company_id, is_new_password, psw1, name, empresa_name);
                                    cred.save_data("key_email", email);
                                    cred.save_data("key_points","0");
                                    cred.save_data("key_user_type",user_type);
                                    final Intent i = new Intent(ctx, ClientesActivity.class);
                                    viewDialog.hideDialog(3);
                                    new Handler().postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            ctx.startActivity(i);
                                            ((LoginActivity)ctx).finish();
                                        }
                                    },3000);

                                }
                            }
                        } catch (Exception e) {
                            viewDialog.hideDialog(3);
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                viewDialog.hideDialog(53);
                System.out.println("login_error: " + error.getMessage());
            }
        }){
            @Override
            protected Map<String, String> getParams()
            {
                Map<String, String> params = new HashMap<>();
                params.put("username", user);
                params.put("psw", psw);
                params.put("mac", mac);
                params.put("company_id", company_id);
                return params;
            }
        };

        // Add the request to the RequestQueue.
        queue.add(stringRequest);
    }
}
