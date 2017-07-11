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
import org.apache.commons.math3.util.FastMath;
import org.junit.Assert;
import static pl.edu.icm.jlargearrays.LargeArrayArithmetics.*;


/**
 * LargeArrayArithmetics unit tests.
 *
 * @author Piotr Wendykier (p.wendykier@icm.edu.pl)
 */
public class LargeArrayArithmeticsTest extends TestCase
{

    private static final double DELTA = 1E-6;

    /**
     * Create the test case
     *
     * @param testName name of the test case
     */
    public LargeArrayArithmeticsTest(String testName)
    {
        super(testName);
    }

    /**
     * @return the suite of tests being tested
     */
    public static Test suite()
    {
        return new TestSuite(LargeArrayArithmeticsTest.class);
    }

    public void testAdd()
    {
        LargeArray.setMaxSizeOf32bitArray(1);
        LargeArray a = LargeArrayUtils.generateRandom(LargeArrayType.BYTE, 10);
        LargeArray b = LargeArrayUtils.generateRandom(LargeArrayType.FLOAT, 10);

        LargeArray res = LargeArrayArithmetics.add(a, b);
        assertEquals(LargeArrayType.FLOAT, res.getType());
        for (long i = 0; i < a.length(); i++) {
            assertEquals(a.getFloat(i) + b.getFloat(i), res.getFloat(i), DELTA);
        }

        a = LargeArrayUtils.generateRandom(LargeArrayType.DOUBLE, 10);
        b = LargeArrayUtils.generateRandom(LargeArrayType.COMPLEX_FLOAT, 10);

        res = LargeArrayArithmetics.add(a, b);
        assertEquals(LargeArrayType.COMPLEX_FLOAT, res.getType());

        for (long i = 0; i < a.length(); i++) {
            assertEquals(a.getFloat(i) + ((ComplexFloatLargeArray) b).getComplexFloat(i)[0], ((ComplexFloatLargeArray) res).getComplexFloat(i)[0], DELTA);
            assertEquals(((ComplexFloatLargeArray) b).getComplexFloat(i)[1], ((ComplexFloatLargeArray) res).getComplexFloat(i)[1], DELTA);
        }

        a = LargeArrayUtils.createConstant(LargeArrayType.BYTE, 10, (byte) 2);
        b = LargeArrayUtils.createConstant(LargeArrayType.FLOAT, 10, 3f);
        res = LargeArrayArithmetics.add(a, b);
        assertTrue(res.isConstant());
        assertEquals(LargeArrayType.FLOAT, res.getType());
        for (long i = 0; i < a.length(); i++) {
            assertEquals(a.getFloat(i) + b.getFloat(i), res.getFloat(i), DELTA);
        }
    }

    public void testDiff()
    {
        LargeArray.setMaxSizeOf32bitArray(1);
        
        LargeArray a = LargeArrayUtils.generateRandom(LargeArrayType.BYTE, 10);
        LargeArray b = LargeArrayUtils.generateRandom(LargeArrayType.FLOAT, 10);

        LargeArray res = LargeArrayArithmetics.diff(a, b);
        assertEquals(LargeArrayType.FLOAT, res.getType());
        for (long i = 0; i < a.length(); i++) {
            assertEquals(a.getFloat(i) - b.getFloat(i), res.getFloat(i), DELTA);
        }

        a = LargeArrayUtils.generateRandom(LargeArrayType.DOUBLE, 10);
        b = LargeArrayUtils.generateRandom(LargeArrayType.COMPLEX_FLOAT, 10);

        res = LargeArrayArithmetics.diff(a, b);
        assertEquals(LargeArrayType.COMPLEX_FLOAT, res.getType());

        for (long i = 0; i < a.length(); i++) {
            assertEquals(a.getFloat(i) - ((ComplexFloatLargeArray) b).getComplexFloat(i)[0], ((ComplexFloatLargeArray) res).getComplexFloat(i)[0], DELTA);
            assertEquals(-((ComplexFloatLargeArray) b).getComplexFloat(i)[1], ((ComplexFloatLargeArray) res).getComplexFloat(i)[1], DELTA);
        }

        a = LargeArrayUtils.createConstant(LargeArrayType.BYTE, 10, (byte) 2);
        b = LargeArrayUtils.createConstant(LargeArrayType.FLOAT, 10, 3f);
        res = LargeArrayArithmetics.diff(a, b);
        assertTrue(res.isConstant());
        assertEquals(LargeArrayType.FLOAT, res.getType());
        for (long i = 0; i < a.length(); i++) {
            assertEquals(a.getFloat(i) - b.getFloat(i), res.getFloat(i), DELTA);
        }
    }

