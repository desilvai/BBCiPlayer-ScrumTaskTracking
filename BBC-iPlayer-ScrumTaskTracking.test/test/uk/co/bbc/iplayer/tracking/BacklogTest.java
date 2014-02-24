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

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import uk.co.bbc.iplayer.tracking.exceptions.TaskTrackerException;
import uk.co.bbc.iplayer.tracking.impl.Backlog;
import uk.co.bbc.iplayer.tracking.impl.StoryDB;
import uk.co.bbc.iplayer.tracking.messages.Messages;
import uk.co.bbc.iplayer.tracking.test.infrastructure.TestUsingDB;

/**
 * Tests the {@link Backlog} class based on the requirements.
 */
public class BacklogTest extends TestUsingDB
{
    //-------------------------------------------------------------------------
    //  DATA MEMBERS
    //-------------------------------------------------------------------------
    /**
     * The backlog under test.  We don't use an IBacklog here because we want to
     * inspect the object.
     */
    private Backlog backlog;
    
    /**
     * The database containing the stories
     */
    private StoryDB storyDB;

    
    
    //-------------------------------------------------------------------------
    //  TEST SET-UP / TEAR-DOWN
    //-------------------------------------------------------------------------
    /**
     * Creates the database (in-memory). 
     * @throws IllegalAccessException  if the DB driver could not be created.
     * @throws InstantiationException  if the DB driver could not be created.
     * @throws SQLException  if there is a problem connecting and creating the 
     *                  DB.
     * @throws TaskTrackerException 
     */
    @Before
    @Override
    public void setUp() throws InstantiationException,
                               IllegalAccessException,
                               SQLException, TaskTrackerException
    {
        super.setUp();
        this.backlog = new Backlog();
        this.storyDB = new StoryDB();
    }


    /**
     * Destroys the in-memory database.
     * @throws SQLException  if we could not destroy the database.
     */
    @After
    @Override
    public void tearDown() throws SQLException
    {
        this.backlog = null;
        this.storyDB = null;
        super.tearDown();
    }
    
    
    
    //-------------------------------------------------------------------------
    //  TEST CASES
    //-------------------------------------------------------------------------
    /**
     * Test method for {@link uk.co.bbc.iplayer.tracking.impl.Backlog#Add(uk.co.bbc.iplayer.tracking.Story)}.
     * Checks that we cannot add two stories with the same id.
     * @throws TaskTrackerException 
     */
    @Test
    public void testAdd_duplicateIds() throws TaskTrackerException
    {
        Story story = new Story();
        story.Points = 5;
        story.Priority = 5;
        story.Id = "TEST_ID";
        
        //Copy for later
        Story expectedStory = new Story(story);
        
        this.backlog.Add(story);
        
        //Make sure it was added successfully
        Assert.assertEquals(1, this.storyDB.getStoryCount());
        
        Story storyDup = new Story();
        storyDup.Points = 2;
        storyDup.Priority = 1;
        storyDup.Id = story.Id;
        
        //Add second story with the same Id as the first.
        try
        {
            this.backlog.Add(story);
        }
        catch(TaskTrackerException e)
        {
            //Make sure there is still only one story and it is the first one we
            //  added (no update occurred)
            List<Story> stories = this.storyDB.getAllStoriesInPriorityOrder();
            Assert.assertEquals(1, stories.size());
            
            Story actualStory = stories.get(0);
            Assert.assertEquals(expectedStory, actualStory);
            
            //Test completed successfully.
            return;
        }
        
        fail("Application should not have allowed us to add a second story "
                + "with the same id as one in the DB.");
    }
    
    

