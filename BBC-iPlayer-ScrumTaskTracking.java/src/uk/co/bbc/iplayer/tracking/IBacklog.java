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
public interface IBacklog
{
    /**
     * 
     * @param s
     */
    public void Add(Story s);
    
    public Story Remove(String id);
    
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
