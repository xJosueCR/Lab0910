package com.example.lab09_10.Data;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.example.lab09_10.Model.Curso;
import com.example.lab09_10.Model.Estudiante;
import com.example.lab09_10.Model.Usuario;

import java.util.ArrayList;
import java.util.List;

public class DBAdapterSQL extends SQLiteOpenHelper{
    private static final String TAG = "com.example.lab09_10.Data";
    private static DBAdapterSQL mInstance = null;
    static final String DATABASE_NAME = "Lab09102.db";
    static final String DATABASE_TABLE_USER = "Usuario";
    static final String DATABASE_TABLE_ESTUDIANTE = "Estudiante";
    static final String DATABASE_TABLE_CURSO = "Curso";
    static final String DATABASE_TABLE_CURSOSESTUDIANTE= "CursosEstudiante";
    static final int DATABASE_VERSION = 1;

    static final String userTable = "create table "+DATABASE_TABLE_USER+"(id integer primary key autoincrement " +
                                     ", username text not null, password text not null, rol text not null)";
    static final String estudianteTable = "create table "+DATABASE_TABLE_ESTUDIANTE+
                                          "(id integer primary key autoincrement, cedula text not null,  nombre text not null," +
                                            "apellidos text not null, edad integer not null, user integer not null," +
                                            "foreign key(user) references Usuario(id))";
    static final String cursoTable = "create table "+DATABASE_TABLE_CURSO+"(id  integer primary key autoincrement,"+
                                        "descripcion text not null, creditos integer not null)";
    static final String cursosEstudiante = "create table "+DATABASE_TABLE_CURSOSESTUDIANTE+" (idEstudiante integer not null," +
            "idCurso integer not null, foreign key(idEstudiante) references Estudiante(id), foreign key(idCurso) references Curso(id))";
    private Context mCtx;
    SQLiteDatabase db;
    public DBAdapterSQL(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        db  = this.getWritableDatabase();
    }

