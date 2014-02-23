/**
 * Copyright (c) 2014, Ian J. De Silva
 * All rights reserved.
 * 
 * Use, distribution, and modification of this work for any purpose is strictly
 * prohibited without the express consent of the copyright holder except as 
 * permitted by law.
 */
package uk.co.bbc.iplayer.tracking.impl;

import java.util.Arrays;
import java.util.List;

import org.junit.Assert;
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
        StoryDB storyDB = new StoryDB();
        
        //Initialize the test case by adding the given stories to the database.
        //We are using the key as our oracle here.  Don't mess it up!
        List<Story> stories = Arrays.asList(new Story("3",  40,  2),
                                            new Story("4",  20,  2),
                                            new Story("5", 101,  2),
                                            new Story("1",   2,  1),
                                            new Story("7",  33,  3),
                                            new Story("2",   2,  1),
                                            new Story("8",  75, 81),
                                            new Story("6",  70,  2));
        
        for(Story story : stories)
        {
            //We skip add here since we don't need the extra checks and we 
            //  aren't testing that.
            storyDB.addStory(story);
        }
        
        List<Story> orderedStories = storyDB.getAllStoriesInPriorityOrder();
        
        int lastStoryId = 0;
        int lastPriority = 0;
        
        //Checks to make sure the stories are in order based on the key (we
        //  chose our keys so that they would be in order after the sort).
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
    }

}
