package com.apdr.anywherechat;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.util.Objects;

public class LoginActivity extends AppCompatActivity {

    FloatingActionButton botaoAdd;
    FloatingActionButton botaoLimpa;
    FloatingActionButton botaoLogin;
    EditText textoLogin;
    EditText textoSenha;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        botaoAdd = (FloatingActionButton)findViewById(R.id.fbadd);
        botaoLogin = (FloatingActionButton)findViewById(R.id.fblogin);
        botaoLimpa = (FloatingActionButton) findViewById(R.id.fbclear);
        textoSenha = (EditText)findViewById(R.id.editpassword);
        textoLogin = (EditText) findViewById(R.id.editname);


        botaoAdd.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("NewApi")
            @Override
            public void onClick(View view) {
                DataBaseController crud = new DataBaseController(getBaseContext());

                String NomeString = textoLogin.getText().toString();
                String SenhaString = textoSenha.getText().toString();

                String resultado;

                resultado = crud.insertData(NomeString, SenhaString);

                Toast.makeText(getApplicationContext(), resultado, Toast.LENGTH_LONG).show();

                if (Objects.equals(resultado, "Usuário cadastrado")) {
                    Intent i;
                    i = new Intent(LoginActivity.this, DevicesActivity.class);
                    i.putExtra("nome", NomeString);
                    i.putExtra("senha", SenhaString);
                    startActivity(i);
                }

            }
        });



        botaoLimpa.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onClick(View view) {
                DataBaseController crud = new DataBaseController(getBaseContext());

                String NomeString = textoLogin.getText().toString();
                String SenhaString = textoSenha.getText().toString();

                String resultado =crud.deletaRegistro(NomeString, SenhaString);

                if (Objects.equals(resultado, "Conta Excluída")){
                    Toast.makeText(getApplicationContext(),"Conta excluída", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(getApplicationContext(),"Conta não deletada", Toast.LENGTH_LONG).show();
                }


            }
        });

        botaoLogin.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("NewApi")
            @Override
            public void onClick(View view) {

                DataBaseController crud = new DataBaseController(getBaseContext());

                String NomeString = textoLogin.getText().toString();
                String SenhaString = textoSenha.getText().toString();
                String resultado;

                resultado = crud.autenticationLogin(NomeString, SenhaString);

                if (Objects.equals(resultado, "autenticado")) {
                    Intent i;
                    i = new Intent(LoginActivity.this, DevicesActivity.class);
                    i.putExtra("nome", NomeString);
                    i.putExtra("senha", SenhaString);
                    startActivity(i);
                } else {
                    Toast.makeText(getApplicationContext(), resultado, Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}