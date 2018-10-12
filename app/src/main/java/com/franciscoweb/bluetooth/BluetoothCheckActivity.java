package com.franciscoweb.bluetooth;

import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

public class BluetoothCheckActivity extends AppCompatActivity {

    protected BluetoothAdapter bluetoothAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bluetooth_check);

        // Instancia do adaptador
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        // Vamos verificar se o equipamento tem bluetooth
        if (bluetoothAdapter == null) {
            Toast.makeText(this, "Bluetooth não está disponível no seu aparelho!", Toast.LENGTH_LONG).show();
            finish();
        }

        // Vamos verificar se o bluetooth está ligado
        if (bluetoothAdapter.isEnabled()) {
            Toast.makeText(this, "Bluetooth está ligado!", Toast.LENGTH_LONG).show();
        } else {
            Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(intent, 0);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (bluetoothAdapter.isEnabled()) {
            Toast.makeText(this, "Bluetooth foi ligado!", Toast.LENGTH_LONG).show();
        }
        finish();
    }
}
