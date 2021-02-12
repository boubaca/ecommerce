package com.example.demo.controllers;

import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.UserOrder;
import com.example.demo.model.requests.CreateUserRequest;
import com.example.demo.model.requests.ModifyCartRequest;
import com.example.demo.services.CartService;
import com.example.demo.services.ItemService;
import com.example.demo.services.OrderService;
import com.example.demo.services.UserService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJsonTesters;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.util.*;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureJsonTesters
public class OrderControllerTest {

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @MockBean
    private UserService userService;

    @Autowired
    private OrderController orderController;

   @MockBean
    private ItemService itemService;

    @Autowired
    private CartService cartService;

    @Autowired
    private CartController cartController;

    @Autowired
    private ItemController itemController;

   @MockBean
    private OrderService orderService;






    @Before
    public void setup(){
        User user = getUser();
        user.setId(1L);
        //given(userService.saveUser(getUserRequest())).willReturn(user);
        given(userService.findByUsername(any())).willReturn(user);
        given(userService.findById(any())).willReturn(java.util.Optional.of(user));
        given(itemService.findAll()).willReturn(Collections.singletonList(getItem()));
        given(itemService.findByName(any())).willReturn(Collections.singletonList(getItem()));
        given(itemService.findById(any())).willReturn(Optional.of(getItem()));
        given(orderService.findByUser(any())).willReturn(Collections.singletonList(getUserOrder()));

    }

    @Test
    public void addTocart(){
        ModifyCartRequest request = getCart();
        ResponseEntity<Cart> responseEntity = cartController.addTocart(request);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(1,responseEntity.getBody().getItems().size());
        assertEquals("barry1",responseEntity.getBody().getUser().getUsername());
    }

    @Test
    public void removeFromcart(){
        ModifyCartRequest request = getCart();
        ResponseEntity<Cart> responseEntity = cartController.removeFromcart(request);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(1L, Objects.requireNonNull(responseEntity.getBody()).getId().longValue());

    }

    @Test
    public void getItems(){
        ResponseEntity<List<Item>> responseEntity = itemController.getItems();
        assertEquals(1, Objects.requireNonNull(responseEntity.getBody()).size());
        assertEquals(HttpStatus.OK,responseEntity.getStatusCode());
    }

    @Test

    public void getItemById(){
        ResponseEntity<Item> responseEntity = itemController.getItemById(1L);
        assertNotNull(responseEntity.getBody());
        assertEquals(HttpStatus.OK,responseEntity.getStatusCode());
    }

    @Test
    public void getItemsByName(){
        ResponseEntity<List<Item>> responseEntity = itemController.getItemsByName("hp");
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(1, Objects.requireNonNull(responseEntity.getBody()).size());
    }

    @Test
    public void submitOrder(){
        User user = getUser();
        user.getCart().setItems(Collections.singletonList(getItem()));
        user.getCart().setTotal(getItem().getPrice());
        given(userService.findByUsername(any())).willReturn(user);

        ResponseEntity<UserOrder> userOrderResponseEntity = orderController.submit(user.getUsername());

        assertEquals(HttpStatus.OK,userOrderResponseEntity.getStatusCode());
        assertEquals(new BigDecimal(1200), Objects.requireNonNull(userOrderResponseEntity.getBody()).getTotal());
    }
    @Test
    public void getOrdersForUser()
    {
        ResponseEntity<List<UserOrder>> responseEntity = orderController.getOrdersForUser("barry1");
        assertEquals(1, Objects.requireNonNull(responseEntity.getBody()).size());

    }


    private User getUser() {
        User user = new User();
        user.setUsername("barry1");
        user.setPassword(bCryptPasswordEncoder.encode("test1234567"));
        Cart cart = new Cart();
        cart.setId(1L);
        cart.setUser(user);
        user.setCart(cart);
        return user;
    }
    private Cart cart(){

        return getUser().getCart();
    }

    private UserOrder getUserOrder(){
        UserOrder userOrder = new UserOrder();
        userOrder.setId(1L);
        userOrder.setUser(getUser());
        userOrder.setItems(Collections.singletonList(getItem()));
        userOrder.setTotal(getItem().getPrice());
        return userOrder;
    }

    private ModifyCartRequest getCart(){
        ModifyCartRequest modifyCartRequest = new ModifyCartRequest();
        modifyCartRequest.setUsername(getUser().getUsername());
        modifyCartRequest.setQuantity(1);
        modifyCartRequest.setItemId(1L);

        return modifyCartRequest;
    }

    private Item getItem(){
        Item item = new Item();
        item.setId(1L);
        item.setName("hp");
        item.setDescription("Laptop i7");
        item.setPrice(new BigDecimal(1200));

        return item;
    }

    private CreateUserRequest getUserRequest(){
        CreateUserRequest userRequest = new CreateUserRequest();
        userRequest.setUsername("barry1");
        userRequest.setPassword("test1234567");
        userRequest.setConfirmPassword("test1234567");

        return userRequest;
    }
}
