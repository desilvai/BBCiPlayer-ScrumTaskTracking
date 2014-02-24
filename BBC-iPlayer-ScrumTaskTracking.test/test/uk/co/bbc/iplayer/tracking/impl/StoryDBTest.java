/**
 * Copyright (c) 2014, Ian J. De Silva
 * All rights reserved.
 * 
 * Use, distribution, and modification of this work for any purpose is strictly
 * prohibited without the express consent of the copyright holder except as 
 * permitted by law.
 */
package uk.co.bbc.iplayer.tracking.impl;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import uk.co.bbc.iplayer.tracking.Story;
import uk.co.bbc.iplayer.tracking.exceptions.TaskTrackerException;
import uk.co.bbc.iplayer.tracking.impl.StoryDB;
import uk.co.bbc.iplayer.tracking.test.infrastructure.TestUsingDB;

/**
 * 
 */
public class StoryDBTest extends TestUsingDB
{
    //-------------------------------------------------------------------------
    //  INNER CLASSES
    //-------------------------------------------------------------------------
    /**
     * A comparator class to use to sort the elements.
     */
    protected class CompareStories implements Comparator<Story>
    {
        /**
         * Returns the comparison of the ids (counting null as less than
         * any non-null string).
         * @param thisStory  a story to compare
         * @param thatStory  the other story to compare
         * @return  the comparison of the id values of the stories.
         */
        @Override
        public int compare(Story thisStory,
                           Story thatStory)
        {
            if(thisStory.Id == null)
            {
                if(thatStory.Id == null)
                {
                    return 0;
                }
                else
                {
                    return -1;
                }
            }
            else
            {
                if(thatStory.Id == null)
                {
                    return 1;
                }
            }
            return thisStory.Id.compareTo(thatStory.Id);
        }
    }
    
    
    //-------------------------------------------------------------------------
    //  DATA MEMBERS
    //-------------------------------------------------------------------------
    /**
     * The object under test.
     */
    protected StoryDB storyDB;
    
    /**
     * The list of stories in the DB.
     */
    protected List<Story> storiesInDB;
    
    
    //-------------------------------------------------------------------------
    //  SET-UP / TEAR-DOWN
    //-------------------------------------------------------------------------
    /**
     * {@inheritDoc}
     * @throws TaskTrackerException 
     */
    @Before
    @Override
    public void setUp() throws InstantiationException,
                               IllegalAccessException,
                               SQLException, TaskTrackerException
    {
        super.setUp();
        
        this.storyDB = new StoryDB();
        
        //Initialize the test case by adding the given stories to the database.
        //I use the key as our oracle here.  Don't mess it up!
        this.storiesInDB = Arrays.asList(new Story("3",  40,   2),
                                         new Story("4",  20,   2),
                                         new Story("5", 101,   2),
                                         new Story("1",   2,   1),
                                         new Story("7",  33,   3),
                                         new Story("2",   2,   1),
                                         new Story("8",  75,  81),
                                         new Story("6",  70,   2),
                                         new Story("9", 999, 999));
        
        
        for(Story story : storiesInDB)
        {
            //I skip add here since I don't need the extra checks and we 
            //  aren't testing that.
            storyDB.addStory(story);
        }
    }
    

    /**
     * Test method for {@link uk.co.bbc.iplayer.tracking.impl.StoryDB#getAllStoriesInPriorityOrder()}.
     * 
     * Tests that we get the results ordered first by priority (low number to 
     * high number) then by age in the database (low to high).
     * @throws TaskTrackerException 
     */
    @Test
    public void testGetAllStoriesInPriorityOrder() throws TaskTrackerException
    {
        List<Story> expectedOrderedStories = new ArrayList<>(this.storiesInDB);
        Collections.sort(expectedOrderedStories, 
                         new CompareStories());
        
        List<Story> orderedStories = storyDB.getAllStoriesInPriorityOrder();
        
        int lastStoryId = 0;
        int lastPriority = 0;
        
        //Checks to make sure the stories are in order based on the key (I
        //  chose the keys so that they would be in order after the sort).
        for(Story story : orderedStories)
        {
            System.out.println(story.Id);
            
            int currentStoryId = Integer.parseInt(story.Id);
            
            Assert.assertTrue("Story id " + story.Id + " was not greater than " 
                                  + lastStoryId, 
                              lastStoryId < currentStoryId);
            Assert.assertTrue(lastPriority <= story.Priority);
            
            lastStoryId = currentStoryId;
            lastPriority = story.Priority;
        }
        
        Assert.assertEquals(expectedOrderedStories, orderedStories);
    }
}
