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
public class TyreStintHistoryData {

    private int endLap;

    private int tyreActualCompound;

    private int tyreVisualCompound;

    /**
     * @return the endLap
     */
    public int getEndLap() {
        return endLap;
    }

    /**
     * @param endLap the endLap to set
     */
    public void setEndLap(int endLap) {
        this.endLap = endLap;
    }

    /**
     * @return the tyreActualCompound
     */
    public int getTyreActualCompound() {
        return tyreActualCompound;
    }

    /**
     * @param tyreActualCompound the tyreActualCompound to set
     */
    public void setTyreActualCompound(int tyreActualCompound) {
        this.tyreActualCompound = tyreActualCompound;
    }

    /**
     * @return the tyreVisualCompound
     */
    public int getTyreVisualCompound() {
        return tyreVisualCompound;
    }

    /**
     * @param tyreVisualCompound the tyreVisualCompound to set
     */
    public void setTyreVisualCompound(int tyreVisualCompound) {
        this.tyreVisualCompound = tyreVisualCompound;
    }

}
