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

import uk.co.bbc.iplayer.tracking.db.StoryDB;
import uk.co.bbc.iplayer.tracking.exceptions.TaskTrackerException;
import uk.co.bbc.iplayer.tracking.messages.Messages;
import uk.co.bbc.iplayer.tracking.test.infrastructure.TestUsingDB;

/**
 * This tests the add function of the IBacklog implementation focusing on the 
 * validation of the ID field.
 * 
 * For testing a story's id on add, we need to test the following:
 *  - null
 *  - Empty string              ("")
 *  - Single character          (" ")
 *  - Embedded Non-printable characters   ("a\7\0\18\28z")
 *  - SQL injection             ("',5,5); DROP TABLE Stories --")
 *  - Questionable characters   ("ID 403.5'4")
 *  - 31 characters             ("abcdefghijklmnopqrstuvwxyz01234")
 *  - 32 characters             ("abcdefghijklmnopqrstuvwxyz012345")
 *  - 33 characters             ("abcdefghijklmnopqrstuvwxyz0123456")
 *  - lots of characters        ("abcdefghijklmnopqrstuvwxyz0123456789ABCDEFGHIJKLMNOP")
 *  - Unicode characters        ("\u212604\u212A00\u216E0E\u090B")
 *  
 *  SQL injection should cause subsequent checks on the DB to fail.
 */
@RunWith(value = Parameterized.class)
public class BacklogTest_AddId extends TestUsingDB
{
    /**
     * The data for boundary value testing of the points part of the story to be
     * added.
     * @return  A collection of boundary values and the expected test outcome.
     */
    @Parameters
    public static Collection<Object[]> testExecutionValues()
    {
        StringBuilder buffer = new StringBuilder();
        for(int k = 0; k < 2*StoryDB.ID_FIELD_SIZE + 1; k++)
        {
            //The modulo keeps us in the ASCII range.
            buffer.append('A' + (char) (k % 60));
        }
        Object[][] data = new Object[][]
                {
                    {null,              false},
                    {"",                true},
                    {" ",               true},
                    {"a\7\0\18\28z",    true},      //Non-printable characters
                    
                    //This test case is too long for the field.
                    {"',5,5);DROP TABLE " + StoryDB.STORY_TABLE + " --", true},
                    {"ID 403.5'4",      true},
                    {buffer.substring(0, StoryDB.ID_FIELD_SIZE - 1),  true},
                    {buffer.substring(0, StoryDB.ID_FIELD_SIZE),      true},
                    {buffer.substring(0, StoryDB.ID_FIELD_SIZE + 1),  false},
                    {buffer.toString(),                               false},
                    {"\u212604\u212A00\u216E0E\u090B",                true}
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
     * The story's Id
     */
    private String id;
    
    /**
     * The expected outcome of the test.  This is true if the test should pass,
     * or false otherwise.
     */
    private boolean expectedResult;
    
    /**
     * The database containing the stories
     */
    private StoryDB storyDB;

    
    
    //-------------------------------------------------------------------------
    //  CONSTRUCTOR
    //-------------------------------------------------------------------------
    /**
     * Constructor for parameterized tests.
     * @param id  the Id of the story.
     * @param expectedResults  the expected outcome of the test (true if the 
     *      test should succeed, false otherwise).
     */
    public BacklogTest_AddId(String id, boolean expectedResult)
    {
        this.id = id;
        this.expectedResult = expectedResult;
        
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
     * {@link uk.co.bbc.iplayer.tracking.Backlog#Add(uk.co.bbc.iplayer.tracking.Story)}.
     * This attempts to add a story that we created with the given id 
     * (all other parts of the story are valid) to the {@link IBacklog} object.
     * We then check for success or failure based on what we were told to expect
     * (if {@link #expectedResult} is true, we expect success, otherwise we 
     * expect failure).
     * @throws TaskTrackerException 
     */
    @Test
    public void testAdd() throws TaskTrackerException
    {
        //The test input.  Only the points varies between executions.
        Story story = new Story();
        story.Id = this.id;
        story.Priority = 5;
        story.Points = 3;
        
        //We need this for comparing later.
        Story expected = new Story(story);
        
        try
        {
            this.backlog.Add(story);
        }
        catch(TaskTrackerException e)
        {
            if(this.expectedResult)
            {
                //We expected success, but didn't actually succeed
                throw e;
            }
            
            //else
            //Check that we got the right error message
            String expectedMessage;
            if(this.id == null)
            {
                expectedMessage = Messages.getString("StoryNullId");
            }
            else
            {
                expectedMessage = Messages.getString("StoryIdTooLong", 
                                                     StoryDB.ID_FIELD_SIZE, 
                                                     this.id.length()); 
            }
            Assert.assertEquals(expectedMessage, e.getMessage());
            
            //Check that the DB is still empty.
            Assert.assertEquals(0, this.storyDB.getStoryCount());
            return;
        }
        
        //Check for success or failure.
        if(!this.expectedResult)
        {
            //Failure occurred.
            Assert.fail("Failed to catch a problem with adding a story with an"
                    + "invalid id value to the database.");
        }
        
        //else success
        Assert.assertEquals(1, this.storyDB.getStoryCount());
        
        
        List<Story> stories = this.storyDB.getAllStories();
        Assert.assertEquals(1, stories.size());
        
        Story actualStory = stories.get(0);
        Assert.assertEquals(expected, actualStory);
    }

}
