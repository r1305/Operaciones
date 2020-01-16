package com.system.operaciones.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.fragment.app.Fragment;

import com.system.operaciones.activities.InstalacionesActivity;
import com.system.operaciones.activities.MantenimientosActivity;
import com.system.operaciones.activities.UrgenciasActivity;
import com.system.operaciones.R;
import com.system.operaciones.utils.Credentials;

public class PrincipalFragment extends Fragment implements View.OnClickListener {
    Context ctx;
    Credentials cred;
    Button btn_instalaciones,btn_mantenimientos,btn_urgencias;

    public PrincipalFragment() {
        // Required empty public constructor
    }
    // TODO: Rename and change types and number of parameters
    public static PrincipalFragment newInstance() {
        PrincipalFragment fragment = new PrincipalFragment();

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ctx = this.getActivity();
        cred = new Credentials(ctx);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_principal, container, false);
        btn_instalaciones = v.findViewById(R.id.btn_instalaciones);
        btn_mantenimientos = v.findViewById(R.id.btn_mantenimientos);
        btn_urgencias = v.findViewById(R.id.btn_urgencias);

        btn_instalaciones.setOnClickListener(this);
        btn_mantenimientos.setOnClickListener(this);
        btn_urgencias.setOnClickListener(this);
        return v;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_instalaciones:
                startActivity(new Intent(ctx, InstalacionesActivity.class));
                break;
            case R.id.btn_mantenimientos:
                startActivity(new Intent(ctx, MantenimientosActivity.class));
                break;
            case R.id.btn_urgencias:
                startActivity(new Intent(ctx, UrgenciasActivity.class));
                break;
        }
    }

}
