package com.apdr.anywherechat;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.Objects;

public class DataBaseController {
     SQLiteDatabase db;
     CreateDataBase banco;

    public DataBaseController(Context context){
        banco = new CreateDataBase(context);
    }

    public String insertData(String nome, String senha){
        ContentValues valores;
        long resultado;
        db = banco.getWritableDatabase();
        valores = new ContentValues();
        valores.put(CreateDataBase.NOME, nome);
        valores.put(CreateDataBase.SENHA, senha);

        resultado = db.insert(CreateDataBase.TABELA, null, valores);
        db.close();

        if (resultado == -1)
            return "Erro ao inserir registro";
        else
            return "Usuário cadastrado";

    }

    @SuppressLint("NewApi")
    public String autenticationLogin(String nome, String senha){
        try {
            db = banco.getWritableDatabase();
            String sql = "SELECT * FROM " + CreateDataBase.TABELA + " WHERE nome = ? OR senha = ?";
            String[] selectionArgs = new String[] {nome, senha};
            Cursor cr;
            cr = db.rawQuery(sql, selectionArgs);
            cr.moveToFirst();
            String retornonome = cr.getString(cr.getColumnIndex("nome"));
            String retornosenha = cr.getString(cr.getColumnIndex("senha"));
            cr.close();
            if (Objects.equals(nome, retornonome) && Objects.equals(senha, retornosenha)){
                return "autenticado";
            } else {
                return "Dados incorretos, tente novamente";
            }
        }catch (Exception e){
            return "Não cadastrado";
        }

    }

    public String deletaRegistro(String nome, String senha){

        try {
            String where = "nome = ? AND senha = ?";
            String[] selectionArgs = new String[] {nome, senha};
            db = banco.getReadableDatabase();
            db.delete(CreateDataBase.TABELA,where, selectionArgs);
            db.close();
        } catch (Exception e){
            return "Não foi possível deletar";
        }

        return "Conta Excluída";
    }
}
