package Utilities;

import java.util.ArrayList;

import Agents.DriverAgent;
import sajas.core.Agent;

public class DriverUtilityCollector extends Agent{

	private ArrayList<Double> weeklyUtilities;
	private int currentWeek = 0;
	private double currentUtility;
	
	public DriverUtilityCollector(){
		weeklyUtilities = new ArrayList<Double>();
		currentUtility = new Double(0.0);
	}
	
	public void registerUtility(double utility){
		currentUtility = currentUtility + utility;
	}
	
	public void registerWeek(){
		weeklyUtilities.add(currentUtility);
		currentUtility = 0d;
		currentWeek++;
	}
	
	public Double getCurrentUtility(){
		return currentUtility;
	}
	
	public Double getUtilityForWeek(int week){
		return weeklyUtilities.get(week);
	}
	
	 public double lastWeekTotalUtility() { 
		 return (currentWeek <= 0) ? 0d : weeklyUtilities.get(currentWeek-1).doubleValue();
	 } 
}
