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
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

import com.greming.io.ByteBufferWriter;
import com.greming.io.ByteBufferReader;
import java.util.Arrays;
import java.util.stream.Collectors;
import java.util.stream.Stream;


/**
 * @param <T>
 */
public abstract class NBTag<T> implements Cloneable
{
    
    
    protected byte type;
    protected T value;
    
    protected static final HashMap<Byte, NBTag> TAGS = new HashMap<>();
    
    static {
        TAGS.put(EndTag.TYPE,       new EndTag());
        TAGS.put(ByteTag.TYPE,      new ByteTag());
        TAGS.put(ShortTag.TYPE,     new ShortTag());
        TAGS.put(IntTag.TYPE,       new IntTag());
        TAGS.put(LongTag.TYPE,      new LongTag());
        TAGS.put(FloatTag.TYPE,     new FloatTag());
        TAGS.put(DoubleTag.TYPE,    new DoubleTag());
        TAGS.put(ByteArrayTag.TYPE, new ByteArrayTag());
        TAGS.put(StringTag.TYPE,    new StringTag());
        TAGS.put(ListTag.TYPE,      new ListTag());
        TAGS.put(CompoundTag.TYPE,  new CompoundTag());
        TAGS.put(IntArrayTag.TYPE,  new IntArrayTag());
        TAGS.put(LongArrayTag.TYPE, new LongArrayTag());
    }
    
    
    /**
     * @param type  int 
     * @param value T
     */
    public NBTag(byte type, T value)
    {
        this.type  = type;
        this.value = value;
    }
    
    
    /**
     * @return byte 
     */
    public byte getType() { return type; }
    
    
    /**
     * @return T
     */
    public T getValue() { return value; }
    
    
    /**
     * @param value T
     */
    public void setValue(T value) { this.value = value; }
    
   
    /**
     * @return int 
     */
    public int tagHash() { return value.hashCode(); }
    
    
    /**
     * @param writer ByteBufferWriter 
     */
    public void encode(ByteBufferWriter writer)
    {
        writer.writeByte(type);
        encodeBody(writer);
    }
    
    
    /**
     * @param writer ByteBufferWriter
     */
    public void encodeBody(ByteBufferWriter writer) { }
    
    
    /**
     * @param reader ByteBufferReader 
     */
    public void decode(ByteBufferReader reader)
    {
        reader.readByte();
        decodeBody(reader);
    }
    
    
    /**
     * @param reader ByteBufferReader
     */
    public void decodeBody(ByteBufferReader reader) { }
    
    
    @Override
    public abstract NBTag clone();
    
    
    /**
     * @param reader ByteBufferReader 
     * @return NBTag
     */
    public static NBTag getTag(ByteBufferReader reader)
    {
        NBTag tag = getTag(reader.readByte());
        tag.decodeBody(reader);
        
        return tag;
    }
    
    
    /**
     * @param type byte
     * @return     NBTag
     */
    public static NBTag getTag(byte type) { return (NBTag) TAGS.getOrDefault(type, new EndTag()).clone(); }
    
    
    /**
     * @param namedtag NamedTag<CompoundTag>
     * @param writer   ByteBufferWriter
     */
    public static void encodeRoot(NamedTag<CompoundTag> namedtag, ByteBufferWriter writer)
    {
        writer.writeByte(CompoundTag.TYPE);
        writer.writeString(namedtag.name);
        namedtag.tag.encodeBody(writer);
    }
    
    
    /**
     * @param reader ByteBufferReader
     * @return       NamedTag<CompoundTag>
     */
    public static NamedTag<CompoundTag> getRoot(ByteBufferReader reader)
    {
        CompoundTag compound = (CompoundTag) getTag(reader.readByte());
        NamedTag<CompoundTag> namedtag = new NamedTag(reader.readString(), compound);
        
        compound.decodeBody(reader);
        return namedtag;
    }
    
    
    /**
     * @param buffer byte[]
     * @return       byte[]
     */
    public static byte[] encodeGZip(byte[] buffer)
    {
        try (
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream(buffer.length);
            GZIPOutputStream gzipStream = new GZIPOutputStream(outputStream);
            ) {
            gzipStream.write(buffer, 0, buffer.length);
            gzipStream.finish();
            return outputStream.toByteArray();
        } catch (Throwable exception) { exception.printStackTrace(); }
        
        return new byte[0];
    }
    
    
    /**
     * @param buffer byte[]
     * @return       byte[]
     */
    public static byte[] decodeGZip(byte[] buffer)
    {
        try (
            ByteArrayInputStream inputStream = new ByteArrayInputStream(buffer);
            GZIPInputStream gzipStream = new GZIPInputStream(inputStream);
            ByteArrayOutputStream output = new ByteArrayOutputStream();
            ) {
            while (gzipStream.available() > 0)
                   output.write(gzipStream.read());
            
            return output.toByteArray();
        } catch (Throwable exception) { exception.printStackTrace(); }
        
        return new byte[0];
    }
    
    
}
