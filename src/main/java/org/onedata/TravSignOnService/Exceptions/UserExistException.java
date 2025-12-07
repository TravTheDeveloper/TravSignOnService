package org.onedata.TravSignOnService.Exceptions;

/**
 * Exception thrown for User existing in memory
 */
public class UserExistException extends RuntimeException {
    public UserExistException(String message) {
        super(message);
    }
}
