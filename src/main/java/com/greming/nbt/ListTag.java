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

import java.util.HashSet;
import java.util.stream.Collectors;

import com.greming.io.ByteBufferReader;
import com.greming.io.ByteBufferWriter;


public class ListTag extends NBTag<HashSet<NBTag>>
{
    
    
    public static final byte TYPE = 9;
    public static final HashSet<NBTag> DEFAULT_VALUE = new HashSet();
    
    protected byte childType;
    
    
    public ListTag() { super(EndTag.TYPE, new HashSet()); }
    
    
    /**
     * @param childType byte 
     * @param value     Set<NBTag>
     */
    public ListTag(byte childType, HashSet<NBTag> value)
    {
        super(TYPE, null);
        setChildType(childType);
        value.stream().forEach(this::addTag);
    }
    
    
    /**
     * @return byte 
     */
    public byte getChildType() { return childType; }
    
    
    /**
     * @param childType byte 
     */
    public final void setChildType(byte childType)
    {
        this.childType = childType;
        value = new HashSet();
    }
    
    
    /**
     * @param tag NBTag 
     */
    public void addTag(NBTag tag)
    {
        if (tag.type == childType)
            value.add(tag);
    }
    
    
    /**
     * @return int
     */
    public int size() { return value.size(); }
    
    
    @Override
    public int tagHash() { return value.stream().map(tag -> tag.tagHash()).reduce((int) type, Integer::sum); }
    
    
    @Override
    public void encodeBody(ByteBufferWriter writer)
    {
        writer.writeByte(childType);
        writer.writeInt32(value.size());
        value.stream().forEach((tag) -> {
            tag.encodeBody(writer);
        });
    }
    
    
    @Override
    public void decodeBody(ByteBufferReader reader)
    {
        setChildType(reader.readByte());
        for (int i = reader.readInt32(); i > 0; i--) {
            NBTag tag = NBTag.getTag(childType);
            
            if (tag instanceof EndTag || tag == null)
                break;
            
            tag.decodeBody(reader);
            addTag((NBTag) tag.clone());
        }
    }
    
    
    @Override 
    public ListTag clone() { return new ListTag(childType, new HashSet(value.stream().map(e -> e.clone()).collect(Collectors.toSet()))); } 
    
    
}
