package com.apdr.anywherechat;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class AccountActivity extends AppCompatActivity {

    EditText userName;
    EditText userPassword;
    FloatingActionButton buttonok;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);

        userName = (EditText)findViewById(R.id.editUserName);
        userPassword = (EditText)findViewById(R.id.editPassword);
        buttonok = (FloatingActionButton)findViewById(R.id.buttonok);

        Intent i = getIntent();
        userName.setText(i.getStringExtra("nome"));
        userPassword.setText(i.getStringExtra("senha"));

        buttonok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i;
                i = new Intent(AccountActivity.this, DevicesActivity.class);
                startActivity(i);
            }
        });

    }

    public void onDestroy() {
        super.onDestroy();
    }
}
