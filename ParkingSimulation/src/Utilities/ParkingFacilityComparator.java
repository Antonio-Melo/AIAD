package Utilities;

import java.util.Comparator;

import Agents.DriverAgent;
import Agents.ParkingFacilityAgent;

public class ParkingFacilityComparator implements Comparator<ParkingFacilityAgent> {

	private DriverAgent driver;
	
	public ParkingFacilityComparator(DriverAgent driver) {
		this.driver = driver;
	}

	@Override
	public int compare(ParkingFacilityAgent firstPark, ParkingFacilityAgent secondPark) {
		double firstParkUtility = driver.utilityValue(firstPark);
		double secondParkUtility = driver.utilityValue(secondPark);
		
		if (firstParkUtility > secondParkUtility)
			return 1;
		else if (firstParkUtility < secondParkUtility)
			return -1;
		return 0;
	}
}
