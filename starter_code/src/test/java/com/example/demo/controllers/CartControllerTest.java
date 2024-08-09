package com.example.demo.controllers;

import com.example.demo.TestUtils;
import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.repositories.CartRepository;
import com.example.demo.model.persistence.repositories.ItemRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import com.example.demo.model.requests.ModifyCartRequest;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

//@RunWith(SpringRunner.class)
@SpringBootTest
public class CartControllerTest {

    private CartController cartController;

    private UserRepository userRepository = mock(UserRepository.class);
    private CartRepository cartRepository = mock(CartRepository.class);
    private ItemRepository itemRepository = mock(ItemRepository.class);

    @Before
    public void setUp() {
        cartController = new CartController();
        TestUtils.injectObjects(cartController, "userRepository", userRepository);
        TestUtils.injectObjects(cartController, "cartRepository", cartRepository);
        TestUtils.injectObjects(cartController, "itemRepository", itemRepository);
    }

    @Test
    public void addToCartHappyPath() {
        User user = new User();
        user.setUsername("testUser");
        Cart cart = new Cart();
        user.setCart(cart);

        Item item = new Item();
        item.setId(1L);
        item.setName("Item1");
        item.setPrice(BigDecimal.valueOf(10.00));

        ModifyCartRequest request = new ModifyCartRequest();
        request.setUsername("testUser");
        request.setItemId(1L);
        request.setQuantity(2);

        when(userRepository.findByUsername("testUser")).thenReturn(user);
        when(itemRepository.findById(1L)).thenReturn(Optional.of(item));

        ResponseEntity<Cart> response = cartController.addTocart(request);
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());

        Cart responseCart = response.getBody();
        assertNotNull(responseCart);
        assertEquals(2, responseCart.getItems().size());
        assertEquals(BigDecimal.valueOf(20.00), responseCart.getTotal());
    }

    @Test
    public void addToCartUserNotFound() {
        ModifyCartRequest request = new ModifyCartRequest();
        request.setUsername("nonExistentUser");
        request.setItemId(1L);
        request.setQuantity(1);

        when(userRepository.findByUsername("nonExistentUser")).thenReturn(null);

        ResponseEntity<Cart> response = cartController.addTocart(request);
        assertNotNull(response);
        assertEquals(404, response.getStatusCodeValue());
    }

    @Test
    public void addToCartItemNotFound() {
        User user = new User();
        user.setUsername("testUser");
        Cart cart = new Cart();
        user.setCart(cart);

        ModifyCartRequest request = new ModifyCartRequest();
        request.setUsername("testUser");
        request.setItemId(1L);
        request.setQuantity(1);

        when(userRepository.findByUsername("testUser")).thenReturn(user);
        when(itemRepository.findById(1L)).thenReturn(Optional.empty());

        ResponseEntity<Cart> response = cartController.addTocart(request);
        assertNotNull(response);
        assertEquals(404, response.getStatusCodeValue());
    }

    @Test
    public void removeFromCartHappyPath() {
        User user = new User();
        user.setUsername("testUser");
        Cart cart = new Cart();
        Item item = new Item();
        item.setId(1L);
        item.setName("Item1");
        item.setPrice(BigDecimal.valueOf(10.00));

        cart.addItem(item);
        cart.addItem(item);
        user.setCart(cart);

        ModifyCartRequest request = new ModifyCartRequest();
        request.setUsername("testUser");
        request.setItemId(1L);
        request.setQuantity(1);

        when(userRepository.findByUsername("testUser")).thenReturn(user);
        when(itemRepository.findById(1L)).thenReturn(Optional.of(item));

        ResponseEntity<Cart> response = cartController.removeFromcart(request);
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());

        Cart responseCart = response.getBody();
        assertNotNull(responseCart);
        assertEquals(1, responseCart.getItems().size());
        assertEquals(BigDecimal.valueOf(10.00), responseCart.getTotal());
    }

    @Test
    public void removeFromCartUserNotFound() {
        ModifyCartRequest request = new ModifyCartRequest();
        request.setUsername("nonExistentUser");
        request.setItemId(1L);
        request.setQuantity(1);

        when(userRepository.findByUsername("nonExistentUser")).thenReturn(null);

        ResponseEntity<Cart> response = cartController.removeFromcart(request);
        assertNotNull(response);
        assertEquals(404, response.getStatusCodeValue());
    }

    @Test
    public void removeFromCartItemNotFound() {
        User user = new User();
        user.setUsername("testUser");
        Cart cart = new Cart();
        user.setCart(cart);

        Item item = new Item();
        item.setId(1L);
        item.setName("Item1");
        item.setPrice(BigDecimal.valueOf(10.00));

        cart.addItem(item);
        cart.addItem(item);

        ModifyCartRequest request = new ModifyCartRequest();
        request.setUsername("testUser");
        request.setItemId(1L);
        request.setQuantity(1);

        when(userRepository.findByUsername("testUser")).thenReturn(user);
        when(itemRepository.findById(1L)).thenReturn(Optional.empty());

        ResponseEntity<Cart> response = cartController.removeFromcart(request);
        assertNotNull(response);
        assertEquals(404, response.getStatusCodeValue());
    }
}