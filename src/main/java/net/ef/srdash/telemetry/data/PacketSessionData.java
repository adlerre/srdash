package net.ef.srdash.telemetry.data;

import java.util.List;

import net.ef.srdash.telemetry.data.elements.Era;
import net.ef.srdash.telemetry.data.elements.MarshalZone;
import net.ef.srdash.telemetry.data.elements.SafetyCarStatus;
import net.ef.srdash.telemetry.data.elements.SessionType;
import net.ef.srdash.telemetry.data.elements.Weather;
import net.ef.srdash.telemetry.data.elements.WeatherForecast;

public class PacketSessionData extends Packet {

    private Weather weather;
    private int trackTemperature;
    private int airTemperature;
    private int totalLaps;
    private int trackLength;
    private SessionType sessionType;
    private int trackId;
    private Era era;
    private int sessionTimeLeft;
    private int sessionDuration;
    private int pitSpeedLimit;
    private boolean gamePaused;
    private boolean spectating;
    private int spectatorCarIndex;
    private boolean sliProNativeSupport;
    private int numMarshalZones;
    private List<MarshalZone> marshalZones;
    private SafetyCarStatus safetyCarStatus;
    private boolean networkGame;
    private int numWeatherForecastSamples;
    private List<WeatherForecast> weatherForecast;
    private int forecastAccuracy;
    private int aiDifficulty;
    private long seasonLinkIdentifier;
    private long weekendLinkIdentifier;
    private long sessionLinkIdentifier;
    private int pitStopWindowIdealLap;
    private int pitStopWindowLatestLap;
    private int pitStopRejoinPosition;
    private boolean steeringAssist;
    private boolean brakingAssist;
    private boolean gearboxAssist;
    private boolean pitAssist;
    private boolean pitReleaseAssist;
    private boolean ERSAssist;
    private boolean DRSAssist;
    private int dynamicRacingLine;
    private int dynamicRacingLineType;

    public PacketSessionData() {
    }

    public Weather getWeather() {
        return weather;
    }

    public void setWeather(Weather weather) {
        this.weather = weather;
    }

    public int getTrackTemperature() {
        return trackTemperature;
    }

    public void setTrackTemperature(int trackTemperature) {
        this.trackTemperature = trackTemperature;
    }

    public int getAirTemperature() {
        return airTemperature;
    }

    public void setAirTemperature(int airTemperature) {
        this.airTemperature = airTemperature;
    }

    public int getTotalLaps() {
        return totalLaps;
    }

    public void setTotalLaps(int totalLaps) {
        this.totalLaps = totalLaps;
    }

    public int getTrackLength() {
        return trackLength;
    }

    public void setTrackLength(int trackLength) {
        this.trackLength = trackLength;
    }

    public SessionType getSessionType() {
        return sessionType;
    }

    public void setSessionType(SessionType sessionType) {
        this.sessionType = sessionType;
    }

    public int getTrackId() {
        return trackId;
    }

    public void setTrackId(int trackId) {
        this.trackId = trackId;
    }

    public Era getEra() {
        return era;
    }

    public void setEra(Era era) {
        this.era = era;
    }

    public int getSessionTimeLeft() {
        return sessionTimeLeft;
    }

    public void setSessionTimeLeft(int sessionTimeLeft) {
        this.sessionTimeLeft = sessionTimeLeft;
    }

    public int getSessionDuration() {
        return sessionDuration;
    }

    public void setSessionDuration(int sessionDuration) {
        this.sessionDuration = sessionDuration;
    }

    public int getPitSpeedLimit() {
        return pitSpeedLimit;
    }

    public void setPitSpeedLimit(int pitSpeedLimit) {
        this.pitSpeedLimit = pitSpeedLimit;
    }

    public boolean isGamePaused() {
        return gamePaused;
    }

    public void setGamePaused(boolean gamePaused) {
        this.gamePaused = gamePaused;
    }

    public boolean isSpectating() {
        return spectating;
    }

    public void setSpectating(boolean spectating) {
        this.spectating = spectating;
    }

