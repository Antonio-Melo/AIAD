package Agents;

import java.util.ArrayList;

import repast.simphony.space.continuous.ContinuousSpace;
import repast.simphony.space.grid.Grid;

public class ParkingFacilityCompetitionAgent extends ParkingFacilityAgent {

	public ParkingFacilityCompetitionAgent(ContinuousSpace<Object> space, Grid<Object> grid, String name,
			String operator, int x, int y, int capacity, ArrayList<Double> priceSchema, double maxPrice,
			double minPrice, boolean dynamic, boolean lastUpdateDecision, double learningRate,
			double capacityDiscount) {
		super(space, grid, name, operator, x, y, capacity, priceSchema, maxPrice, minPrice, dynamic, lastUpdateDecision,
				learningRate, capacityDiscount);
	}

}
