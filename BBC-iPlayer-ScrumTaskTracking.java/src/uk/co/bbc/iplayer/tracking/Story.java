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
}
