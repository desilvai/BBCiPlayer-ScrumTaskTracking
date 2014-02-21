/**
 * Copyright (c) 2014, Ian J. De Silva
 * All rights reserved.
 * 
 * Use, distribution, and modification of this work for any purpose is strictly
 * prohibited without the express consent of the copyright holder except as 
 * permitted by law.
 */
package uk.co.bbc.iplayer.tracking.exceptions;

/**
 * This is our base exception for the Task Tracking software.
 */
@SuppressWarnings("serial")
public class TaskTrackerException extends Exception
{

    /**
     * 
     */
    public TaskTrackerException()
    {
    }


    /**
     * @param message
     */
    public TaskTrackerException(String message)
    {
        super(message);
    }


    /**
     * @param cause
     */
    public TaskTrackerException(Throwable cause)
    {
        super(cause);
    }


    /**
     * @param message
     * @param cause
     */
    public TaskTrackerException(String message,
                                Throwable cause)
    {
        super(message, cause);
    }


    /**
     * @param message
     * @param cause
     * @param enableSuppression
     * @param writableStackTrace
     */
    public TaskTrackerException(String message,
                                Throwable cause,
                                boolean enableSuppression,
                                boolean writableStackTrace)
    {
        super(message, cause, enableSuppression, writableStackTrace);
    }

}
