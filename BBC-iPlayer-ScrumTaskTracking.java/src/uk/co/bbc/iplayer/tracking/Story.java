/**
 * Copyright (c) 2014, Ian J. De Silva
 * All rights reserved.
 * 
 * Use, distribution, and modification of this work for any purpose is strictly
 * prohibited without the express consent of the copyright holder except as 
 * permitted by law.
 */
package uk.co.bbc.iplayer.tracking;

/**
 * Represents a story to add or store in the backlog (an instance of 
 * {@link IBacklog}). 
 * 
 * I assume I have to use these directly and should not go through a 
 * getter/setter as is typically done.
 */
public class Story
{
    /**
     * Unique identifier for each story.
     */
    public String Id;
    
    /**
     * An estimate of the development effort required to complete the story
     */
    public int Points;
    
    /**
     * The business priority of the story.  The lower the numeric value, the 
     * higher the priority (e.g. a story with priority 1 is more important than 
     * a story with priority 3). 
     */
    public int Priority;
    
    
    
    /**
     * Default Constructor
     */
    public Story()
    {
    }
    
    
    /**
     * Initializing Constructor
     * @param id  the unique id of the story
     * @param points  the estimate of the story's difficulty
     * @param priority  the priority of the story
     */
    public Story(String id, int points, int priority)
    {
        this.Id = id;
        this.Points = points;
        this.Priority = priority;
    }
    
    
    /**
     * Copy constructor
     * @param that  the object to copy.
     */
    public Story(Story that)
    {
        this.Id = that.Id;
        this.Points = that.Points;
        this.Priority = that.Priority;
    }
    
    
    /**
     * {@inheritDoc}
     */
    @Override
    public boolean equals(Object other)
    {
        //If other is null, it cannot be equal to this.
        if(other == null)
        {
            return false;
        }
        
        //Are the objects the same type?
        Story that;
        try
        {
            that = (Story) other;
        }
        catch(ClassCastException e)
        {
            return false;
        }
        
        
        //Check the Ids.  Two Ids are equal if they are both null.
        if(this.Id == null)
        {
            if(that.Id != null)
            {
                return false;
            }
            //else both are null
        }
        else
        {
            if(! this.Id.equals(that.Id))
            {
                return false;
            }
            // else the ids are equal.
        }
        
        
        //Check the integer values.
        return this.Points == that.Points && this.Priority == that.Priority;
        
    }
}
