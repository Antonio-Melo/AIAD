package Agents;

import repast.simphony.engine.schedule.IAction;
import repast.simphony.engine.schedule.ISchedulableAction;
import repast.simphony.engine.schedule.ISchedule;
import repast.simphony.engine.schedule.ScheduleParameters;
import repast.simphony.random.RandomHelper;
import repast.simphony.space.SpatialMath;
import repast.simphony.space.continuous.ContinuousSpace;
import repast.simphony.space.continuous.NdPoint;
import repast.simphony.space.grid.Grid;
import repast.simphony.space.grid.GridPoint;
import java.io.IOException;
import java.util.Random;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

import Agents.DriverAgent.DriverState;
import Launcher.ParkingSimulationLauncher;
import behaviors.DriverBehavior;
import javassist.bytecode.stackmap.TypeData.ClassName;
import sajas.core.Agent;

public abstract class DriverAgent extends Agent {

	private static final long serialVersionUID = 1L;
	private static int IDNumber = 0;

	/*
	 * Constant that scales the importance of the parking price for the drivers
	 * of a simulation
	 */
	public static final double ALPHA = 0.5;

	/*
	 * Constant that scales the importance of the walking distance for the
	 * drivers of a simulation
	 */
	public static final double BETA = 0.5;

	/*
	 * Powers applied to the calculated price or walking distance costs to
	 * simulate the non-linearity of the impact an increase on walking distance
	 * or price has on a driver's utlility
	 */
	public static final double POWER_U = 0.9;
	public static final double POWER_V = 0.9;

	/*
	 * Minimum value of the parking or walking distance coefficient
	 */
	public static final double COEF_MIN = 1.0;

	/*
	 * Maximum value of the parking or walking distance coefficient
	 */
	public static final double COEF_MAX = 1.5;

	/*
	 * Maximum attributable ideal utility (maximum utility)
	 */
	public static final double UTILITY_MAX = 1000;

	/*
	 * Utility value attributed when the driver cannot find a suitable parking
	 * space
	 */
	public static final double WORST_UTILITY = -1000;

	/*
	 * Driver's state, either driving or parked
	 */
	public static enum DriverState {
		WAITING, DRIVING, PARKED
	};

	private int ID;

	/*
	 * Driver's starting coordinates
	 */
	private int startX;
	private int startY;

	/*
	 * Current driver state
	 */
	protected DriverState state;

	/*
	 * Destination coordinates
	 */
	private int destinationX;
	private int destinationY;

	/*
	 * Time of arrival at destination
	 */
	private double arrival;

	/*
	 * Maximum price the driver is willing to pay per hour
	 */
	private double maxPricePerHour;

	/*
	 * Maximum walking distance from the park to the destination that the driver
	 * is wiling to walk
	 */
	private int maxWalkingDistance;

	/*
	 * Duration (in hours) of the stay in the park
	 */
	private double durationOfStay;
	private double initialTime;
	private int day;

	/*
	 * Coefficients that scale the cost of the price and walking distance,
	 * respectively, in order to represent the relative importance of the
	 * parking price or distance to the destination
	 */
	private double priceCoefficient;
	private double walkingDistCoefficient;

	/*
	 * Maximum utility a driver can have, if the parking place was free and on
	 * the same point as the destination
	 */
	private double maxUtility;

	/*
	 * Utility reached by the driver
	 */
	protected double achievedUtility;

	/*
	 * Repast variables
	 */
	protected Grid<Object> grid;
	protected ContinuousSpace<Object> space;
	private boolean moved;
	protected GridPoint target;
	private ParkingFacilityAgent[] parkingFacilities;
	protected ParkingFacilityAgent targetPark;
	protected ISchedule schedule;
	protected ISchedulableAction action;
	
	public DriverAgent(ContinuousSpace<Object> space, Grid<Object> grid, ParkingFacilityAgent[] parkingFacilities, ISchedule schedule, int weekDay, int weekCount, double initialTime) throws SecurityException, IOException {

		IDNumber++;
		ID = IDNumber;

		this.startX = RandomHelper.nextIntFromTo(0, 119);
		this.startY = RandomHelper.nextIntFromTo(0, 79);
		this.destinationX = RandomHelper.createNormal(60, 14).nextInt();
		this.destinationY = RandomHelper.createNormal(40, 9).nextInt();
		this.maxPricePerHour = RandomHelper.nextDoubleFromTo(0.8, 1.2);
		this.day = weekDay;
		/*
		 * 0.0055m/ms -> 20km/h
		 * 22meters em 3963ms
		 * In every tick the driver moves one house, which is equivalent to 22m. There for every tick is equivalent to 4000ms
		 * 1tick = 4000ms = 4s
		 * 1 dia = 86400s = 21600 ticks
		 * 1h = 900 ticks
		 * 1min = 15 ticks
		 * 
		 * */
		if(initialTime > 10)
			this.durationOfStay = RandomHelper.nextDoubleFromTo(0, 2) * 900;	
		else
			this.durationOfStay = RandomHelper.nextDoubleFromTo(7.5, 8.5) * 900;	
			
		this.initialTime = initialTime * 900  +  (21600 * weekDay * weekCount);
		this.arrival = initialTime + 1350;
		
		this.maxWalkingDistance = RandomHelper.nextIntFromTo(800, 1200);
		this.grid = grid;
		this.space = space;
		this.state = DriverState.WAITING;
		this.parkingFacilities = parkingFacilities;
		this.schedule = schedule;
		
		
		Random random = new Random();
		/*
		 * Set the coefficients as a random double between COEF_MIN and COEF_MAX
		 */
		this.priceCoefficient = COEF_MIN + ((COEF_MAX - COEF_MIN) * random.nextDouble());
		this.walkingDistCoefficient = COEF_MIN + ((COEF_MAX - COEF_MIN) * random.nextDouble());
		this.maxUtility = random.nextDouble() * UTILITY_MAX;

	}

