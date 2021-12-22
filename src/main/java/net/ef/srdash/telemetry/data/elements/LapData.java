package net.ef.srdash.telemetry.data.elements;

public class LapData {

    private float carIndex;
    private boolean playersCar;
    private float lastLapTime;
    private float currentLapTime;
    private float bestLapTime;
    private float sector1Time;
    private float sector2Time;
    private float lapDistance;
    private float totalDistance;
    private float safetyCarDelta;
    private int carPosition;
    private int currentLapNum;
    private PitStatus pitStatus;
    private int numPitStops;
    private int sector;
    private boolean currentLapInvalid;
    private int penalties;
    private int warnings;
    private int numUnservedDriveThroughPens;
    private int numUnservedStopGoPens;
    private int gridPosition;
    private DriverStatus driverStatus;
    private ResultStatus resultStatus;
    private boolean pitLaneTimerActive;
    private float pitLaneTimeInLane;
    private float pitStopTimer;
    private int pitStopShouldServePen;

    public LapData() {
    }

    public float getCarIndex() {
        return carIndex;
    }

    public void setCarIndex(float carIndex) {
        this.carIndex = carIndex;
    }

    public boolean isPlayersCar() {
        return playersCar;
    }

    public void setPlayersCar(boolean playersCar) {
        this.playersCar = playersCar;
    }

    public float getLastLapTime() {
        return lastLapTime;
    }

    public void setLastLapTime(float lastLapTime) {
        this.lastLapTime = lastLapTime;
    }

    public float getCurrentLapTime() {
        return currentLapTime;
    }

    public void setCurrentLapTime(float currentLapTime) {
        this.currentLapTime = currentLapTime;
    }

    public float getBestLapTime() {
        return bestLapTime;
    }

    public void setBestLapTime(float bestLapTime) {
        this.bestLapTime = bestLapTime;
    }

    public float getSector1Time() {
        return sector1Time;
    }

    public void setSector1Time(float sector1Time) {
        this.sector1Time = sector1Time;
    }

    public float getSector2Time() {
        return sector2Time;
    }

    public void setSector2Time(float sector2Time) {
        this.sector2Time = sector2Time;
    }

    public float getLapDistance() {
        return lapDistance;
    }

    public void setLapDistance(float lapDistance) {
        this.lapDistance = lapDistance;
    }

    public float getTotalDistance() {
        return totalDistance;
    }

    public void setTotalDistance(float totalDistance) {
        this.totalDistance = totalDistance;
    }

    public float getSafetyCarDelta() {
        return safetyCarDelta;
    }

    public void setSafetyCarDelta(float safetyCarDelta) {
        this.safetyCarDelta = safetyCarDelta;
    }

    public int getCarPosition() {
        return carPosition;
    }

    public void setCarPosition(int carPosition) {
        this.carPosition = carPosition;
    }

    public int getCurrentLapNum() {
        return currentLapNum;
    }

    public void setCurrentLapNum(int currentLapNum) {
        this.currentLapNum = currentLapNum;
    }

    public PitStatus getPitStatus() {
        return pitStatus;
    }

    public void setPitStatus(PitStatus pitStatus) {
        this.pitStatus = pitStatus;
    }

    /**
     * @return the numPitStops
     * @since 2021
     */
    public int getNumPitStops() {
        return numPitStops;
    }

    /**
     * @param numPitStops the numPitStops to set
     * @since 2021
     */
    public void setNumPitStops(int numPitStops) {
        this.numPitStops = numPitStops;
    }

    public int getSector() {
        return sector;
    }

    public void setSector(int sector) {
        this.sector = sector;
    }

    public boolean isCurrentLapInvalid() {
        return currentLapInvalid;
    }

    public void setCurrentLapInvalid(boolean currentLapInvalid) {
        this.currentLapInvalid = currentLapInvalid;
    }

    public int getPenalties() {
        return penalties;
    }

    public void setPenalties(int penalties) {
        this.penalties = penalties;
    }

    /**
     * @return the warnings
     * @since 2021
     */
    public int getWarnings() {
        return warnings;
    }

    /**
     * @param warnings the warnings to set
     * @since 2021
     */
    public void setWarnings(int warnings) {
        this.warnings = warnings;
    }

    /**
     * @return the numUnservedDriveThroughPens
     * @since 2021
     */
    public int getNumUnservedDriveThroughPens() {
        return numUnservedDriveThroughPens;
    }

    /**
     * @param numUnservedDriveThroughPens the numUnservedDriveThroughPens to set
     * @since 2021
     */
    public void setNumUnservedDriveThroughPens(int numUnservedDriveThroughPens) {
        this.numUnservedDriveThroughPens = numUnservedDriveThroughPens;
    }

    /**
     * @return the numUnservedStopGoPens
     * @since 2021
     */
    public int getNumUnservedStopGoPens() {
        return numUnservedStopGoPens;
    }

    /**
     * @param numUnservedStopGoPens the numUnservedStopGoPens to set
     * @since 2021
     */
    public void setNumUnservedStopGoPens(int numUnservedStopGoPens) {
        this.numUnservedStopGoPens = numUnservedStopGoPens;
    }

    public int getGridPosition() {
        return gridPosition;
    }

    public void setGridPosition(int gridPosition) {
        this.gridPosition = gridPosition;
    }

    public DriverStatus getDriverStatus() {
        return driverStatus;
    }

    public void setDriverStatus(DriverStatus driverStatus) {
        this.driverStatus = driverStatus;
    }

    public ResultStatus getResultStatus() {
        return resultStatus;
    }

    public void setResultStatus(ResultStatus resultStatus) {
        this.resultStatus = resultStatus;
    }

    /**
     * @return the pitLaneTimerActive
     * @since 2021
     */
    public boolean isPitLaneTimerActive() {
        return pitLaneTimerActive;
    }

    /**
     * @param pitLaneTimerActive the pitLaneTimerActive to set
     * @since 2021
     */
    public void setPitLaneTimerActive(boolean pitLaneTimerActive) {
        this.pitLaneTimerActive = pitLaneTimerActive;
    }

    /**
     * @return the pitLaneTimeInLane
     * @since 2021
     */
    public float getPitLaneTimeInLane() {
        return pitLaneTimeInLane;
    }

    /**
     * @param pitLaneTimeInLane the pitLaneTimeInLane to set
     * @since 2021
     */
    public void setPitLaneTimeInLane(float pitLaneTimeInLane) {
        this.pitLaneTimeInLane = pitLaneTimeInLane;
    }

    /**
     * @return the pitStopTimer
     * @since 2021
     */
    public float getPitStopTimer() {
        return pitStopTimer;
    }

    /**
     * @param pitStopTimer the pitStopTimer to set
     * @since 2021
     */
    public void setPitStopTimer(float pitStopTimer) {
        this.pitStopTimer = pitStopTimer;
    }

    /**
     * @return the pitStopShouldServePen
     * @since 2021
     */
    public int getPitStopShouldServePen() {
        return pitStopShouldServePen;
    }

    /**
     * @param pitStopShouldServePen the pitStopShouldServePen to set
     * @since 2021
     */
    public void setPitStopShouldServePen(int pitStopShouldServePen) {
        this.pitStopShouldServePen = pitStopShouldServePen;
    }

}