    public int getSpectatorCarIndex() {
        return spectatorCarIndex;
    }

    public void setSpectatorCarIndex(int spectatorCarIndex) {
        this.spectatorCarIndex = spectatorCarIndex;
    }

    public boolean isSliProNativeSupport() {
        return sliProNativeSupport;
    }

    public void setSliProNativeSupport(boolean sliProNativeSupport) {
        this.sliProNativeSupport = sliProNativeSupport;
    }

    public int getNumMarshalZones() {
        return numMarshalZones;
    }

    public void setNumMarshalZones(int numMarshalZones) {
        this.numMarshalZones = numMarshalZones;
    }

    public List<MarshalZone> getMarshalZones() {
        return marshalZones;
    }

    public void setMarshalZones(List<MarshalZone> marshalZones) {
        this.marshalZones = marshalZones;
    }

    public SafetyCarStatus getSafetyCarStatus() {
        return safetyCarStatus;
    }

    public void setSafetyCarStatus(SafetyCarStatus safetyCarStatus) {
        this.safetyCarStatus = safetyCarStatus;
    }

    public boolean isNetworkGame() {
        return networkGame;
    }

    public void setNetworkGame(boolean networkGame) {
        this.networkGame = networkGame;
    }

    /**
     * @return the numWeatherForecastSamples
     */
    public int getNumWeatherForecastSamples() {
        return numWeatherForecastSamples;
    }

    /**
     * @param numWeatherForecastSamples the numWeatherForecastSamples to set
     */
    public void setNumWeatherForecastSamples(int numWeatherForecastSamples) {
        this.numWeatherForecastSamples = numWeatherForecastSamples;
    }

    /**
     * @return the weatherForecast
     */
    public List<WeatherForecast> getWeatherForecast() {
        return weatherForecast;
    }

    /**
     * @param weatherForecast the weatherForecast to set
     */
    public void setWeatherForecast(List<WeatherForecast> weatherForecast) {
        this.weatherForecast = weatherForecast;
    }

    /**
     * @return the forecastAccuracy
     */
    public int getForecastAccuracy() {
        return forecastAccuracy;
    }

    /**
     * @param forecastAccuracy the forecastAccuracy to set
     */
    public void setForecastAccuracy(int forecastAccuracy) {
        this.forecastAccuracy = forecastAccuracy;
    }

    /**
     * @return the aiDifficulty
     */
    public int getAiDifficulty() {
        return aiDifficulty;
    }

    /**
     * @param aiDifficulty the aiDifficulty to set
     */
    public void setAiDifficulty(int aiDifficulty) {
        this.aiDifficulty = aiDifficulty;
    }

    /**
     * @return the seasonLinkIdentifier
     */
    public long getSeasonLinkIdentifier() {
        return seasonLinkIdentifier;
    }

    /**
     * @param seasonLinkIdentifier the seasonLinkIdentifier to set
     */
    public void setSeasonLinkIdentifier(long seasonLinkIdentifier) {
        this.seasonLinkIdentifier = seasonLinkIdentifier;
    }

    /**
     * @return the weekendLinkIdentifier
     */
    public long getWeekendLinkIdentifier() {
        return weekendLinkIdentifier;
    }

    /**
     * @param weekendLinkIdentifier the weekendLinkIdentifier to set
     */
    public void setWeekendLinkIdentifier(long weekendLinkIdentifier) {
        this.weekendLinkIdentifier = weekendLinkIdentifier;
    }

    /**
     * @return the sessionLinkIdentifier
     */
    public long getSessionLinkIdentifier() {
        return sessionLinkIdentifier;
    }

    /**
     * @param sessionLinkIdentifier the sessionLinkIdentifier to set
     */
    public void setSessionLinkIdentifier(long sessionLinkIdentifier) {
        this.sessionLinkIdentifier = sessionLinkIdentifier;
    }

    /**
     * @return the pitStopWindowIdealLap
     */
    public int getPitStopWindowIdealLap() {
        return pitStopWindowIdealLap;
    }

