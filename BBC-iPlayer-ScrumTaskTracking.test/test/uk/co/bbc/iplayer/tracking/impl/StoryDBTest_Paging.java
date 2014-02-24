///**
// * Copyright (c) 2014, Ian J. De Silva
// * All rights reserved.
// * 
// * Use, distribution, and modification of this work for any purpose is strictly
// * prohibited without the express consent of the copyright holder except as 
// * permitted by law.
// */
//package uk.co.bbc.iplayer.tracking.impl;
//
//import java.sql.SQLException;
//import java.util.ArrayList;
//import java.util.Arrays;
//import java.util.Collection;
//import java.util.Collections;
//import java.util.Comparator;
//import java.util.List;
//
//import org.junit.Assert;
//import org.junit.Before;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.junit.runners.Parameterized;
//import org.junit.runners.Parameterized.Parameters;
//
//import uk.co.bbc.iplayer.tracking.Story;
//import uk.co.bbc.iplayer.tracking.exceptions.TaskTrackerException;
//import uk.co.bbc.iplayer.tracking.impl.StoryDB;
//import uk.co.bbc.iplayer.tracking.test.infrastructure.TestUsingDB;
//
///**
// * 
// */
//@RunWith(Parameterized.class)
//public class StoryDBTest_Paging extends StoryDBTest
//{
//    /**
//     * The data for testing the paging function of the DB accessor.
//     * @return  A collection of data to test with.
//     */
//    @Parameters
//    public static Collection<Object[]> testExecutionValues()
//    {
//        Object[][] data = new Object[][]
//                {
//                    {1, 1},
//                    {3, 2},
//                    {2, 8},
//                    {1, 9},
//                    {4, 9}
//                };
//        return Arrays.asList(data);
//    }
//    
//    //-------------------------------------------------------------------------
//    //  DATA MEMBERS
//    //-------------------------------------------------------------------------
//    /**
//     * The starting position in the table (counting from 1).
//     */
//    int startPosition;
//    
//    /**
//     * The ending position in the table.
//     */
//    int endPosition;
//    
//    
//    //-------------------------------------------------------------------------
//    //  CONSTRUCTOR
//    //-------------------------------------------------------------------------
//    /**
//     * Constructor for parameterized test cases.
//     * @param startPosition  the starting position in the table
//     * @param numberOfElements  the number of elements to retrieve from the 
//     *          table.
//     */
//    public StoryDBTest_Paging(int startPosition, int numberOfElements)
//    {
//        this.startPosition = startPosition;
//        
//        //we are counting from 1 inclusive, so subtract 1.
//        this.endPosition   = startPosition + numberOfElements - 1;
//    }
//    
//
//    
//    //-------------------------------------------------------------------------
//    //  TEST CASES
//    //-------------------------------------------------------------------------
//    /**
//     * Test method for {@link uk.co.bbc.iplayer.tracking.impl.StoryDB#pageThroughStories(int, int)}.
//     * 
//     * Tests that we get a subset of the results ordered first by priority (low 
//     * number to high number) then by age in the database (low to high).
//     * @throws TaskTrackerException 
//     */
//    @Test
//    public void testPageThroughStories() throws TaskTrackerException
//    {
//        List<Story> expectedOrderedStories = new ArrayList<>(this.storiesInDB);
//        Collections.sort(expectedOrderedStories, 
//                         new CompareStories());
//        
//        
//        //Subsets: size 1, size 2, size 8, size 9, size 10
//        List<Story> orderedStories = storyDB.pageThroughStories(this.startPosition, 
//                                                                this.endPosition);
//        
//        //Don't need to subtract from the end position since sublist is [x, y).
//        List<Story> expectedSubset = this.storiesInDB.subList(this.startPosition - 1, 
//                                                              Math.min(this.storiesInDB.size(), 
//                                                                       this.endPosition));
//        Assert.assertEquals(expectedSubset, orderedStories);
//    }
//
//}
