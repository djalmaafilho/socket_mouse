package dpassos.com.br.socketmouse;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.LocalBroadcastManager;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class SocketAdressFragment extends BaseFragment{

    private EditText editText;

    public SocketAdressFragment(){
        setTitulo("End.IP");
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.activity_socekt_adress, container, false);
        SharedPreferences sp =
                getActivity().getSharedPreferences("socketmouseprefs", Context.MODE_PRIVATE);
        String strIp = sp.getString("ultimo_ip", "");

        editText = (EditText)v.findViewById(R.id.txtIp);
        editText.setText(strIp);

        Button bt = (Button)v.findViewById(R.id.btIniciar);
        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                iniciar();
            }
        });
        return v;
    }

    public void iniciar(){
        if(editText.getText().toString().equals("")){
            Toast.makeText(getActivity(), "Ip Não informado", Toast.LENGTH_SHORT).show();
            return;
        }
        Intent it = new Intent(getActivity(), MainActivity.class);
        it.putExtra("ip", editText.getText().toString());
        startActivity(it);
    }
}