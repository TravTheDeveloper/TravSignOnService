import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.onedata.TravSignOnService.model.User;
import org.onedata.TravSignOnService.repository.UserRepository;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class UserRepositoryTest {

    private UserRepository userRepository;
    private long userID;
    private String email;
    private User newUser;

    /**
     *  Create and store initial user object
     */
    @BeforeEach
    void setUp() {
        userRepository = new UserRepository();
        userID = 1234L;
        email = "testuser@test.com";
        newUser = userRepository.storeUser(userID, new User(userID, "testUser", email));
    }

    /**
     *  Test that user object exists in memory
     */
    @Test
    void shouldSuccessfullyStoreUser() {
        assertNotNull(userRepository.retrieveUser(userID));
        assertEquals(newUser, userRepository.retrieveUser(userID));
    }

    /**
     *  Test that user is able to be retrieved from memory
     */
    @Test
    void shouldSuccessfullyRetrieveUsers() {
        long user2ID = 5678L;
        String user2 = "testUser";
        String email2 = "testuser@test.com";
        User newUser2 = userRepository.storeUser(user2ID, new User(user2ID, user2, email2));

        List<User> users = Arrays.asList(newUser, newUser2);

        assertNotNull(userRepository.retrieveUser(userID));
        assertEquals(newUser, userRepository.retrieveUser(userID));
        assertEquals(newUser2, userRepository.retrieveUser(user2ID));
        assertEquals(users, userRepository.retrieveAllUsers());
    }

    /**
     *  Test that null is returned when user does not exist
     */
    @Test
    void shouldReturnNullWhenUserNotFound() {
        assertNull(userRepository.retrieveUser(9999L));
    }

    /**
     *  Test that existing email is updated with new email
     */
    @Test
    void shouldSuccessfullyUpdateUserEmail() {
        String updatedEmail = "newTestUser@test.com";

        assertEquals(email, newUser.getEmail());

        userRepository.updateUser(newUser, updatedEmail);

        assertEquals(updatedEmail, newUser.getEmail());
        assertEquals(updatedEmail, userRepository.retrieveUser(userID).getEmail());
    }

    /**
     *  Test that user with correlating userID is removed from memory
     */
    @Test
    void shouldSuccessfullyRemoveUser() {
        assertNotNull(userRepository.retrieveUser(userID));
        userRepository.removeUser(userID);
        assertNull(userRepository.retrieveUser(userID));
    }


}
