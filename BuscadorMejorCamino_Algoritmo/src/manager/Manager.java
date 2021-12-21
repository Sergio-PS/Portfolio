package manager;

import dao.dom.DomGenerator;
import libraries.InputMapCreator;
import modelo.jugadores.Jugador;
import modelo.pantallas.Pantalla;
import modelo.pantallas.positions.Position;
import utils.Utils;
import java.util.*;

/*
    Creador:  Sergio Polo
 */

public class Manager{
    private static Manager manager;
    private ArrayList<Jugador> jugadores;
    private ArrayList<Pantalla> pantallas;

    private ArrayList<ArrayList<Position>> bufferedPathPosibilities;

    private ArrayList<ArrayList<ArrayList<ArrayList<Position>>>> solucionesMapas;
    private ArrayList<ArrayList<ArrayList<ArrayList<Position>>>> solucionesMapasMasRapidas;

    public static Manager newInstance(){
        return manager == null ? new Manager() : manager;
    }
    private Manager(){

        try{
            this.bufferedPathPosibilities = new ArrayList<>();
            this.solucionesMapasMasRapidas = new ArrayList<>();
            this.pantallas = Utils.processPantallas("pantallas.txt"); //Se ponen en util por si se quieren utilizar en otro sitio
            this.jugadores = Utils.readEntradaSAX("entrada.xml");
            this.solucionesMapas = new ArrayList<>();
        }catch(Exception e){
            System.out.println(e.getMessage());
        }
    }
    /*
    Importante:
    Este programa se ha creado con el principal objetivo de desarrollar un algoritmo recursivo y automatizado
    para buscar caminos. Para este proyecto, se ha obviado los principios SOLID y patrones de diseño que podrían
    hacer el proyecto más funcional, esta fase se desarrollara dentro de poco.
    El objetivo es concentrarse en el desarrollo matemático y automatizado del algoritmo.

    Para este programa automatizado, se van conservando los caminos generados en vez de eliminar los que no son validos,
    por si acaso se necesitan para otra funcionalidad.

    Disculpa entre la mezcla de ingles + español.
     */

    /*
    El objetivo de este algoritmo es encontrar el camino mas rapido entre 2 posiciones de una matriz.
    Para simplificar el trabajo, utilizaremos conexiones entre posiciones para crear caminos:
    Si existe una conexion de posicion Z con posicion W quiere decir que al menos estan en 1 eje igual (x, y)
    Si P(p1, p2) = V donde p1(x, y) y p2(z, w) quiere decir que ∃x∃y((x = z) ∨ (y = w)) donde Dominio = numeros naturales.
    Resumiendo que si un punto esta detras o delante de otro respecto al eje X o el eje Y, entonces esta conectado: P(p1, p2) --> Q(p1, p2)
    No se puede mover en diagonal.

    Para mejorar el algoritmo, este, funciona a partir de dos puntos cualquieras que avanza si no tiene obstaculo ( . ) o ( X )
    asi pues, si es imprescindible pasar por la o para llegar a la s. (XOS) = (XO) + (OS) - (1 * (O))
    donde (XO) es el camino de posiciones entre e incluidas la posicion X y la posicion O
    y (O) es la posicion O.

    Explicacion del algoritmo:
    Este algoritmo se divide en tres fases (funciones) principales:
    1- A partir de una posicion, obtener las posiciones validas a las que puede ir teniendo en cuenta de donde ha venido --> getConexiones()
    2- A partir de una posicion inicial y una posicion final. Obtener el camino pivotando entre las posiciones y todas sus posibilidades --> findPath()
        Esta funcion es recursiva y dinamica: Es decir, tod0 su resultado obtenido lo almacena en una arraylist y pasa al siguiente.
        Funcionamiento:
            -Empieza en la posicion incial y consigue las conexiones posibles. Entonces crea una arraylist e inserta
            una posicion posible. La funcion se vuelve a llamar con la nueva arraylist y vuelve a checkear conexiones.
            -Cuando no hay mas conexiones, entonces se checkea que la propia arraylist que ha ido construyendo a partir de otras
            contenga en su ultima posicion la Posicion final. Si no la contiene, se elimina la arraylist. Como la funcion
            es recursiva. Ira y contruira el camino y despues se ejecutara el codigo restante al llamarse la funcion,
            en este caso la comprobacion y posterior eliminacion.

            Una parte muy importante del algoritmo findPath() es que se utiliza "arraylist.size() - 1" para hacer referencia
            al camino que ha creado y el cual la funcion recursiva tiene.

    3- Obtenemos las soluciones mas rapidas a partir de todas las posibles generadas --> devolverSolucionesMasRapidas()
           Funcionamiento:
               Obtenidas todas los caminos validos posibles entre dos posiciones, las almacenamos en una arraylist que
               contiene arraylist que contiene arraylist... Es un simple metodo de organizacion de datos.
               Generamos una copia (una copia con elementos new. No podemos hacer referencia a otros valores ya que
               las funciones que implementa una lista eliminan sus componentes e.g: clear() o remove())
               esa copia la procesamos guardando las posiciones que son las mas rapidas/optimas (menos longitud)
               y obtenemos los caminos con mas rapidos para todos los mapas. Procesamos esos caminos a un fichero DOM
               para generar el xml.

    Condicion:
    Una vez cogido un punto de registro. El usuario tiene derecho a pasar por casillas que ya ha pasado para coger
    el registro. Ya que sino las limitaciones de los creadores de mapas se verian muy limitadas y sus mapas
    serian muy faciles....

    Sergio Polo
    */

