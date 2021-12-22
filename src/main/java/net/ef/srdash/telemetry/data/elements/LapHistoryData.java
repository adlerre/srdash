/*
 * This program is free software; you can use it, redistribute it
 * and / or modify it under the terms of the GNU General Public License
 * (GPL) as published by the Free Software Foundation; either version 3
 * of the License or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program, in a file called gpl.txt or license.txt.
 * If not, write to the Free Software Foundation Inc.,
 * 59 Temple Place - Suite 330, Boston, MA  02111-1307 USA
 */
package net.ef.srdash.telemetry.data.elements;

/**
 * @author Ren\u00E9 Adler (eagle)
 *
 */
public class LapHistoryData {

    private float lapTime;

    private float sector1Time;

    private float sector2Time;

    private float sector3Time;

    private int lapValidBitFlags;

    /**
     * @return the lapTime
     */
    public float getLapTime() {
        return lapTime;
    }

    /**
     * @param lapTime the lapTime to set
     */
    public void setLapTime(float lapTime) {
        this.lapTime = lapTime;
    }

    /**
     * @return the sector1Time
     */
    public float getSector1Time() {
        return sector1Time;
    }

    /**
     * @param sector1Time the sector1Time to set
     */
    public void setSector1Time(float sector1Time) {
        this.sector1Time = sector1Time;
    }

    /**
     * @return the sector2Time
     */
    public float getSector2Time() {
        return sector2Time;
    }

    /**
     * @param sector2Time the sector2Time to set
     */
    public void setSector2Time(float sector2Time) {
        this.sector2Time = sector2Time;
    }

    /**
     * @return the sector3Time
     */
    public float getSector3Time() {
        return sector3Time;
    }

    /**
     * @param sector3Time the sector3Time to set
     */
    public void setSector3Time(float sector3Time) {
        this.sector3Time = sector3Time;
    }

    /**
     * @return the lapValidBitFlags
     */
    public int getLapValidBitFlags() {
        return lapValidBitFlags;
    }

    /**
     * @param lapValidBitFlags the lapValidBitFlags to set
     */
    public void setLapValidBitFlags(int lapValidBitFlags) {
        this.lapValidBitFlags = lapValidBitFlags;
    }

}
