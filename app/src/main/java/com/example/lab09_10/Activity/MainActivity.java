package com.example.lab09_10.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.example.lab09_10.Data.DBAdapterSQL;
import com.example.lab09_10.R;

public class MainActivity extends AppCompatActivity {
    DBAdapterSQL db;
    private static final String TAG = "com.example.lab09_10";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        /*String ejercicio = "admin";
        String dia ="admin";
        String tiempo = "admin";*/
         db =  DBAdapterSQL.getInstance(this);
        /*String orden = "INSERT INTO Usuario(usuario,password,rol) VALUES ('" +  ejercicio +"','"+dia+"','"+tiempo+"');";
         db.execSQL(orden);*/
         db.insertarUsuario(null);
        db.close();
    }
}
