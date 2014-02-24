/**
 * Copyright (c) 2014, Ian J. De Silva
 * All rights reserved.
 * 
 * Use, distribution, and modification of this work for any purpose is strictly
 * prohibited without the express consent of the copyright holder except as 
 * permitted by law.
 */
package uk.co.bbc.iplayer.tracking.impl;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import uk.co.bbc.iplayer.tracking.IBacklog;
import uk.co.bbc.iplayer.tracking.Story;
import uk.co.bbc.iplayer.tracking.exceptions.TaskTrackerException;
import uk.co.bbc.iplayer.tracking.messages.Messages;

/**
 * @author desilva
 *
 */
public class Backlog implements IBacklog
{
    //-------------------------------------------------------------------------
    //  CONSTANTS
    //-------------------------------------------------------------------------
    /**
     * The maximum length of a story's ID.
     */
    public static final int MAX_ID_LENGTH = 32;
    
    /**
     * The lower boundary (exclusive) of any stoy's point value.
     */
    private static final int POINT_LOWER_BOUND = 0;
    
    /**
     * The lower boundary (exclusive) of any stoy's priority.
     */
    private static final int PRIORITY_LOWER_BOUND = 0;
    
    /**
     * If the number of (stories * capacity) exceeds this threshold, we want to
     * do some approximating so we don't run out of memory.
     */
    public static final int PACKING_APPROXIMATION_THRESHOLD = 1000000;
    
    
    //-------------------------------------------------------------------------
    //  DATA MEMBERS
    //-------------------------------------------------------------------------
    /**
     * The story database wrapper.  This abstracts out the SQL and allows for 
     * mocking.
     */
    protected StoryDB storyDB = new StoryDB();
    
    
    //-------------------------------------------------------------------------
    //  INTERFACE IMPLEMENTATION
    //-------------------------------------------------------------------------
    /**
     * {@inheritDoc}
     */
    @Override
    public void Add(Story story) throws TaskTrackerException
    {
        //TODO -- log exceptions.
        //Check that the fields of the story are valid.  If they aren't, an 
        //  exception is thrown.
        checkId(story.Id);
        checkPointValue(story.Points);
        checkPriorityValue(story.Priority);
        
        
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
    public List<Story> getSprint(int totalPointsAchievable) throws TaskTrackerException
    {
        //TODO -- log errors 
        
        //If the totalPointsAchievable is impossible (non-positive), throw an
        //  exception.  This indicates something is wrong rather than the 
        //  value is correct but we didn't find anything.
        checkPointValue(totalPointsAchievable);
            
        //Ordered stories is never null.
        List<Story> orderedStories = this.storyDB.getAllStoriesInPriorityOrder();
        
        /**
         * OK, so the idea here is that we can formulate the problem as the 
         * {0,1}-knapsack problem and get the highest possible value in the 
         * sprint (in Scrum, we use priority as a proxy for how much value the 
         * customer places on the story).  However, we needed to finesse the 
         * problem a little to make it work, so we bounded the priority and 
         * point-values so they are strictly positive.
         * 
         * Further, because this problem is NP-complete it is possible that we 
         * will take up too much time and space (it takes 
         * O(|orderedStories| * totalPointsAchievable) time and space, so we 
         * will do some approximation if our problem is too big.  Our very
         * naive approximation takes the highest priority items and just adds 
         * them to the proposed solution until we have made the 
         * potential-solution space small enough to run the knapsack algorithm.
         */
        
        int pointsRemainingInSprint = totalPointsAchievable;
        List<Story> sprintPlan = new ArrayList<>();
        
        
        
        //Approximate until we get to a small enough value that we can use 
        //  the optimal solution.
        //Delete is expensive, so we are going to drop the element from the list
        //      by passing a sublist into the solver.  The story was either 
        //      added or it is too big.  Don't bother the Knapsack problem
        //      solver with it.
        int position = 0;
        for(position = 0; position < orderedStories.size(); position++)
        {
            //If we are small enough that we can do the rest using the 
            //      optimal solution finder, then do that.
            long tableSize = (long)(orderedStories.size() - position) * (long)pointsRemainingInSprint;
            if(tableSize <= PACKING_APPROXIMATION_THRESHOLD)
            {
                break;
            }
            
            Story story = orderedStories.get(position);
            
            if(story.Points <= (pointsRemainingInSprint))
            {
                sprintPlan.add(story);
            }
        }
        
        //Finds the set of stories that fills up the sprint and maximizes the
        //  value to the customer (as defined by the priority of the story).
        List<Story> optimalSolution = KnapsackProblemSolver.solve(orderedStories.subList(position, orderedStories.size()), 
                                                                  totalPointsAchievable);
        
        //Insert the stories from the approximation into the optimal solution 
        //      set so they are ordered by priority (then age).  Since we took 
        //      the stories that will fit in order from the list of stories,
        //      we know that the contents of orderedStories should come first. 
        sprintPlan.addAll(optimalSolution);
        return sprintPlan;
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
    
    
    /**
     * Check that a story's point value is valid with respect to the constraints
     * we imposed on the point values (point values are strictly positive).
     * @param points  the point value to check
     * @throws TaskTrackerException  if the point value is invalid (it is 
     *                  non-positive).
     */
    private static final void checkPointValue(int points) 
            throws TaskTrackerException
    {
        if(points <= POINT_LOWER_BOUND)
        {
            throw new TaskTrackerException(Messages.getString("StoryNonPositivePoints"));
        }
        
        if(points == Integer.MAX_VALUE)
        {
            throw new TaskTrackerException(Messages.getString("StoryPointsTooBig", 
                                                              Integer.MAX_VALUE));
        }
    }
    
    
    /**
     * Check that a story's priority is valid with respect to the constraints
     * we imposed on the priorities (priorities are strictly positive).
     * @param priority  the priority to check
     * @throws TaskTrackerException  if the priority is invalid (it is 
     *                  non-positive).
     */
    private static final void checkPriorityValue(int priority) 
            throws TaskTrackerException
    {
        if(priority <= PRIORITY_LOWER_BOUND)
        {
            throw new TaskTrackerException(Messages.getString("StoryNonPositivePriority"));
        }
    }

}
