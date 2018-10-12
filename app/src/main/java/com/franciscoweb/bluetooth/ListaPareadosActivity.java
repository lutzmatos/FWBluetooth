package com.franciscoweb.bluetooth;

import android.bluetooth.BluetoothDevice;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class ListaPareadosActivity extends BluetoothCheckActivity implements AdapterView.OnItemClickListener {

    List<BluetoothDevice> listaPareados;
    ListView listViewPareados;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_pareados);

        // Instancia da ListView
        listViewPareados = (ListView) findViewById(R.id.listViewPareados);
    }

    @Override
    protected void onResume() {
        super.onResume();

        // Vamos coletar os dispositivos pareados
        listaPareados = new ArrayList<BluetoothDevice>(bluetoothAdapter.getBondedDevices());
        List<String> nomes = new ArrayList<String>();

        // Varredura para preencher a variavel
        for (BluetoothDevice device: listaPareados) {
            nomes.add(device.getName() + "\n" + device.getAddress());
        }

        // Criacao do adaptador
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, nomes);
        // Exportacao do adaptador para o ListView
        listViewPareados.setAdapter(adapter);
        // Clique sobre o item
        listViewPareados.setOnItemClickListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        BluetoothDevice device = listaPareados.get(position);
        String msg = device.getName()
                        + " \n Adress: " + device.getAddress()
                        + " \n Class: " + device.getBluetoothClass()
                        + " \n BondState: " + device.getBondState()
                        + " \n Type: " + device.getType()
                        + " \n Uuids: " + device.getUuids();
        Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
    }
}
