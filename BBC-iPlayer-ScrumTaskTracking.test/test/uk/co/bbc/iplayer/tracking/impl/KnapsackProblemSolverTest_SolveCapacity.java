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
 * Performs boundary value testing on the capacity used by the Knapsack Problem
 * Solver.
 */
@RunWith(Parameterized.class)
public class KnapsackProblemSolverTest_SolveCapacity
{
    /**
     * The data for boundary value testing of the sprint's capacity
     * @return  A collection of boundary values and the expected test outcome.
     */
    @Parameters
    public static Collection<Object[]> testExecutionValues()
    {
        Object[][] data = new Object[][]
                {
                    {Integer.MIN_VALUE, new ArrayList<>()},
                    {-100,              new ArrayList<>()},
                    {-1,                new ArrayList<>()},
                    {0,                 new ArrayList<>()},
                    {1,                 Arrays.asList(new Story("5", 1, 6))},
                    {2,                 Arrays.asList(new Story("3", 2, 2))},
                    {6,                 Arrays.asList(new Story("3", 2, 2),
                                                      new Story("4", 3, 2),
                                                      new Story("5", 1, 6))},
                    //Can't go too high because of a capacity (it is an 
                    //  NP-complete problem solver).
                    {1000,              Arrays.asList(new Story("3", 2, 2),
                                                      new Story("1", 4, 3),
                                                      new Story("2", 3, 4),
                                                      new Story("4", 3, 2),
                                                      new Story("5", 1, 6))}
                    //Per the contract, Integer.MAX_VALUE is not permitted.
                };
        return Arrays.asList(data);
    }

    
    
    //-------------------------------------------------------------------------
    //  DATA MEMBERS
    //-------------------------------------------------------------------------
    /**
     * The capacity.
     */
    private int capacity;
    
    /**
     * The expected result of the test.
     */
    private List<Story> expectedResults;
    
    
    
    //-------------------------------------------------------------------------
    //  CONSTRUCTOR
    //-------------------------------------------------------------------------
    /**
     * Constructor for Parameterized Test
     * @param capcity  the capacity of the knapsack
     * @param expectedResults  the expected results from the knapsack problem
     */
    public KnapsackProblemSolverTest_SolveCapacity(int capacity, 
                                                   List<Story> expectedResults)
    {
        this.capacity = capacity;
        this.expectedResults = expectedResults;
    }
    
    
    
    //-------------------------------------------------------------------------
    //  TEST CASES
    //-------------------------------------------------------------------------
    
    /**
     * Test method for {@link uk.co.bbc.iplayer.tracking.impl.KnapsackProblemSolver#solve(java.util.List, int)}.
     * 
     * Run the same test again with a different order and confirm that the order
     * is preserved.  Need to check this because I need to guarantee a given
     * order of the solution.
     */
    @Test
    public void testSolve()
    {
        List<Story> stories = new ArrayList<>();
        stories.add(new Story("3", 2, 2));
        stories.add(new Story("1", 4, 3));
        stories.add(new Story("2", 3, 4));
        stories.add(new Story("4", 3, 2));
        stories.add(new Story("5", 1, 6));
        
        List<Story> optStories = KnapsackProblemSolver.solve(stories, 
                                                             capacity);
        
        Assert.assertNotNull(optStories);
        
        //optStories should contain stories 3 and 4.
        Assert.assertEquals(expectedResults, optStories);
    }
}
