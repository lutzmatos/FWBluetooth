package com.franciscoweb.bluetooth;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.UUID;

public class BluetoothChatClienteActivity extends BluetoothCheckActivity implements ChatController.ChatListener, View.OnClickListener {

    // Identificador padrao
    protected static final UUID uuid = UUID.fromString("b9233a7a-9e70-4307-9078-43d9d28f804f");

    // Core do chat
    protected BluetoothDevice device;
    protected ChatController chat;

    // Elementos da tela
    protected EditText edtMsg;
    protected TextView txtMsgRecebidas;
    protected Button btnEnviar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bluetooth_chat_cliente);

        // Instancia dos elemetos da tela
        edtMsg = (EditText) findViewById(R.id.edtMsg);
        txtMsgRecebidas = (TextView) findViewById(R.id.txtMsgRecebidas);
        btnEnviar = (Button) findViewById(R.id.btnEnviar);
        btnEnviar.setOnClickListener(this);

        // Vamos identificar a intent que contém um bluetooth convidado
        device = getIntent().getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);

        // Se o dispositivo existir vamos conecta-lo e iniciar o chat
        try {
            if (device != null){
                getSupportActionBar().setTitle("CLI - Conectado com: " + device.getName());
                BluetoothSocket socket = device.createRfcommSocketToServiceRecord(uuid);
                socket.connect();

                chat = new ChatController(socket, this);
                chat.start();
                btnEnviar.setEnabled(true);
            }
        }catch (IOException e){
            Log.d("FW-CHAT-CLIENT", "Ocorreu um erro: " + e.getMessage());
        }
    }

    /**
     * Monitoramento de clique em botão
     * @param v
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnEnviar:
                try {
                    String msgEnviada = edtMsg.getText().toString();
                    chat.sendMessage(msgEnviada);
                    edtMsg.setText("");
                    String txt = txtMsgRecebidas.getText().toString();
                    txtMsgRecebidas.setText(txt + "\nEnviado: " + msgEnviada);
                } catch (IOException e) {
                    Log.d("FW-CHAT-CLIENT", "Ocorreu um erro: " + e.getMessage());
                }
                break;
        }
    }

    /**
     * Sobrescrita do método de coleta de mensagem da interface {ChatController.ChatListener}
     * @param msg
     */
    @Override
    public void onMessageReceived(final String msg) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(BluetoothChatClienteActivity.this, msg, Toast.LENGTH_LONG).show();
                String txt = txtMsgRecebidas.getText().toString();
                txtMsgRecebidas.setText(txt + "\nRecebido: " + msg);
            }
        });
    }

    /**
     * Finalização do chat no cliente
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (chat != null){
            chat.stop();
        }
    }
}
