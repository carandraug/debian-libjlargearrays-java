/* ***** BEGIN LICENSE BLOCK *****
 * JLargeArrays
 * Copyright (C) 2013 onward University of Warsaw, ICM
 * All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 * 
 * 1. Redistributions of source code must retain the above copyright notice, this
 *    list of conditions and the following disclaimer. 
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR
 * ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 *
 * ***** END LICENSE BLOCK ***** */
package pl.edu.icm.jlargearrays;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * Unit tests.
 *
 * @author Piotr Wendykier (p.wendykier@icm.edu.pl)
 */
public class JLargeArraysTest extends TestCase
{

    /**
     * Create the test case
     *
     * @param testName name of the test case
     */
    public JLargeArraysTest(String testName)
    {
        super(testName);
    }

    /**
     * @return the suite of tests being tested
     */
    public static Test suite()
    {
        return new TestSuite(JLargeArraysTest.class);
    }

    public void testLogicLargeArrayEqualsHashCode()
    {
        LogicLargeArray a = new LogicLargeArray(10);
        LogicLargeArray b = new LogicLargeArray(10);
        assertTrue(a.equals(a));
        assertTrue(a.hashCode() == a.hashCode());
        assertFalse(a.equals(b));
        assertFalse(a.hashCode() == b.hashCode());
        LargeArray.setMaxSizeOf32bitArray(1);
        a = new LogicLargeArray(10);
        b = new LogicLargeArray(10);
        assertTrue(a.equals(a));
        assertTrue(a.hashCode() == a.hashCode());
        assertFalse(a.equals(b));
        assertFalse(a.hashCode() == b.hashCode());
    }

    public void testLogicLargeArrayConstant()
    {
        LogicLargeArray a = new LogicLargeArray(1l << 33, (byte) 1);
        assertTrue(a.getBoolean(0));
        assertTrue(a.getBoolean(a.length() - 1));
        Throwable e = null;
        try {
            a.setBoolean(0, false);
        } catch (IllegalAccessError ex) {
            e = ex;
        }
        assertTrue(e instanceof IllegalAccessError);
    }

    public void testLogicLargeArrayGetSet()
    {
        LargeArray.setMaxSizeOf32bitArray(1073741824);
        LogicLargeArray a = new LogicLargeArray(10);
        long idx = 5;
        byte val = 1;
        a.set(idx, val);
        assertEquals(val, a.getByte(idx));
        idx = 6;
        a.set(idx, val);
        assertEquals(val, (byte) a.get(idx));
        LargeArray.setMaxSizeOf32bitArray(1);
        a = new LogicLargeArray(10);
        a.set(idx, val);
        assertEquals(val, a.getByte(idx));
        idx = 6;
        a.set(idx, val);
        assertEquals(val, (byte) a.get(idx));

    }

    public void testLogicLargeArrayGetSetNative()
    {
        LargeArray.setMaxSizeOf32bitArray(1);
        LogicLargeArray a = new LogicLargeArray(10);
        long idx = 5;
        Byte val = 1;
        a.setToNative(idx, val);
        assertEquals(val, a.getFromNative(idx));
    }

    public void testLogicLargeArrayGetData()
    {
        boolean[] data = new boolean[]{true, false, false, false, true, true, true, false, true, true};
        long startPos = 2;
        long endPos = 7;
        long step = 2;
        LargeArray.setMaxSizeOf32bitArray(1 << 30);
        LogicLargeArray a = new LogicLargeArray(data);
        boolean[] res = a.getBooleanData(null, startPos, endPos, step);
        int idx = 0;
        for (long i = startPos; i < endPos; i += step) {
            assertEquals(data[(int) i], res[idx++]);
        }
        LargeArray.setMaxSizeOf32bitArray(1);
        a = new LogicLargeArray(data);
        res = a.getBooleanData(null, startPos, endPos, step);
        idx = 0;
        for (long i = startPos; i < endPos; i += step) {
            assertEquals(data[(int) i], res[idx++]);
        }
    }

    public void testLogicLargeArrayArraycopy()
    {
        boolean[] data = new boolean[1000000];
        for (int i = 0; i < data.length; i++) {
            data[i] = i % 5 == 0;
        }
        int startPos = 2;
        int length = data.length - 2;
        LargeArray.setMaxSizeOf32bitArray(1073741824);
        LogicLargeArray a = new LogicLargeArray(data);
        LogicLargeArray b = new LogicLargeArray(2 * data.length);
        LargeArrayUtils.arraycopy(a, startPos, b, 0, length);
        for (int i = 0; i < length; i++) {
            assertEquals(data[startPos + i], b.getBoolean(i));
        }
        b = new LogicLargeArray(2 * data.length);
        LargeArrayUtils.arraycopy(data, startPos, b, 0, length);
        for (int i = 0; i < length; i++) {
            assertEquals(data[startPos + i], b.getBoolean(i));
        }
        LargeArray.setMaxSizeOf32bitArray(data.length - 1);
        a = new LogicLargeArray(data);
        b = new LogicLargeArray(2 * data.length);
        LargeArrayUtils.arraycopy(a, startPos, b, 0, length);
        for (int i = 0; i < length; i++) {
            assertEquals(data[startPos + i], b.getBoolean(i));
        }
        b = new LogicLargeArray(2 * data.length);
        LargeArrayUtils.arraycopy(data, startPos, b, 0, length);
        for (int i = 0; i < length; i++) {
            assertEquals(data[startPos + i], b.getBoolean(i));
        }
    }

    public void testLogicLargeArrayConvert()
    {
        boolean[] data = new boolean[]{true, false, false, false, true, true, true, false, true, true};
        LogicLargeArray a = new LogicLargeArray(data);
        ByteLargeArray b = (ByteLargeArray) LargeArrayUtils.convert(a, LargeArrayType.BYTE);
        for (int i = 0; i < data.length; i++) {
            assertEquals(data[i] == true ? 1 : 0, b.getByte(i));
        }
    }

    public void testLogicLargeArrayNot()
    {
        LargeArray.setMaxSizeOf32bitArray(1073741824);
        LogicLargeArray a = new LogicLargeArray(10);
        long idx = 5;
        boolean val = true;
        a.setBoolean(idx, val);
        LogicLargeArray b = a.not();
        for (int i = 0; i < a.length; i++) {
            assertEquals(1 - a.getByte(i), b.getByte(i));
        }
    }

    public void testLogicLargeArrayXor()
    {
        LargeArray.setMaxSizeOf32bitArray(1073741824);
        LogicLargeArray a = new LogicLargeArray(10);
        long idx = 5;
        boolean val = true;
        a.setBoolean(idx, val);
        LogicLargeArray b = a.xor(a);
        for (int i = 0; i < a.length; i++) {
            assertEquals(a.getByte(i) ^ a.getByte(i), b.getByte(i));
        }
    }

    public void testLogicLargeArrayOr()
    {
        LargeArray.setMaxSizeOf32bitArray(1073741824);
        LogicLargeArray a = new LogicLargeArray(10);
        long idx = 5;
        boolean val = true;
        a.setBoolean(idx, val);
        LogicLargeArray b = a.or(a);
        for (int i = 0; i < a.length; i++) {
            assertEquals(a.getByte(i) | a.getByte(i), b.getByte(i));
        }
    }

    public void testLogicLargeArrayAnd()
    {
        LargeArray.setMaxSizeOf32bitArray(1073741824);
        LogicLargeArray a = new LogicLargeArray(10);
        long idx = 5;
        boolean val = true;
        a.setBoolean(idx, val);
        LogicLargeArray b = a.and(a);
        for (int i = 0; i < a.length; i++) {
            assertEquals(a.getByte(i) & a.getByte(i), b.getByte(i));
        }
    }

    public void testByteLargeArrayEqualsHashCode()
    {
        ByteLargeArray a = new ByteLargeArray(10);
        ByteLargeArray b = new ByteLargeArray(10);
        assertTrue(a.equals(a));
        assertTrue(a.hashCode() == a.hashCode());
        assertFalse(a.equals(b));
        assertFalse(a.hashCode() == b.hashCode());
        LargeArray.setMaxSizeOf32bitArray(1);
        a = new ByteLargeArray(10);
        b = new ByteLargeArray(10);
        assertTrue(a.equals(a));
        assertTrue(a.hashCode() == a.hashCode());
        assertFalse(a.equals(b));
        assertFalse(a.hashCode() == b.hashCode());
    }

    public void testByteLargeArrayConstant()
    {
        ByteLargeArray a = new ByteLargeArray(1l << 33, (byte) 2);
        assertEquals(2, a.getByte(0));
        assertEquals(2, a.getByte(a.length() - 1));
        Throwable e = null;
        try {
            a.setByte(0, (byte) 3);
        } catch (IllegalAccessError ex) {
            e = ex;
        }
        assertTrue(e instanceof IllegalAccessError);
    }

