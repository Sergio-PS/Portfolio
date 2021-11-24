package dao;

import main.Main;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;

public class Escribir {
    private File ficheroEscritura;
    private FileWriter escritor;
    private BufferedWriter escritorLineas;

    public Escribir(String ruta){
        try{
            this.ficheroEscritura = new File(ruta);
            this.escritor = new FileWriter(this.ficheroEscritura, true);
            this.escritorLineas = new BufferedWriter(this.escritor);
        }catch(Exception e){
            System.out.println("Error al cargar documentos: " + e.getMessage());
            //new Main();
        }
    }
    public void escribir(String texto){
        try{
            this.escritor.write(texto);
            //this.escritorLineas.newLine();  -Nose porque no funciona newLine() --> javadoc: Deberia crear una nueva linea mediante BufferedWriter
            this.escritor.flush();
        }catch(Exception e){
            System.out.println("Error: " + e.getMessage());
        }
    }
    public void insertarLinea(){
        try{
            this.escritor.write(System.lineSeparator());
            this.escritor.flush();
        }catch(Exception e){
            System.out.println("Error: " + e.getMessage());
        }
    }
    public void cerrarWriter(){
        try{
            this.escritor.close();
            this.escritorLineas.close();
        }catch (Exception e){
            System.out.println("Error: " + e.getMessage());
        }
    }
}
