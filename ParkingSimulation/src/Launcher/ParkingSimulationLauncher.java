package Launcher;

import Agents.DriverAgent;
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
		ContinuousSpace <Object > space = spaceFactory.createContinuousSpace("space", context , new RandomCartesianAdder<Object >(), new repast.simphony.space.continuous.StrictBorders(), 50, 50);
		
		GridFactory gridFactory = GridFactoryFinder.createGridFactory(null);

		// Set the boolean to true if more than one car can ocupy the same space
		grid = gridFactory.createGrid("grid", context, new GridBuilderParameters<Object>(
				new StrictBorders(), new SimpleGridAdder<Object>(), true, 50, 50));

		
		for(int i = 0; i < driversCount; i++) {
			context.add(new DriverAgent(space, grid, i, i, 50-i, 50-i, i, i, i, i, i, i));
		}
		
		for(Object obj : context) {
			NdPoint pt = space.getLocation(obj);
			grid.moveTo(obj, (int)pt.getX(), (int)pt.getY());
		}
		
		return super.build(context);
	}

}
