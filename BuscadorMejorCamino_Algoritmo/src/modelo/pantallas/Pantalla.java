package modelo.pantallas;

import modelo.pantallas.positions.Position;

import java.util.ArrayList;
import java.util.Arrays;

public class Pantalla {
    private Position[][] pantalla;
    private String[][] pantallaValues;


    public Pantalla(ArrayList<String[]> values){
        int y = values.size();
        int x = values.get(0).length;
        //Se considera que el tamaño las filas de la matriz no son variables
        this.pantalla = new Position[y][x];
        this.pantallaValues = new String[y][x];

        for(int i = 0; i < y; i++) for(int j = 0; j < x; j++) this.pantalla[i][j] = new Position(i, j, values.get(i)[j]);
        for(int i = 0; i < y; i++) System.arraycopy(values.get(i), 0, this.pantallaValues[i], 0, x);
    }

    public Position[][] getPantalla(){
        return this.pantalla;
    }

    public Position getPositionFromValue(String value){
        for(int i = 0; i < pantalla.length; i++){
            for(int j = 0; j < pantalla[i].length; j++){
                if(pantalla[i][j].toString().equals(value)) return pantalla[i][j];
            }
        }
        return null;
    }

    @Override
    public String toString(){
        StringBuilder retornoPantallaString = new StringBuilder();
        for (String[] pantallaValue : pantallaValues) {
            for (String rutaFichero : pantallaValue) retornoPantallaString.append(rutaFichero).append(" ");
            retornoPantallaString.append("\n");
        }
        return retornoPantallaString.toString();
    }
}
