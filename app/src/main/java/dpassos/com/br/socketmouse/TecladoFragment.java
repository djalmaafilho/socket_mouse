package dpassos.com.br.socketmouse;


import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.Toast;


/**
 * A simple {@link Fragment} subclass.
 */
public class TecladoFragment extends BaseFragment implements View.OnClickListener{


    public TecladoFragment() {
        setTitulo("Teclado");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_teclado, container, false);
        TableLayout layRay = (TableLayout)v.findViewById(R.id.layoutRaiz);
        for(int i= 0; i < layRay.getChildCount(); i++){
            TableRow tr = (TableRow)layRay.getChildAt(i);
            for(int j = 0; j < tr.getChildCount(); j++){
                tr.getChildAt(j).setOnClickListener(this);
            }

        }
        return v;
    }

    @Override
    public void onClick(View v) {
        Button bt = (Button)v;
        Toast.makeText(getActivity(), bt.getText(), Toast.LENGTH_SHORT).show();
    }
}