/**
 * Copyright (c) 2014, Ian J. De Silva
 * All rights reserved.
 * 
 * Use, distribution, and modification of this work for any purpose is strictly
 * prohibited without the express consent of the copyright holder except as 
 * permitted by law.
 */
package uk.co.bbc.iplayer.tracking.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import uk.co.bbc.iplayer.tracking.Story;

/**
 * Stress tests the knapsack problem solver (since it is NP-complete, we want to 
 * find some limitations).
 */
@RunWith(Parameterized.class)
public class KnapsackProblemSolverStressTest
{
    
    /**
     * The number of stories and the capacity we want to try.
     * @return  A collection of backlog sizes (the number of items to put in the
     *          backpack) and the max capacity we want to attempt.
     */
    @Parameters
    public static Collection<Object[]> testExecutionValues()
    {
        Object[][] data = new Object[][]
                {
                    //backlogSize,  sprintCapacity
                    {    1000,      1000},
                    {   10000,      1000},
                    {  100000,      1000}
                    //{ 1000000,       500}       --fails
                    //{ 1000000,      1000}     -- fails
                    //{  100000,     10000},    -- fails
                };
        return Arrays.asList(data);
    }

    
    
    //-------------------------------------------------------------------------
    //  DATA MEMBERS
    //-------------------------------------------------------------------------
    /**
     * The size of the backlog to test.
     */
    private int backlogSize;
    
    
    /**
     * The capacity of the backlog.
     */
    private int capacity;
    
    
    
    //-------------------------------------------------------------------------
    //  CONSTRUCTORS
    //-------------------------------------------------------------------------
    /**
     * Constructor
     * @param backlogSize  the number of stories available to put into the 
     *                     sprint.
     * @param capacity  the expected sprint size.
     */
    public KnapsackProblemSolverStressTest(int backlogSize, int capacity)
    {
        this.backlogSize = backlogSize;
        this.capacity    = capacity;
    }
    
    

    //-------------------------------------------------------------------------
    //  TEST CASES
    //-------------------------------------------------------------------------
    /**
     * Test method for {@link uk.co.bbc.iplayer.tracking.impl.KnapsackProblemSolver#solve(java.util.List, int)}.
     * 
     * Tests that we can solve a sufficiently large problem using the simplistic
     * {0,1}-knapsack algorithm.
     */
    @Test
    public void testSolve()
    {
        List<Story> backlog = new ArrayList<>();
        for(int k = 0; k < this.backlogSize; k++)
        {
            backlog.add(new Story(Integer.toString(k),
                                  100, 
                                  Integer.MAX_VALUE - (k % 10)));
        }

        try
        {
            //We don't care about the value, just that we didn't run out of 
            //  memory.
            KnapsackProblemSolver.solve(backlog, 
                                        this.capacity);
        }
        catch(OutOfMemoryError e)
        {
            Assert.fail("We ran out of memory when using a backlog of size " 
                        + backlog.size() 
                        + ", capacity of " 
                        + this.capacity 
                        + ", and maximum JVM memory size of " 
                        + Runtime.getRuntime().maxMemory());
        }
    }
}
