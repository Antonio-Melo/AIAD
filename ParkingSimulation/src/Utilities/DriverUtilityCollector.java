package Utilities;

import java.util.ArrayList;

import Agents.DriverAgent;

public class DriverUtilityCollector {

	private ArrayList<Double> weeklyUtilities;
	private int currentWeek = 0;
	private Double currentUtility;
	
	public DriverUtilityCollector(){
		weeklyUtilities = new ArrayList<Double>();
		currentUtility = new Double(0.0);
	}
	
	public void registerUtility(double utility){
		currentUtility = currentUtility.doubleValue() + utility;
	}
	
	public void registerWeek(){
		weeklyUtilities.add(currentUtility);
		currentUtility = 0d;
		currentWeek++;
	}
	
	public Double getCurrentUtility(){
		System.out.println("hey");
		return currentUtility.doubleValue();
	}
	
	public Double getUtilityForWeek(int week){
		return weeklyUtilities.get(week);
	}
	
	 public double lastWeekTotalUtility() { 
		 if(currentWeek == 0)
			 return 0;
		 else {
			 return weeklyUtilities.get(currentWeek-1);
		 } 
	 } 
}
