package com.example.demo.controllers;

import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.UserOrder;
import com.example.demo.services.OrderService;
import com.example.demo.services.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/order")
@Slf4j
public class OrderController {
	
	
	private final UserService userService;
	
	private final OrderService orderService;

	public OrderController(UserService userService, OrderService orderService) {
		this.userService = userService;

		this.orderService = orderService;
	}


	@PostMapping("/submit/{username}")
	public ResponseEntity<UserOrder> submit(@PathVariable String username) {
		User user = userService.findByUsername(username);
		if(user == null) {
			log.error("error locating {} user for submit order",username);
			return ResponseEntity.notFound().build();
		}
		log.info("success {} user placed an order ",username);
		UserOrder order = UserOrder.createFromCart(user.getCart());
		orderService.save(order);
		return ResponseEntity.ok(order);
	}
	
	@GetMapping("/history/{username}")
	public ResponseEntity<List<UserOrder>> getOrdersForUser(@PathVariable String username) {
		User user = userService.findByUsername(username);
		if(user == null) {
			log.error("could not locate order for user {} ",username);
			return ResponseEntity.notFound().build();
		}
		log.info("success! order for user {} retrieve",username);
		return ResponseEntity.ok(orderService.findByUser(user));
	}
}
