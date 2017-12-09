package Launcher;

import java.io.IOException;

import Agents.DriverAgent;
import Agents.ExplorerDriverAgent;
import Agents.GuidedDriverAgent;
import Agents.ParkingFacilityAgent;
import jade.core.Profile;
import jade.core.ProfileImpl;
import jade.wrapper.StaleProxyException;
import repast.simphony.context.Context;
import repast.simphony.context.space.continuous.ContinuousSpaceFactory;
import repast.simphony.context.space.continuous.ContinuousSpaceFactoryFinder;
import repast.simphony.context.space.grid.GridFactory;
import repast.simphony.context.space.grid.GridFactoryFinder;
import repast.simphony.engine.environment.RunEnvironment;
import repast.simphony.engine.schedule.ISchedule;
import repast.simphony.engine.schedule.ScheduleParameters;
import repast.simphony.space.continuous.ContinuousSpace;
import repast.simphony.space.continuous.RandomCartesianAdder;
import repast.simphony.space.grid.Grid;
import repast.simphony.space.grid.GridBuilderParameters;
import repast.simphony.space.grid.SimpleGridAdder;
import repast.simphony.space.grid.StrictBorders;
import sajas.core.Runtime;
import sajas.sim.repasts.RepastSLauncher;
import sajas.wrapper.ContainerController;

public class ParkingSimulationLauncher extends RepastSLauncher {

	private ContainerController agentContainer;
	private ContainerController mainContainer;
	private ContinuousSpace<Object> space;
	private Grid<Object> grid;
	private int totalDriversPerWeekDay = 1000;
	private int totalDriversPerWeekendDay = 800;
	private int driversCount = 0;
	private ParkingFacilityAgent[] parkingFacilities;
	private DriverAgent[] drivers;
	protected ISchedule schedule;
	private int weekDay = 1;
	private int weekCount = 1;
	private String experiment;
	
	public static void main(String[] args) {
		ParkingSimulationLauncher model = new ParkingSimulationLauncher();
	}

	@Override
	public String getName() {
		return "ParkingSimulation";
	}

	@Override
	protected void launchJADE() {
		Runtime runtime = Runtime.instance();
		Profile profile = new ProfileImpl();
		mainContainer = runtime.createMainContainer(profile);
		agentContainer = mainContainer;
		schedule = RunEnvironment.getInstance().getCurrentSchedule();
		launchAgents();
	}

	private void launchAgents() {
		parkingFacilities = new ParkingFacilityAgent[] {
					new ParkingFacilityAgent(space, grid, "Cabergerweg", "Q-Park", 2, 46, 698, (float) 1.43, (float) 9),
					new ParkingFacilityAgent(space, grid, "Sphinx-terrein", "Q-Park", 35, 65, 500, (float) 2.22,
							(float) 13),
					new ParkingFacilityAgent(space, grid, "De griend", "Q-Park", 44, 71, 351, (float) 2.22, (float) 13),
					new ParkingFacilityAgent(space, grid, "Bassin", "Q-Park", 47, 49, 407, (float) 2.73, (float) 25),
					new ParkingFacilityAgent(space, grid, "P + R station Maastricht", "Q-Park", 58, 63, 335,
							(float) 1.89, (float) 13),
					new ParkingFacilityAgent(space, grid, "Mosae forum", "Q-Park", 59, 55, 1082, (float) 2.73,
							(float) 25),
					new ParkingFacilityAgent(space, grid, "Vrijthof", "Q-Park", 61, 38, 545, (float) 3.53, (float) 35),
					new ParkingFacilityAgent(space, grid, "P + R meerssenerweg", "Q-Park", 73, 69, 65, (float) 1.89,
							(float) 13),
					new ParkingFacilityAgent(space, grid, "O.L. vrouweparking", "Q-Park", 79, 62, 350, (float) 2.73,
							(float) 25),
					new ParkingFacilityAgent(space, grid, "Plein 1992", "Q-Park", 72, 34, 449, (float) 2.22,
							(float) 13),
					new ParkingFacilityAgent(space, grid, "De colonel", "Q-Park", 79, 17, 297, (float) 2.22,
							(float) 13),
					new ParkingFacilityAgent(space, grid, "Bonnefantenmuseum", "Q-Park", 90, 51, 303, (float) 1.43,
							(float) 25),
					new ParkingFacilityAgent(space, grid, "Brusselse poort", "Q-Park", 88, 40, 610, (float) 1.43,
							(float) 25) };

		/* Add the agents to the JADE container */
		try {
			int i = 0;
			for (ParkingFacilityAgent park : parkingFacilities) {
				agentContainer.acceptNewAgent("park-" + (i++), park).start();
			}
		} catch (StaleProxyException e) {
			e.printStackTrace();
		}
		
		ScheduleParameters  params = ScheduleParameters.createRepeating(1, 21600);
		schedule.schedule(params , this , "launchDrivers");
	}
	
	public void launchDrivers() throws SecurityException, IOException {
		
		
		if(weekDay < 6) {
			drivers = new DriverAgent[totalDriversPerWeekDay];
			for (int i = 0; i < totalDriversPerWeekDay / 2; i++) {		
				drivers[i] = new ExplorerDriverAgent(space, grid, parkingFacilities, schedule, weekDay, weekCount);
			}
			for (int i = totalDriversPerWeekDay / 2; i < totalDriversPerWeekDay; i++)
				drivers[i] = new GuidedDriverAgent(space, grid, parkingFacilities, schedule, weekDay, weekCount);
		}
		else {
			drivers = new DriverAgent[totalDriversPerWeekendDay];
			for (int i = 0; i < totalDriversPerWeekendDay / 2; i++) {		
				drivers[i] = new ExplorerDriverAgent(space, grid, parkingFacilities, schedule, weekDay, weekCount);
			}
			for (int i = totalDriversPerWeekendDay / 2; i < totalDriversPerWeekendDay; i++)
				drivers[i] = new GuidedDriverAgent(space, grid, parkingFacilities, schedule, weekDay, weekCount);
		}		
				
		/* Add the agents to the JADE container */
		try {
			for (DriverAgent driver : drivers) {
				driversCount++;
				agentContainer.acceptNewAgent("driver-" + (driversCount++), driver).start();
			}
		} catch (StaleProxyException e) {
			e.printStackTrace();
		}
		
		weekDay++;
		weekCount++;
		
		if(weekDay > 7)
			weekDay = 1;
	}

	@Override
	public Context build(Context<Object> context) {
		context.setId("ParkingSimulation");
		
		repast.simphony.parameter.Parameters parameters = RunEnvironment.getInstance().getParameters();
		this.totalDriversPerWeekDay = parameters.getInteger("driver_count_weekdays");
		this.totalDriversPerWeekendDay= parameters.getInteger("driver_count_weekends");
		this.experiment = parameters.getString("experiment");

		ContinuousSpaceFactory spaceFactory = ContinuousSpaceFactoryFinder.createContinuousSpaceFactory(null);
		space = spaceFactory.createContinuousSpace("space", context, new RandomCartesianAdder<Object>(),
				new repast.simphony.space.continuous.StrictBorders(), 120, 80);

		GridFactory gridFactory = GridFactoryFinder.createGridFactory(null);

		// Set the boolean to true if more than one car can occupy the same
		// space
		grid = gridFactory.createGrid("grid", context, new GridBuilderParameters<Object>(new StrictBorders(), new SimpleGridAdder<Object>(), true, 120, 80));
		return super.build(context);
	}
}