	public void setup() {
		ScheduleParameters  params = ScheduleParameters.createRepeating(schedule.getTickCount(), 1);
		this.action = schedule.schedule(params , this , "onTick");
	}
	
	public void onTick(){
		if(state == DriverState.WAITING) {
			grid.moveTo(this, this.startX, this.startY);
			space.moveTo(this, this.startX, this.startY);
			state = DriverState.DRIVING;
		} else if (state == DriverState.DRIVING)
			drive();
		else
			parkTick();
	}

	public void parkTick() {
		if (durationOfStay <= 0) {
			targetPark.checkOutCar(this);
			this.doDelete();
			schedule.removeAction(action);
			try {
				this.finalize();
			} catch (Throwable e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else {
			durationOfStay--;
		}
	}

	public void drive() {

		/*
		 * Checks if driver is in the target or in the cell right next to the
		 * target.
		 * 
		 * This verification is necessary due to the fact that the conversion
		 * from space point to grid point sometimes won't convert to the correct
		 * grid point of the target but to the point next to it.
		 * 
		 */
		if (grid.getDistance(grid.getLocation(this), target) > 1){
			moveTowards(target);
		}
		else { // reached target
			/* Small frontend fix for the cases where the above happens */
			grid.moveTo(this, target.getX(), target.getY());

			if (!park()) { // Could not park, or target wasn't park */
				if (!setNextPark()) { // No suitable park found
					achievedUtility = DriverAgent.WORST_UTILITY;
					ParkingSimulationLauncher.utilityCollector.registerUtility(achievedUtility);
					this.doDelete();
					schedule.removeAction(action);
					try {
						this.finalize();
					} catch (Throwable e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		}
	}

	public void moveTowards(GridPoint pt) {
		NdPoint myPoint = space.getLocation(this);
		NdPoint otherPoint = new NdPoint(pt.getX(), pt.getY());
		double angle = SpatialMath.calcAngleFor2DMovement(space, myPoint, otherPoint);
		space.moveByVector(this, 1, angle, 0);
		myPoint = space.getLocation(this);
		grid.moveTo(this, (int) myPoint.getX(), (int) myPoint.getY());
		moved = true;
	}

	/**
	 * Calculates the utility obtained by parking in a given parking facility
	 * 
	 * @param parkingFacility
	 *            candidate parking facility
	 * @return value of the utility function
	 */
	public double utilityValue(ParkingFacilityAgent parkingFacility) {
		double priceUtility = ALPHA * parkingFacility.getFinalPriceForNumberOfHours(durationOfStay, day);

		NdPoint parkingFacilityPoint = new NdPoint(parkingFacility.getX(), parkingFacility.getY());
		NdPoint destinationPoint = new NdPoint(destinationX, destinationY);
		double distanceToDestination = space.getDistance(parkingFacilityPoint, destinationPoint);
		double walkingDistUtility = BETA * distanceToDestination;

		return maxUtility - priceCoefficient * Math.pow(priceUtility, POWER_U)
				- walkingDistCoefficient * Math.pow(walkingDistUtility, POWER_V);
	}

	public abstract ParkingFacilityAgent getNextPark();

	/**
	 * Gets the next park from the driver's list and sets it as the target
	 * 
	 * @return true if a park was successfully set false if there is no more
	 *         parks to visit
	 */
	public boolean setNextPark() {
		targetPark = getNextPark();
		if (targetPark != null) {
			target = new GridPoint(targetPark.getX(), targetPark.getY());
			ParkingSimulationLauncher.driverLogger.fine("Set target to park " + targetPark.getParkName());
			return true;
		}
		ParkingSimulationLauncher.driverLogger.fine("No suitable park found");
		return false;
	}

	public boolean park() {
		if (targetPark == null || targetPark.isFull()) {
			return false;
		}

		ParkingSimulationLauncher.driverLogger.finer("Parked in park " + targetPark.getName());
		this.state = DriverState.PARKED;
		this.achievedUtility = utilityValue(targetPark);
		ParkingSimulationLauncher.utilityCollector.registerUtility(achievedUtility);
		targetPark.parkCar(this, durationOfStay, day);
		return true;
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

	public double getArrival() {
		return arrival;
	}

	public double getMaxPricePerHour() {
		return maxPricePerHour;
	}

	public double getDurationOfStay() {
		return durationOfStay;
	}

	public int getMaxWalkingDistance() {
		return maxWalkingDistance;
	}

	public double getInitialTime() {
		return initialTime;
	}

	public int getDay() {
		return day;
	}

	public DriverState getState() {
		return state;
	}
}
