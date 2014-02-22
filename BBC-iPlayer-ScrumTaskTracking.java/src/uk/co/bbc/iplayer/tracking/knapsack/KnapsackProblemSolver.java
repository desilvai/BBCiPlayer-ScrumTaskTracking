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
     * problem. 
     * @param stories  the stories to consider scheduling.  Stories must have
     *          positive point values.
     * @param capacity  the capacity of the knapsack (the sprint capacity).
     * @return  a table that contains the answer from our dynamic programming 
     *          algorithm.
     */
    public static int[][] setUpTable(List<Story> stories, 
                                     int capacity)
    {
        //TODO -- Change this not to use priorities
        int numElements = stories.size() + 1;
        int capacityBound = capacity + 1;
        
        int[][] table = new int[numElements][capacityBound];
        
        //Initialize
        for(int k = 0; k < capacityBound; k++)
        {
            table[0][k] = 0;
        }
        
        for(int i = 1; i < numElements; i++)
        {
            Story currentStory = stories.get(i-1);
            int weight = currentStory.Points;
            int value = currentStory.Priority;
            
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
                
//                printTable(table);
            }
        }
        
        return table;
    }
    
    /**
     * 
     * @param table
     * @param stories
     * @param capacity
     * @return
     */
    public static List<Story> getOptimalSolution(int[][] table, 
                                                 List<Story> stories)
    {
        int row = table.length - 1;
        int col = table[row].length - 1;
        
        int valueRemaining = table[row][col];
        
        List<Story> optimalStorySet = new ArrayList<>();
        
        while(valueRemaining > 0 && row > 0)
        {
            //If we didn't inherit the value from the row below.
            if(table[row][col] != table[row - 1][col])
            {
                //Must subtract 1 because row 1 corresponds to stories[0].
                Story storyToAdd = stories.get(row - 1);
                optimalStorySet.add(storyToAdd);
                
                int weight = storyToAdd.Points;
                int value = storyToAdd.Priority;
                
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
    
    
    public static void main(String args[])
    {
        List<Story> stories = Arrays.asList(new Story("1", 4, 3),
                                            new Story("2", 3, 2),
                                            new Story("3", 2, 4),
                                            new Story("4", 3, 4),
                                            new Story("5", 0, 5));
        int capacity = 6;
        
        int[][] table = setUpTable(stories, capacity);
        
        
        for(int row = table.length - 1; row >=0 ; row--)
        {
            for(int col = table[row].length - 1; col >= 0; col--)
            {
            
                System.out.print(String.format("%4d  ", table[row][table[row].length - 1 -col]));
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
