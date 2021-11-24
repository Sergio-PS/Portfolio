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
        if(n > 15) return;
        createSquare(n + 2);
    }
    private void createSquare(int n){
        Square square = new Square(n, n);
        int valorInicial = 1;
        setInitialSquare(square, valorInicial);
        fillPositions(square, valorInicial);
        square.printMatrix(0, 0);
    }
    private void setInitialSquare(Square square, int valorInicial){
        square.getInitialPosition().setValue(valorInicial);
    }
    private void fillPositions(Square square, int sequence){
        if(sequence >= square.getSize()) return;
        fillNextPosition(square, sequence);
        fillPositions(square, ++sequence);
    }
    private void fillNextPosition(Square square, int sequence){
        Position positionToChange = checkNewPosition(square, square.getInitialPosition());
        positionToChange.setValue(sequence);
    }
    private Position checkNewPosition(Square square, Position prevPosition){
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
        //Compruebas el cambio de Y (eje vertical)
        int checkY = prevPosition.getY() - 1;
        if(checkY < 0) prevPosition = square.getMatrix()[prevPosition.getX()][square.getMatrix()[prevPosition.getX()].length];
        //Compruebas el cambie de X (eje horizontal)
        int checkX = prevPosition.getX() + 1;
        if(checkX > square.getMatrix().length) prevPosition = square.getMatrix()[0][prevPosition.getY()];
        //Compruebas si esta vacio o no
        if(!prevPosition.toString().equals("")) return prevPosition;
        else return square.getMatrix()[prevPosition.getX()][prevPosition.getY() + 1];

    }
}
