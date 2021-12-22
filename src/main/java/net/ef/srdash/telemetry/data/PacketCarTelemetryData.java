package net.ef.srdash.telemetry.data;

import java.util.List;

import net.ef.srdash.telemetry.data.elements.ButtonStatus;
import net.ef.srdash.telemetry.data.elements.CarTelemetryData;

public class PacketCarTelemetryData extends Packet {
	
	private List<CarTelemetryData> carTelemetryData;
	
	private ButtonStatus buttonStatus; // TODO, create a representation of this data properly
	
	private int mfdPanelIndex;
	
	private int mfdPanelIndexSecondaryPlayer;
	
	private int suggestedGear;
	
	public PacketCarTelemetryData() {}
	
	public List<CarTelemetryData> getCarTelemetryData() {
		return carTelemetryData;
	}
	public void setCarTelemetryData(List<CarTelemetryData> carTelemetryData) {
		this.carTelemetryData = carTelemetryData;
	}

	public ButtonStatus getButtonStatus() {
		return buttonStatus;
	}

	public void setButtonStatus(ButtonStatus buttonStatus) {
		this.buttonStatus = buttonStatus;
	}

    /**
     * @return the mfdPanelIndex
     * @since 2021
     */
    public int getMfdPanelIndex() {
        return mfdPanelIndex;
    }

    /**
     * @param mfdPanelIndex the mfdPanelIndex to set
     * @since 2021
     */
    public void setMfdPanelIndex(int mfdPanelIndex) {
        this.mfdPanelIndex = mfdPanelIndex;
    }

    /**
     * @return the mfdPanelIndexSecondaryPlayer
     * @since 2021
     */
    public int getMfdPanelIndexSecondaryPlayer() {
        return mfdPanelIndexSecondaryPlayer;
    }

    /**
     * @param mfdPanelIndexSecondaryPlayer the mfdPanelIndexSecondaryPlayer to set
     * @since 2021
     */
    public void setMfdPanelIndexSecondaryPlayer(int mfdPanelIndexSecondaryPlayer) {
        this.mfdPanelIndexSecondaryPlayer = mfdPanelIndexSecondaryPlayer;
    }

    /**
     * @return the suggestedGear
     * @since 2021
     */
    public int getSuggestedGear() {
        return suggestedGear;
    }

    /**
     * @param suggestedGear the suggestedGear to set
     * @since 2021
     */
    public void setSuggestedGear(int suggestedGear) {
        this.suggestedGear = suggestedGear;
    }
	
	

}
