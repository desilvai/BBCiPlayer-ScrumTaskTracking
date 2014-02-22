/**
 * Copyright (c) 2014, Ian J. De Silva
 * All rights reserved.
 * 
 * Use, distribution, and modification of this work for any purpose is strictly
 * prohibited without the express consent of the copyright holder except as 
 * permitted by law.
 */
package uk.co.bbc.iplayer.tracking;

import java.util.ArrayList;
import java.util.List;

import uk.co.bbc.iplayer.tracking.db.StoryDB;
import uk.co.bbc.iplayer.tracking.exceptions.TaskTrackerException;
import uk.co.bbc.iplayer.tracking.messages.Messages;

/**
 * @author desilva
 *
 */
public class Backlog implements IBacklog
{
    public static final int MAX_ID_LENGTH = 32;
    
    /**
     * The story database wrapper.  This abstracts out the SQL and allows for 
     * mocking.
     */
    protected StoryDB storyDB = new StoryDB();

    /**
     * {@inheritDoc}
     */
    @Override
    public void Add(Story story) throws TaskTrackerException
    {
        // Check that the story is valid.  
        
        //Check story points are non-negative.  If they are negative, don't do
        //  the add.
        if(story.Points <= 0)
        {
            throw new TaskTrackerException(Messages.getString("StoryNegativePoints"));
        }
        
        
        //TODO -- log exceptions.
        //Check that the Story's Id is valid.  If it isn't, an exception is 
        //  thrown.
        checkId(story.Id);
        
        //NOTE:  We aren't checking if there is a negative priority.  This is 
        //      still valid as far as we are concerned.
        
        
        // Add the story to the backlog
        try
        {
            this.storyDB.addStory(story);
        }
        catch(TaskTrackerException e)
        {
            //TODO -- Add logging code.
            
            //TODO -- would this be better done in the story DB code?
            throw new TaskTrackerException(Messages.getString("DBErrorAdd", 
                                                              story.Id));
        }
    }

    
    /**
     * {@inheritDoc}
     */
    @Override
    public Story Remove(String id) throws TaskTrackerException
    {
        try
        {
            //Check that the id is valid.
            checkId(id);
            
            //Get the story.  We will not return this until the delete succeeds.
            Story story = this.storyDB.selectStory(id);
            this.storyDB.deleteStory(id);
            
            return story;
        }
        catch(TaskTrackerException e)
        {
            //TODO -- Log the exception.
            throw e;
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Story> getSprint(int totalPointsAchievable)
    {
        List<Story> stories = new ArrayList<>();
        
        //If the totalPointsAchievable is impossible (negative), return an 
        //      empty list.
        if(totalPointsAchievable < 0)
        {
            return stories;
        }
            
        
        /**
         * OK, so the idea here is that we can formulate the problem as the 
         * {0,1}-knapsack problem and get the highest possible value in the 
         * sprint (in Scrum, we use priority as a proxy for how much value the 
         * customer places on the story).  However, we need to finesse the 
         * problem a little to make it work.  In the knapsack problem, each 
         * item's value is positive, but our priorities are not.  To get around
         * this, we can either set the 
         */
        
        // TODO Auto-generated method stub
        return null;
    }
    
    
    
    //-------------------------------------------------------------------------
    //  HELPER METHODS
    //-------------------------------------------------------------------------
    /**
     * Check that a story Id is valid with respect to the constraints we imposed
     * on Story Ids for database storage.  These seem generally applicable (to 
     * have a non-null id that is sufficiently short enough) regardless of 
     * using database storage.  More information about how the Id is used is 
     * required to fully address the constraints on the ids. 
     * @param id  the story id to check
     * @throws TaskTrackerException  if the id is invalid 
     */
    private static final void checkId(String id) throws TaskTrackerException
    {
        //Check that the ID is non-null and small enough to fit in the DB.
        if(id == null)
        {
            throw new TaskTrackerException(Messages.getString("StoryNullId"));
        }
        
        if(id.length() > MAX_ID_LENGTH)
        {
            throw new TaskTrackerException(Messages.getString("StoryIdTooLong", 
                                                              MAX_ID_LENGTH, 
                                                              id.length()));
        }
    }

}
