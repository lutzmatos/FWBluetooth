package com.franciscoweb.bluetooth;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import java.io.IOException;
import java.util.UUID;

public class BluetoothChatServidorActivity extends BluetoothChatClienteActivity implements ChatController.ChatListener {

    private static final UUID uuid = UUID.fromString("b9233a7a-9e70-4307-9078-43d9d28f804f");
    private BluetoothServerSocket serverSocket;
    private boolean running;

    /**
     * Activity - Ciclo de vida: Inicial
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_bluetooth_chat_cliente);

        // Vamos deixar este aparelho visível
        Intent intentVisivel = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
        intentVisivel.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 300);
        startActivity(intentVisivel);
    }

    /**
     * Activity - Ciclo de vida: Retomada após onPause
     */
    @Override
    protected void onResume() {
        super.onResume();
        new ChatThread().start();
    }

    /**
     * Classe que conecta um cliente ao chat
     */
    class ChatThread extends Thread{
        @Override
        public void run() {
            super.run();

            try {
                // Vamos iniciar a escuta por conexões
                serverSocket = bluetoothAdapter.listenUsingRfcommWithServiceRecord("Franciscoweb", uuid);
                BluetoothSocket socket = serverSocket.accept();

                // Se houver uma conexão vamos cadastra-la para iniciar os utilização do serviço
                if (socket != null){
                    // Vamos identificar o dispositivo
                    final BluetoothDevice device = socket.getRemoteDevice();

                    // Vamos rodar o serviço em outra thread para não bloquear a aplicação
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            getSupportActionBar().setTitle("SRV - Conectado com: "+device.getName());
                            btnEnviar.setEnabled(true);
                            Toast.makeText(getBaseContext(), "Conectou: "+device.getName(), Toast.LENGTH_SHORT).show();
                        }
                    });

                    // Vamos iniciar a aplicação de chat para o usuario conectado
                    chat = new ChatController(socket, BluetoothChatServidorActivity.this);
                    chat.start();
                }
            } catch (IOException e) {
                running = false;
            }
        }
    }

    /**
     * Activity - Ciclo de vida: Final
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (serverSocket != null){
            try {
                serverSocket.close();
            } catch (IOException e) {

            }
        }
    }
}
