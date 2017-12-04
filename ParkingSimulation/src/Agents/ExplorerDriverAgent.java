package Agents;

import java.util.ArrayList;

import repast.simphony.space.continuous.ContinuousSpace;
import repast.simphony.space.continuous.NdPoint;
import repast.simphony.space.grid.Grid;
import repast.simphony.space.grid.GridPoint;

public class ExplorerDriverAgent extends DriverAgent {

	private final ArrayList<ParkingFacilityAgent> parkList;

	public ExplorerDriverAgent(ContinuousSpace<Object> space, Grid<Object> grid, int startX, int startY,
			int destinationX, int destinatioY, int arrival, float maxPricePerHour, int durationOfStay,
			int maxWalkingDistance, int initialTime, int day) {
		super(space, grid, startX, startY, destinationX, destinatioY, arrival, maxPricePerHour, durationOfStay,
				maxWalkingDistance, initialTime, day);
		this.parkList = new ArrayList<>();
	}

	/**
	 * Gets the next park the agent should visit
	 */
	@Override
	public ParkingFacilityAgent getNextPark() {
		if (parkList.isEmpty())
			return null;

		int closestParkIndex = 0;
		NdPoint myPoint = space.getLocation(this);
		NdPoint parkPoint = space.getLocation(parkList.get(0));
		double closestParkDistance = space.getDistance(myPoint, parkPoint);
		double distance;

		for (int i = 1; i < parkList.size(); i++) {
			parkPoint = space.getLocation(parkList.get(i));
			distance = space.getDistance(myPoint, parkPoint);

			if (distance < closestParkDistance) {
				closestParkDistance = distance;
				closestParkIndex = i;
			}
		}
		
		// Returns the closest park, and removes it from the list
		return parkList.remove(closestParkIndex);
	}

}