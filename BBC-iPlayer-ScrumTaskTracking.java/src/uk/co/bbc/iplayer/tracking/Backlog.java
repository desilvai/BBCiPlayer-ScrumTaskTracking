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

/**
 * @author desilva
 *
 */
public class Backlog implements IBacklog
{
    /**
     * The story database wrapper.  This abstracts out the SQL and allows for 
     * mocking.
     */
    protected StoryDB storyDB = new StoryDB();

    /**
     * {@inheritDoc}
     */
    @Override
    public void Add(Story story)
    {
        // Check that the story is valid.  If it isn't don't add it (gihub 
        //      question 4)
        
        //Check story points are non-negative.  If they are negative, don't do
        //  the add.
        if(story.Points < 0)
        {
            return;
        }
        
        //TODO -- finish checking the inputs.
        
        // Add the story to the backlog
        try
        {
            this.storyDB.addValue(story);
        }
        catch(TaskTrackerException e)
        {
            //TODO -- Add logging code.
            //Because we have no way of returning the error, eat it.  I know
            //  this is a bad practice.
            
            e.printStackTrace();
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
