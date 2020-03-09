package com.system.operaciones.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.google.android.material.navigation.NavigationView;
import com.system.operaciones.fragments.PrincipalFragment;
import com.system.operaciones.R;
import com.system.operaciones.utils.Credentials;

public class MenuPrincipalActivity extends AppCompatActivity implements View.OnClickListener, NavigationView.OnNavigationItemSelectedListener {
    Context ctx;
    Credentials cred;
    DrawerLayout drawer;
    String nav_header;
    TextView header_name;
    TextView header_version;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_principal);
        ctx = this;
        cred = new Credentials(ctx);

        nav_header = "¡Hola, " + cred.getData("key_name") + "!";
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setBackgroundColor(ctx.getResources().getColor(R.color.verdePastel,null));
        setSupportActionBar(toolbar);

        drawer = findViewById(R.id.drawer_layout);

        NavigationView navigationView = findViewById(R.id.nav_view);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);

        getSupportFragmentManager().beginTransaction().replace(R.id.container, new PrincipalFragment()).commit();
        navigationView.setCheckedItem(R.id.navInicio);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_instalaciones:
                cred.logout();
            break;
            case R.id.btn_mantenimientos:
            break;
            case R.id.btn_urgencias:
            break;
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        int id = menuItem.getItemId();
        if(id==R.id.navLogOut){
            cred.logout();
        }
        if(id==R.id.navInicio)
        {
            Intent i = new Intent(ctx,ClientesActivity.class);
            startActivity(i);
            ((MenuPrincipalActivity)ctx).finish();
        }
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
}
