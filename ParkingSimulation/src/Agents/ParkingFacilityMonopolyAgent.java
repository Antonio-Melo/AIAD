package Agents;

import java.util.ArrayList;

import repast.simphony.space.continuous.ContinuousSpace;
import repast.simphony.space.grid.Grid;

public class ParkingFacilityMonopolyAgent extends ParkingFacilityAgent{

	public ParkingFacilityMonopolyAgent(ContinuousSpace<Object> space, Grid<Object> grid, String name, String operator,
			int x, int y, int capacity, ArrayList<Double> priceSchema, double maxPrice, double minPrice,
			boolean dynamic, boolean lastUpdateDecision, double learningRate, double capacityDiscount) {
		super(space, grid, name, operator, x, y, capacity, priceSchema, maxPrice, minPrice, dynamic, lastUpdateDecision,
				learningRate, capacityDiscount);
	}

}