    public void testMult()
    {
        LargeArray.setMaxSizeOf32bitArray(1);
        
        LargeArray a = LargeArrayUtils.generateRandom(LargeArrayType.BYTE, 10);
        LargeArray b = LargeArrayUtils.generateRandom(LargeArrayType.FLOAT, 10);

        LargeArray res = LargeArrayArithmetics.mult(a, b);
        assertEquals(LargeArrayType.FLOAT, res.getType());
        for (long i = 0; i < a.length(); i++) {
            assertEquals(a.getFloat(i) * b.getFloat(i), res.getFloat(i), DELTA);
        }

        a = LargeArrayUtils.generateRandom(LargeArrayType.DOUBLE, 10);
        b = LargeArrayUtils.generateRandom(LargeArrayType.COMPLEX_FLOAT, 10);

        res = LargeArrayArithmetics.mult(a, b);
        assertEquals(LargeArrayType.COMPLEX_FLOAT, res.getType());

        for (long i = 0; i < a.length(); i++) {
            assertEquals(a.getFloat(i) * ((ComplexFloatLargeArray) b).getComplexFloat(i)[0], ((ComplexFloatLargeArray) res).getComplexFloat(i)[0], DELTA);
            assertEquals(a.getFloat(i) * ((ComplexFloatLargeArray) b).getComplexFloat(i)[1], ((ComplexFloatLargeArray) res).getComplexFloat(i)[1], DELTA);
        }

        a = LargeArrayUtils.createConstant(LargeArrayType.BYTE, 10, (byte) 2);
        b = LargeArrayUtils.createConstant(LargeArrayType.FLOAT, 10, 3f);
        res = LargeArrayArithmetics.mult(a, b);
        assertTrue(res.isConstant());
        assertEquals(LargeArrayType.FLOAT, res.getType());
        for (long i = 0; i < a.length(); i++) {
            assertEquals(a.getFloat(i) * b.getFloat(i), res.getFloat(i), DELTA);
        }
    }

    public void testDiv()
    {
        LargeArray.setMaxSizeOf32bitArray(1);
        
        LargeArray a = LargeArrayUtils.generateRandom(LargeArrayType.BYTE, 10);
        LargeArray b = LargeArrayUtils.generateRandom(LargeArrayType.FLOAT, 10);

        LargeArray res = LargeArrayArithmetics.div(a, b);
        assertEquals(LargeArrayType.FLOAT, res.getType());
        for (long i = 0; i < a.length(); i++) {
            assertEquals(a.getFloat(i) / b.getFloat(i), res.getFloat(i), DELTA);
        }

        a = LargeArrayUtils.generateRandom(LargeArrayType.DOUBLE, 10);
        b = LargeArrayUtils.generateRandom(LargeArrayType.COMPLEX_FLOAT, 10);

        res = LargeArrayArithmetics.div(a, b);
        assertEquals(LargeArrayType.COMPLEX_FLOAT, res.getType());

        for (long i = 0; i < a.length(); i++) {
            double r = ((ComplexFloatLargeArray) b).getComplexFloat(i)[0] * ((ComplexFloatLargeArray) b).getComplexFloat(i)[0] + ((ComplexFloatLargeArray) b).getComplexFloat(i)[1] * ((ComplexFloatLargeArray) b).getComplexFloat(i)[1];
            assertEquals(a.getFloat(i) * ((ComplexFloatLargeArray) b).getComplexFloat(i)[0] / r, ((ComplexFloatLargeArray) res).getComplexFloat(i)[0], DELTA);
            assertEquals(-a.getFloat(i) * ((ComplexFloatLargeArray) b).getComplexFloat(i)[1] / r, ((ComplexFloatLargeArray) res).getComplexFloat(i)[1], DELTA);
        }

        a = LargeArrayUtils.createConstant(LargeArrayType.BYTE, 10, (byte) 2);
        b = LargeArrayUtils.createConstant(LargeArrayType.FLOAT, 10, 3f);
        res = LargeArrayArithmetics.div(a, b);
        assertTrue(res.isConstant());
        assertEquals(LargeArrayType.FLOAT, res.getType());
        for (long i = 0; i < a.length(); i++) {
            assertEquals(a.getFloat(i) / b.getFloat(i), res.getFloat(i), DELTA);
        }
    }

