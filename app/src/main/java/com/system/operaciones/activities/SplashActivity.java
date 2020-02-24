package com.system.operaciones.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.system.operaciones.BuildConfig;
import com.system.operaciones.R;
import com.system.operaciones.utils.Credentials;
import com.system.operaciones.response.RespuestaResponse;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.util.HashMap;
import java.util.Map;

import static android.Manifest.permission.ACCESS_BACKGROUND_LOCATION;
import static android.Manifest.permission.ACCESS_COARSE_LOCATION;
import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static android.Manifest.permission.CALL_PHONE;
import static android.Manifest.permission.CAMERA;
import static android.Manifest.permission.FOREGROUND_SERVICE;
import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.READ_PHONE_STATE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

public class SplashActivity extends AppCompatActivity {
    private static int splashTime = 2000;
    Context ctx;
    Credentials cred;
    TextView tv_version;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splah);
        ctx = this;
        cred = new Credentials(ctx);
        cred.save_data("key_tmp","");
        cred.save_data("key_add_equipo","");
        tv_version = findViewById(R.id.app_version);
        String version = "V. " + BuildConfig.VERSION_NAME;
        tv_version.setText(version);
        requestPermission();
    }

    private void requestPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            requestPermissions(new String[]{WRITE_EXTERNAL_STORAGE, CALL_PHONE , READ_PHONE_STATE, ACCESS_COARSE_LOCATION, ACCESS_FINE_LOCATION,FOREGROUND_SERVICE,READ_EXTERNAL_STORAGE,ACCESS_BACKGROUND_LOCATION,CAMERA}, 100);
        }
    }

    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        Log.e("requestCode",requestCode+"");
        if (requestCode == 100) {
            if (ActivityCompat.checkSelfPermission(this, READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED &&
                    ActivityCompat.checkSelfPermission(this, WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED &&
                    ActivityCompat.checkSelfPermission(this, CAMERA) != PackageManager.PERMISSION_GRANTED &&
                    ActivityCompat.checkSelfPermission(this, ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                    ActivityCompat.checkSelfPermission(this, READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED &&
                    ActivityCompat.checkSelfPermission(this, ACCESS_BACKGROUND_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                    ActivityCompat.checkSelfPermission(this, CALL_PHONE) != PackageManager.PERMISSION_GRANTED &&
                    ActivityCompat.checkSelfPermission(this, ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                requestPermission();
            } else {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
//                        SplashActivity.this.validateSession();
                        final String token = cred.getData("token");
                        if (token.isEmpty()) {
                            Intent goToLogin = new Intent(ctx, LoginActivity.class);
                            SplashActivity.this.startActivity(goToLogin);
                            SplashActivity.this.finish();
                        } else {
                            Intent goToMain = new Intent(ctx, ClientesActivity.class);
                            SplashActivity.this.startActivity(goToMain);
                            SplashActivity.this.finish();
                        }
                    }
                }, splashTime);

            }
        }
    }

    public void validateSession()
    {
        String url=ctx.getApplicationContext().getString(R.string.base_url)+ctx.getApplicationContext().getString(R.string.session_url);
        String params = "?token="+cred.getData("token")+"&usuario_id="+cred.getData("user_id");
        Log.i("session_url",url);
        RequestQueue queue = Volley.newRequestQueue(ctx);
        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        System.out.println("session_response: " + response);
                        try {
                            RespuestaResponse cliente = new Gson().fromJson(response, RespuestaResponse.class);
                            if (cliente.getIde_error() == -1) {
                                new Handler().postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        cred.logout();
                                        SplashActivity.this.finish();
                                    }
                                }, 2000);
                            } else if (cliente.getIde_error() == 0) {
                                Toast.makeText(ctx, cliente.getDes_error(), Toast.LENGTH_LONG).show();
                                SplashActivity.this.finish();
                            } else {
                                final String token = cred.getData("token");
                                if (token.isEmpty()) {
                                    Intent goToLogin = new Intent(ctx, LoginActivity.class);
                                    SplashActivity.this.startActivity(goToLogin);
                                    SplashActivity.this.finish();
                                } else {
                                    Intent goToMain = new Intent(ctx, ClientesActivity.class);
                                    SplashActivity.this.startActivity(goToMain);
                                    SplashActivity.this.finish();
                                }
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            System.out.println("catch: "+e.getMessage());
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println("volley_session_error: " + error.getMessage());
            }
        }){
            @Override
            protected Map<String, String> getParams()
            {
                Map<String, String> params = new HashMap<String, String>();
                params.put("token", cred.getData("token"));
                params.put("usuario_id", cred.getData("user_id"));
                return params;
            }
        };

        // Add the request to the RequestQueue.
        queue.add(stringRequest);
    }
}
