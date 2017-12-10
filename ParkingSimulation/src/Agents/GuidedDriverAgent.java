package Agents;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.PriorityQueue;

import Launcher.ParkingSimulationLauncher;
import Utilities.DriverUtilityCollector;
import Utilities.ParkingFacilityComparator;
import repast.simphony.engine.schedule.ISchedule;
import repast.simphony.engine.schedule.Schedule;
import repast.simphony.space.continuous.ContinuousSpace;
import repast.simphony.space.grid.Grid;
import repast.simphony.space.grid.GridPoint;

public class GuidedDriverAgent extends DriverAgent {

	private final PriorityQueue<ParkingFacilityAgent> parkList;

	public GuidedDriverAgent(ContinuousSpace<Object> space, Grid<Object> grid, ParkingFacilityAgent[] parkingFacilities, ISchedule schedule, int weekDay, int weekCount, double initialTime, DriverUtilityCollector utilityCollector)
			throws SecurityException, IOException {
		super(space, grid, parkingFacilities, schedule, weekDay, weekCount, initialTime, utilityCollector);

		this.parkList = new PriorityQueue<>(new ParkingFacilityComparator(this));

		for (ParkingFacilityAgent park : parkingFacilities) {
			this.parkList.add(park);
		}	
	}
	
	public void setup(){
		if(!this.setNextPark()) {
			achievedUtility = DriverAgent.WORST_UTILITY;
			utilityCollector.registerUtility(achievedUtility);
			this.doDelete();
		}else {
			super.setup();
		}
	}

	/**
	 * Get the next park the agent should visit
	 * 
	 * @return
	 */
	@Override
	public ParkingFacilityAgent getNextPark() {
		ParkingFacilityAgent nextPark = parkList.poll();
		GridPoint parkPoint = grid.getLocation(nextPark);
		GridPoint destinationPoint = new GridPoint(getDestinationX(), getDestinationY());
		double pricePerHour = nextPark.getCurrentPricePerHour(getDay());
		
		while ((pricePerHour > getMaxPricePerHour() || grid.getDistance(destinationPoint, parkPoint) > getMaxWalkingDistance()) && nextPark != null)
			nextPark = parkList.poll();
		
		ParkingSimulationLauncher.driverLogger.finer("Checking next park");
		return nextPark;
	}

}
