package Agents;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import repast.simphony.space.continuous.ContinuousSpace;
import repast.simphony.space.continuous.NdPoint;
import repast.simphony.space.grid.Grid;
import repast.simphony.space.grid.GridPoint;

public class ExplorerDriverAgent extends DriverAgent {

	private final ArrayList<ParkingFacilityAgent> parkList;

	public ExplorerDriverAgent(ContinuousSpace<Object> space, Grid<Object> grid, int startX, int startY,
			int destinationX, int destinatioY, int arrival, float maxPricePerHour, int durationOfStay,
			int maxWalkingDistance, int initialTime, int day, ParkingFacilityAgent[] parkingFacilities) throws SecurityException, IOException {
		super(space, grid, startX, startY, destinationX, destinatioY, arrival, maxPricePerHour, durationOfStay,
				maxWalkingDistance, initialTime, day, parkingFacilities);
		this.parkList = new ArrayList<>(Arrays.asList(parkingFacilities));

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
