package com.eCommerc.controllers;

import com.eCommerc.model.persistence.User;
import com.eCommerc.model.persistence.UserOrder;
import com.eCommerc.model.persistence.repositories.OrderRepository;
import com.eCommerc.model.persistence.repositories.UserRepository;

import java.util.List;

import org.apache.logging.log4j.LogManager;

import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;



@RestController
@RequestMapping("/api/order")
public class OrderController {
	private static final Logger log = LogManager.getLogger(OrderController.class);

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private OrderRepository orderRepository;

	@PostMapping("/submit/{username}")
	public ResponseEntity<UserOrder> submit(@PathVariable String username) {
		log.info("Submitting order for user {}", username);
		User user = userRepository.findByUsername(username);
		if(user == null) {
			log.error("Exception: User {}, does not exist", username);
			return ResponseEntity.notFound().build();
		}
		UserOrder order = UserOrder.createFromCart(user.getCart());
		orderRepository.save(order);
		log.info("Order for user {} submitted successfully.", username);
		return ResponseEntity.ok(order);
	}

	@GetMapping("/history/{username}")
	public ResponseEntity<List<UserOrder>> getOrdersForUser(@PathVariable String username) {
		log.info("Get the order history for user {}", username);
		User user = userRepository.findByUsername(username);
		if(user == null) {
			log.error("Exception: User {}, does not exist", username);
			return ResponseEntity.notFound().build();
		}
		return ResponseEntity.ok(orderRepository.findByUser(user));
	}
}