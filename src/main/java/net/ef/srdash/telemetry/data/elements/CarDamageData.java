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
public class CarDamageData {

    private WheelData<Float> tyresWear;

    private WheelData<Integer> tyresDamage;

    private WheelData<Integer> brakesDamage;

    private int frontLeftWingDamage;

    private int frontRightWingDamage;

    private int rearWingDamage;

    private int floorDamage;

    private int diffuserDamage;

    private int sidepodDamage;

    private int drsFault;

    private int gearBoxDamage;

    private int engineDamage;

    private int engineMGUHWear;

    private int engineESWear;

    private int engineCEWear;

    private int engineICEWear;

    private int engineMGUKWear;

    private int engineTCWear;

    /**
     * @return the tyresWear
     */
    public WheelData<Float> getTyresWear() {
        return tyresWear;
    }

    /**
     * @param tyresWear the tyresWear to set
     */
    public void setTyresWear(WheelData<Float> tyresWear) {
        this.tyresWear = tyresWear;
    }

    /**
     * @return the tyresDamage
     */
    public WheelData<Integer> getTyresDamage() {
        return tyresDamage;
    }

    /**
     * @param tyresDamage the tyresDamage to set
     */
    public void setTyresDamage(WheelData<Integer> tyresDamage) {
        this.tyresDamage = tyresDamage;
    }

    /**
     * @return the brakesDamage
     */
    public WheelData<Integer> getBrakesDamage() {
        return brakesDamage;
    }

    /**
     * @param brakesDamage the brakesDamage to set
     */
    public void setBrakesDamage(WheelData<Integer> brakesDamage) {
        this.brakesDamage = brakesDamage;
    }

    /**
     * @return the frontLeftWingDamage
     */
    public int getFrontLeftWingDamage() {
        return frontLeftWingDamage;
    }

    /**
     * @param frontLeftWingDamage the frontLeftWingDamage to set
     */
    public void setFrontLeftWingDamage(int frontLeftWingDamage) {
        this.frontLeftWingDamage = frontLeftWingDamage;
    }

    /**
     * @return the frontRightWingDamage
     */
    public int getFrontRightWingDamage() {
        return frontRightWingDamage;
    }

    /**
     * @param frontRightWingDamage the frontRightWingDamage to set
     */
    public void setFrontRightWingDamage(int frontRightWingDamage) {
        this.frontRightWingDamage = frontRightWingDamage;
    }

    /**
     * @return the rearWingDamage
     */
    public int getRearWingDamage() {
        return rearWingDamage;
    }

    /**
     * @param rearWingDamage the rearWingDamage to set
     */
    public void setRearWingDamage(int rearWingDamage) {
        this.rearWingDamage = rearWingDamage;
    }

    /**
     * @return the floorDamage
     */
    public int getFloorDamage() {
        return floorDamage;
    }

    /**
     * @param floorDamage the floorDamage to set
     */
    public void setFloorDamage(int floorDamage) {
        this.floorDamage = floorDamage;
    }

    /**
     * @return the diffuserDamage
     */
    public int getDiffuserDamage() {
        return diffuserDamage;
    }

    /**
     * @param diffuserDamage the diffuserDamage to set
     */
    public void setDiffuserDamage(int diffuserDamage) {
        this.diffuserDamage = diffuserDamage;
    }

    /**
     * @return the sidepodDamage
     */
    public int getSidepodDamage() {
        return sidepodDamage;
    }

    /**
     * @param sidepodDamage the sidepodDamage to set
     */
    public void setSidepodDamage(int sidepodDamage) {
        this.sidepodDamage = sidepodDamage;
    }

    /**
     * @return the drsFault
     */
    public int getDrsFault() {
        return drsFault;
    }

    /**
     * @param drsFault the drsFault to set
     */
    public void setDrsFault(int drsFault) {
        this.drsFault = drsFault;
    }

    /**
     * @return the gearBoxDamage
     */
    public int getGearBoxDamage() {
        return gearBoxDamage;
    }

    /**
     * @param gearBoxDamage the gearBoxDamage to set
     */
    public void setGearBoxDamage(int gearBoxDamage) {
        this.gearBoxDamage = gearBoxDamage;
    }

    /**
     * @return the engineDamage
     */
    public int getEngineDamage() {
        return engineDamage;
    }

    /**
     * @param engineDamage the engineDamage to set
     */
    public void setEngineDamage(int engineDamage) {
        this.engineDamage = engineDamage;
    }

    /**
     * @return the engineMGUHWear
     */
    public int getEngineMGUHWear() {
        return engineMGUHWear;
    }

    /**
     * @param engineMGUHWear the engineMGUHWear to set
     */
    public void setEngineMGUHWear(int engineMGUHWear) {
        this.engineMGUHWear = engineMGUHWear;
    }

    /**
     * @return the engineESWear
     */
    public int getEngineESWear() {
        return engineESWear;
    }

    /**
     * @param engineESWear the engineESWear to set
     */
    public void setEngineESWear(int engineESWear) {
        this.engineESWear = engineESWear;
    }

    /**
     * @return the engineCEWear
     */
    public int getEngineCEWear() {
        return engineCEWear;
    }

    /**
     * @param engineCEWear the engineCEWear to set
     */
    public void setEngineCEWear(int engineCEWear) {
        this.engineCEWear = engineCEWear;
    }

    /**
     * @return the engineICEWear
     */
    public int getEngineICEWear() {
        return engineICEWear;
    }

    /**
     * @param engineICEWear the engineICEWear to set
     */
    public void setEngineICEWear(int engineICEWear) {
        this.engineICEWear = engineICEWear;
    }

    /**
     * @return the engineMGUKWear
     */
    public int getEngineMGUKWear() {
        return engineMGUKWear;
    }

    /**
     * @param engineMGUKWear the engineMGUKWear to set
     */
    public void setEngineMGUKWear(int engineMGUKWear) {
        this.engineMGUKWear = engineMGUKWear;
    }

    /**
     * @return the engineTCWear
     */
    public int getEngineTCWear() {
        return engineTCWear;
    }

    /**
     * @param engineTCWear the engineTCWear to set
     */
    public void setEngineTCWear(int engineTCWear) {
        this.engineTCWear = engineTCWear;
    }

}
