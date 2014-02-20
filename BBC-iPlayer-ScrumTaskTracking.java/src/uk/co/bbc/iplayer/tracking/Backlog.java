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

/**
 * @author desilva
 *
 */
public class Backlog implements IBacklog
{

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
        
        // Add the story to the backlog
        
        
        // TODO Auto-generated method stub
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
