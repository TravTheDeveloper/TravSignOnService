package org.onedata.TravSignOnService.exceptions;

/**
 * Exception thrown for User not found
 */
public class UserNotFoundException extends RuntimeException {

    public UserNotFoundException(String message)
    {
        super(message);
    }
}
