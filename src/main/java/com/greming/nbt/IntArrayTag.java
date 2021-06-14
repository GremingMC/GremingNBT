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

import java.util.Arrays;

import com.greming.io.ByteBufferReader;
import com.greming.io.ByteBufferWriter;


public class IntArrayTag extends NBTag<int[]>
{
    
    
    public static final byte TYPE = 11;
    public static final int[] DEFAULT_VALUE = new int[0];
    
    
    public IntArrayTag() { this(DEFAULT_VALUE); }
    
    
    /**
     * @param value 
     */
    public IntArrayTag(int[] value) { super(TYPE, value); }
    
    
    @Override
    public void encodeBody(ByteBufferWriter writer)
    {
        writer.writeInt32(value.length);
        for (int i : value) writer.writeInt32(i);
    }
    
    
    @Override
    public void decodeBody(ByteBufferReader reader)
    {
        value = new int[reader.readInt32()];
        for (int i = 0; i < value.length; i++) value[i] = reader.readInt32();
    }
    
    
    @Override
    public IntArrayTag clone() { return new IntArrayTag(Arrays.copyOf(value, value.length)); }   
    
    
}
