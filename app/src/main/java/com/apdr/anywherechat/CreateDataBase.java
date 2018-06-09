package com.apdr.anywherechat;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class CreateDataBase extends SQLiteOpenHelper{

     static final String NOME_BANCO = "banco.db";
     static final String TABELA = "usuarios";
     static final String TABELAMENSAGEM = "mensagens";
     static final String ID = "_id";
     static final String NOME = "nome";
     static final String SENHA = "senha";
     static final String STATUS = "status";
     static final String MENSAGEM = "mensagem";
     private static final int VERSAO = 1;

    CreateDataBase(Context context) {
        super(context, NOME_BANCO, null, VERSAO);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        String sql = "CREATE TABLE "+TABELA+"("
                +ID+ " integer primary key autoincrement, "
                +NOME+" text, "
                +SENHA+" text"
                +")";
        db.execSQL(sql);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS "+ TABELA);
        onCreate(db);
    }


}
