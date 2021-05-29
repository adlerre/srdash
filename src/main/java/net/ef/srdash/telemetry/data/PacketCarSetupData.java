package net.ef.srdash.telemetry.data;

import java.util.List;

import net.ef.srdash.telemetry.data.elements.CarSetupData;

public class PacketCarSetupData extends Packet {

	private List<CarSetupData> carSetups;
	
	public PacketCarSetupData() {}
	
	public List<CarSetupData> getCarSetups() {
		return carSetups;
	}
	public void setCarSetups(List<CarSetupData> carSetups) {
		this.carSetups = carSetups;
	}
	
}
