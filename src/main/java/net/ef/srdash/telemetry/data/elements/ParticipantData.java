package net.ef.srdash.telemetry.data.elements;

public class ParticipantData {

    private boolean aiControlled;

    private int driverId;

    private int teamId;

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

    public int getTeamId() {
        return teamId;
    }

    public void setTeamId(int teamId) {
        this.teamId = teamId;
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
