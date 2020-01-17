package com.system.operaciones.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.os.Bundle;

import com.system.operaciones.R;
import com.system.operaciones.adapters.InstalacionAdapter;
import com.system.operaciones.utils.Credentials;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class InstalacionesActivity extends AppCompatActivity {
    Credentials cred;
    Context ctx;
    InstalacionAdapter adapter;
    RecyclerView recycler;
    List<JSONObject> l=new ArrayList<>();
    JSONArray ar_lineas = new JSONArray();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_instalaciones);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Instalaciones");

        ctx = this;
        cred = new Credentials(ctx);

        recycler= findViewById(R.id.recycler_view_instalaciones);
        recycler.setLayoutManager(new LinearLayoutManager(ctx));
        adapter=new InstalacionAdapter(l);
        recycler.setAdapter(adapter);
        l.clear();

        for (int i =0;i<5;i++){
            JSONObject o = new JSONObject();
            l.add(o);
        }

        adapter.notifyDataSetChanged();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
