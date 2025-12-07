package org.onedata.Repository;

import org.onedata.Model.User;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public class UserRepository {

    private final ConcurrentHashMap<Long, User> users = new ConcurrentHashMap<>(); // thread safe hashmap without locking the entire map

    public void storeUser(long userID, User user) {
        users.put(userID, user);
    }

    public User retrieveUser(long id){
        return users.get(id);
    }

    public List<User> retrieveAllUsers(){
        return List.copyOf(users.values());
    }

    public void updateUser(User user, String email) {
        user.setEmail(email);
    }

    public void removeUser(User user) {
        users.remove(user.getId());
    }
}

//access and add the data to the user
