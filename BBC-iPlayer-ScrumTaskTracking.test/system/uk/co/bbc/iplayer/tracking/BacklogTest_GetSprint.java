/**
 * Copyright (c) 2014, Ian J. De Silva
 * All rights reserved.
 * 
 * Use, distribution, and modification of this work for any purpose is strictly
 * prohibited without the express consent of the copyright holder except as 
 * permitted by law.
 */
package uk.co.bbc.iplayer.tracking;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import uk.co.bbc.iplayer.tracking.exceptions.TaskTrackerException;
import uk.co.bbc.iplayer.tracking.impl.Backlog;
import uk.co.bbc.iplayer.tracking.impl.StoryDB;
import uk.co.bbc.iplayer.tracking.messages.Messages;
import uk.co.bbc.iplayer.tracking.test.infrastructure.TestUsingDB;

/**
 * This tests the getSprint method's input checking capabilities before it tries
 * to populate a list.. 
 * 
 * For testing a story's points on add, there are the following equivalence
 * classes (assuming we cannot have non-positive point values and we remain 
 * below MAX_INT):
 *  - Less than or equal to 0  (non-positive values)
 *  - Greater than 0 but less than MAX_INT  (positive-values)
 *  - MAX_INT                   (too big values)
 *  
 * We therefore for boundary-value testing, need the following test cases:
 *  - Points == MIN_INT      Expect: fail
 *  - Points == -100         Expect: fail     -- Only one of these are needed
 *  - Points == -1           Expect: fail     --
 *  - Points == 0            Expect: fail
 *  - Points == 1            Expect: success
 *  - Points == 2            Expect: success  -- Only one of these are needed
 *  - Points == 50           Expect: success  --
 *  - Points == MAX_INT - 1  Expect: fail
 *  - Points == MAX_INT      Expect: fail
 */
@RunWith(value = Parameterized.class)
public class BacklogTest_GetSprint extends TestUsingDB
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
                    {Integer.MIN_VALUE,     false},
                    {-100,                  false},
                    {-1,                    false},
                    {0,                     false},
                    {1,                     true},
                    {2,                     true},
                    {50,                    true},
                    {Integer.MAX_VALUE - 1, true},
                    {Integer.MAX_VALUE,     false}
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
     * The size of the sprint to plan.
     */
    private int sprintSize;
    
    /**
     * The expected outcome of the test.  This is true if the test should pass,
     * (or really, result in a positive outcome) or false otherwise.
     */
    private boolean expectSuccess;
    
    /**
     * The database containing the stories
     */
    private StoryDB storyDB;

    
    
    //-------------------------------------------------------------------------
    //  CONSTRUCTOR
    //-------------------------------------------------------------------------
    /**
     * Constructor for parameterized tests.
     * @param sprintSize  the size of the sprint to plan.
     * @param expectSuccess  the expected outcome of the test (true if the 
     *      test should succeed, false otherwise).
     */
    public BacklogTest_GetSprint(int sprintSize, boolean expectSuccess)
    {
        this.sprintSize = sprintSize;
        this.expectSuccess = expectSuccess;
        
        //Create a new one each time to make sure that we are working with a 
        //  clean backlog instance.
        this.backlog = new Backlog();
        
        this.storyDB = new StoryDB();
    }
    
    
    //-------------------------------------------------------------------------
    //  TEST CASES
    //-------------------------------------------------------------------------
    /**
     * Test method for 
     * {@link uk.co.bbc.iplayer.tracking.impl.Backlog#getSprint(int)}.
     * 
     * This allows us to verify that the sprint size is being checked before 
     * trying to assemble a sprint.
     * @throws TaskTrackerException   
     */
    @Test
    public void testGetSprint() throws TaskTrackerException
    {
        //Initialize the test case by adding the given stories to the database.
        List<Story> stories = Arrays.asList(new Story("Story 1",  3, 3),
                                            new Story("Story 2",  1, 3),
                                            new Story("Story 3",  1, 2),
                                            new Story("Story 4", 40, 1));
        
        for(Story story : stories)
        {
            //We skip add here since we don't need the extra checks and we 
            //  aren't testing that.
            this.storyDB.addStory(story);
        }
        

        //Plan an iteration
        List<Story> sprintPlan;
        try
        {
             sprintPlan = this.backlog.getSprint(this.sprintSize);
        }
        catch(TaskTrackerException e)
        {
            if(this.expectSuccess)
            {
                //We expected the call to succeed, but it didn't.  Rethrow the
                //      exception.
                throw e;
            }
            
            //else
            //Check that we got the right error message
            String expectedMessage;
            if(this.sprintSize == Integer.MAX_VALUE)
            {
                expectedMessage = Messages.getString("StoryPointsTooBig", Integer.MAX_VALUE);
            }
            else
            {
                expectedMessage = Messages.getString("StoryNonPositivePoints");
            }
            Assert.assertEquals(expectedMessage, e.getMessage());
            return;
        }
        
        if(!this.expectSuccess)
        {
            Assert.fail("Failed to catch a problem with planning a sprint "
                    + "with an invalid sprint size.");
        }
        
        //The call was a success and I expected as much.
        Assert.assertNotNull(sprintPlan);
        
        //I'm only checking the size, not the exact contents of the plan.
        //  Complete checking should be done elsewhere.
        Assert.assertTrue("The sprint plan should have been positive, "
                              + "instead it was " + sprintPlan.size() + ".", 
                          sprintPlan.size() > 0);
    }

}
