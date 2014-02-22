/**
 * Copyright (c) 2014, Ian J. De Silva
 * All rights reserved.
 * 
 * Use, distribution, and modification of this work for any purpose is strictly
 * prohibited without the express consent of the copyright holder except as 
 * permitted by law.
 */
package uk.co.bbc.iplayer.tracking.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import uk.co.bbc.iplayer.tracking.Backlog;
import uk.co.bbc.iplayer.tracking.Story;
import uk.co.bbc.iplayer.tracking.exceptions.TaskTrackerException;
import uk.co.bbc.iplayer.tracking.messages.Messages;

/**
 * Encapsulates the database functions.
 * 
 * NOTE:  If we were running in a web server environment, this would use a 
 *      connection pool provided by the web server rather than raw connections.
 *      This would allow the program to scale.  Because we are not currently 
 *      running within a web server environment, I am opening and closing 
 *      connections here which will inhibit scalability.
 */
public class StoryDB
{
    //=========================================================================
    //  CONSTANTS
    //=========================================================================
    //-------------------------------------------------------------------------
    //  DB CONNECTION AND TABLE CONSTANTS
    //-------------------------------------------------------------------------
    /**
     * The DB protocol to use.  Here, we specify an in-memory DB.
     */
    public static final String DB_PROTOCOL = "jdbc:derby" + ":memory";
    
    
    /**
     * The name of our database.
     */
    public static final String DB_NAME = "taskTrackingDB";
    
    
    /**
     * The name of the table containing the stories.
     */
    public static final String STORY_TABLE = "Stories";
    
    
    /**
     * The fields in the story table (in the same order as they are in the DB 
     * table creation command).
     */
    public enum STORY_FIELDS
    {
        /**
         * The ID of the story.  Notice the field number is invalid.  This is
         * because ID is a primary key.
         */
        ID(1),
        
        /**
         * The point value of the story.
         */
        POINTS(2),
        
        /**
         * The priority of the story.
         */
        PRIORITY(3);
        
        /**
         * The number of the field in the database table.
         */
        private int fieldNumber;
        
        /**
         * Creates an enum with the given field numbers.
         */
        private STORY_FIELDS(int fieldNumber)
        {
            this.fieldNumber = fieldNumber;
        }
        
        /**
         * Gets the field number of the field in the stories database table.
         * @return
         */
        public int getFieldNumber()
        {
            return this.fieldNumber;
        }
    }
    
    public static final int ID_FIELD_SIZE = Backlog.MAX_ID_LENGTH;
    
    
//    //-------------------------------------------------------------------------
//    //  SQL ERROR CODES
//    //-------------------------------------------------------------------------
//    private static final String SQL_ERROR_NULL_VALUE = "23502";
//    private static final String SQL_ERROR_DUPLICATE_KEY = "23505";
//    private static final String SQL_ERROR_CONSTRAINT_VIOLATION = "23513";
//    
    