    public void testPow()
    {
        LargeArray.setMaxSizeOf32bitArray(1);
        double n = 2.2;
        
        LargeArray a = LargeArrayUtils.generateRandom(LargeArrayType.FLOAT, 10);

        LargeArray res = LargeArrayArithmetics.pow(a, n);
        assertEquals(LargeArrayType.FLOAT, res.getType());
        for (long i = 0; i < a.length(); i++) {
            assertEquals(Math.pow(a.getDouble(i), n), res.getFloat(i), DELTA);
        }

        a = LargeArrayUtils.generateRandom(LargeArrayType.COMPLEX_FLOAT, 10);

        res = LargeArrayArithmetics.pow(a, n);
        assertEquals(LargeArrayType.COMPLEX_FLOAT, res.getType());
        ComplexFloatLargeArray ac = (ComplexFloatLargeArray) a;
        float[] elem_res = new float[2];
        for (long i = 0; i < a.length(); i++) {
            float[] elem_a = ac.getComplexFloat(i);
            double mod = Math.pow(Math.sqrt(elem_a[0] * elem_a[0] + elem_a[1] * elem_a[1]), n);
            double arg = Math.atan2(elem_a[1], elem_a[0]);
            elem_res[0] = (float) (mod * Math.cos(n * arg));
            elem_res[1] = (float) (mod * Math.sin(n * arg));
            assertEquals(elem_res[0], ((ComplexFloatLargeArray) res).getComplexFloat(i)[0], DELTA);
            assertEquals(elem_res[1], ((ComplexFloatLargeArray) res).getComplexFloat(i)[1], DELTA);
        }

        a = LargeArrayUtils.createConstant(LargeArrayType.BYTE, 10, (byte) 2);
        res = LargeArrayArithmetics.pow(a, n);
        assertTrue(res.isConstant());
        assertEquals(LargeArrayType.FLOAT, res.getType());
        for (long i = 0; i < a.length(); i++) {
            assertEquals(Math.pow(a.getFloat(i), n), res.getFloat(i), DELTA);
        }
    }
    
    public void testPow2()
    {
        LargeArray.setMaxSizeOf32bitArray(1);
        
        LargeArray a = LargeArrayUtils.generateRandom(LargeArrayType.FLOAT, 10);
        LargeArray b = LargeArrayUtils.generateRandom(LargeArrayType.FLOAT, 10);

        LargeArray res = LargeArrayArithmetics.pow(a, b);
        assertEquals(LargeArrayType.FLOAT, res.getType());
        for (long i = 0; i < a.length(); i++) {
            assertEquals(FastMath.pow(a.getFloat(i), b.getFloat(i)), res.getFloat(i), DELTA);
        }

        a = LargeArrayUtils.generateRandom(LargeArrayType.DOUBLE, 10);
        b = LargeArrayUtils.generateRandom(LargeArrayType.COMPLEX_FLOAT, 10);

        res = LargeArrayArithmetics.pow(a, b);
        assertEquals(LargeArrayType.COMPLEX_FLOAT, res.getType());

        for (long i = 0; i < a.length(); i++) {
            Assert.assertArrayEquals(LargeArrayArithmetics.complexPow(new float[] {a.getFloat(i), 0f}, ((ComplexFloatLargeArray) b).getComplexFloat(i)), ((ComplexFloatLargeArray) res).getComplexFloat(i), (float)DELTA);
        }

        a = LargeArrayUtils.createConstant(LargeArrayType.FLOAT, 10, 2);
        b = LargeArrayUtils.createConstant(LargeArrayType.FLOAT, 10, 3);
        res = LargeArrayArithmetics.pow(a, b);
        assertTrue(res.isConstant());
        assertEquals(LargeArrayType.FLOAT, res.getType());
        for (long i = 0; i < a.length(); i++) {
            assertEquals(FastMath.pow(a.getFloat(i), b.getFloat(i)), res.getFloat(i), DELTA);
        }
    }

