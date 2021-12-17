package modelo;

public class Position{
    private int y;
    private int x;
    private Integer value;

    public Position(int y, int x){
        this.y = y;
        this.x = x;
    }

    public int getX() {
        return this.x;
    }

    public int getY() {
        return this.y;
    }
    public Integer getValue(){
        return this.value;
    }

    public void setY(int y){
        this.y = y;
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setValue(Integer value) {
        this.value = value;
    }

    @Override
    public String toString(){
        return String.valueOf(this.value);
    }
}
