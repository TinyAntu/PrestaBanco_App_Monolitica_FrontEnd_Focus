package tinyantu.prestabancobackend.services;

import tinyantu.prestabancobackend.entities.UserEntity;
import tinyantu.prestabancobackend.repositories.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    //Testo for get
    @Test
    public void testFindUserByIdSuccess() {
        UserEntity user = new UserEntity();
        user.setId(1L);
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        UserEntity result = userService.findUserById(1L);
        assertNotNull(result);
        assertEquals(1L, result.getId());
    }

    @Test
    public void testFindUserByIdNotFound() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        Exception exception = assertThrows(RuntimeException.class, () -> userService.findUserById(1L));
        assertEquals("User Not Found", exception.getMessage());
    }

    @Test
    public void testFindUserById_ZeroId() {
        Long id = 0L;
        when(userRepository.findById(id)).thenReturn(Optional.empty());

        Exception exception = assertThrows(RuntimeException.class, () -> userService.findUserById(id));
        assertEquals("User Not Found", exception.getMessage());
    }

    @Test
    public void testFindUserById_NegativeId() {
        Long id = -1L;

        Exception exception = assertThrows(RuntimeException.class, () -> userService.findUserById(id));
        assertEquals("User Not Found", exception.getMessage());
    }

    @Test
    public void testFindUserById_CallsRepositoryOnce() {
        Long id = 1L;
        UserEntity user = new UserEntity();
        user.setId(id);

        when(userRepository.findById(id)).thenReturn(Optional.of(user));

        userService.findUserById(id);

        verify(userRepository, times(1)).findById(id);
    }

    @Test
    public void testFindUserById_MultipleCalls() {
        Long id = 1L;
        UserEntity user = new UserEntity();
        user.setId(id);

        when(userRepository.findById(id)).thenReturn(Optional.of(user));

        UserEntity firstCall = userService.findUserById(id);
        UserEntity secondCall = userService.findUserById(id);

        assertEquals(firstCall, secondCall);
        verify(userRepository, times(2)).findById(id);
    }

    @Test
    public void testFindUserById_DatabaseError() {
        Long id = 1L;
        when(userRepository.findById(id)).thenThrow(new RuntimeException("Database error"));

        Exception exception = assertThrows(RuntimeException.class, () -> userService.findUserById(id));
        assertEquals("Database error", exception.getMessage());
    }

    //Test for save
    @Test
    public void testSaveUser() {
        UserEntity user = new UserEntity();
        user.setRut("12345678-9");
        user.setPassword("password");
        when(userRepository.save(user)).thenReturn(user);

        UserEntity savedUser = userService.saveUser(user);
        assertNotNull(savedUser);
        assertEquals("12345678-9", savedUser.getRut());
    }

    @Test
    public void testSaveUser_DuplicateRut() {
        UserEntity user = new UserEntity();
        user.setRut("12345678-9");
        user.setPassword("password");
        when(userRepository.save(user)).thenThrow(new DataIntegrityViolationException("RUT already exists"));

        Exception exception = assertThrows(DataIntegrityViolationException.class, () -> userService.saveUser(user));
        assertEquals("RUT already exists", exception.getMessage());
    }


    @Test
    public void testSaveUser_DatabaseError() {
        UserEntity user = new UserEntity();
        user.setRut("12345678-9");
        user.setPassword("password");
        when(userRepository.save(user)).thenThrow(new RuntimeException("Database error"));

        Exception exception = assertThrows(RuntimeException.class, () -> userService.saveUser(user));
        assertEquals("Database error", exception.getMessage());
    }

    @Test
    public void testSaveUser_EmptyRut() {
        UserEntity user = new UserEntity();
        user.setRut(""); // Rut vacío
        user.setPassword("password");

        when(userRepository.save(user)).thenReturn(user);

        UserEntity savedUser = userService.saveUser(user);
        assertNotNull(savedUser);
        assertEquals("", savedUser.getRut());
    }

    @Test
    public void testSaveUser_DuplicateUser() {
        UserEntity user = new UserEntity();
        user.setRut("12345678-9");
        user.setPassword("password");

        when(userRepository.save(user)).thenThrow(new RuntimeException("User already exists"));

        Exception exception = assertThrows(RuntimeException.class, () -> userService.saveUser(user));
        assertEquals("User already exists", exception.getMessage());
    }

    @Test
    public void testSaveUser_2() {
        UserEntity user = new UserEntity();
        user.setRut("12345678-9");
        user.setPassword("securePassword");
        when(userRepository.save(user)).thenReturn(user);
        UserEntity savedUser = userService.saveUser(user);

        assertNotNull(savedUser);
        assertEquals("12345678-9", savedUser.getRut());
        assertEquals("securePassword", savedUser.getPassword());
        verify(userRepository, times(1)).save(user);
    }

    @Test
    public void testSaveUser_EmptyPassword() {
        UserEntity user = new UserEntity();
        user.setRut("12345678-9");
        user.setPassword("");

        when(userRepository.save(user)).thenReturn(user);

        UserEntity savedUser = userService.saveUser(user);
        assertNotNull(savedUser);
        assertEquals("", savedUser.getPassword());
    }

    //Test for AUTHENTICATION
    @Test
    public void testAuthenticateUserSuccess() {
        UserEntity user = new UserEntity();
        user.setRut("12345678-9");
        user.setPassword("password");
        when(userRepository.findByRut("12345678-9")).thenReturn(Optional.of(user));

        UserEntity authenticatedUser = userService.authenticateUser("12345678-9", "password");
        assertNotNull(authenticatedUser);
        assertEquals("12345678-9", authenticatedUser.getRut());
    }

    @Test
    public void testAuthenticateUserIncorrectPassword() {
        UserEntity user = new UserEntity();
        user.setRut("12345678-9");
        user.setPassword("password");
        when(userRepository.findByRut("12345678-9")).thenReturn(Optional.of(user));

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () ->
                userService.authenticateUser("12345678-9", "wrongpassword"));
        assertEquals(HttpStatus.UNAUTHORIZED, exception.getStatusCode());
        assertEquals("Wrong password", exception.getReason());
    }

    @Test
    public void testAuthenticateUserNotFound() {
        when(userRepository.findByRut("12345678-9")).thenReturn(Optional.empty());

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () ->
                userService.authenticateUser("12345678-9", "password"));
        assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
        assertEquals("User not found", exception.getReason());
    }

    @Test
    public void testAuthenticateUser_EmptyPassword() {
        UserEntity user = new UserEntity();
        user.setRut("12345678-9");
        user.setPassword("password");
        when(userRepository.findByRut("12345678-9")).thenReturn(Optional.of(user));

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () ->
                userService.authenticateUser("12345678-9", ""));
        assertEquals(HttpStatus.UNAUTHORIZED, exception.getStatusCode());
        assertEquals("Wrong password", exception.getReason());
    }

    @Test
    public void testAuthenticateUser_NullPassword() {
        UserEntity user = new UserEntity();
        user.setRut("12345678-9");
        user.setPassword("password");
        when(userRepository.findByRut("12345678-9")).thenReturn(Optional.of(user));

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () ->
                userService.authenticateUser("12345678-9", null));
        assertEquals(HttpStatus.UNAUTHORIZED, exception.getStatusCode());
        assertEquals("Wrong password", exception.getReason());
    }

    @Test
    public void testAuthenticateUser_EmptyRut() {
        when(userRepository.findByRut("")).thenReturn(Optional.empty());
        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () ->
                userService.authenticateUser("", "password"));
        assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
        assertEquals("User not found", exception.getReason());
    }

    @Test
    public void testAuthenticateUser_DifferentPassword() {
        UserEntity user = new UserEntity();
        user.setRut("12345678-9");
        user.setPassword("correctPassword");
        when(userRepository.findByRut("12345678-9")).thenReturn(Optional.of(user));

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () ->
                userService.authenticateUser("12345678-9", "differentPassword"));
        assertEquals(HttpStatus.UNAUTHORIZED, exception.getStatusCode());
        assertEquals("Wrong password", exception.getReason());
    }

    //Test for age in years
    @Test
    public void testAgeInYears() {
        LocalDate birthDate = LocalDate.of(1990, 1, 1);
        Date date = Date.from(birthDate.atStartOfDay(ZoneId.systemDefault()).toInstant());

        int age = userService.AgeInYears(date);
        int expectedAge = LocalDate.now().getYear() - birthDate.getYear();

        assertEquals(expectedAge, age);
    }

    @Test
    public void testAgeInYears_BirthdateToday() {
        Date today = Date.from(LocalDate.now().atStartOfDay(ZoneId.systemDefault()).toInstant());
        int age = userService.AgeInYears(today);
        assertEquals(0, age);
    }

    @Test
    public void testAgeInYears_LeapDay() {
        LocalDate birthDate = LocalDate.of(2000, 2, 29);
        Date date = Date.from(birthDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
        int age = userService.AgeInYears(date);
        int expectedAge = LocalDate.now().getYear() - birthDate.getYear();
        if (LocalDate.now().isBefore(birthDate.withYear(LocalDate.now().getYear()))) {
            expectedAge--;
        }

        assertEquals(expectedAge, age);
    }

    @Test
    public void testAgeInYears_FutureDate() {
        LocalDate futureDate = LocalDate.now().plusYears(10);
        Date date = Date.from(futureDate.atStartOfDay(ZoneId.systemDefault()).toInstant());

        int age = userService.AgeInYears(date);

        assertTrue(age < 0, "To be in the future the date has to be negative");
    }

    @Test
    public void testAgeInYears_NewYearBirth() {
        LocalDate birthDate = LocalDate.of(1985, 1, 1);
        Date date = Date.from(birthDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
        int age = userService.AgeInYears(date);
        int expectedAge = LocalDate.now().getYear() - birthDate.getYear();
        assertEquals(expectedAge, age);
    }

    @Test
    public void testAgeInYears_BirthdateDecember() {
        LocalDate birthDate = LocalDate.of(1990, 12, 15);
        Date date = Date.from(birthDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
        int age = userService.AgeInYears(date);
        int expectedAge = LocalDate.now().getYear() - birthDate.getYear();
        if (LocalDate.now().isBefore(birthDate.withYear(LocalDate.now().getYear()))) {
            expectedAge--;
        }
        assertEquals(expectedAge, age);
    }

    @Test
    public void testAgeInYears_SQLDate() {
        LocalDate birthDate = LocalDate.of(1995, 5, 20);
        java.sql.Date sqlDate = java.sql.Date.valueOf(birthDate);
        int age = userService.AgeInYears(sqlDate);
        int expectedAge = LocalDate.now().getYear() - birthDate.getYear();
        if (LocalDate.now().isBefore(birthDate.withYear(LocalDate.now().getYear()))) {
            expectedAge--;
        }
        assertEquals(expectedAge, age);
    }

}

