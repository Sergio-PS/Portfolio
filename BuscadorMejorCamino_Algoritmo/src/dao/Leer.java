package dao;

import main.Main;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class Leer {
    private File ficheroLectura;
    private FileReader lector;
    private BufferedReader lectorLinea;

    public Leer(String ruta){
        try{
            this.ficheroLectura = new File(ruta);
            this.lector = new FileReader(this.ficheroLectura);
            this.lectorLinea = new BufferedReader(this.lector);
        }catch (IOException e){
            System.out.println("Error al cargar documentos: " + e.getMessage());
            new Main();
        }
    }

    public int leerCaracter(){
        try{
            return this.lector.read();
        }catch(Exception e){
            System.out.println("Error: " + e.getMessage());
            return -1;
        }
    }
    public String leerLinea(){
        try{
            return this.lectorLinea.readLine();
        }catch(Exception e){
            System.out.println("Error: " + e.getMessage());
            return null;
        }
    }
    public void cerrarReader(){
        try{
            this.lector.close();
            this.lectorLinea.close();
        }catch(Exception e){
            System.out.println("Error: " + e.getMessage());
        }

    }
}
