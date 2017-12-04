package Agents;

import java.util.PriorityQueue;

import Utilities.ParkingFacilityComparator;
import repast.simphony.space.continuous.ContinuousSpace;
import repast.simphony.space.grid.Grid;

public class GuidedDriverAgent extends DriverAgent {
	
	private final PriorityQueue<ParkingFacilityAgent> parkList;

	public GuidedDriverAgent(ContinuousSpace<Object> space, Grid<Object> grid, int startX, int startY, int destinationX,
			int destinatioY, int arrival, float maxPricePerHour, int durationOfStay, int maxWalkingDistance,
			int initialTime, int day) {
		super(space, grid, startX, startY, destinationX, destinatioY, arrival, maxPricePerHour, durationOfStay,
				maxWalkingDistance, initialTime, day);
		this.parkList = new PriorityQueue<>(new ParkingFacilityComparator(this));
	}

	/**
	 * Get the next park the agent should visit
	 * @return
	 */
	@Override
	public ParkingFacilityAgent getNextPark() {
		return parkList.poll();
	}

}
