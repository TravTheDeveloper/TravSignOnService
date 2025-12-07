package org.onedata.Service;

import org.onedata.Exceptions.UserExistException;
import org.onedata.Exceptions.UserNotFoundException;
import org.onedata.Model.User;
import org.onedata.Repository.UserRepository;

import java.util.UUID;

import static org.onedata.Constants.USER_EXISTS;
import static org.onedata.Constants.USER_NOT_FOUND;

/**
 * Business logic utilizing the injected repository
 */

public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository; // Dependency Injection for in memory storage
    }

    public User createUser(String name, String email) throws UserExistException {
        boolean usernameExists = userRepository.retrieveAllUsers()
                .stream()
                .anyMatch(user -> user.getName().equals(name));
        if (usernameExists) { // thread safe check in case multiple concurrent calls to the resource
            throw new UserExistException(USER_EXISTS); // custom exception thrown
        }
        long userID = UUID.randomUUID().getMostSignificantBits(); // random generated user iD
        return userRepository.storeUser(userID, new User(userID, name, email));

    }

    public User readUser(long id) throws UserNotFoundException {
        User user = userRepository.retrieveUser(id);
        if (user == null) {
            throw new UserNotFoundException(USER_NOT_FOUND); // custom exception thrown
        }
        return user;

    }

    public User updateUser(long id, String email) throws UserNotFoundException {
        User user = userRepository.retrieveUser(id);
        if (user == null) {
            throw new UserNotFoundException(USER_NOT_FOUND); // custom exception thrown
        }
        return userRepository.updateUser(user, email);

    }

    public void deleteUser(long id) throws UserNotFoundException {
        User user = userRepository.retrieveUser(id);
        if (user == null) {
            throw new UserNotFoundException(USER_NOT_FOUND); // custom exception thrown
        }
        userRepository.removeUser(id);
    }

}

