/*
 * Copyright (C) 2017 Universitat Autonoma de Barcelona - David Castells-Rufas <david.castells@uab.cat>
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package cat.uab.cephis;

/**
 *
 * @author dcr
 */
public class ListUtils
{

    public static int minNonNeg(int... vals)
    {
        int min = -1;
        
        for (int val : vals)
        {
            if (val >= 0)
            {
                if (min >= 0)
                    min = Math.min(min, val);
                else
                    min = val;
            }
        }
        
        return min;
    }
    
}
