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

import com.greming.io.ByteBufferReader;
import com.greming.io.ByteBufferWriter;


public class FloatTag extends NBTag<Float>
{
    
    
    public static final byte TYPE = 5;
    public static final float DEFAULT_VALUE = 0f;
    
    
    public FloatTag() { this(DEFAULT_VALUE); }
    
    
    /**
     * @param value float 
     */
    public FloatTag(float value) { super(TYPE, value); }
    
    
    @Override
    public void encodeBody(ByteBufferWriter writer) { writer.writeFloat32(value); }
    
    
    @Override
    public void decodeBody(ByteBufferReader reader) { value = reader.readFloat32(); }
    
    
    @Override
    public FloatTag clone() { return new FloatTag(value); }
    
    
}
