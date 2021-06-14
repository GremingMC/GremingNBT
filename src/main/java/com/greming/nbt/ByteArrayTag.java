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


public class ByteArrayTag extends NBTag<byte[]>
{
    
    
    public static final byte TYPE = 7;
    public static final byte[] DEFAULT_VALUE = new byte[0];
    
    
    public ByteArrayTag() { this(DEFAULT_VALUE); }
    
    
    /**
     * @param value byte[]
     */
    public ByteArrayTag(byte[] value) { super(TYPE, value); }
    
    
    @Override
    public void encodeBody(ByteBufferWriter writer)
    {
        writer.writeInt32(value.length);
        writer.put(value);
    }
    
    
    @Override
    public void decodeBody(ByteBufferReader reader) { value = reader.get(reader.readInt32()); }
    
    
    @Override
    public ByteArrayTag clone() { return new ByteArrayTag(Arrays.copyOf(value, value.length)); }

    
}
