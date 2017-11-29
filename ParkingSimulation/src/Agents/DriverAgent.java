package Agents;

import repast.simphony.engine.schedule.ScheduledMethod;
import repast.simphony.space.SpatialMath;
import repast.simphony.space.grid.Grid;
import repast.simphony.space.grid.GridPoint;
import sajas.core.Agent;

public class DriverAgent extends Agent {

	private static final long serialVersionUID = 1L;
	private static int IDNumber = 0;
	private int ID;
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
	private Grid<Object> grid;
	private boolean moved;

	public DriverAgent(Grid<Object> grid, int startX, int startY, int destinationX,
			int destinatioY, int arrival, float maxPricePerHour, int durationOfStay, int maxWalkingDistance,
			int initialTime, int day) {

		IDNumber++;
		ID = IDNumber;
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
		this.grid = grid;
	}

	public void moveTowards(GridPoint pt) {

		// only move if we are not already in this grid location
		if (!pt.equals(grid.getLocation(this))) {
			NdPoint myPoint = space.getLocation(this);
			NdPoint otherPoint = new NdPoint(pt.getX(), pt.getY());
			double angle = SpatialMath.calcAngleFor2DMovement(space, myPoint, otherPoint);
			space.moveByVector(this, 1, angle, 0);
			myPoint = space.getLocation(this);
			grid.moveTo(this, (int) myPoint.getX(), (int) myPoint.getY());

			moved = true;
		}
	}

	public int getID() {
		return ID;
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
