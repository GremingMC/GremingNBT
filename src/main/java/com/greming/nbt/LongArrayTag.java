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


public class LongArrayTag extends NBTag<long[]>
{
    
    
    public static final byte TYPE = 12;
    public static final long[] DEFAULT_VALUE = new long[0];
    
    
    public LongArrayTag() { this(DEFAULT_VALUE); }
    
    
    /**
     * @param value 
     */
    public LongArrayTag(long[] value) { super(TYPE, value); }
    
    
    @Override
    public void encodeBody(ByteBufferWriter writer)
    {
        writer.writeInt32(value.length);
        for (long i : value) writer.writeInt64(i);
    }
    
    
    @Override
    public void decodeBody(ByteBufferReader reader)
    {
        value = new long[reader.readInt32()];
        for (int i = 0; i < value.length; i++) value[i] = reader.readInt64();
    }
    
    
    @Override
    public LongArrayTag clone() { return new LongArrayTag(Arrays.copyOf(value, value.length)); }   
    
        
}
