package Agents;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import repast.simphony.engine.watcher.Watch;
import repast.simphony.engine.watcher.WatcherTriggerSchedule;
import repast.simphony.space.continuous.ContinuousSpace;
import repast.simphony.space.grid.Grid;
import sajas.core.Agent;

public class ParkingFacilityAgent extends Agent {

	private static final long serialVersionUID = 1L;
	
	/*
	 * Park Facility Name
	 */
	private String name;
	
	/*
	 * Park Facility operator name
	 */
	private String operator;
	
	/*
	 * X coord of the Parking Facility in the grid  
	 */
	private int x;
	
	/*
	 * Y coord of the Parking Facility in the grid  
	 */
	private int y;
	
	/*
	 * Number of cars inside the Parking Facility  
	 */
	private int numCars;
	
	/*
	 * Total capacity of the Parking Facility  
	 */
	private int capacity;
	
	/*
	 * Minimum price that a driver pays when parking  
	 */
	private float minPrice;
	
	/*
	 * Maximum price that a driver pays when parking  
	 */
	private float maxPrice;
	
	/*
	 * Price Schema apllied for every day of the week
	 * [Monday, Tuesday, Wednesday, Thursday, Friday, Saturday, Sunday]  
	 */
	private List<Integer> priceSchema;
	/*
	 * 
	 */
	private boolean dynamic;
	private HashMap<Long,Integer> driversInsideThePark;
	private Grid<Object> grid;
	private ContinuousSpace<Object> space;

	public ParkingFacilityAgent(ContinuousSpace<Object> space, Grid<Object> grid, List<Integer> priceSchema, String name, String operator, int x,
			int y, int capacity, float priceHour, float maxPrice, float minPrice, boolean dynamic) {
		
		this.numCars = 0;
		this.name = name;
		this.operator = operator;
		this.x = x;
		this.y = y;
		this.minPrice = minPrice;
		this.maxPrice = maxPrice;
		this.capacity = capacity;
		this.dynamic = dynamic;
		this.driversInsideThePark = new HashMap<Long,Integer>();
		this.priceSchema = priceSchema;
		this.grid = grid;
		this.space = space;
	}

	//@Watch(watcheeClassName = "ParkingSimulation.Agents.DriverAgent", watcheeFieldNames = "moved", query = "within_moore 1", whenToTrigger = WatcherTriggerSchedule.IMMEDIATE)
	public void run() {

	}

	public String getParkName() {
		return this.name;
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

	public boolean isFull() {
		return numCars == capacity;
	}

	public void parkCar() {
		numCars++;
	}
	
	public void carLeavesPark() {
		numCars--;
	}
	
	public float getFinalPriceForNumberOfHours(double hours, int dayOfTheWeek) {
		float finalPrice = (float) (priceSchema.get(dayOfTheWeek)*hours);
		
		if(finalPrice > maxPrice) {
			return maxPrice;
		}else if(finalPrice < minPrice) {
			return minPrice;
		}else {
			return finalPrice;
		}
	}
}
