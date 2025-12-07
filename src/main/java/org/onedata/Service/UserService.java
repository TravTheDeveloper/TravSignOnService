package org.onedata.Service;

import org.onedata.Exceptions.UserExistException;
import org.onedata.Exceptions.UserNotFoundException;
import org.onedata.Model.User;
import org.onedata.Repository.UserRepository;

import java.util.UUID;


public class UserService {

    private final UserRepository userRepository;

    public UserService (UserRepository userRepository) {
        this.userRepository = userRepository; // Dependency Injection for in memory storage
    }

    public void createUser(String name, String email) throws UserExistException {
      boolean usernameExists = userRepository.retrieveAllUsers()
              .stream()
              .anyMatch(user -> user.getName().equals(name));
      if (usernameExists) { // thread safe check in case multiple concurrent calls to the resource
          throw new UserExistException("Username Already exists"); // custom exception thrown
      } else {
          long userID = UUID.randomUUID().getMostSignificantBits(); // random generated user iD
          userRepository.storeUser(userID, new User(userID, name, email));
      }
    }

    public User readUser(long id) throws UserNotFoundException {
        User user = userRepository.retrieveUser(id);
        if (user == null) {
            throw new UserNotFoundException("User does not exist"); // custom exception thrown
        }
        return user;

    }

    public void updateUser(long id, String email) throws UserNotFoundException {
        User user = userRepository.retrieveUser(id);
        if (user == null) {
            throw new UserNotFoundException("User does not exist"); // custom exception thrown
        }
        userRepository.updateUser(user, email);

    }
    public void deleteUser(long id) throws UserNotFoundException {
        User user = userRepository.retrieveUser(id);
        if (user == null) {
            throw new UserNotFoundException("User does not exist"); // custom exception thrown
        }
        userRepository.removeUser(user);
    }

}


// business logic using the repository