    public void init(){
            /*
            Buscamos las soluciones posibles y las almacenamos en una arraylist global this.solucionesMapas
             */
           for(Pantalla pantalla : this.pantallas){
               //sacamos las todas las soluciones posibles de todos los tramos posibles
               ArrayList<ArrayList<ArrayList<Position>>> solucionesPorPantalla = calcularSolucionesPorTramos(InputMapCreator.posicionesObligatorias, pantalla);

               this.solucionesMapas.add(solucionesPorPantalla);

           }

           //Obtenemos las soluciones mas rapidas, sin eliminar todas las soluciones**  solucionesRapidas = new ArrayList<>(solucionesTotales);


           //Generamos la salida en un fichero xml
           try{
               this.solucionesMapasMasRapidas = devolverSolucionesMasRapidas(this.solucionesMapas);
               generarSalidaDOM();
           }catch (Exception e){
               System.out.println(e.getMessage());
           }
    }
    //in
    private ArrayList<ArrayList<ArrayList<Position>>> calcularSolucionesPorTramos(String[] posicionesObligatorias, Pantalla pantalla){
        ArrayList<ArrayList<ArrayList<Position>>> solucionesPorTramos = new ArrayList<>();
        for(int i = 0; i < posicionesObligatorias.length - 1; i++){
            /*ArrayList<ArrayList<Position>> solucionesTramo = new ArrayList<>(
                    Arrays.asList(new ArrayList<Position>(
                            Arrays.asList(pantalla.getPositionFromValue(posicionesObligatorias[i])))));
            */
            //Position positionToReference = pantalla.getPositionFromValue(posicionesObligatorias[i]);
            Position start = pantalla.getPositionFromValue(posicionesObligatorias[i]);
            Position finish = pantalla.getPositionFromValue(posicionesObligatorias[i + 1]);

            //path(solucionesTramo.get(0), pantalla, positionToReference, start, end, solucionesTramo);
            //al hacer un clear, significa que elimina todos los elementos.
            /*
            Si los referenciamos en otro sitio, este elemento seguira existiendo sin embargo, se pondra vacio ya que se ha
            hecho un clear en la arraylist principal de donde venia. Java
             */
            this.bufferedPathPosibilities.clear();
            this.bufferedPathPosibilities.add(new ArrayList<Position>(Arrays.asList(start)));

            findPath(start, finish, pantalla);

            solucionesPorTramos.add(new ArrayList<>(this.bufferedPathPosibilities)); //De aqui que debemos hacer una copia, sino se elimina su contenido al hacer clear mas arriba
        }

        return solucionesPorTramos;
    }

        //in        //funcion recursiva
    private void findPath(Position changingPosition, Position finish, Pantalla matrix){
        //changingPosition = startingPosition in the first round
        ArrayList<Position> elementByIndex = this.bufferedPathPosibilities.get(this.bufferedPathPosibilities.size() - 1);

        ArrayList<Position> conexiones = getConexiones(elementByIndex, changingPosition, matrix, finish);

        for(Position positionThatChanges : conexiones){
            ArrayList<Position> newElement = new ArrayList<Position>(elementByIndex);
            newElement.add(positionThatChanges);
            this.bufferedPathPosibilities.add(newElement);
             findPath(positionThatChanges, finish, matrix);
        }
        if(!(elementByIndex.get(elementByIndex.size() - 1).equals(finish))) {
            this.bufferedPathPosibilities.remove(elementByIndex);
        }
    }