    //-------------------------------------------------------------------------
    //  SQL CONSTANTS
    //-------------------------------------------------------------------------
    /**
     * SQL to add a story into the DB.
     */
    private static final String ADD_STORY = "INSERT INTO " + STORY_TABLE 
            + "(" + StringUtils.join(STORY_FIELDS.values(), ",") + ")"
            + " VALUES (?,?,?)";
    
    
    /**
     * SQL to delete a story by ID
     */
    private static final String DELETE_STORY = "DELETE FROM " + STORY_TABLE 
            + " WHERE " + STORY_FIELDS.ID.toString() + "= ?"; 
    
    
    /**
     * SQL to get a story by ID
     */
    private static final String GET_STORY = "SELECT "
            + StringUtils.join(STORY_FIELDS.values(), ",") 
            + " FROM " + STORY_TABLE 
            + " WHERE " + STORY_FIELDS.ID.toString() + "= ?"; 
    
    
    //SELECT * FROM Stories WHERE 
    
    
    /**
     * SQL to select all stories from the DB.  We will use this for testing and 
     * debugging.
     */
    private static final String GET_ALL_STORIES = "SELECT " 
            + StringUtils.join(STORY_FIELDS.values(), ",") 
            + " FROM " + STORY_TABLE 
            + " ORDER BY " + STORY_FIELDS.PRIORITY.toString() + "," 
            + STORY_FIELDS.POINTS.toString() + " DESC";
    
    
    /**
     * Gets the number of stories in the table.  This is for testing and debug.
     */
    private static final String GET_STORY_COUNT = "SELECT COUNT(*) FROM " 
                                                    + STORY_TABLE;
    
    
    //=========================================================================
    //  DB UPDATE METHODS
    //=========================================================================
    /**
     * Attempts to add a story to the stories database
     * @param story  the story to add
     * @throws TaskTrackerException  if an error occurred during the add.
     */
    public void addStory(Story story) throws TaskTrackerException
    {
        try(Connection connection = this.openConnection())
        {
            //Get our insert SQL statement ready.  Using a prepared statement
            //  protects us from SQL injection.
            PreparedStatement addStatement = connection.prepareStatement(ADD_STORY);
            addStatement.setString(STORY_FIELDS.ID.getFieldNumber(), story.Id);
            addStatement.setInt(STORY_FIELDS.POINTS.getFieldNumber(),
                                story.Points);
            addStatement.setInt(STORY_FIELDS.PRIORITY.getFieldNumber(),
                                story.Priority);
            
            //Execute the statement
            boolean isResultSet = addStatement.execute();
            
            //We don't expect a result set.
            assert(!isResultSet) : "Was not expecting a result set from our INSERT INTO command.";
            
            //Thus it is an update count.
            int updateCount = addStatement.getUpdateCount();
            if(updateCount != 1)
            {
                throw new TaskTrackerException(
                       String.format("Our update count was %d, but we expected 1.", 
                                     updateCount));
            }
        }
        catch(SQLException e)
        {
//            String message = Messages.getString(DBErrorAdd, story.Id);
//            switch(e.getSQLState())
//            {
//                case SQL_ERROR_NULL_VALUE:
//                    e.
//                    
//            }
            throw new TaskTrackerException(e);
        }
    }
    
    
    /**
     * Attempts to delete a story from the stories database
     * @param storyId  the id of the story to delete
     * @throws TaskTrackerException  if an error occurred during the delete
     */
    public void deleteStory(String storyId) throws TaskTrackerException
    {
        try(Connection connection = this.openConnection())
        {
            //Get our insert SQL statement ready.  Using a prepared statement
            //  protects us from SQL injection.
            PreparedStatement deleteStatement = connection.prepareStatement(DELETE_STORY);
            deleteStatement.setString(STORY_FIELDS.ID.getFieldNumber(), storyId);
            
            //Execute the statement
            boolean isResultSet = deleteStatement.execute();
            
            //We don't expect a result set.
            assert(!isResultSet) : "Was not expecting a result set from our DELETE FROM command.";
            
            //Thus it is an update count.
            int updateCount = deleteStatement.getUpdateCount();
            if(updateCount != 1)
            {
                throw new TaskTrackerException(
                       String.format("Our update count was %d, but we expected 1.", 
                                     updateCount));
            }
        }
        catch(SQLException e)
        {
//            String message = Messages.getString(DBErrorAdd, story.Id);
//            switch(e.getSQLState())
//            {
//                case SQL_ERROR_NULL_VALUE:
//                    e.
//                    
//            }
            throw new TaskTrackerException(e);
        }
    }
    
    
    /**
     * Selects a story based on the id of the story.
     * @param storyId  the id of the story
     * @return  the story matching the id.
     * @throws TaskTrackerException  if there was a problem getting the story 
     *              from the database.
     */
    public Story selectStory(String storyId) throws TaskTrackerException
    {
        try(Connection connection = this.openConnection())
        {
            //Get our insert SQL statement ready.  Using a prepared statement
            //  protects us from SQL injection.
            PreparedStatement queryStatement = connection.prepareStatement(GET_STORY);
            queryStatement.setString(STORY_FIELDS.ID.getFieldNumber(), storyId);
            
            //Execute the statement
            ResultSet results = queryStatement.executeQuery();
            
            Story story = null;
            while(results.next())
            {
                //If story is not null, we got more than 1 story result.  This 
                //  means there is something wrong with the DB set-up.
                if(story != null)
                {
                    throw new TaskTrackerException(
                                Messages.getString("DBTooManyResults", 
                                                   "SELECT"));
                }
                
                story = new Story();
                story.Id = results.getString(STORY_FIELDS.ID.toString());
                story.Points = results.getInt(STORY_FIELDS.POINTS.toString());
                story.Priority = results.getInt(STORY_FIELDS.PRIORITY.toString());
            }
            
            return story;
        }
        catch(SQLException e)
        {
            throw new TaskTrackerException(e);
        }
    }
    
    
    /**
     * Gets a list of all stories in the stories database
     * @throws TaskTrackerException  if an error occurred during the retrieval.
     */
    public List<Story> getAllStories() throws TaskTrackerException
    {
        try(Connection connection = this.openConnection())
        {
            Statement getStatement = connection.createStatement();

            //Execute the statement
            ResultSet results = getStatement.executeQuery(GET_ALL_STORIES);
            
            ArrayList<Story> allStories = new ArrayList<>();
            while(results.next())
            {
                Story story = new Story();
                story.Id = results.getString(STORY_FIELDS.ID.toString());
                story.Points = results.getInt(STORY_FIELDS.POINTS.toString());
                story.Priority = results.getInt(STORY_FIELDS.PRIORITY.toString());
                
                allStories.add(story);
            }
            
            return allStories;
        }
        catch(SQLException e)
        {
            throw new TaskTrackerException(e);
        }
    }
    
    
    /**
     * Gets a list of all stories in the stories database
     * @throws TaskTrackerException  if an error occurred during the retrieval.
     */
    public int getStoryCount() throws TaskTrackerException
    {
        try(Connection connection = this.openConnection())
        {
            Statement getStatement = connection.createStatement();

            //Execute the statement
            ResultSet results = getStatement.executeQuery(GET_STORY_COUNT);
            
            //I don't care to iterate since I should only get one result, but I need
            while(results.next())
            {
                //This will mask out issues like returning too many results, but
                //      this is simpler, easier to maintain code.
                return results.getInt(1);
            }
            
            //else
            throw new TaskTrackerException("We failed to get the story count from the DB.");
        }
        catch(SQLException e)
        {
            throw new TaskTrackerException(e);
        }
    }
    
    
    
    //=========================================================================
    //  HELPER METHODS
    //=========================================================================
    /**
     * Opens a connection to the database
     * @return  the connection to the database
     * @throws SQLException  if there was a problem with the connection.
     */
    private Connection openConnection() throws SQLException
    {
        return DriverManager.getConnection(StoryDB.DB_PROTOCOL
                                               + ": "
                                               + StoryDB.DB_NAME);
    }
}
