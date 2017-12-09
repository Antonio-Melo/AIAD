package Agents;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import repast.simphony.engine.schedule.ISchedule;
import repast.simphony.space.continuous.ContinuousSpace;
import repast.simphony.space.grid.Grid;
import repast.simphony.space.grid.GridPoint;

public class ExplorerDriverAgent extends DriverAgent {

	private final ArrayList<ParkingFacilityAgent> parkList;

	public ExplorerDriverAgent(ContinuousSpace<Object> space, Grid<Object> grid, ParkingFacilityAgent[] parkingFacilities, ISchedule schedule, int weekDay, int weekCount) throws SecurityException, IOException {
		super(space, grid, parkingFacilities, schedule, weekDay, weekCount);
		this.parkList = new ArrayList<>(Arrays.asList(parkingFacilities));
	}
	
	public void setup(){
		super.setup();
		this.target = new GridPoint(this.getDestinationX(), this.getDestinationY());
	}

	/**
	 * Gets the next park the agent should visit
	 */
	@Override
	public ParkingFacilityAgent getNextPark() {
		if (parkList.isEmpty()) {
			return null;
		}
			
		int closestParkIndex = 0;
		GridPoint myPoint = grid.getLocation(this);
		GridPoint parkPoint = grid.getLocation(parkList.get(0));
		double closestParkDistance = grid.getDistance(myPoint, parkPoint);
		double distance;

		for (int i = 1; i < parkList.size(); i++) {
			parkPoint = grid.getLocation(parkList.get(i));
			distance = grid.getDistance(myPoint, parkPoint);

			if (distance < closestParkDistance) {
				closestParkDistance = distance;
				closestParkIndex = i;
			}
		}

		// Returns the closest park, and removes it from the list		
		return parkList.remove(closestParkIndex);
	}

}
