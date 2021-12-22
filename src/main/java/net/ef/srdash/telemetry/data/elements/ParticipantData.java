package net.ef.srdash.telemetry.data.elements;

public class ParticipantData {

    private boolean aiControlled;

    private int driverId;

    private int networkId;

    private int teamId;

    private int myTeam;

    private int raceNumber;

    private int nationality;

    private String name;

    private boolean yourTelemetry;

    public ParticipantData() {
    }

    public boolean isAiControlled() {
        return aiControlled;
    }

    public void setAiControlled(boolean aiControlled) {
        this.aiControlled = aiControlled;
    }

    public int getDriverId() {
        return driverId;
    }

    public void setDriverId(int driverId) {
        this.driverId = driverId;
    }

    /**
     * @return the networkId
     * @since 2020
     */
    public int getNetworkId() {
        return networkId;
    }

    /**
     * @param networkId the networkId to set
     * @since 2020
     */
    public void setNetworkId(int networkId) {
        this.networkId = networkId;
    }

    public int getTeamId() {
        return teamId;
    }

    public void setTeamId(int teamId) {
        this.teamId = teamId;
    }

    /**
     * @return the myTeam
     * @since 2020
     */
    public int getMyTeam() {
        return myTeam;
    }

    /**
     * @param myTeam the myTeam to set
     * @since 2020
     */
    public void setMyTeam(int myTeam) {
        this.myTeam = myTeam;
    }

    public int getRaceNumber() {
        return raceNumber;
    }

    public void setRaceNumber(int raceNumber) {
        this.raceNumber = raceNumber;
    }

    public int getNationality() {
        return nationality;
    }

    public void setNationality(int nationality) {
        this.nationality = nationality;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return the yourTelemetry
     * @since 20198
     */
    public boolean isYourTelemetry() {
        return yourTelemetry;
    }

    /**
     * @param yourTelemetry the yourTelemetry to set
     * @since 20198
     */
    public void setYourTelemetry(boolean yourTelemetry) {
        this.yourTelemetry = yourTelemetry;
    }

}
