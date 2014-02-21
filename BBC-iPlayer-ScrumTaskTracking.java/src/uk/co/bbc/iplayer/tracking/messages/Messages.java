/**
 * Copyright (c) 2014, Ian J. De Silva
 * All rights reserved.
 * 
 * Use, distribution, and modification of this work for any purpose is strictly
 * prohibited without the express consent of the copyright holder except as 
 * permitted by law.
 */
package uk.co.bbc.iplayer.tracking.messages;

import java.text.MessageFormat;
import java.util.ResourceBundle;

/**
 * @author desilva
 *
 */
public class Messages
{
    private static final String BUNDLE_NAME = "uk.co.bbc.iplayer.tracking.messages.messages";
    private static final ResourceBundle MESSAGE_BUNDLE = ResourceBundle.getBundle(BUNDLE_NAME);

    /**
     * Constructor.  Private because this is a static class.
     */
    private Messages()
    {
    }
    
    /**
     * Gets the string from the message bundle.
     * @param key
     * @param parameters
     * @return
     */
    public static String getString(String key, Object... parameters)
    {
        return MessageFormat.format(MESSAGE_BUNDLE.getString(key), parameters);
    }

}
