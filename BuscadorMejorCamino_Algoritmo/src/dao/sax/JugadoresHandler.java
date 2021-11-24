package dao.sax;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;
import modelo.jugadores.Jugador;

import java.util.ArrayList;

public class JugadoresHandler extends DefaultHandler{
    private ArrayList<Jugador> jugadores;
    private ArrayList<String> jugadoresBuilder;
    private boolean condicion;

    public JugadoresHandler(){
        this.jugadores = new ArrayList<Jugador>();
        this.jugadoresBuilder = new ArrayList<String>();
        this.condicion = false;
    }
    private void changeCondition(){
        this.condicion = !this.condicion;
    }

    @Override
    public void characters(char[] ch, int start, int length) throws SAXException {
        if(condicion){
            jugadoresBuilder.add(String.valueOf(ch, start, length));
            changeCondition();
        }
    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        switch (qName){
            case "partida":
                jugadoresBuilder.add(attributes.getValue("jugador"));
                break;
            case "pantalla":
                jugadoresBuilder.add(attributes.getValue("estado"));
                changeCondition();
                break;
            case "puntuacion":
                changeCondition();
                break;
            default: break;
        }
    }
    @Override
    public void endDocument() throws SAXException {
        for(int i = 0; i < jugadoresBuilder.size(); i += 4){
            jugadores.add(new Jugador(jugadoresBuilder.get(i),
                    Integer.parseInt(jugadoresBuilder.get(i + 1)),
                    jugadoresBuilder.get(i + 2),
                    Integer.parseInt(jugadoresBuilder.get(i + 3).substring(1))));
        }

        super.endDocument();
    }

    public ArrayList<Jugador> getJugadores(){
        return this.jugadores;
    }
}