    public void testByteLargeArrayGetSet()
    {
        LargeArray.setMaxSizeOf32bitArray(1073741824);
        ByteLargeArray a = new ByteLargeArray(10);
        long idx = 5;
        byte val = -100;
        a.setByte(idx, val);
        assertEquals(val, a.getByte(idx));
        idx = 6;
        a.set(idx, val);
        assertEquals(val, (a.get(idx)).byteValue());
        LargeArray.setMaxSizeOf32bitArray(1);
        a = new ByteLargeArray(10);
        a.setByte(idx, val);
        assertEquals(val, a.getByte(idx));
        idx = 6;
        a.set(idx, val);
        assertEquals(val, (a.get(idx)).byteValue());
    }

    public void testByteLargeArrayGetSetNative()
    {
        LargeArray.setMaxSizeOf32bitArray(1);
        ByteLargeArray a = new ByteLargeArray(10);
        long idx = 5;
        byte val = -100;
        a.setToNative(idx, val);
        assertEquals(val, (byte) a.getFromNative(idx));
    }

    public void testByteLargeArrayGetData()
    {
        byte[] data = new byte[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10};
        long startPos = 2;
        long endPos = 7;
        long step = 2;
        LargeArray.setMaxSizeOf32bitArray(1 << 30);
        ByteLargeArray a = new ByteLargeArray(data);
        byte[] res = a.getByteData(null, startPos, endPos, step);
        int idx = 0;
        for (long i = startPos; i < endPos; i += step) {
            assertEquals(data[(int) i], res[idx++]);
        }
        LargeArray.setMaxSizeOf32bitArray(1);
        a = new ByteLargeArray(data);
        res = a.getByteData(null, startPos, endPos, step);
        idx = 0;
        for (long i = startPos; i < endPos; i += step) {
            assertEquals(data[(int) i], res[idx++]);
        }
    }

    public void testByteLargeArrayArraycopy()
    {
        byte[] data = new byte[]{-1, -2, -3, -4, -5, 6, 7, 8, 9, 10};
        int startPos = 2;
        int length = 8;
        LargeArray.setMaxSizeOf32bitArray(1073741824);
        ByteLargeArray a = new ByteLargeArray(data);
        ByteLargeArray b = new ByteLargeArray(2 * data.length);
        LargeArrayUtils.arraycopy(a, startPos, b, 0, length);
        for (int i = 0; i < length; i++) {
            assertEquals(data[startPos + i], b.getByte(i));
        }
        b = new ByteLargeArray(2 * data.length);
        LargeArrayUtils.arraycopy(data, startPos, b, 0, length);
        for (int i = 0; i < length; i++) {
            assertEquals(data[startPos + i], b.getByte(i));
        }
        LargeArray.setMaxSizeOf32bitArray(data.length - 1);
        a = new ByteLargeArray(data);
        b = new ByteLargeArray(2 * data.length);
        LargeArrayUtils.arraycopy(a, startPos, b, 0, length);
        for (int i = 0; i < length; i++) {
            assertEquals(data[startPos + i], b.getByte(i));
        }
        b = new ByteLargeArray(2 * data.length);
        LargeArrayUtils.arraycopy(data, startPos, b, 0, length);
        for (int i = 0; i < length; i++) {
            assertEquals(data[startPos + i], b.getByte(i));
        }
    }

    public void testByteLargeArrayConvert()
    {
        byte[] data = new byte[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10};
        ByteLargeArray a = new ByteLargeArray(data);
        ShortLargeArray b = (ShortLargeArray) LargeArrayUtils.convert(a, LargeArrayType.SHORT);
        for (int i = 0; i < data.length; i++) {
            assertEquals(data[i], b.getShort(i));
        }
    }
    
    public void testUnsignedByteLargeArrayEqualsHashCode()
    {
        UnsignedByteLargeArray a = new UnsignedByteLargeArray(10);
        UnsignedByteLargeArray b = new UnsignedByteLargeArray(10);
        assertTrue(a.equals(a));
        assertTrue(a.hashCode() == a.hashCode());
        assertFalse(a.equals(b));
        assertFalse(a.hashCode() == b.hashCode());
        LargeArray.setMaxSizeOf32bitArray(1);
        a = new UnsignedByteLargeArray(10);
        b = new UnsignedByteLargeArray(10);
        assertTrue(a.equals(a));
        assertTrue(a.hashCode() == a.hashCode());
        assertFalse(a.equals(b));
        assertFalse(a.hashCode() == b.hashCode());
    }

    public void testUnsignedByteLargeArrayConstant()
    {
        UnsignedByteLargeArray a = new UnsignedByteLargeArray(1l << 33, (short) 225);
        assertEquals(225, a.getUnsignedByte(0));
        assertEquals(225, a.getUnsignedByte(a.length() - 1));
        Throwable e = null;
        try {
            a.setUnsignedByte(0, (short) 200);
        } catch (IllegalAccessError ex) {
            e = ex;
        }
        assertTrue(e instanceof IllegalAccessError);
    }

    public void testUnsignedByteLargeArrayGetSet()
    {
        LargeArray.setMaxSizeOf32bitArray(1073741824);
        UnsignedByteLargeArray a = new UnsignedByteLargeArray(10);
        long idx = 5;
        short val = 255;
        a.setUnsignedByte(idx, val);
        assertEquals(val, a.getUnsignedByte(idx));
        idx = 6;
        a.set(idx, val);
        assertEquals(val, (a.get(idx)).shortValue());
        LargeArray.setMaxSizeOf32bitArray(1);
        a = new UnsignedByteLargeArray(10);
        a.setUnsignedByte(idx, val);
        assertEquals(val, a.getUnsignedByte(idx));
        idx = 6;
        a.set(idx, val);
        assertEquals(val, (a.get(idx)).shortValue());
    }

    public void testUnsignedByteLargeArrayGetSetNative()
    {
        LargeArray.setMaxSizeOf32bitArray(1);
        UnsignedByteLargeArray a = new UnsignedByteLargeArray(10);
        long idx = 5;
        byte val = -100;
        a.setToNative(idx, val);
        assertEquals(val, (byte) a.getFromNative(idx));
    }

    public void testUnsignedByteLargeArrayGetData()
    {
        byte[] data = new byte[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10};
        long startPos = 2;
        long endPos = 7;
        long step = 2;
        LargeArray.setMaxSizeOf32bitArray(1 << 30);
        UnsignedByteLargeArray a = new UnsignedByteLargeArray(data);
        byte[] res = a.getByteData(null, startPos, endPos, step);
        int idx = 0;
        for (long i = startPos; i < endPos; i += step) {
            assertEquals(data[(int) i], res[idx++]);
        }
        LargeArray.setMaxSizeOf32bitArray(1);
        a = new UnsignedByteLargeArray(data);
        res = a.getByteData(null, startPos, endPos, step);
        idx = 0;
        for (long i = startPos; i < endPos; i += step) {
            assertEquals(data[(int) i], res[idx++]);
        }
    }

    public void testUnsignedByteLargeArrayGetShortData()
    {
        short[] data = new short[]{1, 2, 3, 4, 5, 6, 7, 80, 190, 255};
        long startPos = 2;
        long endPos = 7;
        long step = 2;
        LargeArray.setMaxSizeOf32bitArray(1 << 30);
        UnsignedByteLargeArray a = new UnsignedByteLargeArray(data);
        short[] res = a.getShortData(null, startPos, endPos, step);
        int idx = 0;
        for (long i = startPos; i < endPos; i += step) {
            assertEquals(data[(int) i], res[idx++]);
        }
        LargeArray.setMaxSizeOf32bitArray(1);
        a = new UnsignedByteLargeArray(data);
        res = a.getShortData(null, startPos, endPos, step);
        idx = 0;
        for (long i = startPos; i < endPos; i += step) {
            assertEquals(data[(int) i], res[idx++]);
        }
    }
    
    public void testUnsignedByteLargeArrayArraycopy()
    {
        short[] data = new short[]{1, 2, 3, 4, 5, 6, 7, 80, 190, 255};
        int startPos = 2;
        int length = 8;
        LargeArray.setMaxSizeOf32bitArray(1073741824);
        UnsignedByteLargeArray a = new UnsignedByteLargeArray(data);
        UnsignedByteLargeArray b = new UnsignedByteLargeArray(2 * data.length);
        LargeArrayUtils.arraycopy(a, startPos, b, 0, length);
        for (int i = 0; i < length; i++) {
            assertEquals(data[startPos + i], b.getUnsignedByte(i));
        }
        b = new UnsignedByteLargeArray(2 * data.length);
        LargeArrayUtils.arraycopy(data, startPos, b, 0, length);
        for (int i = 0; i < length; i++) {
            assertEquals(data[startPos + i], b.getUnsignedByte(i));
        }
        LargeArray.setMaxSizeOf32bitArray(data.length - 1);
        a = new UnsignedByteLargeArray(data);
        b = new UnsignedByteLargeArray(2 * data.length);
        LargeArrayUtils.arraycopy(a, startPos, b, 0, length);
        for (int i = 0; i < length; i++) {
            assertEquals(data[startPos + i], b.getUnsignedByte(i));
        }
        b = new UnsignedByteLargeArray(2 * data.length);
        LargeArrayUtils.arraycopy(data, startPos, b, 0, length);
        for (int i = 0; i < length; i++) {
            assertEquals(data[startPos + i], b.getUnsignedByte(i));
        }
    }

