package com.franciscoweb.bluetooth;

import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    Button btnVerificar, btnPareados, btnBuscarDispositivo, btnVisivel, btnServidor, btnOperacoesMatematicas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Botão principal
        btnVerificar = (Button) findViewById(R.id.btnVerificar);
        btnVerificar.setOnClickListener(this);

        // Botão de lista de pareados
        btnPareados = (Button) findViewById(R.id.btnPareados);
        btnPareados.setOnClickListener(this);

        // Botão de busca de dispositivos (pareados e não pareados)
        btnBuscarDispositivo = (Button) findViewById(R.id.btnBuscarDispositivo);
        btnBuscarDispositivo.setOnClickListener(this);

        // Botão para tornar o dispositivo visível
        btnVisivel = (Button) findViewById(R.id.btnVisivel);
        btnVisivel.setOnClickListener(this);

        // Botão que inicializa o servidor
        btnServidor = (Button) findViewById(R.id.btnIniciarServidor);
        btnServidor.setOnClickListener(this);

        // Botão de debug
        btnOperacoesMatematicas = (Button) findViewById(R.id.btnOperacoesMatematicas);
        btnOperacoesMatematicas.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnVerificar:
                Intent intentCheckBluetooth = new Intent(this, BluetoothCheckActivity.class);
                startActivity(intentCheckBluetooth);
                break;
            case R.id.btnPareados:
                Intent intentListaPareados = new Intent(this, ListaPareadosActivity.class);
                startActivity(intentListaPareados);
                break;
            case R.id.btnBuscarDispositivo:
                Intent intentBuscarDispositivo = new Intent(this, ListaDispositivosActivity.class);
                startActivity(intentBuscarDispositivo);
                break;
            case R.id.btnVisivel:
                Intent intentVisivel = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
                intentVisivel.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 300);
                startActivity(intentVisivel);
                break;
            case R.id.btnIniciarServidor:
                Intent intentIniciarServidor = new Intent(this, BluetoothChatServidorActivity.class);
                startActivity(intentIniciarServidor);
                break;
            case R.id.btnOperacoesMatematicas:
                int a = 10;
                int b = 5;
                for (int i = 0; i<10; i++) {
                    Toast.makeText(MainActivity.this, "Clique " + Integer.toString(i), Toast.LENGTH_LONG).show();
                    int c = a + b;
                    a = a + i + 1;
                    //a = a / i;
                    b = b + i;
                }
                break;
        }
    }
}
