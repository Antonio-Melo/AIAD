package behaviors;

import Agents.DriverAgent;
import Agents.DriverAgent.DriverState;
import sajas.core.behaviours.TickerBehaviour;

public class DriverBehavior extends TickerBehaviour {
	
	private static final long serialVersionUID = 1L;

	public DriverBehavior(DriverAgent driver, long period) {
		super(driver, period);
	}
	
	public void onTick(){
		DriverAgent driver =  (DriverAgent) myAgent;
		if (driver.getState() == DriverState.DRIVING)
			driver.drive();
		else 
			driver.parkTick();
	}
}
