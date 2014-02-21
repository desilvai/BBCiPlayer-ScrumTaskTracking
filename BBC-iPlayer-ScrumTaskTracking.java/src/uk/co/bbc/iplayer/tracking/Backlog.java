/**
 * Copyright (c) 2014, Ian J. De Silva
 * All rights reserved.
 * 
 * Use, distribution, and modification of this work for any purpose is strictly
 * prohibited without the express consent of the copyright holder except as 
 * permitted by law.
 */
package uk.co.bbc.iplayer.tracking;

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
        if(story.Points < 0)
        {
            throw new TaskTrackerException(Messages.getString("StoryNegativePoints"));
        }
        
        
        //Check that the ID is non-null and small enough to fit in the DB.
        if(story.Id == null)
        {
            throw new TaskTrackerException(Messages.getString("StoryNullId"));
        }
        
        if(story.Id.length() > MAX_ID_LENGTH)
        {
            throw new TaskTrackerException(Messages.getString("StoryIdTooLong", 
                                                              MAX_ID_LENGTH, 
                                                              story.Id.length()));
        }
        
        //NOTE:  We aren't checking if there is a negative priority.  This is 
        //      still valid as far as we are concerned.
        
        
        // Add the story to the backlog
        try
        {
            this.storyDB.addValue(story);
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
    public Story Remove(String id)
    {
        // TODO Auto-generated method stub
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Story> getSprint(int totalPointsAchievable)
    {
        // TODO Auto-generated method stub
        return null;
    }

}
