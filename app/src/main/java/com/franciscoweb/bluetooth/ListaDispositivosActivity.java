package com.franciscoweb.bluetooth;

import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.util.LogPrinter;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class ListaDispositivosActivity extends BluetoothCheckActivity implements AdapterView.OnItemClickListener {

    // Caixa de diálogo de progressbar
    private ProgressDialog dialog;
    // Lista de dispositivos detectados
    protected List<BluetoothDevice> listaDispositivos;
    // Elemento que exibe a lista de dispositivos detectados
    private ListView listViewDispositivos;

    // Activity - Ciclo de vida: Inicial
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_dispositivos);

        // Objeto do elemento que exibirá os dispositivos
        listViewDispositivos = (ListView) findViewById(R.id.listViewDispositivos);

        // Vamos verificar se o adaptador foi instanciado na classe pai
        if (bluetoothAdapter != null){
            // Coleta dos dispositivos
            listaDispositivos = new ArrayList<>(bluetoothAdapter.getBondedDevices());

            // Escuta dos broadcasts
            this.registerReceiver(receiver, new IntentFilter(BluetoothAdapter.ACTION_DISCOVERY_STARTED));
            this.registerReceiver(receiver, new IntentFilter(BluetoothDevice.ACTION_FOUND));
            this.registerReceiver(receiver, new IntentFilter(BluetoothAdapter.ACTION_DISCOVERY_FINISHED));
        }
    }

    /**
     * Activity - Ciclo de vida: Retomada após pausa (onPause)
     */
    @Override
    protected void onResume() {
        super.onResume();

        // Vamos verificar se o adaptador continua instanciado
        if (bluetoothAdapter != null){
            // Vamos reiniciar a descoberta de dispositivos
            if (bluetoothAdapter.isDiscovering()){
                bluetoothAdapter.cancelDiscovery();
            }
            bluetoothAdapter.startDiscovery();

            // Aviso para o usuário
            dialog = ProgressDialog.show(
                    this,
                    "Buscando...",
                    "Buscando dispositivos Bluetooth!",
                    false,
                    true
            );
        }
    }

    /**
     * Receptor de broadcast
     */
    private final BroadcastReceiver receiver = new BroadcastReceiver() {

        // Contador de dispositivos
        private int count;

        // Ciclo de vida do broadcast: Recepcao
        @Override
        public void onReceive(Context context, Intent intent) {

            // Vamos coletar a ação para sabermos o que estamos recebendo
            String acao = intent.getAction();

            // Ação: Dispositivo encontrado
            if (BluetoothDevice.ACTION_FOUND.equals(acao)){
                // Vamos instanciar uma variável que representa o dispositivo
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);

                // Vamos armazenar em nossa variável local apenas se o dispositivo não estiver pareado
                if (device.getBondState() != BluetoothDevice.BOND_BONDED){
                    listaDispositivos.add(device);
                    Toast.makeText(context, "Encontrou: " + device.getName(), Toast.LENGTH_SHORT).show();
                    count++;
                }
            }
            // Ação: inicialização da busca por dispositivos
            else if (BluetoothAdapter.ACTION_DISCOVERY_STARTED.equals(acao)){
                count = 0;
                Toast.makeText(context, "Busca iniciada!", Toast.LENGTH_SHORT).show();
            }
            // Ação: finalização da busca por dispositivos
            else if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(acao)){
                Toast.makeText(context, "Busca finalizada. " + count + " dispositivo(s) encontrado(s)!", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
                atualizarLista();
            }
        }
    };

    /**
     * Atualização da lsita de dispositivos
     */
    private void atualizarLista(){

        // Array interno para organizar os itens
        List<String> nomes = new ArrayList<>();

        // Varredura na lista de dispositivos
        for (BluetoothDevice device: listaDispositivos){
            boolean pareado = device.getBondState() == BluetoothDevice.BOND_BONDED;
            nomes.add(device.getName() + "\n" + device.getAddress() + (pareado ? "- pareado" : "- novo"));
        }

        // Vamos atribuir os valores para o elemento TextView e habilitar escuta do clique nos itens
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, nomes);
        listViewDispositivos.setAdapter(adapter);
        listViewDispositivos.setOnItemClickListener(this);
    }

    /**
     * Monitoramento do clique nos itens do TextView que lista os dispositivos não pareados
     * @param parent
     * @param view
     * @param position
     * @param id
     */
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        // Aviso simples
        BluetoothDevice device = listaDispositivos.get(position);
        String msg = device.getName() + " - " + device.getAddress();
        Toast.makeText(this, msg, Toast.LENGTH_LONG).show();

        // Vamos abrir a página de chat
        Intent i = new Intent(this, BluetoothChatClienteActivity.class);
        i.putExtra(BluetoothDevice.EXTRA_DEVICE, device);
        startActivity(i);
    }

    /**
     * Activity - Ciclo de vida: Final
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (bluetoothAdapter != null){
            bluetoothAdapter.cancelDiscovery();
            this.unregisterReceiver(receiver);
        }
    }
}
