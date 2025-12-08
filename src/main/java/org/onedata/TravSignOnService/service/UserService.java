package org.onedata.TravSignOnService.service;

import org.onedata.TravSignOnService.exceptions.UserExistException;
import org.onedata.TravSignOnService.exceptions.UserNotFoundException;
import org.onedata.TravSignOnService.model.User;
import org.onedata.TravSignOnService.repository.UserRepository;

import java.util.UUID;

import static org.onedata.TravSignOnService.Constants.USER_EXISTS;
import static org.onedata.TravSignOnService.Constants.USER_NOT_FOUND;

/**
 * Business logic utilizing the injected repository
 */

public class UserService {

    private final UserRepository userRepository;

    /**
     *  Inject repository dependency into Service class
     * @param userRepository repository dependency
     */
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository; // Dependency Injection for in memory storage
    }

    /**
     *  Add user to memory if it doesn't exist, throw exception if it does exist
     * @param name user name
     * @param email user email address
     * @return created User object
     * @throws UserExistException exception for existing user with same name
     */
    public User createUser(String name, String email) throws UserExistException {
        boolean usernameExists = userRepository.retrieveAllUsers()
                .stream()
                .anyMatch(user -> user.getName().equals(name));
        if (usernameExists) { // thread safe check in case multiple concurrent calls to the resource
            throw new UserExistException(USER_EXISTS); // custom exception thrown
        }
        long userID = UUID.randomUUID().getMostSignificantBits() & Long.MAX_VALUE; // random generated user iD
        return userRepository.storeUser(userID, new User(userID, name, email));

    }

    /**
     *  Read user by ID from memory, throw exception if ID does not exist in memory
     * @param id User ID
     * @return retrieved User Object
     * @throws UserNotFoundException exception for provided ID not existing in memory
     */

    public User readUser(long id) throws UserNotFoundException {
        User user = userRepository.retrieveUser(id);
        if (user == null) {
            throw new UserNotFoundException(USER_NOT_FOUND); // custom exception thrown
        }
        return user;

    }

    /**
     *  Update email for provided user ID, throw exception if ID does not exist in memory
     * @param id User ID
     * @param email retrieved User Object
     * @return updated User object
     * @throws UserNotFoundException exception for provided ID not existing in memory
     */
    public User updateUser(long id, String email) throws UserNotFoundException {
        User user = userRepository.retrieveUser(id);
        if (user == null) {
            throw new UserNotFoundException(USER_NOT_FOUND); // custom exception thrown
        }
        return userRepository.updateUser(user, email);

    }

    /**
     *  Delete user from memory, throw exception if ID does not exist in memory
     * @param id User ID
     * @throws UserNotFoundException exception for provided ID not existing in memory
     */
    public void deleteUser(long id) throws UserNotFoundException {
        User user = userRepository.retrieveUser(id);
        if (user == null) {
            throw new UserNotFoundException(USER_NOT_FOUND); // custom exception thrown
        }
        userRepository.removeUser(id);
    }

}

