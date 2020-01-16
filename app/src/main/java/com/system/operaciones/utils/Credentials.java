package com.system.operaciones.utils;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import com.system.operaciones.activities.LoginActivity;
import com.system.operaciones.activities.MenuPrincipalActivity;

import static android.content.Context.MODE_PRIVATE;

public class Credentials {
    private Context ctx;
    private SharedPreferences sp1;

    public Credentials(Context ctx) {
        this.ctx = ctx;
        sp1=ctx.getSharedPreferences("LOGIN", MODE_PRIVATE);
    }

    public void save_credentials(String token,String user_id,String empresa,String key_is_new_password,String psw,String name,String empresa_name){
        SharedPreferences.Editor Ed=sp1.edit();
        Ed.putString("token",token);
        Ed.putString("user_id",user_id);
        Ed.putString("key_empresa",empresa);
        Ed.putString("key_empresa_name",empresa_name);
        Ed.putString("key_password",key_is_new_password);
        Ed.putString("key_psw",psw);
        Ed.putString("key_name",name);
        Ed.apply();
    }

    public void save_data(String key,String data)
    {
        SharedPreferences.Editor Ed=sp1.edit();
        Ed.putString(key,data);
        Ed.apply();
    }

    public String getData(String key)
    {
        return sp1.getString(key,"");
    }

    public void logout(){
        SharedPreferences.Editor Ed=sp1.edit();
        Ed.putString("token","");
        Ed.putString("user_id","");
        Ed.apply();

        Intent i=new Intent(ctx, LoginActivity.class);

        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        PendingIntent pi = PendingIntent.getActivity(ctx, 0, i, 0);
        ctx.startActivity(i);
        new MenuPrincipalActivity().finish();
    }
}
