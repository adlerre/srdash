package net.ef.srdash.telemetry.data;

public class PacketEventData extends Packet {

	private String eventCode;

	public PacketEventData() {}
	
	public String getEventCode() {
		return eventCode;
	}

	public void setEventCode(String eventCode) {
		this.eventCode = eventCode;
	}

}
