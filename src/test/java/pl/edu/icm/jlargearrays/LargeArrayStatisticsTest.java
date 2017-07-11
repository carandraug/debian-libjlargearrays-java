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
 * LargeArrayArithmetics unit tests.
 *
 * @author Piotr Wendykier (p.wendykier@icm.edu.pl)
 */
public class LargeArrayStatisticsTest extends TestCase
{

    private static final double DELTA = 1E-6;

    /**
     * Create the test case
     *
     * @param testName name of the test case
     */
    public LargeArrayStatisticsTest(String testName)
    {
        super(testName);
    }

    /**
     * @return the suite of tests being tested
     */
    public static Test suite()
    {
        return new TestSuite(LargeArrayStatisticsTest.class);
    }

    public void testMin()
    {
        LargeArray.setMaxSizeOf32bitArray(1);
        LargeArray a = LargeArrayUtils.generateRandom(LargeArrayType.FLOAT, 10);

        double res = LargeArrayStatistics.min(a);
        double min = a.getDouble(0);
        for (long i = 1; i < a.length(); i++) {
            if (a.getDouble(i) < min) {
                min = a.getDouble(i);
            }
        }
        assertEquals(min, res, DELTA);

        a = LargeArrayUtils.createConstant(LargeArrayType.BYTE, 10, (byte) 2);
        res = LargeArrayStatistics.min(a);
        assertEquals(2.0, res);
    }

    public void testMax()
    {
        LargeArray.setMaxSizeOf32bitArray(1);
        LargeArray a = LargeArrayUtils.generateRandom(LargeArrayType.FLOAT, 10);

        double res = LargeArrayStatistics.max(a);
        double max = a.getDouble(0);
        for (long i = 1; i < a.length(); i++) {
            if (a.getDouble(i) > max) {
                max = a.getDouble(i);
            }
        }
        assertEquals(max, res, DELTA);

        a = LargeArrayUtils.createConstant(LargeArrayType.BYTE, 10, (byte) 2);
        res = LargeArrayStatistics.max(a);
        assertEquals(2.0, res);

    }
    
     public void testSum()
    {
        LargeArray.setMaxSizeOf32bitArray(1);
        LargeArray a = LargeArrayUtils.generateRandom(LargeArrayType.FLOAT, 10);

        double res = LargeArrayStatistics.sum(a);
        double sum = 0;
        for (long i = 0; i < a.length(); i++) {
            sum += a.getDouble(i);
        }
        assertEquals(sum, res, DELTA);

        a = LargeArrayUtils.createConstant(LargeArrayType.BYTE, 10, (byte) 2);
        res = LargeArrayStatistics.sum(a);
        assertEquals(20.0, res);

    }
    
    public void testSumKahan()
    {
        LargeArray.setMaxSizeOf32bitArray(1);
        LargeArray a = LargeArrayUtils.generateRandom(LargeArrayType.FLOAT, 10);

        double res = LargeArrayStatistics.sumKahan(a);
        double sum = 0;
        for (long i = 0; i < a.length(); i++) {
            sum += a.getDouble(i);
        }
        assertEquals(sum, res, DELTA);

        a = LargeArrayUtils.createConstant(LargeArrayType.BYTE, 10, (byte) 2);
        res = LargeArrayStatistics.sumKahan(a);
        assertEquals(20.0, res);

    }


    public void testAvg()
    {
        LargeArray.setMaxSizeOf32bitArray(1);
        LargeArray a = LargeArrayUtils.generateRandom(LargeArrayType.FLOAT, 10);

        double res = LargeArrayStatistics.avg(a);
        double sum = 0;
        for (long i = 0; i < a.length(); i++) {
            sum += a.getDouble(i);
        }
        assertEquals(sum / a.length(), res, DELTA);

        a = LargeArrayUtils.createConstant(LargeArrayType.BYTE, 10, (byte) 2);
        res = LargeArrayStatistics.avg(a);
        assertEquals(2.0, res);

    }
    
    public void testAvgKahan()
    {
        LargeArray.setMaxSizeOf32bitArray(1);
        LargeArray a = LargeArrayUtils.generateRandom(LargeArrayType.FLOAT, 10);

        double res = LargeArrayStatistics.avgKahan(a);
        double sum = 0;
        for (long i = 0; i < a.length(); i++) {
            sum += a.getDouble(i);
        }
        assertEquals(sum / a.length(), res, DELTA);

        a = LargeArrayUtils.createConstant(LargeArrayType.BYTE, 10, (byte) 2);
        res = LargeArrayStatistics.avgKahan(a);
        assertEquals(2.0, res);

    }

    public void testStd()
    {
        LargeArray.setMaxSizeOf32bitArray(1);
        LargeArray a = LargeArrayUtils.generateRandom(LargeArrayType.FLOAT, 10);

        double res = LargeArrayStatistics.std(a);
        double sum = 0;
        double sum2 = 0;
        for (long i = 0; i < a.length(); i++) {
            sum += a.getDouble(i);
            sum2 += (a.getDouble(i) * a.getDouble(i));
        }
        sum /= a.length();
        sum2 /= a.length();
        assertEquals(Math.sqrt(Math.max(0, sum2 - sum * sum)), res, DELTA);

        a = LargeArrayUtils.createConstant(LargeArrayType.BYTE, 10, (byte) 2);
        res = LargeArrayStatistics.std(a);
        assertEquals(0.0, res);

    }
    
    public void testStdKahan()
    {
        LargeArray.setMaxSizeOf32bitArray(1);
        LargeArray a = LargeArrayUtils.generateRandom(LargeArrayType.FLOAT, 10);

        double res = LargeArrayStatistics.stdKahan(a);
        double sum = 0;
        double sum2 = 0;
        for (long i = 0; i < a.length(); i++) {
            sum += a.getDouble(i);
            sum2 += (a.getDouble(i) * a.getDouble(i));
        }
        sum /= a.length();
        sum2 /= a.length();
        assertEquals(Math.sqrt(Math.max(0, sum2 - sum * sum)), res, DELTA);

        a = LargeArrayUtils.createConstant(LargeArrayType.BYTE, 10, (byte) 2);
        res = LargeArrayStatistics.stdKahan(a);
        assertEquals(0.0, res);

    }
}
