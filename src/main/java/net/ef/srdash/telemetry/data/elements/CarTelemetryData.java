package net.ef.srdash.telemetry.data.elements;

public class CarTelemetryData {

    private int speed;

    private int throttle;

    private int steer;

    private int brake;

    private int clutch;

    private int gear;

    private int engineRpm;

    private boolean drs;

    private int revLightsPercent;

    private int revLightsBitValue;

    private WheelData<Integer> brakeTemperature;

    private WheelData<Integer> tyreSurfaceTemperature;

    private WheelData<Integer> tyreInnerTemperature;

    private int engineTemperature;

    private WheelData<Float> tyrePressure;

    private WheelData<Integer> surfaceType;

    public CarTelemetryData() {
    }

    public int getSpeed() {
        return speed;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }

    public int getThrottle() {
        return throttle;
    }

    public void setThrottle(int throttle) {
        this.throttle = throttle;
    }

    public int getSteer() {
        return steer;
    }

    public void setSteer(int steer) {
        this.steer = steer;
    }

    public int getBrake() {
        return brake;
    }

    public void setBrake(int brake) {
        this.brake = brake;
    }

    public int getClutch() {
        return clutch;
    }

    public void setClutch(int clutch) {
        this.clutch = clutch;
    }

    public int getGear() {
        return gear;
    }

    public void setGear(int gear) {
        this.gear = gear;
    }

    public int getEngineRpm() {
        return engineRpm;
    }

    public void setEngineRpm(int engineRpm) {
        this.engineRpm = engineRpm;
    }

    public boolean isDrs() {
        return drs;
    }

    public void setDrs(boolean drs) {
        this.drs = drs;
    }

    public int getRevLightsPercent() {
        return revLightsPercent;
    }

    public void setRevLightsPercent(int revLightsPercent) {
        this.revLightsPercent = revLightsPercent;
    }

    /**
     * @return the revLightsBitValue
     * @since 2021
     */
    public int getRevLightsBitValue() {
        return revLightsBitValue;
    }

    /**
     * @param revLightsBitValue the revLightsBitValue to set
     * @since 2021
     */
    public void setRevLightsBitValue(int revLightsBitValue) {
        this.revLightsBitValue = revLightsBitValue;
    }

    public WheelData<Integer> getBrakeTemperature() {
        return brakeTemperature;
    }

    public void setBrakeTemperature(WheelData<Integer> brakeTemperature) {
        this.brakeTemperature = brakeTemperature;
    }

    public WheelData<Integer> getTyreSurfaceTemperature() {
        return tyreSurfaceTemperature;
    }

    public void setTyreSurfaceTemperature(WheelData<Integer> tyreSurfaceTemperature) {
        this.tyreSurfaceTemperature = tyreSurfaceTemperature;
    }

    public WheelData<Integer> getTyreInnerTemperature() {
        return tyreInnerTemperature;
    }

    public void setTyreInnerTemperature(WheelData<Integer> tyreInnerTemperature) {
        this.tyreInnerTemperature = tyreInnerTemperature;
    }

    public int getEngineTemperature() {
        return engineTemperature;
    }

    public void setEngineTemperature(int engineTemperature) {
        this.engineTemperature = engineTemperature;
    }

    public WheelData<Float> getTyrePressure() {
        return tyrePressure;
    }

    public void setTyrePressure(WheelData<Float> tyrePressure) {
        this.tyrePressure = tyrePressure;
    }

    /**
     * @return the surfaceType
     * @since 2019
     */
    public WheelData<Integer> getSurfaceType() {
        return surfaceType;
    }

    /**
     * @param surfaceType the surfaceType to set
     * @since 2019
     */
    public void setSurfaceType(WheelData<Integer> surfaceType) {
        this.surfaceType = surfaceType;
    }

}
