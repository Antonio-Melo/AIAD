package Agents;

import java.util.ArrayList;
import java.util.List;

import repast.simphony.engine.watcher.Watch;
import repast.simphony.engine.watcher.WatcherTriggerSchedule;
import repast.simphony.space.continuous.ContinuousSpace;
import repast.simphony.space.grid.Grid;
import sajas.core.Agent;

public class ParkingFacilityAgent extends Agent {

	private static final long serialVersionUID = 1L;
	private String name;
	private String operator;
	private int x;
	private int y;
	private int numCars;
	private int capacity;
	private float priceHour;
	private float maxPrice;
	private List<DriverAgent> driversInsideThePark;
	private Grid<Object> grid;
	private ContinuousSpace<Object> space;

	public ParkingFacilityAgent(ContinuousSpace<Object> space, Grid<Object> grid, String name, String operator, int x,
			int y, int capacity, float priceHour, float maxPrice) {
		this.name = name;
		this.operator = operator;
		this.x = x;
		this.y = y;
		this.priceHour = priceHour;
		this.maxPrice = maxPrice;
		this.numCars = 0;
		this.capacity = capacity;
		this.driversInsideThePark = new ArrayList<DriverAgent>();
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

	public double getPricePerHour() {
		return priceHour;
	}
}
