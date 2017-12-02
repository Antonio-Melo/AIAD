package Launcher;

import Agents.DriverAgent;
import Agents.ParkingFacilityAgent;
import jade.core.AID;
import jade.core.Profile;
import jade.core.ProfileImpl;
import jade.wrapper.StaleProxyException;
import repast.simphony.context.Context;
import repast.simphony.context.space.continuous.ContinuousSpaceFactory;
import repast.simphony.context.space.continuous.ContinuousSpaceFactoryFinder;
import repast.simphony.context.space.graph.NetworkBuilder;
import repast.simphony.context.space.grid.GridFactory;
import repast.simphony.context.space.grid.GridFactoryFinder;
import repast.simphony.space.continuous.ContinuousSpace;
import repast.simphony.space.continuous.NdPoint;
import repast.simphony.space.continuous.RandomCartesianAdder;
import repast.simphony.space.grid.Grid;
import repast.simphony.space.grid.GridBuilderParameters;
import repast.simphony.space.grid.SimpleGridAdder;
import repast.simphony.space.grid.StrictBorders;
import sajas.core.Agent;
import sajas.core.Runtime;
import sajas.sim.repasts.RepastSLauncher;
import sajas.wrapper.ContainerController;
import repast.simphony.space.grid.WrapAroundBorders;

public class ParkingSimulationLauncher extends RepastSLauncher {

	// Number of drivers to deploy
	private static int N_DRIVERS = 10;

	private ContainerController agentContainer;
	private ContinuousSpace<Object> space;
	private Grid<Object> grid;
	private int driversCount = 25;

	public static void main(String[] args) {
		ParkingSimulationLauncher model = new ParkingSimulationLauncher();
	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected void launchJADE() {

		/* 
		Runtime rt = Runtime.instance();
		Profile p1 = new ProfileImpl();
		agentContainer = rt.createAgentContainer(p1);

		launchAgents();
		 */
	}

	private void launchAgents() {

		for (int i = 0; i < N_DRIVERS; i++) {

			// TODO: Set Agents properties
			DriverAgent a = new DriverAgent(space, grid, i, i, i, i, i, i, i, i, i, i);

			try {
				agentContainer.acceptNewAgent("Driver " + a.getID(), a).start();
			} catch (StaleProxyException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	@Override
	public Context build(Context<Object> context) {
		// http://repast.sourceforge.net/docs/RepastJavaGettingStarted.pdf

		context.setId("ParkingSimulation");
		
		ContinuousSpaceFactory spaceFactory = ContinuousSpaceFactoryFinder.createContinuousSpaceFactory(null);	
		ContinuousSpace <Object > space = spaceFactory.createContinuousSpace("space", context , new RandomCartesianAdder<Object >(), new repast.simphony.space.continuous.StrictBorders(), 120, 80);
		
		GridFactory gridFactory = GridFactoryFinder.createGridFactory(null);

		// Set the boolean to true if more than one car can ocupy the same space
		grid = gridFactory.createGrid("grid", context, new GridBuilderParameters<Object>(
				new StrictBorders(), new SimpleGridAdder<Object>(), true, 120, 80));

		ParkingFacilityAgent[] parkingFacilities = {new ParkingFacilityAgent(space, grid, "Cabergerweg", "Q-Park", 2, 35, 698, (float) 1.43, (float) 9),
				new ParkingFacilityAgent(space, grid, "Sphinx-terrein", "Q-Park", 35, 25, 500, (float) 2.22, (float) 13),
				new ParkingFacilityAgent(space, grid, "De griend", "Q-Park", 10, 44, 351, (float) 2.22, (float) 13),
				new ParkingFacilityAgent(space, grid, "Bassin", "Q-Park", 32, 47, 407, (float) 2.73, (float) 25),
				new ParkingFacilityAgent(space, grid, "P + R station Maastricht", "Q-Park", 18, 58, 335, (float) 1.89, (float) 13),
				new ParkingFacilityAgent(space, grid, "Mosae forum", "Q-Park", 26, 59, 1082, (float) 2.73, (float) 25),
				new ParkingFacilityAgent(space, grid, "Vrijthof", "Q-Park", 42, 61, 545, (float) 3.53, (float) 35),
				new ParkingFacilityAgent(space, grid, "P + R meerssenerweg", "Q-Park", 73, 11, 65, (float) 1.89, (float) 13),
				new ParkingFacilityAgent(space, grid, "O.L. vrouweparking", "Q-Park", 79, 19, 350, (float) 2.73, (float) 25),
				new ParkingFacilityAgent(space, grid, "Plein 1992", "Q-Park", 72, 47, 449, (float) 2.22, (float) 13),
				new ParkingFacilityAgent(space, grid, "De colonel", "Q-Park", 79, 64, 297, (float) 2.22, (float) 13),
				new ParkingFacilityAgent(space, grid, "Bonnefantenmuseum", "Q-Park", 84, 29, 303, (float) 1.43, (float) 25),
				new ParkingFacilityAgent(space, grid, "Brusselse poort", "Q-Park", 87, 41, 610, (float) 1.43, (float) 25)
			};
		
		
		DriverAgent[] drivers = new DriverAgent[driversCount];
		for(int i = 0; i < driversCount; i++) {
			drivers[i] = new DriverAgent(space, grid, i, i, 120-i, 80-i, i, i, i, i, i, i);
		}

		for(DriverAgent obj : drivers) {
			context.add(obj);
			space.moveTo(obj, obj.getStartX(), obj.getStartY());
			grid.moveTo(obj, obj.getStartX(), obj.getStartY());
		}
		
		for(ParkingFacilityAgent obj : parkingFacilities) {
			context.add(obj);
			space.moveTo(obj, obj.getX(), obj.getY());
			grid.moveTo(obj, obj.getX(), obj.getY());
		}
		
		return super.build(context);
	}

}
