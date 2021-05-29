package net.ef.srdash.telemetry.data.elements;

import java.math.BigInteger;

public class Header {

    private int packetFormat;

    private int gameMajorVersion;

    private int gameMinorVersion;

    private long packetVersion;

    private int packetId;

    private BigInteger sessionUID;

    private float sessionTime;

    private long frameIdentifier;

    private int playerCarIndex;

    public Header() {
    }

    public int getPacketFormat() {
        return packetFormat;
    }

    public void setPacketFormat(int packetFormat) {
        this.packetFormat = packetFormat;
    }

    /**
     * @return the gameMajorVersion
     * @since 2019
     */
    public int getGameMajorVersion() {
        return gameMajorVersion;
    }

    /**
     * @param gameMajorVersion the gameMajorVersion to set
     * @since 2019
     */
    public void setGameMajorVersion(int gameMajorVersion) {
        this.gameMajorVersion = gameMajorVersion;
    }

    /**
     * @return the gameMinorVersion
     * @since 2019
     */
    public int getGameMinorVersion() {
        return gameMinorVersion;
    }

    /**
     * @param gameMinorVersion the gameMinorVersion to set
     * @since 2019
     */
    public void setGameMinorVersion(int gameMinorVersion) {
        this.gameMinorVersion = gameMinorVersion;
    }

    public long getPacketVersion() {
        return packetVersion;
    }

    public void setPacketVersion(long packetVersion) {
        this.packetVersion = packetVersion;
    }

    public int getPacketId() {
        return packetId;
    }

    public void setPacketId(int packetId) {
        this.packetId = packetId;
    }

    public BigInteger getSessionUID() {
        return sessionUID;
    }

    public void setSessionUID(BigInteger sessionUID) {
        this.sessionUID = sessionUID;
    }

    public float getSessionTime() {
        return sessionTime;
    }

    public void setSessionTime(float sessionTime) {
        this.sessionTime = sessionTime;
    }

    public long getFrameIdentifier() {
        return frameIdentifier;
    }

    public void setFrameIdentifier(long frameIdentifier) {
        this.frameIdentifier = frameIdentifier;
    }

    public int getPlayerCarIndex() {
        return playerCarIndex;
    }

    public void setPlayerCarIndex(int playerCarIndex) {
        this.playerCarIndex = playerCarIndex;
    }

    @Override
    public String toString() {
        return "Header :: packetFormat:" + this.getPacketFormat() +
                ", version:" + this.getPacketVersion() +
                ", packetId:" + this.getPacketId() +
                ", sessionUID:" + this.getSessionUID() +
                ", sessionTime:" + this.getSessionTime() +
                ", frameIdentifier:" + this.getFrameIdentifier() +
                ", playerCarIndex:" + this.getPlayerCarIndex();
    }
}
