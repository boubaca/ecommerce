package com.example.demo.controllers;

import com.example.demo.UserLogin;
import com.example.demo.controllers.UserController;
import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.User;
import com.example.demo.model.requests.CreateUserRequest;
import com.example.demo.services.UserService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJsonTesters;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.net.URI;
import java.util.Objects;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureJsonTesters
public class UserControllerTest {

    @Autowired
    private MockMvc mvc;


    @Autowired
    BCryptPasswordEncoder bCryptPasswordEncoder;


    @Autowired
    private JacksonTester<CreateUserRequest> json;


    @Autowired
    private JacksonTester<UserLogin> jsonUser;

    @Autowired
    private UserController userController;


    @MockBean
    private UserService userService;




    /**
     * Creates pre-requisites for testing, such as an example User.
     */
    @Before
    public void setup(){
        User user = getUser();
        user.setId(1L);
        given(userService.saveUser(any())).willReturn(user);
        given(userService.findByUsername(any())).willReturn(user);
        given(userService.findById(any())).willReturn(java.util.Optional.of(user));

    }

    /**
     * Tests for successful creation of new User in the system
     * @throws Exception when User creation fails in the system
     */

    @Test
    public void creatUser() throws Exception {
       CreateUserRequest userRequest = getUserRequest();
        mvc.perform(
                post(new URI("/api/user/create"))
                        .content(json.write(userRequest).getJson())
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .accept(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk()).andExpect(content().json("{id:1,username:barry1}"));

    }

    @Test
    public void loginSuccess_200() throws  Exception{
        UserLogin user = new UserLogin();
        user.setUsername("barry1");
        user.setPassword("test1234567");
        mvc.perform(
                post(new URI("/login"))
                        .content(jsonUser.write(user).getJson())
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .accept(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().is(200));

    }
    @Test
    public void findUser_200() throws Exception{

        ResponseEntity<User> responseEntity =
                userController.findByUserName(getUser().getUsername());

        assertEquals(HttpStatus.OK,responseEntity.getStatusCode());
        assertEquals("barry1", Objects.requireNonNull(responseEntity.getBody()).getUsername());
    }

    @Test
    public void findUserById_200(){
        ResponseEntity<User> responseEntity = userController.findById(1L);

        assertEquals(HttpStatus.OK,responseEntity.getStatusCode());
        assertEquals("barry1", Objects.requireNonNull(responseEntity.getBody()).getUsername());
    }

    @Test
    public void loginFail_401() throws  Exception{
        UserLogin user = new UserLogin();
        user.setUsername("barry1");
        user.setPassword("test123456");
        mvc.perform(
                post(new URI("/login"))
                        .content(jsonUser.write(user).getJson())
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .accept(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().is(401));

    }



    private User getUser() {
        User user = new User();
        user.setUsername("barry1");
        user.setPassword(bCryptPasswordEncoder.encode("test1234567"));
        user.setCart(new Cart());
        return user;
    }

    private CreateUserRequest getUserRequest(){
        CreateUserRequest userRequest = new CreateUserRequest();
        userRequest.setUsername("barry1");
        userRequest.setPassword("test1234567");
        userRequest.setConfirmPassword("test1234567");

        return userRequest;
    }


}
