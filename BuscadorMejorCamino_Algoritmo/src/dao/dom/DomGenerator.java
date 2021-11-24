package dao.dom;

import manager.Manager;
import modelo.jugadores.Jugador;
import modelo.pantallas.Pantalla;
import modelo.pantallas.positions.Position;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import utils.Utils;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.ArrayList;

public class DomGenerator {
    private static DomGenerator domGenerator;
    private Document document;

    public static DomGenerator newInstance(){
        if(DomGenerator.domGenerator == null){
            return new DomGenerator();
        }
        return domGenerator;
    }

    private DomGenerator(){
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newDefaultInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            this.document = builder.newDocument();
        } catch (ParserConfigurationException e) {
            System.out.println("ERROR generating doucment");
        }
    }

    public void generateDocument(ArrayList<ArrayList<ArrayList<ArrayList<Position>>>> solucionesMasRapidas, ArrayList<Jugador> jugadores){
        Element pantallasNodo = document.createElement("pantallas");
        document.appendChild(pantallasNodo);

        for(Jugador jugador : jugadores){
            int pantallaActualJugador = jugador.getPantallaActual();
            int pantallaAResolver = (jugador.getCompletado()) ? pantallaActualJugador + 1 : pantallaActualJugador;
            /*Esto se debe a que quien tiene completado el nivel no ha empezado el siguiente ya que sino estaria pendiente el siguiente.
            Por esta razon quien tiene completado su nivel, la pantallaAResolver es la siguiente.
            Pero quien esta pendiente en un nivel, esta en ese nivel por lo intenta completar y la pantallaAResolver es la misma
            porque no lo ha completado todavia.
             */


            Element pantalla = document.createElement("pantalla");
            pantalla.setAttribute("jugador", jugador.getNombre());
            pantalla.setAttribute("nivel_actual", String.valueOf(pantallaActualJugador));
            pantalla.setAttribute("pantalla_solucion", String.valueOf(pantallaAResolver));
            pantallasNodo.appendChild(pantalla);

            Element primerCamino = document.createElement("primer_camino");
            primerCamino.setAttribute("origen", "X");
            primerCamino.setAttribute("destino", "o");
            pantalla.appendChild(primerCamino);

            ArrayList<ArrayList<Position>> solucionesParte1 = solucionesMasRapidas.get(pantallaActualJugador - 1).get(0);
            int puntuacion1;
            if(solucionesParte1.size() != 0){
                puntuacion1 = Utils.createCaminoDOM(document, solucionesParte1, primerCamino);
            }else{
                puntuacion1 = 0;
                Element solucionIncorrecta = document.createElement("solucion_incorrecta");
                solucionIncorrecta.setTextContent("No tiene solucion");
                primerCamino.appendChild(solucionIncorrecta);
            }

            Element segundoCamino = document.createElement("segundoCamino");
            segundoCamino.setAttribute("origen", "o");
            segundoCamino.setAttribute("destino", "S");
            pantalla.appendChild(segundoCamino);

            ArrayList<ArrayList<Position>> solucionesParte2 = solucionesMasRapidas.get(pantallaActualJugador - 1).get(1);
            int puntuacion2;
            if(solucionesParte2.size() != 0){
                puntuacion2 = Utils.createCaminoDOM(document, solucionesParte2, segundoCamino);
            }else{
                puntuacion2 = 0;
                Element solucionIncorrecta = document.createElement("solucion_incorrecta");
                solucionIncorrecta.setTextContent("No tiene solucion");
                segundoCamino.appendChild(solucionIncorrecta);
            }

            Element puntuacionSolucion = document.createElement("puntuacion_solucion");
            if(puntuacion1 == 0 || puntuacion2 == 0){
                puntuacionSolucion.setTextContent(String.valueOf(0));
            }else{
                puntuacionSolucion.setTextContent(String.valueOf((puntuacion1 + puntuacion2) - 1)); // ya que se repite la o
            }
            pantalla.appendChild(puntuacionSolucion);

        }
    }

    public void generateXML(String rutaFichero) throws Exception{
        TransformerFactory factory = TransformerFactory.newInstance();
        Transformer transformer = factory.newTransformer();

        Source source = new DOMSource(document);
        File file = new File(rutaFichero);
        FileWriter fw = new FileWriter(file);
        PrintWriter pw = new PrintWriter(fw);
        Result result = new StreamResult(pw);

        transformer.transform(source, result);

    }
}
