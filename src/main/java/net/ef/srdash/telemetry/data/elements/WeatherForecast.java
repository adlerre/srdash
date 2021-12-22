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
public class WeatherForecast {

    private SessionType sessionType;

    private int timeOffset;

    private int weather;

    private int trackTemperature;

    private int trackTemperatureChange;

    private int airTemperature;

    private int airTemperatureChange;

    private int rainPercentage;

    /**
     * @return the sessionType
     */
    public SessionType getSessionType() {
        return sessionType;
    }

    /**
     * @param sessionType the sessionType to set
     */
    public void setSessionType(SessionType sessionType) {
        this.sessionType = sessionType;
    }

    /**
     * @return the timeOffset
     */
    public int getTimeOffset() {
        return timeOffset;
    }

    /**
     * @param timeOffset the timeOffset to set
     */
    public void setTimeOffset(int timeOffset) {
        this.timeOffset = timeOffset;
    }

    /**
     * @return the weather
     */
    public int getWeather() {
        return weather;
    }

    /**
     * @param weather the weather to set
     */
    public void setWeather(int weather) {
        this.weather = weather;
    }

    /**
     * @return the trackTemperature
     */
    public int getTrackTemperature() {
        return trackTemperature;
    }

    /**
     * @param trackTemperature the trackTemperature to set
     */
    public void setTrackTemperature(int trackTemperature) {
        this.trackTemperature = trackTemperature;
    }

    /**
     * @return the trackTemperatureChange
     */
    public int getTrackTemperatureChange() {
        return trackTemperatureChange;
    }

    /**
     * @param trackTemperatureChange the trackTemperatureChange to set
     */
    public void setTrackTemperatureChange(int trackTemperatureChange) {
        this.trackTemperatureChange = trackTemperatureChange;
    }

    /**
     * @return the airTemperature
     */
    public int getAirTemperature() {
        return airTemperature;
    }

    /**
     * @param airTemperature the airTemperature to set
     */
    public void setAirTemperature(int airTemperature) {
        this.airTemperature = airTemperature;
    }

    /**
     * @return the airTemperatureChange
     */
    public int getAirTemperatureChange() {
        return airTemperatureChange;
    }

    /**
     * @param airTemperatureChange the airTemperatureChange to set
     */
    public void setAirTemperatureChange(int airTemperatureChange) {
        this.airTemperatureChange = airTemperatureChange;
    }

    /**
     * @return the rainPercentage
     */
    public int getRainPercentage() {
        return rainPercentage;
    }

    /**
     * @param rainPercentage the rainPercentage to set
     */
    public void setRainPercentage(int rainPercentage) {
        this.rainPercentage = rainPercentage;
    }

}
