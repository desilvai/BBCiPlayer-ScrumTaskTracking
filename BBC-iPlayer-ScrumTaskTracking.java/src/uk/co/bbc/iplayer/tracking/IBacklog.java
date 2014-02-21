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

import uk.co.bbc.iplayer.tracking.exceptions.TaskTrackerException;

/**
 * An interface to the product backlog that can be called by a web service.
 */
public interface IBacklog
{
    /**
     * Adds a story to the product backlog.
     * @param s  the story to add to the backlog.  We assume that the 
     *      Story.Points value is non-negative (see github question 1) and that
     *      Story.Priority can be duplicated among the set of added stories
     *      (github question 2).  Story Ids must be unique.
     * @throws TaskTrackerException  if there was a problem adding the story to 
     *      the product backlog.
     */
    public void Add(Story s) throws TaskTrackerException;
    
    /**
     * Removes a story with the given id from the backlog.
     * @param id  the id of the story to remove.
     * @return  the Story that was removed.
     * @throws TaskTrackerExecption  if there was a problem removing the story
     *      from the database.
     */
    public Story Remove(String id) throws TaskTrackerException;
    
    /**
     * Gets the list of stories that will fit into a sprint given the number
     * of points available in the sprint.
     * @param totalPointsAchievable  the number of points that can be achieved 
     *      in the sprint.
     * @return  a list of Stories in the backlog, ordered by business priority, 
     *      given the totalPointsAchieveable in the sprint
     */
    public List<Story> getSprint(int totalPointsAchievable);
}
