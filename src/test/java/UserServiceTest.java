import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.onedata.TravSignOnService.exceptions.UserExistException;
import org.onedata.TravSignOnService.exceptions.UserNotFoundException;
import org.onedata.TravSignOnService.model.User;
import org.onedata.TravSignOnService.repository.UserRepository;
import org.onedata.TravSignOnService.service.UserService;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private UserRepository userRepository; // mock userRepository

    @InjectMocks
    private UserService userService;

    private final String name = "testUser";
    private final String email = "testuser@test.com";
    private final User newUser = new User(1234L, name, email);


    /**
     *  Test Create User method
     */
    @Test
    void shouldCreateNewUser() {
        when(userRepository.retrieveAllUsers()).thenReturn(List.of());

        when(userRepository.storeUser(any(Long.class), any(User.class))).thenReturn(newUser);

        User storedUser = userService.createUser(name, email);

        verify(userRepository, times(1)).storeUser(any(Long.class), any(User.class));
        verify(userRepository, times(1)).retrieveAllUsers();

        assertNotNull(storedUser);
        assertEquals(name, storedUser.getName());
        assertEquals(email, storedUser.getEmail());
    }

    /**
     *  Test That Exception is thrown when user exists in memory
     */
    @Test
    void shouldThrowExceptionIfUserNameExists() {
        when(userRepository.retrieveAllUsers()).thenReturn(List.of(newUser));

        UserExistException exception =
                assertThrows(UserExistException.class,
                        () -> userService.createUser(name, email));

        verify(userRepository, times(1)).retrieveAllUsers();
        verify(userRepository, times(0)).storeUser(any(Long.class), any(User.class));

        assertEquals("Username already exists", exception.getMessage());
    }

    /**
     *  Test Read User Method
     */
    @Test
    void shouldReadUserById() {
        when(userRepository.retrieveUser(any(Long.class))).thenReturn(newUser);

        User storedUser = userService.readUser(1234);

        verify(userRepository, times(1)).retrieveUser(1234L);

        assertNotNull(storedUser);

        assertEquals(name, storedUser.getName());
        assertEquals(email, storedUser.getEmail());
        assertEquals(newUser, storedUser);

    }

    /**
     *  Test Update User Method
     */
    @Test
    void shouldUpdateUser() {
        String updatedEmail = "newemail@test.com";
        User updatedUser = new User(1234L, name, updatedEmail);

        when(userRepository.retrieveUser(any(Long.class))).thenReturn(newUser);
        when(userRepository.updateUser(any(User.class), any(String.class))).thenReturn(updatedUser);

        User storedUser = userService.updateUser(1234L, updatedEmail);

        verify(userRepository, times(1)).retrieveUser(1234L);
        verify(userRepository, times(1)).updateUser(any(User.class), any(String.class));

        assertNotNull(storedUser);

        assertEquals(name, storedUser.getName());
        assertEquals(updatedEmail, storedUser.getEmail());
        assertEquals(updatedUser, storedUser);

    }

    /**
     *  Test Delete User Method
     */
    @Test
    void shouldDeleteUserById() {
        when(userRepository.retrieveUser(any(Long.class))).thenReturn(newUser);

        userService.deleteUser(1234L);
        verify(userRepository, times(1)).retrieveUser(1234L);
        verify(userRepository, times(1)).removeUser(1234L);
    }

    /**
     *  Test That Exceptions are thrown when user does not exist in memory
     */
    @Test
    void shouldThrowExceptionIfUserNotFound() {
        when(userRepository.retrieveUser(any(Long.class))).thenReturn(null);

        UserNotFoundException readException =
                assertThrows(UserNotFoundException.class,
                        () -> userService.readUser(1234L));

        UserNotFoundException updateException =
                assertThrows(UserNotFoundException.class,
                        () -> userService.updateUser(1234L, email));

        UserNotFoundException deleteException =
                assertThrows(UserNotFoundException.class,
                        () -> userService.deleteUser(1234L));


        verify(userRepository, times(3)).retrieveUser(1234L);

        verify(userRepository, times(0)).storeUser(any(Long.class), any(User.class));
        verify(userRepository, times(0)).updateUser(any(User.class), any(String.class));
        verify(userRepository, times(0)).removeUser(any(Long.class));

        assertEquals("User does not exist", readException.getMessage());
        assertEquals("User does not exist", updateException.getMessage());
        assertEquals("User does not exist", deleteException.getMessage());
    }

}
