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
 * This tests the add function of the IBacklog implementation with respect to 
 * the priority values of the stories to be added. 
 * 
 * For testing a story's priority on add, there are the following equivalence
 * classes (assuming we cannot have non-positive priority values):
 *  - Less than or equal to 0  (non-positive values)
 *  - Greater than 0  (positive-values)
 *  
 * We therefore for boundary-value testing, need the following test cases:
 *  - Points == MIN_INT     Expect: fail
 *  - Points == -100        Expect: fail     -- Only one of these are needed
 *  - Points == -1          Expect: fail     --
 *  - Points == 0           Expect: fail
 *  - Points == 1           Expect: success
 *  - Points == 2           Expect: success  -- Only one of these are needed
 *  - Points == 50          Expect: success  --
 *  - Points == MAX_INT     Expect: success
 *
 */
@RunWith(value = Parameterized.class)
public class BacklogTest_AddPriority extends TestUsingDB
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
                    {0,                 false},
                    {1,                 true},
                    {2,                 true},
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
     * The story's priority
     */
    private int priority;
    
    /**
     * The expected outcome of the test.  This is true if the test should pass,
     * or false otherwise.
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
     * @param priority  the story's priority.
     * @param expectSuccess  the expected outcome of the test (true if the 
     *      test should succeed, false otherwise).
     */
    public BacklogTest_AddPriority(int priority, boolean expectSuccess)
    {
        this.priority = priority;
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
     * {@link uk.co.bbc.iplayer.tracking.impl.Backlog#Add(uk.co.bbc.iplayer.tracking.Story)}.
     * This attempts to add a story that we created with the given priority 
     * (all other parts of the story are valid) to the {@link IBacklog} object.
     * We then check for success or failure based on what we were told to expect
     * (if {@link #expectSuccess} is true, we expect success, otherwise we 
     * expect failure).
     * @throws TaskTrackerException 
     */
    @Test
    public void testAdd() throws TaskTrackerException
    {
        //The test input.  Only the points varies between executions.
        Story story = new Story();
        story.Priority = this.priority;
        story.Points = 3;
        story.Id = "TEST_ID";
        
        //We need this for comparing later.
        Story expected = new Story(story);
        
        try
        {
            this.backlog.Add(story);
        }
        catch(TaskTrackerException e)
        {
            if(this.expectSuccess)
            {
                //We expected success, but didn't actually succeed
                throw e;
            }
            
            //else
            //Check that we got the right error message
            String expectedMessage = Messages.getString("StoryNonPositivePriority"); 
            Assert.assertEquals(expectedMessage, e.getMessage());
            
            //Check that the DB is still empty.
            Assert.assertEquals(0, this.storyDB.getStoryCount());
            return;
        }
        
        //Check for success or failure.
        if(!this.expectSuccess)
        {
            //Failure occurred.
            Assert.fail("Failed to catch a problem with adding a story with "
                    + "priority " + this.priority + " to the database.");
        }
        
        //else success
        Assert.assertEquals(1, this.storyDB.getStoryCount());
        
        
        List<Story> stories = this.storyDB.getAllStoriesInPriorityOrder();
        Assert.assertEquals(1, stories.size());
        
        Story actualStory = stories.get(0);
        Assert.assertEquals(expected, actualStory);
    }

}
