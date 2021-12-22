package net.ef.srdash.telemetry.data;

import java.util.List;

import net.ef.srdash.telemetry.data.elements.CarDamageData;

public class PacketCarDamageData extends Packet {

	private List<CarDamageData> carDamages;
	
	public PacketCarDamageData() {}
	
	public List<CarDamageData> getCarDamages() {
		return carDamages;
	}

    public void setCarDamages(List<CarDamageData> carDamages) {
		this.carDamages = carDamages;
	}
	
}