    public void testNeg()
    {
        LargeArray.setMaxSizeOf32bitArray(1);
        
        LargeArray a = LargeArrayUtils.generateRandom(LargeArrayType.BYTE, 10);

        LargeArray res = LargeArrayArithmetics.neg(a);
        assertEquals(LargeArrayType.BYTE, res.getType());
        for (long i = 0; i < a.length(); i++) {
            assertEquals((byte) (-a.getByte(i)), res.getByte(i));
        }

        a = LargeArrayUtils.generateRandom(LargeArrayType.COMPLEX_FLOAT, 10);

        res = LargeArrayArithmetics.neg(a);
        assertEquals(LargeArrayType.COMPLEX_FLOAT, res.getType());
        ComplexFloatLargeArray ac = (ComplexFloatLargeArray) a;
        for (long i = 0; i < a.length(); i++) {
            float[] elem_a = ac.getComplexFloat(i);
            assertEquals(-elem_a[0], ((ComplexFloatLargeArray) res).getComplexFloat(i)[0], DELTA);
            assertEquals(-elem_a[1], ((ComplexFloatLargeArray) res).getComplexFloat(i)[1], DELTA);
        }

        a = LargeArrayUtils.createConstant(LargeArrayType.BYTE, 10, (byte) 2);
        res = LargeArrayArithmetics.neg(a);
        assertTrue(res.isConstant());
        assertEquals(LargeArrayType.BYTE, res.getType());
        for (long i = 0; i < a.length(); i++) {
            assertEquals((byte) (-a.getByte(i)), res.getByte(i));
        }
    }

    public void testSqrt()
    {
        LargeArray.setMaxSizeOf32bitArray(1);
        
        LargeArray a = LargeArrayUtils.generateRandom(LargeArrayType.FLOAT, 10);

        LargeArray res = LargeArrayArithmetics.sqrt(a);
        assertEquals(LargeArrayType.FLOAT, res.getType());
        for (long i = 0; i < a.length(); i++) {
            assertEquals(Math.sqrt(a.getDouble(i)), res.getFloat(i), DELTA);
        }

        a = LargeArrayUtils.generateRandom(LargeArrayType.COMPLEX_FLOAT, 10);

        res = LargeArrayArithmetics.sqrt(a);
        assertEquals(LargeArrayType.COMPLEX_FLOAT, res.getType());
        ComplexFloatLargeArray ac = (ComplexFloatLargeArray) a;
        float[] elem_res = new float[2];
        for (long i = 0; i < a.length(); i++) {
            float[] elem_a = ac.getComplexFloat(i);
            double mod = Math.sqrt(elem_a[0] * elem_a[0] + elem_a[1] * elem_a[1]);
            elem_res[0] = (float) (Math.sqrt((elem_a[0] + mod) / 2.0));
            elem_res[1] = (float) (Math.signum(elem_a[1]) * Math.sqrt((-elem_a[0] + mod) / 2.0));
            assertEquals(elem_res[0], ((ComplexFloatLargeArray) res).getComplexFloat(i)[0], DELTA);
            assertEquals(elem_res[1], ((ComplexFloatLargeArray) res).getComplexFloat(i)[1], DELTA);
        }

        a = LargeArrayUtils.createConstant(LargeArrayType.BYTE, 10, (byte) 2);
        res = LargeArrayArithmetics.sqrt(a);
        assertTrue(res.isConstant());
        assertEquals(LargeArrayType.FLOAT, res.getType());
        for (long i = 0; i < a.length(); i++) {
            assertEquals(Math.sqrt(a.getFloat(i)), res.getFloat(i), DELTA);
        }
    }

    public void testLog()
    {
        LargeArray.setMaxSizeOf32bitArray(1);
        
        LargeArray a = LargeArrayUtils.generateRandom(LargeArrayType.FLOAT, 10);

        LargeArray res = LargeArrayArithmetics.log(a);
        assertEquals(LargeArrayType.FLOAT, res.getType());
        for (long i = 0; i < a.length(); i++) {
            assertEquals(Math.log(a.getDouble(i)), res.getFloat(i), DELTA);
        }

        a = LargeArrayUtils.generateRandom(LargeArrayType.COMPLEX_FLOAT, 10);

        res = LargeArrayArithmetics.log(a);
        assertEquals(LargeArrayType.COMPLEX_FLOAT, res.getType());
        ComplexFloatLargeArray ac = (ComplexFloatLargeArray) a;
        float[] elem_res = new float[2];
        for (long i = 0; i < a.length(); i++) {
            float[] elem_a = ac.getComplexFloat(i);
            double mod = Math.sqrt(elem_a[0] * elem_a[0] + elem_a[1] * elem_a[1]);
            double arg = Math.atan2(elem_a[1], elem_a[0]);
            elem_res[0] = (float) (Math.log(mod));
            elem_res[1] = (float) arg;
            assertEquals(elem_res[0], ((ComplexFloatLargeArray) res).getComplexFloat(i)[0], DELTA);
            assertEquals(elem_res[1], ((ComplexFloatLargeArray) res).getComplexFloat(i)[1], DELTA);
        }

        a = LargeArrayUtils.createConstant(LargeArrayType.BYTE, 10, (byte) 2);
        res = LargeArrayArithmetics.log(a);
        assertTrue(res.isConstant());
        assertEquals(LargeArrayType.FLOAT, res.getType());
        for (long i = 0; i < a.length(); i++) {
            assertEquals(Math.log(a.getFloat(i)), res.getFloat(i), DELTA);
        }
    }

