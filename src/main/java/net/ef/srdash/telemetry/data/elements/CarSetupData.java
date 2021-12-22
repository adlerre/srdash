package net.ef.srdash.telemetry.data.elements;

public class CarSetupData {

    private int frontWing;
    private int rearWing;
    private int onThrottle;
    private int offThrottle;
    private float frontCamber;
    private float rearCamber;
    private float frontToe;
    private float rearToe;
    private int frontSuspension;
    private int rearSuspension;
    private int frontAntiRollBar;
    private int rearAntiRollBar;
    private int frontSuspensionHeight;
    private int rearSuspensionHeight;
    private int brakePressure;
    private int brakeBias;
    private float frontTyrePressure;
    private float rearTyrePressure;

    private float rearLeftTyrePressure;
    private float rearRightTyrePressure;
    private float frontLeftTyrePressure;
    private float frontRightTyrePressure;

    private int ballast;
    private float fuelLoad;

    public CarSetupData() {
    }

    public int getFrontWing() {
        return frontWing;
    }

    public void setFrontWing(int frontWing) {
        this.frontWing = frontWing;
    }

    public int getRearWing() {
        return rearWing;
    }

    public void setRearWing(int rearWing) {
        this.rearWing = rearWing;
    }

    public int getOnThrottle() {
        return onThrottle;
    }

    public void setOnThrottle(int onThrottle) {
        this.onThrottle = onThrottle;
    }

    public int getOffThrottle() {
        return offThrottle;
    }

    public void setOffThrottle(int offThrottle) {
        this.offThrottle = offThrottle;
    }

    public float getFrontCamber() {
        return frontCamber;
    }

    public void setFrontCamber(float frontCamber) {
        this.frontCamber = frontCamber;
    }

    public float getRearCamber() {
        return rearCamber;
    }

    public void setRearCamber(float rearCamber) {
        this.rearCamber = rearCamber;
    }

    public float getFrontToe() {
        return frontToe;
    }

    public void setFrontToe(float frontToe) {
        this.frontToe = frontToe;
    }

    public float getRearToe() {
        return rearToe;
    }

    public void setRearToe(float rearToe) {
        this.rearToe = rearToe;
    }

    public int getFrontSuspension() {
        return frontSuspension;
    }

    public void setFrontSuspension(int frontSuspension) {
        this.frontSuspension = frontSuspension;
    }

    public int getRearSuspension() {
        return rearSuspension;
    }

    public void setRearSuspension(int rearSuspension) {
        this.rearSuspension = rearSuspension;
    }

    public int getFrontAntiRollBar() {
        return frontAntiRollBar;
    }

    public void setFrontAntiRollBar(int frontAntiRollBar) {
        this.frontAntiRollBar = frontAntiRollBar;
    }

    public int getRearAntiRollBar() {
        return rearAntiRollBar;
    }

    public void setRearAntiRollBar(int rearAntiRollBar) {
        this.rearAntiRollBar = rearAntiRollBar;
    }

    public int getFrontSuspensionHeight() {
        return frontSuspensionHeight;
    }

    public void setFrontSuspensionHeight(int frontSuspensionHeight) {
        this.frontSuspensionHeight = frontSuspensionHeight;
    }

    public int getRearSuspensionHeight() {
        return rearSuspensionHeight;
    }

    public void setRearSuspensionHeight(int rearSuspensionHeight) {
        this.rearSuspensionHeight = rearSuspensionHeight;
    }

    public int getBrakePressure() {
        return brakePressure;
    }

    public void setBrakePressure(int brakePressure) {
        this.brakePressure = brakePressure;
    }

    public int getBrakeBias() {
        return brakeBias;
    }

    public void setBrakeBias(int brakeBias) {
        this.brakeBias = brakeBias;
    }

    public float getFrontTyrePressure() {
        return frontTyrePressure;
    }

    public void setFrontTyrePressure(float frontTyrePressure) {
        this.frontTyrePressure = frontTyrePressure;
    }

    public float getRearTyrePressure() {
        return rearTyrePressure;
    }

    public void setRearTyrePressure(float rearTyrePressure) {
        this.rearTyrePressure = rearTyrePressure;
    }

    /**
     * @return the rearLeftTyrePressure
     * @since 2021
     */
    public float getRearLeftTyrePressure() {
        return rearLeftTyrePressure;
    }

    /**
     * @param rearLeftTyrePressure the rearLeftTyrePressure to set
     * @since 2021
     */
    public void setRearLeftTyrePressure(float rearLeftTyrePressure) {
        this.rearLeftTyrePressure = rearLeftTyrePressure;
    }

    /**
     * @return the rearRightTyrePressure
     * @since 2021
     */
    public float getRearRightTyrePressure() {
        return rearRightTyrePressure;
    }

    /**
     * @param rearRightTyrePressure the rearRightTyrePressure to set
     * @since 2021
     */
    public void setRearRightTyrePressure(float rearRightTyrePressure) {
        this.rearRightTyrePressure = rearRightTyrePressure;
    }

    /**
     * @return the frontLeftTyrePressure
     * @since 2021
     */
    public float getFrontLeftTyrePressure() {
        return frontLeftTyrePressure;
    }

    /**
     * @param frontLeftTyrePressure the frontLeftTyrePressure to set
     * @since 2021
     */
    public void setFrontLeftTyrePressure(float frontLeftTyrePressure) {
        this.frontLeftTyrePressure = frontLeftTyrePressure;
    }

    /**
     * @return the frontRightTyrePressure
     * @since 2021
     */
    public float getFrontRightTyrePressure() {
        return frontRightTyrePressure;
    }

    /**
     * @param frontRightTyrePressure the frontRightTyrePressure to set
     * @since 2021
     */
    public void setFrontRightTyrePressure(float frontRightTyrePressure) {
        this.frontRightTyrePressure = frontRightTyrePressure;
    }

    public int getBallast() {
        return ballast;
    }

    public void setBallast(int ballast) {
        this.ballast = ballast;
    }

    public float getFuelLoad() {
        return fuelLoad;
    }

    public void setFuelLoad(float fuelLoad) {
        this.fuelLoad = fuelLoad;
    }

}
