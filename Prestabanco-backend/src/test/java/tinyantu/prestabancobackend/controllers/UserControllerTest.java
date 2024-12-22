package tinyantu.prestabancobackend.controllers;

import tinyantu.prestabancobackend.entities.UserEntity;
import tinyantu.prestabancobackend.services.UserService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import com.fasterxml.jackson.databind.ObjectMapper;

@WebMvcTest(UserController.class)
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void testSaveUser() throws Exception {
        UserEntity user = new UserEntity();
        user.setRut("12345678-9");
        user.setPassword("password");

        UserEntity savedUser = new UserEntity();
        savedUser.setRut("12345678-9");
        savedUser.setPassword("password");

        Mockito.when(userService.saveUser(Mockito.any(UserEntity.class))).thenReturn(savedUser);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/users/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(user)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.rut").value("12345678-9"));
    }

    @Test
    public void testLoginUser() throws Exception {
        UserEntity user = new UserEntity();
        user.setRut("12345678-9");
        user.setPassword("password");

        Mockito.when(userService.authenticateUser("12345678-9", "password")).thenReturn(user);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/users/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(user)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.rut").value("12345678-9"));
    }
}
