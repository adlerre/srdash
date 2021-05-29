package net.ef.srdash.telemetry.data;

import java.util.List;

import net.ef.srdash.telemetry.data.elements.LapData;

public class PacketLapData extends Packet {
	
	private List<LapData> lapDataList;
	
	public PacketLapData() {}
	
	public List<LapData> getLapDataList() {
		return lapDataList;
	}
	
	public void setLapDataList(List<LapData> lapDataList) {
		this.lapDataList = lapDataList;
	}

}
