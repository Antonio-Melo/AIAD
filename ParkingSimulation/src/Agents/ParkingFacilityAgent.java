package Agents;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
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
	private double minPrice;
	
	/*
	 * Maximum price that a driver pays when parking  
	 */
	private double maxPrice;
	
	/*
	 * Indicates the deflation apllicated to the price of each hour
	 */
	private double deflationPrice;
	
	/*
	 * Week number
	 */
	private int currentWeek;
	
	/*
	 * Price Schema apllied for every day of the week
	 * [Monday, Tuesday, Wednesday, Thursday, Friday, Saturday, Sunday]  
	 */
	private List<Double> priceSchema;
	
	/*
	 * Indicates if a Parking Facility applies dynamic or static prices
	 */
	private boolean dynamic;
	
	/*
	 * HashMap that stores the drivers inside the Parking Facility
	 * <DriverId, HoursInsideThePark>
	 */
	private HashMap<Integer,Double> driversInsideThePark;
	
	/*
	 * HashMap that stores the week revenue
	 * <Week,Revenue>
	 */
	private HashMap<Integer,Double> weeklyRevenue;
	
	private double learningRate;
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
	 * Last update decision (true-up or false-down)
	 */
	private boolean lastUpdateDecision;
	
	/*
	 * 
	 */
	private double capacityDiscount;
	private Grid<Object> grid;
	private ContinuousSpace<Object> space;

	public ParkingFacilityAgent(ContinuousSpace<Object> space, Grid<Object> grid, String name, String operator, int x,
			int y, int capacity, ArrayList<Double> priceSchema, double maxPrice, double minPrice, boolean dynamic, boolean lastUpdateDecision,double learningRate, double capacityDiscount) {
		
		this.numCars = 0;
		this.deflationPrice = 1;
		this.currentWeek = 0;
		this.name = name;
		this.operator = operator;
		this.x = x;
		this.y = y;
		this.minPrice = minPrice;
		this.maxPrice = maxPrice;
		this.capacity = capacity;
		this.dynamic = dynamic;
		this.driversInsideThePark = new HashMap<Integer,Double>();
		this.priceSchema = priceSchema;
		this.weeklyRevenue = new HashMap<Integer,Double>();
		this.weeklyRevenue.put(0, 0d);
		this.learningRate = 0.3;
		this.consecutiveUpdates = 0;
		this.parameter = UpdateParameters.PRICE_PER_HOUR;
		this.lastUpdateDecision = true;
		this.capacityDiscount = 0.3;
		this.grid = grid;
		this.space = space;
	}
	
	public void setup(){
		grid.moveTo(this, this.x, this.y);
		space.moveTo(this, this.x, this.y);
	}

	//@Watch(watcheeClassName = "ParkingSimulation.Agents.DriverAgent", watcheeFieldNames = "moved", query = "within_moore 1", whenToTrigger = WatcherTriggerSchedule.IMMEDIATE)
	public void run() {

	}

	public String getParkName() {
		return this.name;
	}
	
	public int getAvailableSpace() {
		return (capacity-numCars);
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
		return numCars >= capacity;
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
	
	public void nextWeek() {
		currentWeek++;
		weeklyRevenue.put(currentWeek, 0d);
	}
	
	public void parkCar(DriverAgent car, double hours, int day) {
		driversInsideThePark.put(car.getID(), hours);
		incNumCar();
		weeklyRevenue.replace(currentWeek, weeklyRevenue.get(currentWeek) + getFinalPriceForNumberOfHours(hours,day));
	}
	
	public void checkOutCar(DriverAgent car) {
		driversInsideThePark.remove(car.getID());
		decNumCar();
	}
	
	public double getFinalPriceForNumberOfHours(double hours, int dayOfTheWeek) {
		double finalPrice = calculateFinalPrice(hours,dayOfTheWeek);
		finalPrice = calculateOcupacionDiscount(finalPrice);
		
		if(finalPrice > maxPrice) {
			return maxPrice;
		}else if(finalPrice < minPrice) {
			return minPrice;
		}else {
			return finalPrice;
		}
	}
	
	public double calculateFinalPrice(double hours, int dayOfTheWeek){
		double finalPrice;
		double decimalPart = hours % 1;
		
		if(dynamic){
			finalPrice = 0;
			double hoursC = (decimalPart == 0) ? hours: hours - decimalPart; 
			int i;
			for(i = 0; i < hoursC; i++){
				finalPrice += priceSchema.get(dayOfTheWeek) * Math.pow(deflationPrice,i);
			}
			finalPrice += (decimalPart == 0) ? 0: priceSchema.get(dayOfTheWeek) * Math.pow(deflationPrice,i)* decimalPart;
			
		}else{
			finalPrice = priceSchema.get(dayOfTheWeek)*hours;
		}
		return finalPrice;
	}
	
	public double calculateOcupacionDiscount(double price){
		double perOcupation = numCars/capacity;
		
		if(perOcupation < 0.3){
			return price*(1-capacityDiscount);
		}else if(perOcupation > 0.7){
			return price*(1+capacityDiscount);
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
		nextWeek();
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
		for(int i = 0; i < 7 ; i++) {
			priceSchema.set(i, updateChosenParameter(priceSchema.get(i)));
		}
	}
	
	public void updateMinPrice() {
		minPrice = updateChosenParameter(minPrice);
	}
	
	public void updateMaxPrice() {
		maxPrice = updateChosenParameter(maxPrice);
	}
	
	public void updateDeflation() {
		deflationPrice = updateChosenParameter(deflationPrice);
	}
	
	public void updateCapacityDiscount() {
		capacityDiscount = updateChosenParameter(capacityDiscount);
	}
	
	public double updateChosenParameter(double parameter) {
		double newParameter;
		if(lastUpdateDecision) {
			newParameter = parameter + learningRate*parameter*(calculateRevenuePercentil()-1);
		}else {
			newParameter = parameter - learningRate*parameter*(calculateRevenuePercentil()-1);
		}
		lastUpdateDecision = (newParameter-parameter > 0) ? true: false;
		return newParameter;
	}
	
	public double calculateRevenuePercentil() {
		if(currentWeek ==0) return 0;
		else return ((weeklyRevenue.get(currentWeek)-weeklyRevenue.get(currentWeek-1))/weeklyRevenue.get(currentWeek-1));
	}
	
}