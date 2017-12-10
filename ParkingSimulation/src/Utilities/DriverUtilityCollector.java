package Utilities;

import java.util.ArrayList;

import Agents.DriverAgent;
import sajas.core.Agent;

public class DriverUtilityCollector extends Agent{

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
		return currentUtility.doubleValue();
	}
	
	public Double getUtilityForWeek(int week){
		return weeklyUtilities.get(week);
	}
	
	 public double lastWeekTotalUtility() { 
		 System.out.println("Getting utility for week " + (currentWeek - 1));
		 if(currentWeek == 0)
			 return 0d;
		 else {
			 System.out.println(weeklyUtilities.get(currentWeek-1).doubleValue());
			 return weeklyUtilities.get(currentWeek-1).doubleValue();
		 } 
	 } 
}
