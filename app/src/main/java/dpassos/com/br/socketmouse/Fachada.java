package dpassos.com.br.socketmouse;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.content.LocalBroadcastManager;
import java.io.Serializable;
import java.util.Map;

/**
 * Created by djalma on 26/06/2015.
 */
public class Fachada extends Application {

    private static Fachada instancia;
    private Network comunicacao;

    @Override
    public void onCreate() {
        super.onCreate();
        instancia = this;
    }

    public static Fachada getInstancia(){
        if(instancia == null){
           throw new IllegalStateException("Aplicação não declarada no manifest");
        }
        return instancia;
    }

    public void initNetWork(final String ipServer){
        comunicacao = new Network(this, ipServer);
    }

    public void enviarMensagem(Context c, String toServerMesage){
        if(comunicacao != null){
            comunicacao.enviarMensagem(c, toServerMesage);
        }
    }

    public void finalizarConexao(){
        comunicacao.finalizarConexao();
    }

    public void salvarIp(Context c, String ip){
        SharedPreferences sp = c.getSharedPreferences("socketmouseprefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString("ultimo_ip", ip);
        editor.commit();
    }

    public void sendBroadcast(Map<String, Object> mapa){
        Intent it  = new Intent("assinck_task_broad_cast");
        for(String key: mapa.keySet()){
            Object value =  mapa.get(key);
            it.putExtra(key,(Serializable)value);
        }
        LocalBroadcastManager.getInstance(instancia).sendBroadcast(it);
    }
}