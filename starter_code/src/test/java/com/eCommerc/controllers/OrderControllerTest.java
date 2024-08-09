package com.eCommerc.controllers;

import com.eCommerc.TestUtils;
import com.eCommerc.model.persistence.Cart;
import com.eCommerc.model.persistence.User;
import com.eCommerc.model.persistence.UserOrder;
import com.eCommerc.model.persistence.repositories.OrderRepository;
import com.eCommerc.model.persistence.repositories.UserRepository;
import org.junit.Before;
import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

//@RunWith(SpringRunner.class)
@SpringBootTest
public class OrderControllerTest {

    private OrderController orderController;
    private UserRepository userRepository = mock(UserRepository.class);
    private OrderRepository orderRepository = mock(OrderRepository.class);

    @Before
    public void setUp() {
        orderController = new OrderController();
        TestUtils.injectObjects(orderController, "userRepository", userRepository);
        TestUtils.injectObjects(orderController, "orderRepository", orderRepository);
    }

    @Test
    public void submitOrderHappyPath() {
        User user = new User();
        user.setUsername("testUser");
        Cart cart = new Cart();
        cart.setItems(new ArrayList<>()); // تأكد من عدم كون items null
        user.setCart(cart);

        when(userRepository.findByUsername("testUser")).thenReturn(user);

        UserOrder order = new UserOrder();
        when(orderRepository.save(order)).thenReturn(order);

        ResponseEntity<UserOrder> response = orderController.submit("testUser");
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());

        UserOrder responseOrder = response.getBody();
        assertNotNull(responseOrder);
    }

    @Test
    public void submitOrderUserNotFound() {
        when(userRepository.findByUsername("nonexistentUser")).thenReturn(null);

        ResponseEntity<UserOrder> response = orderController.submit("nonexistentUser");
        assertNotNull(response);
        assertEquals(404, response.getStatusCodeValue());
    }

    @Test
    public void getOrdersForUserHappyPath() {
        User user = new User();
        user.setUsername("testUser");

        UserOrder order1 = new UserOrder();
        UserOrder order2 = new UserOrder();
        List<UserOrder> orders = Arrays.asList(order1, order2);

        when(userRepository.findByUsername("testUser")).thenReturn(user);
        when(orderRepository.findByUser(user)).thenReturn(orders);

        ResponseEntity<List<UserOrder>> response = orderController.getOrdersForUser("testUser");
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());

        List<UserOrder> responseOrders = response.getBody();
        assertNotNull(responseOrders);
        assertEquals(2, responseOrders.size());
    }

    @Test
    public void getOrdersForUserNotFound() {
        when(userRepository.findByUsername("nonexistentUser")).thenReturn(null);

        ResponseEntity<List<UserOrder>> response = orderController.getOrdersForUser("nonexistentUser");
        assertNotNull(response);
        assertEquals(404, response.getStatusCodeValue());
    }
}