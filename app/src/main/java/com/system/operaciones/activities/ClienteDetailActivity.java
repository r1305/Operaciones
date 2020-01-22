package com.system.operaciones.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import com.system.operaciones.R;
import com.system.operaciones.utils.Credentials;

import java.util.Objects;

public class ClienteDetailActivity extends AppCompatActivity implements View.OnClickListener {
    Context ctx;
    Credentials cred;
    LinearLayout datos,equipos,servicios;
    LinearLayout tab_datos,tab_equipos,tab_servicios;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cliente_detail);
        ctx = this;
        cred = new Credentials(ctx);

        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("");
        datos = findViewById(R.id.linear_datos);
        tab_datos = findViewById(R.id.tab_datos);
        equipos = findViewById(R.id.linear_equipos);
        tab_equipos = findViewById(R.id.tab_equipos);
        servicios = findViewById(R.id.linear_servicios);
        tab_servicios = findViewById(R.id.tab_servicios);

        datos.setVisibility(View.VISIBLE);
        tab_datos.setBackgroundColor(getResources().getColor(R.color.celesteClaro));

        tab_datos.setOnClickListener(this);
        tab_equipos.setOnClickListener(this);
        tab_servicios.setOnClickListener(this);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();

        switch (id){
            case R.id.tab_datos:
                hideTabs();
                datos.setVisibility(View.VISIBLE);
                tab_datos.setBackgroundColor(getResources().getColor(R.color.celesteClaro));
            break;
            case R.id.tab_equipos:
                hideTabs();
                equipos.setVisibility(View.VISIBLE);
                tab_equipos.setBackgroundColor(getResources().getColor(R.color.celesteClaro));
            break;
            case R.id.tab_servicios:
                hideTabs();
                servicios.setVisibility(View.VISIBLE);
                tab_servicios.setBackgroundColor(getResources().getColor(R.color.celesteClaro));
            break;
        }
    }

    public void hideTabs()
    {
        datos.setVisibility(View.INVISIBLE);
        equipos.setVisibility(View.INVISIBLE);
        servicios.setVisibility(View.INVISIBLE);
        tab_datos.setBackgroundColor(getResources().getColor(R.color.white));
        tab_equipos.setBackgroundColor(getResources().getColor(R.color.white));
        tab_servicios.setBackgroundColor(getResources().getColor(R.color.white));
    }
}
