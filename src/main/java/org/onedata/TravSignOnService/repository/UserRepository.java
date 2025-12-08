package org.onedata.TravSignOnService.repository;

import org.onedata.TravSignOnService.model.User;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Access and add the data to memory (HashMap)
 */

public class UserRepository {

    private final ConcurrentHashMap<Long, User> users = new ConcurrentHashMap<>(); // thread safe hashmap without locking the entire map

    public User storeUser(long userID, User user) {
        users.put(userID, user);
        return user;
    }

    public User retrieveUser(long id){
        return users.get(id);
    }

    public List<User> retrieveAllUsers(){
        return List.copyOf(users.values());
    }

    public User updateUser(User user, String email) {
        user.setEmail(email);
        return user;
    }

    public void removeUser(long id) {
        users.remove(id);
    }
}