    public void testUnsignedByteLargeArrayConvert()
    {
        short[] data = new short[]{1, 2, 3, 4, 5, 6, 7, 80, 190, 255};
        UnsignedByteLargeArray a = new UnsignedByteLargeArray(data);
        IntLargeArray b = (IntLargeArray) LargeArrayUtils.convert(a, LargeArrayType.INT);
        for (int i = 0; i < data.length; i++) {
            assertEquals(data[i], b.getInt(i));
        }
    }
    

    public void testShortLargeArrayEqualsHashCode()
    {
        ShortLargeArray a = new ShortLargeArray(10);
        ShortLargeArray b = new ShortLargeArray(10);
        assertTrue(a.equals(a));
        assertTrue(a.hashCode() == a.hashCode());
        assertFalse(a.equals(b));
        assertFalse(a.hashCode() == b.hashCode());
        LargeArray.setMaxSizeOf32bitArray(1);
        a = new ShortLargeArray(10);
        b = new ShortLargeArray(10);
        assertTrue(a.equals(a));
        assertTrue(a.hashCode() == a.hashCode());
        assertFalse(a.equals(b));
        assertFalse(a.hashCode() == b.hashCode());
    }

    public void testShortLargeArrayConstant()
    {
        ShortLargeArray a = new ShortLargeArray(1l << 33, (short) 2);
        assertEquals(2, a.getShort(0));
        assertEquals(2, a.getShort(a.length() - 1));
        Throwable e = null;
        try {
            a.setShort(0, (short) 3);
        } catch (IllegalAccessError ex) {
            e = ex;
        }
        assertTrue(e instanceof IllegalAccessError);
    }

    public void testShortLargeArrayGetSet()
    {
        LargeArray.setMaxSizeOf32bitArray(1073741824);
        ShortLargeArray a = new ShortLargeArray(10);
        long idx = 5;
        short val = -100;
        a.setShort(idx, val);
        assertEquals(val, a.getShort(idx));
        idx = 6;
        a.set(idx, val);
        assertEquals(val, (a.get(idx)).shortValue());
        LargeArray.setMaxSizeOf32bitArray(1);
        a = new ShortLargeArray(10);
        a.setShort(idx, val);
        assertEquals(val, a.getShort(idx));
        idx = 6;
        a.set(idx, val);
        assertEquals(val, (a.get(idx)).shortValue());
    }

    public void testShortLargeArrayGetSetNative()
    {
        LargeArray.setMaxSizeOf32bitArray(1);
        ShortLargeArray a = new ShortLargeArray(10);
        long idx = 5;
        short val = -100;
        a.setToNative(idx, val);
        assertEquals(val, (short) a.getFromNative(idx));
    }

    public void testShortLargeArrayGetData()
    {
        short[] data = new short[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10};
        long startPos = 2;
        long endPos = 7;
        long step = 2;
        LargeArray.setMaxSizeOf32bitArray(1 << 30);
        ShortLargeArray a = new ShortLargeArray(data);
        short[] res = a.getShortData(null, startPos, endPos, step);
        int idx = 0;
        for (long i = startPos; i < endPos; i += step) {
            assertEquals(data[(int) i], res[idx++]);
        }
        LargeArray.setMaxSizeOf32bitArray(1);
        a = new ShortLargeArray(data);
        res = a.getShortData(null, startPos, endPos, step);
        idx = 0;
        for (long i = startPos; i < endPos; i += step) {
            assertEquals(data[(int) i], res[idx++]);
        }
    }

    public void testShortLargeArrayArraycopy()
    {
        short[] data = new short[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10};
        int startPos = 2;
        int length = 8;
        LargeArray.setMaxSizeOf32bitArray(1073741824);
        ShortLargeArray a = new ShortLargeArray(data);
        ShortLargeArray b = new ShortLargeArray(2 * data.length);
        LargeArrayUtils.arraycopy(a, startPos, b, 0, length);
        for (int i = 0; i < length; i++) {
            assertEquals(data[startPos + i], b.getShort(i));
        }
        b = new ShortLargeArray(2 * data.length);
        LargeArrayUtils.arraycopy(data, startPos, b, 0, length);
        for (int i = 0; i < length; i++) {
            assertEquals(data[startPos + i], b.getShort(i));
        }
        LargeArray.setMaxSizeOf32bitArray(data.length - 1);
        a = new ShortLargeArray(data);
        b = new ShortLargeArray(2 * data.length);
        LargeArrayUtils.arraycopy(a, startPos, b, 0, length);
        for (int i = 0; i < length; i++) {
            assertEquals(data[startPos + i], b.getShort(i));
        }
        b = new ShortLargeArray(2 * data.length);
        LargeArrayUtils.arraycopy(data, startPos, b, 0, length);
        for (int i = 0; i < length; i++) {
            assertEquals(data[startPos + i], b.getShort(i));
        }
    }

    public void testShortLargeArrayConvert()
    {
        short[] data = new short[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10};
        ShortLargeArray a = new ShortLargeArray(data);
        IntLargeArray b = (IntLargeArray) LargeArrayUtils.convert(a, LargeArrayType.INT);
        for (int i = 0; i < data.length; i++) {
            assertEquals(data[i], b.getInt(i));
        }
    }

    public void testIntLargeArrayEqualsHashCode()
    {
        IntLargeArray a = new IntLargeArray(10);
        IntLargeArray b = new IntLargeArray(10);
        assertTrue(a.equals(a));
        assertTrue(a.hashCode() == a.hashCode());
        assertFalse(a.equals(b));
        assertFalse(a.hashCode() == b.hashCode());
        LargeArray.setMaxSizeOf32bitArray(1);
        a = new IntLargeArray(10);
        b = new IntLargeArray(10);
        assertTrue(a.equals(a));
        assertTrue(a.hashCode() == a.hashCode());
        assertFalse(a.equals(b));
        assertFalse(a.hashCode() == b.hashCode());
    }

    public void testIntLargeArrayGetSet()
    {
        LargeArray.setMaxSizeOf32bitArray(1073741824);
        IntLargeArray a = new IntLargeArray(10);
        long idx = 5;
        int val = -100;
        a.setInt(idx, val);
        assertEquals(val, a.getInt(idx));
        idx = 6;
        a.set(idx, val);
        assertEquals(val, (a.get(idx)).intValue());
        LargeArray.setMaxSizeOf32bitArray(1);
        a = new IntLargeArray(10);
        a.setInt(idx, val);
        assertEquals(val, a.getInt(idx));
        idx = 6;
        a.set(idx, val);
        assertEquals(val, (a.get(idx)).intValue());
    }

    public void testIntLargeArrayGetSetNative()
    {
        LargeArray.setMaxSizeOf32bitArray(1);
        IntLargeArray a = new IntLargeArray(10);
        long idx = 5;
        int val = -100;
        a.setToNative(idx, val);
        assertEquals(val, (int) a.getFromNative(idx));
    }

    public void testIntLargeArrayConstant()
    {
        IntLargeArray a = new IntLargeArray(1l << 33, 2);
        assertEquals(2, a.getInt(0));
        assertEquals(2, a.getInt(a.length() - 1));
        Throwable e = null;
        try {
            a.setInt(0, 3);
        } catch (IllegalAccessError ex) {
            e = ex;
        }
        assertTrue(e instanceof IllegalAccessError);
    }

    public void testIntLargeArrayGetData()
    {
        int[] data = new int[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10};
        long startPos = 2;
        long endPos = 7;
        long step = 2;
        LargeArray.setMaxSizeOf32bitArray(1 << 30);
        IntLargeArray a = new IntLargeArray(data);
        int[] res = a.getIntData(null, startPos, endPos, step);
        int idx = 0;
        for (long i = startPos; i < endPos; i += step) {
            assertEquals(data[(int) i], res[idx++]);
        }
        LargeArray.setMaxSizeOf32bitArray(1);
        a = new IntLargeArray(data);
        res = a.getIntData(null, startPos, endPos, step);
        idx = 0;
        for (long i = startPos; i < endPos; i += step) {
            assertEquals(data[(int) i], res[idx++]);
        }
    }

