package org.zxcjaba.casino.RepositoriesTest;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.zxcjaba.casino.persistence.entity.UserEntity;
import org.zxcjaba.casino.persistence.repository.UserRepository;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import static org.junit.jupiter.api.Assertions.*;

public class UserRepositoryTest {

    @Mock
    private UserRepository userRepository;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testFindByEmail() {
        UserEntity user = new UserEntity();
        user.setEmail("test@example.com");
        user.setPassword("securepassword");

        when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.of(user));

        // Выполнение метода
        Optional<UserEntity> foundUser = userRepository.findByEmail("test@example.com");

        assertTrue(foundUser.isPresent());
        assertEquals("test@example.com", foundUser.get().getEmail());
    }

    @Test
    public void testFindByEmailNotFound() {
        when(userRepository.findByEmail("nonexistent@example.com")).thenReturn(Optional.empty());

        Optional<UserEntity> foundUser = userRepository.findByEmail("nonexistent@example.com");

        assertFalse(foundUser.isPresent());
    }

    @Test
    public void testSaveUser() {
        UserEntity user = new UserEntity();
        user.setEmail("test2@example.com");
        user.setPassword("anotherpassword");

        when(userRepository.save(any(UserEntity.class))).thenReturn(user);

        UserEntity savedUser = userRepository.save(user);

        assertNotNull(savedUser);
        assertEquals("test2@example.com", savedUser.getEmail());
    }
}
