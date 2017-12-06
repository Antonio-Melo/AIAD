package Agents;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.PriorityQueue;

import Utilities.ParkingFacilityComparator;
import repast.simphony.space.continuous.ContinuousSpace;
import repast.simphony.space.grid.Grid;
import repast.simphony.space.grid.GridPoint;

public class GuidedDriverAgent extends DriverAgent {

	private final PriorityQueue<ParkingFacilityAgent> parkList;

	public GuidedDriverAgent(ContinuousSpace<Object> space, Grid<Object> grid, int startX, int startY, int destinationX,
			int destinatioY, int arrival, float maxPricePerHour, int durationOfStay, int maxWalkingDistance,
			int initialTime, int day, ParkingFacilityAgent[] parkingFacilities)
			throws SecurityException, IOException {
		super(space, grid, startX, startY, destinationX, destinatioY, arrival, maxPricePerHour, durationOfStay,
				maxWalkingDistance, initialTime, day, parkingFacilities);

		this.parkList = new PriorityQueue<>(new ParkingFacilityComparator(this));

		for (ParkingFacilityAgent park : parkingFacilities) {
			this.parkList.add(park);
		}	
	}
	
	public void setup(){
		super.setup();
		this.setNextPark();
	}

	/**
	 * Get the next park the agent should visit
	 * 
	 * @return
	 */
	@Override
	public ParkingFacilityAgent getNextPark() {
		this.logger.finer("Checking next park");
		return parkList.poll();
	}

}
