package utils;

import dao.Leer;
import dao.sax.JugadoresHandler;
import modelo.jugadores.Jugador;
import modelo.pantallas.Pantalla;
import modelo.pantallas.positions.Position;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class Utils {
    public static ArrayList<Pantalla> processPantallas(String rutaFicheroPantallas){
        ArrayList<Pantalla> pantallas = new ArrayList<>(); //valor retorno
        ArrayList<String[]> bufferPantalla = new ArrayList<>(); //buffer que acaba almacenando una matriz

        Leer lector = new Leer("pantallas.txt");
        String line;
        while((line = lector.leerLinea()) != null){
            if(!(line.charAt(0) == '#')) bufferPantalla.add(line.split(" ")); //caso que no sea #. Vas almacenando en buffer las lineas
            else if(bufferPantalla.size() > 0){ //caso que sea # y no sea la primera linea del fichero añades matriz (mapa) en pantallas y limpias buffer.
                pantallas.add(new Pantalla(bufferPantalla)); //considerando que la longitud de las filas no son variables
                bufferPantalla.clear();
            }
        }

        lector.cerrarReader();
        return pantallas;
    }


    public static ArrayList<Jugador> readEntradaSAX(String rutaFicheroEntrada) throws ParserConfigurationException, SAXException, IOException {
        SAXParserFactory saxParserFactory = SAXParserFactory.newInstance();
        SAXParser saxParser = saxParserFactory.newSAXParser();
        File file = new File("entrada.xml");
        JugadoresHandler jugadoresHandler = new JugadoresHandler();
        saxParser.parse(file, jugadoresHandler);

        return jugadoresHandler.getJugadores();
    }

    public static ArrayList<Position> copyAndAddArrayListPostition(ArrayList<Position> arrayToCopy, Position positionToAdd){
        ArrayList<Position> newArray = new ArrayList<>(arrayToCopy);
        newArray.add(positionToAdd);
        return newArray;
    }

    public static int createCaminoDOM(Document document, ArrayList<ArrayList<Position>> soluciones, Element elementToAppend){
        int puntuacion  = soluciones.get(0).size();
        for(int i = 0; i < soluciones.size(); i ++){
            Element posibleCamino = document.createElement("posible_solucion");
            posibleCamino.setAttribute("num", String.valueOf(i + 1));
            elementToAppend.appendChild(posibleCamino);

            for(Position position : soluciones.get(i)){
                Element pixel = document.createElement("pixel");
                pixel.setAttribute("col", String.valueOf(position.getX()));
                pixel.setAttribute("fil", String.valueOf(position.getY()));
                pixel.setTextContent(position.toString());
                posibleCamino.appendChild(pixel);
            }
        }

        return puntuacion;
    }
}
