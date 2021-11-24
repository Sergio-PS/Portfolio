package modelo.pantallas.positions;

public class Position {
    private final int Y;
    private final int X;
    private final String VALUE;

    public Position(int y, int x, String value){
        this.Y = y;
        this.X = x;
        this.VALUE = value;
    }

    public int getY() {
        return this.Y;
    }
    public int getX() {
        return this.X;
    }

    @Override
    public String toString(){
        return this.VALUE;
    }
}
