package com.apdr.anywherechat;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class ChatActivity extends AppCompatActivity {

    public static final int MESSAGE_STATE_CHANGE = 1;
    public static final int MESSAGE_READ = 2;
    public static final int MESSAGE_WRITE = 3;
    public static final int MESSAGE_DEVICE_OBJECT = 4;
    public static final int MESSAGE_TOAST = 5;
    public static final String DEVICE_OBJECT = "device_name";
    private ArrayAdapter<String> chatAdapter;
    private ArrayList<String> chatMessages;
    private ChatController chatController;
    private BluetoothDevice connectingDevice;
    String adress;
    String myMAC;

    ListView listView;
    EditText editText;
    FloatingActionButton floatButton;
    private BluetoothAdapter bluetoothAdapter;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        chatMessages = new ArrayList<>();
        chatAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, chatMessages);
        listView = (ListView) findViewById(R.id.messageList);
        editText = (EditText)findViewById(R.id.editMessage);
        floatButton = (FloatingActionButton)findViewById(R.id.buttonSend);
        Intent intent = getIntent();
        adress = intent.getStringExtra("s");
        myMAC = intent.getStringExtra("y");

        Toast.makeText(this, ""+adress, Toast.LENGTH_SHORT).show();

        if (adress != null){
            try {
                connectToDevice(adress);
            } catch (Exception e){
                Toast.makeText(ChatActivity.this, ""+e, Toast.LENGTH_SHORT).show();
            }

        } else {
            try {
                connectToDevice(myMAC);
            } catch (Exception e){
                Toast.makeText(ChatActivity.this, ""+e, Toast.LENGTH_SHORT).show();
            }
        }
        listView.setAdapter(chatAdapter);
        floatButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendMessage(editText.getText().toString());
                editText.setText("");
            }
        });
    }
    private void connectToDevice(String deviceAddress) {
        bluetoothAdapter.cancelDiscovery();
        chatController.connect(bluetoothAdapter.getRemoteDevice(deviceAddress));
    }

    public void onStart() {
        super.onStart();
        if (!bluetoothAdapter.isEnabled()) {
            Intent enableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            int REQUEST_ENABLE_BLUETOOTH = 0;
            startActivityForResult(enableIntent, REQUEST_ENABLE_BLUETOOTH);

        } else {
            chatController = new ChatController(this, handler);

        }
    }

    @Override
    public void onResume() {
        super.onResume();

        if (chatController != null) {
            if (chatController.getState() == ChatController.STATE_NONE) {
                chatController.start();
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (chatController != null)
            chatController.stop();
    }

    private void sendMessage(String message) {
        Toast.makeText(this, ""+message, Toast.LENGTH_SHORT).show();
        if (chatController.getState() != ChatController.STATE_CONNECTED) {
            Toast.makeText(this, "Connection was lost!", Toast.LENGTH_SHORT).show();
            return;
        }

        if (message.length() > 0) {
            byte[] send = message.getBytes();
            chatController.write(send);
        }
    }

    private Handler handler = new Handler(new Handler.Callback() {

        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what) {

                case MESSAGE_WRITE:
                    byte[] writeBuf = (byte[]) msg.obj;

                    String writeMessage = new String(writeBuf);
                    chatMessages.add("Me: " + writeMessage);
                    chatAdapter.notifyDataSetChanged();
                    break;
                case MESSAGE_READ:
                    byte[] readBuf = (byte[]) msg.obj;

                    String readMessage = new String(readBuf, 0, msg.arg1);
                    chatMessages.add(connectingDevice.getName() + ":  " + readMessage);
                    chatAdapter.notifyDataSetChanged();
                    break;
                case MESSAGE_DEVICE_OBJECT:
                    connectingDevice = msg.getData().getParcelable(DEVICE_OBJECT);
                    Toast.makeText(getApplicationContext(), "Connected to " + connectingDevice.getName(),
                            Toast.LENGTH_SHORT).show();
                    break;
                case MESSAGE_TOAST:
                    Toast.makeText(getApplicationContext(), msg.getData().getString("toast"),
                            Toast.LENGTH_SHORT).show();
                    break;
            }
            return false;
        }
    });

}
