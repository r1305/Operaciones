package com.system.operaciones.adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.system.operaciones.R;
import com.system.operaciones.activities.InstalacionesActivity;
import com.system.operaciones.activities.MantenimientosActivity;
import com.system.operaciones.activities.UrgenciasActivity;
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
        final String is_uezu = (String)ob.get("is_uezu");
        String tienda = (String)ob.get("tienda");
        String ceco = ob.get("ceco").equals("")?"--":(String)ob.get("ceco");
        String direccion = (String)ob.get("direccion");

        holder.tienda.setText(tienda);
        holder.ceco.setText(ceco);
        holder.direccion.setText(direccion);
        holder.card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("key_act",cred.getData("key_act"));
                if(is_uezu.equals("0")){
                    AlertDialog.Builder builder = new AlertDialog.Builder(ctx);
                    builder.setTitle("¡Atención!");
                    builder.setMessage("Esta tienda no fue instaladad por Uezu\n¿Desea continuar?");
                    builder.setPositiveButton("Continuar", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            String act = cred.getData("key_act");
                            if(act.equals("1"))
                            {
                                Log.e("tienda_adapter_act","1");
                                ctx.startActivity(new Intent(ctx, InstalacionesActivity.class).putExtra("tienda_id",id));
                            }
                            else if(act.equals("2")){
                                Log.e("tienda_adapter_act","2");
                                ctx.startActivity(new Intent(ctx, MantenimientosActivity.class).putExtra("tienda_id",id));
                            }
                            else if(act.equals("3"))
                            {
                                Log.e("tienda_adapter_act","3");
                                ctx.startActivity(new Intent(ctx, UrgenciasActivity.class).putExtra("tienda_id",id));
                            }
                        }
                    });
                    builder.setNegativeButton("Cerrar", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                    AlertDialog alertDialog = builder.create();
                    alertDialog.show();
                }else{
                    String act = cred.getData("key_act");
                    if(act.equals("1"))
                    {
                        Log.e("tienda_adapter_act","1");
                        ctx.startActivity(new Intent(ctx, InstalacionesActivity.class).putExtra("tienda_id",id));
                    }
                    else if(act.equals("2")){
                        Log.e("tienda_adapter_act","2");
                        ctx.startActivity(new Intent(ctx, MantenimientosActivity.class).putExtra("tienda_id",id));
                    }
                    else if(act.equals("3"))
                    {
                        Log.e("tienda_adapter_act","3");
                        ctx.startActivity(new Intent(ctx, UrgenciasActivity.class).putExtra("tienda_id",id));
                    }
                }
            }
        });
        if(is_uezu.equals("0")){
            holder.linear_card.setBackground(null);
            holder.linear_card.setBackgroundColor(ctx.getResources().getColor(R.color.plomoBackground));
        }
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
        LinearLayout linear_card;
        private ViewHolder(View itemView) {
            super(itemView);
            card = itemView.findViewById(R.id.item_card_tienda);
            direccion = itemView.findViewById(R.id.item_tienda_direccion);
            tienda = itemView.findViewById(R.id.item_tienda_tienda);
            ceco = itemView.findViewById(R.id.item_tienda_ceco);
            linear_card = itemView.findViewById(R.id.linear_card);
        }
    }
}