    public void testIntLargeArrayArraycopy()
    {
        int[] data = new int[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10};
        int startPos = 2;
        int length = 8;
        LargeArray.setMaxSizeOf32bitArray(1073741824);
        IntLargeArray a = new IntLargeArray(data);
        IntLargeArray b = new IntLargeArray(2 * data.length);
        LargeArrayUtils.arraycopy(a, startPos, b, 0, length);
        for (int i = 0; i < length; i++) {
            assertEquals(data[startPos + i], b.getInt(i));
        }
        b = new IntLargeArray(2 * data.length);
        LargeArrayUtils.arraycopy(data, startPos, b, 0, length);
        for (int i = 0; i < length; i++) {
            assertEquals(data[startPos + i], b.getInt(i));
        }
        LargeArray.setMaxSizeOf32bitArray(data.length - 1);
        a = new IntLargeArray(data);
        b = new IntLargeArray(2 * data.length);
        LargeArrayUtils.arraycopy(a, startPos, b, 0, length);
        for (int i = 0; i < length; i++) {
            assertEquals(data[startPos + i], b.getInt(i));
        }
        b = new IntLargeArray(2 * data.length);
        LargeArrayUtils.arraycopy(data, startPos, b, 0, length);
        for (int i = 0; i < length; i++) {
            assertEquals(data[startPos + i], b.getInt(i));
        }
    }

    public void testIntLargeArrayConvert()
    {
        int[] data = new int[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10};
        IntLargeArray a = new IntLargeArray(data);
        LongLargeArray b = (LongLargeArray) LargeArrayUtils.convert(a, LargeArrayType.LONG);
        for (int i = 0; i < data.length; i++) {
            assertEquals(data[i], b.getLong(i));
        }
    }

    public void testLongLargeArrayEqualsHashCode()
    {
        LongLargeArray a = new LongLargeArray(10);
        LongLargeArray b = new LongLargeArray(10);
        assertTrue(a.equals(a));
        assertTrue(a.hashCode() == a.hashCode());
        assertFalse(a.equals(b));
        assertFalse(a.hashCode() == b.hashCode());
        LargeArray.setMaxSizeOf32bitArray(1);
        a = new LongLargeArray(10);
        b = new LongLargeArray(10);
        assertTrue(a.equals(a));
        assertTrue(a.hashCode() == a.hashCode());
        assertFalse(a.equals(b));
        assertFalse(a.hashCode() == b.hashCode());
    }

    public void testLongLargeArrayConstant()
    {
        LongLargeArray a = new LongLargeArray(1l << 33, 2);
        assertEquals(2, a.getLong(0));
        assertEquals(2, a.getLong(a.length() - 1));
        Throwable e = null;
        try {
            a.setLong(0, 3);
        } catch (IllegalAccessError ex) {
            e = ex;
        }
        assertTrue(e instanceof IllegalAccessError);
    }

    public void testLongLargeArrayGetSet()
    {
        LargeArray.setMaxSizeOf32bitArray(1073741824);
        LongLargeArray a = new LongLargeArray(10);
        long idx = 5;
        int val = -100;
        a.setLong(idx, val);
        assertEquals(val, a.getLong(idx));
        idx = 6;
        a.set(idx, val);
        assertEquals(val, (a.get(idx)).longValue());
        LargeArray.setMaxSizeOf32bitArray(1);
        a = new LongLargeArray(10);
        a.setLong(idx, val);
        assertEquals(val, a.getLong(idx));
        idx = 6;
        a.set(idx, val);
        assertEquals(val, (a.get(idx)).longValue());
    }

    public void testLongLargeArrayGetSetNative()
    {
        LargeArray.setMaxSizeOf32bitArray(1);
        LongLargeArray a = new LongLargeArray(10);
        long idx = 5;
        long val = -100;
        a.setToNative(idx, val);
        assertEquals(val, (long) a.getFromNative(idx));
    }

    public void testLongLargeArrayGetData()
    {
        long[] data = new long[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10};
        long startPos = 2;
        long endPos = 7;
        long step = 2;
        LargeArray.setMaxSizeOf32bitArray(1 << 30);
        LongLargeArray a = new LongLargeArray(data);
        long[] res = a.getLongData(null, startPos, endPos, step);
        int idx = 0;
        for (long i = startPos; i < endPos; i += step) {
            assertEquals(data[(int) i], res[idx++]);
        }
        LargeArray.setMaxSizeOf32bitArray(1);
        a = new LongLargeArray(data);
        res = a.getLongData(null, startPos, endPos, step);
        idx = 0;
        for (long i = startPos; i < endPos; i += step) {
            assertEquals(data[(int) i], res[idx++]);
        }
    }

    public void testLongLargeArrayArraycopy()
    {
        long[] data = new long[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10};
        int startPos = 2;
        int length = 8;
        LargeArray.setMaxSizeOf32bitArray(1073741824);
        LongLargeArray a = new LongLargeArray(data);
        LongLargeArray b = new LongLargeArray(2 * data.length);
        LargeArrayUtils.arraycopy(a, startPos, b, 0, length);
        for (int i = 0; i < length; i++) {
            assertEquals(data[startPos + i], b.getLong(i));
        }
        b = new LongLargeArray(2 * data.length);
        LargeArrayUtils.arraycopy(data, startPos, b, 0, length);
        for (int i = 0; i < length; i++) {
            assertEquals(data[startPos + i], b.getLong(i));
        }
        LargeArray.setMaxSizeOf32bitArray(data.length - 1);
        a = new LongLargeArray(data);
        b = new LongLargeArray(2 * data.length);
        LargeArrayUtils.arraycopy(a, startPos, b, 0, length);
        for (int i = 0; i < length; i++) {
            assertEquals(data[startPos + i], b.getLong(i));
        }
        b = new LongLargeArray(2 * data.length);
        LargeArrayUtils.arraycopy(data, startPos, b, 0, length);
        for (int i = 0; i < length; i++) {
            assertEquals(data[startPos + i], b.getLong(i));
        }
    }

    public void testLongLargeArrayConvert()
    {
        long[] data = new long[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10};
        LongLargeArray a = new LongLargeArray(data);
        FloatLargeArray b = (FloatLargeArray) LargeArrayUtils.convert(a, LargeArrayType.FLOAT);
        for (int i = 0; i < data.length; i++) {
            assertEquals((float) data[i], b.getFloat(i));
        }
    }

    public void testFloatLargeArrayEqualsHashCode()
    {
        FloatLargeArray a = new FloatLargeArray(10);
        FloatLargeArray b = new FloatLargeArray(10);
        assertTrue(a.equals(a));
        assertTrue(a.hashCode() == a.hashCode());
        assertFalse(a.equals(b));
        assertFalse(a.hashCode() == b.hashCode());
        LargeArray.setMaxSizeOf32bitArray(1);
        a = new FloatLargeArray(10);
        b = new FloatLargeArray(10);
        assertTrue(a.equals(a));
        assertTrue(a.hashCode() == a.hashCode());
        assertFalse(a.equals(b));
        assertFalse(a.hashCode() == b.hashCode());
    }

    public void testFloatLargeArrayConstant()
    {
        FloatLargeArray a = new FloatLargeArray(1l << 33, 2.5f);
        assertEquals(2.5f, a.getFloat(0));
        assertEquals(2.5f, a.getFloat(a.length() - 1));
        Throwable e = null;
        try {
            a.setFloat(0, 3.5f);
        } catch (IllegalAccessError ex) {
            e = ex;
        }
        assertTrue(e instanceof IllegalAccessError);
    }

    public void testFloatLargeArrayGetSet()
    {
        LargeArray.setMaxSizeOf32bitArray(1073741824);
        FloatLargeArray a = new FloatLargeArray(10);
        long idx = 5;
        float val = 3.4f;
        a.setFloat(idx, val);
        assertEquals(val, a.getFloat(idx), 0.0);
        idx = 6;
        a.set(idx, val);
        assertEquals(val, a.get(idx));
        LargeArray.setMaxSizeOf32bitArray(1);
        a = new FloatLargeArray(10);
        a.setFloat(idx, val);
        assertEquals(val, a.getFloat(idx), 0.0);
        idx = 6;
        a.set(idx, val);
        assertEquals(val, a.get(idx));
    }

    public void testFloatLargeArrayGetSetNative()
    {
        LargeArray.setMaxSizeOf32bitArray(1);
        FloatLargeArray a = new FloatLargeArray(10);
        long idx = 5;
        float val = 3.4f;
        a.setToNative(idx, val);
        assertEquals(val, (float) a.getFromNative(idx), 0.0);
    }

    public void testFloatLargeArrayGetData()
    {
        float[] data = new float[]{1.1f, 2.2f, 3.3f, 4.4f, 5.5f, 6.6f, 7.7f, 8.8f, 9.9f, 10.10f};
        long startPos = 2;
        long endPos = 7;
        long step = 2;
        LargeArray.setMaxSizeOf32bitArray(1 << 30);
        FloatLargeArray a = new FloatLargeArray(data);
        float[] res = a.getFloatData(null, startPos, endPos, step);
        int idx = 0;
        for (long i = startPos; i < endPos; i += step) {
            assertEquals(data[(int) i], res[idx++]);
        }
        LargeArray.setMaxSizeOf32bitArray(1);
        a = new FloatLargeArray(data);
        res = a.getFloatData(null, startPos, endPos, step);
        idx = 0;
        for (long i = startPos; i < endPos; i += step) {
            assertEquals(data[(int) i], res[idx++]);
        }
    }

