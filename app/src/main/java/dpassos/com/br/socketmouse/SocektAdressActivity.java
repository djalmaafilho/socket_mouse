package dpassos.com.br.socketmouse;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


public class SocektAdressActivity extends ActionBarActivity {

    private EditText editText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_socekt_adress);

        SharedPreferences sp = getSharedPreferences("socketmouseprefs",MODE_PRIVATE);
        String strIp = sp.getString("ultimo_ip", "");

        editText = (EditText)findViewById(R.id.txtIp);
        editText.setText(strIp);

        Button bt = (Button)findViewById(R.id.btIniciar);
        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                iniciar();
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_socekt_adress, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void iniciar(){
        if(editText.getText().toString().equals("")){
            Toast.makeText(this, "Ip Não informado", Toast.LENGTH_SHORT).show();
            return;
        }
        Intent it = new Intent(this, MainActivity.class);
        it.putExtra("ip", editText.getText().toString());
        startActivity(it);
    }
}