    /**
     * Test method for {@link uk.co.bbc.iplayer.tracking.impl.Backlog#Remove(java.lang.String)}.
     * 
     * Checks that we can remove a story that is in the database successfully
     * 
     * @throws TaskTrackerException 
     */
    @Test
    public void testRemove() throws TaskTrackerException
    {
        String id = "TEST_ID for remove";
        
        //Test story
        Story story = new Story();
        story.Points = 5;
        story.Priority = 5;
        story.Id = id;
        
        Story originalStory = new Story(story);
        
        //Add it so we can remove it
        this.backlog.Add(story);
        
        //Make sure it was added successfully
        Assert.assertEquals(1, this.storyDB.getStoryCount());
        
        Story removedStory = this.backlog.Remove(id);
        Assert.assertEquals(originalStory, removedStory);
        
        //Make sure it was removed successfully
        Assert.assertEquals(0, this.storyDB.getStoryCount());
    }
    
    

    /*
     * Test cases for getSprint().
     * - Can't fit all the high priority items in the sprint.  Should get the 
     *      "right" set of high priority items.
     * - Select lower priority if we can't fit a high priority item in the space
     *      available.
     * - Empty set if we have no stories that will fit in the sprint.
     * - Empty space in sprint if not enough points in backlog to fill the 
     *      sprint.
     */
    /**
     * Test method for {@link uk.co.bbc.iplayer.tracking.impl.Backlog#getSprint(int)}.
     * 
     * This attempts to plan something when the database is empty and gets back
     * an empty list.
     * @throws TaskTrackerException  if there was a problem setting up the story
     *                  database.
     */
    @Test
    public void testGetSprint_noStories() throws TaskTrackerException
    {
        int sprintSize = 1000;
        
        //Plan an iteration
        List<Story> sprintPlan = this.backlog.getSprint(sprintSize);
        
        Assert.assertNotNull(sprintPlan);
        Assert.assertEquals("The plan for a sprint of size "
                                    + sprintSize
                                    + " should have been empty since the "
                                    + "backlog is empty.",
                            0,
                            sprintPlan.size());
    }
    
    
    /**
     * Test method for {@link uk.co.bbc.iplayer.tracking.impl.Backlog#getSprint(int)}.
     * 
     * This initializes the database with stories that will not fit in a sprint 
     * and results in an empty sprint list.
     * @throws TaskTrackerException  if there was a problem setting up the story
     *                  database.
     */
    @Test
    public void testGetSprint_noStoriesFit() throws TaskTrackerException
    {
        //Initialize the test case by adding the given stories to the database.
        List<Story> stories = Arrays.asList(new Story("Story 1", 40, 2),
                                            new Story("Story 2", 80, 2));
        
        for(Story story : stories)
        {
            //We skip add here since we don't need the extra checks and we 
            //  aren't testing that.
            this.storyDB.addStory(story);
        }
        

        //Plan an iteration
        List<Story> sprintPlan = this.backlog.getSprint(20);
        
        Assert.assertNotNull(sprintPlan);
        Assert.assertEquals("The plan for a sprint of size 20 should have been empty.", 
                            0, 
                            sprintPlan.size());
    }
    
    
    /**
     * Test method for {@link uk.co.bbc.iplayer.tracking.impl.Backlog#getSprint(int)}.
     * 
     * This initializes the database with stories that will not fit in a sprint 
     * and results in an empty sprint list.
     * @throws TaskTrackerException  if there was a problem setting up the story
     *                  database.
     */
    @Test
    public void testGetSprint_goodSolution() throws TaskTrackerException
    {
        List<Story> expected = Arrays.asList(new Story("3", 2, 2),
                                             new Story("4", 3, 2));
        
        List<Story> stories = new ArrayList<>();
        stories.add(new Story("1", 4, 3));
        stories.add(new Story("2", 3, 4));
        stories.addAll(expected);
        
        int capacity = 6;
        
        this.getSprintRunner(capacity, stories, expected);
    }
    
    
    /**
     * Test method for {@link uk.co.bbc.iplayer.tracking.impl.Backlog#getSprint(int)}.
     * 
     * This initializes the database with stories that will not fit in a sprint 
     * and results in an empty sprint list.
     * @throws TaskTrackerException  if there was a problem setting up the story
     *                  database.
     */
    @Test
    public void testGetSprint_goodSolutionReordered() throws TaskTrackerException
    {
        List<Story> expected = Arrays.asList(new Story("3", 2, 2),
                                             new Story("4", 3, 2));
        
        List<Story> stories = new ArrayList<>();
        stories.add(expected.get(0));
        stories.add(new Story("1", 4, 3));
        stories.add(new Story("2", 3, 4));
        stories.addAll(expected.subList(1, expected.size()));
        
        int capacity = 6;
        
        this.getSprintRunner(capacity, stories, expected);
    }
    
    
    /**
     * Test method for {@link uk.co.bbc.iplayer.tracking.impl.Backlog#getSprint(int)}.
     * 
     * Tests that a negative capacity results in an empty sprint plan.
     * @throws TaskTrackerException  if there was a problem setting up the story
     *                  database.
     */
    @Test
    public void testGetSprint_negativeCapacity() throws TaskTrackerException
    {
        List<Story> stories = new ArrayList<>();
        stories.add(new Story("3", 2, 2));
        stories.add(new Story("1", 4, 3));
        stories.add(new Story("2", 3, 4));
        stories.add(new Story("4", 3, 2));
        
        int capacity = -1;
        
        for(Story story : stories)
        {
            //We skip add here since we don't need the extra checks and we 
            //  aren't testing that.
            this.storyDB.addStory(story);
        }
        
        try
        {
            //Plan an iteration
            this.backlog.getSprint(capacity);
            
            fail("Expected an exception when capacity is set to " + capacity 
                 + ".");
        }
        catch(TaskTrackerException e)
        {
            Assert.assertEquals(Messages.getString("StoryNonPositivePoints"),
                                e.getMessage());
        }
    }
    
    
    /**
     * Test method for {@link uk.co.bbc.iplayer.tracking.impl.Backlog#getSprint(int)}.
     * 
     * Tests that a negative capacity results in an empty sprint plan.
     * @throws TaskTrackerException  if there was a problem setting up the story
     *                  database.
     */
    @Test
    public void testGetSprint_zeroCapacity() throws TaskTrackerException
    {
        List<Story> stories = new ArrayList<>();
        stories.add(new Story("3", 2, 2));
        stories.add(new Story("1", 4, 3));
        stories.add(new Story("2", 3, 4));
        stories.add(new Story("4", 3, 2));
        
        int capacity = 0;
        
        for(Story story : stories)
        {
            //We skip add here since we don't need the extra checks and we 
            //  aren't testing that.
            this.storyDB.addStory(story);
        }
        
        try
        {
            //Plan an iteration
            this.backlog.getSprint(capacity);
            
            fail("Expected an exception when capacity is set to " + capacity 
                 + ".");
        }
        catch(TaskTrackerException e)
        {
            Assert.assertEquals(Messages.getString("StoryNonPositivePoints"),
                                e.getMessage());
        }
    }
     
    
    
    //-------------------------------------------------------------------------
    //  HELPER METHODS
    //-------------------------------------------------------------------------
    /**
     * Runs a valid test case comparing the results (the generated sprint plan)
     * with the expected.
     * @param capacity  the capacity of the sprint
     * @param stories  the potential stories for the plan
     * @param expected  the expected list of stories
     * @throws TaskTrackerException  if something went wrong when adding the 
     *                  stories to the DB or when getting the sprint.
     */
    private void getSprintRunner(int capacity, 
                                 List<Story> stories, 
                                 List<Story> expected) throws TaskTrackerException
    {
        if(stories != null)
        {
            for(Story story : stories)
            {
                //We skip add here since we don't need the extra checks and we 
                //  aren't testing that.
                this.storyDB.addStory(story);
            }
        }
        
        //Plan an iteration
        List<Story> sprintPlan = this.backlog.getSprint(capacity);
        
        //Check that the sprintPlan is never null and that we get what we 
        //      expected.
        Assert.assertNotNull(sprintPlan);
        Assert.assertEquals(expected, sprintPlan);
    }
}
