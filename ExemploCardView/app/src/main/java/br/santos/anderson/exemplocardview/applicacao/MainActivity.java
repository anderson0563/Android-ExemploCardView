package br.santos.anderson.exemplocardview.applicacao;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.IBinder;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.ArrayList;

import br.santos.anderson.exemplocardview.R;
import br.santos.anderson.exemplocardview.infra.Conexao;
import br.santos.anderson.exemplocardview.modelo.Person;

public class MainActivity extends AppCompatActivity  implements ServiceConnection {
    private Conexao logar;
    Intent intent;
    CardView cartao;
    LinearLayout linear;
    ArrayList<Person> contacts;
    TextView nome, empresa, site;
    public void onClick(View v) throws IOException {
        String jsonFile = logar.conectar();
        System.out.println(jsonFile);
        LinearLayout item;
        final ObjectMapper mapper = new ObjectMapper();
        Person[] lista = mapper.readValue(jsonFile, Person[].class);

        for(Person pessoa: lista){
            cartao = new CardView(MainActivity.this);
            cartao.setPadding(10, 40, 10, 40);

            item = new LinearLayout(MainActivity.this);
            item.setBackgroundColor(Color.rgb(255, 255, 224));
            item.setOrientation(LinearLayout.VERTICAL);

            nome = new TextView(MainActivity.this);
            nome.setPadding(10,5,10,5);
            nome.setTypeface(null, Typeface.BOLD);
            nome.setTextColor(Color.RED);
            nome.setTextSize(24);
            nome.setText(pessoa.getName());

            empresa = new TextView(MainActivity.this);
            empresa.setPadding(10,0,10,5);
            empresa.setTypeface(null, Typeface.BOLD);
            empresa.setTextColor(Color.BLUE);
            empresa.setTextSize(18);
            empresa.setText(pessoa.getCompany().getName());

            site = new TextView(MainActivity.this);
            site.setPadding(10,0,10,15);
            site.setTypeface(null, Typeface.BOLD);
            site.setTextColor(Color.BLACK);
            site.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_END);
            site.setTextSize(12);
            site.setText(pessoa.getWebsite());

            item.addView(nome);
            item.addView(empresa);
            item.addView(site);

            cartao.addView(item);
            linear.addView(cartao);

        }


    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        linear = (LinearLayout) findViewById(R.id.linear);
    }

    @Override
    protected void onResume() {
        super.onResume();
        intent= new Intent(this, Conexao.class);
        bindService(intent, this , Context.BIND_AUTO_CREATE);
    }

    @Override
    protected void onPause() {
        super.onPause();
        unbindService(this);
    }

    @Override
    public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
        Conexao.MyBinder b = (Conexao.MyBinder) iBinder;
        logar = b.getService();

    }

    @Override
    public void onServiceDisconnected(ComponentName componentName) {
        logar=null;
    }

}