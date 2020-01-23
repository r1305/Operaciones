package com.system.operaciones.adapters;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.system.operaciones.R;
import com.system.operaciones.activities.TiendasActivity;
import com.system.operaciones.utils.Credentials;

import org.json.simple.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ClienteAdapter extends RecyclerView.Adapter<ClienteAdapter.ViewHolder> {

    Credentials cred;
    Context ctx;
    List<JSONObject> l = new ArrayList<>();

    public ClienteAdapter(List<JSONObject> l) {
        this.l = l;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ctx = parent.getContext();
        cred = new Credentials(ctx);
        return new ClienteAdapter.ViewHolder(LayoutInflater.from(ctx).inflate(R.layout.item_cliente, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        JSONObject ob = l.get(position);
        final String id = (String)ob.get("id");
        final String cliente = (String)ob.get("razon_social");
        final String ruc = "RUC: "+ob.get("ruc");

        holder.tienda.setText(cliente);
        holder.ruc.setText(ruc);
        holder.card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ctx.startActivity(new Intent(ctx, TiendasActivity.class).putExtra("cliente_id",id));
                Log.e("position",id);
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
        TextView codigo,tienda,ruc;
        private ViewHolder(View itemView) {
            super(itemView);
            card = itemView.findViewById(R.id.item_card_cliente);
            tienda = itemView.findViewById(R.id.item_cliente_name);
            ruc = itemView.findViewById(R.id.item_cliente_ruc);
        }
    }
}
