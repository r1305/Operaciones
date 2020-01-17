package com.system.operaciones.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.system.operaciones.R;
import com.system.operaciones.activities.InstalacionDetailActivity;
import com.system.operaciones.activities.MantenimientoDetailActivity;
import com.system.operaciones.activities.MantenimientosActivity;
import com.system.operaciones.utils.Credentials;

import org.json.simple.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MantenimientoAdapter extends RecyclerView.Adapter<MantenimientoAdapter.ViewHolder>{

    Credentials cred;
    Context ctx;
    List<JSONObject> l = new ArrayList<>();

    public MantenimientoAdapter(List<JSONObject> l) {
        this.l = l;
    }

    @NonNull
    @Override
    public MantenimientoAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ctx = parent.getContext();
        cred = new Credentials(ctx);
        return new MantenimientoAdapter.ViewHolder(LayoutInflater.from(ctx).inflate(R.layout.item_mantenimiento, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MantenimientoAdapter.ViewHolder holder, int position) {
        holder.card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ctx.startActivity(new Intent(ctx, MantenimientoDetailActivity.class));
            }
        });
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
        private ViewHolder(View itemView) {
            super(itemView);
            card = itemView.findViewById(R.id.item_card_mantenimiento);
        }
    }
}
