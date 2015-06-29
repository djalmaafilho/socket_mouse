package dpassos.com.br.socketmouse;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v4.content.LocalBroadcastManager;
import java.io.BufferedReader;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * Created by djalma on 26/06/2015.
 */
public class AsyncMensagem extends AsyncTask<String, Void, Integer>{

    Socket socket;
    PrintWriter out;
    BufferedReader in;
    Context c;

    AsyncMensagem(Context c, Socket socket, PrintWriter out, BufferedReader in) {
        this.socket = socket;
        this.out = out;
        this.in = in;
        this.c = c;
    }

    @Override
    protected Integer doInBackground(String... params) {
        Integer status = 0;
        try {
            while(!isCancelled() && !in.ready()){
                Thread.sleep(50);
            }
            String fromServerMensage = in.readLine();
            if(fromServerMensage != null){
                System.out.println(fromServerMensage);
            }
            out.println(params[0]);
            out.flush();

            status = 200;

        } catch (Exception e) {
            e.printStackTrace();
            status = 500;
        }

        return status;
    }

    @Override
    protected void onPostExecute(Integer resultado) {
        Intent it = new Intent("assinck_task_broad_cast");

        if(resultado.equals(0)){
            it.putExtra("status", "task_concluida");
        }else if(resultado.equals(200)){
            it.putExtra("status", "mensagem_enviada");
        }else if(resultado.equals(500)){
            it.putExtra("status", "finalizar_conexao");
        }
        LocalBroadcastManager.getInstance(c).sendBroadcast(it);
        super.onPostExecute(resultado);
    }
}