    public static DBAdapterSQL getInstance(Context ctx) {
        if (mInstance == null) {
            mInstance = new DBAdapterSQL(ctx.getApplicationContext());
        }
        return mInstance;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        /*String ejercicio = "admin";
        String dia ="admin";
        String tiempo = "admin";
        String orden = "INSERT INTO Usuario(usuario,password,rol) VALUES ('" +  ejercicio +"','"+dia+"','"+tiempo+"');";*/
        try {
            db.execSQL(userTable);
            db.execSQL(estudianteTable);
            db.execSQL(cursoTable);
            db.execSQL(cursosEstudiante);
            //db.execSQL(orden);
           // db.execSQL(orden);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+DATABASE_TABLE_USER);
        onCreate(db);
    }
    public DBAdapterSQL open() throws SQLException
    {
        this.getWritableDatabase();
        return this;
    }
    public void close()
    {
       db.close();
    }
    public void insertarUsuario(Usuario user){
        db.beginTransaction();

        try{
            String orden = "INSERT INTO " + DATABASE_TABLE_USER + " (username,password,rol) VALUES ('" +  user.getUsuario() +"','"+user.getPassword()+"','"+user.getRol()+"');";
            db.execSQL(orden);
            db.setTransactionSuccessful();
        }catch (Exception ex){
            ex.printStackTrace();
        }
        finally {
            db.endTransaction();
            db.close();
        }
    }
    public Usuario getUsuario(String username, String password){
       //db.beginTransaction();
        Usuario user = new Usuario();
        Cursor c = null;
        try{
            String[] args = new String[]{username, password};
            c = db.rawQuery("select * from usuario where username =? and password = ?", args);
            if(c.moveToFirst()){
                user.setId(c.getInt(0));
                user.setUsuario(c.getString(1));
                user.setPassword(c.getString(2));
                user.setRol(c.getString(3));
                return user;
            }
        }catch (Exception ex){
            ex.printStackTrace();
        }
        finally {
            if (c != null) {
                c.close();
            }
        }
        return null;
    }
    public boolean insertarEstudiante(Estudiante est){
        db.beginTransaction();
        try{
            String orden = "INSERT INTO " + DATABASE_TABLE_ESTUDIANTE + " (cedula,nombre,apellidos, edad, user) VALUES ('" +  est.getCedula()
                    +"','"+est.getNombre()+"','"+est.getApellidos()+"','"+est.getEdad()+"','"+est.getUser()+"');";
            db.execSQL(orden);
            db.setTransactionSuccessful();
        }catch (Exception ex){
            ex.printStackTrace();
        }
        finally {
            db.endTransaction();
            db.close();
        }
        return false;
    }
    public List<Estudiante> listEstudiantes(){
        List<Estudiante> list = new ArrayList<>();
         db.beginTransaction();
        Estudiante estudiante = new Estudiante();
        try{
            String orden = "select * from estudiante";
            Cursor c = db.rawQuery(orden, null);
            while(c.moveToNext()){
                estudiante.setId(c.getInt(0));
                estudiante.setCedula(c.getString(1));
                estudiante.setNombre(c.getString(2));
                estudiante.setApellidos(c.getString(3));
                estudiante.setEdad(c.getInt(4));
                estudiante.setUser(c.getInt(5));
                list.add(estudiante);
            }
        }catch (Exception ex){
            ex.printStackTrace();
        }
        finally {
            db.endTransaction();
            db.close();
        }
        return list;
    }
    public void updateEstudiante(Estudiante est){
        db.beginTransaction();
        try{
            String orden = "update estudiante set cedula ='"+est.getCedula()+"','"+est.getNombre()+"','"+est.getApellidos()+
                    "','"+est.getEdad()+"' where id = "+est.getId()+")";
            db.execSQL(orden);
            db.setTransactionSuccessful();
        }catch (Exception ex){
            ex.printStackTrace();
        }
        finally {
            db.endTransaction();
            db.close();
        }
    }

    public boolean deleteEstudiante(int id){
        db.beginTransaction();
        try{
            String orden = "Delete from estudiante where id="+ id + ";";
            db.execSQL(orden);
            db.setTransactionSuccessful();
            return true;
        }catch (Exception ex){
            ex.printStackTrace();
            return false;
        }
        finally {
            db.endTransaction();
            db.close();
        }
    }
    public void insertarCurso(Curso c){
        db.beginTransaction();
        try{
            String orden = "INSERT INTO " + DATABASE_TABLE_CURSO + " (descripcion,creditos) VALUES ('" +  c.getDescripcion()
                    +"','"+c.getCreditos()+"');";
            db.execSQL(orden);
            db.setTransactionSuccessful();
        }catch (Exception ex){
            ex.printStackTrace();
        }
        finally {
            db.endTransaction();
            db.close();
        }
    }
    public List<Curso> listCurso(){
        List<Curso> list = new ArrayList<>();
        db.beginTransaction();
        Curso c = new Curso();
        try{
            String orden = "select * from curso";
            Cursor cursor = db.rawQuery(orden, null);
            while(cursor.moveToNext()){
                c.setId(cursor.getInt(0));
                c.setDescripcion(cursor.getString(1));
                c.setCreditos(cursor.getInt(2));
                list.add(c);
            }
        }catch (Exception ex){
            ex.printStackTrace();
        }
        finally {
            db.endTransaction();
            db.close();
        }
        return list;
    }
    public void updateCurso(Curso c){
        db.beginTransaction();
        try{
            String orden = "update curso set descripcion ='"+c.getDescripcion()+"','"+c.getCreditos()+"' where id = "+c.getId()+")";
            db.execSQL(orden);
            db.setTransactionSuccessful();
        }catch (Exception ex){
            ex.printStackTrace();
        }
        finally {
            db.endTransaction();
            db.close();
        }
    }
    public boolean deleteCurso(int id){
        db.beginTransaction();
        try{
            String orden = "Delete from curso where id="+ id + ";";
            db.execSQL(orden);
            db.setTransactionSuccessful();
            return true;
        }catch (Exception ex){
            ex.printStackTrace();
            return false;
        }
        finally {
            db.endTransaction();
            db.close();
        }
    }

}
