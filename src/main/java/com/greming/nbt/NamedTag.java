/**
 *
 *    ____                    _
 *   / ___|_ __ ___ _ __ ___ (_)_ __   __ _
 *  | |  _| '__/ _ \ '_ ` _ \| | '_ \ / _` |
 *  | |_| | | |  __/ | | | | | | | | | (_| |
 *   \____|_|  \___|_| |_| |_|_|_| |_|\__, |
 *                                    |___/
 *
 * This file is part of Greming.
 *
 * Greming is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Greming is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Greming. If not, see <https://www.gnu.org/licenses/>.
 *
 * @author Brayan Roman
 *
 */

package com.greming.nbt;


/**
 * @param <T>
 */
public class NamedTag<T extends NBTag> implements Cloneable
{
    
    
    protected String name;
    protected T tag;
    
    
    /**
     * 
     * @param name
     * @param tag 
     */
    public NamedTag(String name, T tag)
    {
        this.name = name;
        this.tag  = tag;
    }
    
    
    /**
     * @return String
     */
    public String getName() { return name; }
    
    
    /**
     * @return T 
     */
    public T getTag() { return tag; }

    
    @Override
    public NamedTag<T> clone() { return new NamedTag(name, (T) tag.clone()); }
    
    
}