    public void testLog10()
    {
        LargeArray.setMaxSizeOf32bitArray(1);
        
        LargeArray a = LargeArrayUtils.generateRandom(LargeArrayType.FLOAT, 10);

        LargeArray res = LargeArrayArithmetics.log10(a);
        assertEquals(LargeArrayType.FLOAT, res.getType());
        for (long i = 0; i < a.length(); i++) {
            assertEquals(Math.log10(a.getDouble(i)), res.getFloat(i), DELTA);
        }

        a = LargeArrayUtils.generateRandom(LargeArrayType.COMPLEX_FLOAT, 10);

        res = LargeArrayArithmetics.log10(a);
        assertEquals(LargeArrayType.COMPLEX_FLOAT, res.getType());
        ComplexFloatLargeArray ac = (ComplexFloatLargeArray) a;
        float[] elem_res;
        for (long i = 0; i < a.length(); i++) {
            float[] elem_a = ac.getComplexFloat(i);
            elem_res = LargeArrayArithmetics.complexLog10(elem_a);
            assertEquals(elem_res[0], ((ComplexFloatLargeArray) res).getComplexFloat(i)[0], DELTA);
            assertEquals(elem_res[1], ((ComplexFloatLargeArray) res).getComplexFloat(i)[1], DELTA);
        }

        a = LargeArrayUtils.createConstant(LargeArrayType.BYTE, 10, (byte) 2);
        res = LargeArrayArithmetics.log10(a);
        assertTrue(res.isConstant());
        assertEquals(LargeArrayType.FLOAT, res.getType());
        for (long i = 0; i < a.length(); i++) {
            assertEquals(Math.log10(a.getFloat(i)), res.getFloat(i), DELTA);
        }
    }

    public void testExp()
    {
        LargeArray.setMaxSizeOf32bitArray(1);
        
        LargeArray a = LargeArrayUtils.generateRandom(LargeArrayType.FLOAT, 10);

        LargeArray res = LargeArrayArithmetics.exp(a);
        assertEquals(LargeArrayType.FLOAT, res.getType());
        for (long i = 0; i < a.length(); i++) {
            assertEquals(Math.exp(a.getDouble(i)), res.getFloat(i), DELTA);
        }

        a = LargeArrayUtils.generateRandom(LargeArrayType.COMPLEX_FLOAT, 10);

        res = LargeArrayArithmetics.exp(a);
        assertEquals(LargeArrayType.COMPLEX_FLOAT, res.getType());
        ComplexFloatLargeArray ac = (ComplexFloatLargeArray) a;
        float[] elem_res = new float[2];
        for (long i = 0; i < a.length(); i++) {
            float[] elem_a = ac.getComplexFloat(i);
            elem_res[0] = (float) (Math.exp(elem_a[0]) * Math.cos(elem_a[1]));
            elem_res[1] = (float) (Math.exp(elem_a[0]) * Math.sin(elem_a[1]));
            assertEquals(elem_res[0], ((ComplexFloatLargeArray) res).getComplexFloat(i)[0], DELTA);
            assertEquals(elem_res[1], ((ComplexFloatLargeArray) res).getComplexFloat(i)[1], DELTA);
        }

        a = LargeArrayUtils.createConstant(LargeArrayType.BYTE, 10, (byte) 2);
        res = LargeArrayArithmetics.exp(a);
        assertTrue(res.isConstant());
        assertEquals(LargeArrayType.FLOAT, res.getType());
        for (long i = 0; i < a.length(); i++) {
            assertEquals(Math.exp(a.getFloat(i)), res.getFloat(i), DELTA);
        }
    }

