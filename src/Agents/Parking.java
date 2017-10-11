package Agents;

public class Parking extends jade.core.Agent
{
    private String name;
    private String operator;
    private int x;
    private int y;
    private int capacity;

    public Parking(String name, String operator, int x, int y, int capacity) {
        this.name = name;
        this.operator = operator;
        this.x = x;
        this.y = y;
        this.capacity = capacity;
    }

    public String getOperator() {
        return operator;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getCapacity() {
        return capacity;
    }
}
