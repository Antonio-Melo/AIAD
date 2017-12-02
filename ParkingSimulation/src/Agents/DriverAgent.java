package Agents;

import repast.simphony.engine.schedule.ScheduledMethod;
import repast.simphony.space.SpatialMath;
import repast.simphony.space.continuous.ContinuousSpace;
import repast.simphony.space.continuous.NdPoint;
import repast.simphony.space.grid.Grid;
import repast.simphony.space.grid.GridPoint;
import java.util.Random;
import sajas.core.Agent;

public class DriverAgent extends Agent {

	private static final long serialVersionUID = 1L;
	private static int IDNumber = 0;
	private static final double ALPHA = 0.5; // Constant that scales the
												// importance of the parking
												// price for the drivers of a
												// simulation
	private static final double BETA = 0.5; // Constant that scales the
											// importance of the walking
											// distance for the drivers of a
											// simulation
	private static final double POWER_U = 0.9; // Powers applied to the
												// calculated price or walking
												// distance to
	private static final double POWER_V = 0.9; // simulate the non-linearity of
												// the impact an increase on
												// walking distance or price has
	private static final double COEF_MIN = 1.0; // Minimum value of the parking
												// or walking distance
												// coefficient
	private static final double COEF_MAX = 1.5; // Maximum value of the parking
												// or walking distance
												// coefficient
	private static final double UTILITY_MAX = 1000; // Maximum attributable
													// ideal utility (maximum
													// utility)
	private static final double WORST_UTILITY = -1000; // Utility value
														// attributed when the
														// driver cannot find a
														// suitable parking
														// space
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
	private double priceCoefficient;
	private double walkingDistCoefficient;
	private double maxUtility;
	private Grid<Object> grid;
	private ContinuousSpace<Object> space;
	private boolean moved;

	public DriverAgent(ContinuousSpace<Object> space, Grid<Object> grid, int startX, int startY, int destinationX,
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
		this.space = space;
		Random random = new Random();
		this.priceCoefficient = COEF_MIN + ((COEF_MAX - COEF_MIN) * random.nextDouble()); // sets
																							// the
																							// var
																							// as
																							// a
																							// random
																							// number
																							// between
																							// COEF_MIN
																							// and
																							// COEF_MAX
		this.walkingDistCoefficient = COEF_MIN + ((COEF_MAX - COEF_MIN) * random.nextDouble());
		this.maxUtility = random.nextDouble() * UTILITY_MAX;
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

	/**
	 * Calculates the utility obtained by parking in a given parking facility
	 * 
	 * @param parkingFacility
	 *            candidate parking facility
	 * @return value of the utility function
	 */
	public double utilityValue(ParkingFacilityAgent parkingFacility) {
		double pricePerHour = parkingFacility.getPricePerHour();
		double priceUtility = ALPHA * pricePerHour * durationOfStay;
		
		NdPoint parkingFacilityPoint = new NdPoint(parkingFacility.getX(), parkingFacility.getY());
		NdPoint destinationPoint = new NdPoint(destinationX, destinationY);
		double distanceToDestination = space.getDistance(parkingFacilityPoint, destinationPoint);
		double walkingDistUtility = BETA * distanceToDestination;

		return maxUtility - priceCoefficient * Math.pow(priceUtility, POWER_U)
				- walkingDistCoefficient * Math.pow(walkingDistUtility, POWER_V);
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
