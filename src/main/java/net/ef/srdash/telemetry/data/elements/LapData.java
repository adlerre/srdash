package net.ef.srdash.telemetry.data.elements;

public class LapData {

    private float carIndex;
    private boolean playersCar;
    private float lastLapTime;
    private float currentLapTime;
    private float bestLapTime;
    private int bestLapNum;
    private int bestLapSector1TimeInMS;
    private int bestLapSector2TimeInMS;
    private int bestLapSector3TimeInMS;
    private int bestOverallSector1TimeInMS;
    private int bestOverallSector1LapNum;
    private int bestOverallSector2TimeInMS;
    private int bestOverallSector2LapNum;
    private int bestOverallSector3TimeInMS;
    private int bestOverallSector3LapNum;
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

    /**
     * @return the bestLapNum
     */
    public int getBestLapNum() {
        return bestLapNum;
    }

    /**
     * @param bestLapNum the bestLapNum to set
     */
    public void setBestLapNum(int bestLapNum) {
        this.bestLapNum = bestLapNum;
    }

    /**
     * @return the bestLapSector1TimeInMS
     */
    public int getBestLapSector1TimeInMS() {
        return bestLapSector1TimeInMS;
    }

    /**
     * @param bestLapSector1TimeInMS the bestLapSector1TimeInMS to set
     */
    public void setBestLapSector1TimeInMS(int bestLapSector1TimeInMS) {
        this.bestLapSector1TimeInMS = bestLapSector1TimeInMS;
    }

    /**
     * @return the bestLapSector2TimeInMS
     */
    public int getBestLapSector2TimeInMS() {
        return bestLapSector2TimeInMS;
    }

    /**
     * @param bestLapSector2TimeInMS the bestLapSector2TimeInMS to set
     */
    public void setBestLapSector2TimeInMS(int bestLapSector2TimeInMS) {
        this.bestLapSector2TimeInMS = bestLapSector2TimeInMS;
    }

    /**
     * @return the bestLapSector3TimeInMS
     */
    public int getBestLapSector3TimeInMS() {
        return bestLapSector3TimeInMS;
    }

    /**
     * @param bestLapSector3TimeInMS the bestLapSector3TimeInMS to set
     */
    public void setBestLapSector3TimeInMS(int bestLapSector3TimeInMS) {
        this.bestLapSector3TimeInMS = bestLapSector3TimeInMS;
    }

    /**
     * @return the bestOverallSector1TimeInMS
     */
    public int getBestOverallSector1TimeInMS() {
        return bestOverallSector1TimeInMS;
    }

    /**
     * @param bestOverallSector1TimeInMS the bestOverallSector1TimeInMS to set
     */
    public void setBestOverallSector1TimeInMS(int bestOverallSector1TimeInMS) {
        this.bestOverallSector1TimeInMS = bestOverallSector1TimeInMS;
    }

    /**
     * @return the bestOverallSector1LapNum
     */
    public int getBestOverallSector1LapNum() {
        return bestOverallSector1LapNum;
    }

    /**
     * @param bestOverallSector1LapNum the bestOverallSector1LapNum to set
     */
    public void setBestOverallSector1LapNum(int bestOverallSector1LapNum) {
        this.bestOverallSector1LapNum = bestOverallSector1LapNum;
    }

    /**
     * @return the bestOverallSector2TimeInMS
     */
    public int getBestOverallSector2TimeInMS() {
        return bestOverallSector2TimeInMS;
    }

    /**
     * @param bestOverallSector2TimeInMS the bestOverallSector2TimeInMS to set
     */
    public void setBestOverallSector2TimeInMS(int bestOverallSector2TimeInMS) {
        this.bestOverallSector2TimeInMS = bestOverallSector2TimeInMS;
    }

    /**
     * @return the bestOverallSector2LapNum
     */
    public int getBestOverallSector2LapNum() {
        return bestOverallSector2LapNum;
    }

    /**
     * @param bestOverallSector2LapNum the bestOverallSector2LapNum to set
     */
    public void setBestOverallSector2LapNum(int bestOverallSector2LapNum) {
        this.bestOverallSector2LapNum = bestOverallSector2LapNum;
    }

    /**
     * @return the bestOverallSector3TimeInMS
     */
    public int getBestOverallSector3TimeInMS() {
        return bestOverallSector3TimeInMS;
    }

    /**
     * @param bestOverallSector3TimeInMS the bestOverallSector3TimeInMS to set
     */
    public void setBestOverallSector3TimeInMS(int bestOverallSector3TimeInMS) {
        this.bestOverallSector3TimeInMS = bestOverallSector3TimeInMS;
    }

    /**
     * @return the bestOverallSector3LapNum
     */
    public int getBestOverallSector3LapNum() {
        return bestOverallSector3LapNum;
    }

    /**
     * @param bestOverallSector3LapNum the bestOverallSector3LapNum to set
     */
    public void setBestOverallSector3LapNum(int bestOverallSector3LapNum) {
        this.bestOverallSector3LapNum = bestOverallSector3LapNum;
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
