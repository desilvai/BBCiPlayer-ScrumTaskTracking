/**
 * Copyright (c) 2014, Ian J. De Silva
 * All rights reserved.
 * 
 * Use, distribution, and modification of this work for any purpose is strictly
 * prohibited without the express consent of the copyright holder except as 
 * permitted by law.
 */
package uk.co.bbc.iplayer.tracking;

import static org.junit.Assert.fail;

import java.util.Arrays;
import java.util.Collection;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

/**
 * This tests the add function of the IBacklog implementation.
 * 
 * For testing a story's points on add, there are the following equivalence
 * classes (assuming we cannot have negative point values -- See github question 
 * #1):
 *  - Less than 0  (negative-values)
 *  - Greater than or Equal to 0  (non-negative-values)
 *  
 * We therefore for boundary-value testing, need the following test cases:
 *  - Points == MIN_INT     Expect: fail
 *  - Points == -100        Expect: fail 
 *  - Points == -1          Expect: fail
 *  - Points == 0           Expect: success
 *  - Points == 1           Expect: success  -- Only one of these are needed
 *  - Points == 50          Expect: success  --
 *  - Points == MAX_INT     Expect: success
 *
 */
@RunWith(value = Parameterized.class)
public class BacklogTest_Add
{
    /**
     * The data for boundary value testing of the points part of the story to be
     * added.
     * @return  A collection of boundary values and the expected test outcome.
     */
    @Parameters
    public static Collection<Object[]> testExecutionValues()
    {
        Object[][] data = new Object[][]
                {
                    {Integer.MIN_VALUE, false},
                    {-100,              false},
                    {-1,                false},
                    {0,                 true},
                    {1,                 true},
                    {50,                true},
                    {Integer.MAX_VALUE, true}
                };
        return Arrays.asList(data);
    }
    
    
    //-------------------------------------------------------------------------
    //  DATA MEMBERS
    //-------------------------------------------------------------------------
    /**
     * The backlog under test.  We don't use an IBacklog here because we want to
     * inspect the object.
     */
    private Backlog backlog;
    
    /**
     * The story's points
     */
    private int points;
    
    /**
     * The expected outcome of the test.  This is true if the test should pass,
     * or false otherwise.
     */
    private boolean expectedResult;

    
    
    //-------------------------------------------------------------------------
    //  CONSTRUCTOR
    //-------------------------------------------------------------------------
    /**
     * Constructor for parameterized tests.
     * @param points  the number of points for the story.
     * @param expectedResults  the expected outcome of the test (true if the 
     *      test should succeed, false otherwise).
     */
    public BacklogTest_Add(int points, boolean expectedResult)
    {
        this.points = points;
        this.expectedResult = expectedResult;
        
        //Create a new one each time to make sure that we are working with a 
        //  clean backlog instance.
        this.backlog = new Backlog();
    }
    
    
    //-------------------------------------------------------------------------
    //  TEST CASES
    //-------------------------------------------------------------------------
    /**
     * Test method for 
     * {@link uk.co.bbc.iplayer.tracking.Backlog#Add(uk.co.bbc.iplayer.tracking.Story)}.
     * This attempts to add a story that we created with the given point value 
     * (all other parts of the story are valid) to the {@link IBacklog} object.
     * We then check for success or failure based on what we were told to expect
     * (if {@link #expectedResult} is true, we expect success, otherwise we 
     * expect failure).
     */
    @Test
    public void testAdd()
    {
        Story story = new Story();
        story.Points = this.points;
        story.Priority = 1;
        story.Id = null;
        
        this.backlog.Add(story);
        
        //TODO -- check that this succeeds/fails inside the object.
        if(this.expectedResult)
        {
            //check success
        }
        else
        {
            //check failure.
        }
        fail("Not yet implemented");
    }

}
