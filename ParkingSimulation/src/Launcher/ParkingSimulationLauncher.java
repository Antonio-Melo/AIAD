package Launcher;

import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

import Agents.DriverAgent;
import Agents.ExplorerDriverAgent;
import Agents.GuidedDriverAgent;
import Agents.ParkingFacilityAgent;
import Utilities.DriverUtilityCollector;
import jade.core.Profile;
import jade.core.ProfileImpl;
import jade.wrapper.StaleProxyException;
import javassist.bytecode.stackmap.TypeData.ClassName;
import repast.simphony.context.Context;
import repast.simphony.context.space.continuous.ContinuousSpaceFactory;
import repast.simphony.context.space.continuous.ContinuousSpaceFactoryFinder;
import repast.simphony.context.space.grid.GridFactory;
import repast.simphony.context.space.grid.GridFactoryFinder;
import repast.simphony.engine.environment.RunEnvironment;
import repast.simphony.engine.schedule.ISchedule;
import repast.simphony.engine.schedule.ScheduleParameters;
import repast.simphony.random.RandomHelper;
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
	private int weekDay = 0;
	private int weekCount = 0;
	public static final Logger driverLogger = Logger.getLogger("DriverLogger");
	public DriverUtilityCollector utilityCollector = new DriverUtilityCollector();
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
		this.utilityCollector = new DriverUtilityCollector();
		try {
			agentContainer.acceptNewAgent("UtilityCollector", this.utilityCollector).start();
		} catch (StaleProxyException e1) {
			e1.printStackTrace();
		}		
		parkingFacilities = new ParkingFacilityAgent[] {
					new ParkingFacilityAgent(space, grid, "Cabergerweg", "Q-Park", 2, 46, 698, new ArrayList<Double>() {{
						add(1.43d);
					    add(1.43d);
					    add(1.43d);
					    add(1.43d);
					    add(1.43d);
					    add(1.43d);
					    add(1.43d);
					}}, 9d, 1.43d, true, true, 0.3, 0.3),
					
					new ParkingFacilityAgent(space, grid, "Sphinx-terrein", "Q-Park", 35, 65, 500, new ArrayList<Double>() {{
						add(2.22d);
					    add(2.22d);
					    add(2.22d);
					    add(2.22d);
					    add(2.22d);
					    add(2.22d);
					    add(2.22d);
					}}, 13d, 2.22d, true, true, 0.3, 0.3),
					new ParkingFacilityAgent(space, grid, "De griend", "Q-Park", 44, 71, 351, new ArrayList<Double>() {{
						add(2.22d);
					    add(2.22d);
					    add(2.22d);
					    add(2.22d);
					    add(2.22d);
					    add(2.22d);
					    add(2.22d);
					}}, 13d, 2.22d, true, true, 0.3, 0.3),
					new ParkingFacilityAgent(space, grid, "Bassin", "Q-Park", 47, 49, 407, new ArrayList<Double>() {{
						add(2.73d);
					    add(2.73d);
					    add(2.73d);
					    add(2.73d);
					    add(2.73d);
					    add(2.73d);
					    add(2.73d);
					}}, 25d, 2.73d, true, true, 0.3, 0.3),
					new ParkingFacilityAgent(space, grid, "P + R station Maastricht", "Q-Park", 58, 63, 335,new ArrayList<Double>() {{
						add(1.89d);
					    add(1.89d);
					    add(1.89d);
					    add(1.89d);
					    add(1.89d);
					    add(1.89d);
					    add(1.89d);
					}}, 13d, 1.89d, true, true, 0.3, 0.3),
					new ParkingFacilityAgent(space, grid, "Mosae forum", "Q-Park", 59, 55, 1082, new ArrayList<Double>() {{
						add(2.73d);
					    add(2.73d);
					    add(2.73d);
					    add(2.73d);
					    add(2.73d);
					    add(2.73d);
					    add(2.73d);
					}}, 25d, 2.73d, true, true, 0.3, 0.3),
					new ParkingFacilityAgent(space, grid, "Vrijthof", "Q-Park", 61, 38, 545, new ArrayList<Double>() {{
						add(3.53d);
					    add(3.53d);
					    add(3.53d);
					    add(3.53d);
					    add(3.53d);
					    add(3.53d);
					    add(3.53d);
					}}, 35d, 3.53d, true, true, 0.3, 0.3),
					new ParkingFacilityAgent(space, grid, "P + R meerssenerweg", "Q-Park", 73, 69, 65, new ArrayList<Double>() {{
						add(1.89d);
					    add(1.89d);
					    add(1.89d);
					    add(1.89d);
					    add(1.89d);
					    add(1.89d);
					    add(1.89d);
					}}, 13d, 1.89d, true, true, 0.3, 0.3),
					new ParkingFacilityAgent(space, grid, "O.L. vrouweparking", "Q-Park", 79, 62, 350, new ArrayList<Double>() {{
						add(2.73d);
					    add(2.73d);
					    add(2.73d);
					    add(2.73d);
					    add(2.73d);
					    add(2.73d);
					    add(2.73d);
					}}, 25d, 2.73d, true, true, 0.3, 0.3),
					new ParkingFacilityAgent(space, grid, "Plein 1992", "Q-Park", 72, 34, 449, new ArrayList<Double>() {{
						add(2.22d);
					    add(2.22d);
					    add(2.22d);
					    add(2.22d);
					    add(2.22d);
					    add(2.22d);
					    add(2.22d);
					}}, 13d, 2.22d, true, true, 0.3, 0.3),
					new ParkingFacilityAgent(space, grid, "De colonel", "Q-Park", 79, 17, 297, new ArrayList<Double>() {{
						add(2.22d);
					    add(2.22d);
					    add(2.22d);
					    add(2.22d);
					    add(2.22d);
					    add(2.22d);
					    add(2.22d);
					}}, 13d, 2.22d, true, true, 0.3, 0.3),
					new ParkingFacilityAgent(space, grid, "Bonnefantenmuseum", "Q-Park", 90, 51, 303, new ArrayList<Double>() {{
						add(1.43d);
					    add(1.43d);
					    add(1.43d);
					    add(1.43d);
					    add(1.43d);
					    add(1.43d);
					    add(1.43d);
					}}, 25d, 1.43d, true, true, 0.3, 0.3),
					new ParkingFacilityAgent(space, grid, "Brusselse poort", "Q-Park", 88, 40, 610, new ArrayList<Double>() {{
						add(1.43d);
					    add(1.43d);
					    add(1.43d);
					    add(1.43d);
					    add(1.43d);
					    add(1.43d);
					    add(1.43d);
					}}, 25d, 1.43d, true, true, 0.3, 0.3) };

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
		
	public void scheduleLaunchExplorerDriver(int weekDay, int weekCount) {
		double initialTime = 0;
		if(weekDay < 5) {
			initialTime = RandomHelper.createChiSquare(8).nextDouble();	
		}else {
			initialTime = 25;
			while((initialTime > 24)) {
				initialTime = RandomHelper.createChiSquare(10).nextDouble();
			}
		}
		
		if(weekDay == 7) {
			weekDay = 0;
		}
		ScheduleParameters  params = ScheduleParameters.createOneTime(initialTime * 900 +  (21600 * weekDay) + (21600 * 7 * weekCount));
		schedule.schedule(params , this , "launchExplorerDriver", weekDay, weekCount, initialTime);	
	}
	
	public void scheduleLaunchGuidedDriver(int weekDay, int weekCount) {
		double initialTime = 0;
		if(weekDay < 5) {
			initialTime = RandomHelper.createChiSquare(8).nextDouble();	
		}else {
			initialTime = 25;
			while((initialTime > 24)) {
				initialTime = RandomHelper.createChiSquare(10).nextDouble();
			}
		}
		if(weekDay == 7) {
			weekDay = 0;
		}
		ScheduleParameters  params = ScheduleParameters.createOneTime(initialTime * 900 +  (21600 * weekDay) + (21600 * 7 * weekCount));
		schedule.schedule(params , this , "launchGuidedDriver", weekDay, weekCount, initialTime);	
	}
	
	public void launchExplorerDriver(int weekDay, int weekCount, double initialTime) throws SecurityException, IOException, StaleProxyException {
		DriverAgent driver = new ExplorerDriverAgent(space, grid, parkingFacilities, schedule, weekDay, weekCount, initialTime, utilityCollector);
		agentContainer.acceptNewAgent("driver-" + (driversCount++), driver).start();
		driversCount++;
	}
	
	public void launchGuidedDriver(int weekDay, int weekCount, double initialTime) throws SecurityException, IOException, StaleProxyException {
		DriverAgent driver = new GuidedDriverAgent(space, grid, parkingFacilities, schedule, weekDay, weekCount, initialTime, utilityCollector);
		agentContainer.acceptNewAgent("driver-" + (driversCount++), driver).start();
		driversCount++;
	}
	
	public void launchDrivers() throws SecurityException, IOException {
		if(weekDay == 7) {
			weekCount++;
			weekDay = 0;
			utilityCollector.registerWeek();
			for(ParkingFacilityAgent park: parkingFacilities) {
				park.updateParameter();
			}
		}
		if(weekDay < 5) {
			for (int i = 0; i < totalDriversPerWeekDay / 2; i++) {		
				scheduleLaunchExplorerDriver(weekDay, weekCount);
			}
			for (int i = totalDriversPerWeekDay / 2; i < totalDriversPerWeekDay; i++)
				scheduleLaunchGuidedDriver(weekDay, weekCount);
			}
		else {
			for (int i = 0; i < totalDriversPerWeekendDay / 2; i++) {		
				scheduleLaunchExplorerDriver(weekDay, weekCount);
			}
			for (int i = totalDriversPerWeekendDay / 2; i < totalDriversPerWeekendDay; i++) {
				scheduleLaunchGuidedDriver(weekDay, weekCount);
			}
		}

		weekDay++;			
	}

	@Override
	public Context build(Context<Object> context) {
		context.setId("ParkingSimulation");
		
		// ======== Logger ==============
		driverLogger.setUseParentHandlers(false);
		FileHandler fh = null;
		try {
			fh = new FileHandler("logs/drivers/drivers.txt");
		} catch (SecurityException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		fh.setFormatter(new SimpleFormatter());
		driverLogger.addHandler(fh);
		
		
		// Get the parameters from the repast GUI
		repast.simphony.parameter.Parameters parameters = RunEnvironment.getInstance().getParameters();
		this.totalDriversPerWeekDay = parameters.getInteger("driver_count_weekdays");
		this.totalDriversPerWeekendDay= parameters.getInteger("driver_count_weekends");
		this.experiment = parameters.getString("experiment");

		
		// Build the repast Space and Grid
		ContinuousSpaceFactory spaceFactory = ContinuousSpaceFactoryFinder.createContinuousSpaceFactory(null);
		space = spaceFactory.createContinuousSpace("space", context, new RandomCartesianAdder<Object>(),
				new repast.simphony.space.continuous.StrictBorders(), 120, 80);

		GridFactory gridFactory = GridFactoryFinder.createGridFactory(null);
		grid = gridFactory.createGrid("grid", context, new GridBuilderParameters<Object>(new StrictBorders(), new SimpleGridAdder<Object>(), true, 120, 80));
		
		return super.build(context);
	}
}
