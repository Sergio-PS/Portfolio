package modelo;

public class Position {
    private int x;
    private int y;
    private Integer value;

    public Position(int x, int y){
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return this.x;
    }

    public int getY() {
        return this.y;
    }

    public void setValue(Integer value) {
        this.value = value;
    }

    @Override
    public String toString(){
        return String.valueOf(this.value);
    }
}
