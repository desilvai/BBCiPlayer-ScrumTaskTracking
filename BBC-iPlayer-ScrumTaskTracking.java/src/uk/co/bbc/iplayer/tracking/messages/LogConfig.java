/**
 * Copyright (c) 2014, Ian J. De Silva
 * All rights reserved.
 * 
 * Use, distribution, and modification of this work for any purpose is strictly
 * prohibited without the express consent of the copyright holder except as 
 * permitted by law.
 */
package uk.co.bbc.iplayer.tracking.messages;

import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;

import uk.co.bbc.iplayer.tracking.impl.Backlog;

/**
 * 
 */
public class LogConfig
{
    /**
     * The location of the logging properties.
     */
    public static final String LOG_PROPERTIES = "/uk/co/bbc/iplayer/tracking/messages/logging.properties";
    
    
    /**
     * Sets up the logging facility by loading in the properties file.  The 
     * default properties can be overridden by specifying the 
     * java.util.logging.config.file property on the command line.
     */
    public static final void setUpLogger()
    {
        String logProperties = System.getProperty("java.util.logging.config.file");
        if(logProperties == null)
        {
            InputStream propertyStream = Backlog.class.getResourceAsStream(LOG_PROPERTIES);
            
            try
            {
                LogManager.getLogManager().readConfiguration(propertyStream);
            }
            catch (final IOException e)
            {
                Logger anonymousLogger = Logger.getAnonymousLogger();
                anonymousLogger.severe(Messages.getString("LogPropertiesFail"));
                anonymousLogger.log(Level.SEVERE, e.getMessage(), e);
            }
        }
    }
    
    

    /**
     * Hide the constructor.
     */
    private LogConfig()
    {
    }

}
