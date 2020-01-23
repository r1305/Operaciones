package com.system.operaciones.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.system.operaciones.R;
import com.system.operaciones.activities.ClienteDetailActivity;
import com.system.operaciones.activities.TiendasActivity;
import com.system.operaciones.utils.Credentials;

import org.json.simple.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class TiendaAdapter extends RecyclerView.Adapter<TiendaAdapter.ViewHolder> {

    Credentials cred;
    Context ctx;
    List<JSONObject> l = new ArrayList<>();

    public TiendaAdapter(List<JSONObject> l) {
        this.l = l;
    }

    @NonNull
    @Override
    public TiendaAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ctx = parent.getContext();
        cred = new Credentials(ctx);
        return new TiendaAdapter.ViewHolder(LayoutInflater.from(ctx).inflate(R.layout.item_tienda, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull TiendaAdapter.ViewHolder holder, int position) {
        JSONObject ob = l.get(position);
        final String id = (String)ob.get("id");
        String tienda = (String)ob.get("tienda");
        String ceco = (String)ob.get("ceco");
        String direccion = (String)ob.get("direccion");

        holder.tienda.setText(tienda);
        holder.ceco.setText(ceco);
        holder.direccion.setText(direccion);
        holder.card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ctx.startActivity(new Intent(ctx, ClienteDetailActivity.class).putExtra("tienda_id",id));
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
        TextView direccion,ceco,tienda;
        private ViewHolder(View itemView) {
            super(itemView);
            card = itemView.findViewById(R.id.item_card_tienda);
            direccion = itemView.findViewById(R.id.item_tienda_direccion);
            tienda = itemView.findViewById(R.id.item_tienda_tienda);
            ceco = itemView.findViewById(R.id.item_tienda_ceco);
        }
    }
}