    public void testFloatlargeArrayArraycopy()
    {
        float[] data = new float[]{1.1f, 2.2f, 3.3f, 4.4f, 5.5f, 6.6f, 7.7f, 8.8f, 9.9f, 10.10f};
        int startPos = 2;
        int length = 8;
        LargeArray.setMaxSizeOf32bitArray(1073741824);
        FloatLargeArray a = new FloatLargeArray(data);
        FloatLargeArray b = new FloatLargeArray(2 * data.length);
        LargeArrayUtils.arraycopy(a, startPos, b, 0, length);
        for (int i = 0; i < length; i++) {
            assertEquals(data[startPos + i], b.getFloat(i));
        }
        b = new FloatLargeArray(2 * data.length);
        LargeArrayUtils.arraycopy(data, startPos, b, 0, length);
        for (int i = 0; i < length; i++) {
            assertEquals(data[startPos + i], b.getFloat(i));
        }
        LargeArray.setMaxSizeOf32bitArray(data.length - 1);
        a = new FloatLargeArray(data);
        b = new FloatLargeArray(2 * data.length);
        LargeArrayUtils.arraycopy(a, startPos, b, 0, length);
        for (int i = 0; i < length; i++) {
            assertEquals(data[startPos + i], b.getFloat(i));
        }
        b = new FloatLargeArray(2 * data.length);
        LargeArrayUtils.arraycopy(data, startPos, b, 0, length);
        for (int i = 0; i < length; i++) {
            assertEquals(data[startPos + i], b.getFloat(i));
        }
    }

    public void testFloatLargeArrayConvert()
    {
        float[] data = new float[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10};
        FloatLargeArray a = new FloatLargeArray(data);
        DoubleLargeArray b = (DoubleLargeArray) LargeArrayUtils.convert(a, LargeArrayType.DOUBLE);
        for (int i = 0; i < data.length; i++) {
            assertEquals(data[i], b.getDouble(i), 0.0);
        }
    }

    public void testDoubleLargeArrayEqualsHashCode()
    {
        DoubleLargeArray a = new DoubleLargeArray(10);
        DoubleLargeArray b = new DoubleLargeArray(10);
        assertTrue(a.equals(a));
        assertTrue(a.hashCode() == a.hashCode());
        assertFalse(a.equals(b));
        assertFalse(a.hashCode() == b.hashCode());
        LargeArray.setMaxSizeOf32bitArray(1);
        a = new DoubleLargeArray(10);
        b = new DoubleLargeArray(10);
        assertTrue(a.equals(a));
        assertTrue(a.hashCode() == a.hashCode());
        assertFalse(a.equals(b));
        assertFalse(a.hashCode() == b.hashCode());
    }

    public void testDoubleLargeArrayConstant()
    {
        DoubleLargeArray a = new DoubleLargeArray(1l << 33, 2.5);
        assertEquals(2.5, a.getDouble(0));
        assertEquals(2.5, a.getDouble(a.length() - 1));
        Throwable e = null;
        try {
            a.setDouble(0, 3.5);
        } catch (IllegalAccessError ex) {
            e = ex;
        }
        assertTrue(e instanceof IllegalAccessError);
    }

    public void testDoubleLargeArrayGetSet()
    {
        LargeArray.setMaxSizeOf32bitArray(1073741824);
        DoubleLargeArray a = new DoubleLargeArray(10);
        long idx = 5;
        double val = 3.4;
        a.setDouble(idx, val);
        assertEquals(val, a.getDouble(idx), 0.0);
        idx = 6;
        a.set(idx, val);
        assertEquals(val, a.get(idx));
        LargeArray.setMaxSizeOf32bitArray(1);
        a = new DoubleLargeArray(10);
        a.setDouble(idx, val);
        assertEquals(val, a.getDouble(idx), 0.0);
        idx = 6;
        a.set(idx, val);
        assertEquals(val, a.get(idx));
    }

    public void testDoubleLargeArrayGetSetNative()
    {
        LargeArray.setMaxSizeOf32bitArray(1);
        DoubleLargeArray a = new DoubleLargeArray(10);
        long idx = 5;
        double val = 3.4;
        a.setToNative(idx, val);
        assertEquals(val, a.getFromNative(idx), 0.0);
    }

    public void testDoubleLargeArrayGetData()
    {
        double[] data = new double[]{1.1, 2.2, 3.3, 4.4, 5.5, 6.6, 7.7, 8.8, 9.9, 10.10};
        long startPos = 2;
        long endPos = 7;
        long step = 2;
        LargeArray.setMaxSizeOf32bitArray(1 << 30);
        DoubleLargeArray a = new DoubleLargeArray(data);
        double[] res = a.getDoubleData(null, startPos, endPos, step);
        int idx = 0;
        for (long i = startPos; i < endPos; i += step) {
            assertEquals(data[(int) i], res[idx++]);
        }
        LargeArray.setMaxSizeOf32bitArray(1);
        a = new DoubleLargeArray(data);
        res = a.getDoubleData(null, startPos, endPos, step);
        idx = 0;
        for (long i = startPos; i < endPos; i += step) {
            assertEquals(data[(int) i], res[idx++]);
        }
    }

    public void testDoubleLargeArrayArraycopy()
    {
        double[] data = new double[]{1.1, 2.2, 3.3, 4.4, 5.5, 6.6, 7.7, 8.8, 9.9, 10.10};
        int startPos = 2;
        int length = 8;
        LargeArray.setMaxSizeOf32bitArray(1073741824);
        DoubleLargeArray a = new DoubleLargeArray(data);
        DoubleLargeArray b = new DoubleLargeArray(2 * data.length);
        LargeArrayUtils.arraycopy(a, startPos, b, 0, length);
        for (int i = 0; i < length; i++) {
            assertEquals(data[startPos + i], b.getDouble(i));
        }
        b = new DoubleLargeArray(2 * data.length);
        LargeArrayUtils.arraycopy(data, startPos, b, 0, length);
        for (int i = 0; i < length; i++) {
            assertEquals(data[startPos + i], b.getDouble(i));
        }
        LargeArray.setMaxSizeOf32bitArray(data.length - 1);
        a = new DoubleLargeArray(data);
        b = new DoubleLargeArray(2 * data.length);
        LargeArrayUtils.arraycopy(a, startPos, b, 0, length);
        for (int i = 0; i < length; i++) {
            assertEquals(data[startPos + i], b.getDouble(i));
        }
        b = new DoubleLargeArray(2 * data.length);
        LargeArrayUtils.arraycopy(data, startPos, b, 0, length);
        for (int i = 0; i < length; i++) {
            assertEquals(data[startPos + i], b.getDouble(i));
        }
    }

    public void testDoubleLargeArrayConvert()
    {
        double[] data = new double[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10};
        DoubleLargeArray a = new DoubleLargeArray(data);
        FloatLargeArray b = (FloatLargeArray) LargeArrayUtils.convert(a, LargeArrayType.FLOAT);
        for (int i = 0; i < data.length; i++) {
            assertEquals(data[i], b.getFloat(i), 0.0);
        }
    }

    public void testComplexFloatLargeArrayEqualsHashCode()
    {
        ComplexFloatLargeArray a = new ComplexFloatLargeArray(10);
        ComplexFloatLargeArray b = new ComplexFloatLargeArray(10);
        assertTrue(a.equals(a));
        assertTrue(a.hashCode() == a.hashCode());
        assertFalse(a.equals(b));
        assertFalse(a.hashCode() == b.hashCode());
        LargeArray.setMaxSizeOf32bitArray(1);
        a = new ComplexFloatLargeArray(10);
        b = new ComplexFloatLargeArray(10);
        assertTrue(a.equals(a));
        assertTrue(a.hashCode() == a.hashCode());
        assertFalse(a.equals(b));
        assertFalse(a.hashCode() == b.hashCode());
    }

    public void testComplexFloatLargeArrayConstant()
    {
        ComplexFloatLargeArray a = new ComplexFloatLargeArray(1l << 33, new float[]{2.5f, 1.5f});
        assertEquals(2.5f, a.getComplexFloat(0)[0], 0.0);
        assertEquals(1.5f, a.getComplexFloat(0)[1], 0.0);
        assertEquals(2.5f, a.getComplexFloat(a.length - 1)[0], 0.0);
        assertEquals(1.5f, a.getComplexFloat(a.length - 1)[1], 0.0);
        Throwable e = null;
        try {
            a.setComplexFloat(0, new float[]{3.5f, 4.5f});
        } catch (IllegalAccessError ex) {
            e = ex;
        }
        assertTrue(e instanceof IllegalAccessError);
    }

