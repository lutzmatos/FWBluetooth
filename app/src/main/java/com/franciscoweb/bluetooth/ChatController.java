package com.franciscoweb.bluetooth;

import android.bluetooth.BluetoothSocket;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class ChatController {

    /**
     * Interface para padronizar o tipo de variável do chat
     */
    public interface ChatListener{
        public void onMessageReceived(String msg);
    }

    // Atributos do core do serviço de chat
    private BluetoothSocket socket;
    private InputStream in;
    private OutputStream out;
    private ChatListener listener;
    private boolean running;

    /**
     * Construtor
     * @param socket
     * @param listener
     * @throws IOException
     */
    public ChatController(BluetoothSocket socket, ChatListener listener) throws IOException {
        // Inicialização dos atributos do core do serviço de chat
        this.socket = socket;
        this.in = socket.getInputStream();
        this.out = socket.getOutputStream();
        this.listener = listener;
        this.running = true;
    }

    /**
     * Este método é responsável em manter o servidor escutando as conversas
     */
    public void start(){
        new Thread(){
            @Override
            public void run() {
                super.run();

                running = true;

                byte[] bytes = new byte[1024];
                int tamanho;

                while (running){
                    try {
                        tamanho = in.read(bytes);
                        String msg = new String(bytes, 0, tamanho);
                        listener.onMessageReceived(msg);
                    }catch (Exception e){
                    }
                }
            }
        }.start();
    }

    /**
     * Este método publica as mensagens nos terminais
     * @param msg
     * @throws IOException
     */
    public void sendMessage(String msg) throws IOException{
        if (out != null){
            out.write(msg.getBytes());
        }
    }

    /**
     * Este método finaliza o serviço de escuta do chat
     */
    public void stop(){
        running = false;
        try {
            if (socket != null){
                socket.close();
            }
            if (in != null){
                in.close();
            }
            if (out != null){
                out.close();
            }
        }catch (IOException e){

        }
    }
}
