package dpassos.com.br.socketmouse;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v4.content.LocalBroadcastManager;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * Created by djalma on 26/06/2015.
 */
public class Network {

    Context c;
    private String ip;
    private Socket socket;
    private PrintWriter out;
    private BufferedReader in;

    public Network(Context c, String ip){
        this.c = c;
        this.ip = ip;
        init();
    }

    public void init(){
        new AsyncTask(){
            @Override
            protected Object doInBackground(Object[] params) {
                try {
                    socket = new Socket(ip, 4444);
                    out = new PrintWriter(socket.getOutputStream(), true);
                    in = new BufferedReader(
                            new InputStreamReader(socket.getInputStream()));
                    Intent it  = new Intent("assinck_task_broad_cast");
                    it.putExtra("status", "conexao_estabelecida");
                    LocalBroadcastManager.getInstance(c).sendBroadcast(it);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return null;
            }
        }.execute();
    }

    public void finalizarConexao(){
        new AsyncTask(){
            @Override
            protected Object doInBackground(Object[] params) {
                try {
                    if(socket != null)
                        socket.close();
                } catch (Exception e1) {
                    e1.printStackTrace();
                }
                socket = null;
                out = null;
                in = null;
                return null;
            }
        }.execute();
    }

    public void enviarMensagem(Context c, String toServerMesage){
        AsyncMensagem task = new AsyncMensagem(c, socket, out, in);
        task.execute(toServerMesage);
    }
}