    public void testComplexFloatLargeArrayGetSet()
    {
        LargeArray.setMaxSizeOf32bitArray(1073741824);
        ComplexFloatLargeArray a = new ComplexFloatLargeArray(10);
        long idx = 5;
        float[] val = {3.4f, -3.7f};
        a.setComplexFloat(idx, val);
        assertEquals(val[0], a.getComplexFloat(idx)[0], 0.0);
        assertEquals(val[1], a.getComplexFloat(idx)[1], 0.0);
        idx = 6;
        a.set(idx, val);
        assertEquals(val[0], a.get(idx)[0], 0.0);
        assertEquals(val[1], a.get(idx)[1], 0.0);
        a = new ComplexFloatLargeArray(10);
        a.setComplexFloat(idx, val);
        assertEquals(val[0], a.getComplexFloat(idx)[0], 0.0);
        assertEquals(val[1], a.getComplexFloat(idx)[1], 0.0);
        idx = 6;
        a.set(idx, val);
        assertEquals(val[0], a.get(idx)[0], 0.0);
        assertEquals(val[1], a.get(idx)[1], 0.0);
    }

    public void testComplexFloatLargeArrayGetSetNative()
    {
        LargeArray.setMaxSizeOf32bitArray(1);
        ComplexFloatLargeArray a = new ComplexFloatLargeArray(10);
        long idx = 5;
        float[] val = {3.4f, -3.7f};
        a.setToNative(idx, val);
        assertEquals(val[0], a.getFromNative(idx)[0], 0.0);
        assertEquals(val[1], a.getFromNative(idx)[1], 0.0);
    }

    public void testComplexFloatLargeArrayGetData()
    {
        float[] data = new float[]{1.1f, 2.2f, 3.3f, 4.4f, 5.5f, 6.6f, 7.7f, 8.8f, 9.9f, 10.10f};
        int startPos = 1;
        int endPos = 5;
        int step = 2;
        LargeArray.setMaxSizeOf32bitArray(1 << 30);
        ComplexFloatLargeArray a = new ComplexFloatLargeArray(data);
        float[] res = a.getComplexData(null, startPos, endPos, step);
        int idx = 0;
        for (int i = startPos; i < endPos; i += step) {
            assertEquals(data[2 * i], res[2 * idx], 0.0);
            assertEquals(data[2 * i + 1], res[2 * idx + 1], 0.0);
            idx++;
        }
        LargeArray.setMaxSizeOf32bitArray(1);
        a = new ComplexFloatLargeArray(data);
        res = a.getComplexData(null, startPos, endPos, step);
        idx = 0;
        for (int i = startPos; i < endPos; i += step) {
            assertEquals(data[2 * i], res[2 * idx], 0.0);
            assertEquals(data[2 * i + 1], res[2 * idx + 1], 0.0);
            idx++;
        }
    }

    public void testComplexFloatLargeArrayArraycopy()
    {
        float[] data = new float[]{1.1f, 2.2f, 3.3f, 4.4f, 5.5f, 6.6f, 7.7f, 8.8f, 9.9f, 10.10f};
        int startPos = 1;
        int length = 3;
        LargeArray.setMaxSizeOf32bitArray(1073741824);
        ComplexFloatLargeArray a = new ComplexFloatLargeArray(data);
        ComplexFloatLargeArray b = new ComplexFloatLargeArray(2 * data.length);
        LargeArrayUtils.arraycopy(a, startPos, b, 0, length);
        for (int i = 0; i < length; i++) {
            assertEquals(data[2 * (startPos + i)], b.getComplexFloat(i)[0], 0.0);
            assertEquals(data[2 * (startPos + i) + 1], b.getComplexFloat(i)[1], 0.0);
        }
        b = new ComplexFloatLargeArray(2 * data.length);
        LargeArrayUtils.arraycopy(data, startPos, b, 0, length);
        for (int i = 0; i < length; i++) {
            assertEquals(data[2 * (startPos + i)], b.getComplexFloat(i)[0], 0.0);
            assertEquals(data[2 * (startPos + i) + 1], b.getComplexFloat(i)[1], 0.0);
        }
        LargeArray.setMaxSizeOf32bitArray(data.length - 1);
        a = new ComplexFloatLargeArray(data);
        b = new ComplexFloatLargeArray(2 * data.length);
        LargeArrayUtils.arraycopy(a, startPos, b, 0, length);
        for (int i = 0; i < length; i++) {
            assertEquals(data[2 * (startPos + i)], b.getComplexFloat(i)[0], 0.0);
            assertEquals(data[2 * (startPos + i) + 1], b.getComplexFloat(i)[1], 0.0);
        }
        b = new ComplexFloatLargeArray(2 * data.length);
        LargeArrayUtils.arraycopy(data, startPos, b, 0, length);
        for (int i = 0; i < length; i++) {
            assertEquals(data[2 * (startPos + i)], b.getComplexFloat(i)[0], 0.0);
            assertEquals(data[2 * (startPos + i) + 1], b.getComplexFloat(i)[1], 0.0);
        }
    }

    public void testComplexFloatLargeArrayConvert()
    {
        float[] data = new float[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10};
        ComplexFloatLargeArray a = new ComplexFloatLargeArray(data);
        FloatLargeArray b = (FloatLargeArray) LargeArrayUtils.convert(a, LargeArrayType.FLOAT);
        for (int i = 0; i < data.length / 2; i++) {
            assertEquals(data[2 * i], b.getFloat(i), 0.0);
        }
    }

    public void testComplexDoubleLargeArrayEqualsHashCode()
    {
        ComplexDoubleLargeArray a = new ComplexDoubleLargeArray(10);
        ComplexDoubleLargeArray b = new ComplexDoubleLargeArray(10);
        assertTrue(a.equals(a));
        assertTrue(a.hashCode() == a.hashCode());
        assertFalse(a.equals(b));
        assertFalse(a.hashCode() == b.hashCode());
        LargeArray.setMaxSizeOf32bitArray(1);
        a = new ComplexDoubleLargeArray(10);
        b = new ComplexDoubleLargeArray(10);
        assertTrue(a.equals(a));
        assertTrue(a.hashCode() == a.hashCode());
        assertFalse(a.equals(b));
        assertFalse(a.hashCode() == b.hashCode());
    }

    public void testComplexDoubleLargeArrayConstant()
    {
        ComplexDoubleLargeArray a = new ComplexDoubleLargeArray(1l << 33, new double[]{2.5, 1.5});
        assertEquals(2.5, a.getComplexDouble(0)[0], 0.0);
        assertEquals(1.5, a.getComplexDouble(0)[1], 0.0);
        assertEquals(2.5, a.getComplexDouble(a.length - 1)[0], 0.0);
        assertEquals(1.5, a.getComplexDouble(a.length - 1)[1], 0.0);
        Throwable e = null;
        try {
            a.setComplexDouble(0, new double[]{3.5, 4.5});
        } catch (IllegalAccessError ex) {
            e = ex;
        }
        assertTrue(e instanceof IllegalAccessError);
    }

    public void testComplexDoubleLargeArrayGetSet()
    {
        LargeArray.setMaxSizeOf32bitArray(1073741824);
        ComplexDoubleLargeArray a = new ComplexDoubleLargeArray(10);
        long idx = 5;
        double[] val = {3.4, -3.7};
        a.setComplexDouble(idx, val);
        assertEquals(val[0], a.getComplexDouble(idx)[0], 0.0);
        assertEquals(val[1], a.getComplexDouble(idx)[1], 0.0);
        idx = 6;
        a.set(idx, val);
        assertEquals(val[0], a.get(idx)[0], 0.0);
        assertEquals(val[1], a.get(idx)[1], 0.0);
        a = new ComplexDoubleLargeArray(10);
        a.setComplexDouble(idx, val);
        assertEquals(val[0], a.getComplexDouble(idx)[0], 0.0);
        assertEquals(val[1], a.getComplexDouble(idx)[1], 0.0);
        idx = 6;
        a.set(idx, val);
        assertEquals(val[0], a.get(idx)[0], 0.0);
        assertEquals(val[1], a.get(idx)[1], 0.0);
    }

    public void testComplexDoubleLargeArrayGetSetNative()
    {
        LargeArray.setMaxSizeOf32bitArray(1);
        ComplexDoubleLargeArray a = new ComplexDoubleLargeArray(10);
        long idx = 5;
        double[] val = {3.4, -3.7};
        a.setToNative(idx, val);
        assertEquals(val[0], a.getFromNative(idx)[0], 0.0);
        assertEquals(val[1], a.getFromNative(idx)[1], 0.0);
    }

