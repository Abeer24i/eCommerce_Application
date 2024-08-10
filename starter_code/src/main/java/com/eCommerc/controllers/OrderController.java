package com.eCommerc.controllers;

import com.eCommerc.logging.CsvLogger;
import com.eCommerc.model.persistence.User;
import com.eCommerc.model.persistence.UserOrder;
import com.eCommerc.model.persistence.repositories.OrderRepository;
import com.eCommerc.model.persistence.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@RestController
@RequestMapping("/api/order")
public class OrderController {

	@Autowired
	private CsvLogger csvLogger;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private OrderRepository orderRepository;


	@PostMapping("/submit/{username}")
	public ResponseEntity<UserOrder> submit(@PathVariable String username) {
		User user = userRepository.findByUsername(username);

		if(user == null) {
			csvLogger.logToCsv(null,"submitOrder", "Order", null, "Could not find user with name " + user.getUsername() , "NotFound");

			return ResponseEntity.notFound().build();
		}

		UserOrder order = UserOrder.createFromCart(user.getCart());
		orderRepository.save(order);
		csvLogger.logToCsv(user.getId(),"submitOrder", "Order", order.getId(), "Submitted order", "Success");

		return ResponseEntity.ok(order);
	}

	@GetMapping("/history/{username}")
	public ResponseEntity<List<UserOrder>> getOrdersForUser(@PathVariable String username) {
		User user = userRepository.findByUsername(username);
		if(user == null) {
			return ResponseEntity.notFound().build();
		}

		List<UserOrder> userOrders = orderRepository.findByUser(user);

		csvLogger.logToCsv(user.getId(),"getOrdersForUser", "Orders", null, "Successfully fetched orders", "Success");

		return ResponseEntity.ok(userOrders);
	}
}
