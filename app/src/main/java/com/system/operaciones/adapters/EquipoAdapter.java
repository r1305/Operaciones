package com.system.operaciones.adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.system.operaciones.R;
import com.system.operaciones.activities.TiendasActivity;
import com.system.operaciones.utils.Credentials;

import org.json.simple.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class EquipoAdapter extends RecyclerView.Adapter<EquipoAdapter.ViewHolder> {

    Credentials cred;
    Context ctx;
    List<JSONObject> l = new ArrayList<>();

    public EquipoAdapter(List<JSONObject> l) {
        this.l = l;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ctx = parent.getContext();
        cred = new Credentials(ctx);
        return new EquipoAdapter.ViewHolder(LayoutInflater.from(ctx).inflate(R.layout.item_equipo, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        JSONObject ob = l.get(position);
        final String modelo = (String)ob.get("modelo");
        final String tipo = (String)ob.get("tipo");
        final String nro_serie = (String)ob.get("evap_nro_serie");
        final String logo = (String)ob.get("marca");
        System.out.println(ob);
        holder.modelo.setText(modelo);
        holder.tipo.setText(tipo);
        holder.nro_serie.setText(nro_serie);
        Glide.with(ctx)
                .load(logo)
//                .centerCrop()
//                .crossFade()
                .into(holder.marca);
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
        TextView modelo,tipo,nro_serie;
        ImageView marca;
        private ViewHolder(View itemView) {
            super(itemView);
            card = itemView.findViewById(R.id.item_card_urgencia);
            modelo = itemView.findViewById(R.id.equipo_modelo);
            tipo = itemView.findViewById(R.id.equipo_tipo);
            nro_serie = itemView.findViewById(R.id.equipo_nro_serie);
            marca = itemView.findViewById(R.id.equipo_logo);
        }
    }
}
