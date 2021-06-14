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

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;

import com.greming.io.*;
import java.io.InputStream;
import java.net.URL;


public class NBTest
{
   
    @Test
    public void test()
    {
        test(ByteBufferType.VarInt);
        test(ByteBufferType.LittleEndian);
        test(ByteBufferType.BigEndian);
        testOnline();
    }
    
    protected void test(ByteBufferType bbtype)
    {
        byte bytetag = 100;
        short shorttag = 3345;
        int inttag = 489434;
        long longtag = 83849384394L;
        float floattag = 843434F;
        double doubletag = 849242844.78734D;
        byte[] bytearraytag = new byte[] { 1, 2, 3, 4, 5, 6, 7, 8, 9 };
        int[] intarraytag = new int[] { 345, 6778, 43334, 58953, 57575, 222 };
        long[] longarraytag = new long[] { 3489L, 747274478L, 7787424L, 732783L, 11L };
        String stringtag = "StringTag Test";
        
        CompoundTag compound = new CompoundTag();
        compound.putTag("ByteTag", new ByteTag(bytetag));
        compound.putTag("ShortTag", new ShortTag(shorttag));
        compound.putTag("IntTag", new IntTag(inttag));
        compound.putTag("LongTag", new LongTag(longtag));
        compound.putTag("FloatTag", new FloatTag(floattag));
        compound.putTag("DoubleTag", new DoubleTag(doubletag));
        compound.putTag("ByteArrayTag", new ByteArrayTag(bytearraytag));
        compound.putTag("IntArrayTag", new IntArrayTag(intarraytag));
        compound.putTag("LongArrayTag", new LongArrayTag(longarraytag));
        compound.putTag("StringTag", new StringTag(stringtag));
        
        ByteBuffer buffer = new ByteBuffer(64);
        ByteBufferReader reader = new ByteBufferReader(buffer, bbtype);
        ByteBufferWriter writer = new ByteBufferWriter(buffer, bbtype);
        
        compound.encode(writer);
        
        compound = (CompoundTag) NBTag.getTag(reader);
        
        Assertions.assertEquals(bytetag, compound.getValueOf("ByteTag", ByteTag.DEFAULT_VALUE));
        Assertions.assertEquals(shorttag, compound.getValueOf("ShortTag", ShortTag.DEFAULT_VALUE));
        Assertions.assertEquals(inttag, compound.getValueOf("IntTag", IntTag.DEFAULT_VALUE));
        Assertions.assertEquals(longtag, compound.getValueOf("LongTag", LongTag.DEFAULT_VALUE));
        Assertions.assertEquals(floattag, compound.getValueOf("FloatTag", FloatTag.DEFAULT_VALUE));
        Assertions.assertEquals(doubletag, compound.getValueOf("DoubleTag", DoubleTag.DEFAULT_VALUE));
        Assertions.assertArrayEquals(bytearraytag, compound.getValueOf("ByteArrayTag", ByteArrayTag.DEFAULT_VALUE));
        Assertions.assertArrayEquals(intarraytag, compound.getValueOf("IntArrayTag", IntArrayTag.DEFAULT_VALUE));
        Assertions.assertArrayEquals(longarraytag, compound.getValueOf("LongArrayTag", LongArrayTag.DEFAULT_VALUE));
        Assertions.assertEquals(stringtag, compound.getValueOf("StringTag", StringTag.DEFAULT_VALUE));
    }
    
    protected void testOnline()
    {
        byte[] original = new byte[0];
        
        try(InputStream stream = new URL("https://raw.github.com/Dav1dde/nbd/master/test/bigtest.nbt").openStream()) {
            original = stream.readAllBytes();
        } catch (Throwable exception) {
            exception.printStackTrace();
            return;
        }
        
        ByteBuffer buffer = new ByteBuffer(NBTag.decodeGZip(original));
            
        ByteBufferReader reader = new ByteBufferReader(buffer);
        NamedTag<CompoundTag> namedtag = NBTag.getRoot(reader);
        testRoot(namedtag);
            
            
        ByteBuffer encoded = new ByteBuffer(64);
        ByteBufferWriter writer = new ByteBufferWriter(encoded);
        NBTag.encodeRoot(namedtag, writer);
        
        encoded.setBytes(NBTag.decodeGZip(NBTag.encodeGZip(writer.slice())));

        reader = new ByteBufferReader(encoded);
        testRoot(NBTag.getRoot(reader));
        
        Assertions.assertEquals(buffer.getBytes().length, encoded.getBytes().length);
    }
    
    
    protected void testRoot(NamedTag<CompoundTag> namedtag)
    {
        Assertions.assertEquals(namedtag.name, "Level");
        CompoundTag compound = namedtag.getTag();
        
        Assertions.assertEquals(2, compound.getValueOf("nested compound test", CompoundTag.DEFAULT_VALUE).size());
        Assertions.assertEquals(2147483647, compound.getValueOf("intTest", IntTag.DEFAULT_VALUE));
        Assertions.assertEquals((byte) 127, compound.getValueOf("byteTest", ByteTag.DEFAULT_VALUE));
        Assertions.assertEquals("HELLO WORLD THIS IS A TEST STRING " + ((char) 0xc5) + ((char) 0xc4) + ((char) 0xd6) + "!", compound.getValueOf("stringTest", StringTag.DEFAULT_VALUE));
        Assertions.assertEquals(5, compound.getValueOf("listTest (long)", ListTag.DEFAULT_VALUE).size());
        Assertions.assertEquals(0.49312871321823148d, compound.getValueOf("doubleTest", DoubleTag.DEFAULT_VALUE));
        Assertions.assertEquals(0.49823147058486938f, compound.getValueOf("floatTest", FloatTag.DEFAULT_VALUE));
        Assertions.assertEquals(9223372036854775807L, compound.getValueOf("longTest", LongTag.DEFAULT_VALUE));
        Assertions.assertEquals(2, compound.getValueOf("listTest (compound)", ListTag.DEFAULT_VALUE).size());
        Assertions.assertEquals(1000, compound.getValueOf("byteArrayTest (the first 1000 values of (n*n*255+n*7)%100, starting with n=0 (0, 62, 34, 16, 8, ...))", ByteArrayTag.DEFAULT_VALUE).length);
        Assertions.assertEquals((short) 32767, compound.getValueOf("shortTest", ShortTag.DEFAULT_VALUE));
    }
    
   
}