    public void testAbs()
    {
        LargeArray.setMaxSizeOf32bitArray(1);
        
        LargeArray a = LargeArrayUtils.generateRandom(LargeArrayType.FLOAT, 10);

        LargeArray res = LargeArrayArithmetics.abs(a);
        assertEquals(LargeArrayType.FLOAT, res.getType());
        for (long i = 0; i < a.length(); i++) {
            assertEquals(Math.abs(a.getDouble(i)), res.getFloat(i), DELTA);
        }

        a = LargeArrayUtils.generateRandom(LargeArrayType.COMPLEX_FLOAT, 10);

        res = LargeArrayArithmetics.abs(a);
        assertEquals(LargeArrayType.FLOAT, res.getType());
        ComplexFloatLargeArray ac = (ComplexFloatLargeArray) a;
        for (long i = 0; i < a.length(); i++) {
            float[] elem_a = ac.getComplexFloat(i);
            assertEquals(Math.sqrt(elem_a[0] * elem_a[0] + elem_a[1] * elem_a[1]), res.getFloat(i), DELTA);
        }

        a = LargeArrayUtils.createConstant(LargeArrayType.BYTE, 10, (byte) 2);
        res = LargeArrayArithmetics.abs(a);
        assertTrue(res.isConstant());
        assertEquals(LargeArrayType.BYTE, res.getType());
        for (long i = 0; i < a.length(); i++) {
            assertEquals(a.getByte(i), res.getByte(i));
        }
    }

    public void testSin()
    {
        LargeArray.setMaxSizeOf32bitArray(1);
        
        LargeArray a = LargeArrayUtils.generateRandom(LargeArrayType.FLOAT, 10);

        LargeArray res = LargeArrayArithmetics.sin(a);
        assertEquals(LargeArrayType.FLOAT, res.getType());
        for (long i = 0; i < a.length(); i++) {
            assertEquals(Math.sin(a.getDouble(i)), res.getFloat(i), DELTA);
        }

        a = LargeArrayUtils.generateRandom(LargeArrayType.COMPLEX_FLOAT, 10);

        res = LargeArrayArithmetics.sin(a);
        assertEquals(LargeArrayType.COMPLEX_FLOAT, res.getType());
        ComplexFloatLargeArray ac = (ComplexFloatLargeArray) a;
        float[] elem_res = new float[2];
        for (long i = 0; i < a.length(); i++) {
            float[] elem_a = ac.getComplexFloat(i);
            elem_res[0] = (float) (Math.sin(elem_a[0]) * Math.cosh(elem_a[1]));
            elem_res[1] = (float) (Math.cos(elem_a[0]) * Math.sinh(elem_a[1]));
            assertEquals(elem_res[0], ((ComplexFloatLargeArray) res).getComplexFloat(i)[0], DELTA);
            assertEquals(elem_res[1], ((ComplexFloatLargeArray) res).getComplexFloat(i)[1], DELTA);
        }

        a = LargeArrayUtils.createConstant(LargeArrayType.BYTE, 10, (byte) 2);
        res = LargeArrayArithmetics.sin(a);
        assertTrue(res.isConstant());
        assertEquals(LargeArrayType.FLOAT, res.getType());
        for (long i = 0; i < a.length(); i++) {
            assertEquals(Math.sin(a.getFloat(i)), res.getFloat(i), DELTA);
        }
    }

