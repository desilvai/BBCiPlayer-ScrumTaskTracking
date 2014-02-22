/**
 * Copyright (c) 2014, Ian J. De Silva
 * All rights reserved.
 * 
 * Use, distribution, and modification of this work for any purpose is strictly
 * prohibited without the express consent of the copyright holder except as 
 * permitted by law.
 */
package uk.co.bbc.iplayer.tracking.knapsack;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import uk.co.bbc.iplayer.tracking.Story;

/**
 * Solves the knapsack problem using a simplistic dynamic programming algorithm.  
 * This algorithm is described in more detail in:
 * - http://en.wikipedia.org/wiki/Knapsack_problem#0.2F1_Knapsack_Problem
 * - http://www.micsymposium.org/mics_2005/papers/paper102.pdf
 * - http://www.youtube.com/watch?v=kH7weFvjLPY
 */
public class KnapsackProblemSolver
{
//    /**
//     * Contains the knapsack problem solution path and optimal value.
//     */
//    private int[][] solutionTable;
//    
//    /**
//     * The set of stories to consider when solving the knapsack problem.
//     */
//    private List<Story> storiesToConsider;

    
    /**
     * 
     */
    public KnapsackProblemSolver()
    {
        // TODO Auto-generated constructor stub
    }
    
    
    /**
     * Sets up a table to use to find the optimal solution to the knapsack 
     * problem. The table entry at (stories.size() + 1, capacity + 1) contains
     * the highest value that can be fit into the knapsack
     * @param stories  the stories to consider scheduling.  Stories must have
     *          positive point values.
     * @param capacity  the capacity of the knapsack (the sprint capacity).
     * @return  a table that contains the answer from our dynamic programming 
     *          algorithm, or null if the inputs are invalid.
     */
    public static long[][] setUpTable(List<Story> stories, 
                                     int capacity)
    {
        //Problem constraints:
        //Let:
        //      weight : int            (i.e. Story.Points)
        //      value : int             (i.e. Story.Priority)
        //      capacity : int          (i.e. sprint size)
        //where
        //      weight > 0
        //      value > 0               
        //      capacity > 0
        
        //Notice that the {0, 1}-knapsack problem tries to find the MAXIMUM 
        //  value that can fit in a knapsack without going over the weight.  
        //  This poses a problem since Story.Priority is defined as a lower
        //  number gives us the higher priority.  To get around that, we 
        //  can subtract the priority from MAX_INT and use that as the value.
        //  This could cause overflows from addition, so we build the table
        //  with longs instead.
        
        //Can't do anything if there is no solution table or stories.
        if(capacity < 0 || stories == null || stories.size() == 0)
        {
            return null;
        }
        
        int numElements = stories.size() + 1;
        int capacityBound = capacity + 1;
        
        long[][] table = new long[numElements][capacityBound];
        
        //Initialize row 0 to 0
        for(int k = 0; k < capacityBound; k++)
        {
            table[0][k] = 0;
        }
        
        //i iterates over the rows.
        for(int i = 1; i < numElements; i++)
        {
            //The rows represent stories, but it thinks stories are in the 
            //  range [1, stories.size()].  This is off by one, so we adjust
            //  the story retrieval down 1 to be in the correct range.
            Story currentStory = stories.get(i-1);
            int weight = currentStory.Points;
            long value = Integer.MAX_VALUE - currentStory.Priority;
            
            //j iterates over the columns
            for(int j = 0; j < capacityBound; j++)
            {
                if(weight > j)
                {
                    table[i][j] = table[i-1][j];
                }
                else
                {
                    table[i][j] = Math.max(table[i - 1][j], 
                                           table[i - 1][j - weight] + value);
                }
            }
        }
        
        return table;
    }
    
    /**
     * Gets the optimal solution from the dynamic programming computation of the
     * {0,1}-Knapsack problem.
     * @param table  the solution table
     * @param stories  the set of stories that are considered in computing the 
     *          solution.  This must be the EXACT same list that was used to 
     *          create the table.
     * @return  a list containing the solution to the knapsack problem.  If the
     *          story list that we use to create the table is ordered by 
     *          lowest to highest priority (in returnable order), then we expect
     *          the solution to maintain this order.  
     */
    public static List<Story> getOptimalSolution(long[][] table, 
                                                 List<Story> stories)
    {
        //The optimal solution that will be returned.
        List<Story> optimalStorySet = new ArrayList<>();
        
        //Can't do anything if there is no solution table or stories.
        if(table == null || stories == null || stories.size() == 0)
        {
            return optimalStorySet;
        }
        
        int row = table.length - 1;
        int col = table[row].length - 1;
        
        long valueRemaining = table[row][col];
        
        //This backtracks through the solution table to give us the set of
        //  stories that compose the optimal solution (this highest valued 
        //  knapsack or in our case the highest valued sprint based on 
        //  priority).
        while(valueRemaining > 0 && row > 0)
        {
            //If we didn't inherit the value from the row below.
            if(table[row][col] != table[row - 1][col])
            {
                //Must subtract 1 because row 1 corresponds to stories[0].
                Story storyToAdd = stories.get(row - 1);
                optimalStorySet.add(storyToAdd);
                
                int weight = storyToAdd.Points;
                long value = Integer.MAX_VALUE - storyToAdd.Priority;
                
                //Now, move one row down and num columns equal to the weight left.
                row--;
                col -= weight;

                valueRemaining -= value;
            }
            else
            {
                //Otherwise, we inherited the value, so move left in trace back.
                row--;
            }
        }
        
        return optimalStorySet;
        
    }
    
    
    /**
     * FOR DEBUGGING:
     * 
     * Method to print out the table where (0,0) is in the bottom left and 
     * (stories.size() + 1, capacity + 1) is in the upper right.
     * @param table
     */
    public static void printTable(int[][] table)
    {
        for(int row = table.length - 1; row >=0 ; row--)
        {
            for(int col = table[row].length - 1; col >= 0; col--)
            {
            
                System.out.print(String.format("%4d  ", table[row][table[row].length - 1 -col]));
            }
            System.out.println();
        }
        
        System.out.println();
    }
    
    
    /**
     * Example main
     * @param args
     */
    public static void main(String args[])
    {
        //TODO -- change this into a test case!
        //This was valid before we "inverted" the priorities.
//        List<Story> stories = Arrays.asList(new Story("1", 4, 3),
//                                            new Story("2", 3, 2),
//                                            new Story("3", 2, 4),
//                                            new Story("4", 3, 4));
        
        List<Story> stories = Arrays.asList(new Story("1", 4, 3),
                                            new Story("2", 3, 4),
                                            new Story("3", 2, 2),
                                            new Story("4", 3, 2));
        
        int capacity = 6;
        
        long[][] table = setUpTable(stories, capacity);
        
        
        for(int row = table.length - 1; row >=0 ; row--)
        {
            for(int col = table[row].length - 1; col >= 0; col--)
            {
            
                System.out.print(String.format("%10d  ", table[row][table[row].length - 1 -col]));
            }
            System.out.println();
        }
        
        List<Story> optStories = getOptimalSolution(table, stories);
        
        //optStories should contain stories 3 and 4.
        
        System.out.println("\nOptimal soluion:");
        for(Story story : optStories)
        {
            System.out.println(story.Id + "  Points: " + story.Points 
                               + "  Priority: " + story.Priority);
        }
    }
}
