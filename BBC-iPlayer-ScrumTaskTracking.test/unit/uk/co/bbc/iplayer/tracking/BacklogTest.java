///**
// * Copyright (c) 2014, Ian J. De Silva
// * All rights reserved.
// * 
// * Use, distribution, and modification of this work for any purpose is strictly
// * prohibited without the express consent of the copyright holder except as 
// * permitted by law.
// */
//package uk.co.bbc.iplayer.tracking;
//
//import static org.junit.Assert.*;
//
//import org.junit.After;
//import org.junit.Before;
//import org.junit.Test;
//
///**
// * Tests the {@link Backlog} class based on the requirements.
// */
//public class BacklogTest
//{
//    private Backlog backlog;
//
//    /**
//     * @throws java.lang.Exception
//     */
//    @Before
//    public void setUp() throws Exception
//    {
//        //Create a new one each time to make sure that we are working with a 
//        //  clean backlog instance.
//        this.backlog = new Backlog();
//    }
//
//    /**
//     * @throws java.lang.Exception
//     */
//    @After
//    public void tearDown() throws Exception
//    {
//        //This is just safer given the memory leaks of old.
//        this.backlog = null;
//    }
//
//    
//    
//    //=========================================================================
//    //  TEST CASES
//    //=========================================================================
//    
//    /*
//     *-------------------------------------------------------------------------
//     * TEST ADD
//     *-------------------------------------------------------------------------
//     * For testing a story's points on add, there are the following equivalence
//     * classes:
//     *  - Less than 0  (negative-values)
//     *  - Greater than or Equal to 0  (non-negative-values)
//     *  
//     * We therefore for boundary-value testing, need the following test cases:
//     *  - Points == MIN_INT     Expect: fail
//     *  - Points == -100        Expect: fail 
//     *  - Points == -1          Expect: fail
//     *  - Points == 0           Expect: success
//     *  - Points == 1           Expect: success  -- Only one of these are needed
//     *  - Points == 50          Expect: success  --
//     *  - Points == MAX_INT     Expect: success
//     *-------------------------------------------------------------------------
//     */
//    /**
//     * Test method for {@link uk.co.bbc.iplayer.tracking.Backlog#Add(uk.co.bbc.iplayer.tracking.Story)}.
//     * This checks that the story is not added when the point value is equal to
//     * MIN_INT (low bound on the equivalence class). 
//     */
//    @Test
//    public void testAdd_minInt()
//    {
//        Story story = createStoryWithGivenPoints(Integer.MIN_VALUE);
//        this.backlog.Add(story);
//        
//        //TODO -- check that this failed inside the object.
//        fail("Not yet implemented");
//    }
//    
//    
//    /**
//     * Test method for {@link uk.co.bbc.iplayer.tracking.Backlog#Add(uk.co.bbc.iplayer.tracking.Story)}.
//     * This checks that the story is not added when the point value is a 
//     * non-boundary value in the less-than-0 equivalence class. 
//     */
//    @Test
//    public void testAdd_negativeNonBoundary()
//    {
//        Story story = createStoryWithGivenPoints(-100);
//        this.backlog.Add(story);
//        
//        //TODO -- check that this failed inside the object.
//        fail("Not yet implemented");
//    }
//    
//    
//    /**
//     * Test method for {@link uk.co.bbc.iplayer.tracking.Backlog#Add(uk.co.bbc.iplayer.tracking.Story)}.
//     * This checks that the story is not added when the point value is a 
//     * boundary value in the negative-values equivalence class. 
//     */
//    @Test
//    public void testAdd_negativeBoundary()
//    {
//        Story story = createStoryWithGivenPoints(-1);
//        this.backlog.Add(story);
//        
//        //TODO -- check that this failed inside the object.
//        fail("Not yet implemented");
//    }
//    
//    
//    /**
//     * Test method for {@link uk.co.bbc.iplayer.tracking.Backlog#Add(uk.co.bbc.iplayer.tracking.Story)}.
//     * This checks that the story is successfully added when the point value is 
//     * a boundary value in the non-negative-values equivalence class. 
//     */
//    @Test
//    public void testAdd_nonNegativeBoundary()
//    {
//        Story story = createStoryWithGivenPoints(0);
//        this.backlog.Add(story);
//        
//        //TODO -- check that this succeeded inside the object.
//        fail("Not yet implemented");
//    }
//    
//    
//    /**
//     * Test method for {@link uk.co.bbc.iplayer.tracking.Backlog#Add(uk.co.bbc.iplayer.tracking.Story)}.
//     * This checks that the story is successfully added when the point value is 
//     * a non-boundary value in the non-negative-values equivalence class. 
//     */
//    @Test
//    public void testAdd_nonNegativeNonBoundary()
//    {
//        Story story = createStoryWithGivenPoints(1);
//        this.backlog.Add(story);
//        
//        //TODO -- check that this succeeded inside the object.
//        fail("Not yet implemented");
//    }
//    
//    
//    /**
//     * Test method for {@link uk.co.bbc.iplayer.tracking.Backlog#Add(uk.co.bbc.iplayer.tracking.Story)}.
//     * This checks that the story is successfully added when the point value is 
//     * a non-boundary value in the non-negative-values equivalence class. 
//     */
//    @Test
//    public void testAdd_nonNegativeNonBoundary2()
//    {
//        Story story = createStoryWithGivenPoints(50);
//        this.backlog.Add(story);
//        
//        //TODO -- check that this succeeded inside the object.
//        fail("Not yet implemented");
//    }
//    
//    
//    
//    
//    
//    
//    //=========================================================================
//    //  HELPER METHODS
//    //=========================================================================
//    /**
//     * Creates a story with valid priority and the given number of points.
//     * @param points  the points to set for the story.
//     * @return  the newly created story.
//     */
//    private static final Story createStoryWithGivenPoints(int points)
//    {
//        Story story = new Story();
//        story.Points = points;
//        story.Priority = 1;
//        
//        return story;
//    }
//    
//
//    /**
//     * Test method for {@link uk.co.bbc.iplayer.tracking.Backlog#Remove(java.lang.String)}.
//     */
//    @Test
//    public void testRemove()
//    {
//        fail("Not yet implemented");
//    }
//
//    /**
//     * Test method for {@link uk.co.bbc.iplayer.tracking.Backlog#getSprint(int)}.
//     */
//    @Test
//    public void testGetSprint()
//    {
//        fail("Not yet implemented");
//    }
//
//}
