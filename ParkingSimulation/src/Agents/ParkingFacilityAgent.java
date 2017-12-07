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
	 * Enum representing the possible parameter that is updated each week
	 */
	public enum UpdateParameters{
		PRICE_PER_HOUR,
		MIN_PRICE,
		MAX_PRICE,
		DEFLATION,
		CAPACITY_DISCOUNT
	};
	
	private UpdateParameters parameter;
	
	/*
	 * Consecutive number of updates of one parameter
	 */
	private int consecutiveUpdates;
	
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
	 * Indicates the deflation apllicated to the price of each hour
	 */
	private float deflationPrice;
	
	/*
	 * Week number
	 */
	private int currentWeek;
	
	/*
	 * Price Schema apllied for every day of the week
	 * [Monday, Tuesday, Wednesday, Thursday, Friday, Saturday, Sunday]  
	 */
	private List<Float> priceSchema;
	
	/*
	 * Indicates if a Parking Facility applies dynamic or static prices
	 */
	private boolean dynamic;
	
	/*
	 * HashMap that stores the drivers inside the Parking Facility
	 * <DriverId, HoursInsideThePark>
	 */
	private HashMap<Integer,Integer> driversInsideThePark;
	
	/*
	 * HashMap that stores the week revenue
	 * <Week,Revenue>
	 */
	private HashMap<Integer,Float> weeklyRevenue; 
	private Grid<Object> grid;
	private ContinuousSpace<Object> space;

	public ParkingFacilityAgent(ContinuousSpace<Object> space, Grid<Object> grid, List<Float> priceSchema, String name, String operator, int x,
			int y, int capacity, float priceHour, float maxPrice, float minPrice, boolean dynamic) {
		
		this.numCars = 0;
		this.deflationPrice = 1;
		this.currentWeek = 0;
		this.consecutiveUpdates = 0;
		this.parameter = UpdateParameters.PRICE_PER_HOUR;
		this.name = name;
		this.operator = operator;
		this.x = x;
		this.y = y;
		this.minPrice = minPrice;
		this.maxPrice = maxPrice;
		this.capacity = capacity;
		this.dynamic = dynamic;
		this.driversInsideThePark = new HashMap<Integer,Integer>();
		this.priceSchema = priceSchema;
		this.weeklyRevenue = new HashMap<Integer,Float>();
		this.weeklyRevenue.put(0, (float)0);
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

	public void incNumCar() {
		numCars++;
	}
	
	public void decNumCar() {
		numCars--;
	}
	
	public void incUpdate() {
		consecutiveUpdates++;
	}
	
	public void resetUpdateNum() {
		consecutiveUpdates = 0;
	}
	
	public void parkCar(DriverAgent car, int hours, float price) {
		driversInsideThePark.put(car.getID(), hours);
		incNumCar();
		weeklyRevenue.replace(currentWeek, weeklyRevenue.get(currentWeek) + price);
	}
	
	public void checkOutCar(DriverAgent car) {
		driversInsideThePark.remove(car.getID());
		decNumCar();
	}
	
	public float getFinalPriceForNumberOfHours(int hours, int dayOfTheWeek) {
		float finalPrice = calculateFinalPrice(hours,dayOfTheWeek);
		finalPrice = calculateOcupacionDiscount(finalPrice);
		
		if(finalPrice > maxPrice) {
			return maxPrice;
		}else if(finalPrice < minPrice) {
			return minPrice;
		}else {
			return finalPrice;
		}
	}
	
	public float calculateFinalPrice(int hours, int dayOfTheWeek){
		float finalPrice;
		if(dynamic){
			finalPrice = 0;
			for(int i = 0; i < hours; i++){
				finalPrice += priceSchema.get(dayOfTheWeek) * Math.pow(deflationPrice,i);
			}
		}else{
			finalPrice = (float)(priceSchema.get(dayOfTheWeek)*hours);
		}
		return finalPrice;
	}
	
	public float calculateOcupacionDiscount(float price){
		double perOcupation = numCars/capacity;
		
		if(perOcupation < 0.3){
			return (float)(price*0.7);
		}else if(perOcupation > 0.7){
			return (float)(price*1.3);
		}else {
			return price;
		}
	}
	
	public void updateParameter() {
		checkParameterUpdates();
		
		switch(parameter) {
		case PRICE_PER_HOUR:
			updatePricePerHour();
			break;
		case MIN_PRICE:
			updateMinPrice();
			break;
		case MAX_PRICE:
			updateMaxPrice();
			break;
		case DEFLATION:
			updateDeflation();
			break;
		case CAPACITY_DISCOUNT:
			updateCapacityDiscount();
			break;
		default:
			updatePricePerHour();
			break;
		}
		
		incUpdate();
	}
	
	public void checkParameterUpdates() {
		if(consecutiveUpdates == 5) {
			switch(parameter) {
			case PRICE_PER_HOUR:
				parameter = UpdateParameters.MIN_PRICE;
				break;
			case MIN_PRICE:
				parameter = UpdateParameters.MAX_PRICE;
				break;
			case MAX_PRICE:
				parameter = UpdateParameters.DEFLATION;
				break;
			case DEFLATION:
				parameter = UpdateParameters.CAPACITY_DISCOUNT;
				break;
			case CAPACITY_DISCOUNT:
				parameter = UpdateParameters.PRICE_PER_HOUR;
				break;
			default:
				parameter = UpdateParameters.PRICE_PER_HOUR;
				break;
			}
			resetUpdateNum();
		}
	}
	
	public void updatePricePerHour() {
		
	}
	
	public void updateMinPrice() {
		
	}
	
	public void updateMaxPrice() {
		
	}
	
	public void updateDeflation() {
		
	}
	
	public void updateCapacityDiscount() {
		
	}
}
