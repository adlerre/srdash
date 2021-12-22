package net.ef.srdash.telemetry.data;

import java.util.List;

import net.ef.srdash.telemetry.data.elements.LapHistoryData;
import net.ef.srdash.telemetry.data.elements.TyreStintHistoryData;

public class PacketSessionHistoryData extends Packet {

    private int carIdx;

    private int numLaps;

    private int numTyreStints;

    private int bestLapTimeLapNum;

    private int bestSector1LapNum;

    private int bestSector2LapNum;

    private int bestSector3LapNum;

    private List<LapHistoryData> lapHistoryData;

    private List<TyreStintHistoryData> tyreStintsHistoryData;

    public PacketSessionHistoryData() {
    }

    /**
     * @return the carIdx
     */
    public int getCarIdx() {
        return carIdx;
    }

    /**
     * @param carIdx the carIdx to set
     */
    public void setCarIdx(int carIdx) {
        this.carIdx = carIdx;
    }

    /**
     * @return the numLaps
     */
    public int getNumLaps() {
        return numLaps;
    }

    /**
     * @param numLaps the numLaps to set
     */
    public void setNumLaps(int numLaps) {
        this.numLaps = numLaps;
    }

    /**
     * @return the numTyreStints
     */
    public int getNumTyreStints() {
        return numTyreStints;
    }

    /**
     * @param numTyreStints the numTyreStints to set
     */
    public void setNumTyreStints(int numTyreStints) {
        this.numTyreStints = numTyreStints;
    }

    /**
     * @return the bestLapTimeLapNum
     */
    public int getBestLapTimeLapNum() {
        return bestLapTimeLapNum;
    }

    /**
     * @param bestLapTimeLapNum the bestLapTimeLapNum to set
     */
    public void setBestLapTimeLapNum(int bestLapTimeLapNum) {
        this.bestLapTimeLapNum = bestLapTimeLapNum;
    }

    /**
     * @return the bestSector1LapNum
     */
    public int getBestSector1LapNum() {
        return bestSector1LapNum;
    }

    /**
     * @param bestSector1LapNum the bestSector1LapNum to set
     */
    public void setBestSector1LapNum(int bestSector1LapNum) {
        this.bestSector1LapNum = bestSector1LapNum;
    }

    /**
     * @return the bestSector2LapNum
     */
    public int getBestSector2LapNum() {
        return bestSector2LapNum;
    }

    /**
     * @param bestSector2LapNum the bestSector2LapNum to set
     */
    public void setBestSector2LapNum(int bestSector2LapNum) {
        this.bestSector2LapNum = bestSector2LapNum;
    }

    /**
     * @return the bestSector3LapNum
     */
    public int getBestSector3LapNum() {
        return bestSector3LapNum;
    }

    /**
     * @param bestSector3LapNum the bestSector3LapNum to set
     */
    public void setBestSector3LapNum(int bestSector3LapNum) {
        this.bestSector3LapNum = bestSector3LapNum;
    }

    /**
     * @return the lapHistoryData
     */
    public List<LapHistoryData> getLapHistoryData() {
        return lapHistoryData;
    }

    /**
     * @param lapHistoryData the lapHistoryData to set
     */
    public void setLapHistoryData(List<LapHistoryData> lapHistoryData) {
        this.lapHistoryData = lapHistoryData;
    }

    /**
     * @return the tyreStintsHistoryData
     */
    public List<TyreStintHistoryData> getTyreStintsHistoryData() {
        return tyreStintsHistoryData;
    }

    /**
     * @param tyreStintsHistoryData the tyreStintsHistoryData to set
     */
    public void setTyreStintsHistoryData(List<TyreStintHistoryData> tyreStintsHistoryData) {
        this.tyreStintsHistoryData = tyreStintsHistoryData;
    }

}
