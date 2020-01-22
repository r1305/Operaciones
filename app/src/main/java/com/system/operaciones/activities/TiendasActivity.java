package com.system.operaciones.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.system.operaciones.R;
import com.system.operaciones.adapters.ClienteAdapter;
import com.system.operaciones.adapters.TiendaAdapter;
import com.system.operaciones.utils.Credentials;

import org.json.simple.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class TiendasActivity extends AppCompatActivity {
    Context ctx;
    Credentials cred;
    RecyclerView recycler;
    List<JSONObject> l=new ArrayList<>();
    TiendaAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tiendas);

        ctx = this;
        cred = new Credentials(ctx);

        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Tiendas");

        recycler= findViewById(R.id.recycler_view_tiendas);
        recycler.setLayoutManager(new LinearLayoutManager(ctx));
        adapter=new TiendaAdapter(l);
        recycler.setAdapter(adapter);
        l.clear();

        for (int i =0;i<5;i++){
            JSONObject o = new JSONObject();
            l.add(o);
        }

        adapter.notifyDataSetChanged();
        Intent intent = getIntent();
        String act = intent.getStringExtra("cliente_id");
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
