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
import java.util.Arrays;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import uk.co.bbc.iplayer.tracking.Story;

/**
 * Provides some basic tests of the knapsack problem solver.
 */
public class KnapsackProblemSolverTest
{

    /**
     * Test method for {@link uk.co.bbc.iplayer.tracking.impl.KnapsackProblemSolver#solve(java.util.List, int)}.
     */
    @Test
    public void testSolve()
    {
        List<Story> expected = Arrays.asList(new Story("3", 2, 2),
                                             new Story("4", 3, 2));
        
        List<Story> stories = new ArrayList<>();
        stories.add(new Story("1", 4, 3));
        stories.add(new Story("2", 3, 4));
        stories.addAll(expected);
        
        int capacity = 6;
        
        
        List<Story> optStories = KnapsackProblemSolver.solve(stories, capacity);
        
        //optStories should contain stories 3 and 4.
        Assert.assertEquals(expected, optStories);
        
//        System.out.println("\nOptimal soluion:");
//        for(Story story : optStories)
//        {
//            System.out.println(story.Id + "  Points: " + story.Points 
//                               + "  Priority: " + story.Priority);
//        }
    }
    
    
    /**
     * Test method for {@link uk.co.bbc.iplayer.tracking.impl.KnapsackProblemSolver#solve(java.util.List, int)}.
     * 
     * Run the same test again with a different order and confirm that the order
     * is preserved.  Need to check this because I need to guarantee a given
     * order of the solution.
     */
    @Test
    public void testSolve_checkOrder()
    {
        List<Story> expected = Arrays.asList(new Story("3", 2, 2),
                                             new Story("4", 3, 2));
        
        List<Story> stories = new ArrayList<>();
        stories.add(expected.get(0));
        stories.add(new Story("1", 4, 3));
        stories.add(new Story("2", 3, 4));
        stories.addAll(expected.subList(1, expected.size()));
        
        int capacity = 6;
        
        
        List<Story> optStories = KnapsackProblemSolver.solve(stories, capacity);
//        long[][] solutionTable = KnapsackProblemSolver.setUpTable(stories, 
//                                                                          capacity);
//        printTable(solutionTable);
//        List<Story> optStories = KnapsackProblemSolver.getOptimalSolution(solutionTable, 
//                                                                          stories);
//        
//        //Reverse the order of the list so the highest priority stories come first.
//        Collections.reverse(optStories);
        
        System.out.println("\nOptimal soluion:");
        for(Story story : optStories)
        {
            System.out.println(story.Id + "  Points: " + story.Points 
                               + "  Priority: " + story.Priority);
        }
        
        //optStories should contain stories 3 and 4.
        Assert.assertEquals(expected, optStories);
    }
    
    
    /**
     * FOR DEBUGGING:
     * 
     * Method to print out the table where (0,0) is in the bottom left and 
     * (stories.size() + 1, capacity + 1) is in the upper right.
     * @param table
     */
    public static void printTable(long[][] table)
    {
        for(int row = table.length - 1; row >=0 ; row--)
        {
            for(int col = table[row].length - 1; col >= 0; col--)
            {
            
                System.out.print(String.format("%10d  ", table[row][table[row].length - 1 -col]));
            }
            System.out.println();
        }
        
        System.out.println();
    }

}