    public void testComplexDoubleLargeArrayGetData()
    {
        double[] data = new double[]{1.1, 2.2, 3.3, 4.4, 5.5, 6.6, 7.7, 8.8, 9.9, 10.10};
        int startPos = 1;
        int endPos = 5;
        int step = 2;
        LargeArray.setMaxSizeOf32bitArray(1 << 30);
        ComplexDoubleLargeArray a = new ComplexDoubleLargeArray(data);
        double[] res = a.getComplexData(null, startPos, endPos, step);
        int idx = 0;
        for (int i = startPos; i < endPos; i += step) {
            assertEquals(data[2 * i], res[2 * idx], 0.0);
            assertEquals(data[2 * i + 1], res[2 * idx + 1], 0.0);
            idx++;
        }
        LargeArray.setMaxSizeOf32bitArray(1);
        a = new ComplexDoubleLargeArray(data);
        res = a.getComplexData(null, startPos, endPos, step);
        idx = 0;
        for (int i = startPos; i < endPos; i += step) {
            assertEquals(data[2 * i], res[2 * idx], 0.0);
            assertEquals(data[2 * i + 1], res[2 * idx + 1], 0.0);
            idx++;
        }
    }

    public void testComplexDoubleLargeArrayArraycopy()
    {
        double[] data = new double[]{1.1, 2.2, 3.3, 4.4, 5.5, 6.6, 7.7, 8.8, 9.9, 10.10};
        int startPos = 1;
        int length = 3;
        LargeArray.setMaxSizeOf32bitArray(1073741824);
        ComplexDoubleLargeArray a = new ComplexDoubleLargeArray(data);
        ComplexDoubleLargeArray b = new ComplexDoubleLargeArray(2 * data.length);
        LargeArrayUtils.arraycopy(a, startPos, b, 0, length);
        for (int i = 0; i < length; i++) {
            assertEquals(data[2 * (startPos + i)], b.getComplexDouble(i)[0], 0.0);
            assertEquals(data[2 * (startPos + i) + 1], b.getComplexDouble(i)[1], 0.0);
        }
        b = new ComplexDoubleLargeArray(2 * data.length);
        LargeArrayUtils.arraycopy(data, startPos, b, 0, length);
        for (int i = 0; i < length; i++) {
            assertEquals(data[2 * (startPos + i)], b.getComplexDouble(i)[0], 0.0);
            assertEquals(data[2 * (startPos + i) + 1], b.getComplexDouble(i)[1], 0.0);
        }
        LargeArray.setMaxSizeOf32bitArray(data.length - 1);
        a = new ComplexDoubleLargeArray(data);
        b = new ComplexDoubleLargeArray(2 * data.length);
        LargeArrayUtils.arraycopy(a, startPos, b, 0, length);
        for (int i = 0; i < length; i++) {
            assertEquals(data[2 * (startPos + i)], b.getComplexDouble(i)[0], 0.0);
            assertEquals(data[2 * (startPos + i) + 1], b.getComplexDouble(i)[1], 0.0);
        }
        b = new ComplexDoubleLargeArray(2 * data.length);
        LargeArrayUtils.arraycopy(data, startPos, b, 0, length);
        for (int i = 0; i < length; i++) {
            assertEquals(data[2 * (startPos + i)], b.getComplexDouble(i)[0], 0.0);
            assertEquals(data[2 * (startPos + i) + 1], b.getComplexDouble(i)[1], 0.0);
        }
    }

    public void testComplexDoubleLargeArrayConvert()
    {
        float[] data = new float[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10};
        ComplexFloatLargeArray a = new ComplexFloatLargeArray(data);
        FloatLargeArray b = (FloatLargeArray) LargeArrayUtils.convert(a, LargeArrayType.FLOAT);
        for (int i = 0; i < data.length / 2; i++) {
            assertEquals(data[2 * i], b.getFloat(i), 0.0);
        }
    }

    public void testStringLargeArrayEqualsHashCode()
    {
        StringLargeArray a = new StringLargeArray(10);
        StringLargeArray b = new StringLargeArray(10);
        assertTrue(a.equals(a));
        assertTrue(a.hashCode() == a.hashCode());
        assertFalse(a.equals(b));
        assertFalse(a.hashCode() == b.hashCode());
        LargeArray.setMaxSizeOf32bitArray(1);
        a = new StringLargeArray(10);
        b = new StringLargeArray(10);
        assertTrue(a.equals(a));
        assertTrue(a.hashCode() == a.hashCode());
        assertFalse(a.equals(b));
        assertFalse(a.hashCode() == b.hashCode());
    }

    public void testStringLargeArrayConstant()
    {
        StringLargeArray a = new StringLargeArray(1l << 33, "test0123ąęćńżź");
        assertEquals("test0123ąęćńżź", a.get(0));
        assertEquals("test0123ąęćńżź", a.get(a.length() - 1));
        Throwable e = null;
        try {
            a.set(0, "test0123ąęćńżź");
        } catch (IllegalAccessError ex) {
            e = ex;
        }
        assertTrue(e instanceof IllegalAccessError);
    }

    public void testStringLargeArrayGetSet()
    {
        LargeArray.setMaxSizeOf32bitArray(1073741824);
        StringLargeArray a = new StringLargeArray(10, 14);
        long idx = 5;
        String val1 = "test0123ąęćńżź";
        String val2 = "test";
        a.set(idx, val1);
        assertEquals(val1, a.get(idx));
        a.set(idx, val2);
        assertEquals(val2, a.get(idx));
        LargeArray.setMaxSizeOf32bitArray(1);
        a = new StringLargeArray(10, 14);
        a.set(idx, val1);
        assertEquals(val1, a.get(idx));
        a.set(idx, val2);
        assertEquals(val2, a.get(idx));
    }

    public void testStringLargeArrayGetSetNative()
    {
        LargeArray.setMaxSizeOf32bitArray(1);
        StringLargeArray a = new StringLargeArray(10, 14);
        long idx = 5;
        String val1 = "test0123ąęćńżź";
        String val2 = "test";
        a.setToNative(idx, val1);
        assertEquals(val1, a.getFromNative(idx));
        a.setToNative(idx, val2);
        assertEquals(val2, a.getFromNative(idx));
    }

    public void testStringLargeArrayArraycopy()
    {
        String[] data = new String[]{"a", "ab", "abc", "ąęć", "1234", "test string", "ANOTHER TEST STRING", "", "\n", "\r"};
        int startPos = 2;
        int length = 8;
        LargeArray.setMaxSizeOf32bitArray(1073741824);
        StringLargeArray a = new StringLargeArray(data);
        StringLargeArray b = new StringLargeArray(2 * data.length, 20);
        LargeArrayUtils.arraycopy(a, startPos, b, 0, length);
        for (int i = 0; i < length; i++) {
            assertEquals(data[startPos + i], b.get(i));
        }
        b = new StringLargeArray(2 * data.length, 20);
        LargeArrayUtils.arraycopy(data, startPos, b, 0, length);
        for (int i = 0; i < length; i++) {
            assertEquals(data[startPos + i], b.get(i));
        }
        LargeArray.setMaxSizeOf32bitArray(data.length - 1);
        a = new StringLargeArray(data);
        b = new StringLargeArray(2 * data.length, 20);
        LargeArrayUtils.arraycopy(a, startPos, b, 0, length);
        for (int i = 0; i < length; i++) {
            assertEquals(data[startPos + i], b.get(i));
        }
        b = new StringLargeArray(2 * data.length, 20);
        LargeArrayUtils.arraycopy(data, startPos, b, 0, length);
        for (int i = 0; i < length; i++) {
            assertEquals(data[startPos + i], b.get(i));
        }
    }

    public void testStringLargeArrayConvert()
    {
        String[] data = new String[]{"a", "ab", "abc", "ąęć", "1234", "test string", "ANOTHER TEST STRING", "", "\n", "\r"};
        StringLargeArray a = new StringLargeArray(data);
        IntLargeArray b = (IntLargeArray) LargeArrayUtils.convert(a, LargeArrayType.INT);
        for (int i = 0; i < data.length; i++) {
            assertEquals(data[i].length(), b.getInt(i));
        }
    }

    public void testObjectLargeArrayEqualsHashCode()
    {
        ObjectLargeArray a = new ObjectLargeArray(10);
        ObjectLargeArray b = new ObjectLargeArray(10);
        assertTrue(a.equals(a));
        assertTrue(a.hashCode() == a.hashCode());
        assertFalse(a.equals(b));
        assertFalse(a.hashCode() == b.hashCode());
        LargeArray.setMaxSizeOf32bitArray(1);
        a = new ObjectLargeArray(10);
        b = new ObjectLargeArray(10);
        assertTrue(a.equals(a));
        assertTrue(a.hashCode() == a.hashCode());
        assertFalse(a.equals(b));
        assertFalse(a.hashCode() == b.hashCode());
    }

    public void testObjectLargeArrayConstant()
    {
        ObjectLargeArray a = new ObjectLargeArray(1l << 33, new Float(12345));
        assertEquals(12345f, a.get(0));
        assertEquals(12345f, a.get(a.length() - 1));
        Throwable e = null;
        try {
            a.set(0, new Float(12346));
        } catch (IllegalAccessError ex) {
            e = ex;
        }
        assertTrue(e instanceof IllegalAccessError);
    }

