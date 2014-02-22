/**
 * Copyright (c) 2014, Ian J. De Silva
 * All rights reserved.
 * 
 * Use, distribution, and modification of this work for any purpose is strictly
 * prohibited without the express consent of the copyright holder except as
 * permitted by law.
 */
package uk.co.bbc.iplayer.tracking.test.infrastructure;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

import org.junit.After;
import org.junit.Before;
import org.apache.derby.jdbc.EmbeddedDriver;

import uk.co.bbc.iplayer.tracking.db.StoryDB;
import uk.co.bbc.iplayer.tracking.db.StoryDB.STORY_FIELDS;


/**
 * This class contains the common set-up and tear-down methods required for 
 * testing (by setting up the Derby DB).  
 * 
 * NOTE:  If we were running in a web server environment, this would use a 
 *      connection pool provided by the web server rather than raw connections.
 *      This would allow the program to scale.
 */
public class TestUsingDB
{
    //-------------------------------------------------------------------------
    //  CONSTANTS
    //-------------------------------------------------------------------------
    /**
     * The SQL to create the stories table in the DB.
     */
    private static final String CREATE_TABLE_SQL = "CREATE TABLE " 
            + StoryDB.STORY_TABLE 
            + "( " + StoryDB.STORY_TABLE_PRIMARY_KEY 
                + " INTEGER GENERATED ALWAYS AS IDENTITY"
                + " (START WITH 1, INCREMENT BY 1), "
            + " " + STORY_FIELDS.ID.toString() + " varchar(" + StoryDB.ID_FIELD_SIZE + ") UNIQUE,"
            + " " + STORY_FIELDS.POINTS.toString() + " int NOT NULL"
                + " CHECK (" + STORY_FIELDS.POINTS.toString() + ">0)"
                +","
            + " " + STORY_FIELDS.PRIORITY.toString() + " int NOT NULL"
                + " CHECK ("+ STORY_FIELDS.PRIORITY.toString() + ">0)"
            + ")"; 

    
    //-------------------------------------------------------------------------
    //  TEST SET-UP / TEAR-DOWN
    //-------------------------------------------------------------------------
    /**
     * Creates the database (in-memory). 
     * @throws IllegalAccessException  if the DB driver could not be created.
     * @throws InstantiationException  if the DB driver could not be created.
     * @throws SQLException  if there is a problem connecting and creating the 
     *                  DB.
     */
    @Before
    public void setUp() throws InstantiationException,
                               IllegalAccessException,
                               SQLException
    {
        // Set up derby to output errors to system error.
        Properties systemProperties = System.getProperties();
        systemProperties.put("derby.stream.error.field", 
                             "java.lang.System.err");

        // Set up the derby Database before a test
        EmbeddedDriver.class.newInstance();
        
        // Create the in-memory DB
        Properties connectionProperties = new Properties();
        connectionProperties.put("create", "true");
        
        try(Connection connection = DriverManager.getConnection(StoryDB.DB_PROTOCOL
                                                                    + ": "
                                                                    + StoryDB.DB_NAME,
                                                                connectionProperties))
        {
            Statement createStatement = connection.createStatement();
            createStatement.execute(CREATE_TABLE_SQL);
        }
    }


    /**
     * Destroys the in-memory database.
     * @throws SQLException  if we could not destroy the database.
     */
    @After
    public void tearDown() throws SQLException
    {
        Properties connectionProperties = new Properties();
        connectionProperties.put("drop", "true");
        
        // Destroy the derby database after the test is complete.
        Connection connection = null;
        try
        {
            connection = DriverManager.getConnection(StoryDB.DB_PROTOCOL
                                                        + ": "
                                                        + StoryDB.DB_NAME,
                                                     connectionProperties);
        }
        catch(SQLException e)
        {
            //We expect an SQL state of 08006 as this indicates success.
            if(!e.getSQLState().equals("08006"))
            {
                throw e;
            }
        }
        finally
        {
            //Close the connection
            if(connection != null)
            {
                connection.close();
            }
        }
    }
}
