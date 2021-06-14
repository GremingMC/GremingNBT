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

import java.util.HashMap;

import com.greming.io.ByteBufferReader;
import com.greming.io.ByteBufferWriter;


public class CompoundTag extends NBTag<HashMap<String, NamedTag>>
{

    
    public static final byte TYPE = 10;
    public static final HashMap<String, NamedTag> DEFAULT_VALUE = new HashMap<>();
    
    
    public CompoundTag() { super(TYPE, new HashMap<String, NamedTag>()); }
    
    
    /**
     * @param value HashMap<String, NamedTag>
     */
    public CompoundTag(HashMap<String, NamedTag> value) { super(TYPE, value); }
    
    
    /**
     * @param <T>
     * @param name String
     * @return     T
     */
    public<T extends NBTag> T getTag(String name) { return getTag(name, null); }
    
    
    /**
     * @param <T>
     * @param name         String
     * @param defaultValue T
     * @return             T
     */
    public<T extends NBTag> T getTag(String name, T defaultValue)
    {
        NamedTag tag = value.get(name);
        return (tag == null) ? defaultValue : (T) tag.getTag();
    }

    
    /**
     * @param <T>
     * @param <V>
     * @param name         String  
     * @param defaultValue T
     * @return 
     */
    public<T, V extends NBTag> T getValueOf(String name, T defaultValue)
    {
        V tag = getTag(name);
        return tag == null ? defaultValue : (T) tag.getValue();
    }
    
    
    /**
     * @param name  String
     * @param value NBTag
     */
    public void putTag(String name, NBTag value) { putTag(new NamedTag(name, value)); }
    
    
    /**
     * @param value NamedTag
     */
    public void putTag(NamedTag value)
    {
        if (value.getTag() instanceof EndTag)
            return;
        
        this.value.put(value.getName(), value);
    }
    
    
    /**
     * @return int
     */
    public int size() { return value.size(); }
    
    
    @Override
    public int tagHash() { return value.values().stream().map(namedTag -> namedTag.getTag().tagHash()).reduce((int) type, Integer::sum); }
    
    
    @Override
    public void encodeBody(ByteBufferWriter writer)
    {
        value.values().stream().forEach(namedtag -> {
            writer.writeByte(namedtag.tag.type);
            writer.writeString(namedtag.name);
            namedtag.tag.encodeBody(writer);
        });
        writer.writeByte(EndTag.DEFAULT_VALUE);
    }
    
    
    @Override
    public void decodeBody(ByteBufferReader reader)
    {
        short maxDept = 512;
        
        while (maxDept-- > 0) {
            NBTag tag = NBTag.getTag(reader.readByte());
            
            if (tag instanceof EndTag || tag == null)
                break;
            
            String name = reader.readString();
            
            tag.decodeBody(reader);
            value.put(name, new NamedTag(name, tag));
        }
    }    
    
    
    @Override 
    public CompoundTag clone()
    {
        HashMap<String, NamedTag> map = new HashMap<>();
        value.forEach((name, namedtag) -> map.put(name, namedtag.clone()));
        
        return new CompoundTag(map);
    }
    
    
}