    /**
     * @param pitStopWindowIdealLap the pitStopWindowIdealLap to set
     */
    public void setPitStopWindowIdealLap(int pitStopWindowIdealLap) {
        this.pitStopWindowIdealLap = pitStopWindowIdealLap;
    }

    /**
     * @return the pitStopWindowLatestLap
     */
    public int getPitStopWindowLatestLap() {
        return pitStopWindowLatestLap;
    }

    /**
     * @param pitStopWindowLatestLap the pitStopWindowLatestLap to set
     */
    public void setPitStopWindowLatestLap(int pitStopWindowLatestLap) {
        this.pitStopWindowLatestLap = pitStopWindowLatestLap;
    }

    /**
     * @return the pitStopRejoinPosition
     */
    public int getPitStopRejoinPosition() {
        return pitStopRejoinPosition;
    }

    /**
     * @param pitStopRejoinPosition the pitStopRejoinPosition to set
     */
    public void setPitStopRejoinPosition(int pitStopRejoinPosition) {
        this.pitStopRejoinPosition = pitStopRejoinPosition;
    }

    /**
     * @return the steeringAssist
     */
    public boolean isSteeringAssist() {
        return steeringAssist;
    }

    /**
     * @param steeringAssist the steeringAssist to set
     */
    public void setSteeringAssist(boolean steeringAssist) {
        this.steeringAssist = steeringAssist;
    }

    /**
     * @return the brakingAssist
     */
    public boolean isBrakingAssist() {
        return brakingAssist;
    }

    /**
     * @param brakingAssist the brakingAssist to set
     */
    public void setBrakingAssist(boolean brakingAssist) {
        this.brakingAssist = brakingAssist;
    }

    /**
     * @return the gearboxAssist
     */
    public boolean isGearboxAssist() {
        return gearboxAssist;
    }

    /**
     * @param gearboxAssist the gearboxAssist to set
     */
    public void setGearboxAssist(boolean gearboxAssist) {
        this.gearboxAssist = gearboxAssist;
    }

    /**
     * @return the pitAssist
     */
    public boolean isPitAssist() {
        return pitAssist;
    }

    /**
     * @param pitAssist the pitAssist to set
     */
    public void setPitAssist(boolean pitAssist) {
        this.pitAssist = pitAssist;
    }

    /**
     * @return the pitReleaseAssist
     */
    public boolean isPitReleaseAssist() {
        return pitReleaseAssist;
    }

    /**
     * @param pitReleaseAssist the pitReleaseAssist to set
     */
    public void setPitReleaseAssist(boolean pitReleaseAssist) {
        this.pitReleaseAssist = pitReleaseAssist;
    }

    /**
     * @return the eRSAssist
     */
    public boolean isERSAssist() {
        return ERSAssist;
    }

    /**
     * @param eRSAssist the eRSAssist to set
     */
    public void setERSAssist(boolean eRSAssist) {
        ERSAssist = eRSAssist;
    }

    /**
     * @return the dRSAssist
     */
    public boolean isDRSAssist() {
        return DRSAssist;
    }

    /**
     * @param dRSAssist the dRSAssist to set
     */
    public void setDRSAssist(boolean dRSAssist) {
        DRSAssist = dRSAssist;
    }

    /**
     * @return the dynamicRacingLine
     */
    public int getDynamicRacingLine() {
        return dynamicRacingLine;
    }

    /**
     * @param dynamicRacingLine the dynamicRacingLine to set
     */
    public void setDynamicRacingLine(int dynamicRacingLine) {
        this.dynamicRacingLine = dynamicRacingLine;
    }

    /**
     * @return the dynamicRacingLineType
     */
    public int getDynamicRacingLineType() {
        return dynamicRacingLineType;
    }

    /**
     * @param dynamicRacingLineType the dynamicRacingLineType to set
     */
    public void setDynamicRacingLineType(int dynamicRacingLineType) {
        this.dynamicRacingLineType = dynamicRacingLineType;
    }

}