            //in
    private ArrayList<Position> getConexiones(ArrayList<Position> listToCheck, Position positionToReference, Pantalla matrix, Position finish){
        ArrayList<Position> conexiones = new ArrayList<>();

        if(!positionToReference.equals(finish)){
            int positionInMatrixY = positionToReference.getY(), positionInMatrixX = positionToReference.getX(); //Para localizar la posicion en la matriz
            Position positionToAdd;
            //Bucle que determina las posiciones que conectan que estan disponibles para caminar: contienen un "_"
            for(int i = 0, y = 1, x = 0; i < 2; i++, y = -1, x = 0) {
                for (int j = 0, base = y; j < 2; j++, y = x, x = base) {
                    try {
                        if (!((positionToAdd = matrix.getPantalla()[positionInMatrixY + y][positionInMatrixX + x]).toString().equals("."))
                                && !listToCheck.contains(positionToAdd)) conexiones.add(positionToAdd);
                    } catch (Exception e) {

                    }
                }
            }
        }

        return conexiones;
    }
            //out
        //out

    private ArrayList<ArrayList<ArrayList<ArrayList<Position>>>> devolverSolucionesMasRapidas(ArrayList<ArrayList<ArrayList<ArrayList<Position>>>> solucionesMapas) throws CloneNotSupportedException{
        ArrayList<ArrayList<ArrayList<ArrayList<Position>>>> solucionesMasRapidasTodosMapas =  new ArrayList<>();
        /*
        hacemos una copia de sus elementos pero creando nuevos ya que sino simplemente estamos apuntando a ellos y un cambio que hagamos
        afectara a la arraylist que los almacenaba inicialmente.
         */
        for(ArrayList<ArrayList<ArrayList<Position>>> mapa : solucionesMapas) {
            ArrayList<ArrayList<ArrayList<Position>>> nuevoMapa = new ArrayList<>();
            for (ArrayList<ArrayList<Position>> solucionesHastaPuntoControl : mapa) {
                ArrayList<ArrayList<Position>> nuevaSolucion = new ArrayList<>();
                for(ArrayList<Position> camino : solucionesHastaPuntoControl){
                    ArrayList<Position> nuevoCamino = new ArrayList<>();
                    for(Position position : camino){
                        nuevoCamino.add(new Position(position.getY(), position.getX(), position.toString()));
                    }
                    nuevaSolucion.add(nuevoCamino);
                }
                nuevoMapa.add(nuevaSolucion);
            }
            solucionesMasRapidasTodosMapas.add(nuevoMapa);
        }

        for(ArrayList<ArrayList<ArrayList<Position>>> mapa : solucionesMasRapidasTodosMapas){
            for(ArrayList<ArrayList<Position>> solucionesHastaPuntoControl : mapa){
                if(!solucionesHastaPuntoControl.isEmpty()){
                    //Saca el camino con menor longitud. En este caso, lo tenemos como int para que se entienda mejor.
                    int minLength = solucionesHastaPuntoControl.get(0).size();
                    for(ArrayList<Position> solucionNoProcesada : solucionesHastaPuntoControl){
                        if(solucionNoProcesada.size() < minLength) minLength = solucionNoProcesada.size();
                    }
                    //Sacas los caminos que son mas grandes que la minima longitud. Pueden haber dos o mas caminos con la minima longitud.
                    for(int i = 0; i < solucionesHastaPuntoControl.size(); i++){
                        if(solucionesHastaPuntoControl.get(i).size() != minLength){
                            solucionesHastaPuntoControl.remove(solucionesHastaPuntoControl.get(i));
                            i--;
                        }
                    }
                }
            }
        }

        return solucionesMasRapidasTodosMapas;
    }
    //out

    private void generarSalidaDOM() throws Exception {
        DomGenerator domGenerator = DomGenerator.newInstance();
        domGenerator.generateDocument(this.solucionesMapasMasRapidas, this.jugadores);
        domGenerator.generateXML("salidaResult.xml");
    }



}
