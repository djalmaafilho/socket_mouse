package dpassos.com.br.socketmouse;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

public class BlankFragment extends Fragment implements View.OnTouchListener , SensorEventListener {

    private boolean ativado, podeEnviar;
    private SensorManager sm;
    private float x, y;
    BroadcastReceiver br;

    public static BlankFragment newInstance(String param1, String param2) {
        BlankFragment fragment = new BlankFragment();
        Bundle args = new Bundle();
//        args.putString("p1","p1");
//        args.putString("p2","p2");
        fragment.setArguments(args);
        return fragment;
    }

    public BlankFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        if (getArguments() != null) {
//            String mParam1 = getArguments().getString("p1");
//            String mParam2 = getArguments().getString("p2");
//        }

        final String ip = getActivity().getIntent().getStringExtra("ip");
        Fachada.getInstancia().initNetWork(ip);
        br = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String status = intent.getStringExtra("status");
                if (status.equals("finalizar_conexao")) {
                    finalizarConexao();
                } else if (status.equals("mensagem_enviada")) {
                    podeEnviar = true;
                } else if(status.equals("conexao_estabelecida")){
                    podeEnviar = true;
                    salvarIp(ip);
                    enviarPosicoes(x, y);
                }
            }
        };

        sm = (SensorManager)getActivity().getSystemService(getActivity().SENSOR_SERVICE);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_blank, container, false);
        v.findViewById(R.id.buttonEsq).setOnTouchListener(this);
        v.findViewById(R.id.buttonDir).setOnTouchListener(this);
        v.findViewById(R.id.buttonCentro).setOnTouchListener(this);
        return v;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public void onDetach() {
        finalizarConexao();
        super.onDetach();
    }

    @Override
    public void onStart() {
        super.onStart();
        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(br, new IntentFilter("assinck_task_broad_cast"));
    }


    @Override
    public void onResume() {
        super.onResume();
        Sensor senAccelerometer = sm.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        sm.registerListener(this, senAccelerometer, SensorManager.SENSOR_DELAY_FASTEST);
    }

    @Override
    public void onPause() {
        sm.unregisterListener(this);
        super.onPause();
    }

    @Override
    public void onDestroy() {
        LocalBroadcastManager.getInstance(getActivity()).unregisterReceiver(br);
        super.onDestroy();
    }


    private void enviarMensagem(final String toServerMesage){
        if(!podeEnviar){return;}
        Fachada.getInstancia().enviarMensagem(getActivity(),toServerMesage);
        podeEnviar = false;
    }

    private void finalizarConexao(){
        Fachada.getInstancia().finalizarConexao();
    }

    private void salvarIp(String ip){
        Toast.makeText(getActivity(), "Conectado", Toast.LENGTH_SHORT).show();
        Fachada.getInstancia().salvarIp(getActivity(), ip);
    }

    private void enviarPosicoes(final float xEvent,final float yEvent){
        enviarMensagem(xEvent+"%"+yEvent);
    }

    private void updateMovimento(View v, MotionEvent event){
        if(event.getAction() == MotionEvent.ACTION_UP){
            ativado = !ativado;
            if (ativado){
                v.setBackgroundColor(Color.BLUE);

            }else{
                v.setBackgroundColor(Color.GRAY);
            }
        }
    }

    private void buttonDirEvent(MotionEvent event){
        if(event.getAction() == MotionEvent.ACTION_DOWN){
            enviarMensagem("btDirDown");
        }else if(event.getAction() == MotionEvent.ACTION_UP){
            enviarMensagem("btDirUp");
        }
    }

    private void buttonEsqEvent(MotionEvent event){
        if(event.getAction() == MotionEvent.ACTION_DOWN){
            enviarMensagem("btEsqDown");
        }else if(event.getAction() == MotionEvent.ACTION_UP){
            enviarMensagem("btEsqUp");
        }
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        int fator = 1;
        if(ativado){
            System.out.println(event.values[0]+" "+event.values[1]);
            x -= event.values[0] * fator * Math.abs(event.values[0]);
            y -= event.values[1] * fator * Math.abs(event.values[1]);

            if(x < 0 ) x = 0;
            if(y < 0 ) y = 0;

            enviarPosicoes(x, y);
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if(v.getId() == R.id.buttonCentro){
            updateMovimento(v, event);
        }else if(v.getId() == R.id.buttonEsq){
            buttonEsqEvent(event);
        }else if(v.getId() == R.id.buttonDir){
            buttonDirEvent(event);
        }
        return v.onTouchEvent(event);
    }
}