    public void testCos()
    {
        LargeArray.setMaxSizeOf32bitArray(1);
        
        LargeArray a = LargeArrayUtils.generateRandom(LargeArrayType.FLOAT, 10);

        LargeArray res = LargeArrayArithmetics.cos(a);
        assertEquals(LargeArrayType.FLOAT, res.getType());
        for (long i = 0; i < a.length(); i++) {
            assertEquals(Math.cos(a.getDouble(i)), res.getFloat(i), DELTA);
        }

        a = LargeArrayUtils.generateRandom(LargeArrayType.COMPLEX_FLOAT, 10);

        res = LargeArrayArithmetics.cos(a);
        assertEquals(LargeArrayType.COMPLEX_FLOAT, res.getType());
        ComplexFloatLargeArray ac = (ComplexFloatLargeArray) a;
        float[] elem_res = new float[2];
        for (long i = 0; i < a.length(); i++) {
            float[] elem_a = ac.getComplexFloat(i);
            elem_res[0] = (float) (Math.cos(elem_a[0]) * Math.cosh(elem_a[1]));
            elem_res[1] = (float) (-Math.sin(elem_a[0]) * Math.sinh(elem_a[1]));
            assertEquals(elem_res[0], ((ComplexFloatLargeArray) res).getComplexFloat(i)[0], DELTA);
            assertEquals(elem_res[1], ((ComplexFloatLargeArray) res).getComplexFloat(i)[1], DELTA);
        }

        a = LargeArrayUtils.createConstant(LargeArrayType.BYTE, 10, (byte) 2);
        res = LargeArrayArithmetics.cos(a);
        assertTrue(res.isConstant());
        assertEquals(LargeArrayType.FLOAT, res.getType());
        for (long i = 0; i < a.length(); i++) {
            assertEquals(Math.cos(a.getFloat(i)), res.getFloat(i), DELTA);
        }
    }

    public void testTan()
    {
        LargeArray.setMaxSizeOf32bitArray(1);
        
        LargeArray a = LargeArrayUtils.generateRandom(LargeArrayType.FLOAT, 10);

        LargeArray res = LargeArrayArithmetics.tan(a);
        assertEquals(LargeArrayType.FLOAT, res.getType());
        for (long i = 0; i < a.length(); i++) {
            assertEquals(Math.tan(a.getDouble(i)), res.getFloat(i), DELTA);
        }

        a = LargeArrayUtils.generateRandom(LargeArrayType.COMPLEX_FLOAT, 10);

        res = LargeArrayArithmetics.tan(a);
        assertEquals(LargeArrayType.COMPLEX_FLOAT, res.getType());
        ComplexFloatLargeArray ac = (ComplexFloatLargeArray) a;
        for (long i = 0; i < a.length(); i++) {
            float[] elem_a = ac.getComplexFloat(i);
            float[] s = complexSin(elem_a);
            float[] c = complexCos(elem_a);
            float[] elem_res = complexDiv(s, c);
            assertEquals(elem_res[0], ((ComplexFloatLargeArray) res).getComplexFloat(i)[0], DELTA);
            assertEquals(elem_res[1], ((ComplexFloatLargeArray) res).getComplexFloat(i)[1], DELTA);
        }

        a = LargeArrayUtils.createConstant(LargeArrayType.BYTE, 10, (byte) 2);
        res = LargeArrayArithmetics.tan(a);
        assertTrue(res.isConstant());
        assertEquals(LargeArrayType.FLOAT, res.getType());
        for (long i = 0; i < a.length(); i++) {
            assertEquals(Math.tan(a.getFloat(i)), res.getFloat(i), DELTA);
        }
    }

    public void testAsin()
    {
        LargeArray.setMaxSizeOf32bitArray(1);
        
        LargeArray a = LargeArrayUtils.generateRandom(LargeArrayType.FLOAT, 10);

        LargeArray res = LargeArrayArithmetics.asin(a);
        assertEquals(LargeArrayType.FLOAT, res.getType());
        for (long i = 0; i < a.length(); i++) {
            assertEquals(Math.asin(a.getDouble(i)), res.getFloat(i), DELTA);
        }

        a = LargeArrayUtils.generateRandom(LargeArrayType.COMPLEX_FLOAT, 10);

        res = LargeArrayArithmetics.asin(a);
        assertEquals(LargeArrayType.COMPLEX_FLOAT, res.getType());
        ComplexFloatLargeArray ac = (ComplexFloatLargeArray) a;
        for (long i = 0; i < a.length(); i++) {
            float[] elem_a = ac.getComplexFloat(i);
            float[] elem_res = complexAsin(elem_a);
            assertEquals(elem_res[0], ((ComplexFloatLargeArray) res).getComplexFloat(i)[0], DELTA);
            assertEquals(elem_res[1], ((ComplexFloatLargeArray) res).getComplexFloat(i)[1], DELTA);
        }

        a = LargeArrayUtils.createConstant(LargeArrayType.BYTE, 10, (byte) 2);
        res = LargeArrayArithmetics.asin(a);
        assertTrue(res.isConstant());
        assertEquals(LargeArrayType.FLOAT, res.getType());
        for (long i = 0; i < a.length(); i++) {
            assertEquals(Math.asin(a.getFloat(i)), res.getFloat(i), DELTA);
        }
    }

