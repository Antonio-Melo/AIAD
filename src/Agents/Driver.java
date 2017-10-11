package Agents;

/**
 * Created by amelo on 9/27/17.
 */
public class Driver extends jade.core.Agent
{
    private int id;
    private int startX;
    private int startY;
    private int destinationX;
    private int destinationY;
    private int arrival;
    private float maxPricePerHour;
    private int durationOfStay;
    private int maxWalkingDistance;
    private int initialTime;
    private int day;

    public Driver(int id, int startX, int startY, int destinationX, int destinatioY, int arrival, float maxPricePerHour, int durationOfStay, int maxWalkingDistance, int initialTime, int day) {
        this.id = id;
        this.startX = startX;
        this.startY = startY;
        this.destinationX = destinationX;
        this.destinationY = destinatioY;
        this.arrival = arrival;
        this.maxPricePerHour = maxPricePerHour;
        this.durationOfStay = durationOfStay;
        this.maxWalkingDistance = maxWalkingDistance;
        this.initialTime = initialTime;
        this.day = day;
    }

    public int getId() {
        return id;
    }

    public int getStartX() {
        return startX;
    }

    public int getStartY() {
        return startY;
    }

    public int getDestinationX() {
        return destinationX;
    }

    public int getDestinationY() {
        return destinationY;
    }

    public int getArrival() {
        return arrival;
    }

    public float getMaxPricePerHour() {
        return maxPricePerHour;
    }

    public int getDurationOfStay() {
        return durationOfStay;
    }

    public int getMaxWalkingDistance() {
        return maxWalkingDistance;
    }

    public int getInitialTime() {
        return initialTime;
    }

    public int getDay() {
        return day;
    }
}
