package modelo.jugadores;

public class Jugador {
    private String nombre;
    private int puntuacion;
    private boolean completado;
    private int pantallaActual;

    public Jugador(String nombre, int puntuacion, String estado, int pantallaActual){
        this.nombre = nombre;
        this.puntuacion = puntuacion;
        this.completado = estado.equalsIgnoreCase("completa");
        this.pantallaActual = pantallaActual;
        //Tener cuidado ya que a la hora de trabajar con pantallas de usuarios en la arraylist debemos utilizar pantallaActual - 1;
    }

    public String getNombre() {
        return this.nombre;
    }

    public int getPantallaActual(){
        return this.pantallaActual;
    }

    public int getPuntuacion() {
        return this.puntuacion;
    }

    public boolean getCompletado() {
        return this.completado;
    }

    public boolean isCompletado() {
        return this.completado;
    }

}
