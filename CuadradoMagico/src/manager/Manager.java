package manager;

import modelo.Position;
import modelo.Square;

public class Manager {
    public Manager(){
        init();
    }

    private void init(){
        //Creara cuadrados magicos progresivamente empezando por el n = 3 y acabando en n = 15.
        //Sin embargo, como se ha hecho el proyecto con recursividad debemos iniciar n = 1
        int n = 1;
        createAllSquares(n);
    }
    private void createAllSquares(int n){
        if(n < 15){
            createSquare(n + 2);
            System.out.println();
            createAllSquares(n + 2);
        }
    }
    private void createSquare(int n){
        Square square = new Square(n, n);
        int valorInicial = 1;
        setInitialSquare(square, valorInicial);
        fillPositions(square, ++valorInicial, square.getInitialPosition());
        square.printMatrix(0, 0);
    }
    private void setInitialSquare(Square square, int valorInicial){
        square.getInitialPosition().setValue(valorInicial);
    }
    private void fillPositions(Square square, int sequence, Position position){
        //if(sequence >= square.getSize()) return;
        if(sequence <= square.getSize()){
            position = fillNextPosition(square, sequence, position);
            fillPositions(square, ++sequence, position);
        }
    }
    private Position fillNextPosition(Square square, int sequence, Position position){
        Position positionToChange = checkNewPosition(square, position);
        positionToChange.setValue(sequence);
        return positionToChange;
    }
    private Position checkNewPosition(Square square, Position prevPosition){
        /*Debemos copiar la posicion inicial sin cambios ya que el patron dice que si la casilla final (con cambios)
        esta ocupada entonces la resultante debera ser una debajo de la inicial*/
        /* SImplemente igualaremos la posicion inicial a positionNoChanges. Esto solo es posible si vamos pivotando entre
        posiciones de la matriz un no creamos nuevas. El objetivo: ir moviendose entre posiciones para que prevPosition
        acabe siendo la final y ademas pertenezca a la matriz.
         */
        Position[][] matrix = square.getMatrix();
        Position positionNoChanges = matrix[prevPosition.getY()][prevPosition.getX()];
        /*Recorrido: uno hacia arriba y uno a la derecha.
        Por lo tanto: X = x + 1 && Y = y - 1
        Si Y o X se va de los rangos, han de ir al inicio de su fila o columna respectivamente
        Es decir:
            si Y < 0 entonces Y = n - 1
            si X > n - 1 entonces X = n - n = 0

        Importante que la determinacion de Y se ejecute antes que X. Ya que para el procedimiento secuencial
        es imprescindible utilizar este orden
        Si la posicion tiene valor (es decir: que != null) entonces debemos anadir el valor respectivo a la posicion
        debajo de la previa.
        Si este procedimiento se cumple. No deberia haber ninguna anomalia
         */
        //realizas cambios basicos del patron
        /*Porque se decide hacer setters aqui y esto quiere decir a que creamos posiciones que sean iguales
        a las del cuadrado o completamente diferentes. Si igualamos la prevPosition a una posicion de la matriz
        se puede dar el caso de que esa posicion sea completamente diferente y entonces no exista en el cuadrado y pete.
        El hecho de pivotar solo se ve en checkRangesPosition: funcion por la que la posicion creada es igualada a una
        posicion del cuadrado valida y la otra se elimine mediante el vertedero de Java tan famoso
         */
        Position positionChanges = new Position(prevPosition.getY(), prevPosition.getX());
        positionChanges.setY(prevPosition.getY() - 1);
        positionChanges.setX(prevPosition.getX() + 1);
        positionChanges.setValue(prevPosition.getValue());
        //compruebas que esos cambios sean correctos y los igualas a la variable ya que en la condicion mas abajo se necesita este retorno
        prevPosition = checkRangesPosition(square, positionChanges);
        //Compruebas si esta vacio o no
        if(prevPosition.getValue() == null) return prevPosition;
            //Si no esta vacio. Quiere decir que la nueva posicion = a la previa posicion - 1 en el eje Y
            //Entonces debes hacer los cambios pero a la vez tambien comprobar si se va de los rangos, si no lo esta: se cambia automaticamente
        else{
            positionNoChanges.setY(positionNoChanges.getY() + 1);
            return checkRangesPosition(square, positionNoChanges);
        }
        /*Muchos se pensarian que que pasaria si el segundo cambio de rangos de la funcion: checkRangesPosition tambien esta ocupada
            Entonces deberia haber una recursividad en checkNewPosition. Sin embargo este patron en 3 <= n => 15 no produce esas casuisticas
         */
    }
    private Position checkRangesPosition(Square square, Position prevPosition){
        /*
        En esta funcion deberemos hacer simples if's para comprobar que la posicion cumpla los rangos del cuadrado
        Para resolver este problema deberemos acudir a la ayuda de una array o ArrayList. ¿Porque?
        Muchas veces la programacion secuencial no funciona. Especialmente si trabajamos con objetos de 2 o más dimensiones.
        Utilizando una programacion secuencial encontramos que primero hacemos un cambio y luego otro y asi sucesivamente.
        Pero se puede dar el caso de que no solo Y no este entre los rangos sino que ademas X tampoco lo esta.
        Pues bien, si utilizamos una programacion secuencial partimos de la base de que uno es correcto (esta entre los rangos)
        porque sino el programa peta: Index out of bounds.

        Para esto entonces creamos una arraylist que podamos modificar sus contenidos por posiciones de la matriz.
        --- Pivotamos en la misma array o arraylist en relacion a la cuadrado ---
        Esto se entienda a continuacion:
         */
        //Definimos array o Arraylist. En este caso prefiero una array ya que solo trabajamos con 2 dimensiones: X e Y
        //Las modificaciones de X estarán en la primera posicion de la array. Las de Y en la segunda
        Position[][] matrix = square.getMatrix();
        int[] arrayResultadosFinal = {prevPosition.getY(), prevPosition.getX()}; //prevPosition es una referencia a una posicion del cuadrado
        //Definimos longitudes maximas:
        int maxLengthYMatrix = square.getMatrix()[square.getMatrix().length - 1].length - 1;
        int maxLengthXMatrix = square.getMatrix().length - 1;
        //Deberian ser iguales ya que es un cuadrado
        //Compruebas los cambios de Y (eje vertical) Se comprueban dos condiciones:
        if(prevPosition.getY() < 0) arrayResultadosFinal[0] = maxLengthYMatrix;
        if(prevPosition.getY() > square.getMatrix().length - 1) arrayResultadosFinal[0] = 0;
        //Compruebas los cambie de X (eje horizontal)
        if(prevPosition.getX() < 0) arrayResultadosFinal[1] = maxLengthXMatrix;
        if(prevPosition.getX() > square.getMatrix().length - 1) arrayResultadosFinal[1] = 0;
        /*
        Vease como se cambian los valores si las condiciones se cumplen: Llamese dinamico o algo parecido
        Ya que por cada condicion que se cumple se hara referencia a otra posicion del cuadrado.
        No es secuencial ya que al final se define la posicion correcta y no durante el procedimiento.
        Mas aun, en este caso: La posicion correcta es dependiente a el procedimiento a partir de condiciones
        Y se puede definir esta "posicion correcta" como arbitraria hasta que no se conoce el return.
         */
        Position positionReturn = matrix[arrayResultadosFinal[0]][arrayResultadosFinal[1]];
        return positionReturn;
        //Importante:
        //He decidido no meter las comprobabciones en una funcion aparte (que se podria y se deberia: SOLID)
        //Meramente para que se entienda de principio a fin el hecho de pivotar sobre posiciones
        //Ademas de la importancia de definir clases y la porgramacion orientada a objetos para optimizar codigo
    }
}