    public void testObjectLargeArrayGetSet()
    {
        LargeArray.setMaxSizeOf32bitArray(1073741824);
        ObjectLargeArray a = new ObjectLargeArray(10, 84);
        long idx = 5;
        Double val1 = 2.0;
        Double val2 = Double.MAX_VALUE;
        a.set(idx, val1);
        assertEquals(val1, a.get(idx));
        a.set(idx, val2);
        assertEquals(val2, a.get(idx));
        LargeArray.setMaxSizeOf32bitArray(1);
        a = new ObjectLargeArray(10, 84);
        a.set(idx, val1);
        assertEquals(val1, a.get(idx));
        a.set(idx, val2);
        assertEquals(val2, a.get(idx));
    }

    public void testObjectLargeArrayGetSetNative()
    {
        LargeArray.setMaxSizeOf32bitArray(1);
        ObjectLargeArray a = new ObjectLargeArray(10, 84);
        long idx = 5;
        Double val1 = 2.0;
        Double val2 = Double.MAX_VALUE;
        a.setToNative(idx, val1);
        assertEquals(val1, a.getFromNative(idx));
        a.setToNative(idx, val2);
        assertEquals(val2, a.getFromNative(idx));
    }

    public void testObjectLargeArrayArraycopy()
    {
        Double[] data = new Double[]{1.12345, -1.54321, 100., -100., Double.MAX_VALUE, Double.MIN_VALUE, Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY, Double.NaN, Double.MIN_NORMAL};
        int startPos = 2;
        int length = 8;
        LargeArray.setMaxSizeOf32bitArray(1073741824);
        ObjectLargeArray a = new ObjectLargeArray(data);
        ObjectLargeArray b = new ObjectLargeArray(2 * data.length, 84);
        LargeArrayUtils.arraycopy(a, startPos, b, 0, length);
        for (int i = 0; i < length; i++) {
            assertEquals(data[startPos + i], b.get(i));
        }
        b = new ObjectLargeArray(2 * data.length, 84);
        LargeArrayUtils.arraycopy(data, startPos, b, 0, length);
        for (int i = 0; i < length; i++) {
            assertEquals(data[startPos + i], b.get(i));
        }
        LargeArray.setMaxSizeOf32bitArray(data.length - 1);
        a = new ObjectLargeArray(data);
        b = new ObjectLargeArray(2 * data.length, 84);
        LargeArrayUtils.arraycopy(a, startPos, b, 0, length);
        for (int i = 0; i < length; i++) {
            assertEquals(data[startPos + i], b.get(i));
        }
        b = new ObjectLargeArray(2 * data.length, 84);
        LargeArrayUtils.arraycopy(data, startPos, b, 0, length);
        for (int i = 0; i < length; i++) {
            assertEquals(data[startPos + i], b.get(i));
        }
    }

    public void testObjectLargeArrayConvert()
    {
        double[] data = new double[]{1.12345, -1.54321, 100., -100., Double.MAX_VALUE, Double.MIN_VALUE, Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY, Double.NaN, Double.MIN_NORMAL};
        DoubleLargeArray a = new DoubleLargeArray(data);
        ObjectLargeArray b = (ObjectLargeArray) LargeArrayUtils.convert(a, LargeArrayType.OBJECT);
        for (int i = 0; i < data.length; i++) {
            assertEquals(data[i], b.get(i));
        }
    }

    public void testSelect()
    {
        double[] d = new double[]{1.1, 2.2, 3.3, 4.4, 5.5, 6.6, 7.7, 8.8, 9.9, 10.10};
        byte[] m = new byte[]{0, 0, 1, 0, 1, 1, 1, 0, 0, 0};
        int length = 4;
        LargeArray.setMaxSizeOf32bitArray(1);
        LargeArray data = new DoubleLargeArray(d);
        LogicLargeArray mask = new LogicLargeArray(m);
        LargeArray res = LargeArrayUtils.select(data, mask);
        assertEquals(length, res.length());
        assertEquals(d[2], res.getDouble(0), 0.0);
        assertEquals(d[4], res.getDouble(1), 0.0);
        assertEquals(d[5], res.getDouble(2), 0.0);
        assertEquals(d[6], res.getDouble(3), 0.0);
    }

    public void testGenerateRandom()
    {

        LargeArray.setMaxSizeOf32bitArray(1);
        int length = 10;
        LargeArray res = LargeArrayUtils.generateRandom(LargeArrayType.LOGIC, length);
        assertEquals(length, res.length());
        assertEquals(LargeArrayType.LOGIC, res.getType());

        res = LargeArrayUtils.generateRandom(LargeArrayType.BYTE, length);
        assertEquals(length, res.length());
        assertEquals(LargeArrayType.BYTE, res.getType());

        res = LargeArrayUtils.generateRandom(LargeArrayType.UNSIGNED_BYTE, length);
        assertEquals(length, res.length());
        assertEquals(LargeArrayType.UNSIGNED_BYTE, res.getType());
        
        res = LargeArrayUtils.generateRandom(LargeArrayType.SHORT, length);
        assertEquals(length, res.length());
        assertEquals(LargeArrayType.SHORT, res.getType());

        res = LargeArrayUtils.generateRandom(LargeArrayType.INT, length);
        assertEquals(length, res.length());
        assertEquals(LargeArrayType.INT, res.getType());

        res = LargeArrayUtils.generateRandom(LargeArrayType.LONG, length);
        assertEquals(length, res.length());
        assertEquals(LargeArrayType.LONG, res.getType());

        res = LargeArrayUtils.generateRandom(LargeArrayType.FLOAT, length);
        assertEquals(length, res.length());
        assertEquals(LargeArrayType.FLOAT, res.getType());

        res = LargeArrayUtils.generateRandom(LargeArrayType.DOUBLE, length);
        assertEquals(length, res.length());
        assertEquals(LargeArrayType.DOUBLE, res.getType());

        res = LargeArrayUtils.generateRandom(LargeArrayType.COMPLEX_FLOAT, length);
        assertEquals(length, res.length());
        assertEquals(LargeArrayType.COMPLEX_FLOAT, res.getType());

        res = LargeArrayUtils.generateRandom(LargeArrayType.COMPLEX_DOUBLE, length);
        assertEquals(length, res.length());
        assertEquals(LargeArrayType.COMPLEX_DOUBLE, res.getType());

        res = LargeArrayUtils.generateRandom(LargeArrayType.STRING, length);
        assertEquals(length, res.length());
        assertEquals(LargeArrayType.STRING, res.getType());

        res = LargeArrayUtils.generateRandom(LargeArrayType.OBJECT, length);
        assertEquals(length, res.length());
        assertEquals(LargeArrayType.OBJECT, res.getType());

        LargeArray.setMaxSizeOf32bitArray(100);

        res = LargeArrayUtils.generateRandom(LargeArrayType.LOGIC, length);
        assertEquals(length, res.length());
        assertEquals(LargeArrayType.LOGIC, res.getType());

        res = LargeArrayUtils.generateRandom(LargeArrayType.BYTE, length);
        assertEquals(length, res.length());
        assertEquals(LargeArrayType.BYTE, res.getType());

        res = LargeArrayUtils.generateRandom(LargeArrayType.SHORT, length);
        assertEquals(length, res.length());
        assertEquals(LargeArrayType.SHORT, res.getType());

        res = LargeArrayUtils.generateRandom(LargeArrayType.INT, length);
        assertEquals(length, res.length());
        assertEquals(LargeArrayType.INT, res.getType());

        res = LargeArrayUtils.generateRandom(LargeArrayType.LONG, length);
        assertEquals(length, res.length());
        assertEquals(LargeArrayType.LONG, res.getType());

        res = LargeArrayUtils.generateRandom(LargeArrayType.FLOAT, length);
        assertEquals(length, res.length());
        assertEquals(LargeArrayType.FLOAT, res.getType());

        res = LargeArrayUtils.generateRandom(LargeArrayType.DOUBLE, length);
        assertEquals(length, res.length());
        assertEquals(LargeArrayType.DOUBLE, res.getType());

        res = LargeArrayUtils.generateRandom(LargeArrayType.COMPLEX_FLOAT, length);
        assertEquals(length, res.length());
        assertEquals(LargeArrayType.COMPLEX_FLOAT, res.getType());

        res = LargeArrayUtils.generateRandom(LargeArrayType.COMPLEX_DOUBLE, length);
        assertEquals(length, res.length());
        assertEquals(LargeArrayType.COMPLEX_DOUBLE, res.getType());

        res = LargeArrayUtils.generateRandom(LargeArrayType.STRING, length);
        assertEquals(length, res.length());
        assertEquals(LargeArrayType.STRING, res.getType());

        res = LargeArrayUtils.generateRandom(LargeArrayType.OBJECT, length);
        assertEquals(length, res.length());
        assertEquals(LargeArrayType.OBJECT, res.getType());

    }

}
