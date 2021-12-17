package modelo;

public class Square {
    private int filas;
    private int columnas;
    private int size; //Total amount of positions
    private Position[][] matrix;
    private Position initialPosition;

    public Square(int filas, int columnas){
        this.filas = filas;
        this.columnas = columnas;
        this.size = this.filas * this.columnas;
        this.matrix = new Position[this.filas][this.columnas];
        coordinateMatrixPosition(0, 0);
        this.initialPosition = calculateInitialPosition();
    }
    //Funciones de la clase
    /*
    private void coordinateMatrixPosition(int x, int y){
        if(x >= this.columnas) return;
        if(y <= this.filas - 1) this.matrix[x][y] = new Position(x, y);
        else coordinateMatrixPosition(++x, y = 0);
        coordinateMatrixPosition(x, ++y);
    }*/
    private void coordinateMatrixPosition(int x, int y){
        if(x < this.columnas){
            if(y <= this.filas - 1) this.matrix[x][y] = new Position(x, y);
            else coordinateMatrixPosition(++x, y = 0);
            coordinateMatrixPosition(x, ++y);
        }
        //return;
    }
    public void printMatrix(int n, int k){
        if(n <= this.columnas - 1){
            if(k <= this.filas - 1){
                String value = this.matrix[n][k].toString();
                switch(value.length()){
                    case 1:
                        value = " " + " " + value;
                        break;
                    case 2:
                        value = " " + value;
                        break;
                    default: break;
                }
                System.out.print(value + " ");
                printMatrix(n, ++k);
            }else {
                System.out.println();
                printMatrix(++n, k = 0);
            }
            //No poner esto aqui: printMatrix(n, ++k); Ya que la recursividad intenta cerrar sus funciones abiertas
        }
    }
    /*
    public void printMatrix(int n, int k){
        if(n >= this.columnas - 1) return;
        if(k <= this.filas - 1) System.out.print(this.matrix[n][k].toString());
        else printMatrix(n + 1, k = 0);
        printMatrix(n, ++k);
    }*/
    private Position calculateInitialPosition(){
        int inicioNumero1 = (int) ((this.filas / 2) + (0.5));
        return this.matrix[0][inicioNumero1];
    }

    //Getters y Setters
    public Position[][] getMatrix(){
        return this.matrix;
    }
    public Position getInitialPosition(){
        return this.initialPosition;
    }
    public int getSize(){
        return this.size;
    }

    /*
    @Override
    public String toString(){
        printMatrix(0, 0);
        return "";
    }*/
}