    public void testAcos()
    {
        LargeArray.setMaxSizeOf32bitArray(1);
        
        LargeArray a = LargeArrayUtils.generateRandom(LargeArrayType.FLOAT, 10);

        LargeArray res = LargeArrayArithmetics.acos(a);
        assertEquals(LargeArrayType.FLOAT, res.getType());
        for (long i = 0; i < a.length(); i++) {
            assertEquals(Math.acos(a.getDouble(i)), res.getFloat(i), DELTA);
        }

        a = LargeArrayUtils.generateRandom(LargeArrayType.COMPLEX_FLOAT, 10);

        res = LargeArrayArithmetics.acos(a);
        assertEquals(LargeArrayType.COMPLEX_FLOAT, res.getType());
        ComplexFloatLargeArray ac = (ComplexFloatLargeArray) a;
        for (long i = 0; i < a.length(); i++) {
            float[] elem_a = ac.getComplexFloat(i);
            float[] elem_res = complexAcos(elem_a);
            assertEquals(elem_res[0], ((ComplexFloatLargeArray) res).getComplexFloat(i)[0], DELTA);
            assertEquals(elem_res[1], ((ComplexFloatLargeArray) res).getComplexFloat(i)[1], DELTA);
        }

        a = LargeArrayUtils.createConstant(LargeArrayType.BYTE, 10, (byte) 2);
        res = LargeArrayArithmetics.acos(a);
        assertTrue(res.isConstant());
        assertEquals(LargeArrayType.FLOAT, res.getType());
        for (long i = 0; i < a.length(); i++) {
            assertEquals(Math.acos(a.getFloat(i)), res.getFloat(i), DELTA);
        }
    }

    public void testAtan()
    {
        LargeArray.setMaxSizeOf32bitArray(1);
        
        LargeArray a = LargeArrayUtils.generateRandom(LargeArrayType.FLOAT, 10);

        LargeArray res = LargeArrayArithmetics.atan(a);
        assertEquals(LargeArrayType.FLOAT, res.getType());
        for (long i = 0; i < a.length(); i++) {
            assertEquals(Math.atan(a.getDouble(i)), res.getFloat(i), DELTA);
        }

        a = LargeArrayUtils.generateRandom(LargeArrayType.COMPLEX_FLOAT, 10);

        res = LargeArrayArithmetics.atan(a);
        assertEquals(LargeArrayType.COMPLEX_FLOAT, res.getType());
        ComplexFloatLargeArray ac = (ComplexFloatLargeArray) a;
        for (long i = 0; i < a.length(); i++) {
            float[] elem_a = ac.getComplexFloat(i);
            float[] elem_res = complexAtan(elem_a);
            assertEquals(elem_res[0], ((ComplexFloatLargeArray) res).getComplexFloat(i)[0], DELTA);
            assertEquals(elem_res[1], ((ComplexFloatLargeArray) res).getComplexFloat(i)[1], DELTA);
        }

        a = LargeArrayUtils.createConstant(LargeArrayType.BYTE, 10, (byte) 2);
        res = LargeArrayArithmetics.atan(a);
        assertTrue(res.isConstant());
        assertEquals(LargeArrayType.FLOAT, res.getType());
        for (long i = 0; i < a.length(); i++) {
            assertEquals(Math.atan(a.getFloat(i)), res.getFloat(i), DELTA);
        }
    }

    public void testSignum()
    {
        LargeArray.setMaxSizeOf32bitArray(1);
        
        LargeArray a = LargeArrayUtils.generateRandom(LargeArrayType.BYTE, 10);

        LargeArray res = LargeArrayArithmetics.signum(a);
        assertEquals(LargeArrayType.BYTE, res.getType());
        for (long i = 0; i < a.length(); i++) {
            assertEquals((byte) Math.signum(a.getByte(i)), res.getByte(i));
        }

        a = LargeArrayUtils.createConstant(LargeArrayType.BYTE, 10, (byte) 2);
        res = LargeArrayArithmetics.signum(a);
        assertTrue(res.isConstant());
        assertEquals(LargeArrayType.BYTE, res.getType());
        for (long i = 0; i < a.length(); i++) {
            assertEquals((byte) Math.signum(a.getByte(i)), res.getByte(i));
        }
    }
}
